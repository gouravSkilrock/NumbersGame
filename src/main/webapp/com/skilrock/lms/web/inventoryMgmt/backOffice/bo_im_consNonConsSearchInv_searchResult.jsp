<%@page import="com.skilrock.lms.common.utility.GetDate"%><%@ taglib
	prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
	<head>
		<s:head theme="ajax" />
	</head>

	<body>

		<table width="100%" border="1" cellpadding="3" cellspacing="0"
			bordercolor="#CCCCCC" align="center">
			<tr>
				<th colspan="5" align="center">
					<s:text name="label.add.inv.detail" />
				</th>
			</tr>
			<tr>
				<th align="center">
					<s:text name="label.owner.name" />
				</th>
				<th align="center">
					<s:text name="label.owner.type" />
				</th>
				<th align="center">
					<s:text name="label.inv.type" />
				</th>
				<th align="center">
					<s:text name="label.inv.name" />
				</th>
				<th align="center">
					<s:text name="label.count" />
				</th>
			</tr>

			<s:if test="#session.INV_DET_LIST.size>0">
				<s:iterator id="beanCart" value="#session.INV_DET_LIST_NAV">
					<tr>
						<td align="left">
							<s:property value="ownerName" />
						</td>
						<td align="left">
							<s:if test="%{ownerType eq 'BO'}">
								<s:text name="BOUser" />
							</s:if>
							<s:elseif test="%{ownerType eq 'AGENT'}">
								<s:text name="AGENT" />
							</s:elseif>
							<s:elseif test="%{ownerType eq 'RETAILER'}">
								<s:text name="RETAILER" />
							</s:elseif>
						</td>
						<td align="center">
							<s:property value="invType" />
						</td>
						<td align="right">
							<s:a targets="lastDown" theme="ajax"
								href="bo_im_consNonConsSearchInv_detail.action?ownerId=%{ownerId}& invId=%{invId}& invType=%{invType}& brandId=%{brandId}& modelId=%{modelId}"
								onclick="this.style.color='blue'">
								<s:property value="invName" />
							</s:a>
						</td>
						<td align="right">
							<s:property value="count" />
						</td>
					</tr>
				</s:iterator>
			</s:if>
			<s:else>
				<tr>
					<td colspan="9" align="center">
						<s:text name="msg.no.result" />
					</td>
				</tr>
			</s:else>
		</table>


		<s:div id="naviga" cssStyle="text-align: center;width: 100%">
			<s:if test=" #session.INV_DET_LIST.size >10 ">
				<s:if test="#session.startValueOrderSearch!=0">
					<s:a theme="ajax" targets="downResult"
						href="bo_im_consNonConsSearchInv_navigate.action?end=first"><s:text name="label.first" /></s:a>
					<s:a theme="ajax" targets="downResult"
						href="bo_im_consNonConsSearchInv_navigate.action?end=Previous"> <s:text name="label.pre" /></s:a>
				</s:if>
				<s:else><s:text name="label.first.pre" /></s:else>
				<s:if
					test="#session.startValueOrderSearch==((#session.INV_DET_LIST.size/10)*10)"><s:text name="label.next.last" /></s:if>
				<s:else>
					<s:a theme="ajax" targets="downResult"
						href="bo_im_consNonConsSearchInv_navigate.action?end=Next"><s:text name="label.next" /></s:a>
					<s:a theme="ajax" targets="downResult"
						href="bo_im_consNonConsSearchInv_navigate.action?end=last"><s:text name="label.last" /></s:a>
				</s:else>
			</s:if>
		</s:div>



	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_im_consNonConsSearchInv_searchResult.jsp,v $ $Revision:
	1.1.8.5 $
</code>