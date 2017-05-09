<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

	<head>



		<meta http-equiv="Content-Type"
			content="text/html; charset=iso-8859-1" />

		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			 type="text/css"/ >

		<script>


function addBookSeriesRow(){
	var table = document.getElementById("bookSeriesTable");
	var lastRow = table.rows.length;
	var row = table.insertRow(lastRow);
	row.setAttribute("colspan", "2");
	var cellLeft = row.insertCell(0);
	cellLeft.innerHTML='<td>Book Number From:&nbsp;<input type="text" name="bookNoFrom" onkeypress="return checkNum()"  onchange="isVerified()"/></td>';
	var cellLeft2 = row.insertCell(1);
	cellLeft2.innerHTML='<td>To:&nbsp;<input type="text" name="bookNoTo" onkeypress="return checkNum()"  onchange="isVerified()"/></td><td><div name="serStatusDiv" id="divSe'+(parseInt(lastRow)+1)+'"></div></td>';
}

function addBookRowStatic(){
var table = document.getElementById("bookTable");
	var lastRow = table.rows.length;
	var row = table.insertRow(lastRow);
	row.setAttribute("colspan", "2");
	var cellLeft = row.insertCell(0);
	cellLeft.setAttribute("nowrap", "true");
	cellLeft.setAttribute("colspan", "4");
	cellLeft.innerHTML='Book Number :<input type="text"	name="bookNo" onkeypress="return checkNum()"  onchange="isVerified()"/><td><div id="div'+(parseInt(lastRow)+1)+'"></div></td>';
//alert(cellLeft.innerHTML);
}

function addBookRow(url,div) {
   mainDiv=div;
   
   
   var isBlank = checkBookBlankValues();
   if(isBlank){
     return false;
   }
   
   var book = document.getElementsByName('bookNo');
   
   //alert("bbb:" + book);
   
    var booksVal = "";
    //alert("Initial Length:" + booksVal.length);
    for(i=0; i< book.length; i++){
      if(book[i].value != ""){
      booksVal = booksVal + book[i].value + ",";
      }else{
       booksVal = booksVal + " " + ",";
      }
    }
    
    var length = booksVal.length;
   
    if(length > 1){
    booksVal = booksVal.substring(0,length-1);
    url = url +"?bookNbr = " + booksVal;
    } else{
    url = url +"?bookNbr = " + document.forms[0].bookNo;
    }
   _resp  = _ajaxCall(url);
	if(_resp.result){
      _id.i(mainDiv,_resp.data);
      } else {
        alert("Problem: ");
        }
    
    return true;
  }

  function checkBookBlankValues(){
  
   var book = document.getElementsByName('bookNo');
   
   
   
   //alert("In books blank check" + book.length);
   
   
    for(i=0; i<book.length; i++){
      //alert(trim(book[i].value) + "x");
      if( trim(book[i].value) == ""){
       //alert('Blank Value Present');
       document.getElementById('blankBooksDiv').innerHTML = '<font color=red>Please fill in all the books before using Add Book</font>';
       return true;
   	   } else {
   	   document.getElementById('blankBooksDiv').innerHTML = '';
   	   }
    
    
  }
  return false;
  }
  
  function trim(stringToTrim) {
	return stringToTrim.replace(/^\s+|\s+$/g,"");
}
  
    
  
