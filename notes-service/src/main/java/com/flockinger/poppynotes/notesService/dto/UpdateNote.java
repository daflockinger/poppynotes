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
 * UpdateNote
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-10T08:59:41.039Z")

public class UpdateNote   {
  
  @NotEmpty
  @JsonProperty("id")
  private String id = null;

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

  @JsonProperty("pinned")
  private Boolean pinned = null;
  
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

  public UpdateNote initVector(String initVector) {
    this.initVector = initVector;
    return this;
  }

  public UpdateNote id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Unique Identifier of the Note.
   * @return id
  **/
  @ApiModelProperty(value = "Unique Identifier of the Note.")


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public UpdateNote title(String title) {
    this.title = title;
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

  public UpdateNote content(String content) {
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

  public UpdateNote userHash(String userHash) {
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

  public UpdateNote lastEdit(Date lastEdit) {
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

  public UpdateNote pinned(Boolean pinned) {
    this.pinned = pinned;
    return this;
  }

  /**
   * Pin for important messages to be shown always on top.
   * @return pinned
  **/
  @ApiModelProperty(value = "Pin for important messages to be shown always on top.")


  public Boolean isPinned() {
    return pinned;
  }

  public void setPinned(Boolean pinned) {
    this.pinned = pinned;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateNote updateNote = (UpdateNote) o;
    return Objects.equals(this.id, updateNote.id) &&
        Objects.equals(this.initVector, updateNote.initVector) &&
        Objects.equals(this.title, updateNote.title) &&
        Objects.equals(this.content, updateNote.content) &&
        Objects.equals(this.userHash, updateNote.userHash) &&
        Objects.equals(this.lastEdit, updateNote.lastEdit) &&
        Objects.equals(this.pinned, updateNote.pinned);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, content, userHash, lastEdit, pinned, initVector);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateNote {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    initVector: ").append(toIndentedString(initVector)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    userHash: ").append(toIndentedString(userHash)).append("\n");
    sb.append("    lastEdit: ").append(toIndentedString(lastEdit)).append("\n");
    sb.append("    pinned: ").append(toIndentedString(pinned)).append("\n");
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

