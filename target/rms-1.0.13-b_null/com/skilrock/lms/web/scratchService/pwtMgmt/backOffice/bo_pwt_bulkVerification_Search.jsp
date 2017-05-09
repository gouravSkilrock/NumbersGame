<%@ taglib prefix="s" uri="/struts-tags"%>
<s:head theme="ajax" debug="false"/>
<%response.setDateHeader("Expires",1000);%>	
<%@page import="java.util.List" %>
					<s:if test="%{searchResultsAvailable == 'Yes'}">
						<br />
						<table width="684" border="1" cellpadding="3" cellspacing="0"
							bordercolor="#CCCCCC" align="center">
							<tr align="center">
								<td colspan="4">
									<b>Search Results</b>
								</td>
								<td>
			   <%int resLength =((List)session.getAttribute("SEARCH_RESULTS1")).size();  %>
			   
			   <select  id="resultList" name="resultList" class="option" onchange="searchPwtResult('bo_pwt_bulkVerification_Navigate.action')">
			   <%int i=0;while(i<resLength){
			  	 if(i+5<resLength){
			  	 %>
			  	 <option value="<%=i+1%> to <%=i+5%>"><%=i+1 %> to <%=i+5 %>
			  	 <%
			  	 }else{
			  	 %>
			  		<option value="<%=i+1%> to <%=resLength%>"><%=i+1 %> to <%=resLength %>
			   <%
			   }
			   i=i+5; 
			   }
			   %>
			   </select></td>
							</tr>

							<tr>
								<th>
									Receipt No.
								</th>
								<th>
									<s:property value="#application.TIER_MAP.AGENT" /> Name
								</th>
								<th>
									Date
								</th>
								<th>
									Verify VIRN
								</th>
								<th>
									Verify Ticket
								</th>
							</tr>
							<s:iterator id="SearchRcptResults"
								value="#session.SEARCH_RESULTS">
								<tr>
									<td>
										<s:property id="rcptNum" value="receiptId" />
									</td>
									<td>
										<s:property value="agtOrgName" />
									</td>
									<td>
										<s:property value="recievedDate" />
									</td>
									<td>
									<s:if test="%{status == 'OPEN'}">
									<s:a href="bo_pwt_bulkVerification_VerifyVIRN.action?receiptNum=%{#SearchRcptResults.receiptId}&agtOrgName=%{#SearchRcptResults.agtOrgName}" >Verify VIRN</s:a>
									</s:if>
									<s:else>
											Closed
									</s:else>
									</td>
									<td>
										<s:if test="%{status == 'OPEN'}">
										<s:a href="bo_pwt_bulkVerification_VerifyTkt.action?receiptNum=%{#SearchRcptResults.receiptId}&agtOrgName=%{#SearchRcptResults.agtOrgName}" >Verify Ticket</s:a>
									</s:if>
									<s:else>
											Closed
									</s:else>
									</td>
								</tr>
							</s:iterator>
						</table>
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
									Receipt No.
								</th>
								<th>
									<s:property value="#application.TIER_MAP.AGENT" /> Name
								</th>
								<th>
									Date
								</th>
								<th>
									Verify VIRN
								</th>
								<th>
									Verify Ticket
								</th>
							</tr>
								<tr>
			   						<td colspan="6" align="center">No Results Found</td>
			   					</tr>
									
								
							</table>
						
			

					</s:if>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_bulkVerification_Search.jsp,v $
$Revision: 1.2 $
</code>