<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control","no-store"); //HTTP 1.1
		response.setHeader("Pragma","no-cache"); //HTTP 1.0
		response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	%>
<head>
	<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<s:head theme="ajax" debug="false" /> 
</head>

<body>
    <div id="wrap">
	    <div id="top_form1">
	    	<s:form id="playerPWT" name="playerPWT" action="pwt_plrRegAndApprovalReqAtBO.action" onsubmit="return validatePlrDtl()">
	    		<s:hidden value="%{verifyTicketResponseBean.tktNbr}" id = "tktNbr" name = "tktNbr"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.tktStatus}" id = "tktStatus" name = "tktStatus"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.paymentTime}" id = "paymentTime" name = "paymentTime"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.winningAmt}" id = "winningAmt" name = "winningAmt"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.purchaseTime}" id = "purchaseTime" name = "purchaseTime"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.tktData}" id = "tktData" name = "tktData"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.claimTime}" id = "claimTime" name = "claimTime"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.purchasedFrom}" id = "purchasedFrom" name = "purchasedFrom"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.paymentAllowed}" id = "paymentAllowed" name = "paymentAllowed"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.errorCode}" id = "errorCode" name = "errorCode"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.errorMsg}" id = "errorMsg" name = "errorMsg"></s:hidden>
	    		<s:hidden value="%{verifyTicketResponseBean.isPlayerReg}" id = "isPlayerReg" name = "isPlayerReg"></s:hidden>
			    <tr>
			    	<th><s:text name="label.winning.amnt"/> <s:set name="pwtAmount" value="%{verifyTicketResponseBean.winningAmt}"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmount")) %></th>
			    </tr>
				<s:hidden value="player" name="playerType" id = "playerType" />	
				<tr>
					<td>
						<table  border="0" cellpadding="3" cellspacing="0" border="1" width="400px">
							<tr>
								<td>
									<table  border="0" cellpadding="3" cellspacing="0" id="plrDetail">							
										<tr>
											<td colspan="2"><div id = "fn_err" style="text-align: center"></div></td>
										</tr>
										<tr>
											<td><label><s:text name="label.fname"/>&nbsp;:</label></td>
											<td><table><s:textfield name="firstName" id="firstName"   required="true" size="10" maxlength="15" onchange="_id.non('subAny');_id.non('remainDetail')"/></table></td>
										</tr>
										<tr>
											<td colspan="2"><div id="ln_err" style="text-align: center"></div></td>
										</tr>
										<tr>
											<td><label><s:text name="label.lname"/>&nbsp;:</label></td>
											<td><table><s:textfield name="lastName" id="lastName"  required="true" size="10" maxlength="15" onchange="_id.non('subAny');_id.non('remainDetail')"/></table></td>
										</tr>
										<tr>
											<td colspan="2"> <div id="it_err" style="text-align: center"></div></td>
										</tr>
										<tr>
											<td><label><s:text name="label.id.type"/>&nbsp;:</label></td>
											<td>
												<table>
													<s:select cssClass="option" headerKey="-1" headerValue="%{getText('label.please.select')}" name="idType" id="idType" required="true" list="{getText('label.passport'),getText('label.drivng.licence'),getText('label.nid')}"  onchange="_id.non('subAny');_id.non('remainDetail')"/>
												</table>
											</td>
										</tr>
										<tr>
											<td colspan="2"><div id = "in_err" style="text-align: center"></div></td>
										</tr>
										<tr>
											<td><label><s:text name="label.id.no"/>&nbsp;:</label></td>
											<td><table><s:textfield name="idNumber" id="idNumber"  required="true" size="10" maxlength="15"  onchange="_id.non('subAny');_id.non('remainDetail')"/></table></td>
										</tr>
										<tr>
											<td colspan="2">
												<div id="getPlrDetails" style="text-align: center">
													<a href="#" onclick="verPlrDetails('pwt_common_fetchPlayerDetails.action', 'remainDetail')"><s:text name="label.verify.plr"/></a>
									  			</div>
							  				 </td>
										</tr>
										<tr>
											<td colspan="2"><div id="remainDetail"></div></td>
										</tr>
							     	</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<div id="subCheck" style="position:absolute; display:none">
						<input style = "margin-left : 320px" type = "button" onclick="return submitPlrDetails('pwt_plrRegAndApprovalReqAtBO.action', 'regPlayer')" value = 'Submit and Pay'/>
						</div>
						<%-- <s:submit cssClass="button" targets="regPlayer" formId="playerPWT"  key="btn.send.req.or.save" cssStyle="display: none;" id="subAny" />	 --%>		
					</td>
				</tr>
			</s:form>
		</div>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_PlayerRegForDirectplrIW.jsp,v $
$Revision: 1.3 $
</code>