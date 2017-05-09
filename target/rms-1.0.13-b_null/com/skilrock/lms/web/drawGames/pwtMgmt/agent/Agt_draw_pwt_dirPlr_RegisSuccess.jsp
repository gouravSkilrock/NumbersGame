<%@ page
	import="java.util.*,com.skilrock.lms.dge.beans.PWTDrawBean,com.skilrock.lms.dge.beans.DrawIdBean,com.skilrock.lms.dge.beans.FortunePurchaseBean,com.skilrock.lms.dge.beans.PanelIdBean"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<s:if test="%{#session.plrPwtAppDetMap.isAnonymous}">
					<h3>
						Please pay <s:property value="#application.CURRENCY_SYMBOL" />
						<b><s:property
								value="%{#session.plrPwtAppDetMap.NET_AMOUNT_PAID}" /> </b>
								to the Player .
					</h3>
				</s:if>
				<s:else>
					<h3>
						Please pay <s:property value="#application.CURRENCY_SYMBOL" /> <b><s:property
								value="%{#session.plrPwtAppDetMap.NET_AMOUNT_PAID}" /> </b>
								to 
						<s:property value="%{#session.plrPwtAppDetMap.plrBean.firstName}" />
						&nbsp;
						<s:property value="%{#session.plrPwtAppDetMap.plrBean.lastName}" /> . 

						

					</h3>
				</s:else>
				<s:iterator value="#session.plrPwtAppDetMap">
					<s:if test="%{key=='GAME_NAME'}">
						<s:set name="gameType" value="value"></s:set>
					</s:if>
				</s:iterator>
				<s:div id="pwtResult" >
				</s:div>
				
				<%
					StringBuffer codebaseBuffer = new StringBuffer();
					codebaseBuffer.append(!request.isSecure() ? "http://" : "https://");
					codebaseBuffer.append(request.getServerName());
					if (request.getServerPort() != (!request.isSecure() ? 80 : 443)) {
						codebaseBuffer.append(':');
						codebaseBuffer.append(request.getServerPort());
					}
					codebaseBuffer.append(request.getContextPath());
					codebaseBuffer.append('/');
				%>
				
				<div style="position: absolute; top: 100px; left: 14px;">
					<applet code="TicketApplet" codebase="<%=codebaseBuffer.toString()%>" jnlp_href="applets/App.jnlp" width="200" height="200"	name="TicketApp" mayscript>
										<param name="data" value="108172000002746000" />
											<div style="font-size:12px; height:300px; line-height:center;">
												<table>
													<tr>
													<td height="300px;" align="center">
													No Java Runtime Environment v 1.5.2 found!!<br/>
										        	<a style="color:red" href="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/jre-1_5_0_12-windows-i586-p.exe">Click to install</a>
										        	</td>
										        	</tr>
									        	</table>
									        </div>
					</applet>
				</div>
				<script>
		function verifyPwt() {
		var url = '<s:property value="gameType"/>.action?'+new Date().getTime()+Math.random()*1000000;
		_id.i('pwtResult', '<iframe src='+url+' name="tktFrame" id="tktFrame" style="width:230px;height:300px;" frameborder="0"></iframe>');
			
		}
		verifyPwt();
		function updBalIframe() {
				parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			}
			var verifiedAt = '<s:property value="#session.USER_INFO.orgName"/>';
			var sampleStatus = '<s:property value="#application.SAMPLE"/>';
			function cancelTktAuto(tktNum, totAmt, printStatus) {
				
				return true;
			}
</script>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: Agt_draw_pwt_dirPlr_RegisSuccess.jsp,v $
$Revision: 1.3 $
</code>