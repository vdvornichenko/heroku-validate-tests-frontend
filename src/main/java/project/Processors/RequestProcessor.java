package project.Processors;

import project.GoogleHelper;
import project.SalesforceHepler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class RequestProcessor {
    String users;
    public static Map<String, String> userResults = new HashMap<>();

    public RequestProcessor(String userNames) {
        users = userNames;
        if (userNames == "all") {
            users = String.join(";", GoogleHelper.userCreds.keySet());
        }
    }

    public Map<String, String> getUsersInfo() {
        userResults.clear();
        Stream<String> creds = Arrays.stream(users.split(";"));
        creds.parallel().forEach(value -> {
            SalesforceHepler helper = new SalesforceHepler(value, GoogleHelper.userCreds.get(value));
            helper.processUser();
        });
        return userResults;
    }
}
