<%@ page import="java.util.*"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 

"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>

	<%
		List commonList = (List) session.getAttribute("interfaceList");
		System.out.println("vaibhav "+ commonList);
	%>

	<head>
		<s:head theme="ajax" debug="false" />
		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

		<script>

function displayServiceData(id, act_inact, bgcol){
	if(act_inact=='INACTIVE') return false;
	
	_id.o("result").style.display='none'; 
	_id.o("tier"+id).selectedIndex= '0';                 // reset index level of tierlist
	_id.o("parentTier"+id).selectedIndex= '0';  
	
	var orgId = ("org"+id);
	  _id.o(orgId).style.display = 'none';
	 var interfaceId = ("interface"+id);
	  _id.o(interfaceId).style.display = 'none';
	
	var tierValId =("tierValTr"+id);
	_id.o(tierValId).style.display='none';  
	 
	 var radioName = ("radio"+id); 
	 	  for(var i=0;i<_id.n(radioName).length;i++){
		               var e=_id.n(radioName)[i]; 
		               if(e.value == "All"){
		                   e.checked = true;
		                }
		           }
	var parentorgId =  ("parentTier"+id);
	 _id.o(parentorgId).style.display='none';  
	 
	var noorgId = ("noorg"+id);
    _id.o(noorgId).style.display = 'none'; 
  	 
	
	 
	if(_id.o("div"+id).style.display=='none'){             
	    _id.o("div"+id).style.display='block';
		_id.o("tab"+id).style.backgroundColor=bgcol ;
	}
	
	else{
		_id.o("div"+id).style.display='none';
	}

}


function setOrgName(obj,val){



if(obj.checked){
	_id.o(obj.id+"div").innerHTML='<input type="hidden" name="orgName" value="'+val+'"/>'; //change
	_id.o(obj.id+"div").style.display='none';
}
else{
	_id.o(obj.id+"div").innerHTML='';
}

}




function check(id){

var radio=_id.o(id).getElementsByTagName('input');
var tier=_id.o(id).getElementsByTagName('select');
var str,ch,countCheck,countRadio,checkChBox;
checkChBox=false;
ch=true;
countCheck=0;
countRadio=0;
//alert(tier.length);
for(i=0;i<tier.length;i++)
{
	str=tier[i].id;
	//alert(str.substring(0, 4));
	var str1=str.substring(0, 4);
	if(str1=="tier"){
		//alert(_id.o(str).value);
			if(_id.o(str).value == '-1'){
				  alert("Please Select Tier");
				  return false;
			}
			if(_id.o(str).value =="All"   ){
	    			return true;
	    	}
		
	}
}


for(i=0;i<radio.length;i++)
{
	if(radio[i].type=='radio'){
	     
	
		if( radio[i].checked ==true){
			countRadio++;
	        }
	
	}

	if(radio[i].value=='Specific' && radio[i].checked ==true)
	{   
		checkChBox=true;
	}	
}

if(countRadio==0){
    
	alert("Please select the Required Field");
	return false;
	}
	
if(checkChBox)
{	
	for(i=0;i<tier.length;i++)
{//   alert(1);
	str=tier[i].id;
	//alert(str.substring(0, 4));
	var str1=str.substring(0, 10);
     //alert(str1);
	if(str1=="parentTier"){
		//alert(_id.o(str).value);
			if(_id.o(str).value == '-1'){
				  alert("Please Select Parent Organization");
				  return false;
			}
			
		
	}
}
	
	
	for(i=0;i<radio.length;i++)
	{
		if(radio[i].type=='checkbox'){
			if(radio[i].checked==true )
				countCheck++;
		}	
	}
	if(countCheck==0)  //org 
	{
	alert("Please Select Organization");
	return false;
	}
}
return true;

}


var resArr; // contain string of all organization and org under them

function fetchParentOrgList(){
    var url = "fetchOrgListForService.action";
    var resData = _ajaxCall(url);
   // alert(resData.data);
    
    resArr = (resData.data).split("=");
 
}


