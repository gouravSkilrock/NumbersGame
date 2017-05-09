<%@ page
	import="java.util.*,com.skilrock.lms.web.drawGames.common.Util,com.skilrock.lms.dge.beans.GameMasterLMSBean"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean"%>
<%@page import="com.skilrock.lms.dge.beans.DrawDetailsBean"%>
<%@page import="com.skilrock.lms.common.utility.FormatNumber"%>
<%@page import="com.skilrock.lms.common.utility.TextConfigurator"%>
<%@page import="com.skilrock.lms.dge.beans.BetDetailsBean"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Fortune</title>
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
	</head>
	<s:set name="retName" value="#session.USER_INFO.orgName"/>
	<s:set name="sampleStatus" value="#application.SAMPLE"/>
	<s:set name="parentOrgName" value="#session.USER_INFO.parentOrgName" />
	<s:set name="tktLen" value="#session.TICKET_LEN"/>
<script>
var cancelType = "<%=application.getAttribute("CANCEL_TYPE")%>";
var reprintType = "<%=application.getAttribute("REPRINT_TYPE")%>";
var currSymbol = "<%=application.getAttribute("CURRENCY_SYMBOL")%>";
var tierMap = parent.frames[0].tierArr;
var activeGames = new Array();
var gameDispName = new Object();
var retName = '<%=pageContext.getAttribute("retName")%>';
var orgType = "<%=application.getAttribute("ORG_TYPE_ON_TICKET")%>";
if(orgType != null && tierMap['AGENT'].toLowerCase() == orgType.toLowerCase()){
		retName = "<%=pageContext.getAttribute("parentOrgName")%>";
}

var ticketLength="<%=pageContext.getAttribute("tktLen")%>";
var sampleStatus = '<%=pageContext.getAttribute("sampleStatus")%>';
var isMax = false;
var projectName="<%=request.getContextPath()%>";
<% Date servDate=new java.util.Date();	
%>



var servDate='<%=servDate.getTime()%>';
var serverTime='<%=request.getAttribute("serverTime")%>';
var curSerTime = parseFloat(<%=servDate.getTime()%>);
var startRange='<%=request.getAttribute("startRange")%>';
var endRange='<%=request.getAttribute("endRange")%>';
var maxPlayerPicked='<%=request.getAttribute("pickNum")%>';
var winNum='<%=request.getAttribute("winNum")%>';
var allGames='';
var gameIndx=0;
var GetCountTimer;
var _gameGetShortTimer;
var _isPWTPriv=false;
var _isCancelTckPriv=false;
var _isReprintPriv=false;

var _zerotonineImgPath='<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/images/';
var card16ImgPath='<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/images/';	
var card12ImgPath='<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/images/';
var fortImgPath='<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/images/';
var kenoImgPath='<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/images/';
var lottoImgPath='<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/images/';
var comnImgPath='<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/images/';
var jackpotAmt=new Object();
function displayMainPageReports(){
	parent.frames[1].location.replace("<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/reportsMgmt/retailer/mainPageReports.jsp");
}


function gameNameObj(gameDispName,unitPrice) {
	this.gameDisplayName = gameDispName;
	this.unitPrice = unitPrice.replace("{","").replace("}","").replace("null","default");
}
</script>

