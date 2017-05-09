package com.skilrock.lms.rest.scratchService.inventoryMgmt.serviceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import com.skilrock.lms.beans.TicketBean;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.Utility;
import com.skilrock.lms.common.exception.LMSException;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.BookWiseInvDetailForRetHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.common.SoldTicketEntryForRetHelper;
import com.skilrock.lms.coreEngine.scratchService.pwtMgmt.common.CommonFunctionsHelper;
import com.skilrock.lms.rest.scratchService.dao.ScratchDao;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.dao.InventoryDao;
import com.skilrock.lms.rest.scratchService.inventoryMgmt.daoImpl.InventoryDaoImpl;
import com.skilrock.lms.rest.services.bean.DaoBean;
import com.skilrock.lms.rest.services.bean.ScracthMgmtBean;

@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.skilrock.lms.common.Utility"})
@PrepareForTest({Utility.class})
public class BookActivationServiceImplTest {
	
	@Test
	public void canCreateBookActivationServiceImpl(){
		new BookActivationServiceImpl();
	}
	
	@Test
	public void isBookNumberValid_exists() throws SQLException{
		BookWiseInvDetailForRetHelper helper = Mockito.mock(BookWiseInvDetailForRetHelper.class);
		Mockito.when(helper.activateBooks(Mockito.anyInt())).thenReturn(new HashMap<String,List<String>>());
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean bean = new DaoBean();
		bean.setUserOrgId(26);
		bean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(bean);
		BookActivationServiceImpl bookActivationServiceImpl = new BookActivationServiceImpl(helper,scracthDao);
		bookActivationServiceImpl.isBookNumberValid(new ScracthMgmtBean());
	}
	
	@Test
	public void isBookNumberValid_returnFalseIfGameMapIsNull() throws SQLException{
		BookWiseInvDetailForRetHelper helper = Mockito.mock(BookWiseInvDetailForRetHelper.class);
		Mockito.when(helper.activateBooks(Mockito.anyInt())).thenReturn(null);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean bean = new DaoBean();
		bean.setUserOrgId(26);
		bean.setUserId(0);
		ScracthMgmtBean bean1 = new ScracthMgmtBean();
		bean1.setBookNumber("101-001011");
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(bean);
		BookActivationServiceImpl bookActivationServiceImpl = new BookActivationServiceImpl(helper,scracthDao);
		Assert.assertEquals(false, bookActivationServiceImpl.isBookNumberValid(bean1));
	}
	
	@Test
	public void isValidBookNbrReturn_FalseifGameMapIsNotNullButRecievedBookNbrIsNotInGameMap() throws SQLException{
		BookWiseInvDetailForRetHelper helper = Mockito.mock(BookWiseInvDetailForRetHelper.class);
		Map<String,List<String>> gameBookMap = new HashMap<String,List<String>>();
		List<String> bookNbrList = new ArrayList<String>();
		bookNbrList.add("101-001011");
		gameBookMap.put("1-101-Cash It",bookNbrList);
		Mockito.when(helper.activateBooks(Mockito.anyInt())).thenReturn(gameBookMap);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		BookActivationServiceImpl bookActivationServiceImpl = new BookActivationServiceImpl(helper,scracthDao);
		ScracthMgmtBean bean = new ScracthMgmtBean();
		bean.setBookNumber("101-0010111");
		Assert.assertEquals(false, bookActivationServiceImpl.isBookNumberValid(bean));
	}
	
