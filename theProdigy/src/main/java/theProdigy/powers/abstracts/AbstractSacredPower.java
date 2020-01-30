package theProdigy.powers.abstracts;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;

import static theProdigy.TheProdigy.makePowerPath;

public class AbstractSacredPower extends TwoAmountPower {
    public boolean isBarrierPower = false;

    //To be used with a certain power
    public void extendBarrier(int amount) {
        this.amount += amount;
    }
    public void retaliate(AbstractCreature target) {}

    /**
     * @param bigImageName - is the name of the 84x84 image for your power.
     * @param smallImageName - is the name of the 32x32 image for your power.
     */
    public void setImage(String bigImageName, String smallImageName){
        String path128 = makePowerPath(bigImageName);
        String path48 = makePowerPath(smallImageName);

        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);
    }

    /**
     * @param imgName - is the name of a 16x16 image. Example: setTinyImage("power.png");
     */
    public void setTinyImage(String imgName){
        this.img = ImageMaster.loadImage(makePowerPath(imgName));
    }
}
