<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="java.util.Map"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
    <div id="wrap">
    <div id="top_form">
    <h3>UnClaimed Winning Details For Scratch Games</h3>
	<s:if test="%{#session.unclmPwtDet.size>0}">
		<s:if test="%{#session.USER_INFO.userType =='AGENT'}">
		
			<s:iterator value="#session.unclmPwtDet">		
			 <table  border="1" cellpadding="3" cellspacing="0" width="90%" >
				<tr>
					<th align="center" colspan="5"><s:property value="key" /><s:set name="akey" value="key"/></th>
				</tr>
				<tr>
					<th align="center">VIRN Code</th>				
					<th align="center">Game Name</th>
					<th align="center">Claimed By Type</th>	
					<th align="center">Claimed By Name</th>	
					<th align="center">Prize Amount</th>
							
				</tr>
										
					<s:iterator value="value" >
					<tr>
						<td align="center" nowrap="nowrap"><s:property value="virnCode" /></td>				
						<td align="center" nowrap="nowrap"><s:property value="gameNbr" />-<s:property value="gameName" /></td>
						<td align="left" nowrap="nowrap">
							<s:if test="%{pwtType=='Anonymous'}">
								<s:property value="#application.TIER_MAP.RETAILER" />
							</s:if>
							<s:else>
								DIRECT PLAYER
							</s:else>
						</td>	
						<td align="left" nowrap="nowrap"><s:property value="claimedBy" /></td>
						<td align="right" nowrap="nowrap">
							<s:set name="pwtAmount" value="pwtAmt"/>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmount")) %>
						</td>					
					</tr>
					</s:iterator>		
				
				<tr> 
					<th colspan="4" align="right">Total UnClaimed Amount</th>				
					<%gameAmt = (Double)((Map)session.getAttribute("totalUnclmBal")).get(pageContext.getAttribute("akey")); %>						
					<td align="right"><%=FormatNumber.formatNumberForJSP(gameAmt)%></td>				
				</tr>
			</table>
			</s:iterator>
				
		</s:if>
		
		<s:else>	
			<s:iterator value="#session.unclmPwtDet">		
			 <table  border="1" cellpadding="3" cellspacing="0" width="90%" >
				<tr>
					<th align="center" colspan="6"><s:property value="key" /><s:set name="akey" value="key"/></th>
				</tr>
				<tr>
					<th align="center">VIRN Code</th>	
					<th align="center">Ticket Number</th>			
					<th align="center">Game Name</th>				
					<th align="center">Claimed By Type</th>	
					<th align="center">Claimed By Name</th>
					<th align="center">Prize Amount</th>					
				</tr>
										
					<s:iterator value="value" >
					<tr>
						<td align="center" nowrap="nowrap"><s:property value="virnCode" /></td>	
						<td align="center" nowrap="nowrap"><s:property value="tktNbr" /></td>				
						<td align="center" nowrap="nowrap"><s:property value="gameNbr" />-<s:property value="gameName" /></td>
						<td align="left" nowrap="nowrap">
							<s:if test="%{pwtType=='Anonymous'}">
								Anonymous
							</s:if>
							<s:else>
								Direct Player
							</s:else>
						</td>	
						<td align="left" nowrap="nowrap"><s:property value="claimedBy" /></td>
						<td align="right" nowrap="nowrap">
							<s:set name="pwtAmount" value="pwtAmt"/>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmount")) %>
						</td>					
					</tr>
					</s:iterator>		
				
				<tr> 
					<th colspan="5" align="right">Total UnClaimed Amount</th>
					
					<%! double gameAmt=0.0; %>
					<%gameAmt = (Double)((Map)session.getAttribute("totalUnclmBal")).get(pageContext.getAttribute("akey")); %>						
								
					<td align="right"><%=FormatNumber.formatNumberForJSP(gameAmt)%></td>				
				</tr>
			</table>
			</s:iterator>	
		</s:else>	
			
	 </s:if>
	<s:else>
		<h5>No UnClaimed Winning details for Scratch Game Found in Record.</h5>
	</s:else>
	
	<br /><br /><br />
	<s:if test="%{#application.IS_DRAW =='YES'}">
	<h3>UnClaimed Winning Details For Draw Games</h3>
	<s:if test="%{#session.DRClmPwtDetailMap.size>0}">
		<s:if test="%{#session.USER_INFO.userType =='AGENT'}">
		
			<s:iterator value="#session.DRClmPwtDetailMap">		
			 <table  border="1" cellpadding="3" cellspacing="0" width="90%" >
				<tr>
					<th align="center" colspan="5"><s:property value="key" /><s:set name="bkey" value="key"/></th>
				</tr>
				<tr>					
					<th align="center">Ticket Number</th>			
					<th align="center">Game Name</th>				
					<th align="center">Claimed By Type</th>	
					<th align="center">Claimed By Name</th>
					<th align="center">Prize Amount</th>					
				</tr>
										
					<s:iterator value="value" >
					<tr>						
						<td align="center" nowrap="nowrap"><s:property value="tktNbr" /></td>				
						<td align="center" nowrap="nowrap"><s:property value="gameNbr" />-<s:property value="gameName" /></td>
						<td align="left" nowrap="nowrap">
							<s:if test="%{pwtType=='Anonymous'}">
								<s:property value="#application.TIER_MAP.RETAILER" />
							</s:if>
							<s:else>
								Direct Player
							</s:else>
						</td>	
						<td align="left" nowrap="nowrap"><s:property value="claimedBy" /></td>
						<td align="right" nowrap="nowrap">
							<s:set name="pwtAmount" value="pwtAmt"/>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmount")) %>
						</td>					
					</tr>
					</s:iterator>		
				
				<tr> 
					<th colspan="3" align="right">Total UnClaimed Amount</th>				
					<%gameAmt = (Double)((Map)session.getAttribute("totalUnclmBal")).get(pageContext.getAttribute("bkey")); %>						
					<td align="right"><%=FormatNumber.formatNumberForJSP(gameAmt)%></td>				
				</tr>
			</table>
			</s:iterator>
				
		</s:if>
		
		<s:else>	
			<s:iterator value="#session.DRClmPwtDetailMap">		
			 <table  border="1" cellpadding="3" cellspacing="0" width="90%" >
				<tr>
					<th align="center" colspan="5"><s:property value="key" /><s:set name="bkey" value="key"/></th>
				</tr>
				<tr>					
					<th align="center">Ticket Number</th>			
					<th align="center">Game Name</th>				
					<th align="center">Claimed By Type</th>	
					<th align="center">Claimed By Name</th>
					<th align="center">Prize Amount</th>					
				</tr>
										
					<s:iterator value="value" >
					<tr>						
						<td align="center" nowrap="nowrap"><s:property value="tktNbr" /></td>				
						<td align="center" nowrap="nowrap"><s:property value="gameNbr" />-<s:property value="gameName" /></td>
						<td align="left" nowrap="nowrap">
							<s:if test="%{pwtType=='Anonymous'}">
								Anonymous
							</s:if>
							<s:else>
								Direct Player
							</s:else>
						</td>	
						<td align="left" nowrap="nowrap"><s:property value="claimedBy" /></td>
						<td align="right" nowrap="nowrap">
							<s:set name="pwtAmount" value="pwtAmt"/>
							<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("pwtAmount")) %>
						</td>					
					</tr>
					</s:iterator>		
				
				<tr> 
					<th colspan="4" align="right">Total UnClaimed Amount</th>
					
					<%! double drawGameAmt=0.0; %>
					<%drawGameAmt = (Double)((Map)session.getAttribute("newDRGameWiseTotAmt")).get(pageContext.getAttribute("bkey")); %>						
								
					<td align="right"><%=FormatNumber.formatNumberForJSP(drawGameAmt)%></td>				
				</tr>
			</table><br/>
			</s:iterator>	
		</s:else>	
			
	 </s:if>
	<s:else>
		<h5>No UnClaimed Winning details for Draw Game Found in Record.</h5>
	</s:else>
	</s:if>
		
	 </div>
	 </div>
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: unClmPwtDetails.jsp,v $
$Revision: 1.2 $
</code>