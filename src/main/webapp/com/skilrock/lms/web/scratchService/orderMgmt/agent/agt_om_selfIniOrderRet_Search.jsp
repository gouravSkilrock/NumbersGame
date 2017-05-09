	<%@ taglib prefix="s" uri="/struts-tags"%>
	<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<s:head theme="ajax" debug="false"/>			
					<s:if test="%{searchResultsAvailable == 'Yes'}">
							<br />
								<table width="684" border="1" cellpadding="0" cellspacing="0"
							bordercolor="#CCCCCC" align="center">
							<tr align="center">

								<tr>
									<th align="center" colspan="6">
										Search Results
									</th>
								</tr>
								<tr>
									<th>
										Game Number
									</th>
									<th>
										Game Name
									</th>

									<th>
										Ticket Price
									</th>
									<th>
										Game Start Date
									</th>
									<th>
										Game End Date
									</th>
									<th>
										Order
									</th>
								</tr>
								<s:iterator id="SearchGameResults"
									value="#session.SEARCH_RESULTS">
									<tr>
										<td>
											<s:property value="gameNbr" />
										</td>
										<td>
											<s:property id="gn" value="gameName" />
										</td>
										<td>
											<s:set name="strticketPrice" value="ticketPrice" /><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strticketPrice")) %>
										</td>
										<td>
											<s:property value="startDate" />
										</td>
										<td>
											<s:property value="saleEndDate" />
										</td>
										<td>
											<s:a
												href="agt_om_selfIniOrderRet_OrderDet.action?gameId=%{#SearchGameResults.gameId}">
			      Order
			      </s:a>

										</td>

									</tr>
								</s:iterator>
							</table>
						</s:if>


						<s:if test=" #session.SEARCH_RESULTS1.size >5 ">
							<s:div id="naviga">
								
								<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="resultSearch" href="agt_om_selfIniOrderRet_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="resultSearch" href="agt_om_selfIniOrderRet_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.SEARCH_RESULTS1.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="resultSearch" href="agt_om_selfIniOrderRet_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="resultSearch" href="agt_om_selfIniOrderRet_Navigate.action?end=last">Last</s:a></s:else>      
    
							</s:div>
						</s:if>
						<s:if test="%{searchResultsAvailable == 'No'}">
							<br />
							<table align="center" border="1" cellpadding="0" cellspacing="0"
								bordercolor="#CCCCCC" width="684">


								<tr>
									<th align="center" colspan="6">
										Search Result
									</th>

								</tr>
						
								<tr>
									<td colspan="6" align="center">
										No Order to Process
									</td>
								</tr>
							</table>
						</s:if>
						
						<code id="headId" style="visibility: hidden">$Id: agt_om_selfIniOrderRet_Search.jsp,v 1.1 2010/04/02 12:12:06 gaurav Exp $</code>