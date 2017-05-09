<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
	<head>
		<s:head theme="ajax" debug="false" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					Offline File Approval
				</h3>
				<s:if test="%{#session.Offline_File_List.size()!=0}">

					<s:form action="agt_pm_offline_sale_approval_save">
						<table width="100%" border="1" cellpadding="3" cellspacing="0"
							bordercolor="#CCCCCC">
							<tr>
								<th>
									S.No.
								</th>
								<th>
									Retailer Name
								</th>
								<th>
									File Name
								</th>
								<th>
									Game Name
								</th>
								<th>
									Total Ticket
								</th>
								<th>
									Total Sale
								</th>
								<th>
									Approve
								</th>
							</tr>
							<s:iterator value="#session.Offline_File_List" status="index">
								<tr>
									<td>
										<s:property value="%{#index.index+1}" />
										<s:hidden name="fileId" value="%{fileId}" />
										<s:hidden name="retUserId" value="%{retUserId}" />
										<s:hidden name="gameNo" value="%{gameNo}" />
									</td>
									<td>
										<s:textfield theme="simple" name="retName" value="%{retName}" />
									</td>
									<td>
										<s:property value="fileName" />
									</td>
									<td>
										<s:property value="gameName" />
									</td>
									<td>
										<s:property value="totalTkt" />
									</td>
									<td>
										<s:property value="saleValue" />
									</td>
									<td>
										<s:checkbox theme="simple" name="approvalStatus"
											id="approvalStatus%{#index.index+1}"
											fieldValue="%{#index.index}"></s:checkbox>
									</td>
								</tr>
							</s:iterator>
						</table>
						<s:submit value="Submit"></s:submit>
					</s:form>
				</s:if>
				<s:else>
					<table width="100%" border="1" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC">
						<tr>
							<td align="center">
								No Record To Process.
							</td>
						</tr>
					</table>
				</s:else>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: agt_pm_offline_sale_approval_menu.jsp,v $ $Revision: 1.2 $
</code>