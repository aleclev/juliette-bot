package Exceptions;

import Adapteurs.MessageEventAdapter;
import Discord.ArgumentMetaData;
import Discord.CommandMetadata;
import Exceptions.MessageEventException;

public class ArgumentMissingException extends MessageEventException {
    public ArgumentMissingException(MessageEventAdapter p_evt, ArgumentMetaData ad, CommandMetadata cd) {
        super(
                p_evt,
                String.format("Missing the following Argument:\n%s", ad)
        );
    }
}
