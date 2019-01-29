package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.jira.model.Issue;
import com.rosberry.android.gradle.rawf.jira.model.Transition;
import org.junit.Test;

import java.util.List;

public class JiraModelParserTest {

    private static final String jsonSearch = "{\n" +
            "    \"expand\": \"names,schema\",\n" +
            "    \"startAt\": 0,\n" +
            "    \"maxResults\": 50,\n" +
            "    \"total\": 1,\n" +
            "    \"issues\": [\n" +
            "        {\n" +
            "            \"key\": \"WB-1262\",\n" +
            "            \"fields\": {\n" +
            "                \"issuetype\": {\n" +
            "                    \"self\": \"https://rosberry.atlassian.net/rest/api/3/issuetype/1\",\n" +
            "                    \"id\": \"1\",\n" +
            "                    \"description\": \"A problem which impairs or prevents the functions of the product.\",\n" +
            "                    \"iconUrl\": \"https://rosberry.atlassian.net/secure/viewavatar?size=xsmall&avatarId=11003&avatarType=issuetype\",\n" +
            "                    \"name\": \"Bug\",\n" +
            "                    \"subtask\": false,\n" +
            "                    \"avatarId\": 11003\n" +
            "                },\n" +
            "                \"summary\": \"Напиток затреканный во время подсказок не влияет на баланс.\"\n" +
            "            }\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    private static final String TRANSITIONS_RAW = "{\n" +
            "    \"expand\": \"transitions\",\n" +
            "    \"transitions\": [\n" +
            "        {\n" +
            "            \"id\": \"11\",\n" +
            "            \"name\": \"To Do\",\n" +
            "            \"to\": {\n" +
            "                \"self\": \"https://rosberry.atlassian.net/rest/api/3/status/10304\",\n" +
            "                \"description\": \"\",\n" +
            "                \"iconUrl\": \"https://rosberry.atlassian.net/images/icons/statuses/open.png\",\n" +
            "                \"name\": \"To Do\",\n" +
            "                \"id\": \"10304\",\n" +
            "                \"statusCategory\": {\n" +
            "                    \"self\": \"https://rosberry.atlassian.net/rest/api/3/statuscategory/2\",\n" +
            "                    \"id\": 2,\n" +
            "                    \"key\": \"new\",\n" +
            "                    \"colorName\": \"blue-gray\",\n" +
            "                    \"name\": \"To Do\"\n" +
            "                }\n" +
            "            },\n" +
            "            \"hasScreen\": false,\n" +
            "            \"isGlobal\": true,\n" +
            "            \"isInitial\": false,\n" +
            "            \"isConditional\": false\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"21\",\n" +
            "            \"name\": \"In Progress\",\n" +
            "            \"to\": {\n" +
            "                \"self\": \"https://rosberry.atlassian.net/rest/api/3/status/10303\",\n" +
            "                \"description\": \"\",\n" +
            "                \"iconUrl\": \"https://rosberry.atlassian.net/images/icons/statuses/inprogress.png\",\n" +
            "                \"name\": \"Doing\",\n" +
            "                \"id\": \"10303\",\n" +
            "                \"statusCategory\": {\n" +
            "                    \"self\": \"https://rosberry.atlassian.net/rest/api/3/statuscategory/4\",\n" +
            "                    \"id\": 4,\n" +
            "                    \"key\": \"indeterminate\",\n" +
            "                    \"colorName\": \"yellow\",\n" +
            "                    \"name\": \"In Progress\"\n" +
            "                }\n" +
            "            },\n" +
            "            \"hasScreen\": false,\n" +
            "            \"isGlobal\": true,\n" +
            "            \"isInitial\": false,\n" +
            "            \"isConditional\": false\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"41\",\n" +
            "            \"name\": \"Code Review\",\n" +
            "            \"to\": {\n" +
            "                \"self\": \"https://rosberry.atlassian.net/rest/api/3/status/10802\",\n" +
            "                \"description\": \"\",\n" +
            "                \"iconUrl\": \"https://rosberry.atlassian.net/images/icons/statuses/generic.png\",\n" +
            "                \"name\": \"Code Review\",\n" +
            "                \"id\": \"10802\",\n" +
            "                \"statusCategory\": {\n" +
            "                    \"self\": \"https://rosberry.atlassian.net/rest/api/3/statuscategory/4\",\n" +
            "                    \"id\": 4,\n" +
            "                    \"key\": \"indeterminate\",\n" +
            "                    \"colorName\": \"yellow\",\n" +
            "                    \"name\": \"In Progress\"\n" +
            "                }\n" +
            "            },\n" +
            "            \"hasScreen\": false,\n" +
            "            \"isGlobal\": true,\n" +
            "            \"isInitial\": false,\n" +
            "            \"isConditional\": false\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"51\",\n" +
            "            \"name\": \"Ready\",\n" +
            "            \"to\": {\n" +
            "                \"self\": \"https://rosberry.atlassian.net/rest/api/3/status/10803\",\n" +
            "                \"description\": \"\",\n" +
            "                \"iconUrl\": \"https://rosberry.atlassian.net/images/icons/statuses/generic.png\",\n" +
            "                \"name\": \"Ready\",\n" +
            "                \"id\": \"10803\",\n" +
            "                \"statusCategory\": {\n" +
            "                    \"self\": \"https://rosberry.atlassian.net/rest/api/3/statuscategory/4\",\n" +
            "                    \"id\": 4,\n" +
            "                    \"key\": \"indeterminate\",\n" +
            "                    \"colorName\": \"yellow\",\n" +
            "                    \"name\": \"In Progress\"\n" +
            "                }\n" +
            "            },\n" +
            "            \"hasScreen\": false,\n" +
            "            \"isGlobal\": true,\n" +
            "            \"isInitial\": false,\n" +
            "            \"isConditional\": false\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"61\",\n" +
            "            \"name\": \"Test Build\",\n" +
            "            \"to\": {\n" +
            "                \"self\": \"https://rosberry.atlassian.net/rest/api/3/status/10804\",\n" +
            "                \"description\": \"\",\n" +
            "                \"iconUrl\": \"https://rosberry.atlassian.net/images/icons/statuses/generic.png\",\n" +
            "                \"name\": \"Test Build\",\n" +
            "                \"id\": \"10804\",\n" +
            "                \"statusCategory\": {\n" +
            "                    \"self\": \"https://rosberry.atlassian.net/rest/api/3/statuscategory/4\",\n" +
            "                    \"id\": 4,\n" +
            "                    \"key\": \"indeterminate\",\n" +
            "                    \"colorName\": \"yellow\",\n" +
            "                    \"name\": \"In Progress\"\n" +
            "                }\n" +
            "            },\n" +
            "            \"hasScreen\": false,\n" +
            "            \"isGlobal\": true,\n" +
            "            \"isInitial\": false,\n" +
            "            \"isConditional\": false\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    @Test
    public void getIssueListFromSearchRawString() {
        JiraModelParser parser = new JiraModelParser();
        List<Issue> issues = parser.getIssueListFromSearchRawString(jsonSearch);
        System.out.println(issues);
    }

    @Test
    public void getTransitionListFromRawString() {
        JiraModelParser parser = new JiraModelParser();
        List<Transition> transitions = parser.getTransitionListFromResponse(TRANSITIONS_RAW);
        System.out.println(transitions);
    }
}