package com.skilrock.lms.coreEngine.drawGames.drawMgmt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.Aes;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.DrawGameOfflineHelper;
import com.skilrock.lms.coreEngine.drawGames.playMgmt.OfflineRetSaleHelper;
import com.skilrock.lms.coreEngine.service.IServiceDelegate;
import com.skilrock.lms.coreEngine.service.ServiceDelegate;
import com.skilrock.lms.coreEngine.service.dge.ServiceMethodName;
import com.skilrock.lms.coreEngine.service.dge.ServiceName;
import com.skilrock.lms.dge.beans.FileUploadBean;
import com.skilrock.lms.dge.beans.OfflineTicketBean;
import com.skilrock.lms.web.drawGames.common.Util;

public class OfflineBOFileProcessHelper {
	static Log logger = LogFactory.getLog(OfflineBOFileProcessHelper.class);

	public static FileUploadBean parseFile(int gameNo, String fileRefId) {
		Connection con = null;
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		String selectQuery = null;
		FileUploadBean fileUploadBean = new FileUploadBean();
		fileUploadBean.setRefNo(fileRefId);
		Blob file = null;
		InputStream in = null;
		BufferedReader reader = null;
		String fileString = null;
		String recSeparator = null;
		String fileStatus = null;
		OfflineTicketBean ticketBean = null;
		List<OfflineTicketBean> offlineTktList = new ArrayList<OfflineTicketBean>();
		Set<String> curDrawIds = new HashSet<String>();
		try {
			int startRange = 0, endRange = 0;
			String gameName = Util.getGameName(gameNo);
			if ("LOTTO".equalsIgnoreCase(gameName)) {
				startRange = ConfigurationVariables.LOTTO_START_RANGE;
				endRange = ConfigurationVariables.LOTTO_END_RANGE;
			} else if ("FASTLOTTO".equalsIgnoreCase(gameName)) {
				startRange = ConfigurationVariables.FASTLOTTO_START_RANGE;
				endRange = ConfigurationVariables.FASTLOTTO_END_RANGE;
			} else if ("ZIMLOTTO".equalsIgnoreCase(gameName)) {
				startRange = ConfigurationVariables.ZIMLOTTO_START_RANGE;
				endRange = ConfigurationVariables.ZIMLOTTO_END_RANGE;
			} else if ("ZIMLOTTOTWO".equalsIgnoreCase(gameName)) {
				startRange = ConfigurationVariables.ZIMLOTTOTWO_START_RANGE;
				endRange = ConfigurationVariables.ZIMLOTTOTWO_END_RANGE;
			} else if ("KENO".equalsIgnoreCase(gameName)) {
				startRange = ConfigurationVariables.KENO_START_RANGE;
				endRange = ConfigurationVariables.KENO_END_RANGE;
			} else if ("KENOTWO".equalsIgnoreCase(gameName)) {
				startRange = ConfigurationVariables.KENOTWO_START_RANGE;
				endRange = ConfigurationVariables.KENOTWO_END_RANGE;
			}

			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			selectQuery = "select file from st_dg_offline_files_? where reference_no=?";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setInt(1, gameNo);
			pstmt.setString(2, fileRefId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				file = rs.getBlob("file");
			}
			//logger.debug("file::" + file.toString());
			in = file.getBinaryStream();
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

			byte[] line = new byte[in.available()];
			in.read(line, 0, in.available());
			fileString = new String(line); //
			//logger.info("FileString ::" + fileString);

			recSeparator = fileString.substring(2, 3);
			fileString = fileString.substring(4);
			String dataArr[] = fileString.split(recSeparator);
			//logger.info("dataArr ::" + dataArr.length);
			Aes encDec = new Aes("RETAILERRETAILER".toCharArray(), 128);

			
			for (String element : dataArr) {

				char[] decRec = encDec.decrypt(element.toCharArray());
				String str = new String(decRec).trim();
				
				String strArr[] = str.split("\\|");
				// create beans here
				ticketBean = new OfflineTicketBean();
				ticketBean.setTicketNo(strArr[0].substring(0, strArr[0]
						.length() - 2));
				ticketBean.setPurchaseTime(strArr[1]);
				ticketBean.setGameData(strArr[2]);
				ticketBean.setTktCost(Double.parseDouble(strArr[3]));
				ticketBean.setIsAdvancePlay(Integer.parseInt(strArr[4]));
				ticketBean.setDrawIdList(Arrays.asList(strArr[5].split(","))); // draw
				// Ids
				curDrawIds.addAll(ticketBean.getDrawIdList());
				ticketBean.setPromoCheck(strArr[6]);
				if ("0".equalsIgnoreCase(strArr[6])) {
					OfflineTicketBean promoBean = new OfflineTicketBean();
					promoBean.setTicketNo(strArr[7].substring(0, strArr[7]
							.length() - 2));
					promoBean.setPurchaseTime(strArr[8]);
					promoBean.setGameData(strArr[9]);
					promoBean.setTktCost(Double.parseDouble(strArr[10]));
					promoBean.setIsAdvancePlay(Integer.parseInt(strArr[11]));
					promoBean.setDrawIdList(Arrays
							.asList(strArr[12].split(",")));
					promoBean.setPromoCheck("NO");
					ticketBean.setPromoBean(promoBean);
				}
				offlineTktList.add(ticketBean);
			}

			fileStatus = OfflineRetSaleHelper.validateFileData(offlineTktList,
					gameNo, gameName, startRange, endRange);
			fileUploadBean.setDrawIdSet(curDrawIds);
			fileUploadBean.setTicketBeanList(offlineTktList);
		} catch (FileNotFoundException e) {
			// logger.error("Exception : " + e);
			fileStatus = "ERROR";
			e.printStackTrace();
		} catch (SQLException e) {
			// logger.error("Exception : " + e);
			fileStatus = "ERROR";
			e.printStackTrace();
		} catch (Exception e) {
			// logger.error("Exception : " + e);
			fileStatus = "ERROR";
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (IOException e) {
				// logger.error("Exception : " + e);
				e.printStackTrace();
			} catch (SQLException e) {
				// logger.error("Exception : " + e);
				e.printStackTrace();
			}
		}
		fileUploadBean.setStatus(fileStatus);

		return fileUploadBean;
	}

