var chart; 
var width ;
var height;
var options;
var count  = 0;
var currencySymbol;
var countValue  = 1;
var path;
var country;


function renderDrawablesForDashBoard() {

	//path=projectName+_id.o("csvPath").value;
	path=_id.o("csvPath").value;
	country = _id.o("country").value;
	currencySymbol = _id.o("currencySymbol").value; 

	var csvFile = '';
	var fileName = '';
	var chartType = '';
	var chartTitle = '';
	var chartSubTitle = '';
	var chartxAxisTitle = '';
	var chartyAxisTitle = '';
	var chartContainer = '';

	width = (window.screen.availWidth / 4.3);
	height = (window.screen.availHeight / 3) - 80;

	chartType = 'column';
	chartTitle = '';
	chartSubTitle = '';
	chartxAxisTitle = '';
	chartyAxisTitle = '';
	isStacked = true;
	fileName = 'DASHBOARDSALESCOLLECTIONCOLUMNSTACKED.csv';
	csvFile = path + fileName;
	callDrawablesForDashBoard(fileName, csvFile, chartType, chartContainer, chartTitle.toUpperCase(),
			chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked, height,
			width,false);
	
	
	chartType = 'column';
	chartTitle = '';
	chartSubTitle = '';
	chartxAxisTitle = '';
	chartyAxisTitle = '';
	isStacked = true;
	fileName = 'DASHBOARDRETSTATUSCOLUMNSTACKED.csv';
	csvFile = path + fileName;
	callDrawablesForDashBoard(fileName, csvFile, chartType, chartContainer, chartTitle.toUpperCase(),
			chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked, height,
			width,false);
	
	if(country !== "GHANA")
	{
	chartType = 'column';
	chartTitle = '';
	chartSubTitle = '';
	chartxAxisTitle = '';
	chartyAxisTitle = '';
	isStacked = true;
	fileName = 'DASHBOARDCASHCOLLECTIONDRILLDOWN.csv';
	csvFile = path + fileName;
	callDrawablesForDashBoard(fileName, csvFile, chartType, chartContainer, chartTitle.toUpperCase(),
			chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked, height,
			width,false);
	
	}
	
	chartType = 'line';
	chartTitle = '';
	chartSubTitle = '';
	chartxAxisTitle = '';
	chartyAxisTitle = '';
	isStacked = false;
	fileName = 'DASHBOARDDRAWWISECOLLECTIONLINECHART.csv';
	csvFile = path + fileName;
	callDrawablesForDashBoard(fileName, csvFile, chartType, chartContainer, chartTitle.toUpperCase(),
			chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked, height,
			width,false);
	
	/*chartType = 'pie';
	chartTitle = '';
	chartSubTitle = '';
	chartxAxisTitle = '';
	chartyAxisTitle = '';
	isStacked = false;
	fileName = 'DASHBOARDUSERPIE.csv';
	csvFile = path + fileName;
	callDrawablesForDashBoard(fileName, csvFile, chartType, chartContainer, chartTitle.toUpperCase(),
			chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked, height,
			width,false);*/
}

function callDrawablesForDashBoard(fileName , csvFile, chartType, chartContainer,
		chartTitle, chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked,
		height, width, isOverlay) {
	
	if(isOverlay){
		if (chartType == 'pie')
			options = prepareDrawablePieFromCSVFileForOverLay(csvFile, chartType,
					chartContainer, chartTitle, chartSubTitle, chartxAxisTitle,
					chartyAxisTitle, isStacked, _options.getPie(), (window.screen.availHeight/2), (window.screen.availWidth/2));
		else if (chartType == 'column')
			options = prepareDrawableColumnFromCSVFileForOverLay(csvFile, chartType,
					chartContainer, chartTitle, chartSubTitle, chartxAxisTitle,
					chartyAxisTitle, isStacked, _options.getColumn(), (window.screen.availHeight/2), (window.screen.availWidth/2));
		else if (chartType == 'line')
			options = prepareDrawableLineFromCSVFileForOverLay(csvFile, chartType,
					chartContainer, chartTitle, chartSubTitle, chartxAxisTitle,
					chartyAxisTitle, isStacked, _options.getLine(), (window.screen.availHeight/2), (window.screen.availWidth/2));
	}else{
		if (chartType == 'pie')
			options = prepareDrawablePieFromCSVFile(fileName , csvFile, chartType,
					chartContainer, chartTitle, chartSubTitle, chartxAxisTitle,
					chartyAxisTitle, isStacked, _options.getPie(), height, width);
		else if (chartType == 'column')
			options = prepareDrawableColumnFromCSVFile(fileName , csvFile, chartType,
					chartContainer, chartTitle, chartSubTitle, chartxAxisTitle,
					chartyAxisTitle, isStacked, _options.getColumn(), height, width);
		else if (chartType == 'line')
			options = prepareDrawableLineFromCSVFile(fileName , csvFile, chartType,
					chartContainer, chartTitle, chartSubTitle, chartxAxisTitle,
					chartyAxisTitle, isStacked, _options.getLine(), height, width);	
	}
}


