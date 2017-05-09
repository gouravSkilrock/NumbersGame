<%@ page import="java.util.*,com.skilrock.lms.beans.LottoPurchaseBean,com.skilrock.lms.web.drawGames.common.Util"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%!int constantSize = 260;
	List<Integer> pickNum = null;
	StringBuilder pickNumStr = new StringBuilder("");
	StringBuilder drawDateTimeStr = new StringBuilder("");
	int appletHeight = 0;
	Integer buyModeHeight = 0;
	StringBuilder quickPickArray = new StringBuilder("");%>

<%
	pageContext.setAttribute("gameName", "Tanzanialotto");
	appletHeight = 0;
	buyModeHeight = 0;
	UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
	String verifiedAt = userInfoBean.getOrgName();
	pickNum = null;
	pickNumStr = new StringBuilder("");
	drawDateTimeStr = new StringBuilder("");
	String status = ((LottoPurchaseBean) request
			.getAttribute("lottoPurchaseBean")).getSaleStatus();
	LottoPurchaseBean lottoPurchaseBean = (LottoPurchaseBean) request
			.getAttribute("lottoPurchaseBean");

	if (status.equals("SUCCESS")) {
		quickPickArray = new StringBuilder("");
		for (int i = 0; i < lottoPurchaseBean.getIsQuickPick().length; i++) {
			quickPickArray.append(lottoPurchaseBean.getIsQuickPick()[i]
					+ ",");
		}
%>
<s:iterator value="%{lottoPurchaseBean.drawDateTime}">
	<s:set name="dateTime" value="%{toString()}" />
	<%
		drawDateTimeStr.append(pageContext.getAttribute("dateTime")
						.toString()
						+ ",");
	%>
</s:iterator>
<s:iterator value="%{lottoPurchaseBean.playerPicked}">
	<s:set name="pickedNumber" value="%{toString()}" />
	<%
		pickNumStr.append(pageContext.getAttribute("pickedNumber")
						.toString()
						+ ";");
	%>
</s:iterator>
<s:set name="buyModeHeight"
	value="%{lottoPurchaseBean.drawDateTime.size()*11}" />

<%
	buyModeHeight = (Integer) pageContext
				.getAttribute("buyModeHeight");
		appletHeight = buyModeHeight + constantSize
				+ (pickNumStr.toString().split(";").length * 11);
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

		<title><s:property value="%{lottoPurchaseBean.ticket_no}" />
			<s:property value="%{lottoPurchaseBean.reprintCount}" /></title>

		<s:head theme="ajax" debug="false" />
	</head>
	<script>var isPrintable=false;var errMsg = "";document.oncontextmenu=new Function("return false");</script>
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

		<div id="purchasecontainer">
			<span class="small">**********************************</span>
			<div class="large">
				WINLOT Limited
			</div>
			<div class="drawgame_name">
				ZIMLOTTO TWO
			</div>
			<s:set name="reprintCount" value="%{lottoPurchaseBean.reprintCount}" />
			<%
				String reprintCount = (String) pageContext
								.getAttribute("reprintCount");
						;
						System.out.println("reprint count--" + reprintCount);
						if (Integer.parseInt(reprintCount) > 0) {
			%><span class="small">Re-Print</span>
			<%
				}
			%>
			<s:set name="purchaseTime" value="%{lottoPurchaseBean.purchaseTime}" />
			<%
				//System.out.println("Date time****"+pageContext.getAttribute("purchaseTime"));
						String[] pTime = pageContext.getAttribute("purchaseTime")
								.toString().split(" ");
			%>
			<div id="drawnumbers">
				<span class="date"><%=pTime[0]%></span><span class="time"><%=pTime[1].replace(".0", "")%></span>
			</div>
			<div class="ticket">
				TicNo :
				<s:property value="%{lottoPurchaseBean.ticket_no}" />
				<s:property value="%{lottoPurchaseBean.reprintCount}" />
			</div>
			<span class="small">-----------------------------------------</span>
			<div id="drawnumbers">
				<span class="ttlpay">Draw Date</span><span class="amt">Draw
					Time</span>
			</div>
			<s:iterator value="%{lottoPurchaseBean.drawDateTime}">
				<s:set name="dateTime" value="%{toString()}" />
				<%
					//System.out.println("Date time****"+pageContext.getAttribute("dateTime"));
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
			<s:iterator value="%{lottoPurchaseBean.playerPicked}">
				<s:property value="%{toString()}" />
				<br />
			</s:iterator>

			<div class="small">
				-----------------------------------------
			</div>
			<div id="drawnumbers">
				<span class="sinsign">Total Quantity</span><span class="drawno"><s:property
						value="%{lottoPurchaseBean.panel_id.length}" /> </span>
			</div>
			<div id="drawnumbers">
				<span class="sinsign">No. Of Draws</span><span class="drawno"><s:property
						value="%{lottoPurchaseBean.noOfDraws}" /> </span>
			</div>
			<div id="drawnumbers">
				<span class="sinsign">Total Amount (<s:property
						value="%{#application.CURRENCY_SYMBOL}" />)</span><span class="drawno"><s:property
						value="%{lottoPurchaseBean.totalPurchaseAmt}" /> </span>
			</div>
			<div class="center">
				<img
					src='<%=request.getContextPath()%>/barcode/<s:property value="%{lottoPurchaseBean.ticket_no}" /><s:property value="%{lottoPurchaseBean.reprintCount}" />.jpg'></img>
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
				System.out.println(pickNumStr.toString() + "Data *****");
		%>
				<div class="center">
				<%
					try{
						if(Integer.parseInt(lottoPurchaseBean.getReprintCount()) > 0){
							appletHeight = appletHeight + 16;
						}			
					} catch(NumberFormatException nfe){
						nfe.printStackTrace();
					}
					
					String ticketExpiryEnabled = (String)application.getAttribute("TICKET_EXPIRY_ENABLED");
					
					if("YES".equalsIgnoreCase(ticketExpiryEnabled)){
						appletHeight = appletHeight + 11;
					}
					
					StringBuilder raffleData = new StringBuilder(" ");
					if(lottoPurchaseBean.getRafflePurchaseBeanList() != null){
						appletHeight  = Util.getRaffData(lottoPurchaseBean.getRafflePurchaseBeanList(), raffleData, appletHeight);
					}
					System.out.println("**raffleData*****"+raffleData+"***");
					
					StringBuilder topMsgsStr = new StringBuilder(" ");
					StringBuilder bottomMsgsStr = new StringBuilder(" ");
					if(lottoPurchaseBean.getAdvMsg() != null){
						appletHeight = Util.getAdvMsgs(lottoPurchaseBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, appletHeight);
					}
					
					String totalQuantity=lottoPurchaseBean.getPanel_id().length +"";
					if("Perm6".equalsIgnoreCase(lottoPurchaseBean.getPlayType())){
						totalQuantity=lottoPurchaseBean.getNoOfLines()+"";
					}
				
					String finalData = "data=" + lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount()
							+ "|gameName=" + pageContext.getAttribute("gameName")
							+ "|mode=Buy|saleStatus=" + lottoPurchaseBean.getSaleStatus()
							+ "|reprintCount=" + lottoPurchaseBean.getReprintCount()
							+ "|purchaseTime=" + lottoPurchaseBean.getPurchaseTime()
							+ "|gameDispName=" + lottoPurchaseBean.getGameDispName()
							+ "|ticketNumber=" + lottoPurchaseBean.getTicket_no()
							+ "|drawDateTime=" + drawDateTimeStr.toString()
							+ "|pickedNumbers=" + pickNumStr.toString()
							+ "|isQuickPickArray=" + quickPickArray.toString()
							+ "|expiryPeriod=" + Util.getGameMap().get(Util.getGameName(lottoPurchaseBean.getGame_no())).getTicketExpiryPeriod()
							+ "|noOfDraws=" + lottoPurchaseBean.getNoOfDraws()
							+ "|playType=" + lottoPurchaseBean.getPlayType()
							+ "|unitPrice=" + lottoPurchaseBean.getUnitPrice()
							+ "|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL")
							+ "|totalPurchaseAmt=" + lottoPurchaseBean.getTotalPurchaseAmt()
							+ "|orgName=" + application.getAttribute("ORG_NAME_JSP")
							+ "|advtMsg=" + application.getAttribute("ADVT_MSG") 
							+ "|topAdvMsg=" + topMsgsStr
							+ "|bottomAdvMsg=" + bottomMsgsStr
							+ "|jackPotAmt=" + ((Map)application.getAttribute("jackPotMap")).get(Util.getGameNumber((String)pageContext.getAttribute("gameName")))
							+ "|raffleData=" + raffleData
							+ "|orgAddress=" + application.getAttribute("ORG_ADDRESS")
							+ "|orgMobile=" + application.getAttribute("ORG_MOBILE")
							+ "|ticketExpiryEnabled=" + ticketExpiryEnabled
							+ "|totalQuantity=" + totalQuantity 
							+ "|retailerName=" + Util.getOrgNameFromTktNo((lottoPurchaseBean.getTicket_no() + lottoPurchaseBean.getReprintCount()), (String)application.getAttribute("ORG_TYPE_ON_TICKET"))
							+ "|verifiedAt=" + verifiedAt 
							+ "|ctr=" + appletHeight;
					System.out.println("ZIMLOTTO TWO PURCHASE DATA: " + finalData);
				%>
				<script>setAppData('<%=finalData%>');</script>
				</div>
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
		<script>errMsg="Error!Try Again";parent.displayErrMsg();</script>

		<%
			}
		%>
	</body>
</html>
<script>parent.updBalIframe();</script>

<code id="headId" style="visibility: hidden">
$RCSfile: tanzanialottoSuccess.jsp,v $
$Revision: 1.1.2.1 $
</code>