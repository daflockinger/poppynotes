package com.flockinger.poppynotes.gateway.service.impl;

import static com.flockinger.poppynotes.gateway.util.EncryptionUtils.decodeBase64;
import static com.flockinger.poppynotes.gateway.util.EncryptionUtils.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flockinger.poppynotes.gateway.exception.EncryptionSecurityException;
import com.flockinger.poppynotes.gateway.model.SecureNote;
import com.flockinger.poppynotes.gateway.service.NoteEncryptionService;
import com.flockinger.poppynotes.gateway.util.EncryptionUtils;

@Service
public class NoteEncryptionServiceImpl implements NoteEncryptionService {

	private static Logger logger = Logger.getLogger(NoteEncryptionServiceImpl.class.getName());

	/**
	 * For encryption a AES (265 bit) with Cipher-Block-Chaining and PKCS5 for padding
	 */
	private final static String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private final static String ENCRYPTION_ALGORITHM = "AES";
	private final static String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";

	/**
	 * Must be equal to the block size (128 bit for AES) of the algorithm
	 */
	private final static int IV_SIZE_BYTES = 16;
	/**
	 * Maximal key size in bytes for a 265 bit AES secret key
	 */
	private final static int MAX_KEY_SIZE_BYTES = 32;

	private final static Charset UTF_8 = StandardCharsets.UTF_8;
	private final static String INIT_VECTOR_LITERAL = "initVector";
	private final static String CONTENT_LITERAL = "content";
	private final static String PART_CONTENT_LITERAL = "partContent";

	private ObjectMapper mapper;

	public NoteEncryptionServiceImpl() {
		mapper = new ObjectMapper();
	}
	
	@Override
	public InputStream encryptNote(InputStream noteMessage, String key, String principalName) {
		byte[] encryptedMessage = null;
		try {
			encryptedMessage = IOUtils.toByteArray(noteMessage);
			JsonNode messageJson = mapper.readTree(encryptedMessage);
			IvParameterSpec initVector = createNewIV();
			String encryptedContent = encrypt(createKey(key, principalName), initVector, messageJson);
			messageJson = insertMessageAndInitVectorToJson((ObjectNode) messageJson, encryptedContent, initVector);
			encryptedMessage = mapper.writeValueAsBytes(messageJson);
			
		} catch (IOException e) {
			logger.error("Error reading/writing incomming/outgoing request input stream", e);
		} 
		return new ByteArrayInputStream(encryptedMessage);
	}

	
	private String extractNoteContentFrom(JsonNode noteMessage) {
		String content = "";

		if (noteMessage.has(CONTENT_LITERAL)) {
			content = noteMessage.get(CONTENT_LITERAL).asText();
		} else if (noteMessage.has(PART_CONTENT_LITERAL)) {
			content = noteMessage.get(PART_CONTENT_LITERAL).asText();
		}
		return content;
	}

	
	private String encrypt(Key key, IvParameterSpec initVector, JsonNode messageJson) {
		byte[] plainContent = extractNoteContentFrom(messageJson).getBytes(UTF_8);
		String encryptedContent = "";

		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, key, initVector);
			encryptedContent = encodeBase64(cipher.doFinal(plainContent));
			
		} catch (BadPaddingException | InvalidKeyException e) {
			throw new EncryptionSecurityException("Wrong key/user for decrypting message!", e);
		} catch (GeneralSecurityException e) {
			logger.error("Security configuration/provider error.", e);
		}
		return encryptedContent;
	}

	
	private IvParameterSpec createNewIV() {
		byte[] ivCode = new byte[IV_SIZE_BYTES];
		
		try {
			SecureRandom random = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
			random.nextBytes(ivCode);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Security configuration/provider error.", e);
		}
		return new IvParameterSpec(ivCode);
	}

	
	private Key createKey(String key, String principalName) {
		byte[] keyBytes = decodeBase64(key);
		byte[] principalBytes = getPrincipalWithoutEmailTail(principalName).getBytes(UTF_8);
		
		if(principalBytes.length > MAX_KEY_SIZE_BYTES){
			principalBytes = ArrayUtils.subarray(principalBytes, 0, MAX_KEY_SIZE_BYTES);
		}
		keyBytes = concatenateByteArrays(keyBytes,principalBytes);
		return new SecretKeySpec(keyBytes, 0, keyBytes.length, ENCRYPTION_ALGORITHM);
	}

	private String getPrincipalWithoutEmailTail(String principalName) {
		return StringUtils.substringBefore(principalName, "@");
	}

	
	private JsonNode insertMessageAndInitVectorToJson(ObjectNode json, String content, IvParameterSpec initVector) {
		json = (ObjectNode) insertMessageToJson(json, content);

		if (json.has(INIT_VECTOR_LITERAL)) {
			json.put(INIT_VECTOR_LITERAL, encodeBase64(initVector.getIV()));
		}
		return json;
	}

	
	@Override
	public InputStream decryptNote(InputStream noteMessage, String key, String principalName) {
		byte[] decryptedMessage = null;

		try {
			decryptedMessage = IOUtils.toByteArray(noteMessage);
			JsonNode message = mapper.readTree(decryptedMessage);
			String plainNote = decrypt(createKey(key,principalName), message);

			message = insertMessageToJson((ObjectNode) message, plainNote);
			decryptedMessage = mapper.writeValueAsBytes(message);
		} catch (IOException e) {
			logger.error("Error reading incomming request input stream", e);
		} 
		return new ByteArrayInputStream(decryptedMessage);
	}
	
	
	private String decrypt(Key key, JsonNode messageJson){
		SecureNote secureNote = extractSecureNoteFrom(messageJson);
		byte[] plainNoteBytes = null;
		
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key, secureNote.getInitVector());
			plainNoteBytes = cipher.doFinal(EncryptionUtils.decodeBase64(secureNote.getContent()));
		
		} catch (BadPaddingException | InvalidKeyException e) {
			throw new EncryptionSecurityException("Wrong key/user for decrypting message!", e);
		} catch (GeneralSecurityException e) {
			logger.error("Security configuration/provider error.", e);
		} 
		return new String(plainNoteBytes, StandardCharsets.UTF_8);
	}

	
	private SecureNote extractSecureNoteFrom(JsonNode noteMessage) {
		SecureNote note = new SecureNote();
		note.setContent(extractNoteContentFrom(noteMessage));

		if (noteMessage.has(INIT_VECTOR_LITERAL)) {
			byte[] initVector = EncryptionUtils.decodeBase64(noteMessage.get(INIT_VECTOR_LITERAL).asText());
			note.setInitVector(new IvParameterSpec(initVector));
		}
		return note;
	}

	private JsonNode insertMessageToJson(ObjectNode json, String content) {
		if (json.has(CONTENT_LITERAL)) {
			json.put(CONTENT_LITERAL, content);
		} else if (json.has(PART_CONTENT_LITERAL)) {
			json.put(PART_CONTENT_LITERAL, content);
		}
		return json;
	}
}
