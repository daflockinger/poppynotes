package com.flockinger.poppynotes.gateway;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class TestConfig {
	@Primary
	@Bean(name="testRestTemplate")
	RestTemplate restTemplate() {
		RestTemplate template = new RestTemplate();
		template.setErrorHandler(getResponseErrorHandler());

		return template;
	}

	private ResponseErrorHandler getResponseErrorHandler() {
		return new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
					return true;
				}
				return false;
			}

			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
			}
		};
	}
}
