package project;


import java.io.File;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import java.io.StringWriter;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import project.Rules.*;

public class TaskMapping {

    public static Map<String, Rule> METADATA_CHECK   = new HashMap<>();
    public static Map<String, String> TEST_CLASSES   = new HashMap<>();
    public static String VERSION  = "45.0";
    public static String PathToXMLFile  = "src/main/resources/package.xml";
    static {

        List<sObjectRule.Property> fields = new ArrayList<>();

        Map<String, String> keyValue = new HashMap<>();
        keyValue.put("type", "URL");
        keyValue.put("label", "ImageURL");
        fields.add(new sObjectRule.FieldSObjectInnerClass("ImageURL",keyValue));
        Map<String, String> keyValueTwo = new HashMap<>();
        keyValueTwo.put("type", "Number");
        keyValueTwo.put("label", "Amount");
        keyValueTwo.put("sss", "aa");
        fields.add(new sObjectRule.FieldSObjectInnerClass("Amount__c",keyValueTwo));
        Map<String, String> keyValueThree = new HashMap<>();
        fields.add(new sObjectRule.validationRulesInnerClass("DateReleaseEx",keyValueThree));
        METADATA_CHECK.put("Product__c.object", new sObjectRule("Product__c", fields));

        METADATA_CHECK.put("AccountUtils.cls", new ApexClassRule( "AccountUtils", Arrays.asList("accountsByState")));
        List<String> trigerEvents = new ArrayList<>();
        trigerEvents.add("before update");
        TriggerInfoWraper triger = new TriggerInfoWraper("HelloWorldTrigger", trigerEvents,"HelloWorldTriggerHelper");
        METADATA_CHECK.put("HelloWorldTrigger", new ApexTriggerRule("HelloWorldTrigger", triger));






        // tests: Test Class => Class тестируемый
        TEST_CLASSES.put("WebTest", "IntWebService");
    }

    public static void generatePackageXML(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Package");
            doc.appendChild(rootElement);
            Attr attr = doc.createAttribute("xmlns");
            attr.setValue("http://soap.sforce.com/2006/04/metadata");
            rootElement.setAttributeNode(attr);
            // types
            Map<String, List<String>> metadataMembers = createMapForXML();
            for (String item :metadataMembers.keySet()) {
                Element types = doc.createElement("types");
                rootElement.appendChild(types);
                if (metadataMembers.get(item).size() > 0){
                    for(String m :metadataMembers.get(item)){
                        Element members = doc.createElement("members");
                        members.appendChild(doc.createTextNode(m));
                        types.appendChild(members);
                    }
                    Element nameMembers = doc.createElement("name");
                    nameMembers.appendChild(doc.createTextNode(item));
                    types.appendChild(nameMembers);
                }
            }
            Element version = doc.createElement("version");
            version.appendChild(doc.createTextNode(VERSION));
            rootElement.appendChild(version);

// save XML
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
//            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(new File(PathToXMLFile)));
//            System.out.println(writer.getBuffer().toString());
        } catch (ParserConfigurationException | TransformerException e ) {
            e.printStackTrace();
        }
    }


    private static Map<String, List<String>> createMapForXML(){
        Map<String, List<String>> results = new HashMap<>();
        List<String> membersSobject = new ArrayList<>();
        List<String> membersApexClass = new ArrayList<>();
        List<String> membersTriggerClass = new ArrayList<>();
        for (String item : METADATA_CHECK.keySet()) {
            if (METADATA_CHECK.get(item) instanceof sObjectRule){
                String member = item.substring(0, item.indexOf('.'));
                membersSobject.add(member);
            } else if (METADATA_CHECK.get(item) instanceof ApexClassRule){
                String member = item.substring(0, item.indexOf('.'));
                membersApexClass.add(member);
            } else if (METADATA_CHECK.get(item) instanceof ApexTriggerRule){
                String member = item;
                membersTriggerClass.add(member);
            }
        }
        results.put("CustomObject", membersSobject);
        results.put("ApexClass", membersApexClass);
        results.put("ApexTrigger", membersTriggerClass);
        return results;
    }

    public static void deleteFiles(){

    }
}
