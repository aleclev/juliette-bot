package Discord;

import Adapteurs.MessageEventAdapter;
import Exceptions.*;
import Fonctions.CommandParse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Commande extends Thread {
    public Commande() {}

    public CommandMetadata reqMetadata() {
        return metadata;
    }

    /**
     * Creates a command
     * @param p_bot Le bot maître.
     */
    public Commande(Bot p_bot) {
        bot = p_bot;
        //l_nomsComplets = new ArrayList<>();
        //description = "No description given.";
        //utilisation = "No usage given.";
        //exemple = "No examples given.";
        //slash = false;
        //signature = new CommandSignature();

        metadata = new CommandMetadata();
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
    public abstract void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException, MessageEventException, APIException, MasterException;

    /**
     * retourne vrai si un des noms complets corrèle avec le début du message.
     * @param msg
     * @return
     */
    public boolean estAppelee(String msg) {
        for (String nom : metadata.reqNoms()) {
            if (msg.toUpperCase().startsWith(nom.toUpperCase()) || msg.replace("-", " ").toUpperCase().startsWith(nom.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of arguments in the message as a String List
     * @return
     */
    public ArrayList<String> reqArgs(MessageEventAdapter evt) {
        ArrayList<String> r = new ArrayList<>();

/**        message = CommandParse.dePrefixer(message, bot.reqPrefix());

        for (String nomCommande : this.metadata.reqNoms()) {
            if (message.startsWith(nomCommande + " ")) {
                r.addAll(Arrays.asList(CommandParse.decouperCommande(message.substring(nomCommande.length()+1))));
                return r;
            }
        }**/
        return r;
    }

    public void ajouterNom(String nouvNom) {
        //l_nomsComplets.add(nouvNom);
        metadata.ajouterNom(nouvNom);
    }

    public Bot reqBot() {
        return bot;
    }

    public List<String> reqNoms() {
        //return l_nomsComplets;
        return metadata.reqNoms();
    }

    /**
     *
     * @return returns the command documentation as a string
     */
    public String reqDocDiscord() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("```%s :\n", metadata.reqNoms().get(0)));
        sb.append("Aliases :\n");
        for (int i = 1; i < metadata.reqNoms().size(); i++) {
            sb.append(metadata.reqNoms().get(i)+"\n");
        }
        sb.append(String.format("\nDescription :\n%s\n", metadata.reqDescription()));
        sb.append(String.format("\nUsage :\n%s\n", metadata.reqUtilisation()));
        sb.append(String.format("\nExample :\n%s```", metadata.reqExemple()));
        return sb.toString();
    }

    /**
     * Enregistre la commande comme slash, mais seulement pour Sherpa Run.
     */
    public void makeSlashSherpaRun() {
        if (metadata.reqNoms().isEmpty()) {
            System.out.print("Erreur dans la création d'une commande slash (aucun nom valie).");
            return;
        }
        String nom = metadata.reqNoms().get(0).replace(" ", "_");
        bot.reqMetaAdapter().getSherpaRun().creerSlashCommande(metadata);
        System.out.printf("Commande slash créée. Nom: %s, Description: %s\n", nom, metadata.reqDescription());
    }

    public void ajouterArg(ArgumentMetaData p_metadata) {
        //metadata.req.ajouterMetadata(metadata);
        metadata.reqSignature().ajouterMetadata(p_metadata);
    }

    public List<Argument> creerArguments(MessageEventAdapter evt) throws MasterException {
        return bot.reqMetaAdapter().genArgs(metadata, evt);
    }

    public void setDescription(String desc) {
        //description = desc;
        metadata.setDescription(desc);
    }
    public void setUtilisation(String p_u) {
        //utilisation = p_u;
        metadata.setUtilisation(p_u);
    }
    public void setExemple(String ex) {
        //exemple = ex;
        metadata.setExemple(ex);
    }

    private Bot bot;                        //référence vers le bot en haut.
    //private List<String> l_nomsComplets;    //Complete command name
    //private String description;             //Command description
    //private String utilisation;             //Command usage
    //private String exemple;                 //Command usage example
    //private Boolean slash;                  // Indique si la commande est implémentée avec slash
    private CommandMetadata metadata;
    //private CommandSignature signature;
}
