<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>

		<s:head theme="ajax" debug="false" />
		<%java.util.Calendar calendar= java.util.Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.set(calendar.HOUR_OF_DAY,23);
			calendar.set(calendar.MINUTE,59);
			calendar.set(calendar.SECOND,59);%>
		<%response.setDateHeader("Expires",calendar.getTimeInMillis());%>
		
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/backOffice/js/rgRep.js"></script>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var path="<%=request.getContextPath()%>";</script>
		<script>var projectName="<%=request.getContextPath()%>";</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	</head>
	<body onload="getAgentList();getActiveRGLimitList();">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="Responsible_Gaming"/> <s:text name="Report"/>
				</h3>


				<s:form name="retActivity" id="retActivity" onsubmit="return getRGData()">

					<table border="0" width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td  align="right">
								<i>Select <s:property value="#application.TIER_MAP.AGENT" /> Organization :</i>
							</td>
							<td  nowrap="nowrap" colspan="2" >

								<s:select theme="simple" id="agtOrgName" cssClass="option" 
									list="{}" name="organization_Name" headerKey="-1" headerValue="ALL"/>
								<div id="orgLoading"  style="position: absolute;"></div>
										
							</td>
					
						</tr>
						<tr><td  align="right">
								</br>
							</td>
							<td  nowrap="nowrap" colspan="2" >

									</br>	
							</td></tr>
						<tr>
							<td align="right">
							<i>Criteria Type:</i> 
							</td>
							<td   nowrap="nowrap" colspan="2" >
						<s:select theme="simple" id="filter" cssClass="option" 
									list="#{'Daily':'DAILY','Weekly':'WEEKLY', 'All':'Date Wise'}" name="filter" onchange="showDates();" />
								<div id="orgLoading" style="position: absolute;"></div></td></tr></table>
				
</br>

								
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="center">
								<div id="date" style="display: none;">
									<table align="center" width="400px">
									<%  	Calendar prevCal= Calendar.getInstance(); 
											String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", (String)session.getAttribute("date_format"));
											String endDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis() - 24*60*60*1000).toString(), "yyyy-mm-dd", (String)session.getAttribute("date_format"));
											application.setAttribute("startD",startDate);
											application.setAttribute("endD",endDate);
									 %>
									 		
											<tr>
												<td>
													<label class="label" >Start Date<span>*</span>:&nbsp;</label>
										    		<input  type="text" name="start_date" id="start_date" value="<s:property value="#application.endD"/>" readonly size="12">
										    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('start_date'),'dd-mm-yyyy', this, '<s:property value="#application.endD"/>', '<s:property value="%{archDate}"/>', '<s:property value="#application.endD"/>')" />
										    	</td>
										    </tr>	
											<tr>
												<td>
													<label class="label">End Date<span>*</span>:&nbsp;</label>
										    		<input  type="text" name="end_Date" id="end_date" value="<s:property value="#application.endD"/>" readonly size="12">
										    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('end_date'),'dd-mm-yyyy', this, '<s:property value="#application.endD"/>', '<s:property value="%{archDate}"/>', '<s:property value="#application.endD"/>')" />
										   		</td>
										  </tr>
										</table>
								</div>
							</td>
						</tr>
						<tr>
							<td nowrap="nowrap" colspan="2"><div id="limits"></div></td>
						</tr>
						<tr>
							<td><s:submit name="search" value="Search" align="center"  targets="reportDiv" theme="ajax" cssClass="button" /></td>
						</tr>
					</table>
				</s:form>
				<div id="reportDiv"></div>
			</div>
		</div>

	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_RG_Report_Menu.jsp,v $
$Revision: 1.3 $
</code>