-- User
insert into user(id, password, uid, name, contact, email, department, grade, birth, address, is_in_school, image_url, role, provider)
values ('201912156', '000527', '', '한관희', '010-5147-6788', 'noop103@naver.com', '컴퓨터공학부', 3, '2000-05-27', '수원시', true, '', 'ADMIN', 'LOCAL');
insert into user(id, password, uid, name, contact, email, department, grade, birth, address, is_in_school, image_url, role, provider, created_at)
values ('201912033', '000323', '', '김관식', '010-2134-4323', 'gwansik@naver.com', '컴퓨터공학부', 3, '2000-03-23', '수원시', true, '', 'USER', 'LOCAL', '2023-07-25 18:13:43.000000');

-- Application
insert into application(student_id, name, contact, email, department, grade, birth, address, interests, other_activities, created_at)
values ('201912033', '김관식', '010-2134-4323', 'gwansik@naver.com', '컴퓨터공학부', 3, '2000-03-23', '수원시', '', '', '2023-07-25 18:12:43.000000');
insert into application(student_id, name, contact, email, department, grade, birth, address, interests, other_activities, created_at)
values ('201912034', '박동민', '010-1234-5678', 'dongmin@naver.com', '컴퓨터공학부', 3, '2000-01-01', '수원시', '', '', '2022-07-25 18:12:48.000000');
