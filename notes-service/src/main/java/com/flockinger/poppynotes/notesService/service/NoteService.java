package com.flockinger.poppynotes.notesService.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.flockinger.poppynotes.notesService.dao.NoteRepository;
import static org.springframework.data.cassandra.repository.support.BasicMapId.*;


public class NoteService {

	@Autowired
	private NoteRepository dao;
	
}
