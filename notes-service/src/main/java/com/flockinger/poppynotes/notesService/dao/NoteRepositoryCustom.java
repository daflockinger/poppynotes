package com.flockinger.poppynotes.notesService.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import com.flockinger.poppynotes.notesService.model.Note;

public interface NoteRepositoryCustom {
	List<Note> findByUserIdSorted(Long userId, Pageable pageable);
}
