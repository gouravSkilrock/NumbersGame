var chart; // global
var options;
var path = projectName;

function renderDrawables() {
	var json = $.parseJSON(_id.o("chartData").value);
	var chartType = _id.o("chartType").value;
	var chartContainer = 'container';

	if (chartType == 'pie')
		options = prepareDrawablePie(chartType, chartContainer, json, _options
				.getPie());
	else if (chartType == 'column')
		options = prepareDrawableColumn(chartType, chartContainer, json, _options
				.getColumn());
	else if (chartType == 'line')
		options = prepareDrawableLine(chartType, chartContainer, json, _options
				.getLine());
}

function prepareDrawableColumn(chartType, chartContainer, json, chartOptions) {

	var cat = [];
	var len = json.length;
	var i = 0;
	for (i; i < len; i += 1) {
		if (i === 0) {
			var dat = json[i].data, lenJ = dat.length, j = 0, tmp;
			chartOptions.title.text = json[0].chartTitle;
			chartOptions.subtitle.text = json[0].chartSubTitle;
			chartOptions.xAxis.title.text = json[0].xAxisTitle;
			chartOptions.yAxis.title.text = json[0].yAxisTitle;
			for (j; j < lenJ; j += 1) {
				tmp = dat[j];
				cat.push(tmp);
			}
		} else {
			chartOptions.series.push(json[i]);
		}
	}
	chartOptions.chart.useHTML = true;
	chartOptions.chart.renderTo = chartContainer;
	chartOptions.xAxis.categories = cat;
	chartOptions.chart.type = chartType;
	chart = new Highcharts.Chart(chartOptions);
}

function prepareDrawableLine(chartType, chartContainer, json, chartOptions) {

	var cat = [];
	var len = json.length;
	var i = 0;
	for (i; i < len; i += 1) {
		if (i === 0) {
			var dat = json[i].data, lenJ = dat.length, j = 0, tmp;
			chartOptions.title.text = json[0].chartTitle;
			chartOptions.subtitle.text = json[0].chartSubTitle;
			chartOptions.xAxis.title.text = json[0].xAxisTitle;
			chartOptions.yAxis.title.text = json[0].yAxisTitle;
			for (j; j < lenJ; j += 1) {
				tmp = dat[j];
				cat.push(tmp);
			}
		} else {
			chartOptions.series.push(json[i]);
		}
	}

	chartOptions.chart.useHTML = true;
	chartOptions.xAxis.categories = cat;
	chartOptions.yAxis.min = _lineyAxisMin;
	chartOptions.chart.type = chartType;
	chartOptions.chart.renderTo = chartContainer;
	chart = new Highcharts.Chart(chartOptions);
}

function prepareDrawablePie(chartType, chartContainer, json, chartOptions) {

	var len = json.length;
	var i = 0;
	for (i; i < len; i += 1) {
		if (i === 0) {
			chartOptions.title.text = json[0].chartTitle;
			chartOptions.subtitle.text = json[0].chartSubTitle;
			chartOptions.xAxis.title.text = json[0].xAxisTitle;
			chartOptions.yAxis.title.text = json[0].yAxisTitle;
		} else {
			chartOptions.series.push(json[i]);
		}
	}

	chartOptions.chart.type = chartType;
	chartOptions.chart.renderTo = chartContainer;
	chart = new Highcharts.Chart(chartOptions);
}