<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
	<s:head theme="ajax" debug="false"/>
	</head>


	<body>
	
	<div id="wrap">
     <div id="top_form">
			
				<s:form id="form1" name="form1">


					<s:iterator id="SearchGameResults" value="#session.SEARCH_AGENT">
						<table width="80%" border="0" cellpadding="2" cellspacing="0">
							<tr>
								<td>
									<table>
										<tr>
											<s:set name="orderId" value="#session.OrgId" />
											<th>
												Order Number
											</th>
											<td>
												<s:property value="%{orderId}" />
												<s:hidden name="orderId" value="%{orderId}" />
											</td>
											<th>
												<s:property value="#application.TIER_MAP.AGENT" /> Name
											</th>
											<td>
												<s:property value="name" />
											</td>
										</tr>

										<tr>
											<th>
												Address
											</th>
											<td>
												<s:property id="gn" value="address" />
											</td>
										</tr>
										<tr>
											<td>
												<s:property value="city" />, 
									
												<s:property value="state" />
										</tr>
										<tr>
											<td>
												<s:property value="country" />
										</tr>
										<tr>
											<th>
												Credit Limit :
											</th>
											<s:set name="strstrCreditLimit" value="%{strCreditLimit}" />
											<td>
												<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strstrCreditLimit")) %>
												<s:hidden id="availableCredit" value="%{availableCredit}"></s:hidden>
											</td>
											<th>
												Current Credit:
											</th>
											<s:set name="strstrCreditAmount" value="%{strCreditAmount}" />
											<td>
												<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strstrCreditAmount")) %>
											</td>
										</tr>

									</table>
								</td>
							</tr>
						</table>
					</s:iterator>
					<table border="1" cellpadding="2" cellspacing="0">

						<tr>
							<th colspan="8" align="center">
								Order Details
							</th>
						</tr>
						<tr>
							<th>
								S No.
							</th>
							<th>
								Game Name
							</th>
							<th>
								Game Number
							</th>
							<th>
								Requested Books
							</th>
							<th>
								Books at Back Office
							</th>
							<th>
								Approved Books
							</th>
							<th>
								Remaining Books
							</th>
							<th>
								Alloted Books
							</th>


						</tr>

						<s:iterator id="SearchGameResults" value="#session.SEARCH_RESULTS"
							status="sno">
							<tr>
								<s:hidden name="ListSize" id="ListSize"
									value="%{#session.SEARCH_RESULTS.size}"></s:hidden>
								<td>
									<s:property value="#sno.index+1" />
								</td>
								<td>
									<s:property value="%{gameName}" />
									<s:hidden name="gameName" value="%{gameName}" id="gameName"></s:hidden>
								</td>
								<td>
									<s:property value="%{gameNumber}" />
									<s:hidden name="gameNumber" value="%{gameNumber}"></s:hidden>
									<s:hidden name="gameId" value="%{gameId}"></s:hidden>
								</td>
								<td>
									<s:property value="%{nbrOfBooksReq}" />
									<s:hidden name="nbrOfBooksReq" value="%{nbrOfBooksReq}"></s:hidden>
								</td>
								<td>
									<s:property value="%{nbrOfBooksAtBO}" />
									<s:hidden name="nbrOfBooksAtBO" value="%{nbrOfBooksAtBO}"></s:hidden>
								</td>
								<td>
									<s:property value="%{nbrAppBooks}" />
									<s:hidden name="nbrAppBooks" value="%{nbrAppBooks}"></s:hidden>
								</td>
								<td>
									<s:property value="%{differenceBtBOndApprBooks}" />
									<s:hidden name="differenceBtBOndApprBooks"
										value="%{differenceBtBOndApprBooks}" id="remain"></s:hidden>
								</td>
								<td>
									<s:textfield theme="simple" name="allowedBooks"
										value="%{allowedBooks}" id="allow"></s:textfield>
								</td>
							</tr>
							<s:hidden name="oneBookPrice" value="%{bookPrice}"></s:hidden>
						</s:iterator>


						
						<tr align="center" >
							<td colspan="8">
								<s:submit theme="simple" formId="form1" action="bo_om_processOrdReq_Save" cssClass="button" align="center"
									value="Approve" onclick="return validateAllowBook()" />
							
								<s:submit formId="form1" theme="simple" value="Deny" cssClass="button" align="center" action="bo_om_processOrdReq_Deny"
									 onclick="return verSpChar()"  />
							</td>
						</tr>

					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_om_processOrdReq_EditDet.jsp,v $
$Revision: 1.3 $
</code>