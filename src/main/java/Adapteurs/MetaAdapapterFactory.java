package Adapteurs;

//TODO : Ceci n'est pas une factory
public class MetaAdapapterFactory {
    private static MetaAdapter instance;

    public static void setMetaAdapter(MetaAdapter adapter) {
        if (instance == null) {
            instance = adapter;
        }
    }

    public static MetaAdapter reqMetaAdapter() {
        return instance;
    }
}
