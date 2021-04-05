package com.tim9.admin.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
    private JavaMailSender emailSender;

    @Async
    public void sendEmailWithCertificate(String to, File certificate) throws MessagingException, IOException {
    	MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("aleksa.goljovic4@gmail.com");
        helper.setFrom("noreply.kts.l9@gmail.com");
        helper.setSubject("NEW Certificate");
        helper.setText("Your new certificate is attached.");

        FileInputStream inputStream = new FileInputStream(certificate);
        helper.addAttachment(MimeUtility.encodeText(certificate.getName()), new ByteArrayResource(IOUtils.toByteArray(inputStream)));

        emailSender.send(message);
    }
    
    @Async
	public void sendMail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("noreply.kts.l9@gmail.com");
        message.setTo("jelenacupac99@gmail.com"); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);	
	}
}
