




<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>


<s:div id="boBookListDiv">
<table border="0" id="bookTable">
<s:if test="%{bookList.size()>=1}">
<s:iterator value="bookList" status="book">
		<tr>
			<s:set name="bookIndex" value="#book.index + 1" />

			<td>
				Book Number:
				<s:textfield theme="simple" id="%{bookIndex}" name="bookNo"
					value="%{bookNumber}" onkeypress="return checkNum()"  onchange="isVerified()"/>
			</td>

			<td>
				<s:div id="div%{bookIndex}"></s:div>
			</td>

			<s:if test="%{!isValid}">
				<td>
					<s:property value="%{status}" />
				</td>
				<td>
					<input type=hidden name="invalidBook" />
				</td>
			</s:if>
		</tr>
	</s:iterator>
</s:if>
<s:else>
<s:iterator value="{1,2,3,4,5}"  status="book">
		<tr>
			<s:set name="bookIndex" value="#book.index + 1" />

			<td>
				Book Number:
				<s:textfield theme="simple" id="%{bookIndex}" name="bookNo"
					value="%{bookNumber}" onkeypress="return checkNum()" onchange="isVerified()"/>
			</td>

			<td>
				<s:div id="div%{bookIndex}"></s:div>
			</td>
		</tr>
	</s:iterator>

</s:else>
	<s:hidden name="hidDispatchBooks" value="%{noOfBooksToDispatch}"/>
</table>
</s:div>
 							<s:div id="bookSeriesDiv">
							<table id="bookSeriesTable">
 								<s:if test="%{#session.BOOK_SERIES_ALL.size()<1}">
 								 <tr>
					                    <td>
						                    Book Number From:
						                    	<s:textfield theme="simple" name="bookNoFrom" value="%{bookNbrFrom}" id="bookNoFrom" onkeypress="return checkNum()" onchange="isVerified()"/>
									   </td>
									   
									   <td>
						                    To:
						                    	<s:textfield theme="simple" name="bookNoTo" value="%{bookNbrTo}" id="bookNoTo" onkeypress="return checkNum()" onchange="isVerified()"/>
									   </td>
									   <td><s:div name="serStatusDiv" id="divSe1">
											<s:property value="%{status}"/>
											</s:div>
									 </td>
			                 </tr> 
			                 </s:if>
			                 <s:else>
 								<s:iterator value="#session.BOOK_SERIES_ALL" status="bookSe">
 								<s:set name="bookSeIndex" value="#bookSe.index + 1" />
			                   <tr>
					                    <td>
						                    Book Number From:
						                    	<s:textfield theme="simple" name="bookNoFrom" value="%{bookNbrFrom}" id="bookNoFrom" onkeypress="return checkNum()" onchange="isVerified()"/>
									   </td>
									   
									   <td>
						                    To:
						                    	<s:textfield theme="simple" name="bookNoTo" value="%{bookNbrTo}" id="bookNoTo" onkeypress="return checkNum()" onchange="isVerified()"/>
									   </td>
										<td><s:div name="serStatusDiv" id="divSe%{bookSeIndex}">
											<s:property value="%{status}"/>
											</s:div>
									 </td>
										
			                 </tr> 
			                 
			                 </s:iterator>
			                 </s:else>
                   			 </table>
							</s:div>


<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_assignOrder_VerifyBookSeries.jsp,v $
$Revision: 1.3 $
</code>