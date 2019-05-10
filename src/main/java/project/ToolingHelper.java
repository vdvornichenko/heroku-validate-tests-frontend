package project;

import com.sforce.soap.apex.Connector;
import com.sforce.soap.apex.ExecuteAnonymousResult;
import com.sforce.soap.apex.LogCategory;
import com.sforce.soap.apex.LogCategoryLevel;
import com.sforce.soap.apex.LogInfo;
import com.sforce.soap.apex.LogType;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.apex.*;
import com.sforce.soap.metadata.*;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.soap.tooling.*;
import com.sforce.soap.tooling.RunTestsRequest;
import com.sforce.soap.tooling.RunTestsResult;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import org.mortbay.util.ajax.JSON;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import project.Rules.VisualforcePageRule;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToolingHelper {
    private String username;
    private String pass;
    public MetadataConnection metadataConnection;
    public ToolingHelper(String username, String pass) {
        this.username = username;
        this.pass = pass;
        try {
            getConnection();
        } catch (ConnectionException ex) {
            System.out.println( this.username + ".ToolingHelper >> Connection Exception: " + ex);
        }
    }

    private void getConnection() throws ConnectionException {
         this.metadataConnection = MetadataLoginUtil.login(this.username, this.pass);
    }

    public String executeAnonymousWithReturnStringDebug(String apexCode) {
        String debug = "none(((";
        ConnectorConfig soapConfig = new ConnectorConfig();
        soapConfig.setAuthEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl());
        soapConfig.setServiceEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl().replace("/u/", "/s/"));
        soapConfig.setSessionId(MetadataLoginUtil.mapUserToSessionId.get(this.username));
        try {

            SoapConnection connection = new SoapConnection(soapConfig);
            LogInfo infoApex = new LogInfo();
            infoApex.setCategory(LogCategory.Apex_code);
            infoApex.setLevel(LogCategoryLevel.Debug);
            connection.setDebuggingHeader(new LogInfo[] { infoApex }, LogType.Debugonly);
            ExecuteAnonymousResult result  = connection.executeAnonymous(apexCode);
            debug = connection.getDebuggingInfo().getDebugLog();
            if (result.isCompiled()) {
                if (result.isSuccess()) {
                    System.out.println("Apex code excuted sucessfully");
                    System.out.println(">>" + debug + "<<");
                } else {
                    throw new RuntimeException("Apex code execution failed :" + result.getExceptionMessage());
                }
            } else {
                throw new RuntimeException("Apex code compilition failed :"
                        + result.getCompileProblem());
            }
        } catch (ConnectionException ex) {
            System.out.println( this.username + ".ToolingHelper >>executeAnonymousWithReturnStringDebug>> Connection Exception: " + ex);
        }
        return debug;
    }


    public void createApexClass(String classBody) {

        ConnectorConfig soapConfig = new ConnectorConfig();
        soapConfig.setAuthEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl());
        soapConfig.setServiceEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl());
        soapConfig.setSessionId(MetadataLoginUtil.mapUserToSessionId.get(this.username));



        try {


//        ApexClass apexClass = new ApexClass();
//        apexClass.setBody(classBody);
//        ApexClass[] classes = { apexClass };
//           PartnerConnection connection = new PartnerConnection(soapConfig);
//            MetadataConnection connection = new MetadataConnection(soapConfig);
//            SoapConnection connection = new SoapConnection(soapConfig);
//            MetadataContainer Container = new MetadataContainer();
//            Container.setName("SampleContainer2");
//            MetadataContainer[] Containers = { Container };
////            SaveResult[] containerResults = connection.
            SoapConnection con =  com.sforce.soap.apex.Connector.newConnection(soapConfig);
            MetadataContainer Container = new MetadataContainer();
            Container.setName("SampleContainer2");

            MetadataContainer[] Containers = { Container };
            String[] classes = { classBody };
            CompileClassResult[] containerResults = con.compileClasses( classes );

//            containerResults.
//            MetadataContainer container = new MetadataContainer();
//            container.setName("dsfsd");

//            Metadata meta = new Metadata();
//            meta.
//            SaveResult[] saveResult = metadataConnection.createMetadata();
//
//            ApexClassMember createClassMember = new ApexClassMember();
//            createClassMember.setFullName("Messages");
//            createClassMember.setBody(classBody);
//            ApexClassMember[] createClassMembers = { createClassMember };
//            connection.createMetadata(createClassMembers);
            // create an ApexClass object and set the body

//            ApexClass apexClass = new ApexClass();
//            apexClass.setBody(classBody);
//            ApexClass[] classes = { apexClass };



//            createClassMember.se

//                    com.sforce.soap.partner.SaveResult saveResult = connection.create(new SObject[][] { classes });

        } catch (ConnectionException ex) {
            System.out.println( this.username + " .ToolingHelper >>executeAnonymousWithReturnStringDebug>> Connection Exception: " + ex);
        }

