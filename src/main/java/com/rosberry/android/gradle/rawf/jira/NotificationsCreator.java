package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.jira.model.Issue;
import net.gpedro.integrations.slack.SlackAttachment;
import net.gpedro.integrations.slack.SlackMessage;

import java.util.List;

public class NotificationsCreator {

    private static final String TITLE_DEFAULT = "New Build: ";
    private static final String COLOR_PASSED = "good";
    private static final String BUG = "bug";

    public SlackMessage createMessage(List<Issue> issues, String jiraHost, String buildNumber) {

        boolean hasIssue = false;
        boolean hasFeature = false;

        String title = TITLE_DEFAULT + buildNumber;
        SlackMessage slackMessage = new SlackMessage(title);
        StringBuilder message = new StringBuilder();

        for (Issue issue : issues) {
            if (issue.getType().toLowerCase().equals(BUG)) {
                hasIssue = true;
            } else {
                hasFeature = true;
            }

            String url = generateTicketUrl(jiraHost, issue);
            if (message.length() > 0) message.append("\n");
            appendMessageDescription(message, issue, url);
        }

        String preMessage = getAttachmentTitle(hasIssue, hasFeature);

        SlackAttachment attachments = buildAttachment(preMessage + message.toString());
        slackMessage.addAttachments(attachments);

        return slackMessage;
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

    private String getAttachmentTitle(boolean hasIssue, boolean hasFeature) {
        if (!hasFeature && !hasIssue) return "";
        String preMessage = "";
        if (hasFeature) {
            preMessage = preMessage + ":putin: *Features*";
        }

        if (hasIssue) {
            if (!preMessage.isEmpty()) preMessage = preMessage + " ";
            preMessage = preMessage + ":shit: *Bugs*";
        }

        preMessage = preMessage + ":\n";
        return preMessage;
    }

    private SlackAttachment buildAttachment(String message) {
        SlackAttachment attachments = new SlackAttachment();

        attachments.setFallback("");
        attachments.setColor(COLOR_PASSED);
        attachments.setText(message);

        return attachments;
    }

}
