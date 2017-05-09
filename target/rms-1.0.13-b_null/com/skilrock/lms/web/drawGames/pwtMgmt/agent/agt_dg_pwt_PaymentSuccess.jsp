<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css" type="text/css"/>

<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
 <script type="text/javascript">
   
    function updBalIframe() {
	parent.frames[0].updateBalance(projectName + "/com/skilrock/lms/web/loginMgmt/updateAvlCreditAmt.action");
			}</script>
  </head>
  
  <body onload="updBalIframe()">
  <%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
    <h3>Winning Details</h3>
    <s:form action="agt_dg_submit_pwt" name="retPwtTkt">
    <%double totAmt = 0; double totPwtAmt = 0;%>
    <s:hidden name="retOrgId" value="%{retOrgId}"></s:hidden>
    <table> 
    <tr><th>S.No</th><th>Ticket No</th><th>Status</th>
    </tr> 
    <s:iterator id="ticketMap" value="#session.VERIFIED_TICKET_MAP"  status="sno">
    <tr> <td><s:property value="#sno.index+1" /><s:set name="sNo" value="#sno.index+1"/></td>
    <td ><s:property value="key"/></td>
    <s:set name="newPwtBean" value="value"/>

    <td> <s:if test="#newPwtBean.pwtStatus=='NORMAL_PAY'">
    <s:set name="amt" value="#newPwtBean.totalAmount"></s:set>
    <%totAmt = totAmt+(Double)pageContext.getAttribute("amt");%>
   <s:property value="#amt"/>
    </s:if>
    <s:else>
    <s:property value="value.pwtStatus"/>
    </s:else></td></tr>
    
    
    </s:iterator>
    <s:if test="#session.SUBMITTED=='NO'">
   <tr><td>&nbsp;</td> <td>Total win in said Tickets is <s:property value="#application.CURRENCY_SYMBOL" /> </td><td><b><%=totAmt %></b></td></tr>
   <%if(totAmt>0){ %>
      <tr>
						 <td align="right" colspan="2">
							<s:submit align="right" theme="simple" value="Pay Amount" cssClass="button" id="subTrans"/> 
						 </td>
			</tr>
			<%} %>
    </s:if>
    <s:else>
     <s:iterator id="ticketMap" value="#session.VERIFIED_TICKET_MAP"  status="sno">
     <s:set name="newPwtBean" value="value"/>
     <s:if test="#newPwtBean.pwtStatus=='SUBMITTED'">
    <s:set name="amt" value="#newPwtBean.totalAmount"></s:set>
    <%totPwtAmt = totPwtAmt+(Double)pageContext.getAttribute("amt");%>
    </s:if>
    <s:if test="#newPwtBean.pwtStatus=='ERROR'">
    <tr><td>&nbsp;</td><td><s:property value="key"/></td><td>Not Submitted</td></tr>
    
    </s:if>
     
     </s:iterator>
     <%if(totPwtAmt>0) {%>
     <tr><td colspan="3" align="center"><s:property value="#application.TIER_MAP.RETAILER" /> has been credited for Winning worth <s:property value="#application.CURRENCY_SYMBOL" /> <b><%=totPwtAmt %> </b></td></tr>
    <%}%>
     <%if(totPwtAmt==0){ %>
     <tr><td colspan="3" align="center">Can't Submit !!! Please Try Again Later</td></tr>
     <%} %>
    </s:else>
    </table>
    
    </s:form>
    
  </body>

</html>
<code id="headId" style="visibility: hidden">
$RCSfile: agt_dg_pwt_PaymentSuccess.jsp,v $
$Revision: 1.2 $
</code>