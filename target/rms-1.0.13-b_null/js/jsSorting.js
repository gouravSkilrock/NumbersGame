var i18nObj = parent.frames[0].i18Obj;
/* You can change these values */
//var image_path = "LMSImages/images/";
//var image_up = "arrow-up.gif";
//var image_down = "arrow-down.gif";
//var image_none = "arrow-none.gif";
var europeandate = true;
var alternate_row_colors = true;

/* Don't change anything below this unless you know what you're doing */
addEvent(window, "load", sortables_init);

var SORT_COLUMN_INDEX;
var thead = false;
var start = 0;
var end=0;
function sortables_init() {
	// Find all tables with class sortable and make them sortable
	if (!document.getElementsByTagName) return;
	tbls = document.getElementsByTagName("table");
	for (ti=0;ti<tbls.length;ti++) {
		thisTbl = tbls[ti];
		for (var j=0;j<thisTbl.rows.length;j++) {
			if((thisTbl.rows[j].className).match("startSortable")){
				thisTbl.className = "sortable";
				thisTbl.id = "sortable"+ti;
				break;
			}
		}
		for (var j=thisTbl.rows.length-1;j>0;j--) {
			var r = thisTbl.rows[j];
			if(r.innerHTML.indexOf('Total')!=-1 ||r.innerHTML.indexOf('TOTAL')!=-1 ){
				r.className="sortbottom";
			}else{
				break;
			}
		}
		
		if (((' '+thisTbl.className+' ').indexOf("sortable") != -1) && (thisTbl.id)) {
			ts_makeSortable(thisTbl);
		}
	}
}

function ts_makeSortable(t) {
	var createNewTR = true;
	for (var j=0;j<t.rows.length;j++) {
		if(t.rows[j].id=='sortRow'){
			createNewTR = false;
		}
		if((t.rows[j].className).match("startSortable")){
			start=j;break;
		}
	}
	end=t.rows.length;
	for (var j=0;j<t.rows.length;j++) {
		if((t.rows[j].className).match("sortbottom")){
			end=j;break;
		}
	}
	var startRow = t.rows[start];
	if(createNewTR){
	var newTR = t.insertRow(start);
	newTR.setAttribute("id","sortRow");
	start=start+1;
	for (var j=0;j<startRow.cells.length;j++) {
		var newTD = document.createElement('td');
		newTD.innerHTML = '<a href="#" class="sortheader" onclick="ts_resortTable(this, '+(j)+','+start+','+end+');return false;" style="text-align: center;white-space: nowrap;"><span class="sortarrow"><center>'+i18nObj.prop('label.sort')+'</center></span></a>';
		newTR.appendChild (newTD);
	}
	}
	if (alternate_row_colors) {
		alternate(t);
	}
}

function ts_getInnerText(el) {
	if (typeof el == "string") return el;
	if (typeof el == "undefined") { return el };
	if (el.innerText) return el.innerText;	// Not needed but it is faster
	var str = "";
	
	var cs = el.childNodes;
	var l = cs.length;
	for (var i = 0; i < l; i++) {
		switch (cs[i].nodeType) {
			case 1: // ELEMENT_NODE
				str += ts_getInnerText(cs[i]);
				break;
			case 3:	// TEXT_NODE
				str += cs[i].nodeValue;
				break;
		}
	}
	return str;
}

