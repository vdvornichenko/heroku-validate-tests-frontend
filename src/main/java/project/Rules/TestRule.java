package project.Rules;

import project.Rules.Constants;
import project.Util;
import project.ValidateByTestHelper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestRule implements Rule {

    // tests: Test Class => Class тестируемый
    public Map<String, String> testClass;

    public TestRule(Map<String, String> testClass) {
        this.testClass = testClass;
    }

    public List<Results> checkCondition(String file, String userName) {
        List<Results> results = new ArrayList<>();
        if (    Util.checkNesting(file, "System.assert") > 1 ||
                Util.checkNesting(file, "System.assertEquals") > 1 ||
                Util.checkNesting(file, "System.assertNotEquals") > 1 ) {
            results.add(new Results("TEST", MessageFormat.format(Constants.TEST_SUCCESS_ASSERT,  "Test"), true));
        } else {
            results.add(new Results("TEST",  MessageFormat.format(Constants.TEST_FAIL_ASSERT,  "Test"), false));
        }
        ValidateByTestHelper helper = new ValidateByTestHelper(userName);
//        results.add(helper.validateUserResultUsingTest(testClass));
        return results;
    }



}
