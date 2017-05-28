package com.flockinger.poppynotes.notesService.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

	public ResponseEntity<List<OverviewNote>> apiV1NotesGet(
			@ApiParam(value = "Unique Identifier of the User requesting his notes.", required = true) @RequestHeader(value = "userId", required = true) Long userId,
			@ApiParam(value = "Page of notes that's beeing returned.", defaultValue = "0") @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@ApiParam(value = "Amount of notes per page.", defaultValue = "10") @RequestParam(value = "items", required = false, defaultValue = "5") Integer items,
			@ApiParam(value = "If true, only archived notes are returned.", defaultValue = "false") @RequestParam(value = "showArchived", required = false, defaultValue = "false") Boolean showArchived)
			throws DtoValidationFailedException {
		
		assertPagination(page, items);
		List<OverviewNote> notes = new ArrayList<>();
		
		if (showArchived) {
			notes = service.findArchivedNotesByUserIdPaginated(userId, new PageRequest(page, items));
		} else {
			notes = service.findNotesByUserIdPaginated(userId, new PageRequest(page, items));
		}
		return new ResponseEntity<List<OverviewNote>>(notes,HttpStatus.OK);
	}

	public ResponseEntity<Void> apiV1NotesNoteIdDelete(
			@ApiParam(value = "Unique identifier of a Note.", required = true) @PathVariable("noteId") String noteId,
			@RequestHeader(value = "userId", required = true) Long userId)
			throws NoteNotFoundException, AccessingOtherUsersNotesException {

		service.delete(noteId, userId);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<CompleteNote> apiV1NotesNoteIdGet(
			@ApiParam(value = "Unique identifier of a Note.", required = true) @PathVariable("noteId") String noteId,
			@RequestHeader(value = "userId", required = true) Long userId)
			throws NoteNotFoundException, AccessingOtherUsersNotesException {

		CompleteNote note = service.findNote(noteId, userId);
		return new ResponseEntity<CompleteNote>(note, HttpStatus.OK);
	}

	public ResponseEntity<Void> apiV1NotesPost(
			@ApiParam(value = "", required = true) @Valid @RequestBody CreateNote noteCreate, BindingResult bindingResult)
			throws DtoValidationFailedException {

		assertEntityValidity(bindingResult);
		service.create(noteCreate);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

	public ResponseEntity<Void> apiV1NotesPut(@ApiParam(value = "", required = true) @Valid @RequestBody UpdateNote noteUpdate,
			BindingResult bindingResult)
			throws DtoValidationFailedException, NoteNotFoundException, AccessingOtherUsersNotesException {

		assertEntityValidity(bindingResult);
		service.update(noteUpdate);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	private void assertEntityValidity(BindingResult bindingResult) throws DtoValidationFailedException {
		if (bindingResult.hasErrors()) {
			throw new DtoValidationFailedException("Validation failed!", bindingResult.getFieldErrors());
		}
	}

	private void assertPagination(Integer page, Integer items) throws DtoValidationFailedException {
		if (page < 0) {
			throw new DtoValidationFailedException("Page number must be at least 0!", new ArrayList<>());
		} else if (items < 1) {
			throw new DtoValidationFailedException("Displayed items per page must be at leasst 1!", new ArrayList<>());
		}
	}
}
