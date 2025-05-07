insert into address (address_line1, address_line2, city, postal_code)
values 
  ('grzybowa', '12', 'opole', '12-345'),
  ('lesna', '9', 'opole', '12-345'),
  ('parkowa', '3', 'opole', '12-345'),
  ('iglasta', '11', 'opole', '12-345');

insert into patient (date_of_birth, email, first_name, last_name, patient_number, telephone_number, insured)
values 
  ('2001-01-01', 'eustachy@enum.pl', 'eustachy', 'enum', '123123', '123456789', false),
  ('2002-02-02', 'tomek@domek.pl', 'tomasz', 'dom', '321321', '987654321', true);

insert into doctor (doctor_number, email, first_name, last_name, telephone_number, specialization)
values 
  ('111', 'kacper@kapusta.pl', 'kacper', 'kapusta', '111111111', 'SURGEON'),
  ('222', 'grzegorz@plecy.pl', 'grzegorz', 'plecy', '222222222', 'DERMATOLOGIST');

insert into visit (doctor_id, patient_id, time, description)
values 
  (1, 1, '2025-03-20 11:00:00', 'wizyta kontrolna'),
  (1, 2, '2025-03-20 12:00:00', 'wizyta kontrolna'),
  (1, 1, '2025-03-20 13:00:00', 'operacja'),
  (2, 1, '2025-03-20 14:00:00', 'wizyta kontrolna'),
  (2, 2, '2025-03-20 15:00:00', 'wizyta kontrolna');


insert into medical_treatment (visit_id, description, type)
values 
  (1, 'kontrola brzucha', 'USG'),
  (2, 'kontrola ucha', 'USG'),
  (3, 'zdjęcia brzucha', 'RTG'),
  (4, 'kontrola twarzy', 'USG'),
  (5, 'kontrola twarzy', 'USG');

insert into patient_to_address (address_id, patient_id)
values 
  (1, 1),
  (1, 2);

insert into doctor_to_address (address_id, doctor_id)
values 
  (1, 1),
  (4, 2);