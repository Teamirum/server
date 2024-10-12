-- 데이터베이스 초기화
DROP DATABASE IF EXISTS team;
CREATE DATABASE team;
USE team;
-- Member 테이블
CREATE TABLE `Member` (
                          `idx` BIGINT AUTO_INCREMENT NOT NULL,
                          `member_id` VARCHAR(100) NOT NULL,
                          `email` VARCHAR(100) NOT NULL,
                          `name` VARCHAR(50) NOT NULL,
                          `password` VARCHAR(255) NOT NULL,
                          `refresh_token` VARCHAR(255),
                          `role` ENUM('ROLE_MEMBER', 'ROLE_ADMIN', 'ROLE_GUEST') NOT NULL,
                          `phone_num` VARCHAR(20) NOT NULL,
                          `social_type` ENUM('KAKAO', 'NAVER', 'GOOGLE', 'LOCAL') NOT NULL,
                          `is_connected` BOOLEAN NOT NULL DEFAULT FALSE,
                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          PRIMARY KEY (`idx`)
);
-- Account 테이블
CREATE TABLE `Account` (
                           `idx` BIGINT AUTO_INCREMENT NOT NULL,
                           `member_idx` BIGINT NOT NULL,
                           `account_holder_name` VARCHAR(50) NOT NULL,
                           `amount` INT NOT NULL,
                           `bank_name` VARCHAR(20) NOT NULL,
                           `account_number` VARCHAR(20) NOT NULL,
                           `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                           `account_secret` VARCHAR(20) NOT NULL,
                           PRIMARY KEY (`idx`),
                           UNIQUE (`account_number`),
                           FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE
);

-- AccountHistory 테이블
CREATE TABLE `AccountHistory` (
                                  `idx` BIGINT AUTO_INCREMENT NOT NULL,
                                  `account_idx` BIGINT NOT NULL,
                                  `account_number` VARCHAR(20) NOT NULL,
                                  `account_history_type` ENUM('SEND', 'GET') NOT NULL,
                                  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                  `amount` BIGINT NOT NULL,
                                  `remain_amount` BIGINT NOT NULL,
                                  `name` VARCHAR(100) NOT NULL,
                                  PRIMARY KEY (`idx`),
                                  FOREIGN KEY (`account_idx`) REFERENCES `Account`(`idx`) ON DELETE CASCADE,
                                  FOREIGN KEY (`account_number`) REFERENCES `Account`(`account_number`) ON DELETE CASCADE -- account_number를 참조
);

-- Credit 테이블
CREATE TABLE `Credit` (
                          `idx` BIGINT AUTO_INCREMENT NOT NULL,
                          `member_idx` BIGINT NOT NULL,
                          `credit_number` VARCHAR(250) NOT NULL,
                          `credit_name` VARCHAR(50) NOT NULL,
                          `company_name` VARCHAR(20) NOT NULL,
                          `credit_secret` VARCHAR(20) NOT NULL,
                          `amount_sum` BIGINT NOT NULL,
                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `expiration_date` VARCHAR(20) NOT NULL,
                          PRIMARY KEY (`idx`),
                          FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE
);
-- CreditHistory 테이블
CREATE TABLE `CreditHistory` (
                                 `idx` BIGINT AUTO_INCREMENT NOT NULL,
                                 `credit_idx` BIGINT NOT NULL,
                                 `credit_number` VARCHAR(250) NOT NULL,
                                 `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 `amount` BIGINT NOT NULL,
                                 `amount_sum` BIGINT NOT NULL,
                                 `name` VARCHAR(100) NOT NULL,
                                 PRIMARY KEY (`idx`),
                                 FOREIGN KEY (`credit_idx`) REFERENCES `Credit`(`idx`) ON DELETE CASCADE
);

-- Market 테이블
CREATE TABLE `Market` (
                          `idx` BIGINT AUTO_INCREMENT NOT NULL,
                          `member_idx` BIGINT NOT NULL,
                          `name` VARCHAR(100) NOT NULL,
                          `address` VARCHAR(100) NOT NULL,
                          `max_table_cnt` INT NOT NULL,
                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          PRIMARY KEY (`idx`),
                          FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE
);

