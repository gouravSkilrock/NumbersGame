
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<script>var path="<%=request.getContextPath() %>"</script>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/com/skilrock/lms/web/scratchService/inventoryMgmt/backOffice/js/updBookNVirnStatus.js"></script>
	<s:head theme="ajax" debug="false"/>
</head>

<body onload="gameDetails()">
	<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
	<div id="wrap">
  	<div id="top_form">
    <h3><s:text name="label.update.book.and.virn.status"/></h3>
	
	<s:actionerror/>
	<s:form name="uploadVirn" method="post" action="bo_im_updBookNVirnStatus_verifyNSave" enctype="multipart/form-data" onsubmit="return _subTrans()">
	
		<table border="0" cellpadding="3" cellspacing="0"  bordercolor="#CCCCCC">
			<tr>
			<td><s:text name="label.change.book.status.as"/>:&nbsp;</td>
			<td><input type="radio" id="statusType" name="statusType" value="MISSING" checked="checked" /> <s:text name="label.missing"/>
			&nbsp;&nbsp;<input type="radio" id="statusType" name="statusType" value="ACTIVATE" /> <s:text name="label.activate"/>
			</td>
			</tr>	
					
			<s:select id="game_id" cssClass="option" key="label.slct.game" list="{}" onchange="setTextBoxMaxLength(this.value)" 
				name="gameId" emptyOption="false" headerKey="-1" headerValue="%{getText('label.please.select')}"/>
						
			<tr><td colspan="2"><div id="game_err"><s:actionmessage/></div></td></tr>	
			<tr>
				<td colspan="2">
				<table border="1" cellpadding="3" cellspacing="0"  bordercolor="#CCCCCC" width="100%">			
					<tr><th colspan="2" >
						<s:text name="label.book.details"/> : - &nbsp;&nbsp;
							<input type="button" value="<s:text name="btn.add.book.row"/>" class="button" onclick="addBookRow('bkTbl', 'book')"/>&nbsp;&nbsp;
							<input type="button" value="<s:text name="btn.add.book.series.row"/>" onclick="addBookRow('bkSerTbl', 'bookSeries')" class="button"/>
						</th>
					</tr>	
					<tr>
						<td colspan="2">
							<table id = "bkTbl" border="0" cellpadding="3" cellspacing="0" >
								<tr><td colspan="2"><div id="game_book_err"></div></td></tr>	
								<tr>
									<td><s:text name="label.book.no"/></td><td><s:textfield theme="simple" name="bookNbr" size="12"  maxLength="12" onfocus="this.style.color='gray'" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)"/></td>									
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table id = "bkSerTbl" border="0" cellpadding="3" cellspacing="0">
								<tr><td colspan="2"><div id="bookSerErr"></div></td></tr>	
								<tr>
									<td><s:text name="label.book.series"/></td>
									<td><s:text name="label.from"/> <s:textfield theme="simple" name="bookSeriesFrom" size="12"  maxLength="12" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)"/></td>
									<td><s:text name="label.to"/> <s:textfield theme="simple" name="bookSeriesTo" size="12" maxLength="12" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)"/></td>
									
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>		
			
			
			<tr><td>&nbsp;<div id="packErr" style="color:red"></div></td></tr>
			<tr>
				<td colspan="2">
				<table border="1" cellpadding="3" cellspacing="0"  bordercolor="#CCCCCC" width="100%">				
					<tr>
						<th colspan="2">
							<s:text name="label.pack.details"/> : - &nbsp;&nbsp;
							<input type="button" value="<s:text name="btn.add.pack.row"/>" class="button" onclick="addPackRow('pkTbl', 'pack')"/>&nbsp;&nbsp;
							<input type="button" value="<s:text name="btn.add.pack.series.roe"/>" class="button" onclick="addPackRow('pkSerTbl', 'packSer')"/>
						</th>
					</tr>
					<tr>
						<td colspan="2">
							<table id = "pkTbl" border="0" cellpadding="3" cellspacing="0">
								<tr><td colspan="2"><div id="game_pack_err"></div></td></tr>	
								<tr>
									<td><s:text name="label.pack.nbr"/></td><td><s:textfield theme="simple" name="packNbr" size="12"  maxLength="12" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)"/></td>									
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table id = "pkSerTbl" border="0" cellpadding="3" cellspacing="0">
								<tr><td colspan="2"><div id="packSerErr"></div></td></tr>	
								<tr>
									<td><s:text name="label.pack.series"/></td>
									<td><s:text name="label.from"/> <s:textfield theme="simple" name="packSeriesFrom"  size="12"  maxLength="12" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)"/></td>
									<td><s:text name="label.to"/> <s:textfield theme="simple" name="packSeriesTo"  size="12"  maxLength="12" onblur="isValidBookNbr(this)" onkeypress="return isValidEntries(this)"/></td>									
								</tr>
							</table>
						</td>
					</tr>
					</table>
				</td>
			</tr>
						
			<tr><td colspan="2"><div id="file1"></div></td></tr>
			<s:file key="label.upload.virn.file" id="file" size="34" name="fileName" cssClass="button"
					accept="text/*" ></s:file>
			<tr><td colspan="2"><div id="remarks_err"></div></td></tr>
			<tr>
				<td align="right"><table><s:label key="label.remarks" />:</table></td>
				<td colspan="1"><s:textarea  theme="simple" cssStyle="height: 2cm;width: 7cm; border-color : black; "  
				name="remarks" id="remarks"  value="" onkeypress="return (this.value.length <= 100);" /></td>
			</tr>
				
			<tr><td align="right" colspan="2">
				<div id="subCheck" style="position:absolute;"></div>
				<table><s:submit  key="btn.verify" cssClass="button" id="subTrans" /></table>
			</td></tr>

		</table>
	</s:form>
		
	</div>
	</div>
</body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: bo_im_updBookNVirnStatus_Menu.jsp,v $
$Revision: 1.3 $
</code>