-- User
INSERT INTO "member" (id, password, uid, name, contact, email, department, grade, birth, address, is_in_school, image_url, role, provider)
VALUES ('admin', '{bcrypt}$2a$10$ri5DhfHYNcqjN3HGP4oCYuZ7d8sxULvUOl4gE3OONygd4QUE.1AG2', '', '관리자', '01012341234', 'noop103@kyonggi.ac.kr', '컴퓨터공학부', 3, '2000-05-27', '수원시', true, '', 'ADMIN', 'LOCAL');

INSERT INTO "member" (id, password, uid, name, contact, email, department, grade, birth, address, is_in_school, image_url, role, provider)
VALUES ('201912156', '{bcrypt}$2a$10$uQn6DnXLZEFmRh57SbJzIuIU2vmkVSaXMnjZjcRFwJjmXMD.7OIfS', '', '한관희', '01051476788', 'noop103@naver.com', '컴퓨터공학부', 3, '2000-05-27', '수원시', true, '', 'ADMIN', 'LOCAL');

INSERT INTO "member" (id, password, uid, name, contact, email, department, grade, birth, address, is_in_school, image_url, role, provider, created_at)
VALUES ('201912033', '{bcrypt}$2a$10$CqdfE.qyDbKFAS0w.WBvt.jhH.YbfMvo1MSGv9Ut7j06wUOoW365q', '', '김관식', '01021344323', 'gwansik@naver.com', '컴퓨터공학부', 3, '2000-03-23', '수원시', true, '', 'USER', 'LOCAL', '2023-07-25 18:13:43.000000');

-- Application
INSERT INTO "application" (student_id, name, contact, email, department, grade, birth, address, interests, other_activities, created_at)
VALUES ('201912033', '김관식', '01021344323', 'gwansik@naver.com', '컴퓨터공학부', 3, '2000-03-23', '수원시', '', '', '2023-07-25 18:12:43.000000');

INSERT INTO "application" (student_id, name, contact, email, department, grade, birth, address, interests, other_activities, created_at)
VALUES ('201912034', '박동민', '01012345678', 'dongmin@naver.com', '컴퓨터공학부', 3, '2000-01-01', '수원시', '', '', '2022-07-25 18:12:48.000000');

-- -- Board
-- INSERT INTO "board" (category, title, content, writer, created_at)
-- VALUES ('질문','스프링은 무엇인가요?', '<h1>스프링 짱짱</h1>', 'admin', '2022-07-25 18:12:43.000000');
--
-- INSERT INTO "board" (category, title, content, writer, created_at)
-- VALUES ('졸업', '카카오 문열어', '<h1>네이버 흥해라</h1>', 'admin', '2022-07-25 18:12:43.000000');

-- LoginFailInfo
insert into login_fail_info(member_id, login_fail_count, is_lock)
values ('admin', 0, false);
insert into login_fail_info(member_id, login_fail_count, is_lock)
values ('201912156', 0, false);
insert into login_fail_info(member_id, login_fail_count, is_lock)
values ('201912033', 0, false);

-- Donation
insert into donation(member_id, amount, message, created_at)
values ('admin', 10000, '씨랩 화이팅', '2022-07-02 12:14:45.000000');
insert into donation(member_id, amount, message, created_at)
values ('admin', 53000, '씨랩 화이팅', '2022-07-15 07:43:12.000000');
insert into donation(member_id, amount, message, created_at)
values ('201912156', 32000, '씨랩 화이팅', '2022-08-02 09:46:00.000000');
insert into donation(member_id, amount, message, created_at)
values ('201912033', 150000, '씨랩 화이팅', '2022-09-26 18:32:00.000000');
insert into donation(member_id, amount, message, created_at)
values ('201912033', 256000, '씨랩 화이팅', '2022-10-12 23:59:03.000000');