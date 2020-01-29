# Описание

### Назначение
Плагин создавался для автоматизации таких рабочих процессов как: **Менять статус карточки в Jira в состояние test build**, а также **Отправки уведомления о новой сборке в Slack**.

### Использование
Для подключения плагина в проект, необходимо указать в корневом *build.gradle* репозиторий и плагин с версией

```groovy
buildscript {
    repositories {
        maven { url "https://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath "gradle.plugin.com.rosberry.android.gradle:rawf:version"
    }
}
``` 

А в модуле `app` подлючить плагин 

`apply plugin: "com.rosberry.android.gradle.rawf"`

И добавить блок с настройками, для версии `1.4.0` он выглядит так:

```groovy
rawf {
    enabled = true
    slackUrl = "slack webhook url for notification message"
    errorSlackUrl = "slack webhook url for notification message"
    jiraUrl = "jira server name"
    jiraLogin = 'login email'
    jiraToken = 'login token'
    projectKey = 'AND' //for example
    buildNumber = 'build name which will be set in notification'
    jiraFromStatus = 'READY' //for example
    jiraToStatus = 'TEST BUILD' //for example
    jiraComponent = 'Android' //for example
    buildInformationUrl = 'url of CI build for more information.'
    dependsOnTasks 'assemble'
}
```

Плагин добавляет в проект задачу `releaseNotes`, которая создает в папке проекта файл `releaseNotes.txt` содержание которого в виде разделенных тасков из JIRA.
Плагин добавляет в проект задачу `moveTickets`, которая двигает все тикеты из `jiraFromStatus` в `jiraToStatus` для проекта с ключем `projectKey` внутри компонента `jiraComponent`
Плагин добавляет в проект задачу `sendNotification`, шлет уведомление на `slackUrl` со списком всего что есть в `jiraFromStatus`

Если указана таска в `dependsOnTasks` то по ее успешному завершению будут выполнены `sendNotification`, `moveTickets`. 
В случае если какой либо процесс будет завршен с ошибкой, то придет уведомление по адрессу `errorSlackUrl`