function preparePieDataForDrawables(csvFile , chartOptions , summaryHeadings , summaryData , series){
	
	var title;
	var jsonArrayDataForSeries = [];
	var pieDataJson = {};
	var isSimpleData = false;
	var isSlicedData = false;
	var series = {type:"pie" , data :[]};
	var isSummary = false;
	var _resp = _ajaxCall(csvFile);
	if (_resp.result) {
		_respData = _resp.data;
		var lines = _respData.split('\n');
		lines.splice(lines.length - 1, 1);
		for ( var i = 0; i < lines.length; i += 1) {
			var items = lines[i].split(',');
			if (i == 0) {
				if (items.length == 4)
					isSlicedData = true;
				else
					isSimpleData = true;

				title = items[1];
				chartOptions.subtitle.text = items[2];
				chartOptions.xAxis.title.text = items[3];
				chartOptions.yAxis.title.text = items[4];
				// Do nothing for First row
			}else if(items[0].trim().toUpperCase() == 'SUMMARY'){
				isSummary = true;
			}else if(isSummary){
				$.each(items, function(itemNo, item) {
					if (itemNo == 0) {
						summaryHeadings.push(item.toUpperCase());
					} else {
						summaryData.push(item);
					}
				});
			} else {
				pieDataJson = {};
				pieDataJson.name = items[0];
				pieDataJson.y = parseFloat(items[1]);
				if (isSlicedData) {
						pieDataJson.sliced =  items[2];
						pieDataJson.selected =  items[3];
				}
				series.data.push(pieDataJson);
			}
		}
	} else {
		title = "NO DATA AVAILABLE";
	}
	chartOptions.title.text = title;
	chartOptions.series.push(series);
	chartOptions.series.data = jsonArrayDataForSeries;
	
}

function prepareDrawablePieFromCSVFile(fileName ,csvFile, chartType, chartContainer,
		chartTitle, chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked,
		chartOptions, height, width) {

	var summaryHeadings = [];
	var title = '';
	var summaryData = [];
	var container = fileName+"_"+chartType;
	preparePieDataForDrawables(csvFile , chartOptions , summaryHeadings , summaryData);
	title = chartOptions.title.text;
	chartOptions.title.text = "";
	chartOptions.chart.width = width;
	chartOptions.chart.height = height+(summaryHeadings.length>0?0:33);
	chartOptions.chart.type = chartType;
	chartOptions.chart.renderTo = container;
	chartOptions.chart.backgroundColor = "#f7f7f7";
	chartOptions.legend.itemStyle.fontSize = '9px';
	chartOptions.legend.borderWidth = 0;
	chartOptions.legend.symbolWidth = 3;
	//chartOptions.plotOptions.pie.dataLabels.rotation = 90;
	chartOptions.plotOptions.pie.dataLabels.connectorWidth = 2;
	chartOptions.plotOptions.pie.dataLabels.style.fontSize = '10px';
	//chartOptions.legend.x = -20;
	chartOptions.chart.spacingTop = 40;
	chartOptions.chart.spacingBottom = 0;
	chartOptions.chart.spacingLeft = 5;
	chartOptions.chart.spacingRight = 5;
	chartOptions.legend.y = -40;
	chartOptions.legend.floating = false;
	// chartOptions.legend.floating = true;
	chartOptions.legend.verticalAlign = 'top';
	prepareTableToDisplay(summaryHeadings , summaryData , csvFile, container ,title, chartSubTitle);
	chart = new Highcharts.Chart(chartOptions);
}


