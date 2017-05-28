package com.flockinger.poppynotes.notesService.dto;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * UpdateNote
 */

public class UpdateNote {

	@NotNull
	@JsonProperty("id")
	private String id = null;

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

	@NotNull
	@JsonProperty("archived")
	private Boolean archived = null;

	/**
	 * Unique Identifier of the Note.
	 * 
	 * @return id
	 **/
	@ApiModelProperty(value = "Unique Identifier of the Note.")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	/**
	 * Determines if notes is archived and not shown in regular listing (default
	 * false).
	 * 
	 * @return archived
	 **/
	@ApiModelProperty(value = "Determines if notes is archived and not shown in regular listing (default false).")
	public Boolean getArchived() {
		return archived;
	}

	public void setArchived(Boolean archived) {
		this.archived = archived;
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
		return Objects.equals(this.id, updateNote.id) && Objects.equals(this.title, updateNote.title)
				&& Objects.equals(this.content, updateNote.content) && Objects.equals(this.userId, updateNote.userId)
				&& Objects.equals(this.lastEdit, updateNote.lastEdit) && Objects.equals(this.pinned, updateNote.pinned)
				&& Objects.equals(this.archived, updateNote.archived);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, content, userId, lastEdit, pinned, archived);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class UpdateNote {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    title: ").append(toIndentedString(title)).append("\n");
		sb.append("    content: ").append(toIndentedString(content)).append("\n");
		sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
		sb.append("    lastEdit: ").append(toIndentedString(lastEdit)).append("\n");
		sb.append("    pinned: ").append(toIndentedString(pinned)).append("\n");
		sb.append("    archived: ").append(toIndentedString(archived)).append("\n");
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
