
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<%!String[] serviceArr = null;%>
	<head>

		<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE" />

		
		<script>
	function validate()
{

var checkArray=document.userPriviledge.groupName;
var flag=false;
for(var i=0;i<checkArray.length;i++){


if(checkArray[i].checked!=false){
flag=true;
break;
}
}
if(flag==false){
document.getElementById("userDiv1").innerHTML="<font color=\"red\">Please Assign Atleast One Privilege</font>";
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

		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<s:head theme="ajax" debug="false" />
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>Assign Priviledges to New Users	</h3>
	<s:form action="agt_um_addSubUser_AssignPriv.action" method="POST" onsubmit="return validate()" name="userPriviledge">
	  
	   `<table width="300" cellpadding="2" cellspacing="0" border="0">
						<tr>
							<td>
								<s:textfield label="User" key="#session.USER_DETAILS.userName"
									readonly="true"></s:textfield>
							</td>
						</tr>
					</table>
					<br />

					<table width="700px" border="0" cellpadding="0" cellspacing="0"
						align="center">
						<tr>
							<td>
								<div id="userDiv1"></div>
							</td>
						</tr>

						<tr>
							<td align="left">
	<table cellpadding="0" border="0" cellspacing="0" width="805">
									<tr>
										<td>
											<%
												Map<String, TreeMap<String, TreeMap<String, List<String>>>> headPriviledgeMap = (TreeMap<String, TreeMap<String, TreeMap<String, List<String>>>>) request
															.getAttribute("headPriviledgeMap");
													Iterator itrMap = headPriviledgeMap.entrySet().iterator();
													int i = 0;
													while (itrMap.hasNext()) {
														Map.Entry pairs = (Map.Entry) itrMap.next();
														//List privList = (List)pairs.getValue();
														session.setAttribute("Inter_Priv_Map", pairs.getValue());
														i++;
											%>
	<table bgcolor="#eeeeee" width="800" cellpadding="0" cellspacing="1" border="0">
												<tr id="ser_row<%=i%>" align="center">
	<% int colorcode;
						if(i%2 ==0)
						   colorcode =1;
						   else
	colorcode = 2; %>
	<th height="25" class="color<%=colorcode %>" id="ser_col<%=i%>" style="width:100%" onclick="showHideTab('inter_tbl<%=i%>')"
														onmouseover="this.style.cursor='hand';">
														<!--<input type="checkbox" id="ser_chk<%=i%>" />-->
														<%=pairs.getKey().toString()%>
	</th><br />
												</tr>
												<tr>
													<td>
	<table id="inter_tbl<%=i%>" style="display:none;background: #eeeeee" border="0" cellpadding="0" cellspacing="0" width="785" class="bdrLRB">
															<tr id="inter_row<%=i%>" align="center">
																<%
																	Map<String, TreeMap<String, List<String>>> privMap = (TreeMap<String, TreeMap<String, List<String>>>) session
																					.getAttribute("Inter_Priv_Map");
																			Iterator itrPrivMap = privMap.entrySet().iterator();
																			int j = 0;
																			while (itrPrivMap.hasNext()) {
																				Map.Entry secPairs = (Map.Entry) itrPrivMap.next();
																				j++;
																				serviceArr = secPairs.getKey().toString().split("-");
																%>

	<th  id="inter<%=i%>_col<%=j%>" height="30" align="center" width="790"	onclick="changeTab('inter<%=i%>_col<%=j%>','priv<%=i%>_row<%=j%>',<%=privMap.size()%>)"
																	onmouseover="this.style.cursor='hand';">
																	<input type="hidden" name="mappingId"
																		value="<%=serviceArr[1]%>" />
																	<!--<input type="checkbox" value="<%=serviceArr[1]%>" />-->
																	<%=serviceArr[0]%>
																	<input type="hidden" name="privCount"
																		id="count<%=i%>_col<%=j%>" value="0" />

																</th>


																<%
																	}
																%>
															</tr>
															<%
																itrPrivMap = privMap.entrySet().iterator();
																		int k = 0;
																		while (itrPrivMap.hasNext()) {
																			Map.Entry secPairs = (Map.Entry) itrPrivMap.next();
																			session.setAttribute("privMap", secPairs.getValue());
																			k++;
															%>
															<tr id="priv<%=i%>_row<%=k%>" style="display: none">
																<td colspan="<%=j%>">
		<table id="tbl_priv<%=i%>_row<%=k%>" cellpadding="0" cellspacing="0" border="0" width="100%" bgcolor="#c3d8f2">
																		<tr>
																			<td>
																				<s:iterator value="#session.privMap"
																					status="rowIndex">
	<table cellpadding="4" cellspacing="0" border="0" width="790">
																						<tr>
																							<th style="background: #cadcf1; color: #333333">
																								<input type="checkbox"
																									id="priv<%=i%>_row<%=k%><s:property  value="%{key}"/>"
																									onclick="selectAll('priv<%=i%>_row<%=k%><s:property value="#rowIndex.index"/>',this.id),setRolePriv('tbl_priv<%=i%>_row<%=k%>','count<%=i%>_col<%=k%>')" />
																								<s:if test="%{key=='PWT'}">PWT</s:if>
																								<s:elseif test="%{key=='ACT_MGT'}">Account Management</s:elseif>
																								<s:elseif test="%{key=='GAME_MGT'}">Game Management</s:elseif>
																								<s:elseif test="%{key=='INV_MGT'}">Inventory Management</s:elseif>
																								<s:elseif test="%{key=='ORDER_MGT'}">Order Management</s:elseif>
																								<s:elseif test="%{key=='REPORTS'}">Reports</s:elseif>
																								<s:elseif test="%{key=='USER_MGT'}">User Management</s:elseif>
																								<s:elseif test="%{key=='ROLE_MGT'}">Role Management</s:elseif>
																								<s:elseif test="%{key=='BO_USER_MGT'}">BO User Management</s:elseif>
																								<s:elseif test="%{key=='AGENT_USER_MGT'}"><s:property value="#application.TIER_MAP.AGENT" /> User Management</s:elseif>
																								<s:elseif test="%{key=='RET_USER_MGT'}"><s:property value="#application.TIER_MAP.RETAILER" /> Management</s:elseif>
																								<s:elseif test="%{key=='MISCELLANEOUS'}">
																						<s:text name="menu.misc" />
																					</s:elseif>
																					<s:elseif test="%{key=='SL MANAGEMENT'}">
																						<s:text name="menu.sle.draw.mgmt" />
																					</s:elseif>
																								<s:elseif test="%{key=='MISC'}">Miscellaneous</s:elseif>
																							</th>
																						</tr>
																						<tr
																							id="priv<%=i%>_row<%=k%><s:property value="#rowIndex.index"/>">
																							<td>
		<table cellpadding="4" cellspacing="2" border="0" width="100%" bgcolor="#eeeeee">
																									<%!int x = 0, y = 0;%>
																									<%
																										x = -1;
																														y = -3;
																									%>
																									<s:iterator value="%{value}">
																										<%
																											x += 1;
																																y += 1;
																																if (x % 3 == 0) {
																										%>
																										<tr>
																											<%
																												}
																											%>
																											<td width="33%" bgcolor="#e9eff6">
																												<input type="checkbox"
																													value="<s:property />"
																													id="priv<%=i%>_row<%=k%><s:property/>"
																													name="groupName"
																													onclick="setRolePriv('tbl_priv<%=i%>_row<%=k%>','count<%=i%>_col<%=k%>')" />
																												<s:property />
																											</td>
																											<%
																												if (y % 3 == 0) {
																											%>
																										</tr>

																										<%
																											}
																										%>

																									</s:iterator>
																								</table>
																							</td>
																						</tr>
																					</table>
																				</s:iterator>
																			</td>
																		</tr>
																	</table>
																</td>
															</tr>
															<%
																}
																		session.removeAttribute("privMap");
															%>
														</table>
													</td>
												</tr>
											</table>
											<%
												}
													session.removeAttribute("Inter_Priv_Map");
											%>
										</td>
									</tr>
									<tr>
										<td>
											<s:submit value="Assign" align="center" cssClass="button" />
										</td>
									</tr>
								</table>





							</td>
						</tr>


					</table>
				</s:form>
			</div>
		</div>
	</body>






	<%--<table border="1" bordercolor="#CCCCCC" cellpadding="0" cellspacing="0">
<tr><td colspan="2">
<s:textfield label="User" key="#session.USER_DETAILS.userName" readonly="true"></s:textfield>
</td></tr>
<tr><td colspan="2"><div id="userDiv1"></div></td></tr>
<tr><td colspan="2" >
<table border="0">

<%!int count=-1; %>
<tr>
<s:iterator id="priv"  value="privListforHead">

<%count++;
if(count==3)
{%>
<tr>
<%
}
%>
<td>
<input type="checkbox"  value="<s:property value="pid"/>"  name="rolePriv" id=<%=count %>>
</td>
<td>
 <s:property value="privTitle"/>
 </td>
 <%
 if(count==3)
{
count=-1;
%>
</tr>
 <%
 }

 %>
</s:iterator>
</tr>
</table>
<tr><td>
<s:submit value="OK" align="center" cssClass="button"/>
</td></tr>

 </table>




</s:form>
</div></div>
</body>

--%>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: agt_um_addSubUser_AssignPriv.jsp,v $
$Revision: 1.10 $
</code>