package com.github.alexeykorshun.gradle.slack.jira;

import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.net.URL;
import java.util.Base64;

/**
 * Developed by Alexey Korshun at Rosberry
 */
public class JIRAApi {

    private final static String PATH = "/rest/api/2/issue/";
    private final static String TRANSITION_ID = "61";
    private final static String ID_PROPERTY = "id";
    private final static String TRANSITION_PROPERTY = "transition";
    private String url;

    public JIRAApi(String url) {
        if (url == null) {
            throw new IllegalArgumentException("Missing WebHook URL Configuration @ JIRAApi");
        } else if (!url.contains("atlassian.net")) {
            throw new IllegalArgumentException("Invalid Service URL.");
        }

        this.url = url;
    }

    public void call(String ticketNumber) {
        JsonObject transitionData = new JsonObject();
        transitionData.addProperty(ID_PROPERTY, TRANSITION_ID);

        JsonObject data = new JsonObject();
        data.add(TRANSITION_PROPERTY, transitionData);
        send(ticketNumber, data);
    }

    private void send(String ticketNumber, JsonObject message) {
        URL url;
        HttpsURLConnection connection = null;
        try {
            // Create connection
            String endUrl = this.url + PATH + ticketNumber + "/transitions";
            System.out.println("Request url: " + endUrl);
            url = new URL(endUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            String userpass = "alexey.korshun@rosberry.com" + ":" + "GNs2mV6X0Qm5S9PVTbp1FA29";
            String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userpass.getBytes()));
            connection.setRequestProperty ("Authorization", basicAuth);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");

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
}