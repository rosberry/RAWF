/*
 * Copyright © 2018 Rosberry. All rights reserved.
 */
package com.rosberry.android.gradle.rawf

/**
 * Created by Alexey Korshun on 08/07/18.
 */
class RAWFPluginExtension {
    boolean enabled = true
    String slackUrl = ""
    String jiraUrl = ""
    String projectKey = ""
    String jiraLogin = ""
    String jiraToken = ""
    String jiraComponent = ""
    String jiraStatus = ""
    String buildNumber = "1.0"
    List<Object> dependsOnTasks = Arrays.asList("build")

    void dependsOnTasks(Object... paths) {
        this.dependsOnTasks = Arrays.asList(paths)
    }
}