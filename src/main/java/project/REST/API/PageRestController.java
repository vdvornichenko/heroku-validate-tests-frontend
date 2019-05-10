package project.REST.API;

        import org.springframework.web.bind.annotation.*;
        import project.Processors.RequestProcessor;
        import project.Rules.Results;
        import project.ToolingHelper;

        import java.util.*;

@RestController
public class PageRestController {

    @CrossOrigin
    @PostMapping("/usersInfo")
    public Map<String, List<Results>> getUsersInfo(@RequestBody String userNames) {
        return new RequestProcessor(userNames).getUsersInfo();
    }

    public static class User {
        public String userName;
        public String pass;
        public String data = "";
    }

    @CrossOrigin
    @GetMapping("/getUsers")
    public List<RequestProcessor.CredentialsStorage> getAllUsers() {
        return new RequestProcessor().getUserLogins();
    }
}







//OLLLDDD
//package project.REST.API;
//        import org.springframework.web.bind.annotation.*;
//        import project.Processors.RequestProcessor;
//        import project.Rules.Results;
//        import java.util.*;
//
//@RestController
//public class PageRestController {
//
//    @PostMapping("/usersInfo")
//    public Map<String, List<Results>> getUsersInfo(@RequestBody String userNames) {
//        return new RequestProcessor(userNames).getUsersInfo();
//    }


//    @CrossOrigin()
//    @PostMapping("/userListApexPages")
//    public Map<String, String> getUserApexPages(@RequestBody User user) {
//        System.out.println("***************");
//        System.out.println(user.userName);
//        System.out.println(user.pass);
//        ToolingHelper toolHlp = new ToolingHelper(user.userName, user.pass);
//        Map<String, String> pageLink = toolHlp.getApexPagesAndLink();
//        return pageLink;
//    }
//
//    @CrossOrigin()
//    @PostMapping("/userApexPage")
//    public String getApexPageMetadata(@RequestBody User user) {
//        ToolingHelper toolHlp = new ToolingHelper(user.userName, user.pass);
//        System.out.println("***************");
//        System.out.println(user.userName);
//        System.out.println(user.pass);
//        System.out.println(user.data);
//        String markupPage = toolHlp.getApexPageMetadata(user.data);
//        System.out.println(markupPage);
//        return markupPage;
//    }
//
//    @PostMapping("/test")
//    public void test(@RequestParam(name = "query", required = false) String name) {
//        ToolingHelper toolHlp = new ToolingHelper("eugene.bagaev@gmail.com", "Gp14kost");
//        toolHlp.getApexPageMetadata("ActionFunctionPage.Page");
//    }
