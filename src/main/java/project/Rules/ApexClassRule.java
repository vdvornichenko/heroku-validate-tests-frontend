package project.Rules;

import project.Util;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApexClassRule implements  Rule {

    public String nameClass;
    public List<String> methodsForSearch;
    public List<CheckExecuteMethodWraper> checkExecuteMethods;

    public ApexClassRule(String name, List<String> methodsForSearch) {
        this.nameClass = name;
        this.methodsForSearch = methodsForSearch;
        this.checkExecuteMethods = checkExecuteMethods;
    }

    public List<Results> checkCondition(String file) {
        List<Results> results = new ArrayList<>();
        for (String method : methodsForSearch) {
            if (Util.checkNesting(file, method) == 1) {
                results.add(new Results(nameClass, MessageFormat.format(Constants.APEXCLASS_FOUND_METHOD, nameClass, method), true));
            } else {
                results.add(new Results(nameClass, MessageFormat.format(Constants.APEXCLASS_NOT_FOUND_METHOD, nameClass, method), false));
            }
        }
        return results;
    }

    public Results searchString(String file, String strSearch){
        if (file.contains(strSearch) && file != null){
            if (Util.checkNesting(file, strSearch) == 1) {
                return new Results(nameClass, "Метод " + strSearch + " реализован",true);
            }
        }
        return new Results(nameClass, "Метод " + strSearch + " не реализован",false);
    }

}
//    List<Account> firstList;
//    List<Account> secondList;
//
//    Map<id, Account> firstMap = new Map<id, Account>(firstList);
//    Map<id, Account> secondMap = new Map<id, Account>(secondList);
//
//    If( firstMap.keySet().contailsAll(secondMap.keySet()) && secondMap.keySet().contailsAll(firstMap.keySet())  ){
//        System.debug('List are equals');
//    }




