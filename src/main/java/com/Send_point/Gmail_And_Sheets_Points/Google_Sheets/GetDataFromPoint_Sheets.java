package com.Send_point.Gmail_And_Sheets_Points.Google_Sheets;

import com.Send_point.Gmail_And_Sheets_Points.BuildClass.Student;
import com.Send_point.Gmail_And_Sheets_Points.Google_Gmail.SendPoint;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetDataFromPoint_Sheets {

    public static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";


    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_SEND,SheetsScopes.SPREADSHEETS_READONLY);

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        InputStream in = GetDataFromPoint_Sheets.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    private static List<Student> students = new ArrayList<>();
    public static void StudentSubjectAndPoint(String tablePoint, String tableEmail, int startPoint, int nameTablePoint,int lastNametablePoint, int emailTablePoint) throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = tablePoint;

        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        // Request for the spreadsheet.
        Spreadsheet spreadsheet = service.spreadsheets().get(spreadsheetId).execute();

        // Get the titles of all the sheets in the spreadsheet.
        List<Sheet> sheets = spreadsheet.getSheets();
        for (Sheet sheet : sheets) {
            //System.out.println(sheet.getProperties().getTitle());

            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, sheet.getProperties().getTitle().toString()+"")
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values == null || values.isEmpty()) {
                System.out.println("No data found.");
            } else {
                boolean checkFirst=false;
                String sub="";
                String[] subA= new String[30];        //TODO 5 зделать щоб приймав параметир з таблиці данних //було String[5+1];//5(row.size()); 5 зделать щоб приймав параметир з таблиці данних (предмет)

                int forHourAndCredits=1;
                int behindUp = 0;
                int countStidents=-1;
                boolean hourAndCredit=false;
                boolean forSubjestWords = false;

                for (List row : values) {

                    if(behindUp>=6) {//отступи від начала щоб вибирало тільки потрібні данні
                        int range = row.size();
                        if (checkFirst) {
                            if(forHourAndCredits==2) { // Перевірка щоб дані про кредити та годинни не використовувались
                                hourAndCredit = true;
                            }
                            for (int range2 = startPoint; range > range2; range2++) {               //Статичний студенти(range2=1 вставка в масив без піб студента//TODO range2 зделать щоб приймав параметир з таблиці данних (предмет)
                                String subJ = subA[range2] + "= " + row.get(range2) + "; \n";
                                sub = sub + subJ;
                            }
                            forHourAndCredits=2;
                        } else {
                            for (int range2 = startPoint; range > range2; range2++) {     // ПРЕДМЕТИ//TODO range2 зделать щоб приймав параметир з таблиці данних (предмет)
                                subA[range2] = row.get(range2).toString();
                            }

                        }
                        if (checkFirst && hourAndCredit && forSubjestWords==false ) {
                            Student std = new Student();
                            std.setNumberOfGroup(sheet.getProperties().getTitle().toString());
                            std.setName(row.get(1).toString());
                            std.setSubject(sub);
                            std.setEmail(StudentEmail(
                                                    std.getName(),
                                                    tableEmail,
                                                    nameTablePoint,
                                                    lastNametablePoint,
                                                    emailTablePoint)); //TODO вставка айди таблиці з таблиці данних


                            students.add(std);
                            forSubjestWords=true;
                            countStidents++;
                        }
                        else{
                            if(countStidents!=-1) {
                                students.get(countStidents).setSubjectWords(sub);
                                forSubjestWords = false;
                            }
                        }
                        checkFirst = true;
                        sub = "";
                    }else{
                        behindUp++;
                    }

                }
            }
        }
        for (Student s:
                students) {
            System.out.println(s.allInfo());

        }
    }

    public static String StudentEmail(String nameStudent,String sheetIndex, int numberStudent,int lastNameStudent, int numberEmail) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = sheetIndex;
        final String range = "emails";
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
        } else {
            for (List row : values) {
                String[] nameWithoutGroup=row.get(numberStudent).toString().split(" ");
                int maxRange = nameWithoutGroup.length-1;
                if (nameStudent.indexOf(nameWithoutGroup[maxRange])!=-1 && nameStudent.indexOf(row.get(lastNameStudent).toString())!=-1 ) { // получать данні з таблиці данних(row.get(0))
                    return row.get(numberEmail).toString();     // получать данні з таблиці данних(row.get(3))
                }
            }
        }

        return "пошту не знайдено";
    }

    public static void getDataForStudent() throws IOException, GeneralSecurityException, MessagingException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1pgB6XYkjUkVo1ZqBtEx9ZBfi01FGvB3FYaRP_jHK4zw";
        final String range = "1";
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data");
        } else {
            boolean checkFirst = false;
            for (List row : values) {
                if (checkFirst) {
                    StudentSubjectAndPoint(row.get(0).toString(),
                            row.get(1).toString(),
                            Integer.parseInt(row.get(2).toString()),
                            Integer.parseInt(row.get(3).toString()),
                            Integer.parseInt(row.get(4).toString()),
                            Integer.parseInt(row.get(5).toString())
                            );
                    break;
                }
                checkFirst=true;
            }
        }
        SendPointAll(students);

    }

    public static void SendPointAll(List<Student> studentsd) throws MessagingException, GeneralSecurityException, IOException {
        SendPoint sp = new SendPoint();
        for(Student std:studentsd){
            if(std.getEmail().equals("пошту не знайдено")==false) {
                sp.sendMail("Оцінки студента: " + std.getName() + " група: " + std.getNumberOfGroup(),
                        std.getSubject()+"\n"+std.getSubjectWords(),
                        std.getEmail());
            }else{
                System.out.println("не знайдено пошту "+ std.getName());
            }
        }
    }
}


