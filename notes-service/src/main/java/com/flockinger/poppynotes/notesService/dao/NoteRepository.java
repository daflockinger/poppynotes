package com.flockinger.poppynotes.notesService.dao;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.flockinger.poppynotes.notesService.model.Note;

public interface NoteRepository extends CassandraRepository<Note> {
	Note findByIdAndUserId(Long id, Long userId);
	Note findByIdAndUserIdAndPinned(Long id, Long userId, Boolean pinned);
	List<Note> findByUserId(Long userId);
	List<Note> findByUserIdAndPinned(Long userId, Boolean pinned);
}
