<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>
<html>
<head>
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<s:head theme="ajax" debug="false"/>
</head>
<body>

 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
			   <tr>
			   <td colspan="11" align="center" nowrap="nowrap"><b>Search Results</b></td>
			   </tr>
			  
			   <s:if test=" #session.ORG_SEARCH_RESULTS1.size !=0 ">
			    <tr>
			     <th align="center">Organization Name</th>
			  	 <!--   <th align="center">Type</th> -->
			     <th align="center">Status</th>
			     <!--   <th align="center">Parent Organization</th> -->
			     <th align="center">City</th>
			     <th align="center">CL </th>
			     <th align="center">XCL</th> 
			     <th align="center">Rem. Days</th>
			     <!--   <th align="center">Ledger Balance</th> -->
			     <th align="center">Balance</th>			    
			     <th align="center">Security Deposit</th>
			   </tr>
			  <s:iterator id="SearchOrgResults" value="#session.ORG_SEARCH_RESULTS" status="rowIndex">
			  	<tr id="<s:property value="#rowIndex.index"/>">
			  	<s:set name="trId" value="#rowIndex.index"></s:set>	 			 			 
			    <s:set name="orgbean" />
			    <s:set name="creditLimit" value="orgCreditLimit"/>
			    <s:set name="availCredit" value="availableCredit"/>
			    <s:set name="securityDeposit" value="securityDeposit"/>
			    <s:set name="ledgerBalance" value="ledgerBalance"/>
			    <s:set name="extendedCredit" value="extendedCredit"/>
			    <s:set name="claimableBal" value="claimableBal"/>
			    <% NumberFormat format = NumberFormat.getInstance();
														format.setMaximumFractionDigits(2);
														Double dobl = (Double) pageContext.getAttribute("creditLimit");
															String creditLimit = format.format(dobl); 
														double dobl1 = (Double) pageContext.getAttribute("availCredit");
														double dobl2 = (Double) pageContext.getAttribute("claimableBal");
														double balance=dobl1-dobl2;
														String Balance = format.format(balance);
														 	String availCredit = format.format(dobl1);														 
														Double dobSec = (Double) pageContext.getAttribute("securityDeposit");
															String securityDeposit = format.format(dobSec);
														
														Double ledgerBalance = (Double) pageContext.getAttribute("ledgerBalance");
															String strledgerBalance = format.format(ledgerBalance);
														
														Double extendedCredit = (Double) pageContext.getAttribute("extendedCredit");
															String strextendedCredit = format.format(extendedCredit);
														
														 
														 %>
			      <td align="center"><s:a id="link2" targets="top" href="agt_um_searchRet_Detail.action?orgId=%{orgId}&orgType=%{orgType}"> <s:property value="orgName"/></s:a> </td>
			    <!-- <td> <s:property id="gn" value="orgType"/></td> -->
			      <td align="center"> <s:property value="orgStatus"/></td>
			     <!--  <td><s:property value="parentOrgName"/></td> -->
			      <td align="center"> <s:property value="orgCity"/></td>
			      <td align="right" nowrap="nowrap"><%=creditLimit %></td> 
			      <td align="right"  nowrap="nowrap"><%=strextendedCredit %></td> 
			      <td align="center" nowrap="nowrap"><s:property value="extendsCreditLimitUpto" /></td> 
			      <!--  
			      <s:if test="%{ledgerBalance<0}">
			      	<td align="right" nowrap="nowrap" style="color: red"><s:a href="#" onclick="updatedLedgerBalance('agt_um_searchRet_UpdLedgerBal.action?orgId=%{orgId}& parentOrgId=%{parentOrgId}& orgType=%{orgType}','div-%{trId}')"><s:div id="div-%{trId}"><%=strledgerBalance %></s:div> <div  id ="load-<s:property value="#rowIndex.index"/>" style="text-align: right"></div></s:a></td> 
			      </s:if>
			      <s:else>
			      	<td align="right" nowrap="nowrap" ><s:a href="#" onclick="updatedLedgerBalance('agt_um_searchRet_UpdLedgerBal.action?orgId=%{orgId}& parentOrgId=%{parentOrgId}& orgType=%{orgType}','div-%{trId}')"><s:div id="div-%{trId}"><%=strledgerBalance %></s:div><div id ="load-<s:property value="#rowIndex.index"/>"  style="text-align: right"></div></s:a></td> 
			      </s:else> 
			      -->
			      <s:if test="%{availCredit<0}">
			      	<td align="right" nowrap="nowrap" style="color: red"> <%=Balance %></td>
			      </s:if>
			      <s:else>
			      	<td align="right" nowrap="nowrap"> <%=Balance %></td>
			      </s:else>
			      
			      <td align="right" nowrap="nowrap"> <%=securityDeposit %></td>
			     </tr>
			  </s:iterator> 
			  </s:if>			 
				<s:else>
					 <tr>
			   			<td colspan="11" align="center">No Records to Process</td>
			   		 </tr>
				</s:else>	
			 </table> 
					<s:div id="naviga" cssStyle="text-align: center" >
					<s:if test=" #session.ORG_SEARCH_RESULTS1.size >5 ">
				     
					
	<s:if test="#session.startValueOrgSearch!=0"><s:a theme="ajax" targets="bottom" href="agt_um_searchRet_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="bottom" href="agt_um_searchRet_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrgSearch==((#session.ORG_SEARCH_RESULTS1.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="bottom" href="agt_um_searchRet_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="bottom" href="agt_um_searchRet_Navigate.action?end=last">Last</s:a></s:else>      
    
					
					</s:if>
					</s:div>  		 
		 
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_um_searchRet_Search.jsp,v $
$Revision: 1.3 $
</code>