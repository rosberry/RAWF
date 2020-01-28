package com.rosberry.android.gradle.rawf.jira;

import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;

public class NetworkClient {

    private final String url;
    private final String login;
    private final String token;

    public NetworkClient(String login, String token, String url) {
        this.login = login;
        this.token = token;
        this.url = url;
    }

    String post(String path, JsonObject data) throws Exception {

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

    String get(String path) throws Exception {
        String urlAddress = this.url + path;
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
