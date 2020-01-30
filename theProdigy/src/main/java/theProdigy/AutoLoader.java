package theProdigy;

import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import javassist.CtClass;
import javassist.NotFoundException;
import org.clapper.util.classutil.*;
import theProdigy.cards.abstracts.ProdigyCard;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("unused")
public class AutoLoader {
    public static void addCards() throws URISyntaxException {
        ClassFinder finder = new ClassFinder();
        URL url = TheProdigy.class.getProtectionDomain().getCodeSource().getLocation();
        finder.add(new File(url.toURI()));

        ClassFilter filter = new AndClassFilter(
                new NotClassFilter(new InterfaceOnlyClassFilter()),
                new NotClassFilter(new AbstractClassFilter()),
                new ClassModifiersClassFilter(Modifier.PUBLIC),
                new Filters.CardFilter()
        );

        Collection<ClassInfo> foundClasses = new ArrayList<>();
        finder.findClasses(foundClasses, filter);

        foundClasses.stream()
                .map(AutoLoader::classInfoToCtClass)
                .filter(Objects::nonNull)
                .filter(AutoLoader::ctClassIgnoresAutoLoader)
                .filter(AutoLoader::ctClassIsASacredCard)
                .distinct()
                .map(AutoLoader::ctClassToAbstractCard)
                .filter(Objects::nonNull)
                .forEach(AutoLoader::addToBaseMod);
    }

    private static void addToBaseMod(AbstractCard card) {
        BaseMod.addCard(card);
        UnlockTracker.unlockCard(card.cardID);
        TheProdigy.logger.info("Added Card: " + card.name);
    }

    private static boolean ctClassIgnoresAutoLoader(CtClass ctClass) {
        return !ctClass.hasAnnotation(AutoLoaderIgnore.class);
    }

    private static boolean ctClassIsASacredCard(CtClass ctClass) {
        try {
            return ctClass.subclassOf(Loader.getClassPool().get(ProdigyCard.class.getName()));
        } catch (NotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static CtClass classInfoToCtClass(ClassInfo classInfo) {
        try {
            return Loader.getClassPool().get(classInfo.getClassName());
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static AbstractCard ctClassToAbstractCard(CtClass ctClass) {
        AbstractCard card;
        try {
            card = (AbstractCard) Loader.getClassPool().getClassLoader().loadClass(ctClass.getName()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            card = null;
        }
        return card;
    }

    public static class Filters {
        public static class CardFilter implements ClassFilter {
            private static final String PACKAGE = "theProdigy.cards";
            @Override
            public boolean accept(ClassInfo classInfo, ClassFinder classFinder) {
                return classInfo.getClassName().startsWith(PACKAGE) && !classInfo.getClassName().contains("_deprecated");
            }
        }
    }
}
