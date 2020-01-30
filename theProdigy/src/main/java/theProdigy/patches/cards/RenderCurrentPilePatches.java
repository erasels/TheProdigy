package theProdigy.patches.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import javassist.CtBehavior;
import theProdigy.util.UC;

public class RenderCurrentPilePatches {
    private static final float SUB_X = 17f;
    private static final float SUB_Y = 64f;

    public enum pileRender {
        DRAW, DISCARD, EXHAUST, HAND, UNSPECIFIED
    }

    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class CurrentPileField {
        public static SpireField<pileRender> pileEnum = new SpireField<>(() -> pileRender.UNSPECIFIED);

        public static void set(Object __intance, CardGroup value) {
            String hack = "";
            if (value == UC.p().drawPile) {
                hack = "draw";
            } else if (value == UC.p().discardPile) {
                hack = "discard";
            } else if (value == UC.p().exhaustPile) {
                hack = "exhaust";
            } else if (value == UC.p().hand) {
                hack = "hand";
            }
            pileEnum.set(__intance, getRenderEnum(hack));
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "render", paramtypez = {SpriteBatch.class, boolean.class})
    public static class RenderPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractCard __insatance, SpriteBatch sb, boolean selected) {
            pileRender tex = CurrentPileField.pileEnum.get(__insatance);
            if (tex != pileRender.UNSPECIFIED) {
                Texture tmp = getTextureFromEnum(tex);
                float x = __insatance.current_x - (__insatance.hb.height / 2f);
                float y = __insatance.current_y - (__insatance.hb.width / 2f);
                sb.draw(tmp, x - (SUB_X * Settings.scale), y - (SUB_Y * Settings.scale), ((tmp.getWidth() * __insatance.drawScale) * Settings.scale), ((tmp.getHeight() * __insatance.drawScale) * Settings.scale));
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "render");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    public static Texture getTextureFromEnum(pileRender tex) {
        if (tex == pileRender.DRAW) {
            return ImageMaster.DECK_BTN_BASE;
        } else if (tex == pileRender.DISCARD) {
            return ImageMaster.DISCARD_BTN_BASE;
        } else if (tex == pileRender.EXHAUST) {
            return ImageMaster.CARD_BACK.getTexture();
        } else if (tex == pileRender.HAND) {
            return ImageMaster.CARD_BACK.getTexture();
        } else {
            return ImageMaster.CHECKBOX;
        }
    }

    public static pileRender getRenderEnum(String cardGroup) {
        switch (cardGroup.toLowerCase()) {
            case "draw":
                return pileRender.DRAW;
            case "discard":
                return pileRender.DISCARD;
            case "exhaust":
                return pileRender.EXHAUST;
            case "hand":
                return pileRender.HAND;
            default:
                return pileRender.UNSPECIFIED;
        }
    }
}
