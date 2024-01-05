package com.Send_point.Gmail_And_Sheets_Points.Google_Sheets.Code_For_Unloading_Classes;

import com.Send_point.Gmail_And_Sheets_Points.BuildClass.StudentsMore;
import com.Send_point.Gmail_And_Sheets_Points.GoogleCredentialsAndOtherHelp;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GetEmailsForStudents {

    public List<StudentsMore> getEmailForAll(List<StudentsMore> students,
                                             String tableNameId,
                                             int nameTablePoint,
                                             int lastNametablePoint,
                                             int emailTablePoint,
                                             String InWhichSheets) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, GoogleCredentialsAndOtherHelp.JSON_FACTORY, GoogleCredentialsAndOtherHelp.getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(GoogleCredentialsAndOtherHelp.APPLICATION_NAME)
                        .build();

        Spreadsheet spreadsheet = service.spreadsheets().get(tableNameId).execute();

        List<Sheet> sheets = spreadsheet.getSheets();
        boolean forOne=true;
        for (Sheet sheet : sheets) {
            String nameOfSheet = sheet.getProperties().getTitle().toString();

            ValueRange response = service.spreadsheets().values()
                    .get(tableNameId,nameOfSheet)
                    .execute();
            List<List<Object>> values = response.getValues();
            if(InWhichSheets.equals("InOne") && forOne){
                students=Emails(values,students,nameOfSheet,nameTablePoint,lastNametablePoint,emailTablePoint);
                forOne=false;
            } else if (InWhichSheets.equals("InMany")) {
                students=EmailsInMany(values,students,nameOfSheet,nameTablePoint,lastNametablePoint,emailTablePoint);
            } else if (InWhichSheets.equals("InOtherTable")) {
                students=Emails(values,students,nameOfSheet,nameTablePoint,lastNametablePoint,emailTablePoint);
            }

        }
        return students;
    }

    public List<StudentsMore> EmailsInMany(List<List<Object>> values,
                                     List<StudentsMore> students,
                                     String nameOfSheet,
                                     int numberOfName,
                                     int numberOfLastName,
                                     int numberOfEmail){

        for(StudentsMore std:students) {
            System.out.println(std.getNumberOfGroup().toString()+"  "+nameOfSheet);
            if(std.getEmail()==null && std.getNumberOfGroup().toString().equals(nameOfSheet)) {
                std.setEmail(email(values, nameOfSheet, std.getName().toString(), numberOfName, numberOfLastName, numberOfEmail));
            }
        }
        return students;
    }

    public List<StudentsMore> Emails(List<List<Object>> values,
                                     List<StudentsMore> students,
                                     String nameOfSheet,
                                     int numberOfName,
                                     int numberOfLastName,
                                     int numberOfEmail){

        for(StudentsMore std:students) {
            if(std.getEmail()==null) {
                std.setEmail(email(values, nameOfSheet, std.getName().toString(), numberOfName, numberOfLastName, numberOfEmail));
            }
        }
        return students;
    }
    public String email(List<List<Object>> values,
                        String nameOfSheet,
                        String name,
                        int numberOfName,
                        int numberOfLastName,
                        int numberOfEmail){
        for (List row : values) {
            if (values.isEmpty()) {
                System.out.println("Нема данних " + nameOfSheet);
            } else {
                if(row.size()>numberOfEmail && row.size()>numberOfName && row.size()>numberOfLastName) {
                    String[] nameWithoutGroup = row.get(numberOfName).toString().split(" ");
                    String[] lastNameWithoutGroup = row.get(numberOfLastName).toString().split(" ");
                    for (String s : nameWithoutGroup) {
                        for (String sL : lastNameWithoutGroup) {
                            if (name.indexOf(s) != -1 && name.indexOf(sL) != -1) {
                                return row.get(numberOfEmail).toString();
                            }
                        }
                    }
                }
            }
        }
        return "Пошту не знайдено";
    }
}
