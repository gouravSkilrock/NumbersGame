<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		<script>
			var data="";
			var orgTypeMap=new Object();
			function fillDetail(){
				data=(document.getElementById('userData').value).replace("{","").replace("}","").replace(/ /g,"");
				var dataArr=data.split("],");
				var orgType=_id.o('orgType');
				orgType.options.length=1;
				for(var i=0;i<dataArr.length;i++){
					var dataValue=dataArr[i].split("=");
					orgTypeMap[dataValue[0]]=dataValue[1].replace("[","").replace("]","");
					orgType.options[i+1]=new Option(dataValue[0],dataValue[0]);
				}
			}
			function getUser(orgType){
				var userNameList=orgTypeMap[orgType].split(",");
				var userName=_id.o('userName');
				userName.options.length=1;
				for(var i=0;i<userNameList.length;i++){
					userName.options[i+1]=new Option(userNameList[i],userNameList[i]);
				}
			}
			var req;
  function getUserPriveledges()
{
  var userName=document.getElementById('userName').value;
  var url='admin_userPrivFetch.action?userName='+userName;
    // alert("url is" +url);
    document.getElementById('lodingImage').style.display="block";
   _resp  = _ajaxCall(url);
	if(_resp.result){
        document.getElementById('lodingImage').style.display="none";
        _id.i('result',_resp.data);
     
      } else {
        alert("Problem: ");
        }
 
return false;

}
 
function validate()
{

if(document.editUserPriv.userName.value=="-1"){

document.getElementById("userDiv").innerHTML="<font color=\"red\">Please Select User Name</font>";
document.getElementById("userDiv").colSpan="2";
return false;
}
else
{document.getElementById("userDiv").innerHTML="";
}
var checkArray=document.editUserPriv.groupName;

var flag=false;
for(var i=0;i<checkArray.length;i++){


if(checkArray[i].checked!=false){
flag=true;
break;
}
}
if(flag==false){
document.getElementById("userDiv1").innerHTML="<font color=\"red\">Please Assign Atleast One Privilege or Edit User Status</font>";
}
return flag;

}

function selectAll(rowId,checkBoxId)
{
var checkAll;
if(document.getElementById(checkBoxId).checked==true)
  checkAll=true;
  else
  checkAll=false;

var rowCheckbox =document.getElementById(rowId).getElementsByTagName("input");
var length=rowCheckbox.length;
for(var i=0;i<length;i++)
{
  if(rowCheckbox[i].type=='checkbox')
      rowCheckbox[i].checked=checkAll;
}


}
	function changeTab(interCol,privRow,noCol)
	{
		//alert("vaibhav");
		var i,interColName,privRowName;
		interColName='';
		privRowName='';
		for(i=0;i<interCol.length-1;i++)
			interColName=interColName+interCol.charAt(i);
		for(i=0;i<privRow.length-1;i++)
			privRowName=privRowName+privRow.charAt(i);
		//alert(privRowName);
		
		var check;
		check=true;
		if(document.getElementById(privRow).style.display=='block')
		{
			check=false;
		}
		for(i=1;i<=noCol;i++)
		{
			document.getElementById(interColName+i).className="unselectedTab";
			document.getElementById(privRowName+i).style.display='none';
		}
		if(check==true)
		{
			document.getElementById(interCol).className="selectedTab";
			document.getElementById(privRow).style.display='block';
		}
	}
	function showHideTab(interRow)
	{
		//alert("vaibhav");
		if(document.getElementById(interRow).style.display=='none')
		{
			document.getElementById(interRow).style.display='block';
		}
		else
		{
			document.getElementById(interRow).style.display='none';
		}
	}
	
	
	// added by pavan/vaibhav
	
	function setRolePriv(tableId,countId){
		/*if(document.getElementById(id).checked==true)
			document.getElementById(countId).value=parseInt(document.getElementById(countId).value)+1;
		else{
		if(document.getElementById(countId).value!=0)
			document.getElementById(countId).value=parseInt(document.getElementById(countId).value)-1;
			
			}*/
	var tag=document.getElementById(tableId).getElementsByTagName("input");
	var count=0;
	for(var i=0;i<tag.length;i++){
		if(tag[i].type=='checkbox'&& tag[i].checked==true&&tag[i].name=='groupName'){
			count++;
		}
	}
	document.getElementById(countId).value=count
	}
		</script>
	</head>
	<body onload="fillDetail()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Edit Privilege
				</h3>
				<s:form name="privUser" action="admin_userPrivSave.action"
					method="POST" onsubmit="return validate()">
					<table border="0" cellpadding="2" cellspacing="0">
						<s:hidden name="userData" id="userData" value="%{userData}"></s:hidden>
						<s:select cssClass="option" id="orgType" name="orgType"
							label="Select Orgnization Type" list="{}" headerKey="-1"
							headerValue="--Please Select--" onchange="getUser(this.value)"></s:select>
							<div id="userDiv"></div>
						<s:select cssClass="option" id="userName" name="userName"
							label="Select User Name" list="{}" headerKey="-1"
							headerValue="--Please Select--" onchange="getUserPriveledges()" required="true"></s:select>
						<tr>
							<td id="lodingImage" style="display: none; color: red;">
								Wait...
								<img
									src="<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif" />
							</td>
						</tr>
					</table>
					<br />

					<table width="700" cellpadding="2" cellspacing="0" border="0"
						align="center">
						<tr>
							<td>
								<div id="userDiv1"></div>
							</td>
						</tr>
						<tr>
							<td>
								<div id="result"></div>
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: admin_userPrivMenu.jsp,v $ $Revision: 1.2 $
</code>