//// create an ApexClass object and set the body
//        ApexClass apexClass = new ApexClass();
//        apexClass.Body = classBody;
//
//        ApexClass[] classes = { apexClass };
//// call create() to add the class
//        SaveResult[] saveResults = sforce.create(classes);
//        for (int i = 0; i < saveResults.Length; i++)
//        {
//            if (saveResults[i].success)
//            {
//                Console.WriteLine("Successfully created Class: " +
//                        saveResults[i].id);
//            }
//            else
//            {
//                Console.WriteLine("Error: could not create Class ");
//                Console.WriteLine("   The error reported was: " +
//                        saveResults[i].errors[0].message + "\n");
//            }
//        }
    }


    public SObject[] runQuery(String query) {
        ConnectorConfig soapConfig = new ConnectorConfig();
        soapConfig.setAuthEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl());
        soapConfig.setServiceEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl());
        soapConfig.setSessionId(MetadataLoginUtil.mapUserToSessionId.get(this.username));
        System.out.println("********");
        System.out.println(MetadataLoginUtil.mapUserToSessionId.get(this.username));
        System.out.println(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl());
        try {
            PartnerConnection connection = new PartnerConnection(soapConfig);
            com.sforce.soap.partner.QueryResult result = connection.query(query);
            System.out.println("getSize");
            System.out.println(result.getSize());
            for (SObject record : result.getRecords()) {
                System.out.println("###### record.Id: " + (String)record.getField("Id"));
                System.out.println("###### record.Name: " + (String)record.getField("Name"));
            }
            return result.getRecords();
        } catch (ConnectionException ex) {
            System.out.println( this.username + ".ToolingHelper >>runQuery>> Connection Exception: " + ex);
        }
        return null;
    }
