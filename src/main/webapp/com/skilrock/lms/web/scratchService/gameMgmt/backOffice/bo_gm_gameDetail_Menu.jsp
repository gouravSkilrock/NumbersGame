<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/gameMgmt/backOffice/js/gameDetailsManagement.js"></script>
	<s:head theme="ajax" debug="false"/> 
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
    <div id="top_form">
  	<h3><s:text name="label.game.details"/></h3>
	<div id="hidden_game_name" style="display: none;"><s:select cssStyle="display: none;" theme="simple"  headerKey="-1" headerValue="--Please Select--"  id="game_name_hid" list="%{gameList}" cssClass="option" /></div>
  
  	<s:form name="searchgame" action="bo_gm_gameDetail_Detail" onsubmit="return validateGameName()"> 
  							
	<table  border="0" cellpadding="2" cellspacing="0" width="300"> 
			<tr>
				<td colspan="2"><s:select name="gameStatus" headerKey="-1" headerValue="%{getText('label.please.select')}" key="label.game.status" list="{'CLOSE','OPEN','SALE_HOLD','SALE_CLOSE','TERMINATE'}" cssClass="option" onchange="getGameList(this.value)" /></td>
			</tr>
			<tr>
				<td colspan="2" align="center"><div id="game_name_error"></div></td>
			</tr>
			<tr> 			
				<td colspan="2">
					<s:select name="gameName" key="label.game.name" id="game_name" headerKey="-1" headerValue="%{getText('label.please.select')}" list="%{gameList}" cssClass="option" />
				</td>			
			</tr>

			<tr>
				<td colspan="2"><s:submit name="search" key="btn.srch" align="center"  targets="down" theme="ajax" cssClass="button"/></td>
			</tr>
		
		</table>
	</s:form>
	<div id="down" style="text-align: center" ></div>
	</div></div>
	
 	
 	

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_gameDetail_Menu.jsp,v $
$Revision: 1.3 $
</code>