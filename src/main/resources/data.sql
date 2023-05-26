CREATE SCHEMA IF NOT EXISTS RACE_SCORE;
CREATE SEQUENCE IF NOT EXISTS race_score.hibernate_sequence START 1;

--delete from race_score.event_team where id = 175;
--
--delete from race_score.stage_score where id = 939;
--delete from race_score.stage_score where id = 940;
--delete from race_score.stage_score where id = 942;
--delete from race_score.stage_score where id = 945;

--delete from race_score.users where user_id = 93;

--update race_score.event_team set start_order=number;
--ALTER TABLE  race_score.event ALTER COLUMN HEAD_DESCRIPTION TYPE varchar(1500);

--INSERT INTO race_score.penalty_dict values (100, 'Wpisz ręcznie', false, '0');

--update race_score.car set engine_capacity = 1000 where car_id = 52;
--update race_score.car set engine_capacity = 1000 where car_id = 31;
--update race_score.car set engine_capacity = 1000 where car_id = 34;

--insert into race_score.role values (1, 'USER_ROLE');
--insert into race_score.role values (2, 'ADMIN_ROLE');
--insert into race_score.role values (3, 'EVENT_ADMIN_ROLE');

--insert into race_score.car_class values (1, 'K1');
--insert into race_score.car_class values (2, 'K2');
--insert into race_score.car_class values (3, 'K3');
--insert into race_score.car_class values (4, 'K4');
--insert into race_score.car_class values (5, 'K5');
--insert into race_score.car_class values (6, 'GOŚĆ');
--insert into race_score.car_class values (7, 'OPEN');
--insert into race_score.car_class values (8, 'JUNIOR');
--insert into race_score.car_class values (9, 'RWD');
--insert into race_score.car_class values (10, 'FWD');
--insert into race_score.car_class values (11, 'AWD');
--insert into race_score.car_class values (12, 'Wyczyn');
--insert into race_score.car_class values (13, 'Adventure');
--insert into race_score.car_class values (14, 'Extreme');
--insert into race_score.car_class values (15, 'FIAT');

--INSERT INTO race_score.penalty_dict values (1, 'Opona/pachołek', false, '5');
--INSERT INTO race_score.penalty_dict values (2, 'Kara 10 sek', false, '10');
--INSERT INTO race_score.penalty_dict values (3, 'Rozbicie szykany', false, '15');
--INSERT INTO race_score.penalty_dict values (4, 'Kara 30 sek', false, '30');
--INSERT INTO race_score.penalty_dict values (5, 'Kara 45 sek', false, '45');
--INSERT INTO race_score.penalty_dict values (6, 'Kara 1 min', false, '60');
--INSERT INTO race_score.penalty_dict values (7, 'DYSKWALIFIKACJA', true, '0');
--INSERT INTO race_score.penalty_dict values (8, 'Wycofanie', true, '0');
--INSERT INTO race_score.penalty_dict values (10, 'Taryfa', false, '0');

--
--update race_score.event_Team set driver = (select driver from race_score.team where race_score.event_Team.team_id = team_id);
--update race_score.event_Team set co_driver = (select co_driver from race_score.team where race_score.event_Team.team_id = team_id);
--update race_score.event_Team set car_id = (select current_car_id from race_score.team where race_score.event_Team.team_id = team_id);
--update race_score.event_Team set club = (select club from race_score.team where race_score.event_Team.team_id = team_id);
--update race_score.event_Team set co_club = (select co_club from race_score.team where race_score.event_Team.team_id = team_id);
--update race_score.event_Team set team_name = (select team_name from race_score.team where race_score.event_Team.team_id = team_id);
--
--delete from race_score.stage_score where stage_id is null;

--update race_score.event_Team set car_id = 1328, team_id = 1327, club = 'Rzemieślnik' where id = 478

-- delete from race_Score.penalty where stage_id not in (select stage_id from race_score.stage_score);

-- update race_score.event_Team set car_class = 4 where event_id = 2277 and car_class is null
