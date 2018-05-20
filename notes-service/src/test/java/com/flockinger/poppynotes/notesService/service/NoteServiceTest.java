package com.flockinger.poppynotes.notesService.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.sql.Date;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.flockinger.poppynotes.notesService.BaseDataBaseTest;
import com.flockinger.poppynotes.notesService.config.DatabaseConfig;
import com.flockinger.poppynotes.notesService.config.GeneralConfig;
import com.flockinger.poppynotes.notesService.dao.NoteRepository;
import com.flockinger.poppynotes.notesService.dto.CompleteNote;
import com.flockinger.poppynotes.notesService.dto.CreateNote;
import com.flockinger.poppynotes.notesService.dto.OverviewNote;
import com.flockinger.poppynotes.notesService.dto.PinNote;
import com.flockinger.poppynotes.notesService.dto.UpdateNote;
import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.CantUseInitVectorTwiceException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;
import com.flockinger.poppynotes.notesService.exception.NoteSizeExceededException;
import com.flockinger.poppynotes.notesService.model.Note;
import com.flockinger.poppynotes.notesService.service.impl.NotesServiceImpl;

@ContextConfiguration(classes = {NotesServiceImpl.class, ModelMapper.class, NoteRepository.class})
@RunWith(SpringRunner.class)
@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)
@Import({GeneralConfig.class, DatabaseConfig.class})
@TestPropertySource(properties= {"notes.settings.limits.max-title-length=50",
    "notes.settings.limits.max-content-length=100",
    "notes.settings.limits.max-messages-per-user=11"})
public class NoteServiceTest extends BaseDataBaseTest {

  @Autowired
  private NoteService service;

  @Test
  public void testFindNote_withValidUserId_shouldReturnCorrect()
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    Note note = dao.findAll().stream().findFirst().get();

    CompleteNote readNote = service.findNote(note.getId(), "1");

