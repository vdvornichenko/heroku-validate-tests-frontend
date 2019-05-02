package project.Rules;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;
import org.xml.sax.InputSource;
import org.w3c.dom.NodeList;


public class sObjectRule implements  Rule {

    public String nameFile;
    public List<String> fields;

    public sObjectRule(String nameFile,  List<String> fields){
        this.nameFile = nameFile;
        this.fields = fields;
    }

    public List<Results> checkCondition(String str){
        List<Results> results = new ArrayList<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(str)));
            doc.getDocumentElement().normalize();
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression expr = xpath.compile("//fields/fullName");
            NodeList nodelist = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
            for (String field: fields){
                results.add(checkField(nodelist, field));
//                System.out.println(checkField(nodelist, field).message);
//                System.out.println(checkField(nodelist, field).user);
//                System.out.println(checkField(nodelist, field).status);
//                System.out.println(checkField(nodelist, field).nameMetadata);
            }
        }
        catch (Exception e) {
            System.out.println("!!!!!!!!error sObject checkCondition xpath.evaluate" + e.getMessage());
        }
        return results;
    }


    public Results checkField( NodeList nodelist, String field){
        for (int i = 0; i < nodelist.getLength(); i++) {
            if (nodelist.item(i).getTextContent().equals(field)){
                return new Results(nameFile, "Found field " + field, true);
            }
        }
        return new Results(nameFile, "NOT Found field " + field, false);
    }




//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.ParserConfigurationException;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import javax.xml.xpath.XPathExpressionException;
//import org.w3c.dom.Element;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;
//
//import org.xml.sax.SAXException;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import org.w3c.dom.Document;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;

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
