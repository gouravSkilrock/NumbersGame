package com.skilrock.itg.IDBarcode;

public class GifEncoderHashitem {

	public int count;

	public int index;
	public boolean isTransparent;
	public int rgb;

	public GifEncoderHashitem(int i, int j, int k, boolean flag) {
		rgb = i;
		count = j;
		index = k;
		isTransparent = flag;
	}
}