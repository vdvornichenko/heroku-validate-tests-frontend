package project;

import com.sforce.soap.apex.SoapConnection;
import com.sforce.soap.apex.*;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

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


    public void createApexClass(String apexCode) {

    }


    public void runTest(String apexCode) {

    }


    public void runQuery(String apexCode) {

    }

    public void linkOnVisualPape(String apexCode) {

    }

}