	@Test
	public void isValidBookNbr_ReturnTrueifGameMapIsNotNullButRecievedBookNbrIsInGameMap() throws Exception{
		BookWiseInvDetailForRetHelper helper = Mockito.mock(BookWiseInvDetailForRetHelper.class);
		CommonFunctionsHelper commonHelper = Mockito.mock(CommonFunctionsHelper.class);
		Map<String,List<String>> gameBookMap = new HashMap<String,List<String>>();
		List<String> bookNbrList = new ArrayList<String>();
		bookNbrList.add("101-001011");
		gameBookMap.put("1-101-Cash It",bookNbrList);
		Mockito.when(helper.activateBooks(Mockito.anyInt())).thenReturn(gameBookMap);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		BookActivationServiceImpl bookActivationServiceImpl = new BookActivationServiceImpl(helper,scracthDao,commonHelper);
		TicketBean bookBean = new TicketBean();
		bookBean.setBook_nbr("101-001011");
		bookBean.setValid(true);
		Mockito.when(commonHelper.isBookNbrFormatValid(Mockito.anyString(),Mockito.anyInt())).thenReturn(bookBean);
		ScracthMgmtBean bean = new ScracthMgmtBean();
		bean.setBookNumber("101-001011");
		PowerMockito.mockStatic(Utility.class);
		PowerMockito.when(Utility.class, "getPropertyValue", Mockito.anyString()).thenReturn("3");
		Assert.assertEquals(true, bookActivationServiceImpl.isBookNumberValid(bean));
	}
	
	@Test
	public void isValidBookNbr_ReturnFalseIfBookNbrIsNotValidFormatAndNotPresentInDataBase() throws Exception{
		BookWiseInvDetailForRetHelper helper = Mockito.mock(BookWiseInvDetailForRetHelper.class);
		CommonFunctionsHelper commonHelper = Mockito.mock(CommonFunctionsHelper.class);
		Map<String,List<String>> gameBookMap = new HashMap<String,List<String>>();
		List<String> bookNbrList = new ArrayList<String>();
		bookNbrList.add("101-001011");
		gameBookMap.put("1-101-Cash It",bookNbrList);
		Mockito.when(helper.activateBooks(Mockito.anyInt())).thenReturn(gameBookMap);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		BookActivationServiceImpl bookActivationServiceImpl = new BookActivationServiceImpl(helper,scracthDao,commonHelper);
		TicketBean bookBean = new TicketBean();
		bookBean.setBook_nbr("101001011");
		bookBean.setValid(false);
		Mockito.when(commonHelper.isBookNbrFormatValid(Mockito.anyString(),Mockito.anyInt())).thenReturn(bookBean);
		ScracthMgmtBean bean = new ScracthMgmtBean();
		bean.setBookNumber("101001011");
		PowerMockito.mockStatic(Utility.class);
		PowerMockito.when(Utility.class, "getPropertyValue", Mockito.anyString()).thenReturn("3");
		Assert.assertEquals(false, bookActivationServiceImpl.isBookNumberValid(bean));
	}
	
	@Test
	public void isValidBookNbr_ReturnTrueIfBookNbrIsNotValidFormatButPresentInDB() throws Exception{
		BookWiseInvDetailForRetHelper helper = Mockito.mock(BookWiseInvDetailForRetHelper.class);
		CommonFunctionsHelper commonHelper = Mockito.mock(CommonFunctionsHelper.class);
		Map<String,List<String>> gameBookMap = new HashMap<String,List<String>>();
		List<String> bookNbrList = new ArrayList<String>();
		bookNbrList.add("111-001011");
		gameBookMap.put("1-101-Cash It",bookNbrList);
		Mockito.when(helper.activateBooks(Mockito.anyInt())).thenReturn(gameBookMap);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		BookActivationServiceImpl bookActivationServiceImpl = new BookActivationServiceImpl(helper,scracthDao,commonHelper);
		TicketBean bookBean = new TicketBean();
		bookBean.setBook_nbr("111-001011");
		bookBean.setValid(true);
		Mockito.when(commonHelper.isBookNbrFormatValid(Mockito.anyString(),Mockito.anyInt())).thenReturn(bookBean);
		ScracthMgmtBean bean = new ScracthMgmtBean();
		bean.setBookNumber("111001011");
		PowerMockito.mockStatic(Utility.class);
		PowerMockito.when(Utility.class, "getPropertyValue", Mockito.anyString()).thenReturn("3");
		Assert.assertEquals(true, bookActivationServiceImpl.isBookNumberValid(bean));
	}
	
