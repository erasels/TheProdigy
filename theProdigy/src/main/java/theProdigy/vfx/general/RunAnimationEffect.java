package theProdigy.vfx.general;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import theProdigy.TheProdigy;

public class RunAnimationEffect extends AbstractGameEffect {
    private String key;
    public enum ANIS {
       IDLE, GUARDA, GUARDB, HITLOW, HITHIGH, FAILURE, LOWKICK, GRAB, RODWHACK, RODSTAB, RODSLASH, BACKFLIP, BACKFLIPKICK, SPELLA, SPELLB, SPELLC, THROW, WINA, WINB, DOWNED, DOWNIDLE
    }

    public RunAnimationEffect(String animation_key) {
        key = animation_key;
    }

    public RunAnimationEffect(ANIS ani) {
        switch(ani) {
            case IDLE:
                key = "idle";
                break;
            case GUARDA:
                key = "guardA";
                break;
            case GUARDB:
                key = "guardB";
                break;
            case HITLOW:
                key = "hitLow";
                break;
            case HITHIGH:
                key = "hitHigh";
                break;
            case FAILURE:
                key = "failure";
                break;
            case LOWKICK:
                key = "lowKick";
                break;
            case GRAB:
                key = "grab";
                break;
            case RODWHACK:
                key = "rodWhack";
                break;
            case RODSTAB:
                key = "rodStab";
                break;
            case RODSLASH:
                key = "rodSlash";
                break;
            case BACKFLIP:
                key = "backflip";
                break;
            case BACKFLIPKICK:
                key = "backflipKick";
                break;
            case SPELLA:
                key = "spellA";
                break;
            case SPELLB:
                key = "spellB";
                break;
            case SPELLC:
                key = "spellC";
                break;
            case THROW:
                key = "throw";
                break;
            case WINA:
                key = "winA";
                break;
            case WINB:
                key = "winB";
                break;
            case DOWNED:
                key = "downed";
                break;
            case DOWNIDLE:
                key = "downedIdle";
                break;
        }
    }

    @Override
    public void update() {
        TheProdigy.runAnimation(key);
        isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    @Override
    public void dispose() {
    }
}