<script>	
		<%	
			Map jackPotMap=(Map)application.getAttribute("jackPotMap");
            Iterator iterMap = jackPotMap.entrySet().iterator();
			while (iterMap.hasNext()) {
				Map.Entry pair = (Map.Entry) iterMap.next();
				String gameName=Util.getGameName((Integer)pair.getKey());
		%>
				jackpotAmt['<%=gameName.toLowerCase()%>']='<%=FormatNumber.formatPDFNumbers((Double)pair.getValue())%>';	
		<%
			}
            Map freezeTime=(Map)request.getAttribute("freezeTimeMap");
            Map<Integer,GameMasterLMSBean> gameMap = Util.getGameMap();
			Map drawMap = (TreeMap)request.getAttribute("drawGameData");
			Map<Integer, Map<Integer, DrawDetailsBean>> drawIdMap = (Map<Integer, Map<Integer, DrawDetailsBean>>)request.getAttribute("drawGameNewData");
				
			ArrayList<String> userActionList= new ArrayList<String>(); 
			ArrayList<String> activeGameList= new ArrayList<String>(); 
            userActionList=(ArrayList<String>)session.getAttribute("ACTION_LIST");               
				
			System.out.println("drawMapinRposJSP*****"+drawMap);
				
			Iterator iter = drawMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry pair = (Map.Entry) iter.next();
				GameMasterLMSBean gameBean= gameMap.get((Integer)pair.getKey());
				String gameNameDev = gameBean.getGameNameDev();
				String gameDispName = gameBean.getGameName();
				if("Zerotonine".equals(gameNameDev)) {
					continue;
				}

				Map<String,Double> priceMap=new HashMap<String,Double>();
				for(Map.Entry<String,BetDetailsBean> entry:gameBean.getPriceMap().entrySet()){
					priceMap.put(entry.getKey(),entry.getValue().getUnitPrice());
				}
				
				System.out.println("PriceMap : "+priceMap); // **********
					
				List<List> drawList = (List<List>)pair.getValue();
				String nxtDrwTime = (drawList.get(0)).toString().replace("[","").replace("]","");	
				List<String> winList = 	(List<String>)drawList.get(1);
				//System.out.println("status*********************"+(String)drawList.get(2).get(0));
				Iterator winItr=winList.iterator();
				String winData = "";
				while (winItr.hasNext()) {
					winData = winData+winItr.next()+" Nxt ";					
				}
				
				for(int j=0;j<userActionList.size();j++)
				{
					if(userActionList.get(j).toLowerCase().equals(gameNameDev.toLowerCase()+"buy"))
					{
					  int index=userActionList.get(j).indexOf("Buy");
					  String fileName=userActionList.get(j).substring(0,index).toLowerCase();
					  System.out.println("file Name: "+fileName);
					  activeGameList.add(fileName);
					  
			%>
					
					  activeGames[gameIndx]='<%=gameNameDev.toLowerCase()%>';
					  gameDispName['<%=gameNameDev.toLowerCase()%>']=new gameNameObj('<%=gameDispName%>','<%=priceMap%>');					
					  gameIndx++;
					 <%System.out.println(nxtDrwTime);%>
					  var _<%=gameNameDev.toLowerCase()%>DrawTime='<%=nxtDrwTime%>';
					 <%System.out.println(winData);%>					
					  var _<%=gameNameDev.toLowerCase()%>Win='<%=winData%>';	
					  <%System.out.println(drawList.get(2));%>
					  var _<%=gameNameDev.toLowerCase()%>Status='<%=drawList.get(2).size() > 0 ? (String) drawList.get(2).get(0): ""%>';
					  var _<%=gameNameDev.toLowerCase()%>GetShortTimer;	
					  <%System.out.println(Util.getGameId(gameNameDev));%>
					  <%System.out.println(freezeTime);%>
					  var _<%=gameNameDev.toLowerCase()%>freezeTime='<%=freezeTime.get(Util.getGameId(gameNameDev))%>';
		   <%
				 	}
				}
				//System.out.println(nxtDrwTime+"***in rpos**"+pair.getKey());
				Collection<DrawDetailsBean> drawBeanList = drawIdMap.get(Util.getGameId(gameNameDev)).values();
		    %>
				var _<%=gameNameDev.toLowerCase()%>DrawIdObj = new Array();
		    <%
				for(DrawDetailsBean dbean: drawBeanList){
			%>
					_<%=gameNameDev.toLowerCase()%>DrawIdObj[<%=dbean.getDrawDateTime().getTime()%>] = <%=dbean.getDrawId()%>+','+'<%=dbean.getDrawName()%>';
		<%
				}					
			}
			
			String barcodeType = (String)application.getAttribute("BARCODE_TYPE");
%>
	

