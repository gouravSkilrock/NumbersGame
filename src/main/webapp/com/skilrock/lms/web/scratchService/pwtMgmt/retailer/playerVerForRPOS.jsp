<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body> 
		
		<table border="0"  width="100px" align="left">
		<tr>
		  
		  <td colspan="2">
		    <font>Winning Amount is:&nbsp;<s:property value="%{#session.pwtDetailMapRpos.pwtBean.pwtAmount}"/></font>		      
		  </td>
		</tr>
		<br/>
			<tr>
				<th >
					<input type="radio"  checked="checked" value="player" name="playerType" 
							onclick="_id.blk('plrDetail');"  />Plr Reg.
				</th>
				<th>
					<s:if test="%{!#session.pwtDetailMapRpos.pwtBean.isHighLevel}">
						<input type="radio" value="anonymous" name="playerType" onclick="_id.non('plrDetail');" />Anonymous Plr
					</s:if>
				</th>
			</tr>
			
		<tr>
			<td colspan="2">			
		
			<table border="0"   width="100px" align="left" id="plrDetail">						
				<tr><td colspan="2"><div id = "fn_err" style="text-align: center"></div></td></tr>
				<tr>
					<td align="right"><label>First Name&nbsp;:</label></td>
					<td><input type="text" name="firstName" id="firstName"   required="true" size="15" maxlength="15" /></td>
				</tr>
							
				<tr><td colspan="2"><div id="ln_err" style="text-align: center"></div></td></tr>
				<tr>
					<td align="right"><label>Last Name&nbsp;:</label></td>
					<td><input type="text" name="lastName" id="lastName"  required="true" size="15" maxlength="15"/></td>
				</tr>
							
				<tr><td colspan="2"> <div id="it_err" style="text-align: center"></div></td></tr>
				<tr>
					<td align="right"><label>ID Type&nbsp;:</label></td>
					<td>
						<select name="idType" id="idNum" class="option">
							<option value="Passport">Passport</option>
							<option value="Driving Licence">Driving Licence</option> 
							<option value="NID">NID</option>
						</select>
					</td>
				</tr>
							
				<tr><td colspan="2"><div id = "in_err" style="text-align: center"></div></td></tr>
				<tr>
					<td align="right"><label>ID Number&nbsp;:</label></td>
					<td align="left"><input type="text" name="idNumber" id="idNumber"  required="true" size="15" maxlength="15"/></td>
				</tr>
				
				</table> 
			</td>
		</tr>
		<tr>
		    <td></td>
		 	<td align="left">
		 	   <input type="button" id="subTrans" name="search" value="register" onclick="verifyPlrDetails()"/>
		 	</td>
		</tr>
		
	</table>
 
</body>
</html>


<code id="headId" style="visibility: hidden">
$RCSfile: playerVerForRPOS.jsp,v $
$Revision: 1.2 $
</code>