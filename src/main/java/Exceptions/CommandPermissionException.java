package Exceptions;

import Adapteurs.MessageEventAdapter;

public class CommandPermissionException extends MessageEventException {
    public CommandPermissionException(MessageEventAdapter p_evt, String p_message) {
        super(p_evt, p_message);
    }
}
