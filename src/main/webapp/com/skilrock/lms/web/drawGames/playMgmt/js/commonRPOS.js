function setAppData(buyData) {
	if (typeof parent.retName != "undefined") {
		buyData = buyData.replace(/orgName=/gi, ("sampleStatus="
				+ parent.sampleStatus + "|orgName="));
		parent.setAppData(buyData);
	} else {
		buyData = buyData.replace(/orgName=/gi, ("sampleStatus="
				+ parent.sampleStatus + "|orgName="));
		adjustAppSize(buyData.substring(buyData.indexOf("ctr=") + 4,
				buyData.length));
		parent.document.applets[0].showStatus(buyData);
	}
}
function adjustAppSize(height) {
	parent.document.applets[0].height = height;
}

function setAppDataForVoucher(buyData) {
	adjustAppSize1(buyData.substring(buyData.indexOf("ctr=") + 4,
			buyData.length));
	document.applets[0].showStatus(buyData);
}

function adjustAppSize1(height) {
	document.applets[0].height = height;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/drawGames/playMgmt/js/commonRPOS.js,v $'] = '$Id: commonRPOS.js,v 1.3 2016/10/31 09:46:17 neeraj Exp $';