-- Order 테이블
CREATE TABLE `Order` (
                         `idx` BIGINT AUTO_INCREMENT NOT NULL,
                         `market_idx` BIGINT NOT NULL,
                         `name` VARCHAR(100) NOT NULL,
                         `total_price` INT NOT NULL,
                         `tax_free_price` INT NOT NULL,
                         `vat_price` INT NOT NULL,
                         `table_number` INT NOT NULL,
                         `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         PRIMARY KEY (`idx`),
                         FOREIGN KEY (`market_idx`) REFERENCES `Market`(`idx`) ON DELETE CASCADE
);

-- TogetherOrder 테이블
CREATE TABLE `TogetherOrder` (
                                 `idx` BIGINT AUTO_INCREMENT NOT NULL,
                                 `order_idx` BIGINT NOT NULL,
                                 `price` INT NOT NULL,
                                 `member_idx` BIGINT NOT NULL,
                                 `status` ENUM('WAIT', 'CANCEL', 'COMPLETE') NOT NULL,
                                 `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                 PRIMARY KEY (`idx`),
                                 FOREIGN KEY (`order_idx`) REFERENCES `Order`(`idx`) ON DELETE CASCADE,
                                 FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE
);

-- Menu 테이블
CREATE TABLE `Menu` (
                        `idx` BIGINT AUTO_INCREMENT NOT NULL,
                        `market_idx` BIGINT NOT NULL,
                        `name` VARCHAR(100) NOT NULL,
                        `price` INT NOT NULL,
                        `menu_type` ENUM('FOOD', 'DRINK', 'MAIN') NOT NULL,
                        PRIMARY KEY (`idx`),
                        FOREIGN KEY (`market_idx`) REFERENCES `Market`(`idx`) ON DELETE CASCADE
);

-- OrderMenu 테이블
CREATE TABLE `OrderMenu` (
                             `idx` BIGINT AUTO_INCREMENT NOT NULL,
                             `order_idx` BIGINT NOT NULL,
                             `menu_idx` BIGINT NOT NULL,
                             `menu_name` VARCHAR(100) NOT NULL,
                             `price` INT NOT NULL,
                             `amount` INT NOT NULL,
                             PRIMARY KEY (`idx`),
                             FOREIGN KEY (`order_idx`) REFERENCES `Order`(`idx`) ON DELETE CASCADE,
                             FOREIGN KEY (`menu_idx`) REFERENCES `Menu`(`idx`) ON DELETE CASCADE
);

-- OrderRoom 테이블