function prepareDrawablePieFromCSVFileForOverLay(csvFile, chartType, chartContainer,
		chartTitle, chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked,
		chartOptions, height, width) {
	
	var summaryHeadings = [];
	var summaryData = [];
	preparePieDataForDrawables(csvFile , chartOptions , summaryHeadings , summaryData);
	chartOptions.chart.width = (window.screen.availWidth/2);
	chartOptions.chart.height = (window.screen.availHeight/2);
	chartOptions.chart.type = chartType;
	chartOptions.plotOptions.pie.dataLabels.connectorWidth = 2;
	chartOptions.chart.renderTo = chartContainer;
	chartOptions.chart.backgroundColor = "#f7f7f7";
	//chartOptions.legend.itemStyle.fontSize = '8px';
	chartOptions.legend.borderWidth = 0;
	//chartOptions.legend.symbolWidth = 7;
	chartOptions.legend.verticalAlign = 'bottom';
	chart = new Highcharts.Chart(chartOptions);
}

function prepareColumnDataForDrawables(csvFile , chartOptions , summaryHeadings , summaryData , cat , series){

	var title ;
	var isSummary = false;
	var _resp = _ajaxCall(csvFile);
	if (_resp.result) {
		_respData = _resp.data;
		// alert(_resp.data);
		var lines = _respData.split('\n');
		lines.splice(lines.length - 1, 1);
		for ( var i = 0; i < lines.length; i += 1) {
			var items = lines[i].split(',');
			items.splice(items.length - 1, 1);
			if(i == 0){
				title = items[1];
				chartOptions.subtitle.text = items[2];
				chartOptions.xAxis.title.text = items[3];
				chartOptions.yAxis.title.text = items[4];
			} else if (i == 1) {
				$.each(items, function(itemNo, item) {
					if (itemNo > 0)
						cat.push(item.toUpperCase());
				});
			}  else if(items[0].trim().toUpperCase() == 'SUMMARY'){
				isSummary = true;
			}else if(isSummary){
				$.each(items, function(itemNo, item) {
					if (itemNo == 0) {
						summaryHeadings.push(item.toUpperCase());
					} else {
						summaryData.push(parseFloat(item));
					}
				});
			}else {
				series = {
					data : []
				};
				$.each(items, function(itemNo, item) {
					if (itemNo == 0) {
						series.name = item;
					} else {
						series.data.push(parseFloat(item));
					}
				});
				chartOptions.series.push(series);
			}
		}
	} else {
		title = "NO DATA AVAILABLE";
	}
	chartOptions.title.text = title;
	
	
}


function prepareDrawableColumnFromCSVFile(fileName ,csvFile, chartType, chartContainer,
		chartTitle, chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked,
		chartOptions, height, width) {
	
	var title = '';
	var container = fileName+"_"+chartType;
	var summaryHeadings = [];
	var summaryData = [];
	var cat = [];
	var series = {
		data : []
	};
	prepareColumnDataForDrawables(csvFile , chartOptions , summaryHeadings , summaryData , cat , series)
	title = chartOptions.title.text; 
	chartOptions.chart.width = width;
	chartOptions.chart.height = height+(summaryHeadings.length>0?0:33);
	chartOptions.chart.type = chartType;
	chartOptions.chart.renderTo = container;
	chartOptions.chart.backgroundColor = "#f7f7f7";
	
	chartOptions.title.text = '';
	chartOptions.title.style.fontSize = '10px';
	chartOptions.title.style.color = '#000000';
	//chartOptions.subtitle.text = chartSubTitle;

	chartOptions.xAxis.categories = cat;
	chartOptions.xAxis.labels.style.fontStyle = 'bold';
	chartOptions.xAxis.labels.style.color = '#000000';
	chartOptions.xAxis.labels.style.fontSize = '9px';
	chartOptions.xAxis.title.style.fontSize = '10px';
	//chartOptions.xAxis.title.text = chartxAxisTitle;
	
	chartOptions.yAxis.title.style.fontStyle = 'bold';
	//chartOptions.yAxis.title.style.color = '#000000';
	chartOptions.yAxis.title.style.fontSize = '10px';
	chartOptions.yAxis.labels.style.fontStyle = 'bold';
	chartOptions.yAxis.labels.style.color = '#000000';
	chartOptions.yAxis.labels.style.fontSize = '9px';
	//chartOptions.yAxis.title.text = chartyAxisTitle;
	
	chartOptions.plotOptions.column.pointPadding = 0.2
	
	chartOptions.chart.spacingTop = 40;
	chartOptions.chart.spacingBottom = 0;
	chartOptions.chart.spacingLeft = 5;
	chartOptions.chart.spacingRight = 5;
	chartOptions.legend.enabled = true;
	chartOptions.legend.itemStyle.fontSize = '9px';
	chartOptions.legend.borderWidth = 0;
	chartOptions.legend.symbolWidth = 3;
	//chartOptions.legend.x = -20;
	chartOptions.legend.y = -40;
	//chartOptions.legend.floating = true;
	chartOptions.legend.floating = false;
	chartOptions.legend.verticalAlign = 'top';
	prepareTableToDisplay(summaryHeadings , summaryData , csvFile, container ,title, chartSubTitle);
	chart = new Highcharts.Chart(chartOptions);
}



