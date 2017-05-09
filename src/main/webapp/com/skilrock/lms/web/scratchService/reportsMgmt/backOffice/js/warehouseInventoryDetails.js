var i18nOb = top.frames[0].i18Obj;

var mainDiv;
var req;
var which;

// Not Needed
function executeAjax(newUrl, div) {
	mainDiv = div;
	_resp = _ajaxCall(newUrl);
	if (_resp.result) {
		var resStr = _resp.data;
		// alert(resStr);
		if (resStr == 'NO_RET') {
			alert("NO " + tierMap['RETAILER'].toUpperCase() + " "
					+ i18nOb.prop('error.org.exist'));
			document.getElementById('ret').style.display = "none";
		} else {
			document.getElementById('ret').style.display = "block";
			createRetSelect(resStr);
		}
	} else {
		alert("Problem: ");
	}
}

// Not Needed
function removeAllOptions(newSelectbox) {
	for ( var i = newSelectbox.options.length - 1; i >= 0; i--)
		if (i > 1)
			newSelectbox.remove(i);
}

// Not Needed
function createRetSelect(responseData) {
	var newSelectbox = document.getElementById("ret_org_name");
	removeAllOptions(newSelectbox);
	var retList = new Array();
	retList = responseData.replace("[", "").replace("]", "").split(",");
	for (i = 0; i < retList.length; i++) {
		if (i != 0) {
			var key = retList[i].replace(" ", "");
			var val = retList[i].replace(" ", "");
		} else {
			var key = retList[i];
			var val = retList[i];
		}
		var opt = new Option(key, val);
		newSelectbox.options[i + 1] = opt;
	}
}

// Done
function getInventoryDetails(url, div) {
	mainDiv = div;
	var warehouseId = document.getElementById('warehouseId').value;
	var gameId = document.getElementById('gameId').value;
	document.getElementById("loadingDiv").innerHTML = '<img src="' + path + '/LMSImages/images/loadingAjax.gif"/>';
	var newUrl = url + "?gameId=" + gameId + "& warehouseId=" + warehouseId;
	_resp = _ajaxCall(newUrl);
	if (_resp.result) {
		var resStr = _resp.data;
		if (resStr == 'NONE') {
			alert(i18nOb.prop('error.no.books.exist'));
			document.getElementById('d2').innerHTML = i18nOb.prop('error.no.books.exist');
		} else {
			createInventoryDetailsDiv(resStr);
		}
		document.getElementById("loadingDiv").innerHTML = '';
	} else {
		alert("Problem: ");
	}
}

function createInventoryDetailsDiv(response) {
	//response = "{\"1\":{\"warehouseName\":\"22 Godam\",\"warehouseGameMap\":{\"17\":{\"gameName\":\"Six\",\"packBookList\":{\"666-002\":[\"666-002006\",\"666-002007\",\"666-002008\"],\"666-001\":[\"666-001006\",\"666-001009\"]}},\"16\":{\"gameName\":\"Seven\",\"packBookList\":{\"777-001\":[\"777-001002\",\"777-001007\",\"777-001008\",\"777-001010\"]}}}},\"2\":{\"warehouseName\":\"GGN Warehouse\",\"warehouseGameMap\":{\"17\":{\"gameName\":\"Six\",\"packBookList\":{\"666-002\":[\"666-002001\",\"666-002002\",\"666-002009\",\"666-002010\"],\"666-001\":[\"666-001004\",\"666-001007\",\"666-001010\"]}},\"16\":{\"gameName\":\"Seven\",\"packBookList\":{\"777-001\":[\"777-001009\"]}}}},\"3\":{\"warehouseName\":\"DDL WareHouse\",\"warehouseGameMap\":{\"17\":{\"gameName\":\"Six\",\"packBookList\":{\"666-002\":[\"666-002003\",\"666-002004\",\"666-002005\"],\"666-001\":[\"666-001005\",\"666-001008\"]}}}}}";

	var table = "";
	var innerTable = "";
	var isValue = 0;

	var json = (JSON.parse(response));
	document.getElementById('d2').innerHTML = "";
	$.each(json, function(key, value) {
		isValue = 1;
		table = "";
		table = "<table border='1' cellpadding='0' cellspacing='0' bordercolor='CCCCCC' width='90%'>";
		table += "<tr><td colspan=\"3\" align='center'><b> Warehouse : " + value.warehouseName + "</b></td></tr>";
		table += "<tr><th align='center'>Game Name</th><th align='center'>Pack Number</th><th align='center'>Number Of Books</th></tr>";
		$.each(value.warehouseGameMap, function(key, value) {
			table += "<tr><td align='center'>"+value.gameName+"</td>";
			table += "<td colspan = '2' style='border:0px;'>";
			innerTable = "";
			innerTable = "<table border = '1'; cellpadding='3' cellspacing='0' bordercolor='CCCCCC'b width='100%' >";
			$.each(value.packBookList, function(key, value) {
				innerTable += "<tr>";
				innerTable += "<td align='center' width = '249px' style='border:0px;border-right:1px solid #ccc;border-bottom:1px solid #ccc;'>" +key+ "</td>";
				innerTable += "<td align='center' style='border:0px;border-bottom:1px solid #ccc;'>" +value.length+ "</td>";
				innerTable += "</tr>";
			});
			innerTable += "</table>";
			table += innerTable;
			table += "</td>";
			table += "</tr>";
		});
		table += "</table>";
		document.getElementById('d2').innerHTML += table + "<br />" + "<br />";
	});
	if(isValue == 0) {
		document.getElementById('d2').innerHTML = "Data Not Available";
	}
	return;
}

// Done
function generateSeriesList(first, last) {
	// alert(" first "+first+" last = "+last);
	var list = new Array();
	var afirst = first.split("-");
	var length = afirst[1].length;
	var blast = last.split("-");
	// alert(" afirst "+afirst[1]+" blast = "+blast[1]);
	var firstNumber = parseFloat(afirst[1]);
	var lastNumber = parseFloat(blast[1]);
	// alert(" first No. "+firstNumber+" lastNumber = "+lastNumber);
	var i = 0;
	while (firstNumber <= lastNumber) {
		var book = afirst[0] + "-" + completeBookNbr(firstNumber, length);
		list[i] = book;
		firstNumber += 1;
		i++;
	}

	return list;
}

// Done
function completeBookNbr(number, length) {
	var num = number + "";
	var zeroLength = length - num.length;
	var totalZeros = "";
	while (zeroLength > 0) {
		totalZeros = totalZeros + "0";
		zeroLength -= 1;
	}
	return (totalZeros + num);
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/reportsMgmt/backOffice/js/warehouseInventoryDetails.js,v $'] = '$Id: warehouseInventoryDetails.js,v 1.3 2016/10/31 09:47:02 neeraj Exp $';