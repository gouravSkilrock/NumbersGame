package com.skilrock.itg.IDBarcode;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;


public class BarcodeEncoder {

	Linear barcode;

	public boolean result;

	String sFile;

	String sFormat;

	public BarcodeEncoder(Linear barcode) {

		this.barcode = barcode;
		// saveToGIF();
	}

	public BarcodeEncoder(Linear barcode, String s, String file) {
		sFormat = s;
		sFile = file;
		this.barcode = barcode;
		result = encode();
	}

	private boolean encode() {
		if (sFormat.toUpperCase().compareTo("GIF") == 0) {
			return saveToGIF();
		}
		if (sFormat.toUpperCase().compareTo("JPEG") == 0) {
			return saveToJPEG();
		} else {
			return false;
		}
	}

	public BufferedImage getImage() {
		if (barcode.autoSize) {
			BufferedImage bufferedimage = new BufferedImage(100, 100, 13);
			Graphics2D graphics2d = bufferedimage.createGraphics();
			barcode.paint(graphics2d);
			barcode.invalidate();
			graphics2d.dispose();
		}
		BufferedImage bufferedimage1 = new BufferedImage(
				barcode.getSize().width, barcode.getSize().height, 13);
		Graphics2D graphics2d1 = bufferedimage1.createGraphics();
		barcode.paint(graphics2d1);

		return bufferedimage1;
	}

	private boolean saveToGIF() {
		String s = System.getProperty("java.version");
		if (s.indexOf("1.1") == 0) {
			return false;
		}
		try {
			if (barcode.autoSize) {
				BufferedImage bufferedimage = new BufferedImage(100, 100, 13);
				Graphics2D graphics2d = bufferedimage.createGraphics();
				barcode.paint(graphics2d);
				barcode.invalidate();
				graphics2d.dispose();
			}
			BufferedImage bufferedimage1 = new BufferedImage(
					barcode.getSize().width, barcode.getSize().height, 13);
			Graphics2D graphics2d1 = bufferedimage1.createGraphics();
			barcode.paint(graphics2d1);
			File file = new File(sFile);
			file.delete();
			FileOutputStream fileoutputstream = new FileOutputStream(file);
			GifEncoder gifencoder = new GifEncoder(bufferedimage1,
					fileoutputstream);
			gifencoder.encode();
			fileoutputstream.close();
		} catch (Exception exception) {
			return false;
		}
		return true;
	}

	private boolean saveToJPEG() {
		String s = System.getProperty("java.version");
		if (s.indexOf("1.1") == 0) {
			return false;
		}
		try {
			if (barcode.autoSize) {
				barcode.setSize(170, 90);
				BufferedImage bufferedimage = new BufferedImage(barcode
						.getSize().width, barcode.getSize().height, 13);
				Graphics2D graphics2d = bufferedimage.createGraphics();
				barcode.paint(graphics2d);
				barcode.invalidate();
				graphics2d.dispose();
			}
			BufferedImage bufferedimage1 = new BufferedImage(
					barcode.getSize().width, barcode.getSize().height, 1);
			Graphics2D graphics2d1 = bufferedimage1.createGraphics();
			barcode.paint(graphics2d1);
			File file = new File(sFile);
			file.delete();
			FileOutputStream fileoutputstream = new FileOutputStream(file);
			ImageIO.write(bufferedimage1, "jpeg", fileoutputstream);
			fileoutputstream.close();
		} catch (Exception exception) {
			return false;
		}
		return true;
	}
}