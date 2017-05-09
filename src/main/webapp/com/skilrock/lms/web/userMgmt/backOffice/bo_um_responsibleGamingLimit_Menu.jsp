<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
	response.setDateHeader("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%> <%!String isOffline = null;%>

		</title>
		<s:head theme="ajax" debug="false" />

		<script>
		function getRGDetails(id){
			//alert(_id.o(id).value);
			if(_id.o(id).value!=-1){
			_ajaxCall('<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_reponsibleGamingLimit_Details.action?criteriaType='+_id.o(id).value,'rgDetails');
			}else{
				_id.o('rgDetails').innerHTML="";
			}
			//alert(_id.o('rgDetails').innerHTML);
		}	
		
		function chkValidation(){
		var isValid=true;		
		var criAction=document.getElementsByName("criAction");
		var criLimit=document.getElementsByName("criLimit");
		var status=document.getElementsByName("status");
		for(var i=0;i<criAction.length;i++){
			if(criAction[i].value=="-1"){
				criAction[i].style.color="red";
				isValid=false;
				break;
			}
		}
		for(var i=0;i<criLimit.length;i++){
			if(!validateNumeric(criLimit[i].value)){
				criLimit[i].style.color="red";
				isValid=false;
				break;
			}
		}
		for(var i=0;i<status.length;i++){
			if(status[i].value=="-1"){
				status[i].style.color="red";
				isValid=false;
				break;
			}
		}
		if(!isValid){
			alert("Please fill correct entries");
		}
		return isValid;
		}	
		
		function  validateNumeric( strValue ) {
 			var objRegExp  =  /(^-?\d\d*\.\d*$)|(^-?\d\d*$)|(^-?\.\d\d*$)/;
			  //check for numeric characters
  			return objRegExp.test(strValue);
		}	
		</script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.update.resp.game.limit" />
				</h3>
				<s:form action="bo_um_reponsibleGamingLimit_Save" method="post"
					onsubmit="return chkValidation();">
					<table border="0" cellpadding="2" cellspacing="0" width="100%">
						<tr>
							<td>
								<s:select cssClass="option" key="label.crtria.type"
									headerValue="--Please Select--" headerKey="-1" id="criType"
									name="criteriaType" list="%{#session.criTypeList}"
									onchange="getRGDetails(this.id)" required="true"></s:select>
							</td>
						</tr>
						<tr>
							<td></td>
						</tr>
						<tr>
							<td></td>
						</tr>
					</table>
					<table border="0" cellpadding="2" cellspacing="0" width="100%">
						<tr>
							<td align="center">
								<s:div id="rgDetails"></s:div>
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_um_responsibleGamingLimit_Menu.jsp,v $ $Revision:
	1.1.2.1.2.1.2.2.2.4 $
</code>