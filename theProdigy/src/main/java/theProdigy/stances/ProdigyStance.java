package theProdigy.stances;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import theProdigy.util.UC;
import theProdigy.vfx.combat.unique.BetterStanceAuraEffect;

public abstract class ProdigyStance extends AbstractStance {
    protected static String ENTRANCE_SOUND = "STANCE_ENTER_CALM";
    protected static String LOOP_SOUND = "STANCE_LOOP_CALM";

    protected static long sfxId = -1L;
    protected static StanceStrings stanceString;

    public ProdigyStance(String id, Color col) {
        this.ID = id;
        if (stanceString == null) {
            stanceString = CardCrawlGame.languagePack.getStanceString(id);
        }
        this.name = stanceString.NAME;
        updateDescription();
        this.c = col;
    }

    public ProdigyStance(String id, Color col, String entrance, String loop) {
        this(id, col);
        ENTRANCE_SOUND = entrance;
        LOOP_SOUND = loop;
    }

    public void updateDescription() {
        description = stanceString.DESCRIPTION[0];
    }

    public void onApplyPower(AbstractCreature source, AbstractCreature target, AbstractPower p) { }
    public float modifyBlock(float block) { return block; }
    public void onUseCard(AbstractCard c, UseCardAction UAC) { }

    public void updateAnimation(float c1a, float c1b, float c2a, float c2b, float c3a, float c3b) {
        this.particleTimer2 -= UC.gt();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            AbstractDungeon.effectsQueue.add(new BetterStanceAuraEffect(c1a, c1b, c2a, c2b, c3a, c3b));
        }
    }

    public void onEnterStance() {
        if (sfxId != -1L)
            stopIdleSfx();
        CardCrawlGame.sound.play(ENTRANCE_SOUND);
        sfxId = CardCrawlGame.sound.playAndLoop(LOOP_SOUND);
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(c, true));
    }

    public void onExitStance() {
        stopIdleSfx();
    }

    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop(LOOP_SOUND, sfxId);
            sfxId = -1L;
        }
    }
}
