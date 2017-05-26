package com.flockinger.poppynotes.notesService.dao;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.flockinger.poppynotes.notesService.model.ArchivedNote;

public interface ArchivedNoteRepository extends CassandraRepository<ArchivedNote>{
	ArchivedNote findByIdAndUserId(Long id, Long userId);
	ArchivedNote findByIdAndUserIdAndPinned(Long id, Long userId, Boolean pinned);
	List<ArchivedNote> findByUserId(Long userId);
	List<ArchivedNote> findByUserIdAndPinned(Long userId, Boolean pinned);
}
