package theProdigy.util;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import theProdigy.TheProdigy;
import theProdigy.patches.cards.MpCostCardsPatches;
import theProdigy.patches.mechanics.ManaPatches;
import theProdigy.vfx.general.IncreaseManaPanelEffect;

public class ManaHelper {
    public static final Texture MANA_ICON = TextureLoader.getTexture(TheProdigy.makeUIPath("mana_small.png"));
    public static String[] Mana_Strings;

    public static int getMP() {
        return ManaPatches.ManaField.mp.get(AbstractDungeon.player);
    }

    public static void setMP(int i) {
        int maxmp = getMaxMP();
        if (i > maxmp) {
            i = maxmp;
        } else if (i < 0) {
            i = 0;
        }
        ManaPatches.ManaField.mp.set(AbstractDungeon.player, i);
    }

    public static void addMP(int i) {
        setMP(getMP() + i);
        AbstractDungeon.topLevelEffectsQueue.add(new IncreaseManaPanelEffect((Float) ReflectionHacks.getPrivate(AbstractDungeon.topPanel, TopPanel.class, "hpIconX")));
    }

    public static void loseMP(int i) {
        if (i < 0) {
            i *= -1;
        }
        setMP(getMP() - i);
    }

    public static int getMaxMP() {
        return ManaPatches.ManaField.maxMP.get(AbstractDungeon.player);
    }

    public static boolean hasMana() {
        return UC.p() != null && getMaxMP() > -1;
    }

    public static boolean hasEnoughMana(int i) {
        return hasMana() && i <= getMP();
    }

    //Cards
    public static int getMPCost(AbstractCard c) {
        return MpCostCardsPatches.MPLossFields.mpCost.get(c);
    }

    public static int getBaseMPCost(AbstractCard c) {
        return MpCostCardsPatches.MPLossFields.baseMPCost.get(c);
    }

    public static boolean getMPCostModified(AbstractCard c) {
        return MpCostCardsPatches.MPLossFields.isMPCostModified.get(c);
    }

    public static void setMPCost(AbstractCard c, int amount) {
        MpCostCardsPatches.MPLossFields.mpCost.set(c, amount);
    }

    public static void setBaseMPCost(AbstractCard c, int amount) {
        MpCostCardsPatches.MPLossFields.baseMPCost.set(c, amount);
    }

    public static void setMPCostModified(AbstractCard c, boolean state) {
        MpCostCardsPatches.MPLossFields.isMPCostModified.set(c, state);
    }

    //Strings
    public static String getTitle() { return Mana_Strings[0]; }
    public static String getDesc() { return Mana_Strings[1]; }
    public static String getRestText() { return Mana_Strings[2]; }
}
