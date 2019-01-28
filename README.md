# Описание

### Назначение
Плагин создавался для автоматизации таких рабочих процессов как: **Менять статус карточки в Jira в состояние test build**, а также **Отправки уведомления о новой сборке в Slack**.

### Использование
Для подключения плагина в проект, необходимо указать в корневом *build.gradle* репозиторий и плагин с версией

```groovy
repositories {
        maven { url "https://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath "gradle.plugin.com.rosberry.android.gradle:rawf:version"
    }
``` 

А в модуле `app` подлючить плагин 

`apply plugin: "com.rosberry.android.gradle.rawf"`

И добавить блок с настройками, для версии `0.3.8` он выглядит так:

```groovy
rawf {
    enabled boolean //default: true
    slackUrl 'webhoock for send success message' //default: empty (message won't send if empty)
    jiraUrl 'root url for Jira' //default: empty (task won't moved and message attachment will empty)
    jiraLogin 'login for jira access' //default: empty (can't access to jira)
    jiraToken '' //default: empty (can't access to jira)
    projectKey '' //default: empty (can't get tickets for empty project)
    jiraStatus '' //default: empty (get all tickets on board for current project in current component)
    jiraComponent '' //default: empty (get all tickets on board for current project in current status)
    dependsOnTasks 'for example crashlyticsUploadDistributionDemoDebug' //can be an array, default: build
    buildNumber 'number of your build for use it in notification'
}
```

Плагин добавляет в проект задачу `releaseNotes`, которая устанавливает в проект переменную `releaseMessage` в виде разделенных тасков из JIRA.