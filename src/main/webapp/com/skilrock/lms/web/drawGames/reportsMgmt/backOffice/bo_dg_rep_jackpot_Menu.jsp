
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String deployMentDate = (String) application
			.getAttribute("DEPLOYMENT_DATE");
	deployMentDate = deployMentDate.replaceAll("-", "/");	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

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
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>	
		
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/reportsMgmt/backOffice/js/report.js"></script>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>			
								
		<s:head theme="ajax" debug="false" />
	</head>
	<body style="overflow: none">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
					<h3>
						Jackpot View
					</h3>
					<s:form name="jackpotForm" action="getJackpotView" id="jackpotFormId" onsubmit="return validateDates()">

						<table cellspacing="0" cellpadding="3" border="0">
							<s:select list="#session.GAME_NOS" name="gameName"
								      label="Game Name" id="game_no" cssClass="option" onchange="setGameId()"/>
												
										<input type="text" name="gameId" size="10"  id="gameId" value=""
						               	style="display:none"/>
						               	<script>setGameId();</script><s:textfield name="drawId" label="Draw Id" size="10"  id="draw_id" value=" "
						               	cssClass="option"/><div id="draw_err">&nbsp;</div>
						     
						    <tr><td><div id="dates"></div></td></tr>
							<tr>
								
								<td colspan="2" align="center">
									<s:set name="stDate" value="#session.CURR_TIME"></s:set>
									<s:hidden id="hidStDate" name="hidStDate" value="%{stDate}"></s:hidden>
									<s:div theme="ajax" href="rep_common_fetchDate.action"></s:div>
								</td>
								
							</tr>
					       
					        <s:submit align="center" theme="ajax" value="Search" cssClass="button"
											targets="drawDiv"  />
						
		</table>
		</s:form>

		
		

		<div id="drawDiv">
       </div>
		</div>
		
		
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_dg_rep_jackpot_Menu.jsp,v $
$Revision: 1.3 $
</code>