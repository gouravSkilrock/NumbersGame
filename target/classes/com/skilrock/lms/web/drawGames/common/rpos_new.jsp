<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.skilrock.lms.web.drawGames.common.Util"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<title>Play Online</title>
<!-- draw ui css-->
<link href="https://fonts.googleapis.com/css?family=Roboto:400,300,500,700" rel="stylesheet" type="text/css">
<link href="https://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700" rel="stylesheet" type="text/css">
<link href='https://fonts.googleapis.com/css?family=Droid+Serif:700' rel='stylesheet' type='text/css'>
<link href="https://netdna.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.css" rel="stylesheet">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery.datetimepicker.css" type="text/css" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/draw-ui-css.css" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/ticket.css" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css" media="screen">

	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/theme-zim.css" media="screen">

<!--end draw ui css-->
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/jquery-1.10.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/circular-countdown.js"></script>
<!--[if lt IE 9]>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/posie8.css" media="screen">
<script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/posie8.js"></script>
<![endif]-->
</head>

<body class="page-account" id="page-top">
<input type="hidden" id="currentServerTime" value="<%=Util.getCurrentTimeString() %>">
<div class="beforePageLoad"></div>
<div class="draw-ui" id="pc_pos_game_pannel" style="display: block;">
	<!-- Left Side Starts -->
	<div class="left-col">
          <div class="left-draw-ui">
                    <h5>Game Play</h5>
                    <div class="ui-left-menu" id="side-game-menu">
                    </div>
                 <!--Banner-->
                    <div class="gm-banner">
                    	<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/game-banner.jpg" width="155" alt="">
                    </div>
                <!--Banner-->
            </div>
    </div>
    <!-- Left Side Ends -->
	<!-- Middle Section Starts -->
	<div class="middle-col">
		<div class="draw-no-top">
	    	<div class="draw">
	        <div class="draw-name"><h5>Draw</h5></div>
	        <div class="draw-right">
	        <div class="no-of-draw">
	        	<span class="number-of">No. Of Draws</span>
	            <input type="text" id="numOfDrawsSelected">
	        </div>
	       	 <span class="or">OR</span>
	         <div class="advance-draw" id="advDrawSel">
	         	<span class="number-of">Advance Draw</span>
	         	<a href="#"><span id="noOfAdvDraws">...</span></a>
	         </div>
	         </div>
	        </div>
	        <div class="results">
	        	<div class="result-name"><h5>Results</h5></div>
	            <div class="results-number" id="othersResults">
	            	<div class="win-nmd" id="winDraw"></div>
	            	<ul id="winNum"></ul>
    	           	<ul id="fortunewinNum" class="sign-res" style="display: none;"></ul>
	            </div>
	            <div class="minirou-results-number" id="rouletteResults">
	                <ul class="cls" id="winNumRoulette"></ul>
            	</div>
	        </div>
	    </div>
	    <!--draw results side-->
		<div class="bet-type">
	    	<div class="bettype" id="betTypeSection">
	        	<div class="bet-name"><h5>Bet&nbsp;Type</h5></div>
	            <div class="bet-tab" id="bet-info-div"></div>
	        </div>
	        <div class="bettype float-right" id="numbertopicked" style=" display: none;">
	        	<div class="bet-name bet-name-n"><h5>No. to be picked</h5></div>
	            <div class="bet-tab text-center" id="bet-info-div">
	            	<input type="text" class="ntpk" id="numToBePicked">
	            </div>
	        </div>
	        <div class="quick-p-picked">
				<div class="qp-ck">
					<label>Quick Pick <input type="checkbox" id="qpCheck"></label>
				</div>
				<div id="zimQpTextboxName" style="display:none;"></div>
				<div id="kenoTwoQpTextboxName" style="display:none;">Enter no. to be picked</div>
				<div id="twelveByTwentyQpTextboxName" style="display:none;">Enter no. to be picked</div>
				<div class="qp-pkd-no" style="display:none;" id="twelveByTwentyQp">
					<input type="text" id="twelveByTwentyQpBox">
				</div>
				<div class="qp-pkd-no" style="display:none;" id="zimLottoBonusQp">
					<input type="text" id="zimQpTextbox">
				</div>
				<div class="qp-pkd-no" style="display:none;" id="kenoTwoQp">
					<input type="text" id="numToBePickedFN">
				</div>
	        </div>
	    </div>
	    <!--number show-->
	    <!-- 12/24 -->
	    <div class="numbers-area" id="twelveByTwentyFour" style="display:none;">
	    	<div class="number-select">
		    	<ul>
		            <li class="pmsnumber" id="num01"><span>01</span></li>
		            <li class="pmsnumber" id="num02"><span>02</span></li>
		            <li class="pmsnumber" id="num03"><span>03</span></li>
		            <li class="pmsnumber" id="num04"><span>04</span></li>
		            <li class="pmsnumber" id="num05"><span>05</span></li>
		            <li class="pmsnumber" id="num06"><span>06</span></li>
		            <li class="pmsnumber" id="num07"><span>07</span></li>
		            <li class="pmsnumber" id="num08"><span>08</span></li>
		            <li class="pmsnumber" id="num09"><span>09</span></li>
		            <li class="pmsnumber" id="num10"><span>10</span></li>
		            <li class="pmsnumber" id="num11"><span>11</span></li>
		            <li class="pmsnumber" id="num12"><span>12</span></li>
		            <li class="pmsnumber" id="num13"><span>13</span></li>
		            <li class="pmsnumber" id="num14"><span>14</span></li>
		            <li class="pmsnumber" id="num15"><span>15</span></li>
		            <li class="pmsnumber" id="num16"><span>16</span></li>
		            <li class="pmsnumber" id="num17"><span>17</span></li>
		            <li class="pmsnumber" id="num18"><span>18</span></li>
		            <li class="pmsnumber" id="num19"><span>19</span></li>
		            <li class="pmsnumber" id="num20"><span>20</span></li>
		            <li class="pmsnumber" id="num21"><span>21</span></li>
		            <li class="pmsnumber" id="num22"><span>22</span></li>
		            <li class="pmsnumber" id="num23"><span>23</span></li>
		            <li class="pmsnumber" id="num24"><span>24</span></li>
		        </ul>
	        </div>
	        <div class="enter-number">
	        	<!-- <span class="error-msg" id="error"></span> -->
	        	<div class="input-area twelveByTwenty-input-area">
	            	<input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty1">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty2">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty3">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty4">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty5">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty6">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty7">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty8">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty9">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty10">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty11">
	                <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty12">
	                <div class="twelveByTwenty-perm12-input-area" style="display:none">
	                      <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty13">
	                      <input type="text" minlength="2" maxlength="2" class="manualNumberEnter"  id="twelveByTwenty14">
	                </div>
	            </div>
	            <div class="reset" id="resetTwelveByTwentyFour">
	            	<button>Reset</button>
	            </div>
	        </div>
	         <div class="timer-TwelveByTwentyFour timer-div" style="display: none;">
	       		 <div class="tmr"><div class="draw-fr">DRAW FREEZED</div></div>
	 	     </div>
	    </div>
	    <!-- 12/24 -->
	    <!-- 10/20 start -->
	     <div class="numbers-area"  id="tenByTwenty" style="display:none;">
	    	<div class="number-select">
		    	<ul>
		            <li class="tenByTwentyNumber" id="tbtnum01"><span>01</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum02"><span>02</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum03"><span>03</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum04"><span>04</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum05"><span>05</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum06"><span>06</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum07"><span>07</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum08"><span>08</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum09"><span>09</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum10"><span>10</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum11"><span>11</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum12"><span>12</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum13"><span>13</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum14"><span>14</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum15"><span>15</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum16"><span>16</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum17"><span>17</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum18"><span>18</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum19"><span>19</span></li>
		            <li class="tenByTwentyNumber" id="tbtnum20"><span>20</span></li>
		        </ul>
	        </div>
	        <div class="enter-number">
	        	<!-- <span class="error-msg" id="error"></span> -->
	        	<div class="input-area">
	            	<input type="text" minlength="2" maxlength="2" class="tenByTwentyManualNumberEnter" id="tenByTwenty1">
	                <input type="text" minlength="2" maxlength="2" class="tenByTwentyManualNumberEnter" id="tenByTwenty2">
	                <input type="text" minlength="2" maxlength="2" class="tenByTwentyManualNumberEnter" id="tenByTwenty3">
	                <input type="text" minlength="2" maxlength="2" class="tenByTwentyManualNumberEnter" id="tenByTwenty4">
	                <input type="text" minlength="2" maxlength="2" class="tenByTwentyManualNumberEnter" id="tenByTwenty5">
	                <input type="text" minlength="2" maxlength="2" class="tenByTwentyManualNumberEnter" id="tenByTwenty6">
	                <input type="text" minlength="2" maxlength="2" class="tenByTwentyManualNumberEnter" id="tenByTwenty7">
	                <input type="text" minlength="2" maxlength="2" class="tenByTwentyManualNumberEnter" id="tenByTwenty8">
	                <input type="text" minlength="2" maxlength="2" class="tenByTwentyManualNumberEnter" id="tenByTwenty9">
	                <input type="text" minlength="2" maxlength="2" class="tenByTwentyManualNumberEnter" id="tenByTwenty10">
	            </div>
	            <div class="reset" id="resetTenByTwenty">
	            	<button>Reset</button>
	            </div>
	        </div>
	         <div class="timer-TenByTwenty timer-div" style="display: none;">
	       		 <div class="tmr"><div class="draw-fr">DRAW FREEZED</div></div>
	 	     </div>
	    </div>
	    <!-- 10/20 end -->
	    <!--1/80 game-->
	    <div class="numbers-area" id="eightytwentyten" style="display:none">
	        	<div class="col-my-10">
	           	<div class="select-no-2">
	               	<ul>
	                   	<li class="eightTwentyTenNum" id="ettnum01">01</li>
	                    <li class="eightTwentyTenNum" id="ettnum02">02</li>
	                    <li class="eightTwentyTenNum" id="ettnum03">03</li>
	                    <li class="eightTwentyTenNum" id="ettnum04">04</li>
	                    <li class="eightTwentyTenNum" id="ettnum05">05</li>
	                    <li class="eightTwentyTenNum" id="ettnum06">06</li>
	                    <li class="eightTwentyTenNum" id="ettnum07">07</li>
	                    <li class="eightTwentyTenNum" id="ettnum08">08</li>
	                    <li class="eightTwentyTenNum" id="ettnum09">09</li>
	                    <li class="eightTwentyTenNum" id="ettnum10">10</li>
	                    <li class="eightTwentyTenNum" id="ettnum11">11</li>
	                    <li class="eightTwentyTenNum" id="ettnum12">12</li>
	                    <li class="eightTwentyTenNum" id="ettnum13">13</li>
	                    <li class="eightTwentyTenNum" id="ettnum14">14</li>
	                    <li class="eightTwentyTenNum" id="ettnum15">15</li>
	                    <li class="eightTwentyTenNum" id="ettnum16">16</li>
	                    <li class="eightTwentyTenNum" id="ettnum17">17</li>
	                    <li class="eightTwentyTenNum" id="ettnum18">18</li>
	                    <li class="eightTwentyTenNum" id="ettnum19">19</li>
	                    <li class="eightTwentyTenNum" id="ettnum20">20</li>
	                    <li class="eightTwentyTenNum" id="ettnum21">21</li>
	                    <li class="eightTwentyTenNum" id="ettnum22">22</li>
	                    <li class="eightTwentyTenNum" id="ettnum23">23</li>
	                    <li class="eightTwentyTenNum" id="ettnum24">24</li>
	                    <li class="eightTwentyTenNum" id="ettnum25">25</li>
	                    <li class="eightTwentyTenNum" id="ettnum26">26</li>
	                    <li class="eightTwentyTenNum" id="ettnum27">27</li>
	                    <li class="eightTwentyTenNum" id="ettnum28">28</li>
	                    <li class="eightTwentyTenNum" id="ettnum29">29</li>
	                    <li class="eightTwentyTenNum" id="ettnum30">30</li>
	                    <li class="eightTwentyTenNum" id="ettnum31">31</li>
	                    <li class="eightTwentyTenNum" id="ettnum32">32</li>
	                    <li class="eightTwentyTenNum" id="ettnum33">33</li>
	                    <li class="eightTwentyTenNum" id="ettnum34">34</li>
	                    <li class="eightTwentyTenNum" id="ettnum35">35</li>
	                    <li class="eightTwentyTenNum" id="ettnum36">36</li>
	                    <li class="eightTwentyTenNum" id="ettnum37">37</li>
	                    <li class="eightTwentyTenNum" id="ettnum38">38</li>
	                    <li class="eightTwentyTenNum" id="ettnum39">39</li>
	                    <li class="eightTwentyTenNum" id="ettnum40">40</li>
	                    <li class="eightTwentyTenNum" id="ettnum41">41</li>
	                    <li class="eightTwentyTenNum" id="ettnum42">42</li>
	                    <li class="eightTwentyTenNum" id="ettnum43">43</li>
	                    <li class="eightTwentyTenNum" id="ettnum44">44</li>
	                    <li class="eightTwentyTenNum" id="ettnum45">45</li>
	                    <li class="eightTwentyTenNum" id="ettnum46">46</li>
	                    <li class="eightTwentyTenNum" id="ettnum47">47</li>
	                    <li class="eightTwentyTenNum" id="ettnum48">48</li>
	                    <li class="eightTwentyTenNum" id="ettnum49">49</li>
	                    <li class="eightTwentyTenNum" id="ettnum50">50</li>
	                    <li class="eightTwentyTenNum" id="ettnum51">51</li>
	                    <li class="eightTwentyTenNum" id="ettnum52">52</li>
	                    <li class="eightTwentyTenNum" id="ettnum53">53</li>
	                    <li class="eightTwentyTenNum" id="ettnum54">54</li>
	                    <li class="eightTwentyTenNum" id="ettnum55">55</li>
	                    <li class="eightTwentyTenNum" id="ettnum56">56</li>
	                    <li class="eightTwentyTenNum" id="ettnum57">57</li>
	                    <li class="eightTwentyTenNum" id="ettnum58">58</li>
	                    <li class="eightTwentyTenNum" id="ettnum59">59</li>
	                    <li class="eightTwentyTenNum" id="ettnum60">60</li>
	                    <li class="eightTwentyTenNum" id="ettnum61">61</li>
	                    <li class="eightTwentyTenNum" id="ettnum62">62</li>
	                    <li class="eightTwentyTenNum" id="ettnum63">63</li>
	                    <li class="eightTwentyTenNum" id="ettnum64">64</li>
	                    <li class="eightTwentyTenNum" id="ettnum65">65</li>
	                    <li class="eightTwentyTenNum" id="ettnum66">66</li>
	                    <li class="eightTwentyTenNum" id="ettnum67">67</li>
	                    <li class="eightTwentyTenNum" id="ettnum68">68</li>
	                    <li class="eightTwentyTenNum" id="ettnum69">69</li>
	                    <li class="eightTwentyTenNum" id="ettnum70">70</li>
	                    <li class="eightTwentyTenNum" id="ettnum71">71</li>
	                    <li class="eightTwentyTenNum" id="ettnum72">72</li>
	                    <li class="eightTwentyTenNum" id="ettnum73">73</li>
	                    <li class="eightTwentyTenNum" id="ettnum74">74</li>
	                    <li class="eightTwentyTenNum" id="ettnum75">75</li>
	                    <li class="eightTwentyTenNum" id="ettnum76">76</li>
	                    <li class="eightTwentyTenNum" id="ettnum77">77</li>
	                    <li class="eightTwentyTenNum" id="ettnum78">78</li>
	                    <li class="eightTwentyTenNum" id="ettnum79">79</li>
	                    <li class="eightTwentyTenNum" id="ettnum80">80</li>
	                   </ul>
	               </div>
	           </div>
	           <div class="col-my-2">
	           <div class="input-select-no-2">
	           	<div class="input-box-fill-2">
	               	<input type="text" class="game80ManualEnter" minlength ="2" maxlength="2" id="game-eighty-num1">
	                   <input type="text" class="game80ManualEnter" minlength ="2" maxlength="2" id="game-eighty-num2" style="display:none;">
	                   <input type="text" class="game80ManualEnter" minlength ="2" maxlength="2" id="game-eighty-num3" style="display:none;">
	                   <input type="text" class="game80ManualEnter" minlength ="2" maxlength="2" id="game-eighty-num4" style="display:none;">
	                   <input type="text" class="game80ManualEnter" minlength ="2" maxlength="2" id="game-eighty-num5" style="display:none;">
	                   <input type="text" class="game80ManualEnter" minlength ="2" maxlength="2" id="game-eighty-num6" style="display:none;">
	                   <input type="text" class="game80ManualEnter" minlength ="2" maxlength="2" id="game-eighty-num7" style="display:none;">
	                   <input type="text" class="game80ManualEnter" minlength ="2" maxlength="2" id="game-eighty-num8" style="display:none;">
	                   <input type="text" class="game80ManualEnter" minlength ="2" maxlength="2" id="game-eighty-num9" style="display:none;">
	                   <input type="text" class="game80ManualEnter" minlength ="2" maxlength="2" id="game-eighty-num10" style="display:none;">
	               </div>
	               <div class="reset-2">
	       		<button id="resetKenoSix">Reset</button>
	      		 </div>    	
	           </div>
	           
	           </div>
	            <div class="timer-KenoSix timer-div" style="display: none;">
	       		 	<div class="tmr"><div class="draw-fr">DRAW FREEZED</div></div>
	 	     	</div>
	    </div>
	    <!--1/80 game-->
	    <!--1/12 game-->
		<div class="numbers-area" id="onebytwelve" style="display:none">
		    	<div class="number-select-12">
		        	<ul>
		            	<li class="onetotwelveNum" id="ott01"><span>01</span><span>Aries</span></li>
	                    <li class="onetotwelveNum" id="ott02"><span>02</span><span>Taurus</span></li>
	                    <li class="onetotwelveNum" id="ott03"><span>03</span><span>Gemini</span></li>
	                    <li class="onetotwelveNum" id="ott04"><span>04</span><span>Cancer</span></li>
	                    <li class="onetotwelveNum" id="ott05"><span>05</span><span>Leo</span></li>
	                    <li class="onetotwelveNum" id="ott06"><span>06</span><span>Virgo</span></li>
	                    <li class="onetotwelveNum" id="ott07"><span>07</span><span>Libra</span></li>
	                    <li class="onetotwelveNum" id="ott08"><span>08</span><span>Scorpio</span></li>
	                    <li class="onetotwelveNum" id="ott09"><span>09</span><span>Sagittarius</span></li>
	                    <li class="onetotwelveNum" id="ott10"><span>10</span><span>Capricorn</span></li>
	                    <li class="onetotwelveNum" id="ott11"><span>11</span><span>Aquarius</span></li>
	                    <li class="onetotwelveNum" id="ott12"><span>12</span><span>Pisces</span></li>
		            </ul>
		        </div>
		        <div class="enter-number-12">
		        	<div class="input-area-12">
		            	<input type="text" class="ottManualEnter" minlength ="2" maxlength="2" id="ottnum1">
		            	<input type="text" class="ottManualEnter" minlength ="2" maxlength="2" id="ottnum2">
		            	<input type="text" class="ottManualEnter" minlength ="2" maxlength="2" id="ottnum3">
		            	<input type="text" class="ottManualEnter" minlength ="2" maxlength="2" id="ottnum4">
		            	<input type="text" class="ottManualEnter" minlength ="2" maxlength="2" id="ottnum5">
		            </div>
		            <div class="reset-12">
		            	<button id="resetOneByTwelve">Reset</button>
		            </div>
		        </div>
		         <div class="timer-OneToTwelve timer-div" style="display: none;">
	       			 <div class="tmr"><div class="draw-fr">DRAW FREEZED</div></div>
	 	     	 </div>
		</div>
		<!--1/12 game-->
		<!-- minirou area start -->
		<div class="minirou-areaWrapper cls" id="miniRoulette" style="display:none;">
		    <div class="minirou-area">
				
				<!--left section start -->
				<div class="minirou-left-rightWrap cls">
					<div class="minirou-area-left">
						<div class="minirou-leftCon cls">
							<div class="gridLeftBlock subPlay fl miniRouletteBetType" rouletteBettype="zero">
								<div class="rouPlayEle" >
									<div class="verticalCenter">
										<span class="predictNo">0</span>
										<div class="amtBlock">
											<span class="fa fa-usd"></span>
											<span id="rouletteAmtzero"></span>
										</div>
									</div>
								</div>
							</div>
							<div class="gridMidBlock subPlay fl">
								<div class="gridMidTop cls">
									<ul>
										<li><div class="rouPlayEle redBg sub_1-6 sub_odd sub_red sub_2-1-r1 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">3</span><div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt3"></span></div></div></li>
										<li><div class="rouPlayEle grayBg sub_1-6 sub_4-9 sub_even sub_gray sub_2-1-r1 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">6</span><div class="amtBlock"><span class="fa fa-usd"></span> <span id="rouletteAmt6"></span></div></div></li>
										<li><div class="rouPlayEle redBg sub_4-9 sub_7-12 sub_odd sub_red sub_2-1-r1 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">9</span><div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt9"></span></div></div></li>
										<li><div class="rouPlayEle grayBg sub_7-12 sub_even sub_gray sub_2-1-r1 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">12</span><div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt12"></span></div></div></li>
										<li><div class="rouPlayEle grayBg sub_1-6 sub_even sub_gray sub_2-1-r2 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">2</span><div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt2"></span></div></div></li>
										<li><div class="rouPlayEle redBg sub_1-6 sub_4-9 sub_odd sub_red sub_2-1-r2 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">5</span><div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt5"></span></div></div></li>
										<li><div class="rouPlayEle grayBg sub_4-9 sub_7-12 sub_even sub_gray sub_2-1-r2 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">8</span><div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt8"></span></div></div></li>
										<li><div class="rouPlayEle redBg sub_7-12 sub_odd sub_red sub_2-1-r2 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">11</span><div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt11"></span></div></div></li>
										<li><div class="rouPlayEle redBg sub_1-6 sub_red sub_odd sub_2-1-r3 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">1</span><div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt1"></span></div></div></li>
										<li><div class="rouPlayEle grayBg sub_1-6 sub_4-9 sub_even sub_gray sub_2-1-r3 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">4</span> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt4"></span></div></div></li>
										<li><div class="rouPlayEle redBg sub_4-9 sub_7-12 sub_odd sub_red sub_2-1-r3 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">7</span> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt7"></span></div></div></li>
										<li><div class="rouPlayEle grayBg sub_7-12 sub_even sub_gray sub_2-1-r3 miniRouletteBetType" rouletteBetType="roulette"><span class="predictNo">10</span><div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmt10"></span></div></div></li>
									</ul>
								</div>
								<div class="gridMidBottom cls">
									<ul class="predictRangeCon">
										<li><div class="rouPlayEle parent miniRouletteBetType" id="parent_1-6" rouletteBettype="oneToSix"><span  class="predictRange">1 - 6</span> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmtoneToSix"></span></div></div></li>
										<li><div class="rouPlayEle parent miniRouletteBetType" id="parent_4-9" rouletteBettype="fourToNine"><span  class="predictRange">4 - 9</span> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmtfourToNine"></span></div></div></li>
										<li><div class="rouPlayEle parent miniRouletteBetType" id="parent_7-12" rouletteBettype="sevenToTwelve"><span  class="predictRange">7 - 12</span> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmtsevenToTwelve"></span></div></div></li>
									</ul>
									<ul class="predictCategoryCon cls">
										<li><div class="rouPlayEle parent miniRouletteBetType" id="parent_even" rouletteBettype="allEvenNumbers"><span class="predictType">Even</span> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmtallEvenNumbers"></span></div></div></li>
										<li><div class="rouPlayEle parent miniRouletteBetType" id="parent_red" rouletteBettype="redNumbers"> <div class="diamondShape redBg"></div> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmtredNumbers"></span></div></div></li>
										<li><div class="rouPlayEle parent miniRouletteBetType" id="parent_gray" rouletteBettype="blackNumbers"><div class="diamondShape grayBg"></div> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmtblackNumbers"></span></div></div></li>
										<li><div class="rouPlayEle parent miniRouletteBetType" id="parent_odd" rouletteBettype="allOddNumbers"><span class="predictType">Odd</span> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmtallOddNumbers"></span></div></div></li>
									</ul>
								</div>
							</div>
							<div class="gridRightBlock subPlay fl">
								<ul>
									<li><div class="rouPlayEle parent miniRouletteBetType" id="parent_2-1-r1" rouletteBettype="firstRow"><span  class="predictRange">2 to 1</span> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmtfirstRow"></span></div> </div></li>
									<li><div class="rouPlayEle parent miniRouletteBetType" id="parent_2-1-r2" rouletteBettype="secondRow"><span  class="predictRange">2 to 1</span> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmtsecondRow"></span></div> </div></li>
									<li><div class="rouPlayEle parent miniRouletteBetType" id="parent_2-1-r3" rouletteBettype="thirdRow"><span  class="predictRange">2 to 1</span> <div class="amtBlock"><span class="fa fa-usd"></span><span id="rouletteAmtthirdRow"></span></div> </div></li>
								</ul>
							</div>		
						</div>
					</div>
					<!-- left section end -->
					<!-- right section start -->
					<div class="minirou-area-right">
						<div class="fourBlockCon">
							<div class="fourBlock subPlay miniRouletteBetType" rouletteBettype="firstQuarter">
								<ul>
									<li><span class="redBg">3</span></li>
									<li><span class="grayBg">6</span></li>
									<li><span class="grayBg">2</span></li>
									<li><span class="redBg">5</span></li>
								</ul>	
								<div class="priceBlock"><span class="fa fa-usd"></span><span id="rouletteAmtfirstQuarter"></span></div>
							</div>
							<div class="fourBlock subPlay fourBlock-mid miniRouletteBetType" rouletteBettype="thirdQuarter">
								<ul>
									<li><span class="grayBg">6</span></li>
									<li><span class="redBg">9</span></li>
									<li><span class="redBg">5</span></li>
									<li><span class="grayBg">8</span></li>
								</ul>
								<div class="priceBlock"><span class="fa fa-usd"></span><span id="rouletteAmtthirdQuarter"></span></div>
							</div>
							<div class="fourBlock subPlay miniRouletteBetType" rouletteBettype="sixthQuarter">
								<ul>
									<li><span class="redBg">9</span></li>
									<li><span class="grayBg">12</span></li>
									<li><span class="grayBg">8</span></li>
									<li><span class="redBg">11</span></li>
								</ul>
								<div class="priceBlock"><span class="fa fa-usd"></span><span id="rouletteAmtsixthQuarter"></span></div>
							</div>
							<div class="fourBlock subPlay miniRouletteBetType" rouletteBettype="secondQuarter">
								<ul>
									<li><span class="grayBg">2</span></li>
									<li><span class="redBg">5</span></li>
									<li><span class="redBg">1</span></li>
									<li><span class="grayBg">4</span></li>
								</ul>
								<div class="priceBlock"><span class="fa fa-usd"></span><span id="rouletteAmtsecondQuarter"></span></div>
							</div>
							<div class="fourBlock subPlay fourBlock-mid miniRouletteBetType" rouletteBettype="fourthQuarter">
								<ul>
									<li><span class="redBg">5</span></li>
									<li><span class="grayBg">8</span></li>
									<li><span class="grayBg">4</span></li>
									<li><span class="redBg">7</span></li>
								</ul>
								<div class="priceBlock"><span class="fa fa-usd"></span><span id="rouletteAmtfourthQuarter"></span></div>
							</div>
							<div class="fourBlock subPlay miniRouletteBetType" rouletteBettype="fifthQuarter">
								<ul>
									<li><span class="grayBg">8</span></li>
									<li><span class="redBg">11</span></li>
									<li><span class="redBg">7</span></li>
									<li><span class="grayBg">10</span></li>
								</ul>
								<div class="priceBlock"><span class="fa fa-usd"></span><span id="rouletteAmtfifthQuarter"></span></div>
							</div>
						</div>
						<!-- right bottom section start -->
						<div class="rightBottomSection">
							<div class="fourBlock subPlay miniRouletteBetType" rouletteBettype="firstColumn">
								<ul>
									<li><span class="redBg">3</span></li>
									<li><span class="grayBg">2</span></li>
									<li><span class="redBg">1</span></li>
								</ul>
								<div class="priceBlock"><span class="fa fa-usd"></span><span id="rouletteAmtfirstColumn"></span></div>
							</div>
							<div class="fourBlock subPlay miniRouletteBetType" rouletteBettype="secondColumn">
								<ul>
									<li><span class="grayBg">6</span></li>
									<li><span class="redBg">5</span></li>
									<li><span class="grayBg">4</span></li>
								</ul>
								<div class="priceBlock"><span class="fa fa-usd"></span><span id="rouletteAmtsecondColumn"></span></div>
							</div>
							<div class="fourBlock subPlay miniRouletteBetType" rouletteBettype="thirdColumn">
								<ul>
									<li><span class="redBg">9</span></li>
									<li><span class="grayBg">8</span></li>
									<li><span class="redBg">7</span></li>
								</ul>
								<div class="priceBlock"><span class="fa fa-usd"></span><span id="rouletteAmtthirdColumn">10</span></div>
							</div>
							<div class="fourBlock subPlay miniRouletteBetType" rouletteBettype="fourthColumn">
								<ul>
									<li><span class="grayBg">12</span></li>
									<li><span class="redBg">11</span></li>
									<li><span class="grayBg">10</span></li>
								</ul>
								<div class="priceBlock"><span class="fa fa-usd"></span><span id="rouletteAmtfourthColumn"></span></div>
							</div>
						</div>
						<!-- right bottom section end -->
					</div>
				</div>
				<!--right section end -->
				
				
				<!--chips section start -->
				<div class="minirou-chipsWrap cls">
					<div class="minirou-chipsList fl">
						<ul>
							<li><a id="clearCursor" class="clearCursor betCoins" href="#" betCoinAmt = "0">
								<div class="figure">
									<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips1.png" alt="clear amount" />
									<span class="chipShadow">
										<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
									</span>
								</div>
								</a>
							</li>
							<li><a id="tenCursor" class="tenCursor betCoins" href="#" betCoinAmt = "0.5">
								<div class="figure">
									<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips2.png" alt="chip 0.5" />
									<span class="chipShadow">
										<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
									</span>
								</div>
								</a>
							</li>
							<li><a id="twentyCursor" class="twentyCursor betCoins" href="#" betCoinAmt = "1">
								<div class="figure">
									<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips3.png" alt="chip 1" />
									<span class="chipShadow">
										<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
									</span>
								</div>
							</a>
							</li>
							<li><a id="fiftyCursor" class="fiftyCursor betCoins"  href="#" betCoinAmt = "1.5">
								<div class="figure">
									<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips4.png" alt="chip 1.5" />
									<span class="chipShadow">
										<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
									</span>
								</div>
							</a>
							</li>
							<li><a id="hundredCursor" class="hundredCursor betCoins"  href="#" betCoinAmt = "2">
								<div class="figure">
									<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips5.png" alt="chip 2" />
									<span class="chipShadow">
										<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
									</span>
								</div>
							</a>
							</li>
							<li><a id="FiveHundredCursor" class="FiveHundredCursor betCoins" href="#" betCoinAmt = "5">
								<div class="figure">
									<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips6.png" alt="chip 5" />
									<span class="chipShadow">
										<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
									</span>
								</div>
							</a>
							</li>
						</ul>
					</div>
					
					<div class="green-btn fr">
						<button type="reset" id="resetMiniRoulette">Reset</button>
					</div>
				</div>
				<!--chips section ends -->
		    </div>
		    	<div class="timer-MiniRoulette timer-div" style="display: none;">
	       			 <div class="tmr"><div class="draw-fr">DRAW FREEZED</div></div>
	 	     	</div>
		</div>
	    <!-- minirou area start end-->   
	    <!-- Keno Two Starts -->
		<div class="lnum-game" style="display:none;" id="kenoTwo">
	    	<div class="lnum-no-select">
	        	<ul class="lnum-fortytwo">
		            <li class="kenoTwoNumber" id="kenoTwoNum01"><span>01</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum02"><span>02</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum03"><span>03</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum04"><span>04</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum05"><span>05</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum06"><span>06</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum07"><span>07</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum08"><span>08</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum09"><span>09</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum10"><span>10</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum11"><span>11</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum12"><span>12</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum13"><span>13</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum14"><span>14</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum15"><span>15</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum16"><span>16</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum17"><span>17</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum18"><span>18</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum19"><span>19</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum20"><span>20</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum21"><span>21</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum22"><span>22</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum23"><span>23</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum24"><span>24</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum25"><span>25</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum26"><span>26</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum27"><span>27</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum28"><span>28</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum29"><span>29</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum30"><span>30</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum31"><span>31</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum32"><span>32</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum33"><span>33</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum34"><span>34</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum35"><span>35</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum36"><span>36</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum37"><span>37</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum38"><span>38</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum39"><span>39</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum40"><span>40</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum41"><span>41</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum42"><span>42</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum43"><span>43</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum44"><span>44</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum45"><span>45</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum46"><span>46</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum47"><span>47</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum48"><span>48</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum49"><span>49</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum50"><span>50</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum51"><span>51</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum52"><span>52</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum53"><span>53</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum54"><span>54</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum55"><span>55</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum56"><span>56</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum57"><span>57</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum58"><span>58</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum59"><span>59</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum60"><span>60</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum61"><span>61</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum62"><span>62</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum63"><span>63</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum64"><span>64</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum65"><span>65</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum66"><span>66</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum67"><span>67</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum68"><span>68</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum69"><span>69</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum70"><span>70</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum71"><span>71</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum72"><span>72</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum73"><span>73</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum74"><span>74</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum75"><span>75</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum76"><span>76</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum77"><span>77</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum78"><span>78</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum79"><span>79</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum80"><span>80</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum81"><span>81</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum82"><span>82</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum83"><span>83</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum84"><span>84</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum85"><span>85</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum86"><span>86</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum87"><span>87</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum88"><span>88</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum89"><span>89</span></li>
		            <li class="kenoTwoNumber" id="kenoTwoNum90"><span>90</span></li>
		         </ul>
	    </div>
        <div class="lnum-no-area">
        	<div class="lnum-input-area">
	            	<input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum1">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum2">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum3">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum4">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum5">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum6">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum7">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum8">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum9">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum10">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum11" style="display:none;">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum12" style="display:none;">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum13" style="display:none;">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum14" style="display:none;">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum15" style="display:none;">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum16" style="display:none;">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum17" style="display:none;">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum18" style="display:none;">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum19" style="display:none;">
	                <input type="text" minlength="2" maxlength="2" class="kenoTwoNumberEnter" id="ktnum20" style="display:none;">
	            </div>
	            <div class="ln-reset" id="resetKenoTwo">
	            	<button>Reset</button>
	            </div>
	        </div>
	         <div class="timer-KenoTwo timer-div" style="display: none;">
	       		 <div class="tmr"><div class="draw-fr">DRAW FREEZED</div></div>
	 	     </div>
	    </div>
			    <!-- Keno Two Ends -->
	       <!-- Zim Bonus Lotto Starts -->
		    	
		<div class="sixby-game" id="zimLottoBonus" style="display:none">
    		<div class="sixby-no-select">
        		<ul class="sixby-fortytwo">
		            <li class="bonusLottoNumber" id="bonusLottoNum01"><span>01</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum02"><span>02</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum03"><span>03</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum04"><span>04</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum05"><span>05</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum06"><span>06</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum07"><span>07</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum08"><span>08</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum09"><span>09</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum10"><span>10</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum11"><span>11</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum12"><span>12</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum13"><span>13</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum14"><span>14</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum15"><span>15</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum16"><span>16</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum17"><span>17</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum18"><span>18</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum19"><span>19</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum20"><span>20</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum21"><span>21</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum22"><span>22</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum23"><span>23</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum24"><span>24</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum25"><span>25</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum26"><span>26</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum27"><span>27</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum28"><span>28</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum29"><span>29</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum30"><span>30</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum31"><span>31</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum32"><span>32</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum33"><span>33</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum34"><span>34</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum35"><span>35</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum36"><span>36</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum37"><span>37</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum38"><span>38</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum39"><span>39</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum40"><span>40</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum41"><span>41</span></li>
		            <li class="bonusLottoNumber" id="bonusLottoNum42"><span>42</span></li>
		            
		        </ul>
	        </div>
	        <div class="enter-no-area">
	        	<!-- <span class="error-msg" id="error"></span> -->
	       
	        	<div id="directSix" style="display: none;">
	        	   <div class="nu-input-area active" id="line1">
	        			<div class="radio-click"><input type="radio" class="line" name="radio" id="radio1" lineNum = "1"/></div>
	        			<div id="manualInputLine1">
			            	<input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum1" id="zbl1" lineNum = "1">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum1" id="zbl2" lineNum = "1">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum1" id="zbl3" lineNum = "1">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum1" id="zbl4" lineNum = "1">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum1" id="zbl5" lineNum = "1">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum1" id="zbl6" lineNum = "1">
		                </div>
	                </div>
	                <div class="nu-input-area" id="line2">
	        			<div class="radio-click"><input type="radio" class="line" name="radio" id="radio2" lineNum = "2"/></div>
	        			<div id="manualInputLine2">
			            	<input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum2" id="zbl7" lineNum = "2">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum2" id="zbl8" lineNum = "2">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum2" id="zbl9" lineNum = "2">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum2" id="zbl10" lineNum = "2">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum2" id="zbl11" lineNum = "2">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum2" id="zbl12" lineNum = "2">
		                </div>
	                </div>
	                <div class="nu-input-area" id="line3">
	        			<div class="radio-click"><input type="radio" class="line" name="radio" id="radio3" lineNum = "3"/></div>
	        			<div id="manualInputLine3">
			            	<input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum3" id="zbl13" lineNum = "3">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum3" id="zbl14" lineNum = "3">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum3" id="zbl15" lineNum = "3">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum3" id="zbl16" lineNum = "3">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum3" id="zbl17" lineNum = "3">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum3" id="zbl18" lineNum = "3">
		                </div>
	                </div>
	                <div class="nu-input-area" id="line4">
	        			<div class="radio-click"><input type="radio" class="line" name="radio" id="radio4" lineNum = "4"/></div>
	        			<div id="manualInputLine4">
			            	<input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum4" id="zbl19" lineNum = "4">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum4" id="zbl20" lineNum = "4">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum4" id="zbl21" lineNum = "4">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum4" id="zbl22" lineNum = "4">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum4" id="zbl23" lineNum = "4">
			                <input type="text" minlength="2" maxlength="2" class="manualNumberZimBonusLotto lineNum4" id="zbl24" lineNum = "4">
		                </div>
	                </div>
	            </div>
	
	            
	            <div id="permSix" class="sixby-game_input" style="display: none;">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix1">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix2">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix3">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix4">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix5">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix6">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix7">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix8">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix9">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix10">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix11">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix12">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix13">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix14">
	            	<input type="text" minlength="2" maxlength="2" class="zimBonusLottoPermSix" id="permSix15">    	
	            </div>
	            
	             <div class="sixby-reset" id="resetZimLottoBonus">
	            	<button>Reset</button>
	            </div>
	        </div>
	         <div class="timer-ZimLottoBonus timer-div" style="display: none;">
	       		 <div class="tmr"><div class="draw-fr">DRAW FREEZED</div></div>
	 	     </div>
	    </div>
	    <!-- Zim Bonus Lotto Ends -->
	    <!-- fullrou area start -->
		<div class="full-minirou-areaWrapper cls" id="fullRoulette" style="display: none;">
	  	  <div class="full-minirou-area">
			
			<!--left section start -->
			<div class="minirou-left-rightWrap cls">
			<div class="full-minirou-area-left">
					<div class="minirou-leftCon cls">
					
								<div class="full-gridLeftBlock subPlay fl">
									<div class="rouPlayEle fullRouletteBetType" fullRouletteBetType="zero">
									<div class="full-verticalCenter">
										<span class="predictNo">0</span>
										<div class="full-amtBlock">
											<span class="fa fa-usd"></span>
											<span id="fullRouletteAmtzero"></span>
										</div>
									</div>
									</div>
								</div>
								
								<div class="full-gridMidBlock subPlay fl">
								
									<div class="full-gridMidOuterTop">
										<ul>
											<li class="firstRowRoullete"><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="firstRow" id="parent_1-3"> <span class="">1 - 3</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfirstRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="secondRow" id="parent_4-6"> <span class="">4 - 6</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtsecondRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="thirdRow" id="parent_7-9"> <span class="">7 - 9</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtthirdRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="fourthRow" id="parent_10-12"> <span class="">10 - 12</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfourthRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="fifthRow" id="parent_13-15"> <span class="">13 - 15</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfifthRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="sixthRow" id="parent_16-18"> <span class="">16 - 18</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtsixthRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="seventhRow" id="parent_19-21"> <span class="">19 - 21</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtseventhRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="eighthRow" id="parent_22-24"> <span class="">22 - 24</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmteighthRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="ninthRow" id="parent_25-27"> <span class="">25 - 27</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtninthRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="tenthRow" id="parent_28-30"> <span class="">28 - 30</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmttenthRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="eleventhRow" id="parent_31-33"> <span class="">31 - 33</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmteleventhRow"></span></div> </div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" fullRouletteBetType="twelfthRow" id="parent_34-36"> <span class="">34 - 36</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmttwelfthRow"></span></div> </div></li>	
										</ul>
									</div>
								
								
									<div class="full-gridMidTop cls">
									<ul>
										<li><div class="rouPlayEle redBg sub_red  sub_1-12 sub_1-3 sub_1-18 sub_odd  sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">3</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt3"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray sub_1-12 sub_1-18  sub_even sub_4-6  sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">6</span>  <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt6"></span></div>  </div></li>
										<li><div class="rouPlayEle redBg sub_red sub_1-12 sub_1-18   sub_odd sub_7-9  sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">9</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt9"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray sub_1-12 sub_1-18  sub_even sub_10-12  sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">12</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt12"></span></div> </div></li>
										<li><div class="rouPlayEle redBg sub_red sub_2-12 sub_1-18  sub_odd  sub_13-15  sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">15</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt15"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray  sub_2-12  sub_1-18 sub_even  sub_16-18 sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">18</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt18"></span></div> </div></li>
										<li><div class="rouPlayEle redBg sub_red  sub_2-12 sub_19-36  sub_odd   sub_19-21  sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">21</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt21"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray  sub_2-12 sub_19-36 sub_even sub_22-24 sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">24</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt24"></span></div> </div></li>
										<li><div class="rouPlayEle redBg sub_red   sub_odd sub_3-12 sub_19-36 sub_25-27 sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">27</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt27"></span></div></div></li>
										<li><div class="rouPlayEle grayBg sub_gray  sub_3-12 sub_19-36  sub_even  sub_28-30 sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">30</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt30"></span></div></div></li>
										<li><div class="rouPlayEle redBg sub_red   sub_odd  sub_3-12 sub_19-36 sub_31-33 sub_2-1-r1 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">33</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt33"></span></div></div></li>
										<li><div class="rouPlayEle grayBg sub_gray  sub_even  sub_3-12 sub_19-36 sub_2-1-r1 sub_34-36 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">36</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt36"></span></div> </div></li>
										
										
										<li><div class="rouPlayEle grayBg sub_gray sub_1-12 sub_1-3 sub_1-18 sub_even  sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">2</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt2"></span></div> </div></li>
										<li><div class="rouPlayEle redBg  sub_red sub_1-12 sub_1-18 sub_odd sub_4-6   sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">5</span>  <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt5"></span></div>  </div></li>
										<li><div class="rouPlayEle grayBg sub_gray sub_1-12 sub_1-18   sub_even sub_7-9  sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">8</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt8"></span></div> </div></li>
										<li><div class="rouPlayEle redBg sub_red sub_1-12 sub_1-18 sub_odd sub_10-12  sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">11</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt11"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray  sub_2-12 sub_1-18 sub_even sub_13-15  sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">14</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt14"></span></div> </div></li>
										<li><div class="rouPlayEle redBg sub_red sub_2-12 sub_1-18  sub_odd sub_16-18  sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">17</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt17"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray  sub_2-12 sub_19-36  sub_even sub_19-21  sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">20</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt20"></span></div> </div></li>
										<li><div class="rouPlayEle redBg  sub_red  sub_2-12  sub_19-36 sub_odd sub_22-24  sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">23</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt23"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray    sub_19-36 sub_even sub_3-12 sub_25-27 sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">26</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt26"></span></div></div></li>
										<li><div class="rouPlayEle redBg sub_red   sub_3-12 sub_19-36 sub_odd sub_28-30  sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">29</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt29"></span></div></div></li>
										<li><div class="rouPlayEle grayBg sub_gray   sub_even  sub_19-36 sub_3-12 sub_31-33 sub_2-1-r2 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">32</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt32"></span></div></div></li>
										<li><div class="rouPlayEle redBg  sub_red   sub_3-12 sub_odd  sub_19-36 sub_2-1-r2 sub_34-36 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">35</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt35"></span></div> </div></li>
										
										
										<li><div class="rouPlayEle redBg sub_red  sub_1-12 sub_1-3 sub_1-18  sub_odd  sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">1</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt1"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray sub_1-12 sub_1-18 sub_4-6  sub_even  sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">4</span>  <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt4"></span></div>  </div></li>
										<li><div class="rouPlayEle redBg sub_red sub_1-12 sub_1-18   sub_odd sub_7-9  sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">7</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt7"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray sub_1-12 sub_1-18  sub_even sub_10-12  sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">10</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt10"></span></div> </div></li>
										<li><div class="rouPlayEle redBg sub_red   sub_2-12 sub_1-18  sub_odd sub_13-15   sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">13</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt13"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray  sub_2-12 sub_1-18  sub_even sub_16-18  sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">16</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt16"></span></div> </div></li>
										<li><div class="rouPlayEle redBg  sub_red sub_2-12 sub_19-36 sub_odd  sub_19-21 sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">19</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt19"></span></div> </div></li>
										<li><div class="rouPlayEle grayBg sub_gray  sub_2-12 sub_19-36 sub_even sub_22-24  sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">22</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt22"></span></div> </div></li>
										<li><div class="rouPlayEle redBg sub_red   sub_19-36 sub_odd sub_3-12 sub_25-27 sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">25</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt25"></span></div></div></li>
										<li><div class="rouPlayEle grayBg sub_gray   sub_3-12 sub_19-36 sub_even sub_28-30  sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">28</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt28"></span></div></div></li>
										<li><div class="rouPlayEle redBg  sub_red  sub_19-36 sub_odd  sub_3-12 sub_31-33 sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">31</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt31"></span></div></div></li>
										<li><div class="rouPlayEle grayBg  sub_gray sub_even  sub_3-12 sub_19-36  sub_34-36 sub_2-1-r3 fullRouletteBetType" fullRouletteBetType="roulette"> <span class="predictNo">34</span><div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmt34"></span></div> </div></li>
										
										
										
									</ul>
									</div>
									
									<div class="full-gridMidBottom cls">
										<ul class="full-predictRangeCon">
											<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_1-12 fullRouletteBetType" fullRouletteBetType="firstDozen"><span  class="predictRange">1<sup>st</sup>  12</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfirstDozen"></span></div></div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_2-12 fullRouletteBetType" fullRouletteBetType="secondDozen"><span  class="predictRange">2<sup>nd</sup>  12</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtsecondDozen"></span></div></div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_3-12 fullRouletteBetType" fullRouletteBetType="thirdDozen"><span  class="predictRange">3<sup>rd</sup>  12</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtthirdDozen"></span></div></div></li>
										</ul>
									
										<ul class="full-predictCategoryCon cls">
											<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_1-18 " fullRouletteBetType="firstHalf"><span  class="predictType">1 to 18</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfirstHalf"></span></div></div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_even " fullRouletteBetType="allEvenNumbers"><span  class="predictType">Even</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtallEvenNumbers"></span></div></div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_red " fullRouletteBetType="redNumbers"> <div  class="full-diamondShape redBg"></div> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtredNumbers"></span></div></div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_gray " fullRouletteBetType="blackNumbers"><div  class="full-diamondShape grayBg"></div> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtblackNumbers"></span></div></div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_odd " fullRouletteBetType="allOddNumbers"><span class="predictType">Odd</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtallOddNumbers"></span></div></div></li>
											<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_19-36 " fullRouletteBetType="secondHalf"><span class="predictType">19 to 36</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtsecondHalf"></span></div></div></li>
										</ul>
										
									</div>
								</div>
								
								<div class="full-gridRightBlock subPlay fl">
									<ul>
										<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_2-1-r1 " fullRouletteBetType="thirdColumn"><span  class="predictRange">2 to 1</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtthirdColumn"></span></div> </div></li>
										<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_2-1-r2 " fullRouletteBetType="secondColumn"><span  class="predictRange">2 to 1</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtsecondColumn"></span></div> </div></li>
										<li><div class="rouPlayEle parent fullRouletteBetType" id="parent_2-1-r3 " fullRouletteBetType="firstColumn"><span  class="predictRange">2 to 1</span> <div class="full-amtBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfirstColumn"></span></div> </div></li>
									</ul>
								</div>
								
								
					</div>
			</div>
			<!--left section end -->
			
			
			<!--right section start -->
			<div class="full-minirou-area-right">
					
				<div class="full-minirou-chipsList cls">
				<ul>
					<li><a id="clearCursor" class="clearCursor betCoinsFR" href="#" betCoinAmt = "0">
						<div class="figure">
							<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips1.png" alt="clear amount" />
							<span class="chipShadow">
								<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
							</span>
						</div>
						</a>
					</li>
					
					<li><a id="tenCursor" class="tenCursor betCoinsFR" href="#" betCoinAmt = "0.5">
						<div class="figure">
							<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips2.png" alt="chip 0.5" />
							<span class="chipShadow">
								<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
							</span>
						</div>
						</a>
					</li>
					
					<li><a id="twentyCursor" class="twentyCursor betCoinsFR" href="#" betCoinAmt = "1">
						<div class="figure">
							<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips3.png" alt="chip 1" />
							<span class="chipShadow">
								<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
							</span>
						</div>
					</a>
					</li>
					
					<li><a id="fiftyCursor" class="fiftyCursor betCoinsFR"  href="#" betCoinAmt = "1.5">
						<div class="figure">
							<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips4.png" alt="chip 1.5" />
							<span class="chipShadow">
								<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
							</span>
						</div>
					</a>
					</li>
					
					<li><a id="hundredCursor" class="hundredCursor betCoinsFR"  href="#" betCoinAmt = "2">
						<div class="figure">
							<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips5.png" alt="chip 2" />
							<span class="chipShadow">
								<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
							</span>
						</div>
					</a>
					</li>
					
					
					<li><a id="FiveHundredCursor" class="FiveHundredCursor betCoinsFR" href="#" betCoinAmt = "5">
						<div class="figure">
							<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/chips6.png" alt="chip 5" />
							<span class="chipShadow">
								<img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/shadow.png" alt="shadow" />
							</span>
						</div>
					</a>
					</li>
				</ul>
				</div>
				
				<div class="reset-2 green-btn" id="resetFullRoulette">
					<button type="reset">Reset</button>
				</div>	
			</div>
			
			
			</div>
			<!--right section end -->
					
			
			<!-- fullRoulette-bottomArea block start -->
			<div class="fullRoulette-bottomArea">
				<!-- row1 start -->
				<div class="fourBlockCon cls">
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="firstQuarter">
					<ul>
						<li><span class="redBg">3</span></li>
						<li><span class="grayBg">6</span></li>
						<li><span class="grayBg">2</span></li>
						<li><span class="redBg">5</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfirstQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="secondQuarter">
					<ul>
						<li><span class="grayBg">6</span></li>
						<li><span class="redBg">9</span></li>
						<li><span class="redBg">5</span></li>
						<li><span class="grayBg">8</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtsecondQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="thirdQuarter">
					<ul>
						<li><span class="redBg">9</span></li>
						<li><span class="grayBg">12</span></li>
						<li><span class="grayBg">8</span></li>
						<li><span class="redBg">11</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtthirdQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="fourthQuarter">
					<ul>
						<li><span class="grayBg">12</span></li>
						<li><span class="redBg">15</span></li>
						<li><span class="redBg">11</span></li>
						<li><span class="grayBg">14</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfourthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="fifthQuarter">
					<ul>
						<li><span class="redBg">15</span></li>
						<li><span class="grayBg">18</span></li>
						<li><span class="grayBg">14</span></li>
						<li><span class="redBg">17</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfifthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="sixthQuarter">
					<ul>
						<li><span class="grayBg">18</span></li>
						<li><span class="redBg">21</span></li>
						<li><span class="redBg">17</span></li>
						<li><span class="grayBg">20</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtsixthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="seventhQuarter">
					<ul>
						<li><span class="redBg">21</span></li>
						<li><span class="grayBg">24</span></li>
						<li><span class="grayBg">20</span></li>
						<li><span class="redBg">23</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtseventhQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="eighthQuarter">
					<ul>
						<li><span class="grayBg">24</span></li>
						<li><span class="redBg">27</span></li>
						<li><span class="redBg">23</span></li>
						<li><span class="grayBg">26</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmteighthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="ninthQuarter">
					<ul>
						<li><span class="redBg">27</span></li>
						<li><span class="grayBg">30</span></li>
						<li><span class="grayBg">26</span></li>
						<li><span class="redBg">29</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtninthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="tenthQuarter">
					<ul>
						<li><span class="grayBg">30</span></li>
						<li><span class="redBg">33</span></li>
						<li><span class="redBg">29</span></li>
						<li><span class="grayBg">32</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmttenthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="eleventhQuarter">
					<ul>
						<li><span class="redBg">33</span></li>
						<li><span class="grayBg">36</span></li>
						<li><span class="grayBg">32</span></li>
						<li><span class="redBg">35</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmteleventhQuarter"></span></div>
				</div>
				</div>
				<!-- row1 end -->
				
				<!-- row2 start -->
				<div class="fourBlockCon cls">
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="twelfthQuarter">
					<ul>
						<li><span class="grayBg">2</span></li>
						<li><span class="redBg">5</span></li>
						<li><span class="redBg">1</span></li>
						<li><span class="grayBg">4</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmttwelfthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="thirteenthQuarter">
					<ul>
						<li><span class="redBg">5</span></li>
						<li><span class="grayBg">8</span></li>
						<li><span class="grayBg">4</span></li>
						<li><span class="redBg">7</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtthirteenthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="fourteenthQuarter">
					<ul>
						<li><span class="grayBg">8</span></li>
						<li><span class="redBg">11</span></li>
						<li><span class="redBg">7</span></li>
						<li><span class="grayBg">10</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfourteenthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="fifteenthQuarter">
					<ul>
						<li><span class="redBg">11</span></li>
						<li><span class="grayBg">14</span></li>
						<li><span class="grayBg">10</span></li>
						<li><span class="redBg">13</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtfifteenthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="sixteenthQuarter">
					<ul>
						<li><span class="grayBg">14</span></li>
						<li><span class="redBg">17</span></li>
						<li><span class="redBg">13</span></li>
						<li><span class="grayBg">16</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtsixteenthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="seventeenthQuarter">
					<ul>
						<li><span class="redBg">17</span></li>
						<li><span class="grayBg">20</span></li>
						<li><span class="grayBg">16</span></li>
						<li><span class="redBg">19</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtseventeenthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="eighteenthQuarter">
					<ul>
						<li><span class="grayBg">20</span></li>
						<li><span class="redBg">23</span></li>
						<li><span class="redBg">19</span></li>
						<li><span class="grayBg">22</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmteighteenthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="ninteenthQuarter">
					<ul>
						<li><span class="redBg">23</span></li>
						<li><span class="grayBg">26</span></li>
						<li><span class="grayBg">22</span></li>
						<li><span class="redBg">25</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmtninteenthQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="twentiethQuarter">
					<ul>
						<li><span class="grayBg">26</span></li>
						<li><span class="redBg">29</span></li>
						<li><span class="redBg">25</span></li>
						<li><span class="grayBg">28</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmttwentiethQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="twentyFirstQuarter">
					<ul>
						<li><span class="redBg">29</span></li>
						<li><span class="grayBg">32</span></li>
						<li><span class="grayBg">28</span></li>
						<li><span class="redBg">31</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmttwentyFirstQuarter"></span></div>
				</div>
				
				<div class="full-fourBlock subPlay fullRouletteBetType" fullRouletteBettype="twentySecondQuarter">
					<ul>
						<li><span class="grayBg">32</span></li>
						<li><span class="redBg">35</span></li>
						<li><span class="redBg">31</span></li>
						<li><span class="grayBg">34</span></li>
					</ul>
					<div class="priceBlock"><span class="fa fa-usd"></span> <span id="fullRouletteAmttwentySecondQuarter"></span></div>
				</div>
				</div>
				<!-- row2 end -->	
			</div>
			<!-- fullRoulette-bottomArea block end -->
	    </div>
	    <div class="timer-FullRoulette timer-div" style="display: none;">
	       		 <div class="tmr"><div class="draw-fr">DRAW FREEZED</div></div>
	 	 </div>
		</div>
	    <!-- fullrou area start end-->
		
	    <!-- Error PopUp Start -->
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
	    
	    <div class="popup1 systemMSG1 success" id="success-popup" style="display: none;">
	    	<h5>Success<button id="err-popup-button"></button></h5>
	          <div class="modal-bodyWrap">
	             <div class="modal-body">
	                <div class="row">
	                   <div class="col-xs-9 msgBox" id="success"></div>
	                </div>                  
	             </div>
	          </div>
	    </div>
	    
	</div>
	<!--middle side-->
	<!--right side-->
	<div class="right-col">
	<div class="win-set-button">
		<div class="winning-button" id="pwt">
		    <button>
			    <div class="figure"><img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/winning-claim.png" width="35"></div>
			    <span>Winning Claim</span>
		    </button>
		</div>
		
		
		<div class="setting-button" id="settings">
		    <button id="setting-btn">
			    <div class="figure"><img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/setting-icon.png" width="35" class="set-skm">
			    <img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/setting-icon-zim.png" width="35" class="set-zim">
			    </div>
			    <span>Setting</span>
		    </button>
		</div>
		<div class="down-menu" id="open-list-btn" style="display:none;">
			<button id="reprint">
				<span><img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/reprint-icon.png" class="img-responsive" >Reprint</span>
			</button>
			<button id="results">
				<span><img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/result-icon.png" class="img-responsive" >Results</span>
			</button>
			<button id="cancel">
				<span><img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/cancel-icon.png" class="img-responsive" >Cancel</span>
			</button>
			<button id="reports">
				<span><img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/reports-icon.png" class="img-responsive" >Reports</span>
			</button>
			<button id="testPrint">
					<span><img src="<%=request.getContextPath()%>/com/skilrock/lms/web/drawGames/common/pcposImg/test-print-icon.png" class="img-responsive" >Test Print</span>
			</button>		
			
		</div>
	</div>
	<div class="prize" id="tktView" style="display: none;">
		<h5>Ticket View</h5>
		<div class="ticket-view" >
			<div id="tktGen"></div>
		</div>
    </div>
	      <table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr>
				<td>
					<div id="parentApplet" style="overflow: auto; height: 198px; background-color: white; display: none">
							<%
							String barcodeType = (String)application.getAttribute("BARCODE_TYPE");
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
									<applet code="TicketApplet" codebase="<%=codebaseBuffer.toString()%>" jnlp_href="applets/App.jnlp" width="210" style="height:900px;position: relative;" name="TicketApp" mayscript>
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
								<applet codebase="<%=request.getContextPath()%>/java1.5/" code="PCPOSAppletTicketEngine.class"  width="200" height="500" name="PCPOSAppletTicketEngine" mayscript>
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
	<div class="prize" id="gamePrizeScheme">
    	<h5>Prize Scheme</h5>
        <div class="list-prize">
        	<div id="miniRoulettePrizeScheme" style="display:none;"></div>
        	<div id="otherGamePrizeScheme" style="display:none;">
        	<table class="table-prize-new" width="100%" cellpadding="0" cellspacing="0">
            	<thead>
                	<tr>
                	<th>Match</th>
                    <th>Prize/Jackpot (USD)</th>
                    </tr>
                </thead>
                <tbody id="prizeScheme"></tbody>
            </table>
            </div>
        </div>
    </div>
    <div class="purchase-detail" >
    	<h5>Purchase Details</h5>
        <div class="draw-dt" >
        	<div class="draw-list">
            	<div class="dr-letf">Draw :</div>
                <div class="dr-right" id="drawName"></div>
            </div>
            <div id="purchaseDetails" style="display:none;">
	            <div class="draw-list">
	            	<div class="bt-left">
	                    <div class="h-bt">Bet Type :</div>
	                    <div class="h-dr" id="betTypeSel"></div>
	                </div>
	                <div class="bt-right">
	                    <div class="h-bt">No. of Lines :</div>
	                    <div class="h-dr" id="noOfLines">0</div>
	                </div>
	            </div>
	            <div class="num">
	            	<div class="h-bt">No. :</div>
	                <div class="h-dr" id="numPicked"></div>
	            </div>
	            <div style="display:none;">
	                <div class="h-dr" id="noOfNumber">0</div>
	            </div>
            </div>
        </div>
 <div class="draw-dt" id="roulettePurchaseDetails" style="display:none;margin-top:1px;">
	        <table class="roulette-t" cellspacing="0" cellpadding="0" width="100%" >
				<tr class="hd">
					<td class="greenbg" width="20%">0</td>
					<td class="redBg" width="20%">1</td>
					<td class="grayBg" width="20%">2</td>
					<td class="redBg" width="20%">3</td>
					
				</tr>
				<tr class="getamt">
					
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr0">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr1">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr2">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr3">0</span></td>
				</tr>
				<tr class="hd">
					<td class="grayBg" width="20%">4</td>
					<td class="redBg">5</td>
					<td class="grayBg">6</td>
					<td class="redBg">7</td>
					
				</tr>
				<tr class="getamt">
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr4">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr5">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr6">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr7">0</span></td>
					
				</tr>
				<tr class="hd">
					<td class="grayBg">8</td>
					<td class="redBg">9</td>
					<td class="grayBg">10</td>
					<td class="redBg">11</td>
					
				</tr>
				<tr class="getamt">
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr8">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr9">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr10">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr11">0</span></td>
				</tr>
				<tr class="hd">
					<td class="grayBg" >12</td>
				</tr>
				<tr class="getamt">
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="mr12">0</span></td>
				</tr>
			</table>	
	        </div>
	        <div class="draw-dt" id="fullRoulettePurchaseDetails" style="display:none;margin-top:1px;">
	        <table class="roulette-t" cellspacing="0" cellpadding="0" width="100%" >
				<tr class="hd">
					<td class="greenbg" width="20%">0</td>
					<td class="redBg" width="20%">1</td>
					<td class="grayBg" width="20%">2</td>
					<td class="redBg" width="20%">3</td>
					
				</tr>
				<tr class="getamt">
					
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr0">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr1">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr2">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr3">0</span></td>
				</tr>
				<tr class="hd">
					<td class="grayBg" width="20%">4</td>
					<td class="redBg">5</td>
					<td class="grayBg">6</td>
					<td class="redBg">7</td>
					
				</tr>
				<tr class="getamt">
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr4">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr5">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr6">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr7">0</span></td>
					
				</tr>
				<tr class="hd">
					<td class="grayBg">8</td>
					<td class="redBg">9</td>
					<td class="grayBg">10</td>
					<td class="redBg">11</td>
					
				</tr>
				<tr class="getamt">
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr8">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr9">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr10">0</span></td>
					<td><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr11">0</span></td>
				</tr>
				<tr class="hd">
					<td class="grayBg" >12</td>
					<td class="redBg">13</td>
					<td class="grayBg">14</td>
					<td class="redBg">15</td>
				</tr>
				<tr class="getamt">
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr12">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr13">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr14">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr15">0</span></td>
				</tr>
				<tr class="hd">
					<td class="grayBg" >16</td>
					<td class="redBg">17</td>
					<td class="grayBg">18</td>
					<td class="redBg">19</td>
				</tr>
				<tr class="getamt">
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr16">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr17">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr18">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr19">0</span></td>
				</tr>
				<tr class="hd">
					<td class="grayBg" >20</td>
					<td class="redBg">21</td>
					<td class="grayBg">22</td>
					<td class="redBg">23</td>
				</tr>
				<tr class="getamt">
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr20">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr21">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr22">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr23">0</span></td>
				</tr>
				<tr class="hd">
					<td class="grayBg" >24</td>
					<td class="redBg">25</td>
					<td class="grayBg">26</td>
					<td class="redBg">27</td>
				</tr>
				<tr class="getamt">
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr24">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr25">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr26">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr27">0</span></td>
				</tr>
				<tr class="hd">
					<td class="grayBg" >28</td>
					<td class="redBg">29</td>
					<td class="grayBg">30</td>
					<td class="redBg">31</td>
				</tr>
				<tr class="getamt">
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr28">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr29">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr30">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr31">0</span></td>
				</tr>
				<tr class="hd">
					<td class="grayBg" >32</td>
					<td class="redBg">33</td>
					<td class="grayBg">34</td>
					<td class="redBg">35</td>
				</tr>
				<tr class="getamt">
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr32">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr33">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr34">0</span></td>
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr35">0</span></td>
				</tr>
				<tr class="hd">
					<td class="grayBg" >36</td>
				</tr>
				<tr class="getamt">
					<td ><i class="fa fa-usd" aria-hidden="true"></i><span class="betOnEachNum" id="fr36">0</span></td>
				</tr>
			</table>	
	        </div>
	        
	        
        </div>
        <div class="bet-amt" id="betAmtHead">
        	<h6>Bet Amount</h6>
            <div class="select-amt" id="betAmtPrice"></div>
        </div>
        
        <div class="buy-now">
        	<div class="toottip-div" id="buyNowMessage" style="block">
        		<div id="buyMessage"></div>
        		<span></span>
        	</div>
        	<button id="buy">
            	<span class="rupees-icon"><i class="fa fa-usd" aria-hidden="true"></i><span id="tktPrice">0</span></span>
            	<span class="buy-span">
                	<span>BUY</span>NOW
                </span>
            </button>
        </div>
    </div>   
