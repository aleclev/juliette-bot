package Exceptions;

import Adapteurs.MessageEventAdapter;

public class APIException extends MasterException {
    private final String message;

    public APIException(String p_message) {
        message = p_message;
    }

    public String reqMessage() {
        return message;
    }
}
