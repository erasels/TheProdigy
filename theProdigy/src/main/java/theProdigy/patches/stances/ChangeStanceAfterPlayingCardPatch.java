package theProdigy.patches.stances;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import theProdigy.cards.abstracts.ProdigyCard;
import theProdigy.stances.DimensionalStance;
import theProdigy.stances.ElementalStance;
import theProdigy.stances.OccultStance;
import theProdigy.stances.ProdigyStance;
import theProdigy.util.UC;

@SpirePatch(clz = AbstractPlayer.class, method = "useCard")
public class ChangeStanceAfterPlayingCardPatch {
    @SpirePostfixPatch
    public static void patch(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
        if(c instanceof ProdigyCard && ((ProdigyCard) c).stance != ProdigyCard.AlignedStance.NONE && !checkStanceEquality(((ProdigyCard) c).stance)) {
            UC.changeStance(getStance(((ProdigyCard) c).stance), false);
        }
    }

    private static boolean checkStanceEquality(ProdigyCard.AlignedStance stance) {
        AbstractStance tmp = UC.p().stance;
        return (stance == ProdigyCard.AlignedStance.ELEMENTAL && tmp instanceof ElementalStance) ||
                (stance == ProdigyCard.AlignedStance.OCCULT && tmp instanceof OccultStance) ||
                (stance == ProdigyCard.AlignedStance.DIMENSIONAL && tmp instanceof DimensionalStance);
    }

    private static ProdigyStance getStance(ProdigyCard.AlignedStance stance) {
        switch (stance) {
            case DIMENSIONAL:
                return new DimensionalStance();
            case OCCULT:
                return new OccultStance();
            default:
                return new ElementalStance();
        }
    }
}
