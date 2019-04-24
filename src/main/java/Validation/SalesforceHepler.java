package Validation;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SalesforceHepler {

    public static long startTime = System.currentTimeMillis();

    private String tempUsername;
    private String tempPassword;

    public static String zip_file_for_read = "";
    private static Map<String, List<String>> mapping = TaskMapping.CLASS_ACCOUNT;

    public SalesforceHepler(String username, String password) {
        this.tempUsername = username;
        this.tempPassword = password;
    }

    public static void checkUsersResults() throws InterruptedException {

        if (GoogleHelper.userCreds.size() == 0) {
            System.out.println("No users creds found in Google File");
            System.exit(1);
        }

//         Stress test
//        for (Integer count = 0; count < 50; count++) { // for stress test
//            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> Test: " + count); // for stress test
            for (String item : GoogleHelper.userCreds.keySet()) {

                SalesforceHandlerThread thread = new SalesforceHandlerThread(
                        item,
                        GoogleHelper.userCreds.get(item),
                        "User: " + item
                );
                thread.start();
//            } // for stress test
//            Thread.sleep(10000); // for stress test
        }
    }

    public void processUser() {

        DeployRetrieveHelper instance = new DeployRetrieveHelper(tempUsername, tempPassword);

        instance.retrieveZip();

        readZipFile();
    }

    private void readZipFile() {

        try {

            ZipFile file = new ZipFile(zip_file_for_read);

            for (ZipEntry e : Collections.list(file.entries())) {

                for (String item : mapping.keySet()) {

                    if (e.getName().contains(item) && !e.getName().contains(".xml")) {
                        System.out.println(Thread.currentThread().getName() + ". >> Found class: " + item);

                        checkMethodsInFile(e, item, file);
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println("ioEx.SFHelper.readZip: " + ex.getMessage());
        }
    }

    private void checkMethodsInFile(ZipEntry entry, String className, ZipFile file) {

        InputStream stream = null;

        try {
            stream = file.getInputStream(entry);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {

                while (br.read() != -1) {

                    String currentLine = br.readLine();

                    if (!(currentLine == null)) {

                        for (String item : mapping.get(className)) {

                            if (currentLine.contains(item) && currentLine != null) {
                                System.out.println(Thread.currentThread().getName() + ". >> Found Method: " + item);
                                System.out.println(Thread.currentThread().getName() + ". >> Line: " + currentLine);

                                validateTasksByRunTests();
                            }
                        }
                    }
                }

            } catch (IOException ex) {
                System.out.println(Thread.currentThread().getName() + ". >> ioEx.SFHelper.checkMeth.readClass: " + ex.getMessage());
            }

        } catch (IOException ioEx) {
            System.out.println(Thread.currentThread().getName() + ". >> ioEx.sfHelper.checkMeth" + ioEx.getMessage());
        } finally {

            try {
                stream.close();
            } catch (IOException ex) {
                System.out.println(Thread.currentThread().getName() + ". >> Exception in closing ZipEntry Stream");
            }

        }
    }

    private void validateTasksByRunTests() {

        ValidateByTestHelper helper = new ValidateByTestHelper(tempUsername);

    }

}
