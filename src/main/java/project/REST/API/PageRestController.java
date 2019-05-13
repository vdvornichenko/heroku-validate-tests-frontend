package project.REST.API;

import org.springframework.web.bind.annotation.*;
import project.Processors.RequestProcessor;
import project.Rules.Results;
import project.Storages.FileStorage;
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

    @CrossOrigin
    @PostMapping("/userFile")
    public FileStorage getUserFile(@RequestBody String userNameAndFile) {
        String[] info = userNameAndFile.split(";");
        for (FileStorage file : RequestProcessor.files) {
            if (!info[0].equalsIgnoreCase(file.fileOwner) || !file.fileName.contains(info[1])) continue;
            return file;
        }
        return null;
    }
}