function getParentType(id){
 
		var parentId=id.replace("tier","parentTier");
		var radioID = id.replace("tier","tierValTr");
        var orgID =id.replace("tier","org");
        var noorgmessage = id.replace("tier","noorg");
		
		_id.o(orgID).style.display = 'none';
		_id.o(parentId).style.display = 'none';
		_id.o(noorgmessage).style.display = 'none';
	    var radioName = id.replace("tier","radio");
		 
		
		var servId=id.replace("tier","service"); 
		//alert(_id.o(servId).value);
		var url="<%=request.getContextPath()%>/com/skilrock/lms/web/roleMgmt/getInterface.action?tierLevel="+_id.o(id).value+"&serviceName="+_id.o(servId).value;
		_resp  = _ajaxCall(url);
		//alert(_resp.data);
		var interFaceId=id.replace("tier","interface"); 
		_id.i(interFaceId,_resp.data);
		if(_id.o(id).value!='-1'){
		    _id.o(interFaceId).style.display = 'block';
		   
			   if(_id.o(id).value == 'All'){ 
				    for(var j=0;j<_id.o(interFaceId).getElementsByTagName("div").length;j++){
						var e=_id.o(interFaceId).getElementsByTagName("div")[j];
							//if(e.id.match("forAll")){
								e.style.display='block';
							//}
			        }
			   }     	
			  else{
				   for(var j=0;j<_id.o(interFaceId).getElementsByTagName("div").length;j++){
							var e=_id.o(interFaceId).getElementsByTagName("div")[j];
								//if(e.id.match("forTier")){
									e.style.display='block';
								//}
							
				  }       
			  
			  }
				 
		}
		else{
		 _id.o(interFaceId).style.display = 'none';
		}
		
		
		var parent_typeList = resArr[0].split(":");
		  
		   var parent_Type;     // contains parenttype of selected tier 
		   var flag = true;
		   for(var i=0; i<parent_typeList.length ;i++){
		        
		         if(!flag){
		             break;											// this loop gets us what is parent_Type of selected Tier Level
		          }   
		         var tiercomp = parent_typeList[i].split("-");
		         var tiertype = tiercomp[0];
		         var tierparent = tiercomp[1];
		         if (tiertype == _id.o(id).value){
		             parent_Type=tierparent;
		             flag =false;
		         
		         }
		            
		   }
		  
		   
		 var parent_tier_list = new Array(); // contains arent_org list of selected tier 
		 var commonOrgList = resArr[1].split(":");
		 var k=0; 
		  for(var i=0;i<commonOrgList.length;i++){
		      
		        var parentcomp = commonOrgList[i].split("-");
		        var orgid =  parentcomp[0];
		        var parenttype=parentcomp[1];
		        var orgname= parentcomp[2]; 
		        if (parenttype == parent_Type ){
		              parent_tier_list[k]= orgname;        
		             k++;
		        } 
		  }
		
		var selectBoxID = id.replace("tier","parentTierSelect");
		
		var Selectbox = document.getElementById(selectBoxID);
		
		  Selectbox.options.length=1;
		  Selectbox.options[0] = new Option( "Please Select", "-1"); 
			for(var i=1;i<=parent_tier_list.length;i++){
				 Selectbox.options[i] = new Option(parent_tier_list[i-1],parent_tier_list[i-1]);
			}
		
		
		
		 if(_id.o(id).value != "All" && _id.o(id).value != "-1"){ 
		       _id.o(radioID).style.display = 'block';
		       
		           for(var i=0;i<_id.n(radioName).length;i++){
		               var e=_id.n(radioName)[i]; 
		               if(e.value == "All"){
		                   e.checked = true;
		                }
		           }
		 }
		 else{
		   _id.o(radioID).style.display = 'none';		
		      
		 }
		    
}


function fetchparentOrg(name,hidden){
   
  
   var parentId=name.replace("radio","parentTier");  
   var orgID =name.replace("radio","org");
   var interfaceId = name.replace("radio","interface");
   var orgerrormessage = name.replace("radio","noorg");
   _id.o(orgerrormessage).style.display = 'none';
   _id.o(name.replace("radio","parentTierSelect")).selectedIndex= '0';  
   
   if(_id.o(name.replace("radio","parentTierSelect")).value == "-1"){
      _id.o(orgID).style.display ='none';
}
   for(var i=0;i<_id.n(name).length;i++){
		var e=_id.n(name)[i]; 
		if(e.value=="Specific"&&e.checked==true){
		   _id.o(parentId).style.display = 'block';
		   _id.o(interfaceId).style.display = 'none';
		   _id.o(hidden).value=e.value;
		}
		else if(e.value=="All"&&e.checked==true){
		 _id.o(parentId).style.display = 'none';
		   _id.o(orgID).style.display = 'none';//alert(orgID+"div");
		  _id.o(hidden).value=e.value;
		  _id.o(interfaceId).style.display = 'block';
		  
		}
		
   }
}