function ts_resortTable(lnk, clid,start,end) {
	 //alert(lnk+'***'+clid+"*******"+start);
	start =start;
	end =end;
	var span;
	for (var ci=0;ci<lnk.childNodes.length;ci++) {
		if (lnk.childNodes[ci].tagName && (lnk.childNodes[ci].tagName.toLowerCase() == 'span')) span = lnk.childNodes[ci];
	}
	var spantext = ts_getInnerText(span);
	var td = lnk.parentNode;
	var column = clid;
	var t = getParent(td,'TABLE');
	// Work out a type for the column
	if (t.rows.length <= 1) return;
	var itm = "";
	var i = 0;
	while (itm == "" && i < t.tBodies[0].rows.length) {
		var itm = ts_getInnerText(t.tBodies[0].rows[start].cells[column]);
		//alert(itm+'##'+t.tBodies[0].rows[start].cells[column]+'))))'+column);
		itm = trim(itm);
		if (itm.substr(0,4) == "<!--" || itm.length == 0) {
			itm = "";
		}
		i++;
			}
	if (itm == "") return; 
	sortfn = ts_sort_caseinsensitive;
	if (itm.match(/^\d\d[\/\.-][a-zA-z][a-zA-Z][a-zA-Z][\/\.-]\d\d\d\d$/)) sortfn = ts_sort_date;
	if (((itm.split('-')).length==3)||((itm.split('/')).length==3)) sortfn = ts_sort_date;
	if (itm.match(/^-?[$?]\d/)) sortfn = ts_sort_numeric;
	if (itm.match(/^-?(\d+[,\.]?)+(E[-+][\d]+)?%?$/)) sortfn = ts_sort_numeric;
	if (itm.match("^[_0-9\\+]+(\\:[0-9]+)+(\\:[0-9]+)$")) sortfn = ts_sort_numeric; 
	if (itm.match())
	SORT_COLUMN_INDEX = column;
	var firstRow = new Array();
	var newRows = new Array();
	var endRows = new Array();
	for (k=0;k<t.tBodies.length;k++) {
		for (i=0;i<t.tBodies[k].rows[0].length;i++) { 
			firstRow[i] = t.tBodies[k].rows[0][i]; 
		}
	}
	for (k=0;k<t.tBodies.length;k++) {
		if (!thead) {
			// Skip the first row
			//alert(start+'*****'+end+"********"+t.tBodies[k].rows.length);
			for (j=start;j<=end;j++) { //alert('**'+t.tBodies[k].rows[j]);
				newRows[j-start] = t.tBodies[k].rows[j];
			}
		} else {
			// Do NOT skip the first row
			for (j=start;j<=end;j++) { 
				newRows[j-(start-1)] = t.tBodies[k].rows[j];
			}
		}
	}
	for (k=0;k<t.tBodies.length;k++) {		
		//	alert(start+'*****'+end+"********"+t.tBodies[k].rows.length);
			for (j=end;j<=t.tBodies[k].rows.length-1;j++) { //alert('**'+t.tBodies[k].rows[j]);
				endRows[j-end] = t.tBodies[k].rows[j];
			}
	}
	newRows.sort(sortfn);
		if (span.getAttribute("sortdir") == 'down') {
				ARROW = '<center>'+i18nObj.prop('label.sort.asc')+'</center>';
				newRows.reverse();
				span.setAttribute('sortdir','up');
		} else {
				ARROW = '<center>'+i18nObj.prop('label.sort.desc')+'</center>';
				span.setAttribute('sortdir','down');
		} 
			
    // We appendChild rows that already exist to the tbody, so it moves them
	// rather than creating new ones
    // don't do sortbottom rows
	 for (i=0; i<newRows.length; i++) {
	 	if (!newRows[i].className || (newRows[i].className && (newRows[i].className.indexOf('sortbottom') == -1))) {
			t.tBodies[0].appendChild(newRows[i]);
		}
	}
    
   // do sortbottom rows only
//    for (i=0; i<newRows.length; i++) {
//    	
//		if (newRows[i].className && (newRows[i].className.indexOf('sortbottom') != -1)) 
//			t.tBodies[0].appendChild(newRows[i]);
//	}
    for (i=0; i<endRows.length; i++) {
    	if (endRows[i].className && (endRows[i].className.indexOf('sortbottom') != -1)) {
			t.tBodies[0].appendChild(endRows[i]);
		}
	}
	// Delete any other arrows there may be showing
	var allspans = document.getElementsByTagName("span");
	for (var ci=0;ci<allspans.length;ci++) {//alert(allspans[ci].className+' ** '+allspans[ci].innerHTML);
		if (allspans[ci].className == 'sortarrow') {//alert(allspans[ci].innerHTML +' in'+ getParent(allspans[ci],"table")+" ***"+getParent(lnk,"table"));
			if (getParent(allspans[ci],"table") == getParent(lnk,"table")) { 
			// in the same table as us?
				allspans[ci].innerHTML = '<center>'+i18nObj.prop('label.sort')+'</center>';
			}
		}
	}		
	span.innerHTML = ARROW;
	alternate(t);
}

function getParent(el, pTagName) {
	if (el == null) {
		return null;
	} else if (el.nodeType == 1 && el.tagName.toLowerCase() == pTagName.toLowerCase()) {
		return el;
	} else {
		return getParent(el.parentNode, pTagName);
	}
}

function sort_date(date) {	
	// y2k notes: two digit years less than 50 are treated as 20XX, greater than
	// 50 are treated as 19XX
	dt = "00000000";
	if (date.length == 11) {
		mtstr = date.substr(3,3);
		mtstr = mtstr.toLowerCase();
		switch(mtstr) {
			case "jan": var mt = "01"; break;
			case "feb": var mt = "02"; break;
			case "mar": var mt = "03"; break;
			case "apr": var mt = "04"; break;
			case "may": var mt = "05"; break;
			case "jun": var mt = "06"; break;
			case "jul": var mt = "07"; break;
			case "aug": var mt = "08"; break;
			case "sep": var mt = "09"; break;
			case "oct": var mt = "10"; break;
			case "nov": var mt = "11"; break;
			case "dec": var mt = "12"; break;
			// default: var mt = "00";
		}
		dt = date.substr(7,4)+mt+date.substr(0,2);
		return dt;
	} else if (date.length == 10) {
		if (europeandate == false) {
			dt = date.substr(6,4)+date.substr(0,2)+date.substr(3,2);
			return dt;
		} else {
			dt = date.substr(6,4)+date.substr(3,2)+date.substr(0,2);
			return dt;
		}
	} else if (date.length == 8) {
		yr = date.substr(6,2);
		if (parseInt(yr) < 50) { 
			yr = '20'+yr; 
		} else { 
			yr = '19'+yr; 
		}
		if (europeandate == true) {
			dt = yr+date.substr(3,2)+date.substr(0,2);
			return dt;
		} else {
			dt = yr+date.substr(0,2)+date.substr(3,2);
			return dt;
		}
	}else if (date.length == 19) { // Checks the length of date and then sort // By Mandeep For Timestamp
		if (europeandate == false) {
			dt = date.substr(6,13)+date.substr(0,2)+date.substr(3,2);
			return dt;
		} else {
			dt = date.substr(6,13)+date.substr(3,2)+date.substr(0,2);
			return dt;
		}
	}
	return dt;
}

