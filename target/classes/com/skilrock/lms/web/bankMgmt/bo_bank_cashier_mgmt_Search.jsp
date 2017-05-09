<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	

	
</head>
		<body>


			<s:set name="dataBeanList" value="bankRepDataBeanList"/>
			<s:if test="#dataBeanList.size>0">
			<div align="left">
				<s:form action="../reportsMgmt/exportReportAsExcel.action" method="post"
					enctype="multipart/form-data">
					<s:hidden id="tableValue" name="reportData" />
					<s:hidden name="reportName" value="cashierDetails" />
					<s:submit value="Export As Excel"
						cssStyle="margin-right:-400px;font-size:15px" onclick="getData();" />
				</s:form>
			</div>
			<div id="tableDataDiv">
			<table align="center">
			<tr>
			<td><h3><u> Cashier  Details   Registration From  Date: <s:property value="regStartDate"/> To Date: <s:property value="regEndDate"/></u></h3></td>
			</tr>
			</table>
				<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					
					<tr>
						<th >
							S.No:
						</th>
						<th >
							Cashier Name
						</th>
						<th >
							Bank  Name
						</th>
						<th >
							Bank Full Name
						</th>
							
						<th >
							
							
							Branch Name
						</th>
						<th >
							
							
							Branch Full Name
						</th>
											
						<th >
							
							
						Registration Date
						</th>
						<th >
							
							
						Last Login  Date
						</th>
						<th>
						Status
						</th>
					
					</tr>
					<s:set name="count" value="0"></s:set>
					<s:iterator value="%{#dataBeanList}">
							<s:set name="count" value="#count+1"/>
							<tr class="startSortable">
								<td><s:property value="#count" /></td>
								<td><s:property value="cashierName" /></td>
								<td><s:property value="bankName" /></td>
								<td align="left">
								<s:property value="bankFullName" />
								</td>
							
								<td>
								<s:property value="branchName" />
								</td>
								<td align="left">
								<s:property value="branchFullName" />
								</td>
							
								
								<td align="right">
									<s:property value="cashierRegDate" />
								</td>
								<td align="right">
									<s:property value="cashierLastLoginDate" />
								</td>
								<td align="center">
									<s:property value="status" />
								</td>
								
							</tr>
						
					</s:iterator>
					
				

				</table>
					</div>	
		</s:if>
			<s:else>
						<table  align="center"><tr><td>
								No Records to Process
								</td></tr>
						</table>
			</s:else>
	
		</body>
</html>		
