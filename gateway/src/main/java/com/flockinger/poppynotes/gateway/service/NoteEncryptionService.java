package com.flockinger.poppynotes.gateway.service;

import java.io.InputStream;

public interface NoteEncryptionService {
	InputStream encryptNote(InputStream noteMessage, String key);
	InputStream decryptNote(InputStream noteMessage, String key);
}
