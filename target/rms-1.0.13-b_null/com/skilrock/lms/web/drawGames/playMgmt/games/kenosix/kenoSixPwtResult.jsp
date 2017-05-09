<%@ page
	import="java.util.*,com.skilrock.lms.dge.beans.PWTDrawBean,com.skilrock.lms.dge.beans.DrawIdBean,com.skilrock.lms.dge.beans.PanelIdBean,com.skilrock.lms.web.drawGames.common.Util,com.skilrock.lms.web.drawGames.common.UtilApplet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.dge.beans.KenoPurchaseBean"%>
<%@page import="com.skilrock.lms.dge.beans.MainPWTDrawBean"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@page import="com.skilrock.lms.common.filter.LMSFilterDispatcher"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%!int constantSize = 250;
	int pwtConstantSize = 150;
	List<Integer> pickNum = null;
	StringBuilder pickNumStr = null;
	StringBuilder nonWinStr = null;
	StringBuilder winStr = null;
	StringBuilder clmStr = null;
	StringBuilder registerStr = null;
	StringBuilder claimStr = null;
	StringBuilder outVerifyStr = null;
	StringBuilder pndPayStr = null;
	StringBuilder resultAwaitedStr = null;
	StringBuilder drawAmtStr = null;
	StringBuilder drawDateTimeStr = null;
	StringBuilder betMulStr = null;
	StringBuilder clmPendAmtStr=null;
	StringBuilder verPendStr=null;
	StringBuilder isDrawExpiredStr=null;
	int nonWin[], win[], register[], claim[], outVerify[], pndPay[],clm[],verPend[];
	boolean isResAwaited[];
	boolean isVerPending[];
	boolean isDrawExpired[];
	String[] drawTime;
	int appletHeight = 0;
	Integer buyModeHeight = 0;
	int drawandPanelCtr = 0;
	int pwtHeightCounter = 0;
	int regButtonHeight = 0;
	double totMainTktAmt = 0.0;
	%>

