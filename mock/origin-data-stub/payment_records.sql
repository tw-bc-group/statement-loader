-- MySQL dump 10.13  Distrib 8.0.18, for Linux (x86_64)
--
-- Host: localhost    Database: origin_data
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `origin_data`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `origin_data` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `origin_data`;

--
-- Table structure for table `payment_records`
--

DROP TABLE IF EXISTS `payment_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment_records` (
  `account` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `amount` bigint(20) NOT NULL,
  `biz_serial_number` varchar(255) NOT NULL,
  `bookkeep_status` int(11) NOT NULL,
  `create_time` varchar(255) NOT NULL,
  `currency` varchar(255) NOT NULL,
  `extra` varchar(255) DEFAULT NULL,
  `fee` bigint(20) NOT NULL,
  `fee_account` varchar(255) DEFAULT NULL,
  `id` int(11) NOT NULL,
  `miner_fee` bigint(20) NOT NULL,
  `sequence` int(11) NOT NULL,
  `serial_number` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  `token_name` varchar(255) NOT NULL,
  `tx_hash` varchar(255) NOT NULL,
  `tx_height` bigint(20) NOT NULL,
  `type` int(11) NOT NULL,
  `unfreeze_amount` bigint(20) NOT NULL,
  `unfreeze_serial_number` varchar(255) DEFAULT NULL,
  `update_time` varchar(255) NOT NULL,
  PRIMARY KEY (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment_records`
--

LOCK TABLES `payment_records` WRITE;
/*!40000 ALTER TABLE `payment_records` DISABLE KEYS */;
INSERT INTO `payment_records` VALUES ('1001101363804912129150976','0x5127913D3Bfc822BDcd9D825a285C5321979f02E',20000000,'5eb09701-9df0-4252-b9f5-373ac59318e8',0,'2019-12-16T13:35:01.000','USDTERC',NULL,10000,NULL,2007896,63000,0,'0682440e9918c8772eeec312a7ec5b1d',2,'USDTERC','0xcf183faf821235c31735d6164a76654f314b8ccaf9c3345d4791414d8c02eca7',0,1,0,NULL,'2019-12-16T13:35:01.000'),('1001101363804912099790848','t1aqvGWLDV7QwMe69o8p16UmApV6sBojvn1',1000000,'0291397f5c018c1ef0ee1401a2c60382',0,'2019-12-16T20:36:52.000','ZEC',NULL,0,NULL,2007926,0,-1,'15d274ebeaed46964066b4b875ea2a8e',2,'ZEC','0xf61f431c6bdd6034782ec517f8a27be7514918c4d1c9e7fc85299c3db2a1e2c2',5421390,2,0,NULL,'2019-12-16T20:39:25.000'),('1001101363804912099790848','bm1q028sgnnwkqg3cy4ea947evkmd4pday2vh9rh24',1000000,'1e207a25-9a41-4488-9e7e-9f3564d26239',0,'2019-12-16T20:35:26.000','BTM',NULL,1000,NULL,2007924,5160808,0,'4728ec5ad8938cd5f77ec73fe70135d5',2,'BTM','0xd8f49062550939560728b007ee794aee55e1e8c8a2434eba80bd850a09f1bfcc',0,1,0,NULL,'2019-12-16T20:35:26.000'),('1001101363804912129150976','0x5127913D3Bfc822BDcd9D825a285C5321979f02E',2000000,'680d9884-f8f1-43e9-97d2-797daca91593',0,'2019-12-16T17:31:42.000','USDC',NULL,10000,NULL,2007900,63000,0,'598a52c22fada492dd7e99b257dc304e',2,'USDC','0xcaf7b502a8fbfcd70a1fdc6c9df409e6d0189ef9f96968689e8d9a8fc526b6f2',0,1,0,NULL,'2019-12-16T17:31:42.000'),('1001101364510373593747456','0x8c189BcBDaa4489d12317e33B34602f11bc00F78',10000000,'cf1cfbd01c33c4503b32c92e7837fd2b',0,'2019-12-16T16:03:21.000','USDTERC',NULL,0,NULL,2007898,0,-1,'a6d52e3f78b668600064a47adadc5b71',2,'USDTERC','3f96d21222082e848ebfec2de72d35fc0b9bca0c7564b4999d5d4c199e519dcf',1612657,1,0,NULL,'2019-12-16T17:23:07.000'),('1001101363804912150122496','0xF2Bd6Bb7B22875Ac582Ec1A718F63b532045401D',1000000,'b66fc67a-a7cb-4fdd-a60d-d3533f007d72',0,'2019-12-16T20:39:12.000','DCR',NULL,10000,NULL,2007932,5876,0,'PGW14f13cd3-4ff4-48d4-9d90-cac06a7a0458',2,'DCR','4feaa38303797f34e4f306660169d728672301a271709a29a025963f3524c839',0,2,0,NULL,'2019-12-16T20:39:12.000'),('1001101363804912091402240','t1a5Kh3DDpD3rC49NBafQCXKBMK7zmsqduE',100000,'9402455a-3053-4f20-8aac-d1de3bc47f5f',0,'2019-12-16T20:37:50.000','ZEC',NULL,1000,NULL,2007928,748,0,'PGW1c6fb79e-777a-4c13-9eb9-c3a68a2fcfdb',2,'ZEC','7eac75f86fe4eb75f7c2aa287832918767d89d2d58cbfe9d5dfd72cd743d7f04',0,2,0,NULL,'2019-12-16T20:37:50.000'),('1001101364799690673229824','0xF2Bd6Bb7B22875Ac582Ec1A718F63b532045401D',1000,'1c6fb79e-777a-4c13-9eb9-c3a68a2fcfdb',0,'2019-12-16T20:39:44.000','ETH',NULL,1000,NULL,2007934,488,0,'PGW1e4a116c-accf-462e-9c85-4388a0e93620',2,'ETH','1baa144e7592dea405c4b51b42ab8e6a0ef630ae213a3adb61596bcb0a7b1638',0,2,0,NULL,'2019-12-16T20:39:44.000'),('1001101364799690664841216','TsmsgTSnJsrNUVXXyRJdymHGgfN61EmkmRL',100000000,'387f1c4c-b712-43f8-b896-ec52a4b7740a',0,'2019-12-16T17:34:08.000','USDC',NULL,12000,NULL,2007906,5040,0,'PGW9d46c6f1-02d9-419a-bfb5-3a263867e982',2,'USDC','81b40528a7033005d13e0f97e536e00ecf6e42e704ba71eb1679e95396d0ceca',0,2,0,NULL,'2019-12-16T17:34:08.000'),('1001101363804912112373760','0x1669BA62c45196F30afD56c493F9267FEDB02C7e',1000000,'6f907413-52e7-4575-bf10-401d25a5375e',0,'2019-12-16T20:38:37.000','ETH',NULL,10000,NULL,2007930,2660,0,'PGWf6d33548-ba80-419f-a584-a41f49089d0f',2,'ETH','39dc9051f1aa6744718e8dff92849a7cb2f61d7f499137b9684139763b934e49',0,2,0,NULL,'2019-12-16T20:38:37.000');
/*!40000 ALTER TABLE `payment_records` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-19  6:10:59