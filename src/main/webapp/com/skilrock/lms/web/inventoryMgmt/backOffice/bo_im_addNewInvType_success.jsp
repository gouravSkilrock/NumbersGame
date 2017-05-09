<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			scrolling="no" type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>


	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>

		<div id="wrap">
			<div id="top_form">
				<h3>
					<font color="red"> <s:property value="#session.invListMsg" />
					</font>
					<s:text name="msg.added.in.db" />
				</h3>
				<s:if test="%{#session.invListMsg1!=''}">
					<h3>
						<font color="red"> <s:property value="#session.invListMsg1" />
						</font>
						<s:text name="msg.not.added.in.db.duplicate" />
					</h3>
				</s:if>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_addNewInvType_success.jsp,v $ $Revision: 1.5 $
</code>