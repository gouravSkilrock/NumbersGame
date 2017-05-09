package com.skilrock.itg.IDBarcode;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class LinearImageCreator {

	public Graphics g;

	private Image im;

	public LinearImageCreator() {
	}

	public Graphics getGraphics() {
		return g;
	}

	public Image getImage(int i, int j) {
		int k = i;
		if (j > i) {
			k = j;
		}
		im = new BufferedImage(k, k, 13);
		g = ((BufferedImage) im).createGraphics();
		return im;
	}
}