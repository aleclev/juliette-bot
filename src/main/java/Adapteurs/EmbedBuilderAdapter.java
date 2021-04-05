package Adapteurs;

import java.awt.*;

public abstract class EmbedBuilderAdapter {
    public abstract void setTitle(String titre);
    public abstract void addField(String titre, String value, boolean inline);
    public abstract void setThumbnail(String url);
    public abstract void setFooter(String text, String image_url);
    public abstract void setColor(Color color);
    public abstract Object build();
}
