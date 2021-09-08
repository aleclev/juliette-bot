/**
 * Commandes d'analyse des messages.
 */

package Fonctions;

import Adapteurs.MessageEventAdapter;
import Adapteurs.MetaAdapter;
import Adapteurs.UserAdapter;
import Discord.Argument;
import Discord.ArgumentMetaData;
import Discord.CommandMetadata;
import Exceptions.*;

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

    public static ArrayList<Argument> genererArgumentsText(CommandMetadata metadata, MessageEventAdapter evt) throws MessageEventException, APIException {
        int i = 1;
        String message = evt.reqContenueRaw();
        String temp = enleverCommande(metadata, message);

        ArrayList<Argument> r = new ArrayList<>();

        for (ArgumentMetaData argmd : metadata.reqSignature().reqMetadata()) {

            try {
                //Si l'argument est optionel et que tout le message a été analysé, la fonction retourne.
                if (argmd.reqOptionel() && temp.equals("")) {
                    return r;
                }

                if (!argmd.reqOptionel() && temp.equals("")) {
                    throw new ArgumentMissingException(evt, argmd, metadata);
                }

                Object[] o_temp = new Object[2];

                switch (argmd.reqType()) {
                    case STRING:
                        o_temp = parseString(temp, argmd, i == metadata.reqSignature().reqMetadata().size());
                        break;
                    case INT:
                        o_temp = parseLong(temp, argmd, i == metadata.reqSignature().reqMetadata().size());
                        break;
                    case USER:
                        o_temp = parseUser(temp, argmd, evt.reqMetadapter());
                        break;
                }

                Argument n_arg = (Argument)o_temp[0];

                //Si l'argument est null, la fonction lève une erreur et retourne.
                if (n_arg.reqDataRaw() == null) {
                    throw new Exception();
                }

                r.add((Argument) o_temp[0]);
                temp = (String) o_temp[1];
                i++;
            }
            catch (APIException | MessageEventException e) {
                throw e;
            }
            //Erreur générique
            catch (Exception e) {
                throw new CommandParseException(metadata, argmd, evt);
            }
        }

        return r;
    }

    private static Object[] parseString(String message, ArgumentMetaData argmd, Boolean estDernier) {
        String temp = message.trim();

        //String qui sera retourné
        String r;

        //String de l'Argument
        String nouv;

        if (!estDernier) {
            nouv = premierArg(temp);
        }
        else {
            nouv = temp;
        }

        r = temp.substring(nouv.length());

        Argument arg = new Argument(nouv, argmd);

        return new Object[]{arg, r};
    }

    private static Object[] parseLong(String message, ArgumentMetaData argmd, Boolean estDernier) {
        String temp = message.trim();
        String nouv = premierArg(temp);

        Long data = Long.parseLong(nouv);
        String r = temp.substring(nouv.length());

        Argument arg = new Argument(data, argmd);

        return new Object[]{arg, r};
    }

    private static Object[] parseUser(String message, ArgumentMetaData argmd, MetaAdapter ma) throws UserNotFoundException {
        String temp = message.trim();
        String nouv = premierArg(temp);
        nouv = nouv
                .replace("<", "")
                .replace(">", "")
                .replace("@", "")
                .replace("!", "");

        String r = temp.substring(nouv.length());
        UserAdapter u = ma.trouverUtilisateur(Long.parseLong(nouv));

        Argument arg = new Argument(u, argmd);

        return new Object[]{arg, r};
    }

    private static String premierArg(String message) {
        String temp = message.trim();

        int idx = temp.indexOf(" ");

        if (idx == -1) {
            return temp;
        }
        else {
            return temp.substring(0, idx);
        }
    }

    /**
     * Coupe le message pour conserver seulement les arguments d'une commande.
     */
    public static String enleverCommande(CommandMetadata metadata, String message) {
        String temp = message.substring(2);

        for (String nom : metadata.reqNoms()) {
            if (temp.startsWith(nom)) {
                temp = temp.substring(nom.length());
            }
        }

        return temp;
    }
}
