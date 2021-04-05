package Misc;
import Accesseur.AccessMysql;
import Adapteurs.MetaAdapter;
import Adapteurs.MetaAdapterJDA;
import Discord.Bot;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.SimpleGraph;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class GenerateurGraphNotifTag {
    public GenerateurGraphNotifTag(AccessMysql p_acc) throws SQLException, IOException, ParseException {
        acc = p_acc;
    }

    public InputStream genererGraph() throws SQLException, IOException {
        ResultSet r = acc.reqTousArbeNotif();

        l_rel = new ArrayList<>();
        ArrayList<String> l_string = new ArrayList<String>();

        while (r.next()) {
            Relation rel = new Relation();
            rel.parent = r.getString(1);
            rel.enfant = r.getString(2);
            l_rel.add(rel);
            boolean addParent = true;
            boolean addEnfant = true;

            //Peuple la liste de tous les tags uniques
            for (String s : l_string) {
                if (s.equals(rel.parent)) {
                    addParent = false;
                }
            }
            for (String s : l_string) {
                if (s.equals(rel.enfant)) {
                    addEnfant = false;
                }
            }
            if (addEnfant) {
                l_string.add(rel.enfant);
            }
            if (addParent) {
                l_string.add(rel.parent);
            }
        }

        //Créer le graph
        DirectedAcyclicGraph<String, ConnectionA> g = new DirectedAcyclicGraph<>(ConnectionA.class);

        //Peuple la liste des
        for (String s : l_string) {
            g.addVertex(s);
        }

        for (Relation rel : l_rel) {
            g.addEdge(rel.parent, rel.enfant, new ConnectionA(""));
        }

        JGraphXAdapter<String, ConnectionA> graphAdapter =
                new JGraphXAdapter<String, ConnectionA>(g);
        mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        //File imgFile = new File("graph.png");
        //ImageIO.write(image, "PNG", imgFile);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", os);
        acc.eteindre();
        return new ByteArrayInputStream(os.toByteArray());

    }

    private ArrayList<Relation> l_rel;
    private AccessMysql acc;

}

class Relation {
    public String parent;
    public String enfant;
}

/**
 * objet pour la connection entre les noeuds dans le diagramme.
 */
class ConnectionA {
    public ConnectionA(String label)
    {
        this.label = label;
    }

    public boolean equals(Object obj)
    {
        return false;
    }

    public int hashCode()
    {
        return label.hashCode();
    }

    public String toString() {
        return "";
    }

    String label;
}
