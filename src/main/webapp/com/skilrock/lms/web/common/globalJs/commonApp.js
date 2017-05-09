/**
 * Common application JS : Common java script functionality 
 * 
 */
var i18nObj = parent.frames[0].i18Obj;
var commonApp = {};
var dLanguage = "";
commonApp.ui = {
	/**
	 * Data Table Common Variables
	 */
	DataTableCommon : {
		element : ".common_data_table",
		init : function() {
			dLanguage =  {
				"sProcessing":i18nObj.prop('label.processing'),
			    "sLengthMenu":i18nObj.prop('label.show.menu.entry'),
			    "sZeroRecords":i18nObj.prop('label.no.match.record'),
			    "sInfo": i18nObj.prop('label.showing.entries'),
			    "sInfoEmpty":i18nObj.prop('label.showing.of.entries'),
			    "sInfoFiltered":i18nObj.prop('label.filter.from.tot.entry'),
			    "sInfoPostFix":"",
			    "sSearch":i18nObj.prop('label.search'),
			    "sUrl":"",
			    "oPaginate":{
			        "sFirst":i18nObj.prop('label.first'),
			        "sPrevious":i18nObj.prop('label.previous'),
			        "sNext":i18nObj.prop('label.next'),
			        "sLast":i18nObj.prop('label.sLast')
			     }
			};
		}
	},
	/**
	 * Data Table
	 */
	DataTable : {
		element : "#dTable",
		init : function() {
		var colCount = parseInt($("#colCount").val());
		var rowCount = parseInt($("#rowCount").val());
		var leaveCount = parseInt($("#leaveCount").val());
			$('#dTable').dataTable( {
				"oLanguage":dLanguage,
				"bJQueryUI" : false,
				"sPaginationType" : "full_numbers",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"fnFooterCallback" : function(nRow, aaData, iStart, iEnd, aiDisplay) {
				//alert("nRow"+nRow+"  aaData "+aaData +"   iStart"+iStart+"   iEnd"+iEnd+"  aiDisplay "+aiDisplay );
				if(!isNaN(colCount)) {
						var queryArr = [];
						//alert("Row Count : "+rowCount+"colCount : "+colCount);
						var rowTot= rowCount+colCount+leaveCount;
						for ( var k = colCount; k < rowTot; k++) {
							var iPageMarket = 0;
							for ( var i = iStart; i < iEnd; i++) {
								var id1 = aaData[aiDisplay[i]][k];
								var isN = parseFloat(id1.replace(/,/g,''));
								if (!isNaN(isN)) {
									iPageMarket += isN;
								}
							}
							queryArr.push(iPageMarket);
						}
						var nCells = nRow.getElementsByTagName('th');
						for ( var k = 1; k <= rowCount; k++) {
							nCells[k].innerHTML = queryArr[k-1].toFixed(2);;
						}
					}
				}
			});
		}
	},
	DataTable : {
		element : "#dTableNew",
		init : function() {
		var colCount = parseInt($("#colCount").val());
		var rowCount = parseInt($("#rowCount").val());
		var leaveCount = parseInt($("#leaveCount").val());
			$('#dTableNew').dataTable( {
				"oLanguage":dLanguage,
				"bJQueryUI" : false,
				"sPaginationType" : "full_numbers",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"fnFooterCallback" : function(nRow, aaData, iStart, iEnd, aiDisplay) {
				//alert("nRow"+nRow+"  aaData "+aaData +"   iStart"+iStart+"   iEnd"+iEnd+"  aiDisplay "+aiDisplay );
				if(!isNaN(colCount)) {
						var queryArr = [];
						//alert("Row Count : "+rowCount+"colCount : "+colCount);
						var rowTot= rowCount+colCount+leaveCount;
						for ( var k = colCount; k < rowTot; k++) {
							var iPageMarket = 0;
							for ( var i = iStart; i < iEnd; i++) {
								var id1 = aaData[aiDisplay[i]][k];
								var isN = parseFloat(id1.replace(/,/g,''));
								if (!isNaN(isN)) {
									iPageMarket += isN;
								}
							}
							queryArr.push(iPageMarket);
						}
						var nCells = nRow.getElementsByTagName('th');
						for ( var k = 1; k <= rowCount; k++) {
							nCells[k].innerHTML = queryArr[k-1].toFixed(2);;
						}
					}
				}
			});
		}
	},
	/**
	 * Net Sale Winning report datatable 
	 */
	NetSaleWinDataTable : { 
		element : "#net_sale_dTable",
		init : function() {
			var columnsLength = $('#net_sale_dTable tr:eq(1) > td').length;
			var aoColumns = [];
			if (columnsLength == 9) {
				aoColumns = null,null, 
						   { "sType": "numeric-comma" },
				           { "sType": "numeric-comma" },
				           { "sType": "numeric-comma" },
				           { "sType": "numeric-comma" },
				           { "sType": "numeric-comma" },
				           { "sType": "numeric-comma" },
				           null;
			} else {
				aoColumns = null, 
						   { "sType": "numeric-comma" },
			               { "sType": "numeric-comma" },
			               { "sType": "numeric-comma" },
			               { "sType": "numeric-comma" },
			               { "sType": "numeric-comma" },
			               { "sType": "numeric-comma" },
			               null;
			}
			var colCount = parseInt($("#colCount").val());
			var rowCount = parseInt($("#rowCount").val());
			$('#net_sale_dTable').dataTable({
				"oLanguage":dLanguage,
				"bJQueryUI" : false,
				"aoColumns": aoColumns,
				"sPaginationType" : "full_numbers",
				"aLengthMenu" : [ [ 10, 25, 50, -1 ], [ 10, 25, 50, "All" ] ],
				"fnFooterCallback" : function(nRow, aaData, iStart, iEnd, aiDisplay) {
					//alert("nRow"+nRow+"  aaData "+aaData +"   iStart"+iStart+"   iEnd"+iEnd+"  aiDisplay "+aiDisplay );
					var queryArr = [];
					//alert("Row Count : "+rowCount+"colCount : "+colCount);
					var rowTot= rowCount+colCount+1;
					for ( var k = colCount; k < rowTot; k++) {
						var iPageMarket = 0;
						for ( var i = iStart; i < iEnd; i++) {
							var id1 = aaData[aiDisplay[i]][k];
							var isN = parseFloat(id1.replace(/,/g,''));
							if (!isNaN(isN)) {
								iPageMarket += isN;
							}
						}
						queryArr.push(iPageMarket);
					}
					var nCells = nRow.getElementsByTagName('th');
					for ( var k = 1; k <= rowCount; k++) {
						nCells[k].innerHTML = queryArr[k-1].toFixed(2);;
					}
				} 
			});
		}
	},
	/**
	 * Export to Excel
	 */
	ExpExcel : {
		element : "#expExcel",
		init : function() {
			$("#expExcel").on("click", function(e) {
				exportToExcel('dTable');
			});
		}
	},
	ExcelScratch : {
		element : "#expExcelScratch",
		init : function() {
			$("#expExcelScratch").on("click", function(e) {
				exportToExcel('dTableNew');
			});
		}
	},
	/**
	 * Net Sale Win Export to Excel
	 */
	nSaleWinExpExcel : {
		element : "#nSaleWinExpExcel",
		init : function() {
			$("#nSaleWinExpExcel").on("click", function(e) {
				exportToExcel('net_sale_dTable');
			});
		}
	},
	VSSaleReconcExpExcel : {
		element : "#vsSaleReconExpExcel",
		init : function() {
			$("#vsSaleReconExpExcel").on("click", function(e) {
				exportToExcel('dTableNew');
			});
		}
	},
	IntakeReportExpExcel : {
		element : "#intakeReportExpExcel",
		init : function() {
			$("#intakeReportExpExcel").on("click", function(e) {
				exportToExcel('dTableNew');
			});
		}
	},
	/**
	 * Export to PDF 
	 */
	ExpPDF : {
		element : "#expPdf",
		init : function() {
			$("#expPdf").on("click", function(e) {
				exportToPDF('dTable');
			});
		}
	},
	/**
	 * Net Sale Win Export to PDF 
	 */
	nSaleWinExpPDF : {
		element : "#netSaleWinExpPdf",
		init : function() {
			$("#netSaleWinExpPdf").on("click", function(e) {
				exportToPDF('net_sale_dTable');
			});
		}
	},
	/**
	 * Common Utility
	 */
	utility : {
		element : 'body',
		init : function() {
		}
	}

};

commonApp.init = function($) {
	$.each(commonApp.ui, function(n, v) {
		if ($(v.element).length > 0 && typeof v.init === "function") {
			commonApp.ui[n].element = $(v.element);
			v.init();
		}
	});
};


parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/common/globalJs/commonApp.js,v $'] = '$Id: commonApp.js,v 1.3 2016/10/31 09:46:50 neeraj Exp $';

