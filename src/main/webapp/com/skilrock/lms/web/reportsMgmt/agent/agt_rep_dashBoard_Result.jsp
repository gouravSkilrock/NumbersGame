<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>

<table width="100%" border="1" cellpadding="2" cellspacing="0"
	bordercolor="#CCCCCC" align="center" valign="top">
	<tr>
		<th>
			S.No
		</th>
		<th>
			Retailer Name
		</th>
		<th>
			Location
		</th>
		<th>
			Offline
		</th>
		<th>
			Last Login Device
		</th>
		<th>
			Usable Limit
		</th>
		<th>
			Increase
		</th>
		<th>
			Decrease
		</th>
		<th>
			Status
		</th>
		<th>
			Activity Status
		</th>
		<th>
			RG Status
		</th>
	</tr>
	<s:iterator id="task" value="#session.CONFIG_LIST" status="taskIndex">
		<tr>
			<td>
				<s:property value="#taskIndex.index+1" />
				<s:hidden name="code" value="%{code}" />
			</td>
			<td>
				<s:property value="orgName" />
			</td>
			<td>
				<s:property value="location" />
			</td>
			<td>
				<s:property value="webType" />
			</td>
			<td>
				<s:property value="deviceName" />
			</td>
			<td>
				<s:property value="balance" />
			</td>
			<td>
				Increase
			</td>
			<td>
				Decrease
			</td>
			<td>
				<s:select theme="simple" cssClass="option"
					list="{'ACTIVE','INACTIVE'}" name="orgStatus" headerKey="-1"
					headerValue="%{orgStatus}" />
			</td>
			<td>
				Activity Status
			</td>
			<td>
				RG Status
			</td>
		</tr>
	</s:iterator>
	<tr>
		<td colspan="5" align="center">
			<table>
				<s:submit name="submit" value="Submit" cssClass="button" />
			</table>
		</td>
	</tr>
</table>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_dashBoard_Result.jsp,v $
$Revision: 1.2 $
</code>