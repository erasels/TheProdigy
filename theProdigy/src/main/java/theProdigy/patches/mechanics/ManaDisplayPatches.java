package theProdigy.patches.mechanics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import com.megacrit.cardcrawl.vfx.combat.HealPanelEffect;
import com.megacrit.cardcrawl.vfx.combat.PingHpEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import theProdigy.util.ManaHelper;

import java.util.ArrayList;

public class ManaDisplayPatches {
    @SpirePatch(clz = TopPanel.class, method = "renderHP")
    public static class ManaRender {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(SpriteBatch.class.getName()) && m.getMethodName().equals("draw")) {
                        m.replace("{" +
                                "if(" + ManaHelper.class.getName() + ".hasMana()) {" +
                                "$proceed($1, $2, ICON_Y + 16f *" + Settings.class.getName() + ".scale, $4, $5, $6, $7, $8 *0.75f, $9*0.75f, $10, $11, $12, $13, $14, $15, $16);" +
                                "sb.draw(" + ManaHelper.class.getName() + ".MANA_ICON, hpIconX - 32.0F + 32.0F * " + Settings.class.getName() + ".scale, ICON_Y - 48.0F + 32.0F * " + Settings.class.getName() + ".scale, 32.0F, 32.0F, 64.0F, 64.0F, " + Settings.class.getName() + ".scale*0.75f, " + Settings.class.getName() + ".scale *0.75f, 0.0F, 0, 0, 64, 64, false, false);" +
                                "} else {" +
                                "$proceed($$);" +
                                "}" +
                                "}"
                        );
                    } else if (m.getMethodName().equals("renderFontLeftTopAligned")) {
                        m.replace("{" +
                                "if(" + ManaHelper.class.getName() + ".hasMana()) {" +
                                "$proceed($1, $2, $3, $4, INFO_TEXT_Y + 18f, $6);" +
                                FontHelper.class.getName() + ".renderFontLeftTopAligned(sb, " + FontHelper.class.getName() + ".topPanelInfoFont, " + ManaHelper.class.getName() + ".getMP() + \"/\" + " + ManaHelper.class.getName() + ".getMaxMP(), hpIconX + HP_NUM_OFFSET_X, INFO_TEXT_Y - 14f, " + Color.class.getName() + ".SKY);" +
                                "} else {" +
                                "$proceed($$);" +
                                "}" +
                                "}"
                        );
                    }
                }
            };
        }

        @SpirePostfixPatch
        public static void patch(TopPanel __instance, SpriteBatch sb) {
            if (ManaHelper.hasMana()) {
                ManaHitbox.hb.get(__instance).render(sb);
            }
        }
    }

    @SpirePatch(clz = TopPanel.class, method = SpirePatch.CLASS)
    public static class ManaHitbox {
        public static SpireField<Hitbox> hb = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = TopPanel.class, method = "adjustHitboxes")
    public static class ManaAdjustHitbox {
        private static boolean firstMethod = true;
        private static boolean firstNew = true;

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("move") && firstMethod) {
                        firstMethod = false;
                        m.replace("{" +
                                "if(" + ManaHelper.class.getName() + ".hasMana()) {" +
                                "$proceed($1, $2 + 20f * " + Settings.class.getName() + ".scale);" +
                                ManaAdjustHitbox.class.getName() + ".moveManaHB($1, $2);" +
                                "} else {" +
                                "$proceed($$);" +
                                "}" +
                                "}"
                        );
                    }
                }

                @Override
                public void edit(NewExpr n) throws CannotCompileException {
                    if (firstNew) {
                        firstNew = false;
                        n.replace("{" +
                                "if(" + ManaHelper.class.getName() + ".hasMana()) {" +
                                "$_ = $proceed($1, $2 - 32f * " + Settings.class.getName() + ".scale);" +
                                ManaAdjustHitbox.class.getName() + ".createManaHB($1, $2 - 32f * " + Settings.class.getName() + ".scale);" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}" +
                                "}"
                        );
                    }
                }
            };
        }

        public static void createManaHB(float var1, float var2) {
            ManaHitbox.hb.set(AbstractDungeon.topPanel, new Hitbox(var1, var2));
        }

        public static void moveManaHB(float var1, float var2) {
            ManaHitbox.hb.get(AbstractDungeon.topPanel).move(var1, var2 - 14f * Settings.scale);
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "update")
    public static class ManaHbUpdate {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(TopPanel __instance) {
            if (ManaHelper.hasMana()) {
                ManaHitbox.hb.get(__instance).update();
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(TopPanel.class, "updateAscensionHover");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "updateTips")
    public static class ManaTip {
        private static final float TIP_Y = Settings.HEIGHT - 120.0F * Settings.scale, TIP_OFF_X = 140.0F * Settings.scale;

        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn patch(TopPanel __instance) {
            if (ManaHelper.hasMana() && ManaHitbox.hb.get(__instance).hovered) {
                TipHelper.renderGenericTip(InputHelper.mX - TIP_OFF_X, TIP_Y, ManaHelper.getTitle(), ManaHelper.getDesc());
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(Hitbox.class, "hovered");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }
    }

    @SpirePatch(clz = TopPanel.class, method = "unhoverHitboxes")
    public static class ManaUnhoverHitbox {
        @SpirePostfixPatch
        public static void patch(TopPanel __instance) {
            if (ManaHelper.hasMana()) {
                ManaHitbox.hb.get(__instance).unhover();
            }
        }
    }

    //Heal and Health ping effect fixes
    @SpirePatch(clz = HealPanelEffect.class, method = "render")
    public static class FixHealEffect {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("draw")) {
                        m.replace("{" +
                                "if(" + ManaHelper.class.getName() + ".hasMana()) {" +
                                "$proceed($1, $2, $3+ 16f *" + Settings.class.getName() + ".scale, $4, $5, $6, $7, $8 *0.75f, $9*0.75f, $10, $11, $12, $13, $14, $15, $16);" +
                                "} else {" +
                                "$proceed($$);" +
                                "}" +
                                "}"
                        );
                    }
                }
            };
        }
    }

    @SpirePatch(clz = PingHpEffect.class, method = "render")
    public static class FixHealthPingEffect {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("draw")) {
                        m.replace("{" +
                                "if(" + ManaHelper.class.getName() + ".hasMana()) {" +
                                "$proceed($1, $2, $3+ 16f *" + Settings.class.getName() + ".scale, $4, $5, $6, $7, $8 *0.75f, $9*0.75f, $10, $11, $12, $13, $14, $15, $16);" +
                                "} else {" +
                                "$proceed($$);" +
                                "}" +
                                "}"
                        );
                    }
                }
            };
        }
    }
}


