package Accesseur;

/**
 * Classe pour accéder à l'API de bungie.
 */
public abstract class AccessBungie {
    /**
     * Prends un steam id et retourne le nom bungie du compte correspondant.
     * @param steam_id LE steam id du compte recherché.
     * @return BungieName
     */
    public abstract String reqBungieNameFromSteamID(long steam_id);
}
