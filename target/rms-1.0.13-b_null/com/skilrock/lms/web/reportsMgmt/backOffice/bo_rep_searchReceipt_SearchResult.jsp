	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>	
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />	
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>	
		<s:head theme="ajax" debug="false"/>
	</head>
<body >


	<div id="wrap">

  <div id="top_form">
  
	
        <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="left">
	        <tr>
	          <th><s:text name="label.voucher.nbr"/></th>	
	          <th><s:text name="label.voucher.date"/></th>
	          <th><s:text name="label.amount"/></th>
			  <th><s:text name="label.org.name"/></th>
			  <th><s:text name="label.org.type"/></th>			
			  <s:if test="#session.usertype!=null">
			  <th><s:text name="label.owner.name"/></th>
			  </s:if>
			  <th><s:text name="label.voucher.type"/></th>
			</tr>
	        
	        <s:if test="#session.boreceiptsearch!=null">
	          <s:iterator id="beanCart" value="#session.boreceiptsearch">
	           <tr class="startSortable">
	             <!--   <td><s:a targets="down1" theme="ajax" href="inventoryGameReportLink.action?gameid=%{gameid} & gamenumber=%{gamenbr} & gamename=%{gamename} & saleenddate=%{saleenddate}" ><s:property value="gamenbr" />  </s:a></td>-->
	             <s:if test="%{recieptType.equals('DLCHALLAN')}">
			  	   <td><a target="_blank" href="searchDeliveryChallen.action?id=<s:property value="%{receiptId}"/>& type=<s:property value="%{recieptType}"/>& orgType=<s:property value="%{orgType}"/>& ownerName=<s:property value="%{ownerName}"/>"><s:property value="genratedId" /></a></td>	
				   </s:if>	
				   <s:elseif test="%{recieptType.equals('DSRCHALLAN')}">
				   <td><a target="_blank" href="searchDeliveryChallen.action?id=<s:property value="%{receiptId}"/>& type=<s:property value="%{recieptType}"/>& orgType=<s:property value="%{orgType}"/>& ownerName=<s:property value="%{ownerName}"/>"><s:property value="genratedId" /></a></td>
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
				   				<s:if test="%{recieptType == 'DLNOTE'}">
				   				

				   				<td><a target="_blank" href="<%=request.getContextPath() %>/com/skilrock/lms/web/inventoryMgmt/bo_rep_showDeliveryNote.action?type=DLCHALLAN&Id=<s:property value="%{receiptId}"/>&orgName=<s:property value="%{orgName}"/>&orgType=<s:property value="%{orgType}"/>& ownerName=<s:property value="%{ownerName}"/>"><s:property value="genratedId" /></a></td>
			   						</s:if>
			   						<s:else>
			   						<td><a target="_blank" href="bo_rep_showReceipt.action?receiptId=<s:property value="%{receiptId}"/>&orgName=<s:property value="%{orgName}"/>&orgType=<s:property value="%{orgType}"/>& ownerName=<s:property value="%{ownerName}"/>"><s:property value="genratedId" /></a></td>
			   					</s:else>
				   			</s:else>
			   		</s:else>		
				  </s:else>
				  <td><s:property value="transactionDate" /></td>
				   <td><s:property value="amount" /></td>
				  <td><s:property value="orgName" /></td>
				  <s:if test="%{orgType != 'PLAYER' && orgType != 'GOVT' && orgType != 'OLA_DISTRIBUTOR'}">
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
	           <tr><td colspan="7" align="center"> <s:text name="label.no.rec.to.proess"/></td></tr>
	         </s:else>
	                    
          </table>
         
</div>
<s:div id="naviga" cssStyle="text-align: center" >
	<s:if test=" #session.APP_ORDER_LIST5.size >20 & #session.boreceiptsearch!=null">
 		<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="down" href="bo_rep_searchReceipt_Navigate.action?end=first"><s:text name="label.first"/></s:a><s:a theme="ajax" targets="down" href="bo_rep_searchReceipt_Navigate.action?end=Previous" > <s:text name="label.pre"/></s:a></s:if><s:else><s:text name="label.first.pre"/></s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/5)*5)"><s:text name="label.next.last"/></s:if><s:else><s:a theme="ajax" targets="down" href="bo_rep_searchReceipt_Navigate.action?end=Next" ><s:text name="label.next"/></s:a><s:a theme="ajax" targets="down" href="bo_rep_searchReceipt_Navigate.action?end=last"><s:text name="label.last"/></s:a></s:else>      
    </s:if>
</s:div>

	
</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_rep_searchReceipt_SearchResult.jsp,v $
$Revision: 1.3 $
</code>