function prepareDrawableColumnFromCSVFileForOverLay(csvFile, chartType, chartContainer,
		chartTitle, chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked,
		chartOptions, height, width) {

	var summaryHeadings = [];
	var summaryData = [];
	var isSummary = false;
	var cat = [];
	var series = {
		data : []
	};
	prepareColumnDataForDrawables(csvFile , chartOptions , summaryHeadings , summaryData , cat , series)
	
	chartOptions.chart.height = height;
	chartOptions.chart.width = width;
	
	chartOptions.chart.type = chartType;
	chartOptions.chart.renderTo = chartContainer;
	chartOptions.chart.backgroundColor = "#f7f7f7";
	
	////chartOptions.title.text = chartTitle;
	//chartOptions.title.style.fontSize = '10px';
	//chartOptions.title.style.color = '#000000';
	//chartOptions.subtitle.text = chartSubTitle;
	chartOptions.yAxis.labels.style.fontStyle = 'bold';
	chartOptions.yAxis.labels.style.color = '#000000';
	chartOptions.xAxis.categories = cat;
	chartOptions.xAxis.labels.style.fontStyle = 'bold';
	chartOptions.xAxis.labels.style.color = '#000000';
	//chartOptions.xAxis.labels.style.fontSize = '9px';
	//chartOptions.xAxis.title.text = chartxAxisTitle;
	
	chartOptions.yAxis.title.style.fontStyle = 'bold';
	//chartOptions.yAxis.title.style.color = '#000000';
	//chartOptions.yAxis.title.style.fontSize = '9px';
	//chartOptions.yAxis.title.text = chartyAxisTitle;
	
	chartOptions.plotOptions.column.pointPadding = 0.2
	
	chartOptions.legend.enabled = true;
	//chartOptions.legend.itemStyle.fontSize = '8px';
	chartOptions.legend.borderWidth = 0;
	//chartOptions.legend.symbolWidth = 7;
	// chartOptions.legend.floating = true;
	chartOptions.legend.verticalAlign = 'bottom';
	chart = new Highcharts.Chart(chartOptions);
}


function prepareLineDataForDrawables(csvFile , chartOptions , summaryHeadings , summaryData , cat , series){
	
	var title;
	var isSummary = false;
	var _resp = _ajaxCall(csvFile);
	if (_resp.result) {
		_respData = _resp.data;
		var lines = _respData.split('\n');
		lines.splice(lines.length - 1, 1);
		for ( var i = 0; i < lines.length; i += 1) {
			var items = lines[i].split(',');
			items.splice(items.length - 1, 1);
			if(i == 0){
				title = items[1];
				chartOptions.subtitle.text = items[2];
				chartOptions.xAxis.title.text = items[3];
				chartOptions.yAxis.title.text = items[4];
			} else if (i == 1) {
				$.each(items, function(itemNo, item) {
					if (itemNo > 0)
						cat.push(item.toUpperCase());
				});
			} else if(items[0].trim().toUpperCase() == 'SUMMARY'){
				isSummary = true;
			}else if(isSummary){
				$.each(items, function(itemNo, item) {
					if (itemNo == 0) {
						summaryHeadings.push(item.toUpperCase());
					} else {
						summaryData.push(parseFloat(item));
					}
				});
			}else {
				series = {
					data : []
				};
				$.each(items, function(itemNo, item) {
					if (itemNo == 0) {
						series.name = item;
					} else {
						series.data.push(parseFloat(item));
					}
				});
				chartOptions.series.push(series);
			}
		}
	} else {
		title = "NO DATA AVAILABLE";
	}
	chartOptions.title.text = title;
}

