DROP TABLE IF EXISTS `pembayaran-temp`;

CREATE TABLE `pembayaran-temp` (
       `nobukti` SMALLINT(4) ZEROFILL NOT NULL
     , `nomor` SMALLINT(4) ZEROFILL NOT NULL DEFAULT 0
     , `tanggal` DATE NOT NULL
     , `jenis` VARCHAR(20) NOT NULL
     , `jumlah` FLOAT NOT NULL DEFAULT 0
     , `namauser` VARCHAR(20) NOT NULL
     , `dibayar` DATE NOT NULL
     , `shift` SMALLINT(1) NOT NULL DEFAULT 1
     , PRIMARY KEY (`nobukti`, `nomor`, `tanggal`, `dibayar`)
     , INDEX (`nomor`, `tanggal`)
     , CONSTRAINT `fk_bayar_transaksi_temp` FOREIGN KEY (`nomor`, `tanggal`)
                  REFERENCES `transaksi` (`nomor`, `tanggal`) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (`namauser`)
     , CONSTRAINT `fk_bayar_user_temp` FOREIGN KEY (`namauser`)
                  REFERENCES `operator` (`namauser`) ON DELETE RESTRICT ON UPDATE CASCADE
     , INDEX (`jenis`)
     , CONSTRAINT `fk_jenis_pembayaran_temp` FOREIGN KEY (`jenis`)
                  REFERENCES `jenisbayar` (`jenis`)
)TYPE=InnoDB;

INSERT INTO `pembayaran-temp` SELECT * FROM `pembayaran`;

DROP TABLE IF EXISTS `pembayaran`;

CREATE TABLE `pembayaran` (
       `nobukti` SMALLINT(4) ZEROFILL NOT NULL
     , `nomor` SMALLINT(4) ZEROFILL NOT NULL DEFAULT 0
     , `tanggal` DATE NOT NULL
     , `jenis` VARCHAR(20) NOT NULL
     , `jumlah` FLOAT NOT NULL DEFAULT 0
     , `namauser` VARCHAR(20) NOT NULL
     , `dibayar` DATE NOT NULL
     , `shift` SMALLINT(1) NOT NULL DEFAULT 1
     , PRIMARY KEY (`nobukti`, `nomor`, `tanggal`, `dibayar`)
     , INDEX (`nomor`, `tanggal`)
     , CONSTRAINT `fk_bayar_transaksi` FOREIGN KEY (`nomor`, `tanggal`)
                  REFERENCES `transaksi` (`nomor`, `tanggal`) ON DELETE CASCADE ON UPDATE CASCADE
     , INDEX (`namauser`)
     , CONSTRAINT `fk_bayar_user` FOREIGN KEY (`namauser`)
                  REFERENCES `operator` (`namauser`) ON DELETE RESTRICT ON UPDATE CASCADE
     , INDEX (`jenis`)
     , CONSTRAINT `fk_jenis_pembayaran` FOREIGN KEY (`jenis`)
                  REFERENCES `jenisbayar` (`jenis`)
)TYPE=InnoDB;

INSERT INTO `pembayaran` SELECT * FROM `pembayaran-temp`;

DROP TABLE IF EXISTS `pembayaran-temp`;
