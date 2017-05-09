<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

   
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/commonOla.js"></script>
	<s:head theme="ajax" debug="false" />

</head>

	<body >
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">

				<h1>
					Cash Card Purchase
				</h1>
			
					<s:form action="olaCashCardPurchase_Success" onsubmit="return verifyCashCard()">
						<div style="color: red; text-align: left;"><s:actionmessage /></div>
						<s:actionerror />
					<table border="0" width="600px" cellspacing="0" cellpadding="2" >
					<tr>
								<td colspan="4" >
									<div style="color: red; text-align: center" id="cardType_e"></div>
								</td>
							</tr>	
					<tr>
					<td colspan="2" >
						<table>
								<s:select label="Card Type" name="cardType" headerKey="-1"
										id="cardType" headerValue="--Please Select--" list="#{'Rummy':'Rummy'}"
										cssClass="option" required="true"
										onchange=" "></s:select></table>
																									</td>
							</tr>
							<tr>
								<td colspan="4" >
									<div style="color: red; text-align: center" id="userName_e"></div>
								</td>
							</tr>

							<tr >
								<td colspan="2" >
								<table>
									<s:textfield name="userName" label="User Name" id="userName"
										 />
										</table> 
								</td>
								
							</tr>
								<tr>
							
							<td colspan="2">
								<table id="user_Phone" style="display: none;" border="0"
								 cellspacing="0"  cellpadding="2"  >
								 <tr>
										<td>
											<s:textfield name="userPhone" label="Phone Number"
												id="user_phone" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
							
							</table>
							<br></br>
							<table border="0.5" width="600px" cellspacing="0" cellpadding="2" id="dynData">
							<tr>
					<td colspan="2" align="left">
					
						<input type="button" name="addBtn" id="addBtn" value="Add More"/><div style="color: red; text-align: center" id="denoType_e"></div>		<div style="color: red; text-align: center" id="numberofCards_e"></div></td>
						
						</tr>

							<tr>
					
								<td >
								<table>
									<s:select label="Denomination Type" name="denoType" headerKey="-1"
										id="denoType" headerValue="--Please Select--" list="#{10:10,50:50,100:100,500:500,1000:1000,5000:5000}"
										cssClass="option" required="true"
										onchange=" "	 ></s:select></table>
								</td>
								
								<td>
								<table>
									<s:select label="Number of Cards" name="numberofCards" headerKey="-1"
										id="numberofCards" headerValue="--Please Select--" list="#{1:1,2:2,3:3,4:4,5:5,6:6,7:7,8:8,9:9,10:10}"
										cssClass="option" required="true"
										onchange=" "	 ></s:select></table>
								</td>
							</tr>
							
									
						
						</table>
					<br></br>
										<s:submit value="Submit" align="center" cssClass="button" />
					</s:form>
					
			</div>
		</div>
		
<script type="text/javascript">
window.onload = function(){
var addBtn = document.getElementById("addBtn");

addBtn.onclick = function()
{

var div = document.getElementById("dynData");
var row = document.createElement("tr");
var col = document.createElement("td");
var col1=document.createElement("td");
var tdHtmlData= ' <table><tr><td class="tdLabel"><label for="denoType" class="label">Denomination Type<span class="required">*</span>:</label></td><td><select name="denoType" id="denoType" class="option" onchange=" "><option value="-1">--Please Select--</option><option value="10">10</option><option value="50">50</option><option value="100">100</option><option value="500">500</option><option value="1000">1000</option><option value="5000">5000</option></select></td></tr></table>' ;
var	tdHtmlData1= '<table><tr><td class="tdLabel"><label for="numberofCards" class="label">Number of Cards<span class="required">*</span>:</label></td><td><select name="numberofCards" id="numberofCards" class="option" ><option value="-1">--Please Select--</option><option value="1">1</option><option value="2">2</option>option value="3">3</option><option value="4">4</option><option value="5">5</option>  <option value="6">6</option> <option value="7">7</option> <option value="8">8</option> <option value="9">9</option> <option value="10">10</option></select></td></tr></table>';
	

col.innerHTML=tdHtmlData;
col1.innerHTML=tdHtmlData1;
row.appendChild(col);
row.appendChild(col1);
div.appendChild(row);
 


};


};



</script>
	</body>
	
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: cashCardPurchaseMenu.jsp,v $
$Revision: 1.2 $
</code>