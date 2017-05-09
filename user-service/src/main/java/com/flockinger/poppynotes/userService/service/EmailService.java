package com.flockinger.poppynotes.userService.service;

import org.springframework.mail.javamail.JavaMailSender;

import com.flockinger.poppynotes.userService.exception.InvalidEmailServerConfigurationException;
import com.flockinger.poppynotes.userService.model.User;

public interface EmailService {
	void sendUnlockCodeEmailFor(User user) throws InvalidEmailServerConfigurationException;
	void setMailSender(JavaMailSender sender);
}
