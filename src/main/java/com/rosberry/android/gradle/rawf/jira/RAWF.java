package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.jira.model.Issue;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

import java.util.List;

public class RAWF {

    private final String jiraUrl;
    private final String jiraLogin;
    private final String jiraToken;
    private final String projectKey;
    private final String jiraComponent;
    private final String jiraFromStatus;
    //maybe we need make it not final.
    private final String buildNumber;
    private final String slackUrl;
    private final String errorSlackUrl;
    private final String jiraToStatus;
    private final String buildInformationUrl;

    public RAWF(String jiraUrl, String jiraLogin, String jiraToken, String projectKey, String jiraComponent,
                String jiraFromStatus, String buildNumber, String slackUrl, String errorSlackUrl, String jiraToStatus,
                String buildInformationUrl) {
        this.jiraUrl = jiraUrl;
        this.jiraLogin = jiraLogin;
        this.jiraToken = jiraToken;
        this.projectKey = projectKey;
        this.jiraComponent = jiraComponent;
        this.jiraFromStatus = jiraFromStatus;
        this.buildNumber = buildNumber;
        this.slackUrl = slackUrl;
        this.errorSlackUrl = errorSlackUrl;
        this.jiraToStatus = jiraToStatus;
        this.buildInformationUrl = buildInformationUrl;
    }

    public void sendNotificationMessage() {
        List<Issue> issues = getIssues(jiraUrl, jiraLogin, jiraToken, projectKey, jiraComponent, jiraFromStatus);

        NotificationsCreator notificationsCreator = new NotificationsCreator();
        SlackMessage slackMessage = notificationsCreator.createMessage(issues, jiraUrl, buildNumber);

        SlackApi api = new SlackApi(slackUrl);
        api.call(slackMessage);
    }

    public String getReleaseNotesMessage() {
        List<Issue> issues = getIssues(jiraUrl, jiraLogin, jiraToken, projectKey, jiraComponent, jiraFromStatus);

        StringBuilder message = new StringBuilder();
        for (Issue issue : issues) {
            message.append(issue.getKey())
                    .append(" : ")
                    .append(issue.getTitle())
                    .append(".\n");
        }

        System.out.println(message.toString());
        return message.toString();
    }

    public void moveTickets() {
        JIRAApi jiraApi = new JIRAApi(jiraUrl, jiraLogin, jiraToken);
        List<Issue> issues = jiraApi.getIssues(projectKey, jiraComponent, jiraFromStatus);
        jiraApi.moveTickets(issues, jiraToStatus);
    }

    public void sendErrorMessage() {
        NotificationsCreator notificationsCreator = new NotificationsCreator();
        SlackMessage slackMessage = notificationsCreator.createErrorMessage(buildInformationUrl);
        SlackApi api = new SlackApi(errorSlackUrl);
        api.call(slackMessage);
    }

    private List<Issue> getIssues(String jiraUrl, String jiraLogin, String jiraToken, String projectKey,
                                  String jiraComponent, String jiraFromStatus) {
        JIRAApi jiraApi = new JIRAApi(jiraUrl, jiraLogin, jiraToken);
        return jiraApi.getIssues(projectKey, jiraComponent, jiraFromStatus);
    }
}
