package com.flockinger.poppynotes.notesService.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="note")
@CompoundIndexes(value={
		@CompoundIndex(name="lastedit_user_idx",def="{'lastEdit': -1, 'userId' : 1}"),
		@CompoundIndex(name="user_pinned_idx",def="{'userId' : 1, 'pinned' : -1}"),
		@CompoundIndex(name="lastedit_user_pin_idx",def="{'lastEdit': -1, 'userId' : 1, 'pinned' : -1}")
})
public class Note {

	@NotNull
	@Id
	private String id;
	
	@NotNull
	@Indexed
	private Long userId;

	@NotNull
	private Boolean pinned;
	
	@NotNull
	@Indexed
	private Date lastEdit;

	@NotNull
	private String title;

	
	private String content;
	
	@NotNull
	private String initVector;
	
	
	public String getInitVector() {
		return initVector;
	}

	public void setInitVector(String initVector) {
		this.initVector = initVector;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getPinned() {
		return pinned;
	}

	public void setPinned(Boolean pinned) {
		this.pinned = pinned;
	}

	public Date getLastEdit() {
		return lastEdit;
	}

	public void setLastEdit(Date lastEdit) {
		this.lastEdit = lastEdit;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
