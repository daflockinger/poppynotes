package com.flockinger.poppynotes.notesService.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.flockinger.poppynotes.notesService.dto.CompleteNote;
import com.flockinger.poppynotes.notesService.dto.CreateNote;
import com.flockinger.poppynotes.notesService.dto.Error;
import com.flockinger.poppynotes.notesService.dto.OverviewNote;
import com.flockinger.poppynotes.notesService.dto.UpdateNote;
import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.DtoValidationFailedException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "api", description = "the api API")
public interface NotesApi {

	@ApiOperation(value = "Get All Notes from user", notes = "Returns all Notes in a shorter form from a user, paginated.", response = OverviewNote.class, responseContainer = "List", tags = {
			"Notes", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All notes paginated.", response = OverviewNote.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class) })
	@RequestMapping(value = "/api/v1/notes", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<List<OverviewNote>> apiV1NotesGet(
			@ApiParam(value = "Unique Identifier of the User requesting his notes.", required = true) @RequestHeader(value = "userId", required = true) Long userId,
			@ApiParam(value = "Page of notes that's beeing returned.", defaultValue = "0") @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@ApiParam(value = "Amount of notes per page.", defaultValue = "10") @RequestParam(value = "items", required = false, defaultValue = "10") Integer items,
			@ApiParam(value = "If true, only archived notes are returned.", defaultValue = "false") @RequestParam(value = "showArchived", required = false, defaultValue = "false") Boolean showArchived) throws DtoValidationFailedException;

	@ApiOperation(value = "Delete Note", notes = "Deletes a Note with defined Id.", response = Void.class, tags = {
			"Notes", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Note deleted.", response = Void.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class) })
	@RequestMapping(value = "/api/v1/notes/{noteId}", produces = { "application/json" }, method = RequestMethod.DELETE)
	ResponseEntity<Void> apiV1NotesNoteIdDelete(
			@ApiParam(value = "Unique identifier of a Note.", required = true) @PathVariable("noteId") String noteId,
			@RequestHeader(value = "userId", required = true) Long userId)
			throws NoteNotFoundException, AccessingOtherUsersNotesException;

	@ApiOperation(value = "Get Note", notes = "Fetches Note with defined Id.", response = CompleteNote.class, tags = {
			"Notes", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Note entity.", response = CompleteNote.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class) })
	@RequestMapping(value = "/api/v1/notes/{noteId}", produces = { "application/json" }, method = RequestMethod.GET)
	ResponseEntity<CompleteNote> apiV1NotesNoteIdGet(
			@ApiParam(value = "Unique identifier of a Note.", required = true) @PathVariable("noteId") String noteId,
			@RequestHeader(value = "userId", required = true) Long userId)
			throws NoteNotFoundException, AccessingOtherUsersNotesException;

	@ApiOperation(value = "Create Note", notes = "Creates new Note entry.", response = Void.class, tags = { "Notes", })
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Created Note.", response = Void.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class) })
	@RequestMapping(value = "/api/v1/notes", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.POST)
	ResponseEntity<Void> apiV1NotesPost(
			@ApiParam(value = "", required = true) @RequestBody CreateNote noteCreate,
			BindingResult bindingResult) throws DtoValidationFailedException;

	@ApiOperation(value = "Update Note", notes = "Updated a Note entry.", response = Void.class, tags = { "Notes", })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Note updated.", response = Void.class),
			@ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
			@ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
			@ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
			@ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error.", response = Error.class) })
	@RequestMapping(value = "/api/v1/notes", produces = { "application/json" }, consumes = {
			"application/json" }, method = RequestMethod.PUT)
	ResponseEntity<Void> apiV1NotesPut(@ApiParam(value = "", required = true) @RequestBody UpdateNote noteUpdate,
			BindingResult bindingResult)
			throws DtoValidationFailedException, NoteNotFoundException, AccessingOtherUsersNotesException;

}
