package Exceptions;

import Adapteurs.MessageEventAdapter;

public class OwnerOnlyAccessException extends CommandPermissionException {
    public OwnerOnlyAccessException(MessageEventAdapter p_evt, String p_message) {
        super(p_evt, "Error! This command is only accessible to the bot owner.");
    }
}
