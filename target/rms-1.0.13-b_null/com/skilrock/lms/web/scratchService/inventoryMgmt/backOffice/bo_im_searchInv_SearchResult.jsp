	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">	
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />	
		<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>	
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
		<s:head theme="ajax" debug="false"/>
	</head>

<body >


	<div id="wrap">
  	<div id="top_form">
  

        <table width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
	       <s:if test="#session.retailerOnline!=null">
	        <tr><th align="center" colspan="9"> <s:text name="btn.srch.res"/></th></tr>
	       </s:if>
	       <s:else>
	        <tr><th align="center" colspan="9"> <s:text name="btn.srch.res"/></th></tr>
	       </s:else>
	       
	        <s:if test="#session.searchResultBO.size>0">
	           <tr>
	          <th nowrap="nowrap" colspan="2" align="center"><s:text name="label.game.name"/></th>	
			  
			  <th align="center"><s:text name="label.pwt.end.date"/></th>
			  <th align="center"><s:text name="label.game.status"/></th>
			  <th align="center"><s:text name="label.total.books"/></th>
			  <th align="center"><s:text name="label.books.with.bo"/></th>
			  <th align="center"><s:text name="label.books.with"/> <s:property value="#application.TIER_MAP.AGENT" />s</th>
			  <th align="center"><s:text name="label.books.with"/> <s:property value="#application.TIER_MAP.RETAILER" />s</th>
			  <s:if test="#session.retailerOnline!=null">
			    <th><s:text name="label.active"/></th>
			  </s:if>
             </tr>
	          <s:iterator id="beanCart" value="#session.searchResultBO">
	            
	            
	            <tr>
	              <td nowrap="nowrap" colspan="2" align="left"><s:a targets="down1" theme="ajax" href="bo_im_searchInv_Detail.action?gameid=%{gameid}& gamenumber=%{gamenbr}& gamename=%{gamename}& saleenddate=%{saleenddate}" ><s:property value="gamenbr" />-<s:property value="gamename" /></s:a></td>
				  
				  <td><s:property value="pwtenddate" /></td>
				  <td><s:property value="gamestatus" /></td>
				  <td align="right"><s:property value="totalbooks" /></td>				
				  <td align="right"><s:a targets="down2" theme="simple" href="bo_im_bookWiseInvDet_Menu.action" ><s:property value="bookbo" /></s:a></td>
				  <td align="right"><s:a targets="down2" theme="simple" href="bo_im_bookWiseInvDet_Menu.action"><s:property value="bookagent" /></s:a></td>
				  <td align="right"><s:a targets="down2" theme="simple" href="bo_im_bookWiseInvDet_Menu.action"><s:property value="bookretailer" /></s:a></td>				
				  <s:if test="#session.retailerOnline!=null">
				    <td  align="right"><s:a targets="down2" theme="simple" href="bo_im_bookWiseInvDet_Menu.action"><s:property value="activebooks" /></s:a></td>
			  </s:if>
                </tr>	
	            </s:iterator>
             </s:if>
	        <s:else>
	          <tr><s:if test="#session.retailerOnline!=null">
	            <td colspan="9" align="center"><s:text name="msg.no.result"/> </td>
				  </s:if>
	            <s:else><td colspan="8" align="center"> <s:text name="msg.no.result"/></td></s:else>
	            </tr>
             </s:else>
          </table>

<s:div id="naviga" cssStyle="text-align: center" >

<s:if test="%{#session.SearchResultsAvailable != 'No'}">
	<s:if test=" #session.APP_ORDER_LIST5.size >5 ">
 		
	<s:if test="#session.startValueOrderSearch!=0"><s:a theme="ajax" targets="down" href="bo_im_searchInv_Navigate.action?end=first"> <s:text name="label.first"/></s:a><s:a theme="ajax" targets="down" href="bo_im_searchInv_Navigate.action?end=Previous" >  <s:text name="label.pre"/></s:a></s:if><s:else> <s:text name="label.first.pre"/></s:else>    
    <s:if test="#session.startValueOrderSearch==((#session.APP_ORDER_LIST5.size/5)*5)"> <s:text name="label.next.last"/></s:if><s:else><s:a theme="ajax" targets="down" href="bo_im_searchInv_Navigate.action?end=Next" > <s:text name="label.next"/></s:a><s:a theme="ajax" targets="down" href="bo_im_searchInv_Navigate.action?end=last"> <s:text name="label.last"/></s:a></s:else>      
    
	</s:if>
</s:if>
</s:div>
<div id="down1" style="text-align: center"></div>
<div id="down2" style="text-align: center"></div>	
<div id="down3" style="text-align: center"></div>
</div>
	</div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_searchInv_SearchResult.jsp,v $
$Revision: 1.3 $
</code>