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


}