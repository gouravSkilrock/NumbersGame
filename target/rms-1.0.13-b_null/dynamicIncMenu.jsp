<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<style>
#menubx {
	width: 995px;
	height: auto;
	margin: 0 auto;
}

.chromestyle {
	width: 995px;
	height: auto;
	font-weight: bold;
	background-color: #FFFFFF;
}

.chromestyle:after {
	content: ".";
	display: block;
	height: 0;
	clear: both;
	visibility: hidden;
}

.chromestyle ul {
	border: 1px solid #BBB;
	width: 100%;
	background:
		url("<%=request.getContextPath()%>/LMSImages/images/chromebg6.gif")
		center center repeat-x;
	padding: 5px 0;
	margin: 0;
	text-align: left;
}

.chromestyle ul li {
	display: inline;
	width: 220px;
}

.chromestyle ul li a {
	color: #494949;
	padding: 4px 7px;
	margin: 0;
	width: 220px;
	text-decoration: none;
	border-right: 1px solid #DADADA;
}

.chromestyle ul li a:hover,.chromestyle ul li a.selected {
	background:
		url("<%=request.getContextPath()%>/LMSImages/images/chromebg6-over.gif")
		center center repeat-x;
}

.dropmenudiv {
	position: absolute;
	top: 0;
	border: 1px solid #BBB;
	border-bottom-width: 0;
	font: normal 12px Verdana;
	line-height: 18px;
	z-index: 100;
	background-color: white;
	width: 220px;
	visibility: hidden;
	align: left;
	filter: progid :                           
		   DXImageTransform.Microsoft.Shadow (     
		
		  
		         
		           color =            
		   
		              #CACACA, direction =                               135,
		strength =    
		  
		              
		        4 );
}

.dropmenudiv a {
	width: auto;
	display: block;
	text-indent: 0px;
	border-bottom: 1px solid #BBB;
	padding: 0px 0;
	text-decoration: none;
	text-align: left;
	font-weight: bold;
	color: black;
}

* html .dropmenudiv a {
	width: 100%;
}

.dropmenudiv a:hover {
	background-color: #f0f0f0;
}

.dropmenudivdis {
	top: 0;
	border: 0px solid #BBB;
	border-bottom-width: 0;
	font: normal 0px Verdana;
	line-height: 0px;
	z-index: 100;
	background-color: black;
	width: 0px;
	visibility: hidden;
	align: left;
}
</style>
		<noscript>
			<s:text name="msg.js.disabled" />
		</noscript>
		<script type="text/javascript">
var projectName="<%=request.getContextPath()%>";var lodDiv = 'Please Wait....<img src="'+projectName+'/LMSImages/images/loadingAjax.gif"/>';
	function getCookie() {
		if (parent.frames[0].getCookie()) {
			if (parent.frames[0].document.getElementById("currentLoggedUser").innerText != "bomaster") {
				onLoadAjax(
						projectName
								+ "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action",
						'avlCredit');
			}
		}
	}
