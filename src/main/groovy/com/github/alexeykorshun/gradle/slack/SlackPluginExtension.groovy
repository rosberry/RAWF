/*
 * Copyright © 2018 Rosberry. All rights reserved.
 */
package com.github.alexeykorshun.gradle.slack

/**
 * Created by Alexey Korshun on 08/07/18.
 */
class SlackPluginExtension {
    String slackUrl
    String jiraUrl
    List<Object> dependsOnTasks
    boolean enabled = true
    String buildNumber

    void dependsOnTasks(Object... paths) {
        this.dependsOnTasks = Arrays.asList(paths)
    }
}