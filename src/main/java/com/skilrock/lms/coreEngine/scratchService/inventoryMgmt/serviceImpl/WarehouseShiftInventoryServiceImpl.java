package com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.serviceImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.skilrock.lms.beans.ActiveGameBean;
import com.skilrock.lms.beans.BookBean;
import com.skilrock.lms.beans.BookSeriesBean;
import com.skilrock.lms.beans.GameTicketFormatBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.db.QueryManager;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.common.utility.GenerateRecieptNo;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.WarehouseShiftInventoryHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.PwtTicketHelper;
import com.skilrock.lms.web.drawGames.common.Util;

public class WarehouseShiftInventoryServiceImpl {

	public static List<String> verifyInv(String[] bookArr, String[] bookCountArr1,
			String[] bookFromArr, String[] bookFromCountArr1,
			String[] bookTOArr, String[] gameName, String fromWarehouse,
			String toWarehouse, String action, UserInfoBean userInfoBean)  throws LMSException {

		List<String> inValidBooks = new ArrayList<String>() ;
		PwtTicketHelper pwtTicketHelper = new PwtTicketHelper();
		List<ActiveGameBean> activeGameList = pwtTicketHelper.getActiveGames();
		List<GameTicketFormatBean> gameFormatList = null;
		GameTicketFormatBean gameFormatBean = null;
		if (activeGameList != null && activeGameList.size() > 0) {
			gameFormatList = pwtTicketHelper
					.getGameTicketFormatList(activeGameList);
		} else {
			throw new LMSException("NO Active Game Exist");
		}
		
		
		Map<String, List<List<String>>> gameBookMap = getGameBookMapVerify(
				gameName[0].split(","), bookArr[0].split(","),
				bookFromArr[0].split(","), bookTOArr[0].split(","),
				bookCountArr1[0].split(","), bookFromCountArr1[0]
						.split(","));
		
		Iterator gameBkMapItr = gameBookMap.entrySet().iterator();
		int toWarehouseOwnerId = 0 ;
		if(!("verify".equalsIgnoreCase(action)))
			toWarehouseOwnerId = WarehouseShiftInventoryHelper.getWarehouseOwnerId(toWarehouse) ;
		
		while (gameBkMapItr.hasNext()) {
			Map.Entry gameBkpair = (Map.Entry) gameBkMapItr.next();

			String gmName = (String) gameBkpair.getKey();
			int gameNbr = Integer.parseInt(gmName.split("-")[0]);
			int gameId = pwtTicketHelper.getGameId(activeGameList, gmName);

			for (int i = 0; i < gameFormatList.size(); i++) {
				gameFormatBean = gameFormatList.get(i);
				if (gameId == gameFormatBean.getGameId()) {
					break;
				}

			}

			int gameNbrDigits = gameFormatBean.getGameNbrDigits();
			int bkDigits = gameFormatBean.getBookDigits();
			List<List<String>> bkList = (List<List<String>>) gameBkpair
					.getValue();

			verifyBookSeries(bkList.get(1), bkList.get(0), gameId, gmName,
					gameNbr, gameNbrDigits, bkDigits, fromWarehouse, toWarehouse, action, toWarehouseOwnerId, inValidBooks, userInfoBean);
			
			
		}
		return inValidBooks ;
	}
	
