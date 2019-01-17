/*
 * Copyright © 2018 Rosberry. All rights reserved.
 */
package com.rosberry.android.gradle.rawf

/**
 * Created by Alexey Korshun on 08/07/18.
 */
class RAWFPluginExtension {
    String slackUrl = ""
    String jiraUrl = ""
    List<Object> dependsOnTasks = Arrays.asList("build")
    boolean enabled = true
    String buildNumber = "1.0"
    String login = ""
    String token = ""

    void dependsOnTasks(Object... paths) {
        this.dependsOnTasks = Arrays.asList(paths)
    }
}