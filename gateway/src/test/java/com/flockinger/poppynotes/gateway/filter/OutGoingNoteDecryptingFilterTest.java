package com.flockinger.poppynotes.gateway.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.flockinger.poppynotes.gateway.TestConfig;
import com.flockinger.poppynotes.gateway.service.NoteEncryptionService;
import com.google.common.collect.ImmutableList;

@ActiveProfiles({ "test" })
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(ids = { "com.flockinger.poppynotes:user-service:+:stubs:8002",
		"com.flockinger.poppynotes:notes-service:+:stubs:8001" }, workOffline = true)
@DirtiesContext
@Import(TestConfig.class)
public class OutGoingNoteDecryptingFilterTest {

	@MockBean(reset = MockReset.BEFORE)
	private NoteEncryptionService encryptionService;

	@LocalServerPort
	int randomServerPort;

	@Qualifier("testRestTemplate")
	@Autowired
	RestTemplate restTemplate;

	@Test
	public void testFilter_withValidNotesGetCall_shouldReturnDecryptedNote() throws URISyntaxException, InterruptedException {
		String decryptedSecretMessage = "{\"id\":\"no1\",\"title\":\"top secret\",\"lastEdit\":null,\"archived\":false,\"content\":\"Prosciutto brisket pork turkey filet mignon landjaeger cow.\"}";
		when(encryptionService.decryptNote(any(), any()))
				.thenReturn(new ByteArrayInputStream(decryptedSecretMessage.getBytes()));

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.put("userId", ImmutableList.of("1"));
		headers.put("Content-Type", ImmutableList.of("application/json"));
		headers.put("Accept-Charset", ImmutableList.of("utf-8"));

		RequestEntity<String> request = new RequestEntity<String>(headers, HttpMethod.GET,
				new URI("http://localhost:" + randomServerPort + "/api/v1/notes/existingNoteId"));

		ResponseEntity<String> response = restTemplate.exchange(request, String.class);

		assertEquals("correct response returns 200", HttpStatus.OK, response.getStatusCode());
		assertEquals("check mock-decrypted message", decryptedSecretMessage, response.getBody());
		verify(encryptionService,times(1)).decryptNote(any(), any());
	}

	@Test
	public void testFilter_withNotesPostCall_shouldNotFilter() throws URISyntaxException {		
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.put("userId", ImmutableList.of("1"));
		headers.put("Content-Type", ImmutableList.of("application/json"));
		headers.put("Accept-Charset", ImmutableList.of("utf-8"));

		RequestEntity<String> request = new RequestEntity<String>(headers, HttpMethod.POST,
				new URI("http://localhost:" + randomServerPort + "/api/v1/notes"));
		restTemplate.exchange(request, String.class);

		verify(encryptionService,times(0)).decryptNote(any(), any());
	}
	
	@Test
	public void testFilter_withNotesPutCall_shouldNotFilter() throws URISyntaxException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.put("userId", ImmutableList.of("1"));
		headers.put("Content-Type", ImmutableList.of("application/json"));
		headers.put("Accept-Charset", ImmutableList.of("utf-8"));

		RequestEntity<String> request = new RequestEntity<String>(headers, HttpMethod.PUT,
				new URI("http://localhost:" + randomServerPort + "/api/v1/notes"));
		restTemplate.exchange(request, String.class);

		verify(encryptionService,times(0)).decryptNote(any(), any());
	}
	
	@Test
	public void testFilter_withUserGetCal_shouldNotFilter() throws URISyntaxException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.put("userId", ImmutableList.of("1"));
		headers.put("Content-Type", ImmutableList.of("application/json"));
		headers.put("Accept-Charset", ImmutableList.of("utf-8"));

		RequestEntity<String> request = new RequestEntity<String>(headers, HttpMethod.GET,
				new URI("http://localhost:" + randomServerPort + "/api/v1/users"));
		restTemplate.exchange(request, String.class);

		verify(encryptionService,times(0)).decryptNote(any(), any());
	}
	
}
