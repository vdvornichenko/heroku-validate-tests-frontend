package project.Rules;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApexClassRule implements  Rule {

    public String nameClass;
    public List<String> methodsForSearch;

    public ApexClassRule(String name,List<String> methodsForSearch){
        this.nameClass = name;
        this.methodsForSearch = methodsForSearch;
    }

    public  List<Results>  checkCondition(String file){
        List<Results> results = new ArrayList<>();
        for (String method: methodsForSearch){
            if (Util.checkNesting(file, method) == 1) {
                results.add(new Results(nameClass,MessageFormat.format(Constants.APEXCLASS_FOUND_METHOD,  nameClass, method), true));
            } else {
                results.add(new Results(nameClass, MessageFormat.format(Constants.APEXCLASS_NOT_FOUND_METHOD,  nameClass, method), false));
            }
        }
        return results;
    }
}
