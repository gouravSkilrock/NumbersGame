package com.skilrock.lms.coreEngine.ola.common;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncpDecpUtil {
	public static byte[] keyGeneratorAES() {
		KeyGenerator kgen;

		try {
			kgen = KeyGenerator.getInstance("AES");
			kgen.init(128); // 192 and 256 bits may not be available

			// Generate the secret key specs.
			SecretKey skey = kgen.generateKey();
			return skey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] keyGeneratorDES() {
		KeyGenerator kgen;

		try {
			kgen = KeyGenerator.getInstance("DES");
			kgen.init(56); // 192 and 256 bits may not be available

			// Generate the secret key specs.
			SecretKey skey = kgen.generateKey();
			return skey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static byte[] encodeAES(byte[] rawKey, byte[] value) {

		// KeyGenerator kgen;
		try {

			SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");

			// Instantiate the cipher

			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			return cipher.doFinal(value);
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (NoSuchPaddingException e) {

			e.printStackTrace();
		} catch (InvalidKeyException e) {

			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();
		} catch (BadPaddingException e) {

			e.printStackTrace();
		}

		return null;

	}

	public static byte[] decodeAES(byte[] rawKey, byte[] encrypted) {

		SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");

		// Instantiate the cipher

		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			return cipher.doFinal(encrypted);
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (NoSuchPaddingException e) {

			e.printStackTrace();
		} catch (InvalidKeyException e) {

			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();
		} catch (BadPaddingException e) {

			e.printStackTrace();
		}

		return null;
	}

	public static byte[] encodeDES(byte[] rawKey, byte[] value) {

		// KeyGenerator kgen;
		try {

			SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "DES");

			// Instantiate the cipher

			Cipher cipher = Cipher.getInstance("DES");

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			return cipher.doFinal(value);
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (NoSuchPaddingException e) {

			e.printStackTrace();
		} catch (InvalidKeyException e) {

			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();
		} catch (BadPaddingException e) {

			e.printStackTrace();
		}

		return null;

	}

	public static byte[] decodeDES(byte[] rawKey, byte[] encrypted) {

		SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "DES");

		// Instantiate the cipher

		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			return cipher.doFinal(encrypted);
		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		} catch (NoSuchPaddingException e) {

			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();
		} catch (BadPaddingException e) {

			e.printStackTrace();
		} catch (InvalidKeyException e) {

			e.printStackTrace();
		}

		return null;

	}
}
