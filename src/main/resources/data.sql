CREATE SCHEMA IF NOT EXISTS RACE_SCORE;
CREATE SEQUENCE IF NOT EXISTS race_score.hibernate_sequence START 1;

alter table RACE_SCORE.team drop column drive_type;

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