<%@ page
	import="java.util.*,com.skilrock.lms.beans.PWTDrawBean,com.skilrock.lms.beans.DrawIdBean,com.skilrock.lms.beans.FortunePurchaseBean,com.skilrock.lms.beans.PanelIdBean"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.GameBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
	<script>document.oncontextmenu=new Function("return false");</script>
	<script language='VBScript'>
Sub Print()
       OLECMDID_PRINT = 6
       OLECMDEXECOPT_DONTPROMPTUSER = 2
       OLECMDEXECOPT_PROMPTUSER = 1
       call WB.ExecWB(OLECMDID_PRINT, OLECMDEXECOPT_DONTPROMPTUSER,1)
End Sub
document.write "<object ID='WB' WIDTH=0 HEIGHT=0 CLASSID='CLSID:8856F961-340A-11D0-A96B-00C04FD705A2'></object>"
</script>
	<body onload="parent.updBalIframe(),parent.printFunc()">

		<%
			PWTDrawBean winningBean = (PWTDrawBean) session
					.getAttribute("PWT_RES");
			//System.out.println("winningBean *****************"+winningBean);
		%>
		<div id="maincontainer">
			<span class="small">**********************************</span>
			<div class="large">
				WINLOT Limited
			</div>
			<div class="largeunbold">
				PWT Verification
			</div>
			<div class="ticket">
				TicNo :
				<%=winningBean.getTicketNo()%><%=winningBean.getReprintCount()%></div>

			<%
				double totTktAmt = 0.0;
				int totRegister = 0;
				if (!winningBean.isValid()) {
			%>
			<div class="added">
				Invalid Ticket
			</div>
			<%
				} else if (winningBean.getStatus().equals("SUCCESS")) {

					for (int i = 0; i < winningBean.getDrawWinList().size(); i++) {
						int nonWin = 0;
						int win = 0;
						int register = 0;
						int claim = 0;
						int outVerify = 0;
						int pndPay = 0;
						DrawIdBean drawBean = winningBean.getDrawWinList().get(i);
						String[] drawTime = drawBean.getDrawDateTime().split(" ");
						boolean isResAwaited = false;
						double drawAmt = 0.0;
						List<PanelIdBean> panelWinList = drawBean.getPanelWinList();
						if (panelWinList != null) {
							//System.out.println("aaaa");
							for (int j = 0; j < panelWinList.size(); j++) {
								PanelIdBean panelBean = panelWinList.get(j);
								//System.out.println(panelBean);
								//System.out.println(panelBean.getStatus());
								if (panelBean.getStatus().equals("NON WIN")) {
									nonWin++;
								} else if (panelBean.getStatus().equals(
										"NORMAL_PAY")) {
									drawAmt = Double.parseDouble(panelBean
											.getWinningAmt())
											+ drawAmt;
									win++;
								} else if (panelBean.getStatus().equals("CLAIMED")) {
									claim++;
								} else if (panelBean.getStatus().equals("PND_PAY")) {
									pndPay++;
								} else if (panelBean.getStatus().equals(
										"HIGH_PRIZE")) {
									register++;
								} else if (panelBean.getStatus().equals(
										"OUT_PAY_LIMIT")) {
									register++;
								} else if (panelBean.getStatus().equals(
										"OUT_VERIFY_LIMIT")) {
									outVerify++;
								}

							}
						} else {
							isResAwaited = true;
						}
						totTktAmt = drawAmt + totTktAmt;

						if (isResAwaited) {
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
				if (nonWin != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=nonWin%> Panel (s)</span><span class="amt">TRY
					AGAIN</span>
			</div>
			<%
				}
							if (win != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=win%> Panel (s)</span><span class="amt">WIN
					<%=drawAmt%> <s:property value="%{#application.CURRENCY_SYMBOL}" />
				</span>
			</div>
			<%
				}
							if (register != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=register%> Panel (s)</span><span
					class="amt">REG. REQ.</span>
			</div>
			<%
				}
							if (claim != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=claim%> Panel (s)</span><span class="amt">CLAIMED</span>
			</div>
			<%
				}
							if (pndPay != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=pndPay%> Panel (s)</span><span class="amt">IN
					PROCESS</span>
			</div>
			<%
				}
							if (outVerify != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=outVerify%> Panel (s)</span><span
					class="amt">OUT OF VERIFY</span>
			</div>
			<%
				}
			%><div id="drawnumbers">
				<span class="small">-----------------------------------------</span>
			</div>
			<%
				}
						totRegister = totRegister + register;
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
			<%
				if (totRegister != 0) {
			%><div id="drawnumbers">
				<span class="ttlpay"><%=totRegister%> Panel (s)</span><span
					class="amt">REG. REQ.</span>
			</div>
			<div id="drawnumbers">
				<span class="ttlpay"></span><span class="amt"><input
						type="button" value="Register" onclick="parent.regPlr()" />
				</span>
			</div>
			<%
				}
			%><div id="drawnumbers">
				<span class="small">**********************************</span>
			</div>
		</div>
		<div>
			<%
				System.out.println(winningBean.isReprint()
						+ "****ISREFPRINT*********");
				if (winningBean.isReprint()) {

					FortunePurchaseBean zeroToNineBean = (FortunePurchaseBean) winningBean
							.getPurchaseBean();
					if (zeroToNineBean != null) {
						//System.out.println("fortuneBean "+fortuneBean);
			%>
			<div id="maincontainer">
				<span class="small">*********************************</span>
				<div class="large">
					WINLOT Limited
				</div>
				<div class="drawgame_name">
					Zero To Nine
				</div>
				<span class="small">Duplicate</span>
				<%
					//System.out.println("Date time****"+pageContext.getAttribute("purchaseTime"));
							String[] pTime = zeroToNineBean.getPurchaseTime().toString()
									.split(" ");
							List<String> numbers = Arrays.asList("", "0", "1",
									"2", "3", "4", "5", "6",
									"7", "8", "9");
				%>
				<span class="date"><%=pTime[0]%></span><span class="time"><%=pTime[1].replace(".0", "")%></span>
				<div class="ticket">
					TicNo :
					<%=zeroToNineBean.getTicket_no()%><%=zeroToNineBean.getReprintCount()%></div>
				<span class="small">----------------------------------------</span>
				<div id="drawnumbers">
					<span class="ttlpay">Draw Date</span><span class="amt">Draw
						Time</span>
				</div>
				<%
					for (int i = 0; i < zeroToNineBean.getDrawDateTime().size(); i++) {
								String[] date = ((String) zeroToNineBean.getDrawDateTime()
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
					List<Integer> pickNum = (List<Integer>) zeroToNineBean.getPickedNumbers();
					int totQuantity = 0;
					int[] betMul = (int [])zeroToNineBean.getBetAmountMultiple();
					for (int i = 1; i < pickNum.size(); i++) {
					totQuantity=totQuantity+betMul[i];
				%>
				<div id="drawnumbers">
					<span class="sinsign"><%=numbers.get((pickNum.get(i)))%></span><span class="drawno"><%=betMul[i]%></span>
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
					<span class="sinsign">No. Of Draws</span><span class="drawno"><%=zeroToNineBean.getNoOfDraws()%></span>
				</div>
				<div id="drawnumbers">
					<span class="sinsign">Total Amount (<s:property
							value="%{#application.CURRENCY_SYMBOL}" />)</span><span class="drawno"><%=zeroToNineBean.getTotalPurchaseAmt()%></span>
				</div>
				<%
					String barcodeType = (String) application
									.getAttribute("BARCODE_TYPE");
							if (barcodeType.equals("applet")) {
				%>
				<div class="center">
					<applet Codebase="<%=request.getContextPath()%>/"
						code="BarcodeApplet.class" name="barCodeApplet" width="151"
						height="51">

						<param name="data"
							value="<%=zeroToNineBean.getTicket_no()%><%=zeroToNineBean.getReprintCount()%>" />
					</applet>
				</div>
				<%
					} else {
				%>
				<div class="center">
					<img
						src='<%=request.getContextPath()%>/barcode/<%=zeroToNineBean.getTicket_no()%><%=zeroToNineBean.getReprintCount()%>.jpg'></img>
				</div>
				<%
					}
				%>
				<span class="clear"></span>
				<span class="small">*********************************</span>
			</div>
			<%
				}
				}
			%>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">$Id: zeroToNinePwtResult.jsp,v 1.1 2010/06/07 09:47:32 anant Exp $</code>