CREATE TABLE `OrderRoom` (
                             `idx` BIGINT AUTO_INCREMENT NOT NULL,
                             `order_idx` BIGINT NOT NULL,
                             `owner_member_idx` VARCHAR(100) NOT NULL,
                             `max_member_cnt` INT NOT NULL,
                             `member_cnt` INT NOT NULL,
                             `total_price` INT NOT NULL,
                             `current_price` INT NOT NULL,
                             `type` ENUM('BY_PRICE', 'BY_MENU') NOT NULL,
                             `ready_cnt` INT NOT NULL,
                             `status` ENUM('ACTIVE', 'CANCEL', 'COMPLETE') NOT NULL,
                             `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                             PRIMARY KEY (`idx`),
                             FOREIGN KEY (`order_idx`) REFERENCES `Order`(`idx`) ON DELETE CASCADE
);

-- BusinessCard 테이블
CREATE TABLE `BusinessCard` (
                                `idx` BIGINT AUTO_INCREMENT NOT NULL,
                                `member_idx` BIGINT NOT NULL,
                                `name` VARCHAR(50) NOT NULL,
                                `company` VARCHAR(100) NOT NULL,
                                `phone_num` VARCHAR(15),
                                `email` VARCHAR(100),
                                `tel_num` VARCHAR(100),
                                `part` VARCHAR(50),
                                `position` VARCHAR(100),
                                `address` VARCHAR(255),
                                `memo` VARCHAR(255),
                                `img_url` VARCHAR(255),
                                PRIMARY KEY (`idx`),
                                FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE
);

-- MemberBusinessCard 테이블
CREATE TABLE `MemberBusinessCard` (
                                      `idx` BIGINT AUTO_INCREMENT NOT NULL,
                                      `member_idx` BIGINT NOT NULL,
                                      `businessCard_idx` BIGINT NOT NULL,
                                      `status` ENUM('OWNER', 'NOT_OWNER') NOT NULL,
                                      `memo` VARCHAR(50),
                                      PRIMARY KEY (`idx`),
                                      FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE,
                                      FOREIGN KEY (`businessCard_idx`) REFERENCES `BusinessCard`(`idx`) ON DELETE CASCADE
);

-- Pay 테이블
CREATE TABLE `Pay` (
                       `idx` BIGINT AUTO_INCREMENT NOT NULL,
                       `member_idx` BIGINT NOT NULL,
                       `order_idx` BIGINT NOT NULL,
                       `credit_idx` BIGINT,
                       `account_idx` BIGINT,
                       `pay_method` ENUM('CREDIT', 'ACCOUNT') NOT NULL,
                       `tid` VARCHAR(100) NOT NULL,
                       `price` INT NOT NULL,
                       `pay_status` ENUM('ACCEPT', 'WAIT', 'CANCEL', 'FAIL') NOT NULL,
                       `pay_type` ENUM('TOGETHER', 'ALONE') NOT NULL,
                       `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                       PRIMARY KEY (`idx`),
                       FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE,
                       FOREIGN KEY (`credit_idx`) REFERENCES `Credit`(`idx`) ON DELETE CASCADE,
                       FOREIGN KEY (`account_idx`) REFERENCES `Account`(`idx`) ON DELETE CASCADE,
                       FOREIGN KEY (`order_idx`) REFERENCES `Order`(`idx`) ON DELETE CASCADE
);

