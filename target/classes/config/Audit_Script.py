import re
import MySQLdb
import datetime;
import os.path;

reqArray = [];
resArray = [];

reqMap = {};
reqMapSle = {};
reqMapList = [];
reqMapListSle = [];
resMap = {};
resMapSle = {};
resMapList = [];
resMapListSle = [];

isReqFirst = 1;
isReqFirstSle = 1;
isResFirst = 1;
isResFirstSle = 1;

lastUpdateDate = 0;
dateList = [];
logPath = None;

connection = MySQLdb.connect (host = "localhost", user = "root", passwd = "root", db = "GHANA_5_LMS");
cursor = connection.cursor ();
cursor.execute ("select ifnull(date_format(last_success_time, '%Y-%m-%d'),0) date from st_lms_scheduler_master where dev_name = 'DAILY_AUDIT_ACTIVITY_SCRIPT';");
#cursor.execute ("select ifnull(date_format(last_success_time, '%Y-%m-%d'),0) date from st_lms_scheduler_master where dev_name = 'Agent_Auto_Block_SCHEDULER';");

data = cursor.fetchall();

#print "Fetch Data is %s" % (data);

for row in data:
	if row[0] == '0':
		lastUpdateDate = datetime.datetime.today();
	else:
		lastUpdateDate = row[0];

#print lastUpdateDate;

cursor.execute("""select selected_date from (select adddate('1970-01-01',t4*10000 + t3*1000 + t2*100 + t1*10 + t0) selected_date from (select 0 t0 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t0, (select 0 t1 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t1, (select 0 t2 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t2, (select 0 t3 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t3, (select 0 t4 union select 1 union select 2 union select 3 union select 4 union select 5 union select 6 union select 7 union select 8 union select 9) t4) v where selected_date between %s and now()""", (lastUpdateDate));

data = cursor.fetchall();
for row in data :
	dateList.append(row[0]);

cursor.execute("SELECT VALUE FROM st_lms_property_master WHERE property_dev_name = 'JBOSS_LOG_PATH'");
data = cursor.fetchone();
for row in data:
	logPath = row;

cursor.close();
connection.close();

iCount = 0;

#print "List Size is %s", len(dateList);
#print dateList;

