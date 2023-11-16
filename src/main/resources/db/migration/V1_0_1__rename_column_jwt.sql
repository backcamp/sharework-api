-- 'jwt_token' 컬럼 이름 변경
ALTER TABLE users RENAME COLUMN jwt_token TO jwt;
