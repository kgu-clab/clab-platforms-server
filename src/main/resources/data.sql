-- User
INSERT INTO "member" (id, password, name, contact, email, department, grade, birth, address, student_status, image_url, member_status, role, provider, created_at)
VALUES ('admin', '{bcrypt}$2a$10$ri5DhfHYNcqjN3HGP4oCYuZ7d8sxULvUOl4gE3OONygd4QUE.1AG2', '관리자', '01012341234', 'noop103@kyonggi.ac.kr', '컴퓨터공학부', 3, '2000-05-27', '수원시', 'CURRENT', '', 'ACTIVE', 'SUPER', 'LOCAL', '2023-06-30 06:00:00.000000');

INSERT INTO "member" (id, password, name, contact, email, department, grade, birth, address, student_status, image_url, member_status, role, provider, created_at)
VALUES ('201912156', '{bcrypt}$2a$10$uQn6DnXLZEFmRh57SbJzIuIU2vmkVSaXMnjZjcRFwJjmXMD.7OIfS', '한관희', '01051476788', 'noop103@naver.com', '컴퓨터공학부', 3, '2000-05-27', '수원시', 'CURRENT', '', 'ACTIVE', 'ADMIN', 'LOCAL', '2023-06-30 06:19:51.000000');

INSERT INTO "member" (id, password, name, contact, email, department, grade, birth, address, student_status, image_url, member_status, role, provider, created_at)
VALUES ('201912033', '{bcrypt}$2a$10$CqdfE.qyDbKFAS0w.WBvt.jhH.YbfMvo1MSGv9Ut7j06wUOoW365q', '김관식', '01021344323', 'gwansik@naver.com', '컴퓨터공학부', 3, '2000-03-23', '수원시', 'CURRENT', '', 'ACTIVE', 'USER', 'LOCAL', '2023-07-01 13:10:01.000000');

-- Application
INSERT INTO "application" (student_id, name, contact, email, department, grade, birth, address, interests, other_activities, application_type, is_pass, update_time, created_at)
VALUES ('201912023', '김관식', '01021344323', 'gwansik@naver.com', '컴퓨터공학부', 3, '2000-03-23', '수원시', '', '', 'CORE_TEAM', true, '2023-07-25 18:12:43.000000', '2023-07-25 18:12:43.000000');

INSERT INTO "application" (student_id, name, contact, email, department, grade, birth, address, interests, other_activities, application_type, is_pass, update_time, created_at)
VALUES ('201912034', '박동민', '01012345678', 'dongmin@naver.com', '컴퓨터공학부', 3, '2000-01-01', '수원시', '', '', 'NORMAL', false, '2022-07-25 18:12:48.000000', '2022-07-25 18:12:48.000000');

-- -- Board
INSERT INTO "board" (category, title, content, member_id, created_at)
VALUES ('질문','스프링은 무엇인가요?', '<h1>스프링 짱짱</h1>', 'admin', '2022-07-25 18:12:43.000000');

INSERT INTO "board" (category, title, content, member_id, created_at)
VALUES ('졸업', '카카오 문열어', '<h1>네이버 흥해라</h1>', 'admin', '2022-07-25 18:12:43.000000');

-- -- Comment (Board)
INSERT INTO "comment" (content, member_id, board_id, created_at)
VALUES ('스프링은 좋은 프레임워크입니다.', '201912156', 1, '2022-07-25 18:12:43.000000');

INSERT INTO "comment" (content, member_id, board_id, created_at)
VALUES ('네이버는 좋은 회사입니다.', '201912033', 2, '2022-07-25 18:12:43.000000');

-- Activity group
-- INSERT INTO "activity_group" (id, category, name, content, status, progress, code, image_url, created_at)
-- VALUES ('1', 'project', '스프링', '스프링 스터디', '활동중', 70, 1234,'http://image.png', '2022-07-25 18:12:43.000000');
--
-- INSERT INTO "activity_group" (id, category, name, content, status, progress, code, image_url, created_at)
-- VALUES ('2', 'study', '리액트', '리액트 스터디', '활동중', 30, 1111, 'http://image.png', '2022-07-25 18:12:43.000000');

-- Group schedule

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

-- Recruitment
insert into recruitment(start_date, end_date, application_type, target, status, created_at)
values ('2022-02-20 00:00:00.000000', '2022-03-03 00:00:00.000000', 'NORMAL', '1~2학년', '종료', '2022-02-20 00:00:00.000000');
insert into recruitment(start_date, end_date, application_type, target, status, created_at)
values ('2022-08-01 00:00:00.000000', '2022-08-12 00:00:00.000000', 'NORMAL', '1~2학년', '종료', '2022-08-01 00:00:00.000000');
insert into recruitment(start_date, end_date, application_type, target, status, created_at)
values ('2023-11-06 00:00:00.000000', '2023-11-08 00:00:00.000000', 'CORE_TEAM', '2~3학년', '모집중', '2023-11-05 00:00:00.000000');

