<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<s:head theme="ajax" debug="false" />
</head>
<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
<div id="wrap">
			<div id="top_form">
					<h1> Rummy Withdrawal File Upload  </h1>
					<div style="color: red; text-align: left;"><s:actionmessage /></div>
						<s:actionerror />
						<s:form action="olaRummyFileUpload_Success" onsubmit="" enctype="multipart/form-data" method="post">
							<table>
							<tr>
									<td>
											<label class="label">Select Approve Date<span><font color="RED">*</font> </span>:&nbsp;</label>
								<input type="text" name="fromDate" id="fromDate"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12" onchange="resetType()" />
								<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('fromDate'),'yyyy-mm-dd', this, '<s:property value="#session.presentDate"/>','', '')" />
							</td>
				
								
						
						</tr>
									
										<tr>
										<td><table><s:file name="fileUpload" id="fileUpload" label="Select File To Upload" required="true"></s:file> </table></td>
										<tr>
										<td><s:submit></s:submit></td>
										</tr>
			
								</table>
						</s:form>
							<div id="down"></div>
			<div id="result" style="overflow-x:auto;overflow-y:hidden;"></div>
			</div>
</div>			


</body>
</html>		
<code id="headId" style="visibility: hidden">
$RCSfile: bo_ola_file_upload_menu.jsp,v $
$Revision: 1.2 $
</code>