
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.sql.Timestamp"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/drawGameMgmt.js"></script>

		<s:head theme="ajax" />
	</head>
	<body onload="fillWinningDivs()">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="success">
			<h2>
				<s:text name="label.man.win.entry"/>
			</h2>
			<s:form name="manualEntryForm" action="performManualWinning"
				onsubmit="return validateManualEntry('%{gameName}')">
				<s:hidden name="gameId" id="gameId" value="%{gameId}"></s:hidden>
				<s:hidden name="gameName" id="gameName" value="%{gameName}"></s:hidden>
				<s:hidden id="winNumSize" name="winNumSize" value=""></s:hidden>
				<s:token>
				</s:token>
				<s:if
					test="%{gameName.equalsIgnoreCase('Zimlottotwo') || gameName.equalsIgnoreCase('Zimlottothree') || gameName.equalsIgnoreCase('Zimlotto') || gameName.equalsIgnoreCase('Lotto') || gameName.equalsIgnoreCase('Fastlotto') }">
					<h3 align="left">
						*<s:text name="msg.enter.win.res.ascending.ord"/>.
					</h3>
					<br></br>
				</s:if>
				<s:if
					test="%{gameName.equalsIgnoreCase('FortuneTwo') || gameName.equalsIgnoreCase('FortuneThree') }">				
				<table width="1000" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th align="left" width="40%">
							<s:text name="label.game.no"/>.
						</th>
						<th align="left" colspan="2">
							<s:property value="gameNo" />
						</th>
					</tr>
				</table>
				<table width="1000" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th width="10%" scope="col" align="center" rowspan="2">
							<s:text name="label.draw.id"/>
						</th>
						<th width="15%" scope="col" align="center" rowspan="2">
							<s:text name="label.draw.time"/>
						</th>
						<th width="25%" scope="col" align="center" colspan="4">
							<s:text name="label.win.res"/>
						</th>
						<th width="15%" scope="col" align="center" rowspan="2">
							<s:text name="label.msg"/>
						</th>
						<th width="40%" scope="col" align="center" rowspan="2" style="white-space: nowrap;">
							<s:text name="label.draw.type"/> &nbsp;&nbsp;
						</th>
					</tr>
					
					<tr>
					<th width="15%" scope="col" align="center">1st <s:text name="label.win.no"/>.</th>
					<th width="15%" scope="col" align="center">2nd <s:text name="label.win.no"/>.</th>
					<th width="15%" scope="col" align="center">3rd <s:text name="label.win.no"/>.</th>
					<th width="15%" scope="col" align="center">4th <s:text name="label.win.no"/>.</th>					
					</tr>
					<s:iterator value="drawScheduleBeanResultList">
						<tr>
							<td width="10%">
								<input type="checkbox" checked name="drawIds"
									id="<s:property />chkBx" value="<s:property value="drawId"/>" onclick="" />
								&nbsp;
								<s:property value="drawId"/>
							</td>
							<td width="15%" align="center">
								<div id="drawDateTime">
									<s:property value="drawDateTime" />
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>WinRes1">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>WinRes2">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>WinRes3">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>WinRes4">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>Message">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>DrawTypeSelect">
									&nbsp;
								</div>
							</td>
						</tr>
					</s:iterator>
					<s:iterator value="performStatus">
						<div name="performStatusID" style="display: none;">
							<s:property />
						</div>
					</s:iterator>
					<tr>
						<td colspan="3" align="right">
							<input type="submit" value="<s:text name="btn.submit"/>" />
						</td>
					</tr>
				</table>
				</s:if>
				<s:elseif test="%{gameName.equalsIgnoreCase('KenoFive') || gameName.equalsIgnoreCase('KenoFour')}">
				<table width="1000" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th align="left" width="40%">
							<s:text name="label.game.no"/>.
						</th>
						<th align="left" colspan="2">
							<s:property value="gameNo" />
						</th>
					</tr>
				</table>
				<table width="800" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.id"/>
						</th>
						<th width="20%" scope="col" align="center">
							<s:text name="label.draw.time"/>
						</th>
						<th width="40%" scope="col" align="center">
							<s:text name="label.win.res"/>
						</th>
						<th width="40%" scope="col" align="center">
							<s:text name="label.mc.no.res"/>
						</th>
						<th width="20%" scope="col" align="center">
							<s:text name="label.msg"/>
						</th>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.type"/>
						</th>
					</tr>
					<s:iterator value="drawScheduleBeanResultList">
						<tr>
							<td width="10%">
								<input type="checkbox" checked name="drawIds"
									id="<s:property />chkBx" value="<s:property value="drawId"/>" onclick="" />
								&nbsp;
								<s:property value="drawId"/>
							</td>
							<td width="22%" align="center">
								<div id="drawDateTime">
									<s:property value="drawDateTime" />
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>WinRes">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>MacRes">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>Message">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>DrawTypeSelect">
									&nbsp;
								</div>
							</td>
						</tr>
					</s:iterator>
					<s:iterator value="performStatus">
						<div name="performStatusID" style="display: none;">
							<s:property />
						</div>
					</s:iterator>
					<tr>
						<td colspan="3" align="right">
							<input type="submit" value="<s:text name="btn.submit"/>" />
						</td>
					</tr>
				</table>
				</s:elseif>

				<s:elseif test="%{gameName.equalsIgnoreCase('KenoSeven')}">
					<table width="1000" border="1" cellpadding="5" cellspacing="0" bordercolor="#CCCCCC" align="center">
						<tr>
							<th align="left" width="40%">
								<s:text name="label.game.no"/>.
							</th>
							<th align="left" colspan="2">
								<s:property value="gameNo" />
							</th>
						</tr>
					</table>
					<table width="900" border="1" cellpadding="5" cellspacing="0" bordercolor="#CCCCCC" align="center">
						<tr>
							<th width="10%" scope="col" align="center">
								<s:text name="label.draw.id"/>
							</th>
							<th width="20%" scope="col" align="center">
								<s:text name="label.draw.time"/>
							</th>
							<th width="40%" scope="col" align="center">
								<s:text name="label.win.res"/>
							</th>
							<th width="20%" scope="col" align="center">
								<s:text name="label.msg"/>
							</th>
							<th width="10%" scope="col" align="center">
								<s:text name="label.draw.type"/>
							</th>
						</tr>
						<s:iterator value="drawScheduleBeanResultList">
							<tr>
								<td width="10%">
									<input type="checkbox" checked name="drawIds" id="<s:property />chkBx" value="<s:property value="drawId"/>" onclick="" />
									&nbsp;
									<s:property value="drawId"/>
								</td>
								<td width="22%" align="center">
									<div id="drawDateTime">
										<s:property value="drawDateTime" />
									</div>
								</td>
								<td>
									<div id="<s:property value="drawId"/>WinRes">
										&nbsp;
									</div>
								</td>
								<td>
									<div id="<s:property value="drawId"/>Message">
										&nbsp;
									</div>
								</td>
								<td>
									<div id="<s:property value="drawId"/>DrawTypeSelect">
										&nbsp;
									</div>
								</td>
							</tr>
						</s:iterator>
						<s:iterator value="performStatus">
							<div name="performStatusID" style="display: none;">
								<s:property />
							</div>
						</s:iterator>
						<tr>
							<td colspan="3" align="right">
								<input type="submit" value="<s:text name="btn.submit"/>" />
							</td>
						</tr>
					</table>
				</s:elseif>

				<s:elseif test="%{gameName.equalsIgnoreCase('FourDigit')}">
				<table width="1000" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th align="left" width="40%">
							<s:text name="label.game.no"/>.
						</th>
						<th align="left" colspan="2">
							<s:property value="gameNo" />
						</th>
					</tr>
				</table>
				<table width="800" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.id"/>
						</th>
						<th width="20%" scope="col" align="center">
							<s:text name="label.draw.time"/>
						</th>
						<th width="40%" scope="col" align="center" colspan=2>
							<s:text name="label.win.res"/>
						</th>
						<th width="20%" scope="col" align="center">
							<s:text name="label.msg"/>
						</th>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.type"/>
						</th>
					</tr>
					<s:iterator value="drawScheduleBeanResultList">
						<tr>
							<td width="10%" rowspan=2>
								<input type="checkbox" checked name="drawIds"
									id="<s:property />chkBx" value="<s:property value="drawId"/>" onclick="" />
								&nbsp;
								<s:property value="drawId"/>
							</td>
							<td width="22%" align="center" rowspan=2>
								<div id="drawDateTime">
									<s:property value="drawDateTime" />
								</div>
							</td>
							<td colspan=2>
								<div id="a">
									<s:text name="label.top.prizes"/>
								</div>
								<div id="<s:property value="drawId"/>WinRes1">
									&nbsp;
								</div>
							</td>
							
							
							
							<td rowspan=2>
								<div id="<s:property value="drawId"/>Message">
									&nbsp;
								</div>
							</td>
							<td rowspan=2>
								<div id="<s:property value="drawId"/>DrawTypeSelect">
									&nbsp;
								</div>
							</td>
						</tr>
						<tr>
							<td>
								<div id="a">
									<s:text name="label.spcl.prizes"/>
								</div>
								<div id="<s:property value="drawId"/>WinRes2">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="a">
									<s:text name="label.cons.prizes"/>
								</div>
								<div id="<s:property value="drawId"/>WinRes3">
									&nbsp;
								</div>
							</td>
						</tr>
					</s:iterator>
					<s:iterator value="performStatus">
						<div name="performStatusID" style="display: none;">
							<s:property />
						</div>
					</s:iterator>
					<tr>
						<td colspan="3" align="right">
							<input type="submit" value="<s:text name="btn.submit"/>" />
						</td>
					</tr>
				</table>
				</s:elseif>
				<s:elseif test="%{gameName.equalsIgnoreCase('TwelveByTwentyFour')}">
					<table width="1000" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th align="left" width="40%">
							<s:text name="label.game.no"/>.
						</th>
						<th align="left" colspan="2">
							<s:property value="gameNo" />
						</th>
					</tr>
				</table>
				<table width="800" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.id"/>
						</th>
						<th width="20%" scope="col" align="center">
							<s:text name="label.draw.time"/>
						</th>
						<th  width = "18%" scope="col" align="center">
							<s:text name="DrawName" /> 
						</th>
						<%-- <th  width = "20%" scope="col" align="center">
							<s:text name="EventId" /> 
						</th> --%>
						<th width="40%" scope="col" align="center">
							<s:text name="label.win.res"/>
						</th>
						<th width="20%" scope="col" align="center">
							<s:text name="label.msg"/>
						</th>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.type"/>
						</th>
					</tr>
					<s:iterator value="drawScheduleBeanResultList">
						<tr>
							<td width="10%">
								<input type="checkbox" checked name="drawIds"
									id="<s:property />chkBx" value="<s:property value="drawId"/>" onclick="" />
								&nbsp;
								<s:property value="drawId"/>
							</td>
							<td width="22%" align="center">
								<div id="drawDateTime">
									<s:property value="drawDateTime" />
								</div>
							</td>
							<td  align="center">
									<s:property value="drawName" />
							</td>
							<%-- <td align="center">
									<s:property value="eventId" />
							</td> --%>
							<td>
								<div id="<s:property value="drawId"/>WinRes">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>Message">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>DrawTypeSelect">
									&nbsp;
								</div>
							</td>
						</tr>
					</s:iterator>
					<s:iterator value="performStatus">
						<div name="performStatusID" style="display: none;">
							<s:property />
						</div>
					</s:iterator>
					<tr>
						<td colspan="3" align="right">
							<input type="submit" value="<s:text name="btn.submit"/>" />
						</td>
					</tr>
				</table>
				</s:elseif>
				<s:elseif test="%{gameName.equalsIgnoreCase('RainbowGame') or gameName.equalsIgnoreCase('PickThree') or gameName.equalsIgnoreCase('PickFour') }">
					<table width="1000" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th align="left" width="40%">
							<s:text name="label.game.no"/>.
						</th>
						<th align="left" colspan="2">
							<s:property value="gameNo" />
						</th>
					</tr>
				</table>
				<table width="900" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.id"/>
						</th>
						<th width="15%" scope="col" align="center">
							<s:text name="label.draw.time"/>
						</th>
						<th  width = "18%" scope="col" align="center">
							<s:text name="DrawName" /> 
						</th>
						<%-- <th  width = "20%" scope="col" align="center">
							<s:text name="EventId" /> 
						</th> --%>
						<th width="45%" scope="col" align="center">
							<s:text name="label.win.res"/>
						</th>
						<th width="20%" scope="col" align="center">
							<s:text name="label.msg"/>
						</th>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.type"/>
						</th>
					</tr>
					<s:iterator value="drawScheduleBeanResultList">
						<tr>
							<td width="10%">
								<input type="checkbox" checked name="drawIds"
									id="<s:property />chkBx" value="<s:property value="drawId"/>" onclick="" />
								&nbsp;
								<s:property value="drawId"/>
							</td>
							<td width="22%" align="center">
								<div id="drawDateTime">
									<s:property value="drawDateTime" />
								</div>
							</td>
							<td  align="center">
									<s:property value="drawName" />
							</td>
							<%-- <td align="center">
									<s:property value="eventId" />
							</td> --%>
							<td>
								<div id="<s:property value="drawId"/>WinRes">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>Message">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>DrawTypeSelect">
									&nbsp;
								</div>
							</td>
						</tr>
					</s:iterator>
					<s:iterator value="performStatus">
						<div name="performStatusID" style="display: none;">
							<s:property />
						</div>
					</s:iterator>
					<tr>
						<td colspan="3" align="right">
							<input type="submit" value="<s:text name="btn.submit"/>" />
						</td>
					</tr>
				</table>
				</s:elseif>
				<s:else>
					<table width="1000" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th align="left" width="40%">
							<s:text name="label.game.no"/>.
						</th>
						<th align="left" colspan="2">
							<s:property value="gameNo" />
						</th>
					</tr>
				</table>
				<table width="800" border="1" cellpadding="5" cellspacing="0"
					bordercolor="#CCCCCC" align="center">
					<tr>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.id"/>
						</th>
						<th width="20%" scope="col" align="center">
							<s:text name="label.draw.time"/>
						</th>
						<th  width = "18%" scope="col" align="center">
							<s:text name="DrawName" /> 
						</th>
						<th  width = "20%" scope="col" align="center">
							<s:text name="EventId" /> 
						</th>
						<th width="40%" scope="col" align="center">
							<s:text name="label.win.res"/>
						</th>
						<th width="20%" scope="col" align="center">
							<s:text name="label.msg"/>
						</th>
						<th width="10%" scope="col" align="center">
							<s:text name="label.draw.type"/>
						</th>
					</tr>
					<s:iterator value="drawScheduleBeanResultList">
						<tr>
							<td width="10%">
								<input type="checkbox" checked name="drawIds"
									id="<s:property />chkBx" value="<s:property value="drawId"/>" onclick="" />
								&nbsp;
								<s:property value="drawId"/>
							</td>
							<td width="22%" align="center">
								<div id="drawDateTime">
									<s:property value="drawDateTime" />
								</div>
							</td>
							<td  align="center">
									<s:property value="drawName" />
							</td>
							<td align="center">
									<s:property value="eventId" />
							</td>
							<td>
								<div id="<s:property value="drawId"/>WinRes">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>Message">
									&nbsp;
								</div>
							</td>
							<td>
								<div id="<s:property value="drawId"/>DrawTypeSelect">
									&nbsp;
								</div>
							</td>
						</tr>
					</s:iterator>
					<s:iterator value="performStatus">
						<div name="performStatusID" style="display: none;">
							<s:property />
						</div>
					</s:iterator>
					<tr>
						<td colspan="3" align="right">
							<input type="submit" value="<s:text name="btn.submit"/>" />
						</td>
					</tr>
				</table>
				</s:else>
			</s:form>
		</div>
		<div id="manual_validate_err"></div>
		<s:hidden value="%{bonusBallEnable}" id="bonusBallEnable"/>
	</body>
</html>

<code id="headId" style="visibility: hidden">
$RCSfile: manualWiningEntry.jsp,v $
$Revision: 1.3 $
</code>