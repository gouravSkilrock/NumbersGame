<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/AutoComplete.css"
			media="screen" type="text/css" />
			<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%>

		</title>
		<s:head />

		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/backOffice/js/AutoComplete.js"></script>
		<script>var projectName="<%=request.getContextPath() %>"</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/ola/js/playerReg.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/ajaxRequest.js"></script>
			<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/ola/js/commonOla.js"></script>
		
		<script type="text/javascript">	
		$(document).ready(function(){
			var wallet='<s:property value="%{playerBean.walletName}"/>';
				if(wallet!=""){
				if(wallet=="TabletGaming" || wallet=="GroupRummy" || wallet=="GroupRummy"){
					$("#otherDiv").hide();
					$("#tabletGamingDiv").show();
				}else{
					$("#tabletGamingDiv").hide();
					$("#otherDiv").show();
				}
				$("#walletName").val(wallet);
			}
			$("#firstName").val('');
			$("#lastName").val('');
			$("#username").val('');
			$("#email").val('');
			$("#email2").val('');
			$("#phone").val('');
			$("#address").val('');
			$("#deposit").val('');
			$("#TGphone").val('');
			$("#TGemail").val('');
			$("#TGuserName").val('');
			$("#TGdeposit").val('');
			$("#TGdeposit2").val('');
		
			$("#otherDiv").hide();
			$("#tabletGamingDiv").show();
		
						
				var countryData=$('#countryData').val();
				var obj = jQuery.parseJSON(countryData);
   				$.each(obj,function(index,value){
   				//alert(value.countryCode);
     			$('#country').append(
                    $('<option></option>').val(value.countryCode).html(value.countryName));
   				});
 				$('#country').change(function(){
					$('#state')
    				.find('option')
    				.remove()
    				.end()
    				.append($('<option></option>').val(-1).html('--Please Select--'));
    				$('#city')
    				.find('option')
    				.remove()
    				.end()
    				.append($('<option></option>').val(-1).html('--Please Select--'));
 
 				//alert( $('#country').val());
 				var countryCode=$('#country').val();
 				$.each(obj,function(index,value){
  					// alert(value.countryName);
   					if(countryCode == value.countryCode){
  					// alert("equals");
   						if(value.stateBeanList != undefined){
    						$.each(value.stateBeanList,function(index1,value1){
   							//alert(value1.stateName);
  								$('#state').append(
                    				$('<option></option>').val(value1.stateCode).html(value1.stateName));
   								});
  							 }
   						}
    			});
 
 				});
 
  				$('#state').change(function(){
					$('#city')
					    .find('option')
					    .remove()
					    .end()
					    .append($('<option></option>').val(-1).html('--Please Select--'));
 
 					var stateCode=$('#state').val();
 						$.each(obj,function(index,value){
					  	// alert(value.countryName);
					   if($('#country').val() == value.countryCode){
					 	//  alert("equals");
					   if(value.stateBeanList != undefined){
					    $.each(value.stateBeanList,function(index1,value1){
					   //alert(value1.stateName);
					   if(stateCode == value1.stateCode){
					  // alert("equals");
					   if(value1.cityBeanList != undefined){
					   $.each(value1.cityBeanList,function(index2,value2){
					   //alert(value2.cityName);
  
     					$('#city').append(
                    		$('<option></option>').val(value2.cityCode).html(value2.cityName));
   						});
   					}
   				}
			});
   		}
   	}
   }); 
 });
	});
</script>
			
