package com.trollCorporation.common.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtils {

	public static byte[] encrypt(String toEncryptMessage, String salt) {
		try {
			MessageDigest encrypter = null;
			encrypter = MessageDigest.getInstance("SHA-1");
			encrypter.reset();
			encrypter.update(toEncryptMessage.concat(salt).getBytes());
			byte[] encryptedMsg = encrypter.digest();
			return encryptedMsg;
		} catch (NoSuchAlgorithmException nsae) {
			// can't happen -> shouldn't be a checked exception 
			return null;
		}
	}
	
	public static boolean compare(byte[] bytes1, byte[] bytes2) {
		String pswd1 = new String(bytes1, Charset.forName("UTF-8"));
		String pswd2 = new String(bytes2, Charset.forName("UTF-8"));
		return pswd1.equals(pswd2);
	}
}
