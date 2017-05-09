<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/AutoComplete.css" media="screen" type="text/css"/>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/AutoComplete.js"></script>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<s:head theme="ajax" debug="false"/>
	<script>
	var path = "<%=request.getContextPath() %>";
function verifyRet(id,val,msg){
if(verifyOrg(id,val,msg))
_saleRetAgt('salesReturnAjaxAgt.action?Type='+ escape(_id.v("retOrgName")),'gameDiv');
}
	function _saleRetAgt(url,div){
	
	var load = '<img src="'+path+'/LMSImages/images/loadingAjax.gif"/>';
	if(div=="gameDiv"){
	_id.i("orgLoading",load);
	}else if(url.match("gameName")){

	_id.i("gameLoading",load);
	 url = url+escape(_id.v("retOrgName"));
	}
	 disableTable();
	 emptyAllFields();
	 
	_resp  = _ajaxCall(url);
	
	//alert(_resp.result+":"+_resp.data);
	if(_resp.result){
		_respData=_resp.data;
		 if(div=="gameDiv"){
			 _id.i("gameDiv",_respData);
			 _id.i("orgLoading","");
			 if(_id.o("gameName").options.length<2){
			 disableTable();
			 }
		 }
		else if(url.match("gameName")){
			 getPacknBookArray(_respData);
			 _id.i("gameLoading","");
		 }
	}else{
		if(div=="gameDiv"){
		_id.i("orgLoading",'Error',"e");
		}else if(url.match("gameName")){
		_id.i("gameLoading",'Error',"e");
		}
	}
}
	
	</script>
		<script type="text/javascript"  src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/saleReturn.js"></script>

	</head>


	<body onload="disableTable()">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>
	
	<div id="wrap">

  <div id="top_form">
   <h3>Sale Return</h3>
		

		<s:form name="saleRetAgt" id="saleRet" action="verifyPacksAndBooksAgent" method="POST">

			<table border="0" width="75%" cellpadding="2" cellspacing="0">
				<tr>
						<td>
							Select <s:property value="#application.TIER_MAP.RETAILER" />  Organization  :
						</td>
					<td nowrap="nowrap" colspan="2">
					
						<s:select theme="simple" id="retOrgName" cssClass="option"
							list="%{#session.ORGANIZATION_BEAN_LIST.{organization_Name}}"
							name="organization_Name" emptyOption="false"
							headerKey="Please Select" headerValue="Please Select" 
							onchange="verifyRet(this.id,this.value,'You have selected %{#application.TIER_MAP.RETAILER}:: ')" />
							<div id="orgLoading" style="position: absolute;"></div>
					</td>
				</tr>

				<tr>
					<td>
						Game Name :
					</td>
					<td>
						<div id="gameDiv">
						<s:select theme="simple" name="gameName" id="gameName"  headerKey="-1" cssClass="option" headerValue="--Please Select--" list="{}"/>
						</div>
						
					</td>
					<td>
						<div id="gameLoading">
					</td>
				</tr>

			</table>

<div id="completeDiv" style="position: absolute; background-color: #CCCCCC ;opacity:1 ;filter:alpha(opacity=30)" >
</div>	
		<table id="completeTable" border="1" bordercolor="black" width="85%">
		<tr><td><div  id="packRow">
			<table id="packTable" width="100%" border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td align="left" colspan="2">
						<input type="button" value="Add More Pack" name="addPack"
							id="addPack" class="button"
							onclick="addPackRow()" />
					</td>                               
				</tr>
				<tr>
					<td colspan="3">
						<div id="packErrDiv"></div>
					</td>
				</tr>
				<tr>
				<td width="20%">
					<s:label theme="simple" value="Pack Number :" />
				</td>
				<td width="15%">
					<input type="text" name ="packNumber" id="packNbr1" value="" size="16"/>
				</td>
				<td><div id="packNbr1Div">&nbsp;
					</div>
				</td>
				</tr>
			</table></div>
</td>
</tr>
			<tr>
				<td>
					<table id="bookSeriesTable" border="0" width="100%" cellpadding="2" cellspacing="0">
				<tr>
					<td align="left" colspan="2">
						<input type="button" value="Add Book Series" name="addBookSeries"
							id="addBookSeries" class="button"
							onclick="addBookSeriesRow()" />
					</td>                               
				</tr>
				<tr>
					<td colspan="3">
						<div id="bookSeriesErrDiv">
						</div>
					</td>
				</tr>
				<%for(int i=0;i<3;i++){%>
				<tr>
				<td width="22%">
					<s:label theme="simple" value="Book Number From :" />
				</td>
				<td width="15%">
					<input type="text" name ="bookSeriesFrom" id="bookNbrFmSe<%=i+1%>" value="" size="16"/>
				</td>
				<td width="8%">
					<s:label theme="simple" value="To :" />
				</td>
				<td width="15%">
					<input type="text" name ="bookSeriesTo" id="bookNbrToSe<%=i+1%>" value="" size="16"/>
				</td>
				<td><div id="bookNbrSe<%=i+1%>Div">&nbsp;
					</div>
				</td>
				</tr>
				<%} %>
			</table>
				</td>
			</tr>
<tr>
<td>

			<table id="bookTable" border="0" width="100%" cellpadding="2" cellspacing="0">
				<tr>
					<td align="left" colspan="2">
						<input type="button" value="Add More Book" name="addBook"
							id="addBook" class="button"
							onclick="addBookRow()" />
					</td>                               
				</tr>
				<tr>
					<td colspan="3">
						<div id="bookErrDiv">
						</div>
					</td>
				</tr>
				<%for(int i=0;i<5;i++){%>
				<tr>
				<td width="20%">
					<s:label theme="simple" value="Book Number :" />
				</td>
				<td width="15%">
					<input type="text" name ="bookNumber" id="bookNbr<%=i+1%>" value="" size="16"/>
				</td>
				<td><div id="bookNbr<%=i+1%>Div">&nbsp;
					</div>
				</td>
				</tr>
				<%} %>
			</table>
			</td>
			</tr>

			
			
			<tr>
			<td>	
				<table width="100%">
					<tr>
						<td align="right">
							<s:submit id="verify" theme="simple" value="Verify & Save" onclick="return VerifyBooks()" cssClass="button"/>
						</td>
					</tr>
				</table>
			</td>
			</tr>
			</table>
			
		</s:form>

</div></div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: salesReturnAgent.jsp,v $
$Revision: 1.2 $
</code>