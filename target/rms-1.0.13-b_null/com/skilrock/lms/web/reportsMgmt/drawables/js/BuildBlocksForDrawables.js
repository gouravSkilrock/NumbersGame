var chart;
var _options;

var _options = {
	getPie : function() {
		var options = {
			chart : {},
			title : {style : {}},
			subtitle : {style : {}},
			xAxis : {title : {style : {}}},
			yAxis : {title : {style : {}}},
			tooltip : {
				formatter : function() {
					return '<b>' + this.point.name + '</b>: '
							+ this.percentage.toFixed(2) + ' %';
				},
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
				style : {},
			},
			plotOptions : {
				pie : {
					dataLabels : {
						formatter : function() {
							return '<b>' + this.point.name + '</b>: '
									+ this.percentage.toFixed(2) + ' %';
						},
						style : {}
					}
				}
			},
			legend: {itemStyle : {}},
			series : []
		}

		options = _loader.loadPieDefaults(options);

		return options
	},
	getColumn : function() {
		var options = {
			chart : {},
			title : {style : {}},
			subtitle : {style : {}},
			xAxis : {title : {style : {}},labels: {style: {}}},
			yAxis : {title : {style : {}},labels: {style: {}}},
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
				style : {},
				formatter : function() {
					return '<b>' + this.x + '</b><br/>' + this.series.name
							+ ': ' + this.y;
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
			legend: {itemStyle : {}},
			series : []

		}
		options = _loader.loadColumnDefaults(options);
		return options;
	},

	getLine : function() {

		var options = {
			chart : {},
			title : {},
			subtitle : {},
			xAxis : {title : {style : {}},labels: {style: {}}},
			yAxis : {title : {style : {}},labels: {style: {}}},
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
			style : {},
			formatter : function() {
				if(this.series.name.toUpperCase()=='PPR')
					return '<b>' + this.x + '</b><br/>' + this.series.name
					+ ': ' + this.y+'%';
				else
				return '<b>' + this.x + '</b><br/>' + this.series.name
						+ ': ' + this.y;
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
		legend: {itemStyle : {},borderWidth: 0,navigation: {style: {}}},
			series : []

		}
		options = _loader.loadLinesDefaults(options);
		return options;
	}
};


var _loader = {
	loadPieDefaults : function(options) {
		options.chart.zoomType = _pieChartZoomType;
		options.chart.backgroundColor = _pieChartBackgroundColor;
		options.xAxis.lineColor = _piexAxisLineColor;
		options.xAxis.lineWidth = _piexAxisLineWidth;
		options.xAxis.gridLineColor = _piexAxisGridLineColor;
		options.xAxis.gridLineWidth = _piexAxisGridLineWidth;

		options.yAxis.lineColor = _pieyAxisLineColor;
		options.yAxis.lineWidth = _pieyAxisLineWidth;
		options.yAxis.gridLineColor = _pieyAxisGridLineColor;
		options.yAxis.gridLineWidth = _pieyAxisGridLineWidth;
		
		options.tooltip.borderWidth = _pieToolTipBorderWidth;
		options.tooltip.style.color = _pieToolTipStyleColor;

		options.plotOptions.pie.showInLegend = _piePlotOptionsPieShowInLegend;
		options.plotOptions.pie.allowPointSelect = _piePlotOptionsPieAllowPointSelect;
		options.plotOptions.pie.cursor = _piePlotOptionsPieCursor;
		
		options.legend.itemStyle.color = _columnLegendItemStyleColor;
		options.plotOptions.pie.dataLabels.enabled = _piePlotOptionsPieDataLabelsEnabled;
		options.plotOptions.pie.dataLabels.color = _piePlotOptionsPieDataLabelsColor;
		options.plotOptions.pie.dataLabels.connectorColor = _piePlotOptionsPieDataLabelsConnectorColor;
		return options
	},
	loadColumnDefaults : function(options) {
		options.chart.zoomType = _columnChartZoomType;
		options.chart.backgroundColor = _columnChartBackgroundColor;
		
		options.xAxis.lineColor = _columnxAxisLineColor;
		options.xAxis.lineWidth = _columnxAxisLineWidth;
		options.xAxis.gridLineColor = _columnxAxisGridLineColor;
		options.xAxis.gridLineWidth = _columnxAxisGridLineWidth;

		options.yAxis.lineColor = _columnyAxisLineColor;
		options.yAxis.lineWidth = _columnyAxisLineWidth;
		options.yAxis.gridLineColor = _columnyAxisGridLineColor;
		options.yAxis.gridLineWidth = _columnyAxisGridLineWidth;
		
		options.tooltip.style.color = _columnToolTipStyleColor;
		options.tooltip.borderWidth = _columnToolTipBorderWidth;
		
		options.legend.floating = _columnLegendFloating;
		options.legend.borderColor = _columnLegendBorderColor;
		options.legend.borderWidth = _columnLegendBorderWidth;
		options.legend.shadow = _columnLegendShadow;
		options.legend.itemStyle.color = _columnLegendItemStyleColor;
		//options.legend.itemStyle.fontSize = _columnLegendItemStyleFontSize;
		return options;
		
	},
	loadLinesDefaults : function(options) {
		options.chart.zoomType = _lineChartZoomType;
		options.chart.backgroundColor = _lineChartBackgroundColor;
		
		options.xAxis.lineColor = _linexAxisLineColor;
		options.xAxis.lineWidth = _linexAxisLineWidth;
		options.xAxis.gridLineColor = _linexAxisGridLineColor;
		options.xAxis.gridLineWidth = _linexAxisGridLineWidth;

		options.yAxis.lineColor = _lineyAxisLineColor;
		options.yAxis.lineWidth = _lineyAxisLineWidth;
		options.yAxis.gridLineColor = _lineyAxisGridLineColor;
		options.yAxis.gridLineWidth = _lineyAxisGridLineWidth;
		
		options.tooltip.borderWidth = _lineToolTipBorderWidth;
		options.tooltip.style.color = _lineToolTipStyleColor;
		options.legend.itemStyle.color = _columnLegendItemStyleColor;
		return options;
	}
};