<%
	pageContext.setAttribute("gameName", "Keno");
	
	MainPWTDrawBean mainPWTBean = (MainPWTDrawBean) session.getAttribute("PWT_RES");
	StringBuilder finalData = new StringBuilder("");
	totMainTktAmt = mainPWTBean.getTotlticketAmount();
	appletHeight = 0;
	buyModeHeight = 0;
	pwtHeightCounter = 0;
	regButtonHeight = 0;
	boolean isReprint = mainPWTBean.isReprint();
	drawandPanelCtr = 0;
	pickNumStr = new StringBuilder("");
	nonWinStr = new StringBuilder("");
	winStr = new StringBuilder("");
	clmStr = new StringBuilder("");
	registerStr = new StringBuilder("");
	claimStr = new StringBuilder("");
	outVerifyStr = new StringBuilder("");
	pndPayStr = new StringBuilder("");
	resultAwaitedStr = new StringBuilder("");
	isDrawExpiredStr = new StringBuilder("");
	verPendStr = new StringBuilder("");
	drawAmtStr = new StringBuilder("");
	drawDateTimeStr = new StringBuilder("");
	clmPendAmtStr =new StringBuilder("");
	
	UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
	String verifiedAt = userInfoBean.getOrgName();
	
	StringBuilder tempRecptData = new StringBuilder("");
	StringBuilder isTempRcpt = new StringBuilder("false");
	appletHeight = UtilApplet.getTempReceiptData(session, tempRecptData, isTempRcpt, appletHeight);
	
	if (isReprint) {
		appletHeight = appletHeight + 50;
		KenoPurchaseBean kenoBean = (KenoPurchaseBean) mainPWTBean.getPurchaseBean();
		String reprintData = "";
		pickNumStr = new StringBuilder("");
		betMulStr = new StringBuilder("");
		if (kenoBean != null) {
		StringBuilder topMsgsStr = new StringBuilder(" ");
		StringBuilder bottomMsgsStr = new StringBuilder(" ");
		appletHeight = UtilApplet.getAdvMsgs(kenoBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, appletHeight);
				int noOfDraws = kenoBean.getNoOfDraws();
				
				String[] playerDataApp = kenoBean.getPlayerData();
				String[] playType = kenoBean.getPlayType();
				int[] noOfLines = kenoBean.getNoOfLines();
				double[] unitPrice = kenoBean.getUnitPrice();
				int[] betAmtMul = kenoBean.getBetAmountMultiple();
				int[] isQP = kenoBean.getIsQuickPick();
				double[] panelPrice = new double[playType.length];
				StringBuilder combDispStr = new StringBuilder(" ");
				
				StringBuilder playerDataStr = new StringBuilder(" ");
				StringBuilder playTypeStr = new StringBuilder(" ");
				StringBuilder noOfLinesStr = new StringBuilder(" ");
				StringBuilder unitPriceStr = new StringBuilder(" ");
				StringBuilder isQPStr = new StringBuilder(" ");
				StringBuilder panelPriceStr = new StringBuilder(" ");
				
				if(playerDataApp.length > 0){
					playerDataStr.deleteCharAt(playerDataStr.length() -1);
					playTypeStr.deleteCharAt(playTypeStr.length() -1);
					noOfLinesStr.deleteCharAt(noOfLinesStr.length() -1);
					unitPriceStr.deleteCharAt(unitPriceStr.length() -1);
					isQPStr.deleteCharAt(isQPStr.length() -1);
					panelPriceStr.deleteCharAt(panelPriceStr.length() -1);
					combDispStr.deleteCharAt(combDispStr.length() -1);
					
					for (int panelId = 0; panelId < playerDataApp.length; panelId++) {
						playerDataStr.append(playerDataApp[panelId] + "~");
						playTypeStr.append(playType[panelId] + "~");
						noOfLinesStr.append(noOfLines[panelId] + "~");
						unitPriceStr.append((unitPrice[panelId] * betAmtMul[panelId]) + "~");
						isQPStr.append(isQP[panelId] + "~");
						panelPriceStr.append((unitPrice[panelId] * betAmtMul[panelId] * noOfLines[panelId] * noOfDraws) + "~");
						
						//---
						if("YES".equalsIgnoreCase((String)application.getAttribute("PERM_COMB_DISP"))){
							List<String> comboList = new ArrayList<String>();
							String compDispTemp = null;
							
							if("Perm2".equalsIgnoreCase(playType[panelId])){
								comboList = Util.rec(0, 2, 0, new String[2], playerDataApp[panelId].split(","), new StringBuffer(""), comboList);
								compDispTemp = comboList.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",Nxt,", "Nxt").replace(",Nxt", "");
								appletHeight = appletHeight + (5*comboList.size()/4);
							} else if("Perm3".equalsIgnoreCase(playType[panelId])){
								comboList = Util.rec(0, 3, 0, new String[3], playerDataApp[panelId].split(","), new StringBuffer(""), comboList);
								compDispTemp = comboList.toString().replace("[", "").replace("]", "").replace(" ", "").replace(",Nxt,", "Nxt").replace(",Nxt", "");
								appletHeight = appletHeight + (2*comboList.size()/2);
							}
							if(compDispTemp == null){
								compDispTemp = "NA";
							}
							
							combDispStr.append(compDispTemp + "~");
						} else {
							combDispStr.append("NA" + "~");
						}
						//---
						
						appletHeight = appletHeight + 66;
					}
									
					playerDataStr.deleteCharAt(playerDataStr.length() -1);
					playTypeStr.deleteCharAt(playTypeStr.length() -1);
					noOfLinesStr.deleteCharAt(noOfLinesStr.length() -1);
					unitPriceStr.deleteCharAt(unitPriceStr.length() -1);
					isQPStr.deleteCharAt(isQPStr.length() -1);
					panelPriceStr.deleteCharAt(panelPriceStr.length() -1);
					combDispStr.deleteCharAt(combDispStr.length() -1);
				}
				
				
			//pickNum = bean.getPickedNumbers();
			//betMul = bean.getBetAmountMultiple();
			if (pickNum != null) {
				for (int i = 0; i < pickNum.size(); i++) {
					pickNumStr.append(pickNum.get(i) + ",");
					//betMulStr.append(betMul[i] + ",");
					//totalQuantity = totalQuantity + betMul[i];
				}
			}

			buyModeHeight = kenoBean.getNoOfDraws() * 11 + kenoBean.getPlayerData().length * 11;
			appletHeight = appletHeight + buyModeHeight + constantSize;
			
			try {
				if (Integer.parseInt(kenoBean.getReprintCount()) > 0) {
					appletHeight = appletHeight + 16;
				}
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}

			String ticketExpiryEnabled = (String) application.getAttribute("TICKET_EXPIRY_ENABLED");
			if ("YES".equalsIgnoreCase(ticketExpiryEnabled)) {
				appletHeight = appletHeight + 11;
			}

			StringBuilder raffleData = new StringBuilder(" ");
			if (kenoBean.getRafflePurchaseBeanList() != null) {
				appletHeight = UtilApplet.getRaffPurchaseData(kenoBean.getRafflePurchaseBeanList(), raffleData, appletHeight);
			}
			System.out.println("**raffleData*****" + raffleData + "***"+kenoBean.getBarcodeCount()+ " *****" + (kenoBean.getBarcodeCount()!=-1 && LMSFilterDispatcher.isBarCodeRequired?kenoBean.getBarcodeCount():""));
			reprintData = "data=" + kenoBean.getTicket_no() + kenoBean.getReprintCount() + (kenoBean.getBarcodeCount()!=-1 && LMSFilterDispatcher.isBarCodeRequired?kenoBean.getBarcodeCount():"")
								+ "|gameName=" + pageContext.getAttribute("gameName")
								+ "|mode=PWT|saleStatus=" + kenoBean.getSaleStatus()
								+ "|reprintCount=" + kenoBean.getReprintCount()
								+ "|purchaseTime=" + kenoBean.getPurchaseTime()
								+ "|gameDispName=" + kenoBean.getGameDispName()
								+ "|ticketNumber=" + kenoBean.getTicket_no()
								+ "|pickedNumbers=" + playerDataStr
								+ "|combDispStr=" + combDispStr
								+ "|isQP=" + isQPStr
								+ "|expiryPeriod=" + Util.getGameMap().get(kenoBean.getGameId()).getTicketExpiryPeriod() 
								+ "|noOfDraws=" + noOfDraws 
								+ "|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL")
								+ "|totalPurchaseAmt=" + kenoBean.getTotalPurchaseAmt() 
								+ "|orgName=" + application.getAttribute("ORG_NAME_JSP")
								+ "|advtMsg=" + application.getAttribute("ADVT_MSG")
								+ "|topAdvMsg=" + topMsgsStr
								+ "|bottomAdvMsg=" + bottomMsgsStr
								+ "|orgAddress=" + application.getAttribute("ORG_ADDRESS")
								+ "|orgMobile=" + application.getAttribute("ORG_MOBILE")
								+ "|ticketExpiryEnabled=" + application.getAttribute("TICKET_EXPIRY_ENABLED")
								+ "|totalQuantity=" + noOfLinesStr
								+ "|playType=" + playTypeStr
								+ "|unitPriceStr=" + unitPriceStr
								+ "|panelPriceStr=" + panelPriceStr + "|";
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


	pickNum = null;
	KenoPurchaseBean kenoBean = (KenoPurchaseBean) winningBean
			.getPurchaseBean();
	String playerData[] = null;
	String pickData = null;
	if (kenoBean != null) {
	playerData = kenoBean.getPlayerData();
	
	}
	int winSize = winningBean.getDrawWinList().size();
	nonWin = new int[winSize];
	win = new int[winSize];
	register = new int[winSize];
	claim = new int[winSize];
	outVerify = new int[winSize];
	pndPay = new int[winSize];
	clm = new int[winSize];
	verPend=new int[winSize];
	double drawAmt[] = new double[winSize];
	double clmPendAmt[]=new double[winSize];
	isResAwaited = new boolean[winSize];
	isVerPending = new boolean[winSize];
	isDrawExpired = new boolean[winSize];
	double totTktAmt = 0.0;
	int totRegister = 0;

	

	
