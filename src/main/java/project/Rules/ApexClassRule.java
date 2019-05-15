package project.Rules;

import project.Util;
import project.ValidateByTestHelper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApexClassRule implements  Rule {

    public String nameClass;
    public List<String> methodsForSearch;
    public List<CheckExecuteMethodWraper> checkExecuteMethods;

    public ApexClassRule(String name, List<String> methodsForSearch, List<CheckExecuteMethodWraper> checkExecuteMethods) {
        this.nameClass = name;
        this.methodsForSearch = methodsForSearch;
        this.checkExecuteMethods = checkExecuteMethods;
    }

    public List<Results> checkCondition(String file, String userName) {
        List<Results> results = new ArrayList<>();
        for (String method : methodsForSearch) {
            if (Util.checkNesting(file, method) == 1) {
                results.add(new Results(nameClass, MessageFormat.format(Constants.APEXCLASS_FOUND_METHOD, nameClass, method), true));
            } else {
                results.add(new Results(nameClass, MessageFormat.format(Constants.APEXCLASS_NOT_FOUND_METHOD, nameClass, method), false));
            }
        }
        ValidateByTestHelper hlp = new ValidateByTestHelper(userName);
        results.addAll(hlp.validateApexMethod(checkExecuteMethods));
        return results;
    }
}




