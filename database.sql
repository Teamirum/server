Drop database team;

create database team;

use team;

CREATE TABLE `Member` (
                          `idx` BIGINT auto_increment NOT NULL,
                          `member_id` VARCHAR(100) NOT NULL,
                          `email` VARCHAR(100) NOT NULL,
                          `name` VARCHAR(50) NOT NULL,
                          `password` VARCHAR(255) NOT NULL,
                          `refresh_token` VARCHAR(255),
                          `role` ENUM('ROLE_MEMBER', 'ROLE_ADMIN', 'ROLE_GUEST') NOT NULL,
                          `phone_num` VARCHAR(15) NOT NULL,
                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          PRIMARY KEY (`idx`)
);

CREATE TABLE `Account` (
                           `idx` BIGINT auto_increment NOT NULL,
                           `member_idx` BIGINT NOT NULL,
                           `bank_name` ENUM('bank1', 'bank2') NOT NULL,
                           `number` VARCHAR(20) NOT NULL,
                           `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                           `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                           `fintech_use_num` VARCHAR(100) NULL,
                           `account_status` ENUM('active', 'inactive') NULL COMMENT '연결 되었거나, 직접 등록했는지 여부',
                           `bank_code_std` VARCHAR(50) NULL,
                           `bank_code_sub` VARCHAR(50) NULL,
                           `account_num_masked` VARCHAR(100) NULL,
                           `account_holder_name` VARCHAR(50) NULL,
                           PRIMARY KEY (`idx`),
                           FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`)
);

CREATE TABLE `Credit` (
                          `idx` BIGINT auto_increment NOT NULL,
                          `member_idx` BIGINT NOT NULL,
                          `card_number` VARCHAR(20) NOT NULL,
                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          `card_name` VARCHAR(50) NOT NULL,
                          `company_name` ENUM('KB', 'HYUNDAI') NOT NULL,
                          `company_code` VARCHAR(50) NOT NULL,
                          PRIMARY KEY (`idx`),
                          FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`)
);

CREATE TABLE `Friend` (
                          `idx` BIGINT auto_increment NOT NULL,
                          `member_idx` BIGINT NOT NULL,
                          `friend_member_id` VARCHAR(100) NOT NULL COMMENT '친구관계를 맺을 회원의 ID',
                          `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modified_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          PRIMARY KEY (`idx`),
                          FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`)
);


CREATE TABLE `Market` (
                          `idx` BIGINT auto_increment NOT NULL,
                          `member_idx` BIGINT NOT NULL,
                          `name` VARCHAR(100) NOT NULL,
                          `address` VARCHAR(100) NOT NULL,
                          `kakao_cid` VARCHAR(100) NULL,
                          `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modifiedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          PRIMARY KEY (`idx`),
                          FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`)
);

CREATE TABLE `Order` (
                         `idx` BIGINT auto_increment NOT NULL,
                         `market_idx` BIGINT NOT NULL,
                         `order_id` VARCHAR(100) NOT NULL,
                         `name` VARCHAR(100) NOT NULL,
                         `amount` INT NOT NULL,
                         `tax_free_amount` INT NOT NULL,
                         `vat_amount` INT NULL,
                         `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         PRIMARY KEY (`idx`),
                         FOREIGN KEY (`market_idx`) REFERENCES `Market`(`idx`)
);

CREATE TABLE `SocialPay` (
                             `idx` BIGINT auto_increment NOT NULL,
                             `platform` ENUM('KPAY', 'NPAY') NOT NULL,
                             `aid` VARCHAR(100) NOT NULL,
                             `tid` VARCHAR(100) NOT NULL,
                             `access_token` VARCHAR(255) NULL,
                             PRIMARY KEY (`idx`)
);

CREATE TABLE `BusinessCard` (
                                `idx` BIGINT auto_increment NOT NULL,
                                `member_idx` BIGINT NOT NULL,
                                `name` VARCHAR(50) NOT NULL,
                                `company` VARCHAR(100) NOT NULL,
                                `phone_num` VARCHAR(15) NULL,
                                `email` VARCHAR(100) NULL,
                                `tel_num` VARCHAR(15) NULL,
                                `fax_num` VARCHAR(15) NULL,
                                `part` VARCHAR(50) NULL,
                                `position` VARCHAR(100) NULL,
                                `address` VARCHAR(255) NULL,
                                `img_url` VARCHAR(255) NULL,
                                PRIMARY KEY (`idx`),
                                FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`)
);

CREATE TABLE `MemberBusinessCard` (
                                      `idx` BIGINT auto_increment NOT NULL,
                                      `member_idx` BIGINT NOT NULL,
                                      `businessCard_idx` BIGINT NOT NULL,
                                      `status` ENUM('OWNER', 'NOT_OWNER') NOT NULL COMMENT '본인 명함인지 아닌지 여부',
                                      `memo` VARCHAR(50) NULL,
                                      PRIMARY KEY (`idx`),
                                      FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`),
                                      FOREIGN KEY (`businessCard_idx`) REFERENCES `BusinessCard`(`idx`)
);

CREATE TABLE `Pay` (
                       `idx` BIGINT auto_increment NOT NULL,
                       `member_idx` BIGINT NOT NULL,
                       `order_idx` BIGINT NOT NULL,
                       `card_idx` BIGINT NULL,
                       `account_idx` BIGINT NULL,
                       `socialPay_idx` BIGINT NULL,
                       `pay_method` ENUM('credit', 'debit', 'cash') NOT NULL,
                       `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       `aid` VARCHAR(100) NOT NULL COMMENT '요청 고유번호',
                       `tid` VARCHAR(100) NULL COMMENT '결제 완료 후 응답으로 온 결제 승인번호',
                       `amount` INT NOT NULL COMMENT '각자 결제한 값',
                       `state` ENUM('completed', 'pending', 'failed') NOT NULL,
                       `payType` ENUM('joint', 'single') NOT NULL COMMENT '함께결제, 단일결제',
                       PRIMARY KEY (`idx`),
                       FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`),
                       FOREIGN KEY (`card_idx`) REFERENCES `Credit`(`idx`),
                       FOREIGN KEY (`account_idx`) REFERENCES `Account`(`idx`),
                       FOREIGN KEY (`socialPay_idx`) REFERENCES `SocialPay`(`idx`),
                       FOREIGN KEY (`order_idx`) REFERENCES `Order`(`idx`)

);



CREATE TABLE `transaction` (
                               `idx` BIGINT auto_increment NOT NULL,
                               `member_idx` BIGINT NOT NULL,
                               `card_idx` BIGINT NOT NULL,
                               `socialPay_idx` BIGINT NOT NULL,
                               `account_idx` BIGINT NOT NULL,
                               `time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               `pay_method` ENUM('CARD', 'ACCOUNT', 'NPAY', 'KPAY', 'TogetherPay') NOT NULL,
                               `amount` INT NOT NULL,
                               `memo` VARCHAR(100) NULL,
                               `category` ENUM('food', 'transport', 'entertainment') NULL,
                               `tran_id` VARCHAR(50) NULL COMMENT '마이데이터 연동시 넘어오는 번호',
                               PRIMARY KEY (`idx`),
                               FOREIGN KEY (`member_idx`) REFERENCES `Member`(`idx`),
                               FOREIGN KEY (`card_idx`) REFERENCES `Credit`(`idx`),
                               FOREIGN KEY (`account_idx`) REFERENCES `Account`(`idx`),
                               FOREIGN KEY (`socialPay_idx`) REFERENCES `SocialPay`(`idx`)
);
