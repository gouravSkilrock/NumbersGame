-- liquibase formatted sql

-- changeset GK:1
UPDATE `st_se_priviledge_rep` SET `group_name` = 'Book Recieve Registration' WHERE action_mapping = 'bo_om_bookRecieveRegistration_Success'; 
-- rollback UPDATE `st_se_priviledge_rep` SET `group_name` = NULL WHERE action_mapping = 'bo_om_bookRecieveRegistration_Success'; 
-- changeset GK:2
update st_se_priviledge_rep set status='INACTIVE' where  priv_disp_name='Dispatch Order';
update st_se_priviledge_rep set status='INACTIVE' where  priv_disp_name='Update Agent Invoicing Method';
update st_se_priviledge_rep set status='INACTIVE' where  priv_disp_name='Shift Inventory';
update st_se_priviledge_rep set status='INACTIVE' where  priv_disp_name='Book Recieve Registration' and priv_owner='AGENT';
update st_se_priviledge_rep set status='INACTIVE' where  priv_disp_name='Close Order' and priv_owner='AGENT';
update st_se_priviledge_rep set status='INACTIVE' where  priv_disp_name='Order Request to BO' and priv_owner='AGENT';
update st_se_priviledge_rep set status='INACTIVE' where  priv_disp_name='Quick Order Request' and priv_owner='AGENT';
update st_se_priviledge_rep set status='INACTIVE' where  priv_disp_name='Self Initiated Order for Retailer' and priv_owner='AGENT';
-- rollback update st_se_priviledge_rep set status='ACTIVE' where  priv_disp_name='Dispatch Order';update st_se_priviledge_rep set status='ACTIVE' where  priv_disp_name='Update Agent Invoicing Method';update st_se_priviledge_rep set status='ACTIVE' where  priv_disp_name='Shift Inventory';update st_se_priviledge_rep set status='ACTIVE' where  priv_disp_name='Book Recieve Registration' and priv_owner='AGENT';update st_se_priviledge_rep set status='ACTIVE' where  priv_disp_name='Close Order' and priv_owner='AGENT';update st_se_priviledge_rep set status='ACTIVE' where  priv_disp_name='Order Request to BO' and priv_owner='AGENT';update st_se_priviledge_rep set status='ACTIVE' where  priv_disp_name='Quick Order Request' and priv_owner='AGENT';update st_se_priviledge_rep set status='ACTIVE' where  priv_disp_name='Self Initiated Order for Retailer' and priv_owner='AGENT';

--changeset Gp:1
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='INACTIVE',lrpm.status='INACTIVE' WHERE  spr.priv_disp_name='Warehouse Wise Inventory Report' and lrpm.role_id=1 and lrpm.service_mapping_id=7;
--rollback
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='ACTIVE',lrpm.status='ACTIVE' WHERE  spr.priv_disp_name='Warehouse Wise Inventory Report' and lrpm.role_id=1 and lrpm.service_mapping_id=7;

--changeset Gp:2
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='INACTIVE',lrpm.status='INACTIVE' WHERE  spr.priv_disp_name='track order' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='INACTIVE',lrpm.status='INACTIVE' WHERE  spr.priv_disp_name='Quick Order Request to Agent' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='INACTIVE',lrpm.status='INACTIVE' WHERE  spr.priv_disp_name='Book Recieve Registration' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='INACTIVE',lrpm.status='INACTIVE' WHERE  spr.priv_disp_name='Book Activation' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='INACTIVE',lrpm.status='INACTIVE' WHERE  spr.priv_disp_name='Sell Ticket' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
--rollback
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='ACTIVE',lrpm.status='ACTIVE' WHERE  spr.priv_disp_name='track order' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='ACTIVE',lrpm.status='ACTIVE' WHERE  spr.priv_disp_name='Quick Order Request to Agent' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='ACTIVE',lrpm.status='ACTIVE' WHERE  spr.priv_disp_name='Book Recieve Registration' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='ACTIVE',lrpm.status='ACTIVE' WHERE  spr.priv_disp_name='Book Activation' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='ACTIVE',lrpm.status='ACTIVE' WHERE  spr.priv_disp_name='Sell Ticket' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
-- changeset GK:3
UPDATE st_lms_property_master SET VALUE="NO" WHERE property_dev_name ='IS_SCRATCH_NEW_FLOW_ENABLED';
-- rollback UPDATE st_lms_property_master SET VALUE="YES" WHERE property_dev_name ='IS_SCRATCH_NEW_FLOW_ENABLED';