function fetchOrgUnder(id,bgcolor) {

var interfaceId = id.replace("parentTierSelect","interface");
_id.o(interfaceId).style.display = 'none';

var noorgerrorId = id.replace("parentTierSelect","noorg");
_id.o(noorgerrorId).style.display = 'none';
var parent_org = _id.o(id).value;
var parent_orgUnderlist = new Array();  // contains organizations under parent organization
var parent_orgID;
var idforOrg = id.replace("parentTierSelect","");
		//alert(idforOrg);
var parent_orglist = resArr[1].split(":");


for(var i= 0; i<parent_orglist.length;i++){

   var parentOrgComp = parent_orglist[i].split("-");
   var orgname = parentOrgComp[2];
      
     if(orgname == parent_org ){
        parent_orgID = parentOrgComp[0];  
     }
}


var k=0;
var orgdisplayID = id.replace("parentTierSelect","org");
//alert(orgdisplayID);
if(_id.o(orgdisplayID).style.display = 'none')
    _id.o(orgdisplayID).style.display = 'block';

var tbl = "<div id ='org1' style= 'display: block'><table border='1' cellpadding='2' cellspacing='0' align='center' bordercolor='#CCCCCC' bgcolor='"+ bgcolor +"' width='100%'>";
	
	
	var colNumber=0;
	for(var i =0 ; i<parent_orglist.length;i++){  
	    var parentOrgComp = parent_orglist[i].split("-");
	    var orgparent_id = parentOrgComp[3];
	    if(orgparent_id == parent_orgID){
	        parent_orgUnderlist[k]=  parentOrgComp[2];
	        if(colNumber==0){
	        	tbl=tbl+"<tr>";	 
	        }   
	        tbl=tbl+'<td align="left" width="10PX"><div style="display:none" id = "'+idforOrg+'orgName'+parent_orgUnderlist[k]+'div" ></div> <input type="checkbox" id = "'+idforOrg+'orgName'+parent_orgUnderlist[k]+'" name ="orgNameChk" onclick="setOrgName(this, \''+ parent_orgUnderlist[k]+'\')" value="'+parent_orgUnderlist[k]+'" ></td>' ;
           	tbl=tbl+'<td align="left" width="30%">'+parent_orgUnderlist[k]+'</td>';
	       	colNumber+=1;      
	       	if(colNumber==3){
	       		tbl=tbl+'</tr>';
	       		colNumber=0;
	       	}         
	        k++;
	        
	    }
	}
	if(colNumber<3 && colNumber!=0){
   		tbl=tbl+'</tr>';
	}  
	tbl=tbl +'</div></table>';
	//alert(tbl);
_id.i(orgdisplayID, tbl);


if(k>0){

_id.o(interfaceId).style.display = 'block';
}
else{

_id.o(noorgerrorId).style.display = 'block';
}




}




