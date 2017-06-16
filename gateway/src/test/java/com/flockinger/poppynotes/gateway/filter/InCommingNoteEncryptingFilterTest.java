package com.flockinger.poppynotes.gateway.filter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.io.ByteArrayInputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import com.flockinger.poppynotes.gateway.model.AuthUserResponse;


public class InCommingNoteEncryptingFilterTest extends EncryptionFilterBaseTest{

	@Before
	public void setup() throws Exception {
		super.mvcSetup();
		reset(encryptionService);
	}
	
	@Test
	@WithMockUser(username="daflockinger@gmail.com",authorities={"ADMIN","AUTHOR"},password="none")
	public void testFilter_withValidNotesPostCall_shouldSendEncryptedNote() throws Exception {
		String validPost = "{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"very very secret text\",\"userId\":1}";
		notesMock.stubFor(post(urlEqualTo("/api/v1/notes")).withRequestBody(equalToJson(validPost)).willReturn(aResponse().withStatus(200)));
		
		AuthUserResponse response = new AuthUserResponse();
		response.setCryptKey("sikdfyh089oay3i3ip2wr3o2rj3wio8yf");
		when(userService.getUserInfoFromAuthEmail(matches("daflockinger@gmail.com"))).thenReturn(response);
		String encryptedMessage = "{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"userId\":1}";
		when(encryptionService.encryptNote(any(), matches(response.getCryptKey()),any()))
				.thenReturn(new ByteArrayInputStream(encryptedMessage.getBytes()));

		MvcResult result = 
				mockMvc.perform(post("/api/v1/notes").content(encryptedMessage).contentType(jsonContentType).with(csrf())).andReturn();

		assertEquals("correct response returns 200", 200, result.getResponse().getStatus());
		verify(encryptionService,times(1)).encryptNote(any(), any(),any());
	}
	
	@Test
	@WithMockUser(username="daflockinger@gmail.com",authorities={"ADMIN","AUTHOR"},password="none")
	public void testFilter_withValidNotesPutCall_shouldSendEncryptedNote() throws Exception {
		String validPost = "{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"very very secret text\",\"userId\":1}";
		notesMock.stubFor(post(urlEqualTo("/api/v1/notes")).withRequestBody(equalToJson(validPost)).willReturn(aResponse().withStatus(200)));
		
		AuthUserResponse response = new AuthUserResponse();
		response.setCryptKey("sikdfyh089oay3i3ip2wr3o2rj3wio8yf");
		when(userService.getUserInfoFromAuthEmail(matches("daflockinger@gmail.com"))).thenReturn(response);
		String encryptedMessage = "{\"title\":\"new note\",\"lastEdit\":\"2012-12-12T12:12:12Z\",\"pinned\":false,\"archived\":false,\"content\":\"some text\",\"userId\":1}";
		when(encryptionService.encryptNote(any(), matches(response.getCryptKey()),any()))
				.thenReturn(new ByteArrayInputStream(encryptedMessage.getBytes()));

		MvcResult result = 
				mockMvc.perform(put("/api/v1/notes").content(encryptedMessage).contentType(jsonContentType).with(csrf())).andReturn();

		assertEquals("correct response returns 200", 200, result.getResponse().getStatus());
		verify(encryptionService,times(1)).encryptNote(any(), any(),any());
	}

	@Test
	public void testFilter_withNotesGetCall_shouldNotFilter() throws Exception {
		mockMvc.perform(get("/api/v1/notes/existingNoteId").contentType(jsonContentType));
		verify(encryptionService,times(0)).encryptNote(any(), any(),any());
	}
	
	@Test
	public void testFilter_withUserGetCal_shouldNotFilter() throws Exception {
		mockMvc.perform(post("/api/v1/users").contentType(jsonContentType).with(csrf()));
		verify(encryptionService,times(0)).encryptNote(any(), any(),any());
	}
}
