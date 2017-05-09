
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String deployMentDate = (String) application
			.getAttribute("DEPLOYMENT_DATE");
	deployMentDate = deployMentDate.replaceAll("-", "/");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/lmsCalendar.css"
			media="screen" />
		<script>var projectName="<%=request.getContextPath()%>"</script>
	      <script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/drawMgmt/js/drawGameMgmt.js"></script>
		<s:head theme="ajax" debug="false" />
		<style>
			.systemMSG.popup.error {
			    background-color: #FFFFFF;
			    border-color: #FFFFFF;
			    box-shadow: 0px 0px 47px rgb(49, 49, 49);
			}
			.systemMSG.popup {
			    position: absolute;
			    width: 100%;
			    max-width: 400px;
			    background-color: white;
			    left: 50%;
			    top: 25%;
			    margin: 0;
			    border-radius: 5px;
			    border: 2px solid #fff;
			    overflow: hidden;
			    min-height: 137px;
			    z-index: 999;
			    margin-left: -200px;
			    margin-top: 0px;
			}
			.popup.systemMSG.error h5 {
			    background-color: #DDDDDD;
			    color: #545454;
			}
			.popup.systemMSG.error h5 button {
			    background-color: #ab544f;
			}
			
			.popup h5 {
			    margin: 0px;
			    padding: 10px 10px;
			    background-color: #0c0c0c;
			    color: #fff;
			    font-size: 16px;
			    text-transform: uppercase;
			}
			.modal-body {
			    position: relative;
			    padding: 10px 10px 20px 10px;
			}
			.popup.systemMSG.error .msgBox {
			    color: #545454;
			}
			.systemMSG.popup .msgBox {
			    padding: 0;
			    font-size: 15px;
			    padding-top: 0;
			}
			button.no-btn {
			    margin-right: 10px;
			    padding: 8px 30px;
			    font-size: 14px;
			    background-color: #EB7372;
			    color: #fff;
			    border: 0;
			    border-radius: 2px;
			    cursor: pointer;
			}
			button.yes-btn {
			    padding: 8px 30px;
			    font-size: 14px;
			    background-color: #7BC585;
			    color: #fff;
			    border: 0;
			    border-radius: 2px;
			    cursor: pointer;
			}
			
			.row-d {
			    width: 100%;
			    float: left;
			}
			.dt-tm-d {
			    float: left;
			    width: 50%;
			    margin: 8px -12px;
			    font-size: 14px;
			    font-weight: bold;
			    text-align: -webkit-right;
			}
			.dt-tm-dd {
			    width: 50%;
			    float: left;
			       margin: 8px 0px;
			    font-size: 14px;
			    font-weight: normal;
			}
			.check-in-btn {
			        float: left;
			    width: 100%;
			    margin-top: 15px;
			    text-align: center;
			    margin-bottom: 10px;
			}
			span.do-cun {
			    font-size: 13px;
			    text-align: center;
			    display: block;
			    padding: 4px 0px 0px 0px;
			    color: #333;
			    font-weight: bold;
			    text-transform: uppercase;
			}
			.div-cont{float: left;
			    width: 100%;
			    margin-bottom: 10px;
			    background-color: whitesmoke;}
		</style>
	</head>
	<body onload="fillOptions()" style="overflow: none">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<s:hidden id="mylocale" value="%{#session.WW_TRANS_I18N_LOCALE}"/>
		<div id="wrap">
			<div id="top_form">
				<div id="data">
					<h3>
						<s:text name="btn.srch.draw"/>
					</h3>
					<div style="color: red; text-align: left;">
						<s:actionmessage />
					</div>
					<s:form name="searchForm" id="searchFormId">
						<table cellspacing="0" cellpadding="3" border="0" bordercolor="#CCCCCC">
							<tr>
								<td>
									<s:select list="#session.DRAWGAME_LIST" name="gameId" key="label.game.name" id="game_no" cssClass="option" />
								</td>
							</tr>
							<tr>
								<td>
										<s:select list="#{'FREEZE':getText('FREEZE')}"name="status" key="label.draw.status" id="status" cssClass="option" />
								</td>
							</tr>
							<s:set name="stDate" value="#session.CURR_TIME"></s:set>
							<s:hidden id="hidStDate" name="hidStDate" value="%{stDate}"></s:hidden>
							<tr>
								<td align="right">
									<s:text name="label.from"/>:&nbsp;
								</td>
								<td align="right" nowrap="nowrap">
									<table>
										<tr>
											<td>
												<s:if test="%{#session.INVOKER!='ManualDeclare'}">
													<input type="text" name="fromDate" id="from_date"
														value="<s:property value="%{stDate}"/>" readonly="readonly" size="12" />
												</s:if>

												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(document.getElementById('from_date'),'dd/mm/yyyy', this, '<s:property value="%{stDate}"/>', '01/01/1900', '01/01/3000')" />
											</td>
											<td>
												<table>
													<s:select name="fromHour" cssClass="option" id="from_hour"
														list="{''}" />
												</table>
											</td>
											<td>
												<table>
													<s:select name="fromMin" cssClass="option" id="from_min"
														list="{''}" />
												</table>
											</td>
											<td>
												<table>
													<s:select name="fromSec" cssClass="option" id="from_sec"
														list="{''}" />
												</table>
											</td>
										</tr>
									</table>

								</td>
							</tr>
							<tr>
								<td align="right">
									<s:text name="label.to"/>:&nbsp;
								</td>

								<td align="right" nowrap="nowrap">
									<table id="dateTbl">
										<tr>

											<td>
												<input type="text" name="toDate" id="to_date"
													value="<s:property value="%{stDate}"/>" readonly="readonly"
													size="12" />
												<input type="button"
													style="width: 19px; height: 19px; background: url('<%=request.getContextPath()%>/LMSImages/imagesCal/dateIcon.gif'); top left; border: 0;"
													onclick="displayCalendar(document.getElementById('to_date'),'dd/mm/yyyy', this, '<s:property value="%{stDate}"/>', document.getElementById('from_date').value, '01/12/3000')" />
											</td>

											<td>
												<table>
													<s:select name="toHour" cssClass="option" id="to_hour"
														list="{''}" />
												</table>
											</td>
											<td>
												<table>
													<s:select name="toMin" cssClass="option" id="to_min"
														list="{''}" />
												</table>
											</td>
											<td>
												<table>
													<s:select name="toSec" cssClass="option" id="to_sec"
														list="{''}" />
												</table>
											</td>
										</tr>
									</table>

								</td>
							</tr>

							<tr>
								<td colspan="2" align="right">
									<table>
										<s:hidden name="priv" value="DRAW_MGMT" /> 
										<s:submit id="searchSubmit" theme="ajax" key="btn.srch" cssClass="button" targets="draw_res_div" action="getDrawSchduleAutomatic" formId="searchFormId" />
								</table>
								</td>
							</tr>
						</table>

					</s:form>

					<div id="draw_res_div">

					</div>

				</div>
			</div>
		</div>
		<div class="popup systemMSG error" id="error-popup" style="display: none;">
	    	<h5>SUBMIT</h5>
	          <div class="modal-bodyWrap">
	             <div class="modal-body">
	                <div class="row">
	                   <div class="col-xs-9 msgBox" id="error"></div>
	                   
		                   <div class="div-cont">
		                   		<span class="do-cun">Do you want to continue?</span>
			                   <div class="check-in-btn">
				                   	<button class="no-btn" id="no-btn" onclick="return failedConfirmation()" >No</button>
				                   	<button class="yes-btn" id="yes-btn" onclick=" return successConfirmation()" >Yes</button>
			                   </div>
		                   </div>
		                   
	                </div>                  
	             </div>
	          </div>
	    </div>
	</body>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: searchDrawResult.jsp,v $ $Revision: 1.3 $
</code>