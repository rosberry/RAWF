package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.jira.model.Issue;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JIRAApiTest {

    private final static String URL = "https://rosberry.atlassian.net";
    private final static String LOGIN = "alexey.korshun@rosberry.com";
    private final static String TOKEN = "GNs2mV6X0Qm5S9PVTbp1FA29";
    private final static String PROJECT_KEY = "WB";
    private final static String PROJECT_COMPONENT = "android";
    private final static String STATUS = "doing";

    @Test
    public void getIssues() {
        JIRAApi api = new JIRAApi(URL, LOGIN, TOKEN);
        List<Issue> response = api.getIssues(PROJECT_KEY, PROJECT_COMPONENT, STATUS);
        System.out.println(response);
        assertTrue(true);
    }

    @Test
    public void getTitle() {
        JIRAApi api = new JIRAApi(URL, LOGIN, TOKEN);
        String response = api.getTitle("WB-1262");
        assertEquals(response, "Напиток затреканный во время подсказок не влияет на баланс.");
    }
}