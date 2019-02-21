/*
 * Copyright © 2018 Rosberry. All rights reserved.
 */
package com.rosberry.android.gradle.rawf

import com.rosberry.android.gradle.rawf.jira.RAWF
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.logging.StandardOutputListener
import org.gradle.api.tasks.TaskState

/**
 * Created by Alexey Korshun on 08/07/18.
 */
class RAWFPlugin implements Plugin<Project> {

    RAWFPluginExtension mExtension
    StringBuilder mTaskLogBuilder

    void apply(Project project) {
        mTaskLogBuilder = new StringBuilder()
        mExtension = project.extensions.create('rawf', RAWFPluginExtension)

        project.task('releaseNotes') {
            doLast {
                def message = new RAWF().getReleaseNotesMessage(mExtension.jiraUrl, mExtension.jiraLogin,
                        mExtension.jiraToken, mExtension.projectKey, mExtension.jiraComponent, mExtension.jiraFromStatus)
                new File("$project.projectDir/releaseNotes.txt").text = message
            }
        }

        project.afterEvaluate {
            if (mExtension.enabled) monitorTasksLifecycle(project)
        }
    }

    void monitorTasksLifecycle(Project project) {
        project.getGradle().getTaskGraph().addTaskExecutionListener(new TaskExecutionListener() {
            @Override
            void beforeExecute(Task task) {
                task.logging.addStandardOutputListener(new StandardOutputListener() {
                    @Override
                    void onOutput(CharSequence charSequence) {
                        mTaskLogBuilder.append(charSequence)
                    }
                })
            }

            @Override
            void afterExecute(Task task, TaskState state) {
                handleTaskFinished(task)
            }
        })
    }

    void handleTaskFinished(Task task) {

        boolean shouldDoWork = shouldMonitorTask(task)
        if (shouldDoWork) {
            new RAWF().doWork(mExtension.jiraUrl, mExtension.jiraLogin, mExtension.jiraToken, mExtension.projectKey,
                    mExtension.jiraComponent, mExtension.jiraFromStatus, mExtension.buildNumber, mExtension.slackUrl,
                    mExtension.jiraToStatus)
        }
    }

    boolean shouldMonitorTask(Task task) {
        for (dependentTask in mExtension.dependsOnTasks) {
            if (task.getName() == dependentTask) return true
        }
        return false
    }
}