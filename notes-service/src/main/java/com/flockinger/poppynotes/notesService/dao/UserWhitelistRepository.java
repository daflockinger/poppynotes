package com.flockinger.poppynotes.notesService.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.flockinger.poppynotes.notesService.model.AllowedUser;

public interface UserWhitelistRepository extends MongoRepository<AllowedUser, String>{
  boolean existsByAllowedUserHash(String allowedUserHash);
}
