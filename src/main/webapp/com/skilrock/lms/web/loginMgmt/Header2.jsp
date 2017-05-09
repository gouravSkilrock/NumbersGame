<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*,com.skilrock.lms.beans.PriviledgeBean"%>
<%@page import="com.skilrock.lms.common.utility.CommonMethods"%>
<%@page import="com.skilrock.lms.common.filter.LMSFilterDispatcher"%>
<%@page import="com.skilrock.lms.common.Utility"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
			<link href="https://fonts.googleapis.com/css?family=Roboto:400,300,500,700" rel="stylesheet" type="text/css"/>
			<link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700" rel="stylesheet" type="text/css"/>
			<link href="https://netdna.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.css" rel="stylesheet"/>
			<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/draw-ui-stylesheet.css" type="text/css" />
			<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/new-changes-header.css" type="text/css" />
			<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE' || #application.COUNTRY_DEPLOYED=='SAFARIBET'}">
				<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/theme-header-zim.css" media="screen" />
				<style type="text/css">
				.logo {margin-top: 0px !important;}
				</style>
			</s:if>			
		<script>
			var projectName="<%=request.getContextPath()%>";
			var sesId = '<%=session.getId()%>';
			var offset = <%=TimeZone.getDefault().getRawOffset()%>;
			var lmstierMap = '<%=application.getAttribute("TIER_MAP")%>';
			var isDraw =<%=LMSFilterDispatcher.getIsDraw().equalsIgnoreCase("YES")%>;
			var idDGActive =<%=LMSFilterDispatcher.isDGActiveAtRetWeb%>;
			var country = '<%=application.getAttribute("COUNTRY_DEPLOYED")%>';
			var cookie = document.cookie;
			var lang = '<%=Locale.getDefault().getLanguage()%>';
			if(cookie!=null){
				var cookieArray = cookie.split('; ');
				for(var loop=0; loop < cookieArray.length; loop++)
				{
					var nameValue = cookieArray[loop].split('=');
					if( nameValue[0].toString() == 'LMSLocale' ){
						lang = nameValue[1].toString();
					}
				}
			}
			var fileName="Messages_"+country;
			var path="<%=request.getContextPath()%>";
		</script>
	
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>

		<script type="text/javascript"	src="<%=request.getContextPath()%>/js/jquery.i18n.properties-min-1.0.9.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/i18nLoader.js"></script>
	
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/js/Header.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/js/menu.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/validator.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/ajaxDojo.js" >
		</script>

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/ajaxCall.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/js/respond.js"></script>

		<script>
				$(document).ready(function() {
					$("#inboxEmpty").hide();
					$("#inboxWithMsg").hide();
					var _resp = _ajaxCallText(projectName + "/com/skilrock/lms/web/userMgmt/bo_um_fetchWebMessages.action?messages=[{\"status\":\"UNREAD\"}]&messageNumber=0");
					json = (JSON.parse(_resp)).responseData;
					var noOfMessages = json.noOfMessages;
					if(noOfMessages > 0) {
						$("#inboxEmpty").hide();
						$("#inboxWithMsg").show();
						$("#dispNoOfMsg").html(noOfMessages);					
					} else {
						$("#inboxEmpty").show();
						$("#inboxWithMsg").hide();
					}
					$(".logout").hover(function() {
       					 $(this).css('cursor','pointer').attr('title', 'Logout');
    					}, function() {
       					 $(this).css('cursor','auto');
   					});
   					$(".new-tab").hover(function() {
       					 $(this).css('cursor','pointer').attr('title', 'New Tab');
    					}, function() {
       					 $(this).css('cursor','auto');
   					});
   					$(".message-tab").hover(function() {
       					 $(this).css('cursor','pointer').attr('title', 'Message');
    					}, function() {
       					 $(this).css('cursor','auto');
   					});
   					$("#user-pro").click(function(){
   						$(".bl-info").css("display","block");
   						$(".bl-info").delay(10000).fadeOut("slow");
   					});
				});	
		</script>
