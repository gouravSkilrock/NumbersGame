<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<br />
<br />
    <div align="center">
    	<h3>
		<u>USSD Summary Report&nbsp; From : <s:property
				value="start_date" /> &nbsp;&nbsp;To&nbsp;:&nbsp; <s:property
				value="end_Date" />
		</u>
	</h3>
    </div>
    <div align="left" id="tableDataDiv">
		 <table width="100%" border="1" cellpadding="3" cellspacing="0"	bordercolor="#CCCCCC" align="center">
		  
		       <tr>
				       <th rowspan="2">
				    	    Merchant Name
				       </th>
				       <s:iterator value="merchantwiseSaleWinningBean.gameList">
					       
						       <th colspan="2">
						      	   <s:property />
						       </th>
					      
					   </s:iterator>
					   <th colspan="2">
						  	Total
					   </th>
		       </tr>
		       <tr>
				       <s:iterator value="merchantwiseSaleWinningBean.gameList">
						       <th>
						       	  	Sale
						       </th>
						       <th>
						         	Winning
						       </th>
					   </s:iterator>
					   <th>
					     	Sale
					   </th>
					   <th>
					    	Winning
					   </th>
		       </tr>
		       <s:set name="dataMap" value="merchantwiseSaleWinningBean.dataMap"/>
		      
		       <s:iterator value="#dataMap">
		        <s:set name="saleWinDataMap" value="%{value}"/>
			       <tr class="startSortable">
					       <td>
					       <s:if test="%{key  == 'MTN'}">
								<!--<s:a href="bo_dg_mtn_walletwise_sale_winning_report_search.action?start_date=%{start_date}&end_Date=%{end_Date}" targets="result" theme="ajax">
									<s:property value="%{key}"/>
								</s:a>-->
								<s:url id="url" action="bo_dg_mtn_walletwise_sale_winning_report_search">
									<s:param name="start_date" value="%{start_date}"></s:param>
									<s:param name="end_Date" value="%{end_Date}"></s:param>
								</s:url>
								<s:a href="%{url}" theme="ajax" targets="result"><s:property value="%{key}"/></s:a>
							</s:if>
							<s:else><s:property value="%{key}"/></s:else>
					        	
					       </td>
					       <s:set name="totalSale" value="0"/>
					       <s:set name="totalWin" value="0"/>
					       <s:iterator value="#saleWinDataMap">
							        <s:set name="bean" value="%{value}"/>
								       <s:set name="saleAmount" value="#bean.saleAmt"/>
								       <td>
								         	<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("saleAmount")) %>	
								       </td>
								       <s:set name="totalSale" value="#totalSale+#bean.saleAmt"/>
								       <s:set name="winAmount" value="#bean.winAmt"/>
								       <td>
								         	<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("winAmount")) %>
								       </td>
								       <s:set name="totalWin" value="#totalWin+#bean.winAmt"/>
					       </s:iterator>
					       <td>
					        		<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalSale")) %>
					       </td>
					       <td>
					     			  <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("totalWin")) %>
					       </td>
			       </tr>
		       </s:iterator>
		    
	      </table>
   </div>
 