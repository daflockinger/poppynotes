package com.flockinger.poppynotes.gateway.util;

import java.util.Base64;

import org.apache.commons.lang.ArrayUtils;

public class EncryptionUtils {

	public static String encodeBase64(byte[] messageBytes) {
		return Base64.getEncoder().encodeToString(messageBytes);
	}
	
	public static byte[] decodeBase64(String message){
		return Base64.getDecoder().decode(message);
	}
	
	public static byte[] concatenateByteArrays(byte[] first, byte[] second){
		byte[] keyBytes = ArrayUtils.subarray(first, 0, first.length - second.length);
		return ArrayUtils.addAll(keyBytes, second);
	}
}
