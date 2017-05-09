function clearValues(eleByName) {
	var eleByNames = _id.n(eleByName);
	for ( var i = 0; i < eleByNames.length; i++) {
		if (eleByName == 'walletname') {
			eleByNames[i].checked = false;
		} else if (eleByName == 'error') {
			eleByNames[i].innerHTML = "";
		} else {
			eleByNames[i].value = "";
		}
	}
}

function disableNames(eleByName) {
	var eleByNames = _id.n(eleByName);
	for ( var i = 0; i < eleByNames.length; i++) {
		eleByNames[i].style.display = 'none';
	}
}

function displayElements(elementId) {
	_id.o(elementId).style.display = 'block';
}

function clearAll() {
	clearValues('code');
	clearValues('value');
	clearValues('walletname');
	clearValues('error');
	clearValues('phone');
}

function disableAll(index) {
	disableNames('code');
	disableNames('update');
	disableNames('value');
	disableNames('wallet');
	disableNames('phone');
	clearAll();
	displayElements('update' + index);
	displayElements('value' + index);
	displayElements('wallet' + index);
	displayElements('phone' + index);
}

function check(val) {
	var index = val.replace(/[^0-9]+/ig, "");
	loadWallet(index);
	if (val.indexOf("withdraw") != -1) {
		disableAll(index);
		displayElements('code' + index);
	}
	if (val.indexOf("deposit") != -1) {
		disableAll(index);
	}
}

function loadWallet(index) {
	var _resp1 = _ajaxCall("olaWalletDetails.action");
	var response1 = _resp1.data.split("Nxt");
	var td = '';
	var i = 0;
	while (i < response1.length) {
		td += '<td>' + response1[i].split(":")[1]
				+ '<input type="radio" name="walletname" id=\"walletname' + i
				+ '\" value=\"' + response1[i] + '\"/></td>';
		i += 1;
	}
	_id.o('wallet' + index).innerHTML = td;
}

function fetchActData(val) {
	_id.o("reportDiv").innerHTML = "";
	if (val == 'PLAYERS') {
		_id.o("reportDiv").innerHTML = "";
		var _resp = _ajaxCall("getplayerList.action");
		var response = _resp.data.split(",");
		var table = '<table width="115%" border="1" cellpadding="2" cellspacing="0" bordercolor="#CCCCCC" align="center" style="margin-left:-70px;"><tr><td><b>SNo.</b></td><td><b>Player Name</b></td><td><b>Select For The Type Of Transaction</b></td><td><b>Select Wallet</b></td><td><b>Deposit/WithDraw Amount</b></td><td><b>Phone Number</b></td><td><b>Received Code</b></td><td><b>Update</b></td><td><b>Message</b></td></tr>';
		for ( var i = 0; i < response.length; i++) {
			table += '<tr><td>'
					+ (parseInt(i) + 1)
					+ '</td><td><input type="text" value=\"'
					+ response[i]
					+ '\" id=\"name'
					+ i
					+ '\" size="12" readOnly = true/></td><td>Deposit<input type="radio" id=\"deposit'
					+ i
					+ '\" value=\"deposit'
					+ i
					+ '\" name=\"pick" onclick="check(this.value)" />WithDraw<input type="radio" id=\"withdraw'
					+ i
					+ '\" value=\"withdraw'
					+ i
					+ '\" name=\"pick" onclick="check(this.value)" /></td><td><div id=\"wallet'
					+ i
					+ '\" name="wallet"></div></td><td><input type="text" id=\"value'
					+ i
					+ '\" name="value" style="display:none;" size="8" onkeypress="return onlyDecimal(this)"/></td><td><input type="text" id=\"phone'
					+ i
					+ '\" name="phone" style="display:none;" size="10" onkeypress="return onlyNum(this)"/></td><td><input type="text" id=\"code'
					+ i
					+ '\" name="code" style="display:none;" size="6" onkeypress="return onlyNum(this)"/></td><td><input type="button" value="update" name="update" id=\"update'
					+ i
					+ '\" style="display:none;" onclick="updateDetails(this.id)"/></td><td><div id=\"error'
					+ i + '\" name="error"></div></td></tr>';
		}
		table += '<table>';
		_id.i("reportDiv", table);
		_id.blk("reportDiv");
	}
}

function validateData(walletType, wallet, amount, code) {
	var message = "success";
	if (typeof wallet == "undefined") {
		message = "select wallet";
	}
	if (amount == "" || amount < 0) {
		message = "enter Amount properly";
	}
	if (walletType == 'withdraw' && code == "") {
		message = "enter code";
	}
	return message;

}

function updateDetails(val) {
	var index = val.replace(/[^0-9]+/ig, "");
	var flag = true;
	var walletType;
	var wallet;
	var message;
	var amount = _id.o('value' + index).value;
	var code = _id.o('code' + index).value;
	var name = _id.o('name' + index).value;
	var phone = _id.o('phone' + index).value;
	var wallets = _id.n('walletname');
	for ( var i = 0; i < wallets.length; i++) {
		if (wallets[i].checked) {
			wallet = wallets[i].value;
		}
	}
	if (_id.o('withdraw' + index).checked) {
		walletType = 'withdraw';
		message = validateData(walletType, wallet, amount, code);

	} else if (_id.o('deposit' + index).checked) {
		walletType = 'deposit';
		message = validateData(walletType, wallet, amount, "");
	}
	if (message == 'success') {
		var url = 'updatewallet.action?name=' + name + '&walletType='
				+ walletType + '&wallet=' + wallet + '&amount=' + amount
				+ '&phone=' + phone + '&code=' + code;
		disableAll(index);
		var _resp = _ajaxCall(url);
		_id.o("error" + index).innerHTML = _resp.data;
	} else {
		_id.o("error" + index).innerHTML = message;
	}

}



parent.frames[0].Version['$Source: /rep/LMS_Mgmt/WebRoot/com/skilrock/lms/web/ola/js/playersDashboard.js,v $'] = '$Id: playersDashboard.js,v 1.2 2016/10/31 09:45:45 neeraj Exp $';