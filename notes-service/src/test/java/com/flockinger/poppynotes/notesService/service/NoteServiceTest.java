package com.flockinger.poppynotes.notesService.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.flockinger.poppynotes.notesService.BaseDataBaseTest;
import com.flockinger.poppynotes.notesService.dto.CompleteNote;
import com.flockinger.poppynotes.notesService.dto.CreateNote;
import com.flockinger.poppynotes.notesService.dto.OverviewNote;
import com.flockinger.poppynotes.notesService.dto.UpdateNote;
import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;
import com.flockinger.poppynotes.notesService.model.ArchivedNote;
import com.flockinger.poppynotes.notesService.model.Note;
import com.flockinger.poppynotes.notesService.service.impl.NotesServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteServiceTest extends BaseDataBaseTest {

	@Autowired
	private NoteService service;

	@Test
	public void testFindNote_withValidUserId_shouldReturnCorrect()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		Note note = dao.findAll().stream().findFirst().get();

		CompleteNote readNote = service.findNote(note.getId(), 1l);

		assertFalse("read from normal notes, should not be set archived", BooleanUtils.isTrue(readNote.getArchived()));
		assertEquals("check content equals", note.getContent(), readNote.getContent());
		assertEquals("check last edit date equals", note.getLastEdit(), readNote.getLastEdit());
		assertEquals("check title equals", note.getTitle(), readNote.getTitle());
		assertEquals("check user ID equals", note.getUserId(), readNote.getUserId());
		assertEquals("check init vector equals", note.getInitVector(), readNote.getInitVector());
	}

	@Test
	public void testFindNote_withValidUserIdFromArchived_shouldReturnCorrect()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		Note note = archivedDao.findAll().stream().findFirst().get();

		CompleteNote readNote = service.findNote(note.getId(), 1l);

		assertTrue("read from normal archived notes, should be set archived", readNote.getArchived());
		assertEquals("check content equals", note.getContent(), readNote.getContent());
		assertEquals("check last edit date equals", note.getLastEdit(), readNote.getLastEdit());
		assertEquals("check title equals", note.getTitle(), readNote.getTitle());
		assertEquals("check user ID equals", note.getUserId(), readNote.getUserId());
		assertEquals("check init vector equals", note.getInitVector(), readNote.getInitVector());
	}

	@Test(expected = NoteNotFoundException.class)
	public void testFindNote_withUserWithNoNotes_shouldReturnEmpty()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		service.findNote("NonExistante", 1l);
	}

	@Test(expected = AccessingOtherUsersNotesException.class)
	public void testFindNote_withNoteFromOtherUser_shouldReturnCorrect()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		Note note = dao.findAll().stream().findFirst().get();

		service.findNote(note.getId(), 287l);
	}

	@Test
	public void testCreate_withValidEntry_shouldCreate() {
		CreateNote newNote = new CreateNote();
		newNote.setContent(
				"Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. De carne lumbering animata corpora quaeritis. Summus brains sit​​, morbo vel maleficia?");
		newNote.setTitle("Nescio brains an Undead zombies.");
		newNote.setLastEdit(new Date(0));
		newNote.setPinned(true);
		newNote.setInitVector("ikfyakhkjh89oyoi90pf73");
		newNote.setUserId(3l);

		service.create(newNote);
		
		List<OverviewNote> notes = service.findNotesByUserIdPaginated(3l, new PageRequest(0, 10));
		assertTrue("only one note from user 3", notes.size() == 1);
		assertEquals("check saved content","Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. De carne lumbering animata corpora quaeritis. Summus brains sit​​, morbo vel maleficia?",newNote.getContent());
		assertEquals("check saved title","Nescio brains an Undead zombies.",newNote.getTitle());
		assertEquals("check saved last edit date",new Date(0),newNote.getLastEdit());
		assertTrue("check saved pinned status", newNote.getPinned());
		assertEquals("check saved initialization vector","ikfyakhkjh89oyoi90pf73",newNote.getInitVector());
	}

	@Test
	public void testUpdate_withValidUpdate_shouldUpdate()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		UpdateNote update = new UpdateNote();
		update.setId(dao.findAll().stream().findFirst().get().getId());
		update.setContent(
				"De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris. ");
		update.setTitle("Nigh basal ganglia tofth eliv ingdead.");
		update.setLastEdit(new Date(0));
		update.setPinned(false);
		update.setUserId(1l);

		service.update(update);

		Note updatedNote = dao.findOne(update.getId());
		assertEquals("check updated content", updatedNote.getContent(), update.getContent());
		assertEquals("check updated last edit date", updatedNote.getLastEdit(), update.getLastEdit());
		assertEquals("check updated pinned status", updatedNote.getPinned(), update.getPinned());
		assertEquals("check updated title", updatedNote.getTitle(), update.getTitle());
		assertEquals("check unchanged userId", updatedNote.getUserId(), update.getUserId());
	}

	@Test(expected = NoteNotFoundException.class)
	public void testUpdate_withNonExistingEntry_shouldThrowException()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		UpdateNote update = new UpdateNote();
		update.setId("nonExista");
		update.setContent(
				"De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris.");
		update.setTitle("Nigh basal ganglia tofth eliv ingdead.");
		update.setLastEdit(new Date(0));
		update.setPinned(false);
		update.setUserId(1l);

		service.update(update);
	}

	@Test(expected = AccessingOtherUsersNotesException.class)
	public void testUpdate_withCorrectIdButWrongUser_shouldThrowException()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		UpdateNote update = new UpdateNote();
		update.setId(dao.findAll().stream().findFirst().get().getId());
		update.setContent(
				"De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris.");
		update.setTitle("Nigh basal ganglia tofth eliv ingdead.");
		update.setLastEdit(new Date(0));
		update.setPinned(false);
		update.setUserId(4765l);

		service.update(update);
	}

	@Test
	public void testUpdate_withRequestingArchiving_shouldSaveInArchiveAndDeleteInNotes()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		UpdateNote update = new UpdateNote();
		update.setId(dao.findAll().stream().findFirst().get().getId());
		update.setContent(
				"De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris. ");
		update.setTitle("Nigh basal ganglia tofth eliv ingdead.");
		update.setLastEdit(new Date(0));
		update.setPinned(false);
		update.setUserId(1l);
		update.setArchived(true);

		service.update(update);

		assertFalse("should be deleted on notes", dao.exists(update.getId()));

		ArchivedNote updatedNote = archivedDao.findOne(update.getId());
		assertEquals("check updated content", updatedNote.getContent(), update.getContent());
		assertEquals("check updated last edit date", updatedNote.getLastEdit(), update.getLastEdit());
		assertEquals("check updated pinned status", updatedNote.getPinned(), update.getPinned());
		assertEquals("check updated title", updatedNote.getTitle(), update.getTitle());
		assertEquals("check unchanged userId", updatedNote.getUserId(), update.getUserId());
	}

	@Test
	public void testUpdate_withRequestingUnArchiving_shouldDeleteInArchiveAndStoreInNotes()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		UpdateNote update = new UpdateNote();
		update.setId(archivedDao.findAll().stream().findFirst().get().getId());
		update.setContent(
				"De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris. ");
		update.setTitle("Nigh basal ganglia tofth eliv ingdead.");
		update.setLastEdit(new Date(0));
		update.setPinned(false);
		update.setUserId(1l);
		update.setArchived(false);

		service.update(update);

		assertFalse("should be deleted on archived-notes", archivedDao.exists(update.getId()));

		Note updatedNote = dao.findOne(update.getId());
		assertEquals("check updated content", updatedNote.getContent(), update.getContent());
		assertEquals("check updated last edit date", updatedNote.getLastEdit(), update.getLastEdit());
		assertEquals("check updated pinned status", updatedNote.getPinned(), update.getPinned());
		assertEquals("check updated title", updatedNote.getTitle(), update.getTitle());
		assertEquals("check unchanged userId", updatedNote.getUserId(), update.getUserId());
	}

	@Test
	public void testDelete_withValidIdAndUserId_shouldDelete()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		String id = dao.findAll().stream().findFirst().get().getId();
		service.delete(id, 1l);

		assertFalse("deleted note should not exist anymore", dao.exists(id));
	}
	@Test
	public void testDelete_withValidArchivedIdAndUserId_shouldDelete()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		String id = archivedDao.findAll().stream().findFirst().get().getId();
		service.delete(id, 1l);

		assertFalse("deleted note should not exist anymore", dao.exists(id));
	}

	@Test(expected = AccessingOtherUsersNotesException.class)
	public void testDelete_withValidIdAndWrongUser_shouldThrowException()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		String id = dao.findAll().stream().findFirst().get().getId();
		service.delete(id, 876876l);
	}

	@Test(expected = NoteNotFoundException.class)
	public void testDelete_withNonExistingId_shouldThrowException()
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		service.delete("nonExistante", 1l);
	}

	@Test
	public void testFindNotesByUserIdPaginated_withCorrectUserIdFirstPage4Items_shouldReturnCorrectAndSorted() {
		List<OverviewNote> notes = service.findNotesByUserIdPaginated(1l, new PageRequest(0, 4));

		assertEquals("should return 4 notes", 4, notes.size());
		assertTrue("should contain first pinned entry", notes.get(0).getTitle().equals("1-pinned-latest"));
		assertTrue("should contain second pinned entry", notes.get(1).getTitle().equals("2-pinned-second"));
		assertTrue("should contain third pinned entry", notes.get(2).getTitle().equals("3-pinned-third"));
		assertTrue("should contain first regular entry", notes.get(3).getTitle().equals("1latest"));
		OverviewNote firstNote = notes.get(0);
		Note expectedFirst = dao.findOne(firstNote.getId());

		assertEquals("check last edit", expectedFirst.getLastEdit(), firstNote.getLastEdit());
		assertEquals("check if part content is part of content",
				StringUtils.abbreviate(expectedFirst.getContent(), NotesServiceImpl.OVERVIEW_CONTENT_PART_LENGTH),
				firstNote.getPartContent());
		assertEquals("check title", expectedFirst.getTitle(), firstNote.getTitle());
		assertEquals("check pinned", expectedFirst.getPinned(), firstNote.getPinned());
	}

	@Test
	public void testFindNotesByUserIdPaginated_withUserIdWithNoNotes_shouldReturnEmpty() {
		List<OverviewNote> notes = service.findNotesByUserIdPaginated(87687l, new PageRequest(0, 5));

		assertNotNull("should not be null", notes);
		assertTrue("from user with no notes, should be empty", notes.size() == 0);
	}

	@Test
	public void testFindArchivedNotesByUserIdPaginated_withCorrectUserIdFirstPage4Items_shouldReturnCorrectAndSorted() {
		List<OverviewNote> notes = service.findArchivedNotesByUserIdPaginated(1l, new PageRequest(0, 4));

		assertEquals("should return 4 notes", 4, notes.size());
		assertTrue("should contain first pinned entry", notes.get(0).getTitle().equals("a1-pinned-latest"));
		assertTrue("should contain second pinned entry", notes.get(1).getTitle().equals("a2-pinned-second"));
		assertTrue("should contain third pinned entry", notes.get(2).getTitle().equals("a3-pinned-third"));
		assertTrue("should contain first regular entry", notes.get(3).getTitle().equals("a1latest"));
		OverviewNote firstNote = notes.get(0);
		Note expectedFirst = archivedDao.findOne(firstNote.getId());

		assertEquals("check last edit", expectedFirst.getLastEdit(), firstNote.getLastEdit());
		assertEquals("check if part content is part of content",
				StringUtils.abbreviate(expectedFirst.getContent(), NotesServiceImpl.OVERVIEW_CONTENT_PART_LENGTH),
				firstNote.getPartContent());
		assertEquals("check title", expectedFirst.getTitle(), firstNote.getTitle());
		assertEquals("check pinned", expectedFirst.getPinned(), firstNote.getPinned());
	}

	@Test
	public void testFindArchivedNotesByUserIdPaginated_withUserIdWithNoNotes_shouldReturnEmpty() {
		List<OverviewNote> notes = service.findArchivedNotesByUserIdPaginated(87687l, new PageRequest(0, 5));

		assertNotNull("should not be null", notes);
		assertTrue("from user with no notes, should be empty", notes.size() == 0);
	}
}
