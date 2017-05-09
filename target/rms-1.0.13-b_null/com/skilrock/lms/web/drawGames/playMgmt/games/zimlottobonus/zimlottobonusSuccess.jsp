<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@page import="com.skilrock.lms.common.filter.LMSFilterDispatcher"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.*,com.skilrock.lms.dge.beans.LottoPurchaseBean,com.skilrock.lms.web.drawGames.common.Util"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<script src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/commonRPOS.js"></script>
	<body>
		<s:hidden id="purchaseData" value="%{purchaseData}"></s:hidden>
		<s:if test="purchaseData != 'ERROR'">
			<div class="center">
				<script>setAppData(document.getElementById('purchaseData').value);</script>
			</div>
		</s:if>
		<s:else>
			<table>
				<tr>
					<td>
						<b>Error!Try Again</b>
					</td>
				</tr>
			</table>
		<script>errMsg = "Error!Try Again";	parent.displayErrMsg();</script>
		</s:else>
	</body>
</html>
<script>parent.updBalIframe();</script>
<code id="headId" style="visibility: hidden">
	$RCSfile: zimlottobonusSuccess.jsp,v $ $Revision: 1.3 $
</code>