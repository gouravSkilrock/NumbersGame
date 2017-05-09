jq.fn.selectToUISlider = function(settings){
	var selects = jq(this);
	//accessible slider options
	var options = jq.extend({
		labels: 3, //number of visible labels
		tooltip: true, //show tooltips, boolean
		tooltipSrc: 'text',//accepts 'value' as well
		labelSrc: 'value',//accepts 'value' as well	,
		sliderOptions: null
	}, settings);


	//handle ID attrs - selects each need IDs for handles to find them
	var handleIds = (function(){
		var tempArr = [];
		selects.each(function(){
			tempArr.push('handle_'+jq(this).attr('id'));
		});
		return tempArr;
	})();
	
	//array of all option elements in select element (ignores optgroups)
	var selectOptions = (function(){
		var opts = [];
		selects.eq(0).find('option').each(function(){
			opts.push({
				value: jq(this).attr('value'),
				text: jq(this).text()
			});
		});
		return opts;
	})();
	
	//array of opt groups if present
	var groups = (function(){
		if(selects.eq(0).find('optgroup').size()>0){
			var groupedData = [];
			selects.eq(0).find('optgroup').each(function(i){
				groupedData[i] = {};
				groupedData[i].label = jq(this).attr('label');
				groupedData[i].options = [];
				jq(this).find('option').each(function(){
					groupedData[i].options.push({text: jq(this).text(), value: jq(this).attr('value')});
				});
			});
			return groupedData;
		}
		else return null;
	})();	
	
	//check if obj is array
	function isArray(obj) {
		return obj.constructor == Array;
	}
	//return tooltip text from option index
	function ttText(optIndex){
		return (options.tooltipSrc == 'text') ? selectOptions[optIndex].text : selectOptions[optIndex].value;
	}
	
	//plugin-generated slider options (can be overridden)
	var sliderOptions = {
		step: 1,
		min: 0,
		orientation: 'horizontal',
		max: selectOptions.length-1,
		range: selects.length > 1,//multiple select elements = true
		change:function(e,ui){

			filterData();
		
 			
				var hh2 = (jq('#'+handleIds[0]).css("left"));
				//alert(hh2);	
jq('#adsds').css("width",hh2);
var hh3 = (jq('#'+handleIds[1]).css("left"));	
jq('#adsds1').css("left",hh3);
//alert(hh3);
var hh4 =(100-(hh3.split("%")[0]));

jq('#adsds1').css("width",hh4+"%");

	

		

		},
		slide: function(e, ui) {//slide function
			
				var thisHandle = jq(ui.handle);
				//handle feedback 
				var textval = ttText(ui.value);
				thisHandle
					.attr('aria-valuetext', textval)
					.attr('aria-valuenow', ui.value)
					.find('.ui-slider-tooltip .ttContent')
						.text( textval );

				//control original select menu
				var currSelect = jq('#' + thisHandle.attr('id').split('handle_')[1]);
				currSelect.find('option').eq(ui.value).attr('selected', 'selected');
				var hh2 = (jq('#'+handleIds[0]).css("left"));
//alert(hh2);	
jq('#adsds').css("width",hh2);
var hh3 = (jq('#'+handleIds[1]).css("left"));	
jq('#adsds1').css("left",hh3);
//alert(hh3);
var hh4 =(100-(hh3.split("%")[0]));

jq('#adsds1').css("width",hh4+"%");


    	
		
			//var hh = (jq('#'+handleIds[0]).css("left"));	
			//jq('#adsds').css("width",hh);
			//var hh1 = (jq('#'+handleIds[1]).css("left"));	
			//$('#adsds1').css("width",hh1);
			
	
		},
		values: (function(){
			var values = [];
			selects.each(function(){
				values.push( jq(this).get(0).selectedIndex );
			});
			
			return values;
		})()
	
	};

	//slider options from settings
	options.sliderOptions = (settings) ? jq.extend(sliderOptions, settings.sliderOptions) : sliderOptions;
		
	//select element change event	
	selects.bind('change keyup click', function(){
		var thisIndex = jq(this).get(0).selectedIndex;
		var thisHandle = jq('#handle_'+ jq(this).attr('id'));
		var handleIndex = thisHandle.data('handleNum');
		thisHandle.parents('.ui-slider:eq(0)').slider("values", handleIndex, thisIndex);
	});


	

	//create slider component div
	var sliderComponent = jq('<div ></div>');


		

	//CREATE HANDLES
	selects.each(function(i){
		var hidett = '';
		
		//associate label for ARIA
		var thisLabel = jq('label[for=' + jq(this).attr('id') +']');
		//labelled by aria doesn't seem to work on slider handle. Using title attr as backup
		var labelText = (thisLabel.size()>0) ? 'Slider control for '+ thisLabel.text()+'' : '';
		var thisLabelId = thisLabel.attr('id') || thisLabel.attr('id', 'label_'+handleIds[i]).attr('id');
		
		
		if( options.tooltip == false ){hidett = ' style="display: none;"';}
		jq('<a '+
				'href="#" tabindex="0" '+
				'id="'+handleIds[i]+'" '+
				'class="ui-slider-handle" '+
				'role="slider" '+
				'aria-labelledby="'+thisLabelId+'" '+
				'aria-valuemin="'+options.sliderOptions.min+'" '+
				'aria-valuemax="'+options.sliderOptions.max+'" '+
				'aria-valuenow="'+options.sliderOptions.values[i]+'" '+
				'aria-valuetext="'+ttText(options.sliderOptions.values[i])+'" '+
			'><span class="screenReaderContext">'+labelText+'</span>'+
			'<span class="ui-slider-tooltip ui-widget-content ui-corner-all"'+ hidett +'><span class="ttContent"></span>'+
				'<span class="ui-tooltip-pointer-down ui-widget-content"><span class="ui-tooltip-pointer-down-inner"></span></span>'+
			'</span></a>')
			.data('handleNum',i)
			.appendTo(sliderComponent);
	});
	
	//CREATE SCALE AND TICS
	
	//write dl if there are optgroups
	if(groups) {
		var inc = 0;
		var scale = sliderComponent.append('<dl class="ui-slider-scale ui-helper-reset" role="presentation"></dl>').find('.ui-slider-scale:eq(0)');
		jq(groups).each(function(h){
			scale.append('<dt style="width: '+ (100/groups.length).toFixed(2) +'%' +'; left:'+ (h/(groups.length-1) * 100).toFixed(2)  +'%' +'"><span>'+this.label+'</span></dt>');//class name becomes camelCased label
			var groupOpts = this.options;
			jq(this.options).each(function(i){
				var style = (inc == selectOptions.length-1 || inc == 0) ? 'style="display: none;"' : '' ;
				var labelText = (options.labelSrc == 'text') ? groupOpts[i].text : groupOpts[i].value;
				scale.append('<dd style="left:'+ leftVal(inc) +'"><span class="ui-slider-label">'+ labelText +'</span><span class="ui-slider-tic ui-widget-content"'+ style +'></span></dd>');
				inc++;
			});
		});
	}
	//write ol
	else {
		var scale = sliderComponent.append('<ol class="ui-slider-scale ui-helper-reset" role="presentation"></ol>').find('.ui-slider-scale:eq(0)');
		jq(selectOptions).each(function(i){
			var style = (i == selectOptions.length-1 || i == 0) ? 'style="display: none;"' : '' ;
			var labelText = (options.labelSrc == 'text') ? this.text : this.value;
			scale.append('<li style="left:'+ leftVal(i) +'"><span class="ui-slider-label">'+ labelText +'</span><span class="ui-slider-tic ui-widget-content"'+ style +'></span></li>');
		});
	}
	
	function leftVal(i){
		return (i/(selectOptions.length-1) * 100).toFixed(2)  +'%';
		
	}
	

	
	
	//show and hide labels depending on labels pref
	//show the last one if there are more than 1 specified
	if(options.labels > 1) sliderComponent.find('.ui-slider-scale li:last span.ui-slider-label, .ui-slider-scale dd:last span.ui-slider-label').addClass('ui-slider-label-show');

	//set increment
	var increm = Math.max(1, Math.round(selectOptions.length / options.labels));
	//show em based on inc
	for(var j=0; j<selectOptions.length; j+=increm){
		if((selectOptions.length - j) > increm){//don't show if it's too close to the end label
			sliderComponent.find('.ui-slider-scale li:eq('+ j +') span.ui-slider-label, .ui-slider-scale dd:eq('+ j +') span.ui-slider-label').addClass('ui-slider-label-show');
		}
	}

	//style the dt's
	sliderComponent.find('.ui-slider-scale dt').each(function(i){
		jq(this).css({
			'left': ((100 /( groups.length	))*i).toFixed(2) + '%'
		});
						var hh2 = (jq('#'+handleIds[0]).css("left"));
//alert(hh2);	
jq('#adsds').css("width",hh2);
var hh3 = (jq('#'+handleIds[1]).css("left"));	
jq('#adsds1').css("left",hh3);
//alert(hh3);
var hh4 =(100-(hh3.split("%")[0]));

jq('#adsds1').css("width",hh4+"%");
	});
	

	//inject and return 
	sliderComponent
	.insertAfter(jq(this).eq(this.length-1))
	.slider(options.sliderOptions)
	.attr('role','application')
	.find('.ui-slider-label')
	.each(function(){
		jq(this).css('marginLeft', -jq(this).width()/2);
	});
	
	//update tooltip arrow inner color
	sliderComponent.find('.ui-tooltip-pointer-down-inner').each(function(){
				var bWidth = jq('.ui-tooltip-pointer-down-inner').css('borderTopWidth');
				var bColor = jq(this).parents('.ui-slider-tooltip').css('backgroundColor')
				jq(this).css('border-top', bWidth+' solid '+bColor);
	});
	
	var values = sliderComponent.slider('values');
	
	if(isArray(values)){
		jq(values).each(function(i){
			sliderComponent.find('.ui-slider-tooltip .ttContent').eq(i).text( ttText(this) );
		});
	}
	else {
		sliderComponent.find('.ui-slider-tooltip .ttContent').eq(0).text( ttText(values) );
	}


	
jq('<div id="adsds" class="ui-widget-headerr ui-slider-range" style="left: 0%;"></div>').appendTo(sliderComponent);
jq('<div id="adsds1" class="ui-widget-headerrr ui-slider-range" style="left:0%;width:"></div>').appendTo(sliderComponent);
var hh2 = (jq('#'+handleIds[0]).css("left"));	
jq('#adsds').css("width",hh2);
var hh3 = (jq('#'+handleIds[1]).css("left"));	
jq('#adsds1').css("left",hh3);

var hh4 =(100-(hh3.split("%")[0]));

jq('#adsds1').css("width",hh4+"%");



return this;
}


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/reportsMgmt/slider/js/selectToUISlider.jQuery.js,v $'] = '$Id: selectToUISlider.jQuery.js,v 1.2 2016/11/01 05:52:04 neeraj Exp $';

