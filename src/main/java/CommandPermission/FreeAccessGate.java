package CommandPermission;

import Adapteurs.MessageEventAdapter;

/**
 * Gate sans conditions (défaut). N'importe quel
 * utilisateur peut utilisé cette commande
 * sans restrictions.
 */
public class FreeAccessGate extends CommandPermissionGate {
    @Override
    public void check(MessageEventAdapter evt) {

    }
}
