<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
	<s:head theme="ajax" debug="false"/>
	<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"  type="text/css"/>
	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
</head>
<script>
		var path = "<%=request.getContextPath() %>";
		
		// it contain the details of all desplayed games
		var gameDetMap = new Object();  
		
		function gameDetails() {
				var url = path+"/com/skilrock/lms/web/scratchService/pwtMgmt/gm_pwt_fetchPwtGameDetails.action";
				var _resp = _ajaxCall(url);
				//alert(_resp.data)
				if(_resp.result && _resp.data!=""){
		 			var _respData = _resp.data;
		 			var option = _id.o("game_name");
		 			
					var gameArr = _respData.split("Nx*");
					//alert("gameArr = "+gameArr.length+" data = "+gameArr);
					var gameWiseMap;
					for(var i=0; i<gameArr.length; i++) {
						// creating map of all details of game
						gameWiseMap = new Object();
						
						var gameDetailsArr = gameArr[i].split(":");
						gameWiseMap['gameId'] = gameDetailsArr[0];
						gameWiseMap['gameNbr'] = gameDetailsArr[1];
						gameWiseMap['pack_nbr_digits'] = gameDetailsArr[3];
						gameWiseMap['book_nbr_digits'] = gameDetailsArr[4];
						gameWiseMap['ticket_nbr_digits'] = gameDetailsArr[5];
						gameWiseMap['game_nbr_digits'] = gameDetailsArr[6];
						gameWiseMap['game_virn_digits'] = gameDetailsArr[7];						
						
						var gkey = gameDetailsArr[0]+"-"+gameDetailsArr[1]+"-"+gameDetailsArr[2];						
						gameDetMap[gkey] = gameWiseMap;						
						//alert(gkey+" gkey   === "+(gameDetMap[gkey])['gameId']);
						// create game option
						var dvalue = gameDetailsArr[1]+"-"+gameDetailsArr[2];
						var opt = new Option(dvalue, gkey);
						option.options[i+1] = opt;						
					}
					//alert(gameDetMap);
				}
			
		}
		
		
		function verifyNSave(){
			var isValid = true; 
			var gameName = _id.v("game_name");	
			var tktNo = _id.v("tktNo");
			var virnCode = _id.v("virnNbr");
					
			if(gameName==-1){
				_id.i("gName_e", "Please Select The Game Name", "e");
				isValid = false;
				return false;
			}else{
				_id.i("gName_e", "");				
			}
			
			if(tktNo==""){
				_id.i("tktNo_e", "Please Insert Ticket Number", "e");
				isValid = false;
			}else {
				 var isDesh = tktNo.indexOf("-")!=-1;
				 var deshCountArr =  tktNo.split("-");
				 var newtTktNo ="";
				 var tktLength = parseInt((gameDetMap[gameName])['game_nbr_digits'],10) + 
				 	parseInt((gameDetMap[gameName])['pack_nbr_digits'], 10) + 
				 	parseInt((gameDetMap[gameName])['book_nbr_digits'], 10) + 
				 	parseInt((gameDetMap[gameName])['ticket_nbr_digits'],10);				
				 if((isDesh && tktNo.length==tktLength+2 && deshCountArr.length == 3) || (!isDesh && tktNo.length==tktLength)){
				 	var tktGameNbr = tktNo.substring(0, parseInt((gameDetMap[gameName])['game_nbr_digits'],10));
				 	
				 	if(tktGameNbr != gameName.split("-")[1]){
						_id.i("tktNo_e", "Ticket Number Is Not Of The Selected Game", "e");
						isValid = false;
					}else{
						_id.i("tktNo_e", "");
					}	
				 }else {
				 	_id.i("tktNo_e", "Ticket Number Format is Not Valid.", "e");
					isValid = false;
				 }
				
				 
			}
			
			if(virnCode==""){
				_id.i("virnNbr_e", "Please Insert Virn Code", "e");
				isValid = false;
			}
			else if((gameName!=-1 && virnCode.length !=(gameDetMap[gameName])['game_virn_digits'])){
				_id.i("virnNbr_e", "Virn Code Is Not in Valid Format", "e");
				isValid = false;
			}else{
				_id.i("virnNbr_e", "");
			}
			
			return isValid;
		}
		function _subTrans(){
			return ( verifyNSave() && _subValid('subTrans'));
		}
</script>
<body onload="gameDetails()">
<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp" %>

<div id="wrap"><div id="top_form">

  <h3>Winning Ticket And VIRN Verification</h3>	
	<div id="first">
	  <s:form id="ab" name="tv" action="/com/skilrock/lms/web/scratchService/pwtMgmt/pwt_ret_verifyAndSaveTicketNVirn.action" onsubmit="return _subTrans()"> 		
			 <table  border="0" cellpadding="2" cellspacing="0"  width="400px">
			 	<tr>				
					<td colspan="2"><div style="color: red; text-align: center" id="gName_e"></div></td>
				</tr>
			 	<tr>				
					<td><s:select name="gameIdNbrName" label="Game Name" headerKey="-1" list="{}" cssClass="option"
					headerValue="--Please Select--" id="game_name" /></td>
				</tr>
				<tr>				
					<td colspan="2"><div style="color: red; text-align: center" id="tktNo_e"><s:property value="pwtErrorMap.tktBean.status" /></div></td>
				</tr>
				<tr>				
					<td><s:textfield name="ticketNbr" label="Ticket Number" id="tktNo" /></td>
				</tr>
				<tr>				
					<td colspan="2"><div style="color: red; text-align: center" id="virnNbr_e"><s:property value="pwtErrorMap.pwtBean.message" /></div></td>
				</tr>
				<tr>				
					<td><s:textfield name="virnNbr" label="VIRN Number" id="virnNbr"/></td>
				</tr>			
				<tr>
					<td>&nbsp;</td>					
					<td>
					<div id="subCheck" style="position:absolute;"></div><table>
					<s:submit name="search" formId="ab" id="subTrans" value="Verify And Save" align="right" cssClass="button"/>
					</table>
					</td>
				</tr>		
			 </table>		 
	  </s:form> 
	</div>
	<div id="first_res"></div>
	
</div></div>

</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: RetailerPWTHome.jsp,v $
$Revision: 1.2 $
</code>