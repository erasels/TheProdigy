package theProdigy.vfx.combat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BouncingImageEffect extends AbstractGameEffect {

    private static final float gravityY = 0.5F * Settings.scale; //The relic accelerates downwards by this much every frame.
    private static final float frictionX = 0.01F * Settings.scale;
    private static final float frictionY = 0.04F * Settings.scale;
    private static final float hoveramplificationX = 22 * Settings.scale; //this gets multiplied by the angle you hit the relic at to calculate the end velocity after touching it.
    private static final float hoveramplificationY = 22 * Settings.scale; //this, too.
    private static final float verticalbias = 10 * Settings.scale; //upwards momentum when touching the relic is additively increased by this number, just to make it easier.
    private static final int maxrotation = 0; //maximum rotational velocity in degree
    private static final int difficultyincrease = 3; //every counter increases gravity and decreases friction by this much %. Resets when it hits the ground.

    private static final float bounceplane = AbstractRelic.PAD_X;
    private static final float leftboundary = AbstractRelic.PAD_X;
    private static final float rightboundary = 1920F * Settings.scale - AbstractRelic.PAD_X;
    private static final float upperboundary = 1000F * Settings.scale - AbstractRelic.PAD_X;

    private static final float randombounce = 7.5F * Settings.scale;

    private float velocityX;
    private float velocityY;
    private int radialvelocity;

    private float scaleAmount;

    private float currentX;
    private float currentY;
    private int rotation;

    private Hitbox hb;
    private boolean mouseEntered;
    private Texture img;

    private int counter;

    public BouncingImageEffect(Texture img, float x, float y, float scaleAmount) {
        this.counter = 0;
        this.img = img;
        this.scaleAmount = scaleAmount;

        this.rotation = 0;
        this.currentX = x;
        this.currentY = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.radialvelocity = 0;

        this.hb = new Hitbox(AbstractRelic.PAD_X * scaleAmount, AbstractRelic.PAD_X * scaleAmount);
        this.mouseEntered = false;

    }

    public BouncingImageEffect(Texture img, float x, float y) {
        this(img, x, y, 1.0F);
    }


    public void render(SpriteBatch sb) {
        if (this.currentY > upperboundary + 100F * Settings.scale) {
            sb.draw(ImageMaster.CF_RIGHT_ARROW, this.currentX + 20.0F, upperboundary + 70 * Settings.scale, 64.0F, 64.0F, 128.0F, 128.0F, this.scale * 2, this.scale * 2, 90, 0, 0, 128, 128, false, false);
            sb.draw(img, this.currentX - 64.0F, upperboundary - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, 90, 0, 0, 128, 128, false, false);
        }
        sb.draw(img, this.currentX - 64.0F, this.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale * scaleAmount, this.scale * scaleAmount, this.rotation, 0, 0, 128, 128, false, false);
    }

    public void update() {
        this.hb.update();
        if (this.hb.hovered) {
            if (!mouseEntered) {
                this.mouseEntered = true;
                this.counter++;
                this.radialvelocity = MathUtils.random(-maxrotation, maxrotation);
            }

            float tmp = ((InputHelper.mX - this.hb.cX) / (this.hb.width / 2));
            velocityX = hoveramplificationX * -tmp;

            this.velocityX += MathUtils.random(-randombounce, randombounce);

            tmp = ((InputHelper.mY - this.hb.cY) / (this.hb.height / 2));
            velocityY = hoveramplificationY * Math.abs(tmp) + verticalbias;
            this.velocityY += MathUtils.random(-randombounce, randombounce);
        } else {
            float difficulty = 1 + 1 / (100F / difficultyincrease);
            this.velocityX += (this.velocityX > 0 ? -frictionX : frictionX) / difficulty;
            this.velocityY -= this.velocityY > 0 ? frictionY : -frictionY / difficulty;
            this.velocityY -= gravityY;
            this.mouseEntered = false;
        }

        //You spin me right round right round
        this.rotation += this.radialvelocity;

        //Check for left/right boundary crossings
        float difficulty = 1 + 1 / (100F / difficultyincrease);
        if (this.currentX + this.velocityX < leftboundary) {
            this.velocityX = Math.abs(this.velocityX);
        } else if (this.currentX + this.velocityX > rightboundary) {
            this.velocityX = -Math.abs(this.velocityX);
        } else {
            this.velocityX += (this.velocityX > 0 ? -frictionX : frictionX) / difficulty;
        }
        //Check for hitting the floor
        if (this.currentY + this.velocityY <= bounceplane) {
            this.velocityY = Math.abs(this.velocityY);
            if (this.velocityY > frictionY) {
                this.velocityY -= (this.velocityY > 0 ? frictionY : -frictionY) / difficulty;
            } else {
                this.velocityY = 0;
            }
            this.counter = 0;

            if (Math.abs(this.velocityY) > 10 * Settings.scale) {
                this.radialvelocity = MathUtils.random(-maxrotation, maxrotation);
            } else {
                this.radialvelocity = 0;
            }
        } else {
            this.velocityY -= (this.velocityY > 0 ? frictionY : -frictionY) / difficulty;
            this.velocityY -= gravityY;
        }

        //Move the relic
        this.currentX += this.velocityX;
        this.currentY += this.velocityY;

        //Move the hitbox to where the relic currently resides
        this.hb.move(this.currentX, this.currentY);

        /*if(...) {
            isDone = true;
        }*/
    }

    public void dispose() {

    }
}
