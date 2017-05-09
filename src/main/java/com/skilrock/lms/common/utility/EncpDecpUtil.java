package com.skilrock.lms.common.utility;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


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
		} catch (Exception e) {
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
	
	/**
	 * encrypt given pin number
	 * @param pin_nbr
	 * @param desKey DES Key
	 * @param propKey AES Key
	 * @return Encrypted Pin
	 */
	public  static String encryptString(String pin_nbr,String desKey,String propKey){
		String enPin =null;
		try {
				byte[]propkey = Base64.decodeBase64(propKey.getBytes());
				byte[]deskey = Base64.decodeBase64(desKey.getBytes());
				//byte[] decoAesKey = EncpDecpUtil.decodeDES(deskey,propkey);
				//	key(propkey,deskey,decoAesKey);  //call me to know keys 
				byte[]value=pin_nbr.getBytes();
				byte[] encodedPin = EncpDecpUtil.encodeAES("www.skilrock.com".getBytes(),value); // TEMP 
				enPin = new String(Base64.encodeBase64(encodedPin));
				//String dePin = decryptPin(enPin);
				System.out.println("dePin"+enPin);
		}
		catch(Exception e){
				e.printStackTrace();
				System.out.println("Error In Pin Encryption");
				
		}
		return enPin;
		

	}
	/**
	 * Decrypt Given Pin
	 * @param enPin encrypted Pin 
	 * @param desKey DES Key
	 * @param propKey AES Key
	 * @return decrypt pin
	 */

	public static String decryptString(String enPin,String desKey,String propKey){
		
		byte[]propkey = Base64.decodeBase64(propKey.getBytes());
		byte[]deskey = Base64.decodeBase64(desKey.getBytes());
		//byte[] decoAesKey = EncpDecpUtil.decodeDES(deskey,propkey); // TEMP 
		byte[]pin = Base64.decodeBase64(enPin.getBytes());
		byte[] decodedPin=EncpDecpUtil.decodeAES("www.skilrock.com".getBytes(),pin); // TEMP 

		String st1 = new String(decodedPin);
		System.out.println("decoded Pin"+st1);
			
		return st1;
		
		
	}
	// this method can be used to check the value of AES and DES Keys 
	// to know the keys uncomment key function calling line in encryptPin Function
	private void key(byte[] propkey, byte[] deskey, byte[] decoAesKey) {
		String nothing = new String(propkey);
		String aes= new String(deskey);
		String des = new String(decoAesKey);
		System.out.println("str1 :"+nothing+"str2 :"+aes+"str3 :"+des);
		
	}
}
