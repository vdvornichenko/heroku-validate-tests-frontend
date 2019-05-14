package project;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;
import project.Processors.RequestProcessor;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleHelper {

    // Configuration
    // app name
    private static final String APPLICATION_NAME        = "Google Docs API Java Quickstart";
    // Document ID for read
    private static final String DOCUMENT_ID             = "1GLBSicy1cTK5nOiA3XHDf1PxI7FBP_DH0pqaQUP3itA";
    // Google cred JSON
    private static final String CREDENTIALS_FILE_PATH   = "/credentials.json";
    private static final String TOKENS_DIRECTORY_PATH   = "tokens";
    // users creds for calling orgs
    public static Map<String, String> userCreds         = new HashMap<String, String>();
    public static String documentNname                  = "";

    // Service config. Do not touch
    private static final JsonFactory JSON_FACTORY       = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES            = Collections.singletonList(DocsScopes.DOCUMENTS_READONLY);


    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleHelper.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void callDocument() {
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Docs service = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            System.out.println("Auth successfully");

            Document response = service.documents().get(DOCUMENT_ID).execute();
            String title = response.getTitle();
            documentNname = title;

            System.out.printf("The title of the doc is: %s\n", title);

            String documentBody = readDocumentBody(response);
            populateMapWithUsersCreds(documentBody);

        } catch (IOException ioEx) {
            System.out.println("IO Ex: " + ioEx);
        } catch (GeneralSecurityException genEx) {
            System.out.println("Gen Ex: " + genEx);
        }
    }

    private static String readDocumentBody(Document document) {
        String body = readStructrualElements(document.getBody().getContent());
        System.out.println("Document: " + body);
        return body;
    }

    private static String readStructrualElements(List<StructuralElement> elements) {
        StringBuilder sb = new StringBuilder();

        for (StructuralElement element : elements) {
            if (element.getParagraph() != null) {
                for (ParagraphElement paragraphElement : element.getParagraph().getElements()) {
                    sb.append(readParagraphElement(paragraphElement));
                }
            } else if (element.getTable() != null) {
                // The text in table cells are in nested Structural Elements and tables may be
                // nested.
                for (TableRow row : element.getTable().getTableRows()) {
                    for (TableCell cell : row.getTableCells()) {
                        sb.append(readStructrualElements(cell.getContent()));
                    }
                }
            } else if (element.getTableOfContents() != null) {
                // The text in the TOC is also in a Structural Element.
                sb.append(readStructrualElements(element.getTableOfContents().getContent()));
            }
        }
        return sb.toString();
    }

    private static String readParagraphElement(ParagraphElement element) {
        TextRun run = element.getTextRun();

        if (run == null || run.getContent() == null) {
            return "";
        }
        return run.getContent();
    }

    public static void populateMapWithUsersCreds(String data) {
        String[] credsLines = data.split("\n");

        for (String item : credsLines) {
            if(item.contains("https://login.salesforce.com/?un=learning-dev.koverchik.artem@gmail.com&pw=qwerty321 - Artem Koverchik. Group 1")) {
                continue;
            }
            String login = item.substring(item.indexOf("un=") + 3, item.indexOf("&"));
            String pass = item.substring(item.indexOf("&pw=") + 4, item.indexOf("-") - 1);
            if (item.indexOf("Группа") != -1) {

                String fio = item.substring(item.indexOf(" - ") + 3, item.indexOf(". Группа"));
                String group = item.substring(item.indexOf("Группа") + 6);
                RequestProcessor.userLogins.add(new RequestProcessor.CredentialsStorage(login, pass, fio, group));
            }
            System.out.println(login);
            System.out.println(pass);
            userCreds.put(login, pass);
        }


    }



}
