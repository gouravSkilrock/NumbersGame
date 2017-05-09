<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

	
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<s:head theme="ajax" debug="false" />
	</head>

	<body>


	
				<s:form>


					
						<table width="684" border="1" cellpadding="3" cellspacing="0"
							bordercolor="#CCCCCC" align="center">


							<tr>
								<th>
									Order Number
								</th>
								<th>
									<s:property value="#application.TIER_MAP.AGENT" /> Name
								</th>
								<th>
									Order Date
								</th>
								<th>
									Order Status
								</th>


							</tr>
							<s:if test="#session.APP_ORDER_LIST!=null">
							<s:iterator value="#session.APP_ORDER_LIST">

								<tr>
									<s:url id="es1" action="bo_im_dispatchOrder_OrderDetail.action" encode="true">
										<s:param name="orderId" value="%{orderId}" />
										<s:param name="agentOrgId" value="%{agentOrgId}" />
										<s:param name="orderDate" value="%{orderDate}" />
										<s:param name="agtOrgName" value="%{agentOrgName}" />
									</s:url>
									<td>
										<s:a href="%{es1}">
											<s:property value="orderId" />
										</s:a>
									</td>
									<td>
										<s:property value="agentOrgName" />
									</td>
									<td>
										<s:property value="orderDate" />
									</td>
									<td>
										<s:property value="orderStatus" />
									</td>


								</tr>

							</s:iterator>
						</s:if>
				
		
		<s:else>
			<tr>
			   			<td colspan="4" align="center">No Order to Process</td>
			   	</tr> 
		</s:else>
						</table>
						<br />
						<div align="center">
						<s:div id="naviga">
							<s:if test=" #session.APP_ORDER_LIST1.size >5 " >
								
							
							<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="bottom" href="bo_im_dispatchOrder_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="bottom" href="bo_im_dispatchOrder_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST1.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="bottom" href="bo_im_dispatchOrder_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="bottom" href="bo_im_dispatchOrder_Navigate.action?end=last">Last</s:a></s:else>      
    
							</s:if>
						
						</s:div></div>
					


		</s:form>
	
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_dispatchOrder_SearchResult.jsp,v $
$Revision: 1.3 $
</code>