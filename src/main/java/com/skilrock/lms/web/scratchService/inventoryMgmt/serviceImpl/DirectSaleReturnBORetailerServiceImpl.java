package com.skilrock.lms.web.scratchService.inventoryMgmt.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.PackBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.SalesReturnAgentHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.SalesReturnHelper;

public class DirectSaleReturnBORetailerServiceImpl {
	private static final Log logger = LogFactory
			.getLog(DirectSaleReturnBORetailerServiceImpl.class);

	private String packFlag;

	/**
	 * This method is used to verify the books for sale return
	 * 
	 * @param bookList
	 * @param game_id
	 * @param org_id
	 * @return List of verified books
	 */
	// Do Bulk Verification Of Books.
	public List<BookBean> doVerifiedBooks(List<BookBean> bookList, int game_id,
			int org_id, String isRetOnline, int gameNbr) throws LMSException {
		Connection conn = DBConnect.getConnection();
		System.out
				.println("connection created here =============================");
		try {
			System.out
					.println("Enter in to the bulk Book verification@@@@@@@@@@@@@@@@@@@@@@");
			List<BookBean> list = new ArrayList<BookBean>();
			Iterator<BookBean> iterator = bookList.iterator();
			BookBean bean = null;
			String bknbr = null;
			// boolean bookExistancecheck = false;
			// boolean ownercheck = false;
			// boolean pwtCheck = false;
			// boolean pwtCheckTemp = false;
			boolean bookValidity = false;

			while (iterator.hasNext()) {
				bean = (BookBean) iterator.next();
				bknbr = bean.getBookNumber();

				if (bknbr != null) {
					bookValidity = verifyBook(game_id, bknbr, org_id, conn);
					if (bookValidity) {
						logger.info("book is valid " + bknbr);
						bean.setValid(true);
						bean.setStatus("Book Is Valid");
						list.add(bean);
						setPackFlag("Valid");
					} else {
						logger.info("book is Invalid " + bknbr);
						bean.setValid(false);
						bean.setStatus("Book Is InValid");
						list.add(bean);
					}
				}
				/*
				 * if(bknbr!=null){
				 * 
				 * 
				 * logger.info("Book is not null"+bknbr); bookExistancecheck =
				 * verifyBookWithExistance(game_id, bknbr, org_id, conn);
				 * ownercheck = verifyBookWithOwner(game_id, bknbr,
				 * org_id,isRetOnline, conn); pwtCheck =
				 * verifyBookValidityWithPWT(game_id, bknbr, conn,gameNbr);
				 * pwtCheckTemp=verifyBookValidityWithPWTTempTable(game_id,
				 * bknbr, conn);
				 * 
				 * logger.info("bookExistancecheck:
				 * "+bookExistancecheck); logger.info("ownercheck:
				 * "+ownercheck); logger.info("pwtCheck: "+pwtCheck); }
				 * if(ownercheck == true && pwtCheck == true &&
				 * bookExistancecheck == true && pwtCheckTemp==true){
				 * logger.info("book is valid "+bknbr); bean.setValid(true);
				 * bean.setStatus("Book Is Valid"); list.add(bean);
				 * setPackFlag("Valid"); } else{
				 * logger.info("book is Invalid "+bknbr); bean.setValid(false);
				 * bean.setStatus("Book Is InValid"); list.add(bean); }
				 */
			}
			logger.info("verified booklist:  " + list);
			System.out
					.println("@@@@@@@@@@@@@@@@@@@@@@@@@@ bulk book verification is complete");

			return list;
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException("error in sale return");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				e.printStackTrace();
			}
		}
	}

	public boolean verifyBook(int game_id, String bknbr, int org_id,
			Connection conn, String newBookStatus) throws LMSException {
		System.out
				.println("Enter In To verify book with owner ******************************");

		boolean bookflag = false;
		try {
			String query1 = QueryManager
					.getST4CurrentOwnerDetailsUsingGameBookNbr();
			PreparedStatement pstmt = conn.prepareStatement(query1);
			pstmt.setInt(1, game_id);
			pstmt.setString(2, bknbr);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (org_id == rs.getInt("current_owner_id")
						&& rs.getString("book_status").equals(newBookStatus)
						&& "RETAILER".equalsIgnoreCase(rs
								.getString("current_owner"))) {
					bookflag = true;
				}
			}

			System.out
					.println("************************* book is verified and flag is "
							+ bookflag + " for book " + bknbr);
			return bookflag;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("Exception while book verification");
		}

	}

	public List<PackBean> doVerifiedPacks(List<PackBean> packList, int game_id,
			int org_id, String isRetOnline, int gameNbr) throws LMSException {
		System.out
				.println("Enter in to the bulk pack verification============================");
		List<PackBean> list = new ArrayList<PackBean>();
		Iterator<PackBean> iterator = packList.iterator();
		PackBean bean = null;
		String pknbr = null;
		// boolean packExistancecheck = false;
		// boolean ownercheck = false;
		// boolean pwtCheck = false;
		// boolean pwtCheckTemp = false;
		boolean isValidPack = false;
		Connection conn = null;
		try {

			conn = DBConnect.getConnection();

			while (iterator.hasNext()) {
				bean = (PackBean) iterator.next();
				pknbr = bean.getPackNumber();

				if (pknbr != null) {
					isValidPack = verifyPack(game_id, pknbr, org_id, conn);
					if (isValidPack) {
						logger.info("pack is valid " + pknbr);
						bean.setValid(true);
						bean.setStatus("Pack Is Valid");
						setPackFlag("Valid");
						list.add(bean);
					} else {
						logger.info("pack is Invalid " + pknbr);
						bean.setValid(false);
						bean.setStatus("Pack Is InValid");
						list.add(bean);
					}
				}

				/*
				 * if(pknbr!=null){
				 * 
				 * logger.info("Pack is not null"+pknbr);
				 * 
				 * packExistancecheck = verifyPackWithExistance(game_id, pknbr,
				 * org_id); ownercheck = verifyPackWithOwner(game_id, pknbr,
				 * org_id,isRetOnline); pwtCheck =
				 * verifyPackValidityWithPWT(game_id, pknbr,gameNbr);
				 * pwtCheckTemp=verifyPackValidityWithPWTTempTable(game_id,
				 * pknbr);
				 * 
				 * logger.info("packExistancecheck:
				 * "+packExistancecheck); logger.info("ownercheck:
				 * "+ownercheck); logger.info("pwtCheck: "+pwtCheck); }
				 * 
				 * if(ownercheck == true && pwtCheck == true &&
				 * packExistancecheck == true && pwtCheckTemp==true){
				 * logger.info("pack is valid "+pknbr); bean.setValid(true);
				 * bean.setStatus("Pack Is Valid"); setPackFlag("Valid");
				 * list.add(bean); } else{
				 * logger.info("pack is Invalid "+pknbr);
				 * 
				 * bean.setValid(false); bean.setStatus("Pack Is InValid");
				 * 
				 * list.add(bean); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new LMSException(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				e.printStackTrace();
			}
		}
		logger.info("verified pack list:  " + list);
		System.out
				.println("============================ bulk pack verification is complete");
		return list;
	}

	public boolean verifyBook(int game_id, String bknbr, int org_id,
			Connection conn) throws LMSException {
		System.out
				.println("Enter In To verify book with owner ******************************");

		boolean bookflag = false;
		try {
			String query1 = "select current_owner_id,current_owner,book_status from st_se_game_inv_status aa, st_se_game_master bb where aa.game_id=? and book_nbr= ? and aa.game_id = bb.game_id and cur_rem_tickets = nbr_of_tickets_per_book ";
			// "QueryManager.getST4CurrentOwnerDetailsUsingGameBookNbr();
			PreparedStatement pstmt = conn.prepareStatement(query1);
			pstmt.setInt(1, game_id);
			pstmt.setString(2, bknbr);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				if (org_id == rs.getInt("current_owner_id")
						&& "RETAILER".equalsIgnoreCase(rs
								.getString("current_owner"))) {
					bookflag = true;
				}
			}
			System.out
					.println("************************* book is verified and flag is "
							+ bookflag + " for book " + bknbr);
			return bookflag;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException("Exception while book verification");
		}
	}

	public boolean verifyPack(int game_id, String pknbr, int org_id,
			Connection conn) throws LMSException {
		System.out
				.println("Enter In To verify pack ******************************");

		boolean bookflag = false;
		try {
			// String query1=
			// QueryManager.getST4CurrentOwnerDetailsUsingGameBookNbr();
			PreparedStatement pstmt = conn
					.prepareStatement("select book_status,current_owner_id,current_owner from st_se_game_inv_status where game_id=? and pack_nbr = ?");
			pstmt.setInt(1, game_id);
			pstmt.setString(2, pknbr);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (org_id != rs.getInt("current_owner_id")
						&& !"ACTIVE".equalsIgnoreCase(rs
								.getString("book_status"))
						&& !"AGENT".equalsIgnoreCase(rs
								.getString("current_owner"))) {
					return bookflag;
				} else {
					bookflag = true;
				}
			}
			System.out
					.println("************************* pack is verified and flag is "
							+ bookflag + " for pack " + pknbr);
			return bookflag;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LMSException(
					"Exception while pack verification in sale return @ BO");
		}
	}

	public String startTransaction(int game_id, int retOrgName,
			List<PackBean> savedPackBeanList, List<BookBean> savedBookBeanList,
			String rootPath, String newBookStatus, int userId, int agentOrgId,
			String orgName, String parentOrgName, int userOrgId, int userId2) {
		Connection conn = null;
		String returnTypeRet = null;
		String returnTypeAgt = null;
		SalesReturnHelper helper = new SalesReturnHelper();
		SalesReturnAgentHelper agentHelper = new SalesReturnAgentHelper();
		try {
			conn = DBConnect.getConnection();
			conn.setAutoCommit(false);

			returnTypeRet = agentHelper.doTransaction(game_id, retOrgName,
					savedPackBeanList, savedBookBeanList, rootPath,
					newBookStatus, userId, agentOrgId, orgName, conn);

			returnTypeAgt = helper.doTransaction(game_id, agentOrgId,
					parentOrgName, savedPackBeanList, savedBookBeanList,
					rootPath, userOrgId, userId2, newBookStatus, conn);

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(conn);
		}
		return returnTypeRet + "#$" + returnTypeAgt;
	}

	public String getPackFlag() {
		return packFlag;
	}

	public void setPackFlag(String packFlag) {
		this.packFlag = packFlag;
	}

}
