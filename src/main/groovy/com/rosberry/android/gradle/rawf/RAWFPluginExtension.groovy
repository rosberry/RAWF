/*
 * Copyright © 2018 Rosberry. All rights reserved.
 */
package com.rosberry.android.gradle.rawf

/**
 * Created by Alexey Korshun on 08/07/18.
 */
class RAWFPluginExtension {
    public boolean enabled = true
    public String slackUrl = ""
    public String jiraUrl = ""
    public String projectKey = ""
    public String jiraLogin = ""
    public String jiraToken = ""
    public String jiraComponent = ""
    public String jiraStatus = ""
    public String buildNumber = "1.0"
    public List<Object> dependsOnTasks = Arrays.asList("build")

    public void dependsOnTasks(Object... paths) {
        this.dependsOnTasks = Arrays.asList(paths)
    }
}