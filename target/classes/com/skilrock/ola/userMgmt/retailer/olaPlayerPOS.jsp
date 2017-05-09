<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>Africa Lotto</title>
    <!-- <link href="https://fonts.googleapis.com/css?family=Roboto:400,300,500,700" rel="stylesheet" 

type="text/css"> -->
    <link href="https://netdna.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.css" rel="stylesheet">
    
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/draw-ui-stylesheet.css" 

media="screen">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/lms-style.css" media="screen">
	<!-- new css for ola and scratch -->
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/ajaxCall.js"></script>
    </head>

<body>

<!--section start-->
<div class="page-container">
	<div class="left-page-side">
        <div id="accordian">
    	<ul class="left-nav-side lp-SubMenu">
			<li id="reg">
           	 	<a href="#"><i class="icon-registration"></i><span class="title">Registration</span></a>
            </li>
			
        	<li id="dep">
           	 	<a href="#" ><i class="icon-deposit"></i><span class="title">Deposit</span></a>
            </li>
			
			<li id="withrwl">
           	 	<a href="#" ><i class="icon-withdraw"></i><span class="title">Withdrawal</span></a>
            </li>
            <li id="report">
           	 	<a href="#" ><i class="icon-report1"></i><span class="title">Report</span></a>
            </li>
        </ul>
        </div>
    </div>
    <div class="right-page-side">
    	<div class="page-content-area ola-content-area">
        	
            <div class="page-data">
            	<!--deposits-->
            	<div class="data-content" id="mainForm">
                	<h4 class="page-title">OLA Registration</h4>
                    <div class="detail-page">
                    <div class="group-list">
                                    <div class="date-input select-product col-3" id = "regWalletTab">
										
                                    </div>
                                   <!--  <div class="error-msg" id="wallet-error">
                                    	<span>Please select at least one</span>
                                    </div> -->
                      </div>
                    	<form id="regForm">
                        	<div class="form-row">
                                <div class="group-list" id="username">
                                	<label>Username<span class="mandatory">*</span></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="regUname" maxlength="20"/>
                                    </div>
                                    <div>
                                    	<span id="reg-username-msg"></span>
                                    </div>
                                </div>
                                
                                <div class="group-list" id="cardNo" style="display: none;">
                                	<label>Card Number<span class="mandatory">*</span></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="regCardNo" maxlength="20"/>
                                    </div>
                                    <div>
                                    	<span id="reg-cardNo-msg"></span>
                                    </div>
                                </div>
								
								<div class="group-list clearFix" >
									<div class="colLeft" id="mobile">
										<label>Mobile Number<span class="mandatory">*</span></label>
										<div class="date-input">
											<input type="text" class="date-box" id="regMobile" />
										</div>
										<div >
											<span id="reg-phone-msg" ></span>
										</div>
									</div>
									<div class="colRight" id="email">
										<label>Email ID<span class="mandatory">*</span></label>
										<div class="date-input">
											<input type="text" class="date-box"  id="regEmail" maxlength="50"/>
										</div>
										<div >
											<span id="reg-email-msg"></span>
										</div>
									</div>
                                </div>


                                <div class="group-list clearFix" >
									<div class="colLeft">
										<label>Amount <span class="mandatory" id ="amountAmt">*</span></label>
										<div class="date-input" >
											<input type="text" class="date-box" id="regAmt" maxlength="7"/>
										</div>
										<div class="error-msg">
											<span id="reg-amount-msg"></span>
										</div>
									</div>
									
									<div class="colRight" >
										<label>Verify Amount<span class="mandatory" id ="amountVAmt">*</span> </label>
										<div class="date-input" >
											<input type="text" class="date-box" id="regVerAmt" maxlength="7"/>
										</div>
										<div class="error-msg">
											<span id="reg-verifyamount-msg"></span>
										</div>
									</div>
                                </div>
                                
                                 <div class="group-list clearFix" >
									<div class="colLeft" id="idType" style="display: none;">
										<label>ID Type</label>
										<div class="date-input">
											<select class="drop-dowon" id="regIdType">
													<option value="-1">Please Select</option>
													<option value="NID">NID</option>
													<option value="DL">DL</option>
													<option value="Passport">Passport</option>
												</select>
										</div>
										<div >
											<span id="reg-idType-msg" ></span>
										</div>
									</div>
                                </div>
                                <div class="group-list clearFix" >
									<div class="colLeft" id="idNo" style="display: none;">
										<label>ID Number</label>
										<div class="date-input">
											<input type="text" class="date-box"  id="regIdNo" maxlength="50"/>
										</div>
										<div >
											<span id="reg-idNo-msg"></span>
										</div>
									</div>
                                </div>
                                
                                 <div class="group-list" id="idUpload" style="display: none;">
                                	<label>Image Upload</label>
                                    <div class="date-input">
											<input type="file" accept="image/*" id="imgUpload">
											<img id="userImg" src="#" alt="your image" style="width: 180px; height: 242px;display: none; margin-top: -223px;border: 1px solid black;">
                                    </div>
                                    <div>
                                    	<span id="reg-regIdFile-msg"></span>
                                    </div>
                                </div>
 
                                <div class="submit-btn">
                                    <input type="button" value="Register" id="regBtn">
                                </div>
                                <div class="clear"></div>
                        	</div>
                        </form>
                        
                        <form id="depositForm" style="display: none;">
                        	<div class="form-row">								
                        		<div class="group-list" id="depCardDiv" style="display: none;">
                                	<label>Card Number<span class="mandatory">*</span></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="depCardNo" maxlength="20"/>
                                    </div>
                                    <div>
                                    	<span id="dep-cardNo-msg"></span>
                                    </div>
                                </div>							
                                <div class="group-list" id="depMobDiv">
                                	<label>Mobile Number<span class="mandatory">*</span></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="depMob"/>
                                    </div>
                                    <div>
                                    	<span id="dep-mobile-msg"></span>
                                    </div>
                                </div>
                                <div class="group-list">
                                	<label>Amount<span class="mandatory">*</span></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="depAmt" maxlength="7" />
                                    </div>
                                    <div class="error-msg">
                                        <span id="dep-amt-msg"></span>
                                    </div>
                                </div>
                                <div class="group-list">
                                	<label>Confirm Amount<span class="mandatory">*</span></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="depVerAmt" maxlength="7"/>
                                    </div>
                                    <div class="error-msg">
                                    	<span id="dep-ver-amt-msg"></span>
                                    </div>
                                </div>
 
                                <div class="submit-btn">
                                    <input type="button" value="Deposit" id="depBtn">
                                </div>
                                <div class="clear"></div>
                        	</div>
                        </form>
                        
                        <form id="withdrawalForm" style="display: none;">
                        	<div class="form-row">
                       			<div class="group-list" id="withCardDiv" style="display: none;"> 
                                	<label>Card Number<span class="mandatory">*</span></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="withCardNo" maxlength="20"/>
                                    </div>
                                    <div>
                                    	<span id="with-cardNo-msg"></span>
                                    </div>
                                </div>								
                                <div class="group-list" id="withMobDiv">
                                	<label>Mobile Number<span class="mandatory">*</span></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="withMob" />
                                    </div>
                                    <div>
                                    	<span id="with-mobile-msg"></span>
                                    </div>
                                </div>
                                <div class="group-list">
                                	<label>Amount<span class="mandatory">*</span></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="withAmt" maxlength="7"/>
                                    </div>
                                    <div class="error-msg">
                                        <span id="with-amt-msg"></span>
                                    </div>
                                </div>
                                <div class="group-list">
                                	<label>Verification Code<span class="mandatory">*</span></label>
                                    <div class="date-input">
										<input type="text" class="date-box" id="withVerCode" maxlength="8"/>
                                    </div>
                                    <div class="error-msg">
                                    	<span id="with-verCode-msg"></span>
                                    </div>
                                </div>
 
                                <div class="submit-btn">
                                    <input type="button" value="Done" id="withBtn">
                                </div>
                                <div class="clear"></div>
                        	</div>
                        </form>
                                               
                    </div>
                </div>
                
                <!-- Player reg confirmation box start -->
                   <div class="conf-box" id="regSuccessBox" style="display: none;">
                        <div class="box-crm" >
                            <div class="succes-icon">
                             	<img src="<%=request.getContextPath()%>/LMSImages/images-UI/success.png" alt="" id="regSuccessImg" >
                             	<img src="<%=request.getContextPath()%>/LMSImages/images-UI/faiure-icon.png" alt="" style="display: none;" id="regFailureImg">
                                <h2 id="regSuccessMsg">Registration Success!</h2>
                                <h3 style="display:none" id="regFailureMsg">Registration Failure!</h3>
                            </div>
                                <div class="box-row top-bottom" id="regSuccessData">
                                 </div>
                        </div>
                    </div>
                     <!-- Player reg confirmation box Ends -->
                     
                     <!-- deposit confirmation box -->
                <div class="conf-box" style="display:none" id="depSuccessBox">
                	<div class="box-crm">
                    	<div class="succes-icon">
                         <img src="<%=request.getContextPath()%>/LMSImages/images-UI/success.png" alt="" id="depSuccessImg">
                         <img src="<%=request.getContextPath()%>/LMSImages/images-UI/faiure-icon.png" alt="" style="display:none"id="depFailureImg">
                            <h2 id="depSuccessMsg">Deposit Success!</h2>
                            <h3 style="display:none" id="depFailureMsg">Deposit Failure!</h3>          
                        </div>
                    	
                        	<div class="box-row" id="depSuccessData">
                                </div>
                            
                            <div class="box-modal-footer">
                                <button type="button" class="btn-ok" id="backDep">Back to deposit</button>
                            </div>
                    </div>
                </div>
                
                    <!-- withdrawal confirmation box -->
                <div class="conf-box" style="display:none" id="withSuccessBox">
                	<div class="box-crm">
                    	<div class="succes-icon">
                         <img src="<%=request.getContextPath()%>/LMSImages/images-UI/success.png" alt="" id="withSuccessImg">
                         <img src="<%=request.getContextPath()%>/LMSImages/images-UI/faiure-icon.png" alt="" style="display:none" id="withFailureImg">
                            <h2 id="withSuccessMsg">Withdrawal Success!</h2>
                            <h3 style="display:none" id="withFailureMsg">Withdrawal Failure!</h3>          
                        </div>
                    	
                        	<div class="box-row" id="withSuccessData">
                                </div>
                            
                            <div class="box-modal-footer">
                                <button type="button" class="btn-ok" id="backWith">Back to Withdrawal</button>
                            </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <div class="clear"></div>
    <div class="footer">
    	<div class="page-footer-inner"> 2016 &copy; Sugal & Damani Group</div>
    </div>
    
    <!--popup-->
    
    <!--popup-->
