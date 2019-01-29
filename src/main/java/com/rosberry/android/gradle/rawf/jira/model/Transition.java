package com.rosberry.android.gradle.rawf.jira.model;

public class Transition {

    private final String id;
    private final String name;


    public Transition(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static class TransitionBuilder {
        private String id;
        private String name;

        public TransitionBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public TransitionBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Transition createTransition() {
            return new Transition(id, name);
        }
    }
}
