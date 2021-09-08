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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.security.auth.login.LoginException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GenerateurGraph {
    public static void main(String[] args) throws IOException, ParseException, LoginException, SQLException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("./config.json"));
        JSONObject config = (JSONObject) obj;

        JDA jda = JDABuilder.createDefault((String) config.get("jetton")).build();
        MetaAdapter metaAdapter = MetaAdapterJDA.construireMetaAdapter(jda);
        Bot bot = new Bot(config, metaAdapter);

        AccessMysql acc = bot.reqAccessMysql();

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

        DirectedAcyclicGraph<String, ConnectionA> g = new DirectedAcyclicGraph<>(ConnectionA.class);

        for (String s : l_string) {
            System.out.printf("%s\n", s);
            g.addVertex(s);
        }

        for (Relation rel : l_rel) {
            g.addEdge(rel.parent, rel.enfant, new ConnectionA(""));
        }

        JGraphXAdapter<String, ConnectionA> graphAdapter =
                new JGraphXAdapter<String, ConnectionA>(g);
        mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("graph.png");
        ImageIO.write(image, "PNG", imgFile);
    }

    static ArrayList<Relation> l_rel;

}

class Relation {
    public String parent;
    public String enfant;
}

class Noeud {
    public String nom;
    public int x;
    public int y;
}

class ConnectionA {
    // class implementation
    // define all constructors and methods you need

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