package com.rosberry.android.gradle.rawf.jira.model;

public class Issue {

    public static final int ID_BUG = 1;
    public static final int ID_SUB_BUG = 11015;

    private final int id;
    private final String key;
    private final String title;
    private final String type;

    public Issue(int id, String key, String title, String type) {
        this.id = id;
        this.key = key;
        this.title = title;
        this.type = type;
    }

    public int getId () {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "key='" + key + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public static class Builder {
        private int id;
        private String key;
        private String title;
        private String type;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Issue createIssue() {
            return new Issue(id, key, title, type);
        }
    }
}
