DROP TABLE IF EXISTS `temp-skema`;

CREATE TABLE `temp-skema` (
       `modul` INTEGER(4) NOT NULL
     , `akses` VARCHAR(20) NOT NULL
     , `negasi` BOOLEAN NOT NULL DEFAULT 0
     , PRIMARY KEY (`modul`, `akses`)
     , INDEX (`akses`)
     , CONSTRAINT `fk_skema_akses_temp` FOREIGN KEY (`akses`)
                  REFERENCES `hakakses` (`akses`) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

INSERT INTO `temp-skema` SELECT * FROM `skema`;

DROP TABLE IF EXISTS `skema`;

CREATE TABLE `skema` (
       `modul` INTEGER(4) NOT NULL
     , `akses` VARCHAR(20) NOT NULL
     , `negasi` BOOLEAN NOT NULL DEFAULT 0
     , PRIMARY KEY (`modul`, `akses`)
     , INDEX (`akses`)
     , CONSTRAINT `fk_skema_akses` FOREIGN KEY (`akses`)
                  REFERENCES `hakakses` (`akses`) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

INSERT INTO `skema` SELECT * FROM `temp-skema`;

DROP TABLE IF EXISTS `temp-skema`;
