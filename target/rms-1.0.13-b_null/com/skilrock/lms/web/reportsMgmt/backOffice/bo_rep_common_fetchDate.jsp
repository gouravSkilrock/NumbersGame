<%@ taglib prefix="s" uri="/struts-tags"%>
<s:head theme="ajax" debug="false"/>
<%java.util.Calendar calendar= java.util.Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(calendar.HOUR_OF_DAY,23);
		calendar.set(calendar.MINUTE,59);
		calendar.set(calendar.SECOND,59);%>
<%response.setDateHeader("Expires",calendar.getTimeInMillis());%>

<table align="center" width="400px">
<s:set name="stDate" id="stDate" value="#session.presentDate" />
	<tr>
		<td>
			<label class="label" >Start Date<span>*</span>:&nbsp;</label>
    		<input  type="text" name="start_date" id="start_date" value="<s:property value="%{stDate}"/>" readonly size="12">
    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('start_date'),'yyyy-mm-dd', this, '<s:property value="%{stDate}"/>', false, '<s:property value="%{stDate}"/>')" />
    	</td>
    </tr>	
	<tr>
		<td>
			<label class="label">End Date<span>*</span>:&nbsp;</label>
    		<input  type="text" name="end_Date" id="end_date" value="<s:property value="%{stDate}"/>" readonly size="12">
    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<s:property value="%{stDate}"/>',false, '<s:property value="%{stDate}"/>')" />
   		</td>
  </tr>
</table>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_common_fetchDate.jsp,v $
$Revision: 1.2 $
</code>