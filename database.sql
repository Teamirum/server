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
                          `phone_num` VARCHAR(20),
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
                           PRIMARY KEY (`idx`),
                           FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE
);

-- Credit 테이블
CREATE TABLE `Credit` (
                          `idx` BIGINT AUTO_INCREMENT NOT NULL,
                          `member_idx` BIGINT NOT NULL,
                          `card_number` VARCHAR(250) NOT NULL,
                          `card_name` VARCHAR(50) NOT NULL,
                          `company_name` VARCHAR(20) NOT NULL,
                          `company_code` VARCHAR(50) NOT NULL,
                          `amount_sum` INT NOT NULL,
                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          PRIMARY KEY (`idx`),
                          FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE
);

-- CreditHistory 테이블
CREATE TABLE `CreditHistory` (
                                 `idx` BIGINT AUTO_INCREMENT NOT NULL,
                                 `credit_idx` BIGINT NOT NULL,
                                 `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '거래시각',
                                 `amount` BIGINT NOT NULL COMMENT '거래금액',
                                 `amount_sum` BIGINT NOT NULL COMMENT '누적거래금액',
                                 `name` VARCHAR(100) NOT NULL COMMENT '거래명',
                                 PRIMARY KEY (`idx`),
                                 FOREIGN KEY (`credit_idx`) REFERENCES `Credit`(`idx`) ON DELETE CASCADE
);

-- Friend 테이블
CREATE TABLE `Friend` (
                          `idx` BIGINT AUTO_INCREMENT NOT NULL,
                          `member_idx` BIGINT NOT NULL,
                          `friend_member_id` VARCHAR(100) NOT NULL COMMENT '친구관계를 맺을 회원의 ID',
                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `is_favorite` BOOLEAN DEFAULT FALSE,
                          PRIMARY KEY (`idx`),
                          FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE
);

-- Market 테이블
CREATE TABLE `Market` (
                          `idx` BIGINT AUTO_INCREMENT NOT NULL,
                          `member_idx` BIGINT NOT NULL,
                          `name` VARCHAR(100) NOT NULL,
                          `address` VARCHAR(100) NOT NULL,
                          `kakao_cid` VARCHAR(100),
                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          PRIMARY KEY (`idx`),
                          FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE
);

-- Order 테이블
CREATE TABLE `Order` (
                         `idx` BIGINT AUTO_INCREMENT NOT NULL,
                         `market_idx` BIGINT NOT NULL,
                         `order_id` VARCHAR(100) NOT NULL,
                         `name` VARCHAR(100) NOT NULL,
                         `amount` INT NOT NULL,
                         `tax_free_amount` INT NOT NULL,
                         `vat_amount` INT,
                         `order_member_idx` BIGINT NOT NULL,
                         `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         PRIMARY KEY (`idx`),
                         FOREIGN KEY (`market_idx`) REFERENCES `Market`(`idx`) ON DELETE CASCADE
);

-- SocialPay 테이블
CREATE TABLE `SocialPay` (
                             `idx` BIGINT AUTO_INCREMENT NOT NULL,
                             `platform` ENUM('KPAY', 'NPAY', 'TOSSPAY') NOT NULL,
                             `aid` VARCHAR(100) NOT NULL,
                             `tid` VARCHAR(100) NOT NULL,
                             `access_token` VARCHAR(255),
                             PRIMARY KEY (`idx`)
);

-- BusinessCard 테이블
CREATE TABLE `BusinessCard` (
                                `idx` BIGINT AUTO_INCREMENT NOT NULL,
                                `member_idx` BIGINT NOT NULL,
                                `name` VARCHAR(50) NOT NULL,
                                `company` VARCHAR(100) NOT NULL,
                                `phone_num` VARCHAR(15),
                                `email` VARCHAR(100),
                                `tel_num` VARCHAR(15),
                                `fax_num` VARCHAR(15),
                                `part` VARCHAR(50),
                                `position` VARCHAR(100),
                                `address` VARCHAR(255),
                                `img_url` VARCHAR(255),
                                PRIMARY KEY (`idx`),
                                FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE
);

-- MemberBusinessCard 테이블
CREATE TABLE `MemberBusinessCard` (
                                      `idx` BIGINT AUTO_INCREMENT NOT NULL,
                                      `member_idx` BIGINT NOT NULL,
                                      `businessCard_idx` BIGINT NOT NULL,
                                      `status` ENUM('OWNER', 'NOT_OWNER') NOT NULL COMMENT '본인 명함인지 아닌지 여부',
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
                       `card_idx` BIGINT,
                       `account_idx` BIGINT,
                       `pay_method` ENUM('credit', 'debit', 'cash') NOT NULL,
                       `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       `aid` VARCHAR(100) NOT NULL COMMENT '요청 고유번호',
                       `tid` VARCHAR(100) COMMENT '결제 완료 후 응답으로 온 결제 승인번호',
                       `amount` INT NOT NULL COMMENT '각자 결제한 값',
                       `state` ENUM('completed', 'pending', 'failed') NOT NULL,
                       `payType` ENUM('joint', 'single') NOT NULL COMMENT '함께결제, 단일결제',
                       PRIMARY KEY (`idx`),
                       FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE,
                       FOREIGN KEY (`card_idx`) REFERENCES `Credit`(`idx`) ON DELETE CASCADE,
                       FOREIGN KEY (`account_idx`) REFERENCES `Account`(`idx`) ON DELETE CASCADE,
                       FOREIGN KEY (`order_idx`) REFERENCES `Order`(`idx`) ON DELETE CASCADE
);

-- TogetherPay 테이블
CREATE TABLE `TogetherPay` (
                               `idx` BIGINT AUTO_INCREMENT NOT NULL,
                               `member_idx` BIGINT NOT NULL,
                               `order_idx` BIGINT NOT NULL,
                               `pay_idx` BIGINT NOT NULL,
                               `member_cnt` INT NOT NULL,
                               `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               PRIMARY KEY (`idx`),
                               FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE,
                               FOREIGN KEY (`order_idx`) REFERENCES `Order`(`idx`) ON DELETE CASCADE,
                               FOREIGN KEY (`pay_idx`) REFERENCES `Pay`(`idx`) ON DELETE CASCADE
);

-- Transaction 테이블
CREATE TABLE `Transaction` (
                               `idx` BIGINT AUTO_INCREMENT NOT NULL,
                               `member_idx` BIGINT NOT NULL,
                               `card_idx` BIGINT NOT NULL,
                               `socialPay_idx` BIGINT NOT NULL,
                               `account_idx` BIGINT NOT NULL,
                               `time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               `pay_method` ENUM('CARD', 'ACCOUNT', 'NPAY', 'KPAY', 'TogetherPay') NOT NULL,
                               `amount` INT NOT NULL,
                               `memo` VARCHAR(100),
                               `category` ENUM('food', 'transport', 'entertainment'),
                               `tran_id` VARCHAR(50) COMMENT '마이데이터 연동시 넘어오는 번호',
                               PRIMARY KEY (`idx`),
                               FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`) ON DELETE CASCADE,
                               FOREIGN KEY (`card_idx`) REFERENCES `Credit`(`idx`) ON DELETE CASCADE,
                               FOREIGN KEY (`account_idx`) REFERENCES `Account`(`idx`) ON DELETE CASCADE,
                               FOREIGN KEY (`socialPay_idx`) REFERENCES `SocialPay`(`idx`) ON DELETE CASCADE
);