</script>
<script src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/common.js"></script>
<script type="text/javascript" language="javascript"> PWTWord = '<%=TextConfigurator.getText("PWT")%>'</script>
<%
for(int i=0;i<activeGameList.size();i++){
%>
<script	src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/games/<%= activeGameList.get(i) %>/<%= activeGameList.get(i) %>.js"></script>		 
<%} 
%>
	<body onkeydown="rposKeyPress(event)" onkeyup="rposKeyPressUp(event)" bgcolor="#d6d6d6">
		<div id="drawgame_wrap">
			<div id="topId" class="hideGame" style="display: none;">

			</div>
			<div id="info5Div" class="infoGame" style="display: none;">
			</div>
			<table border="0" cellspacing="0" cellpadding="0" width="100%" align="center">
				<tr>
					<td width="75%" valign="top">
						<div id="infoHideDiv" class="gameHide">
							<div id="drawDiv"></div>
						</div>
					</td>

					<td width="25%" valign="top" bgcolor="#d6d6d6" align="right"
						style="border: 1px #393939 solid; border-left: none; border-top: none">
						<table border="0" bordercolor="red" cellspacing="0"
							cellpadding="0" width="100%" bgcolor="#FFFFFF"
							style="border-bottom: 1px #646464 solid;">
							<%
								if (userActionList.toString().contains("dg_rep")) {
							%>
							<div class="drawgameheader"
								style="height: 21px; text-align: center; border: 2px solid #666; padding-top: 5px;"
								onclick="displayMainPageReports();">
								REPORTS
							</div>
							<%
								}
							%>
							<tr>
								<td valign="top" align="right">
									<table width="100%" border="0" cellspacing="0" cellpadding="0"
										bordercolor="red"
										style="margin-top: 5px; margin-left: 2px; margin-right: 2px;">
										<tr>
											<td align="center" valign="top">
												<table width="100%" border="0" cellspacing="0"
													cellpadding="0" bordercolor="red" bgcolor="#efba01"
													id="sideTbl">
													<tr style="cursor: hand;">
														<%
															if (userActionList.contains("verifyTicket")) {
														%>
														<script
															src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/playMgmt/js/pwtTicket.js"></script>
														<td width="20%" align="center" valign="middle"
															height="25px" onclick="pwtTable();"
															style="border-right: 1px #b27500 solid">
															<span class="txt7" id="pwtStyl">PWT</span>
														</td>
														<%
															}
															if (userActionList.contains("cancelTicket")) {
														%>
														<td width="25%" align="center" valign="middle"
															height="25px" onclick="cancelTable();"
															style="border-right: 1px #b27500 solid">
															<span class="txt8" id="canTktStyl"> Cancel Ticket
															</span>
														</td>
														<%
															}
															if (userActionList.contains("reprintTicket")) {
														%>
														<td width="30%" height="25px" align="center"
															valign="middle" style="border-right: 1px #b27500 solid">
															<span class="txt8" onclick="reprintTable();"
																id="repTktStyl"> Reprint Ticket </span>
														</td>
														<%
															}
															if (userActionList.contains("bo_dg_rep_tpTrackTicketMenu")) {
														%>
														<td width="25%" height="25px" align="center"
															valign="middle">
														<span class="txt8" onclick="trackTPTable();"
																id="trackTPTktStyl"> Verify TPT Ticket </span>
														</td>
														<%
															}
														%>
													</tr>
												</table>
											</td>
										</tr>
										<tr>
											<td align="center" valign="top">
												<div id="pwtDiv">
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
						<table border="0" cellspacing="0" cellpadding="0" width="100%">
							<tr>
								<td>
									<div id="parentApplet"
										style="display: block; overflow: auto; height: 300px; background-color: white;">
										<%
											if (barcodeType.equals("applet")) {
										%>
										<script>
					var codebase = "";
					function stopRender(){
							for (var j=0;j<document.applets.length;j++){
								if(typeof document.applets[j].isActive!="undefined"){
									codebase = (document.applets[j].codeBase).substring('<%=request.getContextPath()%>'.length);
									//document.execCommand('Stop') ;
									//alert(codebase);
									gamesData();fillGame(activeGames[0],'manualCall');							
								 winAjaxReq("jreVersion.action?jreVersion="+ codebase);
								}
							}
					}	
					</script>
	<%
		StringBuffer codebaseBuffer = new StringBuffer();
		codebaseBuffer.append(!request.isSecure() ? "http://" : "https://");
		codebaseBuffer.append(request.getServerName());
		if (request.getServerPort() != (!request.isSecure() ? 80 : 443)) {
			codebaseBuffer.append(':');
			codebaseBuffer.append(request.getServerPort());
		}
		codebaseBuffer.append(request.getContextPath());
		codebaseBuffer.append('/');
	%>
										<div id="regDiv"></div>
										
						<%	if("SIGNED".equalsIgnoreCase((String)application.getAttribute("APPLET_SIGNED"))){ %>
								<applet code="TicketApplet" codebase="<%=codebaseBuffer.toString()%>" jnlp_href="applets/App.jnlp" width="200" height="200"	name="TicketApp" mayscript>
									<param name="data" value="108172000002746000" />
										<div style="font-size:12px; height:300px; line-height:center;">
											<table>
												<tr>
													<td height="300px;" align="center">
														No Java Runtime Environment v 1.5.2 found!!<br/>
										        		<a style="color:red" href="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/jre-1_5_0_12-windows-i586-p.exe">Click to install</a>
										        	</td>
										        </tr>
									        </table>
									       </div>
								</applet>
						<%	}else if("UNSIGNED".equalsIgnoreCase((String)application.getAttribute("APPLET_SIGNED"))){%>
							<applet codebase="<%=request.getContextPath()%>/java1.5/"
								code="TicketApplet.class"  width="200" height="500" name="TicketApp" mayscript>
								<param name="data" value="108172000002746000" />
							</applet>
						<% }%>	
							
										
										<div id="regButton"></div>
										<%
											} else {
										%>

												<script>gamesData();fillGame(activeGames[1],'manualCall');</script>
										<%
											}
										%>
									</div>
								</td>
							</tr>
							<tr>
								<td>
									<div id="pwtResult"></div>
								</td>
							</tr>
						</table>
				</td>
				</tr>

				<tr>
					<td colspan="2">
						<div id="bottom">
							Powered by SUGAL & DAMANI
						</div>
						<!-- <div id='jackpotAmt' style="position:absolute;top:90px;left:400px;"></div> -->
						</td>
				</tr>
			</table>

		</div>
		<!--<div id="facebox_overlay"></div> -->
	</body>
<script>stopRender();</script> 
</html>



<code id="headId" style="visibility: hidden">
$RCSfile: rpos.jsp,v $
$Revision: 1.16 $
</code>