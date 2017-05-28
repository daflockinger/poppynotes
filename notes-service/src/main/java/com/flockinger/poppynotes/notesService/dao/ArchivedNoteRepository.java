package com.flockinger.poppynotes.notesService.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flockinger.poppynotes.notesService.model.ArchivedNote;

public interface ArchivedNoteRepository extends MongoRepository<ArchivedNote,String>, ArchivedNoteRepositoryCustom {
}
