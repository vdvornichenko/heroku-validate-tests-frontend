package project;

import project.Rules.Results;

import java.util.ArrayList;
import java.util.List;

public class UserInfoWrapper {
    public List<Results> results;
    public List<String> loginHistoryList;

    public UserInfoWrapper() {}

    public void addResults(Results results) {
        if (this.results == null) {
            this.results = new ArrayList<>();
        }
        this.results.add(results);
    }

    public void addLoginHistoryElement(String loginHistory) {
        if (this.loginHistoryList == null) {
            this.loginHistoryList = new ArrayList<>();
        }

        loginHistoryList.add(loginHistory);
    }
}
