package com.skilrock.lms.rest.scratchService.inventoryMgmt.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.BookWiseInvDetailForRetHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.SoldTicketEntryForRetHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.embedded.scratchService.pwtMgmt.common.CommonFunctions;
import com.skilrock.lms.rest.scratchService.dao.ScratchDao;
import com.skilrock.lms.rest.scratchService.daoImpl.ScratchDaoImpl;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.dao.InventoryDao;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.daoImpl.InventoryDaoImpl;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.service.BookActivationService;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;

public class BookActivationServiceImpl implements BookActivationService{
	
	private static Logger logger = LoggerFactory.getLogger(BookActivationServiceImpl.class);
	
	private BookWiseInvDetailForRetHelper helper;
	private ScratchDao scracthDao;
	private SoldTicketEntryForRetHelper saleTicketHelper;
	private InventoryDao inventoryDao;
	private CommonFunctionsHelper commonFunctionHelper;
	
	public BookActivationServiceImpl(){
		this.helper = new BookWiseInvDetailForRetHelper();
		this.scracthDao = new ScratchDaoImpl();
		this.saleTicketHelper = new SoldTicketEntryForRetHelper();
		this.inventoryDao = new InventoryDaoImpl();
		this.commonFunctionHelper = new CommonFunctionsHelper();
	}

	
	public BookActivationServiceImpl(BookWiseInvDetailForRetHelper helper,ScratchDao scracthDao){
		this.helper = helper;
		this.scracthDao = scracthDao;
	}
	
	public BookActivationServiceImpl(BookWiseInvDetailForRetHelper helper,ScratchDao scracthDao,CommonFunctionsHelper commonFunctionHelper){
		this.helper = helper;
		this.scracthDao = scracthDao;
		this.commonFunctionHelper = commonFunctionHelper;
	}
	
	public BookActivationServiceImpl(SoldTicketEntryForRetHelper saleTicketHelper,ScratchDao scracthDao){
		this.saleTicketHelper = saleTicketHelper;
		this.scracthDao = scracthDao;
	}
	
	public BookActivationServiceImpl(InventoryDao inventoryDao,ScratchDao scracthDao){
		this.inventoryDao = inventoryDao;
		this.scracthDao = scracthDao;
	}
	
	public boolean isBookNumberValid(ScracthMgmtBean bean){
		try{
			String validBookNumber = isBookNumberFormatValid(bean);
			if(validBookNumber.equals("false"))
				return false;
			bean.setBookNumber(validBookNumber);
			DaoBean daoBean = scracthDao.getUserOrgIdAndUserIdFromTpUserId(bean.getTpUserId());
			if(daoBean != null){
				Map<String, List<String>> gameBookMap = helper.activateBooks(daoBean.getUserOrgId()); 
				if(gameBookMap== null){
					return false;
				}
				List<String> bookNumberListForFinalComaparison = null;
				bookNumberListForFinalComaparison = getFinalComparisonBookNumberList(gameBookMap);
				boolean isValidBookNbr = isRecivedBookNbrListContainsValidBookNbr(bean, bookNumberListForFinalComaparison);
				return isValidBookNbr;
			}
		}catch (Exception e) {
			logger.error("isBookNumberValid breaks====" + e);
		}
		return false;
	}


	private String isBookNumberFormatValid(ScracthMgmtBean bean) throws SQLException {
		if(bean.getBookNumber().indexOf("-") == -1){
			int gameNumber = Integer.parseInt(bean.getBookNumber().substring(0,Integer.parseInt(Utility.getPropertyValue("SCRATCH_GAME_NBR_DIGITS"))));
			TicketBean bookBean = commonFunctionHelper.isBookNbrFormatValid(bean.getBookNumber(),gameNumber);
			if(bookBean.getIsValid() == false){
				return "false";
			}
			return bookBean.getBook_nbr();
		}
		return bean.getBookNumber();
	}
	
