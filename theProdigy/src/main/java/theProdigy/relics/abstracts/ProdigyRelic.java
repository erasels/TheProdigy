package theProdigy.relics.abstracts;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import theProdigy.TheProdigy;
import theProdigy.util.TextureLoader;

public abstract class ProdigyRelic extends AbstractRelic {
    public ProdigyRelic(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, "", tier, sfx);

        imgUrl = imgName;

        if (img == null || outlineImg == null) {
            img = TextureLoader.getTexture(TheProdigy.makeRelicPath(imgName));
            largeImg = TextureLoader.getTexture(TheProdigy.makeRelicPath("big/"+imgName));
            outlineImg = TextureLoader.getTexture(TheProdigy.makeRelicOutlinePath(imgName));
        }
    }
}
