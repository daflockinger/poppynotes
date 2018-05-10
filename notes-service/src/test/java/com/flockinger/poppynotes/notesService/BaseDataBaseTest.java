package com.flockinger.poppynotes.notesService;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import com.flockinger.poppynotes.notesService.dao.NoteRepository;

public abstract class BaseDataBaseTest {

	@Autowired
	protected NoteRepository dao;

	@Before
	public void setup() {
		dao.insert(TestDataFactory.getSimpleTestNotes());
	}
	
	@After
	public void teardown() {
		dao.deleteAll();
	}
}
