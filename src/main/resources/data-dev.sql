INSERT INTO members (id, email, password, role, created_at, is_deleted)
VALUES (1, 'admin@example.com', '1234', 'ADMIN', now(), false);

INSERT INTO members (id, email, password, role, created_at, is_deleted)
VALUES (2, 'user1@example.com', '1234', 'USER', now(), false);

INSERT INTO posts (id, title, content, member_id, created_at, is_deleted)
VALUES (1, '테스트 글', '내용입니다', 2, now(), false);