package Validation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TaskMapping {

    public static Map<String, List<String>> CLASS_ACCOUNT   = new HashMap<>();
    public static Map<String, String> TEST_CLASSES          = new HashMap<>();

    static {
        // class **
        List<String> methods = new LinkedList<>();
        methods.add("accountsByState");
        CLASS_ACCOUNT.put("AccountUtils", methods);

        // tests: Test Class => Class
        TEST_CLASSES.put("WebTest", "IntWebService");
    }

}
