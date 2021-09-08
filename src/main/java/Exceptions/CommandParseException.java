package Exceptions;

import Adapteurs.MessageEventAdapter;
import Discord.ArgumentMetaData;
import Discord.CommandMetadata;

/**
 * Cette exception est levée lorsque l'analyse d'une commande échoue.
 */
public class CommandParseException extends MessageEventException {
    public CommandParseException(CommandMetadata cd, ArgumentMetaData ad, MessageEventAdapter evt) {
        super(evt,
                String.format(
                        "ERROR! Could not parse the following argument:\n**Name:** %s\n**Description:** %s\n**Type:** %s\n**Optional:** %s",
                ad.reqNom(),
                ad.reqDescription(),
                ad.reqType().toString(),
                ad.reqOptionel().toString()
                )
        );
    }
}
