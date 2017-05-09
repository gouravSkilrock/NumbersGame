
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page
	import="com.skilrock.lms.web.commercialService.playMgmt.CSWebSaleAction"%>
<%@page import="com.skilrock.lms.common.utility.LMSUtility"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/commercialService/playMgmt/js/csMgmt.js"></script>


		<s:head theme="ajax" debug="false" />

		<script type="text/javascript">
			function _subValidTest(subBtnId) {
				var divDisp = document.getElementById("subCheck");
				if (typeof subBtnId != "undefined") {
					document.getElementById(subBtnId).style.visibility = "hidden";
				}
				if (typeof divDisp != "undefined") {
					divDisp.innerHTML = "Please Wait....<img src=\"" + projectName + "/LMSImages/images/loadingAjax.gif\"/>";
				}
				var returnType = confirm("Do You Want to Proceed");
				if (returnType) {
					divDisp.style.display="none";
					return true;
				} else {
					if (typeof subBtnId != "undefined") {
						document.getElementById(subBtnId).style.visibility = "visible";
					}
					divDisp.innerHTML = "";
					return false;
				}
			}
			
			function isNumber(n) {
  				return !isNaN(parseFloat(n)) && isFinite(n);
			}
			
			function chkValidation(){
				_id.i('stateMsg','');
				_id.i('opMsg','');
				_id.i('voucherMsg','');
				_id.i('amtMsg','');
				_id.i('denomMsg','');	
				_id.i('confirmAmtMsg','');	
				_id.i('mobileMsg','');	
				var flag = true;
				
				var opCodeVal = _id.o('operatorCode').value;
				var stateVal = _id.o('state').value;
				var isCsShowCircle = '<%=(String) LMSUtility.sc.getAttribute("IS_CS_SHOW_CIRCLE")%>';
				var voucherVal = _id.o('voucherType').value;
				
				var flexiVal = _id.o('amt').value;
				var confirmVal = _id.o('confirmAmt').value;
				var denomVal = _id.o('denomination').value;
				var mobileNo = _id.o('mobileNo').value;
			
				if(isCsShowCircle == 'YES' && stateVal == -1){
					_id.i('stateMsg','Please Select State','e');
					flag = false;
				} 
				if (opCodeVal == -1){
					_id.i('opMsg','Please Select Operator','e');
					flag = false;
				} 
				if (voucherVal == -1){
					_id.i('voucherMsg','Please Select Voucher','e');
					flag = false;
				} else{
					if("Pin" == voucherVal){
						if(denomVal == -1){
							_id.i('denomMsg','Please Select Denomination','e');			
							flag = false;
						}
					} else if("flexi" == voucherVal){
						if (!isNumber(flexiVal) || flexiVal<=0){
							_id.i('amtMsg','Please Enter Valid Amount','e');
							flag = false;
						}
						if (!isNumber(confirmVal) || confirmVal<=0){
							_id.i('confirmAmtMsg','Please Enter Valid Amount','e');
							flag = false;
						}
						
						if(flexiVal != confirmVal){
							_id.i('confirmAmtMsg','Please Enter Same Amount','e');
							flag = false;
						}
						
						if (!isNumber(confirmVal) || confirmVal<=0){
							_id.i('confirmAmtMsg','Please Enter Valid Amount','e');
							flag = false;
						}
						
						if (!isNumber(mobileNo) || mobileNo<=0){
							_id.i('confirmAmtMsg','Please Enter Valid Mobile No','e');
							flag = false;
						}
					}
				}
				 
				if (flag != false){
					_id.i('stateMsg','');
					_id.i('opMsg','');
					_id.i('voucherMsg','');
					_id.i('amtMsg','');
					_id.i('denomMsg','');	
					_id.i('confirmAmtMsg','');	
					return _subValidTest('subTrans');	
					}
				else
					return flag;
			}	
		</script>

	</head>
	<body
		onload="isShowCircle('<%=(String) LMSUtility.sc.getAttribute("IS_CS_SHOW_CIRCLE")%>');">
		<div id="wrap">
			<div id="top_form">
				<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
				<h3>
					CS Sale
				</h3>
				<s:form action="ret_cs_sale_Submit"
					onsubmit="return chkValidation();" method="post">
					<table border="0">
						<tr>
							<td>
								<div id="opMsg"></div>
							</td>
							<td align="right">
								<s:select label="Select Operator" id="operatorCode"
									name="operatorCode" list="#session.STATE_OPERATORS_MAP"
									headerKey="-1" headerValue="--Please Select--"
									onchange="fillCircle(this.value)" cssClass="option" />
							</td>
						</tr>

						<!-- <tr>
							<td>
								<div id="stateMsg"></div>
							</td>
							<td align="right">
								<s:select label="Select Circle" id="state" name="state"
									headerKey="-1" headerValue="--Please Select--" list="{}"
									onchange="fillCategory(this.value)" cssClass="option" />
							</td>
						</tr> -->

						<tr>
							<td align="right">
								<div id="stateMsg"></div>
							</td>
						</tr>

						<tr align="right">
							<td align="right">
								<div id="circleDiv" style="display: none;">
									<i>Select Circle:</i>&nbsp;
									<s:select theme="simple" list="{}" id="state" headerKey="-1"
										headerValue="--Please Select--" name="state"
										onchange="fillCategory(this.value)"
										cssStyle="position: absolute;" cssClass="option" />
								</div>
							</td>

						</tr>

						<tr>
							<td align="right">
								<div id="voucherMsg"></div>
							</td>
							<td align="right">
								<s:select label="Select Type" id="voucherType"
									name="voucherType" list="{}" headerKey="-1"
									headerValue="--Please Select--"
									onchange="fillDenominations(this.value)" cssClass="option" />
							</td>

						</tr>

						<tr>
							<td align="right">
								<div id="denomMsg"></div>
							</td>
						</tr>

						<tr align="right">
							<td align="right">
								<div id="denomDiv" style="display: none;">
									<i>Select Denomination:</i>&nbsp;
									<s:select theme="simple" list="{}" id="denomination"
										headerKey="-1" headerValue="--Please Select--"
										name="denomination" cssStyle="position: absolute;"
										cssClass="option" />
								</div>
							</td>

						</tr>

						<tr>
							<td align="right">
								<div id="amtMsg"></div>
							</td>
						</tr>

						<tr>
							<td align="right">
								<div id="amtDiv1" style="display: none;">
									<i>Select Amount:</i>
								</div>
							</td>
							<td>
								<div id="amtDiv2" style="display: none;">
									<s:textfield theme="simple" id="amt" name="amt"
										cssClass="option" />
								</div>
							</td>

						</tr>

						<tr>
							<td align="right">
								<div id="confirmAmtMsg"></div>
							</td>
						</tr>

						<tr>
							<td align="right">
								<div id="confirmAmtDiv1" style="display: none;">
									<i>Confirm Amount:</i>
								</div>
							</td>
							<td>
								<div id="confirmAmtDiv2" style="display: none;">
									<s:textfield theme="simple" id="confirmAmt" cssClass="option" />
								</div>
							</td>

						</tr>

						<tr>
							<td align="right">
								<div id="mobileMsg"></div>
							</td>
						</tr>

						<tr>
							<td align="right">
								<div id="mobileDiv1" style="display: none;">
									<i>Enter Mobile No:</i>
								</div>
							</td>
							<td>
								<div id="mobileDiv2" style="display: none;">
									<s:textfield theme="simple" id="mobileNo" name="mobileNo"
										cssClass="option" />
								</div>
							</td>

						</tr>

						<tr>
							<td align="left">
								<div id="subCheck"></div>
								<s:submit value="Buy" id="subTrans" />
							</td>
						</tr>
					</table>
				</s:form>
				<div id="down" style="text-align: center"></div>

			</div>
		</div>

	</body>

</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: ret_cs_sale_Menu.jsp,v $ $Revision: 1.1.2.8 $
</code>