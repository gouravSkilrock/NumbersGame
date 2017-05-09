<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/pwtMgmt/backOffice/js/ACT_ST5_Validation.js">
	</script>
	</head>

	<body >
	  <div id="top_form" class="withoutForm">
		<s:if test="%{varForPlayerPWTVerify == 'Yes'}">
			<table align="left" border="0" cellpadding="2" cellspacing="0">

				<s:iterator id="SearchGameResults" value="#session.PLAYER_DETAILS">
					<tr>
						<td>
							<s:textfield name="emailId" label="Email Id" value="%{emailId}"
								disabled="true"></s:textfield>
						</td>
					</tr>

					<tr>
						<td>
							<s:textfield label="Phone" name="phone" value="%{phone}"
								disabled="true"></s:textfield>
						</td>
					</tr>
					<tr>
						<td>
							<s:textfield label="Address Line1" name="plrAddr1"
								value="%{plrAddr1}" disabled="true"></s:textfield>
						</td>
					</tr>
					<tr>
						<td>
							<s:textfield label="Address Line2" name="plrAddr2"
								value="%{plrAddr2}" disabled="true"></s:textfield>
						</td>
					</tr>
					<tr>
						<td>
							<s:textfield label="City" name="plrCity" value="%{plrCity}"
								disabled="true"></s:textfield>
						</td>
					</tr>
					<tr>
						<td>
							<s:textfield label="State" name="plrState" value="%{plrState}" cssClass="option"
								disabled="true"></s:textfield>
						</td>
					</tr>

					<tr>
						<td>
							<s:textfield label="Country" name="plrCountry"
								value="%{plrCountry}" disabled="true"></s:textfield>
						</td>
					</tr>
					<tr>
						<td>
							<s:textfield label="PO Box No." name="plrPin" value="%{plrPin}"
								disabled="true" maxlength="10"></s:textfield>
						</td>
					</tr>

				</s:iterator>
				<tr>
					<td></td>
					<td>
						<s:submit formId="playerPWT" action="donePWTProcess" cssClass="button"
							value="Save %{getText('PWT')} Data" />
					</td>
				</tr>
			</table>
		</s:if>
		<s:if test="%{varForPlayerPWTVerify == 'No'}">
			<table align="left" border="0" cellpadding="2" cellspacing="0">

				<tr>
					<td>
						<div id="emailId1" ></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:textfield name="emailId" label="Email Id" required="true"
							id="emailId" size="10" maxlength="45"></s:textfield>
					</td>
				</tr>
				<tr>
					<td>
						<div id="phone1" ></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:textfield label="Phone Number" required="true" name="phone"
							id="phone" size="10" maxlength="15"></s:textfield>
					</td>
				</tr>
				<tr>
					<td>
						<div id="plrAddr11" ></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:textfield label="Address Line1" required="true" name="plrAddr1"
							id="plrAddr1" size="10" maxlength="30"></s:textfield>
					</td>
				</tr>
				<tr>
					<td>
						<div id="plrAddr21"></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:textfield label="Address Line2" name="plrAddr2" size="10"
							maxlength="30"></s:textfield>
					</td>
				</tr>


				<tr>
					<td>
						<div id="plrCountry1" ></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:select label="Country" name="plrCountry" id="plrCountry" cssClass="option"
							headerKey="-1" headerValue="-- Please Select --" value="-1"
							list="#session.countryList"
							onchange="retrieveURL('getPlayerState.action?plrCountry=' + this.value,'characters')"
							required="true" />
					</td>
				</tr>
				<tr>
					<td>
						<div id="plrState1" ></div>
					</td>
				</tr>

				<tr>
					<td colspan="2" >
						<span id="characters">State:
								<select class="option" id="plrState">
									<option class="option" value="-1">
										--Please Select--
									</option>
								</select>
						</span>
						<br />
						
					</td>
				</tr>

				<tr>
					<td>
						<div id="plrCity1" ></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:textfield label="City" required="true" name="plrCity"
							id="plrCity" size="10" maxlength="15"></s:textfield>
					</td>
				</tr>
				<tr>
					<td>
						<div id="plrPin1" ></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:textfield label="PO Box No." required="true" name="plrPin"
							id="plrPin" size="10" maxlength="10"></s:textfield>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
					<div id="subCheck" style="position:absolute;"></div>
						<s:submit cssClass="button" formId="playerPWT"
							action="donePWTProcess" value="Save %{getText('PWT')} Data" id="subTrans" onclick="return _subTrans()"/>
					</td>
				</tr>

			</table>
		</s:if>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: playerDetail.jsp,v $
$Revision: 1.2 $
</code>