function ts_sort_date(a,b) {
	if(a.cells.length==b.cells.length){
	dt1 = sort_date(ts_getInnerText(a.cells[SORT_COLUMN_INDEX]));
	dt2 = sort_date(ts_getInnerText(b.cells[SORT_COLUMN_INDEX]));
	
	if (dt1==dt2) {
		return 0;
	}
	if (dt1<dt2) { 
		return -1;
	}
	return 1;
	}
}
function ts_sort_numeric(a,b) {
	if(a.cells.length==b.cells.length){
	var aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
	aa = clean_num(aa);
	var bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
	bb = clean_num(bb);
	return compare_numeric(aa,bb);
	}
}
function compare_numeric(a,b) {
	var a = parseFloat(a);
	a = (isNaN(a) ? 0 : a);
	var b = parseFloat(b);
	b = (isNaN(b) ? 0 : b);
	return a - b;
}
function ts_sort_caseinsensitive(a,b) {
	if(a.cells.length==b.cells.length){
		if(!(typeof ts_getInnerText(a.cells[SORT_COLUMN_INDEX]) == 'undefined')){
			aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]).toLowerCase();
			bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]).toLowerCase();
			if (aa==bb) {
				return 0;
			}
			if (aa<bb) {
				return -1;
			}
			return 1;
		
	}
}
}	
function ts_sort_default(a,b) {
	if(a.cells.length==b.cells.length){
	aa = ts_getInnerText(a.cells[SORT_COLUMN_INDEX]);
	bb = ts_getInnerText(b.cells[SORT_COLUMN_INDEX]);
	if (aa==bb) {
		return 0;
	}
	if (aa<bb) {
		return -1;
	}
	return 1;
	}
}
function addEvent(elm, evType, fn, useCapture)
// addEvent and removeEvent
// cross-browser event handling for IE5+, NS6 and Mozilla
// By Scott Andrew
{
	if (elm.addEventListener){
		elm.addEventListener(evType, fn, useCapture);
		return true;
	} else if (elm.attachEvent){
		var r = elm.attachEvent("on"+evType, fn);
		return r;
	} else {
		alert(i18nObj.prop('error.handler.could.not.remove'));
	}
}
function clean_num(str) {
	if(!(typeof str == 'undefined')){
		str = str.replace(new RegExp(/[^-?0-9.]/g),"");
		return str;
	}
}
function trim(s) {
	return s.replace(/^\s+|\s+$/g, "");
}
function alternate(table) {
	// Take object table and get all it's tbodies.
	var tableBodies = table.getElementsByTagName("tbody");
	// Loop through these tbodies
	for (var i = 0; i < tableBodies.length; i++) {
		// Take the tbody, and get all it's rows
		var tableRows = tableBodies[i].getElementsByTagName("tr");
		// Loop through these rows
		// Start at 1 because we want to leave the heading row untouched
		for (var j = start-1; j < tableRows.length; j++) {
			// Check if j is even, and apply classes for both possible results
			if ( (j % 2) == 0  ) {
				if ( !(tableRows[j].className.indexOf('odd') == -1) ) {
					tableRows[j].className = tableRows[j].className.replace('odd', 'even');
				} else {
					if ( tableRows[j].className.indexOf('even') == -1 ) {
						tableRows[j].className += " even";
					}
				}
			} else {
				if ( !(tableRows[j].className.indexOf('even') == -1) ) {
					tableRows[j].className = tableRows[j].className.replace('even', 'odd');
				} else {
					if ( tableRows[j].className.indexOf('odd') == -1 ) {
						tableRows[j].className += " odd";
					}
				}
			} 
		}
	}
}

parent.frames[0].Version["$Source: /rep/LMS_Mgmt/WebRoot/js/jsSorting.js,v $"] = "$Id: jsSorting.js,v 1.3 2016/10/31 09:45:31 neeraj Exp $";

