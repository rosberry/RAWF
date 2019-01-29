package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.jira.model.Issue;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

import java.util.List;

public class RAWF {

    public void doWork(String jiraUrl, String jiraLogin, String jiraToken, String projectKey, String jiraComponent,
                       String jiraFromStatus, String buildNumber, String slackUrl, String jiraToStatus) {

        JIRAApi jiraApi = new JIRAApi(jiraUrl, jiraLogin, jiraToken);
        List<Issue> issues = jiraApi.getIssues(projectKey, jiraComponent, jiraFromStatus);
        jiraApi.moveTickets(issues, jiraToStatus);

        NotificationsCreator notificationsCreator = new NotificationsCreator();
        SlackMessage slackMessage = notificationsCreator.createMessage(issues, jiraUrl, buildNumber);

        SlackApi api = new SlackApi(slackUrl);
        api.call(slackMessage);
    }

    public String getReleaseNotesMessage(String jiraUrl, String jiraLogin, String jiraToken, String projectKey,
                                         String jiraComponent, String jiraStatus) {
        JIRAApi jiraApi = new JIRAApi(jiraUrl, jiraLogin, jiraToken);
        List<Issue> issues = jiraApi.getIssues(projectKey, jiraComponent, jiraStatus);

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
}
