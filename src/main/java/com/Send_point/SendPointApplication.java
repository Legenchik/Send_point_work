package com.Send_point;

import com.Send_point.getData.SheetsExampleWork;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.MessagingException;
import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
public class SendPointApplication {

	public static void main(String[] args) {
		SpringApplication.run(SendPointApplication.class, args);
		try {
			SheetsExampleWork.getDataForStudent();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
