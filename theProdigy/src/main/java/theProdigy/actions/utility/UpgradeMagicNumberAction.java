package theProdigy.actions.utility;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.UUID;

public class UpgradeMagicNumberAction extends AbstractGameAction {
    private final UUID uuid;

    public UpgradeMagicNumberAction(UUID targetUUID, int amount) {
        this.amount = amount;
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.uuid = targetUUID;
    }

    public void update() {
        for (AbstractCard c : GetAllInBattleInstances.get(uuid)) {
            c.baseMagicNumber = Math.max(0, c.baseMagicNumber + amount);
        }
        this.isDone = true;
    }
}