-- Transaction 테이블
CREATE TABLE `Transaction` (
                               `idx` BIGINT AUTO_INCREMENT NOT NULL,
                               `member_idx` BIGINT NOT NULL,
                               `credit_idx` BIGINT,
                               `credit_number` VARCHAR(250),
                               `account_idx` BIGINT,
                               `account_number` VARCHAR(20),
                               `time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               `pay_method` ENUM('CARD', 'ACCOUNT') NOT NULL,
                               `amount` INT NOT NULL,
                               `memo` VARCHAR(100),
                               `category` ENUM('SALARY','INTEREST','ALLOWANCE','FOOD','SHOPPING','TRANSPORT','ENTERTAINMENT','COMMUNICATION','UNCATEGORIZED') NOT NULL,
                               `tran_id` VARCHAR(50),
                               PRIMARY KEY (`idx`),
                               FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE,
                               FOREIGN KEY (`credit_idx`) REFERENCES `Credit`(`idx`) ON DELETE CASCADE,
                               FOREIGN KEY (`account_idx`) REFERENCES `Account`(`idx`) ON DELETE CASCADE
);


INSERT INTO team.Member (idx, member_id, email, name, password, refresh_token, role, phone_num, social_type, is_connected, created_at, modified_at) VALUES (1, 'test@gmail.com', 'test@gmail.com', '백도현', '{bcrypt}$2a$10$ekgEdCSr0NpYqrkQhwAMseO3YcO3PIDMTU9BjYI88iOviGTu1V4bi', 'eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJSZWZyZXNoVG9rZW4iLCJpYXQiOjE3Mjg2MDgxMjcsIm1lbWJlcklkIjoidGVzdEBnbWFpbC5jb20iLCJpc1Jldm9rZSI6ZmFsc2UsImV4cCI6MTczMTIwMDEyN30.U7fmVXHuUgDwNRnkvpJaD6D1AUNY14ReHYO7dXxZXU0c7tM-gIos5kGjLEeC9THd7vN-X3TKi7UeTLjYN7OKOw', 'ROLE_ADMIN', '01054888620', 'LOCAL', 0, '2024-10-09 07:20:05', '2024-10-11 09:55:27');
INSERT INTO team.Member (idx, member_id, email, name, password, refresh_token, role, phone_num, social_type, is_connected, created_at, modified_at) VALUES (2, 'test2@gmail.com', 'test2@gmail.com', '백도현2', '{bcrypt}$2a$10$yOWyHRg7ujr05VSY/acwLu9dHMTIpPMSwg3gIjVx3S1/AvC/gb8K2', null, 'ROLE_MEMBER', '01054888621', 'LOCAL', 0, '2024-10-10 00:27:28', '2024-10-10 00:27:28');
INSERT INTO team.Member (idx, member_id, email, name, password, refresh_token, role, phone_num, social_type, is_connected, created_at, modified_at) VALUES (3, 'test3@gmail.com', 'test3@gmail.com', '썩언니', '{bcrypt}$2a$10$r0ORzMIqBH5qNocR1z70gOZ5BVLopHpqYmrT5QpQR0hHQHHKIVM56', null, 'ROLE_MEMBER', '01012341234', 'LOCAL', 0, '2024-10-10 06:05:27', '2024-10-10 06:05:27');

INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (1, 1, '5521-1234-5678-9012', 'KB 국민카드', 'KB', '1234', 12000, '2024-10-09 16:56:26', '12-25');
INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (2, 1, '5365-2345-6789-0123', 'KB 국민카드', 'KB', '5678', 48300, '2024-10-09 16:56:37', '11-26');
INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (3, 1, '4581-3456-7890-1234', '우리카드', 'Woori', '9876', 10000, '2024-10-09 16:56:44', '08-27');
INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (4, 1, '5123-4567-8901-2345', '우리카드', 'Woori', '4321', 6000, '2024-10-09 16:56:53', '05-28');
INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (5, 1, '4074-5678-9012-3456', '카카오뱅크 카드', 'KakaoBank', '5555', 2578, '2024-10-09 16:57:01', '09-29');
INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (6, 1, '3569-6789-0123-4567', '하나카드', 'Hana', '1111', 7, '2024-10-09 16:57:09', '07-30');
INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (7, 1, '1234-5678-9012-3456', '신한카드', 'Shinhan', '2222', 0, '2024-10-09 16:57:17', '06-31');
INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (8, 1, '2345-6789-0123-4567', '삼성카드', 'Samsung', '3333', 0, '2024-10-09 16:57:25', '05-32');
INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (9, 1, '3456-7890-1234-5678', '롯데카드', 'Lotte', '4444', 0, '2024-10-09 16:57:33', '04-33');
INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (10, 1, '4567-8901-2345-6789', '현대카드', 'Hyundai', '5555', 0, '2024-10-09 16:57:41', '03-34');
INSERT INTO team.Credit (idx, member_idx, credit_number, credit_name, company_name, credit_secret, amount_sum, created_at, expiration_date) VALUES (11, 1, '5678-9012-3456-7890', '신한카드', 'Shinhan', '6666', 0, '2024-10-09 16:57:49', '02-35');

INSERT INTO team.Account (idx, member_idx, account_holder_name, amount, bank_name, account_number, created_at, account_secret) VALUES (1, 1, '서석현', 19890000, '국민은행', '123-456789-012', '2024-10-09 16:52:23', '111111');
INSERT INTO team.Account (idx, member_idx, account_holder_name, amount, bank_name, account_number, created_at, account_secret) VALUES (2, 1, '조현아', 1669789, '하나은행', '321-654321-12345', '2024-10-09 16:52:37', '222222');
INSERT INTO team.Account (idx, member_idx, account_holder_name, amount, bank_name, account_number, created_at, account_secret) VALUES (3, 1, '백도현', 900000, '카카오뱅크', '9876-12-345678', '2024-10-09 16:52:54', '333333');
INSERT INTO team.Account (idx, member_idx, account_holder_name, amount, bank_name, account_number, created_at, account_secret) VALUES (4, 1, '이동훈', 240211, '우리은행', '543-210987-654', '2024-10-09 16:53:13', '444444');
INSERT INTO team.Account (idx, member_idx, account_holder_name, amount, bank_name, account_number, created_at, account_secret) VALUES (5, 1, '이학민', 50181818, '국민은행', '234-567890-123', '2024-10-09 16:53:27', '555555');
INSERT INTO team.Account (idx, member_idx, account_holder_name, amount, bank_name, account_number, created_at, account_secret) VALUES (6, 1, '공희진', 568182, '국민은행', '345-678901-234', '2024-10-09 16:53:39', '666666');

INSERT INTO team.Transaction (idx, member_idx, credit_idx, credit_number, account_idx, account_number, time, pay_method, amount, memo, category, tran_id) VALUES (1, 1, null, null, 6, '345-678901-234', '2024-10-09 16:54:16', 'ACCOUNT', 181818, '학파민', 'UNCATEGORIZED', null);
INSERT INTO team.Transaction (idx, member_idx, credit_idx, credit_number, account_idx, account_number, time, pay_method, amount, memo, category, tran_id) VALUES (2, 1, null, null, 1, '123-456789-012', '2024-10-09 16:55:12', 'ACCOUNT', 110000, '아아', 'UNCATEGORIZED', null);
INSERT INTO team.Transaction (idx, member_idx, credit_idx, credit_number, account_idx, account_number, time, pay_method, amount, memo, category, tran_id) VALUES (3, 1, null, null, 4, '543-210987-654', '2024-10-09 16:55:30', 'ACCOUNT', 1890, 'gkgk', 'UNCATEGORIZED', null);
INSERT INTO team.Transaction (idx, member_idx, credit_idx, credit_number, account_idx, account_number, time, pay_method, amount, memo, category, tran_id) VALUES (4, 1, null, null, 4, '543-210987-654', '2024-10-09 16:55:46', 'ACCOUNT', 57899, 'dmfb', 'UNCATEGORIZED', null);
INSERT INTO team.Transaction (idx, member_idx, credit_idx, credit_number, account_idx, account_number, time, pay_method, amount, memo, category, tran_id) VALUES (5, 1, 1, '5521-1234-5678-9012', null, null, '2024-10-09 16:58:35', 'CARD', 10000, '썩쇼핑', 'ENTERTAINMENT', null);
INSERT INTO team.Transaction (idx, member_idx, credit_idx, credit_number, account_idx, account_number, time, pay_method, amount, memo, category, tran_id) VALUES (6, 1, 1, '5521-1234-5678-9012', null, null, '2024-10-09 16:58:50', 'CARD', 2000, '썩백화점', 'ENTERTAINMENT', null);
INSERT INTO team.Transaction (idx, member_idx, credit_idx, credit_number, account_idx, account_number, time, pay_method, amount, memo, category, tran_id) VALUES (7, 1, 2, '5365-2345-6789-0123', null, null, '2024-10-09 16:59:04', 'CARD', 48000, '조아가전제품', 'ENTERTAINMENT', null);
INSERT INTO team.Transaction (idx, member_idx, credit_idx, credit_number, account_idx, account_number, time, pay_method, amount, memo, category, tran_id) VALUES (8, 1, 2, '5365-2345-6789-0123', null, null, '2024-10-09 16:59:25', 'CARD', 100, '조아의류', 'ENTERTAINMENT', null);
INSERT INTO team.Transaction (idx, member_idx, credit_idx, credit_number, account_idx, account_number, time, pay_method, amount, memo, category, tran_id) VALUES (9, 1, 2, '5365-2345-6789-0123', null, null, '2024-10-09 17:00:07', 'CARD', 200, '조아음식', 'ENTERTAINMENT', null);
INSERT INTO team.Transaction (idx, member_idx, credit_idx, credit_number, account_idx, account_number, time, pay_method, amount, memo, category, tran_id) VALUES (10, 1, 3, '4581-3456-7890-1234', null, null, '2024-10-09 17:00:33', 'CARD', 10000, '훈음료', 'ENTERTAINMENT', null);

