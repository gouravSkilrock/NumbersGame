<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="java.util.ResourceBundle"%>
<%@page import="java.util.Locale"%>
<!--<//%@page import="com.skilrock.lms.web.loginMgmt.I18Util"%>-->
<%@taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.util.Map"%>
<%@page import="org.apache.struts2.ServletActionContext"%>
<%
	//response.setCharacterEncoding("UTF-8");
	ServletContext sc = ServletActionContext.getServletContext();
	Map errorSessionMap = (Map) sc.getAttribute("ERROR_SESSION_MAP");
	if (errorSessionMap != null && errorSessionMap.isEmpty()) {
		response.setDateHeader("Expires",0);
	} else {
		response.setDateHeader("Expires", 1000);
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

<link
	href="https://fonts.googleapis.com/css?family=Roboto:400,300,500,700"
	rel="stylesheet" type="text/css">
<link
	href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700"
	rel="stylesheet" type="text/css">
<link
	href="https://netdna.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.css"
	rel="stylesheet">
<s:if test="%{#application.COUNTRY_DEPLOYED=='SAFARIBET'}">
	<link rel="stylesheet"
			href="<%=request.getContextPath()%>/css/safaribetloginscreen.css"
			"type="text/css" />
</s:if>
<s:else>
	<link rel="stylesheet"
	href="<%=request.getContextPath() %>/css/login-screen-css.css">
</s:else>	

<script>
var presentProjectName = "<%=request.getContextPath()%>";
var host = "<%=application.getAttribute("HOST")%>";
var port = "<%=application.getAttribute("PORT")%>";
var projectName = "<%=request.getContextPath()%>";
var country = '<%=application.getAttribute("COUNTRY_DEPLOYED")%>';
var cookie = document.cookie;
var lang = '<%=Locale.getDefault().getLanguage()%>';
	if (cookie != null) {
		var cookieArray = cookie.split('; ');
		for ( var loop = 0; loop < cookieArray.length; loop++) {
			var nameValue = cookieArray[loop].split('=');
			if (nameValue[0].toString() == 'LMSLocale') {
				lang = nameValue[1].toString();
			}
		}
	}
	var fileName = "Messages_" + country;
</script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/jquery.i18n.properties-min-1.0.9.js"></script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/js/i18nLoader.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/js/login.js"></script>
<script type="text/javascript">
window.history.forward();
function chngcolor(id){

document.getElementById(id).bgcolor="red";
}
</script>


</head>

<body onload="initialFocus(); detectBrowser()">
	<s:url id="indexEN" namespace="/com/skilrock/lms/web/loginMgmt"
		action="locale">
		<s:param name="request_locale">en</s:param>
	</s:url>
	<s:url id="indexFR" namespace="/com/skilrock/lms/web/loginMgmt"
		action="locale">
		<s:param name="request_locale">fr</s:param>
	</s:url>

	<div class="screen-body" id="container">
		<%
			StringBuffer codebaseBuffer = new StringBuffer();
			codebaseBuffer.append(!request.isSecure() ? "http://" : "https://");
			codebaseBuffer.append(request.getServerName());
			if (request.getServerPort() != (!request.isSecure() ? 80 : 443)) {
				codebaseBuffer.append(':');
				codebaseBuffer.append(request.getServerPort());
			}
			codebaseBuffer.append(request.getContextPath());
			codebaseBuffer.append('/');
			System.out.println(codebaseBuffer.toString());
		%>

		<div class="login-box">
			<div class="logo-box">

				<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
					<img
						src="${pageContext.request.contextPath}/LMSImages/images/AfricaLottoLMS.gif"
						width="160" alt="" />
				</s:if>
				<s:elseif test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
					<img
						src="${pageContext.request.contextPath}/LMSImages/images/LoginLMS_Ghana.gif"
						width="160" alt="" />
				</s:elseif>
				<s:elseif test="%{#application.COUNTRY_DEPLOYED=='SAFARIBET'}">
					<img
						src="${pageContext.request.contextPath}/LMSImages/images/header_logo_safaribet.png"
						width="160" alt="" />
				</s:elseif>
				<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
					<img
						src="${pageContext.request.contextPath}/LMSImages/images/LagosLogoLogin.gif"
						width="160" alt="" />
				</s:elseif>
				<s:elseif test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
					<img
						src="${pageContext.request.contextPath}/LMSImages/images/BeninLogoLogin.jpg"
						width="160" alt="" />
				</s:elseif>
				<s:elseif test="%{#application.COUNTRY_DEPLOYED=='SIKKIM'}">
					<img
						src="${pageContext.request.contextPath}/LMSImages/images/SikkimLogoLogin.png"
						width="160" alt="" />
				</s:elseif>
				<s:elseif test="%{#application.COUNTRY_DEPLOYED=='PHILIP'}">
					<img
						src="${pageContext.request.contextPath}/LMSImages/images/banner_philip.gif"
						width="160" alt="" />
				</s:elseif>
				<s:elseif test="%{#application.COUNTRY_DEPLOYED=='INDIA'}">
					<img
						src="${pageContext.request.contextPath}/LMSImages/images/khelplay_lms_header.gif"
						width="437" alt="" />
				</s:elseif>
			</div>
			<div class="login-area">
				<form onsubmit="return isST3Filled()" name="login" 
					action="<%=request.getContextPath()%>/LoginServlet" method="post"
					class="login-form">
					<input type="hidden" name="oldHttpSess"
						value="<%=request.getParameter("oldHttp")%>" /> <input
						type="hidden" name="macId" id="macId" value="" /> <input
						type="text" name="browser" id="browser" value=""
						style="display: none"  />

					<div class="form-group-list">
						<div class="form-group">
							<label class="label-control"> <span class="label-text">Username</span>
							</label> <input type="text" name="username" id="username"
								onfocus="chngcolor(this.id)" size="20" class="form-control" />
						</div>
						<div class="error-div">
							<span class="error-span" id="user"></span>
						</div>
					</div>
					<div class="form-group-list">
						<div class="form-group">
							<label class="label-control"> <span class="label-text">Password</span>
							</label> <input type="password" name="password" id="password"
								onfocus="chngcolor(this.id)" size="20"
								onkeypress="capLock(event)" class="form-control"  />

						</div>
						<div class="error-div">
							<span class="error-span" id="pass"></span> <span
								class="error-caps" id="caps"></span>
						</div>
						<div class="full-err">
							<%
								if (errorSessionMap != null && errorSessionMap.containsKey(request.getParameter("oldHttp"))) {
									if (request.getParameter("oldHttp") != null) {
										String errorMessage = (String) errorSessionMap.get(request.getParameter("oldHttp"));
										errorSessionMap.remove(request.getParameter("oldHttp"));
										sc.setAttribute("ERROR_SESSION_MAP", errorSessionMap);
							%>
							<div class="inCorrect-login">
								<span class="errorMessage"><%=errorMessage%></span>
							</div>
							<%
									}
								}
							%>
							<s:actionerror theme="simple"
								cssStyle="font-family:Verdana; color:#0000FF; font-size:11px"
								cssClass="welcomeNote" />
							<s:fielderror cssClass="welcomeNote" />
						</div>
					</div>

					<div>
						<s:if test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
							<div>
								<div align="left" style="font-size: 13px">
									<b style="margin-left: 11em"> <s:text name="Language:" />
									</b> &nbsp;&nbsp;&nbsp;&nbsp;
									<s:a href="%{indexEN}">English</s:a>
									&nbsp;&nbsp;&nbsp;&nbsp;
									<s:a href="%{indexFR}">français</s:a>
								</div>
							</div>
						</s:if>
					</div>
					<div class="login-btn">
						<input type="submit" value="Login"
							onclick="this.disabled=disabled" class="btn"
							value=<s:text name="btn.login"/> />
					</div>

					<div id="browserErr" class="errorMessage" style="display: none;">
						<s:text name="label.pls.login.using.ie" />
						6.0, 7.0
					</div>

				</form>
			</div>
			<div class="login-footer-area">
				<div class="powered-by">
					<span>Powered By</span>
				</div>
				<div class="footer-logos">
					<div class="left-footer">
						<div class="lms-logo">
							<img
								src="<%=request.getContextPath()%>/LMSImages/images-UI/powered-by-lms.png"
								alt="">
						</div>
						<p>&copy; 2008 - Sugal &amp; Damani</p>
						<div></div>
					</div>
					<div class="right-footer">
						<div class="snd-logo">
							<img
								src="<%=request.getContextPath()%>/LMSImages/images-UI/powered-by-sd.png"
								alt="">
						</div>
						<p>
							<%
						if (CommonMethods.getArchDate() == null) {
						%>
						
						<div class="reld"><%=(String) application.getAttribute("VERSION_DETAILS")%></div>
						<%
						} else {
						%>
						<div class="reld"><%=(String) application.getAttribute("VERSION_DETAILS")%>
							<br />
							<%=CommonMethods.getArchDate()%></div>
						<%
							}
						%>
						</p>
					</div>
				</div>
			</div>
		</div>
	</div>


</body>
	<script type="text/javascript">
        $(window).load(function() {
    getLogin();
        $('.form-group input').on('focus blur', function(e) {
            $(this).parents('.form-group').toggleClass('active', (e.type === 'focus' || this.value.length > 0));
        }).trigger('blur');
    	});
    function getLogin(){
        var userName = $("#username").val();
        var password = $("#password").val();
        if(""==userName){
        	$(".form-group").addClass("active");
        }
        if(""==password){
        	$(".form-group").addClass("active");
        }
    }
    </script> 
    
	<script type="text/javascript">

BrowserDetect.init();
function detectBrowser(){
	document.getElementById('browser').value = BrowserDetect.browser;
	return true;
}

if(BrowserDetect.browser!="Explorer"){
//disableSub();
}else if((BrowserDetect.browser=="Explorer")&&!((BrowserDetect.version=="6")||(BrowserDetect.version=="7")||(BrowserDetect.version=="8"))){
		//disableSub();
}

function disableSub(){
	document.getElementById("submitLogin").disabled=true;
	alert("Please login using IE 6.0 , 7.0");
	document.getElementById("browserErr").style.display="block";
}
</script>
</html>
<code id="headId" style="visibility: hidden"> $RCSfile:
	newLogin.jsp,v $ $Revision: 1.3 $ </code>