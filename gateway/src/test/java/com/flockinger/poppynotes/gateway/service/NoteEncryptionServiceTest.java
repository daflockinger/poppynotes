package com.flockinger.poppynotes.gateway.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flockinger.poppynotes.gateway.exception.EncryptionSecurityException;
import com.flockinger.poppynotes.gateway.service.impl.NoteEncryptionServiceImpl;
import com.flockinger.poppynotes.gateway.util.EncryptionUtils;

public class NoteEncryptionServiceTest {

	private NoteEncryptionService service = new NoteEncryptionServiceImpl();
	private ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void testDecryptEncryptWithFullContent_withValidKeyAndPrincipal_shouldUncoverSecrets() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String noteJson = "{\"title\":\"secret stuff\",\"content\":\"Super secret message!!\",\"userId\":1,\"lastEdit\":null,\"pinned\":false,\"initVector\":null}";
		byte[] key = createKey();
		
		InputStream encryptedStream = service.encryptNote(new ByteArrayInputStream(noteJson.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String encryptedResult = IOUtils.toString(encryptedStream, StandardCharsets.UTF_8);
		
		String encryptedContent = mapper.readTree(encryptedResult).get("content").asText();
		assertFalse("encrypted content should contain something", encryptedContent.isEmpty());
		assertFalse("encrypted content should NOT contain plain message", encryptedContent.contains("Super secret message"));		
		
		InputStream decryptedStream = service.decryptNote(new ByteArrayInputStream(encryptedResult.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String decryptedResult = IOUtils.toString(decryptedStream, StandardCharsets.UTF_8);
		String decryptedContent = mapper.readTree(decryptedResult).get("content").asText();
		String initVector = mapper.readTree(decryptedResult).get("initVector").asText();
		
		assertEquals("decrypted content should be the same",decryptedContent,"Super secret message!!");
		assertNotNull("init vector should be present", initVector);
		assertTrue("init vector should not be empty", !initVector.isEmpty());
	}
	
	@Test
	public void testDecryptEncryptPartFullContent_withValidKeyAndPrincipal_shouldUncoverSecrets() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String noteJson = "{\"title\":\"secret stuff\",\"partContent\":\"Super secret message!!\",\"userId\":1,\"lastEdit\":null,\"pinned\":false,\"initVector\":null}";
		byte[] key = createKey();
		
		InputStream encryptedStream = service.encryptNote(new ByteArrayInputStream(noteJson.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String encryptedResult = IOUtils.toString(encryptedStream, StandardCharsets.UTF_8);
		
		String encryptedContent = mapper.readTree(encryptedResult).get("partContent").asText();
		assertFalse("encrypted content should contain something", encryptedContent.isEmpty());
		assertFalse("encrypted content should NOT contain plain message", encryptedContent.contains("Super secret message"));		
		
		InputStream decryptedStream = service.decryptNote(new ByteArrayInputStream(encryptedResult.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String decryptedResult = IOUtils.toString(decryptedStream, StandardCharsets.UTF_8);
		String decryptedContent = mapper.readTree(decryptedResult).get("partContent").asText();
		String initVector = mapper.readTree(decryptedResult).get("initVector").asText();
		
		assertEquals("decrypted content should be the same",decryptedContent,"Super secret message!!");
		assertNotNull("init vector should be present", initVector);
		assertTrue("init vector should not be empty", !initVector.isEmpty());
	}
	
	@Test(expected=EncryptionSecurityException.class)
	public void testDecryptEncryptPartFullContent_withDecryptWithStolenKeyButWrongEmail_shouldThrowException() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String noteJson = "{\"title\":\"secret stuff\",\"content\":\"Super secret message!!\",\"userId\":1,\"lastEdit\":null,\"pinned\":false,\"initVector\":null}";
		byte[] key = createKey();
		
		InputStream encryptedStream = service.encryptNote(new ByteArrayInputStream(noteJson.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String encryptedResult = IOUtils.toString(encryptedStream, StandardCharsets.UTF_8);
		
		service.decryptNote(new ByteArrayInputStream(encryptedResult.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "sepp@gmail.com");
	}
	
	@Test(expected=EncryptionSecurityException.class)
	public void testDecryptEncryptPartFullContent_withDecryptWithWrongKey_shouldThrowException() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String noteJson = "{\"title\":\"secret stuff\",\"content\":\"Super secret message!!\",\"userId\":1,\"lastEdit\":null,\"pinned\":false,\"initVector\":null}";
		byte[] key = createKey();
		
		InputStream encryptedStream = service.encryptNote(new ByteArrayInputStream(noteJson.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String encryptedResult = IOUtils.toString(encryptedStream, StandardCharsets.UTF_8);
		
		service.decryptNote(new ByteArrayInputStream(encryptedResult.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(createKey()), "flo@gmail.com");
	}
	

	@Test(expected=EncryptionSecurityException.class)
	public void testDecryptEncryptPartFullContent_withEmptyPrincipal_shouldThrowException() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String noteJson = "{\"title\":\"secret stuff\",\"content\":\"Super secret message!!\",\"userId\":1,\"lastEdit\":null,\"pinned\":false,\"initVector\":null}";
		byte[] key = createKey();
		
		InputStream encryptedStream = service.encryptNote(new ByteArrayInputStream(noteJson.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String encryptedResult = IOUtils.toString(encryptedStream, StandardCharsets.UTF_8);
		
		service.decryptNote(new ByteArrayInputStream(encryptedResult.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "");
	}
	
	@Test
	public void testDecryptEncryptPartFullContent_withVeryLongEmail_shouldStillWorkButInternalyUseCutEmailLength() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String noteJson = "{\"title\":\"secret stuff\",\"partContent\":\"Super secret message!!\",\"userId\":1,\"lastEdit\":null,\"pinned\":false,\"initVector\":null}";
		byte[] key = createKey();
		
		InputStream encryptedStream = service.encryptNote(new ByteArrayInputStream(noteJson.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flllllllllllooooooooooooooooooooo@gmail.com");
		String encryptedResult = IOUtils.toString(encryptedStream, StandardCharsets.UTF_8);
		
		String encryptedContent = mapper.readTree(encryptedResult).get("partContent").asText();
		assertFalse("encrypted content should contain something", encryptedContent.isEmpty());
		assertFalse("encrypted content should NOT contain plain message", encryptedContent.contains("Super secret message"));		
		
		InputStream decryptedStream = service.decryptNote(new ByteArrayInputStream(encryptedResult.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flllllllllllooooooooooooooooooooo@gmail.com");
		String decryptedResult = IOUtils.toString(decryptedStream, StandardCharsets.UTF_8);
		String decryptedContent = mapper.readTree(decryptedResult).get("partContent").asText();
		String initVector = mapper.readTree(decryptedResult).get("initVector").asText();
		
		assertEquals("decrypted content should be the same",decryptedContent,"Super secret message!!");
		assertNotNull("init vector should be present", initVector);
		assertTrue("init vector should not be empty", !initVector.isEmpty());
	}
	
	@Test
	public void testDecryptEncryptWithFullContent_withRequestWithoutContent_shouldStillWork() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String noteJson = "{\"title\":\"secret stuff\",\"userId\":1,\"lastEdit\":null,\"pinned\":false,\"initVector\":null}";
		byte[] key = createKey();
		
		InputStream encryptedStream = service.encryptNote(new ByteArrayInputStream(noteJson.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String encryptedResult = IOUtils.toString(encryptedStream, StandardCharsets.UTF_8);
		
		assertFalse("encryption result should also have no content", mapper.readTree(encryptedResult).has("content"));
		
		InputStream decryptedStream = service.decryptNote(new ByteArrayInputStream(encryptedResult.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String decryptedResult = IOUtils.toString(decryptedStream, StandardCharsets.UTF_8);
		String initVector = mapper.readTree(decryptedResult).get("initVector").asText();
		
		assertFalse("decryption result should also have no content", mapper.readTree(encryptedResult).has("content"));
		assertNotNull("init vector should be present", initVector);
		assertTrue("init vector should not be empty", !initVector.isEmpty());
	}
	
	
	@Test
	public void testDecryptEncryptWithFullContent_withNullContent_shouldStillWork() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String noteJson = "{\"title\":\"secret stuff\",\"content\":null,\"userId\":1,\"lastEdit\":null,\"pinned\":false,\"initVector\":null}";
		byte[] key = createKey();
		
		InputStream encryptedStream = service.encryptNote(new ByteArrayInputStream(noteJson.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String encryptedResult = IOUtils.toString(encryptedStream, StandardCharsets.UTF_8);
		
		String encryptedContent = mapper.readTree(encryptedResult).get("content").asText();
		assertFalse("encrypted content should contain something", encryptedContent.isEmpty());
		assertFalse("encrypted content should NOT contain plain message", encryptedContent.contains("Super secret message"));		
		
		InputStream decryptedStream = service.decryptNote(new ByteArrayInputStream(encryptedResult.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String decryptedResult = IOUtils.toString(decryptedStream, StandardCharsets.UTF_8);
		String decryptedContent = mapper.readTree(decryptedResult).get("content").asText();
		String initVector = mapper.readTree(decryptedResult).get("initVector").asText();
		
		assertEquals("decrypted content since it was originally null, should still be null","null",decryptedContent);
		assertNotNull("init vector should be present", initVector);
		assertTrue("init vector should not be empty", !initVector.isEmpty());
	}
	
	
	@Test
	public void testDecryptEncryptWithFullContent_withInvalidRequestJson_shouldStillWork() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		String noteJson = "{\"title\":\"secret stuff\",\"content\":null,\"userId\":1,\"lastEdit\":null,\"pinned\":false,\"initVector\":null";
		byte[] key = createKey();
		
		InputStream encryptedStream = service.encryptNote(new ByteArrayInputStream(noteJson.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String encryptedResult = IOUtils.toString(encryptedStream, StandardCharsets.UTF_8);
		assertEquals("broken json should be unchanged",noteJson,encryptedResult);

		InputStream decryptedStream = service.decryptNote(new ByteArrayInputStream(encryptedResult.getBytes(StandardCharsets.UTF_8)), EncryptionUtils.encodeBase64(key), "flo@gmail.com");
		String decryptedResult = IOUtils.toString(decryptedStream, StandardCharsets.UTF_8);
		
		assertEquals("broken json should be unchanged",noteJson,decryptedResult);
	}
	
	
	private byte[] createKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] salt = new byte[1014];
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.nextBytes(salt);
		
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec("flo@gmail.com".toCharArray(), salt, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
		
		return secret.getEncoded();
	}
}
