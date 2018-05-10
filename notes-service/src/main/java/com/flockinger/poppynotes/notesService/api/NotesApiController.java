package com.flockinger.poppynotes.notesService.api;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.flockinger.poppynotes.notesService.dto.CompleteNote;
import com.flockinger.poppynotes.notesService.dto.CreateNote;
import com.flockinger.poppynotes.notesService.dto.OverviewNote;
import com.flockinger.poppynotes.notesService.dto.UpdateNote;
import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.DtoValidationFailedException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;
import com.flockinger.poppynotes.notesService.service.NoteService;
import io.swagger.annotations.ApiParam;

@RestController
public class NotesApiController implements NotesApi {

  @Autowired
  private NoteService service;


  @Override
  public ResponseEntity<CompleteNote> createNote(@Valid @RequestBody CreateNote noteCreate) {
    CompleteNote note = service.create(noteCreate);
    return new ResponseEntity<CompleteNote>(note, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<CompleteNote> findNote(
      @ApiParam(value = "Unique identifier of a Note.",
          required = true) @PathVariable("noteId") String noteId,
      @ApiParam(value = "Unique Identifier of the User requesting his notes.",
          required = true) @RequestHeader(value = "userId", required = true) String userId)
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    CompleteNote note = service.findNote(noteId, userId);
    return new ResponseEntity<CompleteNote>(note, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<List<OverviewNote>> getNotes(
      @ApiParam(value = "Unique Identifier of the User requesting his notes.",
          required = true) @RequestHeader(value = "userId", required = true) String userId,
      @ApiParam(value = "Page of notes that's beeing returned.",
          defaultValue = "0") @Valid @RequestParam(value = "page", required = false,
              defaultValue = "0") Integer page,
      @ApiParam(value = "Amount of notes per page.", defaultValue = "10") @Valid @RequestParam(
          value = "items", required = false, defaultValue = "10") Integer items)
      throws DtoValidationFailedException {
    assertPagination(page, items);
    List<OverviewNote> notes =
        service.findNotesByUserHashPaginated(userId, PageRequest.of(page, items));
    return new ResponseEntity<List<OverviewNote>>(notes, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> removeNote(
      @ApiParam(value = "Unique identifier of a Note.",
          required = true) @PathVariable("noteId") String noteId,
      @RequestHeader(value = "userId", required = true) String userId)
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    service.delete(noteId, userId);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> updateNote(@Valid @RequestBody UpdateNote noteUpdate)
      throws NoteNotFoundException, AccessingOtherUsersNotesException {
    service.update(noteUpdate);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  private void assertPagination(Integer page, Integer items) throws DtoValidationFailedException {
    if (page == null || page < 0) {
      throw new DtoValidationFailedException("Page number must be at least 0!", new ArrayList<>());
    } else if (items == null || items < 1) {
      throw new DtoValidationFailedException("Displayed items per page must be at leasst 1!",
          new ArrayList<>());
    }
  }
}
