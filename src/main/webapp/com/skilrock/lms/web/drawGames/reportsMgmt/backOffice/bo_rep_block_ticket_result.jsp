
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
</head>
<body>
<div align="center">
	<h3 id="table_header">
		<u>Block Ticket Details Game Wise&nbsp; From : <s:property
				value="start_date" /> &nbsp;&nbsp;To&nbsp;:&nbsp; <s:property
				value="end_Date" />
		</u>
	</h3>
</div>
<s:if test="%{blockTicketList != null && blockTicketList.size()>0}">
	<div align="left">
		<s:form action="bo_rep_block_ticket_exportExcel.action" method="post"
			enctype="multipart/form-data">
			<s:hidden id="tableValue" name="reportData" />
			<s:submit value="Export As Excel"
				cssStyle="margin-right:-400px;font-size:15px"
				onclick="getTableData();" />
		</s:form>
	</div>
</s:if>
<div align="left" id="tableDataDiv">
	<table width="100%" border="1" cellpadding="3" cellspacing="0"	bordercolor="#CCCCCC" align="center">
		<s:if test="%{blockTicketList != null && blockTicketList.size()>0}">
			<tr>

				<th>
					Ticket Number
				</th>
				<th>
					Current Status
				</th>
				<th>
					Date And Time
				</th>
				<th>
					Done By
				</th>
			</tr>

			<s:iterator id="beanCart" value="%{blockTicketList}"
				status="taskIndex">
				<tr class="startSortable">
					<td>

						<s:property value="ticketNumber" />

					</td>

					<td>
						<s:property value="status" />
					</td>

					<td>

						<s:date name="%{updatedTime}" format="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td>
						<s:property value="userName" />
					</td>
				</tr>

			</s:iterator>

		</s:if>
		<s:else>
			<tr>
				<th colspan="5" align="center">
					No Records Found
				</th>
			</tr>
		</s:else>
	</table>
</div>
</body>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_block_ticket_result.jsp,v $ $Revision: 1.3 $
</code>