	@Override
	public String ticketByTicketSale(ScracthMgmtBean bean) {
		try{
			DaoBean daoBean = scracthDao.getUserOrgIdAndUserIdFromTpUserId(bean.getTpUserId());
			if(daoBean != null){
				UserInfoBean userInfoBean = new UserInfoBean();
				userInfoBean.setUserOrgId((Integer)daoBean.getUserOrgId());
				userInfoBean.setUserId((Integer)daoBean.getUserId());
				String refTxnId = getSaleTicketHelper().updateSellTicketStatus(bean.getTicketNumber(), userInfoBean,bean.getTpTransactionId());
				return refTxnId;
			}
		}catch (Exception e) {
			logger.error("ticketByTicketSale breaks====" + e);
		}
		return "FAIL";
	}

	@Override
	public String ticketByTicketUnSold(ScracthMgmtBean bean){
		try{
			DaoBean daoBean = scracthDao.getUserOrgIdAndUserIdFromTpUserId(bean.getTpUserId());
			if(daoBean != null){
				daoBean.setTicketNbr(bean.getTicketNumber());
				daoBean.setTpTransactionId(bean.getTpTransactionId());
				String refTxnId = inventoryDao.updateSellTicketStatusAsUnSold(daoBean);
				return refTxnId;
			}
		}catch(Exception e){
			logger.error("ticketByTicketUnSold breaks====" + e);
		}
		return "FAIL";
	}
	private List<String> getFinalComparisonBookNumberList(Map<String, List<String>> gameBookMap) {
		List<String> bookNumberListForFinalComaparison = null;
		if(gameBookMap != null && !gameBookMap.isEmpty()){
			bookNumberListForFinalComaparison = new ArrayList<String>();
			addBookNumbersToFinalComaprisonList(gameBookMap, bookNumberListForFinalComaparison);
		}
		return bookNumberListForFinalComaparison;
	}
	
	private void addBookNumbersToFinalComaprisonList(Map<String, List<String>> gameBookMap,
			List<String> bookNumberListForFinalComaparison) {
		List<String> retrivedBookNumberList;
		for(Map.Entry<String,List<String>> map : gameBookMap.entrySet()){
			retrivedBookNumberList = map.getValue();
			for(int i=0;i < retrivedBookNumberList.size();i++){
				bookNumberListForFinalComaparison.add(retrivedBookNumberList.get(i));
			}
		}
	}
	
	private boolean isRecivedBookNbrListContainsValidBookNbr(ScracthMgmtBean bean,
			List<String> bookNumberListForFinalComaparison) {
		if(bookNumberListForFinalComaparison != null){
			List<String> recievedBookList = new ArrayList<String>();
			recievedBookList.add(bean.getBookNumber());
			return isFinalBookNbrListContainsRecievedBookNbrList(bookNumberListForFinalComaparison, recievedBookList);
		}
		return false ; 
	}
	
	private boolean isFinalBookNbrListContainsRecievedBookNbrList(List<String> bookNumberListForFinalComaparison,
			List<String> recievedBookList) {
		for(String bookNbr : recievedBookList){
			if(!bookNumberListForFinalComaparison.contains(bookNbr)){
				return false;
			}
		}
		return true;
	}

	@Override
	public String updateBookNumberStatus(ScracthMgmtBean bean) {
		try{
			List<String> bookList = new ArrayList<String>();
			bookList.add(bean.getBookNumber());
			DaoBean daoBean = scracthDao.getUserOrgIdAndUserIdFromTpUserId(bean.getTpUserId());
			if(daoBean != null){
				String[] response = helper.updateBooks((Integer)daoBean.getUserOrgId(),0,bookList);
				if(response != null && "SUCCESS".equals(response[2])){
					return "SUCCESS";
				}
			}
		}catch (Exception e) {
			logger.error("updateBookNumberStatus breaks   "+e);
		}
		return "FAIL";
	}

	public SoldTicketEntryForRetHelper getSaleTicketHelper() {
		return saleTicketHelper;
	}

	
}
