
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
<s:form action="manualDeclareEntry.action"  >	
<div style="color: red; text-align: left;"><s:actionmessage /></div>	
	<s:hidden name="gameNo" value="%{gameNo}"></s:hidden>
	<s:hidden name="gameName" value="%{gameName}"></s:hidden>
	<table width="800" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="left" style="margin-left:6px">
			<tr><s:if test="#session.DRAW_SCH_LIST.size>0">
		 <th> <input type="checkbox" name="check" onclick="checkAll(this.name)" /></th>
		  </s:if>			
				<th colspan="4" align="center">
					<s:text name="label.draw.list"/>
				</th>
			</tr>
			<tr>
				<th width="10%" scope="col" align="center">
					<s:text name="label.draw.id"/>
				</th>
				<th width="22%" scope="col" align="center">
					<s:text name="label.draw.time"/>
				</th>
				<th width="23%" scope="col" align="center">
					<s:text name="label.draw"/> <s:text name="Freeze" /> <s:text name="label.time"/>
				</th>
				<th width="8%" scope="col" align="center">
					<s:text name="label.draw.status"/>
				</th>
			</tr>
	</table>

	<div style="height: 180px; overflow:scroll; clear: both" id="testDiv">			
		<table width="800" border="1" cellpadding="5" cellspacing="0" id="drawResTbl" bordercolor="#CCCCCC" align="center" style="margin-left:6px">	  
			<s:if test="#session.DRAW_SCH_LIST.size>0">
		  		<s:iterator value="#session.DRAW_SCH_LIST">
			  		<tr id="<s:property value="drawId" />">
			    		<td width="10%">
			    			<input type="checkbox" name="drawIds"" id="<s:property value="drawId" />" value="<s:property value="drawId" />" onclick="chk()"/>
			    				&nbsp;<s:property value="drawId" />
			    		</td>
			    		<td width="22%" align="center">
							<s:property value="drawDateTime" />
						</td>
						<td align="center" width="23%">
							<s:property value="freezeDateTime" />
						</td>
						<td align="center" width="8%">
							<s:property value="drawStatus" />
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
			  	<s:submit theme="simple" key="btn.declare.man" onclick="return isAnyChecked()" align="center" cssClass="button"/>		  				  	
			 	</td>			  			   
		  	</tr></table></s:if>
	</div>	
</s:form>
	
	

</html>

<code id="headId" style="visibility: hidden">
$RCSfile: manualDeclareDrawResult.jsp,v $
$Revision: 1.3 $
</code>