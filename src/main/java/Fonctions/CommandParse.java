/**
 * Commandes d'analyse des messages.
 */

package Fonctions;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class CommandParse {
    /**
     * @brief Fonction pour déterminer si le message est un appel de commande (si le message commence par le préfix)
     * @param prefix Le préfix du bot.
     * @param message Le message envoyé par l'utilisateur.
     * @return true si le message est un appel de commande. false sinon.
     */
    public static boolean estRequeteCommande(String message, String prefix) {
        try {
            return (message.substring(0, prefix.length()).equals(prefix));
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * @brief Renvoi une liste des mot clés de la commande et des arguments.
     * @return Liste des mot clés de la commande et des arguments.
     */
    public static String[] decouperCommande(String message) {
        return message.split(" ");
    }

    /**
     * retourne le String sans le prefix.
     * @param rawMsg
     * @param prefix
     * @return
     */
    public static String dePrefixer(String rawMsg, String prefix) {
        try {
            return rawMsg.substring(prefix.length());
        } catch (Exception e) {
            return null; //TODO : pourquoi 'null' et non null ?
        }
    }

    public static ArrayList<String> reqNotifTagList(String message, String id) {
        String[] decouper = message.split(" ");
        ArrayList<String> l_r = new ArrayList<>();

        for (int i = 0; i < decouper.length; i++) {
            if (decouper[i].startsWith(id)) {
                l_r.add(decouper[i].substring(id.length()));
            }
        }
        return l_r;
    }
}
