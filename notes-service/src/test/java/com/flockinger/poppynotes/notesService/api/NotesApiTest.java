package com.flockinger.poppynotes.notesService.api;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.flockinger.poppynotes.notesService.config.SecurityConfig;
import com.flockinger.poppynotes.notesService.config.WebConfig;
import com.flockinger.poppynotes.notesService.dto.CompleteNote;
import com.flockinger.poppynotes.notesService.dto.CreateNote;
import com.flockinger.poppynotes.notesService.dto.UpdateNote;
import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;
import com.flockinger.poppynotes.notesService.service.NoteService;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import({WebConfig.class, SecurityConfig.class})
public class NotesApiTest extends BaseControllerTest {

  @Autowired
  private MockMvc mockMvc;
  
  @MockBean
  private NoteService service;
  
  @Test
  public void testGetNote_withCorrectIdAndUser_shouldReturnCorrect() throws Exception {
    CompleteNote note = new CompleteNote();
    note.setContent("fake something");
    when(service.findNote(anyString(), anyString())).thenReturn(note);
    
    mockMvc.perform(get("/api/v1/notes/2").contentType(jsonContentType).header("userId", "1234"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", is("fake something")));
    
    verify(service).findNote(matches("2"), matches("1234"));
  }
  
  @Test
  public void testGetNote_withNotExistingIdAndUser_shouldReturNotFound() throws Exception {
    when(service.findNote(anyString(), anyString())).thenThrow(NoteNotFoundException.class);
    
    mockMvc.perform(get("/api/v1/notes/999").contentType(jsonContentType).header("userId", "nONExistante"))
        .andExpect(status().isNotFound());
  }
  
  @Test
  public void testGetNote_withIdNotBelongingToUser_shouldReturConflict() throws Exception {
    when(service.findNote(anyString(), anyString())).thenThrow(AccessingOtherUsersNotesException.class);
    
    mockMvc.perform(get("/api/v1/notes/999").contentType(jsonContentType).header("userId", "nONExistante"))
        .andExpect(status().isForbidden());
  }
  
  
  @Test
  public void testDeleteNote_withExistingIdAndUser_shouldReturnCorrect() throws Exception {
    doNothing().when(service).delete(anyString(), anyString());
    
    mockMvc.perform(delete("/api/v1/notes/1").contentType(jsonContentType).header("userId", "1234").with(csrf()))
        .andExpect(status().isOk());
    
    verify(service).delete(matches("1"), matches("1234"));
  }
  
  
  @Test
  public void testDeleteNote_withNotExistingIdAndUser_shouldReturnCorrect() throws Exception {
    doThrow(NoteNotFoundException.class).when(service).delete(anyString(), anyString());
    
    mockMvc.perform(delete("/api/v1/notes/999").contentType(jsonContentType).header("userId", "nONExistante").with(csrf()))
        .andExpect(status().isNotFound());
  }
  
  @Test
  public void testDeleteNote_withIdNotBelongingToUser_shouldReturnForbidden() throws Exception {
    doThrow(AccessingOtherUsersNotesException.class).when(service).delete(anyString(), anyString());
    
    mockMvc.perform(delete("/api/v1/notes/999").contentType(jsonContentType).header("userId", "nONExistante"))
        .andExpect(status().isForbidden());
  }
  
  @Test
  public void testCreateNote_withValidFreshNote_shouldCreate() throws Exception {
    CreateNote note = new CreateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);
    
    mockMvc.perform(post("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(jsonPath("$.id", is("2")))
        .andExpect(status().isCreated());
  }
  
  @Test
  public void testCreateNote_withNoteMissingUserHash_shouldReturnBadRequest() throws Exception {
    CreateNote note = new CreateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);
    
    mockMvc.perform(post("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testCreateNote_withNoteMissingTitle_shouldReturnBadRequest() throws Exception {
    CreateNote note = new CreateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setUserHash("12345");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);
    
    mockMvc.perform(post("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testCreateNote_withNoteMissingLastEdit_shouldReturnBadRequest() throws Exception {
    CreateNote note = new CreateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);
    
    mockMvc.perform(post("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testCreateNote_withNoteMissingContent_shouldReturnBadRequest() throws Exception {
    CreateNote note = new CreateNote();
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);
    
    mockMvc.perform(post("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isBadRequest());
  }
  
  
  
  
  
  
  
  
  
  
  
  @Test
  public void testUpdateNote_withValidFreshNote_shouldUpdate() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setId("123");
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);
    
    mockMvc.perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isOk());
  }
  
  @Test
  public void testUpdateNote_withNoteMissingUserHash_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setId("123");
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());
    
    mockMvc.perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testUpdateNote_withNoteMissingTitle_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setUserHash("12345");
    note.setId("123");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());
    
    mockMvc.perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testUpdateNote_withNoteMissingLastEdit_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setId("123");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());
    
    mockMvc.perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testUpdateNote_withNoteMissingContent_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setId("123");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());
    
