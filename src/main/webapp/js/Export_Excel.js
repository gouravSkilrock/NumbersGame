function exportToExcel(tableId) {
	var headerArr = [];
	var totalColumnsLength = $('#' + tableId + ' tbody:first tr:first').find('td').length;
	var noOfrow = $('#' + tableId + ' thead:first').find('tr').length;
	var noOfcolumn = $('#' + tableId + ' tbody tr:first').find('td').length;

	var noOfFootRow = $('#' + tableId + ' > tfoot >tr').length;
	var footCellArr;
	var footRowArr = new Array();

	for ( var i = 0; i < noOfFootRow; i++) {
		footCellArr = new Array();
		for ( var j = 0; j < noOfcolumn; j++) {
			cellObj = {};
			cellObj['data'] = "";
			cellObj['isDataSet'] = "NO";
			cellObj['isMergedCell'] = "NO";
			cellObj['mergeRange'] = "0,0,0,0";
			footCellArr.push(cellObj);
		}
		footRowArr.push(footCellArr);
	}

	$('#' + tableId + ' > tfoot:first >tr').each(function(index) {
		var row = $(this);
		var currentCol = -1;
		row.find('th').each(function(colno) {
			currentCol++;
			if (footRowArr[index][currentCol].isDataSet == 'YES')
				currentCol++;

			var rowSpan = (typeof $(this).attr('rowspan') == 'undefined') ? 1 : $(this).attr('rowspan');
			var colSpan = (typeof $(this).attr('colspan') == 'undefined') ? 1 : $(this).attr('colspan');
			var col = $(this);
			var colText = "";
			if (typeof col.attr('aria-label') == 'undefined')
				colText = col.text().trim();
			else
				colText = col.attr('aria-label').substr(0, col.attr('aria-label').indexOf(':')).trim();

			if (rowSpan == 1 && colSpan == 1) {
				footRowArr[index][currentCol].isDataSet = 'YES';
				footRowArr[index][currentCol].data = colText;
			}

			var isRowIncrement = true;
			if (rowSpan > 1) {
				for ( var i = index; i < rowSpan; i++) {
					if (isRowIncrement) {
						footRowArr[i][currentCol].isDataSet = 'YES';
						footRowArr[i][currentCol].isMergedCell = 'YES';
						var rowLast = index + rowSpan - 1;
						footRowArr[i][currentCol].mergeRange = index + "," + rowLast + "," + currentCol + "," + currentCol;
						footRowArr[i][currentCol].data = colText;
					} else {
						footRowArr[i][currentCol].isDataSet = 'YES';
					}
					isRowIncrement = false;
				}
			}

			var isColIncrement = true;

			if (colSpan > 1) {
				for ( var j = 0; j < colSpan; j++) {
					if (isColIncrement) {
						footRowArr[index][currentCol].isMergedCell = 'YES';
						var colLast = currentCol + parseInt(colSpan) - 1;
						footRowArr[index][currentCol].mergeRange = index + "," + index + "," + currentCol + "," + colLast;
						footRowArr[index][currentCol].isDataSet = 'YES';
						footRowArr[index][currentCol].data = colText;
					} else {
						currentCol++;
						footRowArr[index][currentCol].isDataSet = 'YES';
					}
					isColIncrement = false;
				}
			}
		});
	});

	var cellObj;
	var rowArr = new Array();

	for ( var i = 0; i < noOfrow; i++) {
		var cellArr = new Array();
		for ( var j = 0; j < noOfcolumn; j++) {
			cellObj = {};
			cellObj['data'] = "";
			cellObj['isDataSet'] = "NO";
			cellObj['isMergedCell'] = "NO";
			cellObj['mergeRange'] = "0,0,0,0";
			cellArr.push(cellObj);
		}
		rowArr.push(cellArr);
	}

	function recursColAdd(rowArr, index, currentCol) {
		if (rowArr[index][currentCol].isDataSet == 'YES') {
			return recursColAdd(rowArr, index, currentCol+1);
		} else {
			return currentCol;
		}
	}

	$('#' + tableId + ' > thead:first >tr').each(function(index) {
		var row = $(this);
		var currentCol = -1;
		row.find('th').each(function(colno) {
			currentCol++;

			/*if (rowArr[index][currentCol].isDataSet == 'YES') {
				//alert(index+' - '+currentCol);
				currentCol++;
			}*/

			currentCol = recursColAdd(rowArr, index, currentCol);

			var rowSpan = (typeof $(this).attr('rowspan') == 'undefined') ? 1 : $(this).attr('rowspan');
			var colSpan = (typeof $(this).attr('colspan') == 'undefined') ? 1 : $(this).attr('colspan');
			var col = $(this);
			var colText = "";
			if (typeof col.attr('aria-label') == 'undefined')
				colText = col.text().trim();
			else
				colText = col.attr('aria-label').substr(0, col.attr('aria-label').indexOf(':')).trim();

			if(index == 1) {
				//alert(currentCol+' - '+rowSpan+' : '+colText);
			}

			if (rowSpan == 1 && colSpan == 1) {
				//alert(index+"-"+currentCol+" "+colText);
				rowArr[index][currentCol].isDataSet = 'YES';
				rowArr[index][currentCol].data = colText;
			}

			var isRowIncrement = true;
			if (rowSpan > 1) {
				for ( var i = index; i < rowSpan; i++) {
					if (isRowIncrement) {
						rowArr[i][currentCol].isDataSet = 'YES';
						rowArr[i][currentCol].isMergedCell = 'YES';
						var rowLast = index + rowSpan - 1;
						rowArr[i][currentCol].mergeRange = index + "," + rowLast + "," + currentCol + "," + currentCol;
						rowArr[i][currentCol].data = colText;
					} else {
						rowArr[i][currentCol].isDataSet = 'YES';
					}
					isRowIncrement = false;
				}
			}

			var isColIncrement = true;
			if (colSpan > 1) {
				for ( var j = 0; j < colSpan; j++) {
					if (isColIncrement) {
						rowArr[index][currentCol].isMergedCell = 'YES';
						var colLast = currentCol + parseInt(colSpan) - 1;
						rowArr[index][currentCol].mergeRange = index + "," + index + "," + currentCol + "," + colLast;
						rowArr[index][currentCol].isDataSet = 'YES';
						rowArr[index][currentCol].data = colText;
					} else {
						currentCol++;
						rowArr[index][currentCol].isDataSet = 'YES';
					}
					isColIncrement = false;
				}
			}
		/*	if(index >1 ){
				var temp = index-1;
				if(index == 2 && 'YES'==(rowArr[temp][1].isMergedCell))  {
					var coloumn = (noOfcolumn/2)-1;
					rowArr[temp][coloumn].isMergedCell = 'YES';
					rowArr[temp][coloumn].mergeRange = rowArr[temp][1].mergeRange;
					rowArr[temp][coloumn].isDataSet = 'YES';
					rowArr[temp][coloumn].data = rowArr[temp][1].data ;
					rowArr[temp][1].data = "";
					rowArr[temp][1].isMergedCell = 'NO';
					rowArr[temp][1].isDataSet = 'NO';
				}
			}*/
			
			
		});
	});

	for(i=0;i<rowArr.length;i++)
		for(j=0;j<rowArr[i].length;j++) {
			//alert(i+"-"+j+" "+rowArr[i][j].data);
		}

	var excelData = {};
	excelData['headerData'] = rowArr;
	excelData['noOfFootRow'] = noOfFootRow;
	if (noOfFootRow > 0) {
		excelData['footerData'] = footRowArr;
	}

	var rowArr = [];
	var t = $('#' + tableId + ' tbody:first tr').each(function() {
		var $row = $(this);
		var clumnLength = $row.find('td').length;
		var cellArr = new Array();
		for ( var j = 1; j < clumnLength + 1; j++) {
			cellObj = {};
			if ($row.find(':nth-child(' + j + ')').find('a').length > 0) {
				cellObj['data'] = $row.find(':nth-child(' + j + ')').find('a').text().trim();
			} else {
				cellObj['data'] = $row.find(':nth-child(' + j + ')').html().trim();
			}
		
			cellObj['dataType'] = $row.find(':nth-child(' + j + ')').attr('class');
			cellArr.push(cellObj);
		}
		rowArr.push(cellArr);
	});

	excelData['rowData'] = rowArr;
	excelData['noOfColumns'] = totalColumnsLength;
	excelData['otherData'] = ($('#other_data').html() == null) ? "" : $('#other_data').html();
	excelData['fromDate'] = ($('#start_date').val() == null) ? "null" : $('#start_date').val();
	excelData['endDate'] = ($('#end_date').val() == null) ? "null" : $('#end_date').val();
	excelData['curDate'] = ($('#cur_date').val() == null) ? "null" : $('#cur_date').val();
	excelData['curTime'] = ($('#cur_time').val() == null) ? "null" : $('#cur_time').val();
	excelData['mainHeader'] = $('#main-heading').text();
	$('#tableData').val(JSON.stringify(excelData));
	$('#excelForm').submit();
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/js/Export_Excel.js,v $'] = '$Id: Export_Excel.js,v 1.3 2016/10/31 09:45:31 neeraj Exp $';