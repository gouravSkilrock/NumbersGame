<%@ page
	import="java.util.*,com.skilrock.lms.beans.PWTDrawBean,com.skilrock.lms.beans.DrawIdBean,com.skilrock.lms.beans.FortunePurchaseBean,com.skilrock.lms.beans.PanelIdBean,com.skilrock.lms.web.drawGames.common.Util,com.skilrock.lms.web.drawGames.common.UtilApplet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.GameBean"%>
<%@page import="com.skilrock.lms.beans.MainPWTDrawBean"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<% 
	List<String> sunSign = Arrays.asList("", "Zero(0)",
			"One(1)", "Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)",
			"Seven(7)", "Eight(8)", "Nine(9)");
			
			pageContext.setAttribute("gameName","Zerotonine");
%>
<%!int constantSize = 280;
	int pwtConstantSize = 150;
	List<Integer> pickNum = null;
	StringBuilder pickNumStr = null;
	StringBuilder nonWinStr = null;
	StringBuilder winStr = null;
	StringBuilder registerStr = null;
	StringBuilder claimStr = null;
	StringBuilder outVerifyStr = null;
	StringBuilder pndPayStr = null;
	StringBuilder resultAwaitedStr = null;
	StringBuilder drawAmtStr = null;
	StringBuilder drawDateTimeStr = null;
	StringBuilder betMulStr = null;
	int[] betMul = null;
	int totalQuantity = 0;
	int nonWin[], win[], register[], claim[], outVerify[], pndPay[];
	boolean isResAwaited[];
	String[] drawTime;
	int appletHeight = 0;
	int pwtHeightCounter=0;
	Integer buyModeHeight = 0;
	int regButtonHeight = 0;
	
	double totMainTktAmt = 0.0;
	%>

