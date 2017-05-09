<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.beans.UserInfoBean;"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Play Online</title>
<link href="https://fonts.googleapis.com/css?family=Roboto:400,300,500,700" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700" rel="stylesheet" type="text/css">
<link href="https://netdna.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/draw-ui-css.css" media="screen">
<link href="<%=request.getContextPath()%>/css/instant-win.css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
<!--[if lt IE 9]>
	<script type="text/javascript" >
		$(document).ready(function(){
			$('.middle-col.inst-midWrap').css('width', $(document).width() - 312+ 'px');
		});
	</script>
<![endif]-->
</head>
<body style="position:relative;">
	<input type="hidden" id="sessionId" value="<%=((UserInfoBean)session.getAttribute("USER_INFO")).getUserSession()%>">
	<input type="hidden" id="userName" value="<%=((UserInfoBean)session.getAttribute("USER_INFO")).getUserName()%>">
	<input type="hidden" id="parentOrgName" value="<%=((UserInfoBean)session.getAttribute("USER_INFO")).getParentOrgName()%>">
	 <div class="middle-col center-fix inst-midWrap">
	    <!--game play-->
	  	 <div class="inst-win-html">
	
	                <div class="instantWinWrapOuter" >
	                    <ul class="instantWinWrapInner" id="iwMidPanel"></ul> 
	                    <div class="clear"></div>                   
	                </div>
	   	 </div>
	   	 <div class="popup systemMSG error" id="error-popup" style="display: none;">
	    	<h5>Error<button id="err-popup-button"></button></h5>
	          <div class="modal-bodyWrap">
	             <div class="modal-body">
	                <div class="row">
	                   <div class="col-xs-9 msgBox" id="error"></div>
	                </div>                  
	             </div>
	          </div>
	    </div>
	</div>
  	<!--game play-->
   	<!--Right side-->
      <div class="right-col right-fix">
           <div class="win-set-button">
                   <div class="insta-winning-button" id="pwt" data-toggle="modal" data-target="#mymodal" data-keyboard="false">
				    <button>
					    <div class="figure"><img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/winning-claim.png" width="35"><span>Winning Claim</span></div>
				    </button>
				</div>
            </div>
            <div class="insta-detail">
   			<h5>Ticket Preview</h5>
			<div class="ticket-insta-view">
       			<div class="ticket-div" id="ticketDiv">
					 <div class="panel-body"  id="parentAppletIW" style="display: none; overflow: auto; height: 550px; background-color: white;">
					    <div id="regDivIW"></div>
					    <applet width="220" height="450" mayscript="" archive="applets/gson-2.2.2.jar" name="TicketApp" jnlp_href="applets/App.jnlp" codebase="<%=request.getContextPath()%>" code="PCPOSAppletTicketEngine"></applet>
					    <div id="regButtonIW"></div>
				     </div>
       			</div>
		    </div>		                 
   		</div>    		
    </div>
 <!--Right side-->

	<!-- POPUP -->
	<div class="full-screen" style="display:none;" id="buyPopUp">
	    <div class="popup instantWinPop">
	    	<h5 id="gameName"><span class="popTitle"></span><button class="popUpClose"></button></h5>
	        
	            <div class="modal-bodyWrap">
	               <div class="modal-body">
	                  <div class="row-ins">
	                  	<div class="col-xs-instant"><img id="img" src="" alt="" style="display:block; margin: auto;width:100%;"></div>
	                    <div class="col-xs-instant8"><p id="gameDesc"></p>
		                <div class="row">
               	  	       <div class="col-xs-instant8 modelbtnBox wid100">
		                      <a href="#" data-dismiss="modal">
                          		 <div class="btncancel" id="cancelBtn">
                              		<div class="priceBox-c">Cancel</div>
                           		</div>
                       		 </a>
		               	      
		                     
		                        <a href="#" data-dismiss="modal" onclick="doPurchase()">
		                           <div class="btnOuterWrap">
		                              <div class="priceBox"></div>
		                              <div class="btnTitle">Buy Ticket</div>
		                           </div>
		                        </a>                        
		                  </div>
		                </div>
		                </div>
	               </div>
	               <div class="row-ins">
                  	</div>
                  <div class="clear"></div>
	            </div>
	    	</div>
		</div>
	</div>
<!-- PopUP end -->
 <!-- Modal Starts -->
	  <div class="full-screen" id="pwt-win" style="display: none">
		    <div class="popup instantWinPop">
		    <h5>Please enter ticket number <button id="pwtClose"></button></h5>
					<div class="modal-body">
						<div class="tkt-no-ct" style="width: 60%;">
							<input type="text" id="pwtTicket" maxlength="20">
						</div>
						<div class="error-msg" id="error-message"></div>
					</div>
					<div class="col-span-12 text-center">
                     <div class="button-cancel">
                        <button id="pwtCancel">Cancel</button>
                     </div>
                     <div class="button-submit">
                        <button id="pwtOk">Ok</button>
                     </div>
                  </div>
					
		    </div>
		</div>
		<!-- Modal Ends -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/instantWin.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/ajaxCall.js"></script>

<script>var path="<%=request.getContextPath()%>";</script>
<script>var projectName="<%=request.getContextPath()%>";</script>
<script>var currencySymbol ='<%=application.getAttribute("CURRENCY_SYMBOL")%>';</script>
<script>var organizationName ='<%=application.getAttribute("ORG_NAME_JSP")%>';</script>
</body>
</html>