</script>
	 	<link rel="stylesheet" 
			href="<%=request.getContextPath()%>/LMSImages/css/jquery-ui-themes-1.10.4/themes/ui-lightness/jquery-ui.css" type="text/css" /> 
 		 <script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/ajaxCall.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/commonApp.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/ajaxDojo.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/ajaxRequest.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/jsSorting.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/js/menu.js"></script>
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/LMSImages/css/jsSortable.css" />
	</head>
	<noscript>
		<b><font size="2"><s:text name="msg.script.disabled" />.</font> </b>
	</noscript>
	<body leftmargin="0">

		<div id="menubx">
			<div class="chromestyle" id="chromemenu">
				<ul>
					<li id="nine">
						<a href="#" rel="dropmenu8"><s:text name="menu.bo.user.mgmt" />
						</a>
					</li>
					<li id="one">
						<a href="#" rel="dropmenu0"><s:property
								value="#application.TIER_MAP.AGENT" /> <s:text name="menu.mgmt" />
						</a>
					</li>
					<li id="five">
						<a href="#" rel="dropmenu3"><s:text name="menu.paymnt" /> </a>
					</li>
					<li id="two">
						<a href="#" rel="dropmenu1"><s:text name="menu.game.mgmt" />
						</a>
					</li>
					<li id="twelve">
						<a href="#" rel="dropmenu11"><s:text name="menu.draw.mgmt" />
						</a>
					</li>
					<li id="three">
						<a href="#" rel="dropmenu6"><s:text name="menu.inv.mgmt" /> </a>
					</li>
					<li id="four">
						<a href="#" rel="dropmenu2"><s:text name="menu.order.mgmt" />
						</a>
					</li>
					<li id="six">
						<a href="#" rel="dropmenu4"><s:text name="menu.pwt" /> </a>
					</li>
					<li id="seven">
						<a href="#" rel="dropmenu5"><s:text name="menu.report" /> </a>
					</li>
					<li id="eight">
						<a href="#" rel="dropmenu7"><s:text name="menu.misc" /> </a>
					</li>
					<li id="ten">
						<a href="#" rel="dropmenu9"><s:text name="menu.user.mgmt" />
						</a>
					</li>
					<li id="eleven">
						<a href="#" rel="dropmenu10"><s:property
								value="#application.TIER_MAP.RETAILER" /> <s:text
								name="menu.user.mgmt" /> </a>
					</li>
					<li id="thirteen">
						<a href="#" rel="dropmenu12"><s:text name="menu.admin.mgmt" />
						</a>
					</li>
					<li id="fourteen">
						<a href="#" rel="dropmenu13"><s:text name="menu.product.mgmt" />
						</a>
					</li>
					<li id="fifteen">
						<a href="#" rel="dropmenu14"><s:text name="menu.ola.mgmt" />
						</a>
					</li>
					<li id="sixteen">
						<a href="#" rel="dropmenu15"><s:text
								name="menu.sport.game.mgmt" /> </a>
					</li>
					<li id="seventeen">
						<a href="#" rel="dropmenu16"><s:text
								name="menu.instant.win" /> </a>
					</li>
					<!--  <li id="sixteen"><a href="#" rel="dropmenu15">OLA Reports</a></li>-->
				</ul>
			</div>
			<div id="mainMenu">
				<div id="dropmenu0" class="dropmenudiv"></div>
				<div id="dropmenu1" class="dropmenudiv"></div>
				<div id="dropmenu6" class="dropmenudiv"></div>
				<div id="dropmenu2" class="dropmenudiv"></div>
				<div id="dropmenu3" class="dropmenudiv"></div>
				<div id="dropmenu4" class="dropmenudiv"></div>
				<div id="dropmenu5" class="dropmenudiv"></div>
				<div id="dropmenu7" class="dropmenudiv"></div>
				<div id="dropmenu8" class="dropmenudiv"></div>
				<div id="dropmenu9" class="dropmenudiv"></div>
				<div id="dropmenu10" class="dropmenudiv"></div>
				<div id="dropmenu11" class="dropmenudiv"></div>
				<div id="dropmenu12" class="dropmenudiv"></div>
				<div id="dropmenu13" class="dropmenudiv"></div>
				<div id="dropmenu14" class="dropmenudiv"></div>
				<div id="dropmenu15" class="dropmenudiv"></div>
				<div id="dropmenu16" class="dropmenudiv"></div>
				<!-- <div id="dropmenu15" class="dropmenudiv" ></div>  -->
			</div>
		</div>
		<script type="text/javascript">
		$(document).ready(function(){
			commonApp.init($);
		}); 
			parent.frames[0].getMenu();
			cssdropdown.startchrome("chromemenu")
			BrowserDetect.init();
			parent.frames[0].firstCallFunc();
			var existingOnload = null;
			if (window.onload) {
				existingOnload = window.onload;
			}
			window.onload = function(ev) {
				if (existingOnload) {
					existingOnload(ev);
					initDojo.startDojo(ev);
					//checkCache();
				} else {
					initDojo.startDojo(ev);
					//checkCache();
				}
			};
		</script>
	</body>