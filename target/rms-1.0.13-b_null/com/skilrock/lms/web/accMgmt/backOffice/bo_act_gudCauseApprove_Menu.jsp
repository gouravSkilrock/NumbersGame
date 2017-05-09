<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<script type="text/javascript">
	$( document ).ready(function() {
$("#trType").hide();
});
			function checkboxValidate(){
				var flag = 0;
				var checkboxArr = document.getElementsByName('taskIds');
				for(i=0; i<checkboxArr.length; i++){
					if(checkboxArr[i].checked){
						flag = 1;
						break;
					}
				}
				if(flag == 0){
					document.getElementById('err').style.display = 'block';
					return false;
				}
				else{
					return true;
				}
			}
			
			function hideErr(){
				document.getElementById('err').style.display = 'none';
				return true;
			}
	function validateFormEntries() {
	var serviceNameObj =_id.o('serviceName');
	var typeObj =_id.o('type');
	//alert(serviceNameObj.value);
	 if(serviceNameObj.value=='-1'){
			_id.i("error1", "Please Select Service Name .", "e");
			document.getElementById('down').innerHTML="";
 	  		document.getElementById('down1').innerHTML="";	
			return false;
	}
	if(serviceNameObj.value != 'DG' && serviceNameObj.value != 'SE'){
		_id.i("error1", "Invalid Service .", "e");
			document.getElementById('down').innerHTML="";
 	  		document.getElementById('down1').innerHTML="";	
			return false;
	} if(typeObj.value=='-1'){
			_id.i("error1", "Please Select Type.", "e");
			document.getElementById('down').innerHTML="";
 	  		document.getElementById('down1').innerHTML="";	
			return false;
	}
	_id.i("error1", "");
	return true;
}	</script>
<script>
$( document ).ready(function() {
$("#trType").hide();
});

function handleTrType(){
var service = $("#serviceName").val();

if(service == 'DG' || service == 'SLE')
	$("#trType").show() ;
else
	$("#trType").hide() ;
}
</script>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
     <div id="top_form">
  <h3>Get <s:text name="Good_Cause"/> Amount to Approve</h3>
	
	
	
  <s:form action="bo_act_gudCauseApprove_Search" onsubmit="return validateFormEntries();"> 		
	 <table  border="0" cellpadding="2" cellspacing="0" width="500">
	 	<s:set name="stDate" value="#session.GOV_CUR_DATE" />
	 	<s:set name="endDate" value="#session.GOV_END_DATE" />	 

		<tr><td align="center" colspan="2"><div id="dates"></div></td></tr>
		<tr>
			<td  align="left">
			<table>
				<tr>
					<td align="center" colspan="2">
						<div id="error1"></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:select name="serviceName" id="serviceName" headerKey="-1"
							headerValue="--Please Select--" label="Select Service"
							list="#session.serviceNameMap" cssClass="option" required="true"  onchange="handleTrType()"/>
					</td>
				</tr>
				<tr id="trType" style="display:none;">
				<td align="right">Select Type<span class="required">*</span>:</td>
					<td>
						<select class="option" name="type" id="type" >
							<option value="-1" >Please Select</option>
							<option value="SALE" >Sale</option>
							<option value="PWT" >Winning</option>
						</select>
					</td>
				</tr>
				</table>
			</td>
		</tr>
		<tr> 			
			<td colspan="1" align="left" style="padding-left:32px;">
				<label class="label">End Date<span class="required">*</span>:</label>
    			<input  type="text" name="endDate" id="end_date" value="<s:property value="#session.GOV_END_DATE"/>" readonly size="12" />
    			<input type="button" style=" width:19px; height: 19px; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border:0 ; " onclick="displayCalendar(document.getElementById('end_date'),'yyyy-mm-dd', this, '<s:property value="#session.GOV_CUR_DATE"/>',false, '<s:property value="#session.GOV_END_DATE"/>')" />
   			</td>
			<td></td>			
		</tr>			
		<tr>
			<td colspan="1"><s:submit name="search" value="Search" align="center"  targets="down" theme="ajax" cssClass="button" /></td>
			<td></td>	
		</tr>
		
	 </table>
	
	</s:form> 
	
 	 <div id="down" style="text-align: center" ></div>
 	  <div id="down1" style="text-align: center" ></div>
   </div></div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_act_gudCauseApprove_Menu.jsp,v $
$Revision: 1.3 $
</code>