package com.skilrock.lms.web.scratchService.utility;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class VirnEncoderNDecoder {
	public static class EncryptionException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EncryptionException(Throwable t) {
			super(t);
		}
	}

	public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
	static Log logger = LogFactory.getLog(VirnEncoderNDecoder.class);

	private static final String UNICODE_FORMAT = "UTF8";

	private static String bytes2String(byte[] bytes) {
		StringBuffer stringBuffer = new StringBuffer();
		for (byte element : bytes) {
			stringBuffer.append((char) element);
		}
		return stringBuffer.toString();
	}

	private Cipher cipher;

	private SecretKeyFactory keyFactory;

	public VirnEncoderNDecoder() throws EncryptionException,
			NoSuchAlgorithmException, NoSuchPaddingException {
		keyFactory = SecretKeyFactory.getInstance(DESEDE_ENCRYPTION_SCHEME);
		cipher = Cipher.getInstance(DESEDE_ENCRYPTION_SCHEME);
	}

	public String decrypt(String encryptedString, String decryptionKey)
			throws EncryptionException {
		if (encryptedString == null || encryptedString.trim().length() <= 0) {
			throw new IllegalArgumentException(
					"encrypted string was null or empty");
		}

		try {
			KeySpec keySpec = fetchKeySpec(decryptionKey);
			SecretKey key = keyFactory.generateSecret(keySpec);
			cipher.init(Cipher.DECRYPT_MODE, key);
			BASE64Decoder base64decoder = new BASE64Decoder();
			byte[] cleartext = base64decoder.decodeBuffer(encryptedString);
			byte[] ciphertext = cipher.doFinal(cleartext);

			return bytes2String(ciphertext);
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	public String encrypt(String unencryptedString, String encryptionKey)
			throws EncryptionException {
		if (unencryptedString == null || unencryptedString.trim().length() == 0) {
			throw new IllegalArgumentException(
					"unencrypted string was null or empty");
		}

		try {

			KeySpec keySpec = fetchKeySpec(encryptionKey);
			SecretKey key = keyFactory.generateSecret(keySpec);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cleartext = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] ciphertext = cipher.doFinal(cleartext);

			BASE64Encoder base64encoder = new BASE64Encoder();
			return base64encoder.encode(ciphertext);
		} catch (Exception e) {
			throw new EncryptionException(e);
		}
	}

	public KeySpec fetchKeySpec(String encryptionKey)
			throws EncryptionException {

		try {
			if (encryptionKey == null) {
				throw new IllegalArgumentException("encryption key was null");
			}
			if (encryptionKey.trim().length() < 24) {
				throw new IllegalArgumentException(
						"encryption key was less than 24 characters");
			}

			byte[] keyAsBytes = encryptionKey.getBytes(UNICODE_FORMAT);
			KeySpec keySpec = new DESedeKeySpec(keyAsBytes);

			return keySpec;

		} catch (InvalidKeyException e) {
			throw new EncryptionException(e);
		} catch (UnsupportedEncodingException e) {
			throw new EncryptionException(e);
		}
	}

	/*
	 * public static void main(String[] args) { try { //VirnEncoderNDecoder obj =
	 * new VirnEncoderNDecoder("DESede"); VirnEncoderNDecoder obj = new
	 * VirnEncoderNDecoder(); String encStr = obj.encrypt("103-101001-001");
	 * logger.debug("103-101001-001"+", encStr = "+encStr); String decStr =
	 * obj.decrypt(encStr); logger.debug(encStr+", decStr = "+decStr); } catch
	 * (Exception e) { e.printStackTrace(); } }
	 */

}