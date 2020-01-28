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

    private RAWFPluginExtension mExtension
    private RAWF core

    void apply(Project project) {
        mExtension = project.extensions.create('rawf', RAWFPluginExtension)
        core = new RAWF(mExtension.jiraUrl, mExtension.jiraLogin, mExtension.jiraToken, mExtension.projectKey,
                mExtension.jiraComponent, mExtension.jiraFromStatus, mExtension.buildNumber, mExtension.slackUrl,
                mExtension.errorSlackUrl, mExtension.jiraToStatus)

        project.task('releaseNotes') { doLast { createReleaseNotes(project) } }
        project.task('moveTickets') { doLast { createReleaseNotes(project) } }

        project.afterEvaluate {
            if (mExtension.enabled) monitorTasksLifecycle(project)
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
                                handleTaskFinished(task, state, project)
                            }
                        }
                )
    }

    void handleTaskFinished(Task task, TaskState state, Project project) {
        if (state.getFailure() != null) {
            core.sendErrorMessage()
            return
        }

        boolean shouldDoWork = shouldMonitorTask(task, state)
        if (shouldDoWork) {
            createReleaseNotes(project)
            core.sendNotificationMessage()
        }
    }

    boolean shouldMonitorTask(Task task, TaskState state) {
        for (dependentTask in mExtension.dependsOnTasks) {
            if (task.getName() == dependentTask && state.didWork) return true
        }
        return false
    }

    private void createReleaseNotes(Project project) {
        def message = core.getReleaseNotesMessage()
        new File("$project.projectDir/releaseNotes.txt").text = message
    }
}