package Adapteurs;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class EmbedBuilderAdapterJDA extends EmbedBuilderAdapter {
    public EmbedBuilderAdapterJDA() {
        eb = new EmbedBuilder();
    }

    @Override
    public void setTitle(String titre) {
        eb.setTitle(titre);
    }

    @Override
    public void addField(String titre, String value, boolean inline) {
        eb.addField(titre, value, inline);
    }

    @Override
    public void setThumbnail(String url) {
        eb.setThumbnail(url);
    }

    @Override
    public void setFooter(String text, String image_url) {
        if (image_url != null) {
            eb.setFooter(text, image_url);
        } else {
            eb.setFooter(text);
        }
    }

    @Override
    public void setColor(Color color) {
        eb.setColor(color);
    }

    public Object build() {
        if (messageEmbed == null) {
            messageEmbed = eb.build();
            return messageEmbed;
        }
        else {
            return messageEmbed;
        }
    }

    public MessageEmbed messageEmbed;
    private final EmbedBuilder eb;
}
