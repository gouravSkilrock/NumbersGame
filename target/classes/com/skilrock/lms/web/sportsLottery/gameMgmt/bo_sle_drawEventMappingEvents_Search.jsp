<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				
			});

			function func() {
				var count = 0;
				var eventSelected = "";
				$('input:checkbox[name=eventList]:checked').each(function() {
					count++;
					eventSelected += ($(this).val())+",";
				});
				eventSelected = eventSelected.substring(0, eventSelected.length-1);
				//alert(count+" - "+eventSelected);
				if(count != parseInt($('#noOfEvents').val())) {
					$('#errorMsgDiv').html('Please Select '+$('#noOfEvents').val()+' Events.');
					return false;
				}

				$('#eventSelected').val(eventSelected);
				return true;
			}
		</script>
	</head>
	<body>
		<div id="wrap">
			<div id="top_form">
				<s:form action="drawEventMappingEventsSubmit" onsubmit="return func();" theme="simple">
					<s:hidden name="gameId" value="%{gameId}" />
					<s:hidden name="gameTypeId" value="%{gameTypeId}" />
					<s:hidden name="drawId" value="%{drawId}" />
					<s:hidden name="noOfEvents" id="noOfEvents" value="%{noOfEvents}" />
					<s:hidden name="eventSelected" id="eventSelected" value="" />
					<div id="errorMsgDiv" style="color:red;">
					</div>
					<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center" style="font-size: 12px">
						<tr>
							<th>S.No</th>
							<th>Event Name</th>
							<th>Event Description</th>
							<th>Start Time</th>
							<th>End Time</th>
							<th>Select</th>
						</tr>
						<s:iterator value="%{eventMasterList}" status="itr">
							<tr>
								<td><s:property value="%{#itr.index+1}"/></td>
								<td><s:property value="%{eventDisplay}" /></td>
								<td><s:property value="%{eventDescription}" /></td>
								<td><s:property value="%{startTime}" /></td>
								<td><s:property value="%{endTime}" /></td>
								<td><input type="checkbox" name="eventList" value='<s:property value="eventId"/>' /></td>
							</tr>
						</s:iterator>
						<tr>
							<td colspan="6" align="center"><s:submit name="submit" value="Submit" cssClass="button" /></td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>