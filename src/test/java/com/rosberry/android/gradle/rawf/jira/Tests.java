package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.jira.model.Issue;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Tests {

    private static final String URL = "https://rosberry.atlassian.net";
    private static final String LOGIN = "ci@rosberry.com";
    private static final String TOKEN = "heuYUc4DoigeZBDtuktU6467";
    private static final String PROJECT_KEY = "LIV";
    private static final String PROJECT_COMPONENT = "Android";
    private static final String STATUS = "Ready";
    private static final String WB_1262 = "WB-1262";
    private static final String TICKET_TITLE = "Напиток затреканный во время подсказок не влияет на баланс.";
    private static final String SLACK_URL = "https://hooks.slack.com/services/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";
    private static final String BUILD_NUMBER = "1.0";
    private static final String TEST_BUILD = "Test Build";

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
        String response = api.getTitle(WB_1262);
        assertEquals(response, TICKET_TITLE);
    }

    @Test
    public void sendMessage() {
        JIRAApi jiraApi = new JIRAApi(URL, LOGIN, TOKEN);

        List<Issue> response = jiraApi.getIssues(PROJECT_KEY, PROJECT_COMPONENT, STATUS);

        NotificationsCreator creator = new NotificationsCreator();
        SlackMessage slackMessage = creator.createMessage(response, URL, BUILD_NUMBER);
        SlackApi api = new SlackApi(SLACK_URL);
        api.call(slackMessage);
        assertTrue(true);
    }

    @Test
    public void sendErrorMessage() {
        NotificationsCreator creator = new NotificationsCreator();
        SlackMessage slackMessage = creator.createErrorMessage(URL);
        SlackApi api = new SlackApi(SLACK_URL);
        api.call(slackMessage);
        assertTrue(true);
    }

    @Test
    public void moveTickets() {
        JIRAApi jiraApi = new JIRAApi(URL, LOGIN, TOKEN);
        List<Issue> issues = jiraApi.getIssues(PROJECT_KEY, PROJECT_COMPONENT, STATUS);
        jiraApi.moveTickets(issues, TEST_BUILD);
    }

    @Test
    public void getTransitions() {
        JIRAApi jiraApi = new JIRAApi(URL, LOGIN, TOKEN);
        System.out.println(jiraApi.getTransitions(WB_1262));
    }
}