</div>

<!-- Reg confirm pop up start -->
	<div class="pop-up-box" style="display:none" id="olaRegConfirmBox">
	    	<div class="box-model">
	        	<div class="box-modal-header">
	                <h4 class="modal-title">Registration Confirmation</h4>
	            </div>
	            <div class="model-page-box">
	            	<div class="box-row" id="regPopUpData">
	            	  
	                </div>
	            </div>
	            <div class="box-modal-footer">
	            	<button type="button" class="btn-cancel" id="regCancel">Cancel</button>
	                <button type="button" class="btn-ok" id="regConfirm">Confirm</button>
	            </div>
	        </div>
	</div>
<!-- Reg confirm pop up End -->

<!-- Deposit confirm pop up start -->
    <div class="pop-up-box" style="display:none" id="olaDepConfirmBox">
    	<div class="box-model">
        	<div class="box-modal-header">
                <h4 class="modal-title">Deposit Confirmation</h4>
            </div>
            <div class="model-page-box">
            	<div class="box-row" id="depPopUpData">
                    </div>
            </div>
            <div class="box-modal-footer">
            	<button type="button" class="btn-cancel" id=depCancel>Cancel</button>
                <button type="button" class="btn-ok" id=depConfirm>Confirm</button>
            </div>
        </div>
  </div>
<!-- Deposit confirm pop up Ends -->
    
<!-- Withdrawal confirm pop up start --> 
    
      <div class="pop-up-box" style="display:none" id="olaWithConfirmBox">
    	<div class="box-model">
        	<div class="box-modal-header">
                <h4 class="modal-title">Withdrawal Confirmation</h4>
            </div>
            <div class="model-page-box">
            	<div class="box-row" id="withPopUpData">
            	 
                </div>
            </div>
            <div class="box-modal-footer">
            	<button type="button" class="btn-cancel" id=withCancel>Cancel</button>
                <button type="button" class="btn-ok" id=withConfirm>Confirm</button>
            </div>
        </div>
    </div>
    
    <!-- Withdrawal confirm pop up Ends --> 

</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/ola/js/olaPos.js"></script>
	<script>var projectName="<%=request.getContextPath()%>"</script>
	<script>var currencySymbol ='<%=application.getAttribute("CURRENCY_SYMBOL")%>';</script>
</html>