		<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	


			<script>
			function validateAllowBook()
		{
		
		var flag=true;
		var availablecredit=document.getElementById('availableCredit');
		//alert("balance is " + availablecredit.value);
		//alert("credit limit is" + availablecredit.value);
		var totalBookPrice=0;
		var ListSize1=document.getElementById('ListSize');
		//vat listSize=Integer.parseInt(ListSize1.value);
		
		var quoteAllow= new Array(ListSize1);
		var quoteRemain= new Array(ListSize1);
		var bookPrice= new Array(ListSize1);
		//var Game= new Array(ListSize1);
		Game =document.getElementsByName('gameName');  
		 quoteAllow =document.getElementsByName('allowedBooks');
		 quoteRemain=document.getElementsByName('differenceBtBOndApprBooks');
		 bookPrice=document.getElementsByName('oneBookPrice');
		var numericExpression = /^[0-9]+$/;
		var totalAllotedBooks=0;
		for(var i=0 ;i<quoteAllow.length ;i++){
		
		//alert(Game[i].value);
		var allowBook=quoteAllow[i].value;
		var remainBook=quoteRemain[i].value;
		var gamebookPrice=bookPrice[i].value;
		totalBookPrice=totalBookPrice + (gamebookPrice*allowBook);
		//alert( "book price for game" + totalBookPrice);
		totalAllotedBooks=parseInt(totalAllotedBooks,10)+parseInt(allowBook,10);
		
		
		//alert(allowBook);
		//alert(remainBook);
		 if(!isNumeric(quoteAllow[i],"Please Enter Corect Value for Allowd Book for Game "+Game[i].value)){
		flag=false;
		 return flag;
		 }
		 var allow=parseInt(allowBook,10);
		  var remain=parseInt(remainBook,10);
		 // alert("dd"+allow);
		 
		if(allow>remain){
		//alert("dd"+remain);
		
		document.getElementById('allowedBooks1').innerHTML = "<font color = 'red'>Alloted Books can not be greater than Remaining Books for Game "+Game[i].value+"</font>";
		flag=false;
		 return flag;
		}
		
		document.getElementById('allowedBooks1').innerHTML ="";
		flag=true;
		
		}		
		if(totalAllotedBooks=="0"){
		alert("Total Alloted Books can not be zero");
		return false;
		}	
		//alert(totalBookPrice);
		if(availablecredit.value < totalBookPrice)
		{
		//alert(totalBookPrice);
		 alert(tierMap['AGENT']+"'s credit limit is not enough to approve these all books ");
		 return false;
		}
		return flag;
		}
		function isNumeric(elem, helperMsg){
				//alert(helperMsg);
			var numericExpression = /^[0-9]+$/;
			if(elem.value.match(numericExpression)){
				doReset(elem);
				return true;
			}else{
				//alert(helperMsg);
				var elem1=elem.name+"1";
			
				document.getElementById(elem1).innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
			//alert(helperMsg);
				elem.focus();
				return false;
			}
		}
		
		
		function verSpChar(){
			//var flag=true;
			var ListSize1=document.getElementById('ListSize');
			Game =document.getElementsByName('gameName');  
			quoteAllow =document.getElementsByName('allowedBooks');
			var numericExpression = /^[0-9]+$/;
			
			for(var i=0 ;i<quoteAllow.length ;i++){
				var allowBook=quoteAllow[i].value;
				
				//helperMsg="Please Enter Corect Value for Allowd Book for Game "+Game[i].value;
				if(quoteAllow[i].value.match(numericExpression)){
					
				}else{
					quoteAllow[i].value=0;
					//document.getElementById(quoteAllow[i].name+'1').innerHTML = "<font color = 'red'>"+helperMsg+"</font>";
					//flag=false;
					//break;
				}
				
			}
			
			
			return true;
			
		}
		
		function doReset(elem){
			var elem1=elem.name+"1";
			
				document.getElementById(elem1).innerHTML = "";
				
		}
			</script>
		<s:head theme="ajax" debug="false"/>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<h3>Process Order Request</h3>
				<div id="allowedBooks1"></div>
				<br />
				<div id="right">
					<s:form action="bo_om_processOrdReq_Search">
						<table border="0" cellpadding="2" cellspacing="0">
							<tr>
								<th>
									Search Order
								</th>
							</tr>
							<tr>
								<s:textfield label="Game Name" name="gameName"></s:textfield>
							</tr>
							<tr>
								<s:textfield label="Game Number" name="gameNumber"></s:textfield>
							</tr>
							<tr>
								<s:textfield label="%{#application.TIER_MAP.AGENT} Organization Name" name="agtOrgName"></s:textfield>
							</tr>
							<tr>
								<s:textfield label="Order Id" name="orderNumber" value=""></s:textfield>
							</tr>
							<s:submit theme="ajax" targets="bottom" value="Search" cssClass="button" />
						</table>
					</s:form>
					<div id="bottom"></div>
				</div>
			</div>
		</div>
	</body>
</html>
<code id="headId" style="visibility: hidden">
	$RCSfile: bo_om_processOrdReq_Menu.jsp,v $
	$Revision: 1.4 $
</code>