
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%response.setDateHeader("Expires",
   System.currentTimeMillis() + 10*24*60*60*1000);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" href="<%=request.getContextPath() %>/LMSImages/css/styles.css"
		type="text/css" />

	<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	<script>



function initialFocus()
{
 document.changePass.password.focus();
}
function isST3Filled()
{
 
  var isInValid = false;
  if(document.changePass.password.value=="")
  {
       isInValid = true;
        document.getElementById("oldPass").innerHTML = "<font color = 'red'>Please Enter Old Password</font>";
             
  }
   else
   {
   
      if(document.changePass.password.value.length<8)
        { 
         document.getElementById("oldPass").innerHTML = "<font color = 'red'>Password Should Be of 8 Digits!</font>";
         document.changePass.password.value="";
         document.changePass.newPassword.value="";
         document.changePass.verifynewPassword.value="";
         isInValid = true;
        }
        else
        {
         document.getElementById("oldPass").innerHTML = "";
        }
    }
    
     if(document.changePass.newPassword.value=="")
     {
         document.getElementById("newPass").innerHTML = "<font color = 'red'>Please Enter New Password</font>";
         isInValid = true;      
     }
     else
     {
      if(document.changePass.newPassword.value.length<8)
        { 
         document.getElementById("newPass").innerHTML = "<font color = 'red'>Password Should Be of 8 Digits!</font>";
         document.changePass.password.value="";
         document.changePass.newPassword.value="";
         document.changePass.verifynewPassword.value="";
         isInValid = true;
        }
        else
        {          
         document.getElementById("newPass").innerHTML = "";
        } 
     }
    
    
    if(document.changePass.verifynewPassword.value=="")
     {
         document.getElementById("verifyPass").innerHTML = "<font color = 'red'>Please Verify  New Password</font>";
          document.changePass.password.value="";
         document.changePass.newPassword.value="";
         document.changePass.verifynewPassword.value="";
         isInValid = true;      
     }
     else
     {
     
      if(document.changePass.verifynewPassword.value==document.changePass.newPassword.value)
        {
          if(document.changePass.password.value==document.changePass.newPassword.value)
          {
            document.getElementById("currentPass").innerHTML = "<font color='Red'>New Password Can Not Be Same As The Old Password Please Enter Another Password!!";
            document.getElementById("verifyPass").innerHTML ="";
            document.changePass.password.value="";
         document.changePass.newPassword.value="";
         document.changePass.verifynewPassword.value="";
            isInValid = true;
          }
          else
            {
             document.getElementById("verifyPass").innerHTML = "";
            }
        }
    
       else
         {
          document.getElementById("verifyPass").innerHTML = "<font color = 'red'>Please Verify  New Password</font>";
          document.getElementById("currentPass").innerHTML ="";
           document.changePass.password.value="";
         document.changePass.newPassword.value="";
         document.changePass.verifynewPassword.value="";
          isInValid = true;
         }
         
       
      }
    
    
    
    if(isInValid){
		return false;
		}
				
		return true;
}		
function isEqual()
	{
	 
	 
	if(document.changePass.newPassword.value!=document.changePass.verifynewPassword.value)
	  {
	   alert("Please Verify New Password");
	   document.changePass.verifynewPassword.focus();
	   return false;
	  }
	  else
	   if(document.changePass.password.value==document.changePass.newPassword.value)
	   {
	    alert("This is Your Current Password");
	    document.changePass.newPassword.focus();
	    return false;
	   }
	   else
	    alert("Please Enter New Password");
	   	
	}



</script>
</head>
	<body onload="initialFocus()">
	
		<s:if test="#session.FIRST=='false'">
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		</s:if>
		<div id="wrap">

  <div id="top_form">
   <h3>Change Password Admin </h3>
		<form onsubmit="return isST3Filled()" name="changePass"
			action="<%=request.getContextPath()%>/com/skilrock/lms/admin/admin_saveChangePassword.action" method="post" >
			<table border="0" cellpadding="2" cellspacing="0">
				<tr>
					<td colspan="2">
						<s:actionerror />
						<s:fielderror />
					</td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td>
						<div id="currentPass" align="center"></div>
					</td>
				</tr>
				<tr>
					<td>
						<div id="oldPass" align="center"></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:password name="password" label="Old Password" required="true" />
					</td>
				</tr>
				<tr>
					<td>
						<div id="newPass"></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:password name="newPassword" label="New Password" required="true" />
					</td>
				</tr>
				<tr>
					<td>
						<div id="verifyPass"></div>
					</td>
				</tr>
				<tr>
					<td>
						<s:password name="verifynewPassword" label="Re-enter New Password" required="true" />
					</td>
				</tr>
				<tr>
					<td>
						<s:submit cssClass="button" value="Change" align="center"
							targets="down" required="true" />
					</td>
					<s:if test="#session.FIRST">
					<td>
					   <a href="adminLogin.jsp">Back to Login Page
					   </a>
					 </td>   
					 </s:if>
				</tr>

			</table>
		</form>
			</div></div>
		<div id="down"></div>

	</body>
</html>
<code id="headId" style="visibility: hidden">
$RCSfile: adminChangePassword.jsp,v $
$Revision: 1.2 $
</code>