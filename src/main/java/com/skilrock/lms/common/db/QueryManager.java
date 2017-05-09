package com.skilrock.lms.common.db;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.common.filter.LMSFilterDispatcher;
import com.skilrock.lms.common.utility.CommonMethods;
import com.skilrock.lms.common.utility.CommonQueriesUtility;

public class QueryManager {
	static Log logger = LogFactory.getLog(QueryManager.class);
	private static String orgCodeQry = " name orgCode ";
	private static String appendOrder ="orgCode ASC ";
	static {
		
		setOrgCodeQuery();
		setAppendOrgOrder();
		
	}
	// DG Jackpot View Queries By Neeraj

	public static String closeST3ExpireGames() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_CLOSE_EXPIRE_GAMES;
	}

	// DG Sale Reports queries by sachin

	public static String createST3Task() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_CREATE_TASK_UNCLM;
	}

	public static String extendPwtEndDate() {

		return MySqlQueries.ST3_EXT_PWT_END_DATE;
	}

	public static String extendSaleEndDate() {

		return MySqlQueries.ST3_EXT_SALE_END_DATE;
	}

	public static String extendSalePwtEndDate() {

		return MySqlQueries.ST3_EXT_SALE_PWT_DATE;
	}

	public static String fetchExistedInvList() {
		return MySqlQueries.FETCH_EXISTED_INV_LIST_QUERY;
	}

	public static String getAgentLatestDRNoteNb() {
		return MySqlQueries.GET_Agent_LATEST_DR_NOTE_NBR;
	}

	public static String getAgentLatestCRNoteNb() {
		return MySqlQueries.GET_Agent_LATEST_CR_NOTE_NBR;
	}

	// DG PWT Reports queries by Sachin

	public static String getAGENTLatestReceiptNb() {
		return MySqlQueries.GET_AGENT_LATEST_RECEIPT_NBR;
	}

	public static String getAgtReconBookwiseDate() {
		return MySqlQueries.GET_AGT_REC_BOOKWISE_DATE;
	}

	public static String getAgtReconTktwiseDate() {
		return MySqlQueries.GET_AGT_REC_TKTWISE_DATE;
	}
	public static String getST2AgentCSSale() {
		return MySqlQueries.ST2_AGENT_CS_SALE;
	}
	public static String getST2AgentOLADeposit() {
		return MySqlQueries.ST2_AGENT_OLA_DEPOSIT;
	}
	
	public static String getST2AgtCSRefnd() {
		return MySqlQueries.ST2_AGENT_CS_RFND;
	}
	public static String getST2AgtOLARefnd() {
		return MySqlQueries.ST2_AGENT_OLA_RFND;
	}
	
	// Gaurav's Queries
	// Function---------------------------------------------------
	// BO LEDGER
	// SELECT
	public static String getBoDate() {
		return MySqlQueries.ST2_BO_DATE;
	}

	public static String getBOLatestDRNoteNb() {
		return MySqlQueries.GET_BO_LATEST_DR_NOTE_NBR;
	}

	public static String getBOLatestReceiptNb() {
		return MySqlQueries.GET_BO_LATEST_RECEIPT_NBR;
	}

	public static String getCountryAndStateCode() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_COUNTRY_STATE_CODE;
	}
	
	public static String getStateAndCityCode() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_STATE_CITY_CODE;
	}

	public static String getCountryAndStateName() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_COUNTRY_STATE_DETAILS;
	}

	public static String getDetailsfromGameMaster() {

		return MySqlQueries.ST3_GET_GAME_RANKS;
	}

	// Query for retailer Daily Ledger

	public static String getGameDates() {

		return MySqlQueries.ST3_GET_GAME_DATES;
	}

	public static String getIWGameDates() {

		return MySqlQueries.ST3_GET_IWGAME_DATES;
	}
	
	// Queries for DG Direct Player PWT Reports

	public static String getGameDetails() {

		return MySqlQueries.ST3_GET_GAME_DATAILS;
	}
	
	public static String getIWGameDetails() {

		return MySqlQueries.ST3_GET_IWGAME_DATAILS;
	}

	public static String getGameDetailsFromGameNameNbr() {
		return MySqlQueries.ST1_GAME_ID_FROM_NAME_NBR;
	}

	public static String getGameFormatInformation() {
		return MySqlQueries.ST1_GAME_FORMAT_INFO;
	}

	public static String getGameId() {

		return MySqlQueries.ST3_GET_GAME_ID;
	}

	public static String getGameIdFromGameNameNbr() {
		return MySqlQueries.ST1_GAME_ID_RET_FROM_NAME_NBR;
	}

	public static String getLatestMonthTDSDG() {

		return MySqlQueries.ST3_GET_LATEST_MONTH_TDS_DG;
	}

	public static String getLatestMonthTDSSE() {

		return MySqlQueries.ST3_GET_LATEST_MONTH_TDS_SE;
	}

	public static String getLatestMonthVATAgtDG() {

		return MySqlQueries.ST3_GET_LATEST_MONTH_VAT_AGT_DG;
	}

	public static String getLatestMonthVATAgtSE() {

		return MySqlQueries.ST3_GET_LATEST_MONTH_VAT_AGT_SE;
	}

	public static String getLatestMonthVATDG() {

		return MySqlQueries.ST3_GET_LATEST_MONTH_VAT_DG;
	}

	// Arun's Query function

	public static String getLatestMonthVATSE() {

		return MySqlQueries.ST3_GET_LATEST_MONTH_VAT_SE;
	}
	
	public static String getLatestMonthVATIW() {

		return MySqlQueries.ST3_GET_LATEST_MONTH_VAT_SE;
	}

	public static String getOrganizationDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_ORG_DETAILS;
	}

	public static String getRemainingBooksAtBO() {

		return MySqlQueries.ST3_GET_REMAINING_BOOKS_BO;
	}

	public static String getRetDaliyLadger() {
		return MySqlQueries.RET_DAILY_LEDGER;
	}

	public static String getRETLatestReceiptNb() {
		return MySqlQueries.GET_RET_LATEST_RECEIPT_NBR;
	}

	public static String getRetReconTktwiseDate() {
		return MySqlQueries.GET_RET_REC_TKT_WISE_DATE;
	}

	public static String getST_AGENT_INVOICE_DETAILS() {
		return MySqlQueries.ST_AGENT_INVOICE_DETAILS;
	}

	public static String getST_AGENT_SALE_REPORT_GAME_WISE() {
		return MySqlQueries.ST_AGENT_SALE_REPORT_GAME_WISE;
	}

	public static String getST_AGENT_SALE_REPORT_GET_GAME_ID() {
		return MySqlQueries.ST_AGENT_SALE_REPORT_GET_GAME_ID;
	}

	public static String getST_AGENT_SALE_RETURN_DETAILS() {
		return MySqlQueries.ST_AGENT_SALE_RETURN_DETAILS;
	}

	public static String getST_BO_AGENT_PWT_DETAILS() {
		return MySqlQueries.ST_BO_AGENT_PWT_DETAILS;
	}

	public static String getST_BO_GAME_WISE_PWT_AGENT_DETAILS() {
		return MySqlQueries.ST_BO_GAME_WISE_PWT_AGENT_DETAILS;
	}

	// pwt details game wise
	public static String getST_BO_GAME_WISE_PWT_GAME_DETAILS() {
		return MySqlQueries.ST_BO_GAME_WISE_PWT_GAME_DETAILS;
	}

	public static String getST_BO_GAME_WISE_PWT_PLAYER_DETAILS() {
		return MySqlQueries.ST_BO_GAME_WISE_PWT_PLAYER_DETAILS;
	}

	public static String getST_BO_GAME_WISE_PWT_RET_DETAILS() {
		return MySqlQueries.ST_BO_GAME_WISE_PWT_RET_DETAILS;
	}

	public static String getST_BO_GAME_WISE_TOTAL_PWT_DETAILS() {
		return MySqlQueries.ST_BO_GAME_WISE_TOTAL_PWT_DETAILS;
	}

	public static String getST_BO_INVOICE_CUSTOMER_DETAILS() {
		return MySqlQueries.ST_BO_INVOICE_CUSTOMER_DETAILS;
	}

	// delivery challan
	public static String getST_BO_INVOICE_DETAILS() {
		return MySqlQueries.ST_BO_INVOICE_DETAILS;
	}

	public static String getST_BO_PLAYER_PWT_DETAILS() {
		return MySqlQueries.ST_BO_PLAYER_PWT_DETAILS;
	}

	public static String getST_BO_SALERETURN_DETAILS() {
		return MySqlQueries.ST_BO_SALE_RETURN_DETAILS;
	}

	public static String getST_CASH_CHEQ_REPORT_BO1() {
		return MySqlQueries.ST_CASH_CHEQ_REPORT_BO1;
	}

	public static String getST_CASH_CHEQ_REPORT_BO2() {
		return MySqlQueries.ST_CASH_CHEQ_REPORT_BO2;
	}
	
	public static String getST_CASH_CHEQ_REPORT_BO3() {
		return MySqlQueries.ST_CASH_CHEQ_REPORT_BO3;
	}

	public static String getST_CASH_CHEQ_REPORT_DETAIL() {
		return MySqlQueries.ST_CASH_CHEQ_REPORT_DETAIL;
	}

	public static String getST_CASH_CHEQ_REPORT_RETAILER_ID() {
		return MySqlQueries.ST_CASH_CHEQ_REPORT_RETAILER_ID;
	}

	public static String getST_COLLECTION_DETAILS() {
		return MySqlQueries.ST_COLLECTION_DETAILS_FOR_BO;
	}

	public static String getST_COLLECTION_DETAILS_FOR_AGENT() {
		return MySqlQueries.ST_COLLECTION_DETAILS_FOR_AGENT;
	}

	public static String getST_COLLECTION_DETAILS_FOR_AGENT_DG() {
		return MySqlQueries.ST_COLLECTION_DETAILS_FOR_AGENT_DG;
	}
	public static String getST_COLLECTION_DETAILS_FOR_AGENT_OLA()
	{
		return MySqlQueries.ST_COLLECTION_DETAILS_FOR_AGENT_OLA;
	}

	public static String getST_COLLECTION_DETAILS_FOR_AGENT_SE() {
		return MySqlQueries.ST_COLLECTION_DETAILS_FOR_AGENT_SE;
	}

	public static String getST_COLLECTION_DETAILS_FOR_BO_DG() {
		return MySqlQueries.ST_COLLECTION_DETAILS_FOR_BO_DG;
	}
	public static String getST_COLLECTION_DETAILS_FOR_BO_OLA() {
		return MySqlQueries.ST_COLLECTION_DETAILS_FOR_BO_OLA;
	}

	public static String getST_COLLECTION_DETAILS_FOR_BO_SE() {
		return MySqlQueries.ST_COLLECTION_DETAILS_FOR_BO_SE;
	}
	
	public static String getST_COLLECTION_DETAILS_FOR_BO_CS() {
		return MySqlQueries.ST_COLLECTION_DETAILS_FOR_BO_CS;
	}

	public static String getST_DG_DIR_PLR_PWT_REPORT_AGENT_WISE_BO() {
		return MySqlQueries.ST_DG_DIR_PLR_PWT_REPORT_AGENT_WISE_BO;
	}

	public static String getST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_AGT() {
		return MySqlQueries.ST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_AGT;
	}

	public static String getST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_AGT_NEW() {
		return MySqlQueries.ST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_AGT_NEW;
	}

	public static String getST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_BO() {
		return MySqlQueries.ST_DG_DIR_PLR_PWT_REPORT_GAME_WISE_BO;
	}

	public static String getST_DG_JACKPOT_REPORT_GAME_WISE_BO() {
		return MySqlQueries.ST_DG_JACKPOT_REPORT_GAME_WISE_BO;
	}

	public static String getST_DG_PWT_REPORT_AGENT_WISE_BO() {
		return MySqlQueries.ST_DG_PWT_REPORT_AGENT_WISE_BO;
	}

	public static String getST_DG_PWT_REPORT_GAME_WISE_AGT() {
		return MySqlQueries.ST_DG_PWT_REPORT_GAME_WISE_AGT;
	}

	public static String getST_DG_PWT_REPORT_GAME_WISE_AGT_NEW() {
		return MySqlQueries.ST_DG_PWT_REPORT_GAME_WISE_AGT_NEW;
	}

	public static String getST_DG_PWT_REPORT_GAME_WISE_BO() {
		return MySqlQueries.ST_DG_PWT_REPORT_GAME_WISE_BO;
	}

	public static String getST_DG_PWT_REPORT_GAME_WISE_RET() {
		return MySqlQueries.ST_DG_PWT_REPORT_GAME_WISE_RET;
	}

	public static String getST_DG_PWT_REPORT_RETAILER_WISE_AGT() {
		return MySqlQueries.ST_DG_PWT_REPORT_RETAILER_WISE_AGT;
	}

	public static String getST_DG_PWT_REPORT_RETAILER_WISE_AGT_NEW() {
		return MySqlQueries.ST_DG_PWT_REPORT_RETAILER_WISE_AGT_NEW;
	}

	public static String getST_DG_PWT_REPORT_RETAILER_WISE_BO() {
		return MySqlQueries.ST_DG_PWT_REPORT_RETAILER_WISE_BO;
	}

	public static String getST_DG_SALE_REPORT() {
		return MySqlQueries.ST_DG_SALE_REPORT;
	}

	public static String getST_DG_SALE_REPORT_AGENT_WISE() {
		return MySqlQueries.ST_DG_SALE_REPORT_AGENT_WISE;
	}

	public static String getST_DG_SALE_REPORT_AGENT_WISE_BO() {
		return MySqlQueries.ST_DG_SALE_REPORT_AGENT_WISE_BO;
	}

	public static String getST_DG_SALE_REPORT_GAME_WISE() {
		return MySqlQueries.ST_DG_SALE_REPORT_GAME_WISE;
	}

	public static String getST_DG_SALE_REPORT_GAME_WISE_AGT() {
		return MySqlQueries.ST_DG_SALE_REPORT_GAME_WISE_AGT;
	}

	public static String getST_DG_SALE_REPORT_GAME_WISE_BO() {
		return MySqlQueries.ST_DG_SALE_REPORT_GAME_WISE_BO;
	}

	public static String getST_DG_SALE_REPORT_GAME_WISE_RET() {
		return MySqlQueries.ST_DG_SALE_REPORT_GAME_WISE_RET;
	}

	public static String getST_DG_SALE_REPORT_RETAILER_WISE_AGT() {
		return MySqlQueries.ST_DG_SALE_REPORT_RETAILER_WISE_AGT;
	}

	public static String getST_DG_SALE_REPORT_RETAILER_WISE_BO() {
		return MySqlQueries.ST_DG_SALE_REPORT_RETAILER_WISE_BO;
	}

	public static String getST_FETCH_CONS_COUNTS_FORORG() {
		
		String consCoountQry ="select party_org_id 'current_owner_id',name orgCode, party_type 'current_owner_type', sum(cumm_qty_count) 'inv_count', cost_per_unit 'cost', bb.inv_id, cc.inv_name from st_lms_cons_inv_status aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc, st_lms_organization_master ee where aa.inv_model_id =  bb.inv_model_id and aa.party_org_id = ee.organization_id and bb.inv_id = cc.inv_id  ";
		
		if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")){
			
			consCoountQry ="select party_org_id 'current_owner_id',org_code orgCode, party_type 'current_owner_type', sum(cumm_qty_count) 'inv_count', cost_per_unit 'cost', bb.inv_id, cc.inv_name from st_lms_cons_inv_status aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc, st_lms_organization_master ee where aa.inv_model_id =  bb.inv_model_id and aa.party_org_id = ee.organization_id and bb.inv_id = cc.inv_id  ";
		
					
		}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")){
		
			consCoountQry ="select party_org_id 'current_owner_id',concat(org_code,'_',name) orgCode , party_type 'current_owner_type', sum(cumm_qty_count) 'inv_count', cost_per_unit 'cost', bb.inv_id, cc.inv_name from st_lms_cons_inv_status aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc, st_lms_organization_master ee where aa.inv_model_id =  bb.inv_model_id and aa.party_org_id = ee.organization_id and bb.inv_id = cc.inv_id  ";
		
				
		
		}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
			consCoountQry ="select party_org_id 'current_owner_id',concat(name,'_',org_code) orgCode, party_type 'current_owner_type', sum(cumm_qty_count) 'inv_count', cost_per_unit 'cost', bb.inv_id, cc.inv_name from st_lms_cons_inv_status aa, st_lms_cons_inv_specification bb, st_lms_inv_master cc, st_lms_organization_master ee where aa.inv_model_id =  bb.inv_model_id and aa.party_org_id = ee.organization_id and bb.inv_id = cc.inv_id  ";
			
			
		}	
	
		return consCoountQry;
	}

	public static String getST_FETCH_CONS_DETAIL_FORORG() {
		return MySqlQueries.ST_FETCH_CONS_DETAIL_FORORG;
	}

	public static String getST_FETCH_NON_CONS_COUNTS_FORORG() {
		
		String nonConsCoountQry ="select current_owner_id, name orgCode, current_owner_type, count(serial_no) 'inv_count', cost_to_bo 'cost', inv_model_id, cc.inv_name, bb.inv_id, brand_id from st_lms_inv_status aa, st_lms_inv_model_master bb, st_lms_inv_master cc,st_lms_organization_master ee where aa.inv_model_id =  bb.model_id and aa.current_owner_id = ee.organization_id and bb.inv_id = cc.inv_id and  current_owner_type<>'REMOVED'";
			
		if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")){
			
			nonConsCoountQry ="select current_owner_id, org_code orgCode , current_owner_type, count(serial_no) 'inv_count', cost_to_bo 'cost', inv_model_id, cc.inv_name, bb.inv_id, brand_id from st_lms_inv_status aa, st_lms_inv_model_master bb, st_lms_inv_master cc,st_lms_organization_master ee where aa.inv_model_id =  bb.model_id and aa.current_owner_id = ee.organization_id and bb.inv_id = cc.inv_id and  current_owner_type<>'REMOVED'";
		
					
		}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")){
		
			nonConsCoountQry ="select current_owner_id, concat(org_code,'_',name) orgCode, current_owner_type, count(serial_no) 'inv_count', cost_to_bo 'cost', inv_model_id, cc.inv_name, bb.inv_id, brand_id from st_lms_inv_status aa, st_lms_inv_model_master bb, st_lms_inv_master cc,st_lms_organization_master ee where aa.inv_model_id =  bb.model_id and aa.current_owner_id = ee.organization_id and bb.inv_id = cc.inv_id and  current_owner_type<>'REMOVED'";
		
				
		
		}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
			nonConsCoountQry ="select current_owner_id, concat(name,'_',org_code) orgCode, current_owner_type, count(serial_no) 'inv_count', cost_to_bo 'cost', inv_model_id, cc.inv_name, bb.inv_id, brand_id from st_lms_inv_status aa, st_lms_inv_model_master bb, st_lms_inv_master cc,st_lms_organization_master ee where aa.inv_model_id =  bb.model_id and aa.current_owner_id = ee.organization_id and bb.inv_id = cc.inv_id and  current_owner_type<>'REMOVED'";
			
			
		}	
		
		
		
		
		
		//return MySqlQueries.ST_FETCH_NON_CONS_COUNTS_FORORG;
		
		return nonConsCoountQry;
	}

	public static String getST_FETCH_NON_CONS_DETAIL_FORORG() {
		return MySqlQueries.ST_FETCH_NON_CONS_DETAIL_FORORG;
	}

	public static String getST_GAME_NAME() {
		return MySqlQueries.ST_GAME_NAME;
	}

	public static String getST_GET_ORG_NAME() {
		return MySqlQueries.ST_GET_ORG_NAME;
	}

	public static String getST_INVENTORY_GAME_REPORT() {
		return MySqlQueries.ST_INVENTORY_GAME_REPORT;
	}

	public static String getST_INVENTORY_GAME_REPORT_FOR_AGENT() {
		return MySqlQueries.ST_INVENTORY_GAME_REPORT_FOR_AGENT;
	}

	public static String getST_INVENTORY_GAME_REPORT_FOR_AGENT_RET_ONLINE() {
		return MySqlQueries.ST_INVENTORY_GAME_REPORT_FOR_AGENT_RET_ONLINE;
	}

	public static String getST_INVENTORY_GAME_REPORT_FOR_RETAILER() {
		return MySqlQueries.ST_INVENTORY_GAME_REPORT_FOR_RETAILER;
	}

	public static String getST_INVENTORY_GAME_REPORT_RET_ONLINE() {
		return MySqlQueries.ST_INVENTORY_GAME_REPORT_RET_ONLINE;
	}

	public static String getST_INVENTORY_GAME_SEARCH() {
		return MySqlQueries.ST_INVENTORY_GAME_SEARCH;
	}

	public static String getST_INVENTORY_GAME_SEARCH_LINK() {
		return MySqlQueries.ST_INVENTORY_GAME_SEARCH_LINK;
	}

	public static String getST_NO_OF_PRIZE_REM() {
		return MySqlQueries.ST_NO_OF_PRIZE_REM;
	}

	public static String getST_PLAYER_PWT_REPORT_BO() {
		return MySqlQueries.ST_PLAYER_PWT_REPORT_BO;
	}

	public static String getST_PWT_PLR_REPORT_AGENT() {
		return MySqlQueries.ST_PWT_PLR_REPORT_AGENT;
	}

	public static String getST_PWT_REPORT_AGENT() {
		return MySqlQueries.ST_PWT_REPORT_AGENT;
	}

	public static String getST_PWT_REPORT_AGENT1() {
		return MySqlQueries.ST_PWT_REPORT_AGENT1;
	}

	public static String getST_PWT_REPORT_BO() {
		return MySqlQueries.ST_PWT_REPORT_BO;
	}

	public static String getST_PWT_REPORT_BO1() {
		return MySqlQueries.ST_PWT_REPORT_BO1;
	}

	public static String getST_RECEIPT_SEARCH() {
		return MySqlQueries.ST_RECEIPT_SEARCH;
	}
	
	public static String getST_RECEIPT_SEARCH_DLNOTE() {
		
		if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")){
		
			return "select aa.dl_id,  aa.date, aa.generated_dl_id,aa.organization_type,aa.name,om.name 'owner_name',orgCode from st_lms_organization_master om,( select bodl.dl_id, bodl.generated_dl_id,ind.date,umas.organization_type,umas.name,umas.org_code orgCode,umas.parent_id  from  st_lms_inv_dl_detail bodl, st_lms_organization_master umas,st_lms_inv_dl_task_mapping dtm, st_lms_inv_detail ind where dtm.dl_id= bodl.dl_id and ind.current_owner_id=umas.organization_id  and ind.current_owner_type=umas.organization_type and ind.task_id = dtm.task_id  and ind.current_owner_type='AGENT'";
					
		}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE_NAME")){
			
			return "select aa.dl_id,  aa.date, aa.generated_dl_id,aa.organization_type,aa.name,om.name 'owner_name',orgCode from st_lms_organization_master om,( select bodl.dl_id, bodl.generated_dl_id,ind.date,umas.organization_type,umas.name,concat(umas.org_code,'_',umas.name) orgCode,umas.parent_id  from  st_lms_inv_dl_detail bodl, st_lms_organization_master umas,st_lms_inv_dl_task_mapping dtm, st_lms_inv_detail ind where dtm.dl_id= bodl.dl_id and ind.current_owner_id=umas.organization_id  and ind.current_owner_type=umas.organization_type and ind.task_id = dtm.task_id  and ind.current_owner_type='AGENT'";
				
		
		}else if((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("NAME_CODE")){
			return "select aa.dl_id,  aa.date, aa.generated_dl_id,aa.organization_type,aa.name,om.name 'owner_name',orgCode from st_lms_organization_master om,( select bodl.dl_id, bodl.generated_dl_id,ind.date,umas.organization_type,umas.name,concat(umas.name,'_',umas.org_code) orgCode,umas.parent_id  from  st_lms_inv_dl_detail bodl, st_lms_organization_master umas,st_lms_inv_dl_task_mapping dtm, st_lms_inv_detail ind where dtm.dl_id= bodl.dl_id and ind.current_owner_id=umas.organization_id  and ind.current_owner_type=umas.organization_type and ind.task_id = dtm.task_id  and ind.current_owner_type='AGENT'";
			
		}
		 
		
		return MySqlQueries.ST_RECEIPT_SEARCH_DLNOTE;
	}
	
	public static String getST_RECEIPT_SEARCH_AGENT_ORGID() {
		return MySqlQueries.ST_RECEIPT_SEARCH_AGENT_ORGID;
	}

	public static String getST_RECEIPT_SEARCH_RET() {
		return MySqlQueries.ST_RECEIPT_SEARCH_RET;
	}

	public static String getST_REPORT_MAIL_SCHEDULAR_GET_AGENT_DETAIL() {
		return MySqlQueries.ST_REPORT_MAIL_SCHEDULAR_GET_AGENT_DETAIL;
	}

	public static String getST_REPORT_MAIL_SCHEDULER_GET_AGENT_DETAIL() {
		return MySqlQueries.ST_REPORT_MAIL_SCHEDULER_GET_AGENT_DETAIL;
	}

	public static String getST_REPORT_MAIL_SCHEDULER_GET_AGENT_EMAILID() {
		return MySqlQueries.ST_REPORT_MAIL_SCHEDULER_GET_AGENT_EMAILID;
	}

	public static String getST_REPORT_MAIL_SCHEDULER_GET_BO_DETAIL() {
		return MySqlQueries.ST_REPORT_MAIL_SCHEDULER_GET_BO_DETAIL;
	}

	public static String getST_REPORT_MAIL_SCHEDULER_GET_BO_EMAILID() {
		return MySqlQueries.ST_REPORT_MAIL_SCHEDULER_GET_BO_EMAILID;
	}

	public static String getST_SALE_REPORT_AGENT_WISE() {
		return MySqlQueries.ST_SALE_REPORT_AGENT_WISE;
	}

	public static String getST_SALE_REPORT_AGENTS_RET_LIST() {
		return MySqlQueries.ST_SALE_REPORT_AGENTS_RET_LIST;
	}

	public static String getST_SALE_REPORT_AGENTS_SALE_DETAIL() {
		return MySqlQueries.ST_SALE_REPORT_AGENTS_SALE_DETAIL;
	}

	public static String getST_SALE_REPORT_GAME_WISE() {
		return MySqlQueries.ST_SALE_REPORT_GAME_WISE;
	}

	public static String getST_SALE_REPORT_GET_AGENT_ID() {
		return MySqlQueries.ST_SALE_REPORT_GET_AGENT_ID;
	}

	public static String getST_SALE_REPORT_GET_GAME_ID() {
		return MySqlQueries.ST_SALE_REPORT_GET_GAME_ID;
	}

	public static String getST_SALE_REPORT_GET_GAMEID() {
		return MySqlQueries.ST_SALE_REPORT_GET_GAMEID;
	}

	public static String getST_SALE_REPORT_GET_RETAILER_ORG_ID() {
		return MySqlQueries.ST_SALE_REPORT_GET_RETAILER_ORG_ID;
	}

	public static String getST_SALE_REPORT_GET_SALE_DETAIL() {
		return MySqlQueries.ST_SALE_REPORT_GET_SALE_DETAIL;
	}

	public static String getST_SALE_REPORT_RETAILER_WISE() {
		return MySqlQueries.ST_SALE_REPORT_RETAILER_WISE;
	}

	public static String getST1_AGENT_ORG_ACTIVE() {
		return MySqlQueries.ST1_AGENT_ORG_ACTIVE;
	}

	// new-------------

	public static String getST1_GAME_PRIZES_LEFT() {
		return MySqlQueries.ST1_GAME_PRIZES_LEFT;
	}

	public static String getST1ActiveAgtGamesQuery() {
		return MySqlQueries.ST1_ACTIVE_AGT_GAMES;
	}

	public static String getST1ActiveGamesQuery() {
		return MySqlQueries.ST1_ACTIVE_GAMES;
	}

	public static String getST1AgentAppBooks() {
		return MySqlQueries.ST1_AGENT_APP_BOOKS;
	}

	public static String getST1AgentAppOrderGamesQuery() {
		return MySqlQueries.ST1_AGENT_APP_ORDER_GAMES;
	}

	public static String getST1AgentAppOrderQuery() {
		return MySqlQueries.ST1_AGENT_APPROVED_ORDERS;
	}

	public static String getST1AgentBOAccFetchQuery() {
		return MySqlQueries.ST1_AGENT_BO_ACC_DETAIL;
	}

	public static String getST1AgentBookInvVerifyQuery() {
		return MySqlQueries.ST1_AGENT_BOOK_INV_VERIFY;
	}

	public static String getST1AgentOrderInInsertQuery() {
		return MySqlQueries.ST1_INSERT_AGENT_ORDER_INVOICES;
	}

	public static String getST1AgentPackInvVerifyQuery() {
		return MySqlQueries.ST1_AGENT_PACK_INV_VERIFY;
	}

	public static String getST1AgentRetailerAccFetchQuery() {
		return MySqlQueries.ST1_AGENT_RET_ACC_DETAIL;
	}

	public static String getST1AgentRetQuery() {
		return MySqlQueries.ST1_AGENT_RETAILER;
	}
	public static String getST1AgentRetQueryForLooseSale() {
		return MySqlQueries.ST1_AGENT_RETAILER_FOR_LOOSE_SALE;
	}

	public static String getST1AgentTotalBooks() {
		return MySqlQueries.ST1_AGENT_TOTAL_BOOKS;
	}

	public static String getST1AgentVerifyQuery() {
		return MySqlQueries.ST1_AGENT_BOOK_VERIFY;
	}

	public static String getST1AgtMasterQuery() {
		return MySqlQueries.ST1_AGENT_MASTER;
	}

	public static String getST1AgtOrdGamesUpdQuery() {
		return MySqlQueries.ST1_UPDATE_AGENT_ORDERED_GAMES;
	}

	public static String getST1AgtOrdUpdQuery() {
		return MySqlQueries.ST1_UPDATE_AGENT_ORDER;
	}

	public static String getST1AgtOrgQuery() {
		return MySqlQueries.ST1_AGENT_ORG;
	}

	public static String getST1AgtOrgQueryPwt() {
		return MySqlQueries.ST1_AGENT_ORG_PWT;
	}

	public static String getST1AgtReceiptsMappingQuery() {
		return MySqlQueries.ST1_INSERT_AGENT_RECEIPTS_MAPPING;
	}

	public static String getST1AgtReceiptsQuery() {
		return MySqlQueries.ST1_INSERT_AGENT_RECEIPTS;
	}

	// added by yogesh for retailer online
	public static String getST1AgtUpdGameInvStatusQuery() {
		return MySqlQueries.ST1_AGT_UPDATE_GAME_INV_STATUS;
	}

	public static String getST1AppOrderGamesQuery() {
		return MySqlQueries.ST1_BO_APP_ORDER_GAMES;
	}

	public static String getST1AppOrderQuery() {
		return MySqlQueries.ST1_BO_APPROVED_ORDERS;
	}

	public static String getST1AutoInsertAgtOrderedGamesQuery() {
		return MySqlQueries.ST1_INSERT_AGENT_ORDERED_GAMES_BY_RET;
	}

	public static String getST1AutoInsertBOOrderedGamesQuery() {
		return MySqlQueries.ST1_INSERT_BO_ORDERED_GAMES_AUTO;
	}

	public static String getST1BOAgentQuery() {
		return MySqlQueries.ST1_BO_AGENT;
	}
	public static String getST1BOAgentLooseSaleQuery() {
		return MySqlQueries.ST1_BO_AGENT_LOOSE_SALE;
	}
	public static String getST1BOAppBooks() {
		return MySqlQueries.ST1_BO_APP_BOOKS;
	}

	public static String getST1BOBookInvVerifyQuery() {
		return MySqlQueries.ST1_BO_BOOK_INV_VERIFY;
	}

	public static String getST1BOMasterQuery() {
		return MySqlQueries.ST1_BO_MASTER;
	}

	public static String getST1BooksForPack() {
		return MySqlQueries.ST1_BOOKS_FOR_PACK;
	}

	public static String getST1BooksWithAgentFetchQuery() {
		return MySqlQueries.ST1_GAME_WITH_AGENT;
	}

	public static String getST1BooksWithRetailerFetchQuery() {
		return MySqlQueries.ST1_GAME_WITH_RETAILER_BY_AGENT;
	}

	public static String getST1BooksWithRetailerFromAgentFetchQuery() {
		return MySqlQueries.ST1_GAME_WITH_RETAILER_BY_AGENT;
	}

	public static String getST1BOOrdGamesUpdQuery() {
		return MySqlQueries.ST1_UPDATE_BO_ORDERED_GAMES;
	}

	public static String getST1BOOrdUpdQuery() {
		return MySqlQueries.ST1_UPDATE_BO_ORDER;
	}

	public static String getST1BOPackInvVerifyQuery() {
		return MySqlQueries.ST1_BO_PACK_INV_VERIFY;
	}

	public static String getST1BOReceiptsMappingQuery() {
		return MySqlQueries.ST1_INSERT_BO_RECEIPTS_MAPPING;
	}

	public static String getST1BOReceiptsQuery() {
		return MySqlQueries.ST1_INSERT_BO_RECEIPTS;
	}

	public static String getST1BOTotalBooks() {
		return MySqlQueries.ST1_BO_TOTAL_BOOKS;
	}

	public static String getST1BOUpdGameInvStatusQuery() {
		return MySqlQueries.ST1_BO_UPDATE_GAME_INV_STATUS;
	}

	public static String getST1BOVerifyQuery() {
		return MySqlQueries.ST1_BO_BOOK_VERIFY;
	}

	public static String getST1DistinctPrizeQuery() {
		return MySqlQueries.ST1_DISTINCT_PRIZES;
	}

	public static String getST1GameDetailsFetchQuery() {
		return MySqlQueries.ST1_GAME_DETAILS;
	}

	public static String getST1GameSearchAgentQuery() {
		return MySqlQueries.ST1_SEARCH_AGENT_GAME;
	}

	public static String getST1GameSearchBOQuery() {
		return MySqlQueries.ST1_SEARCH_BO_GAME;
	}

	// Aman's Query Functions.----------------------------------
	public static String getST1GameSearchQuery() {
		return MySqlQueries.ST1_SEARCH_GAME;
	}

	public static String getST1GameSearchRetailerQuery() {

		return MySqlQueries.ST1_SEARCH_RETAILER_GAME;

	}

	public static String getST1InsertAgtOrderedGamesQuery() {
		return MySqlQueries.ST1_INSERT_AGENT_ORDERED_GAMES;
	}

	public static String getST1InsertAgtOrderQuery() {
		return MySqlQueries.ST1_INSERT_AGENT_ORDER;
	}

	public static String getST1InsertBOOrderedGamesQuery() {
		return MySqlQueries.ST1_INSERT_BO_ORDERED_GAMES;
	}

	public static String getST1InsertBOOrderQuery() {
		return MySqlQueries.ST1_INSERT_BO_ORDER;
	}

	public static String getST1InsertRetOrderedGamesQuery() {
		return MySqlQueries.ST1_INSERT_RET_ORDERED_GAMES;
	}

	public static String getST1InvDetailInsertQuery() {
		return MySqlQueries.ST1_INSERT_GAME_INV_DETAIL;
	}
	
	public static String getST1InvDetailWithWarehouseInsertQuery() {
		return MySqlQueries.ST1_INSERT_GAME_INV_WAREHOUSE_DETAIL;
	}
	
	public static String getST1InvAgentInvoiceDetailInsertQuery() {
		return MySqlQueries.ST1_INSERT_GAME_INV_AGENT_INVOICE_DETAIL;
	}
	
	public static String getST1InvRetailerInvoiceDetailInsertQuery() {
		return MySqlQueries.ST1_INSERT_GAME_INV_RETAILER_INVOICE_DETAIL;
	}

	public static String getST1OrderInInsertQuery() {
		return MySqlQueries.ST1_INSERT_ORDER_INVOICES;
	}

	public static String getST1OrgAddrQuery() {
		return MySqlQueries.ST1_GET_ORG_ADDRESS;
	}

	// new
	public static String getST1OrgCreditQuery() {
		return MySqlQueries.ST1_ORG_CREDIT;
	}

	public static String getST1OrgCreditUpdateQuery() {
		return MySqlQueries.ST1_ORG_CREDIT_UPDATE;
	}

	public static String getST1OrgForUser() {
		return MySqlQueries.ST1_ORG_FOR_USER;
	}

	public static String getST1PackForBook() {
		return MySqlQueries.ST1_PACK_FOR_BOOK;
	}
	
	public static String getST1WarehouseForBook() {
		return MySqlQueries.ST1_WAREHOUSE_FOR_BOOK;
	}

	public static String getST1PrizeListQuery() {
		return MySqlQueries.ST1_GAME_PRIZES_LEFT;
	}

	public static String getST1PwtAgentDetailQuery() {
		return MySqlQueries.ST1_PWT_AGENT_DETAIL;
	}

	public static String getST1PwtAgentMasterQuery() {
		return MySqlQueries.ST1_PWT_AGENT_MASTER;
	}

	public static String getST1PWTBOCheckQuery() {
		return MySqlQueries.ST1_PWT_BO_CHECK;
	}

	public static String getST1PwtBODetailQuery() {
		return MySqlQueries.ST1_PWT_BO_DETAIL;
	}

	public static String getST1PWTBOUpdateQuery() {
		return MySqlQueries.ST1_PWT_BO_UPDATE;
	}

	public static String getST1PWTCheckQuery() {
		return MySqlQueries.ST1_PWT_CHECK;
	}

	public static String getST1PWTRetCheckQuery() {
		return MySqlQueries.ST1_PWT_RET_CHECK;
	}

	public static String getST1PWTUpdateQuery() {
		return MySqlQueries.ST1_PWT_UPDATE;
	}

	public static String getST1RetailerAccFetchQuery() {
		return MySqlQueries.ST1_RET_ACC_DETAIL;
	}

	public static String getST1RetailerGameSearchQuery() {

		return MySqlQueries.ST1_SEARCH_RET_GAME;

	}

	public static String getST1RetOrgActiveQuery() {
		return MySqlQueries.ST1_RETAILER_ORG_ACTIVE;
	}

	public static String getST1RetOrgQuery() {
		return MySqlQueries.ST1_RETAILER_ORG;
	}

	public static String getST1UpdGameInvStatusQuery() {
		return MySqlQueries.ST1_UPDATE_GAME_INV_STATUS;
	}

	public static String getST2AgentCash() {
		return MySqlQueries.ST2_AGENT_CASH;
	}
	public static String getST2AgentOlaCommission() {
		return MySqlQueries.ST2_AGENT_OLA_COMMISSION;
	}
	
	public static String getST2AgentChq() {
		return MySqlQueries.ST2_AGENT_CHQ;
	}

	public static String getST2AgentChqBounce() {
		return MySqlQueries.ST2_AGENT_CHQ_BOUNCE;
	}

	public static String getST2AgentCurrentBal() {
		return MySqlQueries.ST2_AGENT_CURRENT_BAL;
	}

	public static String getST2AgentCurrentBalAgent() {
		return MySqlQueries.ST2_AGENT_CURRENT_BAL_AGENT;
	}

	// AGENT LEDGER
	public static String getST2AgentDate() {
		return MySqlQueries.ST2_AGENT_DATE;
	}

	public static String getST2AgentDGPwtPlr() {
		return MySqlQueries.ST2_AGENT_DG_PWT_PLR;
	}

	public static String getST2AgentDrGmPwtPay() {
		return MySqlQueries.ST2_AGENT_DRW_GM_PWT_PAY;
	}
	public static String getST2AgentOLAWithdrawal() {
		return MySqlQueries.ST2_AGENT_OLA_GM_WITHDRAWAL_PAY;
	}
	public static String getST2AgentDrGmSale() {
		return MySqlQueries.ST2_AGENT_DRW_GM_SALE;
	}

	public static String getST2AgentPwtPay() {
		return MySqlQueries.ST2_AGENT_PWT_PAY;
	}

	public static String getST2AgentPwtPlr() {
		return MySqlQueries.ST2_AGENT_PWT_PLR;
	}

	public static String getST2AgentSale() {
		return MySqlQueries.ST2_AGENT_SALE;
	}

	public static String getST2AgentSaleRet() {
		return MySqlQueries.ST2_AGENT_SALE_RET;
	}

	public static String getST2AgentTransaction() {
		return MySqlQueries.ST2_AGENT_TRANSACTION;
	}

	public static String getST2AgtDrwGmRefnd() {
		return MySqlQueries.ST2_AGENT_DRW_GM_RFND;
	}

	public static String getST2BoAgtTransactions() {

		return MySqlQueries.ST6_BO_AGT_TRANSACTIONS;

	}

	public static String getST2BoCash() {
		return MySqlQueries.ST2_BO_CASH;
	}
	
	public static String getST2BoOlaCommission() {
		return MySqlQueries.ST2_BO_OLA_COMMISSION;
	}
	public static String getST2BoBankDep() {
		return MySqlQueries.ST2_BO_BANK_DEPOSIT;
	}
	
	public static String getST2BoChq() {
		return MySqlQueries.ST2_BO_CHQ;
	}

	public static String getST2BoChqBounce() {
		return MySqlQueries.ST2_BO_CHQ_BOUNCE;
	}

	public static String getST2BoCurrentBal() {
		return MySqlQueries.ST2_BO_CURRENT_BAL;
	}

	public static String getST2BoCurrentBalAgent() {
		return MySqlQueries.ST2_BO_CURRENT_BAL_AGENT;
	}

	public static String getST2BoDGPwtPlr() {
		return MySqlQueries.ST2_BO_DG_PWT_PLR;
	}

	public static String getST2BoDrGmPwtPay() {
		return MySqlQueries.ST2_BO_DRAW_GM_PWT_PAY;
	}

	public static String getST2BoDrwGmRefnd() {
		return MySqlQueries.ST2_BO_DRW_GM_RFND;
	}
	
	public static String getST2BoCsRefnd() {
		return MySqlQueries.ST2_BO_COMM_SERV_RFND;
	}
	public static String getST2BoOlaRefnd()
	{
		return MySqlQueries.ST2_BO_OLA_RFND;
	}
	public static String getST2BoDrwGmSale() {
		return MySqlQueries.ST2_BO_DRW_GAME_SALE;
	}
	
	public static String getST2BoCsSale() {
		return MySqlQueries.ST2_BO_COMM_SERV_SALE;
	}
	public static String getST2BoOlaDeposit() {
		return MySqlQueries.ST2_BO_OLA_DEPOSIT;
	}
	public static String getST2BoGovtComm() {
		return MySqlQueries.ST2_BO_GOVT_COMM;
	}

	public static String getST2BoPwtPay() {
		return MySqlQueries.ST2_BO_PWT_PAY;
	}
	public static String getST2BoOlaWithdrawl(){
		return MySqlQueries.ST2_BO_OLA_WITHDRAWL;
	}

	public static String getST2BoPwtPlr() {
		return MySqlQueries.ST2_BO_PWT_PLR;
	}

	public static String getST2BoSale() {
		return MySqlQueries.ST2_BO_SALE;
	}

	public static String getST2BoSaleRet() {
		return MySqlQueries.ST2_BO_SALE_RET;
	}

	public static String getST2BoTds() {
		return MySqlQueries.ST2_BO_TDS;
	}

	public static String getST2BoTransaction() {
		return MySqlQueries.ST2_BO_TRANSACTION;
	}

	public static String getST2BoUnclmPwt() {
		return MySqlQueries.ST2_BO_UNCLM_PWT;
	}

	public static String getST2InsertAgentCurrentBal() {
		return MySqlQueries.ST2_INSERT_AGENT_CURRENT_BAL;
	}

	// INSERT
	public static String getST2InsertAgentLedger() {
		return MySqlQueries.ST2_INSERT_AGENT_LEDGER;
	}

	// INSERT
	public static String getST2InsertBoLedger() {
		return MySqlQueries.ST2_INSERT_BO_LEDGER;
	}

	// UPDATE
	public static String getST2UpadateAgentCurrentBal() {
		return MySqlQueries.ST2_UPDATE_AGENT_CURRENT_BAL;
	}

	// UPDATE
	public static String getST2UpadateCurrentBalAgent() {
		return MySqlQueries.ST2_UPDATE_CURRENT_BAL_AGENT;
	}

	public static String getST2UpdateCurrentBal() {
		return MySqlQueries.ST2_UPDATE_CURRENT_BAL;
	}

	public static String getST3AgentSearchQuery() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_AGENT_ORG_SEARCH;
	}

	public static String getST3ContactDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_CONTACT_DETAILS;
	}

	public static String getST3Country() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_COUNTRY;
	}

	public static String getST3CountryCode() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_COUNTRY_CODE;
	}

	public static String getST3CreateTask() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_CREATE_TASK;
	}

	public static String getST3CreateTaskAgt() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_CREATE_TASK_AGT;
	}

	public static String getST3ExtendPwtEndDate() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_EXTEND_PWT_END_DATE;
	}

	public static String getST3ForgotPass() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_FORGOT_PASS;
	}

	public static String getST3GameDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_GAME_DETAILS;
	}

	public static String getST3GamesDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_GAME_DETAILS1;
	}

	public static String getST3IWGamesDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_IWGAME_DETAILS1;
	}
	
	// Yogesh's Queries Function----------------------------------------------
	public static String getST3GameSearchQuery() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_SEARCH_GAME;
	}

	public static String getST3GovtCommApprovedDG() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GOVT_COMM_APPROVED_DG;
	}

	public static String getST3GovtCommApprovedSE() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GOVT_COMM_APPROVED_SE;
	}

	public static String getST3GovtCommRate() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GOVT_COMM_RATE;
	}

	public static String getST3LastMonthTDS() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_LAST_MONTH_TDS;
	}

	public static String getST3LastMonthTransactionDG() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_LAST_MONTH_TRANSACTION_DG;
	}

	public static String getST3LastMonthTransactionSE() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_LAST_MONTH_TRANSACTION_SE;
	}

	public static String getST3MrpForGovtComm() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_MRP_AMT;
	}

	public static String getST3OrganizationId() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_ORG_ID;
	}

	public static String getST3OrgDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_ORGANIZATION_DETAILS;
	}

	public static String getST3OrgId() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_ORGANIZATION_ID;
	}

	public static String getST3OrgName() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_ORG_NAME;
	}

	public static String getST3Password() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_PASSWORD;
	}

	public static String getST3PasswordHistory() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_PASSWORD_HISTORY;
	}

	public static String getST3PasswordType() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_PASSWORD_TYPE;
	}

	public static String getST3PrizeListQuery() {
		return MySqlQueries.ST3_GAME_PRIZES_LEFT;
	}

	public static String getST3PwtMrp() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_PWT_MRP;
	}

	public static String getST3RoleId() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_ROLE_ID;
	}

	public static String getST3RoleName() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_ROLE_NAME;
	}

	public static String getST3SaleCloseGames() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_SALE_CLOSE;
	}

	public static String getST3SaleHoldGames() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_SALE_HOLD_GAME;
	}

	public static String getST3StateCode() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_STATE_CODE;
	}

	public static String getST3TDSApprovedDG() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_TDS_APPROVED_DG;
	}

	public static String getST3TDSApprovedSE() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_TDS_APPROVED_SE;
	}

	public static String getST3UnclmPwtApproved() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UNCLM_PWT_APPROVED;
	}

	public static String getST3UpdatePassHistory() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_PASS_HISTORY;
	}

	public static String getST3UpdateUserMaster() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_USER_MASTER;
	}

	public static String getST3UserDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_USER_DETAILS;
	}

	public static String getST3UserId() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_USER_ID;
	}

	public static String getST3UserName() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_USER;
	}

	public static String getST3UserSearchQuery() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_SEARCH_USER;
	}

	public static String getST3VATApprovedAgtDG() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_VAT_APPROVED_AGT_DG;
	}

	public static String getST3VATApprovedAgtSE() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_VAT_APPROVED_AGT_SE;
	}
	
	public static String getST3VATApprovedAgtIW() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_VAT_APPROVED_AGT_IW;
	}

	public static String getST3VATApprovedDG() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_VAT_APPROVED_DG;
	}

	public static String getST3VATApprovedSE() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_VAT_APPROVED_SE;
	}
	public static String getST3VATApprovedIW() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_VAT_APPROVED_IW;
	}

	public static String getST4ActiveGamesQuery() {
		return MySqlQueries.ST4_GET_ACTIVE_GAME_DETAILS;
	}

	public static String getST4AgentOrganizationSearchQuery() {
		return MySqlQueries.ST4_SEARCH_AGENT_ORGANIZATION;
	}

	public static String getST4BookNbrOfPackNbr() {
		return MySqlQueries.ST4_GET_BOOK_NBR_OF_PACK_NBR;
	}

	public static String getST4BookOfTicketsOfPwt() {
		return MySqlQueries.ST4_GET_BOOK_OF_TICKETS_OF_PWT;
	}

	public static String getST4BookOfTicketsOfPwtTmpTable() {
		return MySqlQueries.ST4_GET_BOOK_OF_TICKETS_OF_PWT_TEMP;
	}

	public static String getST4BookStatusDetailsAndBookNbrUsingGamePackNbr() {
		return MySqlQueries.ST4_GET_BOOK_STATUS_DETAILS_AND_BOOK_NBRS_USING_PACK_NBR;
	}

	public static String getST4BookValidityQuery() {
		return MySqlQueries.ST4_GET_BOOK_VALIDITY_DETAILS;
	}

	public static String getST4CurrentOwnerDetailsAndBookNbrUsingGamePackNbr() {
		return MySqlQueries.ST4_GET_CURRENT_OWNER_DETAILS_AND_BOOK_NBRS_USING_PACK_NBR;
	}

	public static String getST4CurrentOwnerDetailsUsingGameBookNbr() {
		return MySqlQueries.ST4_GET_CURRENT_OWNER_DETAILS_USING_GAME_BOOK_NBR;
	}

	public static String getST4CurrentOwnerDetailsUsingGameId() {
		return MySqlQueries.ST4_GET_CURRENT_OWNER_DETAILS_USING_GAME_ID;
	}

	public static String getST4CurrentOwnerTypeUsingOrganizationId() {
		return MySqlQueries.ST4_GET_CURRENT_OWNER_TYPE_USING_ORGANIZATION_ID;
	}

	public static String getST4GameDetailsUsingGameId() {
		return MySqlQueries.ST4_GET_GAME_DETAILS_USING_GAME_ID;
	}

	public static String getST4GameDetailsUsingGameName() {
		return MySqlQueries.ST4_GET_GAME_DETAILS_USING_GAME_NAME;
	}

	// Vishal's Query Functions--------------------------------------------//by
	// yogi
	public static String getST4GameSearchQuery() {
		return MySqlQueries.ST4_SEARCH_GAME;
	}

	public static String getST4GameTicketDetailsUsingGameId() {
		return MySqlQueries.ST4_GET_GAME_TICKET_DETAILS_USING_GAME_ID;
	}

	public static String getST4InsertAgentReceipts() {
		return MySqlQueries.ST4_INSERT_AGENT_RECEIPTS;
	}

	public static String getST4InsertAgentReceiptsTrnMapping() {
		return MySqlQueries.ST4_INSERT_AGENT_RECEIPTS_TRN_MAPPING;
	}

	public static String getST4InsertAgentRetailerTransaction() {
		return MySqlQueries.ST4_INSERT_AGENT_RETAILER_TRANSACTION;
	}

	public static String getST4InsertAgentTransactionMaster() {
		return MySqlQueries.ST4_INSERT_AGENT_TRANSACTION_MASTER;
	}

	public static String getST4InsertBoAgentTransaction() {
		return MySqlQueries.ST4_INSERT_BO_AGENT_TRANSACTION;
	}

	public static String getST4InsertBoReceipts() {
		return MySqlQueries.ST4_INSERT_BO_RECEIPTS;
	}

	public static String getST4InsertBoReceiptsTrnMapping() {
		return MySqlQueries.ST4_INSERT_BO_RECEIPTS_TRN_MAPPING;
	}

	public static String getST4InsertBoTransactionMaster() {
		return MySqlQueries.ST4_INSERT_BO_TRANSACTION_MASTER;
	}

	public static String getST4InsertGameInvDetail() {
		return MySqlQueries.ST4_INSERT_GAME_INV_DETAIL;
	}

	public static String getST4InsertGameInvDetailForAgent() {
		return MySqlQueries.ST4_INSERT_GAME_INV_DETAIL_FOR_AGENT;
	}

	public static String getST4OrganizationIdUsingOrganizationName() {
		return MySqlQueries.ST4_GET_ORGANIZATION_ID_USING_ORGANIZATION_NAME;

	}

	public static String getST4OrganizationSearchQuery() {
		return MySqlQueries.ST4_SEARCH_ORGANIZATION;
	}

	public static String getST4PackNbrOfBookNbr() {
		return MySqlQueries.ST4_GET_PACK_NBR_OF_BOOK_NBR;
	}

	public static String getST4PackValidityQuery() {
		return MySqlQueries.ST4_GET_PACK_VALIDITY_DETAILS;
	}

	public static String getST4PWTDetailsForBookNbrUsingGamePackNbr() {
		return MySqlQueries.ST4_GET_BOOK_OF_TICKETS_OF_PWT_FOR_PACK;
	}

	public static String getST4PWTDetailsForBookNbrUsingGamePackNbrTempTable() {
		return MySqlQueries.ST4_GET_BOOK_OF_TICKETS_OF_PWT_FOR_PACK_TEMP;
	}

	// queries for ticket and pwt verification
	public static String getST4PwtTicketDetailsUsingGameNbr() {
		return MySqlQueries.PWT_TICKETS_DETAILS_GAME_NBR;
	}
	
	public static String getST4PwtTicketDetailsFromPwtInvUsingGameNbr() {
		return MySqlQueries.PWT_INV_TICKETS_DETAILS_GAME_NBR;
	}

	public static String getST4PwtTicketDetailsUsingTicketNbr() {
		return MySqlQueries.ST4_GET_PWT_TICKETS_DETAILS;
	}

	public static String getST4RetailerOrganizationSearchQuery() {
		return MySqlQueries.ST4_SEARCH_RETAILER_ORGANIZATION;
	}

	public static String getST4UdateGameInvStatusForBook() {
		return MySqlQueries.ST4_UPDATE_GAME_INV_STATUS_FOR_BOOK;
	}

	public static String getST4UdateGameInvStatusForBookForAgent() {
		return MySqlQueries.ST4_UPDATE_GAME_INV_STATUS_FOR_BOOK_FOR_AGENT;
	}

	public static String getST4UdateGameInvStatusForPack() {
		return MySqlQueries.ST4_UPDATE_GAME_INV_STATUS_FOR_PACK;
	}

	public static String getST4UdateGameInvStatusForPackForAgent() {
		return MySqlQueries.ST4_UPDATE_GAME_INV_STATUS_FOR_PACK_FOR_AGENT;
	}

	public static String getST4UpdatePwtTicketStatusToAGT() {
		return MySqlQueries.ST4_UPDATE_PWT_TICKET_INV_STATUS_TO_AGT;
	}

	public static String getST4UpdatePwtTicketStatusToPLR() {
		return MySqlQueries.ST4_UPDATE_PWT_TICKET_INV_STATUS_TO_PLR;
	}

	public static String getST4UpdatePwtTicketStatusToRETURN() {
		return MySqlQueries.ST4_UPDATE_PWT_TICKET_INV_STATUS_TO_RETURN;
	}

	public static String getST4UserIdUsingUserName() {
		return MySqlQueries.ST4_GET_USER_ID_USING_USER_NAME;
	}

	public static String getST4UserSearchQuery() {
		return MySqlQueries.ST4_SEARCH_USER;
	}

	public static String getST5_RET_ORG_SEARCH_FOR_AGENT() {
		return MySqlQueries.ST5_RET_ORG_SEARCH_FOR_AGENT;
	}

	public static String getST5AgentChequeQuery() {
		return MySqlQueries.ST5_AGENT_CHQ;
	}

	public static String getST5AgentDetailsQuery() {
		return MySqlQueries.ST5_AGENT_DETAILS;
	}

	/**
	 * This method is to get transaction query for the retailer at Agent
	 * end.This query insert data into st_lms_agent_receipts table
	 * 
	 * @return
	 */
	public static String getST5AGENTReceiptIdQuery() {
		return MySqlQueries.ST5_AGENT_RECEIPTS;
	}

	/**
	 * This method is to get transaction query for the retailer at Agent
	 * end.This query insert data into st_lms_agent_receipts_trn_mapping table
	 * 
	 * @return
	 */
	public static String getST5AGENTReceiptMappingQuery() {
		return MySqlQueries.ST5_AGENT_RECEIPTS_TRN_MAPPING;
	}

	public static String getST5AGENTSaleChequeQuery() {
		return MySqlQueries.ST5_AGENT_SALE_CHEQUE;
	}

	public static String getST5AGENTSaleChequeQuery1() {
		return MySqlQueries.ST5_AGENT_SALE_CHEQUE1;
	}

	/**
	 * This method is to get transaction query for the retailer at Agent
	 * end.This query insert data into st_lms_agent_cash_transaction table
	 * 
	 * @return
	 */
	public static String getST5AGENTTransactionQuery() {
		return MySqlQueries.ST5_AGENT_CASH_TRANSACTION;
	}

	public static String getST5AgtRequestListQuery() {
		return MySqlQueries.ST5_AGENT_REQUEST_LIST;
	}

	public static String getST5BOOrderedGamesQuery() {
		return MySqlQueries.ST5_BO_ORDERED_GAME;
	}

	public static String getST5BOReceiptIdQuery() {
		return MySqlQueries.ST5_BO_RECEIPTS;
	}

	public static String getST5BOReceiptMappingQuery() {
		return MySqlQueries.ST5_BO_RECEIPTS_TRN_MAPPING;
	}

	public static String getST5BORequestListQuery() {
		return MySqlQueries.ST5_BO_REQUEST_LIST;
	}

	public static String getST5BOSaleChequeQuery() {
		return MySqlQueries.ST5_BO_SALE_CHEQUE;
	}

	public static String getST5BOTransactionQuery() {
		return MySqlQueries.ST5_BO_CASH_TRANSACTION;
	}

	public static String getST5BOBDTransactionQuery() {
		return MySqlQueries.ST5_BO_BANK_DEPOSIT_TRANSACTION;
	}
    public static String getST5AgentBDTransactionQuery(){
    	return MySqlQueries.ST5_AGENT_BANK_DEPOSIT_TRANSACTION;
    }

	public static String getST5CashRetailerTransactionQuery() {
		return MySqlQueries.ST5_INSERT_AGENT_MASTER_FOR_CASH;
	}

	public static String getST5CashTransactionQuery() {
		return MySqlQueries.ST5_INSERT_ST_BO_MASTER_FOR_CASH;
	}

	public static String getST5ChequeSearchQuery() {
		
		
		
		return MySqlQueries.ST5_SEARCH_CHEQUE;
	}

	/**
	 * This is to find Cheque at Agent END
	 * 
	 * @return
	 */
	public static String getST5ChequeSearchRetailerQuery() {
		return MySqlQueries.ST5_SEARCH_CHEQUE_RETAILER;
	}

	public static String getST5Country() {

		// depending on the database return appropriate query

		return MySqlQueries.ST55_SELECT_COUNTRY;
	}

	// //for player entry-------
	public static String getST5CountryCodeQuery() {

		// depending on the database return appropriate query

		return MySqlQueries.ST5_COUNTRY_CODE;
	}

	public static String getST5DirectPlayerTransactionQuery() {

		// depending on the database return appropriate query

		return MySqlQueries.ST5_DIRECT_PLAYER_PWT_ENTRY;
	}

	public static String getST5DirectPlrTempTransactionQuery() {
		return MySqlQueries.ST5_PWT_PLAYER_TEP_TRANC;
	}

	public static String getST5GameInvStatusQuery() {
		return MySqlQueries.ST5_GAME_INV_STATUS;
	}

	public static String getST5GameMasterQuery() {
		return MySqlQueries.ST5_GAME_MASTER_ENTRY;
	}
	
	public static String getST5IWGameMasterQuery() {
		return MySqlQueries.ST5_IWGAME_MASTER_ENTRY;
	}

	// Hanuman's Queries
	// Functions-----------------------------------------------------
	public static String getST5GameSearchQuery() {
		return MySqlQueries.ST5_SEARCH_GAME;
	}

	public static String getST5NOOfApprBooksQuery() {
		return MySqlQueries.ST5_NO_OF_BOOKS_APPR;
	}

	// /this query is to find the details of the order Request
	public static String getST5OrderRequest1Query() {
		return MySqlQueries.ST5_ORDER_REQUEST1;
	}

	public static String getST5OrderRequest2Query() {
		return MySqlQueries.ST5_ORDER_REQUEST2;
	}

	// /queries for RequestApproveAction class
	public static String getST5OrderRequest3Query() {
		return MySqlQueries.ST5_ORDER_REQUEST3;
	}

	public static String getST5OrderRequest4Query() {
		return MySqlQueries.ST5_ORDER_REQUEST4;
	}

	public static String getST5OrderRequest5Query() {
		return MySqlQueries.ST5_ORDER_REQUEST5;
	}

	public static String getST5OrgSearchQuery() {
		return MySqlQueries.ST5_ORG_SEARCH;
	}

	public static String getST5PartyTypeForAgentQuery() {
		return MySqlQueries.ST5_SELECT_ORG_TYPE_FOR_AGENT;
	}

	public static String getST5PartyTypeQuery() {
		return MySqlQueries.ST5_SELECT_ORG_TYPE_FOR_BO;
	}

	public static String getST5PasswordType() {
		return MySqlQueries.ST5_GET_PASSWORD_TYPE;
	}

	public static String getST5PlayerDetailQuery() {

		// depending on the database return appropriate query

		return MySqlQueries.ST5_PLAYER_DETAILS;
	}

	public static String getST5PlayerEntryQuery() {

		// depending on the database return appropriate query

		return MySqlQueries.ST5_PLAYER_ENTRY;
	}

	public static String getST5PlrPWTCommRateQuery() {

		// depending on the database return appropriate query

		return MySqlQueries.ST5_PLR_PWT_COMM_RATE;
	}

	public static String getST5PwtBODetailQuery() {
		return MySqlQueries.ST4_PWT_PLAYER_DETAIL;
	}

	public static String getST5RetailerChequeQuery() {
		return MySqlQueries.ST5_RETAILER_CHQ;
	}

	public static String getST5RetailerDetailsQuery() {
		return MySqlQueries.ST5_RETAILER_DETAILS;
	}

	public static String getST5RetailerOrderRequest1Query() {
		return MySqlQueries.ST5_RETAILER_ORDER_REQUEST1;
	}

	public static String getST5RetailerOrderRequest2Query() {
		return MySqlQueries.ST5_RETAILER_ORDER_REQUEST2;
	}

	public static String getST5RetailerOrderRequest3Query() {
		return MySqlQueries.ST5_RETAILER_ORDER_REQUEST3;
	}

	public static String getST5RetailerOrderRequest4Query() {
		return MySqlQueries.ST5_RETAILER_ORDER_REQUEST4;
	}

	public static String getST5RetOrderRequest5Query() {
		return MySqlQueries.ST5_RET_ORDER_REQUEST5;
	}

	public static String getST5RetOrgSearchQuery() {
		return MySqlQueries.ST5_RET_ORG_SEARCH;
	}

	public static String getST5RoleName() {
		return MySqlQueries.ST5_GET_ROLE_NAME;
	}

	public static String getST5RoleQuery() {
		return MySqlQueries.ST5_SEARCH_ROLE;
	}

	public static String getST5SaleChq() {
		return MySqlQueries.ST5_INSERT_st_lms_bo_sale_chq;
	}

	public static String getST5SelectDirectPlrTempTransactionQuery() {
		return MySqlQueries.ST5_PWT_PLAYER_TEP_TRANC_DETAIL;
	}

	/**
	 * This method is to get transaction query for the retailer at Agent end
	 * 
	 */

	public static String getST5State() {

		// depending on the database return appropriate query

		return MySqlQueries.ST55_SELECT_STATE;
	}

	public static String getST5StateCodeQuery() {

		// depending on the database return appropriate query

		return MySqlQueries.ST5_STATE_CODE;
	}

	public static String getST5SupplierQuery() {
		return MySqlQueries.ST5_SEARCH_SUPPLIER;
	}

	public static String getST5SupplierTransQuery() {
		return MySqlQueries.ST5_SUPPLIER_BO_TRANS;
	}

	public static String getST5ToatlDispatchQuery() {
		return MySqlQueries.ST5_TOTAL_DISPATCH;
	}

	public static String getST5TransactionIdQuery() {
		return MySqlQueries.ST5_SELECT_TRANSACTIONId;
	}

	public static String getST5UerNameIdGroupQuery() {
		return MySqlQueries.ST5_SEARCH_USER_FOR_CASH;
	}

	public static String getST5UpdateBOorderInvoicesQuery() {
		return MySqlQueries.ST5_BO_UPDATE_BO_ORDER_INVOICES;
	}

	// ///code is changed by hanuman
	public static String getST5UpdateBOorderQuery() {

		return MySqlQueries.ST5_BO_UPDATE_BO_ORDER;
	}

	public static String getST5UpdateSTBOtempTranction() {

		return MySqlQueries.ST5_UPDATE_ST_BO_TEMP;

	}

	public static String getST5UserDetailQuery() {
		return MySqlQueries.ST5_SEARCH_USER_DETAIL;
	}

	public static String getST5UserDetails() {
		return MySqlQueries.ST5_GET_USER_DETAILS;
	}

	public static String getST5UserSearchQuery() {
		return MySqlQueries.ST5_SEARCH_USER;
	}

	public static String getST6AccountWiseLedgerAgt() {
		
		return MySqlQueries.ST6_ACC_LEDGER_AGT;

	}

	public static String getST6AccountWiseLedgerBO() {

		return MySqlQueries.ST6_ACC_LEDGER_BO;

	}

	public static String getST6AddressQuery() {

		return MySqlQueries.ST6_GET_ADD;

	}

	public static String getST6AddressQueryAgtRetWise() {

		return MySqlQueries.ST6_GET_ADD_AGT_RETWISE;

	}

	public static String getST6AddressQueryBOAgtWise() {

		return MySqlQueries.ST6_GET_ADD_BO_AGTWISE;

	}

	public static String getST6AgentDebitNote() {
		return MySqlQueries.ST6_AGENT_DEBIT_NOTE;
	}

	// Added for agent credit note @ amit
	public static String getST6AgentCreditNote() {
		return MySqlQueries.ST6_AGENT_CREDIT_NOTE;
	}

	public static String getST6AgentVATPayable() {
		return MySqlQueries.ST6_AGENT_VAT_PAY;
	}

	public static String getST6AgentWiseLedgerBO(boolean isReceiptWise) {
		if (isReceiptWise) {
			return MySqlQueries.ST6_AGTWISE_LEDGER_BO_RCPT;
		}
		return MySqlQueries.ST6_AGTWISE_LEDGER_BO;

	}

	public static String getST6AgtCurrBal() {

		return MySqlQueries.ST6_AGT_CURR_BAL;

	}

	public static String getST6AgtTransaction() {

		return MySqlQueries.ST6_AGT_TRANSACTION;

	}

	public static String getST6BoCreditNote() {
		return MySqlQueries.ST6_BO_CREDIT_NOTE;
	}

	public static String getST6BoDebitNote() {
		return MySqlQueries.ST6_BO_DEBIT_NOTE;
	}

	public static String getST6BoVATPayable() {
		return MySqlQueries.ST6_BO_VAT_PAY;
	}

	public static String getST6InsertAgentCurrBal() {

		return MySqlQueries.ST6_INSERT_AGT_CURR_BAL;

	}

	public static String getST6JournalLedgerAgt() {

		return MySqlQueries.ST6_JOURNAL_LEDGER_AGT;

	}

	public static String getST6JournalLedgerBO() {

		return MySqlQueries.ST6_JOURNAL_LEDGER_BO;

	}

	public static String getST6RetWiseLedgerAgt(boolean isReceiptWise) {

		if (isReceiptWise) {
			return MySqlQueries.ST6_RETWISE_LEDGER_AGT_RCPT;
		}
		return MySqlQueries.ST6_RETWISE_LEDGER_AGT;

	}

	public static String getST6TransactionDate() {

		return MySqlQueries.ST6_TRANSACTION_DATE;

	}

	public static String getUnapprovedTDSDG() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_UNAPPROVED_TDS_DG;
	}

	public static String getUnapprovedTDSSE() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_UNAPPROVED_TDS_SE;
	}

	public static String getUnapprovedVATAgtDG() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_UNAPPROVED_VAT_AGT_DG;
	}

	public static String getUnapprovedVATAgtSE() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_UNAPPROVED_VAT_AGT_SE;
	}

	public static String getUnapprovedVATDG() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_UNAPPROVED_VAT_DG;
	}

	public static String getUnapprovedVATSE() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_UNAPPROVED_VAT_SE;
	}
	
	public static String getUnapprovedVATIW() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_UNAPPROVED_VAT_IW;
	}

	public static String getUserAndOrgDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_GET_USER_ORG_DETAILS;
	}

	public static String holdST3SaleGames() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_HOLD_SALE;
	}

	public static String insertAgentReceiptTrnMapping() {
		return MySqlQueries.INSERT_AGENT_RECEIPTS_TRN_MAPPING;
	}

	public static String insertAgentReconBookwise() {
		return MySqlQueries.INSERT_AGENT_RECON_BOOKWISE;
	}

	public static String insertBOReceiptTrnMapping() {
		return MySqlQueries.INSERT_BO_RECEIPTS_TRN_MAPPING;
	}

	public static String insertGameDates() {

		return MySqlQueries.ST3_INSERT_NEW_GAME_DATES;
	}
	
	public static String insertIWGameDates() {

		return MySqlQueries.ST3_INSERT_NEW_IWGAME_DATES;
	}
	
	/*
	 * queries added after the first review
	 */

	public static String insertInAgentReceipts() {
		return MySqlQueries.INSERT_AGENT_RECEIPT;
	}

	public static String insertInAgentTransactionMaster() {
		return MySqlQueries.INSERT_AGENT_TRANSACTION_MASTER;
	}

	public static String insertInBOReceipts() {
		return MySqlQueries.INSERT_BO_RECEIPT;
	}

	public static String insertAgtDailyTrngExp() {
		return MySqlQueries.INSERT_AGENT_DAILY_TRNG_EXP;
	}

	public static String insertAgtWeeklyTrngExp() {
		return MySqlQueries.INSERT_AGENT_WEEKLY_TRNG_EXP;
	}

	public static String insertInBOTransactionMaster() {
		return MySqlQueries.INSERT_BO_TRANSACTION_MASTER;
	}

	public static String insertInLMSTransactionMasterQuery() {
		return MySqlQueries.GENERATE_LMS_TRANSACTION;
	}
	
	public static String insertInLMSTransactionMaster() {

		return new CommonQueriesUtility().insTransaction();
	}

	public static String insertInPwtTicketDetailsUsingGameNbr() {
		return MySqlQueries.INSERT_PWT_TICKETS_DETAILS_GAME_NBR;
	}

	public static String insertInReceiptMaster() {
		return MySqlQueries.INSERT_RECEIPT_MASTER;
	}

	public static String insertInRetailerTransactionMaster() {
		return MySqlQueries.INSERT_RETAILER_TRANSACTION_MASTER;
	}

	public static String insertInRETReceipts() {
		return MySqlQueries.INSERT_RET_RECEIPT;
	}

	public static String insertintoRankMaster() {

		return MySqlQueries.ST3_INSERT_RANK_DETAILS;
	}
	
	public static String insertintoIWRankMaster() {

		return MySqlQueries.ST3_INSERT_IWRANK_DETAILS;
	}

	public static String insertLoginDate() {
		return MySqlQueries.ST5_INSERT_LOGIN_DATE;
	}

	public static String insertRETReceiptTrnMapping() {
		return MySqlQueries.INSERT_RET_RECEIPTS_TRN_MAPPING;
	}

	public static String insertST3AgentDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_AGENT_DETAIL;
	}

	public static String insertST3ContactsDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_CONTACTS_DETAILS;
	}

	public static String insertST3GameDates() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_GAME_DATES;
	}

	public static String insertST3GovtTransaction() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_GOVT_TRANSACTION;
	}

	public static String insertST3GovtTransactionForTDS() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_GOVT_TDS_TRANSACTION;
	}

	public static String insertST3GovtTransactionForTDSAgt() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_GOVT_TDS_TRANSACTION_AGT;
	}

	public static String insertST3LoginDate() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_LOGIN_DATE;
	}

	public static String insertST3OrganizationAgent() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_RETAILER_ORG_;
	}

	public static String insertST3OrganizationHistory() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_ORG_HISTORY;
	}

	public static String insertST3OrganizationMaster() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_ORG_MASTER;
	}

	public static String insertST3PasswordDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_PASSWORD_DETAILS;
	}

	public static String insertUserTimeLimitMapping() {

		// depending on the database return appropriate query

		return MySqlQueries.INSERT_USER_TIME_LIMIT_MAPPING;
	}

	public static String insertST3PasswordHistory() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_PASSWORD_HISTORY;
	}

	// retailers query manager

	public static String insertST3SupplierDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_SUPPLIER_DETAILS;
	}

	public static String insertST3TransactionMaster() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_TRANSACTION_MASTER;
	}

	public static String insertST3TransactionMasterAgt() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_INSERT_TRANSACTION_MASTER_AGT;
	}

	public static String insertTicketFormat() {

		return MySqlQueries.ST3_INSERT_TICKET_FORMAT;
	}
	
	
	public static String insertIWTicketFormat() {

		return MySqlQueries.ST3_INSERT_IWTICKET_FORMAT;
	}

	public static String insertVatDetailsintoGameMaster() {
		return MySqlQueries.ST1_GAME_MASTER_VAT_INSERT;
	}

	public static String selectST3ClosedGames() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_SELECT_CLOSED_GAMES;
	}

	public static String selectST3Country() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_SELECT_COUNTRY;
	}

	// insert entry into st transaction master and bo and agent transaction
	// master tables

	public static String selectST3ExpireGames() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_SELECT_EXPIRE_GAMES;
	}

	public static String selectST3State() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_SELECT_STATE;
	}

	public static String selectST3SupplierDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_SELECT_SUPPLIER_DETAILS;
	}

	public static String submitST3TdsApproved() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_SUBMIT_TDS_APPROVED;
	}

	public static String submitST3UnclmPwtApproved() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_SUBMIT_UNCLM_PWT_APPROVED;
	}

	public static String submitST3UnclmPwtApprovedAgt() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_SUBMIT_UNCLM_PWT_APPROVED_AGT;
	}

	// queries for receipt related tables

	public static String updateIntoPwtTicketsInv() {
		return MySqlQueries.ST4_INSERT_TICKETS_NO;
	}

	public static String updatePwtInvTable() {
		return MySqlQueries.ST4_INSERT_PWT_INV_DETAILS;
	}

	public static String updateST3CloseSaleStatus() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_SALE_CLOSE_STATUS;
	}

	public static String updateST3GameStatus() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_GAME_STATUS;
	}

	public static String updateST3OrgDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDET_ORG_DETAILS;
	}

	public static String updateST3PwtInv() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_PWT_INV;
	}

	public static String updateST3QueryManager() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_QUERY_MANAGER;
	}

	public static String updateST3SaleDateStatus() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_SALE_STATUS;
	}

	public static String updateST3SalePwtStatus() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_SALE_PWT_STATUS;
	}

	public static String updateST3Task() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_TASK_STATUS;
	}

	public static String updateST3TaskAgt() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_TASK_STATUS_AGT;
	}

	public static String updateST3UserDetails() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDET_USER_DETAILS;
	}

	// / for recon table

	public static String updateST3UserMaster() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDATE_USER_MASTER1;
	}

	public static String updateST3UserPass() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDET_USER_PASS;
	}

	public static String updateST3UserStatus() {

		// depending on the database return appropriate query

		return MySqlQueries.ST3_UPDET_USER_STATUS;
	}

	public static String updateST5AGENTReceiptGenMappimg() {
		return MySqlQueries.ST5_AGENT_RECEIPTS_GEN_MAPPING;
	}

	public static String updateST5BOReceiptGenMapping() {
		return MySqlQueries.ST5_BO_RECEIPTS_GEN_MAPPING;
	}

	public static String getST_COLLECTION_DETAILS_FOR_AGENT_OLA1() {
		return MySqlQueries.GET_AGENT_OLA_DATA_RETAILER_WISE;
	}
	
	public static String getST_COLLECTION_DETAILS_FOR_AGENT_CS(){
		return MySqlQueries.GET_AGENT_CS_DATA_RETAILER_WISE;
		
	}
	public static String getAppendOrgOrder(){
			
		return appendOrder ;
	}
	public static void setAppendOrgOrder(){
		
		if( (LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("ORG_ID")){
			appendOrder="organization_id";
			
		}else if( (LMSFilterDispatcher.orgOrderBy).equalsIgnoreCase("DESC")){
			
			appendOrder="orgCode DESC ";
		}
		
	}
	public static String getOrgQryUsingRoleId(String org_type,int roleId){
		String agtOrgQry = "select "+getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_type='"+org_type+"' ";
		agtOrgQry = CommonMethods.appendRoleAgentMappingQuery(agtOrgQry,"organization_id",roleId);
		agtOrgQry=agtOrgQry +" order by "+getAppendOrgOrder(); 
		return agtOrgQry;
	}
	
	public static String getOrgQry(String org_type){
		String agtOrgQry = "select "+getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_type='"+org_type+"' order by "+getAppendOrgOrder();
	
		return agtOrgQry;
	}
	public static String getActiveOrgQry(String org_type){
		String agtOrgQry = "select "+getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_type='"+org_type+"' AND organization_status IN ('ACTIVE','INACTIVE') order by "+getAppendOrgOrder();
	
		return agtOrgQry;
	}
	public static String getActiveInactiveBlockOrgQry(String org_type){
		String agtOrgQry = "select "+getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_type='"+org_type+"' AND organization_status IN ('ACTIVE','INACTIVE','BLOCK') order by "+getAppendOrgOrder();
	
		return agtOrgQry;
	}
	public static String getOrgInfoQry(int userOrgId, String orgType){

		String agtOrgQry = "select "+getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_type='"+orgType+"' and parent_id="+userOrgId+" and organization_status !='TERMINATE' order by "+getAppendOrgOrder();
	
		return agtOrgQry;
	}
	
	public static String getAllOrgInfoQry(int userOrgId, String orgType){

		String agtOrgQry = "select "+getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_status !='TERMINATE' and organization_type='"+orgType+"' and parent_id="+userOrgId+"  order by "+getAppendOrgOrder();
	
		return agtOrgQry;
	}
	
	public static String getST3OrgDetailsWithColletedSdDetail() {

		// depending on the database return appropriate query
		return MySqlQueries.ST3_GET_ORGANIZATION_DETAILS_WITH_SEC_DPST_DETAILS;
	}
	
	public static String getST1BOUpdGameInvStatusInvoiceAgentQuery() {
		return MySqlQueries.ST1_BO_UPDATE_GAME_INV_INVOICE_STATUS;
	}
	
	public static String getST1BOUpdGameInvStatusInvoiceRetailerQuery() {
		return MySqlQueries.ST1_AGENT_UPDATE_GAME_INV_INVOICE_STATUS;
	}
	
	/**
	 * Gives name/code/code_name/name_code according to property 
	 * @Note:- Only use where alias is not used for organization master 
	 * @return
	 */
	public static String  getOrgCodeQuery(){
		
		return orgCodeQry;
		
	}
	public static void setOrgCodeQuery(){
		
		if ((LMSFilterDispatcher.orgFieldType).equalsIgnoreCase("CODE")) {
			orgCodeQry = " org_code orgCode ";
		

		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("CODE_NAME")) {
			orgCodeQry = " concat(org_code,'_',name)  orgCode  ";
		
			
			
		} else if ((LMSFilterDispatcher.orgFieldType)
				.equalsIgnoreCase("NAME_CODE")) {
			orgCodeQry = " concat(name,'_',org_code)  orgCode  ";
		
			
		}
	}
	
	public static String getOrgInfoQryStateAndCityWise(int userOrgId, String orgType, String stateCode, String city) {
		// String agtOrgQry =
		// "select "+getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_type='"+orgType+"' and parent_id="+userOrgId+" and organization_status !='TERMINATE' AND state_code = '"+stateCode+"' AND city = '"+city+"' order by "+getAppendOrgOrder();
		String agtOrgQry = "select "
				+ getOrgCodeQuery()
				+ ",organization_id from st_lms_organization_master where organization_type='"
				+ orgType + "' and parent_id=" + userOrgId
				+ " and organization_status !='TERMINATE'";
		if (!"ALL".equals(stateCode)) {
			agtOrgQry += "AND state_code = '" + stateCode + "'";
		}

		if (!"ALL".equals(city)) {
			agtOrgQry += " AND city = '" + city + "'";
		}
		agtOrgQry += " order by " + getAppendOrgOrder();
		return agtOrgQry;
	}
	public static String getST1AgtOrgQueryWithoutSort() {
		return MySqlQueries.ST1_AGENT_ORG_WITHOUT_SORT;
	}
	public static String getOrgInfoQryWithoutSort(int userOrgId, String orgType){

		String agtOrgQry = "select "+getOrgCodeQuery()+",organization_id from st_lms_organization_master where organization_type='"+orgType+"' and parent_id="+userOrgId+" and organization_status !='TERMINATE'";
	
		return agtOrgQry;
	}
	

	
	public static String getWarehouseIdFromRoleId( int roleId){
		String warehouseIdQuery = "select warehouse_id from st_se_warehouse_master where warehouse_owner_id = (select user_id from st_lms_user_master where isrolehead='Y' and role_id='"+roleId+"')";
		return warehouseIdQuery;
	}
}