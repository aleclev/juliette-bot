package Discord;

import java.util.ArrayList;

/**
 * DTO pour contenir les metadonnées d'un argument.
 */
public class ArgumentMetaData {
    public final ArgumentType type;
    public final Boolean optionel;
    public final String description;
    public final String name;

    public ArgumentMetaData(ArgumentType p_type, Boolean p_optionel, String p_name, String p_description) {
        type = p_type;
        optionel = p_optionel;
        name = p_name;
        description = p_description;
    }

    public ArgumentType reqType() {
        return type;
    }
    public Boolean reqOptionel() {
        return optionel;
    }
    public String reqNom() {
        return name;
    }
    public String reqDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("**Name:** %s\n**Description:** %s\n**Type:** %s\n**Optional:** %s",
                reqNom(),
                reqDescription(),
                reqType().toString(),
                reqOptionel().toString());
    }
}
