package project.Rules;


import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VisualforcePageRule implements Rule {

    public String nameFile;
    public Map<String, List<String>> tagValuesSearch;

    public VisualforcePageRule(String nameFile, Map<String, List<String>> tagValuesSearch){
        this.nameFile = nameFile;
        this.tagValuesSearch = tagValuesSearch;
    }

    public List<Results> checkCondition(String file) {
        List<Results> results = new ArrayList<>();
        Document doc = Jsoup.parse(file);
        for(String tag : tagValuesSearch.keySet()) {
            results.addAll(searchValuesInTag(tag, tagValuesSearch.get(tag), doc));
        }
        return results;

    }

     private List<Results> searchValuesInTag(String tag, List<String> values, Document doc) {
         List<Results> results = new ArrayList<>();
         ArrayList<String> text = new ArrayList<>();
         Elements elements = doc.select(tag);
             if (tag.equals("button")){
                 for (Element element : elements) {
                     if (element.tagName().equals("apex:commandButton") || element.tagName().equals("button")){
                         text.add(element.getAllElements().toString());
                     }
                 }
             } else if (tag.equals("table")){
                 for (Element element : elements) {
                     if (element.tagName().equals("apex:pageBlockTable")
                             || element.tagName().equals("table")
                             || element.tagName().equals("apex:dataTable")){
                         text.add(element.getAllElements().toString());
                     }
                 }
             } else {
                 for (Element element : elements) {
                     if (element.tagName().equals(tag)){
                         text.add(element.getAllElements().toString());
                     }
                 }
             }
            if(text.isEmpty()){
                // not find teg
                 results.add(new Results(this.nameFile,  MessageFormat.format(Constants.VF_NOT_FOUND_TAG, this.nameFile, tag),  false));
                return results;
            }
            for (String searchVal :values){
                long count = text.stream().filter(e -> e.contains(searchVal)).count();
                if (count == 0)  {
                    results.add(new Results(this.nameFile,  MessageFormat.format(Constants.VF_NOT_FOUND_VALUE_IN_TAG, this.nameFile, tag, searchVal),  false));
                }else {
                    results.add(new Results(this.nameFile,  MessageFormat.format(Constants.VF_FOUND_VALUE_IN_TAG, this.nameFile, tag, searchVal),true));
                }
            }

         return results;
     }


}
