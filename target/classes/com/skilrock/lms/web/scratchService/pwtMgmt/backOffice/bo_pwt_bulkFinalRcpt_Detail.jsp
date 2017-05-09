<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>


<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>

<s:head theme="ajax" debug="false"/>
</head>
<body>
 <table width="70%" cellpadding="3" cellspacing="0" border="1" bordercolor="#CCCCCC">
 <tr>
 <td>
 <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#6E6E6E" align="left">
	<tr>
		<td>	
			Temporary Receipt No. <s:property value="receiptNum"/>
		</td>
		<s:if test="%{status=='OPEN'}">
		<td colspan="2">	
			<s:property value="agtOrgName"/>
		</td>
		<td align="right">	
			<s:a href="bo_pwt_bulkFinalRcpt_Save.action?receiptNum=%{receiptNum}&closeReceipt=Yes&agtOrgName=%{agtOrgName}" onclick="heightDiv()"><b><font color="red" >CLOSE TEMPORARY RECEIPT</font></s:a>
		</td>
		</s:if>
		<s:else>
		<td colspan="3">	
			<s:property value="agtOrgName"/>
		</td>
		</s:else>
	</tr>
	</table>
	</td>
	</tr>

	<s:if test="#session.TMP_RCPT_DETAIL.size >0">	
		  <s:set id="oldGameNum" name="oldGameNum" value=""></s:set>
		  <%int i=0;
		 	boolean firstGame= true;
		 	boolean firstGameOfReceipt= true;
		  	boolean firstReceipt = true;
		  	int firstEntry = 0;
			int subTotalVirn=0;
			int subTotalTicket=0;
			int totalVirn = 0;
			int totalTicket=0;	
			int receiptTotalVirn = 0;
			int receiptTotalTicket=0;	  
		   %>
		  <s:set id="oldReceiptNum" name="oldReceiptNum" value=""/>
		  <s:iterator id="SearchPwtResults" value="#session.TMP_RCPT_DETAIL" status="searchRes">
		 <s:set id="newReceiptNum" name="newReceiptNum" value="%{generatedId}"/>
		  <s:set id="newGameNum" name="newGameNum" value="%{gameNum}"></s:set>
		 <s:set id="finalReceipt" name="finalReceipt" value="%{finalReceiptNum}"></s:set>
		 <%
		 //System.out.println(pageContext.getAttribute("newGameNum")+"old gamre num"+pageContext.getAttribute("oldGameNum"));
		 //System.out.println(pageContext.getAttribute("newReceiptNum")+"old Receipt num"+pageContext.getAttribute("oldReceiptNum"));
		 %> 
		  	<%
		  	if(pageContext.getAttribute("newReceiptNum")==null||pageContext.getAttribute("newReceiptNum").equals("")){
		  	//System.out.println(pageContext.getAttribute("finalReceipt")+"------Test final----");
		  	if(pageContext.getAttribute("finalReceipt")!=null&&pageContext.getAttribute("finalReceipt").equals("NO RECEIPT")){
		  	pageContext.setAttribute("newReceiptNum","NO RECEIPT");
		  	}else{
		  	pageContext.setAttribute("newReceiptNum","Not Yet Generated");
		  	}
		  	}
			if(pageContext.getAttribute("newReceiptNum").equals(pageContext.getAttribute("oldReceiptNum"))){
			if(pageContext.getAttribute("newGameNum").equals(pageContext.getAttribute("oldGameNum"))){
			%>
		  	  <tr>
			     <s:set name="virn" value="%{verifiedVIRN}"></s:set>
				 <s:set name="tick" value="%{verifiedTickNum}"></s:set>
			      <td><s:property value="userName"/></td>
			      <td><label id=""> <s:property value="verifiedVIRN"/></label></td>
			      <td><label id=""> <s:property value="verifiedTickNum"/></label></td>
			  </tr>
			  <%
			 subTotalVirn = subTotalVirn+Integer.parseInt((String)pageContext.getAttribute("virn"));
			 subTotalTicket = subTotalTicket+Integer.parseInt((String)pageContext.getAttribute("tick"));
			}else{
					pageContext.setAttribute("oldGameNum",pageContext.getAttribute("newGameNum"));
					if(!firstGameOfReceipt){
					 %>
							  <tr>
							      <th>SubTotal</th>
							      <th><%=subTotalVirn %></th>
							      <th><%=subTotalTicket %></th>
							      <%
									totalVirn = totalVirn+subTotalVirn;
									totalTicket = totalTicket+subTotalTicket;
									subTotalVirn=0;
									subTotalTicket=0;
									 %>
							  </tr>
			  			 	</table>
							</td>
							</tr>
			 		 <%
					}
			%>
				<tr><%//System.out.println(i+" in else of gamenum");
			  		 if(!firstGameOfReceipt){ %>
					<td colspan="4">&nbsp;</td>
			 		 <%
			 			subTotalVirn=0;
						subTotalTicket=0;
			 		 }else{  %>
			 		 	<th>Game Name</th>
				 		<th>User Name</th>
						<th>Verified VIRN</th>
						<th>Verified Ticket No.</th>
			 		 <%} %>
			  </tr>
				<tr>
				<td><s:property value="gameNum"/></td>
				<td colspan="3">
					<table width="100%" cellpadding="3" cellspacing="0" border="1" bordercolor="#CCCCCC">
							  <tr>
							  	 <s:set name="virn" value="%{verifiedVIRN}"></s:set>
							  	 <s:set name="tick" value="%{verifiedTickNum}"></s:set>	
							      <td width="27%"><s:property value="userName"/></td>
							      <td width="31%"><label id=""> <s:property value="verifiedVIRN"/></label></td>
							      <td width="42%"><label id=""> <s:property value="verifiedTickNum"/></label></td>
				 			 </tr> 			
			<%firstGameOfReceipt = false;
			 subTotalVirn = subTotalVirn+Integer.parseInt((String)pageContext.getAttribute("virn"));
			 subTotalTicket = subTotalTicket+Integer.parseInt((String)pageContext.getAttribute("tick"));
					
			}
			
			}else{
				 //System.out.println("First Entry-----");
				 
				 //System.out.println("New Receipt--"+pageContext.getAttribute("newReceiptNum")+" old Receipt"+pageContext.getAttribute("oldReceiptNum"));
				 
				pageContext.setAttribute("oldReceiptNum",pageContext.getAttribute("newReceiptNum"));
				pageContext.setAttribute("oldGameNum",pageContext.getAttribute("newGameNum"));
				if(!firstReceipt){
				%>
				 <tr>
			      <th>SubTotal</th>
			      <th><%=subTotalVirn %></th>
			      <th><%=subTotalTicket %></th>
			     <%
					totalVirn = totalVirn+subTotalVirn;
					totalTicket = totalTicket+subTotalTicket; 
					subTotalVirn=0;
					subTotalTicket=0;
					%>
			  </tr>
			  </table><!-- Game Inner Table comprising username and verified Details -->
				</td>
				</tr>  
				<tr>
					<td>&nbsp;</td>
					<td colspan="3">
					<table width="100%" cellpadding="3" cellspacing="0" border="1" bordercolor="#CCCCCC">
							  <tr>
							      
							      <th width="27%">Total</th>
							      <th width="31%"><%=totalVirn%></th>
							      <th width="42%"><%=totalTicket%></th>
							      <%
									receiptTotalVirn = receiptTotalVirn+totalVirn;
									receiptTotalTicket = receiptTotalTicket+totalTicket;
									totalVirn=0;
									totalTicket=0;
								%>
							  </tr>
					</table>
					</td>		  
				</tr>
				</table><!-- Game Table comprising Game Name  and Details -->
				</td>
				</tr>
				</table><!-- New Receipt Tables -->
				</td>
				</tr>
				<%}%>
				<tr>
				<td>
				<table width="100%" cellpadding="3" cellspacing="0" border="1" bordercolor="black">
				<tr>
					<td colspan="4" align="right">
					<%if(pageContext.getAttribute("newReceiptNum").equals("Not Yet Generated")){%>
					<s:a href="bo_pwt_bulkFinalRcpt_Save.action?receiptNum=%{receiptNum}&agtOrgName=%{agtOrgName}"  onclick="heightDiv()"><b><font color="red" >GENERATE FINAL RECEIPT</font></s:a>
					<%}else if(pageContext.getAttribute("newReceiptNum").equals("NO RECEIPT")){ %>	
							<font color="green" >NO RECEIPT</font>
							
					<%} else{%>
					<font color="green" >Final Receipt No: <b><s:property value="generatedId"/></b></font>
					<%} %>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC">
								<tr>
									<th>Game Name</th>
							 		<th>User Name</th>
									<th>Verified VIRN</th>
									<th>Verified Ticket No.</th>
			 		 			 </tr>
									<tr>
									<td><s:property value="gameNum"/></td>
									<td colspan="3">
										<table width="100%" cellpadding="3" cellspacing="0" border="1" bordercolor="#CCCCCC">
												  <tr>
												  	 <s:set name="virn" value="%{verifiedVIRN}"></s:set>
												  	 <s:set name="tick" value="%{verifiedTickNum}"></s:set>	
												      <td width="27%"><s:property value="userName"/></td>
												      <td width="31%"><label id=""> <s:property value="verifiedVIRN"/></label></td>
												      <td width="42%"><label id=""> <s:property value="verifiedTickNum"/></label></td>
												 </tr> 	
				
				<%
				subTotalVirn = subTotalVirn+Integer.parseInt((String)pageContext.getAttribute("virn"));
				subTotalTicket = subTotalTicket+Integer.parseInt((String)pageContext.getAttribute("tick"));
				
				}
					firstReceipt = false;
					firstGameOfReceipt =false;
						 				%>
		  </s:iterator>
<tr>
			      <th>SubTotal</th>
			      <th><%=subTotalVirn %></th>
			      <th><%=subTotalTicket %></th>
			      			      <%
					totalVirn = totalVirn+subTotalVirn;
					totalTicket = totalTicket+subTotalTicket; %>
			  </tr>
			  </table><!-- Game Inner Table comprising username and verified Details -->
				</td>
				</tr>  
				<tr>
					<td>&nbsp;</td>
					<td colspan="3">
					<table width="100%" cellpadding="3" cellspacing="0" border="1" bordercolor="#CCCCCC">
							  <tr>
							      
							      <th width="27%">Total</th>
							      <th width="31%"><%=totalVirn%></th>
							      <th width="42%"><%=totalTicket%></th>
							  </tr>
					</table>
					</td>		  
				</tr>
				</table><!-- Game Table comprising Game Name  and Details -->
				</td>
				</tr>
				</table><!-- New Receipt Tables -->
				</td>
				</tr>
		</s:if>
		<s:else>
		<tr>
			<td colspan="4" align="center">
					<b>No Records Found	
			</td>
		</tr>
		</s:else>		
		</table> 
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_pwt_bulkFinalRcpt_Detail.jsp,v $
$Revision: 1.2 $
</code>