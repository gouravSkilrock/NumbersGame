<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
		<script>
			function isFilled()
			{
				var correctLen=true;
				var tktnbr=_id.o('tkt_no').value.length;
				if(tktnbr==16 || tktnbr==18 || tktnbr==20){
				correctLen=true;
				_id.o('tktErr').innerHTML = "";
				_id.o('down').innerHTML = "";
				}
				else{
				correctLen=false;
				_id.o('down').innerHTML = "";
				_id.i('tktErr','<s:text name="error.enter.correct.tkt.nbr" />',this);
				}
				return correctLen;
			}
			function checkNum(obj) {
				var e = event || evt;
				var _cC = e.which || e.keyCode;
				obj.style.color = "";
				if ((_cC >= 48 && _cC < 58) || _cC == 45 || _cC == 40 || _cC == 38 || (_cC >= 96 && _cC < 106 || _cC == 46 || _cC == 8 || _cC == 9 || _cC == 37 || _cC == 39)) {
					return true;
				}
				return false;
			}
	
			function moveFocusOnSubmit(value){
				if(value.length == 20){
					_id.o('search').focus();
				}
				return true;
			}
		</script>
	</head>

	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

				<h3>
					<s:text name="label.track.tkt" />
				</h3>


				<s:form name="form" name="trackTkt" onsubmit="return isFilled();">

					<table border="0" cellpadding="2" cellspacing="0" width="400">
						<tr>
							<td>
								<div id="tktErr"></div>
							</td>
						</tr>
						<tr>
							<td>
								<s:textfield name="ticketNum" maxlength="20" id="tkt_no"
									key="label.entr.tckt.numbr" onkeypress="return checkNum(this);"
									onkeyup="return moveFocusOnSubmit(this.value);" />
							</td>
						</tr>
						<tr>
							<td>
								<s:submit name="search" key="btn.srch" id="search" align="right"
									targets="down" theme="ajax" action="fetchDrawTktStatus"
									cssClass="button" />
							</td>
						</tr>
					</table>

				</s:form>

				<div id="down" style="text-align: center"></div>

				<br />
				<br />
			</div>
		</div>



	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: bo_rep_trackTicketMenu.jsp,v $ $Revision: 1.5 $
</code>