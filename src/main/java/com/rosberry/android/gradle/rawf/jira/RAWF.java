package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.RAWFPluginExtension;
import com.rosberry.android.gradle.rawf.jira.model.Issue;
import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;

import java.util.List;

public class RAWF {

    public void doWork(RAWFPluginExtension mExtension) {

        JIRAApi jiraApi = new JIRAApi(mExtension.jiraUrl, mExtension.jiraLogin, mExtension.jiraToken);
        List<Issue> issues = jiraApi.getIssues(mExtension.projectKey, mExtension.jiraComponent, mExtension.jiraStatus);
        jiraApi.moveTickets(issues);

        NotificationsCreator notificationsCreator = new NotificationsCreator();
        SlackMessage slackMessage = notificationsCreator.createMessage(issues, mExtension.jiraUrl, mExtension.buildNumber);

        SlackApi api = new SlackApi(mExtension.slackUrl);
        api.call(slackMessage);
    }
}
