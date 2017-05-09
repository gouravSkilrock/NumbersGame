<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@page import="java.text.NumberFormat"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<s:head theme="ajax" debug="false"/>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/lmsCalendar.css" media="screen"/>
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/common/globalJs/calender.js"></script>
	
	<script type="text/javascript">
	

	function isDateValid()
	{
	  //alert("hello");
	  var pwtEndDate = document.getElementById("pwtEndDate");
	  var pwtDate= pwtEndDate.value;
	   
	    var presentDate=document.extendDate.presentDate.value
	    //alert(presentDate);
	    var pwtDateDb=document.extendDate.pwtDateDb.value
	   
	   if(pwtDate==pwtDateDb)
	   {
	    //alert("no change");
	    document.getElementById("date").innerHTML = "<font color = 'red'><s:text name='error.extnd.date.click.cancel'/></font>";
	    return false;
	   }
	   
	   if(pwtDate < presentDate)
	   {
	    //alert("date is not valid");
	    document.getElementById("date").innerHTML = "<font color = 'red'><s:text name='error.entr.valid.date'/></font>";
	    return false;
	   }
	   return true; 
	    
	   } 
	</script>
</head>
	
	
	<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	<div id="wrap">

  <div id="top_form">
   <h3><s:text name="label.game.details"></s:text></h3>
		
		<s:form method="POST" name="extendDate" >
		<table>
		<tr><td>
		<table  border="0" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC">
				<s:iterator value="#session.GAME_SEARCH_RESULTS">
					<tr>
						<td>
							<s:textfield key="label.game.nbr" name="gameNBR" value="%{gameNbr}" />
						
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
								value="%{bookRemaining}" />
						
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
							<s:textfield key="label.pwt.end.date" readonly="true"
								name="pwtDateDb" value="%{pwt_end_date}" />
							
						</td>
					</tr>
					
				</s:iterator>


				</table>
		</td></tr>
		<tr><td>
			<br />
					<table border="1" cellpadding="2" cellspacing="0" width="400" align="left"> 
							<tr>
							<th align="center" colspan="2"><s:text name="label.prize.details"></s:text></th>
							</tr>
						<tr>
							<th>
								<s:text name="label.prize.amt"></s:text>
							</th>
							<th>
								<s:text name="label.nbrof.prizes.remaining"></s:text>
							</th>
						</tr>
						<s:iterator value="prizeStatusList">
							<tr>
								<td>
								<s:set name="przAmount"  value="%{prizeAmt}"></s:set>
   								<%
									String	przAmount =FormatNumber.formatNumberForJSP(pageContext.getAttribute("przAmount"));				
								%>
									<%=przAmount %>
								</td>
								<td>
									<s:property value="%{nbrOfPrizeLeft}" />
								</td>
							</tr>
						</s:iterator>
					</table>
			
		</td></tr>
		<tr><td >
		<br />
			<s:submit theme="simple" align="center" key="TERMINATE" action="bo_gm_closeGame_Save" cssClass="button" />
			<s:submit theme="simple" action="bo_gm_closeGame_Menu" key="btn.cancel" align="center" cssClass="button"/>
			
		</td></tr>
		
			<tr><td>
				<table border="0" cellpadding="0" cellspacing="0" align="left">
				<tr>
					<td align="left"><br />
						<s:a href="bo_gm_closeGame_ExtendDateView.action" targets="ans" theme="ajax" cssStyle="font-weight:bold;"><s:text name="label.extnd.pwt.end.date"></s:text></s:a>
						<br />
					</td>
					<td>
					
					</td>
				</tr>
			</table>
				<br/>		
			<table border="0"  cellpadding="2" cellspacing="0">
				<tr>
					<td>
						<div id="ans" align="center">
						</div>
					</td>
				</tr>
			</table>
			</td></tr>
		</table>
			
					
			
		
			
		</s:form>
</div></div>


	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_gm_closeGame_Detail.jsp,v $
$Revision: 1.3 $
</code>