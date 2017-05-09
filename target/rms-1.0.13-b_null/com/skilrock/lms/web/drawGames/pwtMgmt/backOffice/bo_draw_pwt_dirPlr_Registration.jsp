<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ page import="java.util.*,com.skilrock.lms.dge.beans.PWTDrawBean,com.skilrock.lms.dge.beans.DrawIdBean,com.skilrock.lms.dge.beans.FortunePurchaseBean,com.skilrock.lms.dge.beans.PanelIdBean" %>
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
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/pwtMgmt/backOffice/js/pwtDirPlayerBO.js" ></script>
	
	<s:head theme="ajax" debug="false" /> 
	<script type="text/javascript">
	function test()
	{
		if($("#clicked").val()=="clicked")
		{
		return validatePlrDetails();
		}
		else if ($('input:radio[name=playerType]:checked').val()=="anonymous")
			return true;
		else
		{
		alert("Please verify first.");
		return false;
		}
		
	}
	function updateClick()
	{
	$("#clicked").val("clicked");
	}
		function fetchCityList() {
				//alert('hi !!!!!!!!!!');
				//alert(projectName);
			var country = _id.o('plrCountry').value;
			var state = _id.o('plrState').value;
	 		_ajaxCall(projectName+"/com/skilrock/lms/web/userMgmt/getCity.action?country=" + country + "&state=" + state,'charactersCity');
		}
	</script>
</head>

<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
    <div id="wrap">
    <div id="top_form">
    	<s:form id="playerPWT" name="playerPWT"  onsubmit="return test()">   
    	 <s:if test="%{#session.isMasAppReq}">
    	 <h3><s:text name="label.win.amt"/>: <b><s:property value="%{#session.PWT_AMT}"/></b></h3>
    	 </s:if>
    	 <s:else>
	  <h3><s:text name="msg.win.needs.approve.from.bo.pls.reg"/>...</h3>
		</s:else>
		<table border="0" cellpadding="3" cellspacing="0" width="400px">
			<tr>
				<th nowrap="nowrap">
					<input type="radio"  checked="checked" value="player" name="playerType" 
							onchange="_id.blk('plrDetail');_id.non('subAnyPlr');_id.blk('remainDetail');_id.non('subAny');" /><s:text name="label.plr.verify"/>
				</th>
				<s:if test="%{#session.isMasAppReq}">
				<th nowrap="nowrap">
					
						<input type="radio" value="anonymous" name="playerType" onclick="_id.non('plrDetail');_id.non('subAny');_id.blk('subAnyPlr');_id.non('remainDetail');"" /><s:text name="label.anonymous.plr"/>
					
				</th>
				</s:if>
			</tr>
		</table>
					
				<table  border="0" cellpadding="3" cellspacing="0" id="plrDetail">							
										
					<tr><td colspan="2"><div id = "fn_err" style="text-align: center"></div></td></tr>
					<tr>
						<td><label><s:text name="label.fname"/>&nbsp;:</label></td>
						<td><table><s:textfield name="firstName" id="firstName"   required="true" size="10" maxlength="15" onchange="_id.non('subAny');_id.non('remainDetail')"/></table></td>
					</tr>
					
					<tr><td colspan="2"><div id="ln_err" style="text-align: center"></div></td></tr>
					<tr>
						<td><label><s:text name="label.lname"/>&nbsp;:</label></td>
						<td><table><s:textfield name="lastName" id="lastName"  required="true" size="10" maxlength="15" onchange="_id.non('subAny');_id.non('remainDetail')"/></table></td>
					</tr>
					
					<tr><td colspan="2"> <div id="it_err" style="text-align: center"></div></td></tr>
					<tr>
						<td><label><s:text name="label.id.type"/>&nbsp;:</label></td>
						<td><table><s:select cssClass="option" headerKey="-1" headerValue="%{getText('label.please.select')}"  
									name="idType" id="idType" required="true"
									list="{'Passport','Driving Licence','NID'}"  onchange="_id.non('subAny');_id.non('remainDetail')"/></table></td>
					</tr>
					
					<tr><td colspan="2"><div id = "in_err" style="text-align: center"></div></td></tr>
					<tr>
						<td><label><s:text name="label.id.no"/>&nbsp;:</label></td>
						<td><table><s:textfield name="idNumber" id="idNumber"  required="true" size="10" maxlength="15"  onchange="_id.non('subAny');_id.non('remainDetail')"/></table></td>
					</tr>
					
					<tr>
						<td colspan="2">
							
								<div id="getPlrDetails" style="text-align: center">
									<a href="#" onclick="verPlrDetails('com/skilrock/lms/web/scratchService/pwtMgmt/pwt_common_fetchPlayerDetails.action', 'remainDetail'),updateClick()" 
										><s:text name="label.verify.plr"/></a>
					  			</div>
							
					   </td>
				   </tr>
					<s:hidden id="clicked" value="" />
						<tr><td colspan="2">
			         <div id="remainDetail"></div>
		           </td></tr>
				</table>
				
				
			
	
				
		<tr><td colspan="2">
			<div id="subCheck" style="position:absolute;"></div>
			<s:submit cssClass="button" formId="playerPWT"  key="btn.send.req.or.save"
					 cssStyle="display: none;" id="subAny" action="pwt_plrRegAndApprovalReqAtBO"/>	
			<s:submit cssClass="button" formId="playerPWT"  key="btn.save.win.data" cssStyle="display: none;" id="subAnyPlr" action="pwt_AnonymousPlayerRegBO"/>		
			
		</td></tr>	
	 
	</s:form>	

</div>
</div>	
</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_draw_pwt_dirPlr_Registration.jsp,v $
$Revision: 1.3 $
</code>