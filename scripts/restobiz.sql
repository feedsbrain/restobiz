DROP INDEX `nama` ON `menu`;

DROP TABLE IF EXISTS `detiltransaksi`;
DROP TABLE IF EXISTS `skema`;
DROP TABLE IF EXISTS `kebijakan`;
DROP TABLE IF EXISTS `pembayaran`;
DROP TABLE IF EXISTS `kas`;
DROP TABLE IF EXISTS `transaksi`;
DROP TABLE IF EXISTS `operator`;
DROP TABLE IF EXISTS `konfigurasi`;
DROP TABLE IF EXISTS `hakakses`;
DROP TABLE IF EXISTS `jenisbayar`;
DROP TABLE IF EXISTS `menu`;

CREATE TABLE `menu` (
       `menu` SMALLINT(4) ZEROFILL NOT NULL AUTO_INCREMENT
     , `kelompok` VARCHAR(20) NOT NULL
     , `nama` VARCHAR(50) NOT NULL
     , `harga` FLOAT NOT NULL DEFAULT 0
     , `aktif` BOOLEAN NOT NULL DEFAULT true
     , `pajak` BOOLEAN NOT NULL DEFAULT true
     , `satuan` VARCHAR(20)
     , PRIMARY KEY (`menu`)
)TYPE=InnoDB;
CREATE UNIQUE INDEX `nama` ON `menu` (`nama` ASC);

CREATE TABLE `jenisbayar` (
       `jenis` VARCHAR(20) NOT NULL
     , `negasi` BOOLEAN NOT NULL DEFAULT false
     , PRIMARY KEY (`jenis`)
)TYPE=InnoDB;

CREATE TABLE `hakakses` (
       `akses` VARCHAR(20) NOT NULL
     , `sistem` BOOLEAN NOT NULL DEFAULT 0
     , PRIMARY KEY (`akses`)
)TYPE=InnoDB;

CREATE TABLE `konfigurasi` (
       `nama` VARCHAR(50) NOT NULL
     , `nilai` VARCHAR(50) NOT NULL
     , PRIMARY KEY (`nama`)
);

CREATE TABLE `operator` (
       `namauser` VARCHAR(20) NOT NULL
     , `password` VARCHAR(50) NOT NULL
     , `namaasli` VARCHAR(50) NOT NULL
     , `akses` VARCHAR(20) NOT NULL
     , PRIMARY KEY (`namauser`)
     , INDEX (`akses`)
     , CONSTRAINT `fk_operator_akses` FOREIGN KEY (`akses`)
                  REFERENCES `hakakses` (`akses`) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

CREATE TABLE `transaksi` (
       `nomor` SMALLINT(4) ZEROFILL NOT NULL
     , `tanggal` DATE NOT NULL
     , `customer` VARCHAR(50) NOT NULL
     , `namauser` VARCHAR(20) NOT NULL
     , `shift` SMALLINT(1) NOT NULL DEFAULT 1
     , `posted` BOOLEAN NOT NULL DEFAULT 0
     , PRIMARY KEY (`nomor`, `tanggal`)
     , INDEX (`namauser`)
     , CONSTRAINT `fk_transaksi_user` FOREIGN KEY (`namauser`)
                  REFERENCES `operator` (`namauser`) ON DELETE RESTRICT ON UPDATE CASCADE
)TYPE=InnoDB;

CREATE TABLE `kas` (
       `transaksi` SMALLINT(4) ZEROFILL NOT NULL
     , `tanggal` DATE NOT NULL
     , `kelompok` VARCHAR(20) NOT NULL
     , `keterangan` VARCHAR(50) NOT NULL
     , `jumlah` FLOAT NOT NULL
     , `namauser` VARCHAR(20) NOT NULL
     , `shift` SMALLINT(1) NOT NULL DEFAULT 1
     , PRIMARY KEY (`tanggal`, `transaksi`)
     , INDEX (`namauser`)
     , CONSTRAINT `fk_kas_user` FOREIGN KEY (`namauser`)
                  REFERENCES `operator` (`namauser`) ON DELETE RESTRICT ON UPDATE CASCADE
)TYPE=InnoDB;

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

CREATE TABLE `kebijakan` (
       `akses` VARCHAR(20) NOT NULL
     , `namauser` VARCHAR(20) NOT NULL
     , `izin` BOOLEAN NOT NULL DEFAULT 1
     , PRIMARY KEY (`akses`, `namauser`, `izin`)
     , INDEX (`namauser`)
     , CONSTRAINT `fk_kebijakan_user` FOREIGN KEY (`namauser`)
                  REFERENCES `operator` (`namauser`) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

CREATE TABLE `skema` (
       `modul` INTEGER(4) NOT NULL
     , `akses` VARCHAR(20) NOT NULL
     , `negasi` BOOLEAN NOT NULL DEFAULT 0
     , PRIMARY KEY (`modul`, `akses`)
     , INDEX (`akses`)
     , CONSTRAINT `fk_skema_akses` FOREIGN KEY (`akses`)
                  REFERENCES `hakakses` (`akses`) ON DELETE CASCADE ON UPDATE CASCADE
)TYPE=InnoDB;

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

INSERT INTO `hakakses` VALUES ('ADMIN', true);
INSERT INTO `hakakses` VALUES ('USER', true);
INSERT INTO `operator` VALUES ('admin', MD5('admin'),'Administrator','ADMIN');
INSERT INTO `operator` VALUES ('user', MD5('user'),'General User','USER');
INSERT INTO `jenisbayar` VALUES ('TUNAI', false);
INSERT INTO `jenisbayar` VALUES ('KEMBALI', true);
INSERT INTO `konfigurasi` VALUES ('DBVersion', '2');
