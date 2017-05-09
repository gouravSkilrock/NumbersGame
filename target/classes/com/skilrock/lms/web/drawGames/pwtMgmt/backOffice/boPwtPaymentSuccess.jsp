<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title>
			<%
				application.getAttribute("JSP_PAGE_TITLE");
			%>
		</title>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<h3>
			<s:text name="msg.win.pay.has.done.success" />
		</h3>
		<table border="1" cellpadding="2" cellspacing="0" width="300px">
			<tr>
				<td>
					<s:text name="label.rec.nbr" /> :
					<a
						href="<%=request.getContextPath()%>/com/skilrock/lms/web/reportsMgmt/rep_common_openPDF.action"
						target="_blank"> <s:property
							value="%{#session.generatedReceiptNumber}" /> </a>
				</td>
			</tr>
			<tr>
				<td>
					<s:text name="label.show.rec" />
				</td>
			</tr>

		</table><p><p>
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
		%>
		<div id="loadingDiv" style="color: red;">
			Loading Please Wait...
		</div>
		
		<s:iterator value="#session.plrPwtAppDetMap">
			<s:if test="%{key=='GAME_NAME'}">
				<s:set name="gameType" value="value"></s:set>
			</s:if>
		</s:iterator>
		<div id="pwtResultDiv" style="display: none">
		</div>
		<script>
		var verifiedAt = '<s:property value="#session.USER_INFO.orgName"/>';
		var sampleStatus = '<s:property value="#application.SAMPLE"/>';
					
       	function verifyPwt() {
       		document.applets[0].repaint();
			var url = '<s:property value="gameType"/>.action?'+new Date().getTime()+Math.random()*1000000;
			_id.i("pwtResultDiv", "<iframe src=\"" + url + "\" name=\"tktFrame\" id=\"tktFrame\" style=\"width:230px;height:300px;\" frameborder=\"0\" ></iframe>");
		}
				
		//setTimeout("verifyPwt()",3000);
		setTimeout("disableDiv()",0);
		
		function updBalIframe() {
			parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
		}
				
		function cancelTktAuto(tktNum, totAmt, printStatus) {
			if(printStatus == 'FAIL'){
				alert(i18Obj.prop('error.printer.not.connected'));
			}
			
			return false;
		}	
	
		function disableDiv(){
			_id.non("loadingDiv");
		}
       </script>

	</body>

</html>

<code id="headId" style="visibility: hidden">
$RCSfile: boPwtPaymentSuccess.jsp,v $
$Revision: 1.3 $
</code>