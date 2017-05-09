package com.skilrock.lms.coreEngine.drawGames.common;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.skilrock.lms.beans.OfflineApprovalBean;
import com.skilrock.lms.beans.ServiceRequest;
import com.skilrock.lms.beans.ServiceResponse;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.ConfigurationVariables;
import com.skilrock.lms.common.db.DBConnect;
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

public class OfflineApprovalHelper {
	public List<OfflineApprovalBean> fetchFileDetails(int agtOrgId,int[] fileId,boolean isFirstTime) {
		List<OfflineApprovalBean> beanList = new ArrayList<OfflineApprovalBean>();
		OfflineApprovalBean bean = null;
		Connection con = DBConnect.getConnection();
		String addSubQry="";
		if (isFirstTime) {
			addSubQry=" status='UPLOADED' and";
		}else{
			StringBuilder tempSb=new StringBuilder("");
			for (int fId : fileId) {
				tempSb.append(fId+",");
			}
			tempSb.deleteCharAt(tempSb.length()-1);
			addSubQry=" file_id in("+tempSb+") and";
		}
		try {
			String selQry = "select game_nbr,game_name from st_dg_game_master where is_offline='Y'";
			PreparedStatement pstmt = con.prepareStatement(selQry);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int gameNo = rs.getInt("game_nbr");
				String gameName = rs.getString("game_name");
				selQry = "select file_id,name,reference_no,retailer_user_id,upload_time,totalTicket,totalSaleValue,status from st_dg_offline_files_? of inner join st_lms_organization_master om on om.organization_id=retailer_org_id where "+addSubQry+" parent_id=?";
				PreparedStatement filePstmt = con.prepareStatement(selQry);
				filePstmt.setInt(1, gameNo);
				filePstmt.setInt(2, agtOrgId);
				ResultSet fileRs = filePstmt.executeQuery();
				while (fileRs.next()) {
					bean = new OfflineApprovalBean();
					bean.setFileId(fileRs.getInt("file_id"));
					bean.setGameName(gameName);
					bean.setGameNo(gameNo);
					bean.setRetName(fileRs.getString("name"));
					bean.setRetUserId(fileRs.getInt("retailer_user_id"));
					bean.setSaleValue(fileRs.getDouble("totalTicket"));
					bean.setTotalTkt(fileRs.getInt("totalSaleValue"));
					bean.setUploadTime(fileRs.getTimestamp("upload_time"));
					bean.setFileStatus(fileRs.getString("status"));
					bean.setFileName(fileRs.getString("reference_no"));
					beanList.add(bean);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return beanList;
	}

	public FileUploadBean uploadTktFile(int fileId, int gameNo,
			Map<Integer, Map<Integer, String>> drawIdTableMap, int uploadedBy) {
		Connection con = null;
		FileUploadBean fileUploadBean = new FileUploadBean();
		Blob fileBlob = null;
		String fileStatus = null;
		int promoGameNo = 0;
		OfflineTicketBean ticketBean = null;
		List<OfflineTicketBean> offlineTktList = null;
		Set<Integer> activeDrawIDs = null;
		Set<Integer> activePromoDrawIDs = null;
		Set<String> curDrawIds = null;
		Set<Integer> curDrawIdSet = null;
		Set<String> curPromoDrawIds = null;
		Set<Integer> curPromoDrawIdSet = null;
		try {
			con = DBConnect.getConnection();
			offlineTktList = new ArrayList<OfflineTicketBean>();

			activeDrawIDs = drawIdTableMap.get(gameNo).keySet();
			curDrawIds = new HashSet<String>();
			curDrawIdSet = new HashSet<Integer>();
			curPromoDrawIds = new HashSet<String>();
			curPromoDrawIdSet = new HashSet<Integer>();
			fileStatus = "UPLOADED";
			String selQry = "select file_id,game_no,game_id,retailer_user_id,retailer_org_id,file,filename,reference_no,status from st_dg_offline_files_? where file_id=? and status='UPLOADED'";
			PreparedStatement pstmt = con.prepareStatement(selQry);
			pstmt.setInt(1, gameNo);
			pstmt.setInt(2, fileId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				fileStatus = rs.getString("status");
				fileUploadBean.setRefNo(rs.getString("reference_no"));
				fileBlob = rs.getBlob("file");
				fileUploadBean.setFileName(rs.getString("filename"));
				fileUploadBean.setGameNo(rs.getInt("game_no"));
				fileUploadBean.setGameId(rs.getInt("game_id"));
				fileUploadBean.setRetailerOrgId(rs.getInt("retailer_org_id"));
				fileUploadBean.setRetailerUserId(rs.getInt("retailer_user_id"));

				gameNo = fileUploadBean.getGameNo();

				String gameName = Util.getGameName(gameNo);
				int startRange = 0, endRange = 0;
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

				if ("UPLOADED".equalsIgnoreCase(fileStatus)) {
					InputStream in = fileBlob.getBinaryStream();

					byte[] line = new byte[in.available()];
					in.read(line, 0, in.available());
					String fileString = new String(line);
					String recSeparator = fileString.substring(2, 3);
					fileString = fileString.substring(4);
					String dataArr[] = fileString.split(recSeparator);
					Aes encDec = new Aes("RETAILERRETAILER".toCharArray(), 128);
					for (String element : dataArr) {

						char[] decRec = encDec.decrypt(element.toCharArray());
						String str = new String(decRec).trim(); //
						System.out.println("------file on tkt data---" + str);

						String strArr[] = str.split("\\|");
						// create beans here
						if (strArr.length == 6 || strArr.length == 12) {
							fileStatus = "ERROR";
							return fileUploadBean;
						}
						ticketBean = new OfflineTicketBean();
						ticketBean.setTicketNo(strArr[0].substring(0, strArr[0]
								.length() - 2));
						ticketBean.setPurchaseTime(strArr[1]);
						ticketBean.setGameData(strArr[2]);
						ticketBean.setTktCost(Double.parseDouble(strArr[3]));
						ticketBean
								.setIsAdvancePlay(Integer.parseInt(strArr[4]));
						ticketBean.setDrawIdList(Arrays.asList(strArr[5]
								.split(","))); // draw Ids

						curDrawIds.addAll(Arrays.asList(strArr[5].split(",")));

						ticketBean.setPromoCheck(strArr[6]);
						if ("0".equalsIgnoreCase(strArr[6])) {
							OfflineTicketBean promoBean = new OfflineTicketBean();
							promoGameNo = Integer.parseInt(strArr[7].charAt(4)
									+ "");
							promoBean.setTicketNo(strArr[7].substring(0,
									strArr[7].length() - 2));
							promoBean.setPurchaseTime(strArr[8]);
							promoBean.setGameData(strArr[9]);
							promoBean
									.setTktCost(Double.parseDouble(strArr[10]));
							promoBean.setIsAdvancePlay(Integer
									.parseInt(strArr[11]));
							promoBean.setDrawIdList(Arrays.asList(strArr[12]
									.split(",")));
							promoBean.setPromoCheck("1");
							ticketBean.setPromoBean(promoBean);
							curPromoDrawIds.addAll(Arrays.asList(strArr[12]
									.split(",")));
						}
						offlineTktList.add(ticketBean);
					}

					fileStatus = OfflineRetSaleHelper.validateFileData(
							offlineTktList, gameNo, gameName, startRange,
							endRange);

					for (String drawId : curDrawIds) {
						curDrawIdSet.add(Integer.parseInt(drawId));
					}
					for (String drawId : curPromoDrawIds) {
						curPromoDrawIdSet.add(Integer.parseInt(drawId));
					}
					activePromoDrawIDs = (promoGameNo != 0) ? drawIdTableMap
							.get(promoGameNo).keySet() : null;
					if (activePromoDrawIDs != null) {
						curPromoDrawIdSet.retainAll(activePromoDrawIDs);
					}
					curDrawIdSet.retainAll(activeDrawIDs);
					if ("UPLOADED".equals(fileStatus)
							&& (curDrawIds.size() != curDrawIdSet.size() || curPromoDrawIds
									.size() != curPromoDrawIdSet.size())) {
						fileStatus = "LATE_UPLOAD";
					}

				}
				fileUploadBean.setTicketBeanList(offlineTktList);
			}

		} catch (Exception e) {
			fileStatus = "ERROR";
			e.printStackTrace();
		} finally {
			fileUploadBean.setStatus(fileStatus);
			DBConnect.closeCon(con);
		}
		return fileUploadBean;
	}

	public String mergeFile(UserInfoBean userInfoBean,
			Map<Integer, Map<Integer, String>> drawIdTableMap, int uploadedBy,
			int gameNo, int fileId, int retUserId, String refMarId,
			String purChannel) {
		Connection con = null;

		ServiceResponse sRes = new ServiceResponse();
		ServiceRequest sReq = new ServiceRequest();
		sReq.setServiceName(ServiceName.DRAWGAME);
		sReq.setServiceMethod(ServiceMethodName.INSERT_QUERY_DGE);
		IServiceDelegate delegate = ServiceDelegate.getInstance();

		String fileStatus = null;
		UserInfoBean retUserBean = new UserInfoBean();
		retUserBean.setUserId(retUserId);
		retUserBean = DrawGameOfflineHelper.fillUserBeanData(retUserBean);
		FileUploadBean fileUploadBean = null;
		StringBuilder result = new StringBuilder("");
		try {
			con = DBConnect.getConnection();
			con.setAutoCommit(false);
			fileUploadBean = uploadTktFile(fileId, gameNo, drawIdTableMap,
					uploadedBy);
			fileStatus = fileUploadBean.getStatus();
			if ("UPLOADED".equalsIgnoreCase(fileUploadBean.getStatus())) {
				Connection newCon = DBConnect.getConnection();
				newCon.setAutoCommit(false);

				OfflineRetSaleHelper.saleBalDeductionOffline(retUserBean,
						gameNo, fileUploadBean.getTicketBeanList(), newCon);

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
					fileUploadBean.setStatus("SUCCESS");
					newCon.commit();
					newCon.close();
				} else {
					fileUploadBean.setStatus("ERROR");
					newCon.rollback();
					newCon.close();
					// System.out.println("Mail for upload: ERROR");
					OfflineRetSaleHelper.sendMailToBo(fileUploadBean.getFile(),
							"" + fileUploadBean.getStatus(), fileUploadBean
									.getFileName());
				}

			} else if ("LATE_UPLOAD".equalsIgnoreCase(fileStatus)) {
				System.out.println("Mail for upload: LATE_UPLOAD");
				OfflineRetSaleHelper.sendMailToBo(fileUploadBean.getFile(),
						fileUploadBean.getStatus(), fileUploadBean
								.getFileName());
			}
			String updateQuery = "update st_dg_offline_files_? set status='"
					+ fileUploadBean.getStatus() + "',updateby="
						+ userInfoBean.getUserId()
						+ ",updatetime='"
						+ new Timestamp(new Date().getTime())
						+ "' where reference_no=?";
			PreparedStatement pstmt = con.prepareStatement(updateQuery);
			pstmt.setInt(1, gameNo);
			pstmt.setString(2, fileUploadBean.getRefNo());
			pstmt.executeUpdate();

			result.append(fileUploadBean.getFileName());
			result.append(":");
			result.append(fileUploadBean.getRefNo());
			result.append(":");
			result.append(fileUploadBean.getStatus());
			result.append("|");
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return result.toString();

	}
}
