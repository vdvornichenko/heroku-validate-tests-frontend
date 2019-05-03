package project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class GreetingController {
    public static String resultForUser = "";

    @GetMapping
    public String dataInput(Model model) throws InterruptedException {

        GoogleHelper.callDocument();
        model.addAttribute("userCredentials", getUserLogins(GoogleHelper.userCreds));
        model.addAttribute("results", "");
//        SalesforceHepler.checkUsersResults();
//        while (Thread.activeCount() != 2) {
//            TimeUnit.SECONDS.sleep(1);
//        }
//        System.out.println("----------\n" + resultForUser);
//        model.addAttribute("results", resultForUser);
        return "inputPage";
    }

    public List<CredentialsStorage> getUserLogins(Map<String, String> creds) {
        List<CredentialsStorage> credentials = new ArrayList<>();
        for(Map.Entry<String, String> userCreds : creds.entrySet()) {
            credentials.add(new CredentialsStorage(userCreds));
        }
        return credentials;
    }

    public class CredentialsStorage {
        public String userName;
        public String password;

        public CredentialsStorage(Map.Entry<String, String> userCreds) {
            userName = userCreds.getKey();
            password = userCreds.getValue();
        }


    }

}
