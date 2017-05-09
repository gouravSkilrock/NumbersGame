package com.skilrock.lms.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author bajrang
 */
public class ClientMain {

	public static PrivateKey readPrivateKeyFromFile(String keyFileName)
			throws IOException {
		InputStream in = new FileInputStream(keyFileName);
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(
				in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey priKey = fact.generatePrivate(keySpec);
			return priKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

	public static byte[] rsaDecrypt(PrivateKey priKey, byte enText[]) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		byte[] cipherData = cipher.doFinal(enText);
		return cipherData;
	}

	public static char getChar(char ch1, char ch2) {
		char ch;
		switch (ch1) {
		case '0':
			ch = 0;
			break;
		case '1':
			ch = (char) (1 << 4);
			break;
		case '2':
			ch = (char) (2 << 4);
			break;
		case '3':
			ch = (char) (3 << 4);
			break;
		case '4':
			ch = (char) (4 << 4);
			break;
		case '5':
			ch = (char) (5 << 4);
			break;
		case '6':
			ch = (char) (6 << 4);
			break;
		case '7':
			ch = (char) (7 << 4);
			break;
		case '8':
			ch = (char) (8 << 4);
			break;
		case '9':
			ch = (char) (9 << 4);
			break;
		case 'A':
			ch = (char) (10 << 4);
			break;
		case 'B':
			ch = (char) (11 << 4);
			break;
		case 'C':
			ch = (char) (12 << 4);
			break;
		case 'D':
			ch = (char) (13 << 4);
			break;
		case 'E':
			ch = (char) (14 << 4);
			break;
		case 'F':
			ch = (char) (15 << 4);
			break;
		default:
			ch = 0;
			break;

		}

		switch (ch2) {
		case '0':
			ch = (char) (ch | 0);
			break;
		case '1':
			ch = (char) (ch | 1);
			break;
		case '2':
			ch = (char) (ch | 2);
			break;
		case '3':
			ch = (char) (ch | 3);
			break;
		case '4':
			ch = (char) (ch | 4);
			break;
		case '5':
			ch = (char) (ch | 5);
			break;
		case '6':
			ch = (char) (ch | 6);
			break;
		case '7':
			ch = (char) (ch | 7);
			break;
		case '8':
			ch = (char) (ch | 8);
			break;
		case '9':
			ch = (char) (ch | 9);
			break;
		case 'A':
			ch = (char) (ch | 10);
			break;
		case 'B':
			ch = (char) (ch | 11);
			break;
		case 'C':
			ch = (char) (ch | 12);
			break;
		case 'D':
			ch = (char) (ch | 13);
			break;
		case 'E':
			ch = (char) (ch | 14);
			break;
		case 'F':
			ch = (char) (ch | 15);
			break;
		default:
			ch = (char) (ch | 0);
			break;

		}
		return ch;
	}

	public static char[] hexToChar(char[] in) {
		int i;
		char[] out = new char[in.length / 2];
		for (i = 0; i < in.length / 2; i++) {
			out[i] = getChar(in[i * 2], in[i * 2 + 1]);
		}
		return out;
	}
}
