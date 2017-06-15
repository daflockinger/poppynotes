package com.flockinger.poppynotes.gateway.filter;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.io.ByteArrayInputStream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import com.flockinger.poppynotes.gateway.model.AuthUserResponse;


public class OutGoingNoteDecryptingFilterTest extends EncryptionFilterBaseTest{
	
	@Before
	public void setup() throws Exception {
		super.mvcSetup();
		
		stubResponse = "{\"id\": 2, \"title\": \"1latest\",\"lastEdit\": null, \"archived\": false, \"content\": \"this is a secret message.\"}";
		notesMock.stubFor(get(urlEqualTo("/api/v1/notes/existingNoteId")).willReturn(aResponse()
                .withStatus(200).withBody(stubResponse)));
	}

	@Test
	@WithMockUser(username="daflockinger@gmail.com",authorities={"ADMIN","AUTHOR"},password="none")
	public void testFilter_withValidNotesGetCall_shouldReturnDecryptedNote() throws Exception {
		AuthUserResponse response = new AuthUserResponse();
		response.setCryptKey("sikdfyh089oay3i3ip2wr3o2rj3wio8yf");
		when(userService.getUserInfoFromAuthEmail(matches("daflockinger@gmail.com"))).thenReturn(response);
		String decryptedSecretMessage = "{\"id\":\"no1\",\"title\":\"top secret\",\"lastEdit\":null,\"archived\":false,\"content\":\"Prosciutto brisket pork turkey filet mignon landjaeger cow.\"}";
		when(encryptionService.decryptNote(any(), matches(response.getCryptKey()),any()))
				.thenReturn(new ByteArrayInputStream(decryptedSecretMessage.getBytes()));

		MvcResult result = 
		mockMvc.perform(get("/api/v1/notes/existingNoteId").contentType(jsonContentType)).andReturn();
		
		assertEquals("correct response returns 200", 200, result.getResponse().getStatus());
		assertEquals("check mock-decrypted message", decryptedSecretMessage, result.getResponse().getContentAsString());
		verify(encryptionService,times(1)).decryptNote(any(), matches(response.getCryptKey()),any());
	}
	
	@Test
	public void testFilter_withNotesPostCall_shouldNotFilter() throws Exception {		
		mockMvc.perform(post("/api/v1/notes/existingNoteId").contentType(jsonContentType));
		verify(encryptionService,times(0)).decryptNote(any(), any(),any());
	}
	
	@Test
	public void testFilter_withNotesPutCall_shouldNotFilter() throws Exception {
		mockMvc.perform(put("/api/v1/notes/existingNoteId").contentType(jsonContentType));
		verify(encryptionService,times(0)).decryptNote(any(), any(),any());
	}
	
	@Test
	public void testFilter_withUserGetCal_shouldNotFilter() throws Exception {
		mockMvc.perform(get("/api/v1/users").contentType(jsonContentType));
		verify(encryptionService,times(0)).decryptNote(any(), any(),any());
	}
}
