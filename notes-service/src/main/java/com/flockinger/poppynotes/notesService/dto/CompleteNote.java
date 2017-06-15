package com.flockinger.poppynotes.notesService.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * CompleteNote
 */

public class CompleteNote {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("title")
	private String title = null;

	@JsonProperty("content")
	private String content = null;

	@JsonProperty("userId")
	private Long userId = null;

	@JsonProperty("lastEdit")
	private Date lastEdit = null;

	@JsonProperty("archived")
	private Boolean archived = null;

	@JsonProperty("initVector")
	private String initVector = null;

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

	/**
	 * Initialization Vector (iv) used for message en/decryption
	 * 
	 * @return initVector
	 **/
	@ApiModelProperty(value = "Initialization Vector (iv) used for message en/decryption")
	public String getInitVector() {
		return initVector;
	}

	public void setInitVector(String initVector) {
		this.initVector = initVector;
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
		return Objects.equals(this.id, completeNote.id) && Objects.equals(this.title, completeNote.title)
				&& Objects.equals(this.content, completeNote.content)
				&& Objects.equals(this.userId, completeNote.userId)
				&& Objects.equals(this.lastEdit, completeNote.lastEdit)
				&& Objects.equals(this.archived, completeNote.archived)
				&& Objects.equals(this.initVector, completeNote.initVector);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, content, userId, lastEdit, archived, initVector);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CompleteNote {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    title: ").append(toIndentedString(title)).append("\n");
		sb.append("    content: ").append(toIndentedString(content)).append("\n");
		sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
		sb.append("    lastEdit: ").append(toIndentedString(lastEdit)).append("\n");
		sb.append("    archived: ").append(toIndentedString(archived)).append("\n");
		sb.append("    initVector: ").append(toIndentedString(initVector)).append("\n");
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
