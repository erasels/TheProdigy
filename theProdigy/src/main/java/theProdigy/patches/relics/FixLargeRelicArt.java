package theProdigy.patches.relics;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.SingleRelicViewPopup;
import theProdigy.relics.abstracts.ProdigyRelic;

@SpirePatch(
        clz = SingleRelicViewPopup.class,
        method = "initializeLargeImg"
)
public class FixLargeRelicArt {
    @SpireInsertPatch(
            rloc = 0,
            localvars = {"relic", "largeImg"}
    )
    public static SpireReturn<Void> Insert(SingleRelicViewPopup __instance, AbstractRelic relic, @ByRef Texture[] largeImg) {
        if (relic instanceof ProdigyRelic && relic.largeImg != null) {
            largeImg[0] = new Texture(relic.largeImg.getTextureData());
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}