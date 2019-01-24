package com.rosberry.android.gradle.rawf.jira.model;

public class Issue {

    private final String key;
    private final String title;
    private final String type;

    public Issue(String key, String title, String type) {

        this.key = key;
        this.title = title;
        this.type = type;
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
        private String key;
        private String title;
        private String type;

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
            return new Issue(key, title, type);
        }
    }
}
