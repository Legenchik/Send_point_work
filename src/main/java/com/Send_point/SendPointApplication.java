package com.Send_point;

import com.Send_point.Gmail_And_Sheets_Points.Main;
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
			Main.getDataForStudent();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
