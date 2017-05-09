<%@ page import="java.util.*" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <link rel="stylesheet" href="<%=request.getContextPath() %>/com/skilrock/lms/web/drawGames/common/terminal.css"  type="text/css"/> 
</head>
<script language="javascript">
function printFrm(){
/*alert();
window.frames['tktFrame'].focus();
window.frames['tktFrame'].print();*/  
}
</script>
<script language='VBScript'>
Sub Print()
       OLECMDID_PRINT = 6
       OLECMDEXECOPT_DONTPROMPTUSER = 2
       OLECMDEXECOPT_PROMPTUSER = 1
       call WB.ExecWB(OLECMDID_PRINT, OLECMDEXECOPT_DONTPROMPTUSER,1)
End Sub
document.write "<object ID='WB' WIDTH=0 HEIGHT=0 CLASSID='CLSID:8856F961-340A-11D0-A96B-00C04FD705A2'></object>"
</script>

<body onload="parent.printFunc()">   
	<div id="maincontainer">
	 <%     
       String dateTime =  new java.sql.Timestamp(new java.util.Date().getTime()).toString();     
    %>
		<span class="small">**********************************</span>
		<div class="large"><s:text name="label.winlot.ltd"/></div>
		<div class="largeunbold"><s:text name="label.temp.receipt"/></div>				
		<span class="date"><%=dateTime.split(" ")[0]%></span><span class="time"><%=(dateTime.split(" ")[1]).split("\\.")[0]%></span>
		<div id="drawnumbers">
          <span class="ttlpay"><s:property value="%{#session.USER_INFO.orgName}" /></span>
       </div>
		
		<span class="small">-----------------------------------------</span>		
		<div id="drawnumbers">
          <span class="ttlpay"><s:text name="label.plr"/></span><span class="amt"><s:property value="%{#session.plrPwtAppDetMapRPOS.playerName}" /></span>
       </div>
		<div id="drawnumbers">
          <span class="ttlpay"><s:text name="label.voc.no"/></span><span class="amt"><s:property value="%{#session.plrPwtAppDetMapRPOS.recId}" /></span>
       </div>
		<div id="drawnumbers">
          <span class="ttlpay"><s:text name="label.voc.date"/></span><span class="amt"><%=dateTime.split(" ")[0]%></span>
       </div>
       <s:if test="%{#session.plrPwtAppDetMapRPOS.playerType=='player'}">
	       <div id="drawnumbers">
	          <span class="ttlpay"><s:text name="label.id"/></span><span class="amt"><s:property value="%{#session.plrPwtAppDetMapRPOS.idType}" /></span>
	       </div>
	       <div id="drawnumbers">
	          <span class="ttlpay"><s:text name="label.id.no"/></span><span class="amt"><s:property value="%{#session.plrPwtAppDetMapRPOS.idNumber}" /></span>
	       </div>
       </s:if>
       <span class="small">-----------------------------------------</span>
       <div id="drawnumbers">
          <span class="ttlpay"><s:text name="label.amt.type"/></span><span class="amt"><s:text name="label.pwt.amt"/></span>
       </div>
       
       <div id="drawnumbers">
          <span class="ttlpay"><s:text name="label.game.no"/>.</span><span class="amt"><s:property value="%{#session.plrPwtAppDetMapRPOS.tktBean.gameNbr}" /></span>
       </div>		
		<div id="drawnumbers">
          <span class="ttlpay"><s:text name="label.tkt.nbr"/></span><span class="amt"><s:property value="%{#session.plrPwtAppDetMapRPOS.tktBean.ticketNumber}" /></span>
       </div>
       <div id="drawnumbers">
          <span class="ttlpay"><s:text name="label.virn"/></span><span class="amt"><s:property value="%{#session.plrPwtAppDetMapRPOS.pwtBean.virnCode}" /></span>
       </div>
       <div id="drawnumbers">
          <span class="ttlpay"><s:text name="label.win.amt"/></span><span class="amt"><s:property value="%{#session.plrPwtAppDetMapRPOS.pwtBean.pwtAmount}" /></span>
       </div>		
			
		<span class="small">-----------------------------------------</span>		
		<div class="ticket"><s:text name="label.go.for.payments"/></div>
		<span class="small">**********************************</span>
	</div>
 
</body>
</html>


<code id="headId" style="visibility: hidden">
$RCSfile: plrRegNAppRPOSSuccess_ret.jsp,v $
$Revision: 1.4 $
</code>