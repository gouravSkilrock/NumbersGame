<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>			
	</head>
	<body>
	
		<s:if test="#session.SMS_BEAN_LIST.size>0">
		
	<table width="100%" border="1" cellpadding="3" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
	<tr>
	<th>Player Name</th>
	<th>Retailer</th>
	<th>Amount</th>
	<th>Pin Type</th>
	<th>Purcahse Time</th>
	<th>Verification Status</th>
	<th>Mobile Number</th>
	<th>Action</th>
		</tr>
		
			<s:set name="dataBean" id="dataBean" value="#session.SMS_BEAN_LIST" />
					<s:iterator id="beanCart" value="%{#dataBean}" >
					<s:set name="phone" value="userPhone"/>
					<tr class="startSortable" id="row<s:property value="id"/>">
						<td align="left" nowrap="nowrap" >
							<s:property value="plrName" />
						</td>
						<td align="left" nowrap="nowrap" >
							<s:property value="retOrgName" />
						</td>
						<td >
							<s:property value="amount" />
						</td>
						<td >
							<s:property value="pinType" />
						</td>
						<td >
							<s:property value="date" />
						</td>
						<td >
							<s:property value="status" />
						</td>
						<td >
							<s:property value="userPhone" />
						</td>
						<td >
							<div id="result<s:property value="id"/>">
							</div>
							<s:if test="%{#phone==''}">
							<div id="smsBtn<s:property value="id"/>">
							<input type="button" name="submit<s:property value="id"/>" disabled="disabled" id="submit<s:property value="id"/>" value="Send SMS" class="button" onclick="sendSMS(this.id)" />
							</div>
							</s:if>
							<s:else>
							<div id="smsBtn<s:property value="id"/>">
							<input type="button" name="submit<s:property value="id"/>" id="submit<s:property value="id"/>" value="Send SMS" class="button" onclick="sendSMS(this.id)" />
							</div>
							</s:else>
						</td>
							</tr>
						</s:iterator>
							</table>	
						</s:if>
						<s:else>
						<table  align="center"><tr><td>
								No Records to Process
								</td></tr>
						</table>
						</s:else>
					

</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_ola_resend_sms_Search.jsp,v $
 $Revision: 1.2 $ 
</code>