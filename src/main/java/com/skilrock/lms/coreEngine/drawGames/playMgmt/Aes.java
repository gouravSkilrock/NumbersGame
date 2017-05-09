package com.skilrock.lms.coreEngine.drawGames.playMgmt;

/*
 * Aes.java
 *
 * Created on June 23, 2010, 1:37 PM
 * 
 */

/**
 * 
 * @author Bajrang Prasad Patidar
 */
public class Aes {

	// The round constant word array, Rcon[i], contains the values given by
	// x to th e power (i-1) being powers of x (x is denoted as {02}) in the
	// field GF(28)
	// Note that i starts at 1, not 0).
	char chRcon[] = { 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80,
			0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63,
			0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91,
			0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94,
			0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01,
			0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8,
			0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a,
			0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3,
			0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83,
			0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10,
			0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f,
			0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa,
			0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f,
			0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8,
			0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b,
			0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6,
			0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39,
			0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33,
			0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02,
			0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab,
			0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4,
			0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd,
			0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d,
			0x3a, 0x74, 0xe8, 0xcb };
	char chRoundKey[] = new char[240];
	char chRsbox[] = { 0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf,
			0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb, 0x7c, 0xe3, 0x39, 0x82,
			0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9,
			0xcb, 0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c,
			0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e, 0x08, 0x2e, 0xa1, 0x66, 0x28,
			0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25,
			0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c,
			0xcc, 0x5d, 0x65, 0xb6, 0x92, 0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed,
			0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84, 0x90,
			0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05,
			0xb8, 0xb3, 0x45, 0x06, 0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f,
			0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b, 0x3a, 0x91,
			0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0,
			0xb4, 0xe6, 0x73, 0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85,
			0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e, 0x47, 0xf1, 0x1a,
			0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18,
			0xbe, 0x1b, 0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a,
			0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4, 0x1f, 0xdd, 0xa8, 0x33,
			0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec,
			0x5f, 0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5,
			0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef, 0xa0, 0xe0, 0x3b, 0x4d, 0xae,
			0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61,
			0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14,
			0x63, 0x55, 0x21, 0x0c, 0x7d };

	char chSbox[] = {
			// 0 1 2 3 4 5 6 7 8 9 A B C D E F
			0x63,
			0x7c,
			0x77,
			0x7b,
			0xf2,
			0x6b,
			0x6f,
			0xc5,
			0x30,
			0x01,
			0x67,
			0x2b,
			0xfe,
			0xd7,
			0xab,
			0x76, // 0
			0xca,
			0x82,
			0xc9,
			0x7d,
			0xfa,
			0x59,
			0x47,
			0xf0,
			0xad,
			0xd4,
			0xa2,
			0xaf,
			0x9c,
			0xa4,
			0x72,
			0xc0, // 1
			0xb7, 0xfd,
			0x93,
			0x26,
			0x36,
			0x3f,
			0xf7,
			0xcc,
			0x34,
			0xa5,
			0xe5,
			0xf1,
			0x71,
			0xd8,
			0x31,
			0x15, // 2
			0x04, 0xc7, 0x23,
			0xc3,
			0x18,
			0x96,
			0x05,
			0x9a,
			0x07,
			0x12,
			0x80,
			0xe2,
			0xeb,
			0x27,
			0xb2,
			0x75, // 3
			0x09, 0x83, 0x2c, 0x1a,
			0x1b,
			0x6e,
			0x5a,
			0xa0,
			0x52,
			0x3b,
			0xd6,
			0xb3,
			0x29,
			0xe3,
			0x2f,
			0x84, // 4
			0x53, 0xd1, 0x00, 0xed, 0x20,
			0xfc,
			0xb1,
			0x5b,
			0x6a,
			0xcb,
			0xbe,
			0x39,
			0x4a,
			0x4c,
			0x58,
			0xcf, // 5
			0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d,
			0x33,
			0x85,
			0x45,
			0xf9,
			0x02,
			0x7f,
			0x50,
			0x3c,
			0x9f,
			0xa8, // 6
			0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38,
			0xf5,
			0xbc,
			0xb6,
			0xda,
			0x21,
			0x10,
			0xff,
			0xf3,
			0xd2, // 7
			0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17,
			0xc4,
			0xa7,
			0x7e,
			0x3d,
			0x64,
			0x5d,
			0x19,
			0x73, // 8
			0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46,
			0xee,
			0xb8,
			0x14,
			0xde,
			0x5e,
			0x0b,
			0xdb, // 9
			0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3,
			0xac,
			0x62,
			0x91,
			0x95,
			0xe4,
			0x79, // A
			0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4,
			0xea,
			0x65,
			0x7a,
			0xae,
			0x08, // B
			0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74,
			0x1f, 0x4b,
			0xbd,
			0x8b,
			0x8a, // C
			0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57,
			0xb9, 0x86, 0xc1,
			0x1d,
			0x9e, // D
			0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87,
			0xe9, 0xce, 0x55, 0x28,
			0xdf, // E
			0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d,
			0x0f, 0xb0, 0x54, 0xbb, 0x16 // F
	};

