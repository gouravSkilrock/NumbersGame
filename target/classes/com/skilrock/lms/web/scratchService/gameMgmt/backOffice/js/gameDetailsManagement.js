var i18nObj =parent.frames[0].i18Obj;
function getGameList(tValue) {

	var games = document.getElementById('game_name');
	var newSelectbox = games;
	var count = 1;
	newSelectbox.options.length = 1;
	var ajaxList = document.getElementById('game_name_hid');
	if (tValue == '-1') {
		for (i = 0; i < ajaxList.options.length; i++) {
			if (i != 0) {
				var key = ajaxList[i].text;
				var val = ajaxList[i].value
				var opt = new Option(key, val);
				newSelectbox.options[count] = opt;
				count++;
			}
		}
	} else {
		for (i = 0; i < ajaxList.options.length; i++) {
			if (i != 0) {
				var key = ajaxList[i].text;
				var val = ajaxList[i].value

				if (val.match(tValue)) {
					var opt = new Option(key, val);
					newSelectbox.options[count] = opt;
					count++;

				}
			}
		}
	}
}

function validateGameName() {
	var games = document.getElementById('game_name');
	var value = games.selectedIndex;
	if (value == '0') {
		document.getElementById('game_name_error').innerHTML = "<font color = 'red'>"
				+ i18nObj.prop('error.entr.game.name') + "</font>";
		return false;
	}
	document.getElementById('game_name_error').innerHTML = "";
	return true;
}

parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/scratchService/gameMgmt/backOffice/js/gameDetailsManagement.js,v $'] = '$Id: gameDetailsManagement.js,v 1.3 2016/10/31 09:46:47 neeraj Exp $';
