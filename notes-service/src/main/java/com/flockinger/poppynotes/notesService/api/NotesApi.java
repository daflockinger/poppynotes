package com.flockinger.poppynotes.notesService.api;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
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
import com.flockinger.poppynotes.notesService.dto.PinNote;
import com.flockinger.poppynotes.notesService.dto.UpdateNote;
import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.CantUseInitVectorTwiceException;
import com.flockinger.poppynotes.notesService.exception.DtoValidationFailedException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen",
    date = "2018-05-10T08:59:41.039Z")

@Api(value = "api", description = "the api API")
public interface NotesApi {

  @ApiOperation(value = "Create Note", nickname = "createNote", notes = "Creates new Note entry.",
      response = CompleteNote.class, tags = {"Notes",})
  @ApiResponses(
      value = {@ApiResponse(code = 201, message = "Created Note.", response = CompleteNote.class),
          @ApiResponse(code = 400, message = "Bad request (validation failed).",
              response = Error.class),
          @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token)."),
          @ApiResponse(code = 403, message = "Forbidden (no rights to access resource)."),
          @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
          @ApiResponse(code = 409, message = "Request results in a conflict.",
              response = Error.class),
          @ApiResponse(code = 500, message = "Internal Server Error.")})
  @RequestMapping(value = "/api/v1/notes", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.POST)
  ResponseEntity<CompleteNote> createNote(
      @ApiParam(value = "", required = true) @Valid @RequestBody CreateNote noteCreate,
      @ApiParam(value = "Unique Identifier of the User requesting his notes.",
          required = true) @RequestHeader(value = "userId", required = true) String userId)
      throws CantUseInitVectorTwiceException;


  @ApiOperation(value = "Get Note", nickname = "findNote", notes = "Fetches Note with defined Id.",
      response = CompleteNote.class, tags = {"Notes",})
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Note entity.", response = CompleteNote.class),
          @ApiResponse(code = 400, message = "Bad request (validation failed).",
              response = Error.class),
          @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token)."),
          @ApiResponse(code = 403, message = "Forbidden (no rights to access resource)."),
          @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
          @ApiResponse(code = 409, message = "Request results in a conflict.",
              response = Error.class),
          @ApiResponse(code = 500, message = "Internal Server Error.")})
  @RequestMapping(value = "/api/v1/notes/{noteId}", produces = {"application/json"}, method = RequestMethod.GET)
  ResponseEntity<CompleteNote> findNote(
      @ApiParam(value = "Unique identifier of a Note.",
          required = true) @PathVariable("noteId") String noteId,
      @ApiParam(value = "Unique Identifier of the User requesting his notes.",
          required = true) @RequestHeader(value = "userId", required = true) String userId)
      throws NoteNotFoundException, AccessingOtherUsersNotesException;


  @ApiOperation(value = "Get All Notes from user", nickname = "getNotes",
      notes = "Returns all Notes in a shorter form from a user, paginated.",
      response = OverviewNote.class, responseContainer = "List", tags = {"Notes",})
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "All notes paginated.", response = OverviewNote.class,
          responseContainer = "List"),
      @ApiResponse(code = 400, message = "Bad request (validation failed).",
          response = Error.class),
      @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token)."),
      @ApiResponse(code = 403, message = "Forbidden (no rights to access resource)."),
      @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
      @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error.")})
  @RequestMapping(value = "/api/v1/notes", produces = {"application/json"},
      method = RequestMethod.GET)
  ResponseEntity<List<OverviewNote>> getNotes(
      @ApiParam(value = "Unique Identifier of the User requesting his notes.",
          required = true) @RequestHeader String userId,
      @ApiParam(value = "Page of notes that's beeing returned.",
          defaultValue = "0") @Valid @RequestParam(value = "page", required = false,
              defaultValue = "0") Integer page,
      @ApiParam(value = "Amount of notes per page.", defaultValue = "10") @Valid @RequestParam(
          value = "items", required = false, defaultValue = "10") Integer items)
      throws DtoValidationFailedException;

  @ApiOperation(value = "Pin Note", nickname = "pinNote", notes = "Pin a Note.", tags = {"Notes",})
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Note pinned/unpinned."),
      @ApiResponse(code = 400, message = "Bad request (validation failed).",
          response = Error.class),
      @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token)."),
      @ApiResponse(code = 403, message = "Forbidden (no rights to access resource)."),
      @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
      @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error.")})
  @RequestMapping(value = "/api/v1/notes/pin", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.PUT)
  ResponseEntity<Void> pinNote(
      @ApiParam(value = "", required = true) @Valid @RequestBody PinNote pinNote,
      @ApiParam(value = "Unique Identifier of the User requesting his notes.",
          required = true) @RequestHeader(value = "userId", required = true) String userId)
      throws NoteNotFoundException, AccessingOtherUsersNotesException;


  @ApiOperation(value = "Delete Note", nickname = "removeNote",
      notes = "Deletes a Note with defined Id.", tags = {"Notes",})
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Note deleted."),
      @ApiResponse(code = 400, message = "Bad request (validation failed).",
          response = Error.class),
      @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token)."),
      @ApiResponse(code = 403, message = "Forbidden (no rights to access resource)."),
      @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
      @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error.")})
  @RequestMapping(value = "/api/v1/notes/{noteId}", produces = {"application/json"},
      method = RequestMethod.DELETE)
  ResponseEntity<Void> removeNote(
      @ApiParam(value = "Unique identifier of a Note.",
          required = true) @PathVariable("noteId") String noteId,
      @ApiParam(value = "Unique Identifier of the User requesting his notes.",
          required = true) @RequestHeader(value = "userId", required = true) String userId)
      throws NoteNotFoundException, AccessingOtherUsersNotesException;


  @ApiOperation(value = "Update Note", nickname = "updateNote", notes = "Updated a Note entry.",
      tags = {"Notes",})
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Note updated."),
      @ApiResponse(code = 400, message = "Bad request (validation failed).",
          response = Error.class),
      @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token)."),
      @ApiResponse(code = 403, message = "Forbidden (no rights to access resource)."),
      @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
      @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
      @ApiResponse(code = 500, message = "Internal Server Error.")})
  @RequestMapping(value = "/api/v1/notes", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.PUT)
  ResponseEntity<Void> updateNote(
      @ApiParam(value = "", required = true) @Valid @RequestBody UpdateNote noteUpdate,
      @ApiParam(value = "Unique Identifier of the User requesting his notes.",
          required = true) @RequestHeader(value = "userId", required = true) String userId)
      throws NoteNotFoundException, AccessingOtherUsersNotesException,
      CantUseInitVectorTwiceException;

}
