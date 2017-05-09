	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
<head>	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<s:head theme="ajax" debug="false"/>
</head>

<body>
<div id="wrap">

  <div id="top_form">
   
    <table border="0" cellpadding="2" cellspacing="0">
    <tr>
    <td colspan="2">
    <s:div id="date"></s:div>
    </td></tr>
    <tr>
    <td>
	 	<s:iterator value="#session.GAME_SEARCH_RESULTS">		
	        <s:set name="startDate"  value="%{pwt_end_date}" ></s:set>
	           
	        <label class="label"><s:text name="label.extended.date"></s:text><span class="required">*</span>:</label>
    		<input  type="text" name="pwtEndDate" id="pwtEndDate" value="<s:property value="%{pwt_end_date}"/>" readonly size="12">
    		<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('pwtEndDate'),'yyyy-mm-dd', this, '<s:property value="%{#session.presentDate}"/>', '<s:property value="%{startDate}"/>', false)" />
	 	  
	</s:iterator>
	</td>
	<td>
		<s:set name="presDate" value="#session.presentDate"></s:set>
	     <s:hidden name="presentDate" value="%{presDate}"/>
	      <table> 
		<s:submit align="left" cssClass="button" key="btn.extnd.date" action="bo_gm_closeGame_ExtendDateSave" onclick="return isDateValid()" onkeypress="return isDateValid()"/>
		</table>
	</td>
	</tr>
	</table>
	
	</div></div>
	<br /><br /><br /><br />
</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_closeGame_ExDateView.jsp,v $
$Revision: 1.3 $
</code>