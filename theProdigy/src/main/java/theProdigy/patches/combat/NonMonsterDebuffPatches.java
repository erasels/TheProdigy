package theProdigy.patches.combat;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;
import theProdigy.characters.ProdigyCharacter;

public class NonMonsterDebuffPatches {
    //Make Frail and Dex work on monsters
    @SpirePatch(clz = AbstractCreature.class, method = "addBlock")
    public static class MakeBlockModifiersWork {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractCreature __instance, @ByRef int[] blockAmount) {
            //To make sure it doesn't interfere with other mods
            if(AbstractDungeon.player.chosenClass == ProdigyCharacter.Enums.THE_PRODIGY) {
                if (__instance instanceof AbstractMonster) {
                    float tmp = blockAmount[0];
                    for (AbstractPower p : __instance.powers) {
                        tmp = p.modifyBlock(tmp);
                    }
                    blockAmount[0] = MathUtils.floor(tmp);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "currentBlock");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }


}
