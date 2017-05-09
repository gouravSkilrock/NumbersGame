<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			"type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/style.css"
			"type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		
		<script type="text/javascript">
			var country = '<%=application.getAttribute("COUNTRY_DEPLOYED")%>';
			var lang = '<%=Locale.getDefault().getLanguage()%>';
			var fileName="Messages_"+country;
			var projectName="<%=request.getContextPath()%>";		
		</script>
		
		<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.10.2.js"></script>

		<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/i18nLoader.js"></script>
		
		<script type="text/javascript">
			var sesId = '<%=session.getId()%>';
			var reqAjax;
			var cachedMap = new Array();
			var Version = new Array();
			var fnToCallGlbl;
			var count = 1;
			function ajaxReq(url, fnToCall) {
				var _c = "?";
				if (url.indexOf("?") != -1) {
					_c = "&";
				}
				if (!url.match("js/ajaxRequest.js")) {
					url = url + _c + new Date().getTime() + sesId + Math.random() * 1000000;
				}
				fnToCallGlbl = fnToCall;
				reqAjax = XMLRequest2();
				reqAjax.onreadystatechange = prsStat3;
				reqAjax.open("GET", url, true);
				reqAjax.setRequestHeader("If-Modified-Since", new Date().getTime());
				reqAjax.send(null);
			}
			function prsStat3() {
						//alert(isDraw+"==="+isScratch);
				if (reqAjax.readyState == 4) { // Complete
					if (reqAjax.status == 200) {
						if (fnToCallGlbl == "fillUserDetails") {
							fillUserDetails(reqAjax.responseText);
							ajaxReq("<%=request.getContextPath()%>/js/ajaxRequest.js", "");
						} else {
							if (fnToCallGlbl == "") {
								ajaxReqReceived = true;
								if (ownerType == "RETAILER") {
									createServiceMenu("dg");
									parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/rpos.action");
								} else {
									parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action");
								}
								document.getElementById("startDivLabel").style.display = "none";
								document.getElementById("startDiv").style.display = "none";
							}
						}
					}
				}
			}
			function XMLRequest2() {
				if (window.XMLHttpRequest) {
					return new XMLHttpRequest();
				}
				return new ActiveXObject("Microsoft.XMLHTTP");
			}
			function fillUserDetails(responseData) {
				var respData = responseData.split("cacheBreakPoint");
				var detArr = respData[0].split(":");
				document.getElementById("currentLoggedUser").innerHTML = detArr[0];
				document.getElementById("currentLoggedUserMoz").value = detArr[0];
				currentUser = detArr[0];
				ownerType = detArr[2];
				if (ownerType == "RETAILER" && isDraw == "YES") {
					document.getElementById("rposDiv").style.display = "block";
				}
				/* var cachedPages = respData[1].replace("{", "").replace("}", "").split(",");
				for (var i in cachedPages) {
					cachedMap[(cachedPages[i].split("=")[0]).replace(/ /g, "")] = (cachedPages[i].split("=")[1]).replace(/ /g, "");
				} */
				createArray(respData[2]);
			}
			var serviceMenuArr = new Array();
			var currentService = "";
			function createArray(responseData) {
				var tabsArr = new Array();
				var compArr = new Array();
				var serviceTab = "";
				var firstService;
				tabsArr = responseData.split("TABS-");
				for (var j = 1; j < tabsArr.length; j++) {
					compArr = tabsArr[j].split("RT-");
					serviceMenuArr[compArr[0].split("-")[1].toLowerCase()] = compArr;
					if (j == 1) {
						firstService = compArr[0].split("-")[1].toLowerCase();
					}
					serviceTab = serviceTab + "<div class=\"tabnew\" id=\"" + compArr[0].split("-")[1].toLowerCase() + "\"> <a href=\"#\" onclick=\"" + compArr[0].split("-")[1].toLowerCase() + "Click()\">" + compArr[0].split("-")[0] + "</a></div>";
				}
				document.getElementById("services").innerHTML = serviceTab;
				createServiceMenu(firstService);
			}
			function createServiceMenu(serviceName) {
				if (currentService != "") {
					document.getElementById(currentService).className = "tabnew";
				}
				document.getElementById(serviceName).className = "tabnewClick";
				currentService = serviceName;
				var compArr = serviceMenuArr[serviceName];
				var name = new Array();
				var action = new Array();
				var module = new Array();
				var relatedTo = new Array();
				if (compArr) {
					for (var i = 1; i < compArr.length; i++) {
						var spliData = compArr[i].split(";");
						name[i] = spliData[1];
						action[i] = spliData[2];
						relatedTo[i] = spliData[0];
					}
				}
				var path = projectName;
				var USER_MGT = "";
				var GAME_MGT = "";
				var DRAW_MGT = "";
				var PLAY_MGT = "";
				var MISC = "";
				var INV_MGT = "";
				var ORDER_MGT = "";
				var ACT_MGT = "";
				var PWT = "";
				var REPORTS = "";
				var ROLE_MGT = "";
				var serName = "";
				var BO_USER_MGT = "";
				var AGENT_USER_MGT = "";
				var RET_USER_MGT = "";
				var ADMIN_MGT="";
				var PROD_MGT="";
				if (serviceName == "se") {
					serName = "/scratchService";
				} else {
					if (serviceName == "dg") {
						serName = "/drawGames";
					}
				}
				var counter = "";
				for (var i = 0; i < relatedTo.length; i++) {
					counter = " " + counter + "*" + relatedTo[i];
					if (relatedTo[i] == "ADMIN_MGT") {
						ADMIN_MGT += "<a href=\"" + path + "/com/skilrock/lms/admin/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "PROD_MGT") {
						PROD_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/productMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "USER_MGT") {
						USER_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/userMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "GAME_MGT") {
						//alert(action[i]);
						GAME_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/gameMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "DRAW_MGT") {
						DRAW_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/drawMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					/*if(relatedTo[i]=="PLAY_MGT"){
						PLAY_MGT+='<a href="'+path+'/com/skilrock/lms/web'+serName+'/playMgmt/'+action[i]+'.action" onclick = " return getCookie()">'+name[i]+'</a>';
						}*/
					if (relatedTo[i] == "INV_MGT") {
						INV_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/inventoryMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "ORDER_MGT") {
						ORDER_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/orderMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "ACT_MGT") {
						ACT_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/accMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "PWT") {
						PWT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/pwtMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "REPORTS") {
						REPORTS += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/reportsMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "ROLE_MGT") {
						ROLE_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/roleMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "MISC") {
						MISC += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/userMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "BO_USER_MGT") {
						BO_USER_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/userMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "AGENT_USER_MGT") {
						AGENT_USER_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/userMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
					if (relatedTo[i] == "RET_USER_MGT") {
						RET_USER_MGT += "<a href=\"" + path + "/com/skilrock/lms/web" + serName + "/userMgmt/" + action[i] + ".action\" onclick = \" return getCookie()\">" + name[i] + "</a>";
					}
				}
						//if(ownerType=='BO'){
						//ROLE_MGT+='<a href="'+path+'/com/skilrock/lms/web/drawGames/drawMenu.action" onclick = " return getCookie()">Draw Game Management</a>';
						//}
				/*GAME_MGT+='<a href="'+path+'/com/skilrock/lms/web/drawGames/fetchDrawDataMenu.action" onclick = " return getCookie()">Draw Game </a>';
						GAME_MGT+='<a href="'+path+'/com/skilrock/lms/web/drawGames/trackDrawTktMenu.action" onclick = " return getCookie()">Track Ticket </a>';
						GAME_MGT+='<a href="'+path+'/com/skilrock/lms/web/drawGames/drawMenu.action?invoker=ManualEntry" onclick = " return getCookie()">Manual Winning Entry </a>';
						GAME_MGT+='<a href="'+path+'/com/skilrock/lms/web/drawGames/drawMenu.action?invoker=ManualDeclare" onclick = " return getCookie()">Manual Declaration Entry </a>';
						GAME_MGT+='<a href="'+path+'/com/skilrock/lms/web/drawGames/drawPerformMenu.action" onclick = " return getCookie()">Claim Conditions </a>';
						*/
						//USER_MGT+='<a href="'+path+'/com/skilrock/lms/web/reportsMgmt/bo_rep_cashChqAdv_Menu.action" onclick = " return getCookie()">Activity rep </a>';
				if (ownerType == "AGENT") {
					AGENT_USER_MGT = AGENT_USER_MGT + ROLE_MGT;
				}
				if (ownerType == "BO") {
					if ("bomaster" == parent.frames[0].document.getElementById("currentLoggedUser").innerText && serviceName == "mgmt") {
						ROLE_MGT += "<a href=\"" + path + "/com/skilrock/lms/web/userMgmt/bo_um_editBoMasPriv_Menu.action\" onclick = \" return getCookie()\">Edit BOMaster Privileges</a>";
						//REPORTS+='<a href="'+path+'/com/skilrock/lms/web/reportsMgmt/bo_rep_soldBookEntry_Menu.action" onclick = " return getCookie()">Retailer Sold Book Entry Report</a>';
					}
					BO_USER_MGT = ROLE_MGT + BO_USER_MGT;
				}
				parent.frames[0].document.getElementById("dropmenu0").innerHTML = USER_MGT;
				parent.frames[0].document.getElementById("dropmenu1").innerHTML = GAME_MGT;
				parent.frames[0].document.getElementById("dropmenu2").innerHTML = ORDER_MGT;
				parent.frames[0].document.getElementById("dropmenu3").innerHTML = ACT_MGT;
				parent.frames[0].document.getElementById("dropmenu4").innerHTML = PWT;
				parent.frames[0].document.getElementById("dropmenu5").innerHTML = REPORTS;
				parent.frames[0].document.getElementById("dropmenu6").innerHTML = INV_MGT;
				parent.frames[0].document.getElementById("dropmenu7").innerHTML = MISC;
				parent.frames[0].document.getElementById("dropmenu8").innerHTML = BO_USER_MGT;
				parent.frames[0].document.getElementById("dropmenu9").innerHTML = AGENT_USER_MGT;
				parent.frames[0].document.getElementById("dropmenu10").innerHTML = RET_USER_MGT;
				parent.frames[0].document.getElementById("dropmenu11").innerHTML = DRAW_MGT;
				parent.frames[0].document.getElementById("dropmenu12").innerHTML = ADMIN_MGT;
				parent.frames[0].document.getElementById("dropmenu13").innerHTML = PROD_MGT;
			}
			function getMenu() {
				var arr = new Array("one", "two", "four", "five", "six", "seven", "three", "eight", "nine", "ten", "eleven", "twelve", "thirteen","fourteen");
				for (var k = 0; k < arr.length; k++) {
					parent.frames[1].document.getElementById("dropmenu" + k).innerHTML = parent.frames[0].document.getElementById("dropmenu" + k).innerHTML;
				}
				for (var i = 0; i < arr.length; i++) {
					var dropmenu = "dropmenu" + i;
					if (parent.frames[1].document.getElementById(dropmenu)) {
						if (parent.frames[1].document.getElementById(dropmenu).innerHTML == "") {
								//parent.frames[1].document.getElementById(arr[i]).style.background = "url('"+projectName+"/LMSImages/images/chromebg6-over.gif')";
								//alert(dropmenu);
							if (parent.frames[1].document.getElementById(arr[i])) {
								parent.frames[1].document.getElementById(arr[i]).style.display = "none";
							}
							parent.frames[1].document.getElementById(dropmenu).className = "dropmenudivdis";
						}
					}
				}
			}
			function firstCallFunc() {
				if (count == 1) {
					count++;
				}
			}
			function adminClick() {
				createServiceMenu("admin");
				parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action");
			}
			function mgmtClick() {
				createServiceMenu("mgmt");
				parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action");
			}
			function dgClick() {
				createServiceMenu("dg");
				parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action");
			}
			function seClick() {
				createServiceMenu("se");
				parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action");
			}
			function getCookie() {
				Version = new Array();
				var cookie = document.cookie;
				if (cookie != null) {
					var cookieArray = cookie.split("; ");
					var currentLoggedUser = "";
						// Parse the cookies: get the stored time
					for (var loop = 0; loop < cookieArray.length; loop++) {
					/*	var nameValue = cookieArray[loop].split("=");
						if (parent.frames[1].BrowserDetect.browser == "Firefox") {
							currentLoggedUser = (window.parent.top.document.getElementById("currentLoggedUserMoz")).value;
						} else {
							currentLoggedUser = parent.frames[0].document.getElementById("currentLoggedUser").innerText;
						}
						if (nameValue[0].toString() == "LMSCookie") {
							if (nameValue[1].toUpperCase().replace(/ /g, "").replace(/"/g, "") != currentLoggedUser.toUpperCase().replace(/ /g, "")) {
								var jsErrMsg = "Current Logged User--" + currentLoggedUser.toUpperCase() + "--User in Cookie--" + nameValue[1].toString().toUpperCase() + "-Brw-" + parent.frames[1].BrowserDetect.browser + " v " + parent.frames[1].BrowserDetect.version + "&agentOrgName=" + currentLoggedUser;
								ajaxReq(projectName + "/com/skilrock/lms/web/loginMgmt/logJsError.action?message=" + jsErrMsg, "jsErr");
								window.open("" + projectName + "/com/skilrock/lms/web/loginMgmt/multipleLogin.action", target = "_top");
								return false;
							}
						}*/
					}
				}
				return true;
			}
		</script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/js/menu.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/validator.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/ajaxDojo.js"></script>
	</head>

	<body>


		<div id="header_links">

			<input type="hidden" value="<%=application.getAttribute("IS_DRAW")%>"
				id="is_draw" name="D_R" />
			<input type="hidden"
				value="<%=application.getAttribute("IS_SCRATCH")%>" id="is_scratch"
				name="S_R" />

			<label
				style="position: absolute; top: 0; left: 340px; text-align: center;"
				id="startDivLabel">
				<b>Please Wait while page load...</b>
			</label>
			<label
				style="position: absolute; display: none; top: 0; left: 340px; text-align: center;"
				id="refreshDivLabel">
				<b>Please Wait while page load...</b>
			</label>
			<div id="startDiv"
				style="width: 1000px; position: absolute; filter: alpha(opacity =  0); height: 50px;"></div>
			<div id="currencyWord" style="display: none;">
				<s:property value="%{#application.CURRENCY_SYMBOL}" />
			</div>
		</div>
		<div style="width: 995px; height: auto; margin: 0 auto;">
			<table width="995px" cellpadding="0" border="0" cellspacing="0"
				style="margin-right: 17px;">
				<tr>
					<td align="left">
						<img
							src="<%=request.getContextPath()%>/LMSImages/images/header_logo_lagos.gif"
							alt="logo" />
					</td>
					<td style="width: 67%">					
						<table width="100%">
							<tr style="background-color: #efba01">
								<td>
									<div id="box1">
										<p>
											<label style="color: #585858;">
												Welcome :
											</label>
											<label id="currentLoggedUser"></label>
											<input type="hidden" id="currentLoggedUserMoz" value="" />
											<script>ajaxReq("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/fetchNamenLimitAdmin.action","fillUserDetails")</script>
										</p>

									</div>
									<div id="box2">
										<p>
											<span class="white">l</span>

											<a
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/AdminSuccess.action"
												target="_blank" name="tab" class="white"
												style="target-new: tab ! important"
												onclick="if(!event.ctrlKey&&!window.opera){alert('Please Try Again while holding down the Ctrl Key');return false;}else{return true;}">New
												Tab</a>
											<span class="white">l</span>
											<a
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/Logout.action"
												target="_top" onclick=" return getCookie()" class="white">Logout</a>
											<span class="white">l</span>
											<a
												href="<%=request.getContextPath()%>/com/skilrock/lms/admin/admin_changePassword.action"
												target="leftbottom" onclick=" return getCookie()"
												class="white">Change Password</a> 
										</p>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div id="services" style="position: absolute;left:200px;top:60px">
									</div>
								</td>
							</tr>
						</table>
						
					</td>
					<td align="center">
						<img
							src="<%=request.getContextPath()%>/LMSImages/images/LmsLogo.gif"
							alt="LMS" style="margin-right: 5px;" />
						<%=(String) application
									.getAttribute("VERSION_DETAILS")%>
					</td>
				</tr>
			</table>
		</div>

		<div id="menuHeader" style="visibility: hidden;">
			<!--0st drop down menu -->
			<div id="dropmenu0" class="dropmenudiv"></div>
			<!--1st drop down menu -->
			<div id="dropmenu1" class="dropmenudiv"></div>
			<div id="dropmenu6" class="dropmenudiv"></div>
			<!--2nd drop down menu -->
			<div id="dropmenu2" class="dropmenudiv"></div>
			<!--3rd drop down menu -->
			<div id="dropmenu3" class="dropmenudiv"></div>
			<!--4st drop down menu -->
			<div id="dropmenu4" class="dropmenudiv"></div>
			<!--5st drop down menu -->
			<div id="dropmenu5" class="dropmenudiv"></div>
			<div id="dropmenu7" class="dropmenudiv"></div>
			<div id="dropmenu8" class="dropmenudiv"></div>
			<div id="dropmenu9" class="dropmenudiv"></div>
			<div id="dropmenu10" class="dropmenudiv"></div>
			<div id="dropmenu11" class="dropmenudiv"></div>
			<div id="dropmenu12" class="dropmenudiv"></div>
			<div id="dropmenu13" class="dropmenudiv"></div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: adminHeader.jsp,v $ $Revision: 1.3 $
</code>