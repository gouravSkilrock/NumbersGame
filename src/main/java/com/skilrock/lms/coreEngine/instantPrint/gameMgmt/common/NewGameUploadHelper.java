package com.skilrock.lms.coreEngine.instantPrint.gameMgmt.common;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

//import com.lowagie.text.pdf.hyphenation.TernaryTree.Iterator;
import com.skilrock.ipe.Bean.GameBasicDetailLMSBean;
import com.skilrock.ipe.Bean.GameLMSBean;
import com.skilrock.ipe.Bean.StartGameLMSBean;
import com.skilrock.ipe.instantprint.GameBasicDetailBean;
import com.skilrock.ipe.instantprint.GameBean;
import com.skilrock.ipe.instantprint.IInstantPrint;
import com.skilrock.ipe.instantprint.InstantPrint;
import com.skilrock.ipe.instantprint.StartGameBean;
import com.skilrock.ipe.instantprint.StringArray;
import com.skilrock.ipe.instantprint.GameBean.ActiveBookMap;
import com.skilrock.ipe.instantprint.GameBean.ImageDataMap;
import com.skilrock.ipe.instantprint.GameBean.ImageSizeMap;
import com.skilrock.ipe.instantprint.GameBean.ImageTypeMap;
import com.skilrock.ipe.instantprint.StartGameBean.GameMap;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.instantPrint.beans.GameInfoBean;
import com.skilrock.lms.coreEngine.instantPrint.common.IPEUtility;

