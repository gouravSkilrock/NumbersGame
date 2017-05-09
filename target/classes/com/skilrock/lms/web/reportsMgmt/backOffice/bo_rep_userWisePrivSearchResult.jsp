	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>

	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
	
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
	<s:head theme="ajax" debug="false"/>
		</head>
		
<body>

	<div id="downUser" style="text-align: center" >
	
	<s:form name="readUserPriv" method="post" enctype="multipart/form-data"> 	
  	<s:actionerror/>
  	
  	<s:if test="%{userActivePrivsDetails.isEmpty()}">
  		<table>
  			<tr>Services for this user is not active.</tr>
  		<table>
  	</s:if>
  	<s:else>
  	<table id="userAllPrivTab" border="1"  bordercolor="#CCCCCC" cellpadding="2" cellspacing="0" width="100%" style="text-align: left;">
  		
  	<tr>
  		<th id = "uname" colspan="3" align="center"> Active Priviledges</th>
  	</tr>
  	<tr>
  		<th align = "center">Service Name</th>
  		<th align = "center">Group Name</th>
  		<th>Privilege Name</th>
  	</tr>
  	<s:iterator id="serviceItr" value = "%{userActivePrivsDetails}">
  	<tr align = "center">
  		<td style="background: #eeeeee" rowspan = <s:property value='key.split("-")[2]' /> ><b><s:property value='key.split("-")[1]'/></b></td>
  		
  		<s:iterator  id="relatedToItr" value = "%{value}" status="taskIndex">
  			<s:if test = '#taskIndex.index > 0'>
  				<tr align = "center">
  			</s:if>
  			<td style="background: #gggggg" rowspan = <s:property value="%{value.size()}" /> ><em><s:property value="key" /></em></td>
  		
  					
  					<td style="background: #ffffff" align = "left"><s:property value="%{value[0]}" /></td>
  						
  	</tr>
  			<s:if test="%{value.size() > 1}">
  					<s:iterator id="groupItr" value = "%{value}" status="taskIndex" > 
  						<s:if test='%{value[#taskIndex.index+1] != ""}'> 						
  							<tr><td style="background: #ffffff" ><s:property value = "%{value[#taskIndex.index+1]}"/></td></tr>
  						</s:if>  						
  					</s:iterator>
  			</s:if>	
  		</s:iterator>
  		
  	</s:iterator>
  					
  	<!--  <tr>
  	 	<td colspan="3">
  				<table id="export"  border="0" cellpadding="5" cellspacing="0" width="100%" >		
					<s:submit value="Export Excel"  align="center" cssClass="button" name="pdfbtn" />						
				</table>				
  		</td>
  		
  	 
  	 </tr> -->
  
  	
  	</table>
  	
	</s:else>
	</s:form>
	</div>
	
</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_userWisePrivSearchResult.jsp,v $
$Revision: 1.2 $
</code>
