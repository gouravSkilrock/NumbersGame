<%@ page import="java.util.*,com.skilrock.lms.beans.FortunePurchaseBean,com.skilrock.lms.web.drawGames.common.Util"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	List<String> sunSign = Arrays.asList("", "Zero(0)", "One(1)",
			"Two(2)", "Three(3)", "Four(4)", "Five(5)", "Six(6)",
			"Seven(7)", "Eight(8)", "Nine(9)");

	pageContext.setAttribute("gameName", "Zerotonine");
%>
<%!List<Integer> pickNum = null;
	List<String> drawDateTime = null;
	int[] betMul = null;
	String[] date = null;
	int constantSize = 250;
	StringBuilder betMulStr = new StringBuilder("");
	StringBuilder pickNumStr = new StringBuilder("");
	StringBuilder drawDateTimeStr = new StringBuilder("");
	int totalQuantity = 0;
	int appletHeight = 0;
	Integer buyModeHeight = 0;%>

<%
	UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
	String verifiedAt = userInfoBean.getOrgName();
	pickNum = null;
	drawDateTime = null;
	betMul = null;
	date = null;
	betMulStr = new StringBuilder("");
	pickNumStr = new StringBuilder("");
	drawDateTimeStr = new StringBuilder("");
	totalQuantity = 0;
	String status = ((FortunePurchaseBean) request
			.getAttribute("fortuneBean")).getSaleStatus();
	FortunePurchaseBean fortuneBean = (FortunePurchaseBean) request
			.getAttribute("fortuneBean");
	System.out.println("status=========" + status);
	if (status.equals("SUCCESS")) {
%>
<s:iterator value="%{fortuneBean.drawDateTime}">
	<s:set name="dateTime" value="%{toString()}" />
	<%
		drawDateTimeStr.append(pageContext.getAttribute("dateTime")
						.toString()
						+ ",");
	%>
</s:iterator>
<s:set name="pickedNumbers" value="%{fortuneBean.pickedNumbers}" />
<s:set name="betMultiple" value="%{fortuneBean.betAmountMultiple}" />
<s:set name="buyModeHeight"
	value="%{fortuneBean.drawDateTime.size()*11+fortuneBean.pickedNumbers.size()*11}" />
<%
	pickNum = (List<Integer>) pageContext
				.getAttribute("pickedNumbers");
		betMul = (int[]) pageContext.getAttribute("betMultiple");
		for (int i = 0; i < pickNum.size(); i++) {
			pickNumStr.append(pickNum.get(i) + ",");
			betMulStr.append(betMul[i] + ",");
			totalQuantity = totalQuantity + betMul[i];
		}

		buyModeHeight = (Integer) pageContext
				.getAttribute("buyModeHeight");
		appletHeight = buyModeHeight + constantSize;
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

		<title><s:property value="%{fortuneBean.ticket_no}" /> <s:property
				value="%{fortuneBean.reprintCount}" /></title>

		<s:head theme="ajax" debug="false" />
	</head>
	<script>var isPrintable=false;var errMsg = "";document.oncontextmenu=new Function("return false");
function cancelTktAuto2(){
						alert("CANCELTKTAUTO2222222222");
					}
</script>
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
				FORTUNE
			</div>
			<s:set name="reprintCount" value="%{fortuneBean.reprintCount}" />
			<%
				String reprintCount = (String) pageContext
								.getAttribute("reprintCount");
						System.out.println("reprint count--" + reprintCount);
						if (Integer.parseInt(reprintCount) > 0) {
			%><span class="small">Re-Print</span>
			<%
				}
			%>
			<s:set name="purchaseTime" value="%{fortuneBean.purchaseTime}" />
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
				<s:property value="%{fortuneBean.ticket_no}" />
				<s:property value="%{fortuneBean.reprintCount}" />
			</div>
			<span class="small">-----------------------------------------</span>
			<div id="drawnumbers">
				<span class="ttlpay">Draw Date</span><span class="amt">Draw
					Time</span>
			</div>
			<s:iterator value="%{fortuneBean.drawDateTime}">
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
			<s:set name="pickedNumbers" value="%{fortuneBean.pickedNumbers}" />
			<s:set name="betMultiple" value="%{fortuneBean.betAmountMultiple}" />
			<%
				System.out.println(pickNum + "*************-----------");
						for (int i = 0; i < pickNum.size(); i++) {
			%>
			<div id="drawnumbers">
				<span class="sinsign"><%=sunSign.get((pickNum.get(i)))%></span><span
					class="drawno"><%=betMul[i]%></span>
			</div>
			<%
				}
			%>
			<div class="small">
				-----------------------------------------
			</div>
			<div id="drawnumbers">
				<span class="sinsign">Total Quantity</span><span class="drawno"><%=totalQuantity%></span>
			</div>
			<div id="drawnumbers">
				<span class="sinsign">No. Of Draws</span><span class="drawno"><s:property
						value="%{fortuneBean.noOfDraws}" /> </span>
			</div>
			<div id="drawnumbers">
				<span class="sinsign">Total Amount (<s:property
						value="%{#application.CURRENCY_SYMBOL}" />)</span><span class="drawno"><s:property
						value="%{fortuneBean.totalPurchaseAmt}" /> </span>
			</div>
			<div class="center">
				<img
					src='<%=request.getContextPath()%>/barcode/<s:property value="%{fortuneBean.ticket_no}" /><s:property value="%{fortuneBean.reprintCount}" />.jpg'></img>
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
		<div class="center" id="appletDiv">
			<%	try{
					if(Integer.parseInt(fortuneBean.getReprintCount()) > 0){
						appletHeight = appletHeight + 16;
					}			
				} catch(NumberFormatException nfe){
					nfe.printStackTrace();
				}
					
				String ticketExpiryEnabled = (String)application.getAttribute("TICKET_EXPIRY_ENABLED");
				
				if("YES".equalsIgnoreCase(ticketExpiryEnabled)){
					appletHeight = appletHeight + 11;
				}
				StringBuilder topMsgsStr = new StringBuilder(" ");
				StringBuilder bottomMsgsStr = new StringBuilder(" ");
				if(fortuneBean.getAdvMsg() != null){
					appletHeight = Util.getAdvMsgs(fortuneBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, appletHeight);
				}
				String finalData = "data=" + fortuneBean.getTicket_no() + fortuneBean.getReprintCount() 
								+ "|gameName=" + pageContext.getAttribute("gameName")
								+ "|mode=Buy|saleStatus=" + fortuneBean.getSaleStatus() 
								+ "|reprintCount=" + fortuneBean.getReprintCount() 
								+ "|purchaseTime=" + fortuneBean.getPurchaseTime() 
								+ "|gameDispName=" + fortuneBean.getGameDispName() 
								+ "|ticketNumber=" + fortuneBean.getTicket_no() 
								+ "|drawDateTime=" + drawDateTimeStr.toString() 
								+ "|pickedNumbers=" + pickNumStr.toString() 
								+ "|betAmountMultiple=" + betMulStr.toString() 
								+ "|isQP=" + fortuneBean.getIsQP() 
								+ "|expiryPeriod=" + Util.getGameMap().get(Util.getGameName(fortuneBean.getGame_no())).getTicketExpiryPeriod() 
								+ "|noOfDraws=" + fortuneBean.getNoOfDraws() 
								+ "|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL")
								+ "|totalPurchaseAmt=" + fortuneBean.getTotalPurchaseAmt() 
								+ "|orgName=" + application.getAttribute("ORG_NAME_JSP")
								+ "|advtMsg=" + application.getAttribute("ADVT_MSG") 
								+ "|topAdvMsg=" + topMsgsStr
								+ "|bottomAdvMsg=" + bottomMsgsStr
								+ "|orgAddress=" + application.getAttribute("ORG_ADDRESS")
								+ "|orgMobile=" + application.getAttribute("ORG_MOBILE")
								+ "|ticketExpiryEnabled=" + ticketExpiryEnabled
								+ "|totalQuantity=" + totalQuantity 
								+ "|retailerName=" + Util.getOrgNameFromTktNo((fortuneBean.getTicket_no() + fortuneBean.getReprintCount()), (String)application.getAttribute("ORG_TYPE_ON_TICKET"))
								+ "|verifiedAt=" + verifiedAt
								+ "|ctr=" + appletHeight;
				System.out.println("ZEROTONINE PURCHASE DATA: " + finalData);
			%>
			<script>setAppData('<%=finalData%>');</script>
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
		
	</body>
	<script>parent.updBalIframe();</script>
</html>


<code id="headId" style="visibility: hidden">
$RCSfile: zeroToNineSuccess.jsp,v $
$Revision: 1.1.2.10.4.4.2.9 $
</code>