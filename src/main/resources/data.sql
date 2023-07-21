-- User
insert into user(id, uid, name, contact, email, department, grade, birth, address, is_in_school, image_url, role, provider)
values ('201912156', '', '한관희', '010-5147-6788', 'noop103@naver.com', '컴퓨터공학부', 3, '2000-05-27', '수원시', true, '', 'ADMIN', 'LOCAL');

-- Application
insert into application(student_id, name, contact, email, department, grade, address, interests, other_activities)
values ('201912033', '김관식', '010-2134-4323', 'gwansik@naver.com', '컴퓨터공학부', 3, '수원시', '', '');
insert into application(student_id, name, contact, email, department, grade, address, interests, other_activities)
values ('201912034', '박동민', '010-1234-5678', 'dongmin@naver.com', '컴퓨터공학부', 3, '수원시', '', '');
