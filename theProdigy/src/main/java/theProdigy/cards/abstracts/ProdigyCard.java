package theProdigy.cards.abstracts;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theProdigy.TheProdigy;
import theProdigy.characters.ProdigyCharacter;
import theProdigy.patches.cards.CardENUMs;
import theProdigy.util.CardInfo;
import theProdigy.util.ManaHelper;
import theProdigy.util.TextureLoader;
import theProdigy.util.UC;

import java.lang.reflect.Field;

import static theProdigy.TheProdigy.makeID;

public abstract class ProdigyCard extends CustomCard {
    protected static final Color EMPOWER_BORDER_GLOW_COLOR = Color.VIOLET.cpy();

    protected CardStrings cardStrings;
    protected String img;

    protected boolean upgradesDescription;

    protected int baseCost;

    protected boolean upgradeCost;
    protected boolean upgradeDamage;
    protected boolean upgradeBlock;
    protected boolean upgradeMagic;

    protected int costUpgrade;
    protected int damageUpgrade;
    protected int blockUpgrade;
    protected int magicUpgrade;

    protected boolean upgradedMPCost;
    protected boolean upgradeMPCost;
    protected int mpCostUpgrade;

    protected boolean baseExhaust;
    protected boolean upgExhaust;
    protected boolean baseInnate;
    protected boolean upgInnate;

    protected boolean upgradeBurst;
    protected boolean upgradeRetain;
    protected boolean upgradeEthereal;
    protected boolean upgradeMultiDmg;

    public int baseMagicNumber2;
    public int magicNumber2;
    public boolean isMagicNumber2Modified;

    public int baseShowNumber;
    public int showNumber;
    public boolean isShowNumberModified;

    public boolean isEmpowered;

    public ProdigyCard(CardInfo cardInfo, boolean upgradesDescription) {
        this(ProdigyCharacter.Enums.COLOR_PRODIGY, cardInfo.cardName, cardInfo.cardCost, cardInfo.cardType, cardInfo.cardTarget, cardInfo.cardRarity, upgradesDescription);
    }

    public ProdigyCard(CardColor color, String cardName, int cost, CardType cardType, CardTarget target, CardRarity rarity, boolean upgradesDescription) {
        super(makeID(cardName), "", (String) null, cost, "", cardType, color, rarity, target);

        cardStrings = CardCrawlGame.languagePack.getCardStrings(cardID);

        img = TextureLoader.getAndLoadCardTextureString(cardName, cardType);
        this.textureImg = img;
        loadCardImage(textureImg);

        this.rarity = autoRarity();

        this.rawDescription = cardStrings.DESCRIPTION;
        this.originalName = cardStrings.NAME;
        this.name = originalName;

        this.baseCost = cost;

        this.upgradesDescription = upgradesDescription;

        this.upgradedMPCost = false;

        this.upgradeCost = false;
        this.upgradeDamage = false;
        this.upgradeBlock = false;
        this.upgradeMagic = false;
        this.upgradeMPCost = false;

        this.costUpgrade = cost;
        this.damageUpgrade = 0;
        this.blockUpgrade = 0;
        this.magicUpgrade = 0;
        this.mpCostUpgrade = 0;

        upgradeBurst = false;
        upgradeRetain = false;
        upgradeEthereal = false;

        isEmpowered = false;

        if (cardName.toLowerCase().contains("strike")) {
            tags.add(CardTags.STRIKE);
        }

        InitializeCard();
    }

    //Methods meant for constructor use
    public void setDamage(int damage) {
        this.setDamage(damage, 0);
    }

    public void setBlock(int block) {
        this.setBlock(block, 0);
    }

    public void setMagic(int magic) {
        this.setMagic(magic, 0);
    }

    public void setMPCost(int mpCost) {
        this.setMPCost(mpCost, 0);
    }

    public void setCostUpgrade(int costUpgrade) {
        this.costUpgrade = costUpgrade;
        this.upgradeCost = true;
    }

