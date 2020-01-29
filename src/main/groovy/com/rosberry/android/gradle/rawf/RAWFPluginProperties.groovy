/*
 * Copyright © 2018 Rosberry. All rights reserved.
 */
package com.rosberry.android.gradle.rawf

/**
 * Created by Alexey Korshun on 08/07/18.
 */
class RAWFPluginProperties {
    public boolean enabled = true
    public String slackUrl = ""
    public String errorSlackUrl = ""
    public String jiraUrl = ""
    public String projectKey = ""
    public String jiraLogin = ""
    public String jiraToken = ""
    public String jiraComponent = ""
    public String jiraFromStatus = ""
    public String jiraToStatus = ""
    public String buildNumber = "1.0"
    public String buildInformationUrl = ""
    public List<Object> dependsOnTasks = Arrays.asList("build")

    public void dependsOnTasks(Object... paths) {
        this.dependsOnTasks = Arrays.asList(paths)
    }
}