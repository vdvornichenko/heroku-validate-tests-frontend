package project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sforce.soap.partner.sobject.SObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import project.Processors.RequestProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class GreetingController {
    public static String resultForUser = "";

//    @GetMapping
//    public String dataInput(Model model) throws InterruptedException {
//
//        GoogleHelper.callDocument();
//        model.addAttribute("userCredentials", getUserLogins(GoogleHelper.userCreds));
//        model.addAttribute("results", "");
//        SalesforceHepler.checkUsersResults();
//        while (Thread.activeCount() != 2) {
//            TimeUnit.SECONDS.sleep(1);
//        }
//        System.out.println("----------\n" + resultForUser);
//        model.addAttribute("results", resultForUser);
//        return "inputPage";
//    }

//    public List<CredentialsStorage> getUserLogins(Map<String, String> creds) {
//        List<CredentialsStorage> credentials = new ArrayList<>();
//        for(Map.Entry<String, String> userCreds : creds.entrySet()) {
//            credentials.add(new CredentialsStorage(userCreds));
//        }
//        return credentials;
//    }

//    public class CredentialsStorage {
//        public String userName;
//        public String password;
//
//        public CredentialsStorage(Map.Entry<String, String> userCreds) {
//            userName = userCreds.getKey();
//            password = userCreds.getValue();
//        }
//    }


    @PostMapping ("url")
    public String url(HttpServletResponse httpServletResponse) {
        try {
//            String name = "nastya@lear.cen";
//            String pass = "naspuh2018$";
            String name = "eugene.bagaev@gmail.com";
            String pass = "Gp14kost";
//            ToolingHelper hlp = new ToolingHelper(name, pass);
//             https://ap8.salesforce.com/apex/TestPage
//            https://eugenebagaev-dev-ed--c.ap7.visual.force.com/apex/ActionFunctionPage
            String link = MetadataLoginUtil.mapUserToLoginResult.get(name).getServerUrl();
            System.out.println(link.substring(0,link.indexOf("/services")) + "/apex/TestPage");
//            String ses = hlp.getSessionId();
//            httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
//            System.out.println(hlp.getSessionId());
//            httpServletResponse.sendRedirect(link.substring(0,link.indexOf("/services")) + "/secur/frontdoor.jsp?sid=" + ses + "&retURL=" + link.substring(0,link.indexOf("/services")) + "/apex/ActionFunctionPage");
//            httpServletResponse.sendRedirect(link.substring(0,link.indexOf("/services")) + "/secur/frontdoor.jsp?sid=" + ses + "&retURL=" + link.substring(0,link.indexOf("/services")) + "/apex/TestPage");

        } catch (Exception e){
        }
        return "inputPage";
    }

    @PostMapping ("query")
    public String url(@RequestParam(name = "query", required = false) String query, Model model) {
//        System.out.println(query);
//        ToolingHelper hlp = new ToolingHelper("eugene.bagaev@gmail.com", "Gp14kost");
//        com.sforce.soap.partner.sobject.SObject[] sObj =  hlp.runQuery(query);
//        String res = "";
//        for(com.sforce.soap.partner.sobject.SObject s:sObj){
//            res = res +s.getSObjectField("Name") + ",  ";
//        }
//        model.addAttribute("results", res);
        return "inputPage";
    }

    @PostMapping ("runCode")
    public String runCode(@RequestParam(name = "runCode", required = false) String runCode, Model model) {
//        System.out.println(runCode);
//        ToolingHelper hlp = new ToolingHelper("eugene.bagaev@gmail.com", "Gp14kost");
//        String res =  hlp.executeAnonymousWithReturnStringDebug(runCode);
//        model.addAttribute("results", res);
        return "inputPage";
    }

    @PostMapping ("apexClass")
    public String createApexClass(@RequestParam(name = "text", required = false) String text, Model model) {
       System.out.println(text);
//        ToolingHelper hlp = new ToolingHelper("eugene.bagaev@gmail.com", "Gp14kost");
//        hlp.createApexClass(text);
        return "inputPage";
    }
}
