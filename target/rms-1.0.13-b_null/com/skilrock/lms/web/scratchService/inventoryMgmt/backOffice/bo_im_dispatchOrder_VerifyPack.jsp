
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<table border="1">
	<s:iterator value="packList" status="pack">


		<tr>
			<s:set name="packIndex" value="#pack.index + 1" />

			<td>
				<s:div id="packDiv%{packIndex}"></s:div>
			</td>
			<td>
				Pack Number:
				<s:textfield theme="simple" id="%{packIndex}" name="packNo"
					value="%{packNumber}" onchange="verifyBOPack(this.value,this.id)" />
			</td>


			<s:if test="%{!isValid}">
				<td>
					<s:property value="%{status}" />
				</td>
				<td>
					<input type=hidden name="invalidPack" />
				</td>
			</s:if>
		</tr>


		<tr>

		</tr>
	</s:iterator>

	<s:hidden name="hidDispatchPacksBooks" value="%{noOfBooksToDispatch}" />
</table>



<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_dispatchOrder_VerifyPack.jsp,v $
$Revision: 1.3 $
</code>