package com.rosberry.android.gradle.rawf.jira;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.rosberry.android.gradle.rawf.jira.model.Issue;

import java.util.ArrayList;
import java.util.List;

class JiraModelParser {

    private final Gson gson;

    public JiraModelParser() {
        this.gson = new Gson();
    }

    String getTitleFromIssueRawString(String rawData) {
        JsonObject json = gson.fromJson(rawData, JsonObject.class);
        return json.get("fields").getAsJsonObject().get("summary").getAsString();
    }

    List<Issue> getIssueListFromSearchRawString(String data) {
        List<Issue> issues = new ArrayList<>();

        JsonObject jsonResponse = gson.fromJson(data, JsonObject.class);
        if (jsonResponse.has("issues")) {
            JsonArray jsonIssues = jsonResponse.getAsJsonArray("issues");
            for (int i = 0; i < jsonIssues.size(); i++) {
                JsonObject jsonIssue = jsonIssues.get(i).getAsJsonObject();

                String key = "";
                String title = "";
                String type = "";

                if (jsonIssue.has("key")) {
                    key = jsonIssue.get("key").getAsString();
                }

                if (jsonIssue.has("fields")) {
                    JsonObject jsonFields = jsonIssue.getAsJsonObject("fields");

                    if (jsonFields.has("summary")) {
                        title = jsonFields.get("summary").getAsString();
                    }

                    if (jsonFields.has("issuetype")) {
                        JsonObject jsonIssueType = jsonFields.get("issuetype").getAsJsonObject();
                        if (jsonIssueType.has("name")) {
                            type = jsonIssueType.get("name").getAsString();
                        }
                    }
                }

                Issue issue = new Issue.Builder()
                        .setKey(key)
                        .setTitle(title)
                        .setType(type)
                        .createIssue();

                issues.add(issue);
            }
        }
        return issues;
    }
}