	@Test
	public void canCreateUpdateBookNbrStatus(){
		BookWiseInvDetailForRetHelper helper = Mockito.mock(BookWiseInvDetailForRetHelper.class);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		new BookActivationServiceImpl(helper,scracthDao).updateBookNumberStatus(new ScracthMgmtBean());
	}
	
	@Test
	public void updateBookNbrStatus_RetureSuccessIfGetSuccessFromHelper() throws LMSException, SQLException{
		BookWiseInvDetailForRetHelper helper = Mockito.mock(BookWiseInvDetailForRetHelper.class);
		List<String> bookNbrList = new ArrayList<String>();
		bookNbrList.add("111");
		Mockito.when(helper.updateBooks(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyList())).thenReturn(new String[]{"0","0","SUCCESS"});
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		BookActivationServiceImpl bookActivationServiceImpl = new BookActivationServiceImpl(helper,scracthDao);		
		String response = bookActivationServiceImpl.updateBookNumberStatus(new ScracthMgmtBean());
		Assert.assertEquals("SUCCESS",response);
	}
	
	@Test
	public void updateBookNbrStatus_ReturnFailIfGetNullFromHelper() throws LMSException, SQLException{
		BookWiseInvDetailForRetHelper helper = Mockito.mock(BookWiseInvDetailForRetHelper.class);
		List<String> bookNbrList = new ArrayList<String>();
		bookNbrList.add("111");
		Mockito.when(helper.updateBooks(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyList())).thenReturn(null);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		BookActivationServiceImpl bookActivationServiceImpl = new BookActivationServiceImpl(helper,scracthDao);		
		String response = bookActivationServiceImpl.updateBookNumberStatus(new ScracthMgmtBean());
		Assert.assertEquals("FAIL",response);
	}
	
	@Test
	public void updateBookNbrStatus_ReturnFailIfHelperIsNull() throws LMSException{
		BookWiseInvDetailForRetHelper helper = null;
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		BookActivationServiceImpl bookActivationServiceImpl = new BookActivationServiceImpl(helper,scracthDao);
		String response = bookActivationServiceImpl.updateBookNumberStatus(new ScracthMgmtBean());
		Assert.assertEquals("FAIL",response);
	}
	
	@Test
	public void isvalidBokkNbr_ReturnsFalseInCaseOfAnyException(){
		BookActivationServiceImpl impl = new BookActivationServiceImpl();
		Assert.assertEquals(false, impl.isBookNumberValid(null));
	}
	
	@Test
	public void isvalidBokkNbrList_ReturnsFalseInCaseOfHelperIsNotInjected(){
		BookWiseInvDetailForRetHelper helper = null;
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		BookActivationServiceImpl impl = new BookActivationServiceImpl(helper,scracthDao);
		Assert.assertEquals(false, impl.isBookNumberValid(null));
	}
	
	@Test
	public void isvalidBokkNbrList_ReturnsFalseInCaseOfScratchDaoIsNotInjected(){
		BookWiseInvDetailForRetHelper helper= null;
		BookActivationServiceImpl impl = new BookActivationServiceImpl(helper,null);
		Assert.assertEquals(false, impl.isBookNumberValid(null));
	}
	
	@Test
	public void updateBookNbrStatus_ReturnFailIfScracthDaoIsNull() throws LMSException{
		BookWiseInvDetailForRetHelper helper= null;
		BookActivationServiceImpl bookActivationServiceImpl = new BookActivationServiceImpl(helper,null);
		String response = bookActivationServiceImpl.updateBookNumberStatus(new ScracthMgmtBean());
		Assert.assertEquals("FAIL",response);
	}
	
	
	
	@Test
	public void isTicketByTicketSale_MethodExists(){
		BookActivationServiceImpl impl = new BookActivationServiceImpl();
		impl.ticketByTicketSale(new ScracthMgmtBean());
	}
	
	@Test
	public void ticketByTicketSale_ReturnsFailInCaseOfScratchDaoIsNotInjected(){
		SoldTicketEntryForRetHelper saleTicketHelper = null;
		BookActivationServiceImpl impl = new BookActivationServiceImpl(saleTicketHelper,null);
		Assert.assertEquals("FAIL", impl.ticketByTicketSale(null));
	}
	
