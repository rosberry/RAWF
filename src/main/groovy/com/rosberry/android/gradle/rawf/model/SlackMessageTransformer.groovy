/*
 * Copyright © 2018 Rosberry. All rights reserved.
 */
package com.rosberry.android.gradle.rawf.model


import com.rosberry.android.gradle.rawf.utils.GitUtils
import net.gpedro.integrations.slack.SlackAttachment
import net.gpedro.integrations.slack.SlackMessage

/**
 * Created by Alexey Korshun on 08/07/18.
 */
class SlackMessageTransformer {
    private static final String TITLE_DEFAULT = 'New Build: '
    private static final String COLOR_PASSED = 'good'

    static SlackMessage buildSlackMessage(String host, String buildNumber, String message) {

        String title = new StringBuilder().append(TITLE_DEFAULT)
                .append(buildNumber)
                .toString()

        String url = new StringBuilder().append(host)
                .append("/")
                .append("browse/")
                .append(GitUtils.ticketNumber())

        SlackMessage slackMessage = new SlackMessage(title)

        SlackAttachment attachments = new SlackAttachment()

        attachments.setFallback("")
        attachments.setColor(COLOR_PASSED)
        attachments.setAuthorName(GitUtils.lastCommitAuthor())
        attachments.setTitle(message)
        attachments.setTitleLink(url)

        slackMessage.addAttachments(attachments)

        return slackMessage
    }
}
