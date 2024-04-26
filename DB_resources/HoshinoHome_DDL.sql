-- Active: 1710890946343@@127.0.0.1@3306@hoshinohome
SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;

SET
    @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS,
    FOREIGN_KEY_CHECKS = 0;

SET
    @OLD_SQL_MODE = @@SQL_MODE,
    SQL_MODE = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema hoshinoHome
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hoshinoHome
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hoshinoHome` DEFAULT CHARACTER SET utf8;

USE `hoshinoHome`;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`Dong`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hoshinoHome`.`Dong` (
    `dong_code` VARCHAR(45) NOT NULL, `si_do_name` VARCHAR(45) NULL, `go_gun_name` VARCHAR(45) NULL, `dong_name` VARCHAR(45) NULL, PRIMARY KEY (`dong_code`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`HouseInfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hoshinoHome`.`HouseInfo` (
    `apt_code` INT NOT NULL, `build_year` INT NULL, `road_name` VARCHAR(45) NULL, `road_name_main_num` VARCHAR(45) NULL, `road_name_sub_num` VARCHAR(45) NULL, `road_name_seq` VARCHAR(45) NULL, `raod_name_basement_code` VARCHAR(45) NULL, `road_name_code` VARCHAR(45) NULL, `dong` VARCHAR(45) NULL, `Main_num` VARCHAR(45) NULL, `sum_num` VARCHAR(45) NULL, `si_gun_gu_code` VARCHAR(45) NULL, `eup_myeon_dong_code` VARCHAR(45) NULL, `dong_code` VARCHAR(45) NULL, `land_code` VARCHAR(45) NULL, `apartment_name` VARCHAR(45) NULL, `jibun` VARCHAR(45) NULL, `lon` VARCHAR(45) NULL, `lat` VARCHAR(45) NULL, `Dong_dong_code` VARCHAR(45) NOT NULL, PRIMARY KEY (`apt_code`), INDEX `fk_HouseInfo_Dong1_idx` (`Dong_dong_code` ASC) VISIBLE, CONSTRAINT `fk_HouseInfo_Dong1` FOREIGN KEY (`Dong_dong_code`) REFERENCES `hoshinoHome`.`Dong` (`dong_code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`HouseDeal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hoshinoHome`.`HouseDeal` (
    `deal_code` INT NOT NULL, `deal_amount` VARCHAR(20) NULL, `deal_year` INT NULL, `deal_month` INT NULL, `deal_day` INT NULL, `area` VARCHAR(45) NULL, `floor` VARCHAR(45) NULL, `aptCode` INT NULL, `HouseInfo_apt_code` INT NOT NULL, PRIMARY KEY (`dealCode`), INDEX `fk_HouseDeal_HouseInfo_idx` (`HouseInfo_apt_code` ASC) VISIBLE, CONSTRAINT `fk_HouseDeal_HouseInfo` FOREIGN KEY (`HouseInfo_apt_code`) REFERENCES `hoshinoHome`.`HouseInfo` (`apt_code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`Enviroment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hoshinoHome`.`Enviroment` (
    `Dong_dong_code` VARCHAR(45) NOT NULL, `station_proximity` INT NULL, `commercial_area` INT NULL, `Academic_num` INT NULL, `Security_Index` INT NULL, `dong_score` INT NULL, PRIMARY KEY (`Dong_dong_code`), INDEX `fk_Enviroment_Dong1_idx` (`Dong_dong_code` ASC) VISIBLE, CONSTRAINT `fk_Enviroment_Dong1` FOREIGN KEY (`Dong_dong_code`) REFERENCES `hoshinoHome`.`Dong` (`dong_code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`UserInfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hoshinoHome`.`UserInfo` (
    `user_id` VARCHAR(45) NOT NULL, `user_pwd` VARCHAR(45) NULL, `user_name` VARCHAR(45) NULL, `user_address` VARCHAR(45) NULL, `user_phone` VARCHAR(45) NULL, `user_favorite_place` VARCHAR(45) NULL, PRIMARY KEY (`user_id`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`Notice`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hoshinoHome`.`Notice` (
    `post_id` INT NOT NULL, `title` VARCHAR(45) NULL, `content` VARCHAR(45) NULL, `date` DATETIME NULL, PRIMARY KEY (`post_id`)
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`DongStory`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hoshinoHome`.`DongStory` (
    `post_id` INT NOT NULL, `Dong_dong_code` VARCHAR(45) NOT NULL, `title` VARCHAR(45) NULL, `content` VARCHAR(45) NULL, `date` DATETIME NULL, `UserInfo_user_id` INT NOT NULL, PRIMARY KEY (`post_id`), INDEX `fk_DongStory_Dong1_idx` (`Dong_dong_code` ASC) VISIBLE, INDEX `fk_DongStory_UserInfo1_idx` (`UserInfo_user_id` ASC) VISIBLE, CONSTRAINT `fk_DongStory_Dong1` FOREIGN KEY (`Dong_dong_code`) REFERENCES `hoshinoHome`.`Dong` (`dong_code`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `fk_DongStory_UserInfo1` FOREIGN KEY (`UserInfo_user_id`) REFERENCES `hoshinoHome`.`UserInfo` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `hoshinoHome`.`Comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hoshinoHome`.`Comment` (
    `comment_id` INT NOT NULL AUTO_INCREMENT, `DongStory_post_id` INT NOT NULL, `content` VARCHAR(45) NULL, `date` DATETIME NULL, `UserInfo_user_id` INT NOT NULL, PRIMARY KEY (`comment_id`), INDEX `fk_Comme_DongStory1_idx` (`DongStory_post_id` ASC) VISIBLE, INDEX `fk_Comment_UserInfo1_idx` (`UserInfo_user_id` ASC) VISIBLE, CONSTRAINT `fk_Comme_DongStory1` FOREIGN KEY (`DongStory_post_id`) REFERENCES `hoshinoHome`.`DongStory` (`post_id`) ON DELETE NO ACTION ON UPDATE NO ACTION, CONSTRAINT `fk_Comment_UserInfo1` FOREIGN KEY (`UserInfo_user_id`) REFERENCES `hoshinoHome`.`UserInfo` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB;

SET SQL_MODE = @OLD_SQL_MODE;

SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;

SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;