
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
			<s:head theme="ajax" debug="false" />
		<script
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/drawGame.js"></script>
		<script language="javaScript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/calander.js"></script>
		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />
		<link
			href="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/boStyle.css"
			rel="stylesheet" type="text/css" />
		<link
			href="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/calander.css"
			rel="stylesheet" type="text/css" />
		
	</head>
	<body>


		<div id="wrap">

			<div id="top_form">


				<s:form action="checkNextDraw.action" onsubmit="return validation()">

					<table cellspacing="0" cellpadding="0" border="0" class="table">

						<tr>
							<th>
								Check Next Draw
							</th>
						</tr>
						<tr>
							<td class="normalText">
								<b> Enter Game No.</b>
							</td>
							<td>
								<input type="text" name="gameNo">
							</td>
						</tr>


						<tr>
							<td class="normalText">
								<table>
									<tr>
										<td class="normalText">
											Enter draw id
										</td>
										<td class="normalText">
											<input type="text" name="drawId">
										</td>
									</tr>
								</table>
							</td>
						</tr>

						<tr>
							<td align="right" colspan="2">
								<s:submit theme="ajax" targets="serachResultDiv" value="Submit"
									cssClass="button" loadingText=" " />
							</td>
						</tr>
						<tr>
							<td align="right">
								<a href="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/managment/drawScheduleMenu.jsp" style="font-size: 11px">draw
									manage menu</a>
							</td>
						</tr>
						<tr>
							<td>
								<div id="cancelLoadingDiv" class="normalText"
									style="display: none;">
									<font color='red'>Please Wait..
									</font>&nbsp;&nbsp;
									<img src="/DrawGameTest/images/loading.gif" />
								</div>
								<s:div theme="ajax" id="cancelResultDiv"></s:div>
							</td>
						</tr>
					</table>
				</s:form>