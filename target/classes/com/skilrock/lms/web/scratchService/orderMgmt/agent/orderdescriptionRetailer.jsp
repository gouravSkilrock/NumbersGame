<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>			
<s:head theme="ajax" debug="false"/>
	</head>

<body>
<center>
<form style="background-color: white;border: 0em;">
 <table border="1">
			 <tr><td colspan="10" align="left">
			    	<s:iterator id="SearchGameResults" value="#session.SEARCH_AGENT">			   
		
						 <table  border="0" >
								 <tr>
								  <s:set name="orderId" value="#session.OrgId"/>
								 <th>Order Number: </th><td><s:property value="%{orderId}"/> <s:hidden name="orderId" value="%{orderId}"/></td>
								 <th><s:property value="#application.TIER_MAP.RETAILER" /> Name: </th><td><s:property value="name"/></td>
								 </tr>
								
								 <tr>
								 <th>Address</th><td colspan="3" align="left"><s:property id="gn" value="address"/></td>
								 </tr>
								 
								  <tr>
								  	<td></td>
								  	<td colspan="3" align="left"><s:property value="city"/></td>
								  </tr>
								 <tr>
								    <td></td>
								 	<td colspan="3" align="left"> <s:property value="state"/></td>
								 </tr>
								 <tr>
								    <td></td>
								 	<td colspan="3" align="left"> <s:property value="country"/></td>
								 </tr>
								 <tr>
									 <th>Credit Limit : </th>
									 <s:set name="strstrCreditLimit" value="%{strCreditLimit}" />
									 <td><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strstrCreditLimit")) %><s:hidden  id="availableCredit" value="%{availableCredit}" ></s:hidden></td>
									 
									 <th>Current Credit: </th>
									 <s:set name="strstrCreditAmount" value="%{strCreditAmount}" />
									 <td>
									   <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strstrCreditAmount")) %>
									  </tr>
					   </table>
    		</s:iterator> 
		</td></tr>
		     	<tr><td colspan="8"><div id="allowedBooks1"></div></td></tr>
			    <tr>
			       <th>S No.</th>
			     <th>Game Name</th>
			     <th>Game Number</th>
			     <th>Requested Books</th>
			     <th>Books at <s:property value="#application.TIER_MAP.AGENT" /></th>
			      <th>Approved Books</th>
			     <th>Remaining Books</th>
			     <th>Alloted Books</th>
			    
			   </tr>
		
			  <s:iterator id="SearchGameResults" value="#session.SEARCH_RESULTS" status="sno">
			    <tr>
			       <s:hidden name="ListSize" id="ListSize" value="%{#session.SEARCH_RESULTS.size}"></s:hidden>
			       <td> <s:property  value="#sno.index+1" /></td>
			      <td> <s:property  value="%{gameName}" /><s:hidden name="gameName" value="%{gameName}" id="gameName"></s:hidden></td>
			      <td>  <s:property  value="%{gameNumber}" /><s:hidden name="gameNumber" value="%{gameNumber}" ></s:hidden><s:hidden name="gameId" value="%{gameId}" ></s:hidden></td>
			      <td><s:property  value="%{nbrOfBooksReq}" /> <s:hidden name="nbrOfBooksReq" value="%{nbrOfBooksReq}" ></s:hidden></td>
			      <td> <s:property  value="%{nbrOfBooksAtAgent}" /><s:hidden name="nbrOfBooksAtAgent" value="%{nbrOfBooksAtAgent}" ></s:hidden></td>
			       <td> <s:property  value="%{nbrAppBooks}" /><s:hidden name="nbrAppBooks" value="%{nbrAppBooks}" ></s:hidden></td>
			      <td> <s:property  value="%{differenceBtAgentandApprBooks}" /><s:hidden name="differenceBtAgentandApprBooks" value="%{differenceBtAgentandApprBooks}" id="remain"></s:hidden></td>
			       <td><s:textfield theme="simple" name="allowedBooks" value="%{allowedBooks}" id="allow"></s:textfield></td>
			    <s:hidden name="oneBookPrice" value="%{bookPrice}" ></s:hidden>
			     </tr>
			  </s:iterator> 		
	<tr>
		<td align="left" colspan="8">
			<s:submit theme="simple"  action="agt_om_processOrdReq_Save"  value="Approve" onclick="return validateAllowBook()" cssClass="button"/>
			<s:submit theme="simple" value="Deny" action="agt_om_processOrdReq_deny" onclick="return confirm('Are you sure to Deny Order ?')" onkeypress="return confirm('Are you sure to Deny Order ?')" cssClass="button"/>
		</td>
	</tr>		

 </table>
 </form>
 </center>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: orderdescriptionRetailer.jsp,v $
$Revision: 1.3 $
</code>