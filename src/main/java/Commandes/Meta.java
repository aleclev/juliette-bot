package Commandes;

import Adapteurs.MessageEventAdapter;
import Discord.Bot;
import Discord.CommandThread;
import Discord.Commande;
import Discord.Module;
import Fonctions.CommandParse;

import java.util.ArrayList;

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
    }

    @Override
    public void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException {
        //String args[] = CommandParse.decouperCommande(evt.reqContenueRaw());
        ArrayList<String> args = reqArgs(evt.reqContenueRaw());
        for (String p : args) {
        }
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
            int index;
            try {
                index = Integer.parseInt(args.get(0));
            }
            catch (Exception e) {
                //TODO : message générique BAD_ARGS
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("You can call this command with the number of a module to inspect the commands of that module.\n```");
            int i = 0;
            for (Commande commande : reqBot().reqModules().get(index).reqCommandes()) {
                sb.append(String.format("%s : %s\n", i, commande.reqNoms().get(0)));
                i++;
            }
            sb.append("```");
            evt.repondre(sb.toString());
        }
        else if (args.size() == 2) {
            Commande cmd = reqBot().reqModules().get(Integer.parseInt(args.get(0))).reqCommandes().get(Integer.parseInt(args.get(1)));
            evt.repondre(cmd.reqDocDiscord());
        }
        else {
            evt.repondre("Cannot parse the given arguments...");
        }
    }
}