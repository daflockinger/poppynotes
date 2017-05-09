package com.flockinger.poppynotes.userService.config;

import java.util.Properties;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@EnableCaching
public class GeneralConfig {
	
	@Value("${settings.email.host}")
	private String emailHost;
	
	@Value("${settings.email.port}")
	private Integer emailPort;
	
	@Value("${settings.email.user}")
	private String emailUser;
	
	@Value("${settings.email.password}")
	private String emailPassword;
	
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
	@Bean
	public JavaMailSender getConfig() {
		
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost(emailHost);
	    mailSender.setPort(emailPort);
	    mailSender.setUsername(emailUser);
	    mailSender.setPassword(emailPassword);
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.debug", "true");
	     
	    return mailSender;
	}
}
