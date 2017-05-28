package com.flockinger.poppynotes.notesService.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.flockinger.poppynotes.notesService.model.ArchivedNote;

public class ArchivedNoteRepositoryImpl extends AbstractNoteRepository<ArchivedNote> implements ArchivedNoteRepositoryCustom {
	@Override
	public List<ArchivedNote> findByUserIdSorted(Long userId, Pageable pageable) {
		return super.findByUserIdSortedFromAnyNote(userId, pageable, ArchivedNote.class);
	}
}