    public void setExhaust(boolean exhaust) {
        this.setExhaust(exhaust, exhaust);
    }

    public void setDamage(int damage, int damageUpgrade) {
        this.baseDamage = this.damage = damage;
        if (damageUpgrade != 0) {
            this.upgradeDamage = true;
            this.damageUpgrade = damageUpgrade;
        }
    }

    public void setBlock(int block, int blockUpgrade) {
        this.baseBlock = this.block = block;
        if (blockUpgrade != 0) {
            this.upgradeBlock = true;
            this.blockUpgrade = blockUpgrade;
        }
    }

    public void setMagic(int magic, int magicUpgrade) {
        this.baseMagicNumber = this.magicNumber = magic;
        if (magicUpgrade != 0) {
            this.upgradeMagic = true;
            this.magicUpgrade = magicUpgrade;
        }
    }

    public void setMPCost(int mpCost, int mpCostUpgrade) {
        ManaHelper.setBaseMPCost(this, mpCost);
        ManaHelper.setMPCost(this, mpCost);
        if (mpCostUpgrade != 0) {
            this.upgradeMPCost = true;
            this.mpCostUpgrade = mpCostUpgrade;
        }
    }

    public void setExhaust(boolean baseExhaust, boolean upgExhaust) {
        this.baseExhaust = baseExhaust;
        this.upgExhaust = upgExhaust;
        this.exhaust = baseExhaust;
    }

    public void setInnate(boolean baseInnate, boolean upgInnate) {
        this.baseInnate = baseInnate;
        this.isInnate = baseInnate;
        this.upgInnate = upgInnate;
    }

    public void setBurst(boolean upgradeToBurst) {
        if (upgradeToBurst) {
            upgradeBurst = true;
        } else {
            tags.add(CardENUMs.BURST);
        }
    }

    public void setRetain(boolean upgradeToRetain) {
        if (upgradeToRetain) {
            upgradeRetain = true;
        } else {
            selfRetain = true;
        }
    }

    public void setEthereal(boolean upgradeToEthereal) {
        if (upgradeToEthereal) {
            upgradeEthereal = true;
        } else {
            isEthereal = true;
        }
    }

    public void setMultiDamage(boolean upgradeMulti) {
        if (upgradeMulti) {
            upgradeMultiDmg = true;
        } else {
            this.isMultiDamage = true;
        }
    }

    public void setSN(int sn) {
        this.showNumber = baseShowNumber = sn;
    }

    public void setMN2(int mn2) {
        this.magicNumber2 = baseMagicNumber2 = mn2;
    }

