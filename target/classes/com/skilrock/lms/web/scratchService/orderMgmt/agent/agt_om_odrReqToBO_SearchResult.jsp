<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<s:head theme="ajax" debug="false"/>
<s:if test="%{searchResultsAvailable == 'Yes'}" >
			 <table border="1" width="96%" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   
			   <tr>
			   <th align="center" colspan="6">Search Results</th>
			   </tr>
			   <tr>
			     <th>Game Name</th>
			     <th>Game Number</th>
			     <th>Ticket Price</th>
			     <th>Game Start Date</th>
			     <th>Game End Date</th>
			     <th>Order</th>
			   </tr>
			  <s:iterator id="SearchGameResults" value="#session.SEARCH_RESULTS">
			    <tr>
			      <td> <s:property id="gn" value="gameName"/></td>
			      <td> <s:property value="gameNbr"/></td>
			      <td> <s:set name="strticketPrice" value="ticketPrice" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strticketPrice")) %></td>
			      <td> <s:property value="startDate"/></td>
			      <td> <s:property value="saleEndDate"/></td>
			      <td> 
			      <s:a  href="agt_om_odrReqToBO_Detail.action?gameId=%{#SearchGameResults.gameId}">
			      Order
			      </s:a>     	   
			      
			      </td>
			      
		        </tr>
			  </s:iterator> 
			 </table>
			  <s:if test=" #session.SEARCH_RESULTS1.size >5 ">			
			<s:div id="naviga" cssStyle="text-align:center">
	
	
	<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="resultSearch" href="agt_om_odrReqToBO_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="resultSearch" href="agt_om_odrReqToBO_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.SEARCH_RESULTS1.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="resultSearch" href="agt_om_odrReqToBO_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="resultSearch"  href="agt_om_odrReqToBO_Navigate.action?end=last">Last</s:a></s:else>      
    
	</s:div>
</s:if> 
			</s:if>
<s:if test="%{searchResultsAvailable == 'No'}" >
						<table border="1" cellpadding="2" cellspacing="0"
							bordercolor="#CCCCCC" align="center" width="600">
							<tr><th align="center" colspan="6">Search Result</th> </tr>

			
							<tr>
								<td colspan="6" align="center">No Order to Process</td>
							</tr>
						</table>
					</s:if>				
	
<code id="headId" style="visibility: hidden">
$RCSfile: agt_om_odrReqToBO_SearchResult.jsp,v $
$Revision: 1.3 $
</code>