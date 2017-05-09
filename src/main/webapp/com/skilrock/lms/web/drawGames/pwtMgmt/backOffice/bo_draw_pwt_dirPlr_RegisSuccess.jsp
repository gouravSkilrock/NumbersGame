	<%@ page import="java.util.*,com.skilrock.lms.dge.beans.PWTDrawBean,com.skilrock.lms.dge.beans.DrawIdBean,com.skilrock.lms.dge.beans.FortunePurchaseBean,com.skilrock.lms.dge.beans.PanelIdBean"%>
	<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ page import="com.skilrock.lms.common.utility.FormatNumber"%>
	<%@ taglib prefix="s" uri="/struts-tags"%>
	
	<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
	<%
		response.setHeader("Cache-Control", "no-store"); //HTTP 1.1
		response.setHeader("Pragma", "no-cache"); //HTTP 1.0
		response.setDateHeader("Expires", 0); //prevents caching at the proxy server
	%>
	<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	
	<head>
		<s:head theme="ajax" debug="false" />
		<meta http-equiv="CACHE-CONTROL" content="NO-CACHE" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="https://fonts.googleapis.com/css?family=Roboto:400,300,500,700" rel="stylesheet" type="text/css" />
		<link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700" rel="stylesheet" type="text/css" />
		<link href='https://fonts.googleapis.com/css?family=Droid+Serif:700' rel='stylesheet' type='text/css' />
		<link href="https://netdna.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.css" rel="stylesheet" />
		<link rel="stylesheet" href="<%=request.getContextPath()%>/LMSImages/css/styles.css" type="text/css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ticket.css" media="screen" />
		<title><%=application.getAttribute("JSP_PAGE_TITLE")%></title>
	</head>
	<body>
		<%@include file="/com/skilrock/lms/web/loginMgmt/menu.jsp"%>
		<div id="wrap">
			<div id="top_form">
				<s:if test="%{#session.plrPwtAppDetMap.isAnonymous}">
					<h3>
						<s:text name="label.pls.pay" />
						<s:property value="#application.CURRENCY_SYMBOL" />
						<b> <s:set name="net_amount_paid"
								value="%{#session.plrPwtAppDetMap.NET_AMOUNT_PAID}" /> <%=FormatNumber.formatNumberForJSP(pageContext
							.getAttribute("net_amount_paid"))%>
						</b>
						<!--<s:property
									value="%{#session.plrPwtAppDetMap.NET_AMOUNT_PAID}" /> </b>-->
						<s:text name="label.to.plr" />
						.
					</h3>
				</s:if>
				<s:else>
					<h3>
						<s:text name="label.plr" />
						<s:property value="%{#session.plrPwtAppDetMap.plrBean.firstName}" />
						&nbsp;
						<s:property value="%{#session.plrPwtAppDetMap.plrBean.lastName}" />
	
						<s:text name="label.has.reg.for" />
						<s:text name="Direct_Player" />
						<s:text name="PWT" />
						<s:text name="label.process" />
						.
	
					</h3>
	
				</s:else>
				<s:iterator value="#session.plrPwtAppDetMap">
					<s:if test="%{key=='GAME_NAME'}">
						<s:set name="gameType" value="value"></s:set>
					</s:if>
				</s:iterator>
				<s:div id="pwtResult">
				</s:div>
			</div>
		</div>
		
        <div id="print_me" style="margin-left:400px;">
			<div class="prize" id="tktView" style="display: block; width:256px;">
				<h5>Ticket View</h5>
				<div class="ticket-view">
				   <div id="tktGen">
					  <div class="hr-line-s"></div>
					  <div class="ticket-format">
					      <div class="tkt-logo border-top">
					           <div class="logo-rel">
					           <s:if test="%{#application.COUNTRY_DEPLOYED=='ZIMBABWE'}">
					           	<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/africalotto_black.png" width="165" height="55">
					           </s:if>
					           <s:else>
					           	<s:property value="%{#application.ORG_NAME_JSP}"/>
					           </s:else>
					            </div>
					            <span class="tkt-gm_nm">
					               <s:iterator value="%{mainPwtBean.winningBeanList}">
								     <s:property value='gameDispName'/>
								   </s:iterator>
					            </span>
					            <div class="name-tkt-2">Winning Verification</div>
					            <div class="tkt-dt-m border-line">
					               <div class="tkt-rw">
					                  <span class="no-o-tkt">Ticket No:</span><span class="no-of-tkt"><s:property value="%{mainPwtBean.ticketNo}"/></span>
					                </div>
					                <div class="tkt-rw">
					                   <span class="no-o-dd">Draw Date</span>
					                   <span class="no-o-dt">Draw Time</span>
					                </div>
					                <s:iterator value="%{mainPwtBean.winningBeanList}">						      
									          <s:iterator value="%{drawWinList}">
										          <s:set name="is_unclm" value="%{status}"></s:set>
										          <s:if test="%{#is_unclm == 'UNCLM_PWT'}">
										               <div class="ndop">
										                    <div class="tkt-rw">
										                        <span class="no-of-ddd"><s:property value='drawDateTime.split("\\\*")[0].split(" ")[0]' /></span><span class="no-of-dtt"><s:property value='drawDateTime.split("\\\*")[0].split(" ")[1]' /></span>
										                    </div>
										                    <div class="ticket-dr-name">
										                       <s:property value="drawname" />
										                    </div>
										                    <div class="tkt-rw">
										                       <span class="tkt-dv-10"></span><span class="ticket-dr-name">Win!!</span>
										                    </div>
										                    <div class="tkt-rw">
										                        <span class="no-o-tkt">Win Amt: </span><span class="no-of-tkt">(INR)
											                        <s:set name="winnAmt" value="%{winningAmt}" />
											                        <%=FormatNumber.formatNumberForJSP(pageContext.getAttribute("winnAmt"))%>
										                         </span>
										                    </div>
										                </div> 							      
												  </s:if>
												  <s:if test="%{#is_unclm == 'RES_AWAITED'}">
										               <div class="ndop">
										                    <div class="tkt-rw"><span class="no-of-ddd"><s:property value='drawDateTime.split("\\\*")[0].split(" ")[0]' /></span><span class="no-of-dtt"><s:property value='drawDateTime.split("\\\*")[0].split(" ")[1]' /></span></div>
	                                                        <div class="tkt-rw"><span class="tkt-dv-10"></span><span class="ticket-dr-name">Result Awaited!!</span></div>
										               </div> 							      
												  </s:if>
											  </s:iterator>							     						    
									 </s:iterator>	
					                <div class="tkt-rw border-top">
					                    <span class="no-o-tkt">Total Pay: </span><span class="no-of-tkt">(INR) <s:property value="%{mainPwtBean.totlticketAmount}"/></span>
					                </div>
					             </div>
					             <div class="bottom-username border-top">
					                <span>
					                   <s:iterator value="%{mainPwtBean.winningBeanList}">
									      <s:property value="%{partyType}"/>						     						    
									   </s:iterator>	
				                    </span>
					             </div>
					        </div>
					    </div>
					
					<!-- For Reprint -->
					  <s:set name="irs_rp" value="%{mainPwtBean.Reprint}"></s:set>
					  <s:if test="%{#irs_rp}">					 
						 <div class="ticket-format">
						       <div class="tkt-logo border-top">
						          <div class="logo-rel"><img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/africalotto_black.png" width="165" height="55"></div>
						          <span class="tkt-gm_nm">
						               <s:iterator value="%{mainPwtBean.winningBeanList}">
									     <s:property value='gameDispName'/>
									   </s:iterator>
						          </span>
						       </div>
						       <div class="name-tkt-2">Reprint-at : 
						           <span>
						               <s:iterator value="%{mainPwtBean.winningBeanList}">
										   <s:property value="%{partyType}"/>						     						    
									   </s:iterator>
						           </span>
						        </div>
						        <div class="tkt-dt-m border-line">
						          <div class="tkt-rw"><span class="no-o-tkt">Ticket No:</span><span class="no-of-tkt">
						          <s:iterator value="mainPwtBean.purchaseBean">						                 
								            <s:property value="%{ticket_no}"/><s:property value="%{reprintCount}"/>				         
								  </s:iterator>
						          </span></div>
						          <div class="tkt-rw"><span class="no-o-tkt">Purchased On:</span>
						             <span class="no-of-tkt">
						                <s:iterator value="mainPwtBean.purchaseBean">
								            <s:property value='purchaseTime'/>
								        </s:iterator>
						             </span>
						          </div>
						        </div>
						        <div class="tkt-dt-m border-top">
						          <div class="tkt-rw"><span class="no-o-dd">Draw Date</span><span class="no-o-dt">Draw Time</span></div>			          
						          
						          <s:iterator value="%{mainPwtBean.winningBeanList}">						      
								      <s:iterator value="%{drawWinList}">						               			      
										   <div class="tkt-rw"><span class="no-of-ddd"><s:property value='drawDateTime.split("\\\*")[0].split(" ")[0]' /></span><span class="no-of-dtt"><s:property value='drawDateTime.split("\\\*")[0].split(" ")[1]' /></span></div>									 
									  </s:iterator>							     						    
								  </s:iterator>
						          
						          
						          <div class="tkt-rw border-top">
						            <span class="draw-nm-s">
						                <s:iterator value="mainPwtBean.purchaseBean">						                 
								            <s:property value="%{playType[0]}"/>				         
								        </s:iterator>
						            </span>
						            <span class="draw-nm-m">
						               Manual
						            </span>
						           <div class='ticket-text-pn'>
						                 <s:iterator value="mainPwtBean.purchaseBean">
						                    <s:property value="%{playerData[0]}"/>
								         </s:iterator>
						            </div>
						          </div>
						          <div class="tkt-dt-m border-top">
							            <s:iterator value="mainPwtBean.purchaseBean">
							               <div class="tkt-rw"><span class="no-o-tkt">No. of Line(s)</span><span class="no-of-tkt"><s:property value="%{noOfLines[0]}"/></span></div>
							               <div class="tkt-rw"><span class="no-o-tkt">Bet Amount(INR)</span><span class="no-of-tkt"><s:property value="unitPrice"/></span></div>
							               <div class="tkt-rw"><span class="no-o-tkt">No. of Draws:</span><span class="no-of-tkt"><s:property value="noOfDraws"/></span></div>
							               <div class="tkt-rw tlam"><span class="no-o-tkt ">Total Amount(INR)</span><span class="no-of-tkt"><s:property value="totalPurchaseAmt"/></span></div>				            			               
								        </s:iterator>
						          </div>
						          <div class="code-img"><img src="<%=request.getContextPath()%>/LMSImages/images/barcode.png" class="code_img" /><span class="code-no"><s:iterator value="mainPwtBean.purchaseBean"><s:property value="%{ticket_no}"/><s:property value="%{reprintCount}"/></s:iterator></span></div>
						          <div class="lottery-sign"><img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/signature.png" class="sign_img" /></div>
						          <div class="bottom-username border-top">
						              <span>
						                  <s:iterator value="%{mainPwtBean.winningBeanList}">
										     <s:property value="%{partyType}"/>						     						    
										  </s:iterator>	
					                  </span>
						          </div>
						          <div class="bottom-username">
						            <div class="dg-dg">
						               <s:iterator value="%{mainPwtBean.purchaseBean}">
						                  <s:iterator value="%{advMsg}">
						                     <s:property value="%{BOTTOM[0]}"/>	
						                  </s:iterator>								     					     						    
									   </s:iterator>
						            </div>
						          </div>
						        </div>
					     </div>
					  </s:if>   
				   </div>					
				</div>
		    </div>
	    </div>
	    <div style="margin-top:10px;">
	       <button onclick="printDiv('print_me')">Print Me</button>
	    </div>	    
	    
		 <script>						
				function printDiv() {
		            var divToPrint =  document.getElementById('print_me').innerHTML;
		            var popupWin = window.open('', '_blank', 'width=256, location=no,left=200px');
		            popupWin.document.open();
		            popupWin.document.write('<html><head><link  href="<%=request.getContextPath()%>/css/ticket.css" rel="stylesheet" type="text/css"><link  href="<%=request.getContextPath()%>/LMSImages/css/styles.css" rel="stylesheet" type="text/css"></head><body onload="window.print()">' + divToPrint + '</body></html>');
		            popupWin.document.close();            
		        }				
		</script> 
	</body>
</html>
	
	<code id="headId" style="visibility: hidden"> $RCSfile:
		bo_draw_pwt_dirPlr_RegisSuccess.jsp,v $ $Revision:
		1.1.2.2.4.2.2.11.10.3 $ </code>