package com.skilrock.lms.embedded.sportsLottery.playMgmt.action;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;
import com.skilrock.lms.beans.UserInfoBean;
import com.skilrock.lms.common.BaseAction;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryGameDrawDataBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryGameEventDataBean;
import com.skilrock.lms.coreEngine.sportsLottery.beans.SportsLotteryGamePlayBean;
import com.skilrock.lms.coreEngine.sportsLottery.common.SLEException;
import com.skilrock.lms.coreEngine.sportsLottery.common.SportsLotteryUtils;
import com.skilrock.lms.coreEngine.sportsLottery.playMgmt.controllerImpl.SportsLotteryGamePlayControllerImpl;
import com.skilrock.lms.embedded.sportsLottery.common.SportsLotteryResponseData;

public class SportsLotteryTerminalGamePlayAction  extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SportsLotteryTerminalGamePlayAction() {
		super(SportsLotteryTerminalGamePlayAction.class);
	}
	
	private int gameId;
	private int gameTypeId;
	private String[] drawInfo;
	private int drawCount;
	private double ticketAmt;
	private String userName;
	public void sportsLotteryPurchaseTicket(){
		
		try{
		UserInfoBean userBean=getUserBean(userName);
				
		SportsLotteryGameEventDataBean eventDataBean =null;
		SportsLotteryGameDrawDataBean gameDrawDataBean=null;
		
		SportsLotteryGamePlayBean gamePlayBean =new SportsLotteryGamePlayBean();
		gamePlayBean.setGameId(gameId);
		gamePlayBean.setGameTypeId(gameTypeId);
		
		int noOfBoard=drawCount;
		int noOfEvents=SportsLotteryUtils.gameTypeInfoMap.get(gameTypeId).getNoOfEvents();
		
		double unitPrice=SportsLotteryUtils.gameTypeInfoMap.get(gameTypeId).getUnitPrice();
		double totalPurchaseAmt=0.0;
		Set<Integer> drawIsSet=new HashSet<Integer>();
		
		gamePlayBean.setNoOfBoard(noOfBoard);
		SportsLotteryGameEventDataBean[] eventDataBeanArray=null;
		SportsLotteryGameDrawDataBean[] gameDrawDataBeanArray=new SportsLotteryGameDrawDataBean[noOfBoard];
		
		for(int i=0;i<noOfBoard;i++){
			
			String drawData=drawInfo[i];
			String[] drawDataArray=drawData.split("~");
			int drawId=Integer.parseInt(drawDataArray[0]);
			int betAmtMultiple=Integer.parseInt(drawDataArray[1]);
			
			String[] evntData=drawDataArray[2].split("\\$");
			
			int noOfLines=1;
			
			eventDataBeanArray=new SportsLotteryGameEventDataBean[noOfEvents];
			drawIsSet.add(drawId);
			for(int j=0;j<noOfEvents;j++){
				eventDataBean = new SportsLotteryGameEventDataBean();
				String[] eventArr=evntData[j].split("@");
				
				eventDataBean.setEventId(Integer.parseInt(eventArr[0]));
				String[] selectedOption=eventArr[1].split(",");
				
				noOfLines*=selectedOption.length;
				
				eventDataBean.setSelectedOption(selectedOption);
				eventDataBeanArray[j] =eventDataBean;
			}
		    			
		    gameDrawDataBean=new SportsLotteryGameDrawDataBean();
		    gameDrawDataBean.setBetAmountMultiple(betAmtMultiple);
		    gameDrawDataBean.setNoOfLines(noOfLines);
		    
		    gameDrawDataBean.setBoardPurchaseAmount(noOfLines*unitPrice*betAmtMultiple);
		    totalPurchaseAmt+=noOfLines*unitPrice*betAmtMultiple;
			gameDrawDataBean.setDrawId(drawId);
			gameDrawDataBean.setGameEventDataBeanArray(eventDataBeanArray);
			gameDrawDataBeanArray[i]=gameDrawDataBean;
			
		}
		
		gamePlayBean.setGameDrawDataBeanArray(gameDrawDataBeanArray);
		gamePlayBean.setServiceCode("SL");
		gamePlayBean.setInterfaceType("TERMINAL");
		gamePlayBean.setMerchantName("WGRL");
		gamePlayBean.setUserType(userBean.getUserType());
		gamePlayBean.setUserId(userBean.getUserId());
		gamePlayBean.setTotalPurchaseAmt(totalPurchaseAmt);
		gamePlayBean.setUnitPrice(unitPrice);
		Integer[] drawIdArray = (Integer[])drawIsSet.toArray(new Integer[drawIsSet.size()]);
		gamePlayBean.setDrawIdArray(drawIdArray);
		
		System.out.println(new Gson().toJson(gamePlayBean));
		SportsLotteryGamePlayControllerImpl gamePlayControllerImpl =new SportsLotteryGamePlayControllerImpl();
		SportsLotteryGamePlayBean gamePlayBeanResponse =gamePlayControllerImpl.purchaseTicketControllerImpl(gamePlayBean,userBean);
		
		double bal = userBean.getAvailableCreditLimit()
				- userBean.getClaimableBal();
		
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		
		String balance = nf.format(bal).replaceAll(",", "");
		
		String responseString=SportsLotteryResponseData.generateSportsLotterySaleResponseData(gamePlayBeanResponse,balance);
		
		System.out.println(responseString);
		response.getOutputStream().write(responseString.getBytes());
		}catch (SLEException e) {
			try {
				response.getOutputStream().write(("ErrorMsg:"+e.getErrorMessage()).getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}catch (IOException e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			return;
		}catch (Exception e) {
			e.printStackTrace();
			try {
				response.getOutputStream().write("ErrorMsg:Error!Try Again".getBytes());
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			return;
		}
			
	}
	
/*	
	public void purchaseTicket() throws SLEException{
		
		UserInfoBean userBean=null;
		userBean=new UserInfoBean();
		userBean.setUserId(11003);
		userBean.setUserOrgId(3);
		userBean.setUserType("RETAILER");
		userBean.setParentOrgId(2);
		
		
		SportsLotteryGameEventDataBean eventDataBean =null;
		SportsLotteryGameDrawDataBean gameDrawDataBean=null;
		
		SportsLotteryGamePlayBean gamePlayBean =new SportsLotteryGamePlayBean();
		gamePlayBean.setGameId(1);
		gamePlayBean.setGameTypeId(1);
		
		int noOfBoard=2;
		int noOfEvents=8;
		Integer[] drawIdArray=new Integer[1];
		drawIdArray[0]=1;
		
		gamePlayBean.setNoOfBoard(noOfBoard);
		SportsLotteryGameEventDataBean[] eventDataBeanArray=new SportsLotteryGameEventDataBean[noOfEvents];
		SportsLotteryGameDrawDataBean[] gameDrawDataBeanArray=new SportsLotteryGameDrawDataBean[noOfBoard];
		
		for(int i=0;i<noOfBoard;i++){
			for(int j=0;j<noOfEvents;j++){
				eventDataBean = new SportsLotteryGameEventDataBean();
				eventDataBean.setEventId(1+j);
				String[] selectedOption=new String[1];
				selectedOption[0]="H";
				
				eventDataBean.setSelectedOption(selectedOption);
				eventDataBeanArray[j] =eventDataBean;
			}
		    			
		    gameDrawDataBean=new SportsLotteryGameDrawDataBean();
		    gameDrawDataBean.setBetAmountMultiple(1);
		    gameDrawDataBean.setBoardPurchaseAmount(12);
		    gameDrawDataBean.setNoOfLines(1);
			gameDrawDataBean.setDrawId(1);
			gameDrawDataBean.setGameEventDataBeanArray(eventDataBeanArray);
			gameDrawDataBeanArray[i]=gameDrawDataBean;
			
		}
		
		gamePlayBean.setGameDrawDataBeanArray(gameDrawDataBeanArray);
		gamePlayBean.setServiceCode("SL");
		gamePlayBean.setInterfaceType("TERMINAL");
		gamePlayBean.setMerchantName("WGRL");
		gamePlayBean.setUserType(userBean.getUserType());
		gamePlayBean.setUserId(userBean.getUserId());
		gamePlayBean.setDrawIdArray(drawIdArray);
		
		System.out.println(new Gson().toJson(gamePlayBean));
		SportsLotteryGamePlayControllerImpl gamePlayControllerImpl =new SportsLotteryGamePlayControllerImpl();
		gamePlayControllerImpl.purchaseTicketControllerImpl(gamePlayBean,userBean);
		
	}
	*/
	
	
	public static void main(String[] args) throws SLEException {
		//new SportsLotteryTerminalGamePlayAction().purchaseTicket();
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getGameTypeId() {
		return gameTypeId;
	}

	public void setGameTypeId(int gameTypeId) {
		this.gameTypeId = gameTypeId;
	}

	public String[] getDrawInfo() {
		return drawInfo;
	}

	public void setDrawInfo(String[] drawInfo) {
		this.drawInfo = drawInfo;
	}

	public int getDrawCount() {
		return drawCount;
	}

	public void setDrawCount(int drawCount) {
		this.drawCount = drawCount;
	}

	public double getTicketAmt() {
		return ticketAmt;
	}

	public void setTicketAmt(double ticketAmt) {
		this.ticketAmt = ticketAmt;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
