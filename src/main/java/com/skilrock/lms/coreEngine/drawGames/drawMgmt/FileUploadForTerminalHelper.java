package com.skilrock.lms.coreEngine.drawGames.drawMgmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.utility.PropertyLoader;

public class FileUploadForTerminalHelper {

	public String uploadFile(String deviceType, String profileName,
			String itemNames, ArrayList<File> sgnFileUploader,
			ArrayList<File> adfFileUploader, ArrayList<File> agnFileUploader,
			ArrayList<String> sgnFileUploaderFileName,
			ArrayList<String> adfFileUploaderFileName,
			ArrayList<String> agnFileUploaderFileName, String version,
			int userId, String isMandatory, String status,String filePath) throws IOException,
			SQLException {

		String deviceTypes = deviceType
				.substring(deviceType.lastIndexOf('-') + 1);
		String deviceId = deviceType.substring(0, 1).trim();
		String[] itemInfo = itemNames.split("-");
		String outFilePath = filePath+ deviceTypes + "/" + profileName.toLowerCase() + "/";
		System.out.println(outFilePath);
		String errorMessage = "PROBLEM";
		System.out.println("OUT FILE PATH..." + outFilePath);
		File files = new File(outFilePath + version.trim());
		if (files.mkdirs()) {
			System.out.println("Version Directory Has Been Created....");
		} else {
			System.out.println("Version Directory Has not Been Created....");
		}

		if (deviceTypes.equalsIgnoreCase("EFT930G")) {
			errorMessage = eftFileUploader(outFilePath, deviceTypes, deviceId,
					itemInfo, profileName, sgnFileUploader.get(0),
					adfFileUploader.get(0), sgnFileUploaderFileName.get(0),
					adfFileUploaderFileName.get(0), version, userId,
					isMandatory, status);
		}
		if (deviceTypes.equalsIgnoreCase("iCT220")
				|| deviceTypes.equalsIgnoreCase("TPS300")
				|| deviceTypes.equalsIgnoreCase("TPS800")
				|| deviceTypes.equalsIgnoreCase("iWL220")) {

			errorMessage = ictFileUploader(outFilePath, deviceTypes, deviceId,
					itemInfo, profileName, agnFileUploader.get(0),
					agnFileUploaderFileName.get(0), version, userId,
					isMandatory, status);
		}
		return errorMessage;
	}

	public String eftFileUploader(String outFilePath, String deviceType,
			String deviceId, String[] itemInfo, String profileName,
			File sgnFile, File adfFile, String sgnFileName, String adfFileName,
			String version, int userId, String isMandatory, String status)
			throws IOException, SQLException {

		String errorMessage = "Failed To Upload";
		Connection con = null;
		PreparedStatement pstm = null;
		int rowspdated;
		long sgnFileSize = sgnFile.length();
		long adfFileSize = adfFile.length();
		String insertQuery = null;
		boolean flag;

		// Uploading the file in directory...
		try {
			
			flag=terminalFilesUploader(sgnFile,outFilePath,version,sgnFileName);
			System.out.println("TEST FOR SGN..."+flag);

			flag=terminalFilesUploader(adfFile,outFilePath,version,adfFileName);
			System.out.println("TEST FOR ADF..."+flag);
			
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			// Updating the Data Base
			insertQuery = "insert into st_lms_htpos_download (device_id,item_id,item_name,version,isMandatory,fileSize,fileSize_adf,updated_date,profile,status,updatedBy) values(?,?,?,?,?,?,?,?,?,?,?)";
			pstm = con.prepareStatement(insertQuery);
			pstm.setDouble(1, Double.parseDouble(deviceId));
			pstm.setDouble(2, 0);
			pstm.setString(3, itemInfo[1]);
			pstm.setString(4, version);
			pstm.setString(5,
					(isMandatory.equalsIgnoreCase("1") ? "NO" : "YES"));
			pstm.setString(6, String.valueOf(sgnFileSize));
			pstm.setString(7, String.valueOf(adfFileSize));
			pstm.setDate(8, new Date(System.currentTimeMillis()));
			pstm.setString(9, profileName);
			pstm.setString(10, status);
			pstm.setInt(11, userId);
			rowspdated = pstm.executeUpdate();
			System.out.println("ROWS UPDATED...." + rowspdated);
			con.commit();
			errorMessage = "success";
			return errorMessage;
		} catch (SQLException e) {
			System.err.println("FileStreamsTdest: " + e);
			errorMessage = e.getMessage();
		} finally {
			DBConnect.closeCon(con);
		}
		return errorMessage;
	}

