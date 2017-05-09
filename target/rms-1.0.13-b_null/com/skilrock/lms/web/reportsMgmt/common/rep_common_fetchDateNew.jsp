<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>
<s:head theme="ajax" debug="false"/>
<%java.util.Calendar calendar= java.util.Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(calendar.HOUR_OF_DAY,23);
		calendar.set(calendar.MINUTE,59);
		calendar.set(calendar.SECOND,59);%>
<%response.setDateHeader("Expires",calendar.getTimeInMillis());%>

<table align="center" width="400px">
<s:set name="stDate" id="stDate" value="#session.presentDate" />
<%  	Calendar prevCal= Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -30);
		String calDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(cal.getTimeInMillis()).toString(), "yyyy-mm-dd", (String)session.getAttribute("date_format"));
		String startDate = CommonMethods.convertDateInGlobalFormat(new java.sql.Date(prevCal.getTimeInMillis()).toString(), "yyyy-mm-dd", (String)session.getAttribute("date_format"));
 %>
	<tr>
		<td>
			<label class="label" ><s:text name="label.start.date" /><span>*</span>:&nbsp;</label>
    		<input  type="text" name="start_date" id="start_date" value="<%=startDate %>" readonly size="12">
    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('start_date'),'dd-mm-yyyy', this, '<%=startDate %>', '<%=calDate %>', '<%=startDate %>')" />
    	</td>
    </tr>	
	<tr>
		<td>
			<label class="label"><s:text name="label.end.date" /><span>*</span>:&nbsp;</label>
    		<input  type="text" name="end_Date" id="end_date" value="<%=startDate %>" readonly size="12">
    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('end_date'),'dd-mm-yyyy', this, '<%=startDate %>',false, '<%=startDate %>')" />
   		</td>
  </tr>
</table>

<code id="headId" style="visibility: hidden">
$RCSfile: rep_common_fetchDateNew.jsp,v $
$Revision: 1.3 $
</code>