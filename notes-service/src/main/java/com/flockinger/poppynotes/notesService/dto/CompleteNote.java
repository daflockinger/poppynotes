package com.flockinger.poppynotes.notesService.dto;

import java.util.Date;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * CompleteNote
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-10T08:59:41.039Z")

public class CompleteNote   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("content")
  private String content = null;

  @JsonProperty("userHash")
  private String userHash = null;

  @JsonProperty("lastEdit")
  private Date lastEdit = null;

  public CompleteNote id(String id) {
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

  public CompleteNote title(String title) {
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

  public CompleteNote content(String content) {
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

  public CompleteNote userHash(String userHash) {
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

  public CompleteNote lastEdit(Date lastEdit) {
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
    CompleteNote completeNote = (CompleteNote) o;
    return Objects.equals(this.id, completeNote.id) &&
        Objects.equals(this.title, completeNote.title) &&
        Objects.equals(this.content, completeNote.content) &&
        Objects.equals(this.userHash, completeNote.userHash) &&
        Objects.equals(this.lastEdit, completeNote.lastEdit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, content, userHash, lastEdit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompleteNote {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
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

