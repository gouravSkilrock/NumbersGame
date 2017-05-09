<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" scrolling="no" type="text/css" / >

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	

<s:head theme="ajax" debug="false"/>
	</head>

<body>
<br>
	
	
	<div id="bottom_table">	<s:form name="StatusClose" id="a">
		
			
			
			 <table width="650" border="1" align="center" cellpadding="5" cellspacing="0" bordercolor="#CCCCCC" >
			   
			   
			   <tr>
			     <th>Order Number<br></th>
			     <th><s:property value="#application.TIER_MAP.RETAILER" /> Name<br></th>
			        <th>Order Date<br></th>
			          <th>Order Status<br></th>
			  
			     
			   </tr>
			   <s:if test="#session.AGT_APP_ORDER_LIST!=null">
			  <s:iterator value="#session.AGT_APP_ORDER_LIST">
			    
			    <tr>
			    
			    
			    
			     <s:url id="es1" action="agt_om_closeOrder_Detail.action" encode="true">  
			     <s:param name="orderId" value="%{orderId}"/>
   					<s:param name="retOrgId" value="%{retailerOrgId}"/>
   					<s:param name="retOrgName" value="%{retOrgName}"/>
   					<s:param name="date" value="%{orderDate}"/>							
				</s:url>
			      <td><s:property  value='orderId'/><br></td>
			      <td> <s:property value="retOrgName"/><br></td>
					         <td><s:property value="orderDate"/> <br></td>
			                <td><s:property value="orderStatus"/></td>
			            <td>  <s:a  href="%{es1}">Change Order Status</s:a></td>
			 
		        </tr>
			     
			  </s:iterator> 
			  </s:if>			 
				<s:else>
				<tr>
			   			<td colspan="4" align="center">No Order to Process</td>
			   	</tr> 
			   	</s:else> 		
			 </table> 
	<s:div id="naviga">
	<s:if test=" #session.AGT_APP_ORDER_LIST1.size >5 ">
    	
	
	<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="bottom" href="agt_om_closeOrder_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="bottom" href="agt_om_closeOrder_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.AGT_APP_ORDER_LIST1.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="bottom" href="agt_om_closeOrder_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="bottom" href="agt_om_closeOrder_Navigate.action?end=last">Last</s:a></s:else>      
    
	</s:if>
	</s:div>
	
			
			
		</s:form></div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_om_closeOrder_Search.jsp,v $
$Revision: 1.2 $
</code>