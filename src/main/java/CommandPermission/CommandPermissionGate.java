package CommandPermission;

import Adapteurs.MessageEventAdapter;
import Exceptions.PermissionException;

/**
 * Classe abstraite. Chaque commande contient une gate d'Autoristation prédéterminée.
 * Pour que la commande soit exécuté, le contexte de l'Appel doit répondre à une liste
 * de critères prédéterminés.
 */
public abstract class CommandPermissionGate {
    //Cette méthode lève une erreur si la condition de permission n'est pas remplie.
    public abstract void check(MessageEventAdapter evt) throws PermissionException;
}
