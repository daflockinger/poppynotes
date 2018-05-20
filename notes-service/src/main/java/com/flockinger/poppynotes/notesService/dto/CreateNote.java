package com.flockinger.poppynotes.notesService.dto;

import java.util.Date;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * CreateNote
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-10T08:59:41.039Z")

public class CreateNote   {
  @NotEmpty
  @JsonProperty("title")
  private String title = null;
  
  @NotEmpty
  @JsonProperty("content")
  private String content = null;

  @NotEmpty
  @JsonProperty("userHash")
  private String userHash = null;

  @NotNull
  @JsonProperty("lastEdit")
  private Date lastEdit = null;

  public CreateNote title(String title) {
    this.title = title;
    return this;
  }

  @NotEmpty
  @JsonProperty("initVector")
  private String initVector = null;
  
  

  /**
   * Unique Initialization Vector of the Note.
   * @return initVector
  **/
  @ApiModelProperty(value = "Unique Initialization Vector of the Note for encryption.")


  public String getInitVector() {
    return initVector;
  }

  public void setInitVector(String initVector) {
    this.initVector = initVector;
  }

  public CreateNote initVector(String initVector) {
    this.initVector = initVector;
    return this;
  }
  
  /**
   * Title of the Note.
   * @return title
  **/
  @ApiModelProperty(value = "Title of the Note.")


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public CreateNote content(String content) {
    this.content = content;
    return this;
  }

  /**
   * Text content of the Note (the actual note).
   * @return content
  **/
  @ApiModelProperty(value = "Text content of the Note (the actual note).")


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public CreateNote userHash(String userHash) {
    this.userHash = userHash;
    return this;
  }

  /**
   * Unique Identifier of the user that created that note.
   * @return userHash
  **/
  @ApiModelProperty(value = "Unique Identifier of the user that created that note.")


  public String getUserHash() {
    return userHash;
  }

  public void setUserHash(String userHash) {
    this.userHash = userHash;
  }

  public CreateNote lastEdit(Date lastEdit) {
    this.lastEdit = lastEdit;
    return this;
  }

  /**
   * Date the user last edited the note.
   * @return lastEdit
  **/
  @ApiModelProperty(value = "Date the user last edited the note.")

  @Valid

  public Date getLastEdit() {
    return lastEdit;
  }

  public void setLastEdit(Date lastEdit) {
    this.lastEdit = lastEdit;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateNote createNote = (CreateNote) o;
    return Objects.equals(this.title, createNote.title) &&
        Objects.equals(this.initVector, createNote.initVector) &&
        Objects.equals(this.content, createNote.content) &&
        Objects.equals(this.userHash, createNote.userHash) &&
        Objects.equals(this.lastEdit, createNote.lastEdit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, content, userHash, lastEdit, initVector);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateNote {\n");
    
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    initVector: ").append(toIndentedString(initVector)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    userHash: ").append(toIndentedString(userHash)).append("\n");
    sb.append("    lastEdit: ").append(toIndentedString(lastEdit)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