	public String ictFileUploader(String outFilePath, String deviceType,
			String deviceId, String[] itemInfo, String profileName,
			File agnFile, String agnFileName, String version, int userId,
			String isMandatory, String status) throws IOException, SQLException {

		String errorMessage = "Failed To Upload";
		Connection con = null;
		PreparedStatement pstm = null;
		int rowspdated;
		long agnFileSize = agnFile.length();
		boolean flag;
		String insertQuery = "insert into st_lms_htpos_download (device_id,item_id,item_name,version,isMandatory,fileSize,fileSize_adf,updated_date,profile,status,updatedBy) values(?,?,?,?,?,?,?,?,?,?,?)";

		// Uploading File in directory
		try {
			flag=terminalFilesUploader(agnFile,outFilePath,version,agnFileName);
			System.out.println("TEST..."+flag);
			// Updating database
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			pstm = con.prepareStatement(insertQuery);
			pstm.setDouble(1, Double.parseDouble(deviceId));
			pstm.setDouble(2, 0);
			pstm.setString(3, itemInfo[1]);
			pstm.setString(4, version);
			pstm.setString(5,
					(isMandatory.equalsIgnoreCase("1") ? "NO" : "YES"));
			pstm.setString(6, String.valueOf(agnFileSize));
			pstm.setString(7, String.valueOf(-1));
			pstm.setDate(8, new Date(System.currentTimeMillis()));
			pstm.setString(9, profileName);
			pstm.setString(10, status);
			pstm.setInt(11, userId);
			rowspdated = pstm.executeUpdate();
			System.out.println("ROWS UPDATED...." + rowspdated);
			con.commit();
			errorMessage = "success";
			return errorMessage;
		} catch (SQLException e) {
			System.err.println("IOException: " + e);
			errorMessage = e.getMessage();
		} finally {
			DBConnect.closeCon(con);
		}
		return errorMessage;
	}

	public boolean terminalFilesUploader(File agnFile, String outFilePath,
			String version, String agnFileName) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		boolean flag=true;
		try {
			fis = new FileInputStream(agnFile);
			fos = new FileOutputStream(outFilePath + version.trim() + "/"
					+ agnFileName);
			int c;
			while ((c = fis.read()) != -1) {
				fos.write(c);
			}
			System.out.println("FILE UPLOADED SUCCESSFULLY ...");
			System.out.println(new Date(System.currentTimeMillis()));
		} catch (Exception e) {
			flag=false;
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public ArrayList<String> getProFileNameList() {

		String query = "select distinct profile from st_lms_htpos_download";
		ArrayList<String> profileList = new ArrayList<String>();
		try {
			Connection con = DBConnect.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				profileList.add(rs.getString("profile"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profileList;
	}

	public ArrayList<String> getDeviceTypeList() {

		String query = "select distinct device_id,device_type from st_lms_htpos_device_master";
		ArrayList<String> deviceList = new ArrayList<String>();
		try {
			Connection con = DBConnect.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				deviceList.add(rs.getString("device_id").trim().concat("-")
						.concat(rs.getString("device_type").trim()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceList;
	}

	public ArrayList<String> getItemNames() {
		String query = "select distinct item_id ,item_name from st_lms_htpos_download";
		ArrayList<String> deviceList = new ArrayList<String>();
		try {
			Connection con = DBConnect.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);

			while (rs.next()) {
				deviceList.add(rs.getString("item_id").trim().concat("-")
						.concat(rs.getString("item_name").trim()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceList;
	}

	public String getTerminalVersion(String deviceType, String profileName) {
		String query = "select max(version) version from st_lms_htpos_download where device_id=? and profile=?";
		String deviceId = deviceType.substring(0, 1).trim();

		String currentVersion = null;
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement pstm = con.prepareStatement(query);
			pstm.setString(1, deviceId);
			pstm.setString(2, profileName);
			ResultSet rs = pstm.executeQuery();

			while (rs.next()) {
				currentVersion = rs.getString("version");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return currentVersion.trim();
		}
		if (currentVersion == null) {
			currentVersion = "Not available";
		}
		return currentVersion.trim();

	}

}
