package theProdigy.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import theProdigy.TheProdigy;

import java.lang.reflect.Field;

public class RenderMPCostHelper {
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
                renderHelper(card, sb, renderColor, MP_COST_ORB, drawX, drawY);

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

    //Single card view shit
    private static Texture BIG_MP_COST_ORB;
    private static float centerX = (float)Settings.WIDTH / 2.0F;
    private static float centerY = (float)Settings.HEIGHT / 2.0F;
    public static void renderMPCostInSingle(AbstractCard card, SpriteBatch sb, SingleCardViewPopup __instance) {
        float fScaleX = FontHelper.SCP_cardEnergyFont.getData().scaleX;
        FontHelper.SCP_cardEnergyFont.getData().setScale(0.75F);

        if (BIG_MP_COST_ORB == null) {
            BIG_MP_COST_ORB = TextureLoader.getTexture(TheProdigy.makeImagePath("1024/CardMPCostOrb.png"));
        }

        if (!card.isLocked && card.isSeen) {
            if (ManaHelper.getMPCost(card) > -1) {
                renderHelperSingle(sb, BIG_MP_COST_ORB, centerX - 355.0F * Settings.scale,
                        centerY + 170.0F * Settings.scale);

                String msg = Integer.toString(ManaHelper.getMPCost(card));
                Color costColor = Color.WHITE.cpy();

                FontHelper.renderFont(sb, FontHelper.SCP_cardEnergyFont, msg, 675.0F * Settings.scale,
                        centerY + 268.0F * Settings.scale, costColor);
            }
        }

        FontHelper.SCP_cardEnergyFont.getData().setScale(fScaleX);
    }

    private static void renderHelperSingle(SpriteBatch sb, Texture img, float drawX, float drawY) {
        sb.draw(img, drawX, drawY,
                0, 0, 164.0F, 164.0F,
                Settings.scale,  Settings.scale,
                0, 0, 0, 164, 164, false, false);

    }
}