	public static Map<String, List<List<String>>> getGameBookMapVerify(
			String[] gameName, String[] bookArr, String[] bookFromArr,
			String[] bookTOArr, String[] bookCountArr, String[] bookFromCountArr) {

		Map<String, List<List<String>>> gameBookMap = new HashMap<String, List<List<String>>>();

		int bookStart = 0;
		int bookSeriesStart = 0;

		for (int i = 0; i < gameName.length; i++) {

			if (!gameName[i].equals("-1") && !gameName[i].equals("")) {
				List<String> bookList = new ArrayList<String>();
				List<String> bookSeriesList = new ArrayList<String>();
				List<List<String>> bkAndbkSerList = new ArrayList<List<String>>();
				int bookEnd = Integer.parseInt(bookCountArr[i]) + bookStart;
				int bookSeriesEnd = Integer.parseInt(bookFromCountArr[i])
						+ bookSeriesStart;

				for (int start = bookStart; start < bookEnd; start++) {
					if (!bookArr[start].equals("")) {
						bookList.add(bookArr[start].replaceAll("-", ""));
					}
					bookStart++;
				}
				bkAndbkSerList.add(bookList);
				for (int start = bookSeriesStart; start < bookSeriesEnd; start++) {
					if (!bookFromArr[start].equals("")
							&& !bookFromArr[start].equals("")) {
						bookSeriesList.add(bookFromArr[start].replaceAll("-",
								"")
								+ ":" + bookTOArr[start].replaceAll("-", ""));
					}
					bookSeriesStart++;
				}
				bkAndbkSerList.add(bookSeriesList);
				gameBookMap.put(gameName[i], bkAndbkSerList);

			} else {
				bookStart = Integer.parseInt(bookCountArr[i]) + bookStart;
				bookSeriesStart = Integer.parseInt(bookFromCountArr[i])
						+ bookSeriesStart;
			}
		}
		System.out.println(" Game Book Map**" + gameBookMap);

		return gameBookMap;

	}

	
	public static List<String> verifyBookSeries(List<String> bkSerList,
			List<String> frontBookList, int gameId, String gameName,
			int gameNbr, int gameNbrDigits, int bkDigits, String fromWarehouse, String toWarehouse, String action, int warehouseOwnerId
			, List<String> inValidBooks, UserInfoBean userInfoBean) throws LMSException {

		// global connection to be used everywhere
		Connection connection = null;
		 
		connection = DBConnect.getConnection();

		List verifiedList = new ArrayList();
		WarehouseShiftInventoryHelper helper = new WarehouseShiftInventoryHelper();
		boolean isValid = false;
		boolean isSeriesValid = true;
		boolean isUpdated = false;

		List<BookBean> bookList = new ArrayList();
		List<BookBean> bookSeriesList = new ArrayList();

		System.out.println("---Series length" + bkSerList.size());

		if (bkSerList != null) {
			for (int seriesNo = 0; seriesNo < bkSerList.size(); seriesNo++) {
				BookSeriesBean bookSeBean = new BookSeriesBean();
				bookSeBean
						.setBookNbrFrom(bkSerList.get(seriesNo).split(":")[0]);
				bookSeBean.setBookNbrTo(bkSerList.get(seriesNo).split(":")[1]);
				bookSeBean.setStatus("");
				bookSeBean.setValid(isValid);

				String bookNbrFrom = bkSerList.get(seriesNo).split(":")[0]
						.replaceAll("-", "");
				String bookNbrTo = bkSerList.get(seriesNo).split(":")[1]
						.replaceAll("-", "");
				if (bookNbrFrom.substring(0, bookNbrFrom.length() - bkDigits)
						.equals(
								bookNbrTo.substring(0, bookNbrTo.length()
										- bkDigits))) {
					int bookNbrFrmInt = Integer.parseInt(bookNbrFrom.substring(
							bookNbrFrom.length() - bkDigits, bookNbrFrom
									.length()));
					int bookNbrToInt = Integer.parseInt(bookNbrTo.substring(
							bookNbrTo.length() - bkDigits, bookNbrTo.length()));
					int noOfbooks = bookNbrToInt - bookNbrFrmInt;

					for (int i = 0; i < noOfbooks + 1; i++) {

						String bookNbr = String.valueOf(bookNbrFrom.substring(
								0, (bookNbrFrom.length() - ("" + bookNbrFrmInt)
										.length()))
								+ bookNbrFrmInt);

						if (bookNbr != null && !bookNbr.trim().equals("")) {
							// add hyphens if necessary

							// System.out.println(gameNbrDigits+":::::::::" +
							// bookNbrFrom);
							bookNbr = bookNbr.substring(0, gameNbrDigits) + "-"
									+ bookNbr.substring(gameNbrDigits);
							System.out.println("New book nbr:::" + bookNbr);
							
							if("verify".equalsIgnoreCase(action))
								isValid = helper.verifyBook(fromWarehouse, bookNbr,	gameId, gameNbr, connection);
							else{
								isUpdated = helper.updateStatus(gameId, fromWarehouse, toWarehouse, bookNbr, connection);
								if(isUpdated)
									helper.createHistory(gameId, bookNbr, gameNbrDigits, bkDigits, toWarehouse, connection, warehouseOwnerId, userInfoBean) ;
								
								inValidBooks.add(bookNbr) ;
							}
							
							

							if (!isValid) {
								inValidBooks.add(bookNbr) ;
							}
						}
						// System.out.println("inside for of verifyBookSeries");
						bookNbrFrmInt++;

					}
				} 
			}
		}
		
		if(frontBookList != null){
			for(String bookNbr : frontBookList){
				bookNbr = bookNbr.substring(0, gameNbrDigits) + "-"
				+ bookNbr.substring(gameNbrDigits);
				if("verify".equalsIgnoreCase(action)){
					isValid = helper.verifyBook(fromWarehouse, bookNbr,	gameId, gameNbr, connection);
					if (!isValid) 
						inValidBooks.add(bookNbr) ;
					
				}
				else{
					isUpdated = helper.updateStatus(gameId, fromWarehouse, toWarehouse, bookNbr, connection);
					if(isUpdated)
						helper.createHistory(gameId, bookNbr, gameNbrDigits, bkDigits, toWarehouse, connection, warehouseOwnerId, userInfoBean) ;
					
					inValidBooks.add(bookNbr) ;
				}
				
			
			
			}}
		
		
		return inValidBooks ;
	}
	

	
	public static String[] generateDeliveryChallan(int boOrgId) throws LMSException {
        System.out.println("Start to Dispach");

        String[] response = new String[2];
        int DCId = -1;
        String autoGeneDCNo = null;

        Connection connection = null;       
        try{
        	
        	connection = DBConnect.getConnection() ;
        	connection.setAutoCommit(false) ;
                 
            PreparedStatement boReceiptStmt = null;
            PreparedStatement boReceiptNoGenStmt = null;        

 
                
                boReceiptNoGenStmt = connection.prepareStatement(QueryManager
                        .getBOLatestReceiptNb());
                boReceiptNoGenStmt.setString(1, "DLCHALLAN");
                ResultSet DCRs = boReceiptNoGenStmt.executeQuery();
                String lastDCNoGenerated = null;

                while (DCRs.next()) {
                    lastDCNoGenerated = DCRs.getString("generated_id");
                }

                autoGeneDCNo = GenerateRecieptNo.getRecieptNo("DLCHALLAN",
                        lastDCNoGenerated, "BO");
                System.out.println("lastDCNoGenerated : " + lastDCNoGenerated
                        + " and autoGeneDCNo : " + autoGeneDCNo);

                

                // insert in receipt transaction master for delivery challan
                boReceiptStmt = connection.prepareStatement(QueryManager
                        .insertInReceiptMaster());
                boReceiptStmt.setString(1, "BO");
                boReceiptStmt.executeUpdate();

                ResultSet rsDC = boReceiptStmt.getGeneratedKeys();
                while (rsDC.next()) {
                    DCId = rsDC.getInt(1);
                }

                // insert bo reciept id for delivery challan

                boReceiptStmt = connection.prepareStatement(QueryManager
                        .insertInBOReceipts());

                boReceiptStmt.setInt(1, DCId);
                boReceiptStmt.setString(2, "DLCHALLAN");
                boReceiptStmt.setInt(3, boOrgId);
                boReceiptStmt.setString(4, "BO");
                boReceiptStmt.setString(5, autoGeneDCNo);
                boReceiptStmt.setTimestamp(6, Util.getCurrentTimeStamp());

                /*
                 * boReceiptStmt.setString(1,"DLCHALLAN");
                 * boReceiptStmt.setInt(2,agentOrgId);
                 */

                boReceiptStmt.execute();

               
                connection.commit();

                /*if (invoiceId > -1) {
                    GraphReportHelper graphReportHelper = new GraphReportHelper();
                    graphReportHelper.createTextReportBO(invoiceId, boOrgName,
                            userOrgID, rootPath);
                }*/

            } catch (SQLException se) {
                try {
                	se.printStackTrace();
                    connection.rollback();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    throw new LMSException("Error During Rollback", e);
                }
                se.printStackTrace();
                throw new LMSException(se);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }

            response[0] = String.valueOf(DCId);
            response[1] = autoGeneDCNo;

            return response;
    }

