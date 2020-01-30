package theProdigy.actions.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theProdigy.util.UC;

public class DealDamageEqualToBlockAction extends AbstractGameAction {
    private DamageInfo.DamageType type;
    AttackEffect ae;

    public DealDamageEqualToBlockAction(AbstractMonster target, DamageInfo.DamageType type, AttackEffect ae) {
        this.target = target;
        this.type = type;
        this.ae = ae;
    }

    @Override
    public void update() {
        if(target == null || target.isDeadOrEscaped()) {
            target = AbstractDungeon.getRandomMonster();
        }
        UC.doDmg(target, UC.p().currentBlock, type, ae, false, true);
        isDone = true;
    }
}