for date in dateList:
	if iCount < len(dateList) - 1:
		iCount += 1;
		#print "iCount is %s", iCount;
		#logFileName = "/home/jboss-4.2.2.GA/server/default/log/server.log."+date;
		logFileName = str(logPath)+"/server.log."+date;
		if (not os.path.isfile(logFileName)):
			continue;

		reqMap = {};
		reqMapSle = {};
		reqMapList = [];
		reqMapListSle = [];
		resMap = {};
		resMapSle = {};
		resMapList = [];
		resMapListSle = [];

		#logFileName = "/root/server.log";
		#print logFileName;
		serverLogs = open(logFileName, "r");
		for line in serverLogs:
			if re.search('REQUEST_AUDIT_TRAIL', line):
				#print line;
				list = line.split('REQUEST_AUDIT_TRAIL-');
				reqArray = list[1].split('#');
				serviceName = reqArray[5].split('=')[1];
				#print serviceName;

				if(reqArray[6].split('=')[1] == 'TERMINAL'):
					continue;

				if(serviceName == 'MGMT' or serviceName == 'DG' or serviceName == 'HOME'):
					if reqArray[0] != 0:
						if (reqArray[0] == 1 or reqArray[0] in reqMap and isReqFirst == 0):
							reqMapList.append(reqMap);
							reqMap = {};
							userNameArray = reqArray[1].split('=');
							if userNameArray[1] and userNameArray[1] != 'null':
								reqMap[reqArray[0]] = list[1];
							'''
							else:
								print "** Audit Id %s is Empty" % (reqArray[0]);
							'''
						else:
							isReqFirst = 0;
							userNameArray = reqArray[1].split('=');
							if userNameArray[1] and userNameArray[1] != 'null':
								reqMap[reqArray[0]] = list[1];
							'''
							else:
								print "Audit Id %s is Empty" % (reqArray[0]);
							'''
				elif serviceName == 'SLE':
					#print "In SLE Else";
					if reqArray[0] != 0:
						if (reqArray[0] == 1 or reqArray[0] in reqMapSle and isReqFirstSle == 0):
							reqMapListSle.append(reqMapSle);
							reqMapSle = {};
							userNameArray = reqArray[1].split('=');
							if userNameArray[1] and userNameArray[1] != 'null':
								reqMapSle[reqArray[0]] = list[1];
							'''
							else:
								print "** Audit Id %s is Empty" % (reqArray[0]);
							'''
						else:
							isReqFirstSle = 0;
							userNameArray = reqArray[1].split('=');
							if userNameArray[1] and userNameArray[1] != 'null':
								reqMapSle[reqArray[0]] = list[1];
							'''
							else:
								print "Audit Id %s is Empty" % (reqArray[0]);
							'''
		reqMapList.append(reqMap);
		reqMapListSle.append(reqMapSle);
		
		#print "Req List For LMS is %s" % (reqMapList);
		#print "Req List For SLE is %s" % (reqMapListSle);

		isReqFirst = 1;
		isReqFirstSle = 1;
		isResFirst = 1;
		isResFirstSle = 1;
		serverLogs.seek(0,0);
		for line in serverLogs:
			if re.search('RESPONSE_AUDIT_TRAIL', line):
				#print line;
				list = line.split('RESPONSE_AUDIT_TRAIL-');
				resArray = list[1].split('#');
				#print resArray;
				serviceName = resArray[4].split('=')[1].rstrip();
				#print "Service Name %s with Length %d" %(serviceName, len(serviceName));
				if resArray[0] != 0:
					if(serviceName == 'MGMT' or serviceName == 'DG' or serviceName == 'HOME'):
						if (resArray[0] in resMap or resArray[0] == 1) and isResFirst == 0 and 'mainPageDisplay' != resArray[3].split('=')[1]:
							resMapList.append(resMap);
							resMap = {};
							resMap[resArray[0]] = list[1];
							isResFirst = 1;
						else:
							isResFirst = 0;
							resMap[resArray[0]] = list[1];
					elif serviceName == 'SLE':
						if (resArray[0] in resMapSle or resArray[0] == 1) and isResFirstSle == 0:
							#print "SLE RES MAP LENGTH %d with map %s" %(len(resMapSle), resMapSle);
							resMapListSle.append(resMapSle);
							resMapSle = {};
							resMapSle[resArray[0]] = list[1];
							isResFirstSle = 1;
						else:
							isResFirstSle = 0;
							resMapSle[resArray[0]] = list[1];
		resMapList.append(resMap);	
		resMapListSle.append(resMapSle);
		serverLogs.close();

		#print "Res List For LMS is %s" % (resMapList);
		#print "Res List For SLE is %s" % (resMapListSle);

		#currentTime = time.strftime("%Y-%m-%d %H:%M:%S");
		currentTime = datetime.datetime.today();
		jCount = 0;		
		for tempReqMap in reqMapList:
			tempResMap = resMapList[jCount];
			jCount += 1;
			con = MySQLdb.connect(host="localhost", user="root", passwd="root", db="GHANA_5_LMS");
			cursor = con.cursor();
			#currentTime = time.strftime("%Y-%m-%d %H:%M:%S");
			currentTime = datetime.datetime.today();
			for key, value in tempReqMap.iteritems():
				tempArray = value.split('#');
				auditId = tempArray[0];
				userName = tempArray[1].split('=')[1];
				accessIp = tempArray[2].split('=')[1];
				reqTime = tempArray[3].split('=')[1];
				actionName = tempArray[4].split('=')[1];
				serviceType = tempArray[5].split('=')[1];
				interface = tempArray[6].split('=')[1].rstrip();

				if interface == 'TERMINAL':
					continue;

				#print tempResMap;
				time = '0000:00:00 00:00:00';
				for tempResMap in resMapList:
					if key in tempResMap:
						time = tempResMap[key].split("#")[2].split("=")[1];
						break;
				'''
				if key in tempResMap:
					time = tempResMap[key].split("#")[2].split("=")[1];
				else:
					time = '0000:00:00 00:00:00';
				'''
				#print time;

				if serviceType != 'NA':
					cursor.execute("""INSERT INTO st_lms_audit_user_access_history(audit_id, user_name, access_ip, request_time, action_name, service_type, interface, response_time, entry_time) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)""",(auditId, userName, accessIp, reqTime, actionName, serviceType, interface, time, currentTime));

			count = cursor.execute("UPDATE st_lms_audit_user_access_history audit INNER JOIN st_dg_priviledge_rep priv ON audit.action_name = priv.action_mapping SET audit.priv_id = priv.priv_id, audit.is_audit_trail_display = priv.is_audit_trail_display WHERE audit.service_type = 'DG' AND audit.is_audit_trail_display IS NULL;");

			count = cursor.execute("UPDATE st_lms_audit_user_access_history audit INNER JOIN st_lms_priviledge_rep priv ON audit.action_name = priv.action_mapping SET audit.priv_id = priv.priv_id, audit.is_audit_trail_display = priv.is_audit_trail_display WHERE audit.service_type in ('HOME', 'MGMT') AND audit.is_audit_trail_display IS NULL;");

			count = cursor.execute("UPDATE st_lms_audit_user_access_history audit INNER JOIN st_lms_user_master um ON audit.user_name = um.user_name SET audit.user_id = um.user_id WHERE audit.user_id IS NULL;");

			#lastSuccessTime = reqTime.split(" ")[0] + ' ' + str(datetime.datetime.now().time());
			#schedulerUpdateQuery = "update st_lms_scheduler_master set last_end_time = '%s', status_msg = 'Success', last_status = 'DONE', last_success_time = '%s' where dev_name = 'DAILY_AUDIT_ACTIVITY_SCRIPT';" % (str(currentTime), lastSuccessTime);

