package com.flockinger.poppynotes.notesService.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * OverviewNote
 */

public class OverviewNote {
	@JsonProperty("id")
	private String id = null;

	@JsonProperty("title")
	private String title = null;

	@JsonProperty("partContent")
	private String partContent = null;

	@JsonProperty("lastEdit")
	private Date lastEdit = null;

	@JsonProperty("pinned")
	private Boolean pinned = null;

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
	 * Only the first lines of the text content.
	 * 
	 * @return partContent
	 **/
	@ApiModelProperty(value = "Only the first lines of the text content.")
	public String getPartContent() {
		return partContent;
	}

	public void setPartContent(String partContent) {
		this.partContent = partContent;
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
		OverviewNote overviewNote = (OverviewNote) o;
		return Objects.equals(this.id, overviewNote.id) && Objects.equals(this.title, overviewNote.title)
				&& Objects.equals(this.partContent, overviewNote.partContent)
				&& Objects.equals(this.lastEdit, overviewNote.lastEdit)
				&& Objects.equals(this.pinned, overviewNote.pinned)
				&& Objects.equals(this.archived, overviewNote.archived)
				&& Objects.equals(this.initVector, overviewNote.initVector);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, partContent, lastEdit, pinned, archived, initVector);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class OverviewNote {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    title: ").append(toIndentedString(title)).append("\n");
		sb.append("    partContent: ").append(toIndentedString(partContent)).append("\n");
		sb.append("    lastEdit: ").append(toIndentedString(lastEdit)).append("\n");
		sb.append("    pinned: ").append(toIndentedString(pinned)).append("\n");
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
