ALTER TABLE `menu` 
    ADD COLUMN `pajak` BOOLEAN NOT NULL DEFAULT true AFTER `aktif`
  , ADD COLUMN `satuan` VARCHAR(20) AFTER `pajak`;
