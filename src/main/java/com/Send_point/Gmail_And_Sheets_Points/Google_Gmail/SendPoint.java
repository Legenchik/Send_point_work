package com.Send_point.Gmail_And_Sheets_Points.Google_Gmail;

import com.Send_point.Gmail_And_Sheets_Points.GoogleCredentialsAndOtherHelp;
import com.Send_point.Gmail_And_Sheets_Points.Google_Sheets.GetDataFromPoint_Sheets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;


public class SendPoint {
//    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    private static final String TOKENS_DIRECTORY_PATH = "tokens";
//
//    private static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_SEND,SheetsScopes.SPREADSHEETS_READONLY);
//            //Collections.singletonList(GmailScopes.GMAIL_SEND);
//
//    private static final List<String> SCOPES2 =
//            Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);
//    private static final String CREDENTIALS_FILE_PATH = "/credentialsOnlyGmail.json";
//
//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
//            throws IOException {
//        InputStream in = SheetsExampleWork.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        if (in == null) {
//            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
//        }
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//    }

    private static final String fromEmailAddress = "legenchik228@gmail.com";

    public String sendMail(String subject, String massage,String toEmail) throws GeneralSecurityException, IOException, MessagingException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, GoogleCredentialsAndOtherHelp.JSON_FACTORY, GoogleCredentialsAndOtherHelp.getCredentials(HTTP_TRANSPORT))
                .setApplicationName(GoogleCredentialsAndOtherHelp.APPLICATION_NAME)
                .build();


        // Create the email content
        //String messageSubject = "Test message";
        //String bodyText = "lorem ipsum.";

        // Encode as MIME message
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        //email.setFrom(new InternetAddress(fromEmailAddress));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(toEmail));    //TODO toEmailAddress
        email.setSubject(subject);
        email.setText(massage);

        // Encode and wrap the MIME message into a gmail message
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
        Message msg = new Message();
        msg.setRaw(encodedEmail);

        try {
            // Create send message
            msg = service.users().messages().send("me", msg).execute();
            //System.out.println("Message id: " + msg.getId());
            return "Message id: " + msg.getId();
            //System.out.println(msg.toPrettyString());
        } catch (GoogleJsonResponseException e) {
            // TODO(developer) - handle error appropriately
            GoogleJsonError error = e.getDetails();
            if (error.getCode() == 403) {
                System.err.println("Unable to send message: " + e.getDetails());
            } else {
                throw e;
            }
        }
        return "Помилка відправки";
    }

//    public static void main(String[] args) throws MessagingException, GeneralSecurityException, IOException {
//        new SendPoint().sendMail("test","hello");
//    }
}