%>
<%
	for (int i = 0; i < winningBean.getDrawWinList().size(); i++) {
		pwtHeightCounter = pwtHeightCounter + 17;
		DrawIdBean drawBean = winningBean.getDrawWinList().get(i);
		String drawStatusForTicket=drawBean.getStatus();
		drawTime = drawBean.getDrawDateTime().split(" ");
		drawDateTimeStr.append(drawBean.getDrawDateTime() + ",");
		List<PanelIdBean> panelWinList = drawBean.getPanelWinList();
		//drawandPanelCtr += 2;
		if (panelWinList != null && !drawStatusForTicket.equals("DRAW_EXPIRED")) {
			//System.out.println("aaaa");
		drawAmt[i]=0.0;
		clmPendAmt[i]=0.0;
			for (int j = 0; j < panelWinList.size(); j++) {
				drawandPanelCtr++;
				PanelIdBean panelBean = panelWinList.get(j);
				System.out.print("PNL STATUS" + panelBean.getStatus());
				//System.out.println(panelBean);
				//System.out.println(panelBean.getStatus());
				if (panelBean.getStatus().equals("NON WIN")) {
					nonWin[i]++;
				} else if (panelBean.getStatus().equals("NORMAL_PAY")) {
					drawAmt[i] = panelBean.getWinningAmt()
							+ drawAmt[i];
					win[i]++;
				} else if (panelBean.getStatus().equals("CLAIM_PENDING")) {
					clmPendAmt[i] = panelBean.getWinningAmt()
							+ clmPendAmt[i];
					clm[i]++;
				}else if (panelBean.getStatus().equals("CLAIMED")) {
					claim[i]++;
				}else if (panelBean.getStatus().equals("PND_PAY")) {
					pndPay[i]++;
				} else if (panelBean.getStatus().equals("HIGH_PRIZE")) {
					register[i]++;
				} else if (panelBean.getStatus()
						.equals("OUT_PAY_LIMIT")) {
					register[i]++;
				} else if (panelBean.getStatus().equals(
						"OUT_VERIFY_LIMIT")) {
					outVerify[i]++;
				}
			}
			
			System.out.print("CLM AMT" + clmPendAmt[i]);
			System.out.print("DRAW AMT" + drawAmt[i]);
			nonWinStr.append(String.valueOf(nonWin[i]) + ",");
			drawAmtStr.append(Util.getBalInString(drawAmt[i]) + ",");
			clmPendAmtStr.append(Util.getBalInString(clmPendAmt[i]) + ",");
			winStr.append(win[i] + ",");
			clmStr.append(clm[i] + ",");
			claimStr.append(claim[i] + ",");
			pndPayStr.append(pndPay[i] + ",");
			registerStr.append(register[i] + ",");
			outVerifyStr.append(outVerify[i] + ",");
			isResAwaited[i] = false;
			isVerPending[i] = false;
			isDrawExpired[i]=false;
			resultAwaitedStr.append(isResAwaited[i] + ",");
			verPendStr.append(isVerPending[i] + ",");
			isDrawExpiredStr.append(isDrawExpired[i] + ",");
			totRegister = totRegister + register[i];
			System.out.println(isResAwaited[i] +"RA "+isVerPending[i]+" VP"+ isDrawExpired+"DE");
		} else {
			nonWinStr.append("NA" + ",");
			drawAmtStr.append("NA" + ",");
			clmPendAmtStr.append("NA" + ",");
			winStr.append("NA" + ",");
			clmStr.append("NA" + ",");
			claimStr.append("NA" + ",");
			pndPayStr.append("NA" + ",");
			registerStr.append("NA" + ",");
			outVerifyStr.append("NA" + ",");
			isResAwaited[i] = false;
			isVerPending[i] = false;
			isDrawExpired[i]= false;
			
			if(drawStatusForTicket.equals("VERIFICATION_PENDING"))
					isVerPending[i] = true;
			else if(drawStatusForTicket.equals("DRAW_EXPIRED"))
					isDrawExpired[i]= true;
			else		
					isResAwaited[i] = true;
			resultAwaitedStr.append(isResAwaited[i] + ",");
			verPendStr.append(isVerPending[i] + ",");
			isDrawExpiredStr.append(isDrawExpired[i] + ",");
			pwtHeightCounter = pwtHeightCounter + 11;
			System.out.println(isResAwaited[i] +"RA "+isVerPending[i]+" VP"+ isDrawExpired+"DE");
		}
		totTktAmt = drawAmt[i] + totTktAmt;

		if (nonWin[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter = pwtHeightCounter + 11;
		}

		if (win[i] > 0 || claim[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter = pwtHeightCounter + 11;
		}

		if (clm[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter = pwtHeightCounter + 11;
		}

		if (pndPay[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter = pwtHeightCounter + 11;
		}

		if (register[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter = pwtHeightCounter + 11;
			regButtonHeight = 22;
		}

		if (outVerify[i] > 0) {
			//drawandPanelCtr += 2;
			pwtHeightCounter = pwtHeightCounter + 11;
		}

	}
%>

<%
	if (kenoBean != null) {
%>
<s:set name="kenoBean" value="%{#session.PWT_RES.purchaseBean}" />
<s:iterator value="%{#kenoBean.pickedNumbers}">
	<s:set name="pickedNumber" value="%{toString()}" />
	<%
		pickNumStr.append(pageContext.getAttribute("pickedNumber")
						.toString()
						+ ",");
	%>
</s:iterator>
<s:set name="buyModeHeight" value="%{#kenoBean.drawDateTime.size()*11}" />
<%
	buyModeHeight = (Integer) pageContext
				.getAttribute("buyModeHeight");
		//appletHeight = buyModeHeight + constantSize
		//	+ (pickNumStr.toString().split(";").length / 7 * 11);

		
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<script
		src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/commonRPOS.js"></script>
	<%
		String barcodeType = (String) application
				.getAttribute("BARCODE_TYPE");
		//System.out.println("winningBean *****************"+winningBean);
		if (!barcodeType.equals("applet")) {
	%>
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
	<script
		src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/commonRPOS.js"></script>
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

		<%
			//System.out.println("winningBean *****************"+winningBean);
		%>
		<div id="maincontainer">
			<span class="small">**********************************</span>
			<div class="large">
				WINLOT Limited
			</div>

			<div class="drawgame_name">
				KENO
			</div>
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
				<span class="ttlpay"><%=nonWin[i]%> Panel (s)</span><span
					class="amt">TRY AGAIN</span>
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
				<span class="ttlpay"><%=register[i]%> Panel (s)</span><span
					class="amt">REG. REQ.</span>
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
				<span class="ttlpay"><%=pndPay[i]%> Panel (s)</span><span
					class="amt">IN PROCESS</span>
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
						if (kenoBean != null) {
							//System.out.println("fortuneBean "+fortuneBean);
			%>
			<div id="maincontainer">
				<span class="small">*********************************</span>
				<div class="large">
					WINLOT Limited
				</div>
				<div class="drawgame_name">
					KENO
				</div>
				<span class="small">Re-Print</span>
				<%
					System.out.println("Date time****"
										+ kenoBean.getPurchaseTime().toString());
								String[] pTime = kenoBean.getPurchaseTime().toString()
										.split(" ");
				%>
				<span class="date"><%=pTime[0]%></span><span class="time"><%=pTime[1].replace(".0", "")%></span>
				<div class="ticket">
					TicNo :
					<%=kenoBean.getTicket_no()%><%=kenoBean.getReprintCount()%></div>
				<span class="small">----------------------------------------</span>
				<div id="drawnumbers">
					<span class="ttlpay">Draw Date</span><span class="amt">Draw
						Time</span>
				</div>
				<%
					for (int i = 0; i < kenoBean.getDrawDateTime().size(); i++) {
									String[] date = ((String) kenoBean
											.getDrawDateTime().get(i)).split(" ");
				%>
				<div id="drawnumbers">
					<span class="ttlpay"><%=date[0]%></span><span class="amt"><%=date[1]%></span>
				</div>
				<%
					}
				%>
				<span class="small">----------------------------------------</span>
				<%
			//if(playerData.indexOf("-1")!=-1)
					//	pickData=playerData.substring(0, playerData.indexOf("-1")-1);
					//else 
					//	pickData=playerData;			
					String playType[] = ((KenoPurchaseBean) request
							.getAttribute("kenoPurchaseBean")).getPlayType();
					int[] noOfLines = ((KenoPurchaseBean) request
							.getAttribute("kenoPurchaseBean")).getNoOfLines();
					double unitPrice[] = ((KenoPurchaseBean) request
							.getAttribute("kenoPurchaseBean")).getUnitPrice();
					int[] betAmtMul = ((KenoPurchaseBean) request
							.getAttribute("kenoPurchaseBean")).getBetAmountMultiple();
					int noOfDraw=((KenoPurchaseBean) request.getAttribute("kenoPurchaseBean")).getNoOfDraws();
					String tempData = "";
					StringBuilder newData = new StringBuilder("");

					//<div id="drawnumbers"><span class="sinsign">Total No. of Line(s)</span><span class="drawno"><s:property value="%{kenoPurchaseBean.noOfLines}" /></span></div>
					for (int panelId = 0; panelId < playerData.length; panelId++) {
						pickData = "";
						newData
								.append("<div id='drawnumbers'><span class='sinsign'><b>"
										+ playType[panelId] + "</b></span>");
						if ("Banker".equalsIgnoreCase(playType[panelId])) {
							pickData = "<u>Upper Line</u><br/>";
							pickData += playerData[panelId].substring(0,
									playerData[panelId].indexOf(",UL"))
									+ "<br/><u>Below Line</u><br/>";
							tempData = playerData[panelId].substring(
									playerData[panelId].indexOf(",UL,") + 4,
									playerData[panelId].indexOf(",BL"));
						} else {
							tempData = playerData[panelId];
						}
						pickData = pickData + tempData;
						if (pickData.length() > 21 && !"Banker".equalsIgnoreCase(playType[panelId])) {
							String tempPickData = "";
							int i = 0;
							for (int x = 0; x < pickData.length() / 21; x++) {
								tempPickData = tempPickData
										+ pickData.substring(i, i + 21)
										+ "<br/>";
								i = i + 21;
							}
							tempPickData = tempPickData
									+ pickData.substring(i, pickData.length());
							pickData = tempPickData;
						}
						newData.append("<br/>" + pickData + "<br/></div>");
						newData
								.append("<div id='drawnumbers'><span class='sinsign'>No. of Line(s)</span><span class='drawno'>"
										+ noOfLines[panelId]*betAmtMul[panelId] + "</span></div>");
						newData
								.append("<div id='drawnumbers'><span class='sinsign'>Unit Price(s)</span><span class='drawno'>"
										+ unitPrice[panelId] + "</span></div>");
						newData
								.append("<div id='drawnumbers'><span class='sinsign'>Panel Price(s)</span><span class='drawno'>"
										+ betAmtMul[panelId]*unitPrice[panelId]*noOfLines[panelId]*noOfDraw  + "</span></div>");
						newData
								.append("<span class='small'>-----------------------------------------</span>");
					}
		%>
				<div id="drawnumbers">
					<span><%=pickData%></span>
				</div>
				<div class="small">
					----------------------------------------
				</div>
				<div id="drawnumbers">
					<span class="sinsign">Total No. of Line(s)</span>
				</div>
				<div id="drawnumbers">
					<span class="sinsign">No. Of Draws</span><span class="drawno"><%=kenoBean.getNoOfDraws()%></span>
				</div>
				<div id="drawnumbers">
					<span class="sinsign">Total Amount (<s:property
							value="%{#application.CURRENCY_SYMBOL}" />)</span><span class="drawno"><%=kenoBean.getTotalPurchaseAmt()%></span>
				</div>

				<div class="center">
					<img
						src='<%=request.getContextPath()%>/barcode/<%=kenoBean.getTicket_no()%><%=kenoBean.getReprintCount()%>.jpg'></img>
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
			} else {
				if(!"true".equalsIgnoreCase(isTempRcpt.toString())){
				System.out.println(resultAwaitedStr.toString() + "************"
						+ resultAwaitedStr.toString().split(",").length);
		%>
		<div class="center">
			<%
				appletHeight = appletHeight + constantSize;
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
							+ "|gameName=" + pageContext.getAttribute("gameName")
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
							+ "|resultAwaitedStr=" + resultAwaitedStr.toString()
							+ "|retailerName=" + Util.getOrgNameFromTktNo((winningBean.getTicketNo() + winningBean.getReprintCount()), (String)application.getAttribute("ORG_TYPE_ON_TICKET"))
							+ "|drawAmtStr=" + drawAmtStr.toString()
							+ "|clmPendAmtStr=" + clmPendAmtStr.toString()
							+ "|clmStr=" + clmStr.toString()
							+ "|verPendStr=" + verPendStr.toString()
							+ "|isDrawExpiredStr=" + isDrawExpiredStr.toString();
					
					System.out.println("**pwtData*****" + pwtData + "***");
					finalData.append(pwtData);
			%>
			
		</div>
		<%
			
				} else{
					String pwtData = "";
					if(!isReprint){
						pwtData = "data=" + winningBean.getTicketNo() + winningBean.getReprintCount() + "|";
						
						pwtData = pwtData + "mode=PWT|gameDispName=" + winningBean.getGameDispName()
								+ "|ticketNumber=" + winningBean.getTicketNo()
								+ "|orgName=" + application.getAttribute("ORG_NAME_JSP")
								+ "|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL") + "|";
					}
					
					pwtData = pwtData + "reprintCountPWT=" + winningBean.getReprintCount()
							+ "|isReprint=" + String.valueOf(isReprint)
							+ "|retailerName=" + Util.getOrgNameFromTktNo((winningBean.getTicketNo() + winningBean.getReprintCount()), (String)application.getAttribute("ORG_TYPE_ON_TICKET"))
							+ "|drawDateTime=" + drawDateTimeStr.toString();
							
					System.out.println("**pwtData**isTempRcpt***" + pwtData + "***");
					finalData.append(pwtData);
				}
			}
		%>
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
		<%
		} else if ("RAFFLE".equalsIgnoreCase(pwtTktType)) {
					StringBuilder raffleData = new StringBuilder(" ");
					if (winningBean.getRaffleDrawIdBeanList() != null) {
						appletHeight = UtilApplet.getRaffPWTData(winningBean.getRaffleDrawIdBeanList(), raffleData,appletHeight);
					}
					finalData.append("|raffPWTData=" + raffleData);
				}
			}
			
			
			finalData.append(tempRecptData + "|verifiedAt=" + verifiedAt + "|totMainTktAmt=" + Util.getBalInString(totMainTktAmt) + "|ctr=" + (pwtConstantSize + pwtHeightCounter + appletHeight + regButtonHeight));

			System.out.println("KENO SIX Winning DATA: " + finalData.toString());
		 %>
		 <script>setAppData('<%=finalData.toString()%>');</script>
	</body>
	<script>parent.updBalIframe();</script>
</html>


<code id="headId" style="visibility: hidden">
$RCSfile: kenoSixPwtResult.jsp,v $
$Revision: 1.3 $
</code>