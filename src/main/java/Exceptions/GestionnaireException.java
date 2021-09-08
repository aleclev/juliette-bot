package Exceptions;

public class GestionnaireException {
    private static GestionnaireException instance = null;

    private GestionnaireException() {}

    public static GestionnaireException reqGestionnaireException() {
        if (instance == null) {
            instance = new GestionnaireException();
        }
        return instance;
    }

    public String reqMessageErreur(Exception e) {
        if (e instanceof APIException) {
            return ((APIException)e).reqMessage();
        }

        return "Error! This error is classified as an API error. No other information is provided.";
    }
}
