package com.flockinger.poppynotes.gateway.filter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.flockinger.poppynotes.gateway.TestConfig;
import com.flockinger.poppynotes.gateway.service.NoteEncryptionService;
import com.flockinger.poppynotes.gateway.service.UserClientService;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

@ActiveProfiles({ "test","default" })
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Import(TestConfig.class)
@TestExecutionListeners(listeners={ServletTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class,
		WithSecurityContextTestExecutionListener.class, 
		MockitoTestExecutionListener.class})
public abstract class EncryptionFilterBaseTest {
	
	@MockBean(reset = MockReset.BEFORE)
	protected NoteEncryptionService encryptionService;
	
	@MockBean(reset = MockReset.BEFORE)
	protected UserClientService userService;
	
	@Rule
	public WireMockRule notesMock = new WireMockRule(8001);
	
	@Autowired
	protected WebApplicationContext webApplicationContext;
	
	@Autowired 
	protected FilterChainProxy filterChain;
	
	protected MockMvc mockMvc;
	protected String stubResponse;
	
	protected MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
	
	protected void mvcSetup() {
		this.mockMvc = webAppContextSetup(webApplicationContext)
				.addFilters(filterChain)
				.apply(springSecurity()).build();
	}
	
	public abstract void setup() throws Exception;
}
