<%@ taglib prefix="s" uri="/struts-tags"%>
<table border="2" cellpadding="2" cellspacing="0" width="100%">
	<tr>
		<th>
			S.No.
		</th>
		<th>
			User Name
		</th>
		<th>
			Organization Name
		</th>
		<th>
			Status
		</th>
		<th>
			Reset 
		</th>
		<th colspan="2">
			New Password
		</th>
		<th>
			Log Out
		</th>
		<th width="20%">
			Result
		</th>
	</tr>
	<s:iterator value="#session.USER_LIST" status="index">
		<tr>
			<td>
				<s:property value="%{#index.index+1}" />
			</td>
			<td>
				<s:property value="userName" />
			</td>
			<td>
				<s:property value="orgName" />
			</td>
			<td>
				<s:property value="orgStatus" />
			</td>
			<td>
				<s:a href="admin_resetPasswordAnyUser.action?userId=%{userId}&emailId=%{emailId}&firstName=%{firstName}&lastName=%{lastName}&userName=%{userName}" theme="ajax" targets="res%{#index.index+1}" id="href%{#index.index+1}">Reset Password</s:a>
			</td>
			<td>
				<s:textfield theme="simple" name="newPass" id="newPass%{#index.index+1}" value="" size="12"/>
			</td>
			<td>
				<input type="button" onclick="updatePass('newPass<s:property value="%{#index.index+1}"/>');" value="Submit" class="button">
			</td>
			<td>
				<s:a href="admin_logoutAnyUser.action?userName=%{userName}" theme="ajax" targets="res%{#index.index+1}">LogOut</s:a>
			</td>
			<td><div id="res<s:property value="%{#index.index+1}"/>"  style="color:red;"></div>	
			</td>
		</tr>
	</s:iterator>
</table>
<code id="headId" style="visibility: hidden">
	$RCSfile: admin_setResetPassAnyUserSearch.jsp,v $ $Revision: 1.1.2.1 $
</code>