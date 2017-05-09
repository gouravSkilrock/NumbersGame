
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<s:head theme="ajax" />
</head>
<s:form name="manualEntryForm" onsubmit="makeHiddenPerformStatus()" action="manualWiningEntry.action">		
<div style="color: red; text-align: left;"><s:actionmessage /></div>
	<s:hidden name="gameId" value="%{gameId}"></s:hidden>
	<s:hidden name="gameName" value="%{gameName}"></s:hidden>
	<div id="hiddenPerformStatus"></div>	
	<table width="800" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center" style="margin-left:6px">
			<tr><s:if test="#session.DRAW_SCH_LIST.size>0">
		 <th> <input type="checkbox" name="check" onclick="checkAll(this.name)" /></th>
		  </s:if>			
				<th colspan="7" align="center">
					<s:text name="label.draw.list"/>
				</th>
			</tr>
			<tr>
				<th width = "10%" scope="col" align="center">
					<s:text name="label.draw.id"/>
				</th>
				<th  width = "18%" scope="col" align="center">
					<s:text name="label.draw.time"/>
				</th>
				<th width = "18%" scope="col" align="center">
					<s:text name="label.draw"/> <s:text name="Freeze" /> 
				</th>
				<th  width = "18%" scope="col" align="center">
					<s:text name="DrawName" /> 
				</th>
				<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
					<th  width = "10%" scope="col" align="center">
						<s:text name="EventId" /> 
					</th>
				</s:if>
				<th  width = "10%" scope="col" align="center">
					<s:text name="label.draw.status"/>
				</th>
				<th  scope="col" align="center">
					<s:text name="label.perform.stat"/>
				</th>
			</tr>
	</table>

	<div style="height: 180px;overflow-y:scroll;overflow-x: hidden;clear: both" id="testDiv">			
		<table width="800" border="1" cellpadding="3" cellspacing="0" id="drawResTbl" bordercolor="#CCCCCC" align="center" style="margin-left:6px">	  
			<s:if test="#session.DRAW_SCH_LIST.size>0">
		  		<s:iterator value="#session.DRAW_SCH_LIST">
			  		<tr id="<s:property value="drawId" />">
			    		<td width = "10%">
			    			<input type="checkbox" name="drawIds"" id="r<s:property value="drawId" />" value="<s:property value="drawId" />" onclick="chk()"/>
			    				&nbsp;<s:property value="drawId" />
			    		</td>
			    		<td  width = "18%" align="center">
							<s:property value="drawDateTime" />
						</td>
						<td width = "18%" align="center" >
							<s:property value="freezeDateTime" />
						</td>
						<td width = "18%" align="center" >
							<s:property value="drawName" />
						</td>
						<s:if test="%{#application.COUNTRY_DEPLOYED=='GHANA'}">
							<td width = "10%" align="center" >
								<s:property value="eventId" />
							</td>
						</s:if>
						<td width = "10%" align="center" >
							<s:property value="drawStatus" />
						</td>
						<td align="center" >
							<div id="performStatusID"><s:property  value="performStatus" />&nbsp;</div>						
						</td>										    	  		  
			  		</tr>
		  		</s:iterator>		 
		  	</s:if> 
		  	<s:else>
		  		<tr>
		  			<td colspan="5" align="center"><s:text name="msg.no.result"/></td>			   		    
		 		</tr>
		  	</s:else>		  
		</table>
	</div>
	<div>
		<s:if test="#session.DRAW_SCH_LIST.size>0">
		<table width="750" border="0" cellpadding="0" cellspacing="0" bordercolor="#CCCCCC" align="center">
			<tr> 	 
				<td align="right">
			  	<s:submit theme="simple" key="btn.update.win" onclick="return isAnyChecked()" align="center" cssClass="button"/>		  				  	
			 	</td>			  			   
		  	</tr></table></s:if>
	</div>	
</s:form>
	
	

</html>

<code id="headId" style="visibility: hidden">
$RCSfile: manualEntryDrawResult.jsp,v $
$Revision: 1.3 $
</code>