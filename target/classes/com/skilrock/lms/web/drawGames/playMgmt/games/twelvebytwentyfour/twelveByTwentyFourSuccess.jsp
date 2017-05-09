<%@ page
	import="java.util.*,com.skilrock.lms.dge.beans.KenoPurchaseBean,com.skilrock.lms.web.drawGames.common.Util"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@page import="com.skilrock.lms.common.filter.LMSFilterDispatcher"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%!int constantSize = 335;
	List<Integer> pickNum = null;
	StringBuilder pickNumStr = new StringBuilder("");
	StringBuilder drawDateTimeStr = new StringBuilder("");
	int appletHeight = 0;
	Integer buyModeHeight = 0;%>

<%
	pageContext.setAttribute("gameName", "TwelveByTwentyFour");
	appletHeight = 0;
	buyModeHeight = 0;
	
	UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
	String verifiedAt = userInfoBean.getOrgName();
	
	KenoPurchaseBean kenoBean = (KenoPurchaseBean) request
			.getAttribute("twelveByTwentyFourPurchaseBean");
	pickNum = null;
	pickNumStr = new StringBuilder("");
	drawDateTimeStr = new StringBuilder("");
	String status = ((KenoPurchaseBean) request.getAttribute("twelveByTwentyFourPurchaseBean")).getSaleStatus();
	String playerData[] = ((KenoPurchaseBean) request.getAttribute("twelveByTwentyFourPurchaseBean")).getPlayerData();
	String drawData = null;
	String pickData = "";
	if (status.equals("SUCCESS")) {
%>
<s:iterator value="%{twelveByTwentyFourPurchaseBean.drawDateTime}">
	<s:set name="dateTime" value="%{toString()}" />
	<%
		drawDateTimeStr.append(pageContext.getAttribute("dateTime").toString().split("&")[0] + ",");
	%>
</s:iterator>
<s:iterator value="%{twelveByTwentyFourPurchaseBean.playerData}">
	<s:set name="pickedNumber" value="%{toString()}" />
	<%
		pickNumStr.append(pageContext.getAttribute("pickedNumber")
						.toString()
						+ ",");
	%>
</s:iterator>
<s:set name="buyModeHeight"
	value="%{twelveByTwentyFourPurchaseBean.drawDateTime.size()*22}" />



<%
	buyModeHeight = (Integer) pageContext
				.getAttribute("buyModeHeight");
		//appletHeight = buyModeHeight + constantSize + (playerData.split(",").length / 7 * 11);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<script
		src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/commonRPOS.js"></script>
	<%
		String barcodeType = (String) application
					.getAttribute("BARCODE_TYPE");
			if (!barcodeType.equals("applet")) {
	%>

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/terminal.css"
			type="text/css" />

		<title><s:property value="%{twelveByTwentyFourPurchaseBean.ticket_no}" /> <s:property
				value="%{twelveByTwentyFourPurchaseBean.reprintCount}" />
		</title>

		<s:head theme="ajax" debug="false" />
	</head>
	<script>var isPrintable=false;var errMsg = "";//document.oncontextmenu=new Function("return false");</script>
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
		<object ID="WebBrowser1" WIDTH="0" HEIGHT="0"
			CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2">
		</object>
		<%
			//if(playerData.indexOf("-1")!=-1)
					//	pickData=playerData.substring(0, playerData.indexOf("-1")-1);
					//else 
					//	pickData=playerData;			
					String playType[] = ((KenoPurchaseBean) request
							.getAttribute("twelveByTwentyFourPurchaseBean")).getPlayType();
					int[] noOfLines = ((KenoPurchaseBean) request
							.getAttribute("twelveByTwentyFourPurchaseBean")).getNoOfLines();
					double unitPrice[] = ((KenoPurchaseBean) request
							.getAttribute("twelveByTwentyFourPurchaseBean")).getUnitPrice();
					int[] betAmtMul = ((KenoPurchaseBean) request
							.getAttribute("twelveByTwentyFourPurchaseBean")).getBetAmountMultiple();
					int noOfDraw=((KenoPurchaseBean) request.getAttribute("twelveByTwentyFourPurchaseBean")).getNoOfDraws();
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
										+ noOfLines[panelId] + "</span></div>");
						newData
								.append("<div id='drawnumbers'><span class='sinsign'>Unit Price(s)</span><span class='drawno'>"
										+ unitPrice[panelId]*betAmtMul[panelId] + "</span></div>");
						newData
								.append("<div id='drawnumbers'><span class='sinsign'>Panel Price(s)</span><span class='drawno'>"
										+ betAmtMul[panelId]*unitPrice[panelId]*noOfLines[panelId]*noOfDraw  + "</span></div>");
						newData
								.append("<span class='small'>-----------------------------------------</span>");
					}
		%>
		<div id="purchasecontainer">
			<span class="small">**********************************</span>
			<div class="large">
				WINLOT Limited
			</div>
			<div class="drawgame_name">
				KENO
			</div>
			<s:set name="reprintCount" value="%{twelveByTwentyFourPurchaseBean.reprintCount}" />
			<%
				String reprintCount = (String) pageContext
								.getAttribute("reprintCount");
						System.out.println("reprint count--" + reprintCount);
						if (Integer.parseInt(reprintCount) > 0) {
			%><span class="small">Re-Print</span>
			<%
				}
			%>
			<s:set name="purchaseTime" value="%{twelveByTwentyFourPurchaseBean.purchaseTime}" />
			<%
				System.out.println("Date time****"
								+ pageContext.getAttribute("purchaseTime"));
						String[] pTime = pageContext.getAttribute("purchaseTime")
								.toString().split(" ");
			%>
			<div id="drawnumbers">
				<span class="date"><%=pTime[0]%></span><span class="time"><%=pTime[1].replace(".0", "")%></span>
			</div>
			<div class="ticket">
				TicNo :
				<s:property value="%{twelveByTwentyFourPurchaseBean.ticket_no+twelveByTwentyFourPurchaseBean.reprintCount}" />
			</div>
			<span class="small">-----------------------------------------</span>
			<div id="drawnumbers">
				<span class="ttlpay">Draw Date</span><span class="amt">Draw
					Time</span>
			</div>
			<s:iterator value="%{twelveByTwentyFourPurchaseBean.drawDateTime}">
				<s:set name="dateTime" value="%{toString()}" />
				<%
					System.out.println("Date time****"
										+ pageContext.getAttribute("dateTime")
												.toString() + "hiiiiii");
								String[] date = pageContext.getAttribute("dateTime")
										.toString().split(" ");
				%>
				<div id="drawnumbers">
					<span class="ttlpay"><%=date[0]%></span><span class="amt"><%=date[1]%></span>
				</div>
			</s:iterator>
			<div class="small">
				-----------------------------------------
			</div>
			<%=newData%><br />
			<div class="small">
				-----------------------------------------
			</div>
			<div id="drawnumbers">
				<span class="sinsign">No. Of Draws</span><span class="drawno"><s:property
						value="%{twelveByTwentyFourPurchaseBean.noOfDraws}" /> </span>
			</div>
			<div id="drawnumbers">
				<span class="sinsign">Total Amount (<s:property
						value="%{#application.CURRENCY_SYMBOL}" />)</span><span class="drawno"><s:property
						value="%{twelveByTwentyFourPurchaseBean.totalPurchaseAmt}" /> </span>
			</div>
			<div class="center">
				<img
					src='<%=request.getContextPath()%>/barcode/<s:property value="%{twelveByTwentyFourPurchaseBean.ticket_no}" /><s:property value="%{twelveByTwentyFourPurchaseBean.reprintCount}" />.jpg'></img>
			</div>
			<span class="clear"></span>
			<span class="small">**********************************</span>
		</div>
		<script>isPrintable=true;</script>
		<div>
			<b>SAMPLE NOT FOR SALE<b>
		</div>
		<script>parent.printFunc();</script>

		<%
			} else {
		%>
		<div class="center">


			<%
				StringBuilder topMsgsStr = new StringBuilder(" ");
				StringBuilder bottomMsgsStr = new StringBuilder(" ");
				if(kenoBean.getAdvMsg() != null){
					appletHeight = Util.getAdvMsgs(kenoBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, appletHeight);
				}
				
				int noOfDraws = kenoBean.getNoOfDraws();
				
				String[] playerDataApp = kenoBean.getPlayerData();
				String[] playType = kenoBean.getPlayType();
				int[] noOfLines = kenoBean.getNoOfLines();
				double[] unitPrice = kenoBean.getUnitPrice();
				int[] betAmtMul = kenoBean.getBetAmountMultiple();
				int[] isQP = kenoBean.getIsQuickPick();
				double[] panelPrice = new double[playType.length];
				//String[] combDispArr = new String[playType.length];
				
				StringBuilder playerDataStr = new StringBuilder(" ");
				StringBuilder playTypeStr = new StringBuilder(" ");
				StringBuilder noOfLinesStr = new StringBuilder(" ");
				StringBuilder unitPriceStr = new StringBuilder(" ");
				StringBuilder isQPStr = new StringBuilder(" ");
				StringBuilder panelPriceStr = new StringBuilder(" ");
				
				StringBuilder combDispStr = new StringBuilder(" ");
				
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
					
						NumberFormat nf = NumberFormat.getInstance();
						nf.setMinimumFractionDigits(2);
						
						unitPriceStr.append(nf.format(unitPrice[panelId] * betAmtMul[panelId]) + "~");
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
				
				
				//String finalData1="data-"+fortuneBean.getTicket_no()+fortuneBean.getReprintCount()+";"+"Fortune"+";"+"Buy"+";"+fortuneBean.getSaleStatus()+";"+pTimeNew+";"+fortuneBean.getGameDispName()+";"+drawDateTimeStr.toString()+";"+pickNumStr.toString()+";"+betMulStr.toString()+";"+fortuneBean.getNoOfDraws()+";"+pageContext.getAttribute("currSymbol")+";"+fortuneBean.getTotalPurchaseAmt()+";"+pageContext.getAttribute("orgName")+";"+totalQuantity;
				System.out.println("kenoBean.getGame_no() - "+kenoBean.getGame_no());
						String finalData = "data=" + kenoBean.getTicket_no() + kenoBean.getReprintCount() + (kenoBean.getBarcodeCount()!=-1 && LMSFilterDispatcher.isBarCodeRequired?kenoBean.getBarcodeCount():"")
								+ "|gameName=" + pageContext.getAttribute("gameName")
								+ "|mode=Buy|saleStatus=" + kenoBean.getSaleStatus()
								+ "|reprintCount=" + kenoBean.getReprintCount()
								+ "|purchaseTime=" + kenoBean.getPurchaseTime()
								+ "|gameDispName=" + kenoBean.getGameDispName()
								+ "|ticketNumber=" + kenoBean.getTicket_no()
								+ "|drawDateTime=" + drawDateTimeStr.toString()
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
								+ "|panelPriceStr=" + panelPriceStr
								+ "|retailerName=" + Util.getOrgNameFromTktNo((kenoBean.getTicket_no() + kenoBean.getReprintCount()), (String)application.getAttribute("ORG_TYPE_ON_TICKET"))
								+ "|verifiedAt=" + verifiedAt;
						
						System.out.println("KENO TWO PURCHASE DATA: " + finalData);
						
			String promoData = "";
						
			if(kenoBean.getPromoPurchaseBean() != null){
				finalData = finalData + "|ctr=" + (appletHeight + constantSize + 300);
				String promoOriginalData = "|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL")
							+ "|orgName=" + application.getAttribute("ORG_NAME_JSP")
							+ "|advtMsg=" + application.getAttribute("ADVT_MSG")
							+ "|orgAddress=" + application.getAttribute("ORG_ADDRESS")
							+ "|orgMobile=" + application.getAttribute("ORG_MOBILE")
							+ "|ticketExpiryEnabled=" + application.getAttribute("TICKET_EXPIRY_ENABLED");
							
							
				promoData = Util.getPromoData(kenoBean.getPromoPurchaseBean() , promoOriginalData);
				if(promoData.length() > 0){
					promoData = "PROMO" + promoData + "|ctr=" + (appletHeight + constantSize + 300);
				}
			} else{
				finalData = finalData + "|ctr=" + (appletHeight + constantSize);
			}
		String appData = finalData + promoData;
		long responseCRC=Util.getCheckSum(appData);
		appData += "|CRC="+responseCRC;
		System.out.println("KENO TWO PURCHASE CUM PROMO DATA: " + appData);
						
						
			%>
			<script>setAppData('<%=appData%>');</script>
		</div>
	</body>

	<%
		}
		
		
		
		} else {
	%>
	<table>
		<tr>
			<td>
				<b>Error!Try Again</b>
			</td>
		</tr>
	</table>
	<script>parent.displayErrMsg();errMsg="Error!Try Again";</script>

	<%
		}
	%>
	<script>parent.updBalIframe();</script>
</html>


<code id="headId" style="visibility: hidden">
$RCSfile: twelveByTwentyFourSuccess.jsp,v $
$Revision: 1.3 $
</code>