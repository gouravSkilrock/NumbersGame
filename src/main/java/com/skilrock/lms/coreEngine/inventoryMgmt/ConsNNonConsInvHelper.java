package com.skilrock.lms.coreEngine.inventoryMgmt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.utils.DBConnectionManager;

import com.skilrock.lms.api.lmsWrapper.common.WrapperUtility;
import com.skilrock.lms.beans.ConsNNonConsBean;
import com.skilrock.lms.beans.InvDetailBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.db.TableConstants;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.common.exception.LMSErrors;

public class ConsNNonConsInvHelper {

	static Log logger = LogFactory.getLog(ConsNNonConsInvHelper.class);

	public String addNewBrandDetails(int[] invIdForBrand,
			String[] newBrandName, String[] newBrandDesc) throws LMSException {

		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		logger.debug("\n newBrandName = " + newBrandName + "\n inv Id = "
				+ invIdForBrand + "\n newBrandDesc = " + newBrandDesc);

		if (newBrandName != null && newBrandDesc != null
				&& invIdForBrand != null) {

			logger.debug(" newBrandName size = " + newBrandName.length
					+ ", newBrandDesc size = " + newBrandDesc.length
					+ ", invIdForBrand size = " + invIdForBrand.length);

			Connection connection = DBConnect.getConnection();
			try {
				connection.setAutoCommit(false);
				String addNewInvQuery = "insert into st_lms_inv_brand_master(brand_name, inv_id, brand_desc) values(?, ?, ?)";
				PreparedStatement pstmt = connection
						.prepareStatement(addNewInvQuery);

				PreparedStatement vpstmt = connection
						.prepareStatement("select * from st_lms_inv_brand_master where inv_id=? and brand_name = ?");
				ResultSet rs = null;

				int rowCount = 0;
				for (int i = 0, l = newBrandName.length; i < l; i++) {
					logger.debug("newBrandName = " + newBrandName[i]
							+ "; newBrandDesc = " + newBrandDesc[i]
							+ "; inv Id = " + invIdForBrand[i]);

					if (!"".equals(newBrandName[i].trim())
							&& invIdForBrand[i] > 0) {

						vpstmt.setInt(1, invIdForBrand[i]);
						vpstmt.setString(2, newBrandName[i].trim());
						rs = vpstmt.executeQuery();
						rowCount = 0;
						if (!rs.next()) {
							try {
								pstmt.setString(1, newBrandName[i].trim());
								pstmt.setInt(2, invIdForBrand[i]);
								pstmt.setString(3, newBrandDesc[i].trim());
								rowCount = pstmt.executeUpdate();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							if (rowCount > 0) {
								logger.debug("Total row inserted is = "
										+ rowCount);
								sb.append(newBrandName[i].trim()).append(";");
							} else {
								sb1.append(newBrandName[i].trim()).append(";");
								throw new LMSException(
										"Row is not inserted for "
												+ newBrandName[i].trim());
							}
						} else {
							sb1.append(newBrandName[i].trim()).append(";");
							logger.debug("Row is not inserted for 111"
									+ newBrandName[i].trim());
						}

					}
				}

				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new LMSException(e);
				}
			}

		}
		String invListStr = sb.toString();
		if ("".equals(invListStr)) {
			return "NO Brand " + "||" + sb1.toString();
		} else {
			return invListStr + "||" + sb1.toString();
		}

	}

	/**
	 * This Method is Used to Add new Inventory type and Their Brand and Model.
	 * for Consumable or Non Consumable Devices.
	 */
	public String addNewInvDetails(String invType, String[] newInvName,
			String[] newInvDesc, String[] img) throws LMSException {

		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		logger.debug("invType = " + invType + "\n newInvName = " + newInvName
				+ "\n newInvDesc = " + newInvDesc);

		if (newInvName != null && newInvDesc != null && img != null) {
			logger.debug(" newInvName size = " + newInvName.length
					+ ", newInvDesc size = " + newInvDesc.length);

			Connection connection = DBConnect.getConnection();

			try {
				connection.setAutoCommit(false);
				String addNewInvQuery = "insert into st_lms_inv_master(inv_name, inv_type, inv_img, inv_desc) values(?, ?, ?, ?)";
				PreparedStatement pstmt = connection
						.prepareStatement(addNewInvQuery);
				PreparedStatement vpstmt = connection
						.prepareStatement("select * from st_lms_inv_master where inv_type=? and inv_name = ?");
				ResultSet rs = null;

				int rowCount = 0;
				File imgFile = null;
				FileInputStream fin = null;
				for (int i = 0, l = newInvName.length; i < l; i++) {
					logger.debug("newInvName = " + newInvName[i]
							+ "; newInvDesc = " + newInvDesc[i]
							+ "; inv Image = " + img[i]);

					if (!"".equals(newInvName[i].trim())) {
						vpstmt.setString(1, invType.trim());
						vpstmt.setString(2, newInvName[i].trim());
						rs = vpstmt.executeQuery();
						rowCount = 0;
						if (!rs.next()) {
							try {
								pstmt.setString(1, newInvName[i].trim());
								pstmt.setString(2, invType.trim());

								// insert image into DB
								try {
									logger.debug("=============== " + img[i]);
									if (!"".equals(img[i].trim())) {
										imgFile = new File(img[i]);
										fin = new FileInputStream(imgFile);
										pstmt.setBinaryStream(3, fin,
												(int) imgFile.length());
									} else {
										pstmt.setBlob(3, (Blob) null);
									}
								} catch (FileNotFoundException e) {
									pstmt.setBlob(3, (Blob) null);
								}

								pstmt.setObject(4, newInvDesc[i].trim());
								rowCount = pstmt.executeUpdate();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							if (rowCount > 0) {
								logger.debug("Total row inserted is = "
										+ rowCount);
								sb.append(newInvName[i].trim()).append("; ");
							} else {
								logger.debug("Row is not inserted for "
										+ newInvName[i].trim());
								sb1.append(newInvName[i].trim()).append("; ");
							}

						} else {
							sb1.append(newInvName[i].trim()).append("; ");
							logger.debug("Row is not inserted for "
									+ newInvName[i].trim());
						}
					}
				}
				connection.commit();

			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new LMSException(e);
				}
			}

		}

		String invListStr = sb.toString();
		if ("".equals(invListStr)) {
			return "NO Inventory" + "||" + sb1.toString();
		} else {
			return invListStr + "||" + sb1.toString();
		}
	}

	public String addNewInvSpec(int[] invIdForModSpec, String[] specType,
			String[] specUnit, String[] specValue, double[] costPerUnit,
			String[] newModSpecDesc) throws LMSException {
		logger.debug("addNewInvSpec called");
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		boolean flag = true;
		logger.debug("\n specType = " + specType + ", costPerUnit = "
				+ costPerUnit + "\n specValue = " + specValue + "\n inv Id = "
				+ invIdForModSpec + "specUnit = " + specUnit
				+ ", newModSpecDesc = " + newModSpecDesc);

		if (invIdForModSpec != null && specType != null && specValue != null
				&& costPerUnit != null && newModSpecDesc != null) {

			logger.debug("\n specType size = " + specType.length
					+ ", costPerUnit size = " + costPerUnit.length
					+ "\n specValue size = " + specValue.length
					+ "\n inv Id size = " + invIdForModSpec.length
					+ "specUnit size = " + specUnit.length
					+ ", newModSpecDesc size = " + newModSpecDesc.length);

			Connection connection = DBConnect.getConnection();
			try {
				connection.setAutoCommit(false);
				String checkExisting = "select * from st_lms_cons_inv_specification where spec_type=? AND spec_value=? AND spec_unit=?";
				ResultSet rs = null;
				PreparedStatement pstmt1 = connection
						.prepareStatement(checkExisting);
				String addNewInvQuery = "insert into st_lms_cons_inv_specification(inv_id, spec_type, spec_value,"
						+ " spec_unit, cost_per_unit, spec_desc) values(?,?,?,?,?,?)";
				PreparedStatement pstmt = connection
						.prepareStatement(addNewInvQuery);

				int rowCount = 0;
				for (int i = 0, l = invIdForModSpec.length; i < l; i++) {
					logger.debug("\n specType = " + specType[i]
							+ ", costPerUnit = " + costPerUnit[i]
							+ "\n specValue = " + specValue[i] + " inv Id = "
							+ invIdForModSpec[i] + "\nspecUnit = "
							+ specUnit[i] + ", newModSpecDesc = "
							+ newModSpecDesc[i]);

					if (!"".equals(specValue[i].trim())
							&& !"".equals(specType[i].trim())
							&& invIdForModSpec[i] > 0) {
						//
						pstmt1.clearParameters();
						pstmt1.setString(1, specType[i].trim());
						pstmt1.setInt(2, Integer.parseInt(specValue[i].trim()));
						pstmt1.setString(3, specUnit[i].trim());
						rs = pstmt1.executeQuery();
						if (!rs.next()) {// duplicate entry not found
							pstmt.setInt(1, invIdForModSpec[i]);
							pstmt.setString(2, specType[i].trim());
							pstmt.setInt(3, Integer.parseInt(specValue[i]
									.trim()));
							pstmt.setString(4, specUnit[i].trim());
							pstmt.setDouble(5, costPerUnit[i]);
							pstmt.setString(6, newModSpecDesc[i].trim());
							logger.debug(pstmt);
							rowCount = pstmt.executeUpdate();

							if (rowCount > 0) {
								logger.debug("Total row inserted is = "
										+ rowCount);
								sb.append(specValue[i].trim()).append(
										"-" + specUnit[i]).append(
										"-" + specType[i]).append(";");
							} else {
								sb1.append(specValue[i].trim()).append(
										"-" + specUnit[i]).append(
										"-" + specType[i]).append(";");
								throw new LMSException("Row is not inserted. ");
							}
						} else {
							sb1.append(specValue[i].trim()).append(
									"-" + specUnit[i])
									.append("-" + specType[i]).append(";");
						}
					}
				}

				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new LMSException(e);
				}
			}

		}

