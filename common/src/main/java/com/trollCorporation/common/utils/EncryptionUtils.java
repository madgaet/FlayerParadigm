package com.trollCorporation.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtils {

	public static String encrypt(String toEncryptMessage, String salt) {
		try {
			MessageDigest encrypter = null;
			encrypter = MessageDigest.getInstance("SHA-1");
			encrypter.reset();
			encrypter.update(toEncryptMessage.concat(salt).getBytes());
			byte[] encryptedMsg = encrypter.digest();
			return new String(encryptedMsg);
		} catch (NoSuchAlgorithmException nsae) {
			// can't happen -> shouldn't be a checked exception 
			return null;
		}
	}
}