-- Review
insert into review(member_id, content, is_public, created_at)
values ('admin', '씨랩 화이팅', true, '2022-07-02 12:14:45.000000');
insert into review(member_id, content, is_public, created_at)
values ('admin', '씨랩 화이팅', false, '2022-07-15 07:43:12.000000');
insert into review(member_id, content, is_public, created_at)
values ('201912156', '씨랩 화이팅', false, '2022-08-02 09:46:00.000000');
insert into review(member_id, content, is_public, created_at)
values ('201912033', '씨랩 화이팅', true, '2022-09-26 18:32:00.000000');
insert into review(member_id, content, is_public, created_at)
values ('201912033', '씨랩 화이팅', false, '2022-10-12 23:59:03.000000');

-- Product
insert into product(name, description, url, created_at)
values ('clab-server', '씨랩 공식 홈페이지 백엔드', 'https://github.com/KGU-C-Lab/Clab-Server', '2022-07-02 12:14:45.000000');
insert into product(name, description, url, created_at)
values ('clab-web', '씨랩 공식 홈페이지 프론트엔드', 'https://github.com/KGU-C-Lab/clab-web', '2022-07-15 07:43:12.000000');
insert into product(name, description, url, created_at)
values ('petmily-server', '제10회 소프트웨어 개발보안 시큐어코딩 해커톤', 'https://github.com/KGU-C-Lab/petmily-server', '2022-08-02 09:46:00.000000');
insert into product(name, description, url, created_at)
values ('petmily-web', '제10회 소프트웨어 개발보안 시큐어코딩 해커톤', 'https://github.com/KGU-C-Lab/petmily-web', '2022-09-26 18:32:00.000000');

-- Award
insert into award(competition_name, organizer, award_name, award_date, member_id)
values ('제10회 소프트웨어 개발보안 시큐어코딩 해커톤', '한국정보보호학회', '우수상', '2022-08-02 09:46:00.000000', 'admin');
insert into award(competition_name, organizer, award_name, award_date, member_id)
values ('제10회 소프트웨어 개발보안 시큐어코딩 해커톤', '한국정보보호학회', '우수상', '2022-08-02 09:46:00.000000', '201912156');
insert into award(competition_name, organizer, award_name, award_date, member_id)
values ('제10회 소프트웨어 개발보안 시큐어코딩 해커톤', '한국정보보호학회', '우수상', '2022-08-02 09:46:00.000000', '201912033');

-- WorkExperience
insert into work_experience(company_name, position, start_date, end_date, member_id)
values ('씨랩', '개발자', '2022-08-02 09:46:00.000000', '2022-08-02 09:46:00.000000', 'admin');
insert into work_experience(company_name, position, start_date, end_date, member_id)
values ('씨랩', '개발자', '2022-08-02 09:46:00.000000', '2022-08-02 09:46:00.000000', '201912156');
insert into work_experience(company_name, position, start_date, end_date, member_id)
values ('씨랩', '개발자', '2022-08-02 09:46:00.000000', '2022-08-02 09:46:00.000000', '201912033');

-- Blog
insert into blog(member_id, title, sub_title, content, image_url, tag, update_time, created_at)
values ('admin', '씨랩 화이팅', '씨랩 화이팅', '씨랩 화이팅', 'https://shopping-phinf.pstatic.net/main_3250387/32503877629.20220527022132.jpg?type=w300', '씨랩 화이팅', '2022-07-02 12:14:45.000000', '2022-07-02 12:14:45.000000');
insert into blog(member_id, title, sub_title, content, image_url, tag, update_time, created_at)
values ('admin', '씨랩 화이팅', '씨랩 화이팅', '씨랩 화이팅', 'https://shopping-phinf.pstatic.net/main_3250387/32503877629.20220527022132.jpg?type=w300', '씨랩 화이팅', '2022-07-15 07:43:12.000000', '2022-07-15 07:43:12.000000');
insert into blog(member_id, title, sub_title, content, image_url, tag, update_time, created_at)
values ('201912156', '씨랩 화이팅', '씨랩 화이팅', '씨랩 화이팅', 'https://shopping-phinf.pstatic.net/main_3250387/32503877629.20220527022132.jpg?type=w300', '씨랩 화이팅', '2022-08-02 09:46:00.000000', '2022-08-02 09:46:00.000000');
insert into blog(member_id, title, sub_title, content, image_url, tag, update_time, created_at)
values ('201912033', '씨랩 화이팅', '씨랩 화이팅', '씨랩 화이팅', 'https://shopping-phinf.pstatic.net/main_3250387/32503877629.20220527022132.jpg?type=w300', '씨랩 화이팅', '2022-09-26 18:32:00.000000', '2022-09-26 18:32:00.000000');
insert into blog(member_id, title, sub_title, content, image_url, tag, update_time, created_at)
values ('201912033', '씨랩 화이팅', '씨랩 화이팅', '씨랩 화이팅', 'https://shopping-phinf.pstatic.net/main_3250387/32503877629.20220527022132.jpg?type=w300', '씨랩 화이팅', '2022-10-12 23:59:03.000000', '2022-10-12 23:59:03.000000');