package com.skilrock.lms.rest.scratchService.orderMgmt.ServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.orderMgmt.common.BookRecieveRegistrationRetailerHelper;
import com.skilrock.lms.rest.scratchService.dao.ScratchDao;
import com.skilrock.lms.rest.scratchService.orderMgmt.serviceImpl.ScratchServiceImpl;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;

public class ScratchServiceImplTest {

	@Test
	public void canCreateScratchServiceImpl(){
		new ScratchServiceImpl();
	}
	
	@Test
	public void isBookNumberListValid_exists() throws SQLException{
		BookRecieveRegistrationRetailerHelper helper = Mockito.mock(BookRecieveRegistrationRetailerHelper.class);
		Mockito.when(helper.getBooks(Mockito.anyInt(),Mockito.anyString())).thenReturn(new HashMap<String,List<String>>());
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		ScratchServiceImpl scratchServiceImpl = new ScratchServiceImpl(helper,scracthDao);
		scratchServiceImpl.isBookNumberListValid(new ScracthMgmtBean());
	}
	
	
	@Test
	public void isBookNumberListValid_returnFalseIfGameMapIsNull() throws SQLException{
		BookRecieveRegistrationRetailerHelper helper = Mockito.mock(BookRecieveRegistrationRetailerHelper.class);
		Mockito.when(helper.getBooks(Mockito.anyInt(),Mockito.anyString())).thenReturn(null);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		ScratchServiceImpl scratchServiceImpl = new ScratchServiceImpl(helper,scracthDao);
		Assert.assertEquals(false, scratchServiceImpl.isBookNumberListValid(new ScracthMgmtBean()));
	}
	
	@Test
	public void isBookNumberListValid_ReturnFalseifGameMapIsNotNullButRecievedBookNbrIsNotInGameMap() throws SQLException{
		BookRecieveRegistrationRetailerHelper helper = Mockito.mock(BookRecieveRegistrationRetailerHelper.class);
		Map<String,List<String>> gameBookMap = new HashMap<String,List<String>>();
		List<String> bookNbrList = new ArrayList<String>();
		bookNbrList.add("101-001011");
		gameBookMap.put("1-101-Cash It",bookNbrList);
		Mockito.when(helper.getBooks(Mockito.anyInt(),Mockito.anyString())).thenReturn(gameBookMap);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		ScratchServiceImpl scratchServiceImpl = new ScratchServiceImpl(helper,scracthDao);
		ScracthMgmtBean bean = new ScracthMgmtBean();
		List<String> bookNbrListTes = new ArrayList<String>();
		bookNbrListTes.add("101-0010111");
		bean.setBookList(bookNbrListTes);
		Assert.assertEquals(false, scratchServiceImpl.isBookNumberListValid(bean));
	}
	
	@Test
	public void isValidBookNbr_ReturnTrueifGameMapIsNotNullButRecievedBookNbrIsInGameMap() throws SQLException{
		BookRecieveRegistrationRetailerHelper helper = Mockito.mock(BookRecieveRegistrationRetailerHelper.class);
		Map<String,List<String>> gameBookMap = new HashMap<String,List<String>>();
		List<String> bookNbrList = new ArrayList<String>();
		bookNbrList.add("101-001011");
		gameBookMap.put("1-101-Cash It",bookNbrList);
		Mockito.when(helper.getBooks(Mockito.anyInt(),Mockito.anyString())).thenReturn(gameBookMap);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		ScratchServiceImpl scratchServiceImpl = new ScratchServiceImpl(helper,scracthDao);
		ScracthMgmtBean bean = new ScracthMgmtBean();
		bean.setBookList(bookNbrList);
		Assert.assertEquals(true, scratchServiceImpl.isBookNumberListValid(bean));
	}
	
	@Test
	public void canCreateUpdateBookNbrListStatus(){
		BookRecieveRegistrationRetailerHelper helper = Mockito.mock(BookRecieveRegistrationRetailerHelper.class);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		new ScratchServiceImpl(helper,scracthDao).updateBookListStatus(new ScracthMgmtBean());
	}
	
	@Test
	public void updateBookNbrListStatus_RetureSuccessIfGetSuccessFromHelper() throws LMSException, SQLException{
		BookRecieveRegistrationRetailerHelper helper = Mockito.mock(BookRecieveRegistrationRetailerHelper.class);
		List<String> bookNbrList = new ArrayList<String>();
		bookNbrList.add("111");
		Mockito.when(helper.updateBooks(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyList())).thenReturn(new String[]{"0","0","SUCCESS"});
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		ScratchServiceImpl scratchServiceImpl = new ScratchServiceImpl(helper,scracthDao);
		String response = scratchServiceImpl.updateBookListStatus(new ScracthMgmtBean());
		Assert.assertEquals("SUCCESS",response);
	}
	
	@Test
	public void updateBookNbrListStatus_ReturnFailIfGetNullFromHelper() throws LMSException, SQLException{
		BookRecieveRegistrationRetailerHelper helper = Mockito.mock(BookRecieveRegistrationRetailerHelper.class);
		List<String> bookNbrList = new ArrayList<String>();
		bookNbrList.add("111");
		Mockito.when(helper.updateBooks(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyList())).thenReturn(null);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		ScratchServiceImpl scratchServiceImpl = new ScratchServiceImpl(helper,scracthDao);
		String response = scratchServiceImpl.updateBookListStatus(new ScracthMgmtBean());
		Assert.assertEquals("Fail",response);
	}
	
	@Test
	public void updateBookNbrListStatus_ReturnFailIfGetHelperIsNull() throws LMSException, SQLException{
			ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
			ScratchServiceImpl scratchServiceImpl = new ScratchServiceImpl(null,scracthDao);
			String response = scratchServiceImpl.updateBookListStatus(new ScracthMgmtBean());
			Assert.assertEquals("Fail",response);
	}
	
	@Test
	public void isvalidBokkNbrList_ReturnsFalseInCaseOfAnyException(){
		ScratchServiceImpl scratchServiceImpl = new ScratchServiceImpl();
		Assert.assertEquals(false, scratchServiceImpl.isBookNumberListValid(null));
	}
	
	@Test
	public void isvalidBokkNbrList_ReturnsFalseInCaseOfHelperIsNotInjected(){
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		ScratchServiceImpl scratchServiceImpl = new ScratchServiceImpl(null,scracthDao);
		Assert.assertEquals(false, scratchServiceImpl.isBookNumberListValid(null));
	}
	
	@Test
	public void isvalidBokkNbrList_ReturnsFalseInCaseOfScracthDaoIsNotInjected(){
		ScratchServiceImpl scratchServiceImpl = new ScratchServiceImpl(null,null);
		Assert.assertEquals(false, scratchServiceImpl.isBookNumberListValid(null));
	}
	
}