    mockMvc.perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testUpdateNote_withNoteMissingId_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setContent("Small batch Distillery");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());
    
    mockMvc.perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testUpdateNote_withNoteOfWrongUserNotOwningNote_shouldReturnForbidden() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setId("123");
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("HACKER");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doThrow(AccessingOtherUsersNotesException.class).when(service).update(any());
    
    mockMvc.perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)))
        .andExpect(status().isForbidden());
  }
  
  
  @Test
  public void testUpdateNote_withNoteNotExisting_shouldReturnNotFound() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setId("99999");
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("NONEXISTANTE");
    
    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doThrow(NoteNotFoundException.class).when(service).update(any());
    
    mockMvc.perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).with(csrf()))
        .andExpect(status().isNotFound());
  }
  /*
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("content")
  private String content = null;

  @JsonProperty("userHash")
  private String userHash = null;

  @JsonProperty("lastEdit")
  private Date lastEdit = null;
   * */
  
  /*
   @ApiOperation(value = "Create Note", nickname = "createNote", notes = "Creates new Note entry.",
      tags = {"Notes",})
 
  @RequestMapping(value = "/api/v1/notes", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.POST)
  ResponseEntity<CompleteNote> createNote(
      @ApiParam(value = "", required = true) @Valid @RequestBody CreateNote noteCreate);



  @ApiOperation(value = "Get All Notes from user", nickname = "getNotes",
      notes = "Returns all Notes in a shorter form from a user, paginated.",
      response = OverviewNote.class, responseContainer = "List", tags = {"Notes",})
  
  @RequestMapping(value = "/api/v1/notes", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.GET)
  ResponseEntity<List<OverviewNote>> getNotes(
      @ApiParam(value = "Unique Identifier of the User requesting his notes.",
          required = true) @RequestHeader(value = "userId", required = true) String userId,
      @ApiParam(value = "Page of notes that's beeing returned.",
          defaultValue = "0") @Valid @RequestParam(value = "page", required = false,
              defaultValue = "0") Integer page,
      @ApiParam(value = "Amount of notes per page.", defaultValue = "10") @Valid @RequestParam(
          value = "items", required = false, defaultValue = "10") Integer items)
      throws DtoValidationFailedException;


  @ApiOperation(value = "Delete Note", nickname = "removeNote",
      notes = "Deletes a Note with defined Id.", tags = {"Notes",})
  
  @RequestMapping(value = "/api/v1/notes/{noteId}", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.DELETE)
  ResponseEntity<Void> removeNote(
      @ApiParam(value = "Unique identifier of a Note.",
          required = true) @PathVariable("noteId") String noteId,
      @ApiParam(value = "Unique Identifier of the User requesting his notes.",
          required = true) @RequestHeader(value = "userId", required = true) String userId)
      throws NoteNotFoundException, AccessingOtherUsersNotesException;


  @ApiOperation(value = "Update Note", nickname = "updateNote", notes = "Updated a Note entry.",
      tags = {"Notes",})
  
  @RequestMapping(value = "/api/v1/notes", produces = {"application/json"},
      consumes = {"application/json"}, method = RequestMethod.PUT)
  ResponseEntity<Void> updateNote(
      @ApiParam(value = "", required = true) @Valid @RequestBody UpdateNote noteUpdate)
      throws NoteNotFoundException, AccessingOtherUsersNotesException;

   * */
  
}
