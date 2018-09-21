package net.gpedro.integrations.slack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Developed by gpedro https://github.com/gpedro/slack-webhook
 */

public class SlackAttachment {

    private String fallback;
    private String text;
    private String pretext;
    private String color;
    private String title;
    private String titleLink;
    private String authorName;
    private String authorLink;
    private String authorIcon;
    private List<SlackField> fields;

    public SlackAttachment addFields(SlackField field) {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
        }

        this.fields.add(field);

        return this;
    }

    private boolean isHex(String pair) {
        return pair.matches("^([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$");
    }

    private JsonArray prepareFields() {
        JsonArray data = new JsonArray();
        for (SlackField field : fields) {
            data.add(field.toJson());
        }

        return data;
    }

    public SlackAttachment removeFields(Integer index) {
        if (this.fields != null) {
            this.fields.remove(index);
        }

        return this;
    }

    public SlackAttachment setColor(String color) {
        if (color != null) {
            if (color.charAt(0) == '#') {
                if (!isHex(color.substring(1))) {
                    throw new IllegalArgumentException(
                            "Invalid Hex Color @ SlackAttachment");
                }
            } else if (!color.matches("^(good|warning|danger)$")) {
                throw new IllegalArgumentException(
                        "Invalid PreDefined Color @ SlackAttachment");
            }
        }

        this.color = color;

        return this;
    }

    public SlackAttachment setFallback(String fallback) {
        this.fallback = fallback;

        return this;
    }

    public SlackAttachment setFields(ArrayList<SlackField> fields) {
        this.fields = fields;

        return this;
    }

    public SlackAttachment setPretext(String pretext) {
        this.pretext = pretext;

        return this;
    }

    public SlackAttachment setText(String text) {
        this.text = text;

        return this;
    }

    public SlackAttachment setTitle(String title) {
        this.title = title;
        return this;
    }

    public SlackAttachment setTitleLink(String url) {
        this.titleLink = url;
        return this;
    }

    public SlackAttachment setAuthorName(String name) {
        this.authorName = name;
        return this;
    }

    public SlackAttachment setAuthorIcon(String icon) {
        this.authorIcon = icon;
        return this;
    }

    public SlackAttachment setAuthorLink(String link) {
        this.authorLink = link;
        return this;
    }

    public JsonObject toJson() {
        JsonObject data = new JsonObject();

        if (fallback == null) {
            throw new IllegalArgumentException(
                    "Missing Fallback @ SlackAttachment");
        } else {
            data.addProperty("fallback", fallback);
        }

        if (text != null) {
            data.addProperty("text", text);
        }

        if (pretext != null) {
            data.addProperty("pretext", pretext);
        }

        if (color != null) {
            data.addProperty("color", color);
        }

        if (fields != null && fields.size() > 0) {
            data.add("fields", prepareFields());
        }

        if (title != null) {
            data.addProperty("title", title);
        }

        if (titleLink != null) {
            data.addProperty("title_link", titleLink);
        }

        if (authorName != null) {
            data.addProperty("author_name", authorName);
        }

        if (authorLink != null) {
            data.addProperty("author_link", authorLink);
        }

        if (authorIcon != null) {
            data.addProperty("author_icon", authorIcon);
        }

        return data;
    }

}
