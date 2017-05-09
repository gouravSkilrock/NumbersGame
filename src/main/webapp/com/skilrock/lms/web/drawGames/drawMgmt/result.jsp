
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<s:head theme="ajax" />
</head>

<body>
		<div  style="height:158px;overflow-y: auto;">
	
		
		<table width="800" border="1" cellpadding="4" cellspacing="0" id="drawResTbl" bordercolor="#CCCCCC" align="center">
		  <tr>
		  
		  <s:if test="#session.DRAW_SCH_LIST.size>0">
		  <th><input type="checkbox" name="check" onclick="checkAll(this.name)" /></th>
		  </s:if>
		  <th colspan="5" align="center">Active Draw List</th></tr>
		  <tr>
				<th width="15%" scope="col" align="center">
					Draw Id
				</th>
				<th width="25%" scope="col" align="center">
					Draw Time
				</th>
				<th width="25%" scope="col" align="center">
					Draw <s:text name="Freeze" /> Time
				</th>
				<th width="25%" scope="col" align="center">
					Draw Status
				</th>
				<th width="10%" scope="col" align="center">
					Result
				</th>
			</tr>
		 <s:if test="#session.DRAW_SCH_LIST.size>0">
		  <s:iterator value="#session.DRAW_SCH_LIST">
			  <tr id="<s:property value="drawId" />">
			    <td>
			    	<input type="checkbox" name="drawIds"" id="<s:property value="drawId" />" value="<s:property value="drawId" />" onclick="chk()"/>
			    	&nbsp;<s:property value="drawId" />
			    </td>
			    <td><s:property value="freezeDateTime" /></td>
			    <td><s:property value="drawDateTime" /></td>			  		   
			  </tr>
		  </s:iterator>		 
		  </s:if>
		  <s:else>
		  <tr>
		    <td colspan="3" align="center">No Result Found</td>
		   		    
		  </tr>
		  </s:else>
		  
		</table>
	</div>
	<div>
	<s:if test="#session.DRAW_SCH_LIST.size>0">
	<table width="750" border="0" cellpadding="0" cellspacing="0" bordercolor="#CCCCCC" align="center">
	 <tr>
			  <td align="right">
			  	<input type="button" value="Postpone" onclick="return checkedDraw('url', 'active', 'p')"  class="button" />			  	
			  	&nbsp;<input type="button" value="Cancel" onclick="return checkedDraw('url', 'active', 'c')" class="button" />
			  </td>			  			   
		  </tr></table></s:if>
	</div>
	
	
	
</body>
</html>
