package project.REST.API;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.Processors.RequestProcessor;
import project.Rules.Results;


import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.util.*;

@RestController
public class PageRestController {

    @PostMapping("/usersInfo")
    public Map<String, List<Results>> getUsersInfo(@RequestBody String userNames) {
        return new RequestProcessor(userNames).getUsersInfo();
    }


    @PostMapping("/test")
    public void test(@RequestBody String userNames) {
        new RequestProcessor(userNames).test();
    }


//    @PostMapping("/test")
//    public @ResponseBody
//    ResponseEntity<String> test() {
//
//        return new ResponseEntity<String>("POST Response", HttpStatus.OK);
////        return new ResponseEntity<String>("Post", HttpStatus.OK);
//    }


}
