package com.rosberry.android.gradle.rawf.jira;

import com.google.gson.JsonObject;
import com.rosberry.android.gradle.rawf.jira.model.Issue;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Developed by Alexey Korshun at Rosberry
 */
public class JIRAApi {

    private final static String TRANSITION_ID = "61"; //todo: make dynamic

    private final static String BASE_PATH = "/rest/api/3/";
    private final static String ISSUE_PATH = "issue";
    private final static String SEARCH_PATH = "search";
    private final static String TRANSITION_PATH = "transitions";
    private final static String SEPARATOR = "/";
    private final static String AND = "%20AND%20";

    private final static String ID_PROPERTY = "id";
    private final static String TRANSITION_PROPERTY = "transition";

    private final String url;
    private final String login;
    private final String token;

    private final JiraModelParser jiraModelParser;

    public JIRAApi(String url, String login, String token) {

        if (login == null || token == null || url == null) {
            throw new IllegalArgumentException("Wrong credentials @ JIRAApi");
        }

        this.login = login;
        this.token = token;
        this.url = url;

        this.jiraModelParser = new JiraModelParser();
    }

    public void moveTickets(List<Issue> issues) {
        if (url.isEmpty()) return;

        for (Issue issue : issues) {
            moveTicket(issue.getKey());
        }
    }

    public void moveTicket(String ticketNumber) {
        if (url.isEmpty()) return;

        JsonObject transitionData = new JsonObject();
        transitionData.addProperty(ID_PROPERTY, TRANSITION_ID);

        JsonObject data = new JsonObject();
        data.add(TRANSITION_PROPERTY, transitionData);
        String path = BASE_PATH + ISSUE_PATH + SEPARATOR + ticketNumber + SEPARATOR + TRANSITION_PATH;

        try {
            sendPost(path, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTitle(String ticketNumber) {
        if (url.isEmpty()) return "";

        try {
            String issueUrl = url + BASE_PATH + ISSUE_PATH + SEPARATOR + ticketNumber;

            String responseString = sendGet(issueUrl);
            String title = jiraModelParser.getTitleFromIssueRawString(responseString);
            System.out.println(title);
            return title;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<Issue> getIssues(String projectKey, String componentName, String status) {
        if (url.isEmpty()) return new ArrayList<>();

        try {
            StringBuilder jql = new StringBuilder();
            if (projectKey != null && !projectKey.isEmpty()) {
                if (jql.length() != 0) jql.append(AND);
                jql.append("project=").append(projectKey);
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
            jql.append("sprint%20in%20openSprints()");

            String issueUrl = url + BASE_PATH + SEARCH_PATH + "?" + "jql=" + jql.toString();

            String data = sendGet(issueUrl);
            return jiraModelParser.getIssueListFromSearchRawString(data);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String sendPost(String path, JsonObject data) throws Exception {

        URL url;
        HttpsURLConnection connection = null;
        try {
            // Create connection
            String endUrl = this.url + path;
            System.out.println("Request url: " + endUrl);
            url = new URL(endUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            initConnection(connection);
            addHeaders(connection);

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(data.toString());
            wr.flush();
            wr.close();

            // Get Response
            connection.connect();
            System.out.println("Response code: " + connection.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return br.readLine();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String sendGet(String urlAddress) throws Exception {
        HttpsURLConnection connection = null;
        try {
            System.out.println("Request url: " + urlAddress);

            URL url = new URL(urlAddress);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            initConnection(connection);
            addHeaders(connection);

            connection.connect();
            System.out.println("Response code: " + connection.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return br.readLine();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void initConnection(HttpsURLConnection connection) {
        connection.setConnectTimeout(5000);
        connection.setUseCaches(false);
        connection.setDoInput(true);
    }

    private void addHeaders(HttpsURLConnection connection) {
        String userpass = login + ":" + token;
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
        connection.setRequestProperty("Authorization", basicAuth);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
    }
}