	@Test
	public void ticketByTicketSale_ReturnsFailInCaseOfUserOrgIdAndUserIdComesNull() throws SQLException{
		SoldTicketEntryForRetHelper saleTicketHelper = null;
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(null);
		BookActivationServiceImpl impl = new BookActivationServiceImpl(saleTicketHelper,scracthDao);
		Assert.assertEquals("FAIL",impl.ticketByTicketSale(null));
	}
	
	@Test
	public void ticketByTicketSale_ReturnsValidResponseStringIfSaleTicketHelperIsInjected() throws SQLException, LMSException{
		SoldTicketEntryForRetHelper saleTicketHelper = Mockito.mock(SoldTicketEntryForRetHelper.class);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		Mockito.when(saleTicketHelper.updateSellTicketStatus(Mockito.anyString(),
				Mockito.any(UserInfoBean.class), Mockito.anyString())).thenReturn("11112236");
		BookActivationServiceImpl impl = new BookActivationServiceImpl(saleTicketHelper,scracthDao);
		Assert.assertEquals("11112236",impl.ticketByTicketSale(new ScracthMgmtBean()));
	}
	
	
	@Test
	public void ticketByTicketSale_ReturnsFailIfSaleTicketHelperIsNotInjected() throws SQLException, LMSException{
		SoldTicketEntryForRetHelper saleTicketHelper = null;
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		BookActivationServiceImpl impl = new BookActivationServiceImpl(saleTicketHelper,scracthDao);
		Assert.assertEquals("FAIL",impl.ticketByTicketSale(new ScracthMgmtBean()));
	}
	
	@Test
	public void isTicketByTicketUnSold_MethodExists(){
		BookActivationServiceImpl impl = new BookActivationServiceImpl();
		impl.ticketByTicketUnSold(new ScracthMgmtBean());
	}
	
	@Test
	public void ticketByTicketUnSold_ReturnsFailInCaseOfScratchDaoIsNotInjected(){
		SoldTicketEntryForRetHelper saleTicketHelper = null;
		BookActivationServiceImpl impl = new BookActivationServiceImpl(saleTicketHelper,null);
		Assert.assertEquals("FAIL", impl.ticketByTicketUnSold(null));
	}
	
	@Test
	public void ticketByTicketUnSold_ReturnsFailInCaseOfDaoBeanNull() throws SQLException{
		SoldTicketEntryForRetHelper saleTicketHelper = null;
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(null);
		BookActivationServiceImpl impl = new BookActivationServiceImpl(saleTicketHelper,scracthDao);
		Assert.assertEquals("FAIL",impl.ticketByTicketUnSold(null));
	}
	
	@Test
	public void ticketByTicketUnSold_ReturnsValidResponseStringIfSaleTicketHelperIsInjected() throws SQLException, LMSException{
		InventoryDao inventoryDao = Mockito.mock(InventoryDaoImpl.class);
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		Mockito.when(inventoryDao.updateSellTicketStatusAsUnSold(daobean)).thenReturn("11112236");
		BookActivationServiceImpl impl = new BookActivationServiceImpl(inventoryDao,scracthDao);
		Assert.assertEquals("11112236",impl.ticketByTicketUnSold(new ScracthMgmtBean()));
	}
	
	
	@Test
	public void ticketByTicketUnSold_ReturnsFailIfSaleTicketHelperIsNotInjected() throws SQLException, LMSException{
		InventoryDao inventoryDao = null;
		ScratchDao scracthDao = Mockito.mock(ScratchDao.class);
		DaoBean daobean = new DaoBean();
		daobean.setUserOrgId(26);
		daobean.setUserId(0);
		Mockito.when(scracthDao.getUserOrgIdAndUserIdFromTpUserId(Mockito.anyString())).thenReturn(daobean);
		BookActivationServiceImpl impl = new BookActivationServiceImpl(inventoryDao,scracthDao);
		Assert.assertEquals("FAIL",impl.ticketByTicketSale(new ScracthMgmtBean()));
	}
}
