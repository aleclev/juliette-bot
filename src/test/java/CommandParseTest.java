import Discord.Argument;
import Discord.ArgumentMetaData;
import Discord.ArgumentType;
import Discord.CommandMetadata;
import Fonctions.CommandParse;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static Fonctions.CommandParse.genererArgumentsText;

public class CommandParseTest {

    @Test
    public void test() {
        String t1 = "j/notif add tag some text";

        CommandMetadata cd = new CommandMetadata();

        cd.ajouterNom("notif add");

        Assert.assertEquals(CommandParse.enleverCommande(cd, t1), "tag some text");
    }

    @Test
    public void argumentTest1() {
        CommandMetadata cd = new CommandMetadata();
        cd.ajouterNom("some func");

        ArgumentMetaData A1 = new ArgumentMetaData(ArgumentType.STRING, false, "arg1", "first argument");
        ArgumentMetaData A2 = new ArgumentMetaData(ArgumentType.STRING, false, "arg2", "second argument");

        cd.reqSignature().ajouterMetadata(A1);
        cd.reqSignature().ajouterMetadata(A2);
        /**
        ArrayList<Argument> a_l = genererArgumentsText(cd, "j/some func hello wolrd how is it going??");

        for (Argument arg: a_l) {
            System.out.print(arg);
            System.out.println();
        }**/
    }

    @Test
    public void argumentTest() {
        CommandMetadata cd = new CommandMetadata();
        cd.ajouterNom("some func");

        ArgumentMetaData A1 = new ArgumentMetaData(ArgumentType.STRING, false, "arg1", "first argument");
        ArgumentMetaData A2 = new ArgumentMetaData(ArgumentType.INT, false, "arg2", "second argument");

        cd.reqSignature().ajouterMetadata(A1);
        cd.reqSignature().ajouterMetadata(A2);

        /**ArrayList<Argument> a_l = genererArgumentsText(cd, "j/some func hello 11122 wolrd how is it going??");

        for (Argument arg: a_l) {
            System.out.print(arg);
            System.out.println();
        }**/
    }
}
