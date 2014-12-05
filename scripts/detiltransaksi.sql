DROP TABLE IF EXISTS `detiltransaksi-temp`;

CREATE TABLE `detiltransaksi-temp` (
       `nomor` SMALLINT(4) ZEROFILL NOT NULL DEFAULT 0
     , `tanggal` DATE NOT NULL
     , `menu` SMALLINT(4) ZEROFILL NOT NULL DEFAULT 0
     , `jumlah` FLOAT NOT NULL DEFAULT 1
     , `harga` FLOAT NOT NULL DEFAULT 0
     , `diskon` FLOAT NOT NULL DEFAULT 0
     , `pajak` FLOAT NOT NULL DEFAULT 0
     , `namauser` VARCHAR(20) NOT NULL
     , `berat` FLOAT NOT NULL DEFAULT 0
     , PRIMARY KEY (`menu`, `nomor`, `tanggal`)
     , INDEX (`nomor`, `tanggal`)
     , CONSTRAINT `fk_transaksi_detil_temp` FOREIGN KEY (`nomor`, `tanggal`)
                  REFERENCES `transaksi` (`nomor`, `tanggal`) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (`menu`)
     , CONSTRAINT `fk_menu_detil_temp` FOREIGN KEY (`menu`)
                  REFERENCES `menu` (`menu`) ON DELETE RESTRICT ON UPDATE CASCADE
     , INDEX (`namauser`)
     , CONSTRAINT `fk_detil_user_temp` FOREIGN KEY (`namauser`)
                  REFERENCES `operator` (`namauser`) ON DELETE RESTRICT ON UPDATE CASCADE
)TYPE=InnoDB;

INSERT INTO `detiltransaksi-temp` SELECT dt.*, 0 as berat FROM `detiltransaksi` dt;

DROP TABLE IF EXISTS `detiltransaksi`;

CREATE TABLE `detiltransaksi` (
       `nomor` SMALLINT(4) ZEROFILL NOT NULL DEFAULT 0
     , `tanggal` DATE NOT NULL
     , `menu` SMALLINT(4) ZEROFILL NOT NULL DEFAULT 0
     , `jumlah` FLOAT NOT NULL DEFAULT 1
     , `harga` FLOAT NOT NULL DEFAULT 0
     , `diskon` FLOAT NOT NULL DEFAULT 0
     , `pajak` FLOAT NOT NULL DEFAULT 0
     , `namauser` VARCHAR(20) NOT NULL
     , `berat` FLOAT NOT NULL DEFAULT 0
     , PRIMARY KEY (`menu`, `nomor`, `tanggal`)
     , INDEX (`nomor`, `tanggal`)
     , CONSTRAINT `fk_transaksi_detil` FOREIGN KEY (`nomor`, `tanggal`)
                  REFERENCES `transaksi` (`nomor`, `tanggal`) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (`menu`)
     , CONSTRAINT `fk_menu_detil` FOREIGN KEY (`menu`)
                  REFERENCES `menu` (`menu`) ON DELETE RESTRICT ON UPDATE CASCADE
     , INDEX (`namauser`)
     , CONSTRAINT `fk_detil_user` FOREIGN KEY (`namauser`)
                  REFERENCES `operator` (`namauser`) ON DELETE RESTRICT ON UPDATE CASCADE
)TYPE=InnoDB;

INSERT INTO `detiltransaksi` SELECT * FROM `detiltransaksi-temp`;

DROP TABLE IF EXISTS `detiltransaksi-temp`;
