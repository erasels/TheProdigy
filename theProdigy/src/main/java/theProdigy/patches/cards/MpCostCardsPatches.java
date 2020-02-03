package theProdigy.patches.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CtBehavior;
import theProdigy.actions.common.ChangeManaAction;
import theProdigy.cards.abstracts.ProdigyCard;
import theProdigy.universal.CardMPCostHooks;
import theProdigy.util.ManaHelper;
import theProdigy.util.RenderMPCostHelper;
import theProdigy.util.UC;

import java.lang.reflect.Field;

public class MpCostCardsPatches {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class MPLossFields {
        public static SpireField<Integer> baseMPCost = new SpireField<>(() -> -1);
        public static SpireField<Integer> mpCost = new SpireField<>(() -> -1);
        public static SpireField<Boolean> isMPCostModified = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class CopyShenans {
        @SpirePostfixPatch
        public static AbstractCard patch(AbstractCard __result, AbstractCard __instance) {
            ManaHelper.setBaseMPCost(__result, ManaHelper.getBaseMPCost(__instance));
            ManaHelper.setMPCost(__result, ManaHelper.getMPCost(__instance));
            return __result;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "resetAttributes")
    public static class ResetShenans {
        @SpirePostfixPatch
        public static void patch(AbstractCard __instance) {
            ManaHelper.setMPCost(__instance, ManaHelper.getBaseMPCost(__instance));
            ManaHelper.setMPCostModified(__instance, false);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ApplyPowersShenans {
        @SpirePostfixPatch
        public static void patch(AbstractCard __instance) {
            ManaHelper.setMPCostModified(__instance, false);
            ManaHelper.setMPCost(__instance, ManaHelper.getBaseMPCost(__instance));
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof CardMPCostHooks) {
                    ManaHelper.setMPCost(__instance, ((CardMPCostHooks) p).modifyMPCost(__instance, ManaHelper.getMPCost(__instance)));
                }
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof CardMPCostHooks) {
                    ManaHelper.setMPCost(__instance, ((CardMPCostHooks) r).modifyMPCost(__instance, ManaHelper.getMPCost(__instance)));
                }
            }
            ManaHelper.setMPCostModified(__instance, ManaHelper.getMPCost(__instance) != ManaHelper.getBaseMPCost(__instance));
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class mpCostCapture {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractPlayer __instance, AbstractCard c, AbstractMonster m, int energyOnUse) {
            int mpcost = ManaHelper.getMPCost(c);
            for (AbstractPower p : __instance.powers) {
                if (p instanceof CardMPCostHooks) {
                    mpcost = ((CardMPCostHooks) p).preMPLoss(c, mpcost);
                }
            }
            for (AbstractRelic r : __instance.relics) {
                if (r instanceof CardMPCostHooks) {
                    mpcost = ((CardMPCostHooks) r).preMPLoss(c, mpcost);
                }
            }
            if ((!(c instanceof ProdigyCard) || ((ProdigyCard) c).isEmpowered) && mpcost > 0) {
                UC.att(new ChangeManaAction(-mpcost));

                if (c instanceof ProdigyCard) {
                    ((ProdigyCard) c).empoweredUse(__instance, m);
                }

                for (AbstractPower p : __instance.powers) {
                    if (p instanceof CardMPCostHooks) {
                        ((CardMPCostHooks) p).postMPLoss(c, mpcost);
                    }
                }
                for (AbstractRelic r : __instance.relics) {
                    if (r instanceof CardMPCostHooks) {
                        ((CardMPCostHooks) r).postMPLoss(c, mpcost);
                    }
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.NewExprMatcher(UseCardAction.class);
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }


    @SpirePatch(clz = AbstractCard.class, method = "renderEnergy", paramtypez = {SpriteBatch.class})
    public static class MPCardRenderInLibraryPatch {
        public static void Postfix(AbstractCard __instance, SpriteBatch sb) {
            RenderMPCostHelper.renderMPCost(__instance, sb);
        }
    }

    //TODO: Make seperate render that actually works here
    private static Field cardField;

    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderCost", paramtypez = {SpriteBatch.class})
    public static class MPCardRenderInSingleCardPatch {
        public static void Postfix(SingleCardViewPopup __instance, SpriteBatch sb) {
            try {
                if (cardField == null) {
                    cardField = __instance.getClass().getDeclaredField("card");
                    cardField.setAccessible(true);
                }

                AbstractCard c = (AbstractCard) cardField.get(__instance);
                if (ManaHelper.hasMana()) {
                    RenderMPCostHelper.renderMPCostInSingle(c, sb, __instance);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

