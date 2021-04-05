package Discord;

import Adapteurs.MessageEventAdapter;
import Fonctions.CommandParse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Commande extends Thread {
    public Commande() {}

    /**
     *
     * @param p_bot Le bot maître.
     */
    public Commande(Bot p_bot) {
        bot = p_bot;
        l_nomsComplets = new ArrayList<>();
        description = "No description given.";
        utilisation = "No usage given.";
        exemple = "No examples given.";
    }

    /**
     * Cette méthode est appelée par Bot pour signaler qu'une commande doît être exécutée.
     * La méthode créer une instance de CommandThread afin d'exécuter la commande dans un thread.
     * @param evt L'événement qui a provoqué l'appel de la commande (Méthode surchargée pour chaque type d'événement.)
     */
    public CommandThread call(MessageEventAdapter evt) {
        return new CommandThread(this, evt);
    }

    /**
     * méthode étant appelée lors de l'exécution de la commande par un utilisateur
     * @param evt
     */
    public abstract void traitement(MessageEventAdapter evt, CommandThread cmdThread) throws InterruptedException;

    /**
     * retourne vrai si un des noms complets corrèle avec le début du message.
     * @param msg
     * @return
     */
    public boolean estAppele(String msg) {
        for (String nom : l_nomsComplets) {
            if (msg.toUpperCase().startsWith(nom.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

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

    public void setDescription(String desc) {
        description = desc;
    }

    public void setUtilisation(String p_u) {
        utilisation = p_u;
    }

    public void setExemple(String ex) {
        exemple = ex;
    }

    Bot bot; //référence vers le bot en haut.
    List<String> l_nomsComplets;
    String description;
    String utilisation;
    String exemple;
}
