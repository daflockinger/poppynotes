package com.flockinger.poppynotes.notesService.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.flockinger.poppynotes.notesService.model.ArchivedNote;

public interface ArchivedNoteRepositoryCustom {
	/**
	 * Sorts first by pinned (true first) and secondary by lastEdit date descending
	 * (new date first) and returns all entries of a user, limited by page-size, 
	 * and started by the page-offset.
	 * 
	 * @param userId
	 * @param noteClass
	 * @param pageable
	 * @return
	 */
	List<ArchivedNote> findByUserIdSorted(Long userId, Pageable pageable);
}
