package com.skilrock.lms.common.utility;

import java.io.File;
import java.util.Date;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BarcodeImageDelete extends Thread {
	static Log logger = LogFactory.getLog(BarcodeImageDelete.class);

	public static String path = "";

	public static void main(String[] args) {
		BarcodeImageDelete coll = new BarcodeImageDelete(null);
		coll.start();

	}

	public static void readFiles() {
		File foundFile = null;
		String files[] = new File(path).list();
		for (String element : files) {
			foundFile = new File(path + "\\" + element);
			if (new Date().getTime() - foundFile.lastModified() > 2 * 60 * 1000) {
				foundFile.delete();
			}
		}
	}

	public BarcodeImageDelete(ServletContext servletContext) {
		path = servletContext.getRealPath("/barcode");
		// path =
		// "D:\\jboss-4.2.2.GA\\server\\default\\deploy\\LMSLinuxNew.war\\barcode";

	}

	@Override
	public void run() {

		logger.debug("Delete Barcode Images start....");
		readFiles();
		try {
			Thread.sleep(5 * 60 * 1000);
			run();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
