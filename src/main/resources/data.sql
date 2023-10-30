-- User
INSERT INTO "member" (id, password, uid, name, contact, email, department, grade, birth, address, is_in_school, image_url, role, provider, created_at)
VALUES ('admin', '{bcrypt}$2a$10$ri5DhfHYNcqjN3HGP4oCYuZ7d8sxULvUOl4gE3OONygd4QUE.1AG2', '', '관리자', '01012341234', 'noop103@kyonggi.ac.kr', '컴퓨터공학부', 3, '2000-05-27', '수원시', true, '', 'ADMIN', 'LOCAL', '2023-06-30 06:00:00.000000');

INSERT INTO "member" (id, password, uid, name, contact, email, department, grade, birth, address, is_in_school, image_url, role, provider, created_at)
VALUES ('201912156', '{bcrypt}$2a$10$uQn6DnXLZEFmRh57SbJzIuIU2vmkVSaXMnjZjcRFwJjmXMD.7OIfS', '', '한관희', '01051476788', 'noop103@naver.com', '컴퓨터공학부', 3, '2000-05-27', '수원시', true, '', 'ADMIN', 'LOCAL', '2023-06-30 06:19:51.000000');

INSERT INTO "member" (id, password, uid, name, contact, email, department, grade, birth, address, is_in_school, image_url, role, provider, created_at)
VALUES ('201912033', '{bcrypt}$2a$10$CqdfE.qyDbKFAS0w.WBvt.jhH.YbfMvo1MSGv9Ut7j06wUOoW365q', '', '김관식', '01021344323', 'gwansik@naver.com', '컴퓨터공학부', 3, '2000-03-23', '수원시', true, '', 'USER', 'LOCAL', '2023-07-01 13:10:01.000000');

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

-- Notification
insert into notification(content, created_at, member_id)
values ('종강 언제 해요', '2022-10-13 16:45:02.000000', 'admin');
insert into notification(content, created_at, member_id)
values ('개강 싫어요', '2022-10-13 16:45:13.000000', 'admin');
insert into notification(content, created_at, member_id)
values ('씨랩 좋아요', '2022-10-13 16:40:54.000000', '201912156');
insert into notification(content, created_at, member_id)
values ('씨랩 사랑해요', '2022-10-13 18:42:09.000000', '201912033');

-- MembershipFee
insert into membership_fee(category, content, image_url, created_at, member_id)
values ('신청', '스프링 책 사고 싶어요', 'https://contents.kyobobook.co.kr/sih/fit-in/458x0/pdt/9788960773431.jpg', '2023-10-14 02:18:04.461149', 'admin');
insert into membership_fee(category, content, image_url, created_at, member_id)
values ('신청', '리액트 책 사고 싶어요', 'https://image.yes24.com/goods/78233628/XL', '2023-10-14 02:18:04.461149', '201912156');
insert into membership_fee(category, content, image_url, created_at, member_id)
values ('후원', '씨랩 화이팅', '', '2022-09-26 18:32:00.000000', '201912033');
insert into membership_fee(category, content, image_url, created_at, member_id)
values ('후원', '씨랩 화이팅', '', '2022-10-12 23:59:03.000000', '201912033');

-- Book
insert into book(category, title, author, publisher, image_url, created_at)
values ('개발', '토비의 스프링 3', '이일민', '에이콘출판', 'https://shopping-phinf.pstatic.net/main_3250387/32503877629.20220527022132.jpg?type=w300', '2023-10-16 07:39:39.042000');
insert into book(category, title, author, publisher, image_url, created_at)
values ('개발', '토비의 스프링 3', '이일민', '에이콘출판', 'https://shopping-phinf.pstatic.net/main_3250387/32503877629.20220527022132.jpg?type=w300', '2023-10-16 07:39:39.042000');
insert into book(category, title, author, publisher, image_url, created_at)
values ('개발', '토비의 스프링 3', '이일민', '에이콘출판', 'https://shopping-phinf.pstatic.net/main_3250387/32503877629.20220527022132.jpg?type=w300', '2023-10-16 07:39:39.042000');

-- SharedAccount
insert into shared_account(username, password, platform_name, platform_url)
values ('test1', 'test1', 'test1', 'test1');
insert into shared_account(username, password, platform_name, platform_url)
values ('test2', 'test2', 'test2', 'test2');
insert into shared_account(username, password, platform_name, platform_url)
values ('test3', 'test3', 'test3', 'test3');
insert into shared_account(username, password, platform_name, platform_url)
values ('test4', 'test4', 'test4', 'test4');