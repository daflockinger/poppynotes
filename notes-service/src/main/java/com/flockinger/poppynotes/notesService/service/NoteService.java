package com.flockinger.poppynotes.notesService.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.flockinger.poppynotes.notesService.dto.CompleteNote;
import com.flockinger.poppynotes.notesService.dto.CreateNote;
import com.flockinger.poppynotes.notesService.dto.OverviewNote;
import com.flockinger.poppynotes.notesService.dto.UpdateNote;
import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;

public interface NoteService {
	CompleteNote findNote(String id, Long userId) throws NoteNotFoundException, AccessingOtherUsersNotesException;
	void create(CreateNote note);
	void update(UpdateNote note) throws NoteNotFoundException, AccessingOtherUsersNotesException;
	void delete(String id, Long userId) throws NoteNotFoundException, AccessingOtherUsersNotesException;
	List<OverviewNote> findNotesByUserIdPaginated(Long userId, Pageable page);
	List<OverviewNote> findArchivedNotesByUserIdPaginated(Long userId, Pageable page);
}
