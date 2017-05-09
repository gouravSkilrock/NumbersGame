<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

   
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/commonOla.js"></script>
	<s:head theme="ajax" debug="false" />
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath()%>"</script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
			
	
</head>

	<body onload="fetchActiveWallets('<%=request.getContextPath()%>')">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

				<h1>
					 Pin Status Report 
				</h1>
			
					<s:form action="olaPinStatusReport_Search" onsubmit="">
						<div style="color: red; text-align: left;"><s:actionmessage /></div>
						<s:actionerror />
					<table border="0"  cellspacing="0" cellpadding="2" >
					<tr>
								<td colspan="2">
									<div style="color: red; text-align: center" id="walletName_e"></div>
					
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<s:select label="Wallet Name" name="walletName" headerKey="-1"
										id="walletName" headerValue="--All--" list="{}"
										cssClass="option" required="true"
										onchange="displayemail(this.value);"></s:select>
								</td>
							</tr>
					<tr>
								<td>
									<div style="color: red; text-align: center" id="distributorType_e"></div>
								</td>
						</tr>
						<tr>
							<td><label class="label">
										Select Pin Distributor
										<span><font color="RED">*</font> </span>:&nbsp;
									</label></td>
								<td >
									<table><s:select name="distributorType" headerKey="-1"
										id="distributorType" headerValue="--All--" list="#{'PW':'PW','OLA':'OLA'}"
										cssClass="option" required="true"
										onchange=" "></s:select></table>
								</td>
						</tr>
						<tr>
								<td>
									<div style="color: red; text-align: center" id="denoType_e"></div>
								</td>
						</tr>
						<tr>
								<td><label class="label">
										Denomination   Type
										<span><font color="RED">*</font> </span>:&nbsp;
									</label></td>
								<td >
									<table><s:select name="denoType" headerKey="-1"
										id="denoType" headerValue="--All--" list="#{50:50,100:100,500:500,1000:1000,5000:5000}"
										cssClass="option" required="true"
										onchange=" "	 ></s:select></table>
								</td>
								
								
						</tr>
					</table>	
									
				
					<br></br>
										<s:submit value="Submit" align="center" cssClass="button" targets="down" theme="ajax" />
				</s:form>
				
					<div id="down"  style="overflow-x:auto;overflow-y:hidden;"></div>
		
			</div>
		</div>
		

	</body>
	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_ola_rep_pin_status_menu.jsp,v $
$Revision: 1.3 $
</code>