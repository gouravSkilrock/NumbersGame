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
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="js/pwtDirPlayer.js" ></script>
	<s:head theme="ajax" debug="false" /> 
	
</head>

<body>

	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
    <div id="wrap">
    <div id="top_form">
    
    <h3>
    	Winning Amount is
		<s:set name="pwtAmount" value="%{#session.pwtDetailMap.pwtBean.pwtAmount}"/>
		<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmount")) %>
	</h3>

    <s:form id="playerPWT" name="playerPWT" onsubmit="return validatePlrDtl()" action="com/skilrock/lms/web/scratchService/pwtMgmt/pwt_plrRegAndApprovalReq.action">    
		
		
		<table border="0" cellpadding="3" cellspacing="0" width="400px">
			<tr>
				<th nowrap="nowrap">
					<input type="radio"  checked="checked" value="player" name="playerType" 
							onclick="_id.blk('plrDetail');_id.non('subAny');_id.non('remainDetail')""  />Player Verification
				</th>
				<th nowrap="nowrap">
					<s:if test="%{!#session.pwtDetailMap.pwtBean.isHighLevel}">
						<input type="radio" value="anonymous" name="playerType" onclick="_id.non('plrDetail');_id.blk('subAny');_id.non('remainDetail')"" />Anonymous Player
					</s:if>
				</th>
			</tr>
		</table>
		
		
		
		<table  border="0" cellpadding="3" cellspacing="0" id="plrDetail"  width="400px">							
			<tr><td colspan="2"><div id = "fn_err" style="text-align: center"></div></td></tr>
			<tr>
				<td align="right"><label>First Name&nbsp;:</label></td>
				<td><table><s:textfield name="firstName" id="firstName"   required="true" size="15" maxlength="15" onchange="_id.non('subAny');_id.non('remainDetail')" /></table></td>
			</tr>
						
			<tr><td colspan="2"><div id="ln_err" style="text-align: center"></div></td></tr>
			<tr>
				<td align="right"><label>Last Name&nbsp;:</label></td>
				<td><table><s:textfield name="lastName" id="lastName"  required="true" size="15" maxlength="15" onchange="_id.non('subAny');_id.non('remainDetail')" /></table></td>
			</tr>
						
			<tr><td colspan="2"> <div id="it_err" style="text-align: center"></div></td></tr>
			<tr>
				<td align="right"><label>ID Type&nbsp;:</label></td>
				<td><table>
					<s:select cssClass="option" headerKey="-1" headerValue="-- Please Select --"  
							name="idType" id="idType" required="true"
							list="{'Passport','Driving Licence','NID'}"  onchange="_id.non('subAny');_id.non('remainDetail')" />
					</table>
				</td>
			</tr>
						
			<tr><td colspan="2"><div id = "in_err" style="text-align: center"></div></td></tr>
			<tr>
				<td align="right"><label>ID Number&nbsp;:</label></td>
				<td align="left"><table><s:textfield name="idNumber" id="idNumber"  required="true" size="15" maxlength="15" onchange="_id.non('subAny');_id.non('remainDetail')" /></table></td>
			</tr>
						
			<tr>
				<td colspan="2">
					<div id="getPlrDetails" style="text-align: center">
						<a href="#" onclick="verPlrDetails('com/skilrock/lms/web/scratchService/pwtMgmt/pwt_common_fetchPlayerDetails.action', 'remainDetail')" 
							>Verify Player</a>
					</div>
				</td>
			 </tr>
		</table>					
		
	    
	    <br />
		<div id="remainDetail" ></div>
		
		
		
		<table width="400px" style="text-align: center">	
			<tr>
				<td>
					<div id="subCheck" style="position:absolute;"></div>
					<s:submit cssClass="button" formId="playerPWT" value="Save Winning Data" 
						cssStyle="display: none;" id="subAny" />			
					
				</td>
			</tr>	
	 	</table>
	 	
	 	
	 	
	</s:form>	
	
	</div>
	</div>	
</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: playerVerForApp.jsp,v $
$Revision: 1.3 $
</code>