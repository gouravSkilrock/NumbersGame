 
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<s:head theme="ajax" debug="false"/>
</head>
<body>


	<div id="wrap">

  <div id="top_form">
  

					<table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">

						<tr>
							<th colspan="6" align="center">
								Search Results
							</th>
						</tr>
						<tr>
							
							<th>
								Game Name
							</th>

							<th>
								Player  Name
							</th>
							
							<th>
								Transaction Date
							</th>
							<th>
								Status
							</th>
							<th>
								Details
							</th>
						</tr>
						<s:if test="%{#session.SearchResultsAvailable == 'Yes'}">
						<s:iterator id="SearchGameResults" value="#session.SEARCH_RESULTS">
							<tr>
								
								<td>
									<s:property id="gn" value="gameName" />
								</td>
								<td>
									<s:property value="playerFirstName" /> <s:property value="playerLastName" />
								</td>
								
								<td>
									<s:property value="trancDate" />
								</td>
								<td>
									<s:property value="status" />
									
								</td>
							<s:if test="status=='Pending PWT'">
								<td>
									<s:a href="playerPWTDetails.action?transactionId=%{transactionId}" > Process </s:a>

								</td>
							</s:if>
							<s:else>
							<td>
									<s:a href="playerPWTDetails.action?transactionId=%{transactionId}&& detail=true" > Details </s:a>

								</td>
							</s:else>
							</tr>
						</s:iterator>
						</s:if>
						<s:else>
							<tr>
								<td colspan="6" align="center">No Records to Process</td>
							</tr>
						</s:else> 
					</table>
					</div></div>
				 
  <s:div id="naviga">	
<s:if test=" #session.SEARCH_RESULTS1.size >5 ">	 
					
					
					<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="result" href="searchpendingPWTAjax.action?end=first">First</s:a><s:a theme="ajax" targets="result" href="searchpendingPWTAjax.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.SEARCH_RESULTS1.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="result" href="searchpendingPWTAjax.action?end=Next" >Next</s:a><s:a theme="ajax" targets="result" href="searchpendingPWTAjax.action?end=last">Last</s:a></s:else>      
    
			
</s:if>
	</s:div>
 


</body>
</html><code id="headId" style="visibility: hidden">$Id: pendingPWTSearchResult.jsp,v 1.1 2010/04/02 12:12:07 gaurav Exp $</code>