	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
		
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>	
		<title>lottery_skilrock</title>	
		<s:head theme="ajax" debug="false"/>
	</head>

	<body >


	<div id="wrap">

 	<div id="top_form">
  
	
        <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	        <tr>
	          <th>Voucher Number</th>	
	          <th>Transaction Date</th>
	          <th>Amount</th>
			  <th>Organization Name</th>
			  <th>Organization Type</th>
			  <s:if test="#session.usertype!=null">
			  <th>Owner Name</th>
			  </s:if>
			  <th>Voucher Type</th>
			  
			</tr>
	        
	        <s:if test="#session.boreceiptsearch!=null">
	          <s:iterator id="beanCart" value="#session.boreceiptsearch">
	           <tr class="startSortable">
	             <!--   <td><s:a targets="down1" theme="ajax" href="inventoryGameReportLink.action?gameid=%{gameid} & gamenumber=%{gamenbr} & gamename=%{gamename} & saleenddate=%{saleenddate}" ><s:property value="gamenbr" />  </s:a></td>-->
				  <s:if test="%{recieptType.equals('DLCHALLAN')}">	
			  	   <td><a target="_blank" href="agt_rep_showDeliveryChallen.action?id=<s:property value="%{receiptId}"/>& type=<s:property value="%{recieptType}"/>"><s:property value="genratedId" /></a></td>	
				   </s:if>	
				   <s:elseif test="%{recieptType.equals('DSRCHALLAN')}">
				   <td><a target="_blank" href="agt_rep_showDeliveryChallen.action?id=<s:property value="%{receiptId}"/> & type=<s:property value="%{recieptType}"/>"><s:property value="genratedId" /></a></td>
				   </s:elseif>
				  <s:else>
				  	<s:if test="%{recieptType == 'DG_PWT_PLR'}">
				   				<td><a target="_blank" href="bo_rep_showReceipt.action?serviceCode=DG&receiptId=<s:property value="%{receiptId}"/>&orgName=<s:property value="%{orgName}"/>&orgType=<s:property value="%{orgType}"/>& ownerName=<s:property value="%{ownerName}"/>"><s:property value="genratedId" /></a></td>
				   			</s:if>
				   			<s:else>
				   				<s:if test="%{recieptType == 'PWT_PLR'}">
			  						<td><a target="_blank" href="bo_rep_showReceipt.action?serviceCode=SE&receiptId=<s:property value="%{receiptId}"/>&orgName=<s:property value="%{orgName}"/>&orgType=<s:property value="%{orgType}"/>& ownerName=<s:property value="%{ownerName}"/>"><s:property value="genratedId" /></a></td>
				   				</s:if>
				   				<s:else>
			   						<td><a target="_blank" href="bo_rep_showReceipt.action?receiptId=<s:property value="%{receiptId}"/>&orgName=<s:property value="%{orgName}"/>&orgType=<s:property value="%{orgType}"/>& ownerName=<s:property value="%{ownerName}"/>"><s:property value="genratedId" /></a></td>
			   					</s:else>
				   			</s:else>
				   </s:else>
			  	  
				  <td><s:property value="transactionDate" /></td>
				  <td><s:property value="amount" /></td>
				  <td><s:property value="orgName" /></td>
				  <s:if test="%{orgType != 'PLAYER'}">
				  		 <td><s:property value="%{#application.TIER_MAP[orgType].toUpperCase()}" /></td>
				  </s:if>
				  <s:else>
				  		<td><s:property value="orgType"/></td>
				  </s:else>
				  <s:if test="#session.usertype!=null">
			  		<td><s:property value="ownerName" /></td>
			  	  </s:if>	
			  	   <td><s:property value="recieptType" /></td>
				</tr>	
	            </s:iterator>
             </s:if>
	         <s:else>
	         	<tr><td colspan="6" align="center"> No Voucher to Process</td></tr>
	         </s:else>
	                    
          </table>
</div>
<s:div id="naviga" cssStyle="text-align: center" >
	<s:if test=" #session.APP_ORDER_LIST5.size >5 & #session.boreceiptsearch!=null">
 		<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="down" href="agt_rep_searchReceipt_Navigate.action?end=first">First</s:a><s:a theme="ajax" targets="down" href="agt_rep_searchReceipt_Navigate.action?end=Previous" > Previous</s:a></s:if><s:else>First Previous</s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/5)*5)">Next Last</s:if><s:else><s:a theme="ajax" targets="down" href="agt_rep_searchReceipt_Navigate.action?end=Next" >Next</s:a><s:a theme="ajax" targets="down" href="agt_rep_searchReceipt_Navigate.action?end=last">Last</s:a></s:else>      
    </s:if>
</s:div>

	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_rep_searchReceipt_SearchResult.jsp,v $
$Revision: 1.2 $
</code>