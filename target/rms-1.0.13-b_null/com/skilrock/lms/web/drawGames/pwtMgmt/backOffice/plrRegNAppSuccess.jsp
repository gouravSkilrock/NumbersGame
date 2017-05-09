<%@ page import="java.util.*,com.skilrock.lms.dge.beans.PWTDrawBean,com.skilrock.lms.dge.beans.DrawIdBean,com.skilrock.lms.dge.beans.PanelIdBean" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

<div id="wrap"><div id="top_form">

  <h3>
	    Player
	  	<s:property value="%{#session.plrPwtAppDetMap.plrBean.firstName}"  /> &nbsp;
	  	<s:property value="%{#session.plrPwtAppDetMap.plrBean.lastName}"  />
	  	has beean Registered For Direct Player Winning Process.
  </h3>	  	
	
	<b>Ticket No.</b><b><s:property value="#session.pwtDrawBean.ticketNo"/></b>
	<br></br>
	  
<%
		PWTDrawBean winningBean =(PWTDrawBean)session.getAttribute("pwtDrawBean");
		
		
		double totTktAmt = 0.0;int totRegister = 0;
		if(!winningBean.isValid()){
			%>
			<tr><td align="left"><b>Invalid Ticket</b></td></tr>
			<%
				}else if (winningBean.getStatus().equals("SUCCESS")){
			
		for(int i=0;i<winningBean.getDrawWinList().size();i++){
		int nonWin =0;int win = 0;int claim = 0;
			DrawIdBean drawBean = winningBean.getDrawWinList().get(i);
			String drawTime = drawBean.getDrawDateTime();
			boolean isResAwaited = false;double drawAmt = 0.0;
			List<PanelIdBean> panelWinList =  drawBean.getPanelWinList();
			if(panelWinList!=null){
			
				for(int j=0;j<panelWinList.size();j++){ 
					PanelIdBean panelBean = panelWinList.get(j);
					
					if(panelBean.getStatus().equals("NON WIN")){
						nonWin++;
					}else if(panelBean.isValid()){
						drawAmt = panelBean.getWinningAmt()+drawAmt;
						win++;
					}else if(panelBean.getStatus().equals("CLAIMED")){
						claim++;	
					}
				
				}
			}else{isResAwaited = true;
				}
			totTktAmt = drawAmt+totTktAmt;
						
			if(isResAwaited){
			%>Draw <%=drawTime %>
			<table border="1" cellpadding="0" cellspacing="0" width="60%" ><tr><td align="left" colspan="3"><b>Result Awaited</b></td></tr></table><br></br><%
			}else{
				%><b>Draw</b><b> <%=drawTime %></b>
				<table border="1"   cellpadding="0" cellspacing="0" width="60%" >
				<tr><th>No Of Panels</th><th>Status</th></tr>
				<%
				if(nonWin!=0){
				%><tr><td><b><%=nonWin %>&nbsp;</b></td><td ><b>TRY AGAIN</b></td></tr><%
				}if(win!=0){
				%><tr><td  ><b><%=win %>&nbsp;</b></td><td ><b>WIN <%=drawAmt %> $</b></td></tr><%
				}if(claim!=0){
				%><tr><td ><b><%=claim %>&nbsp;</b></td><td ><b>CLAIMED</b></td></tr><%
				}
				%></table><br/><%
			};
		}
	}else if (winningBean.getStatus().equals("ERROR")){
	%>
	<tr><td  colspan="3">ERROR! Please Try Again..</td></tr>	
	<%
	}
	%>
    <tr><td align="left"><b>Total</td><td align="left" ><b>&nbsp;Winning&nbsp;&nbsp;</b></td><td align="right"><b><%=totTktAmt %></b></td></tr>	

	<br></br>
	
	<div id="first">
	 
			 <table  border="0" cellpadding="2" cellspacing="0"  width="400px">
			 	
			 	<tr>	
				 	<td>
				 		Receipt Id : 
				 		 <a  href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action" target="_blank">
				 			<s:property value="#session.plrPwtAppDetMap.recId"  />
				 		</a>
				 	</td>			
				</tr>		
			 </table>		 

	</div>
	
</div></div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: plrRegNAppSuccess.jsp,v $
$Revision: 1.4 $
</code>