public class NewGameUploadHelper {
	public void saveGameBasicDetails(int gameNo, String gameName,
			double pricePerTicket, int ticketPetBook, int booksPerPack,
			int gameVirnDigits, int digitsofBook, int digitsofPack,
			int digitsofTicket, double agentPWTCommRate,
			double agentSaleCommRate, double retailerPWTCommRate,
			double retailerSaleCommRate, String govtCommRule,
			double govtCommRate, double minAssProfit, File rankupload,File imageupload, File printschemeupload,
			double prizePayOutRatio, double vatPercentage, long ticketsInScheme, String gameType, String printType)
			throws LMSException {

		if (govtCommRule.equals("fixedper")) {
			govtCommRule = "FIXED_PER";
		} else if (govtCommRule.equals("minprofit")) {
			govtCommRule = "MIN_PROFIT";
		} else if (govtCommRule.equals("notapplicable")) {
			govtCommRule = "NOT_APP";
		}
		GameBasicDetailBean gameBean = new GameBasicDetailBean();
		gameBean.setGameNo(gameNo);
		gameBean.setGameName(gameName);
		gameBean.setTicketPrice(pricePerTicket);
		gameBean.setNoOfBooksPerPack(booksPerPack);
		gameBean.setDigitsOfVirn(gameVirnDigits);
		gameBean.setNoOfTicketsPerBook(100);
		gameBean.setDigitsOfBook(digitsofBook);
		gameBean.setDigitsOfPack(digitsofPack);
		gameBean.setDigitsOfTicket(digitsofTicket);
		gameBean.setRankFile(rankupload.getPath());
		gameBean.setImgFile(imageupload.getPath());
		gameBean.setGameType(gameType);
		gameBean.setPrintType(printType);
		gameBean.setXmlScheme(printschemeupload.getPath());
		InstantPrint service = new InstantPrint();
		IInstantPrint portType = service.getInstantPrintServicePort();
		gameBean =portType.newGameUpload(gameBean);
		System.out.println(gameBean.isSuccess());
		GameBasicDetailLMSBean gameBasicDetailsLMSBean = new GameBasicDetailLMSBean();
		gameBasicDetailsLMSBean.setGameNo(gameBean.getGameNo());
		gameBasicDetailsLMSBean.setGameName(gameBean.getGameName());
		gameBasicDetailsLMSBean.setTicketPrice(gameBean.getTicketPrice());
		gameBasicDetailsLMSBean.setNoOfBooksPerPack(gameBean.getNoOfBooksPerPack());
		gameBasicDetailsLMSBean.setDigitsOfVirn(gameBean.getDigitsOfVirn());
		gameBasicDetailsLMSBean.setNoOfTicketsPerBook(gameBean.getNoOfTicketsPerBook());
		gameBasicDetailsLMSBean.setDigitsOfBook(gameBean.getDigitsOfBook());
		gameBasicDetailsLMSBean.setDigitsOfPack(gameBean.getDigitsOfPack());
		gameBasicDetailsLMSBean.setDigitsOfTicket(gameBean.getDigitsOfTicket());
		gameBasicDetailsLMSBean.setRankFile(new File(gameBean.getRankFile()));
		gameBasicDetailsLMSBean.setImgFile(new File(gameBean.getImgFile()));
		gameBasicDetailsLMSBean.setGameType(gameType);
		gameBasicDetailsLMSBean.setPrintType(printType);
		gameBasicDetailsLMSBean.setXMLScheme(new File(gameBean.getXmlScheme()));
		gameBasicDetailsLMSBean.setSuccess(gameBean.isSuccess());
		if (gameBasicDetailsLMSBean.isSuccess()) {
			Connection con = DBConnect.getConnection();
			try {
				con.setAutoCommit(false);
				PreparedStatement pstmt = null;
				String insQry = "insert into st_ipe_game_master(game_id, game_no, game_name, agent_sale_comm_rate,agent_pwt_comm_rate,retailer_sale_comm_rate,retailer_pwt_comm_rate,govt_comm_rate,game_status,govt_comm_type,prize_payout_ratio,vat_amt,tickets_in_scheme) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt = con.prepareStatement(insQry);
				pstmt.setInt(1, gameBasicDetailsLMSBean.getGameId());
				pstmt.setInt(2, gameBasicDetailsLMSBean.getGameNo());
				pstmt.setString(3, gameBasicDetailsLMSBean.getGameName());
				pstmt.setDouble(4, agentSaleCommRate);
				pstmt.setDouble(5, agentPWTCommRate);
				pstmt.setDouble(6, retailerSaleCommRate);
				pstmt.setDouble(7, retailerPWTCommRate);
				pstmt.setDouble(8, govtCommRate);
				pstmt.setString(9, "NEW");
				pstmt.setString(10, govtCommRule);
				pstmt.setDouble(11, prizePayOutRatio);
				pstmt.setDouble(12, vatPercentage);
				pstmt.setLong(13, ticketsInScheme);
				pstmt.executeUpdate();

				// Create All game tables for retailer only
				String crTbl = "create table st_ipe_ret_sale_?(saleId bigint(20) unsigned NOT NULL AUTO_INCREMENT,transaction_id bigint(20) unsigned NOT NULL,retailer_org_id int(10) NOT NULL,game_id int(10) unsigned NOT NULL DEFAULT '0',ticket_nbr bigint(20) unsigned NOT NULL DEFAULT '0',mrp_amt decimal(20,2) unsigned NOT NULL, net_amt decimal(20,2) NOT NULL,ret_comm_rate decimal(20,2) NOT NULL,agt_comm_rate decimal(20,2) NOT NULL,agent_net_amt decimal(20,2) NOT NULL,claim_status enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,good_cause_rate decimal(10,2) NOT NULL,agent_ref_transaction_id bigint(20) unsigned DEFAULT NULL,vat_amt decimal(20,2) NOT NULL,taxable_sale decimal(20,2) NOT NULL,PRIMARY KEY (saleId)) ENGINE=InnoDB DEFAULT CHARSET=latin1 CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC";
				pstmt = con.prepareStatement(crTbl);
				pstmt.setInt(1, gameNo);
//				System.out.println("Query to  sale tlb::" + pstmt);
				pstmt.executeUpdate();

				crTbl = "create table st_ipe_ret_sale_refund_?(refundId bigint(20) unsigned NOT NULL AUTO_INCREMENT,transaction_id bigint(20) unsigned NOT NULL,retailer_org_id int(10) unsigned NOT NULL,game_id int(10) unsigned NOT NULL DEFAULT '0',ticket_nbr bigint(20) unsigned NOT NULL DEFAULT '0',mrp_amt decimal(20,2) NOT NULL, net_amt decimal(20,2) NOT NULL, ret_comm_rate decimal(20,2) NOT NULL, agt_comm_rate decimal(20,2) NOT NULL, agent_net_amt decimal(20,2) NOT NULL,good_cause_rate decimal(10,2) NOT NULL, agent_ref_transaction_id bigint(20) unsigned DEFAULT NULL,vat_amt decimal(20,2) NOT NULL, taxable_sale decimal(20,2) NOT NULL,cancellation_charges decimal(10,2) DEFAULT '0.00',sale_ref_trans_id bigint(20) DEFAULT '0',claim_status enum('DONE_CLAIM','CLAIM_BAL') NOT NULL,cancel_cause enum('LMS_SERVER','IPE_SERVER','PRINTER','MANUAL') NOT NULL, PRIMARY KEY (refundId)) ENGINE=InnoDB DEFAULT CHARSET=latin1";
				pstmt = con.prepareStatement(crTbl);
				pstmt.setInt(1, gameNo);
//				System.out.println("Query to  sale refund tlb::" + pstmt);
				pstmt.executeUpdate();
				
				crTbl = "create table st_ipe_pwt_inv_? (tid bigint(20) unsigned NOT NULL AUTO_INCREMENT,ticket_nbr bigint(20) unsigned NOT NULL,game_id int(10) unsigned NOT NULL,pwt_amt decimal(20,2) NOT NULL,claim_status enum('CLAIM_PLR_RET_CLM','CLAIM_PLR_RET_UNCLM','CLAIM_PLR_RET_CLM_DIR','CLAIM_PLR_RET_UNCLM_DIR','CLAIM_RET_CLM_AUTO','PND_PAY','CLAIM_PLR_BO','UNCM_PWT','UNCM_PWT_CANCELLED','CLAIM_RET_CLM','CLAIM_RET_UNCLM','REQUESTED','CLAIM_AGT_AUTO','CLAIM_PLR_AGT_CLM_DIR','CLAIM_AGT_TEMP','CLAIM_PLR_AGT_UNCLM_DIR','CLAIM_AGT','CLAIM_RET_AGT_TEMP','CLAIM_PLR_AGT_TEMP','CLAIM_AGT_CLM_AUTO','CANCELLED_PERMANENT','PND_MAS') NOT NULL,retailer_transaction_id bigint(20) unsigned DEFAULT NULL,agent_transaction_id bigint(20) unsigned DEFAULT NULL,bo_transaction_id bigint(20) unsigned DEFAULT NULL,is_direct_plr enum('Y','N') DEFAULT NULL,PRIMARY KEY (tid),UNIQUE KEY (ticket_nbr,game_id))ENGINE=InnoDB DEFAULT CHARSET=latin1";
				pstmt = con.prepareStatement(crTbl);
				pstmt.setInt(1, gameNo);
//				System.out.println("Query to  sale refund tlb::" + pstmt);
				pstmt.executeUpdate();
				
				crTbl = "create table st_ipe_ret_pwt_?(tid bigint(20) unsigned NOT NULL AUTO_INCREMENT,transaction_id bigint(20) unsigned NOT NULL,retailer_user_id int(10) unsigned NOT NULL,retailer_org_id int(10) unsigned NOT NULL,game_id int(10) unsigned NOT NULL,ticket_nbr bigint(20) unsigned NOT NULL,pwt_amt decimal(20,2) NOT NULL,retailer_claim_comm decimal(20,2) NOT NULL DEFAULT '0.00', agt_claim_comm decimal(20,2) NOT NULL,status enum('CLAIM_BAL','UNCLAIM_BAL','DONE_CLM') NOT NULL,PRIMARY KEY (tid),UNIQUE KEY (ticket_nbr,game_id)) ENGINE=InnoDB DEFAULT CHARSET=latin1";
				pstmt = con.prepareStatement(crTbl);
				pstmt.setInt(1, gameNo);
//				System.out.println("Query to  sale refund tlb::" + pstmt);
				pstmt.executeUpdate();
				con.commit();
				onStartGame();
			} catch (Exception e) {
				e.printStackTrace();
				throw new LMSException("Error in new game upload");
			} finally {
				DBConnect.closeCon(con);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public static void onStartGame() {
		System.out.println("inside the methodddddddddddddd");
		InstantPrint service = new InstantPrint();
		IInstantPrint portType = service.getInstantPrintServicePort();
		StartGameBean startGameBean = new StartGameBean();
		startGameBean =portType.onStartGameData();
		StartGameLMSBean stratGameLMSBean = new StartGameLMSBean();
	   	Map<Integer,GameLMSBean> lmsMap = new HashMap<Integer,GameLMSBean>();
	   	GameMap map=startGameBean.getGameMap();
	   	
	   	System.out.println(map.getEntry().size());
	   	
	   	com.skilrock.ipe.instantprint.StartGameBean.GameMap.Entry gMapEntry=null; 
	   	ActiveBookMap.Entry bMapEntry =null;
	   	ImageSizeMap.Entry iMapEntry =null;
	   	ImageDataMap.Entry dMapEntry =null;
	   	ImageTypeMap.Entry tMapEntry =null;
	   	for(int i=0;i<map.getEntry().size();i++){
	   			gMapEntry=map.getEntry().get(i);
	   		 	GameBean gameBean=gMapEntry.getValue();
	   			GameLMSBean gameLMSBean = new GameLMSBean();
	   			gameLMSBean.setGameId(gameBean.getGameId());
				gameLMSBean.setGameNo(gameBean.getGameNo());
				gameLMSBean.setGameName(gameBean.getGameName());
				gameLMSBean.setTicketPrice(gameBean.getTicketPrice());
				if(gameBean.getStartDate()!=null){
					gameLMSBean.setStartDate( new Timestamp(gameBean.getStartDate()));
				}
				if(gameBean.getSaleEndDate()!=null){
					gameLMSBean.setSaleEndDate( new Timestamp(gameBean.getSaleEndDate()));
					
				}
				if(gameBean.getPwtEndDate()!=null){
					gameLMSBean.setPwtEndDate( new Timestamp(gameBean.getPwtEndDate()));
				}
			
				gameLMSBean.setGameStatus(gameBean.getGameStatus());
				ActiveBookMap activeMap = gameBean.getActiveBookMap();
				Map<String,Integer> activeBookMap = new HashMap<String,Integer>();
				for(int j=0;j<activeMap.getEntry().size();j++){
					bMapEntry=activeMap.getEntry().get(j);		
					activeBookMap.put(bMapEntry.getKey(), bMapEntry.getValue());
				}
				gameLMSBean.setActiveBookMap(activeBookMap);
				gameLMSBean.setNoOfTktPerBook(gameBean.getNoOfTktPerBook());
				gameLMSBean.setGameKey(gameBean.getGameKey());
				gameLMSBean.setGameNoDigit(gameBean.getGameNoDigit());
				gameLMSBean.setBookNoDigit(gameBean.getBookNoDigit());
				gameLMSBean.setTktNoDigit(gameBean.getTktNoDigit());
				gameLMSBean.setRankDigit(gameBean.getRankDigit());
				gameLMSBean.setVirnDigit(gameBean.getVirnDigit());
				gameLMSBean.setPackNoDigit(gameBean.getPackNoDigit());
				gameLMSBean.setGamePrintScheme(gameBean.getGamePrintScheme());
				gameLMSBean.setIsSample(gameBean.getIsSample());
				gameLMSBean.setTextOrImage(gameBean.getTextOrImage());
				ImageSizeMap imageMap = gameBean.getImageSizeMap();
				Map<String,Integer> imageSizeMap = new HashMap<String,Integer>();
				for(int j=0;j<imageMap.getEntry().size();j++){
					iMapEntry=imageMap.getEntry().get(j);		
					imageSizeMap.put(iMapEntry.getKey(), iMapEntry.getValue());
				}
				gameLMSBean.setImageSizeMap(imageSizeMap);
				ImageDataMap imgMap = gameBean.getImageDataMap();
				Map<String,byte[]> imgDataMap = new HashMap<String,byte[]>();
				for(int j=0;j<imgMap.getEntry().size();j++){
					dMapEntry=imgMap.getEntry().get(j);		
					imgDataMap.put(dMapEntry.getKey(), dMapEntry.getValue());
				}
				gameLMSBean.setImageDataMap(imgDataMap);
				ImageTypeMap imgTypeMap = gameBean.getImageTypeMap();
				Map<String,ArrayList<String>> itmap = new HashMap<String,ArrayList<String>>();
				for(int j=0;j<imgTypeMap.getEntry().size();j++){
					tMapEntry=imgTypeMap.getEntry().get(j);		
					StringArray list =tMapEntry.getValue();
					itmap.put(tMapEntry.getKey(),new ArrayList<String>(list.getItem()));
				}
				gameLMSBean.setGameLogoType(gameBean.getGameLogoType());
				gameLMSBean.setGameLogoCode(gameBean.getGameLogoCode());	
				gameLMSBean.setImageTypeMap(itmap);
				gameLMSBean.setPrizeLogoType(gameBean.getPrizeLogoType());
				lmsMap.put(gMapEntry.getKey(), gameLMSBean);
				
			
	   	}
	   	
		/*for(Entry<Integer, GameBean> entry:){
			GameBean gameBean = entry.getValue();
			GameLMSBean gameLMSBean = new GameLMSBean();
			gameLMSBean.setGameId(gameBean.getGameId());
			gameLMSBean.setGameNo(gameBean.getGameNo());
			gameLMSBean.setGameName(gameBean.getGameName());
			gameLMSBean.setTicketPrice(gameBean.getTicketPrice());
			gameLMSBean.setStartDate( new Timestamp(gameBean.getStartDate()));
			gameLMSBean.setSaleEndDate( new Timestamp(gameBean.getSaleEndDate()));
			gameLMSBean.setPwtEndDate( new Timestamp(gameBean.getPwtEndDate()));
			gameLMSBean.setGameStatus(gameBean.getGameStatus());
			gameLMSBean.setActiveBookMap((Map<String, Integer>) gameBean.getActiveBookMap());
			gameLMSBean.setNoOfTktPerBook(gameBean.getNoOfTktPerBook());
			gameLMSBean.setGameKey(gameBean.getGameKey());
			gameLMSBean.setGameNoDigit(gameBean.getGameNoDigit());
			gameLMSBean.setBookNoDigit(gameBean.getBookNoDigit());
			gameLMSBean.setTktNoDigit(gameBean.getTktNoDigit());
			gameLMSBean.setRankDigit(gameBean.getRankDigit());
			gameLMSBean.setVirnDigit(gameBean.getVirnDigit());
			gameLMSBean.setPackNoDigit(gameBean.getPackNoDigit());
			gameLMSBean.setGamePrintScheme(gameBean.getGamePrintScheme());
			gameLMSBean.setIsSample(gameBean.getIsSample());
			gameLMSBean.setTextOrImage(gameBean.getTextOrImage());
			gameLMSBean.setImageSizeMap((Map<String, Integer>) gameBean.getImageSizeMap());
			gameLMSBean.setImageDataMap((Map<String, byte[]>) gameBean.getImageDataMap());
			gameLMSBean.setImageTypeMap((Map<String, ArrayList<String>>) gameBean.getImageTypeMap());
			gameLMSBean.setPrizeLogoType(gameBean.getPrizeLogoType());
			lmsMap.put(entry.getKey(), gameLMSBean);
		}
		*/stratGameLMSBean.setGameMap(lmsMap);
		
		
		stratGameLMSBean.setSuccess(startGameBean.isSuccess());
	//		System.out.println(sRes.getResponseData());
		IPEUtility.setGameMap(stratGameLMSBean.getGameMap());
		IPEUtility.setOrgWiseGameInfo(updateOrgGameInfoBean());
	}

	public boolean saveStartGame(int gameId) {
		Connection con = DBConnect.getConnection();
		PreparedStatement pstmt = null;
		InstantPrint service = new InstantPrint();
		IInstantPrint portType = service.getInstantPrintServicePort();
		boolean isStart = false;
		isStart = portType.saveStartGame(gameId);
		
//		System.out.println(sRes.getResponseData());
		if (isStart) {
			try {
				con.setAutoCommit(false);
				String updQry = "update st_ipe_game_master set game_status='OPEN' where game_id=?";
				pstmt = con.prepareStatement(updQry);
				pstmt.setInt(1, gameId);
				pstmt.executeUpdate();
				con.commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				DBConnect.closeCon(con);
			}
		}
		return isStart;
	}
	public static Map<Integer, Map<Integer, GameInfoBean>> updateOrgGameInfoBean() {
		Connection con = DBConnect.getConnection();
		Map<Integer, Map<Integer, GameInfoBean>> orgWiseGameInfo = new HashMap<Integer, Map<Integer, GameInfoBean>>();
		Map<Integer, GameInfoBean> gameMap = new HashMap<Integer, GameInfoBean>();
		GameInfoBean gameInfoBean = null;
		try {
			String selQry = "select game_id, game_no, game_name,agent_sale_comm_rate,agent_pwt_comm_rate,retailer_sale_comm_rate,retailer_pwt_comm_rate,govt_comm_rate,prize_payout_ratio,vat_amt,tickets_in_scheme from st_ipe_game_master where game_status!='CLOSE'";
			ResultSet rs = con.prepareStatement(selQry).executeQuery();
			while (rs.next()) {
				gameInfoBean = new GameInfoBean();
				gameInfoBean.setGameId(rs.getInt("game_id"));
				gameInfoBean.setGameNo(rs.getInt("game_no"));
				gameInfoBean.setGameName(rs.getString("game_name"));
				gameInfoBean.setAgentSaleComm(rs
						.getDouble("agent_sale_comm_rate"));
				gameInfoBean.setAgentPwtComm(rs
						.getDouble("agent_pwt_comm_rate"));
				gameInfoBean.setRetSaleComm(rs
						.getDouble("retailer_sale_comm_rate"));
				gameInfoBean.setRetPwtComm(rs
						.getDouble("retailer_pwt_comm_rate"));
				gameInfoBean.setGovtComm(rs.getDouble("govt_comm_rate"));
				gameInfoBean.setPpr(rs.getDouble("prize_payout_ratio"));
				gameInfoBean.setVatComm(rs.getDouble("vat_amt"));
				gameInfoBean.setTicketInScheme(rs.getInt("tickets_in_scheme"));
				gameMap.put(gameInfoBean.getGameId(), gameInfoBean);
			}

			orgWiseGameInfo.put(0, gameMap);

			selQry = "select retailer_org_id,agtId,retComm.game_id,retSaleComm,retPwtComm,agtSaleComm,agtPwtComm from (select retailer_org_id,parent_id agtId,game_id,sale_comm_variance+default_sale_comm_rate retSaleComm,pwt_comm_variance+default_pwt_comm_rate retPwtComm from st_ipe_agent_retailer_sale_pwt_comm_variance inner join st_lms_organization_master on retailer_org_id=organization_id) retComm left outer join (select agent_org_id,game_id,sale_comm_variance+default_sale_comm_rate agtSaleComm,pwt_comm_variance+default_pwt_comm_rate agtPwtComm from st_ipe_bo_agent_sale_pwt_comm_variance) agtComm on agtId=agent_org_id and retComm.game_id=agtComm.game_id";
			rs = con.prepareStatement(selQry).executeQuery();
			int retOrgId = 0;
			int gameId = 0;
			while (rs.next()) {
				retOrgId = rs.getInt("retailer_org_id");
				orgWiseGameInfo.put(retOrgId,
						new HashMap<Integer, GameInfoBean>(gameMap));
				gameId = rs.getInt("game_id");
				gameInfoBean = (GameInfoBean) orgWiseGameInfo.get(retOrgId)
						.get(gameId).clone();
				gameInfoBean.setRetSaleComm(rs.getDouble("retSaleComm"));
				gameInfoBean.setRetPwtComm(rs.getDouble("retPwtComm"));
				if (rs.getObject("agtSaleComm") != null) {
					gameInfoBean.setAgentSaleComm(rs.getDouble("agtSaleComm"));
				}
				if (rs.getObject("agtPwtComm") != null) {
					gameInfoBean.setAgentSaleComm(rs.getDouble("agtPwtComm"));
				}
				orgWiseGameInfo.get(retOrgId).put(gameId, gameInfoBean);
			}

			selQry = "select agent_org_id,game_id,sale_comm_variance+default_sale_comm_rate agtSaleComm,pwt_comm_variance+default_pwt_comm_rate agtPwtComm from st_ipe_bo_agent_sale_pwt_comm_variance";
			rs = con.prepareStatement(selQry).executeQuery();
			int agtOrgId = 0;
			while (rs.next()) {
				agtOrgId = rs.getInt("agent_org_id");
				orgWiseGameInfo.put(agtOrgId,
						new HashMap<Integer, GameInfoBean>(gameMap));
				gameId = rs.getInt("game_id");
				gameInfoBean = (GameInfoBean) orgWiseGameInfo.get(agtOrgId)
						.get(gameId).clone();
				gameInfoBean.setAgentSaleComm(rs.getDouble("agtSaleComm"));
				gameInfoBean.setAgentPwtComm(rs.getDouble("agtPwtComm"));
				orgWiseGameInfo.get(agtOrgId).put(gameId, gameInfoBean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnect.closeCon(con);
		}
		return orgWiseGameInfo;
	}
}