function prepareDrawableLineFromCSVFile(fileName ,csvFile, chartType, chartContainer,
		chartTitle, chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked,
		chartOptions, height, width) {
	var container = fileName+"_"+chartType;
	var title = '';
	var isSummary = false;
	var summaryHeadings = [];
	var summaryData = [];
	var cat = [];
	var series = {
		data : []
	};
	prepareLineDataForDrawables(csvFile , chartOptions , summaryHeadings , summaryData , cat , series);
	
	title  = chartOptions.title.text;
	chartOptions.xAxis.labels.style.fontStyle = 'bold';
	chartOptions.xAxis.labels.style.color = '#000000';
	chartOptions.xAxis.labels.style.fontSize = '9px';
	
	chartOptions.xAxis.title.style.fontSize = '10px';
	chartOptions.yAxis.title.style.fontSize = '10px';
	//chartOptions.yAxis.title.margin = 60;
	//chartOptions.xAxis.title.align = 'low';
	//chartOptions.xAxis.title.offset = 10;
	
	chartOptions.yAxis.min = 0;
	chartOptions.yAxis.labels.style.fontStyle = 'bold';
	chartOptions.yAxis.labels.style.color = '#000000';
	chartOptions.yAxis.labels.style.fontSize = '9px';
	
	chartOptions.legend.x = -10;
	chartOptions.legend.y = -10;
	
	
	chartOptions.legend.enabled = true;
	chartOptions.legend.borderWidth = 0;
	chartOptions.legend.symbolWidth = 3;
	chartOptions.legend.verticalAlign = 'top';
	chartOptions.legend.navigation.arrowSize = 12;
	chartOptions.legend.itemStyle.fontSize = '9px';
	chartOptions.legend.navigation.inactiveColor = '#CCC';
	chartOptions.legend.navigation.style.fontWeight = 'bold';
	chartOptions.legend.navigation.style.color = '#333';
	chartOptions.legend.navigation.style.fontSize = '12px';

	chartOptions.chart.height = height+(summaryHeadings.length>0?0:33);
	chartOptions.chart.width = width;
	
	chartOptions.chart.spacingTop = 40;
	chartOptions.chart.spacingBottom = 0;
	chartOptions.chart.spacingLeft = 5;
	chartOptions.chart.spacingRight = 5;
	
	//chartOptions.legend.x = -20;
	chartOptions.legend.y = -40;
	chartOptions.legend.floating = true;
	chartOptions.chart.backgroundColor = "#f7f7f7";
	chartOptions.title.text = '';
	chartOptions.chart.type = chartType;
	chartOptions.xAxis.categories = cat;
	chartOptions.chart.renderTo = container;
	
	prepareTableToDisplay(summaryHeadings , summaryData , csvFile, container ,title, chartSubTitle);
	chart = new Highcharts.Chart(chartOptions);
}


function prepareDrawableLineFromCSVFileForOverLay(csvFile, chartType, chartContainer,
		chartTitle, chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked,
		chartOptions, height, width) {

	var isSummary = false;
	var cat = [];
	var series = {
		data : []
	};
	var summaryHeadings = [];
	var summaryData = [];
	prepareLineDataForDrawables(csvFile , chartOptions , summaryHeadings , summaryData , cat , series);
	
	chartOptions.xAxis.labels.style.fontStyle = 'bold';
	chartOptions.xAxis.labels.style.color = '#000000';
	chartOptions.yAxis.labels.style.fontStyle = 'bold';
	chartOptions.yAxis.labels.style.color = '#000000';
	//chartOptions.xAxis.labels.style.fontSize = '9px';
	chartOptions.yAxis.min = 0;
	chartOptions.legend.enabled = true;
	//chartOptions.legend.itemStyle.fontSize = '8px';
	chartOptions.legend.borderWidth = 0;
	//chartOptions.legend.symbolWidth = 2;
	chartOptions.legend.verticalAlign = 'bottom';
	//chartOptions.legend.navigation.arrowSize = 12;
	chartOptions.legend.navigation.inactiveColor = '#CCC';
	chartOptions.legend.navigation.style.fontWeight = 'bold';
	chartOptions.legend.navigation.style.color = '#333';
	//chartOptions.legend.navigation.style.fontSize = '12px';

	chartOptions.chart.height = height;
	chartOptions.chart.width = width;
	chartOptions.chart.backgroundColor = "#f7f7f7";
	chartOptions.chart.type = chartType;
	chartOptions.xAxis.categories = cat;
	chartOptions.chart.renderTo = chartContainer;
	chart = new Highcharts.Chart(chartOptions);
}

