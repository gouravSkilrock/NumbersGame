<%@ page import="java.util.*,com.skilrock.lms.dge.beans.PWTDrawBean,com.skilrock.lms.dge.beans.DrawIdBean,com.skilrock.lms.dge.beans.FortunePurchaseBean,com.skilrock.lms.dge.beans.PanelIdBean,com.skilrock.lms.dge.beans.RaffleDrawIdBean,com.skilrock.lms.web.drawGames.common.Util" %>
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
	margin:4px 0 4px 37.5%;
}

.drawTable2{
	background:#FEEBF3;
	border-bottom:1px dotted #FDD7E7;
	padding:3px;
	margin:4px 0 4px 37.5%;
}

.drawTable3{
	background:#E1FFF0;
	border-bottom:1px dotted #C4FFE1;
	padding:3px;
	margin:4px 0 4px 37.5%;
}

.drawTable4{
	background:#FFFFD7;
	border-bottom:1px dotted #FFFFB9;
	padding:3px;
	margin:4px 0 4px 37.5%;
}

</style>
	</head>

<body >
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

 
  <%
  String className="drawTable1";
  
  
  //PWTDrawBean winningBean =(PWTDrawBean)session.getAttribute("PWT_RES");
  
  	MainPWTDrawBean mainPWTBean = (MainPWTDrawBean)session.getAttribute("PWT_RES");
  	if("UN_AUTH".equalsIgnoreCase(mainPWTBean.getStatus())) {
%><h1><s:text name="msg.you.unauth.to.claim.tkt"/></h1>
	<% 
	 } else{

  	if(!mainPWTBean.isValid() || "CANCELLED".equalsIgnoreCase(mainPWTBean.getStatus())){
  		%>
			<table width="25%" border="0" cellspacing="0" cellpadding="0" align="center" class="light_border_dgray">
            <tr><td align="center" colspan="2" class="pwtjspTicket"><%=application.getAttribute("ORG_NAME_JSP") %></td></tr>
            <tr><td align="center" colspan="2" class="pwtjspTicket"><s:text name="label.win.verify"/></td></tr></table>
            <table width="25%" border="0" cellspacing="0" cellpadding="0" class="<%=className%>"><tr><td>
            <s:text name="label.TicNo"/> :</td><td> <%=mainPWTBean.getTicketNo()%></td></tr></table>			
		    <table width="25%" border="0" cellspacing="0" cellpadding="0" align="center" class="<%=className%>"><tr><td><s:text name="label.inv.tkt"/></td></tr></table>
			<%
  	}  else {
  		List<PWTDrawBean> winningBeanList = mainPWTBean.getWinningBeanList();
	double totTktAmt = 0.0;
  	if(winningBeanList != null){
  		 for(int k=0; k<winningBeanList.size(); k++){
  			 PWTDrawBean winningBean = winningBeanList.get(k);
			String pwtTktType = winningBean.getPwtTicketType();
			if("DRAW".equalsIgnoreCase(pwtTktType)){
			%>
		
		<table  width="25%" border="0" cellspacing="0" cellpadding="0" align="center"  class="light_border_dgray">
            <tr><td align="center" colspan="2" class="pwtjspTicket"><%=application.getAttribute("ORG_NAME_JSP") %></td></tr>
            <tr><td align="center" colspan="2" class="pwtjspTicket"><s:text name="label.win.verify"/></td></tr></table>
 <table  width="25%" border="0" cellspacing="0" cellpadding="0" align="center"  class="<%=className%>"><tr><td>
 <s:text name="label.TicNo"/> :</td><td> <%=winningBean.getTicketNo()%><%=(winningBean.getReprintCount()==null)? " " :winningBean.getReprintCount() %></td></tr></table>

<%
		
		
		
		
		if(!winningBean.isValid()){
				if("TICKET_EXPIRED".equalsIgnoreCase(winningBean.getStatus())){
				%>
			 <table  width="25%" border="0" cellspacing="0" cellpadding="0" align="center"  class="<%=className%>"><tr><td><s:text name="label.tkt.expired"/>.</td></tr></table>
			<%
				}else {
			%>
			 <table  width="25%" border="0" cellspacing="0" cellpadding="0" align="center"  class="<%=className%>"><tr><td><s:text name="label.inv.tkt"/></td></tr></table>
			<%
			}	}else if (winningBean.getStatus().equals("SUCCESS")){
			
		for(int i=0;i<winningBean.getDrawWinList().size();i++){
		
		if(i%2==0)
		className="drawTable2";
		else
		className="drawTable3";
		int nonWin =0;int win = 0;int register = 0;int claim = 0;int outVerify=0;int pndPay=0;int clmPend=0;
			DrawIdBean drawBean = winningBean.getDrawWinList().get(i);
			String drawStatusForTicket=drawBean.getStatus();
			System.out.print("STS:-"+drawStatusForTicket);
			String[] drawTime= drawBean.getDrawDateTime().split(" ");
			String[] drawTimeArr = drawTime[1].split("\\*");
			%>
		<table  width="25%" border="0" cellspacing="0" cellpadding="0" align="center"  class="<%=className%>">
			 <tr><td><s:text name="label.date"/></td><th><%=drawTime[0]%></th><td><s:text name="label.time"/></td><th><%=drawTimeArr[0]%></th></tr>
		<%
			boolean isResAwaited = false;double drawAmt = 0.0;double clmPendAmt = 0.0;
			boolean isVerPending=false;boolean isExpired=false;
			List<PanelIdBean> panelWinList =  drawBean.getPanelWinList();
			if(panelWinList!=null && !drawStatusForTicket.equals("DRAW_EXPIRED")){
			//System.out.println("aaaa");
				for(int j=0;j<panelWinList.size();j++){ 
					PanelIdBean panelBean = panelWinList.get(j);
					//System.out.println(panelBean);
					//System.out.println(panelBean.getStatus());
					if(panelBean.getStatus().equals("NON WIN")){
						nonWin++;
					}else if(panelBean.getStatus().equals("CLAIMED")){
						claim++;	
					}else if(panelBean.getStatus().equals("PND_PAY")){
						pndPay++;	
					}else if(panelBean.getStatus().equals("PND_MAS")){
						pndPay++;	
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
			<td><s:text name="DRAW"/></td><td><s:text name="EXPIRED"/></td></tr>
			<%
			}else if(isVerPending){
			%>
			<tr>
			<td><s:text name="VERIFICATION"/></td><td><s:text name="PENDING"/></td></tr>
			<%
			}else if(isResAwaited){
			%>
			<tr>
			<td><s:text name="label.res"/></td><td><s:text name="label.await"/></td></tr>
			<%
			}else{
				%><%
				if(clmPend!=0){
				%><tr><td colspan="2" align="left"><%=clmPendAmt %> <s:text name="label.win.tkt.verify"/><s:text name="AMOUNT"/></td><td colspan="2" align="right"><s:text name="CLAIM.PENDING"/></td></tr>
				<%
				}if(nonWin!=0){
				%><tr><td colspan="2" align="left"><%=nonWin %> <s:text name="label.panel"/> (s)</td><td colspan="2" align="right"><s:text name="TRY.AGAIN"/></td></tr>
				<%
				}if(claim!=0){
				%><tr><td colspan="2" align="left"><%=claim %> <s:text name="label.panel"/> (s)</td><td colspan="2" align="right"><s:text name="CLAIMED"/></td></tr>
				<%
				}if(pndPay!=0){
				%><tr><td colspan="2" align="left"><%=pndPay %> <s:text name="label.panel"/> (s)</td><td colspan="2" align="right"><s:text name="IN.PROCESS"/></td></tr>
				<%
				}if(outVerify!=0){}
			}
		}%></table>
	<%}else if (winningBean.getStatus().equals("ERROR")){%>
	<table  width="25%" border="0" cellspacing="0" cellpadding="0" align="center" class="<%=className%>"><tr><td><s:text name="error.try.again"/>..</td></tr></table>
	<%
	}
	%>
	<table  width="25%" border="0" cellspacing="0" cellpadding="0" align="center" class="<%=className%>"><tr><td><s:text name="msg.no.prize.for.this.tkt"/>.</td></tr></table>
	
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
			<table  width="25%" border="0" cellspacing="0" cellpadding="0" align="center"  class="<%=className%>">
				<tr align="center">
					<td>****************************</td>	
				</tr>
				<tr>
					<td><s:text name="label.raff.draw.status"/></td>	
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
  <table  width="25%" border="0" cellspacing="0" cellpadding="0" align="center"  class="<%=className%>"><tr><td colspan="2" align="left"><s:text name="label.total.pay"/></td><td colspan="2" align="right"><%=totTktAmt %></td></tr></table>
	
	
  	<%}} %>
	

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_draw_pwt_dirPlr_Success.jsp,v $
$Revision: 1.3 $
</code>