<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/inventoryMgmt/agent/js/consNonConsSearchInv_agt.js" />
	<s:head theme="ajax" debug="false"/>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
    <div id="top_form">
  	<h3>Search Consumable & Non Consumable Inventory</h3>	
	
	
  	<s:form name="searchgame" action="agt_im_consNonConsSearchInv_search" onsubmit="return _validateForm();"> 		
	 	<s:select name="noInv" id="nonCons_list" theme="simple" cssStyle="display:none" list="#session.INV_NAME_LIST" />
	 	<s:select name="noInv" id="cons_list" theme="simple" cssStyle="display:none" list="#session.CONS_INV_NAME_LIST" />
	
	 	<s:select name="noInv" id="brand_list" theme="simple" cssStyle="display:none" list="#session.BRAND_NAME_LIST" />
	  	<s:select name="noInv" id="model_list" theme="simple" cssStyle="display:none" list="#session.MODEL_NAME_LIST" />
	  	<s:select name="noInv" id="model_spec_list" theme="simple" cssStyle="display:none" list="#session.MODEL_SPEC_LIST" />
	 	<div id="err_div"></div>
	 	<table  border="0" cellpadding="2" cellspacing="0" width="500px" >
			
			<tr>
				<td align="right"><label class="label">Owner Type:&nbsp;</label></td>
				<td><s:select name="ownerType" id="owner_type" theme="simple" value="'ALL'" list="#{'ALL':'ALL', 'RETAILER':#application.TIER_MAP.RETAILER.toUpperCase()}" onchange="changeOwnerType(this)" cssClass="option" /></td>
			</tr>
			
			<tr>
					<td>
					<div id="retlistDiv" style="display: none">
								<table>
								<s:select name="retOrgId" id="ret_org_list"
									label="Select %{#application.TIER_MAP.RETAILER} Organization" list="{}"
									cssClass="option" headerKey="-1"
									headerValue="--Please Select--" 
									required="true" />
								</table>
								
								</div>
				</td>	</tr>	
			
			
			<tr>
				<td align="right"><label class="label">Inv Type:&nbsp;</label></td>
				<td>
					<s:radio list="#{'NON_CONS':'Non Consumable', 'CONS':'Consumable'}" theme="simple" value="'NON_CONS'" label="Inv Type" name="invType" onclick="invTypeShow(this.value)"/> 
				</td>
			</tr>
						
			<tr>
				<td align="right"><label class="label">Inv Name:&nbsp;</label></td>
				<td><s:select name="invId" id="inv_id" theme="simple" label="Inv Name" headerKey="-1" headerValue="--Please Select--" list="#session.INV_NAME_LIST" cssClass="option" onchange="fillBrandNModel(this.value)" /></td>
			</tr>		
									
			<tr>
				<td align="right"><label id="brand_label" style="display:block" class="label">Brand Name:&nbsp;</label></td>
				<td><s:select name="brandId" id="brand" cssStyle="display:block" theme="simple" headerKey="-1" headerValue="--Please Select--" list="{}" cssClass="option" onchange="fillSelBox(this.value, 'model', 'model_list');_id.i('downResult', '');_id.i('lastDown', '');" />	</td>
			</tr>	
			
									
			<tr>
				<td align="right"><label id="model_label" style="display:block" class="label">Model Name:&nbsp;</label></td>
				<td>
					<s:select name="modelId" id="model" theme="simple" headerKey="-1" headerValue="--Please Select--" list="{}" cssClass="option" />					
				</td>
			</tr>	
			
			<tr>
				<td align="right"><label class="label">Count:&nbsp;</label></td>
				<td><s:select name="sign" theme="simple"  list="#{'<=':'Less Than Equal To','>=':'Greater Than Equal To','==':'Equal To'}" cssClass="option" />	&nbsp;<s:textfield theme="simple" id="count_text" value="" size="10" name="count" /> </td>
			</tr>
				
			<tr>
			<table width="21%">
			<tr>
				<td align="left" colspan="2" width="50%"><s:submit value="Search" targets="downResult" theme="ajax" cssStyle="text-align:center" cssClass="button" /></td>				
		         <td width="50%">&nbsp;</td>
		    </tr>
		    </table>
			</tr>
		</table>
	 

	 
	</s:form> 
		
		<div id="downResult" style="text-align: left;width: 100%" ></div>
		<br/>
		<div id="lastDown" style="text-align: left;width: 90%" ></div>
		
	</div></div>
	
 	 
 	 

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_im_consNonConsSearchInv_menu.jsp,v $
$Revision: 1.2 $
</code>