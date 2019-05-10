package project.Rules;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static Integer checkNesting(String file, String strSearch) {
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
