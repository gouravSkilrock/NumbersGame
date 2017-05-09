getAdvMsg<%@ page
	import="java.util.*,com.skilrock.lms.dge.beans.PWTDrawBean,com.skilrock.lms.dge.beans.DrawIdBean,com.skilrock.lms.dge.beans.FortunePurchaseBean,com.skilrock.lms.dge.beans.PanelIdBean,com.skilrock.lms.web.drawGames.common.Util,com.skilrock.lms.web.drawGames.common.*,com.skilrock.lms.common.*"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.GameBean"%>
<%@page import="com.skilrock.lms.dge.beans.LottoPurchaseBean"%>
<%@page import="com.skilrock.lms.web.drawGames.pwtMgmt.javaBeans.*"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@page import="com.skilrock.lms.common.filter.LMSFilterDispatcher"%>
<%@ taglib prefix="s" uri="/struts-tags"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<script src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/commonRPOS.js"></script>
<%
	String barcodeType = (String)application.getAttribute("BARCODE_TYPE");	
			//System.out.println("winningBean *****************"+winningBean);
	if(!barcodeType.equals("applet")){ %>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/terminal.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/LMSImages/css/styles.css"
			type="text/css" />
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/LMSImages/css/newstyle.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/LMSImages/css/gameStyle.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/LMSImages/css/styleDraw.css" />
		<s:head theme="ajax" debug="false" />
		
	</head>
	<% 
	}
	 %>
	 <script src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/commonRPOS.js"></script>
	<script>parent.isPrintable=true;document.oncontextmenu=new Function("return false");</script>
		
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
<%
int appletHeight = 400 ;
int constantSize = 500;
appletHeight = appletHeight + constantSize;

PwtVerifyTicketBean pwtBean = (PwtVerifyTicketBean) session.getAttribute("PWT_BEAN");
StringBuilder topMsgsStr = new StringBuilder(" ");
				StringBuilder bottomMsgsStr = new StringBuilder(" ");
				appletHeight = UtilApplet.getAdvMsgs(pwtBean.getAdvMsg(), topMsgsStr, bottomMsgsStr, appletHeight);
UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
String verifiedAt = userInfoBean.getOrgName();
//String retailerName = Util.getOrgNameFromTktNo((pwtBean.getTicketNumber()), (String)application.getAttribute("ORG_TYPE_ON_TICKET"));
String finalData = "data="+pwtBean.getTicketNumber()+"|totMainTktAmt="+pwtBean.getTotalWinAmt()+"|ctr="+appletHeight+"|gameName="+Util.getGameMap().get(pwtBean.getGameId()).getGameNameDev()+"|mode=TPPWT|saleStatus=SUCCESS|reprintCount= |purchaseTime="+pwtBean.getPurchaseDateTime()+"|gameDispName="+Util.getGameDisplayName(pwtBean.getGameId())+"|ticketNumber="+pwtBean.getTicketNumber()+"|drawDateTime="+pwtBean.getVerifyTicketDrawDataBeanList().get(0).getDrawDateTime()+"*"+pwtBean.getVerifyTicketDrawDataBeanList().get(0).getDrawName()+",|currSymbol="+application.getAttribute("CURRENCY_SYMBOL")+"|totalPurchaseAmt="+pwtBean.getTotalPurchaseAmt()+"|orgName="+application.getAttribute("ORG_NAME_JSP")+"|advtMsg="+application.getAttribute("ADVT_MSG")+"|topAdvMsg="+topMsgsStr+"|bottomAdvMsg="+bottomMsgsStr+"|retailerName="+verifiedAt+"|verifiedAt="+verifiedAt;
System.out.println("##########################################"+finalData);
//finalData = "data=10000|gameName=TwelveByTwentyFour|mode=TPPWT|saleStatus=SUCCESS|reprintCount=0|purchaseTime=2015-10-21 14:11:55|gameDispName=12-24-Game|ticketNumber=36663429417128038|drawDateTime=2015-10-21 19:45:00*Winlot Surprise,|currSymbol=USD|totMainTktAmt=10.0|ctr=50|orgName=AFRICA LOTTO|advtMsg=YES|topAdvMsg= |bottomAdvMsg= |retailerName=Test Retailer|verifiedAt=Test Retailer";
 %>
		
	<script>setAppData("<%= finalData %>");</script>
	</body>
<script>parent.updBalIframe();</script></html>


<code id="headId" style="visibility: hidden">
$RCSfile: zimlottobonustwoPwtResult_TEST.jsp,v $
$Revision: 1.3 $
</code>