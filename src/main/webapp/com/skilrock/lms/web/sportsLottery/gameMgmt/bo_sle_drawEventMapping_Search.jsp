<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript">
			$(document).ready(function() {
				
			});
		</script>
	</head>
	<body>
		<div id="errorDiv" style="color: red;">
			<s:actionmessage />
		</div>
		<div id="wrap">
			<div id="top_form">
				<s:form action="drawEventMappingEventsSearch" onsubmit="return formSubmit(this.id,'result');" theme="simple">
					<s:hidden name="gameId" value="%{gameId}" />
					<s:hidden name="gameTypeId" value="%{gameTypeId}" />
					<table width="450" border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td align="right">
								Current Draw List
							</td>
							<td align="left">
								<s:select id="drawId" headerKey="-1" headerValue="--Please Select--"
									name="drawId" list="%{drawMasterList}" listKey="drawId"
									listValue="drawName" cssClass="option"></s:select>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<s:submit name="submit" value="Submit" align="center"
									targets="down" theme="ajax" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>