<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
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
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


		<script>
			function validate()
			{
			_id.i("actionError","","e");
			var isEmpty = true;
			if(document.rolePrivForm.roleName.value==""){
			_id.i("roleNameDiv",'<s:text name="error.role.name"/>',"e");
			isEmpty=false;
			}else{_id.non("roleNameDiv");}
			
			if(document.rolePrivForm.roleDesc.value==""){
			_id.i("roleDescDiv",'<s:text name="error.role.desc"/>',"e");
			isEmpty=false;
			}else{_id.non("roleDescDiv");}
			

			var checkArray=document.rolePrivForm.rolePriv;
			var flag=false;
			for(var i=0;i<checkArray.length;i++){
			
			
			if(checkArray[i].checked!=false){
			flag=true;
			break;
			}
			}
			if(flag==false){
			document.getElementById("userDiv1").innerHTML="<font color=\"red\"><s:text name='error.asignprivileges'/></font>";
			}
			return flag&&isEmpty;
			
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
				//alert(interCol + "Intercol hai bhai");
														
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
					//document.getElementById(ser_col).style.border="2px solid #eeeeee";
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
				if(tag[i].type=='checkbox'&& tag[i].checked==true&&tag[i].name=='rolePriv'){
					count++;
				}
			}
			document.getElementById(countId).value=count
			}

		</script>

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />

		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<s:head theme="ajax" debug="false" />
	</head>
	<body>
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
					<s:text name="menu.create.role" />
				</h3>
				<s:form action="bo_rm_createRole_Save.action" method="POST"
					onsubmit="return validate();" name="rolePrivForm">
		<div id="actionError">
							<s:actionerror />
						</div>
				
					<table width="300" cellpadding="2" cellspacing="0" border="0">
					
						<tr>
							<td>
								&nbsp;
							</td>
							<td>
								<s:div id="roleNameDiv"></s:div>
								<s:div id="roleDescDiv"></s:div>
							</td>
						</tr>
						<s:textfield key="label.role.name" name="roleName"></s:textfield>
						<s:textfield key="label.role.descr" maxlength="25" name="roleDesc"></s:textfield>
					</table>
					<br />

					<table width="100%" cellpadding="2" cellspacing="0" border="0">
						<tr>
							<td>
								<div id="userDiv1"></div>
							</td>
						</tr>
						<tr>
							<td>
								<%
									Map<String, TreeMap<String, TreeMap<String, List<String>>>> rolePrivMap = (TreeMap<String, TreeMap<String, TreeMap<String, List<String>>>>) session
													.getAttribute("ROLE_PRIV_MAP");
											Iterator itrMap = rolePrivMap.entrySet().iterator();
											int i = 0;
											while (itrMap.hasNext()) {
												Map.Entry pairs = (Map.Entry) itrMap.next();
												//List privList = (List)pairs.getValue();
												session.setAttribute("Inter_Priv_Map", pairs.getValue());
												i++; 
								%>

								<table bgcolor="#eeeeee" width="100%" cellpadding="0"
									cellspacing="0" border="0">
									<tr id="ser_row<%=i%>" align="center">
										<%
											int colorcode;
														if (i % 2 == 0)
															colorcode = 1;
														else
															colorcode = 2;
										%>

										<th height="25" id="ser_col<%=i%>"
											class="color<%=colorcode%>"
											onclick="showHideTab('inter_tbl<%=i%>')"
											onmouseover="this.style.cursor='hand';">
											<!--<input type="checkbox" id="ser_chk<%=i%>" /> -->
											<%=pairs.getKey().toString()%>
										</th>
										<br />
									</tr>

									<tr>
										<td>
											<table id="inter_tbl<%=i%>"
												style="display: none; background: #eeeeee;" border="0"
												cellpadding="0" cellspacing="0" width="795" class="bdrLRB">
												<tr id="inter_row<%=i%>" align="center">
													<%
														Map<String, TreeMap<String, List<String>>> privMap = (TreeMap<String, TreeMap<String, List<String>>>) session
																			.getAttribute("Inter_Priv_Map");
																	Iterator itrPrivMap = privMap.entrySet().iterator();
																	int j = 0;
																	while (itrPrivMap.hasNext()) {
																		Map.Entry secPairs = (Map.Entry) itrPrivMap.next();
																		j++;
																		serviceArr = secPairs.getKey().toString()
																				.split("-");
													%>

													<th height="30" align="center" width="790"
														id="inter<%=i%>_col<%=j%>"
														onclick="changeTab('inter<%=i%>_col<%=j%>','priv<%=i%>_row<%=j%>',<%=privMap.size()%>)"
														onmouseover="this.style.cursor='hand';">
														<input type="hidden" name="mappingId"
															value="<%=serviceArr[1]%>" />
														<!--  <input type="checkbox" value="<%=serviceArr[1]%>" />-->
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
																	session
																			.setAttribute("privMap", secPairs
																					.getValue());
																	k++;
												%>
												<tr id="priv<%=i%>_row<%=k%>" style="display: none"
													colspan="4">
													<td colspan="<%=j%>">
														<table id="tbl_priv<%=i%>_row<%=k%>" cellpadding="0"
															cellspacing="0" border="0" width="795" bgcolor="#c3d8f2">
															<tr>
																<td>
																	<s:iterator value="#session.privMap" status="rowIndex">
																		<table cellpadding="4" cellspacing="0" border="0"
																			width="100%">
																			<tr>
																				<th style="background: #cadcf1; color: #333333"
																					align="center">
																					<input type="checkbox"
																						id="priv<%=i%>_row<%=k%><s:property  value="%{key}"/>"
																						onclick="selectAll('priv<%=i%>_row<%=k%><s:property value="#rowIndex.index"/>',this.id),setRolePriv('tbl_priv<%=i%>_row<%=k%>','count<%=i%>_col<%=k%>')" />
																					<s:if test="%{key=='PWT'}">
																						<s:text name="menu.pwt" />
																					</s:if>
																					<s:elseif test="%{key=='ACT_MGT'}">
																						<s:text name="menu.acc.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='GAME_MGT'}">
																						<s:text name="menu.game.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='PROD_MGT'}">
																						<s:text name="menu.product.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='DRAW_MGT'}">
																						<s:text name="menu.draw.game.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='TERMINAL_MGT'}">
																						<s:text name="menu.terminal.mgt" />
																					</s:elseif>
																					<s:elseif test="%{key=='INV_MGT'}">
																						<s:text name="menu.inv.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='ORDER_MGT'}">
																						<s:text name="menu.order.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='REPORTS'}">
																						<s:text name="menu.report" />
																					</s:elseif>
																					<s:elseif test="%{key=='USER_MGT'}">
																						<s:text name="menu.user.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='ROLE_MGT'}">
																						<s:text name="menu.role.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='BO_USER_MGT'}">
																						<s:text name="menu.bo.user.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='BO MANAGEMENT'}">
																						<s:text name="menu.bo.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='AGENT_USER_MGT'}">
																						<s:property value="#application.TIER_MAP.AGENT" />
																						<s:text name="menu.user.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='RET_USER_MGT'}">
																						<s:property value="#application.TIER_MAP.RETAILER" />
																						<s:text name="menu.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='MISCELLANEOUS'}">
																						<s:text name="menu.misc" />
																					</s:elseif>
																					<s:elseif test="%{key=='SL MANAGEMENT'}">
																						<s:text name="menu.sle.draw.mgmt" />
																					</s:elseif>
																					<s:elseif test="%{key=='IW_PWT'}">
																						<s:text name="menu.iw.pwt" />
																					</s:elseif>
																					<s:elseif test="%{key=='IW_REPORTS'}">
																						<s:text name="menu.iw.reports" />
																					</s:elseif>
																					<s:elseif test="%{key=='MISC'}">
																						<s:text name="menu.misc" />
																					</s:elseif>
																				</th>
																			</tr>
																			<tr
																				id="priv<%=i%>_row<%=k%><s:property value="#rowIndex.index"/>">
																				<td>
																					<table cellpadding="4" cellspacing="2" border="0"
																						width="100%" bgcolor="#eeeeee">
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
																									<input type="checkbox" value="<s:property />"
																										id="priv<%=i%>_row<%=k%><s:property/>"
																										name="rolePriv"
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
								<s:submit key="menu.create.role" align="center" cssClass="button" />
							</td>
						</tr>
					</table>
				</s:form>
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
$RCSfile: bo_rm_createRole_Menu.jsp,v $
$Revision: 1.18 $
</code>