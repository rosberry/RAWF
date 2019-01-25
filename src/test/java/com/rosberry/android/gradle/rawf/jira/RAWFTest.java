package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.RAWFPluginExtension;
import org.junit.Test;

public class RAWFTest {

    private static final String slackUrl = "https://hooks.slack.com/services/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";
    private static final String jiraUrl = "";
    private static final String projectKey = "ALCO";
    private static final String jiraLogin = "alexey.korshun@rosberry.com";
    private static final String jiraToken = "GNs2mV6X0Qm5S9PVTbp1FA29";
    private static final String jiraComponent = "android";
    private static final String jiraStatus = "ready";
    private static final String buildNumber = "1.0";

    @Test
    public void doWork() {
        RAWFPluginExtension extension = new RAWFPluginExtension();
        extension.slackUrl = slackUrl;
        extension.jiraUrl = jiraUrl;
        extension.projectKey = projectKey;
        extension.jiraLogin = jiraLogin;
        extension.jiraToken = jiraToken;
        extension.jiraComponent = jiraComponent;
        extension.jiraStatus = jiraStatus;
        extension.buildNumber = buildNumber;

        new RAWF().doWork(extension);
    }
}