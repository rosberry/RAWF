package net.gpedro.integrations.slack;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Developed by gpedro https://github.com/gpedro/slack-webhook
 */

public class SlackApi {

    private String service;

    public SlackApi(String service) {
        if (service == null) {
            throw new IllegalArgumentException(
                    "Missing WebHook URL Configuration @ SlackApi");
        } else if (!service.isEmpty() && !service.startsWith("https://hooks.slack.com/services/")) {
            throw new IllegalArgumentException(
                    "Invalid Service URL. WebHook URL Format: https://hooks.slack.com/services/{id_1}/{id_2}/{token}");
        } else {
            this.service = service;
        }
    }

    /**
     * Prepare Message and Send to request
     *
     * @param message message to send
     */
    public void call(SlackMessage message) {
        if (message != null) {
            this.send(message.prepare());
        }
    }

    /**
     * Send request to WebService
     *
     * @param message slack message as json
     */
    private void send(JsonObject message) {
        if (service.isEmpty()) return;
        URL url;
        HttpURLConnection connection = null;
        try {
            // Create connection
            url = new URL(this.service);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            String payload = "payload=" + URLEncoder.encode(message.toString(), "UTF-8");

            // Send request
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(payload);
            wr.flush();
            wr.close();

            // Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            System.out.println(response.toString());
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

}
