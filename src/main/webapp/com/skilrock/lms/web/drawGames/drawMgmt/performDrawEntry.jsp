
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@page import="java.sql.Timestamp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<s:head theme="ajax" />
</head>
 <table width="800" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="left" style="margin-left:6px">
			<tr>			
				<th colspan="4" align="center">
					Draw List
				</th>
			</tr>
			<tr>
				<th width="10%" scope="col" align="center">
					Draw Id
				</th>
				<th width="22%" scope="col" align="center">
					Draw Time
				</th>
				<th width="23%" scope="col" align="center">
					Draw <s:text name="Freeze" /> Time
				</th>
				<th width="8%" scope="col" align="center">
					Draw Status
				</th>
			</tr>
			</table>
		<div style="height: 160px; overflow-y:scroll;overflow-x:hidden; clear: both" id="testDiv">
	
		
		<table width="800" border="1" cellpadding="5" cellspacing="0" id="drawResTbl" bordercolor="#CCCCCC" align="center">
		  
		 <s:if test="#session.DRAW_SCH_LIST.size>0">
		  <s:iterator value="#session.DRAW_SCH_LIST">
			  <tr style="cursor:hand;" id="<s:property value="drawId" />" onclick="document.getElementById('r<s:property value="drawId" />').checked=true,checkedDraw('url', 'active', 'a')">
			    <td width="10%">
			    	<input type="radio" name="drawIds" id="r<s:property value="drawId" />" value="<s:property value="drawId" />"/>
			    	&nbsp;<s:property value="drawId" />
			    </td>
			    <td width="22%" align="center" id="<s:property value="drawId" />drawDateTime">
							<s:property value="drawDateTime" />
						</td>
						<td align="center" width="23%" id="<s:property value="drawId" />freezeDateTime">
							<s:property value="freezeDateTime" />
						</td>
						<td align="center" width="8%">
							<s:property value="drawStatus" />
						</td>
 		   
			  </tr>
		  </s:iterator>		 
		  </s:if>
		  <s:else>
		  <tr>
		    <td colspan="4" align="center">No Result Found</td>
		   		    
		  </tr>
		  </s:else>
		  
		</table>
	</div>
	<div id="curTime" style="visibility: hidden;"><%=new java.sql.Timestamp(new java.util.Date().getTime()).toString()%></div>
	</html>

