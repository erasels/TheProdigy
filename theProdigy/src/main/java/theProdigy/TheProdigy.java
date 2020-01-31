package theProdigy;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theProdigy.cards.variables.MagicNumber2;
import theProdigy.cards.variables.ShowNumber;
import theProdigy.characters.ProdigyCharacter;
import theProdigy.relics.special.MagicalPendant;
import theProdigy.relics.special.PurificationRod;
import theProdigy.util.ManaHelper;
import theProdigy.util.TextureLoader;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class TheProdigy implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        PreStartGameSubscriber,
        PostBattleSubscriber {
    public static final Logger logger = LogManager.getLogger(TheProdigy.class.getName());
    private static String modID;

    public static Properties theProdigySettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true;

    private static final String MODNAME = "The Prodigy";
    private static final String AUTHOR = "erasels";
    private static final String DESCRIPTION = "TODO"; //TODO: Write character Description

    public static final Color PRODIGY_PURPLE = CardHelper.getColor(60, 36, 60);

    private static final String ATTACK_PRODIGY_PURPLE = "theProdigyResources/images/512/bg_attack_immortal_red.png";
    private static final String SKILL_PRODIGY_PURPLE = "theProdigyResources/images/512/bg_skill_immortal_red.png";
    private static final String POWER_PRODIGY_PURPLE = "theProdigyResources/images/512/bg_power_immortal_red.png";

    private static final String ENERGY_ORB_PRODIGY_PURPLE = "theProdigyResources/images/512/card_immortal_red_orb.png";
    private static final String CARD_ENERGY_ORB = "theProdigyResources/images/512/card_small_orb.png";

    private static final String ATTACK_PRODIGY_PURPLE_PORTRAIT = "theProdigyResources/images/1024/bg_attack_immortal_red.png";
    private static final String SKILL_PRODIGY_PURPLE_PORTRAIT = "theProdigyResources/images/1024/bg_skill_immortal_red.png";
    private static final String POWER_PRODIGY_PURPLE_PORTRAIT = "theProdigyResources/images/1024/bg_power_immortal_red.png";
    private static final String ENERGY_ORB_PRODIGY_PURPLE_PORTRAIT = "theProdigyResources/images/1024/card_immortal_red_orb.png";

    private static final String THE_PRODIGY_BUTTON = "theProdigyResources/images/charSelect/CharacterButton.png";
    private static final String THE_PRODIGY_PORTRAIT = "theProdigyResources/images/charSelect/CharacterPortraitBG.png";
    public static final String THE_PRODIGY_SHOULDER_1 = "theProdigyResources/images/char/shoulder.png";
    public static final String THE_PRODIGY_SHOULDER_2 = "theProdigyResources/images/char/shoulder2.png";
    public static final String THE_PRODIGY_CORPSE = "theProdigyResources/images/char/corpse.png";

    public static final String BADGE_IMAGE = "theProdigyResources/images/Badge.png";

    public TheProdigy() {
        BaseMod.subscribe(this);

        setModID("theProdigy");

        logger.info("Creating the color " + ProdigyCharacter.Enums.COLOR_PRODIGY.toString());

        BaseMod.addColor(ProdigyCharacter.Enums.COLOR_PRODIGY, PRODIGY_PURPLE, PRODIGY_PURPLE, PRODIGY_PURPLE,
                PRODIGY_PURPLE, PRODIGY_PURPLE, PRODIGY_PURPLE, PRODIGY_PURPLE,
                ATTACK_PRODIGY_PURPLE, SKILL_PRODIGY_PURPLE, POWER_PRODIGY_PURPLE, ENERGY_ORB_PRODIGY_PURPLE,
                ATTACK_PRODIGY_PURPLE_PORTRAIT, SKILL_PRODIGY_PURPLE_PORTRAIT, POWER_PRODIGY_PURPLE_PORTRAIT,
                ENERGY_ORB_PRODIGY_PURPLE_PORTRAIT, CARD_ENERGY_ORB);

        theProdigySettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE");
        try {
            SpireConfig config = new SpireConfig("theProdigy", "theProdigyConfig", theProdigySettings);
            config.load();
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        TheProdigy immortalMod = new TheProdigy();
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + ProdigyCharacter.Enums.THE_PRODIGY.toString());
        BaseMod.addCharacter(new ProdigyCharacter("the Prodigy", ProdigyCharacter.Enums.THE_PRODIGY), THE_PRODIGY_BUTTON, THE_PRODIGY_PORTRAIT, ProdigyCharacter.Enums.THE_PRODIGY);
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        ModPanel settingsPanel = new ModPanel();

        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, enablePlaceholder, settingsPanel, (label) -> {
        }, (button) -> {
            enablePlaceholder = button.enabled;
            try {
                SpireConfig config = new SpireConfig("theProdigy", "theProdigyConfig", theProdigySettings);
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        settingsPanel.addUIElement(enableNormalsButton);

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        ManaHelper.Mana_Strings = CardCrawlGame.languagePack.getUIString(makeID("Mana")).TEXT;
    }

    //Checks to make sure player is playing this character before running animations
    public static void runAnimation(String anim) {
        if (AbstractDungeon.player.chosenClass == ProdigyCharacter.Enums.THE_PRODIGY) {
            ((ProdigyCharacter) AbstractDungeon.player).runAnim(anim);
        }
    }

    @Override
    public void receivePostBattle(AbstractRoom room) {
        if (MathUtils.randomBoolean()) {
            runAnimation("winA");
        } else {
            runAnimation("winB");
        }
    }

    @Override
    public void receivePreStartGame() {
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new PurificationRod(), ProdigyCharacter.Enums.COLOR_PRODIGY);
        BaseMod.addRelicToCustomPool(new MagicalPendant(), ProdigyCharacter.Enums.COLOR_PRODIGY);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new MagicNumber2());
        BaseMod.addDynamicVariable(new ShowNumber());

        try {
            AutoLoader.addCards();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(CardStrings.class, getModID() + "Resources/localization/eng/cardStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, getModID() + "Resources/localization/eng/powerStrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, getModID() + "Resources/localization/eng/relicStrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, getModID() + "Resources/localization/eng/eventStrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, getModID() + "Resources/localization/eng/potionStrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, getModID() + "Resources/localization/eng/characterStrings.json");
        BaseMod.loadCustomStringsFile(OrbStrings.class, getModID() + "Resources/localization/eng/orbStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, getModID() + "Resources/localization/eng/uiStrings.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, getModID() + "Resources/localization/eng/monsterStrings.json");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/keywordStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    public static String makePath(String resourcePath) {
        return getModID() + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return getModID() + "Resources/images/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "Resources/images/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    public static String makeUIPath(String resourcePath) {
        return getModID() + "Resources/images/ui/" + resourcePath;
    }

    public static String makeCharPath(String resourcePath) {
        return getModID() + "Resources/images/char/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }


    public static void setModID(String ID) {
        modID = ID;
    }

    public static String getModID() { // NO
        return modID;
    }
}
