package project.REST.API;

import org.springframework.web.bind.annotation.*;
import project.Processors.RequestProcessor;
import project.Rules.Results;

import java.util.*;

@RestController
public class PageRestController {

    @CrossOrigin
    @PostMapping("/usersInfo")
    public Map<String, List<Results>> getUsersInfo(@RequestBody String userNames) {
        return new RequestProcessor(userNames).getUsersInfo();
    }

    @CrossOrigin
    @GetMapping("/getUsers")
    public List<RequestProcessor.CredentialsStorage> getAllUsers() {
        return new RequestProcessor().getUserLogins();
    }
}
