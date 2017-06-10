package com.flockinger.poppynotes.gateway.service.impl;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.flockinger.poppynotes.gateway.service.NoteEncryptionService;

@Service
public class NoteEncryptionServiceImpl implements NoteEncryptionService {

	private final static String ENCRYPTION_ALGORITHM = "AES";
	
	@Override
	public InputStream encryptNote(InputStream noteMessage, String key) {

		return null;
	}

	@Override
	public InputStream decryptNote(InputStream noteMessage, String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
