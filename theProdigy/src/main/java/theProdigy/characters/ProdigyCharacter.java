package theProdigy.characters;

import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.PrismaticShard;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theProdigy.TheProdigy;
import theProdigy.animation.BetterSpriterAnimation;
import theProdigy.animation.PlayerListener;
import theProdigy.cards.basic.Defend;
import theProdigy.cards.basic.Strike;
import theProdigy.cards.basic.Teleport;
import theProdigy.cards.basic.UnrulySpark;
import theProdigy.patches.mechanics.ManaPatches;
import theProdigy.relics.special.MagicalPendant;
import theProdigy.ui.MyEnergyOrb;

import java.util.ArrayList;

import static theProdigy.TheProdigy.*;

public class ProdigyCharacter extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(TheProdigy.class.getName());

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_PRODIGY;
        @SpireEnum(name = "PRODIGY_PURPLE_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor COLOR_PRODIGY;
        @SpireEnum(name = "PRODIGY_PURPLE_COLOR")
        @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    private int prevSpeed = 0;

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 68;
    public static final int MAX_HP = 68;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;
    public static final int MANA = 30;

    public static final float SIZE_MULTI = 1.2f;

    private static final String ID = makeID("ProdigyCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    public ProdigyCharacter(String name, PlayerClass setClass) {
        super(name, setClass, new MyEnergyOrb(), new BetterSpriterAnimation(
                "theProdigyResources/images/char/Spriter/Sacred.scml"));

        ManaPatches.ManaField.maxMP.set(this, MANA);
        initializeClass(null, // required call to load textures and setup energy/loadout.
                THE_PRODIGY_SHOULDER_1,
                THE_PRODIGY_SHOULDER_2,
                THE_PRODIGY_CORPSE,
                getLoadout(), 0.0F, 0.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN));

        PlayerListener listener = new PlayerListener(this);
        ((BetterSpriterAnimation)this.animation).myPlayer.addListener(listener);

        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 220.0F * Settings.scale);
    }

    public String getPortraitImageName() {
        return BaseMod.getPlayerPortrait(chosenClass);
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0], STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public void damage(DamageInfo info) {
        int thisHealth = this.currentHealth;
        super.damage(info);
        int trueDamage = thisHealth - this.currentHealth;
        if (info.owner instanceof AbstractMonster) {
            if (this.isDead) {
                playDeathAnimation();
            } else {
                if(trueDamage > 15) {
                    TheProdigy.runAnimation("hitHigh");
                } else if (trueDamage > 0) {
                    TheProdigy.runAnimation("hitLow");
                } else {
                    if(info.output > 20) {
                        TheProdigy.runAnimation("guardB");
                    } else {
                        TheProdigy.runAnimation("guardA");
                    }
                }
            }
        }
    }

    //Stops the corpse img from overwriting the SpriterAnimation
    @Override
    public void playDeathAnimation() {
        TheProdigy.runAnimation("downed");
    }

    //Runs a specific animation
    public void runAnim(String animation) {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation(animation);
    }

    //Resets character back to idle animation
    public void resetAnimation() {
        ((BetterSpriterAnimation)this.animation).myPlayer.setAnimation("idle");
    }

    //Prevents any further animation once the death animation is finished
    public void stopAnimation() {
        int time = ((BetterSpriterAnimation)this.animation).myPlayer.getAnimation().length;
        ((BetterSpriterAnimation)this.animation).myPlayer.setTime(time);
        prevSpeed = ((BetterSpriterAnimation)this.animation).myPlayer.speed;
        ((BetterSpriterAnimation)this.animation).myPlayer.speed = 0;
    }

    @Override
    public void preBattlePrep() {
        super.preBattlePrep();
        if(prevSpeed != 0) {
            ((BetterSpriterAnimation)this.animation).myPlayer.speed = prevSpeed;
        }
        resetAnimation();
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for(int i = 0; i<4;i++) {
            retVal.add(Strike.ID);
        }
        for(int i = 0; i<4;i++) {
            retVal.add(Defend.ID);
        }
        retVal.add(Teleport.ID);
        retVal.add(UnrulySpark.ID);
        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(PrismaticShard.ID);
        retVal.add(MagicalPendant.ID);
        UnlockTracker.markRelicAsSeen(MagicalPendant.ID);
        return retVal;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_WHIRLWIND", 1.75f);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.LONG, true);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_WHIFF_1";
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.COLOR_PRODIGY;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return PRODIGY_PURPLE;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    //TODO: Add other starter card
    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new ProdigyCharacter(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return PRODIGY_PURPLE;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return PRODIGY_PURPLE;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL,
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL,
                AbstractGameAction.AttackEffect.SLASH_VERTICAL,
                AbstractGameAction.AttackEffect.FIRE,};

    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    //TODO: Write better vampire text
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

}
