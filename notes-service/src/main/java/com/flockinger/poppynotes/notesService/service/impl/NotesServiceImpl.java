package com.flockinger.poppynotes.notesService.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.flockinger.poppynotes.notesService.dao.ArchivedNoteRepository;
import com.flockinger.poppynotes.notesService.dao.NoteRepository;
import com.flockinger.poppynotes.notesService.dto.CompleteNote;
import com.flockinger.poppynotes.notesService.dto.CreateNote;
import com.flockinger.poppynotes.notesService.dto.OverviewNote;
import com.flockinger.poppynotes.notesService.dto.UpdateNote;
import com.flockinger.poppynotes.notesService.exception.AccessingOtherUsersNotesException;
import com.flockinger.poppynotes.notesService.exception.NoteNotFoundException;
import com.flockinger.poppynotes.notesService.model.ArchivedNote;
import com.flockinger.poppynotes.notesService.model.Note;
import com.flockinger.poppynotes.notesService.service.NoteService;

@Component
public class NotesServiceImpl implements NoteService {

	@Autowired
	private NoteRepository dao;
	
	@Autowired
	private ArchivedNoteRepository archivedDao;
	
	@Autowired
	private ModelMapper mapper;
	
	public final static int OVERVIEW_CONTENT_PART_LENGTH = 50;
	
	
	@Override
	public CompleteNote findNote(String id, Long userId)
			throws NoteNotFoundException, AccessingOtherUsersNotesException {
		Note note = findNoteById(id);
		assertCorrectNoteUser(note.getUserId(),userId);
		boolean isArchived = archivedDao.exists(id);
		
		return map(note,isArchived);
	}
	
	private void assertCorrectNoteUser(Long internalUserId, Long externalUserId) throws AccessingOtherUsersNotesException {
		if(internalUserId != externalUserId) {
			throw new AccessingOtherUsersNotesException("Cannot access/edit other user's note entries!!");
		}
	}
	
	
	private Note findNoteById(String id) throws NoteNotFoundException{
		Note note = dao.findOne(id);
		
		if(note == null) {
			note = archivedDao.findOne(id);
		}
		assertNoteNotNull(note, id);
		
		return note;
	}
	
	private void assertNoteNotNull(Note note, String id) throws NoteNotFoundException{
		if(note == null){
			throw new NoteNotFoundException("Note not found with ID: " + id);
		}
	}

	@Override
	public void create(CreateNote note) {
		dao.save(map(note));
	}

	@Override
	public void update(UpdateNote note) throws NoteNotFoundException, AccessingOtherUsersNotesException {
		assertNoteExisting(note.getId());
		assertCorrectNoteUser(findNoteById(note.getId()).getUserId(), note.getUserId());
		
		if(BooleanUtils.isTrue(note.getArchived())) {
			dao.delete(note.getId());
			archivedDao.save(mapArchived(note));
		} else {
			archivedDao.delete(note.getId());
			dao.save(map(note));
		}
	}
	
	private void assertNoteExisting(String noteId) throws NoteNotFoundException {
		if(!dao.exists(noteId) && !archivedDao.exists(noteId)) {
			throw new NoteNotFoundException("Note not found with ID: " + noteId);
		}
	}

	@Override
	public void delete(String id, Long userId) throws NoteNotFoundException, AccessingOtherUsersNotesException {
		assertNoteExisting(id);
		assertCorrectNoteUser(findNoteById(id).getUserId(), userId);
		
		dao.delete(id);
		archivedDao.delete(id);
	}

	@Override
	public List<OverviewNote> findNotesByUserIdPaginated(Long userId, Pageable page) {
		List<Note> notes = dao.findByUserIdSorted(userId, page);
		return notes.stream().map(this::mapOverview).collect(Collectors.toList());
	}

	@Override
	public List<OverviewNote> findArchivedNotesByUserIdPaginated(Long userId, Pageable page) {
		List<ArchivedNote> notes = archivedDao.findByUserIdSorted(userId, page);
		return notes.stream().map(this::mapOverview).collect(Collectors.toList());
	}

	
	private OverviewNote mapOverview(Note note) {
		OverviewNote overview = mapper.map(note, OverviewNote.class);
		overview.setPartContent(StringUtils.abbreviate(note.getContent(), OVERVIEW_CONTENT_PART_LENGTH));
		return overview;
	}
	
	private ArchivedNote mapArchived(UpdateNote note){
		return mapper.map(note, ArchivedNote.class);
	}
	
	private Note map(UpdateNote note){
		return mapper.map(note, Note.class);
	}
	
	private Note map(CreateNote note){
		return mapper.map(note, Note.class);
	}
	
	private CompleteNote map(Note note,boolean isArchived) {
		CompleteNote completeNote =  mapper.map(note, CompleteNote.class);
		completeNote.setArchived(isArchived);
		return completeNote;
	}

}
