package com.flockinger.poppynotes.notesService.dto;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * CreateNote
 */

public class CreateNote {
	
	@NotNull
	@JsonProperty("title")
	private String title = null;

	@JsonProperty("content")
	private String content = null;

	@NotNull
	@JsonProperty("userId")
	private Long userId = null;

	@NotNull
	@JsonProperty("lastEdit")
	private Date lastEdit = null;

	@NotNull
	@JsonProperty("pinned")
	private Boolean pinned = null;

	public CreateNote title(String title) {
		this.title = title;
		return this;
	}

	/**
	 * Title of the Note.
	 * 
	 * @return title
	 **/
	@ApiModelProperty(value = "Title of the Note.")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Text content of the Note (the actual note).
	 * 
	 * @return content
	 **/
	@ApiModelProperty(value = "Text content of the Note (the actual note).")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Unique Identifier of the user that created that note.
	 * 
	 * @return userId
	 **/
	@ApiModelProperty(value = "Unique Identifier of the user that created that note.")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Date the user last edited the note.
	 * 
	 * @return lastEdit
	 **/
	@ApiModelProperty(value = "Date the user last edited the note.")
	public Date getLastEdit() {
		return lastEdit;
	}

	public void setLastEdit(Date lastEdit) {
		this.lastEdit = lastEdit;
	}

	/**
	 * Pin for important messages to be shown always on top.
	 * 
	 * @return pinned
	 **/
	@ApiModelProperty(value = "Pin for important messages to be shown always on top.")
	public Boolean getPinned() {
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
		CreateNote createNote = (CreateNote) o;
		return Objects.equals(this.title, createNote.title) && Objects.equals(this.content, createNote.content)
				&& Objects.equals(this.userId, createNote.userId) && Objects.equals(this.lastEdit, createNote.lastEdit)
				&& Objects.equals(this.pinned, createNote.pinned);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, content, userId, lastEdit, pinned);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CreateNote {\n");

		sb.append("    title: ").append(toIndentedString(title)).append("\n");
		sb.append("    content: ").append(toIndentedString(content)).append("\n");
		sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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
