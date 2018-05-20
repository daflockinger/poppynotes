package com.flockinger.poppynotes.notesService.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import com.flockinger.poppynotes.notesService.dto.CompleteNote;
import com.flockinger.poppynotes.notesService.dto.CreateNote;
import com.flockinger.poppynotes.notesService.dto.OverviewNote;
import com.flockinger.poppynotes.notesService.dto.PinNote;
import com.flockinger.poppynotes.notesService.dto.UpdateNote;
import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.CantUseInitVectorTwiceException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;

public interface NoteService {
  CompleteNote findNote(String id, String userHash)
      throws NoteNotFoundException, AccessingOtherUsersNotesException;

  CompleteNote create(CreateNote note) throws CantUseInitVectorTwiceException;

  void update(UpdateNote note) throws NoteNotFoundException, AccessingOtherUsersNotesException,
      CantUseInitVectorTwiceException;

  void delete(String id, String userHash)
      throws NoteNotFoundException, AccessingOtherUsersNotesException;

  List<OverviewNote> findNotesByUserHashPaginated(String userHash, Pageable page);
  
  void pinNote(PinNote pinNote, String userHash) throws NoteNotFoundException, AccessingOtherUsersNotesException;
}
