CREATE SCHEMA IF NOT EXISTS `hoshinoHome` DEFAULT CHARACTER SET utf8;

USE hoshinoHome;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`dong`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `dong`;

CREATE TABLE `dong` (
    `dong_code` varchar(10) NOT NULL,
    `si_do_name` varchar(30) DEFAULT NULL,
    `gu_gun_name` varchar(30) DEFAULT NULL,
    `dong_name` varchar(30) DEFAULT NULL,
    PRIMARY KEY (`dong_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`houseinfo`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `houseinfo`;

CREATE TABLE `houseinfo` (
    `house_code` bigint NOT NULL,
    `house_type` int NOT NULL,
    `build_year` int DEFAULT NULL,
    `dong_code` varchar(10) DEFAULT NULL,
    `house_name` varchar(30) DEFAULT NULL,
    `si_gun_gu_name` varchar(20) DEFAULT NULL,
    `dong_name` varchar(20) DEFAULT NULL,
    `road_name` varchar(20) DEFAULT NULL,
    `bonbun` varchar(5) DEFAULT NULL,
    `bubun` varchar(5) DEFAULT NULL,
    `jibun_address` varchar(40) DEFAULT NULL,
    `road_address` VARCHAR(40) DEFAULT NULL,
    `lng` varchar(30) DEFAULT NULL,
    `lat` varchar(30) DEFAULT NULL,
    PRIMARY KEY (`house_code`),
    UNIQUE KEY `UNIQUE` (
        `jibun_address`,
        `road_address`
    ),
    KEY `houseinfo_dong_code_dong_dong_code_fk_idx` (`dong_code`),
    CONSTRAINT `houseinfo_dong_code_dong_dong_code_fk` FOREIGN KEY (`dong_code`) REFERENCES `dong` (`dong_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`housedeal`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `housedeal`;

CREATE TABLE `housedeal` (
    `deal_code` bigint NOT NULL,
    `deal_type` INT NOT NULL,
    `deal_amount` varchar(20) DEFAULT NULL,
    `deposit_amount` varchar(20) DEFAULT NULL,
    `monthly_amount` varchar(20) DEFAULT NULL,
    `jibun_address` varchar(40) DEFAULT NULL,
    `road_address` varchar(40) DEFAULT NULL,
    `deal_year` int DEFAULT NULL,
    `deal_month` int DEFAULT NULL,
    `deal_day` int DEFAULT NULL,
    `area` varchar(20) DEFAULT NULL,
    `floor` varchar(4) DEFAULT NULL,
    `house_code` bigint DEFAULT NULL,
    `house_type` int DEFAULT NULL,
    PRIMARY KEY (`deal_code`),
    KEY `housedeal_house_code_houseinfo_house_code_fk_idx` (`house_code`),
    CONSTRAINT `housedeal_house_code_houseinfo_house_code_fk` FOREIGN KEY (`house_code`) REFERENCES `houseinfo` (`house_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`Enviroment`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `Enviroment`;

CREATE TABLE `Enviroment` (
    `dong_code` varchar(10) NOT NULL,
    `station_proximity` INT NULL,
    `commercial_area` INT NULL,
    `Academic_num` INT NULL,
    `Security_Index` INT NULL,
    `dong_score` INT NULL,
    PRIMARY KEY (`dong_code`),
    KEY `Enviroment_dong_code_dong_dong_code_fk_idx` (`dong_code`),
    CONSTRAINT `Enviroment_dong_code_dong_dong_code_fk` FOREIGN KEY (`dong_code`) REFERENCES `dong` (`dong_code`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`UserInfo`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `UserInfo`;

CREATE TABLE `UserInfo` (
    `user_id` int NOT NULL AUTO_INCREMENT,
    `user_name` varchar(50) NOT NULL,
    `user_password` varchar(100) NOT NULL,
    `user_address` varchar(100) NOT NULL,
    `user_favorite_place` varchar(100) NOT NULL,
    `user_type` varchar(10) NOT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`Notice`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `Notice`;

CREATE TABLE `Notice` (
    `post_id` INT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(45) NULL,
    `content` VARCHAR(45) NULL,
    `date` DATETIME NULL,
    PRIMARY KEY (`post_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`DongStory`
-- -----------------------------------------------------

DROP TABLE IF EXISTS `DongStory`;

CREATE TABLE `DongStory` (
    `post_id` INT NOT NULL AUTO_INCREMENT,
    `dong_code` varchar(10) NOT NULL,
    `title` VARCHAR(45) NULL,
    `content` VARCHAR(45) NULL,
    `date` DATETIME NULL,
    `user_id` int NOT NULL,
    PRIMARY KEY (`post_id`),
    KEY `DongStory_dong_code_dong_dong_code_fk_idx` (`dong_code`),
    CONSTRAINT `DongStory_dong_code_dong_dong_code_fk` FOREIGN KEY (`dong_code`) REFERENCES `dong` (`dong_code`),
    KEY `DongStory_user_id_userinfo_user_id_fk_idx` (`user_id`),
    CONSTRAINT `DongStory_user_id_userinfo_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `UserInfo` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`Comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Comment`;

CREATE TABLE Comment (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT,
    board_type VARCHAR(50),
    user_id INT,
    user_name VARCHAR(50),
    content VARCHAR(255),
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES UserInfo (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;