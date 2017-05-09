<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />
		
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<s:head debug="false" />
	</head>
<%! boolean isValid =false; %>
	<body>
		<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
				
				  <h3>
					Submitted Payment Detail
				</h3>
	<table width="684" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">

						<tr>
							<td colspan="5" align="center">
							<b>	Total Payment Description</b>
							</td>
						</tr>
						<tr>
							<th width="12">
								S.No
							</th>
							<th>
								Cheque Nbr
							</th>
							<th>
								Bank
							</th>
							<th>
								Amount
							</th>
							<th>
								Status
							</th>
						</tr>
					  <s:set name="total" value="0.0"/>
						<s:iterator id="payList" value="#session.CHEQUE_PAYMENT_LIST" status="sno">
							<tr>
                                <td>
									<s:property value="#sno.index+1" /><s:set name="sNo" value="#sno.index+1"/>
								</td>								
								<td>
									<s:property value="chequeNo" />
								</td>
								<td>
									<s:property value="bankName" />
								</td>
								<td>
									<s:set name="amt" value="strAmount"/><%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("amt"))%>
								</td>
								<s:set name = "total2" value="%{strAmount}"/>
							<s:set  name = "total" value="#total.doubleValue()+#total2.doubleValue()"/>
                              <td>
                             	<s:property value="chqeueStatus" />
                             	<s:if test="%{chqeueStatus=='Submitted'}">
                             	<%isValid = true; %>
                             	</s:if>
								</td>
							</tr>
						</s:iterator>
						</table>
						<%if(isValid){ %>
		<div id="wrap">
			<div id="top_form">
				<h3>
					<s:property value="#application.TIER_MAP.AGENT" /> Payment done successfully
				</h3>
				
				<table>
				
				  <tr>
							<td>
								<a theme="simple" target="_blank"
									href="<%=request.getContextPath() %>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action">
									<div align="center">
										Show Temp Receipt
									</div>
								</a>
							</td>
						</tr>
				 <tr>
				 	<td>
				 		<a href="<%=request.getContextPath() %>/com/skilrock/lms/web/accMgmt/bo_act_chq_Menu.action">
									Submit more cheques
								</a>
							</td>
						</tr>
				</table>
						
			</div>
		</div>
	<%} %>			
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_chq_Success.jsp,v $
$Revision: 1.2 $
</code>