</script>





	</head>
	<body onload="fetchParentOrgList()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<%
			UserInfoBean user = null;
			user = (UserInfoBean) session.getAttribute("USER_INFO");
			System.out.println("Name:--" + user.getUserName() + " Head "
					+ user.getIsRoleHeadUser() + " Role  "
					+ user.getIsMasterRole());
			String isRoleHeadUser = user.getIsRoleHeadUser();
			String isMasterRole = user.getIsMasterRole();
			if (isRoleHeadUser.equalsIgnoreCase("Y")
					& isMasterRole.equalsIgnoreCase("Y")) {
		%>



		<div id="wrap">
			<div id="top_form">
				<h3>
					Create Service
				</h3>


				<s:actionerror />
				<s:iterator value="%{serviceDataMap}" status="serviceIndex1">
					<s:set name="optionKey" value="%{key}"  />
					<s:set name="serviceIndex" value="#serviceIndex1.index"  />
					<s:form id="form%{optionKey}" action="ProcessServiceAction.action"
						onsubmit="return check('form%{optionKey}')" method="post">

	<table width="100%" cellpadding="0" cellspacing="0" border="0"	bordercolor="#cccccc" class="tabBdr">
							<tr>

								<s:set name="val" value="%{value}"></s:set>
								
								<input type="hidden" value="<s:property  value="%{key}" />"
										name="serviceName" id="service<s:property  value="%{key}"/>" />
										&nbsp;&nbsp;
										
										
													
								<s:if  test="%{#serviceIndex %2 ==0}">
								
								<td id="<s:property  value="%{key}" />" bgcolor="#d1e3f3"
										width="15%" align="left"
										onclick="displayServiceData(this.id,'<%=request.getAttribute("val").toString()%>','#dce7f1')"
										onmouseover="this.style.cursor='hand';"><s:property value="%{key}" />
								
								</td>
								</s:if>
								<s:else>
								<td id="<s:property  value="%{key}" />" bgcolor="#f0ead4"
										width="15%" align="left"
										onclick="displayServiceData(this.id,'<%=request.getAttribute("val").toString()%>','#efebdd')"
										onmouseover="this.style.cursor='hand';"><s:property value="%{key}" />
									
								</td>
							
								</s:else>	
										
								
								<td width="70%" align="center">
									<s:property value="%{value}" />
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="4">
									<div style="display: none" id="div<s:property  value="%{key}"/>">
	<table id="tab<s:property  value="%{key}"/>" border="0" width="100%" cellpadding="0" align="center" cellspacing="0" bgcolor="#e1eaf5" class="tabBdr">
											
	<tr>
	<td width="10%">Tiers</td>
	<td valign="top" width="90%"><s:select list="%{tierList}" name="tierLevel" id="tier%{optionKey}" theme="simple" cssClass="option"
														headerKey="-1" headerValue="--Please Select--"
														onchange="getParentType(this.id)"></s:select>
												</td>
	</tr>
										
	<tr id="tierValTr<s:property  value="%{key}"/>"	style="display: none">
	<td bgcolor="#eeeeee"><input type="hidden" name="tierValue"	id="tierValHidden<s:property value="%{key}" />" value = "All" /><input type="radio" value="All" id="tierVal1<s:property value="%{key}" />" name="radio<s:property value="%{key}" />"	onclick="fetchparentOrg(this.name,'tierValHidden<s:property value="%{key}" />')" />All</td>
	<td bgcolor="#eeeeee"><input type="radio" value="Specific"	id="tierVal<s:property value="%{key}" />" name="radio<s:property value="%{key}" />"	onclick="fetchparentOrg(this.name,'tierValHidden<s:property value="%{key}" />')" />Specific</td>
	</tr>

	<s:if  test="%{#serviceIndex %2 ==0}">								
	<tr id="parentTier<s:property  value="%{key}"/>" style =" display: none">
	<td width="10%">Parent Org</td>
	<td><select id="parentTierSelect<s:property  value="%{key}"/>" name="parentTierSeLGroup" class="option" onchange="fetchOrgUnder(this.id,'#e7ebef')" /></td>
	</tr>	
	</s:if>	
			
	<s:else>
	<tr id="parentTier<s:property  value="%{key}"/>" style =" display: none">
	<td width="10%">Parent Org</td>
	<td><select id="parentTierSelect<s:property  value="%{key}"/>" name="parentTierSeLGroup" class="option" onchange="fetchOrgUnder(this.id,'#f7f4ea')" /></td>
	</tr>
	</s:else>									
										
	<tr>
	<td colspan="2" align="center">
	<div id="org<s:property  value="%{key}"/>"></div>
	</td>
	</tr>
	
	
	<tr>  <td><div style = "display:none;width: 200px;color: red" id ="noorg<s:property value="%{key}" />"> No organization to proceed</div> </td>
	</tr>										

    <tr>
	<td valign="bottom" colspan="2" align="center"><div style="display: none" id="interface<s:property value="%{key}" />"></div></td>
	
	</tr>
	<tr>
	<td valign="bottom" align="left" colspan="2"><s:submit align="left" cssClass="button" formId="form%{optionKey}" theme="ajax" targets="result" />
												</td>


											</tr>
										</table>
									</div>
								</td>

							</tr>
						</table>
						<br />
					</s:form>
				</s:iterator>

				<br />

				<table width="700px" border="0" cellpadding="0" cellspacing="0"
					align="center">
					<tr>
						<td>
							<div id="result"></div>
						</td>
					</tr>




				</table>

			</div>
		</div>


		<%
			} else {
		%>
		<div>
			<jsp:include page="/com/skilrock/lms/web/loginMgmt/unauthorize.jsp"></jsp:include>
		</div>
		<%
			}
		%>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: bo_rm_createService_menu.jsp,v $
$Revision: 1.23 $
</code>