</head>
	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	
	
		<h1>Player Registration</h1>				
		<s:form name="reg_form" action="olaPlayerRegistrationSubmit"
			onsubmit="return isValidated();">
			<s:hidden id="countryData" name="countryData"></s:hidden>
				<s:token />
				<table border="0" width="780px" cellspacing="0" cellpadding="2" align="center">
				<tr>
					<th align="left" colspan="2">
						Player Registration Details :
					</th>
					<td><table id="checkAvail" style="display: none;" align="center"> <tr><td><div id="checkMsg"></div>
						</td></tr></table>	</td>
				</tr>
				<tr>
						<td>
									<div id="walletMsg"></div>
								</td>
							</tr>
						<tr>
							<td>
							<!--<s:select id="walletName" name="walletName" list="{}" label="Wallet Name" headerValue="--Please Select--" headerKey="-1" 
							cssClass="option" required="true" onchange="displayPassword(this.value);" ></s:select>
							-->
							<s:select label="Wallet Name" id="walletName" name="playerBean.walletName"  list="walletDetailsMap" listKey="value.walletDevName" listValue="value.walletDisplayName"
										cssClass="option" required="true" onchange="displayPassword(this.value);"></s:select>
							</td>
							
						</tr>
			</table>
			<s:hidden name="mobileNumberlimit"  id="mobileNumberlimit"  value="%{mobileNumberlimit}"></s:hidden>
			<div id="actionMsg" style="color: red; text-align: left"><s:actionmessage/></div>
			<div id="otherDiv">
			<table border="1" width="800px" cellspacing="0" cellpadding="0"
				bordercolor="#CCCCCC" align="center">
				<tr>
					<td>
						<table border="0" width="400px" cellspacing="0" cellpadding="2">
						
							<tr>
								<td>
									<div id="fnameMsg"></div>
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield label="First Name" maxlength="30" name="playerBean.firstName"
										id="firstName" required="true" />
								</td>
							</tr>

							<tr>
								<td>
									<div id="lnameMsg"></div>
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield label="Last Name" maxlength="30" name="playerBean.lastName"
										id="lastName" required="true" />
								</td>
							</tr>
							<tr>
								<td>
									<div id="genderMsg"></div>
								</td>
							</tr>
							<tr>
							
							<td>
							<table id="genDiv1" align="right"  >
									<tr >
									<td>
						
							<label class="label">
											Gender
										<span><font color="RED">*</font> </span>:&nbsp;
									</label>
							</td>
										</tr>
										</table>
							</td>
								<td>
									<table id="genDiv" align="center"  >
									<tr >
									<td>
									<s:radio name="playerBean.gender" list="#{'M':'MALE','F':'FEMALE'}" value="M" id="gender" required="true"/>
										</td>
										</tr>
										</table>
								
								</td>
							
							
							
							</tr>
							<tr>
								<td>
									<div id="dobMsg"></div>
								</td>
							</tr>
							<s:set name="stDate" id="stDate" value="#session.presentDate" />
							<%
								Calendar prevCal = Calendar.getInstance();
									prevCal.add(Calendar.YEAR,-18);
									String startDate = CommonMethods
											.convertDateInGlobalFormat(new java.sql.Date(prevCal
													.getTimeInMillis()).toString(), "yyyy-mm-dd",
													"yyyy-mm-dd");
							%>
							<tr>
								<td align="right">
									<label class="label">
										Date of Birth
										<span><font color="RED">*</font> </span>:&nbsp;
									</label>
								</td>
								<td>
									<input type="text" name="playerBean.dateOfBirth" id="dateOfBirth"
										value="<%=startDate%>" readonly size="12"></input>
									<input type="button"
										style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(document.getElementById('dateOfBirth'),'yyyy-mm-dd', this, '', false, '<%=startDate%>')" />
								</td>
							</tr>
							<tr>
								<td>
									<div id="unameMsg"></div>
								</td>
							</tr>
							<tr>
								<td align="center" colspan="2">
										<s:property value="errorMap.userError" />
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield label="Username" maxlength="30" name="playerBean.username"
										id="username" required="true" onchange="checkAvlUser()"/>
									
								</td>
							</tr>
														
							<tr >
								<td colspan="2">
								
								<table id="password11" style="display: none;"  >
								<tr><td>
									<div id="p1Msg"></div>
									</td></tr>
										 <tr><td>
									<s:password label="Password" maxlength="30" name="password1"
										id="password1" required="true" />
										</td></tr>
										
										
										</table>
								</td>
							</tr>
					
							<tr >
								<td colspan="2">
								
								<table id="password22" style="display: none;"  >
								<tr><td>
									<div id="p2Msg"></div>
									</td></tr>
										 <tr><td>
									<s:password label="Re-confirm Password" maxlength="30" name="password2"
										id="password2" required="true" />
										</td></tr>
										
										
										</table>
								</td>
							</tr>
							
						</table>
					</td>
					<td>
						<table border="0" width="400px" cellspacing="0" cellpadding="2">
							<tr>
								<td>
									<div id="emailMsg"></div>
								</td>
							</tr>
							<tr>
								<td align="center" colspan="2">
										<s:property value="errorMap.userError" />
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield label="E-mail" maxlength="50" name="playerBean.email"
										id="email" required="true" onchange="checkAvlEmail()"/>
								</td>
							</tr>
							<tr>
								<td>
									<div id="email2Msg"></div>
								</td>
							</tr>
							
							<tr>
								<td>
									<s:textfield label="Re-Confirm E-mail" maxlength="50" name="email2"
										id="email2" required="true" />
								</td>
							</tr>
							<!--<tr>
										<td align="right">
											<label class="label">
												<i>LandLine Number</i>&nbsp;
											</label>
										</td>
										<td align="center">
											<s:textfield theme="simple" name="cityCode" id="cityCode"
												cssStyle="width:24px;float:left;" required="true"
												readonly="true" /><b>-</b>
											<input type="text" maxlength="12" name="phone" id="phone"
												style="margin-right: 100px;" />
										</td>
									</tr>
							--><tr>
								<td>
									<div id="phoneMsg"></div>
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield label="Phone Number" name="playerBean.phone" maxlength="%{mobileNumberlimit}"
										id="phone" required="true" onchange="checkAvlMobile()"/>
											<s:hidden theme="simple" name="code" id="code"
												required="true" cssStyle="width:24px;float:left;"
												 />
								</td>
							</tr>
							<tr>
								<td>
									<div id="addressMsg"></div>
								</td>
							</tr>
							
							<tr >
							<td>
							<table id="addDiv1" align="right"  >
									<tr >
									<td>
						
							<label class="label">
											Address
										<span><font color="RED">*</font> </span>:&nbsp;
									</label>
							</td>
										</tr>
										</table>
							</td>
								<td>
									<table id="addDiv" align="center"  >
									<tr >
									<td>
									<s:textfield  maxlength="15" name="playerBean.address"
										id="address" required="true" />
										</td>
										</tr>
										</table>
								
								</td>
							</tr>
							<tr>
								<td>
									<div id="depositMsg"></div>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<s:textfield name="depositReqBean.depositAmt" label="Deposit Amount" id="deposit"
										maxLength="7" />
								</td>
							</tr>
							<!--<tr>
								<td>
												
									<s:select label="Deposit Amount" name="depositReqBean.depositAmt" headerKey="-1"
										id="deposit" headerValue="--Please Select--" list="#{50:50,100:100,500:500,1000:1000,5000:5000}"
										cssClass="option" required="true"
										onchange=" "	 ></s:select>
								</td>
							</tr>
							--><tr>
										<td align="center" colspan="2">
											<div id="orgcountry"></div>
										</td>
									</tr>
							<tr>
								<td>
									<div id="countryMsg"></div>
								</td>
							</tr>
							<tr>
							<td>
								<label class="label">Country
								<span class="required">*</span>:
								</label></td>
								<td>
									<s:select label="Country" cssClass="option" name="playerBean.country" id="country" list="{}" headerKey="-1" headerValue="--Please Select--" theme="simple" ></s:select> 
								</td>
							</tr>
							<tr>
								<td>
									<div id="stateMsg"></div>
								</td>
							</tr>
							<tr>
								<td>
								<label class="label">State
								<span class="required">*</span>:
								</label></td>
								<td>
									<s:select label="State" cssClass="option" name="playerBean.state" id="state" list="{}" headerKey="-1" headerValue="--Please Select--" theme="simple"></s:select> 
								</td>
							</tr>
							<tr>
								<td>
									<div id="cityMsg"></div>
								</td>
							</tr>
							<tr>
							<td>
								<label class="label">City
								<span class="required">*</span>:
								</label></td>
								<td>
									<s:select label="City" cssClass="option" name="playerBean.city" id="city" list="{}" headerKey="-1" headerValue="--Please Select--" theme="simple"></s:select> 
								</td>
							</tr>
									

									<!--<tr>
										<td align="center" colspan="2">
											<div id="stateMsg"></div>
										</td>
									</tr>
									<tr>
										<td class="tdLabel">
										<table id="stateDiv1" align="center"  >
										<tr >
										<td>
											<label class="label">
												State
												<span class="required">*</span>:
											</label>
											</td>
											</tr>
											</table>
										</td>
										<td >
										<table id="stateDiv" align="center"  >
										<tr >
										<td>
											<span id="characters" >
											 </span>
											 </td>
											 </tr>
											 </table>
										</td>
									</tr>

									<tr>
										<td align="center" colspan="2">
											<div id="cityMsg"></div>
										</td>
									</tr>
									<tr>
										<td  class="tdLabel">
											<table id="cityDiv1" align="center"  >
										<tr >
										<td>
											<label class="label">
												City
												<span class="required">*</span>:
											</label>
											</td>
											</tr>
											</table>
										</td>
										<td >
											<table id="cityDiv" align="center"  >
										<tr >
										<td>
											<span id="charactersCity"> 
												</span>
												</td>
												</tr>
												</table>
										</td>
									</tr>-->
							
							
						</table>
					</td>
				</tr>
			</table>
			</div>
			<div id="tabletGamingDiv" style="display: none;">
				<table border="1" width="800px" cellspacing="0" cellpadding="0" bordercolor="#CCCCCC" align="center">
					<tr>
						<td>
							<table border="0" cellspacing="4" cellpadding="0" width="780px">
							<tr>
								<td>
									<s:textfield label="User Name" maxlength="20" name="playerBean.tgUserName"
										id="TGuserName" required="true" onchange="checkAvlUser();"/>
								</td>
							</tr>
							<tr>
								<td></td>
								<td>
									<div id="TGUnameMsg"></div>
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield label="E-mail" maxlength="50" name="playerBean.tgEmail"
										id="TGemail" required="true" onchange="checkAvlEmail();"/>
								</td>
							</tr>
							<tr>
							<td></td>
								<td>
									<div id="TGEmailMsg"></div>
								</td>
							</tr>
							
							<tr>
								<td>
									<s:textfield label="Phone Number" name="playerBean.tgPhone" maxlength="%{mobileNumberlimit}"
										id="TGphone" required="true" onchange="checkAvlMobile()"/>
											<s:hidden theme="simple" name="code" id="code"
												required="true" cssStyle="width:24px;float:left;"/>
								</td>
							</tr>
							<tr>
							<td></td>
								<td>
									<div id="TGphoneMsg"></div>
								</td>
							</tr>
							
							
							<tr>
								<td colspan="2">
									<s:textfield name="depositReqBean.tgDepositAmt" label="Deposit Amount" id="TGdeposit"
										maxLength="7" />
								</td>
							</tr>
							<tr>
							<td></td>
								<td>
									<div id="TGdepositMsg"></div>
								</td>
							</tr>
							
							
							<tr>
								<td colspan="2">
									<s:textfield label="Verify Amount" maxLength="7" id="TGdeposit2"/>
								</td>
							</tr>
							<tr>
							<td></td>
								<td>
									<div id="TGdeposit2Msg"></div>
								</td>
							</tr>
							
							
			</table>
						</td>
					</tr>
				</table>
			</div>
			<table border="0" width="780px" cellspacing="0" cellpadding="2" align="center">
				<tr>
					<td>
						<s:submit id="submit" name="submit" value="Register" align="center" cssClass="button" />
					</td>
				</tr>
			</table>
		</s:form>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: olaPlayerRegMenu.jsp,v $ $Revision:
	1.1.8.4 $
</code>