--changeset Gp:3
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='ACTIVE',lrpm.status='ACTIVE' WHERE  spr.priv_disp_name='Book Wise Inventory Details' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';
--rollback
UPDATE st_se_priviledge_rep spr INNER JOIN st_lms_role_priv_mapping lrpm ON spr.priv_id=lrpm.priv_id SET spr.status='INACTIVE',lrpm.status='INACTIVE' WHERE  spr.priv_disp_name='Book Wise Inventory Details' and lrpm.role_id=3 and lrpm.service_mapping_id=15 and spr.priv_owner='RETAILER';

-- changeset GK:4
UPDATE `st_iw_game_master` SET `game_status` = 'SALE_OPEN' WHERE `game_dev_name` = 'INSTANT_WIN'; 
-- rollback UPDATE `st_iw_game_master` SET `game_status` = 'SALE_HOLD' WHERE `game_dev_name` = 'INSTANT_WIN'; 

--changeset Gp:4
update st_lms_scheduler_master set scheduled_Time='0 05 00 * * ?' where jobGroup='AutoQuartzJob';
update st_lms_scheduler_master set scheduled_Time='0 01 00 * * ?' where jobGroup='DailyLogoutAllRetJob';
update st_lms_scheduler_master set scheduled_Time='0 15 05 * * ?' where jobGroup='QuartzDailyJob';
update st_lms_scheduler_master set scheduled_Time='0 15 01 * * ?' where jobGroup='OlaCommDistribution';
update st_lms_scheduler_master set scheduled_Time='0 00 23 * * ?' where jobGroup='OlaCommUpdateWeekly';
update st_lms_scheduler_master set scheduled_Time='0 15 03 3 * ?' where jobGroup='OlaCommUpdateMonthly';
update st_lms_scheduler_master set scheduled_Time='0 10 00 ? * MON' where jobGroup='QuartzWeeklyJob';
update st_lms_scheduler_master set scheduled_Time='0 10 00 ? * TUE' where jobGroup='GenMapIdweeklyCronExpr';
update st_lms_scheduler_master set scheduled_Time='0 15 00 1 * ?' where jobGroup='QuartzMonthlyJob';
update st_lms_scheduler_master set scheduled_Time='0 25 23 * * ?' where jobGroup='QuartzJobUpdateLedgerBalance';
update st_lms_scheduler_master set scheduled_Time='0 2 0 * * ?' where jobGroup='ICS';
update st_lms_scheduler_master set scheduled_Time='0 15 00 * * ?' where jobGroup='UpdateLedger';
update st_lms_scheduler_master set scheduled_Time='0 14 6 * * ?' where jobGroup='QuartzReconEntryJob';
update st_lms_scheduler_master set scheduled_Time='0 20 00 * * ?' where jobGroup='OlaRummyWithRequest';
update st_lms_scheduler_master set scheduled_Time='0 10 02 * * ?' where jobGroup='AutomaticArchiving';
update st_lms_scheduler_master set scheduled_Time='0 00 04 * * ?' where jobGroup='DailyActivityHistory';
update st_lms_scheduler_master set scheduled_Time='0 40 11 * * ?' where jobGroup='updateLevyNSecDeposit';
update st_lms_scheduler_master set scheduled_Time='0 00 11 * * ?' where jobGroup='auditScriptDailyGroup';
update st_lms_scheduler_master set scheduled_Time='0 05 17 * * ?' where jobGroup='AgentAutoBlockJob';
update st_lms_scheduler_master set scheduled_Time='0 58 17 1/1 * ? *' where jobGroup='ScratchQuartzDailyJob';
update st_lms_scheduler_master set scheduled_Time='0 05 17 * * ?' where jobGroup='TallyXmlCashBankJob';
update st_lms_scheduler_master set scheduled_Time='0 05 17 * * ?' where jobGroup='TallyXmlSalePwtTrainingJob';
update st_lms_scheduler_master set scheduled_Time='0 29 14 * * ?' where jobGroup='OrgBalUpdateJob';
update st_lms_scheduler_master set scheduled_Time='0 00 23 * * ?' where jobGroup='VSSaleReconcGroup';
update st_lms_scheduler_master set scheduled_Time='0 9 17 * * ?' where jobGroup='LicensingServerJob';
update st_lms_scheduler_master set scheduled_Time='0 53 16 * * ?' where jobGroup='GoodCauseJob';
insert into `st_lms_scheduler_master` (`dev_name`, `display_name`, `jobGroup`, `status`, `scheduled_Time`, `last_start_time`, `last_end_time`, `last_status`, `last_success_time`, `estimated_time`, `status_msg`) values('Good_Cause_Job_SCHEDULER','GoodCauseJobSCHEDULER','GoodCauseJob','INACTIVE','0 05 00 * * ?','2017-01-03 00:05:00','2017-01-03 00:05:00','DONE','2017-01-03 00:05:00',NULL,'Success');
insert into `st_lms_scheduler_master` (`dev_name`, `display_name`, `jobGroup`, `status`, `scheduled_Time`, `last_start_time`, `last_end_time`, `last_status`, `last_success_time`, `estimated_time`, `status_msg`) values('Licensing_Server_Job_SCHEDULER','LicensingServerJobSCHEDULER','LicensingServerJob','INACTIVE','0 05 00 * * ?','2017-01-03 00:05:00','2017-01-03 00:05:00','DONE','2017-01-03 00:05:00',NULL,'Success');
insert into `st_lms_scheduler_master` (`dev_name`, `display_name`, `jobGroup`, `status`, `scheduled_Time`, `last_start_time`, `last_end_time`, `last_status`, `last_success_time`, `estimated_time`, `status_msg`) values('VS_Sale_Reconc_Group_SCHEDULER','VSSaleReconcGroupSCHEDULER','VSSaleReconcGroup','INACTIVE','0 05 00 * * ?','2017-01-03 00:05:00','2017-01-03 00:05:00','DONE','2017-01-03 00:05:00',NULL,'Success');
insert into `st_lms_scheduler_master` (`dev_name`, `display_name`, `jobGroup`, `status`, `scheduled_Time`, `last_start_time`, `last_end_time`, `last_status`, `last_success_time`, `estimated_time`, `status_msg`) values('TallyXmlCashBankJob_SCHEDULER','Tally_Xml_Cash_Bank_Job_SCHEDULER','TallyXmlCashBankJob','INACTIVE','0 05 00 * * ?','2017-01-03 00:05:00','2017-01-03 00:05:00','DONE','2017-01-03 00:05:00',NULL,'Success');