<%
	MainPWTDrawBean mainPWTBean = (MainPWTDrawBean) session.getAttribute("PWT_RES");
	StringBuilder finalData = new StringBuilder("");
	appletHeight = 0;
	totMainTktAmt = 0.0;
	totMainTktAmt = mainPWTBean.getTotlticketAmount();
	
	UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
	String verifiedAt = userInfoBean.getOrgName();
	
	boolean isReprint = mainPWTBean.isReprint();
	if (isReprint) {
		appletHeight = appletHeight + 50;
		FortunePurchaseBean bean = (FortunePurchaseBean) mainPWTBean.getPurchaseBean();
		String reprintData = "";
		pickNumStr = new StringBuilder("");
		betMulStr = new StringBuilder("");
		if (bean != null) {
			pickNum = bean.getPickedNumbers();
			betMul = bean.getBetAmountMultiple();
			if (pickNum != null) {
				for (int i = 0; i < pickNum.size(); i++) {
					pickNumStr.append(pickNum.get(i) + ",");
					betMulStr.append(betMul[i] + ",");
					totalQuantity = totalQuantity + betMul[i];
				}
			}

			buyModeHeight = bean.getNoOfDraws() * 11 + bean.getPickedNumbers().size() * 11;
			appletHeight = buyModeHeight + constantSize;
			
			try {
				if (Integer.parseInt(bean.getReprintCount()) > 0) {
					appletHeight = appletHeight + 16;
				}
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}

			String ticketExpiryEnabled = (String) application.getAttribute("TICKET_EXPIRY_ENABLED");
			if ("YES".equalsIgnoreCase(ticketExpiryEnabled)) {
				appletHeight = appletHeight + 11;
			}

			StringBuilder topMsgsStr = new StringBuilder(" ");
			StringBuilder bottomMsgsStr = new StringBuilder(" ");
			appletHeight = UtilApplet.getAdvMsgs(bean.getAdvMsg(), topMsgsStr, bottomMsgsStr, appletHeight);
			
			StringBuilder raffleData = new StringBuilder(" ");
			if (bean.getRafflePurchaseBeanList() != null) {
				appletHeight = UtilApplet.getRaffPurchaseData(bean.getRafflePurchaseBeanList(), raffleData, appletHeight);
			}
			System.out.println("**raffleData*****" + raffleData + "***");
			reprintData = "data=" + bean.getTicket_no() + bean.getReprintCount() 
								+ "|gameName=" + pageContext.getAttribute("gameName")
								+ "|mode=Buy|saleStatus=" + bean.getSaleStatus() 
								+ "|reprintCount=" + bean.getReprintCount() 
								+ "|purchaseTime=" + bean.getPurchaseTime() 
								+ "|gameDispName=" + bean.getGameDispName() 
								+ "|ticketNumber=" + bean.getTicket_no() 
								+ "|pickedNumbers=" + pickNumStr.toString() 
								+ "|betAmountMultiple=" + betMulStr.toString() 
								+ "|isQP=" + bean.getIsQP() 
								+ "|expiryPeriod=" + Util.getGameMap().get(Util.getGameName(bean.getGame_no())).getTicketExpiryPeriod() 
								+ "|noOfDraws=" + bean.getNoOfDraws() 
								+ "|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL")
								+ "|totalPurchaseAmt=" + bean.getTotalPurchaseAmt() 
								+ "|orgName=" + application.getAttribute("ORG_NAME_JSP")
								+ "|advtMsg=" + application.getAttribute("ADVT_MSG") 
								+ "|topAdvMsg=" + topMsgsStr
								+ "|bottomAdvMsg=" + bottomMsgsStr
								+ "|orgAddress=" + application.getAttribute("ORG_ADDRESS")
								+ "|orgMobile=" + application.getAttribute("ORG_MOBILE")
								+ "|ticketExpiryEnabled=" + ticketExpiryEnabled
								+ "|totalQuantity=" + totalQuantity + "|";
		}
		System.out.println("**reprintData*****" + reprintData + "***");
		finalData.append(reprintData);
	}

	List<PWTDrawBean> winningBeanList = mainPWTBean
			.getWinningBeanList();
	for (int k = 0; k < winningBeanList.size(); k++) {
		PWTDrawBean winningBean = winningBeanList.get(k);
		String pwtTktType = winningBean.getPwtTicketType();
		if ("DRAW".equalsIgnoreCase(pwtTktType)) {
			FortunePurchaseBean fortuneBean = (FortunePurchaseBean) winningBean.getPurchaseBean();


	pickNum = null;
	betMul = null;
	
	appletHeight = 0;
	buyModeHeight=0;
	pwtHeightCounter=0;
	
	pickNumStr = new StringBuilder("");
	nonWinStr = new StringBuilder("");
	winStr = new StringBuilder("");
	registerStr = new StringBuilder("");
	claimStr = new StringBuilder("");
	outVerifyStr = new StringBuilder("");
	pndPayStr = new StringBuilder("");
	resultAwaitedStr = new StringBuilder("");
	drawAmtStr = new StringBuilder("");
	drawDateTimeStr = new StringBuilder("");
	betMulStr = new StringBuilder("");
	totalQuantity = 0;
	int winSize = winningBean.getDrawWinList().size();
	nonWin = new int[winSize];
	win = new int[winSize];
	register = new int[winSize];
	claim = new int[winSize];
	outVerify = new int[winSize];
	pndPay = new int[winSize];
	double drawAmt[] = new double[winSize];
	isResAwaited = new boolean[winSize];
	double totTktAmt = 0.0;
	int totRegister = 0;
	//int drawandPanelCtr = 0;
	regButtonHeight = 0;
	

	if (fortuneBean != null) {
		pickNum = fortuneBean.getPickedNumbers();
		betMul = fortuneBean.getBetAmountMultiple();
		if (pickNum != null) {
			for (int i = 0; i < pickNum.size(); i++) {
				pickNumStr.append(pickNum.get(i) + ",");
				betMulStr.append(betMul[i] + ",");
				totalQuantity = totalQuantity + betMul[i];
			}
		}

		buyModeHeight = fortuneBean.getNoOfDraws() * 11
				+ fortuneBean.getPickedNumbers().size() * 11;
		//appletHeight = buyModeHeight + constantSize
			//	+ (pickNumStr.toString().split(";").length * 11);
			appletHeight=buyModeHeight+constantSize;
	}
	int betMultiple = 0;
	for (int i = 0; i < winningBean.getDrawWinList().size(); i++) {
	
		pwtHeightCounter=pwtHeightCounter+17;
		DrawIdBean drawBean = winningBean.getDrawWinList().get(i);
		drawTime = drawBean.getDrawDateTime().split(" ");
		drawDateTimeStr.append(drawBean.getDrawDateTime() + ",");
		List<PanelIdBean> panelWinList = drawBean.getPanelWinList();
		//drawandPanelCtr += 2;
		if (panelWinList != null) {
			//System.out.println("aaaa");
			for (int j = 0; j < panelWinList.size(); j++) {
				PanelIdBean panelBean = panelWinList.get(j);
				betMultiple = panelBean.getBetAmtMultiple();
				if (panelBean.getStatus().equals("NON WIN")) {
					nonWin[i] += betMultiple;
				} else if (panelBean.getStatus().equals("NORMAL_PAY")) {
					drawAmt[i] = panelBean.getWinningAmt()
							+ drawAmt[i];
					win[i] += betMultiple;
				} else if (panelBean.getStatus().equals("CLAIMED")) {
					claim[i] += betMultiple;
				} else if (panelBean.getStatus().equals("PND_PAY")) {
					pndPay[i] += betMultiple;
				} else if (panelBean.getStatus().equals("HIGH_PRIZE")) {
					register[i] += betMultiple;
				} else if (panelBean.getStatus()
						.equals("OUT_PAY_LIMIT")) {
					register[i] += betMultiple;
				} else if (panelBean.getStatus().equals(
						"OUT_VERIFY_LIMIT")) {
					outVerify[i] += betMultiple;
				}
			}
			nonWinStr.append(String.valueOf(nonWin[i]) + ",");
			drawAmtStr.append(drawAmt[i] + ",");
			winStr.append(win[i] + ",");
			claimStr.append(claim[i] + ",");
			pndPayStr.append(pndPay[i] + ",");
			registerStr.append(register[i] + ",");
			outVerifyStr.append(outVerify[i] + ",");
			isResAwaited[i] = false;
			resultAwaitedStr.append(isResAwaited[i] + ",");
			totRegister = totRegister + register[i];
		} else {
			nonWinStr.append("NA" + ",");
			drawAmtStr.append("NA" + ",");
			winStr.append("NA" + ",");
			claimStr.append("NA" + ",");
			pndPayStr.append("NA" + ",");
			registerStr.append("NA" + ",");
			outVerifyStr.append("NA" + ",");
			isResAwaited[i] = true;
			resultAwaitedStr.append(isResAwaited[i] + ",");
			pwtHeightCounter=pwtHeightCounter+11;
		}
		totTktAmt = drawAmt[i] + totTktAmt;
		if (nonWin[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter=pwtHeightCounter+11;
		}

		if (win[i] > 0 || claim[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter=pwtHeightCounter+11;
		}

		if (pndPay[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter=pwtHeightCounter+11;
		}

		if (register[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter=pwtHeightCounter+11;
			regButtonHeight = 22;
		}

		if (outVerify[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter=pwtHeightCounter+11;
		}
		//System.out.println(drawandPanelCtr+"******************"+i);
	}
%>



		
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<script src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/commonRPOS.js"></script>
<%
	String barcodeType = (String)application.getAttribute("BARCODE_TYPE");	
			//System.out.println("winningBean *****************"+winningBean);
	if(!barcodeType.equals("applet")){ %>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/terminal.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/LMSImages/css/newstyle.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/LMSImages/css/gameStyle.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/LMSImages/css/styleDraw.css" />
		<s:head theme="ajax" debug="false" />
	</head>
	<script>parent.isPrintable=true;document.oncontextmenu=new Function("return false");</script>
	<script language='VBScript'>
Sub Print()
       OLECMDID_PRINT = 6
       OLECMDEXECOPT_DONTPROMPTUSER = 2
       OLECMDEXECOPT_PROMPTUSER = 1
       call WB.ExecWB(OLECMDID_PRINT, OLECMDEXECOPT_DONTPROMPTUSER,1)
End Sub
document.write "<object ID='WB' WIDTH=0 HEIGHT=0 CLASSID='CLSID:8856F961-340A-11D0-A96B-00C04FD705A2'></object>"
</script>
	<body>	
		<div id="maincontainer">
			<span class="small">**********************************</span>
			<div class="large">
				WINLOT Limited
			</div>
			
			<div class="drawgame_name"> ZERO TO NINE</div>
			<div class="largeunbold">
				Winning Verification
			</div>
			<div class="ticket">
				TicNo :
				<%=winningBean.getTicketNo()%><%=winningBean.getReprintCount()%></div>

			<%
				
				if (!winningBean.isValid()) {
			%>
			<div class="added">
				Invalid Ticket
			</div>
			<script>parent.isPrintable=false;</script>
			<%
				} else if (winningBean.getStatus().equals("SUCCESS")) {

					for (int i = 0; i < winningBean.getDrawWinList().size(); i++) {						
						if (isResAwaited[i]) {
			%>
			<div id="drawnumbers">
				<span class="date"><%=drawTime[0]%></span><span class="time"><%=drawTime[1]%></span>
			</div>
			<div id="drawnumbers">
				<span class="ttlpay">Result</span><span class="amt">Awaited</span>
			</div>
			<%
				} else {
			%><div id="drawnumbers">
				<span class="date"><%=drawTime[0]%></span><span class="time"><%=drawTime[1]%></span>
			</div>
			<%
				if (nonWin[i] != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=nonWin[i]%> Panel (s)</span><span class="amt">TRY
					AGAIN</span>
			</div>
			<%
				}
							if (win[i] != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=win[i]%> Panel (s)</span><span class="amt">WIN
					<%=drawAmt[i]%> <s:property value="%{#application.CURRENCY_SYMBOL}" />
				</span>
			</div>
			<%
				}
							if (register[i] != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=register[i]%> Panel (s)</span><span class="amt">REG.
					REQ.</span>
			</div>
			<%
				}
							if (claim[i] != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=claim[i]%> Panel (s)</span><span class="amt">CLAIMED</span>
			</div>
			<%
				}
							if (pndPay[i] != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=pndPay[i]%> Panel (s)</span><span class="amt">IN
					PROCESS</span>
			</div>
			<%
				}
							if (outVerify[i] != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=outVerify[i]%> Panel (s)</span><span
					class="amt">OUT OF VERIFY</span>
			</div>
			<%
				}
			%><div id="drawnumbers">
				<span class="small">-----------------------------------------</span>
			</div>
			<%
				}
						
					}
				} else if (winningBean.getStatus().equals("ERROR")) {
			%>
			<div id="drawnumbers">
				<span class="ttlpay"></span><span class="amt">ERROR! Please
					Try Again..</span>
			</div>
			<%
				}
			%><div id="drawnumbers">
				<span class="ttlpay">Total Pay</span><span class="amt"><%=totTktAmt%></span>
			</div>
			<div id="drawnumbers">
				<span class="small">**********************************</span>
			</div>
		</div>
		<div>
			<%
				System.out.println(winningBean.isReprint()
						+ "****ISREFPRINT*********");
				if (winningBean.isReprint()) {					
					if (fortuneBean != null) {
						//System.out.println("fortuneBean "+fortuneBean);
			%>
			<div id="maincontainer">
				<span class="small">*********************************</span>
				<div class="large">
					WINLOT Limited
				</div>
				<div class="drawgame_name">
					CARD16
				</div>
				<span class="small">Re-Print</span>
				<%
					//System.out.println("Date time****"+pageContext.getAttribute("purchaseTime"));
							String[] pTime = fortuneBean.getPurchaseTime().toString()
									.split(" ");							
				%>
				<span class="date"><%=pTime[0]%></span><span class="time"><%=pTime[1].replace(".0", "")%></span>
				<div class="ticket">
					TicNo :
					<%=fortuneBean.getTicket_no()%><%=fortuneBean.getReprintCount()%></div>
				<span class="small">----------------------------------------</span>
				<div id="drawnumbers">
					<span class="ttlpay">Draw Date</span><span class="amt">Draw
						Time</span>
				</div>
				<%
					for (int i = 0; i < fortuneBean.getDrawDateTime().size(); i++) {
								String[] date = ((String) fortuneBean.getDrawDateTime()
										.get(i)).split(" ");
				%>
				<div id="drawnumbers">
					<span class="ttlpay"><%=date[0]%></span><span class="amt"><%=date[1]%></span>
				</div>
				<%
					}
				%>
				<span class="small">----------------------------------------</span>
				<%
					List<Integer> pickNum = (List<Integer>) fortuneBean.getPickedNumbers();
					int totQuantity = 0;
					int[] betMul = (int [])fortuneBean.getBetAmountMultiple();
					for (int i = 0; i < pickNum.size(); i++) {
					totQuantity=totQuantity+betMul[i];
				%>
				<div id="drawnumbers">
					<span class="sinsign"><%=sunSign.get((pickNum.get(i)))%></span><span class="drawno"><%=betMul[i]%></span>
				</div>
				<%
							}
				%>
				<div class="small">
					----------------------------------------
				</div>
				<div id="drawnumbers">
					<span class="sinsign">Total Quantity</span><span
						class="drawno"><%=totQuantity%></span>
				</div>
				<div id="drawnumbers">
					<span class="sinsign">No. Of Draws</span><span class="drawno"><%=fortuneBean.getNoOfDraws()%></span>
				</div>
				<div id="drawnumbers">
					<span class="sinsign">Total Amount (<s:property
							value="%{#application.CURRENCY_SYMBOL}" />)</span><span class="drawno"><%=fortuneBean.getTotalPurchaseAmt()%></span>
				</div>
			
				<div class="center">
					<img
						src='<%=request.getContextPath()%>/barcode/<%=fortuneBean.getTicket_no()%><%=fortuneBean.getReprintCount()%>.jpg'></img>
				</div>
				
				<span class="clear"></span>
				<span class="small">*********************************</span>
			</div>
			<%
				}
				}
			%>
		</div>
		<%
		}else{
		%>
		<div class="center">	
    		<%
	   	   			StringBuilder topMsgsStrPwt = new StringBuilder(" ");
					StringBuilder bottomMsgsStrPwt = new StringBuilder(" ");
					appletHeight = UtilApplet.getAdvMsgs(mainPWTBean.getAdvMsg(), topMsgsStrPwt, bottomMsgsStrPwt, appletHeight);	
					
	   	   			String pwtData = "";
					if(!isReprint){
						pwtData = "data=" + winningBean.getTicketNo() + winningBean.getReprintCount() + "|";
					}

					

					pwtData = pwtData + "reprintCountPWT=" + winningBean.getReprintCount()
						+ "|mode=PWT|orgName=" + application.getAttribute("ORG_NAME_JSP")
   	   					+ "|advtMsg=" + application.getAttribute("ADVT_MSG") 
   	   					+ "|isValid=" + String.valueOf(winningBean.isValid())
   	   					+ "|isReprint=" + String.valueOf(isReprint)
   	   					+ "|gameName="+pageContext.getAttribute("gameName")
   	   					+ "|gameDispName=" + winningBean.getGameDispName() 
   	   					+ "|ticketNumber=" + winningBean.getTicketNo() 
   	   					+ "|drawDateTime=" + drawDateTimeStr.toString()  
   	   					+ "|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL") 
   	   					+ "|status=" + winningBean.getStatus()
   	   					+ "|topAdvMsgPwt=" + topMsgsStrPwt
						+ "|bottomAdvMsgPwt=" + bottomMsgsStrPwt
   	   					+ "|nonWinStr=" + nonWinStr.toString() 
   	   					+ "|winStr=" + winStr.toString() 
   	   					+ "|registerStr=" + registerStr.toString() 
   	   					+ "|claimStr=" + claimStr.toString() 
   	   					+ "|outVerifyStr=" + outVerifyStr.toString() 
   	   					+ "|pndPayStr=" + pndPayStr.toString() 
   	   					+ "|retailerName=" + Util.getOrgNameFromTktNo((winningBean.getTicketNo() + winningBean.getReprintCount()), (String)application.getAttribute("ORG_TYPE_ON_TICKET"))
   	   					+ "|verifiedAt=" + verifiedAt
   	   					+ "|resultAwaitedStr=" + resultAwaitedStr.toString() 
   	   					+ "|drawAmtStr=" + drawAmtStr.toString();
   	   			System.out.println("**pwtData*****" + pwtData + "***");
				finalData.append(pwtData);
	    	   	%>
	    	   
		</div>
		<%}%>
		<%
				if (totRegister != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=totRegister%> Panel (s)</span><span
					class="amt">REG. REQ.</span>
			</div>
			<script>parent.showRegButton();</script>
			<div id="drawnumbers">
				<span class="ttlpay"></span><span class="amt"><input
						type="button" value="Register" onclick="parent.regPlr()" /> </span>
			</div>
			<%
				}
			%>
			<%} else if ("RAFFLE".equalsIgnoreCase(pwtTktType)) {
					StringBuilder raffleData = new StringBuilder(" ");
					if (winningBean.getRaffleDrawIdBeanList() != null) {
						appletHeight = UtilApplet.getRaffPWTData(winningBean.getRaffleDrawIdBeanList(), raffleData,appletHeight);
					}
					finalData.append("|raffPWTData=" + raffleData);
				}
			}

			finalData.append("|verifiedAt=" + verifiedAt + "|totMainTktAmt=" + totMainTktAmt + "|ctr=" + (pwtConstantSize + pwtHeightCounter + appletHeight + regButtonHeight + 50));

			System.out.println("ZEROTONINE Winning DATA: " + finalData.toString());
		%>
		<script>setAppData('<%=finalData.toString()%>');</script>
	</body>
<script>parent.updBalIframe();</script></html>


<code id="headId" style="visibility: hidden">
$RCSfile: zeroToNinePwtResult.jsp,v $
$Revision: 1.1.2.11.4.2.2.15 $
</code>