package CommandPermission;

import Adapteurs.MessageEventAdapter;
import Exceptions.NotOwnerException;

/**
 * La commande n'est disponible qu'a l'auteur du bot.
 */
public class IsOwnerGate extends CommandPermissionGate {
    @Override
    public void check(MessageEventAdapter evt) throws NotOwnerException {
        if (evt.reqIdAuteur() != evt.reqMetadapter().reqOwner().reqId()) {
            throw new NotOwnerException(evt);
        }
    }
}
