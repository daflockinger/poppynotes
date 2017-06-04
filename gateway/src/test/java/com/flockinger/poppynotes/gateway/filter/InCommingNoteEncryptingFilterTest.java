package com.flockinger.poppynotes.gateway.filter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
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
public class InCommingNoteEncryptingFilterTest {

	@MockBean(reset = MockReset.BEFORE)
	private NoteEncryptionService encryptionService;
	
	@LocalServerPort
	int randomServerPort;

	@Qualifier("testRestTemplate")
	@Autowired
	RestTemplate restTemplate;
	
	@Test
	public void testFilter_withValidNotesPostCall_shouldSendEncryptedNote() throws URISyntaxException, InterruptedException, UnsupportedEncodingException {
		String validPost = "{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"very very secret text\",\"userId\":1}";
		String encryptedMessage = "{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"userId\":1}";
		when(encryptionService.encryptNote(any(), any()))
				.thenReturn(new ByteArrayInputStream(encryptedMessage.getBytes()));

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.put("userId", ImmutableList.of("1"));
		headers.put("Content-Type", ImmutableList.of("application/json"));
		headers.put("Accept-Charset", ImmutableList.of("utf-8"));

		RequestEntity<String> request = new RequestEntity<String>(validPost,headers, HttpMethod.POST,
				new URI("http://localhost:" + randomServerPort + "/api/v1/notes"));
		

		ResponseEntity<String> response = restTemplate.exchange(request, String.class);

		assertEquals("correct response returns 200", HttpStatus.OK, response.getStatusCode());
		verify(encryptionService,times(1)).encryptNote(any(), any());
	}
	
	@Test
	public void testFilter_withValidNotesPutCall_shouldSendEncryptedNote() throws URISyntaxException, InterruptedException, UnsupportedEncodingException {
		String validPost = "{\"id\":\"existingNoteId\",\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"very very secret text\",\"userId\":1}";
		String encryptedMessage = "{\"id\":\"existingNoteId\",\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"userId\":1}";
		when(encryptionService.encryptNote(any(), any()))
				.thenReturn(new ByteArrayInputStream(encryptedMessage.getBytes()));

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.put("userId", ImmutableList.of("1"));
		headers.put("Content-Type", ImmutableList.of("application/json"));
		headers.put("Accept-Charset", ImmutableList.of("utf-8"));

		RequestEntity<String> request = new RequestEntity<String>(validPost,headers, HttpMethod.PUT,
				new URI("http://localhost:" + randomServerPort + "/api/v1/notes"));
		

		ResponseEntity<String> response = restTemplate.exchange(request, String.class);

		assertEquals("correct response returns 200", HttpStatus.OK, response.getStatusCode());
		verify(encryptionService,times(1)).encryptNote(any(), any());
	}

	@Test
	public void testFilter_withNotesGetCall_shouldNotFilter() throws URISyntaxException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.put("userId", ImmutableList.of("1"));
		headers.put("Content-Type", ImmutableList.of("application/json"));
		headers.put("Accept-Charset", ImmutableList.of("utf-8"));

		RequestEntity<String> request = new RequestEntity<String>(headers, HttpMethod.GET,
				new URI("http://localhost:" + randomServerPort + "/api/v1/notes/existingNoteId"));

		restTemplate.exchange(request, String.class);
		verify(encryptionService,times(0)).encryptNote(any(), any());
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

		verify(encryptionService,times(0)).encryptNote(any(), any());
	}
}