		String invListStr = sb.toString();
		if ("".equals(invListStr)) {
			return "NO Specification||" + sb1.toString();
		} else {
			return invListStr + "||" + sb1.toString();
		}

	}

	public String addNewModelDetails(int[] invIdForModel,
			String[] brandIdForModel, String[] newModelName,
			String[] newModelDesc, String isReqOnReg[], String[] checkBindingLength) throws LMSException {

		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		logger.debug("\n newBrandName = " + newModelName + "\n newBrandDesc = "
				+ newModelDesc + "\n inv Id = " + invIdForModel
				+ "brandIdForModel = " + brandIdForModel);

		if (newModelName != null && newModelDesc != null
				&& invIdForModel != null && brandIdForModel != null) {

			logger.debug(" newModelName size = " + newModelName.length
					+ ", newModelDesc size = " + newModelDesc.length
					+ ", invIdForModel size = " + invIdForModel.length
					+ ", brandIdForModel size " + brandIdForModel.length);

			Connection connection = DBConnect.getConnection();
			try {
				connection.setAutoCommit(false);
				String addNewInvQuery = "insert into st_lms_inv_model_master(brand_id, inv_id, model_name, model_desc, is_req_on_reg, check_binding_length, inv_column_name) values(?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmt = connection
						.prepareStatement(addNewInvQuery);
				PreparedStatement vpstmt = connection
						.prepareStatement("select * from st_lms_inv_model_master where brand_id=? and inv_id=? and model_name = ?");
				ResultSet rs = null;
				int rowCount = 0;
				String[] bidArr = null;
				for (int i = 0, l = newModelName.length; i < l; i++) {
					logger.debug("newModelName = " + newModelName[i]
							+ "; newModelDesc = " + newModelDesc[i]
							+ "; inv Id = " + invIdForModel[i]
							+ ", brandIdForModel = " + brandIdForModel[i]);

					bidArr = brandIdForModel[i].trim().split("-");

					if (!"".equals(newModelName[i].trim())
							&& !"".equals(brandIdForModel[i].trim())
							&& invIdForModel[i] > 0 && bidArr.length > 1
							&& Integer.parseInt(bidArr[1]) == invIdForModel[i]) {

						vpstmt.setInt(1, Integer.parseInt(bidArr[0]));
						vpstmt.setInt(2, invIdForModel[i]);
						vpstmt.setString(3, newModelName[i].trim());
						rs = vpstmt.executeQuery();
						rowCount = 0;
						if (!rs.next()) {
							try {
								pstmt.setInt(1, Integer.parseInt(bidArr[0]));
								pstmt.setInt(2, invIdForModel[i]);
								pstmt.setString(3, newModelName[i].trim());
								pstmt.setString(4, newModelDesc[i].trim());
								pstmt.setString(5, isReqOnReg[i].trim());
								pstmt.setInt(6, Integer.parseInt(checkBindingLength[i].trim()));
								pstmt.setString(7, "serial_number");
								rowCount = pstmt.executeUpdate();
							} catch (SQLException e) {
								e.printStackTrace();
							}
							if (rowCount > 0) {
								logger.debug("Total row inserted is = "
										+ rowCount);
								sb.append(newModelName[i].trim()).append(";");
							} else {
								logger.debug("Row is not inserted for "
										+ newModelName[i].trim());
								sb1.append(newModelName[i].trim()).append(";");
							}
						} else {
							sb1.append(newModelName[i].trim()).append(";");
							logger.debug("Row is not inserted for "
									+ newModelName[i].trim());
						}

					}
				}

				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			} finally {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new LMSException(e);
				}
			}

		}
		String invListStr = sb.toString();
		if ("".equals(invListStr)) {
			return "NO Model" + "||" + sb1.toString();

		} else {
			return invListStr + "||" + sb1.toString();
		}

	}

	public String agtNonConsInvReturnSave(int retOrgId, String invSrNo,
			int agtOrgId,String modelId, String userType, int agtId) throws LMSException {
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String status = "error";
		String srNo = invSrNo;// serial no of inventory

		int  model  = 0;
		//String deviceType =null;
		String invColName =null;
		try {
			
			model  = Integer.parseInt(modelId.split("-")[0]);// device type of inventory
			
			String modelDeatials = "select  inv_column_name from st_lms_inv_model_master where model_id=?  " ;
			String assignSerNoQuery = "update st_lms_inv_status set current_owner_type = ?, current_owner_id = ? where serial_no =  ? and current_owner_id = ?  and inv_model_id = ?";
			String insIntoInvDetQuery = "insert into st_lms_inv_detail(user_id,user_org_type,user_org_id, inv_model_id,serial_no,date,cost_to_bo,is_new,current_owner_type,current_owner_id) select ?,?,?,inv_model_id,?,?,cost_to_bo, is_new,?,? from st_lms_inv_status where serial_no = ? and inv_model_id = ?";
			con = DBConnect.getConnection();
			psmt =con.prepareStatement(modelDeatials);
			psmt.setInt(1, model);
			rs =psmt.executeQuery();
			if(rs.next()){
				invColName =rs.getString("inv_column_name");
			}else{
				
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			DBConnect.closeConnection(psmt, rs);
			con.setAutoCommit(false);
			psmt = con.prepareStatement(assignSerNoQuery);
			psmt.setString(1, userType);
			psmt.setInt(2, agtOrgId);
			psmt.setString(3, srNo);
			psmt.setInt(4, retOrgId);
			psmt.setInt(5, model);
			logger.debug("update query for inventory::::::" + psmt);
			psmt.executeUpdate();
			PreparedStatement insIntoInvDet = con.prepareStatement(insIntoInvDetQuery);
			insIntoInvDet.setInt(1, agtId);
			insIntoInvDet.setString(2, userType);
			insIntoInvDet.setInt(3, agtOrgId);
			// insIntoInvDet.setInt(4, inv_modelId);
			insIntoInvDet.setString(4, srNo);
			insIntoInvDet.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
			insIntoInvDet.setString(6, userType);
			insIntoInvDet.setInt(7, agtOrgId);
			insIntoInvDet.setString(8, srNo);
			insIntoInvDet.setInt(9, model);
			logger.debug("insert query for inventory::::::"+ insIntoInvDet);
			insIntoInvDet.executeUpdate();
			String offLoginStatus = "update st_lms_ret_offline_master set "+invColName+" = -1 , device_type = -1 where  organization_id=?";
			psmt = con.prepareStatement(offLoginStatus);
			psmt.setInt(1,retOrgId );
			psmt.executeUpdate();
			con.commit();
			status = "success";

		}catch (Exception e) {
			logger.error("Exception",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				logger.error("Exception",e1);
			}
			return "error";
		}finally{
			
			DBConnect.closeConnection(con, psmt);
			
		}
		return status;
	}

	private void assignConsInv(int userOrgId, int userId, String ownerType,
			int agtOrgId, int retOrgId, int[] consInvId, int[] consModelId,
			int[] consQty, Connection conn, String userType,int DNID)
			throws SQLException {

		List<String> validSerNoList = new ArrayList<String>();
		List<String> inValidSerNoList = new ArrayList<String>();

		String fetchDetailsQuery = "select party_org_id, aa.inv_model_id, cumm_qty_count, inv_name, "
				+ " concat(spec_value, '-', spec_unit, '-', spec_type) 'model_name' from st_lms_cons_inv_status "
				+ " aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc where aa.inv_model_id = "
				+ " bb.inv_model_id and bb.inv_id = cc.inv_id ";
		PreparedStatement pstmt = conn.prepareStatement(fetchDetailsQuery);
		ResultSet rs = pstmt.executeQuery();
		Map<String, Long> partyDetMap = new TreeMap<String, Long>();
		Map<Integer, ConsNNonConsBean> invDetMap = new TreeMap<Integer, ConsNNonConsBean>();
		while (rs.next()) {
			partyDetMap.put(rs.getInt("party_org_id") + "-"
					+ rs.getInt("inv_model_id"), rs.getLong("cumm_qty_count"));

		}

		int partyOrgId = "AGENT".equalsIgnoreCase(ownerType.trim()) ? agtOrgId
				: retOrgId;
		int fstRowUpd = -1, scdRowUpd = -1;
		ConsNNonConsBean bean = null;
		for (int i = 0, len = consModelId.length; i < len; i = i + 1) {
			if (consInvId[i] > 0 && consModelId[i] > 0 && consQty[i] > 0) {

				if (partyDetMap.get(userOrgId + "-" + consModelId[i]) != null
						&& partyDetMap.get(userOrgId + "-" + consModelId[i]) >= consQty[i]) {

					// update st_lms_cons_inv_status table to add inventory
					// quantity
					String redInvStatusQuery = "update st_lms_cons_inv_status set cumm_qty_count=cumm_qty_count-?"
							+ " where inv_model_id =? and party_org_id = ? and cumm_qty_count>=?";
					pstmt = conn.prepareStatement(redInvStatusQuery);
					pstmt.setLong(1, consQty[i]);
					pstmt.setInt(2, consModelId[i]);
					pstmt.setInt(3, userOrgId);
					pstmt.setLong(4, consQty[i]);
					logger.debug(pstmt);
					fstRowUpd = pstmt.executeUpdate();

					if (partyDetMap.get(partyOrgId + "-" + consModelId[i]) != null
							&& fstRowUpd > 0) {

						// update st_lms_cons_inv_status table to add inventory
						// quantity
						String incInvStatusQuery = "update st_lms_cons_inv_status set cumm_qty_count=cumm_qty_count+?"
								+ " where inv_model_id =? and party_org_id = ?";
						pstmt = conn.prepareStatement(incInvStatusQuery);
						pstmt.setLong(1, consQty[i]);
						pstmt.setInt(2, consModelId[i]);
						pstmt.setInt(3, partyOrgId);
						logger.debug(pstmt);
						scdRowUpd = pstmt.executeUpdate();

						String insIntoUserInvDetailQuery = "insert into st_lms_cons_inv_detail(user_id, user_org_id, "
								+ " current_owner_id, inv_model_id, quantity, date, ref_transaction_id, amount) select ?, ?,"
								+ " ?, ?, ?, ?, ?, cost_per_unit*? from st_lms_cons_inv_specification where inv_model_id=?";
						pstmt = conn
								.prepareStatement(insIntoUserInvDetailQuery);
						pstmt.setInt(1, userId);
						pstmt.setInt(2, userOrgId);
						pstmt.setInt(3, partyOrgId);
						pstmt.setInt(4, consModelId[i]);
						pstmt.setLong(5, consQty[i]);
						pstmt.setTimestamp(6, new Timestamp(
								new java.util.Date().getTime()));
						pstmt.setObject(7, null);
						pstmt.setLong(8, consQty[i]);
						pstmt.setInt(9, consModelId[i]);
						logger.debug("st_lms_cons_inv_detail = " + pstmt);
						int rowCount = pstmt.executeUpdate();
						logger.debug("Total row inserted = " + rowCount);
						if (rowCount > 0) {
							ResultSet rsTask=pstmt.getGeneratedKeys();
							if(rsTask.next()){
								int taskId=rsTask.getInt(1);
								String insQuery="insert into st_lms_inv_dl_task_mapping values("+DNID+","+taskId+",'CONS')";
							PreparedStatement	pstmt1=conn.prepareStatement(insQuery);
								pstmt1.executeUpdate();
							}
							
							
							bean = new ConsNNonConsBean();
							// bean.
							// validSerNoList.add(bean);
						}

					} else if (fstRowUpd > 0) {
						// insert into st_lms_cons_inv_status table to add
						// inventory quantity
						String insIntoInvStatusQuery = "insert into st_lms_cons_inv_status(party_type, party_org_id, "
								+ " inv_model_id, cumm_qty_count) values(?, ?, ?, ?)";
						pstmt = conn.prepareStatement(insIntoInvStatusQuery);
						pstmt.setString(1, ownerType);
						pstmt.setInt(2, partyOrgId);
						pstmt.setInt(3, consModelId[i]);
						pstmt.setLong(4, consQty[i]);
						logger.debug(pstmt);
						scdRowUpd = pstmt.executeUpdate();
						logger.debug("Total row inserted = " + scdRowUpd);
						
						String insIntoUserInvDetailQuery = "insert into st_lms_cons_inv_detail(user_id, user_org_id, "
							+ " current_owner_id, inv_model_id, quantity, date, ref_transaction_id, amount) select ?, ?,"
							+ " ?, ?, ?, ?, ?, cost_per_unit*? from st_lms_cons_inv_specification where inv_model_id=?";
					pstmt = conn
							.prepareStatement(insIntoUserInvDetailQuery);
					pstmt.setInt(1, userId);
					pstmt.setInt(2, userOrgId);
					pstmt.setInt(3, partyOrgId);
					pstmt.setInt(4, consModelId[i]);
					pstmt.setLong(5, consQty[i]);
					pstmt.setTimestamp(6, new Timestamp(
							new java.util.Date().getTime()));
					pstmt.setObject(7, null);
					pstmt.setLong(8, consQty[i]);
					pstmt.setInt(9, consModelId[i]);
					logger.debug("st_lms_cons_inv_detail = " + pstmt);
					int rowCount = pstmt.executeUpdate();
					logger.debug("Total row inserted = " + rowCount);
					if (rowCount > 0) {
						ResultSet rsTask=pstmt.getGeneratedKeys();
						if(rsTask.next()){
							int taskId=rsTask.getInt(1);
							String insQuery="insert into st_lms_inv_dl_task_mapping values("+DNID+","+taskId+",'CONS')";
						PreparedStatement	pstmt1=conn.prepareStatement(insQuery);
							pstmt1.executeUpdate();
						}
						
						
						bean = new ConsNNonConsBean();
						// bean.
						// validSerNoList.add(bean);
					}
						
					}

					/*if (scdRowUpd > 0 && fstRowUpd > 0) {
						String insIntoInvDetailQuery = "insert into st_lms_cons_inv_detail(user_id, user_org_id, "
								+ " current_owner_id, inv_model_id, quantity, date, ref_transaction_id, amount) select ?, ?,"
								+ " ?, ?, ?, ?, ?, cost_per_unit*? from st_lms_cons_inv_specification where inv_model_id=?";
						pstmt = conn.prepareStatement(insIntoInvDetailQuery);
						pstmt.setInt(1, userId);
						pstmt.setInt(2, userOrgId);
						pstmt.setInt(3, partyOrgId);
						pstmt.setInt(4, consModelId[i]);
						pstmt.setLong(5, consQty[i]);
						pstmt.setTimestamp(6, new Timestamp(
								new java.util.Date().getTime()));
						pstmt.setObject(7, null);
						pstmt.setLong(8, consQty[i]);
						pstmt.setInt(9, consModelId[i]);
						logger.debug("st_lms_cons_inv_detail = " + pstmt);
						int rowCount = pstmt.executeUpdate();
						logger.debug("Total row inserted = " + rowCount);
						if (rowCount > 0) {
							bean = new ConsNNonConsBean();
							// bean.
							// validSerNoList.add(bean);
						}
					}*/
				}

			} else {
				// values are not in valid format

			}
		}

		logger.debug("valid list = " + validSerNoList);
		logger.debug("invalid list = " + inValidSerNoList);

	}

	public int assignConsNNonConsInv(int userOrgId, int userId,
			String ownerType, int agtOrgId, int retOrgId, int[] nonConsInvId,
			int[] nonConsModelId, int[] nonConsBrandId, String[] serNo,
			int[] consInvId, int[] consModelId, int[] consQty, String userType)
			throws LMSException {

		Connection conn = DBConnect.getConnection();
		int DNID=0;
		try {
			conn.setAutoCommit(false);
			
			
			String lastDCNoGenerated = null, autoGeneDCNo = null;
			PreparedStatement boReceiptNoGenStmt=null;
			boReceiptNoGenStmt = conn.prepareStatement("SELECT * from st_lms_inv_dl_detail where dl_owner_type=?  ORDER BY generated_dl_id DESC LIMIT 1");
			boReceiptNoGenStmt.setString(1, "BO");
			ResultSet DCRs = boReceiptNoGenStmt.executeQuery();

			while (DCRs.next()) {
				lastDCNoGenerated = DCRs.getString("generated_dl_id");
			}
			autoGeneDCNo = GenerateRecieptNo.getRecieptNo("DNCHALLAN",
					lastDCNoGenerated, "BO");
			System.out.println("lastDCNoGenerated " + lastDCNoGenerated
					+ "autoGeneDCNo " + autoGeneDCNo);
			
			// insert into lms dl inv detail table for delivery challan
			PreparedStatement boReceiptStmt = conn.prepareStatement("insert into st_lms_inv_dl_detail(dl_owner_type,generated_dl_id) values(?,?)");
			
			boReceiptStmt.setString(1, "BO");
			boReceiptStmt.setString(2, autoGeneDCNo);
			System.out.println("bo DL receipt = " + boReceiptStmt);
			boReceiptStmt.execute();
			//find dlID
			boReceiptNoGenStmt = conn.prepareStatement("SELECT * from st_lms_inv_dl_detail where generated_dl_id=?  ORDER BY generated_dl_id DESC LIMIT 1");
			boReceiptNoGenStmt.setString(1, autoGeneDCNo);
			 DCRs = boReceiptNoGenStmt.executeQuery();

			while (DCRs.next()) {
				DNID = DCRs.getInt("dl_id");
			}
			
			// assign non consumable inventory
			assignNonConsInv(userOrgId, userId, ownerType, agtOrgId, retOrgId,
					nonConsInvId, nonConsModelId, nonConsBrandId, serNo, conn,
					userType,DNID);
			// assign consumable inventory
			assignConsInv(userOrgId, userId, ownerType, agtOrgId, retOrgId,
					consInvId, consModelId, consQty, conn, userType,DNID);

			
			//insertDlTaskMapTable(nonConsInvId,nonConsModelId,nonConsBrandId,serNo,conn,DNID);
			
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return DNID;
	}

	private void assignNonConsInv(int userOrgId, int userId, String ownerType,
			int agtOrgId, int retOrgId, int[] nonConsInvId,
			int[] nonConsModelId, int[] nonConsBrandId, String[] serNo,
			Connection conn, String userType,int DNID) throws SQLException {

		List<String> validSerNoList = new ArrayList<String>();
		List<String> inValidSerNoList = new ArrayList<String>();

		String assignSerNoQuery = "update st_lms_inv_status set user_id = ?, user_org_type = ?, user_org_id = ?, "
				+ "current_owner_type = ?, current_owner_id = ? where inv_model_id =? and serial_no = "
				+ " ? and current_owner_id = ?";
		PreparedStatement assignSerNoPstmt = conn
				.prepareStatement(assignSerNoQuery);
		assignSerNoPstmt.setInt(1, userId);
		assignSerNoPstmt.setString(2, userType);
		assignSerNoPstmt.setInt(3, userOrgId);
		assignSerNoPstmt.setString(4, ownerType);
		assignSerNoPstmt.setInt(5,
				"AGENT".equalsIgnoreCase(ownerType.trim()) ? agtOrgId
						: retOrgId);
		// assignSerNoPstmt.setInt(6, modId); inside for loop
		// assignSerNoPstmt.setString(7, sNo.trim()); inside for loop
		assignSerNoPstmt.setInt(8, userOrgId);

		// insert into st_lms_inv_detail table to add inventory quantity
		String insIntoInvDetQuery = "insert into st_lms_inv_detail(user_id,user_org_type,user_org_id,"
				+ " inv_model_id,serial_no,date,cost_to_bo,is_new,current_owner_type,current_owner_id)"
				+ " select ?,?,?,?,?,?,cost_to_bo, is_new,?,? from st_lms_inv_status where inv_model_id =? "
				+ " and serial_no = ?";
		PreparedStatement insIntoInvDet = conn
				.prepareStatement(insIntoInvDetQuery);
		insIntoInvDet.setInt(1, userId);
		insIntoInvDet.setString(2, userType);
		insIntoInvDet.setInt(3, userOrgId);
		// insIntoInvDet.setInt(4, modId); inside for loop
		// insIntoInvDet.setString(5, sNo.trim()); inside for loop
		insIntoInvDet.setTimestamp(6, new Timestamp(new java.util.Date()
				.getTime()));
		insIntoInvDet.setString(7, ownerType);
		insIntoInvDet.setInt(8,
				"AGENT".equalsIgnoreCase(ownerType.trim()) ? agtOrgId
						: retOrgId);
		// insIntoInvDet.setInt(9, modId); inside for loop
		// insIntoInvDet.setString(10, sNo.trim()); inside for loop

		String[] serNoArr = null;
		int fstRowUpd = -1, scdRowUpd = -1;
		for (int i = 0, len = nonConsModelId.length; i < len; i = i + 1) {
			if (nonConsInvId[i] > 0 && nonConsModelId[i] > 0
					&& nonConsBrandId[i] > 0 && !"".equals(serNo[i].trim())) {

				assignSerNoPstmt.setInt(6, nonConsModelId[i]);

				insIntoInvDet.setInt(4, nonConsModelId[i]);
				insIntoInvDet.setInt(9, nonConsModelId[i]);

				serNoArr = serNo[i].split(",");
				for (int k = 0, klen = serNoArr.length; k < klen; k = k + 1) {
					fstRowUpd = -1;
					scdRowUpd = -1;
					if (!"".equals(serNoArr[k].trim())) {
						// update st_lms_inv_status
						assignSerNoPstmt.setString(7, serNoArr[k].trim());
						logger.debug("updPstmt = " + assignSerNoPstmt);
						fstRowUpd = assignSerNoPstmt.executeUpdate();

						if (fstRowUpd == 1) {
							// insert data into st_lms_inv_detail
							insIntoInvDet.setString(5, serNoArr[k].trim());
							insIntoInvDet.setString(10, serNoArr[k].trim());
							logger.debug("insPstmt = " + insIntoInvDet);
							scdRowUpd = insIntoInvDet.executeUpdate();
							if (scdRowUpd > 0) {
								ResultSet rsTask=insIntoInvDet.getGeneratedKeys();
								if(rsTask.next()){
									int taskId=rsTask.getInt(1);
									String insQuery="insert into st_lms_inv_dl_task_mapping values("+DNID+","+taskId+",'NON_CONS')";
								PreparedStatement	pstmt=conn.prepareStatement(insQuery);
									pstmt.executeUpdate();
								}
								validSerNoList.add(serNoArr[k].trim());
							}
						} else {
							inValidSerNoList.add(serNoArr[k].trim());
						}
					}
				}

			} else {
				// values are not in valid format
				logger.debug("inside else ==== " + nonConsInvId[i] + " == "
						+ nonConsModelId[i] + " === " + nonConsBrandId[i]
						+ "==" + serNo[i].trim());
			}
		}

		logger.debug("valid list = " + validSerNoList);
		logger.debug("invalid list = " + inValidSerNoList);

	}

	public String consInvUpload(int invId, String modelId, String quantity,
			int userId, int userOrgId, String orgType) throws LMSException {

		logger.debug("invId = " + invId + ", modelId = " + modelId
				+ ", quantity = " + quantity);

		StringBuilder sb = new StringBuilder();
		Connection connection = DBConnect.getConnection();
		try {
			connection.setAutoCommit(false);

			if (!"".equals(modelId.trim()) && !"".equals(quantity.trim())
					&& invId > 0) {

				String fetchDetailsQuery = "select party_type, party_org_id, inv_model_id, cumm_qty_count"
						+ " from st_lms_cons_inv_status where party_org_id =? and inv_model_id = ?";

				PreparedStatement pstmt = connection
						.prepareStatement(fetchDetailsQuery);
				pstmt.setInt(1, userOrgId);
				int modId = Integer.parseInt(modelId.trim().split("-")[0]);
				pstmt.setInt(2, modId);
				ResultSet rs = pstmt.executeQuery();

				if (rs.next()) {
					// update st_lms_cons_inv_status table to add inventory
					// quantity
					String insIntoInvStatusQuery = "update st_lms_cons_inv_status set cumm_qty_count=cumm_qty_count+?"
							+ " where inv_model_id =? and party_org_id = ?";
					pstmt = connection.prepareStatement(insIntoInvStatusQuery);
					pstmt.setLong(1, Long.parseLong(quantity.trim()));
					pstmt.setInt(2, modId);
					pstmt.setInt(3, userOrgId);
					logger.debug(pstmt);
					int rowCount = pstmt.executeUpdate();
					logger.debug("Total row Updated = " + rowCount);

				} else {
					// insert into st_lms_cons_inv_status table to add inventory
					// quantity
					String insIntoInvStatusQuery = "insert into st_lms_cons_inv_status(party_type, party_org_id, "
							+ " inv_model_id, cumm_qty_count) values(?, ?, ?, ?)";
					pstmt = connection.prepareStatement(insIntoInvStatusQuery);
					pstmt.setString(1, orgType);
					pstmt.setInt(2, userOrgId);
					pstmt.setInt(3, modId);
					pstmt.setLong(4, Long.parseLong(quantity.trim()));
					logger.debug(pstmt);
					int rowCount = pstmt.executeUpdate();
					logger.debug("Total row inserted = " + rowCount);
				}

				String insIntoInvDetailQuery = "insert into st_lms_cons_inv_detail(user_id, user_org_id, "
						+ " current_owner_id, inv_model_id, quantity, date, ref_transaction_id, amount) select ?, ?,"
						+ " ?, ?, ?, ?, ?, cost_per_unit*? from st_lms_cons_inv_specification where inv_model_id=?";
				pstmt = connection.prepareStatement(insIntoInvDetailQuery);
				pstmt.setInt(1, userId);
				pstmt.setInt(2, userOrgId);
				pstmt.setInt(3, userOrgId);
				pstmt.setInt(4, modId);
				pstmt.setLong(5, Long.parseLong(quantity.trim()));
				pstmt.setTimestamp(6, new Timestamp(new java.util.Date()
						.getTime()));
				pstmt.setObject(7, null);
				pstmt.setLong(8, Long.parseLong(quantity.trim()));
				pstmt.setInt(9, modId);
				logger.debug("st_lms_cons_inv_detail = " + pstmt);
				int rowCount = pstmt.executeUpdate();
				logger.debug("Total row inserted = " + rowCount);

				connection.commit();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		String invListStr = sb.toString();
		if ("".equals(invListStr)) {
			return "NO Specification";
		} else {
			return invListStr;
		}
	}

	private String createQuery(String ownerType, String invType, int agtOrgId,
			int retOrgId, int invId, int brandId, String modelId) {

		StringBuilder strQuery = null;
		if ("NON_CONS".equalsIgnoreCase(invType.trim())) {
			strQuery = new StringBuilder(QueryManager
					.getST_FETCH_NON_CONS_COUNTS_FORORG());

			if (ownerType != null && !"ALL".equalsIgnoreCase(ownerType.trim())) {
				strQuery
						.append(" and current_owner_type = '" + ownerType + "'");
			}

			if (ownerType != null && "AGENT".equalsIgnoreCase(ownerType.trim())
					&& agtOrgId > 0) {
				strQuery.append(" and current_owner_id = " + agtOrgId);
			} else if (ownerType != null
					&& "RETAILER".equalsIgnoreCase(ownerType.trim())
					&& retOrgId > 0) {
				strQuery.append(" and current_owner_id = " + retOrgId);
			}else if (ownerType != null
					&& "RETAILER".equalsIgnoreCase(ownerType.trim())
					&& retOrgId < 0) {
				strQuery.append(" and ee.parent_id = " + agtOrgId);
			}

			if (invId > 0) {
				strQuery.append(" and bb.inv_id = " + invId);
			}

			if (brandId > 0) {
				strQuery.append(" and bb.brand_id = " + brandId);
			}

			if (modelId != null && !"".equalsIgnoreCase(modelId.trim())
					&& !"-1".equalsIgnoreCase(modelId.trim())) {
				strQuery.append(" and aa.inv_model_id = " + modelId);
			}

			strQuery
					.append(" group by current_owner_id, bb.inv_id  order by "+QueryManager.getAppendOrgOrder());

		} else {
			strQuery = new StringBuilder(QueryManager
					.getST_FETCH_CONS_COUNTS_FORORG());

			if (ownerType != null && !"ALL".equalsIgnoreCase(ownerType.trim())) {
				strQuery.append(" and party_type = '" + ownerType + "'");
			}

			if (ownerType != null && "AGENT".equalsIgnoreCase(ownerType.trim())
					&& agtOrgId > 0) {
				strQuery.append(" and party_org_id = " + agtOrgId);
			} else if (ownerType != null
					&& "RETAILER".equalsIgnoreCase(ownerType.trim())
					&& retOrgId > 0) {
				strQuery.append(" and party_org_id = " + retOrgId);
			}

			if (invId > 0) {
				strQuery.append(" and bb.inv_id = " + invId);
			}

			if (modelId != null && !"".equalsIgnoreCase(modelId.trim())
					&& !"-1".equalsIgnoreCase(modelId.trim())) {
				strQuery.append(" and aa.inv_model_id = " + modelId);
			}

			strQuery.append(" group by party_org_id, bb.inv_id  order by "+QueryManager.getAppendOrgOrder());
		}

		logger.debug("inv query = " + strQuery.toString());
		return strQuery.toString();
	}
	
	private String createQueryAtAgent(String ownerType, String invType, int agtOrgId,
			int retOrgId, int invId, int brandId, String modelId) {

		StringBuilder strQuery = null;
		if ("NON_CONS".equalsIgnoreCase(invType.trim())) {
			strQuery = new StringBuilder(QueryManager
					.getST_FETCH_NON_CONS_COUNTS_FORORG());

			if (ownerType != null && !"ALL".equalsIgnoreCase(ownerType.trim())) {
				strQuery
						.append(" and current_owner_type = '" + ownerType + "' AND parent_id="+agtOrgId);
			}

			if (ownerType != null && "ALL".equalsIgnoreCase(ownerType.trim())
					&& agtOrgId > 0) {
				strQuery.append(" and (current_owner_id = " + agtOrgId + " OR (current_owner_type='RETAILER' AND parent_id="+agtOrgId+"))");
			} else if (ownerType != null
					&& "RETAILER".equalsIgnoreCase(ownerType.trim())
					&& retOrgId > 0) {
				strQuery.append(" and current_owner_id = " + retOrgId);
			}

			if (invId > 0) {
				strQuery.append(" and bb.inv_id = " + invId);
			}

			if (brandId > 0) {
				strQuery.append(" and bb.brand_id = " + brandId);
			}

			if (modelId != null && !"".equalsIgnoreCase(modelId.trim())
					&& !"-1".equalsIgnoreCase(modelId.trim())) {
				strQuery.append(" and aa.inv_model_id = " + modelId);
			}

			strQuery
					.append(" group by current_owner_id, bb.inv_id  order by "+QueryManager.getAppendOrgOrder());

		} else {
			strQuery = new StringBuilder(QueryManager
					.getST_FETCH_CONS_COUNTS_FORORG());

			if (ownerType != null && !"ALL".equalsIgnoreCase(ownerType.trim())) {
				strQuery.append(" and party_type = '" + ownerType + "'");
			}

			if (ownerType != null && "ALL".equalsIgnoreCase(ownerType.trim())
					&& agtOrgId > 0) {
				strQuery.append(" and party_org_id = " + agtOrgId);
			} else if (ownerType != null
					&& "RETAILER".equalsIgnoreCase(ownerType.trim())
					&& retOrgId > 0) {
				strQuery.append(" and party_org_id = " + retOrgId);
			}

			if (invId > 0) {
				strQuery.append(" and bb.inv_id = " + invId);
			}

			if (modelId != null && !"".equalsIgnoreCase(modelId.trim())
					&& !"-1".equalsIgnoreCase(modelId.trim())) {
				strQuery.append(" and aa.inv_model_id = " + modelId);
			}

			strQuery.append(" group by party_org_id, bb.inv_id  order by "+QueryManager.getAppendOrgOrder());
		}

		logger.debug("inv query = " + strQuery.toString());
		return strQuery.toString();
	}

	private String createQueryToFetchDetails(int ownerId, String invType,
			int invId, int brandId, String modelId) {

		StringBuilder strQuery = null;
		if ("NON_CONS".equalsIgnoreCase(invType.trim())) {
			strQuery = new StringBuilder(QueryManager
					.getST_FETCH_NON_CONS_DETAIL_FORORG());
			strQuery.append(" and current_owner_type!= 'REMOVED' ");
			if (ownerId > 0) {
				strQuery.append(" and current_owner_id = " + ownerId);
			}
			if (invId > 0) {
				strQuery.append(" and bb.inv_id = " + invId);
			}
			if (brandId > 0) {
				strQuery.append(" and bb.brand_id = " + brandId);
			}
			if (modelId != null && !"".equalsIgnoreCase(modelId.trim())
					&& !"-1".equalsIgnoreCase(modelId.trim())) {
				strQuery.append(" and aa.inv_model_id = " + modelId);
			}
			strQuery
					.append(" order by name, inv_name, brand_name, model_name, serial_no");

		} else {
			strQuery = new StringBuilder(QueryManager
					.getST_FETCH_CONS_DETAIL_FORORG());
			if (ownerId > 0) {
				strQuery.append(" and party_org_id = " + ownerId);
			}
			if (invId > 0) {
				strQuery.append(" and bb.inv_id = " + invId);
			}
			if (modelId != null && !"".equalsIgnoreCase(modelId.trim())
					&& !"-1".equalsIgnoreCase(modelId.trim())) {
				strQuery.append(" and aa.inv_model_id = " + modelId);
			}
			strQuery
					.append(" order by name, inv_name, brand_name, model_name, serial_no ");
		}

		logger.debug("inv query = " + strQuery.toString());
		return strQuery.toString();
	}

	public Map<String, HashMap<String, String>> fetchAgentNRetList()
			throws LMSException {
		Connection conn = null;
		PreparedStatement pstmt =null;
		ResultSet result =null;
		Map<String, HashMap<String, String>> map = new TreeMap<String, HashMap<String, String>>();

		HashMap<String, String> agentMap = new LinkedHashMap<String, String>();
		 HashMap<String, String> retMap = new LinkedHashMap<String, String>();
		HashMap<String, String> modelMap = new LinkedHashMap<String, String>();
		HashMap<String, String> invSpecMap = new LinkedHashMap<String, String>();

		try {
			conn =DBConnect.getConnection();
			
			// fetch retailer list
			 pstmt = conn.prepareStatement("select "+QueryManager.getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_status !='TERMINATE' and organization_type='AGENT' order by "+QueryManager.getAppendOrgOrder());
			 result = pstmt.executeQuery();

			String orgId = null, orgName = null;
			while (result.next()) {
				orgId = result.getInt(TableConstants.ORG_ID) + "";
				orgName = result.getString("orgCode");
				agentMap.put(orgId, orgName);
			}

			// fetch retailer list
			pstmt = conn
					.prepareStatement("select "+QueryManager.getOrgCodeQuery()+", organization_id, parent_id from st_lms_organization_master where organization_status !='TERMINATE' and  organization_type='RETAILER' and parent_id not in (select orgANIZATION_id from st_lms_organization_master where organization_type='AGENT' and organization_status='TERMINATE') order by "+QueryManager.getAppendOrgOrder());
			result = pstmt.executeQuery();

			orgId = null;
			orgName = null;
			while (result.next()) {
				orgId = result.getInt(TableConstants.ORG_ID) + "-"
						+ result.getInt("parent_id");
				orgName = result.getString("orgCode");
				retMap.put(orgId, orgName);
			}

			// fetch model name list from database
			pstmt = conn
					.prepareStatement("select model_id, brand_id, model_name from st_lms_inv_model_master");
			result = pstmt.executeQuery();
			while (result.next()) {
				modelMap.put(result.getInt("model_id") + "-"
						+ result.getInt("brand_id"), result
						.getString("model_name"));
			}
			logger.debug("modelMap = " + modelMap);

			// fetch model specification list from database
			pstmt = conn
					.prepareStatement("select inv_model_id, inv_id, concat(spec_value, '-', spec_unit, '-', spec_type) 'model_name' from st_lms_cons_inv_specification ");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				invSpecMap.put(rs.getInt("inv_model_id") + "-"
						+ rs.getInt("inv_id"), rs.getString("model_name"));
			}
			logger.debug("invSpecMap = " + invSpecMap);

			map.put("agentMap", agentMap);
			map.put("retMap", retMap);
			map.put("invSpecMap", invSpecMap);
			map.put("modelMap", modelMap);

			map.putAll(fetchInvDetails());

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return map;
	}
	
	public Map<String, Map<String, String>> fetchAgentNRetListAtAgent(UserInfoBean userBean) throws LMSException {
		Connection conn = null;
		Map<String, Map<String, String>> map = new TreeMap<String, Map<String, String>>();
		
		Map<String, String> retMap = new LinkedHashMap<String, String>();
		Map<String, String> modelMap = new LinkedHashMap<String, String>();
		Map<String, String> invSpecMap = new LinkedHashMap<String, String>();
		
		try {
			conn =DBConnect.getConnection();
		
			// fetch retailer list
			PreparedStatement pstmt = conn
					.prepareStatement("select name, organization_id, parent_id from st_lms_organization_master where organization_status !='TERMINATE' and organization_type='RETAILER' and parent_id=?");
			pstmt.setInt(1, userBean.getUserOrgId());
			ResultSet result = pstmt.executeQuery();
		
		String orgId = null, orgName = null;
			while (result.next()) {
				orgId = result.getString(TableConstants.ORG_ID);
				orgName = result.getString(TableConstants.NAME);
				retMap.put(orgId, orgName);
			}
		
			// fetch model name list from database
			pstmt = conn
					.prepareStatement("select model_id, brand_id, model_name from st_lms_inv_model_master");
			result = pstmt.executeQuery();
			while (result.next()) {
				modelMap.put(result.getInt("model_id") + "-"
						+ result.getInt("brand_id"), result
						.getString("model_name"));
			}
			logger.debug("modelMap = " + modelMap);
		
			// fetch model specification list from database
			pstmt = conn
					.prepareStatement("select inv_model_id, inv_id, concat(spec_value, '-', spec_unit, '-', spec_type) 'model_name' from st_lms_cons_inv_specification ");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				invSpecMap.put(rs.getInt("inv_model_id") + "-"
						+ rs.getInt("inv_id"), rs.getString("model_name"));
			}
			logger.debug("invSpecMap = " + invSpecMap);
		
			map.put("retMap", retMap);
			map.put("invSpecMap", invSpecMap);
			map.put("modelMap", modelMap);
		
			map.putAll(fetchInvDetails());
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return map;
	}

	public Map<String, Map<String, String>> fetchConsInvNModelSpecDetail(
			String invType,String cntrlType) throws LMSException {

		Connection con = DBConnect.getConnection();
		Map<String, Map<String, String>> invDetMap = new TreeMap<String, Map<String, String>>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = null;

			Map<String, String> invMap = new TreeMap<String, String>();
			Map<String, String> brandMap = new TreeMap<String, String>();
			Map<String, String> modelMap = new TreeMap<String, String>();
			Map<String, String> modelMapForBindingLength = new TreeMap<String, String>();
			Map<String, String> invSpecMap = new TreeMap<String, String>();
			Map<String, String> invCodeMap = new TreeMap<String, String>();
			// fetch inventory name list from database
			
			String invQry ="select inv_id, inv_name, inv_type from st_lms_inv_master where inv_type = '"+ invType + "'" ;
			if("N".contentEquals(cntrlType)){
				invQry="select inv_id, inv_name, inv_type from st_lms_inv_master where inv_type = '"+ invType + "' and is_usr_cntrl='N'" ;
				
			}else if("Y".contentEquals(cntrlType)){
				invQry ="select inv_id, inv_name, inv_type from st_lms_inv_master where inv_type = '"+ invType + "' and is_usr_cntrl='Y'" ;
				
			}
			rs = stmt
					.executeQuery(invQry);
			while (rs.next()) {
				invMap.put(rs.getInt("inv_id") + "", rs.getString("inv_name"));
			}
			logger.debug("invMap = " + invMap);

			if ("NON_CONS".equalsIgnoreCase(invType.trim())) {

				// fetch brand name list from database
				rs = stmt
						.executeQuery("select brand_id, inv_id, brand_name from st_lms_inv_brand_master");
				while (rs.next()) {
					brandMap.put(rs.getInt("brand_id") + "-"
							+ rs.getInt("inv_id"), rs.getString("brand_name"));
				}
				logger.debug("brandMap = " + brandMap);

				// fetch model name list from database
				rs = stmt
						.executeQuery("select model_id, brand_id, model_name, check_binding_length,is_inv_code_req from st_lms_inv_model_master");
				while (rs.next()) {
					modelMap
							.put(rs.getInt("model_id") + "-"
									+ rs.getInt("brand_id"), rs
									.getString("model_name"));
					
					modelMapForBindingLength
					.put(rs.getInt("model_id") + "-"
							+ rs.getInt("brand_id"), rs
							.getString("check_binding_length"));
					invCodeMap.put(rs.getInt("model_id")+ "-"
							+ rs.getInt("brand_id"), rs.getString("is_inv_code_req"));
					
				}
				logger.debug("modelMap = " + modelMap);

			} else {
				// fetch model specification list from database
				rs = stmt
						.executeQuery("select inv_model_id, inv_id, concat(spec_value, '-', spec_unit, '-', spec_type) 'model_name' from st_lms_cons_inv_specification ");
				while (rs.next()) {
					invSpecMap.put(rs.getInt("inv_model_id") + "-"
							+ rs.getInt("inv_id"), rs.getString("model_name"));
				}
				logger.debug("invSpecMap = " + invSpecMap);
			}

			invDetMap.put("invMap", invMap);
			invDetMap.put("invSpecMap", invSpecMap);
			invDetMap.put("brandMap", brandMap);
			invDetMap.put("modelMap", modelMap);
			invDetMap.put("modelMapBindingLength", modelMapForBindingLength);
			invDetMap.put("invCodeMap", invCodeMap);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return invDetMap;
	}

	public Map<String, HashMap<String, String>> fetchInvDetails()
			throws LMSException {

		Connection con = DBConnect.getConnection();
		Map<String, HashMap<String, String>> invDetMap = new LinkedHashMap<String, HashMap<String, String>>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = null;

			// fetch inventory name list from database
			rs = stmt
					.executeQuery("select inv_id, inv_name, inv_type from st_lms_inv_master");
			HashMap<String, String> nonConsInvMap = new LinkedHashMap<String, String>();
			HashMap<String, String> consInvMap = new LinkedHashMap<String, String>();
			while (rs.next()) {
				if ("NON_CONS"
						.equalsIgnoreCase(rs.getString("inv_type").trim())) {
					nonConsInvMap.put(rs.getInt("inv_id") + "", rs
							.getString("inv_name"));
				} else {
					consInvMap.put(rs.getInt("inv_id") + "", rs
							.getString("inv_name"));
				}
			}

			// fetch inventory name list from database
			rs = stmt
					.executeQuery("select brand_id, inv_id, brand_name from st_lms_inv_brand_master");
			HashMap<String, String> brandMap = new LinkedHashMap<String, String>();
			while (rs.next()) {
				brandMap.put(rs.getInt("brand_id") + "-" + rs.getInt("inv_id"),
						rs.getString("brand_name"));
			}

			invDetMap.put("nonConsInvMap", nonConsInvMap);
			invDetMap.put("consInvMap", consInvMap);
			invDetMap.put("brandMap", brandMap);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return invDetMap;
	}

	
	
	public Map<String,String> fetchAgentModuleSerial(String modelid,int orgId) throws LMSException {

		Connection con = DBConnect.getConnection();
		
		Map<String, String> agentModelSerialMap = new LinkedHashMap<String, String>();
				
		try {
			
			String orgCodeQry = "  name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode ";


			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode ";
			

			}
			String fetchInvDetQuery="select "+orgCodeQry+",organization_id,serial_no,current_owner_type, parent_id,inv_model_id " +
					"from st_lms_organization_master inner join st_lms_inv_status on " +
					"current_owner_type=organization_type and current_owner_id=organization_id where" +
					" organization_type in('RETAILER','AGENT')and parent_id="+orgId +" and inv_model_id='"+modelid +"' or organization_id ="+orgId +" and inv_model_id='"+modelid +"'  order by "+QueryManager.getAppendOrgOrder();
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(fetchInvDetQuery);
			StringBuilder agentSerial=new StringBuilder();
		
			while (rs.next()) {
				if("agent".equalsIgnoreCase(rs.getString("current_owner_type"))){
					agentSerial.append(rs.getString("serial_no")+", ");
					agentModelSerialMap.put(rs.getString("current_owner_type")+"-" + rs.getString("orgCode"), agentSerial.toString());
				}else{
				agentModelSerialMap.put(rs.getString("current_owner_type")+"-" + rs.getString("orgCode"), rs.getString("serial_no"));
				}
				
				logger.debug("agentModelSerialMap = " + agentModelSerialMap);
				
				
			}
			
			
			
			logger.debug("agentModelSerialMap = " + agentModelSerialMap);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return agentModelSerialMap;
	}
	
	public Map<String, TreeMap<String, String>> fetchTerminalRetailerInvntoryCount(int orgId) throws LMSException {

		Connection con = DBConnect.getConnection();
		List<ConsNNonConsBean> invDetList = new ArrayList<ConsNNonConsBean>();
		Map<String, String> modelMap = new TreeMap<String, String>();
		
		Map<String,TreeMap<String,String>> retailerCountMap = new LinkedHashMap<String,TreeMap<String,String>>();
		try {
			PreparedStatement pstmt;
			pstmt = con
			.prepareStatement("select model_id, model_name from st_lms_inv_model_master where inv_id=1");
			ResultSet rs = pstmt.executeQuery();
	while (rs.next()) {
		modelMap.put(rs.getString("model_id") , rs
				.getString("model_name"));
	}
	logger.debug("modelMap = " + modelMap);
	String orgCodeQry = "  name orgCode ";

	
	if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
		orgCodeQry = " org_code orgCode ";


	} else if ((LMSFilterDispatcher.orgFieldType)
			.equalsIgnoreCase("CODE_NAME")) {
		orgCodeQry = " concat(org_code,'_',name)  orgCode ";
	

	} else if ((LMSFilterDispatcher.orgFieldType)
			.equalsIgnoreCase("NAME_CODE")) {
		orgCodeQry = " concat(name,'_',org_code)  orgCode ";
	

	}
			
			String fetchInvDetQuery="select orgCode,organization_id,a.serial_no,a.model_name,a.current_owner_type,a.parent_id,a.inv_model_id,a.count from(select "+orgCodeQry+",serial_no,(select model_name from st_lms_inv_model_master where model_id=inv_model_id) model_name,current_owner_type, parent_id,organization_id,inv_model_id,count(serial_no) count from st_lms_organization_master inner join st_lms_inv_status on current_owner_type=organization_type and" +
					" current_owner_id=organization_id where organization_type in('RETAILER','AGENT')and parent_id="+orgId+" " +
					" or current_owner_id="+orgId+" group by current_owner_id,inv_model_id) a inner join st_lms_inv_model_master b on b.model_id = a.inv_model_id where b.inv_id=1 order by "+QueryManager.getAppendOrgOrder();
			Statement stmt = con.createStatement();
			 rs = stmt.executeQuery(fetchInvDetQuery);
			
				
			while (rs.next()) {
				
				Map<String, String> modelCountMap = new TreeMap<String,String>();
				
				for (Map.Entry<String, String> entry : modelMap.entrySet()) {
			        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			        if(rs.getString("model_name").equals(entry.getValue())){       	
			        	    
			        	modelCountMap.put(rs.getString("inv_model_id"),rs.getString("count"));
			        	boolean agentCount=retailerCountMap.containsKey(rs.getString("orgCode"));
						if(!agentCount){
							retailerCountMap.put(rs.getString("orgCode"), (TreeMap<String, String>) modelCountMap);
			        	}else{
			        		
			        		retailerCountMap.get(rs.getString("orgCode")).putAll(modelCountMap);
			        	}	
			        }
			        	
			    }
				for (Map.Entry<String, String> entry : modelMap.entrySet()) {
			        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			        if(!(retailerCountMap.get(rs.getString("orgCode")).containsKey(entry.getKey()))){ 
			        	retailerCountMap.get(rs.getString("orgCode")).put(entry.getKey(), null);
			        
			        }
			}
			
			
			}
			logger.debug("retailerCountMap = " + retailerCountMap);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return retailerCountMap;
	}
	
	
	public Map<String,TreeMap<String,String>> fetchTerminalInvntoryCount() throws LMSException {

		Connection con = DBConnect.getConnection();
		List<ConsNNonConsBean> invDetList = new ArrayList<ConsNNonConsBean>();
		TreeMap<String, String> modelMap = new TreeMap<String, String>();
		
		Map<String,TreeMap<String,String>> agentCountMap = new LinkedHashMap<String,TreeMap<String,String>>();
		try {
			PreparedStatement pstmt;
			pstmt = con
			.prepareStatement("select model_id, model_name from st_lms_inv_model_master where inv_id = 1");
			ResultSet rs = pstmt.executeQuery();
	while (rs.next()) {
		modelMap.put(rs.getString("model_id") , rs
				.getString("model_name"));
	}
	logger.debug("modelMap = " + modelMap);
	String orgCodeQry = "  name orgCode ";

	if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
		orgCodeQry = " org_code orgCode ";

	} else if ((LMSFilterDispatcher.orgFieldType)
			.equalsIgnoreCase("CODE_NAME")) {
		orgCodeQry = " concat(org_code,'_',name)  orgCode ";
	

	} else if ((LMSFilterDispatcher.orgFieldType)
			.equalsIgnoreCase("NAME_CODE")) {
		orgCodeQry = " concat(name,'_',org_code)  orgCode ";
	

	}
	
	
	
			String fetchInvBoDet="select a.orgCode,a.model_name,a.organization_id orgId,a.inv_model_id,a.count,a.current_owner_type user_org_type from(select "+orgCodeQry+",(select model_name from st_lms_inv_model_master where model_id=inv_model_id) model_name, organization_id,inv_model_id,count(serial_no) count,current_owner_type from st_lms_organization_master inner join st_lms_inv_status on current_owner_type=organization_type" +
					" and current_owner_id=organization_id where organization_type='BO' group by current_owner_id,inv_model_id) a inner join st_lms_inv_model_master b on b.model_id = a.inv_model_id where b.inv_id=1 order by "+QueryManager.getAppendOrgOrder(); 
			Statement stmt = con.createStatement();
			 rs = stmt.executeQuery(fetchInvBoDet);
			 while (rs.next()) {
					TreeMap<String, String> modelCountMap = null;
					
					for (Map.Entry<String, String> entry : modelMap.entrySet()) {
				        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				        if(rs.getString("model_name").equals(entry.getValue())){       	
				        	    
				        	
				        	boolean agentCount=agentCountMap.containsKey(rs.getString("orgCode")+"Nxt"+rs.getString("user_org_type")+"Nxt"+rs.getInt("orgId"));
							if(!agentCount){
								modelCountMap = new TreeMap<String,String>();
								modelCountMap.put(rs.getString("inv_model_id"),rs.getString("count"));
								agentCountMap.put(((rs.getString("orgCode")+"Nxt"+rs.getString("user_org_type")+"Nxt"+rs.getInt("orgId"))), modelCountMap);
				        	}else{
				        		modelCountMap=agentCountMap.get((rs.getString("orgCode")+"Nxt"+rs.getString("user_org_type")+"Nxt"+rs.getInt("orgId")));
				        		modelCountMap.put(rs.getString("inv_model_id"),rs.getString("count"));
				        		agentCountMap.get((rs.getString("orgCode")+"Nxt"+rs.getString("user_org_type")+"Nxt"+rs.getInt("orgId"))).putAll(modelCountMap);
				        	}	
				        }
				        	
				    }
					for (Map.Entry<String, String> entry : modelMap.entrySet()) {
				        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				        if(!(agentCountMap.get(rs.getString("orgCode")+"Nxt"+rs.getString("user_org_type")+"Nxt"+rs.getInt("orgId")).containsKey(entry.getKey()))){ 
				        	agentCountMap.get((rs.getString("orgCode")+"Nxt"+rs.getString("user_org_type")+"Nxt"+rs.getInt("orgId"))).put(entry.getKey(), null);
				        
				        }
				}
				 
			 }
			
			
			String fetchInvDetQuery="select a.orgCode,a.model_name,a.organization_id orgId,a.inv_model_id,a.count,a.current_owner_type from(select (select  "+orgCodeQry+" from st_lms_organization_master where organization_id=invTlb.organization_id) orgCode," +
					"organization_id,inv_model_id,sum(count) count, (select model_name from st_lms_inv_model_master where model_id=invTlb.inv_model_id) model_name,current_owner_type " +
					"from (select  "+orgCodeQry+", organization_id,inv_model_id,count(serial_no) count,current_owner_type from st_lms_organization_master inner join st_lms_inv_status on current_owner_type=organization_type" +
					" and current_owner_id=organization_id where organization_type='AGENT' group by current_owner_id,inv_model_id union all select  "+orgCodeQry+", parent_id,inv_model_id,count(inv_model_id)" +
					" count,current_owner_type from st_lms_organization_master inner join st_lms_inv_status on current_owner_type=organization_type and current_owner_id=organization_id where organization_type='RETAILER'" +
					" group by current_owner_id,inv_model_id) invTlb group by organization_id,inv_model_id) a inner join st_lms_inv_model_master b on b.model_id = a.inv_model_id where b.inv_id=1 order by "+QueryManager.getAppendOrgOrder();
		 stmt = con.createStatement();
			 rs = stmt.executeQuery(fetchInvDetQuery);
			
			
			while (rs.next()) {
				
				TreeMap<String, String> modelCountMap = null;
				
				for (Map.Entry<String, String> entry : modelMap.entrySet()) {
			        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			        if(rs.getString("model_name").equals(entry.getValue())){       	
			        	    
			        	
			        	boolean agentCount=agentCountMap.containsKey(rs.getString("orgCode")+"Nxt"+rs.getInt("orgId"));
						if(!agentCount){
							modelCountMap = new TreeMap<String,String>();
							modelCountMap.put(rs.getString("inv_model_id"),rs.getString("count"));
							agentCountMap.put((rs.getString("orgCode")+"Nxt"+rs.getInt("orgId")), modelCountMap);
			        	}else{
			        		modelCountMap=agentCountMap.get((rs.getString("orgCode")+"Nxt"+rs.getInt("orgId")));
			        		modelCountMap.put(rs.getString("inv_model_id"),rs.getString("count"));
			        		agentCountMap.get((rs.getString("orgCode")+"Nxt"+rs.getInt("orgId"))).putAll(modelCountMap);
			        	}	
			        }
			        	
			    }
				for (Map.Entry<String, String> entry : modelMap.entrySet()) {
			        System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			        if(!(agentCountMap.get((rs.getString("orgCode")+"Nxt"+rs.getInt("orgId"))).containsKey(entry.getKey()))){ 
			        	agentCountMap.get((rs.getString("orgCode")+"Nxt"+rs.getInt("orgId"))).put(entry.getKey(), null);
			        
			        }
			}
			
			
			}
			logger.debug("agentCountMap = " + agentCountMap);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return agentCountMap;
	}
	
	public TreeMap<String,String> fetchModelInvntory() throws LMSException {

		Connection con = DBConnect.getConnection();
		TreeMap<String, String> modDetMap = new TreeMap<String, String>();

		try {
			//String fetchInvDetQuery = createQuery(ownerType, invType, agtOrgId,
				//	retOrgId, invId, brandId, modelId);
			String fetchModelDetQuery="select model_id, model_name from st_lms_inv_model_master where inv_id = 1";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(fetchModelDetQuery);
			String modelName;

			while (rs.next()) {
			
				
				modDetMap.put(rs.getString("model_id") , rs
						.getString("model_name"));
							
			}
			logger.debug("modDetMap = " + modDetMap);
			

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return modDetMap;
	}
	public List<ConsNNonConsBean> fetchInvntoryCount(String ownerType,
			int agtOrgId, int retOrgId, String invType, int invId, int brandId,
			String modelId, String sign, Integer count) throws LMSException {

		Connection con = DBConnect.getConnection();
		List<ConsNNonConsBean> invDetList = new ArrayList<ConsNNonConsBean>();

		try {
			String fetchInvDetQuery = createQuery(ownerType, invType, agtOrgId,
					retOrgId, invId, brandId, modelId);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(fetchInvDetQuery);
			ConsNNonConsBean bean = null;

			while (rs.next()) {
				bean = new ConsNNonConsBean();
				bean.setOwnerId(rs.getInt("current_owner_id"));
				bean.setOwnerName(rs.getString("orgCode"));
				bean.setOwnerType(rs.getString("current_owner_type"));
				bean.setInvId(rs.getInt("inv_id"));
				bean.setInvName(rs.getString("inv_name"));
				bean.setBrandId(brandId);
				bean.setModelId(Integer.parseInt(modelId));
				bean.setInvType(invType.trim());
				bean.setCount(rs.getInt("inv_count"));
				bean.setCost(rs.getDouble("cost") * rs.getInt("inv_count"));
				if (isValid(sign, count, rs.getInt("inv_count"))) {
					invDetList.add(bean);
				}
			}
			logger.debug("invDetList = " + invDetList);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return invDetList;
	}
	
	public List<ConsNNonConsBean> fetchInvntoryCountAtAgent(String ownerType,
			int agtOrgId, int retOrgId, String invType, int invId, int brandId,
			String modelId, String sign, Integer count) throws LMSException {

		Connection con = DBConnect.getConnection();
		List<ConsNNonConsBean> invDetList = new ArrayList<ConsNNonConsBean>();

		try {
			String fetchInvDetQuery = createQueryAtAgent(ownerType, invType, agtOrgId,
					retOrgId, invId, brandId, modelId);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(fetchInvDetQuery);
			ConsNNonConsBean bean = null;

			while (rs.next()) {
				bean = new ConsNNonConsBean();
				bean.setOwnerId(rs.getInt("current_owner_id"));
				bean.setOwnerName(rs.getString("orgCode"));
				bean.setOwnerType(rs.getString("current_owner_type"));
				bean.setInvId(rs.getInt("inv_id"));
				bean.setInvName(rs.getString("inv_name"));
				bean.setBrandId(brandId);
				bean.setModelId(Integer.parseInt(modelId));
				bean.setInvType(invType.trim());
				bean.setCount(rs.getInt("inv_count"));
				bean.setCost(rs.getDouble("cost") * rs.getInt("inv_count"));
				if (isValid(sign, count, rs.getInt("inv_count"))) {
					invDetList.add(bean);
				}
			}
			logger.debug("invDetList = " + invDetList);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return invDetList;
	}

	public List<ConsNNonConsBean> fetchInvntoryWiseDetail(int ownerId,
			String invType, int invId, int brandId, String modelId)
			throws LMSException {

		Connection con = DBConnect.getConnection();
		List<ConsNNonConsBean> invDetList = new ArrayList<ConsNNonConsBean>();

		try {
			String fetchInvDetQuery = createQueryToFetchDetails(ownerId,
					invType, invId, brandId, modelId);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(fetchInvDetQuery);
			ConsNNonConsBean bean = null;

			while (rs.next()) {
				bean = new ConsNNonConsBean();
				bean.setOwnerName(rs.getString("name"));
				bean.setOwnerType(rs.getString("owner"));
				// bean.setInvId(rs.getInt("inv_id"));
				bean.setInvName(rs.getString("inv_name"));
				// bean.setBrandId(brandId);
				bean.setBrandName(rs.getString("brand_name"));
				// bean.setModelId(Integer.parseInt(modelId));
				bean.setModelName(rs.getString("model_name"));
				bean.setSerialNo(rs.getString("serial_no"));
				bean.setInvType(invType.trim());
				bean.setCount(rs.getInt("count"));
				bean.setCost(rs.getDouble("cost") * rs.getInt("count"));
				if ("NON_CONS".equalsIgnoreCase(invType.trim())){
					bean.setInvCode(rs.getString("inv_code"));
				}
				invDetList.add(bean);

			}
			logger.debug("invDetList = " + invDetList);

		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);

		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return invDetList;
	}

	public Map<String, String> fetchRetList(int userOrgId) throws LMSException {
		Connection conn =null;
		Map<String, String> retMap = new LinkedHashMap<String, String>();

		try {
			conn = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			ResultSet result = null;
			String orgId = null, orgName = null;

			// fetch retailer list
			pstmt = conn
					.prepareStatement("select name, organization_id from st_lms_organization_master where organization_type='RETAILER' and parent_id="
							+ userOrgId);
			System.out.println("retailer query is :::::::" + pstmt);
			result = pstmt.executeQuery();
			while (result.next()) {
				orgId = result.getInt(TableConstants.ORG_ID) + "";
				orgName = result.getString(TableConstants.NAME);
				retMap.put(orgId, orgName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return retMap;
	}

	// method added by amit for agent end....
	public Map<String, String> fetchRetListForAsnInv(int userOrgId)
			throws LMSException {
		Connection conn =null;
		Map<String, String> retMap = new LinkedHashMap<String, String>();

		try {
			conn = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			ResultSet result = null;
			String orgId = null, orgName = null;

			// fetch retailer list
			pstmt = conn
					.prepareStatement("select name, organization_id from st_lms_organization_master as ret "
							+ " where organization_type='RETAILER' and parent_id= "
							+ userOrgId
							+ " and ret.organization_id in (select organization_id from st_lms_ret_offline_master where serial_number=-1 )");
			System.out.println("retailer query is :::::::" + pstmt);
			result = pstmt.executeQuery();
			while (result.next()) {
				orgId = result.getInt(TableConstants.ORG_ID) + "";
				orgName = result.getString(TableConstants.NAME);
				retMap.put(orgId, orgName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return retMap;
	}

	public String fetchRetailerForReassignInv(int userOrgId) throws LMSException {
		Connection conn = null;
		StringBuilder orgList = new StringBuilder();

		try {
			conn = DBConnect.getConnection();
			PreparedStatement pstmt = null;
			ResultSet result = null;
			//String orgId = null, orgName = null;
			String orgCodeQry = "  name orgCode ";

			
			if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
				orgCodeQry = " org_code orgCode ";
	

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("CODE_NAME")) {
				orgCodeQry = " concat(org_code,'_',name)  orgCode ";
			

			} else if ((LMSFilterDispatcher.orgFieldType)
					.equalsIgnoreCase("NAME_CODE")) {
				orgCodeQry = " concat(name,'_',org_code)  orgCode  ";
			

			}	
			
			
			// fetch retailer list
			pstmt = conn
					.prepareStatement("select "+orgCodeQry+", organization_id from st_lms_organization_master as ret "
							+ " where organization_type='RETAILER' and organization_status !='TERMINATE' and parent_id= "
							+ userOrgId
							+ " and ret.organization_id in (select organization_id from st_lms_ret_offline_master where serial_number=-1 or sim1 =-1  or sim2 =-1 or sim3 =-1 ) order by "+QueryManager
							.getAppendOrgOrder());
			System.out.println("retailer query is :::::::" + pstmt);
			result = pstmt.executeQuery();
			while (result.next()) {
				orgList.append(result.getString("orgCode")).append("|").append(result.getString("organization_id")).append(":"); 
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return orgList.toString();
	}
	
	//public Map<String,String> fetchSerNoList(String[] serNo,String[] invCode, String serNoFileName,int bindLength,String modelId) throws  LMSException {
	public Map<String,InvDetailBean> fetchInvMap(String[] serNo,String[] invCode, String serNoFileName,int modelId,int bindLength,boolean isInvCodeReq) throws  LMSException {
		//Map<String, HashSet<String>> map = new HashMap<String,HashSet<String>>();
		// List<Map<String,InvDetailBean>> mapList = new ArrayList<Map<String,InvDetailBean>>();
		Map<String,InvDetailBean>  invMap =new HashMap<String,InvDetailBean>();
		// Map<String,InvDetailBean>  duplicateInvMap =null;
		Set<String> dupSerNoSet = new HashSet<String>();
		Set<String> dupInvCodeSet = new HashSet<String>();
		// insert image into DB
		
		try {
			
			for (int i=0;i<serNo.length;i++ ) {
				   InvDetailBean invBean =new InvDetailBean();
				if (!"".equals(serNo[i].trim())) {
					if(serNo[i].length() < bindLength){
						
						throw new LMSException(LMSErrors.INVALID_INV_BIND_LENGTH_ERROR_CODE,LMSErrors.INVALID_INV_BIND_LENGTH_ERROR_MESSAGE );
						
					}
				   if(bindLength > 0){
					 	if (!dupSerNoSet.add(serNo[i].substring(serNo[i].length()-bindLength, serNo[i].length()))) {
					 		//dupSerNoSet.add(dupSerNoSet.substring(serNo[i].length()-bindLength,serNo[i].length()));
					 		 throw new LMSException(LMSErrors.DUP_INV_ERROR_CODE,LMSErrors.DUP_INV_ERROR_MESSAGE);
					 	}
					}else if (bindLength == 0){
						if (!dupSerNoSet.add(serNo[i])) {
							 throw new LMSException(LMSErrors.DUP_INV_ERROR_CODE,LMSErrors.DUP_INV_ERROR_MESSAGE);
						}
					}
				   if(isInvCodeReq){
					   if(invCode[i]==null|| "".equals(invCode[i].trim())){
						   
						   throw new LMSException(LMSErrors.INVALID_INV_ERROR_CODE,LMSErrors.INVALID_INV_ERROR_MESSAGE );
					   }else{
						   
						   if(!dupInvCodeSet.add(invCode[i].trim())){
							   
							   throw new LMSException(LMSErrors.DUP_INV_ERROR_CODE,LMSErrors.DUP_INV_ERROR_MESSAGE);
						   }
						   
					   }
					   
					   invBean.setCode(invCode[i].trim().replaceAll("\\s+","").toUpperCase());
				   }
				
				   invBean.setSerialNo(serNo[i].trim().replaceAll("\\s+","").toUpperCase());
				   invMap.put(invBean.getSerialNo(), invBean);
				}
			}
			logger.debug("SerialNo File Name: " + serNoFileName);
			try{
				if (serNoFileName != null && !"".equals(serNoFileName.trim())) {
					File file = new File(serNoFileName);
					FileReader fstream = new FileReader(file);
					BufferedReader br = new BufferedReader(fstream);
					String strLine;
					while ((strLine = br.readLine()) != null) {
						if (!"".equals(strLine.trim())) {
							InvDetailBean invBean =new InvDetailBean();	
						//allSerNo.add(strLine.trim().replaceAll("\\s+","").toUpperCase());
						String str[] =strLine.split(" ",2);
							if(!"".equals(str[0].trim())){
								if(str[0].length() < bindLength){
									
									throw new LMSException(LMSErrors.INVALID_INV_BIND_LENGTH_ERROR_CODE,LMSErrors.INVALID_INV_BIND_LENGTH_ERROR_MESSAGE );
									
								}
								if(bindLength > 0){
								 	if (!dupSerNoSet.add(str[0].substring(str[0].length()-bindLength, str[0].length()))) {
								 		//dupSerNoSet.add(dupSerNoSet.substring(serNo[i].length()-bindLength,serNo[i].length()));
								 		 throw new LMSException(LMSErrors.DUP_INV_ERROR_CODE,LMSErrors.DUP_INV_ERROR_MESSAGE);
								 	}
								}else if (bindLength == 0){
									if (!dupSerNoSet.add(str[0])) {
										 throw new LMSException(LMSErrors.DUP_INV_ERROR_CODE,LMSErrors.DUP_INV_ERROR_MESSAGE);
									}
								}
								   if(isInvCodeReq){
									   if(str[1]==null|| "".equals(str[1].trim())){
										   
										   throw new LMSException(LMSErrors.INVALID_INV_ERROR_CODE,LMSErrors.INVALID_INV_ERROR_MESSAGE );
									   }else{
										   
										   if(!dupInvCodeSet.add(str[1].trim())){
											   
											   throw new LMSException(LMSErrors.DUP_INV_ERROR_CODE,LMSErrors.DUP_INV_ERROR_MESSAGE);
										   }
										   
									   }
									   
									   invBean.setCode(str[1].trim().replaceAll("\\s+","").toUpperCase());
								   }
								
								
								
							}
							
						   //invMap.put(str[0].trim().replaceAll("\\s+","").toUpperCase(), str[1].trim().replaceAll("\\s+","").toUpperCase());
							
						   invBean.setSerialNo(str[0].trim().replaceAll("\\s+","").toUpperCase());
						   invMap.put(invBean.getSerialNo(), invBean);	
						}
					}
				}
			}catch(LMSException e){
				throw  e;
			}catch (FileNotFoundException e) {
				//logger.error("Exception: ",e);
			}catch (IOException e) {
				logger.error("Exception: ",e);
			} catch (Exception e) {
				logger.error("Exception: ",e);
			} 
			
			if(invMap.size() == 0){
				
				  throw new LMSException(LMSErrors.INV_LIST_EMPTY_ERROR_CODE,LMSErrors.INV_LIST_EMPTY_ERROR_MESSAGE);
				
			}
		
		
			
		}catch (LMSException e) {
			logger.error("LMSException: ",e);
			throw e;
		}catch (Exception e) {
			logger.error("Exception: ",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}		
		return invMap;
	}
	
	public Map<String, HashSet<String>> checkForDuplicates(List<String> allSerNo,int bind_len){
		
		Map<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		HashSet<String> returnedSet = null;
		returnedSet = (HashSet<String>)findDuplicates(allSerNo, bind_len);		
		map.put("correct", new HashSet<String>(allSerNo));
		map.put("duplicate", returnedSet);
		return map;
	}

	public String invListForRet(int retOrgId) throws LMSException {
		//ArrayList<String> invList = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		/*String fetchInvQry = "select CONCAT_WS('-',slim.inv_name,slimm.model_name,serial_no) as terminal"
				+ " from st_lms_inv_status slis,st_lms_inv_master slim,st_lms_inv_model_master slimm "
				+ "where current_owner_type='RETAILER' and current_owner_id = "
				+ retOrgId
				+ " and slis.inv_model_id =slimm.model_id   and slim.inv_id= slimm.inv_id";*/
		
		String fetchInvQry ="select slimm.model_id as modelId,serial_no  srNO,model_name  from st_lms_inv_status slis,st_lms_inv_master slim,st_lms_inv_model_master slimm where current_owner_type='RETAILER' and current_owner_id =?  and slis.inv_model_id =slimm.model_id   and slim.inv_id= slimm.inv_id";
		try {
			con =DBConnect.getConnection();
			psmt = con.prepareStatement(fetchInvQry);
			psmt.setInt(1,retOrgId);
			logger.debug("fetchInvQry==" + fetchInvQry);
			rs = psmt.executeQuery();
			while (rs.next()) {
				sb.append(rs.getString("model_name")+"-"+rs.getString("srNO")+"-"+rs.getInt("modelId")+";");
			}
			logger.debug("List is==" + sb.toString());
			
		}catch (SQLException e) {
			logger.error("SQL Exception",e);
			 //e.printStackTrace();
		}catch (Exception e) {
			logger.error("Exception",e);
		}finally{
			DBConnect.closeConnection(con, psmt, rs);
		}
		return sb.toString();
	}

	public boolean isValid(String sign, Integer count, int invCount) {
		boolean flag = true;

		if (count != null && sign != null && !"".equals(sign.trim())) {
			if ("<=".equals(sign.trim())) {
				if (invCount <= count) {
					flag = true;
				} else {
					flag = false;
				}
			}
			if (">=".equals(sign.trim())) {
				if (invCount >= count) {
					flag = true;
				} else {
					flag = false;
				}
			}
			if ("==".equals(sign.trim())) {
				if (invCount == count) {
					flag = true;
				} else {
					flag = false;
				}
			}
		}
	
		
		return flag;
	}
/**
 * @deprecated
 * @param modelId
 * @param cost
 * @param isNew
 * @param serNoFileName
 * @param serNo
 * @param userId
 * @param userOrgId
 * @param userType
 * @return
 * @throws LMSException
 */
	public ArrayList nonConsInvUpload(String modelId, double cost,
			String isNew, String serNoFileName, String[] serNo, int userId,
			int userOrgId, String userType) throws LMSException {

		logger.debug("cost per unit = " + cost + ", modelId = " + modelId
				+ ", isNew = " + isNew + "\nserNoFileName = " + serNoFileName
				+ " serNo size = " + serNo.length);
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();

		Connection connection = DBConnect.getConnection();
		ArrayList<String> returnList = new ArrayList<String>();
		boolean emptyFlag = true;
		boolean wrongFile = false;
		try {
			connection.setAutoCommit(false);

			//Set<String> serNoSet = fetchSerNoList(serNo, serNoFileName);
			
			if (modelId != null && !"".equals(modelId.trim()) && cost > 0
					&& serNo.length > 0) {

				int modId = Integer.parseInt(modelId.trim().split("-")[0]);

				// insert into st_lms_inv_status table to add inventory quantity
				String insIntoInvStatusQuery = "insert into st_lms_inv_status(user_org_type, user_org_id, "
						+ " inv_model_id, user_id, serial_no, cost_to_bo, is_new, current_owner_type, "
						+ " current_owner_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?);";
				String chkDupSNo = "select serial_no from st_lms_inv_status where upper(serial_no)=? and inv_model_id = ?";
				ResultSet rs = null;
				PreparedStatement pstmt1 = connection
						.prepareStatement(chkDupSNo);
				PreparedStatement pstmt = connection
						.prepareStatement(insIntoInvStatusQuery);
				pstmt.setString(1, userType);
				pstmt.setInt(2, userOrgId);
				pstmt.setInt(3, modId);
				pstmt.setInt(4, userId);
				pstmt.setDouble(6, cost);
				pstmt.setString(7, isNew);
				pstmt.setString(8, userType);
				pstmt.setInt(9, userOrgId);

				// insert into st_lms_inv_detail table to add inventory quantity
				String insIntoInvDetQuery = "insert into st_lms_inv_detail(user_id,user_org_type,user_org_id,"
						+ " inv_model_id,serial_no,date,cost_to_bo,is_new,current_owner_type,current_owner_id)"
						+ " values(?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pstmt2 = connection
						.prepareStatement(insIntoInvDetQuery);
				pstmt2.setInt(1, userId);
				pstmt2.setString(2, userType);
				pstmt2.setInt(3, userOrgId);
				pstmt2.setInt(4, modId);
				pstmt2.setTimestamp(6, new Timestamp(new java.util.Date()
						.getTime()));
				pstmt2.setDouble(7, cost);
				pstmt2.setString(8, isNew);
				pstmt2.setString(9, userType);
				pstmt2.setInt(10, userOrgId);
		
				int stRowUpd = 0, detRowUpd = 0;
				for (String sNo : serNo) {
					if (emptyFlag && !sNo.trim().equals("")) {// at least one
						// not empty
						emptyFlag = false;
					}
					pstmt1.clearParameters();
					pstmt1.setString(1, sNo.trim().toUpperCase());
					pstmt1.setInt(2, modId);
					rs = pstmt1.executeQuery();
					if (rs.next()) {
						sb1.append(sNo.trim() + ";");
					} else {
						sb.append(sNo.trim() + ";");
						pstmt.setString(5, sNo.trim());
						logger.debug("st_lms_inv_status = " + pstmt);
						stRowUpd += pstmt.executeUpdate();
						pstmt2.setString(5, sNo.trim());
						logger.debug("st_inv_det = " + pstmt2);
						detRowUpd += pstmt2.executeUpdate();
					}
				}
				if (emptyFlag || detRowUpd == 0) {
					// present and at least one non
					// duplicate is not added,ie all
					// duplicate entered
					connection.rollback();
				} else {
					connection.commit();
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
			wrongFile = true;
			// throw new LMSException(e);
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
		String invListStr = sb.toString();
		String notuploadedinvListStr = sb1.toString();
		logger.debug("notuploadedinvListStr " + notuploadedinvListStr);

		returnList.add(0, notuploadedinvListStr);
		if (emptyFlag) {
			returnList.add(1, "error");
		} else if (wrongFile) {
			returnList.add(1, "fileError");
		} else {
			returnList.add(1, "");
		}

		if ("".equals(invListStr)) {
			returnList.add(2, "NO Specification");
			// return "NO Specification";
		} else {
			returnList.add(2, invListStr);
			// return invListStr;
		}

		return returnList;
	}
	public ArrayList nonConsInvUpload(String[] serNo,String[] invCode, String serNoFileName,int modelId, double cost,
			String isNew,Map<String,InvDetailBean> invMap, int userId,
			int userOrgId, String userType) throws LMSException {

		logger.debug("cost per unit = " + cost + ", modelId = " + modelId
				+ ", isNew = " + isNew);
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();

		Connection con = null;
		ArrayList<String> returnList = new ArrayList<String>();
		boolean emptyFlag = true;
		boolean wrongFile = false;
		boolean isInvCodeReq = false ; // get InveCodeReq For Model
		int bindLength =0;
		ResultSet rs =null;
		Map<String,InvDetailBean> duplicateInvMap = null;
		try {
			con =DBConnect.getConnection();
			String query = "select check_binding_length,is_inv_code_req from st_lms_inv_model_master where model_id=? ";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1,modelId);
			logger.info(pstmt);
			rs=pstmt.executeQuery();
			if(rs.next()){
				isInvCodeReq = rs.getString("is_inv_code_req").equalsIgnoreCase("Y")? true:false ;
				bindLength=rs.getInt("check_binding_length");
			}else{
				logger.debug("Inv Model Id Not Found");
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			
			//Set<String> serNoSet = fetchSerNoList(serNo, serNoFileName);
			invMap=  fetchInvMap(serNo,invCode, serNoFileName,modelId,bindLength,isInvCodeReq);
			duplicateInvMap=getDuplicateInvetoryMap(invMap,bindLength,modelId,isInvCodeReq,con);
			///	mapList.add(invMap);
			//mapList.add(duplicateInvMap);
			con =DBConnect.getConnection();
			
			con.setAutoCommit(false);
			if (cost > 0 && invMap.size() > 0) {

				
				
				// insert into st_lms_inv_status table to add inventory quantity
				String insIntoInvStatusQuery = "insert into st_lms_inv_status(user_org_type, user_org_id, "
						+ " inv_model_id, user_id, serial_no, cost_to_bo, is_new, current_owner_type, "
						+ " current_owner_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?);";
				// String chkDupSNo = "select invStatus.serial_no serial_no ,inv_code invCode from st_lms_inv_status invStatus  inner join st_lms_inv_mapping  invMap on invStatus.serial_no=invMap.serial_no where invStatus.inv_model_id =? and (upper(invStatus.serial_no)=? or upper(inv_code )=?)";
				// PreparedStatement pstmt1 = con.prepareStatement(chkDupSNo);
				pstmt = con.prepareStatement(insIntoInvStatusQuery);
				pstmt.setString(1, userType);
				pstmt.setInt(2, userOrgId);
				pstmt.setInt(3, modelId);
				pstmt.setInt(4, userId);
				pstmt.setDouble(6, cost);
				pstmt.setString(7, isNew);
				pstmt.setString(8, userType);
				pstmt.setInt(9, userOrgId);

				// insert into st_lms_inv_detail table to add inventory quantity
				String insIntoInvDetQuery = "insert into st_lms_inv_detail(user_id,user_org_type,user_org_id,"
						+ " inv_model_id,serial_no,date,cost_to_bo,is_new,current_owner_type,current_owner_id)"
						+ " values(?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pstmt2 = con
						.prepareStatement(insIntoInvDetQuery);
				pstmt2.setInt(1, userId);
				pstmt2.setString(2, userType);
				pstmt2.setInt(3, userOrgId);
				pstmt2.setInt(4, modelId);
				pstmt2.setTimestamp(6, new Timestamp(new java.util.Date()
						.getTime()));
				pstmt2.setDouble(7, cost);
				pstmt2.setString(8, isNew);
				pstmt2.setString(9, userType);
				pstmt2.setInt(10, userOrgId);
				
				// Insert Inventory Mapping 
				
				String insInvMapp = " insert into st_lms_inv_mapping(inv_model_id,serial_no,inv_code) values(?,?,?) ";
				PreparedStatement pstmt3 = con.prepareStatement(insInvMapp);
				pstmt3.setInt(1, modelId);
		
		
				int stRowUpd = 0, detRowUpd = 0,mapRowUpd=0;
				Iterator<Entry<String,InvDetailBean>> itr = invMap.entrySet().iterator();
				while(itr.hasNext()){
					Entry<String,InvDetailBean> entry =itr.next();
					if (emptyFlag && !entry.getValue().getSerialNo().trim().equals("")) {// at least one
						// not empty
						emptyFlag = false;
					}
					if(isInvCodeReq){
						sb.append(entry.getKey()+","+entry.getValue().getCode()+ ";");
					
						
					} else{
						
						sb.append(entry.getKey()+";");
					}
					//pstmt1.clearParameters();
					//pstmt1.setString(1, entry.getValue().getSerialNo().trim().toUpperCase());
					//pstmt1.setString(2, entry.getValue().getCode().trim().toUpperCase());
					//pstmt1.setInt(3, modelId);
					// rs = pstmt1.executeQuery();
				/*	if (rs.next()) {
					
						 sb1.append(entry.getKey()+","+entry.getValue() + ";");
					} else {*/
						
					
						pstmt.setString(5,  entry.getValue().getSerialNo());
						logger.debug("st_lms_inv_status = " + pstmt);
						stRowUpd += pstmt.executeUpdate();
						pstmt2.setString(5, entry.getValue().getSerialNo());
						logger.debug("st_inv_det = " + pstmt2);
						detRowUpd += pstmt2.executeUpdate();
						pstmt3.setString(2,entry.getValue().getSerialNo());
						pstmt3.setString(3,entry.getValue().getCode());
						logger.debug("st_inv_mapping = " + pstmt3);
						mapRowUpd += pstmt3.executeUpdate();
						logger.debug("mapRowUpd"+mapRowUpd);
					//}
				}
				
		/*		for (String sNo : serNo) {
					if (emptyFlag && !sNo.trim().equals("")) {// at least one
						// not empty
						emptyFlag = false;
					}
					pstmt1.clearParameters();
					pstmt1.setString(1, sNo.trim().toUpperCase());
					pstmt1.setInt(2, modId);
					rs = pstmt1.executeQuery();
					if (rs.next()) {
						sb1.append(sNo.trim() + ";");
					} else {
						sb.append(sNo.trim() + ";");
						pstmt.setString(5, sNo.trim());
						logger.debug("st_lms_inv_status = " + pstmt);
						stRowUpd += pstmt.executeUpdate();
						pstmt2.setString(5, sNo.trim());
						logger.debug("st_inv_det = " + pstmt2);
						detRowUpd += pstmt2.executeUpdate();
					}
				}*/
				if (emptyFlag || detRowUpd == 0 ||stRowUpd==0) {
					// present and at least one non
					// duplicate is not added,ie all
					// duplicate entered
					con.rollback();
				} else {
					con.commit();
				}

			}
		}catch(LMSException e){
			throw  e;
		}catch (SQLException e) {
			logger.error("SQL Exception ", e);
		}catch (Exception e) {
			logger.error("Exception ", e);
			wrongFile = true;
			// throw new LMSException(e);
		} finally {
			DBConnect.closeCon(con);
		}
		String invListStr = sb.toString();
		Iterator<Entry<String,InvDetailBean>> itr = duplicateInvMap.entrySet().iterator();
		while(itr.hasNext()){
			Entry<String,InvDetailBean> entry =itr.next();
			if(isInvCodeReq){
				sb1.append(entry.getValue().getSerialNo()+",").append(entry.getValue().getCode()+";");
				
			}else{
				sb1.append(entry.getValue().getSerialNo()+";");
				
			}
			
			
		}
		String notuploadedinvListStr = sb1.toString();
		logger.debug("notuploadedinvListStr " + notuploadedinvListStr);

		returnList.add(0, notuploadedinvListStr);
		if (emptyFlag) {
			returnList.add(1, "error");
		} else if (wrongFile) {
			returnList.add(1, "fileError");
		} else {
			returnList.add(1, "");
		}

		if ("".equals(invListStr)) {
			returnList.add(2, "NO Specification");
			// return "NO Specification";
		} else {
			returnList.add(2, invListStr);
			// return invListStr;
		}

		return returnList;
	}
	public String reassignNonsRetSave(int retOrgId, String invSrNo,
			int agtOrgId,String modelId, String userType, int agtId) throws LMSException {

		Connection con = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String status = "error";
		String srNo = invSrNo;// serial no of inventory
		int  model  = 0;
	
		String invColName =null;
		String device_type = null;
		
		try {
			
			model  = Integer.parseInt(modelId.split("-")[0]);// device type of inventory
		
			String modelDeatials = "select  inv_column_name, model_name  from st_lms_inv_model_master where model_id=?  " ;
			String assignSerNoQuery = "update st_lms_inv_status set user_org_type = '"+userType+"',user_org_id=?, current_owner_type = ?, current_owner_id = ? where serial_no = ? and current_owner_id = ? and inv_model_id = ?";
			String insIntoInvDetQuery = "insert into st_lms_inv_detail(user_id,user_org_type,user_org_id, inv_model_id,serial_no,date,cost_to_bo,is_new,current_owner_type,current_owner_id) select ?,?,?,inv_model_id,?,?,cost_to_bo, is_new,?,? from st_lms_inv_status where serial_no = ? and inv_model_id = ?";
			con = DBConnect.getConnection();

			psmt =con.prepareStatement(modelDeatials);
			psmt.setInt(1, model);
			rs =psmt.executeQuery();
			if(rs.next()){
				
				invColName =rs.getString("inv_column_name");
				device_type = rs.getString("model_name");
			
			}else{
				
				throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
			}
			DBConnect.closeConnection(psmt, rs);
		
				// Check  Inventory Already Assgined or not
			    String terminalCheck ="select "+invColName+"  from st_lms_ret_offline_master  where organization_id=? ";
				psmt =con.prepareStatement(terminalCheck);
				psmt.setInt(1, retOrgId);
				logger.debug("Terminal Check"+psmt);
				rs =psmt.executeQuery();
				if(rs.next()){
					if(!rs.getString(invColName).equalsIgnoreCase("-1")){
						logger.debug(rs.getString(invColName)+" Inventory Already Assigned To User ");
						throw new LMSException(LMSErrors.INV_ALREADY_ASSIGNED_ERROR_CODE,LMSErrors.INV_ALREADY_ASSIGNED_ERROR_MESSAGE);	
					}
				
				}
				
			
		
			DBConnect.closeConnection(psmt, rs);
			
			con.setAutoCommit(false);
			psmt = con.prepareStatement(assignSerNoQuery);
			psmt.setInt(1, agtOrgId);
			psmt.setString(2, "RETAILER");
			psmt.setInt(3, retOrgId);
			psmt.setString(4, srNo);
			psmt.setInt(5, agtOrgId);
			psmt.setInt(6, model);
			logger.info("update query for inventory::::::" + psmt);
			psmt.executeUpdate();
			DBConnect.closePstmt(psmt);
			PreparedStatement insIntoInvDet = con.prepareStatement(insIntoInvDetQuery);
			insIntoInvDet.setInt(1, agtId);
			insIntoInvDet.setString(2, userType);
			insIntoInvDet.setInt(3, agtOrgId);
			// insIntoInvDet.setInt(4, inv_modelId);
			insIntoInvDet.setString(4, srNo);
			insIntoInvDet.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
			insIntoInvDet.setString(6, "RETAILER");
			insIntoInvDet.setInt(7, retOrgId);
			insIntoInvDet.setString(8, srNo);
			insIntoInvDet.setInt(9, model);
			logger.info("insert query for inventory::::::"+ insIntoInvDet);
			insIntoInvDet.executeUpdate();
			DBConnect.closePstmt(insIntoInvDet);
			String offLoginStatus = "update st_lms_ret_offline_master set "+invColName+" = '"+ srNo + "', device_type = '"+device_type+"'  where organization_id= ?";
			psmt = con.prepareStatement(offLoginStatus);
			psmt.setInt(1,retOrgId );
			logger.info("ret offline master update:" + offLoginStatus);
			psmt.executeUpdate();
			con.commit();
			status = "success";

		} catch (LMSException e){
		  throw e ;	
		}catch (Exception e) {
			logger.error("Exception",e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				logger.error("Exception",e1);
			}
			return "error";
		}finally{
			
			DBConnect.closeConnection(con, psmt);
			
		}
		return status;

	}

	private void returnConsInv(int userOrgId, int userId, String ownerType,
			int agtOrgId, int retOrgId, int[] consInvId, int[] consModelId,
			int[] consQty, Connection conn, String userType,int DNID)
			throws SQLException {

		List<String> validSerNoList = new ArrayList<String>();
		List<String> inValidSerNoList = new ArrayList<String>();

		String fetchDetailsQuery = "select party_org_id, aa.inv_model_id, cumm_qty_count, inv_name, "
				+ " concat(spec_value, '-', spec_unit, '-', spec_type) 'model_name' from st_lms_cons_inv_status "
				+ " aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc where aa.inv_model_id = "
				+ " bb.inv_model_id and bb.inv_id = cc.inv_id ";
		PreparedStatement pstmt = conn.prepareStatement(fetchDetailsQuery);
		ResultSet rs = pstmt.executeQuery();
		Map<String, Long> partyDetMap = new TreeMap<String, Long>();
		Map<Integer, ConsNNonConsBean> invDetMap = new TreeMap<Integer, ConsNNonConsBean>();
		while (rs.next()) {
			partyDetMap.put(rs.getInt("party_org_id") + "-"
					+ rs.getInt("inv_model_id"), rs.getLong("cumm_qty_count"));

		}

		int partyOrgId = "AGENT".equalsIgnoreCase(ownerType.trim()) ? agtOrgId
				: retOrgId;
		int fstRowUpd = -1, scdRowUpd = -1;
		ConsNNonConsBean bean = null;
		for (int i = 0, len = consModelId.length; i < len; i = i + 1) {
			if (consInvId[i] > 0 && consModelId[i] > 0 && consQty[i] > 0) {

				if (partyDetMap.get(partyOrgId + "-" + consModelId[i]) != null
						&& partyDetMap.get(partyOrgId + "-" + consModelId[i]) >= consQty[i]) {

					// update st_lms_cons_inv_status table to add inventory
					// quantity
					String redInvStatusQuery = "update st_lms_cons_inv_status set cumm_qty_count=cumm_qty_count-?"
							+ " where inv_model_id =? and party_org_id = ? and cumm_qty_count>=?";
					pstmt = conn.prepareStatement(redInvStatusQuery);
					pstmt.setLong(1, consQty[i]);
					pstmt.setInt(2, consModelId[i]);
					pstmt.setInt(3, partyOrgId);
					pstmt.setLong(4, consQty[i]);
					logger.debug(pstmt);
					fstRowUpd = pstmt.executeUpdate();

					String insIntoUserInvDetailQuery = "insert into st_lms_cons_inv_detail(user_id, user_org_id, "
							+ " current_owner_id, inv_model_id, quantity, date, ref_transaction_id, amount) select ?, ?,"
							+ " ?, ?, ?, ?, ?, cost_per_unit*? from st_lms_cons_inv_specification where inv_model_id=?";
					pstmt = conn.prepareStatement(insIntoUserInvDetailQuery);
					pstmt.setInt(1, userId);
					pstmt.setInt(2, userOrgId);
					pstmt.setInt(3, partyOrgId);
					pstmt.setInt(4, consModelId[i]);
					pstmt.setLong(5, consQty[i]);
					pstmt.setTimestamp(6, new Timestamp(new java.util.Date()
							.getTime()));
					pstmt.setObject(7, null);
					pstmt.setLong(8, consQty[i]);
					pstmt.setInt(9, consModelId[i]);
					logger.debug("st_lms_cons_inv_detail = " + pstmt);
					int rowCount = pstmt.executeUpdate();
					logger.debug("Total row inserted = " + rowCount);
					if (rowCount > 0) {
						ResultSet rsTask=pstmt.getGeneratedKeys();
						if(rsTask.next()){
							int taskId=rsTask.getInt(1);
							String insQuery="insert into st_lms_inv_dl_task_mapping values("+DNID+","+taskId+",'CONS')";
						PreparedStatement	pstmt1=conn.prepareStatement(insQuery);
							pstmt1.executeUpdate();
						}
						
						
						bean = new ConsNNonConsBean();
						// bean.
						// validSerNoList.add(bean);
					}

					if (fstRowUpd > 0
							&& partyDetMap
									.get(userOrgId + "-" + consModelId[i]) != null) {

						// update st_lms_cons_inv_status table to add inventory
						// quantity
						String incInvStatusQuery = "update st_lms_cons_inv_status set cumm_qty_count=cumm_qty_count+?"
								+ " where inv_model_id =? and party_org_id = ?";
						pstmt = conn.prepareStatement(incInvStatusQuery);
						pstmt.setLong(1, consQty[i]);
						pstmt.setInt(2, consModelId[i]);
						pstmt.setInt(3, userOrgId);
						logger.debug(pstmt);
						scdRowUpd = pstmt.executeUpdate();

					} else if (fstRowUpd > 0) {
						// insert into st_lms_cons_inv_status table to add
						// inventory quantity
						String insIntoInvStatusQuery = "insert into st_lms_cons_inv_status(party_type, party_org_id, "
								+ " inv_model_id, cumm_qty_count) values(?, ?, ?, ?)";
						pstmt = conn.prepareStatement(insIntoInvStatusQuery);
						pstmt.setString(1, userType);
						pstmt.setInt(2, userOrgId);
						pstmt.setInt(3, consModelId[i]);
						pstmt.setLong(4, consQty[i]);
						logger.debug(pstmt);
						scdRowUpd = pstmt.executeUpdate();
						logger.debug("Total row inserted = " + scdRowUpd);
					}

					/*if (scdRowUpd > 0 && fstRowUpd > 0) {
						String insIntoInvDetailQuery = "insert into st_lms_cons_inv_detail(user_id, user_org_id, "
								+ " current_owner_id, inv_model_id, quantity, date, ref_transaction_id, amount) select ?, ?,"
								+ " ?, ?, ?, ?, ?, cost_per_unit*? from st_lms_cons_inv_specification where inv_model_id=?";
						pstmt = conn.prepareStatement(insIntoInvDetailQuery);
						pstmt.setInt(1, userId);
						pstmt.setInt(2, userOrgId);
						pstmt.setInt(3, userOrgId);
						pstmt.setInt(4, consModelId[i]);
						pstmt.setLong(5, consQty[i]);
						pstmt.setTimestamp(6, new Timestamp(
								new java.util.Date().getTime()));
						pstmt.setObject(7, null);
						pstmt.setLong(8, consQty[i]);
						pstmt.setInt(9, consModelId[i]);
						logger.debug("st_lms_cons_inv_detail = " + pstmt);
						rowCount = pstmt.executeUpdate();
						logger.debug("Total row inserted = " + rowCount);
						if (rowCount > 0) {
							bean = new ConsNNonConsBean();
							// bean.
							// validSerNoList.add(bean);
						}
					}*/
				}

			} else {
				// values are not in valid format

			}
		}

		logger.debug("valid list = " + validSerNoList);
		logger.debug("invalid list = " + inValidSerNoList);

	}

	public int returnConsNNonConsInv(int userOrgId, int userId,
			String ownerType, int agtOrgId, int retOrgId, int[] nonConsInvId,
			int[] nonConsModelId, int[] nonConsBrandId, String[] serNo,
			int[] consInvId, int[] consModelId, int[] consQty, String userType)
			throws LMSException {

		Connection conn = DBConnect.getConnection();
		int DNID=0;
		try {
			conn.setAutoCommit(false);
			
			String lastDCNoGenerated = null, autoGeneDCNo = null;
			PreparedStatement boReceiptNoGenStmt=null;
			boReceiptNoGenStmt = conn.prepareStatement("SELECT * from st_lms_inv_dl_detail where dl_owner_type=?  ORDER BY generated_dl_id DESC LIMIT 1");
			boReceiptNoGenStmt.setString(1, "BO");
			ResultSet DCRs = boReceiptNoGenStmt.executeQuery();

			while (DCRs.next()) {
				lastDCNoGenerated = DCRs.getString("generated_dl_id");
			}
			autoGeneDCNo = GenerateRecieptNo.getRecieptNo("DNCHALLAN",
					lastDCNoGenerated, "BO");
			System.out.println("lastDCNoGenerated " + lastDCNoGenerated
					+ "autoGeneDCNo " + autoGeneDCNo);
			
			// insert into lms dl inv detail table for delivery challan
			PreparedStatement boReceiptStmt = conn.prepareStatement("insert into st_lms_inv_dl_detail(dl_owner_type,generated_dl_id) values(?,?)");
			
			boReceiptStmt.setString(1, "BO");
			boReceiptStmt.setString(2, autoGeneDCNo);
			System.out.println("bo DL receipt = " + boReceiptStmt);
			boReceiptStmt.execute();
			//find dlID
			boReceiptNoGenStmt = conn.prepareStatement("SELECT * from st_lms_inv_dl_detail where generated_dl_id=?  ORDER BY generated_dl_id DESC LIMIT 1");
			boReceiptNoGenStmt.setString(1, autoGeneDCNo);
			 DCRs = boReceiptNoGenStmt.executeQuery();

			while (DCRs.next()) {
				DNID = DCRs.getInt("dl_id");
			}
			
			// assign non consumable inventory
			returnNonConsInv(userOrgId, userId, ownerType, agtOrgId, retOrgId,
					nonConsInvId, nonConsModelId, nonConsBrandId, serNo, conn,
					userType,DNID);
			// assign consumable inventory
			returnConsInv(userOrgId, userId, ownerType, agtOrgId, retOrgId,
					consInvId, consModelId, consQty, conn, userType,DNID);

			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}

		return DNID;
	}

	private void returnNonConsInv(int userOrgId, int userId, String ownerType,
			int agtOrgId, int retOrgId, int[] nonConsInvId,
			int[] nonConsModelId, int[] nonConsBrandId, String[] serNo,
			Connection conn, String userType,int DNID) throws SQLException {

		List<String> validSerNoList = new ArrayList<String>();
		List<String> inValidSerNoList = new ArrayList<String>();

		String assignSerNoQuery = "update st_lms_inv_status set user_id = ?, user_org_type = ?, user_org_id = ?, "
				+ "current_owner_type = ?, current_owner_id = ? where inv_model_id =? and serial_no = "
				+ " ? and current_owner_id = ?";
		PreparedStatement assignSerNoPstmt = conn
				.prepareStatement(assignSerNoQuery);
		assignSerNoPstmt.setInt(1, userId);
		assignSerNoPstmt.setString(2, userType);
		assignSerNoPstmt.setInt(3, userOrgId);
		assignSerNoPstmt.setString(4, userType);
		assignSerNoPstmt.setInt(5, userOrgId);
		// assignSerNoPstmt.setInt(6, modId); inside for loop
		// assignSerNoPstmt.setString(7, sNo.trim()); inside for loop
		assignSerNoPstmt.setInt(8,
				"AGENT".equalsIgnoreCase(ownerType.trim()) ? agtOrgId
						: retOrgId);		
		logger.debug(assignSerNoQuery);
		// insert into st_lms_inv_detail table to add inventory quantity
		String insIntoInvDetQuery = "insert into st_lms_inv_detail(user_id,user_org_type,user_org_id,"
				+ " inv_model_id,serial_no,date,cost_to_bo,is_new,current_owner_type,current_owner_id)"
				+ " select ?,?,?,?,?,?,cost_to_bo, is_new,?,? from st_lms_inv_status where inv_model_id =? "
				+ " and serial_no = ?";
		PreparedStatement insIntoInvDet = conn
				.prepareStatement(insIntoInvDetQuery);
		insIntoInvDet.setInt(1, userId);
		insIntoInvDet.setString(2, userType);
		insIntoInvDet.setInt(3, userOrgId);
		// insIntoInvDet.setInt(4, modId); inside for loop
		// insIntoInvDet.setString(5, sNo.trim()); inside for loop
		insIntoInvDet.setTimestamp(6, new Timestamp(new java.util.Date()
				.getTime()));
		insIntoInvDet.setString(7, userType);
		insIntoInvDet.setInt(8, userOrgId);
		// insIntoInvDet.setInt(9, modId); inside for loop
		// insIntoInvDet.setString(10, sNo.trim()); inside for loop		
		logger.debug(insIntoInvDetQuery);		
		String[] serNoArr = null;
		int fstRowUpd = -1, scdRowUpd = -1;
		for (int i = 0, len = nonConsModelId.length; i < len; i = i + 1) {
			if (nonConsInvId[i] > 0 && nonConsModelId[i] > 0
					&& nonConsBrandId[i] > 0 && !"".equals(serNo[i].trim())) {

				assignSerNoPstmt.setInt(6, nonConsModelId[i]);

				insIntoInvDet.setInt(4, nonConsModelId[i]);
				insIntoInvDet.setInt(9, nonConsModelId[i]);

				serNoArr = serNo[i].split(",");
				for (int k = 0, klen = serNoArr.length; k < klen; k = k + 1) {
					fstRowUpd = -1;
					scdRowUpd = -1;
					if (!"".equals(serNoArr[k].trim())) {
						// update st_lms_inv_status
						assignSerNoPstmt.setString(7, serNoArr[k].trim());
						logger.debug("updPstmt = " + assignSerNoPstmt);
						fstRowUpd = assignSerNoPstmt.executeUpdate();

						if (fstRowUpd == 1) {
							// insert data into st_lms_inv_detail
							insIntoInvDet.setString(5, serNoArr[k].trim());
							insIntoInvDet.setString(10, serNoArr[k].trim());
							logger.debug("insPstmt = " + insIntoInvDet);
							scdRowUpd = insIntoInvDet.executeUpdate();
							if (scdRowUpd > 0) {
								ResultSet rsTask=insIntoInvDet.getGeneratedKeys();
								if(rsTask.next()){
									int taskId=rsTask.getInt(1);
									String insQuery="insert into st_lms_inv_dl_task_mapping values("+DNID+","+taskId+",'NON_CONS')";
								PreparedStatement	pstmt=conn.prepareStatement(insQuery);
									pstmt.executeUpdate();
								}
								
								validSerNoList.add(serNoArr[k].trim());
							}
						} else {
							inValidSerNoList.add(serNoArr[k].trim());
						}
					}
				}

			} else {
				// values are not in valid format
				logger.debug("inside else ==== " + nonConsInvId[i] + " == "
						+ nonConsModelId[i] + " === " + nonConsBrandId[i]
						+ "==" + serNo[i].trim());
			}
		}

		logger.debug("valid list = " + validSerNoList);
		logger.debug("invalid list = " + inValidSerNoList);

	}

	private String verifyAssignConsInv(int userOrgId, int[] consInvId,
			int[] consModelId, int[] consQty, Connection conn)
			throws SQLException {
		logger.debug("verifyAssignConsInv called");
		// List<String> validSerNoList = new ArrayList<String>();
		List<String> inValidSerNoList = new ArrayList<String>();
		Statement stmt1 = conn.createStatement();
		int countrows = 0;
		String fetchDetailsQuery = "select party_org_id, aa.inv_model_id, cumm_qty_count, inv_name, "
				+ " concat(spec_value, '-', spec_unit, '-', spec_type) 'model_name' from st_lms_cons_inv_status"
				+ " aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc where aa.inv_model_id =  "
				+ " bb.inv_model_id and bb.inv_id = cc.inv_id and aa.inv_model_id = ? and party_org_id = ?"
				+ " and cumm_qty_count < ?";
		PreparedStatement pstmt = conn.prepareStatement(fetchDetailsQuery);
		if (consModelId != null) {
			for (int i = 0, len = consModelId.length; i < len; i = i + 1) {
				if (consInvId[i] > 0 && consModelId[i] > 0 && consQty[i] > 0) {
					if (countrows == 0) {
						logger
								.debug("query 111 3333"
										+ "select party_org_id, aa.inv_model_id, cumm_qty_count, inv_name, "
										+ " concat(spec_value, '-', spec_unit, '-', spec_type) 'model_name' from st_lms_cons_inv_status"
										+ " aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc where aa.inv_model_id =  "
										+ " bb.inv_model_id and bb.inv_id = cc.inv_id and aa.inv_model_id = "
										+ consModelId[i]
										+ " and party_org_id = " + userOrgId
										+ "" + " ");
						ResultSet rs = stmt1
								.executeQuery("select party_org_id, aa.inv_model_id, cumm_qty_count, inv_name, "
										+ " concat(spec_value, '-', spec_unit, '-', spec_type) 'model_name' from st_lms_cons_inv_status"
										+ " aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc where aa.inv_model_id =  "
										+ " bb.inv_model_id and bb.inv_id = cc.inv_id and aa.inv_model_id = "
										+ consModelId[i]
										+ " and party_org_id = "
										+ userOrgId
										+ "");
						if (rs.next()) {
							countrows++;
						}
					}
					if (countrows > 0) {
						logger
								.debug("query 111 "
										+ "select party_org_id, aa.inv_model_id, cumm_qty_count, inv_name, "
										+ " concat(spec_value, '-', spec_unit, '-', spec_type) 'model_name' from st_lms_cons_inv_status"
										+ " aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc where aa.inv_model_id =  "
										+ " bb.inv_model_id and bb.inv_id = cc.inv_id and aa.inv_model_id = "
										+ consModelId[i]
										+ " and party_org_id = " + userOrgId
										+ "" + " and cumm_qty_count < "
										+ consQty[i] + "");
						pstmt.setInt(1, consModelId[i]);
						pstmt.setInt(2, userOrgId);
						pstmt.setInt(3, consQty[i]);
						ResultSet rs = pstmt.executeQuery();
						while (rs.next()) {
							inValidSerNoList.add(consModelId[i] + "="
									+ consQty[i]);
						}
					}
				}

			}

			if (countrows == 0) {
				for (int i = 0, len = consModelId.length; i < len; i = i + 1) {
					if (consInvId[i] > 0 && consModelId[i] > 0
							&& consQty[i] > 0) {
						inValidSerNoList.add(consModelId[i] + "=" + consQty[i]);
					}
				}
			}
		}

		logger.debug("invalid list = " + inValidSerNoList);
		return inValidSerNoList.toString().replace("[", "").replace("]", "");

	}

	private String verifyAssignNonConsInv(int userOrgId, int[] nonConsInvId,
			int[] nonConsModelId, int[] nonConsBrandId, String[] serNo,
			Connection conn) throws SQLException {
		logger.debug("verifyAssignNonConsInv called");
		List<String> validSerNoList = new ArrayList<String>();
		List<String> inValidSerNoList = new ArrayList<String>();

		String assignSerNoQuery = null;
		Statement assignSerNoPstmt = conn.createStatement();
		ResultSet rs = null;

		String[] serNoArr = null;
		List<String> dbEntries = new ArrayList<String>();

		if (nonConsModelId != null) {
			for (int i = 0, len = nonConsModelId.length; i < len; i = i + 1) {
				if (nonConsInvId[i] > 0 && nonConsModelId[i] > 0
						&& nonConsBrandId[i] > 0 && !"".equals(serNo[i].trim())) {

					assignSerNoQuery = "select serial_no from st_lms_inv_status  where inv_model_id = "
							+ nonConsModelId[i]
							+ " and serial_no in ( "
							+ "'"
							+ serNo[i].replace(",", "','").toUpperCase()
							+ "'"
							+ " ) and  current_owner_type !='REMOVED' and current_owner_id = " + userOrgId;
					logger.debug("fetch nonCons list1 Pstmt = "
							+ assignSerNoQuery);
					rs = assignSerNoPstmt.executeQuery(assignSerNoQuery);
					dbEntries.clear();
					while (rs.next()) {
						dbEntries.add(rs.getString("serial_no"));
					}

					serNoArr = serNo[i].split(",");
					for (int k = 0, klen = serNoArr.length; k < klen; k = k + 1) {
						if (!"".equals(serNoArr[k].trim())) {
							if (dbEntries.contains(serNoArr[k].trim()
									.toUpperCase())) {
								validSerNoList.add(serNoArr[k].trim());
							} else {
								inValidSerNoList.add(nonConsModelId[i] + "="
										+ serNoArr[k].trim());
							}
						}
					}

				} else {
					// values are not in valid format
					logger.debug("inside else ==== " + nonConsInvId[i] + " == "
							+ nonConsModelId[i] + " === " + nonConsBrandId[i]
							+ "==" + serNo[i].trim());
				}
			}
		}

		logger.debug("valid list = " + validSerNoList);
		logger.debug("invalid list = " + inValidSerNoList);

		return inValidSerNoList.toString().replace("[", "").replace("]", "");
	}

	public String verifyConsNNonConsInv(int userOrgId, int[] nonConsInvId,
			int[] nonConsModelId, int[] nonConsBrandId, String[] serNo,
			int[] consInvId, int[] consModelId, int[] consQty, String userType)
			throws LMSException {

		Connection conn = DBConnect.getConnection();
		try {
			// conn.setAutoCommit(false);
			// assign non consumable inventory
			String nonConsInvRes = verifyAssignNonConsInv(userOrgId,
					nonConsInvId, nonConsModelId, nonConsBrandId, serNo, conn);
			// assign consumable inventory
			String consInvRes = verifyAssignConsInv(userOrgId, consInvId,
					consModelId, consQty, conn);

			return nonConsInvRes + ":" + consInvRes;
			// conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
	}
	
	public String verifyConsNNonConsInv(int userOrgId, int[] nonConsInvId,
			int[] nonConsModelId, int[] nonConsBrandId, String[] serNo,
			int[] consInvId, int[] consModelId, int[] consQty, String userType,int agentOrgId)
			throws LMSException {

		Connection conn = DBConnect.getConnection();
		try {
			// conn.setAutoCommit(false);
			// assign non consumable inventory
			String nonConsInvRes = verifyAssignNonConsInv(userOrgId,
					nonConsInvId, nonConsModelId, nonConsBrandId, serNo, conn);
			String consInvRes="";
			if(WrapperUtility.isAgentExist(agentOrgId))
			// assign consumable inventory
			 consInvRes = verifyAssignConsInv(userOrgId, consInvId,	consModelId, consQty, conn);

			return nonConsInvRes + ":" + consInvRes;
			// conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new LMSException(e);
			}
		}
	}
	
	
	public List<String> fetchSerialNoList(String modelID) throws SQLException {
		logger.debug("fetchSerialNoList called");		
		List<String> serialNoList = new ArrayList<String>();		
		String[] model_brand = modelID.split("-");		
		Connection con = null;		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try {
			
				con = DBConnect.getConnection();			
				pstmt = con.prepareStatement("select serial_no from st_lms_inv_detail where inv_model_id = ?");
				pstmt.setString(1,model_brand[0]);		
				rs = pstmt.executeQuery();		
				while(rs.next()){
					serialNoList.add(rs.getString("serial_no"));
				}	
				logger.debug(serialNoList);
			}catch (SQLException e) {
				logger.error("Exception:"+e);	
			}catch (Exception e) {
				logger.error("Exception:"+e);	
			} finally {
				DBConnect.closeConnection(con, pstmt, rs);
			}
			return serialNoList;
	}
	
	
	public Set<String> findDuplicates(List<String> listContainingDuplicates, int bind_len)
	{ 
		final Set<String> setToReturn = new HashSet<String>(); 
		final Set<String> set1 = new HashSet<String>();
		
		for (String str : listContainingDuplicates)
		{
			if(bind_len > 0){
			 	if (!set1.add(str.substring(str.length()-bind_len, str.length()))) {
			 		set1.add(str.substring(str.length()-bind_len, str.length()));
			 		setToReturn.add(str);
			 	}
			}else if (bind_len == 0){
				if (!set1.add(str)) {
				   setToReturn.add(str);
				}
			}
		}
		return setToReturn;
	}

	public Map<String, InvDetailBean> getDuplicateInvetoryMap(Map<String, InvDetailBean> invMap,
			int bindLen,int modelId,boolean isInvCodeReq,Connection con) throws LMSException {

		
		Map<String,InvDetailBean> duplicateInvMap = new HashMap<String, InvDetailBean>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Map<String,String> getSrNoFromInvCode=new HashMap<String,String>();
		try{
			
			Iterator<Entry<String, InvDetailBean>> invItr =invMap.entrySet().iterator();
			
			StringBuilder snSbr = new StringBuilder();
			StringBuilder invcSbr = new StringBuilder();
			boolean isFirst =true;
			while(invItr.hasNext()){
				Entry<String, InvDetailBean> entry=invItr.next();
				if(!isFirst){
					
					snSbr.append(",");
					invcSbr.append(",");
				}
				if(bindLen>0){
					snSbr.append("'"+entry.getValue().getSerialNo().substring(entry.getValue().getSerialNo().length()-bindLen,entry.getValue().getSerialNo().length())+"'");
					
				}else{
					snSbr.append("'"+entry.getValue().getSerialNo()+"'");
					
				}
				invcSbr.append("'"+entry.getValue().getCode()+"'");
				getSrNoFromInvCode.put(entry.getValue().getCode(), entry.getValue().getSerialNo());
				isFirst=false;
			}
			con =DBConnect.getConnection();
			
			String getDuplicates =null ;
			if(isInvCodeReq){
				if(bindLen>0){
					getDuplicates=" select invStatus.serial_no srNo ,inv_code invCode from st_lms_inv_status invStatus  inner join st_lms_inv_mapping  invMap on invStatus.serial_no=invMap.serial_no where invStatus.inv_model_id =? and (SUBSTRING(invStatus.serial_no,?)  in ("+snSbr.toString()+") or inv_code in ("+invcSbr.toString()+"))";	
				}else{
					
					getDuplicates=" select invStatus.serial_no srNo ,inv_code invCode from st_lms_inv_status invStatus  inner join st_lms_inv_mapping  invMap on invStatus.serial_no=invMap.serial_no where invStatus.inv_model_id =? and (invStatus.serial_no  in ("+snSbr.toString()+") or inv_code in ("+invcSbr.toString()+"))";
					
				}
				
				
			}else{
				if(bindLen>0){
					getDuplicates ="select serial_no srNo from st_lms_inv_status where inv_model_id =? and SUBSTRING(serial_no,?)  in ("+snSbr.toString()+")";
				}else{
					getDuplicates ="select serial_no srNo from st_lms_inv_status where inv_model_id =? and serial_no in ("+snSbr.toString()+")";
				}
				
			}
			
			pstmt =con.prepareStatement(getDuplicates);
			pstmt.setInt(1, modelId);
			if(bindLen>0){
				pstmt.setInt(2, bindLen);
			}
			
			logger.info("Inventory Check :-"+pstmt);
			rs =pstmt.executeQuery();
			// Get Duplicate Inventory Map And Remove Duplicate Inventory  
			if(isInvCodeReq){
				while(rs.next()){
					   InvDetailBean invBean =new InvDetailBean();
					   if(invMap.containsKey(rs.getString("srNo"))){
						   invBean.setSerialNo(rs.getString("srNo"));
						   invBean.setCode(invMap.get(rs.getString("srNo")).getCode());
						  
					   	}else{
					   		
					   	   invBean.setSerialNo(getSrNoFromInvCode.get(rs.getString("invCode")));
						   invBean.setCode(rs.getString("invCode"));
					   	}
					   invMap.remove(invBean.getSerialNo());
					   duplicateInvMap.put(invBean.getSerialNo(),invBean);
				
				}
				
			}else{
				while(rs.next()){
					 InvDetailBean invBean =new InvDetailBean();
					 invBean.setSerialNo(rs.getString("srNo"));
					 duplicateInvMap.put(invBean.getSerialNo(),invBean);
					 invMap.remove(rs.getString("srNo"));
				}
			}
			
			
			
			
		}catch(SQLException e){
			logger.error("SQL Exception : ",e);
			throw new LMSException(LMSErrors.SQL_EXCEPTION_ERROR_CODE,LMSErrors.SQL_EXCEPTION_ERROR_MESSAGE);
		}catch (Exception e) {
			logger.error("Exception: ",e);
			throw new LMSException(LMSErrors.GENERAL_EXCEPTION_ERROR_CODE,LMSErrors.GENERAL_EXCEPTION_ERROR_MESSAGE);
		}finally{
			DBConnect.closeConnection(pstmt,rs);
		}
		//logger.debug(returnList);
		return duplicateInvMap;
  
	}
	
	
   }
