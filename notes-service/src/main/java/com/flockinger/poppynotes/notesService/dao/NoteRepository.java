package com.flockinger.poppynotes.notesService.dao;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.flockinger.poppynotes.notesService.model.Note;

public interface NoteRepository extends MongoRepository<Note,String> {
  List<Note> findByUserHash(String userHash, Pageable page);
  boolean existsByUserHashAndInitVector(String userHash, String initVector);
  Long countByUserHash(String userHash);
}
