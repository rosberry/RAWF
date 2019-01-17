/*
 * Copyright © 2018 Rosberry. All rights reserved.
 */
package com.rosberry.android.gradle.rawf


import com.rosberry.android.gradle.rawf.jira.JIRAApi
import com.rosberry.android.gradle.rawf.model.SlackMessageTransformer
import com.rosberry.android.gradle.rawf.utils.GitUtils
import net.gpedro.integrations.slack.SlackApi
import net.gpedro.integrations.slack.SlackMessage
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

        project.afterEvaluate {
            if (mExtension.enabled)
                monitorTasksLifecycle(project)
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
            String ticketNumber = GitUtils.ticketNumber()

            JIRAApi jiraApi = new JIRAApi(mExtension.jiraUrl, mExtension.login, mExtension.token)
            jiraApi.moveTicket(ticketNumber)

            String message = ticketNumber + " :\"" + jiraApi.getTitle(ticketNumber) + "\""
            SlackMessage slackMessage = SlackMessageTransformer.buildSlackMessage(
                    mExtension.jiraUrl,
                    mExtension.buildNumber,
                    message
            )

            SlackApi api = new SlackApi(mExtension.slackUrl)
            api.call(slackMessage)
        }
    }

    boolean shouldMonitorTask(Task task) {
        for (dependentTask in mExtension.dependsOnTasks) {
            if (task.getName() == dependentTask) return true
        }
        return false
    }
}