#			print schedulerUpdateQuery;
			#count = cursor.execute(schedulerUpdateQuery);
			con.commit();
			cursor.close();
			con.close;

		jCount = 0;		
		for tempReqMap in reqMapListSle:
			tempResMap = resMapListSle[jCount];
			jCount += 1;
			con = MySQLdb.connect(host="localhost", user="root", passwd="root", db="GHANA_5_SLE");
			#con = MySQLdb.connect(host="192.168.124.201", user="root", passwd="root", db="sle_ghana_13052015");
			cursor = con.cursor();
			for key, value in tempReqMap.iteritems():
				tempArray = value.split('#');
				auditId = tempArray[0];
				userName = tempArray[1].split('=')[1];
				accessIp = tempArray[2].split('=')[1];
				reqTime = tempArray[3].split('=')[1];
				actionName = tempArray[4].split('=')[1];
				serviceType = tempArray[5].split('=')[1];
				interface = tempArray[6].split('=')[1].rstrip();

				if interface == 'TERMINAL':
					continue;

				#print tempResMap;
				time = '0000:00:00 00:00:00';
				for tempResMap in resMapListSle:
					if key in tempResMap:
						time = tempResMap[key].split("#")[2].split("=")[1];
						break;
				'''
				if key in tempResMap:
					time = tempResMap[key].split("#")[2].split("=")[1];
				else:
					time = '0000:00:00 00:00:00';
				'''
				#print time;

				if serviceType != 'NA':
					cursor.execute("""INSERT INTO st_lms_audit_user_access_history(audit_id, merchant_id, user_name, access_ip, request_time, action_name, service_type, interface, response_time, entry_time) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)""",(auditId, 2, userName, accessIp, reqTime, actionName, serviceType, interface, time, currentTime));

			count = cursor.execute("UPDATE st_lms_audit_user_access_history audit INNER JOIN st_rm_priv_action_mapping priv ON priv.action_mapping = CONCAT(audit.action_name, '.action') SET audit.priv_id = priv.priv_id, audit.is_audit_trail_display = priv.is_audit_trail_display WHERE audit.is_audit_trail_display IS NULL;");

			count = cursor.execute("UPDATE st_lms_audit_user_access_history audit INNER JOIN st_sl_merchant_user_master um ON um.user_name = audit.user_name AND audit.merchant_id = um.merchant_id SET audit.user_id = um.merchant_user_id;");
			con.commit();
			cursor.close();
			con.close;


