/*
 * Copyright © 2018 Rosberry. All rights reserved.
 */
package com.rosberry.android.gradle.rawf

import com.rosberry.android.gradle.rawf.jira.RAWF
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState

/**
 * Created by Alexey Korshun on 08/07/18.
 */
class RAWFPlugin implements Plugin<Project> {

    private RAWFPluginProperties pluginProperties
    private RAWF core

    void apply(Project project) {
        pluginProperties = project.extensions.create('rawf', RAWFPluginProperties)

        project.task('releaseNotes') { doLast { createReleaseNotes(project) } }
        project.task('moveTickets') { doLast { moveTickets() } }
        project.task('sendNotification') { doLast { sendNotification() } }

        project.afterEvaluate {
            core = new RAWF(pluginProperties.jiraUrl, pluginProperties.jiraLogin, pluginProperties.jiraToken, pluginProperties.projectKey,
                    pluginProperties.jiraComponent, pluginProperties.jiraFromStatus, pluginProperties.buildNumber,
                    pluginProperties.slackUrl, pluginProperties.errorSlackUrl, pluginProperties.jiraToStatus,
                    pluginProperties.buildInformationUrl)

            if (pluginProperties.enabled) monitorTasksLifecycle(project)
        }
    }

    void monitorTasksLifecycle(Project project) {
        project.getGradle()
                .getTaskGraph()
                .addTaskExecutionListener(
                        new TaskExecutionListener() {
                            @Override
                            void beforeExecute(Task task) {
                            }

                            @Override
                            void afterExecute(Task task, TaskState state) {
                                handleTaskFinished(task, state)
                            }
                        }
                )

        project.gradle.buildFinished { result ->
            if (pluginProperties.enabled && result.failure != null) {
                core.sendErrorMessage()
            }
        }
    }

    void handleTaskFinished(Task task, TaskState state) {
        boolean shouldDoWork = shouldMonitorTask(task, state)
        if (shouldDoWork) {
            core.sendNotificationMessage()
            core.moveTickets()
        }
    }

    boolean shouldMonitorTask(Task task, TaskState state) {
        for (dependentTask in pluginProperties.dependsOnTasks) {
            if (task.getName() == dependentTask && state.didWork) return true
        }
        return false
    }

    void createReleaseNotes(Project project) {
        def message = core.getReleaseNotesMessage()
        new File("$project.projectDir/releaseNotes.txt").text = message
    }

    void moveTickets() {
        core.moveTickets()
    }

    void sendNotification() {
        core.sendNotificationMessage()
    }
}