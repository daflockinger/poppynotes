package com.flockinger.poppynotes.userService.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.flockinger.poppynotes.userService.exception.InvalidEmailServerConfigurationException;
import com.flockinger.poppynotes.userService.model.User;
import com.flockinger.poppynotes.userService.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendUnlockCodeEmailFor(User user)
			throws InvalidEmailServerConfigurationException {
		try {
			mailSender.send(createMessage(user));
		} catch(MailException mailException) {
			throw new InvalidEmailServerConfigurationException(mailException.getCause().getMessage());
		}
	}
	
	private SimpleMailMessage createMessage(User user) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getRecoveryEmail());
		message.setSubject("PoppyNotes 2-factor-auth Unlock Code");
		message.setText("Code to unlock account: " + user.getUnlockCode() + 
						"\n\n Cheers Poppy!");
		
		return message;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
}
