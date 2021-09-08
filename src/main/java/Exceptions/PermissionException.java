package Exceptions;

import Adapteurs.MessageEventAdapter;

public class PermissionException extends MessageEventException {
    public PermissionException(MessageEventAdapter p_evt, String p_message) {
        super(p_evt, p_message);
    }
}
