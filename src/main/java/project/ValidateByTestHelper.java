package project;

import com.sforce.soap.apex.CodeCoverageResult;
import com.sforce.soap.apex.CodeLocation;
import com.sforce.soap.apex.SoapConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import project.Processors.RequestProcessor;
import project.REST.API.PageRestController;
import project.Rules.Constants;
import project.Rules.Results;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidateByTestHelper {

    public List<Results> results;

    public ValidateByTestHelper() {

    }

    public List<Results> validateUserResultUsingTest(String un) {

        List<Results> res = new ArrayList<>();

        String debugThreadName = un;
        System.out.println(debugThreadName + ": >> Test Run");
        SoapConnection connection;
        ConnectorConfig soapConfig = new ConnectorConfig();

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
                res.add(new Results("Test", MessageFormat.format(Constants.TEST_FAILED, item.getName()), false));
            }
            if (result.getCodeCoverage() != null) {

                for (CodeCoverageResult ccr : result.getCodeCoverage()) {

                    for (String nameTestClass : TaskMapping.TEST_CLASSES.keySet()) {

                        if (TaskMapping.TEST_CLASSES.get(nameTestClass).equals(ccr.getName())) {

Integer percentCoverage = checkCoverage((ccr.getNumLocations() - ccr.getNumLocationsNotCovered()), ccr.getNumLocations());
if(percentCoverage < 75){
    res.add(new Results("Test", MessageFormat.format(Constants.TEST_SUCCESS_BUT_NOT_ENOUGH_COVERAGE, nameTestClass, percentCoverage), false));
} else {
    res.add(new Results("Test", MessageFormat.format(Constants.TEST_SUCCESS,  nameTestClass, percentCoverage), true));
}
                            System.out.println(debugThreadName + ": >> Test Result For Class: " + TaskMapping.TEST_CLASSES.get(nameTestClass));
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
            return res;
    }

    private Integer checkCoverage(Integer summNumLocationsCovered, Integer summNumLocations) {
        return summNumLocationsCovered * 100 / summNumLocationsCovered;
    }

    private String[] setStringToArray(Set<String> originalSet) {
        int n = originalSet.size();
        String arr[] = new String[n];

        int i = 0;
        for (String x : originalSet)
            arr[i++] = x;
        return arr;
    }

}
