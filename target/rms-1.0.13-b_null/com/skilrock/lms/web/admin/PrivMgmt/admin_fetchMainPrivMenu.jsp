<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>
			function getPriveledges()
			{//alert()
			  var tlbName=document.getElementById('privTlbName').value;
			  var url='admin_mainPriviledgeSearch.action?privTlbName='+tlbName;
			    // alert("url is" +url);
			    document.getElementById('lodingImage').style.display="block";
			   _resp  = _ajaxCall(url);//alert(_resp.data);
				if(_resp.result){
			        document.getElementById('lodingImage').style.display="none";
			        _id.i('result',_resp.data);
			     
			      } else {
			        alert("Problem: ");
			        }
			 
			return false;
			
			}
		</script>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Select Table Name
				</h3>
				<s:form name="mainPriviledge">
					<table border="0" cellpadding="2" cellspacing="0">
						<s:select cssClass="option" id="privTlbName" name="privTlbName"
							label="Select Table Name" list="#session.PRIV_TLB_MAP"
							headerKey="-1" headerValue="--Please Select--" onchange="getPriveledges()"></s:select>
						<tr>
							<td id="lodingImage" style="display: none; color: red;">
								Wait...
								<img
									src="<%=request.getContextPath()%>/LMSImages/images/loadingAjax.gif" />
							</td>
						</tr>
					</table>
				</s:form>
			</div>
		</div>
		<div id="result"></div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: admin_fetchMainPrivMenu.jsp,v $ $Revision: 1.2 $
</code>