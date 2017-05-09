<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.web.drawGames.common.Util"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean;"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML>
<html>
<head>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Africa Lotto</title>
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,300,500,700" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700" rel="stylesheet" type="text/css">
    <link href="https://netdna.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/draw-ui-stylesheet.css" media="screen">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/lms-style.css" media="screen">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery.datetimepicker.css" type="text/css" media="screen">
    <script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/ajaxCall.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
    
    </head>

<body>
<!--section start-->
<input type="hidden" id="currentServerTime" value="<%=Util.getCurrentTimeString() %>">
<input type="hidden" id="userOrgId" value="<%=((UserInfoBean)session.getAttribute("USER_INFO")).getUserOrgId()%>">
<div class="page-container">
	<div class="left-page-side">
        <div id="accordian">
    	<ul class="left-nav-side lp-SubMenu">
			<li id="winClaim" class="scratch">
           	 	<a href="#" ><i class="fa fa-trophy" aria-hidden="true"></i><span class="title"><s:text name="label.scratch.navbar.winningclaim"/></span></a>
            </li>
              <li id="sellTkt" class="scratch">
           	 	<a href="#" ><i class="fa fa-ticket" aria-hidden="true"></i><span class="title"><s:text name="label.scratch.navbar.saleticket"/></span></a>
            </li>
        	<li id="activate" class="scratch">
           	 	<a href="#" ><i class="fa fa-book" aria-hidden="true"></i><span class="title"><s:text name="label.scratch.navbar.activatebook"/></span></a>
            </li>
			<li id="receiveReg" class="scratch">
           	 	<a href="#" ><i class="fa fa-envelope" aria-hidden="true"></i><span class="title"><s:text name="label.scratch.navbar.bookReceiveRegister"/></span></a>
            </li>
            <li id="soldTkt" class="scratch">
           	 	<a href="#" ><i class="fa fa-bar-chart" aria-hidden="true"></i><span class="title"><s:text name="label.scratch.navbar.soldTicketEntry"/></span></a>
            </li>
            <li id="qOrder" class="scratch">
           	 	<a href="#" ><i class="fa fa-check-circle" aria-hidden="true"></i><span class="title"><s:text name="label.scratch.navbar.quickOrder"/></span></a>
            </li>
            <li id="trackOrder" class="scratch">
           	 	<a href="#" ><i class="fa fa-search" aria-hidden="true"></i><span class="title"><s:text name="label.scratch.navbar.trackOrder"/></span></a>
            </li>
        </ul>
        </div>
    </div>
    <div class="right-page-side">
    	<div class="page-content-area ola-content-area">
        	
            <div class="page-data">
                <!--setting right-->
                <div class="setting-right">
                	<button class="setting-btn"><i class="setting-icon"></i></button>
                    <div class="setting-box">
                    	<a href="#" id="report"><s:text name="menu.report"/></a>
                    </div>
                </div>
                <!--setting right-->
                
                <!--Result OLA-->
                <div class="data-content">
                	<h4 class="page-title"></h4>
				 <div class="detail-page" id="winningClaimForm">
                    	<form>
                        	<div class="form-row">								
                                <div class="group-list">
                                	<label><s:text name="label.scratch.form.input.ticket.no"/></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="winTktNbr" maxlength="20"/>
                                    </div>
                                    <div class="error-msg">
                                    	<span id="winTktErrSpan"></span>
                                    </div>
                                </div>
                                <div class="group-list">
                                	<label><s:text name="label.scratch.form.input.virn.no"/></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="winVirnNbr" maxlength="16"/>
                                    </div>
                                    <div class="error-msg">
                                        <span id="winVirnErrSpan"></span>
                                    </div>
                                </div>

                                <div class="submit-btn">
                                    <input type="button" value="<s:text name="label.scratch.btn.verify.and.save"/>" id="winningBtn">
                                </div>
                                <div class="clear"></div>
                        	</div>
                        </form>
                    </div>
                    
                     <div class="detail-page" id="bookActivateForm">
                    	<form onsubmit="return false;">
                        	<div class="form-row">								
                                <div class="group-list">
                                	<label><s:text name="label.scratch.form.input.book.no"/></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id ="bookNumber" maxlength="15" />
                                    </div>
                                    <div class="error-msg">
                                    	<span id="bookActivationSpan"></span>
                                    </div>
                                </div>

                                <div class="submit-btn">
                                    <input type="button" value="<s:text name="label.scratch.btn.activate"/>" id="activateBtn">
                                </div>
                                <div class="clear"></div>
                        	</div>
                        </form>
                    </div>
             		
             		<!-- Sale Ticket Entry Starts -->
             		
			   			 <div class="detail-page" id ="saleTicketForm">
                    	<form onsubmit="return false;">
                        	<div class="form-row">								
                                <div class="group-list">
                                	<label><s:text name="label.scratch.form.input.ticket.no"/></label>
                                    <div class="date-input">
											<input type="text" id="saleTktNbr" class="date-box" maxlength="20" />
                                    </div>
                                    <div class="error-msg">
			                             	<span id="saleTktMsg"></span>
                                    </div>
                                </div>

                                <div class="submit-btn">
                                    <input type="button"  id="saleTktSubmit" value="<s:text name="label.scratch.btn.mark.as.sold"/>">
                                </div>
                                <div class="clear"></div>
                        	</div>
                        </form>
                    </div>
                    
                    
     		<!-- Sale Ticket Entry Ends -->
                    
		        <div class="search-view-data detail-page" id="soldTktEntryForm" style="width: 100%;">
		         <form onsubmit="return false;">
                	<div class="sold-tkt-e">
                	   <div id="soldTktDataEntry">
                    	
                        </div>
                    </div>
                     <!--  <div class="submit-btn">
                            <input type="button" value="Submit" id="soldTktSubmit">
                        </div> -->
                     </form>
                </div>
		
			
				         <!-- Book receieve reg. form  start -->
			
						<div class="detail-page" id="bookReceiveForm" style="width: 100%;">
						
	                    	<%-- <form style="width: 480px; margin: auto;" >
	                        	<div class="form-row">								
	                                <div class="group-list clearFix">
	                                	<label>Enter Challan ID</label>
	                                    
										<div class="date-input">
											<input type="text" class="date-box"  id="challanId" maxlength="25"/> 
	                                    </div>
										
										<div class="error-msg">
	                                    	<span id="chlErrMsg"></span>
	                                    </div>
										
										 
										<div class="submit-btn">
											<input type="button" id="bookRcvBtn" value="<s:text name="label.scratch.btn.get.detail"/>">
										</div>
	
	
	                                </div>
	
	
	                                <div class="clear"></div>
	                        	</div>
	                        </form> --%>
	                        <div id = "fetchChallanForm"></div>
							<div class="book-data" id="challanBookData"></div>	
	                  </div>
	                  <div id="dlChallanTable"></div>
                  
                   <!-- Book receieve reg. form ends -->
			
             	 <!-- Quick Order form start -->
                  
	                  <div class="detail-page" id="quickOrderForm" style="width: 100%;">
	                    	<form>
	                        	<div class="form-row">	
									
									<div class="quickOrderGrid"  id="quickOrderDataDiv">
										
									</div>	
	                                <div class="clear"></div>
	                        	</div>
	                        </form>
	                    </div>      
                    
                    <!-- Quick Order form ends -->         
                    
                    <!--Track order form start -->
	                    
	                      <div class="detail-page" id="trackOrderForm" style="width:100%;">
	                    	<form style="width: 480px; margin: auto;">
	                        	<div class="form-row">
									
									<div class="group-list clearFix">
										<div class="colLeft">
											<label><s:text name="label.scratch.form.input.game.name"/></label>
											<div class="date-input">
												<input type="text" id="trackGameName" class="date-box" maxlength="15">
											</div>
											<div class="error-msg">
												<span id="track-game-msg"></span>
											</div>
										</div>
										<div class="colRight">
											<label><s:text name="label.scratch.form.input.game.number"/></label>
											<div class="date-input">
												<input type="text" id="trackGameNo" class="date-box" maxlength="3">
											</div>
											<div class="error-msg">
												<span id="track-game-no-msg"></span>
											</div>
										</div>
	                                </div>
	                                <div class="group-list clearFix">
										<div class="colLeft">
											<label><s:text name="label.scratch.form.input.order.status"/></label>
											<div class="date-input">
												<select class="drop-dowon" id="trackOrderStatus">
													<option value="ALL"><s:text name="label.scratch.dropdown.option.all"/></option>
													<option value="PROCESSED"><s:text name="label.scratch.dropdown.option.processed"/></option>
													<option value="CLOSED"><s:text name="label.scratch.dropdown.option.closed"/></option>
													<option value="REQUESTED"><s:text name="label.scratch.dropdown.option.closed"/></option>
													<option value="DENIED"><s:text name="label.scratch.dropdown.option.denied"/></option>
													<option value="SEMI_PROCESSED"><s:text name="label.scratch.dropdown.option.semi.processed"/></option>
													<option value="APPROVED"><s:text name="label.scratch.dropdown.option.approved"/></option>
													<option value="AUTO_APPROVED"><s:text name="label.scratch.dropdown.option.auto.approved"/></option>
												</select>
											</div>
											<div class="error-msg">
												<span id="track-order-status-msg"></span>
											</div>
										</div>
										<div class="colRight">
											<label><s:text name="label.scratch.form.input.order.id"/></label>
											<div class="date-input">
												<input type="text" id="trackOrderId" class="date-box" maxlength="10">
											</div>
											<div class="error-msg">
												<span id="track-orderId-msg"></span>
											</div>
										</div>
	                                </div>
	                                <div class="group-list clearFix">
										<div class="colLeft">
											<label><s:text name="label.scratch.form.input.start.date"/></label>
											<div class="date-input">
												<input type="text" id="selectedDateTimePicker"  name="" class="date-box">
	                                          	<img id="selectedDateTimePickerDiv" src="<%=request.getContextPath()%>/LMSImages/images-UI/calendar.png" alt="">
											</div>
											<div class="error-msg">
												<span id="track-startDate-msg"></span>
											</div>
										</div>
										<div class="colRight">
											<label><s:text name="label.scratch.form.input.end.date"/></label>
											<div class="date-input">
												<input type="text" id="selectedDateTimePicker1" name="" class="date-box">
	                                            <img id="selectedDateTimePickerDiv1" src="<%=request.getContextPath()%>/LMSImages/images-UI/calendar.png" alt="">
											</div>
											<div class="error-msg">
												<span id="track-endDate-msg"></span>
											</div>
										</div>
	                                </div>
	
	                                <div class="submit-btn">
	                                    <input type="button" id="trackOrderBtn" value="<s:text name="label.scratch.btn.search"/>">
	                                </div>
	                                <div class="clear"></div>
	                        	</div>
	                        </form>
	                        <div class="search-view-data">
	                			<div id="searchOrderDiv">
		                    	 </div>
	                  			<div id="orderDetailDiv">			
	                    		</div>
	                		</div>
	                        
	                  	</div>
                  	
                  	<!--Track order form ends -->
                  	
                  	  	<div class="popup systemMSG error" id="timeErrorDiv" style="display: none;">
					    	<h5>Error<button id="close-popup-button"></button></h5>
					          <div class="modal-bodyWrap">
					             <div class="modal-body">
					                <div class="row">
					                   <div class="col-xs-9 msgBox" id="erroMsg"></div>
					                </div>                  
					             </div>
					          </div>
			    		</div>
                  	 <div class="conf-box" id="winSuccessBox" style="display: none;">
                        <div class="box-crm" >
                            <div class="succes-icon">
                             	<img src="<%=request.getContextPath()%>/LMSImages/images-UI/success.png" alt="" id="winSuccessImg" >
                             	<img src="<%=request.getContextPath()%>/LMSImages/images-UI/faiure-icon.png" alt="" style="display: none;" id="regFailureImg">
                                <h2 id="regSuccessMsg"><s:text name="msg.scratch.success"/></h2>
                                <h3 style="display:none" id="regFailureMsg"><s:text name="msg.scratch.failure"/></h3>
                            </div>
                                <div class="box-row top-bottom" id="winSuccessData">
                                 </div>
                        </div>
                    </div> 	
                </div>
                                       
            </div>
        </div>
    </div>
    <div class="clear"></div>
    <div class="footer">
    	<div class="page-footer-inner"> 
    	<!-- 2016 &copy; Sugal and Damani Group -->
    	</div>
    </div>
    
    <!--popup-->
</div>
    <!--popup-->

<!--header end-->
</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/scratchPOS.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.datetimepicker.js"></script>
<script>var projectName="<%=request.getContextPath()%>"</script>
	<script>var currencySymbol ='<%=application.getAttribute("CURRENCY_SYMBOL")%>';</script>
	<script>var countryDeployed ='<%=application.getAttribute("COUNTRY_DEPLOYED")%>';</script>
</html>