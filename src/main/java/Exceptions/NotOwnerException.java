package Exceptions;

import Adapteurs.MessageEventAdapter;

public class NotOwnerException extends PermissionException {
    public NotOwnerException(MessageEventAdapter evt) {
        super(evt, "Error! This command is only accessible by the bot owner.");
    }
}
