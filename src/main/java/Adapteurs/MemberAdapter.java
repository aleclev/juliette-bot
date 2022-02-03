package Adapteurs;

public abstract class MemberAdapter {
    public abstract boolean estEnLigne();
    public abstract UserAdapter reqUser();
    public abstract boolean notNull();
    public abstract void ajouterRole(long role_id);
    public abstract String reqNick();
}
