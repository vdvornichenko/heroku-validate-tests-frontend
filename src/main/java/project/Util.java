package project;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static Integer checkNesting(String file, String strSearch) {
        file = file.toLowerCase();
        strSearch = strSearch.toLowerCase();
        if (!file.contains(strSearch)) {
            return -1;
        }
        List<String> quotes = new ArrayList<>();
        for (int i = 0; i < file.indexOf(strSearch); i ++) {
            String symb = Character.toString(file.charAt(i));
            if (symb.equalsIgnoreCase("{")) {
                quotes.add(symb);
            } else {
                if (symb.equalsIgnoreCase("}")) {
                    quotes.remove(quotes.size() - 1);
                }
            }
        }
        return quotes.size();
    }
}