	public String declineFile(String fileRefId, int gameNo, int userId) {
		Connection con = null;
		PreparedStatement pstmt = null;

		String updateQuery = null;
		String fileStatus = "DECLINED";
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			updateQuery = "update st_dg_offline_files_? set status=?,updateby=?,updatetime=? where reference_no=?";
			pstmt = con.prepareStatement(updateQuery);
			pstmt.setInt(1, gameNo);
			pstmt.setString(2, fileStatus);
			pstmt.setInt(3, userId);
			pstmt.setTimestamp(4, new Timestamp(new Date().getTime()));
			pstmt.setString(5, fileRefId);
			pstmt.executeUpdate();
			con.commit();
		} catch (Exception e) {
			// logger.error("Exception : " + e);
			fileStatus = "ERROR";
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return fileStatus;
	}

	public Map<String, List<FileUploadBean>> featchRetFileUploadStatus(
			int retUserId) {
		Connection con = null;
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		String selectQuery = null;
		Map<String, List<FileUploadBean>> retFileMap = new HashMap<String, List<FileUploadBean>>();
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			selectQuery = "select game_nbr,game_name from st_dg_game_master where is_offline='Y' order by game_nbr";
			pstmt = con.prepareStatement(selectQuery);
			rs = pstmt.executeQuery();
			int gameNo;
			String gameName = null;
			List<FileUploadBean> fileList = null;
			FileUploadBean fileUploadBean = null;
			while (rs.next()) {
				fileList = new ArrayList<FileUploadBean>();
				gameNo = rs.getInt("game_nbr");
				gameName = rs.getString("game_name");
				selectQuery = "select filename,upload_time,status,reference_no from st_dg_offline_files_? where retailer_user_id=? and status not in('SUCCESS','APPROVED','DECLINED','EMPTY')";
				pstmt = con.prepareStatement(selectQuery);
				pstmt.setInt(1, gameNo);
				pstmt.setInt(2, retUserId);
				logger.info("---get file qry--" + pstmt);
				ResultSet rsFile = pstmt.executeQuery();
				while (rsFile.next()) {
					fileUploadBean = new FileUploadBean();
					fileUploadBean.setRetailerUserId(retUserId);
					fileUploadBean.setGameNo(gameNo);
					fileUploadBean.setFileName(rsFile.getString("filename"));
					fileUploadBean.setUploadTime(rsFile
							.getTimestamp("upload_time"));
					fileUploadBean.setStatus(rsFile.getString("status"));
					fileUploadBean.setRefNo(rsFile.getString("reference_no"));
					fileList.add(fileUploadBean);
				}
				retFileMap.put(gameNo + "-" + gameName, fileList);
			}

			con.commit();
		} catch (SQLException e) {
			// logger.error("Exception : " + e);
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// logger.error("Exception : " + e);
				e.printStackTrace();
			}
		}

		return retFileMap;
	}

	public String mergeFile(String fileRefId, String status, int retUserId,
			int gameNo, int userId, String refMarId, String purChannel) {
		Connection con = null;
		PreparedStatement pstmt = null;

		ResultSet rs = null;
		String selectQuery = null;

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq.setServiceMethod(ServiceMethodName.INSERT_QUERY_DGE);
		IServiceDelegate delegate = ServiceDelegate.getInstance();

		String fileStatus = null;
		UserInfoBean retUserBean = null;
		FileUploadBean fileUploadBean = null;
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			fileUploadBean = parseFile(gameNo, fileRefId);
			fileStatus=fileUploadBean.getStatus();
			if ("UPLOADED".equalsIgnoreCase(fileUploadBean.getStatus())) {
			
			selectQuery = "select om.organization_id,om.organization_type,om.parent_id from st_lms_organization_master om,st_lms_user_master um where om.organization_id=um.organization_id and um.user_id=?";
			pstmt = con.prepareStatement(selectQuery);
			pstmt.setInt(1, retUserId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				retUserBean = new UserInfoBean();
				retUserBean.setUserId(retUserId);
				retUserBean.setUserOrgId(rs.getInt("organization_id"));
				retUserBean.setUserType(rs.getString("organization_type"));
				retUserBean.setParentOrgId(rs.getInt("parent_id"));
			} else {
				throw new LMSException("This user is invalid");
			}
			Connection newCon = DBConnect.getConnection();
			newCon.setAutoCommit(false);

			// drawGameQryMap = DrawGameOfflineHelper
			// .drawTcketSaleBalDeductionOffline(retUserBean, gameNo,
			// ticketAmtMap, list, drawIdSet, newCon);
			// logger.debug("*******drawGameQryMap" + drawGameQryMap);
			OfflineRetSaleHelper.saleBalDeductionOffline(retUserBean, gameNo,
					fileUploadBean.getTicketBeanList(), newCon);

			List<Object> offDgeData = new ArrayList<Object>();
			offDgeData.add(gameNo);
			offDgeData.add(fileUploadBean.getTicketBeanList());
			offDgeData.add(retUserBean.getUserOrgId() + ","
					+ retUserBean.getUserId() + ",'"
					+ retUserBean.getUserType() + "'");
			offDgeData.add("'" + refMarId + "','" + purChannel + "'");

			sReq.setServiceData(offDgeData);

			// sReq.setServiceData(drawGameQryMap);

			delegate = ServiceDelegate.getInstance();
			sRes = delegate.getResponse(sReq);
			if (sRes.getIsSuccess()) {
				fileStatus = "APPROVED";
				newCon.commit();
				newCon.close();
				// Reperform Draw
				logger.debug("*********Reperform Draw");
				sReq
						.setServiceMethod(ServiceMethodName.REPERFORM_DRAW_FOROFFLINE);
				fileUploadBean.setGameNo(gameNo);
				sReq.setServiceData(fileUploadBean);
				delegate = ServiceDelegate.getInstance();
				sRes = delegate.getResponse(sReq);
				if (sRes.getIsSuccess()) {
					fileStatus = "APPROVED";
					logger
							.debug("*****Reperform Draw"
									+ sRes.getResponseData());
				} else {
					fileStatus = "ERROR";
					DrawGameOfflineHelper
							.sendMailToBo(
									fileUploadBean.getFile(),
									"File Merge Successfully!! But Error in Reperform Draw!!",
									fileUploadBean.getFileName());
				}
			} else {
				fileStatus = "ERROR";
				newCon.rollback();
				newCon.close();
				DrawGameOfflineHelper.sendMailToBo(fileUploadBean.getFile(),
						"Error At BO Merge File Proccess !!", fileUploadBean
								.getFileName());
			}
			logger.debug("*********After DGE QUERY File Status" + fileStatus);
			}
		} catch (SQLException e) {
			// logger.error("Exception : " + e);
			fileStatus = "ERROR";
			e.printStackTrace();
		} catch (Exception e) {
			// logger.error("Exception : " + e);
			fileStatus = "ERROR";
			e.printStackTrace();
		} finally {
			try {
				String updateQuery = "update st_dg_offline_files_? set status='"
						+ fileStatus
						+ "',updateby="
						+ userId
						+ ",updatetime='"
						+ new Timestamp(new Date().getTime())
						+ "' where reference_no=?";
				pstmt = con.prepareStatement(updateQuery);
				pstmt.setInt(1, gameNo);
				pstmt.setString(2, fileRefId);
				pstmt.executeUpdate();
				con.commit();

				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				// logger.error("Exception : " + e);
				e.printStackTrace();
			}
		}

		return fileStatus;

	}
}