	public static String[] generateDeliveryChallanAndUpdateDetails(int userOrgId,
			List<String> books) throws LMSException {
		
		String[] response = generateDeliveryChallan(userOrgId);
		int dcId = Integer.parseInt(response[0]);

		updateDCIDInDetailTable(dcId, books) ;
		
		return response;
	}

	private static void updateDCIDInDetailTable(int dcId, List<String> books) {
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		
		try{
			conn = DBConnect.getConnection() ;
			conn.setAutoCommit(false) ;
			String updateQuery = "update st_se_game_inv_detail set order_invoice_dc_id = ? where book_nbr = ? and order_invoice_dc_id =0 ;" ;
			
			pstmt = conn.prepareStatement(updateQuery) ;
			
			
			for(String bookNbr : books){
				pstmt.setInt(1, dcId) ;
				pstmt.setString(2, bookNbr) ;
				System.out.println("update query : " + pstmt);
				pstmt.addBatch() ;
			}
			
			pstmt.executeBatch() ;
			
			updateQuery="update st_se_game_inv_status set bo_dl_id = ? where book_nbr = ?";
			pstmt=conn.prepareStatement(updateQuery);
			
			for(String bookNbr : books){
				pstmt.setInt(1, dcId) ;
				pstmt.setString(2, bookNbr) ;
				pstmt.addBatch() ;
			}
			
			pstmt.executeBatch() ;
			conn.commit() ;
			
		}catch(Exception e){
			e.printStackTrace() ;
		}finally{
			DBConnect.closeResource(pstmt, conn) ;
		}
		
	}	
	
}
