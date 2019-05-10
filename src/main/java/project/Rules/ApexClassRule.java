package project.Rules;

import project.Util;

import java.util.ArrayList;
import java.util.List;

public class ApexClassRule implements  Rule {

    public String nameClass;
    public List<String> stringsForSearch;

    public ApexClassRule(String name,List<String> stringsForSearch){
        this.nameClass = name;
        this.stringsForSearch = stringsForSearch;
    }

    public  List<Results>  checkCondition(String file){
        List<Results> results = new ArrayList<>();
        for (String str: stringsForSearch){
            results.add(searchString(file, str));
//                System.out.println(searchString(file, str).message);
//                System.out.println(searchString(file, str).user);
//                System.out.println(searchString(file, str).status);
//                System.out.println(searchString(file, str).nameMetadata);
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
