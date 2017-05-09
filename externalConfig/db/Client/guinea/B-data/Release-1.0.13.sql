--liquibase formatted sql

--changeset Rachit-8025:1
insert into `st_dg_game_master` (`game_id`, `game_nbr`, `game_name`, `game_name_dev`, `agent_sale_comm_rate`, `agent_pwt_comm_rate`, `retailer_sale_comm_rate`, `retailer_pwt_comm_rate`, `vat_amt`, `govt_comm`, `govt_comm_pwt`, `high_prize_amt`, `prize_payout_ratio`, `game_status`, `offline_freeze_time`, `is_offline`, `raffle_ticket_type`, `closing_time`, `display_order`, `is_sale_allowed_through_terminal`, `bonus_ball_enable`) values('17','1','RaffleDailyCash','RaffleDailyCash','0.00','0.00','0.00','0.00','0.00','0.00','0.00','100000.00','50.00','OPEN','10','N','ORIGINAL',NULL,'1','N','N');
-- rollback delete from st_dg_game_master where game_name_dev = 'RaffleDailyCash';
insert into `st_dg_game_master` (`game_id`, `game_nbr`, `game_name`, `game_name_dev`, `agent_sale_comm_rate`, `agent_pwt_comm_rate`, `retailer_sale_comm_rate`, `retailer_pwt_comm_rate`, `vat_amt`, `govt_comm`, `govt_comm_pwt`, `high_prize_amt`, `prize_payout_ratio`, `game_status`, `offline_freeze_time`, `is_offline`, `raffle_ticket_type`, `closing_time`, `display_order`, `is_sale_allowed_through_terminal`, `bonus_ball_enable`) values('18','2','RaffleWeeklyCash','RaffleWeeklyCash','0.00','0.00','0.00','0.00','0.00','0.00','0.00','100000.00','50.00','OPEN','10','N','ORIGINAL',NULL,'2','N','N');
-- rollback delete from st_dg_game_master where game_name_dev = 'RaffleWeeklyCash';
insert into `st_dg_game_master` (`game_id`, `game_nbr`, `game_name`, `game_name_dev`, `agent_sale_comm_rate`, `agent_pwt_comm_rate`, `retailer_sale_comm_rate`, `retailer_pwt_comm_rate`, `vat_amt`, `govt_comm`, `govt_comm_pwt`, `high_prize_amt`, `prize_payout_ratio`, `game_status`, `offline_freeze_time`, `is_offline`, `raffle_ticket_type`, `closing_time`, `display_order`, `is_sale_allowed_through_terminal`, `bonus_ball_enable`) values('19','3','RaffleMonthlyCash','RaffleMonthlyCash','0.00','0.00','0.00','0.00','0.00','0.00','0.00','100000.00','50.00','OPEN','10','N','ORIGINAL',NULL,'3','N','N');
-- rollback delete from st_dg_game_master where game_name_dev = 'RaffleMonthlyCash';

--changeset kannu_9634:1
update st_dg_priviledge_rep set status='INACTIVE' where priv_id in(40);
--changeset kannu_9635:1
update st_dg_priviledge_rep set status='INACTIVE' where priv_id in(34);
--changeset kannu_9702:1
update st_dg_priviledge_rep set group_name='Draw Game Reports' where priv_id in (11,12);
--changeset kannu_9602:1
update st_lms_priviledge_rep set status='ACTIVE' where priv_id in(74);
--changeset kannu_9702:2
update st_dg_priviledge_rep set group_name_en='Draw Game Reports' where priv_id in (11,12);
--changeset kannu_9635:3
update st_dg_priviledge_rep set status='INACTIVE' where priv_id in (33,35,36,38,39,1,9,45,28,19,21,22,27);
--changeset kannu_9635:4
update st_dg_priviledge_rep set status='ACTIVE' where priv_id in (73);