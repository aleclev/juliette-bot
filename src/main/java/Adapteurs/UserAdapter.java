package Adapteurs;

public abstract class UserAdapter {
    public abstract void messagePrive(String message);
    public abstract void messagePrive(EmbedBuilderAdapter eb);

    public abstract boolean estEnLigne();

    //getters
    public abstract String reqNom();
    public abstract Object reqOriginal();
    public abstract String reqMention();
    public abstract long reqId();
}
