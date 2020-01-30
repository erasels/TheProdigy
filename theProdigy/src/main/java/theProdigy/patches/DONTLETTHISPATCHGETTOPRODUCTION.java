package theProdigy.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import com.megacrit.cardcrawl.screens.splash.SplashScreen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SpirePatch(clz = SplashScreen.class, method = "update")
public class DONTLETTHISPATCHGETTOPRODUCTION {
    public static SpireReturn Prefix(SplashScreen __instance) {
        if (CardCrawlGame.playerName.toLowerCase().equals("rordev")) {
            __instance.isDone = true;
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    @SpirePatch(clz = MenuButton.class, method = "update")
    public static class fkmainmenu {
        public static void Prefix(MenuButton __instance) {
            if (CardCrawlGame.playerName.toLowerCase().equals("rordev")) {
                if (__instance.result == MenuButton.ClickResult.RESUME_GAME) {
                    CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.NONE;
                    CardCrawlGame.mainMenuScreen.hideMenuButtons();
                    CardCrawlGame.mainMenuScreen.darken();
                    try {
                        Method resumeGame = MenuButton.class.getDeclaredMethod("resumeGame");
                        resumeGame.setAccessible(true);
                        resumeGame.invoke(__instance);
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
