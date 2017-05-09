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
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
		<script>var projectName="<%=request.getContextPath() %>"</script>

		
		<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		
		<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/scratchService/gameMgmt/backOffice/js/closeSaleGameDetails.js"></script>
		<s:head theme="ajax" debug="false" />
	</head>

	<body>
		<%@include
				file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					<s:text name="label.game.details"/>
				</h3>

				<s:form method="POST" name="extendDate" >

					<table width="350" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC">
						<s:iterator value="#session.SALE_ClOSE_SEARCH_RESULTS">
							<tr>
								<td>
									<s:textfield key="label.game.nbr" readonly="true"
										value="%{gameNbr}" />
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield key="label.game.name" readonly="true"
										value="%{gameName}" />
								</td>
							</tr>

							<tr>
								<td>
									<s:textfield key="label.nbrof.books" readonly="true"
										value="%{nbrBooks}" />
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield key="label.nbrof.tickets" readonly="true"
										value="%{nbrTickets}" />
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield key="label.books.remaining" readonly="true"
										value="%{bookRemaining}"></s:textfield>
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield key="label.start.date" readonly="true"
										value="%{gameStartDate}" />
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield key="label.sale.end.date" readonly="true"
										name="saleDateDB" value="%{saleEndDate}" />
								</td>
							</tr>
							<tr>
								<td>
									<s:textfield key="label.pwt.end.date" readonly="true"
										name="pwtDateDB" value="%{pwt_end_date}" />
								</td>
							</tr>


							<s:hidden name="gameId" value="%{gameId}" />
							<s:hidden name="gameNbr" value="%{gameNbr}" />
							<s:hidden name="gameName" value="%{gameName}" />
						</s:iterator>


					</table>
					<br />

					<table border="1" cellpadding="2" cellspacing="0" width="300">

						<tr>
							<th>
								<s:text name="label.prize.amt"/>
							</th>
							<th>
								<s:text name="label.nbrof.prizes.remaining"/>
							</th>
						</tr>
						<s:iterator value="prizeStatusList">
							<tr>
								<td>
									<s:property value="%{prizeAmt}" />
								</td>
								<td>
									<s:property value="%{nbrOfPrizeLeft}" />
								</td>
							</tr>
						</s:iterator>
					</table>

<br/>
					
					<table border="0" cellpadding="2" cellspacing="0">
						<tr>
							<td>
								<s:a
									href="bo_gm_stopSale_ExtendDateView.action"
									theme="ajax" targets="ans"><s:text name="label.extend.end.dates"/></s:a>
								</td>
						</tr>
					</table>
					
					<div id="ans">
						<s:actionerror />
						<s:fielderror />
					</div>
                    <br>
					<table align="left" align="center" border="0" cellpadding="2"
						cellspacing="0">
						<s:submit align="left" action="bo_gm_stopSale_Save" key="btn.close" theme="simple"
						cssClass="button" />
						<s:submit action="bo_gm_stopSale_Menu" key="btn.cancel" align="center" theme="simple"
							cssClass="button">
						</s:submit>
					</table>
				</s:form>
				</div>
				</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_stopSale_Detail.jsp,v $
$Revision: 1.3 $
</code>