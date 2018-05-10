package com.flockinger.poppynotes.notesService.dto;

import java.util.Date;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

/**
 * OverviewNote
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-10T08:59:41.039Z")

public class OverviewNote   {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("title")
  private String title = null;

  @JsonProperty("content")
  private String content = null;

  @JsonProperty("lastEdit")
  private Date lastEdit = null;

  @JsonProperty("pinned")
  private Boolean pinned = null;

  public OverviewNote id(String id) {
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

  public OverviewNote title(String title) {
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

  public OverviewNote content(String content) {
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

  public OverviewNote lastEdit(Date lastEdit) {
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

  public OverviewNote pinned(Boolean pinned) {
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
    OverviewNote overviewNote = (OverviewNote) o;
    return Objects.equals(this.id, overviewNote.id) &&
        Objects.equals(this.title, overviewNote.title) &&
        Objects.equals(this.content, overviewNote.content) &&
        Objects.equals(this.lastEdit, overviewNote.lastEdit) &&
        Objects.equals(this.pinned, overviewNote.pinned);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, content, lastEdit, pinned);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OverviewNote {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
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

