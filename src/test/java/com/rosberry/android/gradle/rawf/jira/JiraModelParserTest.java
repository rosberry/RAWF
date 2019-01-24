package com.rosberry.android.gradle.rawf.jira;

import com.rosberry.android.gradle.rawf.jira.model.Issue;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

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

    @Test
    public void getTitleFromIssueRawString() {
    }

    @Test
    public void getIssueListFromSearchRawString() {
        JiraModelParser parser = new JiraModelParser();
        List<Issue> issues = parser.getIssueListFromSearchRawString(jsonSearch);
        System.out.println(issues);
        assertTrue(true);
    }
}