<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="%{#session.isOffRetLogIn}">
	<h3>
		<s:text name="msg.usr.comm.var.detail.not.edit.bcs.usr.stat.login.and.offline"/>
	</h3>
</s:if>
<s:else>
	<%="error"%>
</s:else>