<%@ page import="java.util.*,com.skilrock.lms.beans.FortunePurchaseBean" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<% List<String> numbers = Arrays.asList("","0","1","2","3","4","5","6","7","8","9"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>



<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/terminal.css"  type="text/css"/>

<title><s:property value="%{fortuneBean.ticket_no}" /><s:property value="%{fortuneBean.reprintCount}" /></title>
	

<s:head theme="ajax" debug="false"/>
	</head>
<script>var isPrintable=false;var errMsg = "";document.oncontextmenu=new Function("return false");</script>
<script language='VBScript'>
Sub Print()
       OLECMDID_PRINT = 6
       OLECMDEXECOPT_DONTPROMPTUSER = 2
       OLECMDEXECOPT_PROMPTUSER = 1
       call WB.ExecWB(OLECMDID_PRINT, OLECMDEXECOPT_DONTPROMPTUSER,1)
End Sub
document.write "<object ID='WB' WIDTH=0 HEIGHT=0 CLASSID='CLSID:8856F961-340A-11D0-A96B-00C04FD705A2'></object>"
</script>
<body onload="parent.printFunc(),parent.updBalIframe()">
	  <object ID="WebBrowser1" WIDTH="0" HEIGHT="0" 
    CLASSID="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2"> 
    </object>
    <%
    String status = ((FortunePurchaseBean)request.getAttribute("fortuneBean")).getSaleStatus();
    System.out.println("*****Draw Status*******"+ status);
    if(status.equals("SUCCESS")){%>
  <div id="purchasecontainer">
<span class="small">**********************************</span>
<div class="large">WINLOT Limited</div>
<div class="drawgame_name">Zero TO Nine</div>
<s:set name="reprintCount" value="%{fortuneBean.reprintCount}"/>
<%String reprintCount=(String)pageContext.getAttribute("reprintCount");System.out.println("reprint count--"+reprintCount);
if(Integer.parseInt(reprintCount)>0){
 %><span class="small">Duplicate</span>
 <%}%>
<s:set name="purchaseTime" value="%{fortuneBean.purchaseTime}"/>
<%//System.out.println("Date time****"+pageContext.getAttribute("purchaseTime"));
		String[] pTime = pageContext.getAttribute("purchaseTime").toString().split(" ");
%>
<span class="date"><%=pTime[0]%></span><span class="time"><%=pTime[1].replace(".0","")%></span>
<div class="ticket"> TicNo : <s:property value="%{fortuneBean.ticket_no}" /><s:property value="%{fortuneBean.reprintCount}" /></div>
<span class="small">-----------------------------------------</span>
<div id="drawnumbers">
<span class="ttlpay">Draw Date</span><span class="amt">Draw Time</span>
</div>
<s:iterator value="%{fortuneBean.drawDateTime}">
		<s:set name="dateTime" value="%{toString()}"/>
		<%//System.out.println("Date time****"+pageContext.getAttribute("dateTime"));
		String[] date = pageContext.getAttribute("dateTime").toString().split(" ");
		%>
	<div id="drawnumbers">
		<span class="ttlpay"><%=date[0] %></span><span class="amt"><%=date[1] %></span>
	</div>
</s:iterator>
<div class="small">-----------------------------------------</div>
<s:set name="pickedNumbers" value="%{fortuneBean.pickedNumbers}"/>
<s:set name="betMultiple" value="%{fortuneBean.betAmountMultiple}"/>
  <% 
			      	List<Integer> pickNum = (List<Integer>)pageContext.getAttribute("pickedNumbers");
			      	int totQuantity = 0;
			      	int[] betMul = (int [])pageContext.getAttribute("betMultiple");
			      	System.out.println(pickNum+"*************-----------");
			      	for(int i=0;i<pickNum.size();i++){
			      	totQuantity=totQuantity+betMul[i];
					%>
<div id="drawnumbers">
<span class="sinsign"><%=numbers.get((pickNum.get(i))) %></span><span class="drawno"><%=betMul[i]%></span>
</div><%}%>

<div class="small">-----------------------------------------</div>
<div id="drawnumbers">
<span class="sinsign">Total Quantity</span><span class="drawno"><%=totQuantity%></span>
</div>
<div id="drawnumbers">
<span class="sinsign">No. Of Draws</span><span class="drawno"><s:property value="%{fortuneBean.noOfDraws}" /></span>
</div>
<div id="drawnumbers">
<span class="sinsign">Total Amount (<s:property value="%{#application.CURRENCY_SYMBOL}" />)</span><span class="drawno"><s:property value="%{fortuneBean.totalPurchaseAmt}"/></span>
</div>
<%
String barcodeType = (String)application.getAttribute("BARCODE_TYPE");
if(barcodeType.equals("applet")){%>
<div class="center">
	<applet Codebase="<%=request.getContextPath() %>/" code="BarcodeApplet.class" 
            name="barCodeApplet" 
           	width="151"             
            height="51">
      
     <param name="data" value="<s:property value="%{fortuneBean.ticket_no}" /><s:property value="%{fortuneBean.reprintCount}" />"/>
    </applet>
</div>
<%}else{ %>
<div class="center"><img src='<%=request.getContextPath() %>/barcode/<s:property value="%{fortuneBean.ticket_no}" /><s:property value="%{fortuneBean.reprintCount}" />.jpg'></img></div>
<%} %>
<span class="clear"></span>
<span class="small">**********************************</span>
</div>  
<script>isPrintable=true;</script>   
   
<%}else if(status.equals("AGT_INS_BAL")){%>
<table><tr><td><b>Agent has insufficient Balance</b></td></tr></table><script>errMsg="Agent has insufficient Balance";</script><% 
}else if(status.equals("RET_INS_BAL")){%>
<table><tr><td><b>Retailer has insufficient Balance</b></td></tr></table><script>errMsg="Retailer has insufficient Balance";</script><% 
}else if(status.equals("REPRINT_FAIL")){%>
<table><tr><td><b>Last Transaction Not Sale</b></td></tr></table><script>errMsg="Last Transaction Not Sale";</script><% 
}else if(status.equals("PERFORMED")){%>
<table><tr><td><b>Draw Performed</b></td></tr></table><script>errMsg="Draw Performed";</script><% 
}else{%>
<table><tr><td><b>Error!Try Again</b></td></tr></table><script>errMsg="Error!Try Again";</script><% 
}
 %>	
 		
</body>
</html>

<code id="headId" style="visibility: hidden">$Id: zeroToNineSuccess.jsp,v 1.1 2010/06/07 09:47:32 anant Exp $</code>
