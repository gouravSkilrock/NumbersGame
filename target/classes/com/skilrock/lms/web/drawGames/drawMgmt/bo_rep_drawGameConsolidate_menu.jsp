<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<s:head theme="ajax" debug="false"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet"	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"	type="text/css" />
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<script>var projectName="<%=request.getContextPath()%>"</script>
<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/slider/js/jquery-1.3.2.min.js"></script>
<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/drawMgmt/js/bo_rep_drawGameConsolidate.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
</head>
<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="label.draw.game.consolidate" /> <s:text name="Report"></s:text>
				</h3>


				<s:form name="searchConsolidateData" action="bo_rep_drawGameConsolidate_Search" onsubmit="return validate()">

					<table border="0" cellpadding="2" cellspacing="0" width="400" style="width:100%;vertical-align:middle; ">
						<tr>
							<td align="left" colspan="2">
								<div id="error"></div>
							</td>
						</tr>
						<tr>
							<td align="left" colspan="2">
							<div style="color: red; text-align: center" id="gameNo_e"></div>
								
							</td>
						</tr>
						
						<tr>
									
							<td colspan="2">
								<s:text name="label.game.name" />:
								<s:select name="gameNo" id="gameNo" theme="simple" headerKey="-1" headerValue="%{getText('label.please.select')}"
									 list="#session.DRAWGAME_LIST"
									cssClass="option"
									/>
							</td>
							
							
						</tr>
						<tr>
							<td align="left" colspan="2">
							<div style="color: red; text-align: center" id="date_e"></div>
								
							</td>
						</tr>
						
						<tr>
							<td colspan="2">
								<table cellpadding="3"
									cellspacing="0" border="0">
									<s:set name="stDate" id="stDate" value="#session.presentDate" />
									
									<tr>
										<td>
											<label class="label">
												<s:text name="label.start.date" />
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="start_date" id="start_date"
												value="<s:property value="#session.presentDate"/>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this,'<s:property value="#session.presentDate"/>', false, '')" />
										</td>
									</tr>
									<tr>
										<td>
											<label class="label">
												<s:text name="label.end.date" />
												<span>*</span>:&nbsp;
											</label>
											<input type="text" name="end_Date" id="end_Date"
												value="<s:property value="#session.presentDate"/>" readonly size="12">
											<input type="button"
												style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
												onclick="displayCalendar(document.getElementById('end_Date'),'yyyy-mm-dd', this,'<s:property value="#session.presentDate"/>', false, '')" />
										</td>
									</tr>
									
									
								</table>
							</td>
						</tr>
							
						<tr>
							<td colspan="2">
								<s:submit name="search" key="btn.srch" align="center"
									targets="down" theme="ajax" cssClass="button"/>
							</td>
						</tr>

					</table>
				</s:form>

				<div id="down" style="text-align: center"></div>

				<br />
				<br />
			</div>
		</div>

</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_drawGameConsolidate_menu.jsp,v $ $Revision: 1.3 $
</code>
