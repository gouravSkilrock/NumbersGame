<%@ page contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<HTML xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>	
	<s:head theme="ajax" debug="false"/>	
</head>
  
<body>
      <h3><s:text name="label.virn.status"/></h3>
      <table border="1" cellpadding="4" cellspacing="0" width="100%" align="center" bordercolor="#cccccc">
        <tr style="background: #eee">
          <th><s:text name="label.game.name"/></th>
          <th><s:text name="label.virn.nbr"/></th>
          <th><s:text name="label.prize.amt"/></th>
          <th><s:text name="label.status"/></th>
          <th><s:text name="label.remarks"/></th>
        </tr>
        
	    <s:if test="%{trackVirnBean.virn_code!='' && trackVirnBean.virn_code!=null}">
	       <tr>	          
	          <td><s:property value="%{trackVirnBean.game_name}"/></td>
	          <td><s:property value="%{trackVirnBean.virn_code}"/></td>
	          <td><s:property value="%{trackVirnBean.pwt_amt}"/></td>
	          <td><s:property value="%{trackVirnBean.status}"/></td>
	          <s:set name="ramarksFormatted" value="%{trackVirnBean.remarks}"/>
	          <td><%=pageContext.getAttribute("ramarksFormatted") %></td>
	       </tr>	
	   </s:if> 
		   <s:else>
		       <tr>
		         <td colspan="6" align="center"><s:text name="label.no.virn.tracked"/></td>
		       </tr>
		   </s:else>
	        
     </table>
     </br>
     </br>
     <h3><s:text name="label.tkt.status"/></h3>
     <table border="1" cellpadding="4" cellspacing="0" width="100%" align="center" bordercolor="#cccccc">
        <tr style="background: #eee">
          <th><s:text name="label.game.name"/></th>
          <th><s:text name="label.tckt.no"/></th>          
          <th><s:text name="label.status"/></th>
          <th><s:text name="label.remarks"/></th>
        </tr>
	   <s:if test="%{trackTicketBean.ticket_nbr!='' && trackTicketBean.ticket_nbr!=null}">
		       <tr>	          
		          <td><s:property value="%{trackTicketBean.game_name}"/></td>
		          <td><s:property value="%{trackTicketBean.ticket_nbr}"/></td>
		          <td>&nbsp;<s:property value="%{trackTicketBean.status}"/></td>
		          <td><s:property value="%{trackTicketBean.remarks}"/></td>
		       </tr>
		               
			    <s:if test="%{trackTicketBean.isTicketFormatValid=='true'}">
				     <tr><td colspan="6" align="center">
			            <s:div id="ticketFlowId"  href="getTicketFlow.action?gameId=%{trackTicketBean.game_id}& bookNumber=%{trackTicketBean.book_nbr}" theme="ajax"></s:div>
			        </td></tr>
		      </s:if>
	   </s:if>   
         <s:else>
              <tr>
                <td colspan="6" align="center"><s:text name="label.no.tkt.tracked"/></td>
              </tr>
         </s:else>	     
	       
     </table>
       
</body>



</HTML>
<code id="headId" style="visibility: hidden">
$RCSfile: trackVirnResult.jsp,v $
$Revision: 1.3 $
</code>