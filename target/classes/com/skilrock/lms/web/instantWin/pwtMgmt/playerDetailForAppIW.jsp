<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" /> 
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
</head> 

<body>
	<div id="top_form" class="withoutForm">
		<s:hidden value="%{#session.plrAlreadyReg}" name="plrAlreadyReg" id="plrAlreadyReg"/>
		<s:hidden value="%{#session.playerBean.plrId}" name="playerId" id = "playerId"/> 
		<h3> <s:text name="label.plr.detail" /> : - </h3>
			<s:if test="%{#session.plrAlreadyReg == 'YES'}">
				<table style="text-align:left" border="0" cellpadding="3" cellspacing="0" width="400px"> 
					<tr>
						<td> <s:textfield id = "emailId" name="emailId" key="label.email.id" value="%{#session.playerBean.emailId}" disabled="true" /> </td>
					</tr>
					<tr>
						<td> <s:textfield key="label.ph.no" id = "phone" name="phone" value="%{#session.playerBean.phone}" disabled="true" /></td>
					</tr>
					<tr>
						<td> <s:textfield key="label.add.line1" id = "plrAddr1" name="plrAddr1" value="%{#session.playerBean.plrAddr1}" disabled="true" /> </td>
					</tr>
					<tr>
						<td> <s:textfield key="label.add.line2" id = "plrAddr2" name="plrAddr2" value="%{#session.playerBean.plrAddr2}" disabled="true" /></td>
					</tr>
					<tr>
						<td> <s:textfield key="label.city" id="city" name="city" value="%{#session.playerBean.plrCity}" disabled="true" /></td>
					</tr>
					<tr> 
						<td> <s:textfield key="label.state" id="state" name="state" value="%{#session.playerBean.plrState}" cssClass="option" disabled="true" /></td>
					</tr>
					<tr>
						<td><s:textfield key="label.country" id="plrCountry" name="plrCountry" value="%{#session.playerBean.plrCountry}" disabled="true" /></td> 
					</tr>
					<tr> 
						<td style="display: none;"> <s:textfield theme="simple" key="label.po.box" id="plrPin" name="plrPin" value="%{#session.playerBean.plrPin}" maxlength="10" disabled="true" /> </td>
					</tr>
					<tr> 
						<td><s:textfield key="label.bank.acc.nbr" disabled="true" name="bankAccNbr" id="bankAccNbr" value="%{#session.playerBean.bankAccNbr}" size="15" maxlength="20" /> </td>
					</tr>
					<tr>
						<td><s:textfield key="label.bnk.name" disabled="true" name="bankName" id="bankName" value="%{#session.playerBean.bankName}" size="15" maxlength="38" /> </td>
					</tr>
					<tr>
						<td><s:textfield key="label.bank.br.name" disabled="true" name="bankBranch" id="bankBranch" value="%{#session.playerBean.bankBranch}" size="15" maxlength="20" /> </td>
					</tr>
					<tr>
						<td><s:textfield key="label.bank.city.name" disabled="true" name="locationCity" id="locationCity" value="%{#session.playerBean.locationCity}" size="15" maxlength="35" /></td>
					</tr>
				</table>
			</s:if>
			<s:else>
				<table align="left" border="0" cellpadding="3" cellspacing="0" width="400px">
					<tr>
						<td colspan="2"><div id="ei_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td><s:textfield name="emailId" key="label.email.id" required="true" id="emailId" size="15" maxlength="45" /></td>
					</tr>
					<tr>
						<td colspan="2"><div id="ph_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td><s:textfield key="label.ph.no" required="true" name="phone" id="phone" size="15" maxlength="15"/></td>
					</tr>
					<tr>
						<td colspan="2"><div id="pa1_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td><s:textfield key="label.add.line1" required="true" name="plrAddr1" id="plrAddr1" size="15" maxlength="30"/></td>
					</tr>
					<tr>
						<td colspan="2"><div id="pa2_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td><s:textfield key="label.add.line2" name="plrAddr2" id="plrAddr2" size="15" maxlength="30"/></td>
					</tr>
					<tr>
						<td colspan="2"><div id="pc_err" style="text-align: center"></div></td>
					</tr>
					<s:select key="label.country" name="plrCountry" id="plrCountry" cssClass="option" headerKey="-1" headerValue="%{getText('label.please.select')}" value="-1" list="%{#session.countryList}" onchange="retrieveURL('/com/skilrock/lms/web/userMgmt/getState.action?country=' + this.value,'stDiv')" required="true" /> 
					<tr>
						<td colspan="2"><div id="ps_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td id="label" align="right">State:<font color="red">*</font></td>
						<td>
							<div id="stDiv">
								<select class="option" id="state" name="state">
									<option class="option" value="-1">
										<s:text name="label.please.select" />
									</option>
								</select>
							<div>
						</td>
					</tr>
					<tr>
						<td colspan="2"><div id="pct_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td id="label" align="right"><s:text name="label.city" /><span class="required">*</span>: </td>
						<td><span id="charactersCity"> <select class="option" name="plrCity" id="plrCity"> <option value="----Please Select----"><s:text name="label.please.select" /> </option> </select> </span></td>
					</tr>
					<tr>
						<td colspan="2"><div id="pp_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td style="display: none;"><s:textfield theme="simple" key="label.po.box" required="true" name="plrPin" id="plrPin" size="15" maxlength="10" value="0"></s:textfield> </td> 
					</tr>
					<tr>
						<td colspan="2"><div id="ban_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td> <s:textfield key="label.bank.acc.nbr" name="bankAccNbr" id="bankAccNbr" size="15" maxlength="20" /> </td>
					</tr>
					<tr>
						<td colspan="2"><div id="bkn_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td> <s:textfield key="label.bnk.name" name="bankName" id="bankName" size="15" maxlength="38" /> </td>
					</tr>
					<tr>
						<td colspan="2"><div id="bnb_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td> <s:textfield key="label.bank.br.name" name="bankBranch" id="bankBranch" size="15" maxlength="20" /> </td>
					</tr>
					<tr>
						<td colspan="2"> <div id="bnlc_err" style="text-align: center"></div></td>
					</tr>
					<tr>
						<td> <s:textfield key="label.bank.city.name" name="locationCity" id="locationCity" size="15" maxlength="35" /> </td>
					</tr>
				</table>
			</s:else>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
playerDetailForApp.jsp,v $ $Revision: 1.3 $ </code>