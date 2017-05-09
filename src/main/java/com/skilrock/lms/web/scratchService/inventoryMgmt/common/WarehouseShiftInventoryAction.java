package com.skilrock.lms.web.scratchService.inventoryMgmt.common;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.scratchService.gameMgmt.common.GameuploadHelper;
import com.skilrock.lms.coreEngine.scratchService.inventoryMgmt.serviceImpl.WarehouseShiftInventoryServiceImpl;

public class WarehouseShiftInventoryAction extends BaseAction{
	
	static Log logger = LogFactory.getLog(DirectSaleBOAction.class);
	
	public WarehouseShiftInventoryAction(){
		super(WarehouseShiftInventoryAction.class);
	}
	
	private String[] bookArr;
	private int[] bookCountArr;
	private String[] bookCountArr1;
	private String[] bookFromArr;
	private int[] bookFromCountArr;
	private String[] bookFromCountArr1;
	private String[] bookNumber;

	private String[] bookTOArr;

	private int[] bookToCountArr;
	private String[] bookToCountArr1;
	private String[] gameName;
	private String fromWarehouse ;
	private String toWarehouse ;
	
	public String execute(){
		
		HttpSession session = request.getSession() ;
		UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("USER_INFO") ;
		session.setAttribute("FROM_WAREHOUSES", GameuploadHelper.getAllWarehouses(userInfoBean.getUserId(), "FROM")) ;
		session.setAttribute("TO_WAREHOUSES", GameuploadHelper.getAllWarehouses(userInfoBean.getUserId(), "TO")) ;
		
		return SUCCESS ;
	}
	
	
	
	public void verifyInv() throws Exception
	{
		logger.info("Book Arr : " + Arrays.asList(bookArr)) ; // Value of Book number field in each game.
		logger.info("Book Count Arr 1 : " + Arrays.asList(bookCountArr1)) ; // Count of books entered in Book Number field
		logger.info("Book From Arr : " + Arrays.asList(bookFromArr)) ; // Book Series Started From
		logger.info("Book From Count Arr 1 : " + Arrays.asList(bookFromCountArr1)) ; // count of series
		logger.info("Book TO Arr : " + Arrays.asList(bookTOArr)) ; // Book Series Ended to
		logger.info("Game Name : " + Arrays.asList(gameName)); // Name of Game
		logger.info("From Warehouse : " + fromWarehouse) ; // From which warehouse inventory has to be shifted
		logger.info("To Warehouse : " + toWarehouse) ; // To which warehouse inventory has to be shifted
		
		StringBuilder finalData = new StringBuilder();
		UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("USER_INFO") ;
		List<String> inValidBooks = 
			WarehouseShiftInventoryServiceImpl.verifyInv(bookArr, bookCountArr1, bookFromArr, bookFromCountArr1, bookTOArr, gameName, fromWarehouse, toWarehouse, "verify", userInfoBean);
		
		if(inValidBooks.size() > 0){
			for(String bookId : inValidBooks)
				finalData.append(bookId+", ");
			
			finalData.toString().trim().replace(Character.toString(finalData.charAt(finalData.lastIndexOf(","))), "") ;
		}
		else
			finalData.append("success");
		
		
		logger.info("Final Data : " + finalData.toString()) ;
		response.getOutputStream().write((finalData.toString()).getBytes());
		
	}
	
	public String shiftInv() throws Exception{
		UserInfoBean userInfoBean = (UserInfoBean)request.getSession().getAttribute("USER_INFO") ;
		 HttpSession session = request.getSession() ;
		List<String> books = WarehouseShiftInventoryServiceImpl.verifyInv(bookArr, bookCountArr1, bookFromArr, bookFromCountArr1, bookTOArr, gameName, fromWarehouse, toWarehouse, "insert", userInfoBean);

		String[] response = WarehouseShiftInventoryServiceImpl.generateDeliveryChallanAndUpdateDetails(userInfoBean.getUserOrgId(), books); 
		session.setAttribute("dcId", response[0]);
		session.setAttribute("autoGeneDCNo", response[1]);

		return SUCCESS;
	}

	public String[] getBookArr() {
		return bookArr;
	}

	public void setBookArr(String[] bookArr) {
		this.bookArr = bookArr;
	}

	public int[] getBookCountArr() {
		return bookCountArr;
	}

	public void setBookCountArr(int[] bookCountArr) {
		this.bookCountArr = bookCountArr;
	}

	public String[] getBookCountArr1() {
		return bookCountArr1;
	}

	public void setBookCountArr1(String[] bookCountArr1) {
		this.bookCountArr1 = bookCountArr1;
	}

	public String[] getBookFromArr() {
		return bookFromArr;
	}

	public void setBookFromArr(String[] bookFromArr) {
		this.bookFromArr = bookFromArr;
	}

	public int[] getBookFromCountArr() {
		return bookFromCountArr;
	}

	public void setBookFromCountArr(int[] bookFromCountArr) {
		this.bookFromCountArr = bookFromCountArr;
	}

	public String[] getBookFromCountArr1() {
		return bookFromCountArr1;
	}

	public void setBookFromCountArr1(String[] bookFromCountArr1) {
		this.bookFromCountArr1 = bookFromCountArr1;
	}

	public String[] getBookNumber() {
		return bookNumber;
	}

	public void setBookNumber(String[] bookNumber) {
		this.bookNumber = bookNumber;
	}

	public String[] getBookTOArr() {
		return bookTOArr;
	}

	public void setBookTOArr(String[] bookTOArr) {
		this.bookTOArr = bookTOArr;
	}

	public int[] getBookToCountArr() {
		return bookToCountArr;
	}

	public void setBookToCountArr(int[] bookToCountArr) {
		this.bookToCountArr = bookToCountArr;
	}

	public String[] getBookToCountArr1() {
		return bookToCountArr1;
	}

	public void setBookToCountArr1(String[] bookToCountArr1) {
		this.bookToCountArr1 = bookToCountArr1;
	}

	public String getFromWarehouse() {
		return fromWarehouse;
	}

	public void setFromWarehouse(String fromWarehouse) {
		this.fromWarehouse = fromWarehouse;
	}

	public String getToWarehouse() {
		return toWarehouse;
	}

	public void setToWarehouse(String toWarehouse) {
		this.toWarehouse = toWarehouse;
	}



	public String[] getGameName() {
		return gameName;
	}



	public void setGameName(String[] gameName) {
		this.gameName = gameName;
	}
	
	

}
