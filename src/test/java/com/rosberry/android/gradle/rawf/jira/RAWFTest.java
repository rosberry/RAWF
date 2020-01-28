package com.rosberry.android.gradle.rawf.jira;

import org.junit.Before;
import org.junit.Test;

public class RAWFTest {

    private static final String slackUrl = "https://hooks.slack.com/services/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";
    private static final String jiraUrl = "https://rosberry.atlassian.net";
    private static final String projectKey = "ALCO";
    private static final String jiraLogin = "alexey.korshun@rosberry.com";
    private static final String jiraToken = "GNs2mV6X0Qm5S9PVTbp1FA29";
    private static final String jiraComponent = "android";
    private static final String jiraFromStatus = "ready";
    private static final String buildNumber = "1.0";
    private static final String jiraToStatus = "test build";

    private RAWF rawf;

    @Before
    public void init() {
        rawf = new RAWF(jiraUrl, jiraLogin, jiraToken, projectKey, jiraComponent, jiraFromStatus, buildNumber, slackUrl,
                slackUrl, jiraToStatus);
    }

    @Test
    public void sendMessage() {
        rawf.sendNotificationMessage();
    }

    @Test
    public void getReleaseNotesMessage() {
        rawf.getReleaseNotesMessage();
    }

    @Test
    public void sendErrorMessage() {
        rawf.sendErrorMessage();
    }

    @Test
    public void moveTickets() {
        rawf.moveTickets();
    }
}