<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.Calendar"%>

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

	<body >
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

				<h1>
					Pin Generator 
				</h1>
			
					<s:form action="olaPinGenerator_Success" onsubmit="return verifyPinGenerator()">
						<div style="color: red; text-align: left;"><s:actionmessage /></div>
						<s:actionerror />
					<table border="0"  cellspacing="0" cellpadding="2" >
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
										id="distributorType" headerValue="--Please Select--" list="#{'PW':'PW'}"
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
										id="denoType" headerValue="--Please Select--" list="#{50:50,100:100,500:500,1000:1000,5000:5000}"
										cssClass="option" required="true"
										onchange=" "	 ></s:select></table>
								</td>
								
								
						</tr>
	
						<tr>
								<td>
									<div style="color: red; text-align: center" id="pinQuantity_e"></div>
								</td>
						</tr>
						<tr>
								<td><label class="label">
										Pin Quantity
										<span><font color="RED">*</font> </span>:&nbsp;
									</label></td>
								<td>
									<table>
									<s:textfield name="pinQuantity"  id="pinQuantity" maxlength="4" />
										</table> 
								</td>
						</tr>
						<tr>
								<td >
									<div style="color: red; text-align: center" id="commRate_e"></div>
								</td>
							</tr>

							<tr>
								<td><label class="label">
										Commission Rate
										<span><font color="RED">*</font> </span>:&nbsp;
									</label></td>
								<td >
									<table>
									<s:textfield name="commRate"  id="commRate" maxlength="5"/></table>
								</td>
							</tr>
						<tr>
								<td>
									<div style="color: red; text-align: center" id="partyType_e"></div>
								</td>
						</tr>
						<tr>
						<td><label class="label">
										Select Party for Pin
										<span><font color="RED">*</font> </span>:&nbsp;
									</label></td>
								<td>
								<table>
									<s:select  name="partyType" id="partyType" headerKey="-1" 
									headerValue="--Please Select--" cssClass="option" required="true"
										onchange=" " list="#{'2:RM':'KhelPlayRummy','3:GR':'GroupRummy'}" ></s:select>
										</table>
								</td>
						</tr>
								
						<tr>
						<td><label class="label">
									Select Expiry Date
										<span><font color="RED">*</font> </span>:&nbsp;
									</label></td>
							
							<td>
															
						
								<input type="text" name="expirydate" id="expirydate"
									value="<s:property value="#session.presentDate"/>" readonly
									size="12" onchange="resetType()" />
								<input type="button"
									style=" width:19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; "
									onclick="displayCalendar(document.getElementById('expirydate'),'yyyy-mm-dd', this, '<s:property value="#session.presentDate"/>','<s:property value="#session.presentDate"/>', '')" />
							</td>
				
								
						
						</tr>


					</table>	
									
				
					<br></br>
										<s:submit value="Submit" align="center" cssClass="button" />
				</s:form>
					
			</div>
		</div>
		

	</body>
	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: pinGeneratorMenu.jsp,v $
$Revision: 1.3 $
</code>