<!--[if lt IE 9]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
	</head>

	<body>
	
	<div id='headerDiv' style="position: fixed; width: 100%; height: 100%; padding: 0px; margin: 0px; top: 0px; left: 0px;z-index: 9999;">

	<div class="header">
	<div class="logo">
    	
    	<s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
					<s:if test="%{ #session.USER_INFO.parentOrgName.contains('Africa Bet')}">
						<img
							src="${pageContext.request.contextPath}/LMSImages/images/africaBet_black.png"
							alt="logo" width="183" height="70" />
					</s:if>
					<s:else>
						<img
							src="${pageContext.request.contextPath}/LMSImages/images/header_logo_africa.png"
							alt="logo" />
					</s:else>
				</s:if>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='SAFARIBET'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/safari_bet_header_ret_logo.png"
							alt="logo" />
                     </s:elseif>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/header_logo_ghana.gif"
							alt="logo" width="183" height="60"/>
					</s:elseif>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/header_logo_lagos.gif"
							alt="logo" width="183" height="60"/>
                     </s:elseif>
                     <s:elseif test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/lnb_header-01.jpg"
							alt="logo" width="183" height="60"/>
					</s:elseif>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='SIKKIM'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/Rajshree_Logo.png"
							alt="logo" id="sikkimLogoImg" />
					</s:elseif>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='PHILIP'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/header_logo_philip.gif"
							alt="logo" width="183" height="60"/>
					</s:elseif>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='INDIA'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/olamskhlplaylogo.gif"
							alt="logo" width="183" height="60"/>
					</s:elseif>
					
    </div>
    
    <nav>
    	<div id="services">
									</div>
    </nav>
    <div class="head-right-side">
		    <s:if test="%{#application.COUNTRY_DEPLOYED!='SAFARIBET'}">
				<div class="message-tab" id="showBetSlip" onclick="onBetShowClick()">
    				<img src="${pageContext.request.contextPath}/LMSImages/images-UI/eBetFlipIcon.png" class="ebat-icon-zim"/>
    			</div>
		    </s:if>
     <div class="my-profile" onclick="showBalance()">
        	<div class="user-name-info active" id="user-pro" onclick="updateBalance('<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action')">
        		<div class="user-icon"><img src="${pageContext.request.contextPath}/LMSImages/images/default_user-icon.png" width="40" height="40" alt=""/></div>
					<div class="user-am">
						<span class="click-user"><label id="currentLoggedUser"></label> <input
							type="hidden" id="currentLoggedUserMoz" value="" /> </span>
						<div class="bl-info">
						<div class="amount-bl" id="balLimitCredit"
							onclick="updateBalance('<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action')">
						</div>
						</div>
					</div>
					<script>ajaxReq("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/fetchNamenLimit.action","fillUserDetails")</script>
			</div>
        </div>
        <div class="message-tab">
        
											<a id="inboxEmpty"
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_viewWebMessages.action?request_locale=<%=session.getAttribute("WW_TRANS_I18N_LOCALE") %>"
												target="leftbottom" name="tab" class="white"
												style="target-new: tab ! important" > <img src="${pageContext.request.contextPath}/LMSImages/images-UI/message-icon.png" width="26" height="20" class="icon-skm"/>
												<img src="${pageContext.request.contextPath}/LMSImages/images-UI/message-icon-zim.png" width="26" height="20" class="icon-zim"/>
												</a>
											<a id="inboxWithMsg"
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_viewWebMessages.action?request_locale=<%=session.getAttribute("WW_TRANS_I18N_LOCALE") %>"
												target="leftbottom" name="tab" class="white"
												style="target-new: tab ! important" > <img src="${pageContext.request.contextPath}/LMSImages/images-UI/message-icon.png" width="26" height="20" class="icon-skm"/>
												<img src="${pageContext.request.contextPath}/LMSImages/images-UI/message-icon-zim.png" width="26" height="20" class="icon-zim"/>
												<span id="dispNoOfMsg" class="new-msg"></span></a> 
        
       
      </div>
        <div class="new-tab">
        
        	<s:if test="%{#application.COUNTRY_DEPLOYED eq 'NIGERIA' && #session.USER_INFO.userType eq 'AGENT'}"> 
											</s:if>
											<s:else>
									
											<a
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/LoginSuccessRet.action?request_locale=<%=session.getAttribute("WW_TRANS_I18N_LOCALE") %>"
												target="_blank" name="tab" class="white"
												style="target-new: tab ! important"
												onclick="if(!event.ctrlKey&&!window.opera){alert('<s:text name="error.pls.try.again.hold.down.ctrl"/>');return false;}else{return true;}"><img src="${pageContext.request.contextPath}/LMSImages/images-UI/new-tab.png" width="21" height="20" class="icon-skm"/>
												<img src="${pageContext.request.contextPath}/LMSImages/images-UI/new-tab-zim.png" width="21" height="20" class="icon-zim"/></a>
											</s:else>
        	
        	
        	
        	
      </div>
      <div class="change-pass">
				<a
					href="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/changePassword.action?request_locale=<%=session.getAttribute("WW_TRANS_I18N_LOCALE")%>"
					target="leftbottom" onclick=" return getCookie()" class="white" title="Change Password">
					 <img src="${pageContext.request.contextPath}/LMSImages/images-UI/changePasswordIcon.png" width="23" height="23" class="icon-skm"/>
					 <img src="${pageContext.request.contextPath}/LMSImages/images-UI/changePassword-zim.png" width="23" height="23" class="icon-zim"/>
				</a>
			</div>
        <div class="logout">
        	
        	
        	<a href="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/Logout.action" target="_top" onclick=" return getCookie()" class="white"><img src="${pageContext.request.contextPath}/LMSImages/images-UI/logout.png" width="27" height="20" class="icon-skm"/>
        	<img src="${pageContext.request.contextPath}/LMSImages/images-UI/logout-zim.png" width="27" height="20" class="icon-zim"/></a>
        </div>
			
		</div>
    
		<input type="hidden" value="<%=application.getAttribute("IS_DRAW")%>"
			id="is_draw" name="D_R" />
		<input type="hidden"
			value="<%=application.getAttribute("IS_SCRATCH")%>" id="is_scratch"
			name="S_R" />
		<div id="header_links">
			<label
				style="position: absolute; top: 0; left: 340px; text-align: center;"
				id="startDivLabel">
				<b><s:text name="msg.pls.wait.while.page.load"/>...</b>
			</label>
			<label
				style="position: absolute; display: none; top: 0; left: 340px; text-align: center;"
				id="refreshDivLabel">
				<b><s:text name="msg.pls.wait.while.page.load"/>...</b>
			</label>
			<div id="startDiv"
				style="width: 1000px; position: absolute; filter: alpha(opacity = 0); height: 50px;"></div>
			<div id="currencyWord" style="display: none;">
				<s:property value="%{#application.CURRENCY_SYMBOL}" />
			</div>
		</div>
		<div style="width: 995px; height: auto; margin: 0 auto; display: none">
			<table width="995px" cellpadding="0" border="0" cellspacing="0"
				align="left" style="margin-right: 17px">
				<tr>
					<td align="left">
					
				     <s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
			          <img
							src="${pageContext.request.contextPath}/LMSImages/images/header_logo_africa.gif"
							alt="logo" />
					</s:if>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/header_logo_ghana.gif"
							alt="logo" />
					</s:elseif>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='SAFARIBET'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/header_logo_safaribet.png"
							alt="logo" />
                     </s:elseif>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='NIGERIA'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/header_logo_lagos.gif"
							alt="logo" />
                     </s:elseif>
                     <s:elseif test="%{#application.COUNTRY_DEPLOYED=='BENIN'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/lnb_header-01.jpg"
							alt="logo" />
					</s:elseif>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='SIKKIM'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/lnb_header-01.jpg"
							alt="logo" />
					</s:elseif>
					<s:elseif test="%{#application.COUNTRY_DEPLOYED=='PHILIP'}">
					 <img
							src="${pageContext.request.contextPath}/LMSImages/images/header_logo_philip.gif"
							alt="logo" />
					</s:elseif>
					</td>
					<td width=" 77%" align="center">
					
						<table width="100%">
							<tr style="background-color: #efba01">
								<td>
									<div id="box1">
										<p>
											<label style="color: #585858;">
											<s:text name="label.welcome"/> :
											</label>
											<label id="currentLoggedUser"></label>
											<input type="hidden" id="currentLoggedUserMoz" value="" />
											<script>ajaxReq("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/fetchNamenLimit.action","fillUserDetails")</script>

										</p>
										<p>
											<label id="balLimitDiv"
												style="visibility: hidden; color: #585858">
												<s:text name="label.blnce"/> :
											</label>
											<a href="#" id="balLimitCredit"
												style="cursor: hand; color: white;"
												onclick="updateBalance('<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action')">
											</a>
										</p>
									</div>
									<div id="box2" style="width: 440px; height: 35px">
										<p>
											<span id="login"></span>
											<span class="white">l</span>
											<!--<input type="hidden" id="noOfMsg" value="0" />-->

											<a id="inboxEmpty"
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_viewWebMessages.action?request_locale=<%=session.getAttribute("WW_TRANS_I18N_LOCALE") %>"
												target="leftbottom" name="tab" class="white"
												style="target-new: tab ! important"> <img
													src="<%=request.getContextPath()%>/LMSImages/images/inbox.gif"
													height=15px width=15px style="margin-bottom: -3px"></img><s:text name="label.inbox"/></a>
											<a id="inboxWithMsg"
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/bo_um_viewWebMessages.action?request_locale=<%=session.getAttribute("WW_TRANS_I18N_LOCALE") %>"
												target="leftbottom" name="tab" class="white"
												style="target-new: tab ! important"> <img
													src="<%=request.getContextPath()%>/LMSImages/images/icon-link-inbox.gif"
													height=15px width=18px style="margin-bottom: -2px"></img>
												<s:text name="label.inbox"/>(<span id="dispNoOfMsg"></span>)</a> 
											<s:if test="%{#application.COUNTRY_DEPLOYED eq 'NIGERIA' && #session.USER_INFO.userType eq 'AGENT'}"> 
											</s:if>
											<s:else>
											<span class="white">l</span>
											<a
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/LoginSuccess.action?request_locale=<%=session.getAttribute("WW_TRANS_I18N_LOCALE") %>"
												target="_blank" name="tab" class="white"
												style="target-new: tab ! important"
												onclick="if(!event.ctrlKey&&!window.opera){alert('<s:text name="error.pls.try.again.hold.down.ctrl"/>');return false;}else{return true;}"><s:text name="label.new.tab"/></a>
											</s:else>
											<span class="white">l</span>
											<a
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/Logout.action"
												target="_top" onclick=" return getCookie()" class="white"><s:text name="label.logout"/></a>
											<span class="white">l</span>
											<a
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/changePassword.action?request_locale=<%=session.getAttribute("WW_TRANS_I18N_LOCALE") %>"
												target="leftbottom" onclick=" return getCookie()"
												class="white"><s:text name="label.change.pass"/></a>
										</p>
										<p>
											<a href="#" id="clmLink" class="white"
												style="cursor: hand; visibility: hidden; font-weight: bold;"
												onclick="updateBalance('<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/updateClmBal.action', true)">
												<span style="color: #585858"> <!--  Update Ledger l-->
											</span> </a>
											<label id="limitDivUNClm"
												style="font-weight: bold; color: #585858">
												<!--  UnClaimable Balance :-->
											</label>
											<a
												href="<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/fetchUnClmPwtDetail.action"
												style="visibility: hidden;" target="leftbottom"
												class="white" id="avlCreditUNClm"> </a>
										</p>
									</div>
									<s:if test="%{#session.USER_INFO.userType eq 'BO'}">
										<div id="subscription_div" style="width:100%; height:35px; ">
											<s:if test="%{new com.skilrock.lms.common.Utility().getPropertyFromDB('IS_GRACE_PERIOD') eq 'YES' and new com.skilrock.lms.common.Utility().getPropertyFromDB('IS_LS_ENABLE') eq 'YES'}">
												<%
													UserInfoBean userBean = (UserInfoBean) session.getAttribute("USER_INFO");

													String marqueUser = Utility.getPropertyFromDB("LS_MARQUE_USERS").replaceAll(" ", "");
													List<String> users = Arrays.asList(marqueUser.split(","));
													if(users.contains(userBean.getUserName().trim())) {
												%>
														<p style="color:blue; background-color:#FFBF00; margin-bottom:0%; "><marquee><s:property value="%{new com.skilrock.lms.common.Utility().getPropertyFromDB('SUBSCRIPTION_EXPIRED_MESSAGE')}" /></marquee></p>
												<%
													}
												%>
											</s:if>
										</div>
									</s:if>
								</td>
							</tr>
							<tr>
								<td>
									<p
										style="color: #000000; font-family: Arial; font-size: 12px; float: right;">
										<label id="ExtLimitDiv" style="visibility: hidden"
											title="Extended Credit Limit">
											<s:text name="XCL"/> :
										</label>
										<a href="#" id="ExtLimit" title="Extended Credit Limit"
											style="cursor: hand; color: black"
											onclick="updateBalance('<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action')">
										</a>
										<label id="RemainingDaysDiv" style="visibility: hidden"
											title="No. of Days Remaining">
											| <s:text name="label.rem.days"/> :
										</label>
										<a href="#" title="No. of Days Remaining" id="Remain_Days"
											style="cursor: hand; color: black"
											onclick="updateBalance('<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action')">
										</a>
										<label id="pipe" style="visibility: hidden">
											|
										</label>
										<label id="CredLimitDiv" style="visibility: hidden"
											title="Credit Limit">
											<s:text name="CL"/> :
										</label>
										<a href="#" id="CredLimit" title="Credit Limit"
											style="cursor: hand; color: black"
											onclick="updateBalance('<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action')">
										</a>
									</p>
								</td>
							</tr>
							<!--<tr>
								<td>
									<p
										style="color: #000000; font-family: Arial; font-size: 12px; float: right;">
										
									</p>
								</td>
							</tr>-->
							<tr>
								<td>
									<div id="services">
									</div>
								</td>
							</tr>
						</table>
						
					</td>
					<%if(CommonMethods.getArchDate()==null){%>
					<td align="center">
						<img
							src="<%=request.getContextPath()%>/LMSImages/images/LmsLogo.gif"
							alt="LMS" style="margin-right: 5px;" />
						<%=(String) application
									.getAttribute("VERSION_DETAILS")%>
					</td>
					<%}else{ %>
					<td align="center">
						<img
							src="<%=request.getContextPath()%>/LMSImages/images/LmsLogo.gif"
							alt="LMS" style="margin-right: 5px;" />
						<%=(String) application
									.getAttribute("VERSION_DETAILS")%>
						<br />
						<s:text name="label.arch.date"/>
						<%=CommonMethods.getArchDate()%>
					</td>

					<%} %>
				</tr>
			</table>
		</div>
		<div id="link_tabs">
			<label id="limitDiv" style="visibility: hidden">
				<s:text name="label.avail.credit"/> :
			</label>
			<b> <a href="#" id="avlCredit" style="cursor: hand;"
				onclick="updateBalance('<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action')">
					<label id="avlCreditClm" style="visibility: hidden"></label> </a> </b>
			<label id="limitDivClm">
				| <s:text name="label.claim.bal"/> :
			</label>
			<b> <a href="#" style="cursor: hand;"
				onclick="updateBalance('<%=request.getContextPath()%>/com/skilrock/lms/web/userMgmt/updateClmBal.action', true)">
					<label id="avlCreditClm" style="visibility: hidden"></label> </a> </b>
		</div>

		<div id="menuHeader" style="display: none">
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
			<div id="dropmenu14" class="dropmenudiv"></div>
			<div id="dropmenu15" class="dropmenudiv"></div>
			<div id="dropmenu16" class="dropmenudiv"></div>
			<!-- <div id="dropmenu15" class="dropmenudiv"></div> -->
		</div>
