package project;

import com.sforce.soap.apex.*;
import com.sforce.soap.apex.ExecuteAnonymousResult;
import com.sforce.soap.apex.LogCategory;
import com.sforce.soap.apex.LogCategoryLevel;
import com.sforce.soap.apex.LogInfo;
import com.sforce.soap.apex.LogType;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.tooling.*;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import project.Rules.*;

import project.Processors.RequestProcessor;
import project.Storages.FileStorage;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;
import java.text.MessageFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class SalesforceHepler {

    public static String templateNotFoundFile =  "Не найден файл: {0}";

    public static long startTime = System.currentTimeMillis();

    private String tempUsername;
    private String tempPassword;
    private Map<String, UserInfoWrapper> userResults;

    public static String zip_file_for_read = "";
    private static Map<String, Rule> mapping = TaskMapping.METADATA_CHECK;

    public SalesforceHepler(String username, String password, Map<String, UserInfoWrapper> userResults) {
        this.tempUsername = username;
        this.tempPassword = password;
        this.userResults = userResults;
    }

    public void processUser() {

        DeployRetrieveHelper instance = new DeployRetrieveHelper(tempUsername, tempPassword, userResults);

//        instance.retrieveZip();
//        RequestProcessor.userListResults = checkZipFile();
        if (!zip_file_for_read.equalsIgnoreCase("")) {
            checkZipFile();
            instance.deleteFileZip();
        }
        //readZipFile();
//
    }


    private List<Results> checkZipFile() {

        List<Results> results = new ArrayList<>();
        try {
            ZipFile file = new ZipFile(zip_file_for_read);

            for (String item : TaskMapping.tasks) {
                Enumeration< ? extends ZipEntry > e = file.entries();
                boolean fileFound = false;
                while (e.hasMoreElements()) {
                    ZipEntry entry = e.nextElement();
                    if (entry.getName().contains("/" + item) && !entry.getName().contains(".xml")) {
                        fileFound = true;
                        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(entry)));
                        String allFile = "";
                        String theFile = "";
                        String line = null;
                        while ((line = br.readLine()) != null){
                            allFile = allFile + line;
                            theFile += "<br/>" + line.replaceAll(" ", "&nbsp;")
                                    .replaceAll("<", "&lt").replaceAll(">", "&gt;");
                        }
                        System.out.println(item);
                        System.out.println(tempUsername);
                        System.out.println(allFile);
                        if (!addFileToContainer(tempUsername, item, theFile)) {
                            RequestProcessor.files.add(new FileStorage(item, tempUsername, theFile));
                        }
                        results.addAll(mapping.get(item).checkCondition(allFile, this.tempUsername));
                        break;
                    }
                }
                // not found file
                if (!fileFound){
                    if (item.contains("Test")){
                        results.add(new Results("Test", MessageFormat.format(templateNotFoundFile, item), false));
                    } else {
                        results.add(new Results(item, MessageFormat.format(templateNotFoundFile, item), false));
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println("ioEx.SFHelper.readZip: " + ex.getMessage());
        }
        for (Results res : results) {
            System.out.println(">>> " + res.status + " " + res.nameMetadata + " " +  res.message);
        }
        RequestProcessor.getMapValue(tempUsername, userResults).setResults(results);
        return results;
    }

    private Boolean addFileToContainer(String owner, String fileName, String fileContent) {
        for (int i = 0; i < RequestProcessor.files.size(); i ++) {
            FileStorage file = RequestProcessor.files.get(i);
            if (file.fileName.equalsIgnoreCase(fileName) && file.fileOwner.equalsIgnoreCase(owner)) {
                if (!fileContent.equalsIgnoreCase(file.content)) {
                    System.out.println(file.content);
                    RequestProcessor.files.get(i).content = fileContent;
                }
                return true;
            }
        }
        return false;
    }
}
