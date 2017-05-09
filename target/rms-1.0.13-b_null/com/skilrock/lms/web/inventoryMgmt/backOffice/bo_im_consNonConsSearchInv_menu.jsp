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
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />	
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<script>var projectName="<%=request.getContextPath() %>"</script>
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/inventoryMgmt/backOffice/js/consNonConsSearchInv.js" />
	<s:head theme="ajax" debug="false"/>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
    <div id="top_form">
  	<h3><s:text name="label.srch.cons.noncons.inv" /></h3>	
	
	
  	<s:form name="searchgame" action="bo_im_consNonConsSearchInv_search" onsubmit="return _validateForm();"> 		
	 	<s:select name="noInv" id="nonCons_list" theme="simple" cssStyle="display:none" list="#session.INV_NAME_LIST" />
	 	<s:select name="noInv" id="cons_list" theme="simple" cssStyle="display:none" list="#session.CONS_INV_NAME_LIST" />
	 	<s:select name="noInv" id="brand_list" theme="simple" cssStyle="display:none" list="#session.BRAND_NAME_LIST" />
	  	<s:select name="noInv" id="model_list" theme="simple" cssStyle="display:none" list="#session.MODEL_NAME_LIST" />
	  	<s:select name="noInv" id="model_spec_list" theme="simple" cssStyle="display:none" list="#session.MODEL_SPEC_LIST" />
	 	<div id="err_div"></div>
	 	<table  border="0" cellpadding="2" cellspacing="0" width="500px" >
			
			<tr>
				<td align="right"><label class="label"><s:text name="label.owner.type" />:&nbsp;</label></td>
				<td><s:select name="ownerType" id="owner_type" theme="simple" value="'ALL'" list="#{'ALL':getText('label.ALL'), 'BO':getText('BOUser'),'AGENT':getText('AGENT'), 'RETAILER':getText('Retailer')}" onchange="changeOwnerType(this)" cssClass="option" /></td>
			</tr>
			
			<tr>
				
				<td colspan="2">
				<div id="agentlistDiv" style="margin-left:20px;display: none">
								<table style="margin-left:10px">
									<!--<s:select name="agtOrgId" id="agentOrgId"
									label="Select %{#application.TIER_MAP.AGENT} Organization" list="{}" cssClass="option"
									headerKey="" headerValue="--Please Select--"
									onchange="getData(this.value);"  />
									--><s:select name="agtOrgId" id="agentOrgId"
									key="label.slct.agnt.org" list="{}" cssClass="option"
									headerKey="" headerValue="%{getText('label.please.select')}"
									onchange="getData(this.value);"  />
									</table>
								</div>
				
				</td>
			</tr>	
			
			<tr>
				
				<td colspan="2">
				<div id="retlistDiv" style="display: none">
								<table>
								<!--<s:select name="retOrgId" id="retOrgId"
									label="Select %{#application.TIER_MAP.RETAILER} Organization" list="{}"
									cssClass="option" headerKey="-1"
									headerValue="--Please Select--" 
									required="true" />
									--><s:select name="retOrgId" id="retOrgId"
									key="label.slct.rtailr.org" list="{}"
									cssClass="option" headerKey="-1"
									headerValue="%{getText('label.please.select')}" 
									required="true" />
									
								</table>
				</div>				
				</td>
			</tr>	
			
			
			<tr>
				<td align="right"><label class="label"><s:text name="label.inv.type" />:&nbsp;</label></td>
				<td>
					<s:radio list="#{'NON_CONS':getText('label.nonconsum'), 'CONS':getText('label.consum')}" theme="simple" value="'NON_CONS'" key="label.inv.type" name="invType" onclick="invTypeShow(this.value)"/> 
				</td>
			</tr>
						
			<tr>
				<td align="right"><label class="label"><s:text name="label.inv.name" />:&nbsp;</label></td>
				<td><s:select name="invId" id="inv_id" theme="simple" key="label.inv.name" headerKey="-1" headerValue="%{getText('label.please.select')}" list="#session.INV_NAME_LIST" cssClass="option" onchange="fillBrandNModel(this.value)" /></td>
			</tr>		
									
			<tr>
				<td align="right"><label id="brand_label" style="display:block" class="label"><s:text name="label.brand.name" />:&nbsp;</label></td>
				<td><s:select name="brandId" id="brand" cssStyle="display:block" theme="simple" headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}" cssClass="option" onchange="fillSelBox(this.value, 'model', 'model_list');_id.i('downResult', '');_id.i('lastDown', '');" />	</td>
			</tr>	
			
									
			<tr>
				<td align="right"><label id="model_label" style="display:block" class="label"><s:text name="label.model.name" />:&nbsp;</label></td>
				<td>
					<s:select name="modelId" id="model" theme="simple" headerKey="-1" headerValue="%{getText('label.please.select')}" list="{}" cssClass="option" />					
				</td>
			</tr>	
			
			<tr>
				<td align="right"><label class="label"><s:text name="label.count" />:&nbsp;</label></td>
				<td><s:select name="sign" theme="simple"  list="#{'<=':getText('label.lt.equal.to'),'>=':getText('label.gt.equal.to'),'==':getText('label.equal.to')}" cssClass="option" />	&nbsp;<s:textfield theme="simple" id="count_text" value="" size="10" name="count" /> </td>
			</tr>
				
			<tr>
			<table width="21%">
			<tr>
				<td align="left" colspan="2" width="50%"><s:submit key="btn.srch" targets="downResult" theme="ajax" cssStyle="text-align:center" cssClass="button" /></td>				
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
$RCSfile: bo_im_consNonConsSearchInv_menu.jsp,v $
$Revision: 1.4 $
</code>