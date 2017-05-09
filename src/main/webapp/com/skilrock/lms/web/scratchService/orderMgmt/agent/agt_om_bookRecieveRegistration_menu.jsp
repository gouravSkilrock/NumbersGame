<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>

		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />


		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head theme="ajax" debug="false" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/inventoryMgmt/retailer/js/ret_activateBooks.js"></script>

	</head>
	<script>
	function validateForm(){
	var challanId = _id.o('challanId')
	if(challanId.value.length!=0)
	return true;
	else{
	alert("Enter Challan ID");
	return false
	}
	}
	</script>
	<body onload="">

		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:text name="Book Recieve" />
				</h3>

				<s:form name="bookRecieve" action="agt_om_bookRecieveRegistration_getBooks" onsubmit="return validateForm()">
					<table border="0" cellpadding="3" cellspacing="0" width="100%" >
						<tr>
							<td width="70%">
								<div id="retOrgDiv"></div>

							</td>
						</tr>
						<tr>
							<td >
							<i><b>	Enter Challan ID:</b></i>
								&nbsp;&nbsp;<input type="text" label="Enter Challan Id" name="challanId"
									id="challanId" />
							</td>
						</tr>
						<tr>
							<td >
								<s:submit align="center" value="Get Details" cssClass="button" theme="ajax" targets="bookDiv"/>
							</td>
						</tr>
					</table>
				</s:form>
				<div id="bookDiv" ></div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: agt_om_bookRecieveRegistration_menu.jsp,v $ $Revision:
	1.1.2.1.8.4.8.1.2.1 $
</code>