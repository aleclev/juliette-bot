package Exceptions;

import Adapteurs.MessageEventAdapter;

public class MessageEventException extends MasterException {
    private final MessageEventAdapter evt;
    private final String message;
    private final Boolean skipNotif;

    public MessageEventException(MessageEventAdapter p_evt, String p_message) {
        evt = p_evt;
        message = p_message;
        skipNotif = false;

    }

    /**
     * Envoi un message de notification où l'erreur à eu lieu.
     */
    public void envNotif() {
        if (skipNotif) {
            return;
        }
        evt.repondre(message);
    }
}
