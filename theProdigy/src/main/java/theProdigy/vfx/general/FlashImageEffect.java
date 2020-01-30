package theProdigy.vfx.general;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FlashImageEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 0.75f;
    private float scale, scaleMult;
    private Color color;
    private Texture texture;
    private float x, y;

    public FlashImageEffect(Texture tex, float x, float y, Color color, float duration, float scaleMult) {
        this.scale = Settings.scale;
        this.color = color.cpy();
        this.texture = tex;
        this.x = x;
        this.y = y;
        this.startingDuration = this.duration = duration;
        this.scaleMult = scaleMult;
    }

    public FlashImageEffect(Texture tex, float x, float y, Color color) {
        this(tex, x, y, color, EFFECT_DUR, 4);
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.scale *= 1.0f + Gdx.graphics.getDeltaTime() * scaleMult;
        this.color.a = Interpolation.fade.apply(0.0f, 0.75f, this.duration / startingDuration);
        if (this.color.a < 0.0f)
            this.color.a = 0.0f;
        if (this.duration < 0.0f) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 771);
        sb.draw(this.texture, x, y, 64f, 64f, texture.getWidth(), texture.getHeight(), scale, scale, 0.0f, 0, 0, texture.getWidth(), texture.getHeight(), false, false);
    }

    @Override
    public void dispose() {
        this.texture.dispose();
    }
}
