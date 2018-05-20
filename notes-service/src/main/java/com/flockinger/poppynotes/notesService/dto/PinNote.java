package com.flockinger.poppynotes.notesService.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * PinNote
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-05-19T22:33:34.800Z")

public class PinNote   {
  
  @NotNull
  @JsonProperty("pinIt")
  private Boolean pinIt = null;

  @NotNull
  @JsonProperty("noteId")
  private String noteId = null;

  public PinNote pinIt(Boolean pinIt) {
    this.pinIt = pinIt;
    return this;
  }

  /**
   * Boolean if the note should be set pinned or not
   * @return pinIt
  **/
  @ApiModelProperty(value = "Boolean if the note should be set pinned or not")


  public Boolean isPinIt() {
    return pinIt;
  }

  public void setPinIt(Boolean pinIt) {
    this.pinIt = pinIt;
  }

  public PinNote noteId(String noteId) {
    this.noteId = noteId;
    return this;
  }

  /**
   * Unique Identifier of the Note.
   * @return noteId
  **/
  @ApiModelProperty(value = "Unique Identifier of the Note.")


  public String getNoteId() {
    return noteId;
  }

  public void setNoteId(String noteId) {
    this.noteId = noteId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PinNote pinNote = (PinNote) o;
    return Objects.equals(this.pinIt, pinNote.pinIt) &&
        Objects.equals(this.noteId, pinNote.noteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pinIt, noteId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PinNote {\n");
    
    sb.append("    pinIt: ").append(toIndentedString(pinIt)).append("\n");
    sb.append("    noteId: ").append(toIndentedString(noteId)).append("\n");
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

