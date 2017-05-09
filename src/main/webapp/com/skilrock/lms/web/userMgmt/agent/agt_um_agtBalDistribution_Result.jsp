<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
				
								
		<s:head theme="ajax" debug="false" />
	</head>
<body >
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
<h3>
						<center><s:property value="#application.TIER_MAP.AGENT" /> Balance Distribution</center>
					</h3>
<br/>  	
	<%double totAmt = 0; double totClmAmt = 0;double totUnClmAmt = 0; double totBal=0.0;
	UserInfoBean userBean = (UserInfoBean)session.getAttribute("USER_INFO");
	totBal= userBean.getAvailableCreditLimit()-userBean.getClaimableBal();%>
	 <table width="95%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 	<tr>
	 		<th ><s:property value="#application.TIER_MAP.AGENT" /> Name</th>
	 		<th >Usable Limit</th>
			</tr>	
			
		
			<s:set name="userBean" value="#session.USER_INFO"/>
			<s:set name="agtAmt" value="#userBean.availableCreditLimit"/>
			<tr>			
				<td width="10%"  align="left" nowrap="nowrap"><s:property value="#userBean.orgCode" /></td>	
				<td width="10%"  align="left" nowrap="nowrap"><%=FormatNumber.formatNumberForJSP(totBal )%></td>		
	     	</tr>
	</table>
	 <br/>
	 <br/>
	 <br/>
	 <table width="95%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 	<tr>
	 		<th colspan="4" align="center"><h3><s:property value="#application.TIER_MAP.RETAILER" />'s List</h3></th>
	 	</tr>
	 	<tr>
	 		<th>S.No</th>
	 		<th ><s:property value="#application.TIER_MAP.RETAILER" /> Name</th>
	 		<th >Usable Limit</th>
    	</tr>	
	
		
		<s:if test="#session.agtBalDistMap.size>0">
			<s:iterator  value="#session.agtBalDistMap"  status="taskIndex">
			<s:set name="retBean" value="value" />
			<s:set name="availableCredit" value="#retBean.availableCredit" />
			<%if((Double)pageContext.getAttribute("availableCredit")>0)
			totAmt = totAmt+(Double)pageContext.getAttribute("availableCredit");
			%>
			<tr class="startSortable">				
				<td width="5%"><s:property value="#taskIndex.index+1" /></td>
				<td width="45%"  align="left" nowrap="nowrap"><s:property value="#retBean.orgName" /></td>	
				<td width="50%"  align="left" nowrap="nowrap"><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("availableCredit")) %></td>				
			</tr>
			</s:iterator> 
			<tr>				
				<td>&nbsp;</td>
				<td width="10%"  align="left" nowrap="nowrap"><h2>Total</h2></td>	
				<td width="10%"  align="left" nowrap="nowrap"><h3><%=FormatNumber.formatNumberForJSP(totAmt) %></h3></td>				
			</tr>
			
		</s:if>
		<s:else>
			<tr>
				<td colspan="9" align="center"> No Records to Process</td>
			</tr>
		</s:else>
		
	 </table>
	 <br/>
	 <%String msg = "";if(((Double)pageContext.getAttribute("agtAmt")-totAmt)<=0){ 
	 msg="You Cannot Distribute any Amount";
	 }else{
	msg=" You Can Distribute "+FormatNumber.formatNumberForJSP(totBal-totAmt)+" Amount Only";
	 }
	 %>
	 <table width="100%"><th align="center"><h2><%=msg%></h2></th></table>
	 
	 </div>
	 </body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_um_agtBalDistribution_Result.jsp,v $
$Revision: 1.2 $
</code>