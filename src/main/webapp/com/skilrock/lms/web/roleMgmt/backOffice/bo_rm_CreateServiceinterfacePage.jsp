
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<s:head theme="ajax" />
		
	</head>

	<body>


		<div id="forAll">
			<table cellpadding="0" cellspacing="0" border="1"
				bordercolor="#CCCCCC" width="100%" bgcolor="#EEEEEE" align="center">
				<tr bgcolor="#f0f0f0">
					<th width="25%" height="20">
						Tier
					</th>
					<!-- 	<th width="25%" height="20">Channel</th> -->
					<th width="25%" height="20">
						Interface
					</th>
					<th width="25%" height="20">
						Status
					</th>
				</tr>
				<s:iterator value="%{interfaceList}" status="intStatus">
					<tr height="20">
						<td width="25%">
							<s:property value="%{tier_id}" />
						</td>
						<!-- 	<td width="25%"><s:property value="%{channel}" /></td> -->
						<td width="25%">
							<s:property value="%{tier_interface}" />
						</td>
						<td width="25%">
							<select class="option"
								id="<s:property value="#intStatus.index" />"
								name="interfaceStatus">
								<option value="<s:property value="%{status}" />"
									selected="selected">
									<s:property value="%{status}" />
								</option>
								<s:if test="%{status=='INACTIVE'}">
									<option value="ACTIVE">
										ACTIVE
									</option>
								</s:if>
								<s:else>
									<option value="INACTIVE">
										INACTIVE
									</option>
								</s:else>
							</select>
							<input type="hidden" name="interfaceStatusPrev" id="intPrev<s:property value="#intStatus.index" />" value="<s:property value="%{status}" />" />
						</td>
					</tr>
				</s:iterator>
			</table>
		</div>
	
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rm_CreateServiceinterfacePage.jsp,v $
$Revision: 1.10 $
</code>