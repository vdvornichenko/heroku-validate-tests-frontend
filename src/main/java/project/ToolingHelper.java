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

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.util.List;

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

}