//    https://eugenebagaev-dev-ed--c.ap7.content.force.com/secur/contentDoor?startURL=https%3A%2F%2Feugenebagaev-dev-ed.my.salesforce.com%2Fapex%2FActionFunctionPage&sid=00D28000001iPqh%21AREAQCb_P6f_EgI8oPU7LCrl.ZShTXH9.I3dKDfW95P.EN1_gmU2tDHUl5klQmr2XFdLmWi6ui4QD5KDwhY0yKWI.OiqgLDP&skipRedirect=1&lm=eyJlbmMiOiJBMjU2R0NNIiwiYXVkIjoiMDBEMjgwMDAwMDFpUHFoIiwia2lkIjoie1widFwiOlwiMDBEMjgwMDAwMDFpUHFoXCIsXCJ2XCI6XCIwMkcwSTAwMDAwMGwyZ3pcIixcImFcIjpcImNvbnRlbnRkb29ydXNlcnRyYW5zaWVudGtleWVuY3J5cHRcIixcInVcIjpcIjAwNTI4MDAwMDA0WGIzWlwifSIsImNyaXQiOlsiaWF0Il0sImlhdCI6MTU1NzQ0NjgwNzQxNiwiZXhwIjowfQ%3D%3D..ZTIRIAwSl7QTZaSc.a8AUYiLW0pNZ6wu6RcuSdg%3D%3D.kPjWhzSBk1g5gq01LFkiwQ%3D%3D
    public String getSessionId() {
        com.sforce.soap.metadata.SessionHeader_element ee = metadataConnection.getSessionHeader();

        try {

            ListMetadataQuery query = new ListMetadataQuery();
            query.setType("ApexPage");
//            query.setType("CustomObject");
            FileProperties[] listMeta = metadataConnection.listMetadata(new ListMetadataQuery[]{query},43.00);
            System.out.println("***************");
            for (FileProperties fp : listMeta) {
                System.out.println(fp.getFullName());
            }
            System.out.println("***************");

        } catch (ConnectionException ce) {
            ce.printStackTrace();
        }
        return ee.getSessionId();

    }

    public Map<String, String> getApexPagesAndLink() {
        Map<String, String> pageLink = new HashMap();
        try {
            ListMetadataQuery query = new ListMetadataQuery();
            query.setType("ApexPage");
            FileProperties[] listMeta = metadataConnection.listMetadata(new ListMetadataQuery[]{query},TaskMapping.VERSION);

            com.sforce.soap.metadata.SessionHeader_element ee = metadataConnection.getSessionHeader();
            String sessia = ee.getSessionId();

            String link = MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl();
            String linkSubstr = link.substring(0,link.indexOf("/services"));
            String url =  linkSubstr + "/secur/frontdoor.jsp?sid=" + sessia + "&retURL=" + linkSubstr + "/apex/";
            for (FileProperties fp : listMeta) {
                pageLink.put(fp.getFullName(), url + fp.getFullName());
            }
        } catch (ConnectionException ce) {
            ce.printStackTrace();
        }
        return pageLink;
    }

    public String getApexPageMetadata(String page) {
        System.out.println("getApexPageMetadata");
        TaskMapping.METADATA_CHECK = new HashMap<>();
        TaskMapping.METADATA_CHECK.put(page, new VisualforcePageRule());

        TaskMapping.generatePackageXML();
        System.out.println("getApexPageMetadata");
        DeployRetrieveHelper instance = new DeployRetrieveHelper(username, pass);
        instance.retrieveZipWithoutSave();
        System.out.println("getApexPageMetadata");
//        try {
//
//
//        } catch (ConnectionException ce) {
//            ce.printStackTrace();
//        }
        return "";
    }


//        public static void generatePackageXML(String typesMeta, String memberMeta){
//            try {
//                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//                // root elements
//                Document doc = docBuilder.newDocument();
//                Element rootElement = doc.createElement("Package");
//                doc.appendChild(rootElement);
//                Attr attr = doc.createAttribute("xmlns");
//                attr.setValue("http://soap.sforce.com/2006/04/metadata");
//                rootElement.setAttributeNode(attr);
//                // types
//
//                Element types = doc.createElement("types");
//                rootElement.appendChild(types);
//
//                Element members = doc.createElement("members");
//                members.appendChild(doc.createTextNode(memberMeta));
//                types.appendChild(members);
//
//                Element nameMembers = doc.createElement("name");
//                nameMembers.appendChild(doc.createTextNode(typesMeta));
//                types.appendChild(nameMembers);
//
//                Element version = doc.createElement("version");
//                version.appendChild(doc.createTextNode(String.valueOf(43.0)));
//                rootElement.appendChild(version);
//// save XML
//                TransformerFactory tf = TransformerFactory.newInstance();
//                Transformer transformer = tf.newTransformer();
////            StringWriter writer = new StringWriter();
//                transformer.transform(new DOMSource(doc), new StreamResult(new File(PathToXMLFile)));
////            System.out.println(writer.getBuffer().toString());
//            } catch (ParserConfigurationException | TransformerException e ) {
//                e.printStackTrace();
//            }
//        }




}
