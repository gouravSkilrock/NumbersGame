package com.skilrock.lms;

import java.io.File;

import net.sf.jasperreports.engine.JasperCompileManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JasperCompiler {
	public static Log logger = LogFactory.getLog(JasperCompiler.class);

	public static void main(String[] args) {
		try {
			String str = "c:\\Jasper\\"; // Source Path of JRXML file
			File file = new File(str);

			if (file.exists() && file.isDirectory()) {
				// JasperReport jasperReport = null;
				int i = 0;

				for (File childFile : file.listFiles()) {
					i += 1;
					// JasperCompileManager.compileReportToFile(Source
					// Path+fileName,Destination Path+targetFileName+.jasper) ;
					JasperCompileManager.compileReportToFile("c:\\Jasper\\"
							+ childFile.getName(), "c:\\CompliedReports\\"
							+ targetFileName(childFile.getName()) + ".jasper");

				}
				logger.debug("total file compiled: " + i);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static String targetFileName(String fileName) {
		logger.debug(fileName);
		String fileNameArr = fileName.substring(0, fileName.lastIndexOf("."));
		logger.debug(fileNameArr);
		return fileNameArr;
	}
}
