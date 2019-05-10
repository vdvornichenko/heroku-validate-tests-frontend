package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }
}



//package project;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//@SpringBootApplication
//public class Application {
//    public static Map<String, Boolean> areThereCreds = new HashMap<>();
//    public static String allInfo = "";
//
//    public static void main(String[] args) throws InterruptedException {
//        SpringApplication.run(Application.class, args);
//    }
//
//    private static Boolean isAllValuesIsFilled() {
//        for (Boolean b : areThereCreds.values()) {
//            if(b) continue;
//            return false;
//        }
//        return true;
//    }
//}