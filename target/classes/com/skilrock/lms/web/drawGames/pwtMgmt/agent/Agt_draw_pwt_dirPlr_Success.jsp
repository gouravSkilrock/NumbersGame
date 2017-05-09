<%@ page import="java.util.*,com.skilrock.lms.dge.beans.PWTDrawBean,com.skilrock.lms.dge.beans.DrawIdBean,com.skilrock.lms.dge.beans.FortunePurchaseBean,com.skilrock.lms.dge.beans.PanelIdBean,com.skilrock.lms.web.drawGames.common.Util" %>
<%@page import="com.skilrock.lms.dge.beans.MainPWTDrawBean"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.web.drawGames.common.UtilApplet"%>
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
	<s:head theme="ajax" debug="false"/>
	<style type="text/css">


body {
	color: #666666;
	text-align: center;
	background-color: #FFFFFF;	
	font-family: Verdana, Tahoma, Helvetica, sans-serif;
	font-size: 0.7em;
	
}


a {
	color: #4284B0;
	background-color: inherit;
	text-decoration: none;
	width: 900px;
	text-align: right;
}
.light_border_dgray {
	border:#E7E7CF 1px solid;
	
}

.large { font-size: 1.18em; 
		 font-weight:300; color:#000; 
		 font-family:"Letter Gothic Std";
}
.drawgame_namejsp { font-weight:800;
				 font-size:2em; 
				 font-family:"Letter Gothic Std"; 
				 font-stretch:semi-expanded;  letter-spacing:0.36em;
} 
.pwtjspTicket { font-size: 1.54em; 
			   font-weight:300; 
			   color:#000; 
			   font-family:"Letter Gothic Std"; 
}

.drawTable1{
	background:#DFE6FD;
	border-bottom:1px dotted #CDD8FC;
	padding:3px;
	margin:4px 0 4px 0;
}

.drawTable2{
	background:#FEEBF3;
	border-bottom:1px dotted #FDD7E7;
	padding:3px;
	margin:4px 0 4px 0;
}

.drawTable3{
	background:#E1FFF0;
	border-bottom:1px dotted #C4FFE1;
	padding:3px;
	margin:4px 0 4px 0;
}

.drawTable4{
	background:#FFFFD7;
	border-bottom:1px dotted #FFFFB9;
	padding:3px;
	margin:4px 0 4px 0;
}

</style>
	</head>

