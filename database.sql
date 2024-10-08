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
                          `kakao_cid` VARCHAR(100),
                          `maxTable_cnt` INT NOT NULL,
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
                                 `member_idx` BIGINT NOT NULL,
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
                            `member_cnt` INT NOT NULL,
                            `amount` INT NOT NULL,
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
                       `pay_method` ENUM('CARD', 'ACCOUNT') NOT NULL,
                       `tid` VARCHAR(100) NOT NULL,
                       `amount` INT NOT NULL,
                       `state` ENUM('ACCEPT', 'WAIT', 'CANCEL', 'FAIL') NOT NULL,
                       `payType` ENUM('TOGETHER', 'ALONE') NOT NULL,
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
                               `category` ENUM('FOOD', 'TRANSPORT', 'ENTERTAINMENT') NOT NULL,
                               `tran_id` VARCHAR(50),
                               PRIMARY KEY (`idx`),
                               FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE,
                               FOREIGN KEY (`credit_idx`) REFERENCES `Credit`(`idx`) ON DELETE CASCADE,
                               FOREIGN KEY (`account_idx`) REFERENCES `Account`(`idx`) ON DELETE CASCADE
);