function prepareTableToDisplay(summaryHeadings , summaryData , csvFile, container ,chartTitle, chartSubTitle){
	
	var summarData = summaryHeadings.length>0?true:false;
	var table = document.createElement("table");
		     table.setAttribute("id","table"+container);
		     table.setAttribute("cellpadding","2");
		     table.setAttribute("cellspacing","0");
		 
	var tr = document.createElement("tr");	
	var td = document.createElement("td");
			td.setAttribute("colspan","3");
			td.setAttribute("class","mainHead");
			td.setAttribute("colspan","3");
			td.innerHTML = chartTitle;	
			tr.appendChild(td);
			table.appendChild(tr);

		 tr = document.createElement("tr");	
		 td = document.createElement("td");
			td.setAttribute("colspan","3");
			
			if(summarData)
				td.setAttribute("class","sideBorders");
			else
				td.setAttribute("class","sideBordersNoSummary");
			
	 var containerDiv = document.createElement("div");
			containerDiv.setAttribute("id",container);	
			
			td.appendChild(containerDiv);
			tr.appendChild(td);
			table.appendChild(tr);

		if(summarData){
			tr = document.createElement("tr");
			for(var i =0;i<3; i+=1){
				td = document.createElement("td");
				td.setAttribute("class","downHead");
				td.innerHTML = summaryHeadings[i];
				tr.appendChild(td);
		 }
		 table.appendChild(tr);
		tr = document.createElement("tr");
			for(var i =0;i<3; i+=1){
				td = document.createElement("td");
				td.setAttribute("class","data");
				td.innerHTML = summaryData[i];
				tr.appendChild(td);
		}
			table.appendChild(tr);
	}
		 td = document.createElement("td");
		 td.setAttribute("id","GRAPH"+count);
		 table.onclick=overlayDrawables;
		 td.appendChild(table);
		
		if(count==0){
			tr = document.createElement("tr");
			tr.setAttribute("id" , "tr"+countValue);	
		}else if(count%3 == 0){
			countValue+=1;
			tr = document.createElement("tr");
			tr.setAttribute("id" , "tr"+countValue);
		}else {
			tr =_id.o("tr"+countValue); 
		}
		
		tr.appendChild(td);
		var obj  = _id.o("mainTable");
		obj.appendChild(tr);
		count+= 1;
}


function overlayDrawables(){

	var div  =  this.id;
	//alert(div);
	var csvFile = '';
	var chartType = '';
	var chartTitle = '';
	var chartSubTitle = '';
	var chartxAxisTitle = '';
	var chartyAxisTitle = '';
	var chartContainer = 'temp';
	var chartDetails = div.split("_");
	var fileName = chartDetails[0].replace("table","");
	var chartType = chartDetails[1];
	var isStacked = true;
	csvFile = path + fileName;

	var overlay = document.createElement("div");
	  overlay.setAttribute("id","overlay");
	   overlay.setAttribute("class", "overlay");
	   document.body.appendChild(overlay);
	  // alert(height/3);
	   var imgSrc=projectName+"/LMSImages/images/close.png";
	   var img = document.createElement("img");
	   img.setAttribute("src",imgSrc);
	   img.setAttribute("height","10");
	   img.setAttribute("width","10");
	   img.setAttribute("style","margin-top: 50px;margin-left: 700px;border:0px solid #000;");
	   img.setAttribute("title", "close");
	   img.onclick=removeOverlay;
	   overlay.appendChild(img);
	   var div = document.createElement("div");
	   div.setAttribute("id","temp");
	   div.setAttribute("style","margin-top: 0px;margin-left: 300px;border:0px solid #000;");
	   overlay.appendChild(div);
	   callDrawablesForDashBoard(fileName ,csvFile, chartType, chartContainer, chartTitle.toUpperCase(),
			chartSubTitle, chartxAxisTitle, chartyAxisTitle, isStacked, height,
			width , true);
	
	
	}
