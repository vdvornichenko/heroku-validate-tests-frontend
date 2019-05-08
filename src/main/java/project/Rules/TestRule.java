package project.Rules;

import com.sforce.soap.apex.CodeCoverageResult;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import project.MetadataLoginUtil;
import project.TaskMapping;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TestRule  implements  Rule {

    public String nameUser;
    public List<String> nameClasses;

    public static String templateFailedTests =  "Failed Tests: {0}";

    public TestRule(String nameUser){
        this.nameUser = nameUser;
    }

    public List<Results> checkCondition(String nameTest){
        List<Results> results = new ArrayList<>();
        validateUserResultUsingTest(nameUser);
        return results;
    }

    private void validateUserResultUsingTest(String un) {
        String resultForUser = "";
        List<Results> results = new ArrayList<>();
        String debugThreadName = un;
        System.out.println(debugThreadName + ": >> Test Run");


        SoapConnection connection;
        ConnectorConfig soapConfig = new ConnectorConfig();
       // results.add(new Results(item, MessageFormat.format(templateNotFoundFile, item), false));
        try {

            System.out.println("*****************" + debugThreadName + " >> Un: " + un);
            System.out.println("*****************" + debugThreadName + " >> SI " + MetadataLoginUtil.mapUserToSessionId.get(un));
//            Thread.sleep(3000);
            soapConfig.setAuthEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(un).getServerUrl());
            soapConfig.setServiceEndpoint(MetadataLoginUtil.mapUserToLoginResult.get(un).getServerUrl().replace("/u/", "/s/"));
            soapConfig.setSessionId(MetadataLoginUtil.mapUserToSessionId.get(un));

            connection = new SoapConnection(soapConfig);

            com.sforce.soap.apex.RunTestsRequest request = new com.sforce.soap.apex.RunTestsRequest();

            request.setClasses(setStringToArray(TaskMapping.TEST_CLASSES.keySet()));

            com.sforce.soap.apex.RunTestsResult result = connection.runTests(request);

            for (com.sforce.soap.apex.RunTestFailure item : result.getFailures()) {
                System.out.println("Failed Tests: " + item.getName());
                resultForUser += "Failed Tests:" + item.getName();
//                results.add(new Results("Tests", MessageFormat.format(templateFailedTests, item.getName()), false));

            }

            if (result.getCodeCoverage() != null) {

                for (CodeCoverageResult ccr : result.getCodeCoverage()) {

                    for (String item : TaskMapping.TEST_CLASSES.keySet()) {

                        if (TaskMapping.TEST_CLASSES.get(item).equals(ccr.getName())) {
                            resultForUser += ": Test Result For Class: " + TaskMapping.TEST_CLASSES.get(item);
                            System.out.println(debugThreadName + ": >> Test Result For Class: " + TaskMapping.TEST_CLASSES.get(item));

                            resultForUser += ": Coverage: " + (ccr.getNumLocations() - ccr.getNumLocationsNotCovered()) + " of " + ccr.getNumLocations() + "\n";
                            System.out.println(debugThreadName + ": >> Coverage: " + (ccr.getNumLocations() - ccr.getNumLocationsNotCovered()) + " of " + ccr.getNumLocations()
                            );
                        }
                    }
                }
            }



        } catch (ConnectionException ex) {
            System.out.println( debugThreadName + " >> ValidateByTestEx: " + ex);
        } catch (Exception commEx) {
            System.out.println("Ex: " + commEx);
        }

    }

    private String[] setStringToArray(Set<String> originalSet) {
        int n = originalSet.size();
        String arr[] = new String[n];

        int i = 0;
        for (String x : originalSet)
            arr[i++] = x;
        return arr;
    }

    private Integer countPercentCoverage(Integer summNumLocations, Integer notCovered) {
        return notCovered * 100 / summNumLocations;
    }

}
