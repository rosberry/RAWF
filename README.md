
# Описание

### Назначение
Плагин создавался для автоматизации таких рабочих процессов как: **Менять статус карточки в Jira в состояние test build**, а также **Посылать уведомление о новой сборке в Slack**.

### Использование
Для подключения плагина в проект, необходимо указать в корневом *build.gradle* репозиторий и плагин с версией

```groovy
repositories {
        maven { url "https://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath "gradle.plugin.com.github.alexeykorshun:slack-build-notify:version"
    }
``` 

А в модуле `app` подлючить плагин 

`apply plugin: "com.github.alexeykorshun.slack-build-notify"`

И добавить блок с настройками, для версии `0.3.5` он выглядит так:

```groovy
slack {
    slackUrl 'webhoock for send success message'
    jiraUrl 'root url for Jira, for Rosberry is https://rosberry.atlassian.net'
    dependsOnTasks 'for example crashlyticsUploadDistributionDemoDebug'
    enabled isCIMachine()
    buildNumber 'number of your build'
}
```