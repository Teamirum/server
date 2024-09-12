CREATE TABLE `Member` (
                          `idx` BIGINT auto_increment NOT NULL,
                          `memberId` VARCHAR(100) NOT NULL,
                          `email` VARCHAR(100) NOT NULL,
                          `password` VARCHAR(255) NOT NULL,
                          `role` ENUM('user', 'admin') NOT NULL,
                          `profileImg` VARCHAR(255) NULL,
                          `phoneNum` VARCHAR(15) NOT NULL,
                          `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modifiedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          PRIMARY KEY (`idx`)
);

CREATE TABLE `Account` (
                           `idx` BIGINT auto_increment NOT NULL,
                           `memberIdx` BIGINT NOT NULL,
                           `bank_name` ENUM('bank1', 'bank2') NOT NULL,
                           `number` VARCHAR(20) NOT NULL,
                           `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                           `modifiedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                           `fintech_use_num` VARCHAR(100) NULL,
                           `account_status` ENUM('active', 'inactive') NULL COMMENT '연결 되었거나, 직접 등록했는지 여부',
                           `bank_code_std` VARCHAR(50) NULL,
                           `bank_code_sub` VARCHAR(50) NULL,
                           `account_num_masked` VARCHAR(100) NULL,
                           `account_holder_name` VARCHAR(50) NULL,
                           PRIMARY KEY (`idx`),
                           FOREIGN KEY (`memberIdx`) REFERENCES `Member`(`idx`)
);

CREATE TABLE `Credit` (
                          `idx` BIGINT auto_increment NOT NULL,
                          `idx2` BIGINT NOT NULL,
                          `number` VARCHAR(20) NOT NULL,
                          `createAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modifiedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          `card_name` VARCHAR(50) NOT NULL,
                          `company_name` ENUM('company1', 'company2') NOT NULL,
                          `company_code` VARCHAR(50) NOT NULL,
                          PRIMARY KEY (`idx`)
);

CREATE TABLE `Friend` (
                          `idx` BIGINT auto_increment NOT NULL,
                          `memberIdx` BIGINT NOT NULL,
                          `friendMemberId` VARCHAR(100) NOT NULL COMMENT '친구관계를 맺을 회원의 ID',
                          `friendStatus` ENUM('pending', 'accepted') NULL COMMENT '수락 대기중, 수락 완료',
                          `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modifiedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          PRIMARY KEY (`idx`),
                          FOREIGN KEY (`memberIdx`) REFERENCES `Member`(`idx`)
);

CREATE TABLE `Order` (
                         `idx` BIGINT auto_increment NOT NULL,
                         `market_idx` BIGINT NOT NULL,
                         `order_id` VARCHAR(100) NOT NULL,
                         `name` VARCHAR(100) NOT NULL,
                         `amount` INT NOT NULL,
                         `tax_free_amount` INT NOT NULL,
                         `vat_amount` INT NULL,
                         `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         PRIMARY KEY (`idx`)
);

CREATE TABLE `Pay` (
                       `idx` BIGINT auto_increment NOT NULL,
                       `memberIdx` BIGINT NOT NULL,
                       `payIdx` BIGINT NOT NULL,
                       `cardIdx` BIGINT NULL,
                       `accountIdx` BIGINT NULL,
                       `sociaPayIdx` BIGINT NULL,
                       `payMethod` ENUM('credit', 'debit', 'cash') NOT NULL,
                       `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       `aid` VARCHAR(100) NOT NULL COMMENT '요청 고유번호',
                       `tid` VARCHAR(100) NULL COMMENT '결제 완료 후 응답으로 온 결제 승인번호',
                       `amount` INT NOT NULL COMMENT '각자 결제한 값',
                       `state` ENUM('completed', 'pending', 'failed') NOT NULL,
                       `payType` ENUM('joint', 'single') NOT NULL COMMENT '함께결제, 단일결제',
                       PRIMARY KEY (`idx`),
                       FOREIGN KEY (`memberIdx`) REFERENCES `Member`(`idx`)
);

CREATE TABLE `BusinessCard` (
                                `idx` BIGINT auto_increment NOT NULL,
                                `memberIdx` BIGINT NOT NULL,
                                `name` VARCHAR(50) NOT NULL,
                                `company` VARCHAR(100) NOT NULL,
                                `phoneNum` VARCHAR(15) NULL,
                                `email` VARCHAR(100) NULL,
                                `telNum` VARCHAR(15) NULL,
                                `faxNum` VARCHAR(15) NULL,
                                `part` VARCHAR(50) NULL,
                                `position` VARCHAR(100) NULL,
                                `address` VARCHAR(255) NULL,
                                `imgUrl` VARCHAR(255) NULL,
                                PRIMARY KEY (`idx`),
                                FOREIGN KEY (`memberIdx`) REFERENCES `Member`(`idx`)
);

CREATE TABLE `MemberBusinessCard` (
                                      `idx` BIGINT auto_increment NOT NULL,
                                      `memberIdx` BIGINT NOT NULL,
                                      `businessCardIdx` BIGINT NOT NULL,
                                      `status` ENUM('own', 'not_own') NOT NULL COMMENT '본인 명함인지 아닌지 여부',
                                      `memo` VARCHAR(50) NULL,
                                      PRIMARY KEY (`idx`),
                                      FOREIGN KEY (`memberIdx`) REFERENCES `Member`(`idx`),
                                      FOREIGN KEY (`businessCardIdx`) REFERENCES `BusinessCard`(`idx`)
);

CREATE TABLE `Market` (
                          `idx` BIGINT auto_increment NOT NULL,
                          `memberIdx` BIGINT NOT NULL,
                          `name` VARCHAR(100) NOT NULL,
                          `address` VARCHAR(100) NOT NULL,
                          `kakao_cid` VARCHAR(100) NULL,
                          `createdAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                          `modifiedAt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
                          PRIMARY KEY (`idx`),
                          FOREIGN KEY (`memberIdx`) REFERENCES `Member`(`idx`)
);

CREATE TABLE `transaction` (
                               `idx` BIGINT auto_increment NOT NULL,
                               `memberIdx` BIGINT NOT NULL,
                               `cardIdx` BIGINT NOT NULL,
                               `socialPayIdx` BIGINT NOT NULL,
                               `accountIdx` BIGINT NOT NULL,
                               `time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               `payMethod` ENUM('credit', 'debit') NOT NULL,
                               `amount` INT NOT NULL,
                               `memo` VARCHAR(100) NULL,
                               `category` ENUM('food', 'transport', 'entertainment') NULL,
                               `tran_id` VARCHAR(50) NULL COMMENT '마이데이터 연동시 넘어오는 번호',
                               PRIMARY KEY (`idx`),
                               FOREIGN KEY (`memberIdx`) REFERENCES `Member`(`idx`)
);

CREATE TABLE `SocialPay` (
                             `idx` BIGINT auto_increment NOT NULL,
                             `platform` ENUM('platform1', 'platform2') NOT NULL,
                             `aid` VARCHAR(100) NOT NULL,
                             `tid` VARCHAR(100) NOT NULL,
                             `accessToken` VARCHAR(255) NULL,
                             PRIMARY KEY (`idx`)
);