</div>
	<!--right side-->
	<div class="full-screen" style="display:none;" id="advanceDraw">
         <div class="popup">
            <h5>Select Advance Draw <button id="advClose"></button></h5>
            <div class="gm-tab-result-list">
               <div class="col-span-12" id="drawInfo"></div>
               <div class="error-div"><span class="error-msg" id="advError"></span></div>
               <div class="button-tab">
                  <div class="col-span-12 text-center">
                     <div class="button-cancel">
                        <button id="drawCancel">Cancel</button>
                     </div>
                     <div class="button-submit">
                        <button id="drawSubmit">Submit</button>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </div>
	  <!-- Modal Starts -->
	  <div class="full-screen" id="pwt-win" style="display: none">
		    <div class="popup">
		    <h5>Please enter ticket number <button id="pwtClose"></button></h5>
					<div class="modal-body">
						<div class="tkt-no-ct">
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
		
		  <div class="full-screen" id="card-no" style="display: none">
		    <div class="popup">
		    <h5>Please enter card number <button id="cardClose"></button></h5>
					<div class="modal-body">
						<div class="tkt-no-ct">
							<input type="text" id="cardNo" maxlength="20">
						</div>
						<div class="error-msg" id="error-message1"></div>
					</div>
					<div class="col-span-12 text-center">
                     <div class="button-cancel">
                        <button id="cardCancel">Cancel</button>
                     </div>
                     <div class="button-submit">
                        <button id="cardOk">Ok</button>
                     </div>
                  </div>
					
		    </div>
		</div>
		<!-- Modal Ends -->
		
	<div class="full-screen" style="display:none" id="gameResults">
    	<div class="popup">
	    	<h5>Game Results<button id="resultClose"></button></h5>
	        <div class="gm-result">
	        	<div class="game-name-tab" id="drawGameSel"></div>
	            <div id="drawGameResults"></div>
	            <div id="back-top" class="filter_bar">
	            	<div class="div-filter-4">
	            		<div class="date-filter">
	            			<span>Date :</span>
	            			<input type="text" id="selectedDateTimePicker" readonly="readonly" name="selectedDate" class="form-control" />
	            			<div class="input-group-addon" id="selectedDateTimePickerDiv">	<i class="fa fa-calendar"></i></div>
	            			<span id="dateErrorDiv" class="error-msg" style="display: none;"></span>
	            		</div>
					</div>
					<div class="div-filter-4">
						<div class="time-filter">
							<span>Time : </span>
							<select id="drawTimeSelect">
							</select>
						</div>
					</div>
					<div class="div-filter-4">
						<div class="dearch-filter">
							<input type="button" value="Print" id="printResult">
						</div>
					</div>
				</div>
				
				<div class="popup systemMSG error" id="timeErrorDiv" style="display: none;">
			    	<h5>Error<button id="close-popup-button"></button></h5>
			          <div class="modal-bodyWrap">
			             <div class="modal-body">
			                <div class="row">
			                   <div class="col-xs-9 msgBox" id="noDrawDiv"></div>
			                </div>                  
			             </div>
			          </div>
			    </div>
			   
	        </div>
        </div>
    </div>
    
    <div  id="no_game_avail" style="display: none;">
      <h1>No Game Available, Please Contact with Agent</h1>
    </div>

</body>
<!-- Placed at the end of the document so the pages load faster -->
<script type="text/javascript" src="<%=request.getContextPath()%>/com/skilrock/lms/web/common/globalJs/ajaxCall.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/pickthree.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/pickfour.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/eightytwentyten.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/onebytwelve.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/twelveByTwentyFourPCPOS.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/miniRoulette.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/fullRoulette.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/kenoTwo.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/zimLottoBonus.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/tenByTwenty.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/rpos.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.datetimepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/dateformat.js"></script>

<script>var path="<%=request.getContextPath()%>";</script>
<script>var projectName="<%=request.getContextPath()%>";</script>
<script>var currencySymbol ='<%=application.getAttribute("CURRENCY_SYMBOL")%>';</script>
<script>var organizationName ='<%=application.getAttribute("ORG_NAME_JSP")%>';</script>
<script>var countryDeployed ='<%=application.getAttribute("COUNTRY_DEPLOYED")%>';</script>
<script>var cardInfoReq ='<%=application.getAttribute("WEAVER_CARD_DURING_SALE")%>';</script>
</html>

<code id="headId" style="visibility: hidden">
	$RCSfile: rpos_new.jsp,v $ $Revision:
	1.1.1.1$
</code>