</script>


		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>


		<script  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/dsptchOrder.js"></script>


		<s:head theme="ajax" debug="false" />
	</head>



	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">

			<div id="top_form">
				<h3>
					Order Process : Book Dispatching
				</h3>


				<s:form name="dspchOrderGame">

					<p>
						&nbsp;
					</p>
					<table width="86%" border="1" align="center" cellpadding="3"
						cellspacing="0" bordercolor="#CCCCCC">
						<tr>
							<td>
								<table cellpadding="3">
									<tr>
										<th>
											Order Number :
										</th>
										<td>
											<s:property value="#session.ORDER_ID" />
										</td>
										<th>
											Order Date :
										</th>
										<td>
											<s:property value="#session.ORDER_DATE" />
										</td>
									</tr>
									<tr>
										<th>
											<s:property value="#application.TIER_MAP.AGENT" /> Name :
										</th>
										<td>
											<s:property value="#session.AGT_ORG_NAME" />
										</td>
										<th>
											Address :
										</th>
										<td>
											<s:property value="#session.ORG_ADDR.addrLine1" />
											,
											<s:property value="#session.ORG_ADDR.addrLine2" />
										</td>
									</tr>

									<tr>
										<td></td>
										<td></td>
										<td></td>
										<td>
											<s:property value="#session.ORG_ADDR.city" />
											,
											<s:property value="#session.ORG_ADDR.state" />
										</td>
									</tr>

									<tr>
										<td></td>
										<td></td>
										<td></td>
										<td>
											<s:property value="#session.ORG_ADDR.country" />
										</td>
									</tr>
									<tr>

										<th>
											Credit Limit :
										</th>
										<s:set name="creditLimitStr" value="%{#session.CREDIT_LIMIT}" ></s:set>
										<td>
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("creditLimitStr")) %>
										</td>
										<th>
											Current Credit Amount :
										</th>
										<s:set name="creditAmtStr" value="%{#session.CREDIT_AMT}" ></s:set>
										<td>
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("creditAmtStr")) %>
										</td>
										<th>
											Balance :
										</th>
										<s:set name="availableAmtStr" value="%{#session.AVAILABLE_BALANCE}" ></s:set>
										<td>
											<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("availableAmtStr")) %>
											<s:hidden id="remainingCreditLimit" key="#session.REMAINING_AVAILABLE_CREDIT_AMT"></s:hidden>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>


					<p>
						&nbsp;
					</p>
					<table border="1" align="center" cellpadding="3" cellspacing="0"
						bordercolor="#CCCCCC">

						<tr>
							<th>
								Game Number
							</th>
							<td>
								<s:property id="gameNumber" value="orderdedGame.gameNbr" />
								<script>
								var gameNumber = "<s:property id="gameNumber" value="orderdedGame.gameNbr" />";	
								</script>
							</td>
							<th>
								Game Name
							</th>
							<td>
								<s:property  value="orderdedGame.gameName" />

							</td>

						</tr>


						<tr>
							<th>
								Number of Books Per Pack
							</th>
							<td>
								<s:property value="orderdedGame.nbrOfBooksPerPack" />
							</td>

						</tr>
                        <tr>
							<th>
								Book Price
							</th>
							<td>
								<s:set name="strbookPrice" value="orderdedGame.bookPrice" ></s:set>
								<%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("strbookPrice")) %>
								<s:hidden name="bookPrice" id="bookPrice"
						            value="%{orderdedGame.bookPrice}" />
						            <s:hidden name="agtCommRate" id="agtCommRate"
						            value="%{orderdedGame.agtSaleCommRate}" />
						            <s:hidden name="agtCommVariance" id="agtCommVariance"
						            value="%{orderdedGame.agtGameCommVariance}" />
							</td>
          
                         
						</tr>

						<tr>
							<td>
								<s:div id="dispatchDiv"></s:div>
							</td>
						</tr>

						<tr>
							<th>
								Total Books already With <s:property value="#application.TIER_MAP.AGENT" />
							</th>
							<td>
								<b><s:property value="%{#session.TOTAL_BOOKS}" />
								</b>

							</td>
						</tr>

						<tr>

							<th colspan="2">
								Total Dispatched Books to <s:property value="#application.TIER_MAP.RETAILER" /> by the <s:property value="#application.TIER_MAP.AGENT" />
							</th>
							<td colspan="2">
								<s:property value="#session.TOTAL_BOOKS_WITH_RET_BY_AGT" />
							</td>
						</tr>


						<tr>

							<th colspan="2">
								Total Dispatched Books to <s:property value="#application.TIER_MAP.AGENT" /> by the BO
							</th>
							<td colspan="2">
								<s:property value="#session.TOTAL_BOOKS_FOR_AGT_BY_BO" />
							</td>
						</tr>

						<tr>

							<th>
								Number of Books Approved
							</th>
							<td>
								<s:property value="orderdedGame.nbrOfBooksApp" />
							</td>
							<th>
								Number of Books Already Dispatch
							</th>
							<td>
								<s:property value="orderdedGame.nbrOfBooksDlvrd" />
							</td>
						<tr>
							<th>
								Remaining books To Dispatch
							</th>
							<td>
								<s:property value="orderdedGame.remainingBooksToDispatch" />
							</td>

							<th>
								Ready To Dispatch
							</th>
							<td>
								<s:textfield theme="simple" name="noOfBooksToDispatch" id="noOfBooksToDispatch"
									value="%{noOfBooksToDispatch}" readonly="true" />
							</td>
						</tr>

					</table>

					<p>
						&nbsp;
					</p>
					<p>
						<s:div id="invalidEntryDiv"></s:div>
						
					</p>
					<s:if test="%{!isProceedNext}">

						<table border="1" cellpadding="3" width="86%" align="center"
							cellspacing="0">

							<tr>
								<td>


									<s:div id="boPackListDiv">

										<s:if test="packList != null">

											<table border="0" cellpadding="0" cellspacing="0">
												<s:iterator value="packList" status="pack">


													<tr>
														<s:set name="packIndex" value="#pack.index + 1" />

														<td>
															<s:div id="packDiv%{packIndex}"></s:div>
														</td>

														<td>
															Pack Number:
															<s:textfield theme="simple" id="%{packIndex}"
																name="packNo" value="%{packNumber}"
																onchange="verifyBOPack(this.value,this.id)" onkeypress="return checkNum()"/>
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
											</table>
										</s:if>
									</s:div>



								</td>
							</tr>
						</table>



						<p>
							&nbsp;<s:div id="loadingDiv" cssStyle="visibility : hidden" ><center>Please wait...<img src="<%=request.getContextPath() %>/LMSImages/images/loadingAjax.gif"/></center></s:div>
						</p>
						<table border="1" width="86%" cellpadding="3" align="center"
							cellspacing="0" bordercolor="#CCCCCC">

							
								<tr>
									<td>
										<input type="button" name="b1" value="Add Book"
											onclick="addBookRowStatic()"
											class="button" /><s:if test="%{isAddBookEnabled}">
											<input type="button" name="bookSeries" value="Add Book Series" onclick="addBookSeriesRow()" align="right" class="button" />
											</s:if>
									</td>

								</tr>
							

							<tr>
								<td>
									<s:div id="blankBooksDiv"></s:div>
								</td>
							</tr>

							<tr>
								<td><s:div id="bookSerDiv">
									<s:div id="boBookListDiv">



										<table border="0" cellpadding="0" cellspacing="0"
											id="bookTable">
											<s:iterator value="bookList" status="book">
												<tr>
													<s:set name="bookIndex" value="#book.index + 1" />



													<td>
														Book Number:
														<s:textfield theme="simple" id="%{bookIndex}"
															name="bookNo" value="%{bookNumber}" onkeypress="return checkNum()" onchange="isVerified()"/>
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
											
										</table>
 									</s:div>
 							<s:div id="bookSeriesDiv" >
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
											<s:property  value="%{status}"/>
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
										<td><s:div  name="serStatusDiv" id="divSe%{bookSeIndex}">
											<s:property value="%{status}"/>
											</s:div>
									 </td>
										
			                 </tr> 
			                 
			                 </s:iterator>
			                 </s:else>
                   			 </table>
							</s:div>
							</s:div>
								</td>
							</tr>
							<tr>
							<s:if test="%{isAddBookEnabled}">
								<tr>

								</tr>
								</s:if>
							</tr>
							<tr><td>

							</td>
							</tr>
							<tr>
								<td>
								<i> <input type="button" value="Verify All Books" onclick="return VerifyBOBookSeries()" class="button"/></i>
								</td>
							</tr>
					</table>
						<p>
							&nbsp;
						</p>
					</s:if>

                   
					<s:submit value="Proceed to Next Game" action="bo_im_dispatchOrder_ProceedNxtGame"
						align="center" onclick=" return verifyEntry()" cssClass="button" />

					<s:hidden name="gameId" value="%{orderdedGame.gameId}" />
					<s:hidden name="nbrOfBooksApp"
						value="%{orderdedGame.nbrOfBooksApp}" />
					<s:hidden name="nbrOfBooksRemain"
						value="%{orderdedGame.remainingBooksToDispatch}" />
					<s:hidden name="nbrOfBooksPerPack"
						value="%{orderdedGame.nbrOfBooksPerPack}" />

				</s:form>



			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_dispatchOrder_DispatchGame.jsp,v $
$Revision: 1.3 $
</code>