    assertEquals("check content equals", note.getContent(), readNote.getContent());
    assertEquals("check last edit date equals", note.getLastEdit(), readNote.getLastEdit());
    assertEquals("check title equals", note.getTitle(), readNote.getTitle());
    assertEquals("check user ID equals", note.getUserHash(), readNote.getUserHash());
  }


  @Test(expected = NoteNotFoundException.class)
  public void testFindNote_withUserWithNoNotes_shouldReturnEmpty()
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    service.findNote("NonExistante", "1");
  }

  @Test(expected = AccessingOtherUsersNotesException.class)
  public void testFindNote_withNoteFromOtherUser_shouldReturnCorrect()
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    Note note = dao.findAll().stream().findFirst().get();

    service.findNote(note.getId(), "287");
  }

  @Test
  public void testCreate_withValidEntry_shouldCreate() throws CantUseInitVectorTwiceException {
    CreateNote newNote = new CreateNote();
    newNote.setContent(
        "Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. brains morbo vel maleficia?");
    newNote.setTitle("Nescio brains an Undead zombies.");
    newNote.setLastEdit(new Date(0));
    newNote.setUserHash("3");
    newNote.setInitVector("8888888");

    service.create(newNote);

    List<OverviewNote> notes = service.findNotesByUserHashPaginated("3", PageRequest.of(0, 10));
    assertTrue("only one note from user 3", notes.size() == 1);
    assertEquals("check saved content",
        "Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. brains morbo vel maleficia?",
        newNote.getContent());
    assertEquals("check saved title", "Nescio brains an Undead zombies.", notes.get(0).getTitle());
    assertEquals("check saved last edit date", new Date(0), notes.get(0).getLastEdit());
    assertNull("check saved pinned status", notes.get(0).isPinned());
  }
  
  @Test(expected=NoteSizeExceededException.class)
  public void testCreate_withTooLongTitle_shouldThrowException() throws CantUseInitVectorTwiceException {
    CreateNote newNote = new CreateNote();
    newNote.setContent(
        "Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. brains morbo vel maleficia?");
    newNote.setTitle("Nescio brains an Undead zombies.an Undead zombiies.");
    newNote.setLastEdit(new Date(0));
    newNote.setUserHash("3");
    newNote.setInitVector("8888888");

    service.create(newNote);
  }
  
  @Test(expected=NoteSizeExceededException.class)
  public void testCreate_withTooLongContent_shouldThrowException() throws CantUseInitVectorTwiceException {
    CreateNote newNote = new CreateNote();
    newNote.setContent(
        "Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. brains morbo vel maleficia????");
    newNote.setTitle("Nescio brains an Undead zombies.");
    newNote.setLastEdit(new Date(0));
    newNote.setUserHash("3");
    newNote.setInitVector("8888888");

    service.create(newNote);
  }
  
  @Test(expected=NoteSizeExceededException.class)
  public void testCreate_withMaxEntriesEachedPerUser_shouldThrowException() throws CantUseInitVectorTwiceException {
    CreateNote newNote = new CreateNote();
    newNote.setContent(
        "Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. brains morbo vel maleficia?");
    newNote.setTitle("Nescio brains an Undead zombies.");
    newNote.setLastEdit(new Date(0));
    newNote.setUserHash("1");
    newNote.setInitVector("8888888");

    service.create(newNote);
    
    CreateNote newNote2 = new CreateNote();
    newNote2.setContent(
        "Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. brains morbo vel maleficia?");
    newNote2.setTitle("Nescio brains an Undead zombies.");
    newNote2.setLastEdit(new Date(0));
    newNote2.setUserHash("1");
    newNote2.setInitVector("8887778");

    service.create(newNote2);
  }

  @Test(expected = CantUseInitVectorTwiceException.class)
  public void testCreate_withAlreadyUsedInitVector_shouldThrowException()
      throws CantUseInitVectorTwiceException {
    CreateNote newNote = new CreateNote();
    newNote.setContent(
        "Zombie ipsum reversus ab viral inferno, nam rick grimes malum cerebro. brains morbo vel maleficia?");
    newNote.setTitle("Nescio brains an Undead zombies.");
    newNote.setLastEdit(new Date(0));
    newNote.setInitVector("4321");
    newNote.setUserHash("1");

    service.create(newNote);
  }

  @Test
  public void testUpdate_withValidUpdate_shouldUpdate() throws NoteNotFoundException,
      AccessingOtherUsersNotesException, CantUseInitVectorTwiceException {
    UpdateNote update = new UpdateNote();
    update.setId(dao.findAll().stream().findFirst().get().getId());
    update.setContent(
        "De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris. ");
    update.setTitle("Nigh basal ganglia tofth eliv ingdead.");
    update.setLastEdit(new Date(0));
    update.setPinned(false);
    update.setUserHash("1");
    update.setInitVector("99999999");

    service.update(update);

    Note updatedNote = dao.findById(update.getId()).get();
    assertEquals("check updated content", updatedNote.getContent(), update.getContent());
    assertEquals("check updated last edit date", updatedNote.getLastEdit(), update.getLastEdit());
    assertEquals("check updated pinned status", updatedNote.getPinned(), update.isPinned());
    assertEquals("check updated title", updatedNote.getTitle(), update.getTitle());
    assertEquals("check unchanged userId", updatedNote.getUserHash(), update.getUserHash());
  }
  
  @Test(expected=NoteSizeExceededException.class)
  public void testUpdate_withTooLongTitle_shouldThrowException() throws NoteNotFoundException,
      AccessingOtherUsersNotesException, CantUseInitVectorTwiceException {
    UpdateNote update = new UpdateNote();
    update.setId(dao.findAll().stream().findFirst().get().getId());
    update.setContent(
        "De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris. ");
    update.setTitle("Nigh basal ganglia tofth eliv ingdead. Zombiiieee!!");
    update.setLastEdit(new Date(0));
    update.setPinned(false);
    update.setUserHash("1");
    update.setInitVector("99999999");

    service.update(update);
  }
  
  @Test(expected=NoteSizeExceededException.class)
  public void testUpdate_withTooLongContent_shouldThrowException() throws NoteNotFoundException,
      AccessingOtherUsersNotesException, CantUseInitVectorTwiceException {
    UpdateNote update = new UpdateNote();
    update.setId(dao.findAll().stream().findFirst().get().getId());
    update.setContent(
        "De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris. Braaaiiiins!");
    update.setTitle("Nigh basal ganglia tofth eliv ingdead.");
    update.setLastEdit(new Date(0));
    update.setPinned(false);
    update.setUserHash("1");
    update.setInitVector("99999999");

    service.update(update);
  }

  @Test(expected = CantUseInitVectorTwiceException.class)
  public void testUpdate_withAlreadyExistingInitVector_shouldThrowException()
      throws NoteNotFoundException, AccessingOtherUsersNotesException,
      CantUseInitVectorTwiceException {
    UpdateNote update = new UpdateNote();
    update.setId(dao.findAll().stream().findFirst().get().getId());
    update.setContent(
        "De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris. ");
    update.setTitle("Nigh basal ganglia tofth eliv ingdead.");
    update.setLastEdit(new Date(0));
    update.setPinned(false);
    update.setInitVector("4321");
    update.setUserHash("1");

    service.update(update);
  }

  @Test(expected = NoteNotFoundException.class)
  public void testUpdate_withNonExistingEntry_shouldThrowException() throws NoteNotFoundException,
      AccessingOtherUsersNotesException, CantUseInitVectorTwiceException {
    UpdateNote update = new UpdateNote();
    update.setId("nonExista");
    update.setContent(
        "De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris.");
    update.setTitle("Nigh basal ganglia tofth eliv ingdead.");
    update.setLastEdit(new Date(0));
    update.setPinned(false);
    update.setUserHash("1");

    service.update(update);
  }

  @Test(expected = AccessingOtherUsersNotesException.class)
  public void testUpdate_withCorrectIdButWrongUser_shouldThrowException()
      throws NoteNotFoundException, AccessingOtherUsersNotesException,
      CantUseInitVectorTwiceException {
    UpdateNote update = new UpdateNote();
    update.setId(dao.findAll().stream().findFirst().get().getId());
    update.setContent(
        "De braaaiiiins apocalypsi gorger omero prefrontal cortex undead survivor fornix dictum mauris.");
    update.setTitle("Nigh basal ganglia tofth eliv ingdead.");
    update.setLastEdit(new Date(0));
    update.setPinned(false);
    update.setUserHash("4765");

    service.update(update);
  }

  @Test
  public void testDelete_withValidIdAndUserId_shouldDelete()
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    Note noteToDelete = dao.findAll().stream().findFirst().get();
    service.delete(noteToDelete.getId(), noteToDelete.getUserHash());

    assertFalse("deleted note should not exist anymore", dao.existsById(noteToDelete.getId()));
  }

  @Test(expected = AccessingOtherUsersNotesException.class)
  public void testDelete_withValidIdAndWrongUser_shouldThrowException()
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    String id = dao.findAll().stream().findFirst().get().getId();
    service.delete(id, "NONEXISTANTE");
  }

  @Test(expected = NoteNotFoundException.class)
  public void testDelete_withNonExistingId_shouldThrowException()
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    service.delete("nonExistante", "1");
  }

  @Test
  public void testfindNotesByUserHashPaginated_withCorrectUserIdFirstPage4Items_shouldReturnCorrectAndSorted() {
    List<OverviewNote> notes = service.findNotesByUserHashPaginated("1", PageRequest.of(0, 4));

    assertEquals("should return 4 notes", 4, notes.size());
    assertEquals("should contain first pinned entry", "1-pinned-latest", notes.get(0).getTitle());
    assertEquals("should contain second pinned entry", "2-pinned-second", notes.get(1).getTitle());
    assertEquals("should contain third pinned entry", "3-pinned-third", notes.get(2).getTitle());
    assertEquals("should contain first regular entry", "1latest", notes.get(3).getTitle());
    OverviewNote firstNote = notes.get(0);
    Note expectedFirst = dao.findById(firstNote.getId()).get();

    assertEquals("check last edit", expectedFirst.getLastEdit(), firstNote.getLastEdit());
    assertEquals("check if part content is part of content", expectedFirst.getContent(),
        firstNote.getContent());
    assertEquals("check title", expectedFirst.getTitle(), firstNote.getTitle());
    assertEquals("check pinned", expectedFirst.getPinned(), firstNote.isPinned());
  }

  @Test
  public void testfindNotesByUserHashPaginated_withUserIdWithNoNotes_shouldReturnEmpty() {
    List<OverviewNote> notes = service.findNotesByUserHashPaginated("87687", PageRequest.of(0, 5));

    assertNotNull("should not be null", notes);
    assertTrue("from user with no notes, should be empty", notes.size() == 0);
  }
  
  @Test
  public void testPinNote_withValidPinRequest_shouldPinCorrectly() throws NoteNotFoundException, AccessingOtherUsersNotesException {
    Note unpinnedNote = dao.findAll().stream().filter(note -> !note.getPinned()).findFirst().get();
    PinNote request = new PinNote();
    request.setNoteId(unpinnedNote.getId());
    request.setPinIt(true);
    
    service.pinNote(request, unpinnedNote.getUserHash());
    
    Note maybePinned = dao.findById(unpinnedNote.getId()).get();
    assertEquals("verify that note was pinned", true, maybePinned.getPinned());
  }
  
  @Test(expected=NoteNotFoundException.class)
  public void testPinNote_withNotExistingUserId_shouldThrowException() throws NoteNotFoundException, AccessingOtherUsersNotesException {
    Note unpinnedNote = dao.findAll().stream().filter(note -> !note.getPinned()).findFirst().get();
    PinNote request = new PinNote();
    request.setNoteId("NONEXISTANTE");
    request.setPinIt(true);
    
    service.pinNote(request, unpinnedNote.getUserHash());
  }
  
  @Test(expected=AccessingOtherUsersNotesException.class)
  public void testPinNote_withNotCorrectIdButWrongUser_shouldThrowException() throws NoteNotFoundException, AccessingOtherUsersNotesException {
    Note unpinnedNote = dao.findAll().stream().filter(note -> !note.getPinned()).findFirst().get();
    PinNote request = new PinNote();
    request.setNoteId(unpinnedNote.getId());
    request.setPinIt(true);
    
    service.pinNote(request, "hACkeR");
  }
  
}
