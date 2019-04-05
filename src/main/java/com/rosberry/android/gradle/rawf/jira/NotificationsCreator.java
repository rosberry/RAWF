package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.jira.model.Issue;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackMessage;

import javax.annotation.Nullable;
import java.util.List;

public class NotificationsCreator {

    private static final String TITLE_DEFAULT = "New Build: ";
    private static final String COLOR_PASSED = "good";
    private static final String COLOR_BAD = "danger";
    private static final String BUG = "bug";

    public SlackMessage createMessage(List<Issue> issues, String jiraHost, String buildNumber) {
        String title = TITLE_DEFAULT + buildNumber;
        SlackMessage slackMessage = new SlackMessage(title);

        SlackAttachment featureAttachment = makeFeatureAttachment(issues, jiraHost);
        SlackAttachment bugAttachment = makeBugAttachment(issues, jiraHost);

        if (featureAttachment != null) slackMessage.addAttachments(featureAttachment);
        if (bugAttachment != null) slackMessage.addAttachments(bugAttachment);

        return slackMessage;
    }

    @Nullable
    private SlackAttachment makeFeatureAttachment(List<Issue> issues, String jiraHost) {
        StringBuilder message = new StringBuilder();
        for (Issue issue : issues) {
            if (!issue.getType().toLowerCase().equals(BUG)) {
                String url = generateTicketUrl(jiraHost, issue);
                if (message.length() > 0) message.append("\n");
                appendMessageDescription(message, issue, url);
            }
        }

        String preMessage = ":putin: *Features*:\n";
        if (message.length() == 0) return null;
        return buildAttachment(preMessage + message.toString(), true);
    }

    @Nullable
    private SlackAttachment makeBugAttachment(List<Issue> issues, String jiraHost) {
        StringBuilder message = new StringBuilder();
        for (Issue issue : issues) {
            if (issue.getType().toLowerCase().equals(BUG)) {
                String url = generateTicketUrl(jiraHost, issue);
                if (message.length() > 0) message.append("\n");
                appendMessageDescription(message, issue, url);
            }
        }

        String preMessage = ":shit: *Bugs*:\n";
        if (message.length() == 0) return null;
        return buildAttachment(preMessage + message.toString(), false);
    }

    private void appendMessageDescription(StringBuilder message, Issue issue, String url) {
        message.append(":heavy_check_mark: ")
                .append("<")
                .append(url)
                .append("|")
                .append(issue.getKey())
                .append(">: ")
                .append(issue.getTitle());
    }

    private String generateTicketUrl(String jiraHost, Issue issue) {
        return jiraHost +
                "/" +
                "browse/" +
                issue.getKey();
    }

    private SlackAttachment buildAttachment(String message, boolean isFeature) {
        SlackAttachment attachments = new SlackAttachment();

        attachments.setFallback("");
        if (isFeature) attachments.setColor(COLOR_PASSED);
        else attachments.setColor(COLOR_BAD);
        attachments.setText(message);

        return attachments;
    }

}
