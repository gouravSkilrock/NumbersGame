<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page import="java.util.List" %>

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
	<script src="<%=request.getContextPath() %>/com/skilrock/lms/web/inventoryMgmt/backOffice/js/consNonConsSearchInv.js" />
	<s:head theme="ajax" debug="false"/>
	
	<script type="text/javascript">
	function showHideData(ids){
	 if (document.getElementById(ids).checked == false) {
	 document.getElementById("CheckboxDiv").style.display='none';
	 document.getElementById("ModelDiv").style.display='none';
	 document.getElementById("lastDown").style.display='none';
	 document.getElementById("downResult").style.display='none';
	 
	}else 
	{
	document.getElementById("CheckboxDiv").style.display='block';
	 document.getElementById("ModelDiv").style.display='block';
	}
	}
 function checkmodel(btn) {
 var total = 0;
		var dataarr=new Array();
		var max =  document.getElementsByName("modelname").length;
		var total =btn.charAt(10);
		//alert(total);
		
		//for  (var idx =  1; idx <=  max; idx++) {
			var str="modelname-"+total;	
      		 if (document.getElementById(str).checked == false) {
      		 show_hide_column(total,false);
      			// dataarr[total]=document.getElementById(str).value;
       			// total += 1;    
       		  }
       		  else{
       		  show_hide_column(total,true);
       		  }
         //}	
         
 }
  function show_hide_column(col_no, do_show) {
    var stl;
    
    if(do_show)
    {   
         if(document.all) stl='block';
           else  stl = 'table-cell';
    }
      else         stl = 'none';
  

    
     
    var tbl  = document.getElementById('id_of_table');
    var rows = tbl.getElementsByTagName('tr');

    for (var row=0; row<rows.length;row++) {
      var cels = rows[row].getElementsByTagName('td')
      cels[col_no].style.display=stl;
     
    }
  }

function showRetailer()
{
if(document.getElementById("downResult").style.display='block'){
document.getElementById("downResult").style.display='none';
}
}
function showModel(){
if(document.getElementById("lastDown").style.display='block'){
document.getElementById("lastDown").style.display='none';
}

}
</script>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<s:select name="noInv" id="model_list" theme="simple" cssStyle="display:none" list="#session.MODEL_NAME_MAP" />
	<div id="wrap">
    <div id="top_form">
  	<h3><s:text name="label.srch.cons.noncons.inv"/></h3>	
	
	
  		
	 	
	 	<div id="err_div"></div>
	 	
		<s:property value="%{checkedModelNameArr}" />
		
		<s:checkbox id="nonconsterminal" name="nonconsterminal" key="label.nonconsum" onclick="showHideData(this.id);" />
		<div id="CheckboxDiv" style="display: block;">
	 	<s:checkboxlist list="#session.MODEL_NAME_MAP"   value="#session.MODEL_NAME_MAP"  name="modelname"  id="modelname"  onclick="checkmodel(this.id);"></s:checkboxlist>
	 	</div>
	 	
				

				<div id="ModelDiv" style="display: block;" >
	 	 <table class="sample" id='id_of_table' width="100%" border="1" cellpadding="3" cellspacing="0" bordercolor="#CCCCCC" align="center">
		
		<tr>
		<td ><s:text name="label.org.name"/></td>
		
		<s:if test="#session.MODEL_NAME_MAP.size>0">
		
		<s:iterator id="MODEL_NAME_MAP" value="#session.MODEL_NAME_MAP">
			
			<td cellspacing="20" cellpadding="20" ><s:property value="%{value}"/></td>
		</s:iterator>
		</s:if>
		</tr>
		
		
			 <s:if test="#session.INV_DET_MAP.size>0">
		 
			<s:iterator value="#session.INV_DET_MAP">
			<s:if test="%{#key.split('Nxt')[1]=='BO'}">
				<s:set name="orgId" value="%{key.split('Nxt')[2]}" />
			</s:if>
			<s:else>
			<s:set name="orgId" value="%{key.split('Nxt')[1]}" />
			</s:else>
		
			<s:set name="ownName" value="%{key}" />
			<tr>
			<td onclick="showRetailer();">
			<s:if test="%{#ownName.split('Nxt')[1]=='BO'}">
			<s:property  value="%{Key.split('Nxt')[0]}"/>
			</s:if>
			
			<s:else>
			<s:a href="bo_im_consNonConsSearchRetailer.action?orgId=%{#orgId}" theme="ajax" targets="lastDown" id="bo_im_consNonConsSearchRetailer">
			<s:property  value="%{Key.split('Nxt')[0]}"/>
			</s:a></s:else>
			</td>
			
			
							<s:iterator  value="%{value}" >	
							<s:set name="modelCount" value="%{value}"></s:set>
							<td onclick="showModel();">	
							<s:if test="%{#ownName.split('Nxt')[1]=='BO'}">
							<s:property  value="%{value}"/>
							</s:if>
					<s:elseif test="%{#modelCount==null}">N.A
					</s:elseif>
							<s:else>
					<s:a href="bo_im_consNonConsSearchModelSerial.action?modelId=%{key}&orgId=%{#orgId}" theme="ajax" targets="downResult" id="bo_im_consNonConsSearchModelSerial">
				<s:property  value="%{value}"/></s:a></s:else>
						
						
						</td>		
						</s:iterator>
			
			
			
			
			 </tr>
			</s:iterator>
	
		</s:if> 
			
		
			
		<s:else>
			<tr>
				<td colspan="9" align="center"><s:text name="msg.no.result"/> </td>
			</tr>
		</s:else>
	 </table></div>
		<div id="downResult" style="display: none" style="text-align: left;width: 100%" ></div>
		<br/>
		<div id="lastDown" style="display: none" style="text-align: left;width: 90%" ></div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_consNonConsSearchTerminalInv_searchResult.jsp,v $
$Revision: 1.3 $
</code>