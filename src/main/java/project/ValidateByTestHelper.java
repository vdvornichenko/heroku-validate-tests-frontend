package project;

import com.sforce.soap.apex.*;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import project.Processors.RequestProcessor;
import project.REST.API.PageRestController;
import project.Rules.CheckExecuteMethodWraper;
import project.Rules.Constants;
import project.Rules.Results;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidateByTestHelper {

    public String username;

    public ValidateByTestHelper(String username) {
        this.username = username;
    }

//
//
//    public List<Results> validateUserResultUsingTest() {
//
//        List<Results> res = new ArrayList<>();
//
//        String debugThreadName = this.username;
//        System.out.println(debugThreadName + ": >> Test Run");
//        SoapConnection connection;
//        ConnectorConfig soapConfig = new ConnectorConfig();
//
//        try {
//
//            System.out.println("*****************" + debugThreadName + " >> Un: " + this.username);
//            System.out.println("*****************" + debugThreadName + " >> SI " + MetadataLoginUtil.mapUserToSessionId.get(this.username));
//
////            Thread.sleep(3000);
//
//            soapConfig.setAuthEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl());
//            soapConfig.setServiceEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl().replace("/u/", "/s/"));
//            soapConfig.setSessionId(MetadataLoginUtil.mapUserToSessionId.get(this.username));
//
//            connection = new SoapConnection(soapConfig);
//
//            com.sforce.soap.apex.RunTestsRequest request = new com.sforce.soap.apex.RunTestsRequest();
//
//            request.setClasses(setStringToArray(TaskMapping.TEST_CLASSES.keySet()));
//            com.sforce.soap.apex.RunTestsResult result = connection.runTests(request);
//            for (com.sforce.soap.apex.RunTestFailure item : result.getFailures()) {
//                System.out.println("Failed Tests: " + item.getName());
//                res.add(new Results("Test", MessageFormat.format(Constants.TEST_FAILED, item.getName()), false));
//            }
//
//            if (result.getCodeCoverage() != null) {
//
//                for (String nameTestClass : TaskMapping.TEST_CLASSES.keySet()) {
//                    Boolean testRun = false;
//                    for (CodeCoverageResult ccr : result.getCodeCoverage()) {
//                        if (TaskMapping.TEST_CLASSES.get(nameTestClass).equals(ccr.getName())) {
//                            testRun = true;
//                            Integer percentCoverage = checkCoverage((ccr.getNumLocations() - ccr.getNumLocationsNotCovered()), ccr.getNumLocations());
//                            if(percentCoverage < 75){
//                                res.add(new Results("Test", MessageFormat.format(Constants.TEST_SUCCESS_BUT_NOT_ENOUGH_COVERAGE, nameTestClass, percentCoverage), false));
//                            } else {
//                                res.add(new Results("Test", MessageFormat.format(Constants.TEST_SUCCESS,  nameTestClass, percentCoverage), true));
//                            }
//                            System.out.println(debugThreadName + ": >> Test Result For Class: " + TaskMapping.TEST_CLASSES.get(nameTestClass));
//                            System.out.println(debugThreadName + ": >> Coverage: " + (ccr.getNumLocations() - ccr.getNumLocationsNotCovered()) + " of " + ccr.getNumLocations()
//                            );
//                        }
//                    }
//                    if(!testRun) res.add(new Results("Test", MessageFormat.format(Constants.TEST_NOT_FOUND,  nameTestClass), false));
//
//                }
//            } else{
//                System.out.println(" else: ");
//            }
//
//        } catch (ConnectionException ex) {
//            System.out.println( debugThreadName + " >> ValidateByTestEx: " + ex);
//        } catch (Exception commEx) {
//            System.out.println("Ex: " + commEx);
//        }
//        System.out.println(" end: ");
//        return res;
//    }
//
//    private Integer checkCoverage(Integer summNumLocationsCovered, Integer summNumLocations) {
//        if (summNumLocationsCovered == 0) return 0;
//        Integer total = summNumLocationsCovered * 100 / summNumLocations;
//        return total;
//    }
//
//    private String[] setStringToArray(Set<String> originalSet) {
//        int n = originalSet.size();
//        String arr[] = new String[n];
//
//        int i = 0;
//        for (String x : originalSet)
//            arr[i++] = x;
//        return arr;
//    }
//
//    public List<Results> validateApexMethod( List<CheckExecuteMethodWraper> checkExecuteMethods) {
//        System.out.println("validateApexMethod");
//        List<Results> res = new ArrayList<>();
//        for(CheckExecuteMethodWraper methodWraper : checkExecuteMethods){
//            String apexCode = methodWraper.stringExecuted;
//            System.out.println(apexCode);
//            res.add(executeAnonymousWithReturnStringDebug(apexCode, methodWraper));
//        }
//        return res;
//    }
//
//
//    public Results executeAnonymousWithReturnStringDebug(String apexCode, CheckExecuteMethodWraper methodWraper) {
//        String debug = "none(((";
//        ConnectorConfig soapConfig = new ConnectorConfig();
//        soapConfig.setAuthEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl());
//        soapConfig.setServiceEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(this.username).getServerUrl().replace("/u/", "/s/"));
//        soapConfig.setSessionId(MetadataLoginUtil.mapUserToSessionId.get(this.username));
//        try {
//            SoapConnection connection = new SoapConnection(soapConfig);
//            LogInfo infoApex = new LogInfo();
//            infoApex.setCategory(LogCategory.Apex_code);
//            infoApex.setLevel(LogCategoryLevel.Debug);
//            connection.setDebuggingHeader(new LogInfo[] { infoApex }, LogType.Debugonly);
//            ExecuteAnonymousResult result  = connection.executeAnonymous(apexCode);
//            debug = connection.getDebuggingInfo().getDebugLog();
//            if (result.isCompiled()) {
//                if (result.isSuccess()) {
////                    new Results(nameClass, MessageFormat.format(Constants.APEXCLASS_FOUND_METHOD, nameClass, method), true)
//                    System.out.println("Apex code excuted sucessfully");
//                    System.out.println(">>" + debug + "<<");
//                    return new Results(methodWraper.nameClass, MessageFormat.format(Constants.METHOD_SUCCESS, methodWraper.nameMethod, methodWraper.nameClass), true);
//                } else {
//                    return new Results(methodWraper.nameClass, MessageFormat.format(Constants.METHOD_EXECUTE_FAIL, methodWraper.nameMethod, methodWraper.nameClass), false);
////                    throw new RuntimeException("Apex code execution failed :" + result.getExceptionMessage());
//                }
//            } else {
//                return new Results(methodWraper.nameClass, MessageFormat.format(Constants.METHOD_NOT_COMPILE, methodWraper.nameMethod, methodWraper.nameClass), false);
////                throw new RuntimeException("Apex code compilition failed :" + result.getCompileProblem());
//            }
//        } catch (ConnectionException ex) {
//            System.out.println( this.username + ".ToolingHelper >>executeAnonymousWithReturnStringDebug>> Connection Exception: " + ex);
//        }
//        return new Results(methodWraper.nameClass, MessageFormat.format(Constants.METHOD_NOT_COMPILE, methodWraper.nameMethod, methodWraper.nameClass), false);
//    }
//
//

}