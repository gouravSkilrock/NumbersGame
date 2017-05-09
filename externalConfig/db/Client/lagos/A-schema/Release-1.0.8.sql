--liquibase formatted sql

-- changeset NJ_5216:1
CREATE TABLE `ret_raffle_exclude` (
  `ret_user_id` int(11) NOT NULL,
  PRIMARY KEY (`ret_user_id`)
) ENGINE=InnoDB;
-- rollback drop TABLE ret_raffle_exclude;
-- changeset NJ_5216:2
CREATE TABLE `ret_raffle_config` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `code` varchar(50) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `remarks` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
-- rollback drop TABLE ret_raffle_config;
-- changeset NJ_5216:3
CREATE TABLE `ret_raffle_draw_master` (
  `draw_id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `from_date` date DEFAULT NULL,
  `to_date` date DEFAULT NULL,
  `draw_date` datetime DEFAULT NULL,
  `draw_status` enum('ACTIVE','INACTIVE','DONE') DEFAULT NULL,
  PRIMARY KEY (`draw_id`)
) ENGINE=InnoDB ;
-- rollback drop TABLE ret_raffle_draw_master;