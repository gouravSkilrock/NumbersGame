<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.dge.beans.PWTDrawBean"%>
<%@page import="com.skilrock.lms.dge.beans.DrawIdBean"%>
<%@page import="com.skilrock.lms.dge.beans.PanelIdBean"%>
<%@page import="java.util.List"%>

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
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>
     var path = "<%=request.getContextPath() %>";
   </script>
   
   
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/gameTktVirnvalidate.js"></script>
   <script>
   
     function changeSubmitButton(gameType){
      //alert(gameType);
    
     if(gameType=="Draw")
     {        
        document.getElementById('scratchTable').style.display="none";
        document.getElementById('drawGameDiv').style.display="block";
        document.getElementById('drawRadio').checked="true";
        
     }else
     {
       document.getElementById('scratchTable').style.display="block";
        document.getElementById('drawGameDiv').style.display="none";
        document.getElementById('scratchRadio').checked="true";
     }
    
    }
   
   
     function _subTrans(){
			return ( verifyNSave() && _subValid('subTrans'));
		}
   </script>
 
</head>

<body onload="gameDetails('RETAILER')">
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

<div id="wrap"><div id="top_form">

  <h3><s:text name="label.pwt.tkt.virn.varification"/></h3>	
	<div id="first">
	  <s:form id="ab" name="tv"> 		
			
		<table width="35%">	<tr><td></td><td align="center"> <s:text name="label.game.type"/>: <input type="radio" name="gameType" id="scratchRadio" value="Scratch" onclick="changeSubmitButton(this.value)"  checked="checked"/><s:text name="label.scratch"/> </td></tr> 
			<!--  <input type="radio" name="gameType" value="Draw" id="drawRadio" onclick="changeSubmitButton(this.value)" />Draw			
			 -->
		</table>	 
			 <table id="scratchTable" border="0" cellpadding="2" cellspacing="0"  width="400px">
					 	<tr>				
							<td colspan="2"><div style="color: red; text-align: center" id="gName_e"></div></td>
						</tr>
					 	
					 	<tr>				
							<td><s:select name="gameIdNbrName" key="label.game.name" headerKey="-1" list="{}" cssClass="option"
							headerValue="%{getText('label.please.select')}" id="game_name" /></td>
						</tr>
						<tr>				
							<td colspan="2"><div style="color: red; text-align: center" id="tktNo_e"><s:property value="pwtErrorMap.tktBean.status" /></div></td>
						</tr>
						<tr>				
							<td><s:textfield name="ticketNbr" id="ticketNbr" key="label.tckt.no" /></td>
						</tr>
						<tr>				
							<td colspan="2"><div style="color: red; text-align: center" id="virnNbr_e"><s:property value="pwtErrorMap.pwtBean.message" /></div></td>
						</tr>
						<tr>				
							<td><s:textfield name="virnNbr" id="virnNbr" key="label.virn.nbr" /></td>
						</tr>	
						
						<tr>
							<td>&nbsp;</td>					
							<td>
								 <div id="subCheck" style="position:absolute;"></div>
								 <table>	
								 	<s:submit  formId="ab" id="subTrans" key="btn.verifyandsave" align="right" action="pwt_BODirectplr_verifyTicketNVirn" cssClass="button" onclick="return _subTrans()" onkeypress="return _subTrans()" />
								 </table>
						   </td>
						</tr>	
								
			 </table>
				
				
				
				
<div id="drawGameDiv" style="display: none;">				
<table id="drawTable">		    
				       
	 <tr>				
		<td align="right"><br /></td>
	 </tr>
						
						
			<s:if test="%{gameType=='Draw'}">			
			 <script>changeSubmitButton('Draw')</script>
				<%
					PWTDrawBean winningBean =(PWTDrawBean)session.getAttribute("pwtDrawBean");		
					double totTktAmt = 0.0;int totRegister = 0;
					if(!winningBean.isValid()){		
				%>
						<tr><td align="left"><b>Invalid Ticket</b><br /></td></tr>
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
						%><tr><td>Draw <%=drawTime %><br /></td></tr>
						<table border="1" cellpadding="0" cellspacing="0" width="60%" ><tr><td align="left" colspan="3"><b><s:text name="label.res.await"/></b><br /></td></tr></table><br></br><%
						}else{
							%><tr><td><b>Draw</b><b> <%=drawTime %></b><br /></td></tr>
							<table border="1"   cellpadding="0" cellspacing="0" width="60%" >
							<tr><th>No Of Panels<br /></th><th>Status<br /></th></tr>
							<%
							if(nonWin!=0){
							%><tr><td><b><%=nonWin %>&nbsp;</b><br /></td><td ><b><s:text name="TRY.AGAIN"/></b><br /></td></tr><%
							}if(win!=0){
							%><tr><td  ><b><%=win %>&nbsp;</b><br /></td><td ><b><s:text name="WIN"/> <%=drawAmt %> $</b><br /></td></tr><%
							}if(claim!=0){
							%><tr><td ><b><%=claim %>&nbsp;</b><br /></td><td ><b><s:text name="CLAIMED"/></b><br /></td></tr><%
							}
							%></table><br><%
						};
					}
				}else if (winningBean.getStatus().equals("ERROR")){
				%>
				<tr><td  colspan="3"><s:text name="label.error.pls.try.again"/><br /></td></tr>	
				<%
				}
				%>				
			</s:if>		
									
		<tr>
			<td>&nbsp;<br /></td>					
			<td>
			 <div id="subCheck" style="position:absolute;"></div>
				 <table>
					 <s:submit  formId="ab" id="subTransDraw" key="btn.vrfyNsave.draw" align="right" action="pwtDraw_BODirectplr_verifyTicket" cssClass="button" />
				 </table>
		  </td>
		</tr>							
														
</table>
				
	</div>				
					 
	  </s:form> 
	</div>
	
	
</div></div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: BODirectPlrPWTHome.jsp,v $
$Revision: 1.3 $
</code>