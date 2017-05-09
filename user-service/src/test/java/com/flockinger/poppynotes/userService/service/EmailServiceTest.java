package com.flockinger.poppynotes.userService.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;
import com.flockinger.poppynotes.userService.exception.InvalidEmailServerConfigurationException;
import com.flockinger.poppynotes.userService.model.User;
import com.flockinger.poppynotes.userService.service.impl.EmailServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = { "default", "test" })
public class EmailServiceTest {

	private SimpleSmtpServer dumbster;
	
	private EmailServiceImpl service = new EmailServiceImpl();
	
	@Before
	public void setup() throws IOException {
		service.setMailSender(getValidFakeConfig());
		dumbster = SimpleSmtpServer.start(SimpleSmtpServer.DEFAULT_SMTP_PORT);
	}
	
	@Test
	public void testSendEmail_WithWorkingCredentials_ShouldWork() throws InvalidEmailServerConfigurationException {		
		User lockedOutUser = new User();
		lockedOutUser.setRecoveryEmail("sep@gmail.com");
		lockedOutUser.setUnlockCode("12345");
		
		service.sendUnlockCodeEmailFor(lockedOutUser);
			
		SmtpMessage message = dumbster.getReceivedEmails().get(0);
		assertNotNull(message);
		assertTrue(message.getBody().contains("12345"));
		assertTrue(message.getBody().contains("unlock"));
		
		dumbster.reset();
	}
	
	@Test(expected=InvalidEmailServerConfigurationException.class)
	public void testSendEmail_WithWrongCredentials_ShoulThrowException() throws InvalidEmailServerConfigurationException {
		service.setMailSender(getWrongFakeConfig());
		
		User lockedOutUser = new User();
		lockedOutUser.setRecoveryEmail("sep@gmail.com");
		lockedOutUser.setUnlockCode("12345");
		
		service.sendUnlockCodeEmailFor(lockedOutUser);
		
		dumbster.reset();
	}
	
	@After
	public void tearwodn() throws Exception {
		dumbster.stop();
		dumbster.close();
	}
	
	
	private JavaMailSender getValidFakeConfig() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("localhost");
	    mailSender.setPort(SimpleSmtpServer.DEFAULT_SMTP_PORT);
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
	

	private JavaMailSender getWrongFakeConfig() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("google.bla");
	    mailSender.setPort(12345);
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
}
