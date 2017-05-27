package com.flockinger.poppynotes.notesService;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.poppynotes.notesService.dao.ArchivedNoteRepository;
import com.flockinger.poppynotes.notesService.dao.NoteRepository;

public abstract class BaseDataBaseTest {

	@Autowired
	protected NoteRepository dao;
	@Autowired
	protected ArchivedNoteRepository archivedDao;

	@Before
	public void setup() {
		dao.insert(TestDataFactory.getSimpleTestNotes());
		archivedDao.insert(TestDataFactory.getSimpleTestArchivedNotes());
	}
	
	@After
	public void teardown() {
		dao.deleteAll();
		archivedDao.deleteAll();
	}
}
