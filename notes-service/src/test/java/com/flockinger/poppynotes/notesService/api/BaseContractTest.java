package com.flockinger.poppynotes.notesService.api;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

import com.flockinger.poppynotes.notesService.TestDataFactory;
import com.flockinger.poppynotes.notesService.dao.ArchivedNoteRepository;
import com.flockinger.poppynotes.notesService.dao.NoteRepository;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(profiles = { "default", "test" })
public abstract class BaseContractTest {

	@Autowired
	protected NoteRepository dao;
	@Autowired
	protected ArchivedNoteRepository archivedDao;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		dao.insert(TestDataFactory.getSimpleTestNotes());
		archivedDao.insert(TestDataFactory.getSimpleTestArchivedNotes());
		RestAssuredMockMvc.mockMvc(webAppContextSetup(webApplicationContext).build());
	}
	
	@After
	public void teardown() {
		dao.deleteAll();
		archivedDao.deleteAll();
	}
}