    private CardRarity autoRarity() {
        String packageName = this.getClass().getPackage().getName();

        String directParent;
        if (packageName.contains(".")) {
            directParent = packageName.substring(1 + packageName.lastIndexOf("."));
        } else {
            directParent = packageName;
        }
        switch (directParent) {
            case "common":
                return CardRarity.COMMON;
            case "uncommon":
                return CardRarity.UNCOMMON;
            case "rare":
                return CardRarity.RARE;
            case "basic":
                return CardRarity.BASIC;
            default:
                if (Settings.isDebug) {
                    TheProdigy.logger.info("Automatic Card rarity resulted in SPECIAL, input: " + directParent);
                }
                return CardRarity.SPECIAL;
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();

        if (card instanceof ProdigyCard) {
            card.rawDescription = this.rawDescription;
            ((ProdigyCard) card).upgradesDescription = this.upgradesDescription;

            ((ProdigyCard) card).baseCost = this.baseCost;

            ((ProdigyCard) card).upgradeCost = this.upgradeCost;
            ((ProdigyCard) card).upgradeDamage = this.upgradeDamage;
            ((ProdigyCard) card).upgradeBlock = this.upgradeBlock;
            ((ProdigyCard) card).upgradeMagic = this.upgradeMagic;
            ((ProdigyCard) card).upgradeMPCost = this.upgradeMPCost;

            ((ProdigyCard) card).costUpgrade = this.costUpgrade;
            ((ProdigyCard) card).damageUpgrade = this.damageUpgrade;
            ((ProdigyCard) card).blockUpgrade = this.blockUpgrade;
            ((ProdigyCard) card).magicUpgrade = this.magicUpgrade;
            ((ProdigyCard) card).mpCostUpgrade = this.mpCostUpgrade;

            ((ProdigyCard) card).baseExhaust = this.baseExhaust;
            ((ProdigyCard) card).upgExhaust = this.upgExhaust;
            ((ProdigyCard) card).baseInnate = this.baseInnate;
            ((ProdigyCard) card).upgInnate = this.upgInnate;

            ((ProdigyCard) card).upgradeMultiDmg = this.upgradeMultiDmg;
            ((ProdigyCard) card).upgradeRetain = this.upgradeRetain;
            ((ProdigyCard) card).upgradeEthereal = this.upgradeEthereal;
            ((ProdigyCard) card).upgradeBurst = this.upgradeBurst;

            ((ProdigyCard) card).baseMagicNumber2 = this.baseMagicNumber2;
            ((ProdigyCard) card).magicNumber2 = this.magicNumber2;
            ((ProdigyCard) card).baseShowNumber = this.baseShowNumber;
            ((ProdigyCard) card).showNumber = this.showNumber;
        }

        return card;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.upgradeName();

            if (this.upgradesDescription)
                this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;

            if (upgradeCost) {
                int diff = this.baseCost - this.cost; //positive if cost is reduced

                this.upgradeBaseCost(costUpgrade);
                this.cost -= diff;
                this.costForTurn -= diff;
                if (cost < 0)
                    cost = 0;

                if (costForTurn < 0)
                    costForTurn = 0;
            }

            if (upgradeDamage)
                this.upgradeDamage(damageUpgrade);

            if (upgradeBlock)
                this.upgradeBlock(blockUpgrade);

            if (upgradeMagic)
                this.upgradeMagicNumber(magicUpgrade);

            if (upgradeMPCost)
                this.upgradeMPCost(mpCostUpgrade);

            if (baseExhaust ^ upgExhaust) //different
                this.exhaust = upgExhaust;

            if (baseInnate ^ upgInnate) //different
                this.isInnate = upgInnate;

            if (upgradeBurst) {
                tags.add(CardENUMs.BURST);
            }

            if (upgradeRetain) {
                selfRetain = true;
            }

            if (upgradeEthereal) {
                isEthereal = true;
            }

            if (upgradeMultiDmg) {
                this.isMultiDamage = true;
            }

            this.initializeDescription();
        }
    }