--rollback
update st_lms_scheduler_master set scheduled_Time='0 05 00 * * ?' where jobGroup='AutoQuartzJob';
update st_lms_scheduler_master set scheduled_Time='0 01 00 * * ?' where jobGroup='DailyLogoutAllRetJob';
update st_lms_scheduler_master set scheduled_Time='0 20 08 * * ?' where jobGroup='QuartzDailyJob';
update st_lms_scheduler_master set scheduled_Time='0 15 01 * * ?' where jobGroup='OlaCommDistribution';
update st_lms_scheduler_master set scheduled_Time='0 00 23 * * ?' where jobGroup='OlaCommUpdateWeekly';
update st_lms_scheduler_master set scheduled_Time='0 15 03 3 * ?' where jobGroup='OlaCommUpdateMonthly';
update st_lms_scheduler_master set scheduled_Time='0 05 03 * * ?' where jobGroup='QuartzWeeklyJob';
update st_lms_scheduler_master set scheduled_Time='0 10 00 ? * TUE' where jobGroup='GenMapIdweeklyCronExpr';
update st_lms_scheduler_master set scheduled_Time='0 00 05 1 * ?' where jobGroup='QuartzMonthlyJob';
update st_lms_scheduler_master set scheduled_Time='0 20 03 * * ?' where jobGroup='QuartzJobUpdateLedgerBalance';
update st_lms_scheduler_master set scheduled_Time='0 2 0 * * ?' where jobGroup='ICS';
update st_lms_scheduler_master set scheduled_Time='0 10 03 * * ?' where jobGroup='UpdateLedger';
update st_lms_scheduler_master set scheduled_Time='0 14 6 * * ?' where jobGroup='QuartzReconEntryJob';
update st_lms_scheduler_master set scheduled_Time='0 20 00 * * ?' where jobGroup='OlaRummyWithRequest';
update st_lms_scheduler_master set scheduled_Time='0 10 02 * * ?' where jobGroup='AutomaticArchiving';
update st_lms_scheduler_master set scheduled_Time='0 20 04 * * ?' where jobGroup='DailyActivityHistory';
update st_lms_scheduler_master set scheduled_Time='0 40 11 * * ?' where jobGroup='updateLevyNSecDeposit';
update st_lms_scheduler_master set scheduled_Time='0 00 11 * * ?' where jobGroup='auditScriptDailyGroup';
update st_lms_scheduler_master set scheduled_Time='0 05 17 * * ?' where jobGroup='AgentAutoBlockJob';
update st_lms_scheduler_master set scheduled_Time='0 58 17 1/1 * ? *' where jobGroup='ScratchQuartzDailyJob';
update st_lms_scheduler_master set scheduled_Time='0 05 17 * * ?' where jobGroup='TallyXmlCashBankJob';
update st_lms_scheduler_master set scheduled_Time='0 05 17 * * ?' where jobGroup='TallyXmlSalePwtTrainingJob';
update st_lms_scheduler_master set scheduled_Time='0 29 14 * * ?' where jobGroup='OrgBalUpdateJob';
update st_lms_scheduler_master set scheduled_Time='0 00 23 * * ?' where jobGroup='VSSaleReconcGroup';
update st_lms_scheduler_master set scheduled_Time='0 9 17 * * ?' where jobGroup='LicensingServerJob';
update st_lms_scheduler_master set scheduled_Time='0 53 16 * * ?' where jobGroup='GoodCauseJob';
delete from st_lms_scheduler_master where jobGroup='GoodCauseJob';
delete from st_lms_scheduler_master where jobGroup='TallyXmlCashBankJob';
delete from st_lms_scheduler_master where jobGroup='TallyXmlSalePwtTrainingJob';
delete from st_lms_scheduler_master where jobGroup='VSSaleReconcGroup';
delete from st_lms_scheduler_master where jobGroup='LicensingServerJob';

--changeset Rishi:1
update `ge_merchant_master` set `status`='ACTIVE' where `merchant_code`='IW';
--rollback
update `ge_merchant_master` set `status`='INACTIVE' where `merchant_code`='IW';

--changeset Ishu:1
update st_lms_scheduler_master set status = 'INACTIVE' where dev_name = 'SCRATCH_INVOICE_GENERATE_SCHEDULER';
--rollback
update st_lms_scheduler_master set status = 'ACTIVE' where dev_name = 'SCRATCH_INVOICE_GENERATE_SCHEDULER';
