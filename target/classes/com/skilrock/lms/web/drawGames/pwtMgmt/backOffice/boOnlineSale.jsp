<%@ page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<s:head theme="ajax" debug="false"/>
 <script>
 
   //get the agent list from database
	  	function getAgents(url, targetDiv, orgType) {
	  		_id.i('retlist', "");
			_id.i("retDiv", "");
			//_id.i("down", "");	
			//alert(orgType);   			  		
	  		if(orgType=='AGENT' || orgType=='RETAILER') {
				var ajaxCall = _ajaxCall(url);
				//alert(ajaxCall.data);
				_id.i(targetDiv,"&nbsp;&nbsp;"+ajaxCall.data);
				_id.i("agentDiv","%{#application.TIER_MAP.AGENT} Name:");		
			 }
			 else {
			 	 _id.i(targetDiv, "");
			 	 _id.i("agentDiv", "");				 	 	 
			 }			 
	  	}
 
 
 </script>
</head>

<body>
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	 <div id="wrap">
        <div id="top_form">
     
         <s:form>
             <table>
                <s:select list="" label="%{#application.TIER_MAP.AGENT}: " name="agentOrgName"></s:select>
                <s:textfield name="amount"></s:textfield>
                <s:submit value="submit"></s:submit>
             </table>
         
         </s:form>
     
     
        </div>
     </div>
	
	
</body>

</html>