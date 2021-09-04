package Discord;

import Adapteurs.MessageEventAdapter;
import Adapteurs.UserAdapter;

import java.util.List;

public class Argument {
    private final Type type;            // Indique le type présumé de l'argument.
    private final boolean optionel;     // Indique que l'argument est optionel.
    private final boolean template;     // Indique que l'argument est utilisé pour décrire la signature d'une commande.
    private Object data;
    private String nom;
    private String desc;

    public Argument(String p_nom, String p_desc, Type p_type, boolean p_optionel) {
        type = p_type;
        optionel = p_optionel;
        template = true;
        nom = p_nom;
        desc = p_desc;
    }

    public Argument(Object obj, Type p_type) {
        data = obj;
        type = p_type;
        optionel = false;
        template = false;
    }

    public Boolean sameType(Argument info) {
        return this.type == info.type;
    }

    /**
     *
     * @return
     */
    public Integer toInt() {
        return (Integer)data;
    }

    public Float toFloat() {
        return (Float)data;
    }

    public String toStr() {
        return (String)data;
    }

    public UserAdapter toUser() {
        return (UserAdapter)data;
    }

    public static Argument stringToArgument(String s, Type t) {
        Object obj = new Object();
        switch (t) {
            default:
                obj = null;
                break;
            case INT:
                obj = Integer.parseInt(s);
                break;
            case FLOAT:
                obj = Float.parseFloat(s);
                break;
            case STRING:
                obj = s;
                break;
            case USER:
                
        }

        return new Argument(obj, t);
    }

    public Type reqType() {
        return type;
    }

    public boolean estOptionel() {
        return optionel;
    }

    public String reqNom() {
        return nom;
    }

    public String reqDescription() {
        return desc;
    }

    public enum Type {
        STRING,
        INT,
        FLOAT,
        USER,
        ROLE
    }
}
