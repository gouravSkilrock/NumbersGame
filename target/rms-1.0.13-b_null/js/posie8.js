
//$(document).ready(function(){
//"use strict";
$( window ).load(function() {
	$('*:last-child').addClass('last_Child');
	$('.middle-col').css('width', $(window).width()-471 + 'px');
	$('.numbers-area .number-select').css('width', $('#twelveByTwentyFour').width()-222 + 'px');
	$('#onebytwelve .number-select-12').css('width', $('#twelveByTwentyFour').width()-7 + 'px');
	$('.middle-col .draw-no-top .draw .draw-right').css('width', $('.middle-col .draw-no-top .draw').width()-36 + 'px');
	$('.middle-col .draw-no-top .results .results-number').css('width', $('.middle-col .draw-no-top .results').width()-35 + 'px');	
	$('.bet-type .bettype').css('width', $('.bet-type').width()-125 + 'px');
	$('.bet-tab').css('width', $('#betTypeSection').width()-37 + 'px');
	$('.setting-button BUTTON > SPAN').css('width', $('.setting-button BUTTON#setting-btn').width()-50 + 'px');
	$('.winning-button BUTTON > SPAN').css('width', $('.winning-button BUTTON').width()-50 + 'px');
	$('.results-number ul#winNum').css('width', $('.results-number').width() - 92 + 'px');
	//console.log(realWidth('.full-minirou-areaWrapper', '.middle-col', '.minirou-left-rightWrap'));
	var fullRoullMidBlockWidth = realWidth('.full-minirou-areaWrapper', '.middle-col', '.minirou-left-rightWrap');
	$('.full-minirou-area-left').css('width', fullRoullMidBlockWidth-250 + 'px');
	$('.full-gridMidBlock').css('width', fullRoullMidBlockWidth-(250+110) + 'px');
	$('.minirou-results-number').css('width', $('.results').width() - 35 + 'px');
	var minRoullMidBlockWidth = realWidth('.minirou-areaWrapper', '.middle-col', '.minirou-left-rightWrap');
	$('.minirou-area-left').css('width', minRoullMidBlockWidth-355 + 'px');
});

function realWidth(cloneObj, appendParent, objForWidth){    
	var clone = $(cloneObj).clone();
    clone.addClass('quickClone');    
	clone.css("visibility","hidden");   
	$(appendParent).append(clone);    
	var width = $('.quickClone').find(objForWidth).outerWidth();    
	clone.remove();
	return width;
}

