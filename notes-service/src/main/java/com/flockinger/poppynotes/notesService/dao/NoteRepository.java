package com.flockinger.poppynotes.notesService.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.flockinger.poppynotes.notesService.model.Note;

public interface NoteRepository extends MongoRepository<Note,String>, NoteRepositoryCustom {
}
