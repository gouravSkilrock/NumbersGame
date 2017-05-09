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
   parent.printFunc();
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

<body>
 
 
   <s:if test="%{#session.pwtDetailMapRpos.returnType == 'success' || #session.pwtDetailMapRpos.returnType == 'input'}">  
    <%     
       String dateTime =  new java.sql.Timestamp(new java.util.Date().getTime()).toString();      
    %>
	<div id="maincontainer">
		<span class="small">*************************************</span>
		<div class="large"><s:text name="label.winlot.ltd"/></div>
		<div class="largeunbold"><s:text name="label.pwt.varification"/></div>				
		
		<span class="date"><%=dateTime.split(" ")[0]%></span><span class="time"><%=(dateTime.split(" ")[1]).split("\\.")[0]%></span>
		<div id="drawnumbers">
          <span class="ttlpay"><s:text name="label.tkt.nbr"/></span><span class="amt"><s:property value="%{#session.pwtDetailMapRpos.tktBean.ticketNumber}" /></span>
       </div>
       <div id="drawnumbers">
          <span class="ttlpay"><s:text name="label.virn"/></span><span class="amt"><s:property value="%{#session.pwtDetailMapRpos.virn}" /></span>
       </div>		
			
		<div class="small">----------------------------------------------</div>
		<span class="clear">		
		  <s:if test="%{#session.pwtDetailMapRpos.tktBean.isValid}">		    
		    <s:property value="%{#session.pwtDetailMapRpos.pwtBean.message}" />
		     <s:if test="%{#session.pwtDetailMapRpos.pwtBean.isValid}">		        
		        <div id="drawnumbers">
                 <span class="ttlpay"><s:text name="label.total.pay"/></span><span class="amt"><s:property value="%{#session.pwtDetailMapRpos.pwtBean.pwtAmount}" /></span>
               </div>		      
		    </s:if>		    
		  </s:if>
		  <s:else>		  
		    <div class="ticket"><s:text name="label.inv.tkt"/></div>	
		    
		  </s:else>
		</span>
		<span class="small">*************************************</span>
	</div>
	<script>	
	 printFrm()	 
	 </script> 
	
 </s:if>
 
	 <s:elseif test="%{#session.pwtDetailMapRpos.returnType == 'registration'}">
	 
	 <s:if test="%{#session.pwtDetailMapRpos.pwtBean.isHighLevel}">
		  <script>
		    var disableRadio="DISABLED";
		  </script>	     
	 
	 </s:if>
	 
	 <s:else>
	   <script>
		    var disableRadio="";
		  </script>	   
	 </s:else>
	 	 
	 <script>		   
	    var plrRegHtml = '<table border="0"> 			<tr> 				<th nowrap="nowrap"> 					<input type="radio" checked="checked" value="player" 						name="playerType" onclick="showPlrDet(this.value)" /> 					Plr Reg. 				</th> 				<th nowrap="nowrap"> 					<input type="radio" value="anonymous" name="playerType" 						onclick="showPlrDet(this.value)"  '+disableRadio+'/> 					Anonymous Plr 				</th> 			</tr> 			<tr> 			 <td colspan="2"> 				<table id="plrDetail" border="0" width="100%"> 				<tr> 					<td colspan="2"> 						<div style="color: white; text-align: center" id="fName_e"></div> 					</td> 				</tr> 				<tr> 					<td align="right"> 						<label for="fname" class="label"> 							First Name: 						</label> 					</td> 					<td align="left"> 						<input type="text" name="firstName" value="" id="fname" /> 					</td> 				</tr> 				<tr> 					<td colspan="2"> 						<div style="color: white; text-align: center" id="lName_e"></div> 					</td> 				</tr> 				<tr> 					<td align="right"> 						<label for="lname" class="label"> 							Last Name: 						</label> 					</td> 					<td align="left"> 						<input type="text" name="lastName" value="" id="lname" /> 					</td> 				</tr> 				<tr> 					<td align="right"> 						<label for="idType" class="label"> 							Id Type: 						</label> 					</td> 					<td align="left"> 						<select name="idType" class="option"><option value="Passport">Passport</option><option value="Driving Licence">Driving Licence</option><option value="NID">NID</option></select></td> 				</tr> 				<tr> 					<td colspan="2"> 						<div style="color: white; text-align: center" id="idNum_e"></div> 					</td> 				</tr> 				<tr> 					<td align="right"> 						<label for="idNum" class="label"> 							Id num: 						</label> 					</td> 					<td align="left"> 						<input type="text" name="idNumber" value="" id="idNum" /> 					</td> 				</tr> 							</table> 		  </td>			</tr>				<tr> 				<td align="right"> 					&nbsp; 				</td> 				<td colspan="2" align="left"> 					<input type="button" id="subTrans" name="search" value="register" 		style="cursor: hand;"				onclick="verifyPlrDetails()" /> 				</td> 			</tr> 			</tr> 		</table>';  
		parent.document.getElementById('pwtResult').innerHTML=plrRegHtml;		 
	 </script>		
		
	 </s:elseif>


		
	</body>
</html>


<code id="headId" style="visibility: hidden">
$RCSfile: retPwtRPOSSuccess.jsp,v $
$Revision: 1.4 $
</code>