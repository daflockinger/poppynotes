package com.flockinger.poppynotes.notesService.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
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
import com.flockinger.poppynotes.notesService.service.NoteService;

@Component
public class NotesServiceImpl implements NoteService {

  @Autowired
  private NoteRepository dao;

  @Autowired
  private ModelMapper mapper;
  
  @Value("${notes.settings.limits.max-title-length}")
  private Integer maxTitleLength;
  @Value("${notes.settings.limits.max-content-length}")
  private Integer maxContentLength;
  @Value("${notes.settings.limits.max-messages-per-user}")
  private Integer maxMessagesPerUser;

  @Override
  public CompleteNote findNote(String id, String userHash)
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    Note note = findNoteById(id);
    assertCorrectNoteUser(note.getUserHash(), userHash);

    return map(note);
  }

  private void assertCorrectNoteUser(String internalUserHash, String externalUserHash)
      throws AccessingOtherUsersNotesException {
    if (!StringUtils.equals(internalUserHash, externalUserHash)) {
      throw new AccessingOtherUsersNotesException("Cannot access/edit other user's note entries!!");
    }
  }


  private Note findNoteById(String id) throws NoteNotFoundException {
    Optional<Note> note = dao.findById(id);
    assertIsPresent(note, id);

    return note.get();
  }

  private void assertIsPresent(Optional<Note> possibleNote, String id)
      throws NoteNotFoundException {
    if (!possibleNote.isPresent()) {
      throw new NoteNotFoundException("Note not found with ID: " + id);
    }
  }

  @Override
  public CompleteNote create(CreateNote note) throws CantUseInitVectorTwiceException {
    assertInitVectorNotAlreadyUsed(note.getInitVector(), note.getUserHash());
    Note createdNote = map(note);
    validateNote(createdNote);
    
    return map(dao.save(createdNote));
  }

  @Override
  public void update(UpdateNote note) throws NoteNotFoundException,
      AccessingOtherUsersNotesException, CantUseInitVectorTwiceException {
    assertNoteExisting(note.getId());
    assertInitVectorNotAlreadyUsed(note.getInitVector(), note.getUserHash());
    assertCorrectNoteUser(findNoteById(note.getId()).getUserHash(), note.getUserHash());
    Note updateNote = map(note);
    validateNote(updateNote);
    
    dao.save(updateNote);
  }

  private void assertNoteExisting(String noteId) throws NoteNotFoundException {
    if (!dao.existsById(noteId)) {
      throw new NoteNotFoundException("Note not found with ID: " + noteId);
    }
  }

  private void assertInitVectorNotAlreadyUsed(String initVector, String userHash)
      throws CantUseInitVectorTwiceException {
    if (dao.existsByUserHashAndInitVector(userHash, initVector)) {
      throw new CantUseInitVectorTwiceException("Can't use one InitVector twice!");
    }
  }

  @Override
  public void delete(String id, String userHash)
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    assertNoteExisting(id);
    assertCorrectNoteUser(findNoteById(id).getUserHash(), userHash);

    dao.deleteById(id);
  }
  
  @Override
  public void pinNote(PinNote pinNote, String userHash) throws NoteNotFoundException, AccessingOtherUsersNotesException {
    assertNoteExisting(pinNote.getNoteId());
    Note note = findNoteById(pinNote.getNoteId());
    assertCorrectNoteUser(note.getUserHash(), userHash);
    
    note.setPinned(pinNote.isPinIt());
    dao.save(note);
  }

  @Override
  public List<OverviewNote> findNotesByUserHashPaginated(String userHash, Pageable page) {
    Sort pinnedFirstLastEditDescOrder = Sort.by(Order.desc("pinned"), Order.desc("lastEdit"));
    PageRequest paging =
        PageRequest.of(page.getPageNumber(), page.getPageSize(), pinnedFirstLastEditDescOrder);
    List<Note> notes = dao.findByUserHash(userHash, paging);
    return notes.stream().map(this::mapOverview).collect(Collectors.toList());
  }

  private void validateNote(Note note) throws NoteSizeExceededException {
    if(StringUtils.length(note.getTitle()) > maxTitleLength) {
      throw new NoteSizeExceededException("Title exceeded max length of " + maxTitleLength + " characters!");
    }
    if(StringUtils.length(note.getContent()) > maxContentLength) {
      throw new NoteSizeExceededException("Note content exceeded max length of " + maxContentLength + " characters!");
    }
    Long usersNoteCount = dao.countByUserHash(note.getUserHash());
    if(usersNoteCount >= maxMessagesPerUser) {
      throw new NoteSizeExceededException("Max number of notes per user reached (" + maxContentLength + ")!");
    }
  }
  

  private OverviewNote mapOverview(Note note) {
    OverviewNote overview = mapper.map(note, OverviewNote.class);
    return overview;
  }

  private Note map(UpdateNote note) {
    return mapper.map(note, Note.class);
  }

  private Note map(CreateNote note) {
    return mapper.map(note, Note.class);
  }

  private CompleteNote map(Note note) {
    CompleteNote completeNote = mapper.map(note, CompleteNote.class);
    return completeNote;
  }
}
