package project.Rules;

import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import javax.xml.xpath.*;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.w3c.dom.NodeList;


public class sObjectRule implements  Rule {
    public static String templateNotFoundField =  "Не найдено поле: {0} у обьекта: {1}";
    public static String templateFoundField =  "Найдено поле: : {0} у обьекта: {1}";
    public static String templateNotFoundProperty =  "Не орректное свойство: {0}  у поля  {1} у обьекта  {2} ";
    public static String templateFoundProperty =  "Корректное свойство: {0}  у поля  {1} у обьекта  {2} ";
    public static String templateNotFoundValidationRules =  "Не найдены validationRules: {0} у обьекта: {1} ";
    public static String templateFoundValidationRules =  "Найдены validationRules: {0} у обьекта: {1}";

    public String nameFile;
    public List<Property> properties;

    public sObjectRule(String nameFile,  List<Property> properties){
        this.nameFile = nameFile;
        this.properties = properties;
    }
    private static Document getDocument(String fileName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(fileName)));
        doc.getDocumentElement().normalize();
        return doc;
    }

    public List<Results> checkCondition (String file){
        List<Results> results = new ArrayList<>();
        try {
            Document doc = getDocument(file);
            for (Property pr :properties){
                results.addAll(pr.checkProperty(doc, nameFile));
            }
        }
        catch (Exception e) {
            System.out.println("!!!!!!!!error sObject checkCondition " + e.getMessage());
        }
        return results;
    }

    public interface Property {
        List<Results> checkProperty(Document doc, String fileName);
    }

    public static class FieldSObjectInnerClass implements  Property {
        public String name;
        public Map<String, String> keyValue;
        public  FieldSObjectInnerClass(String name, Map<String, String> keyValue) {
            this.name = name;
            this.keyValue = keyValue;
        }
        public List<Results> checkProperty(Document doc, String fileName) {
            List<Results> results = new ArrayList<>();
            try {
                XPath xpath = XPathFactory.newInstance().newXPath();
                XPathExpression expr = xpath.compile("//fields[fullName='"+ name +"']");
                Node fieldNode = (Node)expr.evaluate(doc, XPathConstants.NODE);
                if (fieldNode == null) {
                    results.add( new Results(fileName, MessageFormat.format(templateNotFoundField, name, fileName), false));
                    return results;
                }
                results.add( new Results(fileName,  MessageFormat.format(templateFoundField, name, fileName), true));
                final Node nodeClone = fieldNode.cloneNode(true);
                for (String key : keyValue.keySet()) {
                    XPathExpression expClone = xpath.compile("//" + key);
                    String fieldKey = (String)expClone.evaluate(nodeClone, XPathConstants.STRING);
                    if (fieldKey.equals(keyValue.get(key))){
                        results.add( new Results(fileName,  MessageFormat.format(templateFoundProperty, key, name, fileName), true));
                    } else {
                        results.add( new Results(fileName,  MessageFormat.format(templateNotFoundProperty, key, name, fileName),  false));
                    }
                }
            }
            catch (Exception e) {
                System.out.println("!!!!!!!!error FieldSObjectInnerClass" + e.getMessage());
            }
            return results;
        }
    }


    public static class validationRulesInnerClass implements  Property {
        public String name;
        public Map<String, String> keyValue;
        public  validationRulesInnerClass(String name, Map<String, String> keyValue) {
            this.name = name;
            this.keyValue = keyValue;
        }
        public List<Results> checkProperty(Document doc, String fileName) {
            List<Results> results = new ArrayList<>();
            try {
                XPath xpath = XPathFactory.newInstance().newXPath();
                XPathExpression expr = xpath.compile("//validationRules[fullName='"+ name +"']");
                Node validationRulesNode = (Node)expr.evaluate(doc, XPathConstants.NODE);
                if (validationRulesNode == null) {
                    results.add( new Results(fileName, MessageFormat.format(templateNotFoundValidationRules, name, fileName), false));
                    return results;
                }
                results.add( new Results(fileName,  MessageFormat.format(templateFoundValidationRules, name, fileName), true));
            }
            catch (Exception e) {
                System.out.println("!error FieldSObjectInnerClass" + e.getMessage());
            }
            return results;
        }
    }


//    public  List<Condition> conditions = new ArrayList<>();
//    public String nameFile = "";

//    public sObjectRule(String nameFile,  Element element){
//        this.nameFile = nameFile;
//        NodeList fields = element.getElementsByTagName("fields");
//        for (int i = 0; i < fields.getLength(); i++){
//            conditions.addAll(setFieldsRule(fields.item(i).getChildNodes()));
//        }
//    }
////if("HtmlTag".equals(node.getNodeName()))
////    String nodeContent=node.getAttributes().getNamedItem("car").getNodeValue()
//
//    public List<Condition> setFieldsRule (NodeList fieldsChild){
//        List<Condition> conds= new ArrayList<>();
//        for (int i = 0; i < fieldsChild.getLength(); i++){
//            Node node = fieldsChild.item(i);
////            Element e = (Element)node;
////            String name = e.getAttribute("name");
//            if (node.getNodeType() != Node.TEXT_NODE) {
////                System.out.println("***************");
////                System.out.println(node.getNodeName() + ":" + node.getChildNodes().item(0).getTextContent());
//                conds.add(new Condition( "fields",node.getNodeName(), node.getChildNodes().item(0).getTextContent()));
//            }
//        }
//        return conds;
//    }






}