    public void empoweredUse(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerOnGlowCheck() {
        if (CardCrawlGame.isInARun()) {
            if ((this.hasTag(CardENUMs.BURST) && UC.anonymousCheckBurst())) {
                glowColor = GOLD_BORDER_GLOW_COLOR;
            } else if (ManaHelper.hasEnoughMana(ManaHelper.getMPCost(this)) && isEmpowered) {
                glowColor = EMPOWER_BORDER_GLOW_COLOR;
            } else {
                glowColor = BLUE_BORDER_GLOW_COLOR;
            }
        }
    }

    public void InitializeCard() {
        FontHelper.cardDescFont_N.getData().setScale(1.0f);
        this.initializeTitle();
        this.initializeDescription();
    }

    @Override
    public void update() {
        clickUpdate();
        super.update();
    }

    public void clickUpdate() {
        if (HitboxRightClick.rightClicked.get(this.hb)) {
            onRightClick();
        }
    }

    public void onRightClick() {
        if (isEmpowered) {
            isEmpowered = false;
        } else {
            if (ManaHelper.hasEnoughMana(ManaHelper.getMPCost(this)) && ManaHelper.getMPCost(this) > -1) {
                isEmpowered = true;
                this.superFlash(Color.ROYAL.cpy());
            }
        }
        UC.p().hand.glowCheck();
    }

    @Override
    public void resetAttributes() {
        super.resetAttributes();
        this.magicNumber2 = baseMagicNumber2;
        this.isMagicNumber2Modified = false;
        this.showNumber = baseShowNumber;
        this.isShowNumberModified = false;
    }

    @Override
    public void applyPowers() {
        this.applyPowersToMN2();
        this.applyPowersToSN();
        super.applyPowers();
    }

    private void applyPowersToMN2() {
        this.isMagicNumber2Modified = magicNumber2 != baseMagicNumber2;
    }

    private void applyPowersToSN() {
        this.isShowNumberModified = showNumber != baseShowNumber;
    }

    @Override
    public void displayUpgrades() {
        super.displayUpgrades();
        if (this.upgradedMPCost) {
            ManaHelper.setMPCost(this, ManaHelper.getBaseMPCost(this));
            ManaHelper.setMPCostModified(this, true);
        }
    }

    protected void upgradeMPCost(int amount) {
        ManaHelper.setBaseMPCost(this, ManaHelper.getBaseMPCost(this) + amount);
        ManaHelper.setMPCost(this, ManaHelper.getBaseMPCost(this));
        this.upgradedMPCost = true;
    }

    private static Texture MP_COST_ORB;
    private static Color renderColor = Color.WHITE.cpy();

    public static void renderMPCost(AbstractCard card, SpriteBatch sb) {
        float drawX = card.current_x - 256.0F;
        float drawY = card.current_y - 256.0F;

        if (MP_COST_ORB == null) {
            MP_COST_ORB = TextureLoader.getTexture(TheProdigy.makeImagePath("512/CardMPCostOrb.png"));
        }
        if (ENERGY_COST_MODIFIED_COLOR == null) {
            getColorConstants();
        }

        if (!card.isLocked && card.isSeen) {
            if (ManaHelper.getMPCost(card) > -1) {
                ProdigyCard.renderHelper(card, sb, renderColor, MP_COST_ORB, drawX, drawY);

                String msg = Integer.toString(ManaHelper.getMPCost(card));
                Color costColor = Color.WHITE.cpy();
                if (AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(card)) {
                    if (ManaHelper.getMPCostModified(card)) {
                        if (ManaHelper.getMPCost(card) > ManaHelper.getBaseMPCost(card) && ManaHelper.getMPCost(card) > 0) {
                            costColor = ENERGY_COST_RESTRICTED_COLOR;
                        } else if (ManaHelper.getMPCost(card) < ManaHelper.getBaseMPCost(card)) {
                            costColor = ENERGY_COST_MODIFIED_COLOR;
                        }
                    }
                }
                costColor.a = card.transparency;

                FontHelper.renderRotatedText(sb, getMPCostFont(card), msg, card.current_x,
                        card.current_y, -132.0F * card.drawScale * Settings.scale,
                        129.0F * card.drawScale * Settings.scale, card.angle,
                        true, costColor);
            }
        }
    }

    private static void renderHelper(AbstractCard __instance, SpriteBatch sb, Color color, Texture img, float drawX, float drawY) {
        sb.setColor(color);
        sb.draw(img, drawX, drawY, 256.0F, 256.0F, 512.0F, 512.0F, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, 512, 512, false, false);
    }

    private static BitmapFont getMPCostFont(AbstractCard card) {
        FontHelper.cardEnergyFont_L.getData().setScale(card.drawScale * 0.75f);
        return FontHelper.cardEnergyFont_L;
    }

    private static Color ENERGY_COST_RESTRICTED_COLOR, ENERGY_COST_MODIFIED_COLOR;

    private static void getColorConstants() {
        Field f;
        try {
            f = AbstractCard.class.getDeclaredField("ENERGY_COST_RESTRICTED_COLOR");
            f.setAccessible(true);
            ENERGY_COST_RESTRICTED_COLOR = (Color) f.get(null);

            f = AbstractCard.class.getDeclaredField("ENERGY_COST_MODIFIED_COLOR");
            f.setAccessible(true);
            ENERGY_COST_MODIFIED_COLOR = (Color) f.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}