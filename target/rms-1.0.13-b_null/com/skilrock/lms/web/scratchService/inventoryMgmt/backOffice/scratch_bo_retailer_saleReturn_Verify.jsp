<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
	type="text/css" />

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

<s:head theme="ajax" debug="false" />

<script>
function back()
{
//window.opener.document.getElementById("game_name").value=document.getElementById("game").value;
                            

window.history.back();
//document.window.history.forward(-2);
}
</script>
</head>
<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
		<div id="top_form">
			<h3><s:text name="label.sale.return"/></h3>
			<s:form action="bo_scratch_boToRetailerDirectSaleRefund_Save">
				<table>
					<tr>
						<td>
							<table align="left" cellpadding="2" cellspacing="0"
								bordercolor="#CCCCCC">
								<tr>
									<td align="right"><s:property
											value="#application.TIER_MAP.AGENT" /><s:text name="label.org"/> :</td>
									<td><b><s:property value="#session.ORG_NAME" /> </b></td>
								</tr>
								<tr>
									<td align="right"><s:text name="label.game"/>:</td>
									<td><s:set name="gameName" value="#session.GAME_NAME" />
										<s:hidden id="game" value="#gameName" /> <b> <s:property
												value="#session.GAME_NAME" /> </b></td>
								</tr>
							</table></td>
					</tr>
					<tr>
						<td><s:if test="#session.VERIFIED_PACK_LIST.size!=0">
								<table border="1" width="400" cellpadding="4"
									bordercolor="#CCCCCC" cellspacing="0">
									<tr>
										<td width="50%"><s:text name="Pack Number" /></td>
										<td width="50%"><s:text name="Validity Status" /></td>
									</tr>
									<s:iterator value="#session.VERIFIED_PACK_LIST">
										<tr>
											<td width="50%"><s:property value="%{packNumber}" /></td>
											<td width="50%"><font color="red"> <s:property
													value="%{status}" /> </font></td>
										</tr>
									</s:iterator>
								</table>
							</s:if> <br /></td>
					</tr>
					<tr>
						<td><s:if test="#session.VERIFIED_BOOK_LIST.size!=0">
								<table border="1" width="400" bordercolor="#CCCCCC"
									cellpadding="4" cellspacing="0">
									<tr>
										<td width="50%"><s:text name="Book Number" /></td>
										<td width="50%"><s:text name="Validity Status" /></td>
									</tr>
									<s:iterator value="#session.VERIFIED_BOOK_LIST">
										<tr>
											<td width="50%"><s:property value="%{bookNumber}" /></td>
											<td width="50%"><font color="red"> <s:property
													value="%{status}" /> </font></td>
										</tr>
									</s:iterator>
								</table>
							</s:if></td>
					</tr>
					<tr>
						<td><s:if test="%{saleReturnBO=='Valid'}">
								<table width="75%" cellpadding="4" cellspacing="0" border="0">
									<tr>
										<td align="right">
											<div id="subCheck" style="position:absolute;"></div> <s:submit
												theme="simple" id="subTrans" cssClass="button"
												key="btn.accept.valid.books.packs"
												onclick="return _subValid(this.id);" /></td>
									</tr>
								</table>
							</s:if> <s:else>
								<table border="0" width="75%" cellpadding="4" cellspacing="0">
									<tr>
										<td><s:a theme="simple"
												href="bo_scratch_boToRetailerDirectSaleRefund_Menu.action"><s:text name="label.do.another.sale.return"/></s:a>
										</td>
									</tr>
								</table>
							</s:else></td>
					</tr>
				</table>
			</s:form>
		</div>
	</div>
</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	scratch_bo_retailer_saleReturn_Verify.jsp,v $ $Revision: 1.3 $ </code>