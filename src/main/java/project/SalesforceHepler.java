package project;

import project.Rules.*;

import project.Processors.RequestProcessor;
import project.Storages.FileStorage;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class SalesforceHepler {

    public static long startTime = System.currentTimeMillis();

    private String tempUsername;
    private String tempPassword;

    public static String zip_file_for_read = "";
    private static Map<String, Rule> mapping = TaskMapping.METADATA_CHECK;

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
                        item
                );
                thread.start();
//            } // for stress test
//            Thread.sleep(10000); // for stress test
        }
    }

    public void processUser() {

        DeployRetrieveHelper instance = new DeployRetrieveHelper(tempUsername, tempPassword);

        instance.retrieveZip();
        //readZipFile();
        System.out.println(tempUsername);

        checkZipFile();
    }

    private List<Results> checkZipFile() {

        List<Results> results = new ArrayList<>();
        try {
            ZipFile file = new ZipFile(zip_file_for_read);
            for (String item : mapping.keySet()) {
                Enumeration< ? extends ZipEntry > e = file.entries();
                boolean fileFound = false;
                while (e.hasMoreElements()) {
                    ZipEntry entry = e.nextElement();
                    if (entry.getName().contains(item) && !entry.getName().contains(".xml")) {
                        fileFound = true;
                        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(entry)));
                        String allFile = "";
                        String theFile = "";
                        String line = null;
                        while ((line = br.readLine()) != null){
                            allFile = allFile + line;
                            theFile += "<br/>" + line.replaceAll(" ", "&nbsp;").replaceAll("<", "&lt").replaceAll(">", "&gt;");
                        }

                        RequestProcessor.files .add(new FileStorage(item, tempUsername, theFile));
                        results.addAll(mapping.get(item).checkCondition(allFile));
                        break;
                    }
                }
                // not found file
                if (!fileFound){
                    results.add(new Results(item, "NOT FOUND FILE " + item, false));
                }
            }
        } catch (IOException ex) {
            System.out.println("ioEx.SFHelper.readZip: " + ex.getMessage());
        }
        for (Results res : results) {
            System.out.println(">>> " + res.status + " " + res.nameMetadata + " " +  res.message);
        }
        System.out.println(">>>>>>>>>>>>> ");
        System.out.println(tempUsername);
        RequestProcessor.userResults.put(tempUsername, results);
        return results;
    }







    private void readZipFile() {

//        try {
//
//            ZipFile file = new ZipFile(zip_file_for_read);
//
//            for (ZipEntry e : Collections.list(file.entries())) {
//
//                for (String item : mapping.keySet()) {
//
//                    if (e.getName().contains(item) && !e.getName().contains(".xml")) {
//                        System.out.println(Thread.currentThread().getName() + ". >> Found class: " + item);
//
//                        checkMethodsInFile(e, item, file);
//                    }
//                }
//            }
//
//        } catch (IOException ex) {
//            System.out.println("ioEx.SFHelper.readZip: " + ex.getMessage());
//        }
    }

    private void checkMethodsInFile(ZipEntry entry, String className, ZipFile file) {

//        InputStream stream = null;
//
//        try {
//            stream = file.getInputStream(entry);
//
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
//
//                while (br.read() != -1) {
//
//                    String currentLine = br.readLine();
//
//                    if (!(currentLine == null)) {
//
//                        for (String item : mapping.get(className)) {
//
//                            if (currentLine.contains(item) && currentLine != null) {
//                                System.out.println(Thread.currentThread().getName() + ". >> Found Method: " + item);
//                                System.out.println(Thread.currentThread().getName() + ". >> Line: " + currentLine);
//
//                                validateTasksByRunTests();
//                            }
//                        }
//                    }
//                }
//
//            } catch (IOException ex) {
//                System.out.println(Thread.currentThread().getName() + ". >> ioEx.SFHelper.checkMeth.readClass: " + ex.getMessage());
//            }
//
//        } catch (IOException ioEx) {
//            System.out.println(Thread.currentThread().getName() + ". >> ioEx.sfHelper.checkMeth" + ioEx.getMessage());
//        } finally {
//
//            try {
//                stream.close();
//            } catch (IOException ex) {
//                System.out.println(Thread.currentThread().getName() + ". >> Exception in closing ZipEntry Stream");
//            }
//
//
//        }
    }

    private void validateTasksByRunTests() {

        ValidateByTestHelper helper = new ValidateByTestHelper(tempUsername);

    }

}
