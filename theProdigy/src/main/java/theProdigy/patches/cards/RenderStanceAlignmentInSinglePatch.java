package theProdigy.patches.cards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import theProdigy.cards.abstracts.ProdigyCard;

@SpirePatch(clz = SingleCardViewPopup.class, method = "renderFrame")
public class RenderStanceAlignmentInSinglePatch {
    @SpireInsertPatch(rloc = 0, localvars = {"card"})
    public static void Method(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard card) {
        if (card instanceof ProdigyCard) {
            ProdigyCard c = (ProdigyCard) card;
            c.renderAlignment(sb, true);
        }
    }
}