	char chState[][] = new char[4][4];

	/** Creates a new instance of Aes */
	int iNr, iNk, iNb;

	public Aes(char chKey[], int iKeysize) {

		iNb = 4; // fixed value
		iNk = iKeysize / 32;
		iNr = iNk + 6;
		setKey(chKey);
	}

	// This function adds the round key to chState.
	// The round key is added to the chState by an XOR function.
	void AddRoundKey(int round) {
		int i, j;
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				chState[j][i] ^= chRoundKey[round * iNb * 4 + i * iNb + j];
			}
		}
	}

	void charToHex(char[] in, char[] out, int len) {
		int i;
		for (i = 0; i < len; i++) {
			out[i * 2] = getHex(in[i] >> 4);
			out[i * 2 + 1] = getHex(in[i] & 0x0F);
		}
	}

	// Cipher is the main function that encrypts the PlainText.
	char[] cipher(char chIn[], int index) {
		int i, j, round = 0;
		char chOut[] = new char[32];
		char[] chEnc = new char[16];

		// Copy the input PlainText to chState array.
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				chState[j][i] = chIn[index + i * 4 + j];
			}
		}

		// Add the First round key to the chState before starting the rounds.
		AddRoundKey(0);

		// There will be Nr rounds.
		// The first Nr-1 rounds are identical.
		// These Nr-1 rounds are executed in the loop below.
		for (round = 1; round < iNr; round++) {
			SubBytes();
			ShiftRows();
			MixColumns();
			AddRoundKey(round);
		}

		// The last round is given below.
		// The MixColumns function is not here in the last round.
		SubBytes();
		ShiftRows();
		AddRoundKey(iNr);

		// The encryption process is over.
		// Copy the chState array to output array.
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				chEnc[i * 4 + j] = chState[j][i];
			}
		}
		charToHex(chEnc, chOut, 16);

		return chOut;
	}

	public char[] decrypt(char[] in) {
		int i, j, len;
		char[] temp = null;// new char[16];
		len = in.length;
		char[] out = new char[len / 2];

		for (i = 0; i < len / 32; i++) {
			temp = inv_cipher(in, i * 32 /* , out , i*16 */);
			for (j = 0; j < 16; j++) {
				out[i * 16 + j] = temp[j];
			}
		}
		return out;
	}

	// /functions for encryptions

	public char[] encrypt(char[] in) {
		int i, j, len, k;
		char[] inter;
		char[] out;
		len = in.length;
		char[] temp = new char[32];
		if (len % 16 != 0) {
			out = new char[32 * (len / 16 + 1)];
		} else {
			out = new char[len * 2];
		}

		for (i = 0; i < len / 16; i++) {
			temp = cipher(in, i * 16 /* out , i*32 */);
			for (j = 0; j < 32; j++) {
				out[i * 32 + j] = temp[j];
			}
		}
		if (len % 16 != 0) {
			k = len / 16;
			k = k * 16;
			inter = new char[16];
			for (i = 0; i < len % 16; i++) {
				inter[i] = in[k + i];
			}

			for (j = i; j < 16; j++) {
				inter[j] = ' ';
			}

			temp = cipher(inter, 0 /* , out , k*32 */);
			for (j = 0; j < 32; j++) {
				out[k * 2 + j] = temp[j];
			}
		}
		return out;
	}

	char getChar(char ch1, char ch2) {
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
			ch = ch;
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
			ch = ch;
			break;

		}
		return ch;
	}

	char getHex(int n) {
		char ch;
		switch (n) {
		case 0:
			ch = '0';
			break;
		case 1:
			ch = '1';
			break;
		case 2:
			ch = '2';
			break;
		case 3:
			ch = '3';
			break;
		case 4:
			ch = '4';
			break;
		case 5:
			ch = '5';
			break;
		case 6:
			ch = '6';
			break;
		case 7:
			ch = '7';
			break;
		case 8:
			ch = '8';
			break;
		case 9:
			ch = '9';
			break;
		case 10:
			ch = 'A';
			break;
		case 11:
			ch = 'B';
			break;
		case 12:
			ch = 'C';
			break;
		case 13:
			ch = 'D';
			break;
		case 14:
			ch = 'E';
			break;
		case 15:
			ch = 'F';
			break;
		default:
			ch = 'X';

		}
		return ch;
	}

	void hexToChar(char[] in, int index, char[] out, int len) {
		int i;
		for (i = 0; i < len; i++) {
			out[i] = getChar(in[index + i * 2], in[index + i * 2 + 1]);
		}
	}

	// InvCipher is the main function that decrypts the CipherText.
	char[] inv_cipher(char chIn[], int index) {
		int i, j, round = 0;
		char chOut[] = new char[16];
		// Copy the input CipherText to chState array.
		char[] chDec = new char[16];

		hexToChar(chIn, index, chDec, 16);

		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				chState[j][i] = chDec[i * 4 + j];
			}
		}

		// Add the First round key to the chState before starting the rounds.
		AddRoundKey(iNr);

		// There will be Nr rounds.
		// The first Nr-1 rounds are identical.
		// These Nr-1 rounds are executed in the loop below.
		for (round = iNr - 1; round > 0; round--) {
			InvShiftRows();
			InvSubBytes();
			AddRoundKey(round);
			InvMixColumns();
		}

		// The last round is given below.
		// The MixColumns function is not here in the last round.
		InvShiftRows();
		InvSubBytes();
		AddRoundKey(0);

		// The decryption process is over.
		// Copy the chState array to output array.
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				chOut[i * 4 + j] = chState[j][i];
			}
		}
		return chOut;
	}

	// MixColumns function mixes the columns of the chState matrix.
	// The method used to multiply may be difficult to understand for the
	// inexperienced.
	// Please use the references to gain more information.
	void InvMixColumns() {
		int i;
		char a, b, c, d;
		for (i = 0; i < 4; i++) {

			a = chState[0][i];
			b = chState[1][i];
			c = chState[2][i];
			d = chState[3][i];

			chState[0][i] = (char) (Multiply(a, (char) 0x0e)
					^ Multiply(b, (char) 0x0b) ^ Multiply(c, (char) 0x0d) ^ Multiply(
					d, (char) 0x09));
			chState[1][i] = (char) (Multiply(a, (char) 0x09)
					^ Multiply(b, (char) 0x0e) ^ Multiply(c, (char) 0x0b) ^ Multiply(
					d, (char) 0x0d));
			chState[2][i] = (char) (Multiply(a, (char) 0x0d)
					^ Multiply(b, (char) 0x09) ^ Multiply(c, (char) 0x0e) ^ Multiply(
					d, (char) 0x0b));
			chState[3][i] = (char) (Multiply(a, (char) 0x0b)
					^ Multiply(b, (char) 0x0d) ^ Multiply(c, (char) 0x09) ^ Multiply(
					d, (char) 0x0e));
		}
	}

	// The ShiftRows() function shifts the rows in the chState to the left.
	// Each row is shifted with different offset.
	// Offset = Row number. So the first row is not shifted.
	void InvShiftRows() {
		char chTemp;

		// Rotate first row 1 columns to right
		chTemp = chState[1][3];
		chState[1][3] = chState[1][2];
		chState[1][2] = chState[1][1];
		chState[1][1] = chState[1][0];
		chState[1][0] = chTemp;

		// Rotate second row 2 columns to right
		chTemp = chState[2][0];
		chState[2][0] = chState[2][2];
		chState[2][2] = chTemp;

		chTemp = chState[2][1];
		chState[2][1] = chState[2][3];
		chState[2][3] = chTemp;

		// Rotate third row 3 columns to right
		chTemp = chState[3][0];
		chState[3][0] = chState[3][1];
		chState[3][1] = chState[3][2];
		chState[3][2] = chState[3][3];
		chState[3][3] = chTemp;
	}

	// The SubBytes Function Substitutes the values in the
	// chState matrix with values in an S-box.
	void InvSubBytes() {
		int i, j;
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				chState[i][j] = chRsbox[(chState[i][j] & 0x00ff)];

			}
		}
	}

	// MixColumns function mixes the columns of the chState matrix
	void MixColumns() {
		int i;
		char chTmp, chTm, chT;
		for (i = 0; i < 4; i++) {
			chT = chState[0][i];
			chTmp = (char) (chState[0][i] ^ chState[1][i] ^ chState[2][i] ^ chState[3][i]);
			chTm = (char) (chState[0][i] ^ chState[1][i]);
			chTm = xtime(chTm);
			chState[0][i] ^= chTm ^ chTmp;
			chTm = (char) (chState[1][i] ^ chState[2][i]);
			chTm = xtime(chTm);
			chState[1][i] ^= chTm ^ chTmp;
			chTm = (char) (chState[2][i] ^ chState[3][i]);
			chTm = xtime(chTm);
			chState[2][i] ^= chTm ^ chTmp;
			chTm = (char) (chState[3][i] ^ chT);
			chTm = xtime(chTm);
			chState[3][i] ^= chTm ^ chTmp;
		}
	}

	// Multiplty is a funtion used to multiply numbers in the field GF(2^8)
	char Multiply(char x, char y) {
		return (char) ((y & 1) * x ^ (y >> 1 & 1) * xtime(x) ^ (y >> 2 & 1)
				* xtime(xtime(x)) ^ (y >> 3 & 1) * xtime(xtime(xtime(x))) ^ (y >> 4 & 1)
				* xtime(xtime(xtime(xtime(x)))));
	}

	private void setKey(char chKey[]) {
		int i, j;
		char chTemp[] = new char[4];
		char k;
		// The first round key is the key itself.
		for (i = 0; i < iNk; i++) {
			chRoundKey[i * 4] = chKey[i * 4];
			chRoundKey[i * 4 + 1] = chKey[i * 4 + 1];
			chRoundKey[i * 4 + 2] = chKey[i * 4 + 2];
			chRoundKey[i * 4 + 3] = chKey[i * 4 + 3];
		}

		// All other round keys are found from the previous round keys.
		while (i < iNb * (iNr + 1)) {
			for (j = 0; j < 4; j++) {
				chTemp[j] = chRoundKey[(i - 1) * 4 + j];
			}
			if (i % iNk == 0) {
				// This function rotates the 4 bytes in a word to the left once.
				// [a0,a1,a2,a3] becomes [a1,a2,a3,a0]

				// Function RotWord()
				{
					k = chTemp[0];
					chTemp[0] = chTemp[1];
					chTemp[1] = chTemp[2];
					chTemp[2] = chTemp[3];
					chTemp[3] = k;
				}

				// SubWord() is a function that takes a four-byte input word and
				// applies the S-box to each of the four bytes to produce an
				// output word.

				// Function Subword()
				{
					chTemp[0] = chSbox[chTemp[0]];
					chTemp[1] = chSbox[chTemp[1]];
					chTemp[2] = chSbox[chTemp[2]];
					chTemp[3] = chSbox[chTemp[3]];
				}

				chTemp[0] = (char) (chTemp[0] ^ chRcon[i / iNk]);
			} else if (iNk > 6 && i % iNk == 4) {
				// Function Subword()
				{
					chTemp[0] = chSbox[chTemp[0]];
					chTemp[1] = chSbox[chTemp[1]];
					chTemp[2] = chSbox[chTemp[2]];
					chTemp[3] = chSbox[chTemp[3]];
				}
			}
			chRoundKey[i * 4 + 0] = (char) (chRoundKey[(i - iNk) * 4 + 0] ^ chTemp[0]);
			chRoundKey[i * 4 + 1] = (char) (chRoundKey[(i - iNk) * 4 + 1] ^ chTemp[1]);
			chRoundKey[i * 4 + 2] = (char) (chRoundKey[(i - iNk) * 4 + 2] ^ chTemp[2]);
			chRoundKey[i * 4 + 3] = (char) (chRoundKey[(i - iNk) * 4 + 3] ^ chTemp[3]);
			i++;

		}
	}

	// The ShiftRows() function shifts the rows in the chState to the left.
	// Each row is shifted with different offset.
	// Offset = Row number. So the first row is not shifted.
	void ShiftRows() {
		char chTemp;

		// Rotate first row 1 columns to left
		chTemp = chState[1][0];
		chState[1][0] = chState[1][1];
		chState[1][1] = chState[1][2];
		chState[1][2] = chState[1][3];
		chState[1][3] = chTemp;

		// Rotate second row 2 columns to left
		chTemp = chState[2][0];
		chState[2][0] = chState[2][2];
		chState[2][2] = chTemp;

		chTemp = chState[2][1];
		chState[2][1] = chState[2][3];
		chState[2][3] = chTemp;

		// Rotate third row 3 columns to left
		chTemp = chState[3][0];
		chState[3][0] = chState[3][3];
		chState[3][3] = chState[3][2];
		chState[3][2] = chState[3][1];
		chState[3][1] = chTemp;
	}

	// The SubBytes Function Substitutes the values in the
	// chState matrix with values in an S-box.
	void SubBytes() {
		int i, j;
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				chState[i][j] = chSbox[(chState[i][j] & 0x00ff)];

			}
		}
	}

	char xtime(char x) {
		return (char) (x << 1 ^ (x >> 7 & 1) * 0x1b);
	}

}
