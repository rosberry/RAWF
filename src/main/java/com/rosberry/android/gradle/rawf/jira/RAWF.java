package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.jira.model.Issue;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

import java.util.List;

public class RAWF {

    public void doWork(String jiraUrl, String jiraLogin, String jiraToken, String projectKey, String jiraComponent, String jiraStatus,
                       String buildNumber, String slackUrl) {

        JIRAApi jiraApi = new JIRAApi(jiraUrl, jiraLogin, jiraToken);
        List<Issue> issues = jiraApi.getIssues(projectKey, jiraComponent, jiraStatus);
        jiraApi.moveTickets(issues);

        NotificationsCreator notificationsCreator = new NotificationsCreator();
        SlackMessage slackMessage = notificationsCreator.createMessage(issues, jiraUrl, buildNumber);

        SlackApi api = new SlackApi(slackUrl);
        api.call(slackMessage);
    }
}
