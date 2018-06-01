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
import java.io.IOException;
import java.util.Date;
import org.junit.Before;
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
import com.flockinger.poppynotes.notesService.dao.UserWhitelistRepository;
import com.flockinger.poppynotes.notesService.dto.CompleteNote;
import com.flockinger.poppynotes.notesService.dto.CreateNote;
import com.flockinger.poppynotes.notesService.dto.PinNote;
import com.flockinger.poppynotes.notesService.dto.UpdateNote;
import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.CantUseInitVectorTwiceException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;
import com.flockinger.poppynotes.notesService.service.NoteService;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import({WebConfig.class, SecurityConfig.class, 
  RateLimitInterceptor.class, WhitelistUserInterceptor.class, 
  UserWhitelistRepository.class})
public class NotesApiTest extends BaseControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private NoteService service;
  
  @MockBean
  private RateLimitInterceptor mockCopter;
  @MockBean
  private UserWhitelistRepository whitelistMockRepository;

  @Before
  public void setup() throws Exception {
    when(mockCopter.preHandle(any(), any(), any())).thenReturn(true);
    when(whitelistMockRepository
        .existsByAllowedUserHash(anyString())).thenReturn(true);
  }
  
  @Test
  public void testGetNote_withCorrectIdAndUser_shouldReturnCorrect() throws Exception {
    CompleteNote note = new CompleteNote();
    note.setContent("fake something");
    when(service.findNote(anyString(), anyString())).thenReturn(note);

    mockMvc.perform(get("/api/v1/notes/2").contentType(jsonContentType).header("userId", "1234"))
        .andExpect(status().isOk()).andExpect(jsonPath("$.content", is("fake something")));

    verify(service).findNote(matches("2"), matches("1234"));
  }

  @Test
  public void testGetNote_withNotExistingIdAndUser_shouldReturNotFound() throws Exception {
    when(service.findNote(anyString(), anyString())).thenThrow(NoteNotFoundException.class);

    mockMvc
        .perform(
            get("/api/v1/notes/999").contentType(jsonContentType).header("userId", "nONExistante"))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testGetNote_withIdNotBelongingToUser_shouldReturConflict() throws Exception {
    when(service.findNote(anyString(), anyString()))
        .thenThrow(AccessingOtherUsersNotesException.class);

    mockMvc
        .perform(
            get("/api/v1/notes/999").contentType(jsonContentType).header("userId", "nONExistante"))
        .andExpect(status().isForbidden());
  }


  @Test
  public void testDeleteNote_withExistingIdAndUser_shouldReturnCorrect() throws Exception {
    doNothing().when(service).delete(anyString(), anyString());

    mockMvc.perform(delete("/api/v1/notes/1").contentType(jsonContentType).header("userId", "1234")
        ).andExpect(status().isOk());

    verify(service).delete(matches("1"), matches("1234"));
  }


  @Test
  public void testDeleteNote_withNotExistingIdAndUser_shouldReturnCorrect() throws Exception {
    doThrow(NoteNotFoundException.class).when(service).delete(anyString(), anyString());

    mockMvc.perform(delete("/api/v1/notes/999").contentType(jsonContentType)
        .header("userId", "nONExistante")).andExpect(status().isNotFound());
  }

  @Test
  public void testDeleteNote_withIdNotBelongingToUser_shouldReturnForbidden() throws Exception {
    doThrow(AccessingOtherUsersNotesException.class).when(service).delete(anyString(), anyString());

    mockMvc.perform(
        delete("/api/v1/notes/999").contentType(jsonContentType).header("userId", "nONExistante"))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testCreateNote_withValidFreshNote_shouldCreate() throws Exception {
    CreateNote note = new CreateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);

    mockMvc
        .perform(
            post("/api/v1/notes").contentType(jsonContentType).content(json(note)).header("userId", "1234"))
        .andExpect(jsonPath("$.id", is("2"))).andExpect(status().isCreated());
  }

  @Test
  public void testCreateNote_withExistingInitVector_shouldReturnForbidden() throws Exception {
    CreateNote note = new CreateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("1");
    note.setInitVector("4321");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenThrow(CantUseInitVectorTwiceException.class);

    mockMvc
        .perform(
            post("/api/v1/notes").contentType(jsonContentType).content(json(note)).header("userId", "1234"))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testCreateNote_withNoteMissingUserHash_shouldReturnUnauthorized() throws Exception {
    CreateNote note = new CreateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);

    mockMvc
        .perform(
            post("/api/v1/notes").contentType(jsonContentType).content(json(note)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testCreateNote_withNoteMissingTitle_shouldReturnBadRequest() throws Exception {
    CreateNote note = new CreateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setUserHash("12345");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);

    mockMvc
        .perform(
            post("/api/v1/notes").contentType(jsonContentType)
            .content(json(note)).header("userId", "1234"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateNote_withNoteMissingLastEdit_shouldReturnBadRequest() throws Exception {
    CreateNote note = new CreateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);

    mockMvc
        .perform(
            post("/api/v1/notes").contentType(jsonContentType)
            .content(json(note)).header("userId", "1234"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateNote_withNoteMissingContent_shouldReturnBadRequest() throws Exception {
    CreateNote note = new CreateNote();
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);

    mockMvc
        .perform(
            post("/api/v1/notes").contentType(jsonContentType)
            .content(json(note)).header("userId", "1234"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testCreateNote_withNoteMissingInitVector_shouldReturnBadRequest() throws Exception {
    CreateNote note = new CreateNote();
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setContent("bla blu");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);

    mockMvc
        .perform(
            post("/api/v1/notes").contentType(jsonContentType)
            .content(json(note)).header("userId", "1234"))
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
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    when(service.create(any())).thenReturn(complete);

    mockMvc
        .perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).header("userId", "1234"))
        .andExpect(status().isOk());
  }

  @Test
  public void testUpdateNote_withNoteMissingUserHash_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setId("123");
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());

    mockMvc
        .perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testUpdateNote_withNoteMissingTitle_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setUserHash("12345");
    note.setId("123");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());

    mockMvc
        .perform(put("/api/v1/notes").contentType(jsonContentType)
            .content(json(note)).header("userId", "1234"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateNote_withNoteMissingLastEdit_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setId("123");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());

    mockMvc
        .perform(put("/api/v1/notes").contentType(jsonContentType)
            .content(json(note)).header("userId", "1234"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateNote_withNoteMissingContent_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setId("123");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());

    mockMvc
        .perform(put("/api/v1/notes").contentType(jsonContentType)
            .content(json(note)).header("userId", "1234"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateNote_withNoteMissingId_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setContent("Small batch Distillery");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());

    mockMvc
        .perform(put("/api/v1/notes").contentType(jsonContentType)
            .content(json(note)).header("userId", "1234"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateNote_withNoteMissingInitVector_shouldReturnBadRequest() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("12345");
    note.setContent("Small batch Distillery");
    note.setId("1");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doNothing().when(service).update(any());

    mockMvc
        .perform(put("/api/v1/notes").contentType(jsonContentType)
            .content(json(note)).header("userId", "1234"))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateNote_withNoteOfWrongUserNotOwningNote_shouldReturnForbidden()
      throws Exception {
    UpdateNote note = new UpdateNote();
    note.setId("123");
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("HACKER");
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doThrow(AccessingOtherUsersNotesException.class).when(service).update(any());

    mockMvc.perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).header("userId", "1234"))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testUpdateNote_withExistingInitVector_shouldReturnForbidden() throws Exception {
    UpdateNote note = new UpdateNote();
    note.setId("123");
    note.setContent("Flexitarian offal locavore unicorn hammock banh mi single-origin coffee ");
    note.setLastEdit(new Date());
    note.setTitle("DIY. Distillery banjo");
    note.setUserHash("1");
    note.setInitVector("4321");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doThrow(CantUseInitVectorTwiceException.class).when(service).update(any());
    
    mockMvc
        .perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).header("userId", "1234"))
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
    note.setInitVector("87268473");

    CompleteNote complete = new CompleteNote();
    complete.setId("2");
    doThrow(NoteNotFoundException.class).when(service).update(any());

    mockMvc
        .perform(put("/api/v1/notes").contentType(jsonContentType).content(json(note)).header("userId", "1234"))
        .andExpect(status().isNotFound());
  }
    
  @Test
  public void testPinNote_withCorrectRequest_shouldPinIt() throws Exception {
    PinNote pinNote = new PinNote();
    pinNote.setPinIt(true);
    pinNote.setNoteId("2");
    
    mockMvc
    .perform(put("/api/v1/notes/pin").contentType(jsonContentType).content(json(pinNote))
        .header("userId", "1234"))
      .andExpect(status().isOk());
    
    verify(service).pinNote(any(), matches("1234"));
  }
  
  @Test
  public void testPinNote_withNotExistingNote_shouldReturnNotFound() throws Exception {
    PinNote pinNote = new PinNote();
    pinNote.setPinIt(true);
    pinNote.setNoteId("7657652");
    
    doThrow(NoteNotFoundException.class).when(service).pinNote(any(), anyString());
    
    mockMvc
    .perform(put("/api/v1/notes/pin").contentType(jsonContentType).content(json(pinNote))
        .header("userId", "1234"))
      .andExpect(status().isNotFound());
  }
  
  @Test
  public void testPinNote_withAccessingOtherUsersNote_shouldReturnForbidden() throws Exception {
    PinNote pinNote = new PinNote();
    pinNote.setPinIt(true);
    pinNote.setNoteId("2");
    
    doThrow(AccessingOtherUsersNotesException.class).when(service).pinNote(any(), anyString());
    
    mockMvc
    .perform(put("/api/v1/notes/pin").contentType(jsonContentType).content(json(pinNote))
        .header("userId", "hacker"))
      .andExpect(status().isForbidden());
  }
  
  @Test
  public void testPinNote_withMissingPinBoolean_shouldRetunBadRequest() throws Exception {
    PinNote pinNote = new PinNote();
    pinNote.setPinIt(null);
    pinNote.setNoteId("2");
    
    doThrow(AccessingOtherUsersNotesException.class).when(service).pinNote(any(), anyString());
    
    mockMvc
    .perform(put("/api/v1/notes/pin").contentType(jsonContentType).content(json(pinNote))
        .header("userId", "1234"))
      .andExpect(status().isBadRequest());
  }
  
  @Test
  public void testPinNote_withMissingNoteId_shouldRetunBadRequest() throws Exception {
    PinNote pinNote = new PinNote();
    pinNote.setPinIt(true);
    pinNote.setNoteId(null);
    
    doThrow(AccessingOtherUsersNotesException.class).when(service).pinNote(any(), anyString());
    
    mockMvc
    .perform(put("/api/v1/notes/pin").contentType(jsonContentType).content(json(pinNote))
        .header("userId", "1234"))
      .andExpect(status().isBadRequest());
  }
}
