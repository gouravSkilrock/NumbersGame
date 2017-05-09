<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

	</head>
<script>
function crtResTab(){

var resData = _id.o('resDiv').innerHTML,gmArr = resData.split('Nx*'),erCd = gmArr[gmArr.length-1].split('Msg');
var erCdMap = new Array(),gmNm;
var fT="",_err="";
for(var k=0;k<erCd.length-1;k++){
erCdMap[erCd[k].split('-')[0]]=erCd[k].split('-')[1];
}
for (var i=0,l=gmArr.length-1;i<l;i++){
var tktDet = gmArr[i].split(':');gmNm=tktDet[0],_tT=tktDet.length-2,_vT=0,_iT=0;
if(i%2==0){_bC="#f4fdf9";_cls="light_border_green";_tR="tktResultGr"}else{_bC="#fdf3f3";_cls="light_border_red";_tR="tktResultRd"}
var gmTab = '<table width="100%" border="0" cellspacing="1" cellpadding="3" bgcolor="#FFFFFF" class="'+_cls+'"><tr><th class="'+_tR+'">Ticket Number</th><th class="'+_tR+'">Validity Status</th><th class="'+_tR+'">Message Code</th><th class="'+_tR+'">Remark</th></tr>';
for(var j=1,m=tktDet.length-1;j<m;j++){
_c=tktDet[j].split('*M*')[0];_m=tktDet[j].split('*M*')[1];
if(erCdMap[_m].split(':')[0]=="InValid Ticket"){_iT++;_err="errRow";}else{_vT++;_err="nonErrRow";}
gmTab=gmTab+'<tr bgcolor="'+_bC+'" class="'+_err+'"><td>'+_c+'</td><td>'+erCdMap[_m].split(':')[0]+'</td><td>'+_m+'</td><td>'+erCdMap[_m].split(':')[1]+'</td></tr>';
}
var gmOuTab='<table width="100%" class="'+_cls+'" cellspacing="0" cellpadding="3"><tr><td colspan="4" height="20" bgcolor="'+_bC+'"><b>Game Name: </b>'+gmNm+'</td></tr><tr bgcolor="'+_bC+'"><td><b>Valid Tickets: </b>'+_vT+'</td><td><b>Total Tickets: </b>'+_tT+'</td></tr><tr bgcolor="'+_bC+'"><td><b>Invalid Tickets: </b>'+_iT+'</td><td>&nbsp;</td></tr><tr><td colspan="2">';

gmOuTab=gmOuTab+gmTab+'</table></td></tr></table><br/>';

fT=fT+gmOuTab;
//alert(gmTab);
}
//alert(document.getElementById('crResDiv'));
_id.i('crResDiv',fT);
}
</script>
<body onload="crtResTab()">

<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

<div id="wrap">

  <div id="top_form">
	
	<h3>Ticket Validation Before Winning Receipts</h3>
	
	
	
	<s:form>
			<div id="resDiv" style="display:none"><s:property value="#session.VERIFIED_TICKET_JSSTRING"/></div>
	
		<table width="100%" border="1" align="center" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" id="resTab"><tr><td><div id='crResDiv'></div></td></tr>
		</table>
		

			<table width="100%" border="1" align="center" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
				<tr>
			 		 <td align="left" colspan="4"><s:a theme="simple" href="agt_pwt_tktVerification_Menu.action">Do  Another Winning Ticket Check</s:a></td>
				</tr>
			</table>
		</s:form>
		</div></div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_pwt_tktVerification_Success.jsp,v $
$Revision: 1.2 $
</code>