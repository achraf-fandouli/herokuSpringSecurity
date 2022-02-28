package com.sip.ams;

import com.sip.ams.controllers.ProviderController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sip.ams.controllers.ArticleController;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class AmsdataApplication extends SpringBootServletInitializer
//	implements CommandLineRunner
{
    public static void main(String[] args) {
        //to create the folder on project starting to stocke article image
        new File(ArticleController.uploadDirectory).mkdir();

        //to create the folder on project starting to stocke provider image
        new File(ProviderController.uploadDirectoryProvider).mkdir();

        SpringApplication.run(AmsdataApplication.class, args);
    }





/*	@Autowired
	private JavaMailSender javaMailSender;

	void sendEmail() {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo("achraf.fandouli@gmail.com");

		msg.setSubject("Testing from Spring Boot");
		msg.setText("Hello World \n Spring Boot Email");

		javaMailSender.send(msg);

	}*/

/*	void sendEmailWithAttachment() throws MessagingException, IOException {
		MimeMessage msg = javaMailSender.createMimeMessage();

		// true = multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		helper.setTo("1@gmail.com");

		helper.setSubject("Testing from Spring Boot");

		// default = text/plain
		//helper.setText("Check attachment for image!");

		// true = text/html
		helper.setText("<h1>Check attachment for image!</h1>", true);

		helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

		javaMailSender.send(msg);

	}*/

/*	@Override
	public void run(String... args) throws MessagingException, IOException {
		System.out.println("Sending Email...");
		sendEmail();
		System.out.println("Done");
	}*/

}
