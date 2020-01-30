package theProdigy.animation;

import com.brashmonkey.spriter.Animation;
import com.brashmonkey.spriter.Player;
import theProdigy.characters.ProdigyCharacter;

public class PlayerListener implements Player.PlayerListener {

    private ProdigyCharacter character;

    public PlayerListener(ProdigyCharacter character) {
        this.character = character;
    }
    public void animationFinished(Animation animation){
        if (animation.name.contains("downed")) {
            //character.stopAnimation();
            character.runAnim("downedIdle");
        } else if (animation.name.contains("win")) {
            character.stopAnimation();
        }else if (!animation.name.equals("idle")) {
            character.resetAnimation();
        }
    }

    //UNUSED
    public void animationChanged(Animation var1, Animation var2){

    }

    //UNUSED
    public void preProcess(Player var1){

    }

    //UNUSED
    public void postProcess(Player var1){

    }

    //UNUSED
    public void mainlineKeyChanged(com.brashmonkey.spriter.Mainline.Key var1, com.brashmonkey.spriter.Mainline.Key var2){

    }
}
