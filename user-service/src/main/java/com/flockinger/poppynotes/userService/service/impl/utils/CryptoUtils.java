package com.flockinger.poppynotes.userService.service.impl.utils;

import org.apache.commons.codec.digest.Crypt;

public class CryptoUtils {
//FIXME implement that
		public static String getAuthEmailHash(String authEmail) {
			return Crypt.crypt(authEmail);
		}
		
		public static boolean checkPasswordWithHash(String enteredPassword, String storedHash) {
			return storedHash.equals(Crypt.crypt(enteredPassword, storedHash));
		}
}
