<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.filter.LMSFilterDispatcher"%>

<s:set name="statusBean" value="columnStatus" />
<s:hidden id="SE" name="#statusBean.SE" />
<s:hidden id="DG" name="#statusBean.DG" />
<s:hidden id="SLE" name="#statusBean.SLE" />
<s:hidden id="IW" name="#statusBean.IW" />
<s:hidden id="VS" name="#statusBean.VS" />
<s:hidden id="Login" name="#statusBean.Login" />

<div id="reportData">
	<s:iterator value="retActivityMap">
			<s:set name="retBean" value="value" />
			<s:property value="#retBean.retAddress" />:
			<s:property value="#retBean.retName" />:
			<s:property value="#retBean.retCoordinate" />:			
			<s:property value="#retBean.retName" />:
			<s:property value="#retBean.retParentName" />:
			<s:property value="#retBean.location.toUpperCase()" />:
			<s:property value="#retBean.terminalId" />:
			<s:property value="#retBean.currentVersion" />:
			<s:property value="#retBean.lastConDevice" />:
			<s:property value="#retBean.orgStatus" />:
			<s:property value="#retBean.loginStatus" />:
			<s:date name="#retBean.lastHeartBeatTime" format="yyyy-MM-dd HH-mm-ss" />:
			<s:date name="#retBean.login" format="yyyy-MM-dd HH-mm-ss" />:
				<%if(LMSFilterDispatcher.getIsDraw().equals("YES")){ %>
			<s:date name="#retBean.drawSale" format="yyyy-MM-dd HH-mm-ss" />:
			<s:date name="#retBean.drawPwt" format="yyyy-MM-dd HH-mm-ss" />:
			<s:property value="#retBean.days" />:
			<% }%>
			<%if(LMSFilterDispatcher.getIsScratch().equals("YES")){ %>
			<s:date name="#retBean.scratchSale" format="yyyy-MM-dd HH-mm-ss" />:
			<s:date name="#retBean.scratchPwt" format="yyyy-MM-dd HH-mm-ss" />:
			<% }%>
			<%if(LMSFilterDispatcher.getIsIW().equals("YES")){ %>
			<s:date name="#retBean.iwSale" format="yyyy-MM-dd HH-mm-ss" />:
			<s:date name="#retBean.iwPwt" format="yyyy-MM-dd HH-mm-ss" />:
			<s:property value="#retBean.iwDays" />:
			<% }%>
			<%if(LMSFilterDispatcher.getIsVS().equals("YES")){ %>
			<s:date name="#retBean.vsSale" format="yyyy-MM-dd HH-mm-ss" />:
			<s:date name="#retBean.vsPwt" format="yyyy-MM-dd HH-mm-ss" />:
			<s:property value="#retBean.vsDays" />:
			<% }%>
			<%if(LMSFilterDispatcher.getIsSLE().equals("YES")){ %>
			<s:date name="#retBean.sleSale" format="yyyy-MM-dd HH-mm-ss" />:
			<s:date name="#retBean.slePwt" format="yyyy-MM-dd HH-mm-ss" />:
			<s:property value="#retBean.sleDays" />: , 
			<% }else{%>,
			<%
			}
			%>
	</s:iterator>
</div>
<div id="curDate"><%=new java.util.Date().getTime()%></div>
<div id="repGenTime"><%=new java.util.Date()%></div>

<code id="headId" style="visibility: hidden">
	$RCSfile: agt_rep_new_retActivity_Result.jsp,v $ $Revision: 1.3 $
</code>