<%@ taglib prefix="s" uri="/struts-tags"%>
<s:head theme="ajax" debug="false"/>
<%java.util.Calendar calendar= java.util.Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(calendar.HOUR_OF_DAY,23);
		calendar.set(calendar.MINUTE,59);
		calendar.set(calendar.SECOND,59);%>
<%response.setDateHeader("Expires",calendar.getTimeInMillis());%>	

<table>
	<tr>
		<td colspan="3">
				<s:text name="from"><b>Game Start Date</b></s:text>
				<s:text name="from">&nbsp;From:</s:text>
				<input  type="text" name="startFromDate" id="start_date"  readonly size="12">
    			<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<s:property value="%{#session.END_DATE_START}"/>', '1900-01-01', '<s:property value="#session.presentDate" />')" />
																
				<s:text name="to">&nbsp;To:</s:text>
				<input  type="text" name="startToDate" id="startToDate"  readonly size="12">
    			<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('startToDate'),'yyyy-mm-dd', this, '<s:property value="%{#session.END_DATE_START}"/>', '1900-01-01', '<s:property value="#session.presentDate" />')" />
		</td>
	</tr>

	<tr>
		<s:set name="endDateInitialize" value="#session.END_DATE_START" />
		<td colspan="3">
				<s:text name="endDate">&nbsp;&nbsp;&nbsp;&nbsp;<b>Sale End Date</b></s:text>
				<s:text name="from">&nbsp;From:</s:text>
				<input  type="text" name="endFromDate" id="endFromDate" value="<s:property value="%{#session.END_DATE_START}"/>" readonly size="12">
    			<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('endFromDate'),'yyyy-mm-dd', this, '<s:property value="%{#session.END_DATE_START}"/>', '<s:property value="%{#session.END_DATE_START}"/>', '3000-12-31')" />
					
				<s:text name="to">&nbsp;To:</s:text>
				<input  type="text" name="endToDate" id="endToDate"  readonly size="12">
    			<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('endToDate'),'yyyy-mm-dd', this, '<s:property value="%{#session.END_DATE_START}"/>', '<s:property value="%{#session.END_DATE_START}"/>', '3000-12-31')" />
		</td>
	</tr>
	
</table>
<code id="headId" style="visibility: hidden">$Id: agt_om_selfIniOrderRet_Date.jsp,v 1.1 2010/04/02 12:12:06 gaurav Exp $</code>					