package project.Processors;

import project.GoogleHelper;
import project.Rules.Results;
import project.SalesforceHepler;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import project.TaskMapping;
import project.Storages.FileStorage;

public class RequestProcessor {
    String users;
    public static Map<String, List<Results>> userResults = new HashMap<>();
    public static List<CredentialsStorage> userLogins = new ArrayList<>();
    public static List<FileStorage> files;

    public RequestProcessor() {
        userLogins = new ArrayList<>();
        GoogleHelper.callDocument();
    }

    public RequestProcessor(String userNames) {
        GoogleHelper.callDocument();
        users = userNames;

        if (userNames.equalsIgnoreCase("all")) {
            users = String.join(";", GoogleHelper.userCreds.keySet());
            System.out.println(users);
        }
    }

    public Map<String, List<Results>> getUsersInfo() {
        files = new ArrayList<>();
        userResults.clear();
        Stream<String> creds = Arrays.stream(users.split(";"));
        TaskMapping.generatePackageXML();
        creds.parallel().forEach(value -> {
            SalesforceHepler helper = new SalesforceHepler(value, GoogleHelper.userCreds.get(value));
            helper.processUser();

        });
        return userResults;
    }

    public List<CredentialsStorage> getUserLogins() {

        return userLogins;
    }

    public static class CredentialsStorage {
        public String userName;
        public String password;
        public String fio;
        public String group;

        public CredentialsStorage(String userName, String password, String fio, String group) {
            this.userName = userName;
            this.password = password;
            this.fio = fio;
            this.group = group;
        }
    }
}
