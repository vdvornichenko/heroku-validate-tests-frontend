package project;

import project.Rules.Results;

import java.util.ArrayList;
import java.util.List;

public class UserInfoWrapper {
    public List<Results> results;
    public List<String> loginHistoryList;
    public List<String> errors;

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

    public void addError(String errorMessage) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(errorMessage);
    }

    public UserInfoWrapper setResults(List<Results> userResults) {
        this.results = userResults;
        return this;
    }

    public UserInfoWrapper setLoginHistoryList(List<String> loginHistoryList) {
        this.loginHistoryList = loginHistoryList;
        return this;
    }
}
