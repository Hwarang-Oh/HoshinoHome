-- Active: 1710890946343@@127.0.0.1@3306@hoshinohome''
CREATE SCHEMA IF NOT EXISTS `hoshinoHome` DEFAULT CHARACTER SET utf8;

use hoshinohome;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`dong`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `dong`;

CREATE TABLE `dong` (
    `dong_code` varchar(10) NOT NULL, `si_do_name` varchar(30) DEFAULT NULL, `gu_gun_Name` varchar(30) DEFAULT NULL, `dong_name` varchar(30) DEFAULT NULL, PRIMARY KEY (`dong_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`housedeal`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `housedeal`;

CREATE TABLE `housedeal` (
    `deal_code` bigint NOT NULL, `deal_amount` varchar(20) DEFAULT NULL, `deal_year` int DEFAULT NULL, `deal_month` int DEFAULT NULL, `deal_day` int DEFAULT NULL, `area` varchar(20) DEFAULT NULL, `floor` varchar(4) DEFAULT NULL, `cancel_deal_type` varchar(1) DEFAULT NULL, `apt_code` bigint DEFAULT NULL, PRIMARY KEY (`deal_code`), KEY `housedeal_apt_code_houseinfo_apt_code_fk_idx` (`apt_code`), CONSTRAINT `housedeal_apt_code_houseinfo_apt_code_fk` FOREIGN KEY (`apt_code`) REFERENCES `houseinfo` (`apt_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`houseinfo`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `houseinfo`;

CREATE TABLE `houseinfo` (
    `apt_code` bigint NOT NULL, `build_year` int DEFAULT NULL, `road_name` varchar(40) DEFAULT NULL, `road_name_bonbun` varchar(5) DEFAULT NULL, `road_name_bubun` varchar(5) DEFAULT NULL, `road_name_seq` varchar(2) DEFAULT NULL, `road_name_basement_code` varchar(1) DEFAULT NULL, `road_name_code` varchar(7) DEFAULT NULL, `dong` varchar(40) DEFAULT NULL, `bonbun` varchar(4) DEFAULT NULL, `bubun` varchar(4) DEFAULT NULL, `si_gun_gu_code` varchar(5) DEFAULT NULL, `eub_myun_dong_code` varchar(5) DEFAULT NULL, `dong_code` varchar(10) DEFAULT NULL, `land_code` varchar(1) DEFAULT NULL, `apartment_name` varchar(40) DEFAULT NULL, `jibun` varchar(10) DEFAULT NULL, `lng` varchar(30) DEFAULT NULL, `lat` varchar(30) DEFAULT NULL, PRIMARY KEY (`apt_code`), UNIQUE KEY `UNIQUE` (
        `build_year`, `apartment_name`, `jibun`, `si_gun_gu_code`, `eub_myun_dong_code`
    ) /*!80000 INVISIBLE */, KEY `houseinfo_dong_code_dong_dong_code_fk_idx` (`dong_code`) /*!80000 INVISIBLE */, CONSTRAINT `houseinfo_dong_code_dong_dong_code_fk` FOREIGN KEY (`dong_code`) REFERENCES `dong` (`dong_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`Enviroment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Enviroment`;

CREATE TABLE `Enviroment` (
    `dong_code` varchar(10) NOT NULL, `station_proximity` INT NULL, `commercial_area` INT NULL, `Academic_num` INT NULL, `Security_Index` INT NULL, `dong_score` INT NULL, PRIMARY KEY (`dong_code`), KEY `Enviroment_dong_code_dong_dong_code_fk_idx` (`dong_code`), CONSTRAINT `Enviroment_dong_code_dong_dong_code_fk` FOREIGN KEY (`dong_code`) REFERENCES `dong` (`dong_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`UserInfo`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserInfo`;

CREATE TABLE `UserInfo` (
    `user_id` VARCHAR(45) NOT NULL, `user_pwd` VARCHAR(45) NULL, `user_name` VARCHAR(45) NULL, `user_address` VARCHAR(45) NULL, `user_phone` VARCHAR(45) NULL, `user_favorite_place` VARCHAR(45) NULL, PRIMARY KEY (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`Notice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Notice`;

CREATE TABLE `Notice` (
    `post_id` INT NOT NULL AUTO_INCREMENT, `title` VARCHAR(45) NULL, `content` VARCHAR(45) NULL, `date` DATETIME NULL, PRIMARY KEY (`post_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`DongStory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `DongStory`;

CREATE TABLE `DongStory` (
    `post_id` INT NOT NULL AUTO_INCREMENT, `dong_code` varchar(10) NOT NULL, `title` VARCHAR(45) NULL, `content` VARCHAR(45) NULL, `date` DATETIME NULL, `user_id` VARCHAR(45) NOT NULL, PRIMARY KEY (`post_id`), KEY `DongStory_dong_code_dong_dong_code_fk_idx` (`dong_code`), CONSTRAINT `DongStory_dong_code_dong_dong_code_fk` FOREIGN KEY (`dong_code`) REFERENCES `dong` (`dong_code`), KEY `DongStory_user_id_userinfo_user_id_fk_idx` (`user_id`), CONSTRAINT `DongStory_user_id_userinfo_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `userinfo` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`Comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Comment`;

CREATE TABLE `Comment` (
    `comment_id` INT NOT NULL AUTO_INCREMENT, `post_id` INT NOT NULL, `user_id` VARCHAR(45) NOT NULL, `content` VARCHAR(45) NULL, `date` DATETIME NULL, PRIMARY KEY (`comment_id`), KEY `Comment_post_id_DongStory_post_id_fk_idx` (`post_id`), CONSTRAINT `Comment_post_id_DongStory_post_id_fk` FOREIGN KEY (`post_id`) REFERENCES `DongStory` (`post_id`), KEY `Comment_user_id_userinfo_user_id_fk_idx` (`user_id`), CONSTRAINT `Comment_user_id_userinfo_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `userinfo` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;