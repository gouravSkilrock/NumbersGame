<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.*"%>
<%!String[] serviceArr = null;%>
<table bgcolor="#eeeeee" width="100%" cellpadding="0" cellspacing="1"
	border="0">
	<tr>
		<td>
			<%
				Map<String, TreeMap<String, TreeMap<String, List<String>>>> headPriviledgeMap = (TreeMap<String, TreeMap<String, TreeMap<String, List<String>>>>) request
						.getAttribute("userPriviledgeMap");
				Iterator itrMap = headPriviledgeMap.entrySet().iterator();
				int i = 0;
				while (itrMap.hasNext()) {
					Map.Entry pairs = (Map.Entry) itrMap.next();
					//List privList = (List)pairs.getValue();
					session.setAttribute("Inter_Priv_Map", pairs.getValue());
					i++;
			%>
			<table bgcolor="#eeeeee" width="100%" cellpadding="0" cellspacing="0"
				border="0">
				<tr id="ser_row<%=i%>" align="center">
					<% int colorcode;
						if(i%2 ==0)
						   colorcode =1;
						   else
	colorcode = 2; %>
					<th height="25" class="color<%=colorcode %>" id="ser_col<%=i%>"
						onclick="showHideTab('inter_tbl<%=i%>')"
						onmouseover="this.style.cursor='hand';">
						<!--<input type="checkbox" id="ser_chk<%=i%>" />-->
						<%=pairs.getKey().toString()%>
					</th>
					<br />
				</tr>
				<tr>
					<td>
						<table id="inter_tbl<%=i%>"
							style="display: none; background: #eeeeee" cellpadding="0"
							cellspacing="0" width="795" class="bdrLRB">
							<tr id="inter_row<%=i%>">
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

								<th height="30" align="center" width="790"
									id="inter<%=i%>_col<%=j%>"
									onclick="changeTab('inter<%=i%>_col<%=j%>','priv<%=i%>_row<%=j%>',<%=privMap.size()%>)"
									onmouseover="this.style.cursor='hand';">
									<input type="hidden" name="mappingId"
										value="<%=serviceArr[1]%>" />
									<!--<input type="checkbox" value="<%=serviceArr[1]%>" />-->
									<%=serviceArr[0]%>
								</th>


								<%
									}
								%>
							</tr>
							<%
								itrPrivMap = privMap.entrySet().iterator();
									int k = 0;
									int privCount=0;
									while (itrPrivMap.hasNext()) {
										Map.Entry secPairs = (Map.Entry) itrPrivMap.next();
										session.setAttribute("privMap", secPairs.getValue());
										k++;
							%>
							<tr id="priv<%=i%>_row<%=k%>" style="display: none">
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
																<s:elseif test="%{key=='DRAW_MGT'}">
																	<s:text name="menu.draw.mgmt" />
																</s:elseif>
																<s:elseif test="%{key=='GAME_MGT'}">
																	<s:text name="menu.game.mgmt" />
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
																<s:elseif test="%{key=='AGENT_USER_MGT'}">
																	<s:property value="#application.TIER_MAP.AGENT" />
																	<s:text name="menu.user.mgmt" />
																</s:elseif>
																<s:elseif test="%{key=='RET_USER_MGT'}">
																	<s:property value="#application.TIER_MAP.RETAILER" />
																	<s:text name="menu.mgmt" />
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
																				<s:if test="status=='ACTIVE'">
																					<%privCount++; %>
																					<input type="checkbox" checked="checked"
																						value='<s:property  value="%{privTitle}"/>'
																						id='priv<%=i%>_row<%=k%><s:property  value="%{privTitle}"/>'
																						name="groupName"
																						onclick="setRolePriv('tbl_priv<%=i%>_row<%=k%>','count<%=i%>_col<%=k%>')" />

																				</s:if>
																				<s:else>
																					<input type="checkbox"
																						value='<s:property  value="%{privTitle}"/>'
																						id='priv<%=i%>_row<%=k%><s:property  value="%{privTitle}"/>'
																						name="groupName"
																						onclick="setRolePriv('tbl_priv<%=i%>_row<%=k%>','count<%=i%>_col<%=k%>')" />

																				</s:else>
																				<s:property value="%{privTitle}" />
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
												<input type="hidden" name="privCount"
													id="count<%=i%>_col<%=k%>" value="<%=privCount%>" />
												<%privCount=0; %>
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
			<table>
				<tr>
					<td>
						<input type="button" value="<s:text name="btn.reset.priv"/>"
							class="button" onclick="_id.i('result',_resp.data);" />
					</td>
					<td>
						&nbsp;
					</td>
					<td>
						<table>
							<s:submit key="btn.assign.priv" align="left" cssClass="button" />
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>




<code id="headId" style="visibility: hidden">
	$RCSfile: bo_om_editOrgPriv_FetchPriv.jsp,v $ $Revision: 1.10 $
</code>