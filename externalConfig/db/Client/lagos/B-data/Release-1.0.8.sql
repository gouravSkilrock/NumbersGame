--liquibase formatted sql

-- changeset NJ_5216:1
insert  into `ret_raffle_config`(`id`,`code`,`value`,`remarks`) values 

(1,'saleAmtForIndoor','15000','Indoor Game and Instant Win Game Sale Amt'),

(2,'saleAmtForOthers','20000','5/90 Ghana 10/90 Ghana Game Sale Amt'),

(3,'promoMsg','Promo Msg T&C APPLIED','Promo Msg');
-- rollback delete from ret_raffle_config where code in('saleAmtForIndoor','saleAmtForOthers','promoMsg');

-- changeset ishu:1
insert  into `st_lms_property_master`(`property_code`,`property_dev_name`,`property_display_name`,`status`,`editable`,`value`,`value_type`,`description`) values ('MOBILE_NO_WLS','MOBILE_NO_WLS','MOBILE_NO_WLS','ACTIVE','YES','9999999999,8888888888','String','Mobile No on which SMS has to sent for WLS');

-- rollback delete from st_lms_property_master where property_dev_name like 'MOBILE_NO_WLS';


-- changeset ishu:2
update st_dg_priviledge_rep set status = 'INACTIVE' where priv_disp_name in ('Claim Condition','Machine Number Entry','bo_dm_block_specific_ticket_menu','bo_dm_block_specific_ticket_result','bo_dm_unblock_specific_ticket_menu','bo_dm_unblock_ticket_display_search','bo_dm_unblock_specific_ticket_result','bo_rep_block_tickets_menu','bo_rep_block_tickets_search','bo_rep_block_ticket_exportExcel');

-- rollback update st_dg_priviledge_rep set status = 'ACTIVE' where priv_disp_name in ('Claim Condition','Machine Number Entry','bo_dm_block_specific_ticket_menu','bo_dm_block_specific_ticket_result','bo_dm_unblock_specific_ticket_menu','bo_dm_unblock_ticket_display_search','bo_dm_unblock_specific_ticket_result','bo_rep_block_tickets_menu','bo_rep_block_tickets_search','bo_rep_block_ticket_exportExcel');


-- changeset ishu:3
update st_dg_priviledge_rep set status = 'ACTIVE' where priv_disp_name like 'Ticket Cancellation';

-- rollback update st_dg_priviledge_rep set status = 'INACTIVE' where priv_disp_name like 'Ticket Cancellation';


-- changeset vatsal_LMS-5363:1
UPDATE `st_dg_game_master` SET `game_status` = 'SALE_CLOSE' WHERE `game_name_dev` = 'TwelveByTwentyFour';

-- rollback UPDATE `st_dg_game_master` SET `game_status` = 'OPEN' WHERE `game_name_dev` = 'TwelveByTwentyFour'; 


