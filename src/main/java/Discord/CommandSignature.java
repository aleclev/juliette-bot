package Discord;

import java.util.ArrayList;

public class CommandSignature {
    private ArrayList<ArgumentMetaData> l_args;

    public CommandSignature() {
        l_args = new ArrayList<>();
    }

    public ArrayList<ArgumentMetaData> reqMetadata() {
        return l_args;
    }

    /**
     * Ajoute les metadonnées à la liste sous conditions qu'aucun argument optionel précède un argyment non-optionel.
     * @param metadata
     */
    public void ajouterMetadata(ArgumentMetaData metadata) {
        int i = 0;

        while (i < l_args.size()) {
            if (l_args.get(i).reqOptionel()) {
                i++;
                break;
            }
            i++;
        }

        while (i < l_args.size()) {
            if (!l_args.get(i).reqOptionel()) {
                System.out.printf("Argument optionel après un argument non optionel. %s", metadata);
                return;
            }
        }

        l_args.add(metadata);
    }
}
