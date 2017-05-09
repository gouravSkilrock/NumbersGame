<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>


		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<style type="text/css">
			.colorclass {
				background-color: #cccccc;
			}
		</style>

		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<s:head theme="ajax" debug="false" />
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/instantPrint/gameMgmt/backOffice/js/gameUploadValidation.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/instantPrint/gameMgmt/backOffice/js/validation.js"></script>
	</head>
    <body onload="onLoadFn('bo_gm_newGame_FetchProfitRate.action')">


		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					Game Details:
				</h3>
				<s:actionerror />
				<s:form id="uploadForm" name="uploadForm" action="bo_gm_newGame_Save"
					method="POST" enctype="multipart/form-data"
					onsubmit="return ST5_Validation()">
					<tr>
						<td>
					<table cellpadding="2" cellspacing="0" border="0" width="450">
						<tr>
							<td>
								<div id="gameNumber1" align="center"></div>
							</td>
						</tr>
						<s:textfield name="gameNumber" id="gameNumber" label="Game Number"
							size="8" maxlength="4" required="true" />
						<tr>
							<td>
								<div id="gameName1" align="center"></div>
							</td>
						</tr>
						<s:textfield name="gameName" id="gameName" label="Game Name"
							size="8" maxlength="30" required="true" />
						<tr>
							<td>
								<div id="priceperTicket1" align="center"></div>
							</td>
						</tr>
						<s:textfield name="priceperTicket" id="priceperTicket"
							label="Price Per Ticket" size="8" maxlength="6" required="true" />
						<tr>
							<td>
								<div id="ticketpetBook1" align="center"></div>
							</td>
						</tr>
						<s:textfield name="ticketpetBook" id="ticketpetBook"
							label="Tickets Per Book" size="8" maxlength="5" required="true" />
						<tr>
							<td>
								<div id="booksperPack1" align="center"></div>
							</td>
						</tr>
						<s:textfield name="booksperPack" id="booksperPack"
							label="Books  Per Pack" size="8" maxlength="5" required="true" />
							
							<tr>
							<td>
								<div id="gameVirnDigits1" align="center"></div>
							</td>
						</tr>
						<s:textfield name="gameVirnDigits" id="gameVirnDigits"
							label="Enter Game Virn Digits" size="15" maxlength="2" required="true" />
						<tr>
							<td>
								<div id="generate"></div>
							</td>
							<td align="left">
							<input type="button" value="generate"
									onclick="return retrieveURL()" class="button" />
							</td>
						</tr>
						
						<tr>
							<td>
					

								
								<s:textfield label="Digits of Book" maxlength="1" size="8"
									name="digitsofBook" id="digitsofBook"></s:textfield>
								<s:textfield label="Digits of Pack" maxlength="1" size="8"
									name="digitsofPack" id="digitsofPack"></s:textfield>
								<s:textfield label="Digits of Ticket" maxlength="1" size="8"
									name="digitsofTicket" id="digitsofTicket"></s:textfield>
							</td>
						</tr>
					
							<tr>
								<td>
										<div id="agentSaleCommRate1" align="center"></div>
								</td>
							</tr>
							<s:textfield name="agentSaleCommRate" id="agentSaleCommRate"
							label="%{#application.TIER_MAP.AGENT} sale Profit Rate" size="8" maxlength="4"
							value="%{#session.x.agent_sale_comm_rate}" required="true" />

							<s:textfield  cssClass="colorclass"
							name="agentPWTCommRate" id="agentPWTCommRate"
							label="%{#application.TIER_MAP.AGENT} %{getText('PWT')} Profit Rate" size="8" maxlength="4"
							value="%{#session.x.agent_pwt_comm_rate}" required="true" />
						<tr>
							<td>
								<div id="retailerSaleCommRate1" align="center"></div>
							</td>
						</tr>
						<s:textfield name="retailerSaleCommRate" id="retailerSaleCommRate"
							label="%{#application.TIER_MAP.RETAILER} sale Profit Rate" size="8" maxlength="10"
							value="%{#session.x.retailer_sale_comm_rate}" required="true" />


						<s:textfield  id="retailerPWTCommRate"
							cssClass="colorclass" name="retailerPWTCommRate"
							label="%{#application.TIER_MAP.RETAILER} %{getText('PWT')} Profit Rate" size="8" maxlength="3"
							value="%{#session.x.retailer_pwt_comm_rate}" required="true" />

						<s:hidden id="commRule" name="govtCommRule"
							value="%{govtCommRule}"></s:hidden>

						<tr>
							<td>
								<div id="govtCommRate1" align="center"></div>
							</td>
						</tr>
						<s:textfield id="govtCommRate" name="govtCommRate"
							label="Good Causes" size="8" maxlength="5" />

						<tr>
							<td>
								<div id="minAssuredProfit1" align="center"></div>
							</td>
						</tr>

						<tr>
							<td>
								<div id="minAssuredProfit" align="center">
									<s:textfield name="minAssProfit" id="minAssProfit" disabled="true"
										label="Minimum Assured Profit" size="8" maxlength="3" />
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div id=rank1></div>
							</td>
						</tr>
						<tr>
							<td>
								<s:label theme="simple" value="Select Rank Details File Path: " />
							</td>
							<td>
								<s:file id="rank" theme="simple" name="rankupload"
									accept="text/*"></s:file>
							</td>
						</tr>
						<tr>
							<td>
								<div id=image1></div>
							</td>
						</tr>
						<tr>
							<td>
								<s:label theme="simple" value="Select Image Details File Path: " />
							</td>
							<td>
								<s:file id="image" theme="simple" name="imageupload"
									accept="text/*"></s:file>
							</td>
						</tr>
						<tr>
							<td>
								<div id=printscheme1></div>
							</td>
						</tr>
						<tr>
							<td>
								<s:label theme="simple" value="Select Print Scheme XML File Path: " />
							</td>
							<td>
								<s:file id="printscheme" theme="simple" name="printschemeupload"
									accept="text/*"></s:file>
							</td>
						</tr>
                        <tr>
							<td>
								<div id="prizePayOutRatio1" align="center"></div>
							</td>
						</tr>
						
                        <tr>
							<s:textfield label="Prize Payout Ratio : " name="prizePayOutRatio" id="prizePayOutRatio" maxlength="5"></s:textfield>
						</tr>
                       <tr>
							<td>
								<div id="vatPer1" align="center"></div>
							</td>
						</tr>
						
						<s:if test="%{#session.x.vat_applicable=='no'}">
                         <tr>
                         		<s:textfield label="VAT % : " name="vatPercentage" id="vatPer" maxlength="5" readonly="true" value="0.00"></s:textfield>
						</tr>
						</s:if>
						<s:else>
						    <tr>
						    	<s:textfield label="VAT % : " name="vatPercentage" id="vatPer" maxlength="5"></s:textfield>
						</tr>
						</s:else>
                       <tr>
							<td>
								<div id="ticketsinScheme1" align="center"></div>
							</td>
						</tr>
                          <tr>
							<s:textfield label="Expected Tickets to Sale : " name="ticketsInScheme" id="ticketsinScheme" ></s:textfield>
						</tr>
						<tr>
						<td align="center" colspan="2">
						<div id="gameType1"></div>
						</tr><tr>
                       <s:select label="Game Type" name="gameType" list="{'Buy','Promo'}" headerValue="-Select-"></s:select>
                       </tr>
                       
                       <tr>
						<td align="center" colspan="2">
						<div id="printType1"></div>
						</tr><tr>
                       <s:select label="Print Type" name="printType" list="{'Image','Text'}" headerValue="-Select-"></s:select>
                       </tr>
						<tr>
							<td align="right">
								<s:submit theme="simple" value="Submit" cssClass="button" />
							</td>
							<td align="left">
						
								<s:reset theme="simple" value="Reset" cssClass="button" />
							</td>
						</tr>
					</table>
					</td>
					</tr>
				</s:form>
			</div>
		</div>
	</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_uploadNewGame_Menu.jsp,v $
$Revision: 1.2 $
</code>