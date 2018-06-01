package com.flockinger.poppynotes.notesService.model;

import javax.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="allowedUser")
public class AllowedUser {
  
  @Id
  private String id;
  
  @Indexed
  @NotNull
  private String allowedUserHash;

  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAllowedUserHash() {
    return allowedUserHash;
  }

  public void setAllowedUserHash(String allowedUserHash) {
    this.allowedUserHash = allowedUserHash;
  }
}
