<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<link href="css/print.css" rel="stylesheet" type="text/css" media="print" />
	<link href="css/page_style.css" rel="stylesheet" type="text/css" media="screen" />
</head>



<body>


<a href="generateDeliveryChallanToPDF.action"><s:text name="label.exp.to.pdf"/></a> 
<br/>
<table width="98%" border="0" cellspacing="0" cellpadding="0">

	<thead>
	<TR>
	<TD colspan="3" align="left">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td colspan="3" align="center" height="35"><b><s:text name="label.delivery.challan"/></b></td>
	</tr>
  <tr>
    <td width="50%" align="center" height="35"><b>&nbsp;</b></td>
    <td width="31%" height="17" align="right"><s:text name="label.invoice.gen.date"/> :</td>
	<td width="19%" height="18" align="right"><s:property value="#session.staticReportMap.invoiceGenerationTime" /></td>
  </tr>
  <tr>
    <td colspan="3" align="left">&nbsp;</td>
    </tr></table>	
	</TD></TR>
      
      
	
	<tr><td valign="top" align="left" colspan="4">
    <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td align="left" width="70%" valign="middle"><h2><s:property value="#session.staticReportMap.orgName" /></h2></td>
<td width="30%" align="right" colspan="3">
  <table width="100%" border="0" cellspacing="0" cellpadding="2" align="right">
                            <tr>
                             <td width="63" align="right" valign="top"><s:text name="label.addr"/></td>
                             <td width="26" valign="top" align="right">:</td>
                             <td width="258" align="right"><s:property value="#session.staticReportMap.orgAdd" /></td>
                </tr>
                             <td colspan="3" align="left"><s:text name="label.vat.nbr"/> : <b><s:property value="#session.staticReportMap.vatRef" /><b></td>
                             
                             </table>
           					 </td></tr>
							 <tr><td colspan="6" height="4"><hr style="border-bottom:#000000 thin solid" /></td></tr>
                             <tr><td colspan="4" bgcolor="#CCCCCC">
                            			<table width="100%" border="0" cellspacing="0" cellpadding="2" align="left">
                                        <tr>
                                        <td width="60%" align="left"><b><s:property value="#session.staticReportMap.customerName" /></b></td>
                                        <td width="8%">&nbsp;</td>
                                        <td width="17%" align="right"><s:text name="label.invoice.id"/> : </td>
                                        <td width="15%" align="right" style="margin-right:4PX"><s:property value="#session.staticReportMap.invoiceId" /></td>
                                        </tr><tr>
                                        <td width="60%" align="left"><s:property value="#session.staticReportMap.customerAdd1" /><br />
                                        <s:property value="#session.staticReportMap.customerAdd2" /></td>
                                        <td width="8%">&nbsp;</td>
                                        <td width="17%" align="right"><s:text name="label.invoice.date"/> : </td>
                                        <td width="15%" align="right" style="margin-right:4PX"><s:property value="#session.staticReportMap.invoiceDate" /></td>
                                        </tr></table>
                             </td></tr></table>    
    </td></tr>
     <tr><td colspan="6" class="lightBorder" height="4"></td></tr>
    <tr><td valign="top" align="left" colspan="4">
	
	<table width="100%" border="0" cellspacing="0" cellpadding="4">
   
   </table></thead>


<%! int i=0,j=0; %>

  <tbody>
	
	<tr><td valign="top" align="left" colspan="4">
		<table width="100%" border="0" cellspacing="0" cellpadding="4">
			<s:iterator value="#session.invoiceDetailsMap">
			<tr>
				<td>
					<table width="100%" align="left" cellpadding="2" border="0">
						<tr><td><u><b> <s:text name="label.game.name"/> :</b> <s:property value="key"/></u></td></tr>
						<tr><td><b><s:text name="label.pack.no"/><b></td></tr>
						<tr>
							<td>
							<table width="100%"><%i=-1;j=-5; %>
								<s:iterator id="priv" value="value.packNbrList">
									<%i+=1; j+=1;%>
									<%if(i%5==0) {%>
										<tr>
									<%}%>
										<td><input type="checkbox" value="as" name="boadm" /> <s:property/></td>
									<%if(j%5==0) {%>
										</tr>
									<%} %>
				
								</s:iterator>
							</table>
							</td>
						</tr>
						<tr><td><b><s:text name="label.book.no"/><b></td></tr>
						<tr>
							<td>
							<table width="100%"><%i=-1;j=-5; %>
								<s:iterator id="priv" value="value.bookNbrList">
									<%i+=1; j+=1;%>
									<%if(i%5==0) {%>
										<tr>
									<%}%>
										<td><input type="checkbox" value="as" name="boadm" /> <s:property/></td>
									<%if(j%5==0) {%>
										</tr>
									<%} %>
				
								</s:iterator>
							</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>  
			</s:iterator>
		</table>

</td></tr></tbody>
</table>
  
  </body></html>
<code id="headId" style="visibility: hidden">
$RCSfile: invoiceAsHtml.jsp,v $
$Revision: 1.3 $
</code>