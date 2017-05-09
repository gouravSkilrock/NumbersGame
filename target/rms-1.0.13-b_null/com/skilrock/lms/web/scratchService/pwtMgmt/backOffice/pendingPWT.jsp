	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>	
	<script>
	  function displayDate(transDate)
	  {
	    var isChecked=document.getElementById('searchByDate').checked;
	     if(isChecked==true)
	     {
	          document.getElementById('fromDate').value=transDate;
	     }
	     else
	     {
	     document.getElementById('fromDate').value="";
	     }
	  
	  
	  }
	</script>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>		
	<s:head theme="ajax" debug="false"/>
	</head>
	<body>
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

	<div id="wrap">

  <div id="top_form">
   <h3>Pending PWT</h3>
	
	
				<s:form name="pendingPwtForm" action="searchPendingPWT">

				<table border="0" cellpadding="2" cellspacing="0">
                <tr><td>   
               <table>
					<tr>
						<td>
							Search Direct Player PWTs
						</td>
					</tr>

					<tr>
						<td>
							
							<s:textfield label="Player First Name" name="playerFirstName"></s:textfield>
						</td>
					</tr>
					<tr>
						<td>
							
							<s:textfield label="Game Number" name="gameNbr"></s:textfield>
						</td>
					</tr>

					<tr>
						<td >
							<s:textfield label="Game Name" name="gameName"></s:textfield>
						</td>
					</tr>

					<tr>
						<td>
						
							<s:select  headerKey="1" value="'Pending PWT'" cssClass="option"
								name="status" label="Status" 
								list="{'Pending PWT','Claimed PWT','Cancelled PWT'}"></s:select>
						</td>
					</tr>


					<tr>

					<td align="right" colspan="2">
							<label class="label">Transaction Date<span class="required">*</span>:</label>				
							<input  type="text" name="trancDate" id="fromDate"  readonly size="12">
    						<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('fromDate'),'yyyy-mm-dd', this, '<s:property value="%{#session.presentDate}"/>', false, '<s:property value="%{#session.presentDate}"/>'),document.getElementById('searchByDate').checked=true" />
					</td>
					<td>
					  &nbsp;<input  type="checkbox" id="searchByDate" name="searchByDate" value="yes"  onclick="displayDate('<s:property value="%{#session.presentDate}"/>')"></input><font color="red">Include Date for search</font> 
					</td>

					</tr>

					<s:submit value="Search" theme="ajax" targets="result" cssClass="button"/>

                </table>
                </td></tr>
				</table>

</s:form>
			</div></div>

<div id="result">

</div>

		
	</body>
</html><code id="headId" style="visibility: hidden">$Id: pendingPWT.jsp,v 1.1 2010/04/02 12:12:07 gaurav Exp $</code>
