var chart; // global
var options;

function tempFunction() {
	var json = $.parseJSON(_id.o("chartData").value);
	var chartType = _id.o("chartType").value;
	if (chartType != 'pie')
		prepareLineOrColumnChart(chartType, json);
	else
		preparePieChart(chartType, json);
}



function prepareLineOrColumnChart(chartType, json) {
	
	var cat = [];
	options = {
		chart : {},
		title : {},
		subtitle : {},
		xAxis : {
			lineColor : '#000000',
			lineWidth : 2.0,
			gridLineColor : '#000000',
			gridLineWidth : 0.0,
			title : {}
		},
		yAxis : {
			lineColor : '#000000',
			lineWidth : 2.0,
			gridLineColor : '#000000',
			gridLineWidth : 0.0,
			title : {}
		},
		tooltip : {
			backgroundColor : {
				linearGradient : {
					x1 : 0,
					y1 : 0,
					x2 : 0,
					y2 : 1
				},
				stops : [ [ 0, 'rgba(96, 96, 96, .8)' ],
						[ 1, 'rgba(16, 16, 16, .8)' ] ]
			},
			borderWidth : 0,
			style : {
				color : '#FFF'
			},
			formatter : function() {
				return '<b>' + this.x + '</b><br/>' + this.series.name + ': '
						+ this.y;
			}
		},
		plotOptions : {
			'column' : {
				stacking : 'normal',
				dataLabels : {
					enabled : false
				}
			}
		},
		series : []

	}

	var len = json.length;
	var i = 0;
	for (i; i < len; i += 1) {
		if (i === 0) {
			var dat = json[i].data, lenJ = dat.length, j = 0, tmp;
			options.title.text = json[0].chartTitle;
			options.xAxis.title.text = json[0].xAxisTitle;
			options.yAxis.title.text = json[0].yAxisTitle;
			for (j; j < lenJ; j += 1) {
				tmp = dat[j];
				cat.push(tmp);
			}
		} else {
			options.series.push(json[i]);
		}
	}

	options.chart.useHTML = true;
	options.chart.renderTo = 'container3';
	options.chart.zoomType = 'xy';
	options.chart.backgroundColor = '#F9F9FF';
	options.xAxis.categories = cat;
	

	if (chartType == 'line')
		options.chart.type = 'line';
	else if (chartType == 'column')
		options.chart.type = 'column';
	chart = new Highcharts.Chart(options);
}
