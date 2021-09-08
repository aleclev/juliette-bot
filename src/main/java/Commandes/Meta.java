package Commandes;

import Adapteurs.MessageEventAdapter;
import Discord.*;
import Discord.Module;
import Exceptions.APIException;
import Exceptions.CommandParseException;
import Exceptions.MasterException;

import java.util.List;

public class Meta extends Module {
    public Meta(Bot bot) {
        super(bot);

        //liste des commandes,
        this.ajouterCommande(new help(bot));
        this.setNom("Meta");
    }
}

class help extends Commande {
    public help(Bot bot) {
        super(bot);
        ajouterNom("help");
        ajouterArg(new ArgumentMetaData(ArgumentType.INT, true, "module_index", "Index of the desired module."));
        ajouterArg(new ArgumentMetaData(ArgumentType.INT, true, "command_index", "Index of the desired command."));

        makeSlashSherpaRun();
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MasterException {
        //String args[] = CommandParse.decouperCommande(evt.reqContenueRaw());


        List<Argument> args = creerArguments(evt);

        //helper des modules
        if (args.size() == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("You can call this command with the number of a module to inspect the commands of that module.\n```");
            int i = 0;
            for (Module module : reqBot().reqModules()) {
                sb.append(String.format("%s : %s\n", i, module.reqNom()));
                i++;
            }
            sb.append("```");
            evt.repondre(sb.toString());
        }
        else if (args.size() == 1) {
            StringBuilder sb = new StringBuilder();
            sb.append("You can call this command with the number of a module to inspect the commands of that module.\n```");
            int i = 0;
            for (Commande commande : reqBot().reqModules().get(args.get(0).toInt()).reqCommandes()) {
                sb.append(String.format("%s : %s\n", i, commande.reqNoms().get(0)));
                i++;
            }
            sb.append("```");
            evt.repondre(sb.toString());
        }
        else if (args.size() == 2) {
            Commande cmd = reqBot().reqModules().get(args.get(0).toInt()).reqCommandes().get(args.get(1).toInt());
            evt.repondre(cmd.reqDocDiscord());
        }
        else {
            evt.repondre("Cannot parse the given arguments...");
        }
    }
}