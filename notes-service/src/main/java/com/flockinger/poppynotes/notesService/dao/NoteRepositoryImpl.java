package com.flockinger.poppynotes.notesService.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.flockinger.poppynotes.notesService.model.Note;

public class NoteRepositoryImpl extends AbstractNoteRepository<Note> implements NoteRepositoryCustom{

	@Override
	public List<Note> findByUserIdSorted(Long userId, Pageable pageable) {
		return super.findByUserIdSortedFromAnyNote(userId, pageable, Note.class);
	}
}
