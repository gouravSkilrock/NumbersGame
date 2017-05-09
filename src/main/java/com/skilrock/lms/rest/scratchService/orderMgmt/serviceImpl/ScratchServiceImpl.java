package com.skilrock.lms.rest.scratchService.orderMgmt.serviceImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.db.DBConnect;
import com.skilrock.lms.common.exception.LMSErrors;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.common.ScratchException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.BookRecieveRegistrationRetailerHelper;
import com.skilrock.lms.rest.scratchService.dao.ScratchDao;
import com.skilrock.lms.rest.scratchService.daoImpl.ScratchDaoImpl;
import com.skilrock.lms.rest.scratchService.orderMgmt.dao.OrderMgmtDao;
import com.skilrock.lms.rest.scratchService.orderMgmt.daoImpl.OrderMgmtDaoImpl;
import com.skilrock.lms.rest.scratchService.orderMgmt.service.ScratchService;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;
import com.skilrock.lms.scratchService.orderMgmt.controllerImpl.GameListResponseData;

public class ScratchServiceImpl implements ScratchService{
	
	static Log logger = LogFactory.getLog(ScratchServiceImpl.class);
	
	private BookRecieveRegistrationRetailerHelper helper;
	private ScratchDao scracthDao;
	private OrderMgmtDao dao;
	
	public ScratchServiceImpl(){
		this.helper = new BookRecieveRegistrationRetailerHelper();
		this.scracthDao = new ScratchDaoImpl();
		dao=new OrderMgmtDaoImpl();
	}
	
	public ScratchServiceImpl(BookRecieveRegistrationRetailerHelper helper,ScratchDao scracthDao){
		this.helper = helper;
		this.scracthDao = scracthDao;
	}
	
	public ScratchServiceImpl(OrderMgmtDao dao){
		this.dao= dao;
	}
	
	public boolean isBookNumberListValid(ScracthMgmtBean bean){
		try{
			DaoBean daoBean = scracthDao.getUserOrgIdAndUserIdFromTpUserId(bean.getTpUserId());
			if(daoBean != null){
				Map<String, List<String>> gameBookMap = helper.getBooks(daoBean.getUserOrgId(),bean.getDlNumber());
				List<String> bookNumberListForFinalComaparison = null;
				bookNumberListForFinalComaparison = getFinalComparisonBookNumberList(gameBookMap);
				boolean isValidBookNbr = isRecivedBookNbrListContainsValidBookNbr(bean, bookNumberListForFinalComaparison);
				return isValidBookNbr;
			}
		}catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
	
	public List<GameListResponseData> getGameList() throws Exception, ScratchException{
		GameListResponseData gameListResponseData=null;
		Connection connection=null;
		List<GameListResponseData> gameList =new ArrayList<GameListResponseData>();
		
		try{
			connection=DBConnect.getConnection();
			gameListResponseData=new GameListResponseData();
			gameList = dao.getGameListDaoImpl(connection);
			
			
		}catch(Exception e){
			throw new ScratchException();
		}
		finally{
			connection.close();
		}
		
		return gameList;
		
	}

	public String updateBookListStatus(ScracthMgmtBean bean){
		try{
			String[] response = helper.updateBooks((int)bean.getRequestId(),0,bean.getBookList());
			if(response != null && "SUCCESS".equals(response[2])){
				return "SUCCESS";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "Fail";
	}

	private boolean isRecivedBookNbrListContainsValidBookNbr(ScracthMgmtBean bean,
			List<String> bookNumberListForFinalComaparison) {
		if(bookNumberListForFinalComaparison != null){
			List<String> recievedBookList = bean.getBookList();
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

	public OrderMgmtDao getDao() {
		return dao;
	}

	
	public DaoBean getRetailerData(String tpUserID) throws LMSException{
		DaoBean daoBean = null;
		try {
			daoBean = scracthDao.getUserOrgIdAndUserIdFromTpUserId(tpUserID);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
		if(daoBean == null){
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, "TpUserId Is Invalid");
		}
		return daoBean;
	}
	
	public int getAgentOrganizationId(String tpUserID) throws LMSException{
		int agentOrgId = scracthDao.getAgentOrgIdFromTPUserId(tpUserID);
		if(agentOrgId == 0){
			throw new LMSException(LMSErrors.INVALID_DATA_ERROR_CODE, "TpUserId Is Invalid");
		}
		return agentOrgId;
	}

	@Override
	public UserInfoBean getUserBeanFromTPUserId(String tpUserId) throws LMSException {
		Connection connection=DBConnect.getConnection();
		return scracthDao.getUserBeanFromTpUserId(tpUserId, connection);
	}
	
	@Override
	public int getParentOrgId(int userId) throws LMSException {
		Connection connection=DBConnect.getConnection();
		return scracthDao.getParentOrgId(userId, connection);
	}
	
}
