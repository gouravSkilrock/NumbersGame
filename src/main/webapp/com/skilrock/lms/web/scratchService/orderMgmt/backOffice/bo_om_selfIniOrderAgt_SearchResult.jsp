<%@ taglib prefix="s" uri="/struts-tags"%>
<s:head theme="ajax" debug="false"/>
					<s:if test="%{searchResultsAvailable == 'Yes'}">
						<br />
						<table width="684" border="1" cellpadding="3" cellspacing="0"
							bordercolor="#CCCCCC" align="center">
							<tr align="center">
								<td colspan="6">
									<b>Search Results</b>
								</td>
							</tr>

							<tr>
								<th>
									Game Name
								</th>
								<th>
									Game Number
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
										<s:property id="gn" value="gameName" />
									</td>
									<td>
										<s:property value="gameNbr" />
									</td>
									<td>
										<s:property value="ticketPrice" />
									</td>
									<td>
										<s:property value="startDate" />
									</td>
									<td>
										<s:property value="saleEndDate" />
									</td>
									<td>
										<s:a
											href="bo_om_selfIniOrderAgt_OrderDet.action?gameId=%{#SearchGameResults.gameId}" >
			      Order
			      </s:a>

									</td>

								</tr>
							</s:iterator>
						</table>

						<s:if test=" #session.SEARCH_RESULTS1.size >5 ">
							<s:div id="naviga">
								
								
								<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="resultSearch"  href="bo_om_selfIniOrderAgt_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="resultSearch"  href="bo_om_selfIniOrderAgt_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.SEARCH_RESULTS1.size/5)*5)">Next Last</s:if><s:else><s:a href="bo_om_selfIniOrderAgt_Navigate.action?end=Next" theme="ajax" targets="resultSearch"  >Next</s:a><s:a href="bo_om_selfIniOrderAgt_Navigate.action?end=last" theme="ajax" targets="resultSearch" >Last</s:a></s:else>      
    
							</s:div>
						</s:if>
					</s:if>
					<s:if test="%{searchResultsAvailable == 'No'}">
						<br />
						
									<table align="center" border="1" cellpadding="2" cellspacing="0"
								bordercolor="#CCCCCC" width="684">


								<tr>
									<th align="center" colspan="6">
										Search Results
									</th>

								</tr>
								<tr>
								<th>
									Game Name
								</th>
								<th>
									Game Number
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
								<tr>
			   						<td colspan="6" align="center">No Game to Process</td>
			   					</tr>
									
								
							</table>
						
			

					</s:if>
<div id="order">
				</div>
				
				<code id="headId" style="visibility: hidden">$Id: bo_om_selfIniOrderAgt_SearchResult.jsp,v 1.1 2010/04/02 12:12:10 gaurav Exp $</code>