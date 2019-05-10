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
    public static double VERSION  = 45.0;
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
        keyValueThree.put("errorConditionFormula", "vasya__c");
        fields.add(new sObjectRule.validationRulesInnerClass("DateReleaseEx",keyValueThree));
        METADATA_CHECK.put("Product__c.object", new sObjectRule("Product__c", fields));
        METADATA_CHECK.put("AccountUtils.cls", new ApexClassRule( "AccountUtils", Arrays.asList("accountsByState")));
        List<String> triggerEvents = new ArrayList<>();
        triggerEvents.add("before insert");
        triggerEvents.add("before update");

        METADATA_CHECK.put("AccountAddressTrigger.trigger", new ApexTriggerRule("AccountAddressTrigger", new TriggerInfoWraper("Account", triggerEvents, "asd")));
        List<String> trigerEvents = new ArrayList<>();
        trigerEvents.add("before update");
        TriggerInfoWraper triger = new TriggerInfoWraper("HelloWorldTrigger", trigerEvents,"HelloWorldTriggerHelper");
        METADATA_CHECK.put("HelloWorldTrigger.trigger", new ApexTriggerRule("HelloWorldTrigger", triger));
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

            Map<String, List<String>> metadataMembers = createMapForXML();
            for (String item :metadataMembers.keySet()) {

                if (metadataMembers.get(item).size() > 0){
                    Element types = doc.createElement("types");
                    rootElement.appendChild(types);
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
            version.appendChild(doc.createTextNode(String.valueOf(VERSION)));
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
    //DELETE .
    private static Map<String, List<String>> createMapForXML(){
        Map<String, List<String>> results = new HashMap<>();
        List<String> membersSobject = new ArrayList<>();
        List<String> membersApexClass = new ArrayList<>();
        List<String> membersTrigger = new ArrayList<>();
        List<String> membersTriggerClass = new ArrayList<>();
        List<String> membersVisualforcePage = new ArrayList<>();
        for (String item : METADATA_CHECK.keySet()) {
            if (METADATA_CHECK.get(item) instanceof sObjectRule){
                String member = item.substring(0, item.indexOf('.'));
                membersSobject.add(member);
            } else if (METADATA_CHECK.get(item) instanceof ApexClassRule){
                String member = item.substring(0, item.indexOf('.'));
                membersApexClass.add(member);
            } else if (METADATA_CHECK.get(item) instanceof ApexTriggerRule) {
                String member = item.substring(0, item.indexOf('.'));
                membersTrigger.add(member);
            } else if (METADATA_CHECK.get(item) instanceof ApexTriggerRule){
                String member = item.substring(0, item.indexOf('.'));
                membersTriggerClass.add(member);
            } else if (METADATA_CHECK.get(item) instanceof VisualforcePageRule){
                System.out.println(item);
                String member = item.substring(0, item.indexOf('.'));
                membersVisualforcePage.add(member);
            }
        }
        if (!membersSobject.isEmpty()) {
            results.put("CustomObject", membersSobject);
        }
        if (!membersApexClass.isEmpty()) {
            results.put("ApexClass", membersApexClass);
        }
        if (!membersTriggerClass.isEmpty()) {
            results.put("ApexTrigger", membersTriggerClass);
        }
        if (!membersVisualforcePage.isEmpty()) {
            results.put("ApexPage", membersVisualforcePage);
        }
        return results;
    }
}
