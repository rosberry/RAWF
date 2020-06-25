package com.rosberry.android.gradle.rawf.jira;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rosberry.android.gradle.rawf.jira.model.Issue;
import com.rosberry.android.gradle.rawf.jira.model.Transition;

import java.util.ArrayList;
import java.util.List;

class JiraModelParser {

    private static final String TRANSITIONS = "transitions";
    private static final String FIELDS = "fields";
    private static final String SUMMARY = "summary";
    private static final String ISSUES = "issues";
    private static final String KEY = "key";
    private static final String ISSUE_TYPE = "issuetype";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PARENT = "parent";
    private final Gson gson;

    public JiraModelParser() {
        this.gson = new Gson();
    }

    String getTitleFromIssueRawString(String rawData) {
        JsonObject json = gson.fromJson(rawData, JsonObject.class);
        return json.get(FIELDS).getAsJsonObject().get(SUMMARY).getAsString();
    }

    List<Issue> getIssueListFromSearchRawString(String data) {
        List<Issue> issues = new ArrayList<>();

        JsonObject jsonResponse = gson.fromJson(data, JsonObject.class);
        if (jsonResponse.has(ISSUES)) {
            JsonArray jsonIssues = jsonResponse.getAsJsonArray(ISSUES);
            for (int i = 0; i < jsonIssues.size(); i++) {
                JsonObject jsonIssue = jsonIssues.get(i).getAsJsonObject();

                int id = 0;
                String key = "";
                String title = "";
                String type = "";

                if (jsonIssue.has(KEY)) {
                    key = jsonIssue.get(KEY).getAsString();
                }

                if (jsonIssue.has(FIELDS)) {
                    JsonObject jsonFields = jsonIssue.getAsJsonObject(FIELDS);

                    if (jsonFields.has(SUMMARY)) {
                        if (jsonFields.has(PARENT)) {
                            StringBuilder titleBuilder = new StringBuilder();
                            title = titleBuilder
                                    .append("_")
                                    .append(jsonFields.getAsJsonObject(PARENT).getAsJsonObject(FIELDS).get(SUMMARY).getAsString())
                                    .append("_")
                                    .append(" -> ")
                                    .append(jsonFields.get(SUMMARY).getAsString())
                                    .toString();
                        } else {
                            title = jsonFields.get(SUMMARY).getAsString();
                        }
                    }

                    if (jsonFields.has(ISSUE_TYPE)) {
                        JsonObject jsonIssueType = jsonFields.get(ISSUE_TYPE).getAsJsonObject();
                        if (jsonIssueType.has(NAME)) {
                            type = jsonIssueType.get(NAME).getAsString();
                        }
                        if (jsonIssueType.has(ID)) {
                            id = jsonIssueType.get(ID).getAsInt();
                        }
                    }
                }

                Issue issue = new Issue.Builder()
                        .setId(id)
                        .setKey(key)
                        .setTitle(title)
                        .setType(type)
                        .createIssue();

                issues.add(issue);
            }
        }
        return issues;
    }

    List<Transition> getTransitionListFromResponse(String data) {
        List<Transition> transitions = new ArrayList<>();

        try {
            JsonObject responseObject = gson.fromJson(data, JsonObject.class);

            if (responseObject.has(TRANSITIONS)) {
                JsonArray transitionsArray = responseObject.get(TRANSITIONS).getAsJsonArray();

                for (int i = 0; i < transitionsArray.size(); i++) {

                    JsonObject transitionObject = transitionsArray.get(i).getAsJsonObject();
                    if (transitionObject.has(ID) && transitionObject.has(NAME)) {
                        Transition transition = new Transition.TransitionBuilder()
                                .setId(transitionObject.get(ID).getAsString())
                                .setName(transitionObject.get(NAME).getAsString())
                                .createTransition();

                        transitions.add(transition);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return transitions;
    }
}