<body >
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
  <%
  String className="drawTable1";
  
  //PWTDrawBean winningBean =(PWTDrawBean)session.getAttribute("PWT_RES");
  MainPWTDrawBean mainPWTBean = (MainPWTDrawBean)session.getAttribute("PWT_RES");
  if("OUT_VERIFY_LIMIT".equalsIgnoreCase(mainPWTBean.getStatus())){
  	%>
  		<h3>Out of Verification Limit</h3>
  	<%
  } else if("OUT_PAY_LIMIT".equalsIgnoreCase(mainPWTBean.getStatus())){
  	%>
  		<h3>Out of Pay Limit</h3>
  	<%
  } else if("HIGH_PRIZE".equalsIgnoreCase(mainPWTBean.getStatus())){
  	%>
  		<h3>High Prize Ticket Can't be claimed at Agent.</h3>
  	<%
  } else{
  	List<PWTDrawBean> winningBeanList = mainPWTBean.getWinningBeanList();
	double totTktAmt = 0.0;
   if(winningBeanList != null){
   	for(int k=0; k<winningBeanList.size(); k++){
   PWTDrawBean winningBean = winningBeanList.get(k);
		String pwtTktType = winningBean.getPwtTicketType();
		if("DRAW".equalsIgnoreCase(pwtTktType)){
  %>
  <table width="25%" border="0" cellspacing="0" cellpadding="0" align="center" class="light_border_dgray">
            <tr><td align="center" colspan="2" class="pwtjspTicket"><%=application.getAttribute("ORG_NAME_JSP") %></td></tr>
            <tr><td align="center" colspan="2" class="pwtjspTicket">Winning verification</td></tr></table>
 <table width="25%" border="0" cellspacing="0" cellpadding="0" class="<%=className%>"><tr><td>
 TicNo :</td><td> <%=winningBean.getTicketNo()%><%=(winningBean.getReprintCount()==null)? " " :winningBean.getReprintCount() %></td></tr></table>

<%
		
		
		
		//double totTktAmt = 0.0;
		if(!winningBean.isValid()){
			%>
			 <table width="25%" border="0" cellspacing="0" cellpadding="0" class="<%=className%>"><tr><td>Invalid Ticket</td></tr></table>
			<%
				}else if (winningBean.getStatus().equals("SUCCESS")){
			
		for(int i=0;i<winningBean.getDrawWinList().size();i++){
		
		if(i%2==0)
		className="drawTable2";
		else
		className="drawTable3";
		int nonWin =0;int win = 0;int register = 0;int claim = 0;int outVerify=0;int pndPay=0;int clmPend=0;
			DrawIdBean drawBean = winningBean.getDrawWinList().get(i);
			String[] drawTime= drawBean.getDrawDateTime().split(" ");
			String[] drawTimeArr = drawTime[1].split("\\*");
			String drawStatusForTicket=drawBean.getStatus();
			%>
		<table width="25%" border="0" cellspacing="0" cellpadding="0" class="<%=className%>">
			 <tr><td>Date</td><th><%=drawTime[0]%></th><td>Time</td><th><%=drawTimeArr[0]%></th></tr>
		<%
			boolean isResAwaited = false;double drawAmt = 0.0;double clmPendAmt = 0.0;
			boolean isExpired=false;boolean isVerPending=false;
			List<PanelIdBean> panelWinList =  drawBean.getPanelWinList();
			if(panelWinList!=null && !drawStatusForTicket.equals("DRAW_EXPIRED")){
			//System.out.println("aaaa");
				for(int j=0;j<panelWinList.size();j++){ 
					PanelIdBean panelBean = panelWinList.get(j);
					//System.out.println(panelBean);
					//System.out.println(panelBean.getStatus());
					if(panelBean.getStatus().equals("NON WIN")){
						nonWin+=panelBean.getBetAmtMultiple();
					}else if(panelBean.getStatus().equals("CLAIMED")){
						claim+=panelBean.getBetAmtMultiple();	
					}else if(panelBean.getStatus().equals("PND_PAY")){
						pndPay+=panelBean.getBetAmtMultiple();	
					}else if(panelBean.getStatus().equals("CLAIM_PENDING")){
						clmPend+=panelBean.getBetAmtMultiple();	
						clmPendAmt+=panelBean.getWinningAmt();
					}			
				}
			}else if(drawStatusForTicket.equals("DRAW_EXPIRED")){
							isExpired=true;
			}else if(drawStatusForTicket.equals("VERIFICATION_PENDING")){
							isVerPending=true;
			}else{
							isResAwaited = true;
			}
			totTktAmt = (drawStatusForTicket.equals("CLAIM_PENDING")?0.0:drawAmt)+totTktAmt;
				
			if(isExpired){
			
			%>
			<tr>
			<td>DRAW</td><td>EXPIRED</td></tr>
			
			<%
			}else if(isVerPending){
			
			%>
			<tr>
			<td>VERIFICATION</td><td>PENDING</td></tr>
			
			<%
			}
			
			else if(isResAwaited){
			
			%>
			<tr>
			<td>Result</td><td>Awaited</td></tr>
			
			<%
			}else{
				
				%><%
				if(clmPend!=0){
				%><tr><td colspan="2" align="left"><%=clmPendAmt %> AMOUNT</td><td colspan="2" align="right">CLAIM PENDING</td></tr>
				<%
				}
				if(nonWin!=0){
				%><tr><td colspan="2" align="left"><%=nonWin %> Panel (s)</td><td colspan="2" align="right">TRY AGAIN</td></tr>
				<%
				}if(claim!=0){
				%><tr><td colspan="2" align="left"><%=claim %> Panel (s)</td><td colspan="2" align="right">CLAIMED</td></tr>
				<%
				}if(pndPay!=0){
				%><tr><td colspan="2" align="left"><%=pndPay %> Panel (s)</td><td colspan="2" align="right">IN PROCESS</td></tr>
				<%
				}if(outVerify!=0){}
				
			}
		}%></table>
		<table width="25%" border="0" cellspacing="0" cellpadding="0" class="<%=className%>"><tr><td colspan="2" align="left">Total Pay</td><td colspan="2" align="right"><%=totTktAmt %></td></tr></table>
	
	
	<%}else if (winningBean.getStatus().equals("ERROR")){%>
	
	<table width="25%" border="0" cellspacing="0" cellpadding="0" class="<%=className%>"><tr><td>ERROR! Please Try Again..</td>	</tr></table>
	<%
	}
	%>
	<table width="25%" border="0" cellspacing="0" cellpadding="0" class="<%=className%>"><tr><td>No prize for this Ticket No.</td></tr></table>
	<%
		} else if("RAFFLE".equalsIgnoreCase(pwtTktType)){			
			StringBuilder raffleData = new StringBuilder(" ");
			if(winningBean.getRaffleDrawIdBeanList() != null){
				 UtilApplet.getRaffPWTData(winningBean.getRaffleDrawIdBeanList(), raffleData, 100);
			}
			String raffData=raffleData.toString();
			if (raffleData != null && !raffData.trim().equalsIgnoreCase("null") && raffData.trim().length() > 0) {
				String[] raffPwtArr = raffData.split("Nxt");
				for(int i=0; i<raffPwtArr.length; i++){
					String raffArr = raffPwtArr[i];
					String[] rafflDataArr = raffArr.split("\\~");
					String raffMsg = rafflDataArr[0];
					String raffDrawTime = null;
					if(rafflDataArr.length > 1){
						raffDrawTime = rafflDataArr[1];
					}					
			%>
			<table width="25%" border="0" cellspacing="0" cellpadding="0" class="<%=className%>">
				<tr align="center">
					<td>****************************</td>	
				</tr>
				<tr>
					<td>Raffle Draw Status</td>	
				</tr>
				<tr>
					<td> <%=raffDrawTime %> </td>	
				</tr>
				<tr>
					<td align="center"><%=raffMsg %> </td>	
				</tr>
				
				<tr align="center">
					<td>****************************</td>	
				</tr>
			</table>
			
			<%
			}
		}
		}
		
		
   }
   }
   %>
   <table width="25%" border="0" cellspacing="0" cellpadding="0" class="<%=className%>"><tr><td colspan="2" align="left">Total Pay</td><td colspan="2" align="right"><%=totTktAmt %></td></tr></table>
   <%
  }
	
  
  %>
	
	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: Agt_draw_pwt_dirPlr_Success.jsp,v $
$Revision: 1.3 $
</code>