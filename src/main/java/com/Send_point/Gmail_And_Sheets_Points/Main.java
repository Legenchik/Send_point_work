package com.Send_point.Gmail_And_Sheets_Points;

import com.Send_point.Gmail_And_Sheets_Points.Google_Sheets.GetDataFromPoint_Sheets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class Main {
    public static void getDataForStudent() throws IOException, GeneralSecurityException, MessagingException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        final String spreadsheetId = "1pgB6XYkjUkVo1ZqBtEx9ZBfi01FGvB3FYaRP_jHK4zw";
        final String range = "1";
        Sheets service =
                new Sheets.Builder(HTTP_TRANSPORT, GoogleCredentialsAndOtherHelp.JSON_FACTORY, GoogleCredentialsAndOtherHelp.getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(GoogleCredentialsAndOtherHelp.APPLICATION_NAME)
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
                    GetDataFromPoint_Sheets.StudentSubjectAndPoint(row.get(0).toString(),
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
        //SendPointAll(students);//TODO вернуть

    }
}
