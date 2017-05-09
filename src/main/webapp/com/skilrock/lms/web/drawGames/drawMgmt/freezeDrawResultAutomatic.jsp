
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css" />
<s:head theme="ajax" />
</head>

<table width="810" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="left" style="margin-left: 2px">
	<div style="color: red; text-align: left;">
		<s:actionmessage />
	</div>
	<tr>
		<!--
			 <s:if test="#session.DRAW_SCH_LIST.size>0">
		 <th> <input type="checkbox" name="check" onclick="checkAll(this.name)" /></th>
		  </s:if>	-->
		<tr>
			<th colspan="6" align="center"><s:text name="label.draw.list" />
			</th>
		</tr>
		<tr>
			<th width="12%" scope="col" align="center"><s:text name="label.draw.id" /></th>
			<th width="12%" scope="col" align="center"><s:text name="label.draw.time" /></th>
			<th width="12%" scope="col" align="center"><s:text name="label.draw" /> <s:text name="Freeze" /> <s:text name="label.time" /></th>
			<th width="12%" scope="col" align="center"><s:text name="label.draw.status" /></th>
			</th>
			<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
				<th width="12%" scope="col" align="center" id="eventIdCap"><s:text
						name="EventId" /></th>
			</s:if>
		</tr>
</table>
<div style="height: 125px; overflow-y: scroll; overflow-x: hidden; clear: both" id="testDiv">


	<table width="810" border="1" cellpadding="5" cellspacing="0" id="drawResTbl" bordercolor="#CCCCCC" align="center">
		<s:if test="#session.DRAW_SCH_LIST.size>0">
			<s:iterator value="#session.DRAW_SCH_LIST">
				<tr id="<s:property value="drawId" />">
					<td width="12%"><input type="radio" name="drawIds"
						" id="<s:property value="drawId" />"
						value="<s:property value="drawId" />" /> &nbsp; <s:property
							value="drawId" /></td>
					<td width="12%" align="center"
						id="<s:property value="drawId" />drawDateTime"><s:property
							value="drawDateTime" /></td>
					<td align="center" width="12%"
						id="<s:property value="drawId" />freezeDateTime"><s:property
							value="freezeDateTime" /></td>
					<td align="center" width="12%"><s:property value="drawStatus" />
					</td>
					<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
						<td width="12%" align="center"><s:property value="eventId" />
						</td>
					</s:if>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
			<tr>
				<td colspan="4" align="center"><s:text name="msg.no.result" />
				</td>

			</tr>
		</s:else>

	</table>
</div>
<input type="hidden" name="gameId" id="gameId" value="<s:property value="gameId" />" />
<input type="hidden" name="gameName" id="gameName"value="<s:property value="gameName" />" />
<div>
	<s:if test="#session.DRAW_SCH_LIST.size>0">
		<s:hidden id="privName" value="%{priv}" />
		<table width="750" border="0" cellpadding="0" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			<tr>
				<td align="right">
					<div id="forToken">
						<s:token name="token" />
					</div> 
					<input  type="button" value="<s:text name="label.generete.submit"/>" onclick="return automaticResultSubmision()" class="button" />
				</td>
			</tr>
		</table>
	</s:if>
</div>




</html>