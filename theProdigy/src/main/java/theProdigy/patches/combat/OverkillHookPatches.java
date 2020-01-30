package theProdigy.patches.combat;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.StrikeEffect;
import javassist.CtBehavior;

public class OverkillHookPatches {
    @SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
    public static class OverkillDamage {
        public static SpireField<Integer> ovkDmg = new SpireField<>(() -> -1);
        public static SpireField<AbstractGameAction> callbackAction = new SpireField<>(() -> null);

        public static void set(DamageInfo info, AbstractGameAction action) {
            callbackAction.set(info, action);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class OverkillHook {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractMonster __instance, DamageInfo info) {
            if(OverkillDamage.callbackAction.get(info) != null && __instance.currentHealth < 0) {
                OverkillDamage.ovkDmg.set(info, -__instance.currentHealth);
                OverkillDamage.callbackAction.get(info).amount = -__instance.currentHealth;
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.NewExprMatcher(StrikeEffect.class);
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}
