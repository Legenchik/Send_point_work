package com.Send_point.Gmail_And_Sheets_Points.Google_Sheets;

import com.Send_point.Gmail_And_Sheets_Points.BuildClass.Student;
import com.Send_point.Gmail_And_Sheets_Points.BuildClass.StudentsMore;
import com.Send_point.Gmail_And_Sheets_Points.GoogleCredentialsAndOtherHelp;
import com.Send_point.Gmail_And_Sheets_Points.Google_Gmail.SendPoint;
import com.Send_point.Gmail_And_Sheets_Points.Google_Sheets.Code_For_Unloading_Classes.GetDataFromOnePointSheet;
import com.Send_point.Gmail_And_Sheets_Points.Google_Sheets.Code_For_Unloading_Classes.GetEmailsForStudents;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class GetDataFromPoint_Sheets_Web {



    public  static List  StudentSubjectAndPoint(String tablePoint,
                                                String tableEmail,
                                                int FromBehind,
                                                int startPoint,
                                                int rowWithNames,
                                                int rowDontUsed,
                                                String countOfGroup,
                                                int nameTablePoint,
                                                int lastNametablePoint,
                                                int emailTablePoint,
                                                String ChoiceForEmailTable) throws IOException, GeneralSecurityException {

        List<StudentsMore> students = new ArrayList<>();

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, GoogleCredentialsAndOtherHelp.JSON_FACTORY, GoogleCredentialsAndOtherHelp.getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(GoogleCredentialsAndOtherHelp.APPLICATION_NAME)
                        .build();

        Spreadsheet spreadsheet = service.spreadsheets().get(tablePoint).execute();

        List<Sheet> sheets = spreadsheet.getSheets();
        GetDataFromOnePointSheet s = new GetDataFromOnePointSheet();
        for (Sheet sheet : sheets) {

            String nameOfSheet = sheet.getProperties().getTitle().toString();

            ValueRange response = service.spreadsheets().values()
                    .get(tablePoint,nameOfSheet)
                    .execute();
            List<List<Object>> values = response.getValues();

            if (countOfGroup.equals("All")) {
                students.addAll(s.sheet_Processing(values,nameOfSheet,FromBehind,rowDontUsed,startPoint,rowWithNames));
            } else if (nameOfSheet.equals(countOfGroup)) {
                students=s.sheet_Processing(values,nameOfSheet,FromBehind,rowDontUsed,startPoint,rowWithNames);
            }

        }
        GetEmailsForStudents em = new GetEmailsForStudents();
        students=em.getEmailForAll(students,tableEmail,nameTablePoint,lastNametablePoint,emailTablePoint,ChoiceForEmailTable);
        return students;
    }

//    public static String StudentEmail(String nameStudent,String sheetIndex, int numberStudent,int lastNameStudent, int numberEmail) throws IOException, GeneralSecurityException {
//        // Build a new authorized API client service.
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        final String spreadsheetId = sheetIndex;
//        final String range = "emails";
//        Sheets service =
//                new Sheets.Builder(HTTP_TRANSPORT, GoogleCredentialsAndOtherHelp.JSON_FACTORY, GoogleCredentialsAndOtherHelp.getCredentials(HTTP_TRANSPORT))
//                        .setApplicationName(GoogleCredentialsAndOtherHelp.APPLICATION_NAME)
//                        .build();
//        ValueRange response = service.spreadsheets().values()
//                .get(spreadsheetId, range)
//                .execute();
//        List<List<Object>> values = response.getValues();
//        if (values == null || values.isEmpty()) {
//        } else {
//            for (List row : values) {
//                String[] nameWithoutGroup=row.get(numberStudent).toString().split(" ");
//                int maxRange = nameWithoutGroup.length-1;
//                if (nameStudent.indexOf(nameWithoutGroup[maxRange])!=-1 && nameStudent.indexOf(row.get(lastNameStudent).toString())!=-1 ) { // получать данні з таблиці данних(row.get(0))
//                    return row.get(numberEmail).toString();     // получать данні з таблиці данних(row.get(3))
//                }
//            }
//        }
//
//        return "пошту не знайдено";
//    }
//
//
//
//    public static void SendPointAll(List<Student> studentsd) throws MessagingException, GeneralSecurityException, IOException {
//        SendPoint sp = new SendPoint();
//        for(Student std:studentsd){
//            if(std.getEmail().equals("пошту не знайдено")==false) {
//                sp.sendMail("Оцінки студента: " + std.getName() + " група: " + std.getNumberOfGroup(),
//                        std.getSubject()+"\n"+std.getSubjectWords(),
//                        std.getEmail());
//            }else{
//                System.out.println("не знайдено пошту "+ std.getName());
//            }
//        }
//    }
}


