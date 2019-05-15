package project;


import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
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


import com.google.gson.*;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.gson.reflect.*;
import org.mortbay.util.ajax.JSON;
import org.mortbay.util.ajax.JSONObjectConvertor;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import project.Rules.*;

public class TaskMapping {

    public static Map<String, Rule> METADATA_CHECK   = new HashMap<>();
    public static Map<String, String> TEST_CLASSES   = new HashMap<>();
    public static List<String> tasks                 = Arrays.asList(
            "Product__c.object",
            "ProductTablePage.page",
            "ProductTablePageController.cls",
            "ProductTrigger.trigger"
    );
    public static  ArrayList<CheckExecuteMethodWraper> TEST_METHOD   = new ArrayList<>();

    public static double VERSION  = 45.0;
    public static String PathToXMLFile  = "src/main/resources/package.xml";
    static {
        List<sObjectRule.Property> fields = new ArrayList<>();
//        •  Поле для хранения цены на продукт Field Name = ImageURL
        Map<String, String> keyValue = new HashMap<>();
        keyValue.put("type", "Url"); // Type = URL
        keyValue.put("label", "ImageURL"); // Label = ImageURL
        fields.add(new sObjectRule.FieldSObjectInnerClass("ImageURL__c",keyValue));
//        •  Поле для хранения цены на продукт Field Name = UnitPrice
        Map<String, String> keyValueTwo = new HashMap<>();
        keyValueTwo.put("type", "Currency");  //Type = Currency
        keyValueTwo.put("label", "UnitPrice"); //Label = UnitPrice
        fields.add(new sObjectRule.FieldSObjectInnerClass("UnitPrice__c",keyValueTwo));
//        • Поле для хранения количества продуктов Field Name = UnitsAvailable
        Map<String, String> keyValueThree = new HashMap<>();
        keyValueThree.put("type", "Number");  //Type = Number
        keyValueThree.put("label", "UnitsAvailable"); //Label = UnitsAvailable
        fields.add(new sObjectRule.FieldSObjectInnerClass("UnitsAvailable__c",keyValueThree));
//        • Поле для хранения количества продуктов Field Name = UnitsAvailable
        Map<String, String> keyValueFour = new HashMap<>();
        keyValueFour.put("type", "LongTextArea");  //Type = Number
        keyValueFour.put("label", "Description"); //Label = UnitsAvailable
        fields.add(new sObjectRule.FieldSObjectInnerClass("Description__c",keyValueFour));
//        • Поле для хранения даты добавления продукта Field Name = AddedDate
        Map<String, String> keyValueFive = new HashMap<>();
        keyValueFive.put("type", "DateTime");  //Type = Number
        keyValueFive.put("label", "AddedDate"); //Label = UnitsAvailable
        fields.add(new sObjectRule.FieldSObjectInnerClass("AddedDate__c",keyValueFive));
//        Создать Validation Rule, который не будет позволять создавать записи Product, если
        Map<String, String> keyValueValidRule = new HashMap<>();
        keyValueValidRule.put("errorConditionFormula", "UnitPrice__c");
        fields.add(new sObjectRule.validationRulesInnerClass("CorrectPriceValidation",keyValueValidRule));
//          Label = Product
        fields.add(new sObjectRule.labelInnerClass("Product"));

        METADATA_CHECK.put("Product__c.object", new sObjectRule("Product__c", fields));

//      Trigger
        List<String> triggerEvents = new ArrayList<>();
        triggerEvents.add("before insert");
        triggerEvents.add("before update");
        METADATA_CHECK.put("ProductTrigger.trigger", new ApexTriggerRule("ProductTrigger", new TriggerInfoWraper("Product__c", triggerEvents, "ProductTriggerHelper")));
        //      Apex class
        METADATA_CHECK.put("ProductTablePageController.cls", new ApexClassRule( "ProductTablePageController", Arrays.asList("getProducts")));
// use key-word "button", "table" for search values in this tags
//      VisualforcePage
        Map<String, List<String>> tagValuesForSearchVF = new HashMap<>();
        ArrayList<String> searchInTable = new ArrayList<String>() {
            {
                add("Image");
                add("Name");
                add("Description");
                add("Price");
                add("Available Units");
            }
        };
        tagValuesForSearchVF.put("table", searchInTable);
        tagValuesForSearchVF.put("apex:page", new ArrayList<String>() {{ add("ProductTablePageController");}});
        tagValuesForSearchVF.put("button", new ArrayList<String>() {{ add("New");add("Save");}});
        METADATA_CHECK.put("ProductTablePage.page", new VisualforcePageRule("ProductTablePage", tagValuesForSearchVF));

        TEST_METHOD.add(new CheckExecuteMethodWraper(
                        "ProductTablePageController",
                        "getProducts",
                        "ProductTablePageController cl = new ProductTablePageController();"
                                + " List<Product__c> executeMethod = cl.getProducts();"));
        // tests: Test Class => Class тестируемый
        TEST_CLASSES.put("TestProductTablePageController", "ProductTablePageController");
        TEST_CLASSES.put("TestProductTrigger", "ProductTrigger");
        TEST_CLASSES.put("TestProductTriggerHelper", "ProductTriggerHelper");



//        JSONObject obj = new JSONObject();
//        obj.put("METADATA_CHECK", METADATA_CHECK);
//        obj.put("TEST_METHOD", TEST_METHOD);
//        obj.put("TEST_CLASSES", TEST_CLASSES);


//        GsonBuilder builder = new GsonBuilder();
//        Type type = new TypeToken<TaskMapping>() {}.getType();
//        Gson gson = builder
//                .setPrettyPrinting()
//                .registerTypeAdapter(type, new TaskMappingConverter())
//                .create();
//        String json = gson.toJson(gson.toJson(METADATA_CHECK));
//        System.out.println(json);
//        Object natural = gson.fromJson(json, Object.class);
//        System.out.println("sssss");
//        System.out.println( natural.getClass().getName());
//        System.out.println(natural);

//
//        Map<String,Object> map = new HashMap<String,Object>();
//        map = (Map<String,Object>) gson.fromJson(json, map.getClass());


//        Map<String, Rule> empJoiningDateMap = gson.fromJson(json, type);
//        System.out.println(json);
//        System.out.println(empJoiningDateMap);








//        Map map = gson.fromJson(json, Map.class);
//        System.out.println(map);
//        System.out.println(map.size());

//        Type empMapType = new TypeToken<Map<String, Rule>>() {}.getType();
//        Map<String, Rule> nameEmployeeMap = gson.fromJson(json, empMapType);
//
//        for (String s: nameEmployeeMap.keySet()){
//            System.out.println(s);
//        }



//        try{
//        FileWriter file = new FileWriter("src/main/resources/StorageTaskMapping.json");
//        file.write(gson.toJson(METADATA_CHECK));
//        file.flush();
//    } catch(Exception e){
//
//    }


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
        List<String> membersTriggerClass = new ArrayList<>();
        List<String> membersVisualforcePage = new ArrayList<>();
        for (String item : METADATA_CHECK.keySet()) {
            String member = (item.contains(".")  ? item.substring(0, item.indexOf('.')) : item);
            if (METADATA_CHECK.get(item) instanceof sObjectRule){
                membersSobject.add(member);
            } else if (METADATA_CHECK.get(item) instanceof ApexClassRule){
                membersApexClass.add(member);
            } else if (METADATA_CHECK.get(item) instanceof ApexTriggerRule){
                membersTriggerClass.add(member);
            }else if (METADATA_CHECK.get(item) instanceof VisualforcePageRule){
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
