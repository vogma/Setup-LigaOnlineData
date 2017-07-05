-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema liga
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema liga
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `liga` DEFAULT CHARACTER SET utf8 ;
USE `liga` ;

-- -----------------------------------------------------
-- Table `liga`.`stadion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `liga`.`stadion` ;

CREATE TABLE IF NOT EXISTS `liga`.`stadion` (
  `stadion_id` INT(11) NOT NULL,
  `s_name` VARCHAR(200) NULL DEFAULT NULL,
  `s_gps` VARCHAR(200) NULL DEFAULT NULL,
  `s_adress` VARCHAR(200) NULL DEFAULT NULL,
  `s_bildURL` VARCHAR(255) NULL DEFAULT NULL,
  `s_kapazitaet` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`stadion_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `liga`.`verein`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `liga`.`verein` ;

CREATE TABLE IF NOT EXISTS `liga`.`verein` (
  `verein_id` INT(11) NOT NULL,
  `v_name` VARCHAR(200) NULL DEFAULT NULL,
  `v_gruendung` DATE NULL DEFAULT NULL,
  `v_trainer` VARCHAR(200) NULL DEFAULT NULL,
  `v_praesident` VARCHAR(200) NULL DEFAULT NULL,
  `v_ort` VARCHAR(200) NULL DEFAULT NULL,
  `v_logoURL` VARCHAR(255) NULL DEFAULT NULL,
  `v_stadionID` VARCHAR(45) NULL DEFAULT NULL,
  `stadion_stadion_id` INT(11) NOT NULL,
  PRIMARY KEY (`verein_id`),
  INDEX `fk_verein_stadion1_idx` (`stadion_stadion_id` ASC),
  CONSTRAINT `fk_verein_stadion1`
    FOREIGN KEY (`stadion_stadion_id`)
    REFERENCES `liga`.`stadion` (`stadion_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `liga`.`matches`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `liga`.`matches` ;

CREATE TABLE IF NOT EXISTS `liga`.`matches` (
  `match_id` INT(11) NOT NULL,
  `m_saison` INT(11) NULL DEFAULT NULL,
  `m_datum` DATETIME NULL DEFAULT NULL,
  `m_zuschauer` INT(11) NULL DEFAULT NULL,
  `m_spieltag` INT(5) NULL DEFAULT NULL,
  `m_stadion` VARCHAR(45) NULL DEFAULT NULL,
  `m_stadionID` VARCHAR(45) NULL DEFAULT NULL,
  `m_endergebnis` VARCHAR(45) NULL DEFAULT NULL,
  `m_halbzeitergebnis` VARCHAR(45) NULL DEFAULT NULL,
  `m_punkteHeim` INT(11) NULL DEFAULT NULL,
  `m_punkteGast` INT(11) NULL DEFAULT NULL,
  `m_heimID` INT(11) NOT NULL,
  `m_gastID` INT(11) NOT NULL,
  PRIMARY KEY (`match_id`),
  INDEX `fk_match_verein1_idx` (`m_heimID` ASC),
  INDEX `fk_match_verein2_idx` (`m_gastID` ASC),
  CONSTRAINT `fk_match_verein1`
    FOREIGN KEY (`m_heimID`)
    REFERENCES `liga`.`verein` (`verein_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_match_verein2`
    FOREIGN KEY (`m_gastID`)
    REFERENCES `liga`.`verein` (`verein_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `liga`.`player`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `liga`.`player` ;

CREATE TABLE IF NOT EXISTS `liga`.`player` (
  `player_id` INT(11) NOT NULL AUTO_INCREMENT,
  `p_name` VARCHAR(200) NULL DEFAULT NULL,
  `p_verein_id` INT(11) NOT NULL,
  PRIMARY KEY (`player_id`),
  INDEX `fk_player_verein1_idx` (`p_verein_id` ASC),
  CONSTRAINT `fk_player_verein1`
    FOREIGN KEY (`p_verein_id`)
    REFERENCES `liga`.`verein` (`verein_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `liga`.`goal`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `liga`.`goal` ;

CREATE TABLE IF NOT EXISTS `liga`.`goal` (
  `goal_id` INT(11) NOT NULL,
  `g_toreHeim` INT(11) NULL DEFAULT NULL,
  `g_toreGast` INT(11) NULL DEFAULT NULL,
  `g_minute` INT(11) NULL DEFAULT NULL,
  `g_schuetze` VARCHAR(200) NULL DEFAULT NULL,
  `g_playerID` INT(11) NULL DEFAULT NULL,
  `g_matchID` INT(11) NULL DEFAULT NULL,
  `match_match_id` INT(11) NOT NULL,
  `player_player_id` INT(11) NOT NULL,
  PRIMARY KEY (`goal_id`),
  INDEX `fk_goal_match1_idx` (`match_match_id` ASC),
  INDEX `fk_goal_player1_idx` (`player_player_id` ASC),
  CONSTRAINT `fk_goal_match1`
    FOREIGN KEY (`match_match_id`)
    REFERENCES `liga`.`matches` (`match_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_goal_player1`
    FOREIGN KEY (`player_player_id`)
    REFERENCES `liga`.`player` (`player_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `liga`.`group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `liga`.`group` ;

CREATE TABLE IF NOT EXISTS `liga`.`group` (
  `groupid` INT(11) NOT NULL AUTO_INCREMENT,
  `groupName` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`groupid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `liga`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `liga`.`user` ;

CREATE TABLE IF NOT EXISTS `liga`.`user` (
  `userid` INT(11) NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(45) NOT NULL,
  `userPassword` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`userid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `liga`.`user_group`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `liga`.`user_group` ;

CREATE TABLE IF NOT EXISTS `liga`.`user_group` (
  `group_groupid` INT(11) NOT NULL,
  `user_userid` INT(11) NOT NULL,
  INDEX `fk_user_group_group1_idx` (`group_groupid` ASC),
  INDEX `fk_user_group_user1_idx` (`user_userid` ASC),
  CONSTRAINT `fk_user_group_group1`
    FOREIGN KEY (`group_groupid`)
    REFERENCES `liga`.`group` (`groupid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_group_user1`
    FOREIGN KEY (`user_userid`)
    REFERENCES `liga`.`user` (`userid`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
