package project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import project.Processors.RequestProcessor;

@SpringBootApplication
public class Application {
    public static String zz = "ssssssss";
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }
}
