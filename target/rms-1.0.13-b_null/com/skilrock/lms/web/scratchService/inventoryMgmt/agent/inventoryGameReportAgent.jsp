<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
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
	function validateGameNumber()
	{
	var num=document.getElementById("gameNumber").value;
	if(isNaN(num)){			
		document.getElementById("gameNumber1").innerHTML = "<font color=red>Please enter a numeric value</font>";
		return false;
		}else {
		
		if(num.indexOf(".")!=-1){
		document.getElementById("gameNumber1").innerHTML = "<font color=red>Please enter a numeric value</font>";
		return false;
		}else{
		document.getElementById("gameNumber1").innerHTML = "";
		return true;}
		}
	}
	</script>

		<s:head theme="ajax" debug="false" />
	</head>

	<body>


		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>
					Search Inventory
				</h3>

				<s:form name="searchgame" action="inventorySearchReportAgent"
					onsubmit="return validateGameNumber()">
					<table border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td>
								<s:textfield name="gamename" value="" label="Game Name"
									size="20" />
							</td>
						</tr>
						<tr>
							<td>
								<div id="gameNumber1"></div>
								<s:textfield name="gamenumber" value="" label="Game Number"
									id="gameNumber" size="20" />
							</td>
						</tr>

						<tr>
							<td>
								<s:select name="gamestatus" label="Game Status"
									cssClass="option"
									list="{'ALL','OPEN','CLOSE','TERMINATE','SALE_HOLD','SALE_CLOSE'}" />
							</td>
						</tr>
		

						<tr>
							<td>
								<s:submit name="search" value="Search" align="right"
									cssClass="button" targets="down" theme="ajax" />
							</td>
						</tr>

					</table>
				</s:form>
			</div>
		</div>


		<div id="down">

		</div>


	</body>
</html><code id="headId" style="visibility: hidden">$Id: inventoryGameReportAgent.jsp,v 1.1 2010/04/02 12:12:08 gaurav Exp $</code>