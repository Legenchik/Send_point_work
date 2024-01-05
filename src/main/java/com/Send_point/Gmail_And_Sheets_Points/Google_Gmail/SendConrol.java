package com.Send_point.Gmail_And_Sheets_Points.Google_Gmail;


import com.Send_point.Gmail_And_Sheets_Points.BuildClass.StudentsMore;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class SendConrol {
    public static String SendPointAll(List<StudentsMore> studentsd) throws MessagingException, GeneralSecurityException, IOException {
        String sendOK="";
        String sendLose="";
        SendPoint sp = new SendPoint();
        for (StudentsMore std : studentsd) {
            if (std.getEmail().equals("Пошту не знайдено") == false) {
                sendOK=sendOK+"\n"+ sp.sendMail("Оцінки студента: " + std.getName() + " група: " + std.getNumberOfGroup(),
                                                        std.forSendEmail(),
                                                        std.getEmail());
            } else {
                //System.out.println("не знайдено пошту " + std.getName());
                sendLose=sendLose+"\n"+"Пошту не знайдено, студент "+std.forErrorSend();
            }
        }
        return sendOK+"\n\n"+sendLose;
    }
}
