package theProdigy.patches;

import com.badlogic.gdx.backends.lwjgl.audio.OpenALMusic;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.lib.*;
import javassist.CtBehavior;
import theProdigy.TheProdigy;

@SpirePatch(clz = OpenALMusic.class, method = "play")
public class AntiAudioBufferCrashPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"errorCode"})
    public static SpireReturn patch(OpenALMusic __instance, int errorCode) {
        TheProdigy.logger.info("Intercepted audio buffer crash, Error Code: " + errorCode);
        return SpireReturn.Return(null);
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.NewExprMatcher(GdxRuntimeException.class);
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
