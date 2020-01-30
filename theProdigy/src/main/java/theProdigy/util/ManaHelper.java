package theProdigy.util;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import theProdigy.TheProdigy;
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
        return getMaxMP() > -1;
    }

    public static String getTitle() { return Mana_Strings[0]; }
    public static String getDesc() { return Mana_Strings[1]; }
    public static String getRestText() { return Mana_Strings[2]; }
}