</div>
</div>
	</body>
</html>
<script>BrowserDetect.init();
function dgClick(){
if(ownerType=='RETAILER'){
createServiceMenu("dg");
parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/rpos.action?request_locale="+lang+"");
}else{
createServiceMenu("dg");
parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action?request_locale="+lang+"");
}
}
function seClick(){
if(ownerType=='RETAILER'){
createServiceMenu("se");
<%-- parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/pwtMgmt/retailer/RetailerPWTHome.jsp"); --%>
parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/scratchPOS.jsp");
}else{
createServiceMenu("se");
parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action");
}
}

function csClick(){
	//alert('hello' + ownerType);
	if(ownerType=='RETAILER'){
		//alert('helloddddd');
		createServiceMenu("cs");
		parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/commercialService/playMgmt/ret_cs_sale_Menu.action");
	}else{
		//alert('hellodddddddddddddd');
		createServiceMenu("cs");
		parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action");
	}
}
function olaClick(){
	if(ownerType=='RETAILER'){
		createServiceMenu("ola");
		parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/ola/userMgmt/retailer/olaPlayerPOS.jsp");
	}else{
		createServiceMenu("ola");
		parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action");
	}
}

var isopen="false";
var myWindow; 
function vsClick(){
	checkWindowCLosed();
	if(isopen=="false"){
		 isopen="true";
		 if(ownerType=='RETAILER'){
				       //createServiceMenu("vs");
			      	  <%-- parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/openVSPage.action?userType=RETAILER"); --%>		
		 		      myWindow = window.open("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/openVSPage.action?userType=RETAILER",'_blank',"width=1500, height=1000,toolbar=0,location=0, directories=0, status=0,location=no,menubar=0");
		 }
	}         
}
window.onbeforeunload = function(){
	if(myWindow != undefined)
       myWindow.close();
};
function checkWindowCLosed(){
  if(myWindow && myWindow.closed){
     isopen="false";
  }
}
function sleClick(){
	//createServiceMenu("sle");
	if(ownerType=='RETAILER'){
		parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/openSLEPage.action?userType=RETAILER");
	} else{
		parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/openSLEPage.action?userType=BO");
	}	
	
	serviceName = "sle";
	if(currentService!=""){
		document.getElementById(currentService).className="tabnew";
	}
	document.getElementById(serviceName).className="tabnew tabnewClick";
	currentService = serviceName;
}

function iwClick(){
	if(ownerType=='RETAILER'){
		createServiceMenu("iw");
		parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/instantPrint/instantWinPos.jsp");
	}else{
		createServiceMenu("iw");
		parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/loginMgmt/mainPageDisplay.action?request_locale="+lang+"");
	}
}

function showBalance() {
	if ($(".balance-view").hasClass("click-bln")) {
		$(".balance-view").removeClass("click-bln");
	} else {
		$(".balance-view").addClass("click-bln");
	}
}

function onBetShowClick() {
	parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/eBet.jsp");
}

</script>

<code id="headId" style="visibility: hidden">
	$RCSfile: Header2.jsp,v $ $Revision: 1.3 $
</code>