-- MySQL dump 10.13  Distrib 5.7.12, for osx10.9 (x86_64)
--
-- Host: localhost    Database: sys
-- ------------------------------------------------------
-- Server version	5.7.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Moment`
--

DROP TABLE IF EXISTS `Moment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Moment` (
  `id` bigint(15) NOT NULL,
  `date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `content` varchar(300) DEFAULT NULL,
  `userId` int(5) NOT NULL,
  `pictureList` json DEFAULT NULL,
  `praiseUserIdList` json DEFAULT NULL,
  `commentIdList` json DEFAULT NULL,
  `commentUserIdList` json DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Moment`
--

LOCK TABLES `Moment` WRITE;
/*!40000 ALTER TABLE `Moment` DISABLE KEYS */;
INSERT INTO `Moment` VALUES (12,'2017-02-08 08:06:11',NULL,70793,'[\"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\"]',NULL,NULL,NULL),(15,'2017-02-08 08:06:11',NULL,70793,'[\"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg\"]',NULL,NULL,NULL),(32,'2017-02-08 08:06:11',NULL,82002,'[\"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\", \"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg\"]',NULL,NULL,NULL),(58,'2017-02-01 11:14:31','This is a Content...-435',90814,'[\"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg\", \"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\"]',NULL,NULL,NULL),(170,'2017-02-01 11:14:31','This is a Content...-73',70793,'[\"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg\", \"http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png\"]',NULL,NULL,NULL),(235,'2017-02-08 08:06:11',NULL,38710,'[\"http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg\", \"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\"]',NULL,NULL,NULL),(301,'2017-02-01 11:14:31','This is a Content...-301',93793,'[\"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\"]','[38710, 82001, 93793]',NULL,NULL),(371,'2017-02-01 11:14:31','This is a Content...-371',82002,'[\"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\"]','[82001, 90814, 82002, 93793]',NULL,NULL),(470,'2017-02-01 11:14:31','This is a Content...-470',38710,NULL,NULL,NULL,NULL),(511,'2017-02-08 08:06:11',NULL,38710,'[\"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\"]',NULL,NULL,NULL),(543,'2017-02-08 08:06:11',NULL,93793,'[\"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\"]',NULL,NULL,NULL),(551,'2017-02-08 08:06:11',NULL,70793,'[\"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\", \"https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg\", \"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067\", \"https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067\", \"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067\", \"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg\"]',NULL,NULL,NULL),(592,'2017-02-08 08:06:11',NULL,82001,'[\"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg\", \"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg\", \"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067\"]',NULL,NULL,NULL);
/*!40000 ALTER TABLE `Moment` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-05  0:12:22
