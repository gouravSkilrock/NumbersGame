<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>			
	</head>
	<body>
		<s:set name="dataBeanList" value="plrDetailsList"/>
		<div style="color: red; text-align: left;"><s:actionmessage /></div>
						<s:actionerror />
		<s:if test="#dataBeanList.size>0">
			<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
					<th><s:text name="label.plr.name"/></th>
					<th>UserName</th>
					<th><s:text name="label.wallet"/></th>
					<th><s:text name="label.regi.date.(yyyy-MM-dd)"/></th>
					<th><s:text name="label.email"/></th>
					<th><s:text name="label.regi.through"/></th>
					</tr>	
					<s:set name="dataBean" id="dataBean" value="#dataBeanList" />
						<s:iterator id="beanCart" value="%{#dataBean}" >
						<tr class="startSortable" >
							<td><s:property value="firstName" /></td>
							<td><s:property value="username" /></td>
							<td><s:property value="walletName" /></td>
							<td><s:property value="plrRegDate" /></td>
							<td><s:property value="email" /></td>
							<td><s:property value="regType" /></td>
						
						</tr>
						</s:iterator>
			</table>		
		</s:if>
		<s:else>
						<table  align="center"><tr><td>
								<s:text name="msg.no.record.process"/>
								</td></tr>
						</table>
		</s:else>
					

</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_ola_search_plr_result.jsp,v $
 $Revision: 1.3 $ 
</code>