package Discord;

import Adapteurs.MessageEventAdapter;
import Adapteurs.UserAdapter;

import java.util.List;

public class Argument {
    private final ArgumentMetaData metadata;
    private final Object data;

    public Argument(Object p_data, ArgumentMetaData p_metadata) {
        metadata = p_metadata;
        data = p_data;
    }

    public ArgumentMetaData reqMetadata() {
        return metadata;
    }

    /**
     *
     * @return
     */
    public int toInt() {
        return ((Long)data).intValue();
    }

    public Long toLong() {
        return (Long)data;
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

    public Object reqDataRaw() {
        return data;
    }

    /**
     * Utilisé pour le debugging
     * @return
     */
    @Override
    public String toString() {
        String d = "No correct cast";

        switch (metadata.reqType()) {
            case STRING:
                d = (String)data;
                break;
            case INT:
                d = String.valueOf((Long)data);
                break;
        }

        return String.format("Name:%s\nDescription:%s\nType:%s\nOptional?:%s\nContent:%s\n",
                metadata.reqNom(),
                metadata.reqDescription(),
                metadata.reqType(),
                metadata.reqOptionel(),
                d);
    }
}
