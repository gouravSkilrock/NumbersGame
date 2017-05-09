<%@ page
	import="java.util.*,com.skilrock.lms.beans.PWTDrawBean,com.skilrock.lms.beans.DrawIdBean,com.skilrock.lms.beans.FortunePurchaseBean,com.skilrock.lms.beans.PanelIdBean"%>
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
				<s:set name="gameType" value="#session.REPRINT_TYPE"></s:set>
				<%
					Object gameBean = null;
					String url = null;
					String retName = null;
					if(request.getAttribute("fortuneBean") != null){
						gameBean = request.getAttribute("fortuneBean");
						url = (String)pageContext.getAttribute("gameType")+".action?gameType=fortune";
					}else if(request.getAttribute("lottoPurchaseBean") != null){
						gameBean = request.getAttribute("lottoPurchaseBean");
						url = (String)pageContext.getAttribute("gameType")+".action?gameType=lotto";
					}else if(request.getAttribute("kenoPurchaseBean") != null){
						gameBean = request.getAttribute("kenoPurchaseBean");
						url = (String)pageContext.getAttribute("gameType")+".action?gameType=keno";
					}
					session.setAttribute("TEMP_BEAN",gameBean);
					retName=(String)session.getAttribute("boReprintRetOrgName");
				 %>
				<s:div id="pwtResult">
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

				<div>
					<applet code="TicketApplet"
						codebase="<%=codebaseBuffer.toString()%>"
						jnlp_href="applets/App.jnlp" width="200" height="500"
						name="TicketApp" mayscript>
					<param name="data" value="108172000002746000" />
					<div style="font-size: 12px; height:auto; line-height: center;">
						<table>
							<tr>
								<td height="300px;" align="center">
									No Java Runtime Environment v 1.5.2 found!!
									<br />
									<a style="color: red"
										href="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/jre-1_5_0_12-windows-i586-p.exe">Click
										to install</a>
								</td>
							</tr>
						</table>
					</div>
					</applet>
				</div>
				<script language="javascript">
				var retName = '<%=retName%>';
				var sampleStatus='<%=(String)application.getAttribute("SAMPLE")%>';
				function updBalIframe(){
					return true;
				} 
		
				function verifyReprint() {
					var url = '<%=url%>';
					_id.i('pwtResult', '<iframe src="'+ url +'" name="tktFrame" id="tktFrame" style="width:0px;height:0px;" frameborder="0"></iframe>');
				
				}
				
				function setAppData(buyData){
					document.applets[0].showStatus(buyData);
				}
				
				function cancelTktAuto(tktNum, totAmt, printStatus) {
					return true;
				}
</script>
	</body>
	
	<script>verifyReprint();</script>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_dm_Ticket_Reprint_Success.jsp,v $
$Revision: 1.2 $
</code>