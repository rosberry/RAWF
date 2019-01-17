/*
 * Copyright © 2018 Rosberry. All rights reserved.
 */
package com.rosberry.android.gradle.rawf.utils

/**
 * Created by Alexey Korshun on 08/07/18.
 */
class GitUtils {

    public static String branchName() {
        def workingBranch = "git rev-parse --abbrev-ref HEAD".execute().text.trim()
        return workingBranch
    }

    public static String lastCommitAuthor() {
        def lastCommitAuthor = "git log -1 --pretty=%an".execute().text.trim()
        return lastCommitAuthor
    }

    public static String lastCommitMessage() {
        def message = "git log -1 --pretty=%B".execute().text.trim()
        return message
    }

    public static String ticketNumber() {
        def commit = lastCommitMessage()
        def (ticketNumber, _) = commit.tokenize( ':' )
        return ticketNumber
    }
}
