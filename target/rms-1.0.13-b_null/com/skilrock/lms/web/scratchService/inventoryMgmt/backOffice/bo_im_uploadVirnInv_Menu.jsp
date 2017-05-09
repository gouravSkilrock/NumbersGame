

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/virnUpload.js"></script>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath()%>"</script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	<s:head theme="ajax" debug="false"/>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">

  <div id="top_form">
   <h3><s:text name="btn.upload.virn"/></h3>
	
		<s:actionerror/>
				<s:form name="uploadVirn" method="post"
					action="bo_im_uploadVirnInv_Save" enctype="multipart/form-data"
					onsubmit="return validateDates()">
					<table border="0" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC">
						<tr>
							<td colspan="2" nowrap="nowrap" align="left">
								<s:radio id="gtype" key="label.slct.type" name="gameType"
									value="'NEW'"
									list="#{'NEW':'New Games','OPEN':'Old Open Games'}"
									onclick="retrieveURL('bo_im_uploadVirnInv_GameList.action?gameType=' + this.value,'game'), disableDates(this.value)" />
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<div id="gamename1"></div>
							</td>
						</tr>
						<tr>
							<td align="right">
								<s:text name="Select Game:"></s:text>
							</td>
							<td>
								<strong><span id="game"> <s:select
											id="game_Name" cssClass="option" key="label.slct.game"
											theme="simple" list="%{#session.NEW_GAME_LIST.{gameName}}"
											name="gameName" emptyOption="false" headerKey="Please Select"
											headerValue="%{getText('label.please.select')}" /> </span> </strong>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="dates"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="dates1"></div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div id="startDate1" align="center"></div>
							</td>
						</tr>

						<s:set name="stDate" id="stDate" value="#session.presentDate" />
						<s:hidden theme="simple" id="r" value="%{stDate}"></s:hidden>
						<s:set name="st1Date" value="%{dateFormate}" />
						<tr>
							<td colspan="2">

								<div id="gameStartDate" style="margin-left: 72px;">
									<label class="label">
										<s:text name="label.start.date"/>
										<span class="required">*</span>:
									</label>
									<input type="text" name="startDate" id="startDate"
										value="<s:property value="%{stDate}"/>" readonly size="12">
									<input type="button" id="startDateButton"
										style="width: 19px; height: 19px;  top: 2px; position: relative; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
										onclick="displayCalendar(document.getElementById('startDate'),'yyyy-mm-dd', this, '<s:property value="%{stDate}"/>', '<s:property value="%{stDate}"/>', false)" />

									<input type="hidden" id="startDateHid"
										value="#session.presentDate" />
									<div id="startDateDiv" style="display: none"></div>
								</div>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<div id="saleendDate1" align="center"></div>
							</td>
						</tr>


						<tr>
							<td colspan="2">
								<label class="label" style="margin-left: 50px;">
									<s:text name="label.sale.end.date"/>
									<span class="required">*</span>:
								</label>
								<input type="text" name="saleendDate" id="saleendDate"
									value="<s:property value="%{stDate}"/>" readonly size="12">
								<input type="button" id="saleendDateButton"
									style="width: 19px; height: 19px;top: 2px; position: relative; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('saleendDate'),'yyyy-mm-dd', this, '<s:property value="%{stDate}"/>', '<s:property value="%{stDate}"/>', false)" />

								<input type="hidden" id="saleendDateHid"
									value="#session.presentDate" />
								<div id="saleendDateDiv" style="display: none"></div>
							</td>
						</tr>


						<tr>
							<td colspan="2">
								<div id="pwtendDate1" align="center"></div>
							</td>
						</tr>

						<tr>
							<td colspan="2">
								<label class="label" style="margin-left: 50px;">
									<s:text name="label.pwt.end.date"/>
									<span class="required">*</span>:
								</label>
								<input type="text" name="pwtendDate" id="pwtendDate"
									value="<s:property value="%{stDate}"/>" readonly size="12">
								<input type="button" id="pwtendDateButton"
									style="width: 19px; height: 19px;top: 2px; position: relative; background: url('<%=request.getContextPath() %>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
									onclick="displayCalendar(document.getElementById('pwtendDate'),'yyyy-mm-dd', this, '<s:property value="%{stDate}"/>', '<s:property value="%{stDate}"/>', false)" />

								<input type="hidden" id="endDateHid"
									value="#session.presentDate" />
								<div id="endDateDiv" style="display: none"></div>

							</td>
						</tr>





						<tr>
							<td>
								<div id="file1">
								</div>
							</td>
						</tr>
						<tr>
							<td align="left">
								<s:file key="label.slct.file.to.upload.virn" id="file"
									name="details" cssClass="button" accept="text/*"></s:file>
							</td>
						</tr>

						<tr>
							<td align="right">
								<s:submit key="btn.upload.virn" cssClass="button"></s:submit>
							</td>
						</tr>

					</table>
				</s:form>

			</div></div>
	</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_uploadVirnInv_Menu.jsp,v $
$Revision: 1.3 $
</code>