package com.rosberry.android.gradle.rawf.jira;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rosberry.android.gradle.rawf.utils.GitUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;

/**
 * Developed by Alexey Korshun at Rosberry
 */
public class JIRAApi {

    private final static String PATH = "/rest/api/3/issue/";
    private final static String TRANSITION_ID = "61";
    private final static String ID_PROPERTY = "id";
    private final static String TRANSITION_PROPERTY = "transition";
    private final String url;
    private final String login;
    private final String token;

    public JIRAApi(String url, String login, String token) {
        if (login == null || token == null || login.isEmpty() || token.isEmpty()) {
            throw new IllegalArgumentException("Wrong credentials @ JIRAApi");
        }

        this.login = login;
        this.token = token;

        if (url == null) {
            throw new IllegalArgumentException("Missing WebHook URL Configuration @ JIRAApi");
        } else {
            this.url = url;
        }
    }

    public void moveTicket(String ticketNumber) {
        JsonObject transitionData = new JsonObject();
        transitionData.addProperty(ID_PROPERTY, TRANSITION_ID);

        JsonObject data = new JsonObject();
        data.add(TRANSITION_PROPERTY, transitionData);
        String path = PATH + ticketNumber + "/transitions";
        send(path, data);
    }

    public String getTitle(String ticketNumber) {
        if (url.isEmpty()) return GitUtils.lastCommitMessage();

        URL url;
        HttpsURLConnection connection = null;
        try {
            String endUrl = this.url + PATH + ticketNumber;
            System.out.println("Request url: " + endUrl);
            url = new URL(endUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            initConnection(connection);
            addHeaders(connection);

            // Get Response
            connection.connect();
            System.out.println("Response code: " + connection.getResponseCode());

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String rawResponse = br.readLine();
            return getTitleFromRawData(rawResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return GitUtils.lastCommitMessage();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void send(String path, JsonObject message) {
        if (url.isEmpty()) return;
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
            wr.writeBytes(message.toString());
            wr.flush();
            wr.close();

            // Get Response
            connection.connect();
            System.out.println("Response code: " + connection.getResponseCode());

        } catch (Exception e) {
            e.printStackTrace();
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

    private String getTitleFromRawData(String rawData) {
        JsonObject json = new Gson().fromJson(rawData, JsonObject.class);
        return json.get("fields").getAsJsonObject().get("summary").getAsString();
    }
}