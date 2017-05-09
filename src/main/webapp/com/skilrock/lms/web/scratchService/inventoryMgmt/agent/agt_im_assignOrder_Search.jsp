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

			
			
			<s:form>		
		    
			 <table align="center" width="650" border="1" cellpadding="5" cellspacing="0" bordercolor="#CCCCCC" >
			   
			   
			   <tr><th align="center" colspan="4">Search Result</th></tr>
		
			   <s:if test="#session.AGT_APP_ORDER_LIST!=null">
			          <tr>
			     <th>Order Number</th>
			     <th><s:property value="#application.TIER_MAP.RETAILER" /> Name</th>
			        <th>Order Date</th>
			          <th>Order Status</th>
			     
			   </tr>
			  <s:iterator value="#session.AGT_APP_ORDER_LIST">
			    
			    <tr>
			     
			      <td><s:a  href="agt_im_assignOrder_OrderDetail.action?orderId=%{orderId}&retOrgId=%{retailerOrgId}&retOrgName=%{retOrgName}&orderDate=%{orderDate}"><s:property value="orderId"/></s:a></td>
			      <td> <s:property value="retOrgName"/></td>
			      <td> <s:property value="orderDate"/></td>
			 		 <td> <s:property value="orderStatus"/></td>
		        </tr>
			     
			  </s:iterator> 
			  </s:if>	 
			<s:else><tr>
			   			<td colspan="4" align="center">No Order to Process</td>
			   	</tr>
			</s:else>
				<tr>
					<td colspan="4" align="center">
						<div id="naviga" align="center">
							<s:if test=" #session.AGT_APP_ORDER_LIST1.size >5 ">
							<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="bottom" href="agt_im_assignOrder_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="bottom" href="agt_im_assignOrder_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
						    <s:if test="#session.startValueOrderSearch==((#session.AGT_APP_ORDER_LIST1.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="bottom" href="agt_im_assignOrder_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="bottom" href="agt_im_assignOrder_Navigate.action?end=last">Last</s:a></s:else>      
							</s:if>
						</div>
					</td>
				</tr>
			 </table> 
		</s:form>

	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_im_assignOrder_Search.jsp,v $
$Revision: 1.3 $
</code>