function removeOverlay() {
	 document.body.removeChild(document.getElementById("overlay"));
	}

function CSVToArray(strData, strDelimiter) {
	// Check to see if the delimiter is defined. If not,
	// then default to comma.
	strDelimiter = (strDelimiter || ",");
	// Create a regular expression to parse the CSV values.
	var objPattern = new RegExp((
	// Delimiters.
			"(\\" + strDelimiter + "|\\r?\\n|\\r|^)" +
			// Quoted fields.
					"(?:\"([^\"]*(?:\"\"[^\"]*)*)\"|" +
					// Standard fields.
					"([^\"\\" + strDelimiter + "\\r\\n]*))"), "gi");
	// Create an array to hold our data. Give the array
	// a default empty first row.
	var arrData = [ [] ];
	// Create an array to hold our individual pattern
	// matching groups.
	var arrMatches = null;
	// Keep looping over the regular expression matches
	// until we can no longer find a match.
	while (arrMatches = objPattern.exec(strData)) {
		// Get the delimiter that was found.
		// alert(arrMatches);
		var strMatchedDelimiter = arrMatches[1];
		// Check to see if the given delimiter has a length
		// (is not the start of string) and if it matches
		// field delimiter. If id does not, then we know
		// that this delimiter is a row delimiter.
		if (strMatchedDelimiter.length && (strMatchedDelimiter != strDelimiter)) {
			// alert('block 1');
			// Since we have reached a new row of data,
			// add an empty row to our data array.
			arrData.push( []);
		}
		// Now that we have our delimiter out of the way,
		// let's check to see which kind of value we
		// captured (quoted or unquoted).
		if (arrMatches[2]) {
			// alert('block 2');
			// We found a quoted value. When we capture
			// this value, unescape any double quotes.
			var strMatchedValue = arrMatches[2].replace(
					new RegExp("\"\"", "g"), "\"");
		} else {
			// alert('block 3');
			// We found a non-quoted value.
			var strMatchedValue = arrMatches[3];
			// alert(strMatchedValue);
		}
		// Now that we have our value string, let's add
		// it to the data array.
		arrData[arrData.length - 1].push(strMatchedValue);
	}
	// Return the parsed data.
	return (arrData);
}

function CSV2JSON(csv) {
	var array = CSVToArray(csv);
	var objArray = [];
	for ( var i = 1; i < array.length; i++) {
		objArray[i - 1] = {};
		for ( var k = 0; k < array[0].length && k < array[i].length; k++) {
			var key = array[0][k];
			objArray[i - 1][key] = array[i][k]
		}
	}

	var json = JSON.stringify(objArray);
	var str = json.replace(/},/g, "},\r\n");

	return str;
}

function CSV2JSON1(csv) {
	var cat = [];
	var array = CSVToArray(csv, ',');
	// alert(array);
	// alert(array.length);
	array.splice(array.length - 1, 1);
	// alert(array);
	var objArray = [];
	var count = 0;
	for ( var i = 1; i < array.length; i++) {

		var items = array[i].split(',');
		// alert(items);
		if (i == 0 && count == 0) {
			$.each(items, function(itemNo, item) {
				if (itemNo > 0)
					cat.push(item);
			});
			alert(cat);
		} else {
			var series = {
				data : []
			};
			$.each(items, function(itemNo, item) {
				if (itemNo == 0) {
					series.name = item;
				} else {
					series.data.push(parseFloat(item));
				}
			});
			alert(series);
			// chartOptions.series.push(series);
		}

		/*
		 * objArray[i - 1] = {}; if(i==1){
		 * 
		 * }else{
		 * 
		 * for (var k = 0; k < array[0].length && k < array[i].length; k++) {
		 * alert(array[0]); var key = array[0][k]; alert(key); objArray[i -
		 * 1][key] = array[i][k] } }
		 */
		count += 1;

	}
	alert(objArray);
	var json = JSON.stringify(objArray, null, '\t');
	alert(json);
	var str = json.replace(/},/g, "},\r\n");
	alert(str);
	return str;
}