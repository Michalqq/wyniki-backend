CREATE SCHEMA IF NOT EXISTS RACE_SCORE;
CREATE SEQUENCE IF NOT EXISTS race_score.hibernate_sequence START 1;

delete from race_score.users where user_id = 93;

--
--update race_score.event_team set start_order=number;
--ALTER TABLE  race_score.event ALTER COLUMN description TYPE varchar(1500);


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

--INSERT INTO race_score.penalty_dict values (1, 'Opona/pachołek', false, '5');
--INSERT INTO race_score.penalty_dict values (2, 'Kara 10 sek', false, '10');
--INSERT INTO race_score.penalty_dict values (3, 'Rozbicie szykany', false, '15');
--INSERT INTO race_score.penalty_dict values (4, 'Kara 30 sek', false, '30');
--INSERT INTO race_score.penalty_dict values (5, 'Kara 45 sek', false, '45');
--INSERT INTO race_score.penalty_dict values (6, 'Kara 1 min', false, '60');
--INSERT INTO race_score.penalty_dict values (7, 'DYSKWALIFIKACJA', true, '0');
--INSERT INTO race_score.penalty_dict values (8, 'Wycofanie', true, '0');