<jsp:include page="/dynamicIncMenu.jsp" flush="true"/>
<s:if test="%{#application.COUNTRY_DEPLOYED=='SAFARIBET'}">
	<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/safaribetbody.css"
			"type="text/css" />
</s:if>
