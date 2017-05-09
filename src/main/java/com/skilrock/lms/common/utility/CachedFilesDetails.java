package com.skilrock.lms.common.utility;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CachedFilesDetails {
	private static List<String> duplicateFile = new ArrayList<String>();

	static Log logger = LogFactory.getLog(CachedFilesDetails.class);
	private static Set<String> tempSet = new TreeSet<String>();
	private static List withoutCodeTag = new ArrayList();

	public static void getCachedFiles(ServletContext servletContext) {
		File src = new File(servletContext.getRealPath("/com"));
		logger.debug("Source path---" + src);
		CachedFilesDetails cachedDetails = new CachedFilesDetails();
		try {
			cachedDetails.checkFiles(src);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger
					.info("Error In File*****************************************Server Stopped");
			e.printStackTrace();
		}
		// logger.debug( "Total Number of Jsp Files"+
		// cachedDetails.totalJspFileList.size()+"\n--List"+cachedDetails.totalJspFileList);
		// logger.debug( "Total Number of Cached Jsp Files"+
		// cachedDetails.cachedJspFileList.size()+"\n--List"+cachedDetails.cachedJspFileList);
		logger.debug(cachedDetails.cachedFileMap + ""
				+ cachedDetails.cachedFileMap.size());
		servletContext.setAttribute("CACHED_FILES_MAP",
				cachedDetails.cachedFileMap);
		logger.debug("\n\n Duplicate File Names :========== \n" + duplicateFile
				+ "");
		Collections.sort(duplicateFile);
		if (!withoutCodeTag.isEmpty() || !duplicateFile.isEmpty()) {
			logger.info("The Files " + withoutCodeTag
					+ " does not contains code Tag");
			logger.info("\n\n Duplicate File Names :========== \n"
					+ duplicateFile + "");
			// System.exit(0);
		}

	}

	public static void main(String[] args) {
		CachedFilesDetails details = new CachedFilesDetails();
		try {
			// details
			// .checkFiles(new File(
			// "D:/WorkSpace_Consolidate/jboss-4.2.2.GA/server/default/./deploy/LMSLinuxNew.war/com/skilrock/lms/web/scratchService/inventoryMgmt/retailer/ret_im_activateBooks_Menu.jsp"));
			details
					.checkFiles(new File(
							"D:/WorkSpace_Consolidate/jboss-4.2.2.GA/server/default/./deploy/LMSLinuxNew.war/com/skilrock/lms/web/scratchService/inventoryMgmt/retailer/js/ret_invMgmt_bookWiseDtls.js"));

		} catch (Exception e) {
			logger.debug("--main");
			e.printStackTrace();
		}
	}

	Map cachedFileMap = new HashMap();

	List cachedJspFileList = new ArrayList();

	List totalJspFileList = new ArrayList();

	/*
	 * public static void main(String[] args) { File src = new
	 * File("D:\\jboss-4.2.2.GA\\server\\default\\.\\deploy\\LMSLinuxNew.war\\com");
	 * CachedFilesDetails t = new CachedFilesDetails(); try { t.readFiles(src); }
	 * catch (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } logger.debug( "Total Number of Jsp Files"+
	 * t.totalJspFileList.size()+"\n--List"+t.totalJspFileList); logger.debug(
	 * "Total Number of Cached Jsp Files"+
	 * t.cachedJspFileList.size()+"\n--List"+t.cachedJspFileList); logger.debug(
	 * t.cachedFileMap+""+t.cachedFileMap.size()); }
	 */

	public void checkFiles(File srcPath) throws Exception {
		try {
			if (srcPath.isDirectory()) {
				String files[] = srcPath.list();
				for (String element : files) {
					checkFiles(new File(srcPath, element));
				}
			} else {
				String Path = srcPath.getAbsolutePath();
				String newPath = Path.replace('\\', '/');
				if (newPath.contains("js")) {
					FileInputStream in = new FileInputStream(srcPath);
					byte[] line = new byte[in.available()];
					in.read(line, 0, in.available());
					String fileString = new String(line);
					// logger.debug(fileString.replaceAll("\r\n",""));
					fileString = fileString.replaceAll("\r\n", "");
					// String strLine = "";
					boolean isCachable = false;
					boolean hasCodeTag = false;
					boolean hasSourceTag = false;
					String key = "";
					String version = "";
					String value = "";

					// while loop previously

					if (fileString.contains("response.setDateHeader")) {
						isCachable = true;
					}

					if (fileString.contains("</code>")) {
						hasCodeTag = true;
						String strLine = fileString.substring(fileString
								.indexOf("<code"), fileString
								.indexOf("</code>"));
						if (isCachable || newPath.contains("Header.jsp")) {
							cachedJspFileList.add(newPath);
							key = strLine.substring(
									strLine.indexOf("RCSfile:") + 8,
									strLine.indexOf("jsp") + 3).trim();
							version = strLine.substring(
									strLine.indexOf("Revision:") + 9,
									strLine.lastIndexOf("$") - 1).trim();
							cachedFileMap.put(key, version);
						}
					} else if (fileString.contains("$Source:")) {
						String strLine = fileString.substring(fileString
								.indexOf("$Source:"));
						// logger.debug(strLine);
						hasSourceTag = true;
						cachedJspFileList.add(newPath);
						key = strLine.substring(strLine.indexOf("/com"),
								strLine.lastIndexOf(",v $") - 3);
						version = strLine.substring(strLine
								.lastIndexOf(".js,v") + 5, strLine
								.lastIndexOf("/"));
						value = version.substring(0,
								version.lastIndexOf("/") - 4);
						// logger.debug
						// (key+"*********"+version+"*****JS*****"+value);
						cachedFileMap.put(key, value.trim());

					}
					// check the duplicate name of JSP's
					if (fileString.contains("</code>")) {
						String strLine = fileString.substring(
								fileString.indexOf("<code"),
								fileString.indexOf("</code>"))
								.replace("\n", "");
						// logger.debug(strLine);
						key = strLine.substring(
								strLine.indexOf("$RCSfile:") + 9,
								strLine.indexOf("jsp") + 3).trim();
						boolean isFileDuplicate = tempSet.add(key.trim());
						// logger.debug(isFileDuplicate+" == files
						// ==="+key.trim()+"===");
						if (!isFileDuplicate) {
							// logger.debug(isFileDuplicate+"duplicate file
							// == "+key);
							duplicateFile.add(key + "=" + srcPath);
						}
					}

					if (!(hasCodeTag || hasSourceTag)) {
						if (!newPath.contains("menu.jsp")
								&& !newPath.contains(".css")) {
							withoutCodeTag.add(newPath);
						}
					}
					in.close();

				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger
					.info("Error In File*****************************************"
							+ srcPath);
			e.printStackTrace();
			throw new Exception();
		}
	}

	public List readFiles(File srcPath) throws FileNotFoundException {
		if (srcPath.isDirectory()) {
			String files[] = srcPath.list();
			for (String element : files) {
				readFiles(new File(srcPath, element));
			}
		} else {
			String Path = srcPath.getAbsolutePath();

			String newPath = Path.replace('\\', '/');
			if (newPath.contains("js")) {
				try {

					FileInputStream fstream = new FileInputStream(newPath);

					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in));
					String strLine = "";
					boolean isCachable = false;
					boolean hasCodeTag = false;
					boolean hasSourceTag = false;
					String key = "";
					String version = "";
					String value = "";

					while ((strLine = br.readLine()) != null) {
						if (strLine.contains("response.setDateHeader")) {
							isCachable = true;
						}

						if (strLine.contains("</code>")) {
							hasCodeTag = true;
							if (isCachable || newPath.contains("Header.jsp")) {
								cachedJspFileList.add(newPath);
								key = strLine.substring(
										strLine.indexOf("Id:") + 3, strLine
												.indexOf("jsp") + 3);
								version = strLine.substring(strLine
										.indexOf(",v") + 2, strLine
										.lastIndexOf("/") - 1);
								value = version.substring(0, version
										.indexOf("/") - 4);
								// logger.debug (key+"--"+version+"**"+value);
								cachedFileMap.put(key.trim(), value.trim());
							}
						} else if (strLine.contains("$Source:")) {

							hasSourceTag = true;
							cachedJspFileList.add(newPath);
							key = strLine.substring(strLine.indexOf("/com"),
									strLine.lastIndexOf("v $'") - 4);
							version = strLine.substring(strLine
									.lastIndexOf(".js,v") + 5, strLine
									.lastIndexOf("/"));
							value = version.substring(0, version
									.lastIndexOf("/") - 4);
							// logger.info(key+"*********"+version+"*****JS*****"+value);
							cachedFileMap.put(key, value.trim());

						}
						// check the duplicate name of JSP's
						if (strLine.contains("</code>")) {
							key = strLine.substring(strLine.indexOf("Id:") + 3,
									strLine.indexOf("jsp") + 3);
							boolean isFileDuplicate = tempSet.add(key.trim());
							logger.info(isFileDuplicate + " == files ==="
									+ key.trim() + "===");
							if (!isFileDuplicate) {
								duplicateFile.add(key);
							}
						}

					}

					if (!(hasCodeTag || hasSourceTag)) {
						if (!newPath.contains("menu.jsp")
								&& !newPath.contains(".css")) {
							withoutCodeTag.add(newPath);
						}
					}
					in.close();
				} catch (Exception e) {// Catch exception if any
					logger.info("Err File*******************" + newPath);
					e.printStackTrace();
					withoutCodeTag.add(newPath);
				}

				totalJspFileList.add(newPath);
			}

		}

		return totalJspFileList;
	}

}
