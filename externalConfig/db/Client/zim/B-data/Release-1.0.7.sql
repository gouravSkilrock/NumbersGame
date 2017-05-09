--liquibase formatted sql

--changeset nikhil:1
update st_dg_game_master set game_name='Mini Roulette' where game_name_dev='MiniRoulette';