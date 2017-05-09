<%@ page
	import="java.util.*,com.skilrock.lms.beans.PWTDrawBean,com.skilrock.lms.beans.DrawIdBean,com.skilrock.lms.beans.PanelIdBean,com.skilrock.lms.web.drawGames.common.Util,com.skilrock.lms.web.drawGames.common.UtilApplet"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.KenoPurchaseBean"%>
<%@page import="com.skilrock.lms.beans.MainPWTDrawBean"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean, com.skilrock.lms.beans.RaffleDrawIdBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<script
		src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/commonRPOS.js"></script>
		

		 
	
<% 	pageContext.setAttribute("gameName", "RaffleGame1");
	MainPWTDrawBean mainPWTBean = (MainPWTDrawBean) session.getAttribute("PWT_RES");
	StringBuilder finalData = new StringBuilder("");
	
	double totMainTktAmt = 0.0;
	
	UserInfoBean userInfoBean = (UserInfoBean) session.getAttribute("USER_INFO");
	String verifiedAt = userInfoBean.getOrgName();
	
	int appletHeight = 0;
	int pwtConstantSize = 150;
	
	List<PWTDrawBean> winningBeanList = mainPWTBean.getWinningBeanList();
	
	for (int k = 0; k < winningBeanList.size(); k++) {
		PWTDrawBean winningBean = winningBeanList.get(k);
		String pwtTktType = winningBean.getPwtTicketType();
		
		if ("RAFFLE".equalsIgnoreCase(pwtTktType)) {
			StringBuilder topMsgsStrPwt = new StringBuilder(" ");
			StringBuilder bottomMsgsStrPwt = new StringBuilder(" ");
			appletHeight = UtilApplet.getAdvMsgs(winningBean.getAdvMsg(), topMsgsStrPwt, bottomMsgsStrPwt, appletHeight);	
				
			finalData.append("data=" + winningBean.getTicketNo() 
					+ "|reprintCountPWT=" + winningBean.getReprintCount()
					+ "|mode=RafflePWT|orgName=" + application.getAttribute("ORG_NAME_JSP")
					+ "|advtMsg=" + application.getAttribute("ADVT_MSG")
					+ "|gameDispName=" + winningBean.getGameDispName()
					+ "|ticketNumber=" + winningBean.getTicketNo()
					+ "|currSymbol=" + application.getAttribute("CURRENCY_SYMBOL")
					+ "|topAdvMsgPwt=" + topMsgsStrPwt
					+ "|bottomAdvMsgPwt=" + bottomMsgsStrPwt
					+ "|retailerName=" + Util.getOrgNameFromTktNo((winningBean.getTicketNo()), (String)application.getAttribute("ORG_TYPE_ON_TICKET")));
			
			StringBuilder raffleData = new StringBuilder(" ");
			List<RaffleDrawIdBean> raffDrawIdBeanList = winningBean.getRaffleDrawIdBeanList();
			if (raffDrawIdBeanList != null) {
				appletHeight = UtilApplet.getRaffPWTData(raffDrawIdBeanList, raffleData,appletHeight);
				for(int i=0; i<raffDrawIdBeanList.size(); i++){
					if(raffDrawIdBeanList.get(i).getWinningAmt() != null){
						totMainTktAmt = totMainTktAmt + Double.valueOf(raffDrawIdBeanList.get(i).getWinningAmt());
					}
				}
			}
			finalData.append("|raffPWTData=" + raffleData);
			
		}
	}

	finalData.append("|verifiedAt=" + verifiedAt + "|totMainTktAmt=" + totMainTktAmt + "|ctr=" + (pwtConstantSize + appletHeight));
	System.out.println("RaffleGame1 Winning DATA: " + finalData.toString());
%>
	<body>
	<script>setAppData('<%=finalData.toString()%>');</script>
	</body>
	<script>parent.updBalIframe();</script>
</html>


<code id="headId" style="visibility: hidden">
$RCSfile: RafflePwtResult.jsp,v $
$Revision: 1.2 $
</code>