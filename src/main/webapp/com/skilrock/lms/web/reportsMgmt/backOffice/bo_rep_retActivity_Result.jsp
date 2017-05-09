<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%!boolean isDrawPwt = true; //2
	boolean isCancel = false; //3
	boolean isScratchPwt = false; //4
	boolean isLogin = false; //5 
	boolean isLastTrx = false; //5
	boolean isCurrentVersion = true;
	boolean isTerminalId = true;
	boolean isOfflineStatus = true;%>
<div id="statusDiv"><% if(isOfflineStatus){
	%>isOfflineStatus = true;<%
		}
		if(isCurrentVersion){
	%>isCurrentVersion = true;<%
		}
		if(isTerminalId){
	 %>isTerminalId = true;<%
	 	}
		if (isDrawPwt) {
	%>isDrawPwt = true;<%
		}
		if (isCancel) {
	%>isCancel = true;<%
		}
		if (isScratchPwt) {
	%>isScratchPwt = true;<%
		}
		if (isLogin) {
	%>isLogin = true;<%
		}
		if (isLastTrx) {
	%>isLastTrx=true;<%
		}
	  %></div>
<!-- Name:Draw Sale:Draw PWT:Draw Cancel:Scratch PWT:Login-->
<div id="reportData">

	<s:iterator value="#session.RetActivityMap">
		<s:set name="retBean" value="value" />
		<s:property value="#retBean.retName" />:<s:property
			value="#retBean.retParentName" />:<s:property
			value="#retBean.location.toUpperCase()" />:
			<%
				if (isOfflineStatus) {
			%>
			<s:property value="#retBean.offlineStatus" />:
			 <%
			 }
				if (isCurrentVersion) {
			%>
			<s:property value="#retBean.currentVersion" />:
			<%
			}
			if (isTerminalId) {
			%>
			<s:property value="#retBean.terminalId" />:
			<%} %>
			<s:property value="#retBean.drawSale" />:
			<%
			if (isDrawPwt) {
		%>
		<s:property value="#retBean.drawPwt" />:
			<%
			}
				if (isCancel) {
		%>
		<s:property value="#retBean.drawCancel" />:
			<%
			}
				if (isScratchPwt) {
		%>
		<s:property value="#retBean.scratchPwt" />:
			<%
			}
				if (isLogin) {
		%>
		<s:property value="#retBean.login" />:
			<%
			}
		%>

		<s:property value="#retBean.orgStatus" />:,
			
</s:iterator>
</div>
<div id="curDate"><%=new java.util.Date().getTime()%></div>
<div id="repGenTime"><%=new java.util.Date()%></div>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_retActivity_Result.jsp,v $
$Revision: 1.2 $
</code>