package Discord;

import Adapteurs.MessageEventAdapter;
import Exceptions.NoArgumentsInCommand;
import Fonctions.CommandParse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Commande extends Thread {
    public Commande() {}

    /**
     * Creates a command
     * @param p_bot Le bot maître.
     */
    public Commande(Bot p_bot) {
        bot = p_bot;
        l_nomsComplets = new ArrayList<>();
        description = "No description given.";
        utilisation = "No usage given.";
        exemple = "No examples given.";
        slash = false;
        signature = new ArrayList<>();
    }

    /**
     * This method is called when a command is requested
     *
     * Cette méthode est appelée par Bot pour signaler qu'une commande doît être exécutée.
     * La méthode crée une instance de CommandThread afin d'exécuter la commande dans un thread.
     * @param evt L'événement qui a provoqué l'appel de la commande (Méthode surchargée pour chaque type d'événement.)
     */
    public CommandThread call(MessageEventAdapter evt) {
        return new CommandThread(this, evt);
    }

    /**
     * This method represents the actual command treatment, i.e. what the command does
     *
     * méthode étant appelée lors de l'exécution de la commande par un utilisateur
     * @param evt
     */
    public abstract void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException;

    /**
     * retourne vrai si un des noms complets corrèle avec le début du message.
     * @param msg
     * @return
     */
    public boolean estAppelee(String msg) {
        for (String nom : l_nomsComplets) {
            if (msg.toUpperCase().startsWith(nom.toUpperCase()) || msg.replace("_", " ").toUpperCase().startsWith(nom.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of arguments in the message as a String List
     * @param message
     * @return
     */
    public ArrayList<String> reqArgs(String message) {
        ArrayList<String> r = new ArrayList<>();

        message = CommandParse.dePrefixer(message, bot.reqPrefix());

        for (String nomCommande : this.l_nomsComplets) {
            if (message.startsWith(nomCommande + " ")) {
                r.addAll(Arrays.asList(CommandParse.decouperCommande(message.substring(nomCommande.length()+1))));
                return r;
            }
        }
        return r;
    }

    public void ajouterNom(String nouvNom) {
        l_nomsComplets.add(nouvNom);
    }

    public Bot reqBot() {
        return bot;
    }

    public List<String> reqNoms() {
        return l_nomsComplets;
    }

    /**
     *
     * @return returns the command documentation as a string
     */
    public String reqDocDiscord() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("```%s :\n", l_nomsComplets.get(0)));
        sb.append("Aliases :\n");
        for (int i = 1; i < l_nomsComplets.size(); i++) {
            sb.append(l_nomsComplets.get(i)+"\n");
        }
        sb.append(String.format("\nDescription :\n%s\n", description));
        sb.append(String.format("\nUsage :\n%s\n", utilisation));
        sb.append(String.format("\nExample :\n%s```", exemple));
        return sb.toString();
    }

    /**
     * Enregistre la commande comme slash, mais seulement pour Sherpa Run.
     */
    public void makeSlashSherpaRun() {
        if (l_nomsComplets.isEmpty()) {
            System.out.print("Erreur dans la création d'une commande slash (aucun nom valie).");
            return;
        }
        String nom = l_nomsComplets.get(0).replace(" ", "_");
        bot.reqMetaAdapter().getSherpaRun().creerSlashCommande(nom, description, signature);
        slash = true;
        System.out.printf("Commande slash créée. Nom: %s, Description: %s\n", nom, description);
    }

    /**
     * Ajoute un nouvel argument à la signature.
     * @param type Le type.
     * @param opt True si l'argument est optionel.
     */
    public void ajouterArg(String nom, String desc, Argument.Type type, Boolean opt) {
        signature.add(new Argument(nom, desc, type, opt));
    }

    public List<Argument> creerArguments(MessageEventAdapter evt) {
        //if (signature.size() == 0) {
        //   throw new NoArgumentsInCommand();
        //}

        List<Argument> args = new ArrayList<>();
        String nouv = "";

        if (evt.estSlash()) {

        }
        else {
            String prefix = reqBot().reqPrefix();
            String call = CommandParse.dePrefixer(evt.reqContenueRaw(), prefix);
            boolean skip = true;

            for (String nom : l_nomsComplets) {
                if (call.startsWith(nom)) {
                    nom = nom + " ";
                    nouv = call.replace(nom, "");
                    skip = false;
                    break;
                }
            }
            if (skip) {
                return args;
            }

            String[] l_s;

            l_s = CommandParse.decouperCommande(nouv);

            for (int i = 0; i < l_s.length && i < signature.size(); i++) {
                args.add(Argument.stringToArgument(l_s[i], signature.get(i).reqType()));
            }
        }

        return args;
    }

    public Boolean estSlash() {
        return slash;
    }

    public void setDescription(String desc) {
        description = desc;
    }
    public void setUtilisation(String p_u) {
        utilisation = p_u;
    }
    public void setExemple(String ex) {
        exemple = ex;
    }

    private Bot bot;                        //référence vers le bot en haut.
    private List<String> l_nomsComplets;    //Complete command name
    private String description;             //Command description
    private String utilisation;             //Command usage
    private String exemple;                 //Command usage example
    private Boolean slash;                  // Indique si la commande est implémentée avec slash
    private List<Argument> signature;   //Optionel. Décrit les arguments de la commande.
}
