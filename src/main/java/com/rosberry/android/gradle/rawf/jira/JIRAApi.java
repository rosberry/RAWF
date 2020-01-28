package com.rosberry.android.gradle.rawf.jira;

import com.google.gson.JsonObject;
import com.rosberry.android.gradle.rawf.jira.model.Issue;
import com.rosberry.android.gradle.rawf.jira.model.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Developed by Alexey Korshun at Rosberry
 */
public class JIRAApi {

    private final static String BASE_PATH = "/rest/api/3/";
    private final static String ISSUE_PATH = "issue";
    private final static String SEARCH_PATH = "search";
    private final static String TRANSITION_PATH = "transitions";
    private final static String SEPARATOR = "/";
    private final static String AND = "%20AND%20";
    private final static String QUOTE_MARK = "%27";

    private final static String ID_PROPERTY = "id";
    private final static String TRANSITION_PROPERTY = "transition";

    private final JiraModelParser jiraModelParser;
    private final NetworkClient networkClient;

    public JIRAApi(String url, String login, String token) {

        if (login == null || token == null || url == null) {
            throw new IllegalArgumentException("Wrong credentials @ JIRAApi");
        }
        networkClient = new NetworkClient(login, token, url);
        this.jiraModelParser = new JiraModelParser();
    }

    public void moveTickets(List<Issue> issues, String toStatus) {
        for (Issue issue : issues) {
            moveTicket(issue.getKey(), toStatus);
        }
    }

    public void moveTicket(String ticketNumber, String toStatus) {
        List<Transition> transitions = getTransitions(ticketNumber);
        for (Transition transition : transitions) {
            if (Objects.equals(transition.getName(), toStatus)) {
                JsonObject transitionData = new JsonObject();
                transitionData.addProperty(ID_PROPERTY, transition.getId());

                JsonObject data = new JsonObject();
                data.add(TRANSITION_PROPERTY, transitionData);
                String path = BASE_PATH + ISSUE_PATH + SEPARATOR + ticketNumber + SEPARATOR + TRANSITION_PATH;

                try {
                    networkClient.post(path, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getTitle(String ticketNumber) {
        try {
            String path = BASE_PATH + ISSUE_PATH + SEPARATOR + ticketNumber;

            String responseString = networkClient.get(path);
            String title = jiraModelParser.getTitleFromIssueRawString(responseString);
            System.out.println(title);
            return title;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<Issue> getIssues(String projectKey, String componentName, String status) {
        try {
            StringBuilder jql = new StringBuilder();
            if (projectKey != null && !projectKey.isEmpty()) {
                if (jql.length() != 0) jql.append(AND);
                jql.append("project=").append(QUOTE_MARK).append(projectKey).append(QUOTE_MARK);
            }
            if (componentName != null && !componentName.isEmpty()) {
                if (jql.length() != 0) jql.append(AND);
                jql.append("component=").append(componentName);
            }
            if (status != null && !status.isEmpty()) {
                if (jql.length() != 0) jql.append(AND);
                jql.append("status=").append(status);
            }

            if (jql.length() != 0) jql.append(AND);
            jql.append("sprint%20in%20openSprints%28%29");

            String issueUrl = BASE_PATH + SEARCH_PATH + "?" + "jql=" + jql.toString();

            String data = networkClient.get(issueUrl);
            return jiraModelParser.getIssueListFromSearchRawString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Transition> getTransitions(String ticketKey) {
        try {
            String issueUrl = BASE_PATH + ISSUE_PATH + SEPARATOR + ticketKey + SEPARATOR + TRANSITION_PATH;

            String data = networkClient.get(issueUrl);
            return jiraModelParser.getTransitionListFromResponse(data);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}