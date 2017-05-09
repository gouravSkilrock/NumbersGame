package com.skilrock.itg.IDBarcode;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import org.apache.struts2.ServletActionContext;

public class IDBarcode {
	public static BufferedImage getBarcode(String data) {
		Linear linear = new Linear();
		linear.barcode = data;
		linear.barcodeType = Linear.CODE128;
		linear.X = 0.03;
		linear.leftMarginCM = 0.1;
		linear.topMarginCM = 0.1;
		linear.barColor = Color.black;
		linear.textFontColor = Color.black;
		linear.backgroundColor = Color.white;
		linear.textFont = new Font("Arial", 0, 12);
		linear.Code128Set = '0';
		linear.barHeightCM = 0.8;
		linear.showText = true;
		linear.processTilde = true;

		linear.textMarginCM = 0.1;
		linear.whiteBarIncrease = 0;
		linear.bearerBarHori = 0;
		linear.bearerBarVert = 0;
		linear.createBarcodeImage(ServletActionContext.getServletContext()
				.getRealPath("/")
				+ "barcode/" + data + ".jpg");

		// logger.debug(linear.barcode+ServletActionContext.getServletContext().getRealPath("/"));
		BarcodeEncoder barCodeEncoder = new BarcodeEncoder(linear);
		return barCodeEncoder.getImage();
	}

	public static void main(String[] args) {
		getBarcode("10431120000001256");
		// getBarcode("1234567891113456");
	}
}
