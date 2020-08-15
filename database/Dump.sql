CREATE DATABASE  IF NOT EXISTS `RealtyDB` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `RealtyDB`;
-- MySQL dump 10.13  Distrib 5.7.31, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: RealtyDB
-- ------------------------------------------------------
-- Server version	5.7.31-0ubuntu0.18.04.1

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
-- Table structure for table `Access`
--

DROP TABLE IF EXISTS `Access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Access` (
  `idAccess` int(11) NOT NULL AUTO_INCREMENT,
  `Authority` varchar(100) NOT NULL,
  `User_EmailAddress` varchar(45) NOT NULL,
  PRIMARY KEY (`idAccess`),
  UNIQUE KEY `cn_Access_Unique` (`User_EmailAddress`),
  CONSTRAINT `fk_Access_User` FOREIGN KEY (`User_EmailAddress`) REFERENCES `User` (`EmailAddress`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Address`
--

DROP TABLE IF EXISTS `Address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Address` (
  `idAddress` int(11) NOT NULL AUTO_INCREMENT,
  `Address1` varchar(100) DEFAULT NULL,
  `Address2` varchar(100) DEFAULT NULL,
  `Address3` varchar(100) DEFAULT NULL,
  `Zip` int(5) unsigned zerofill DEFAULT NULL,
  `CityName` varchar(100) DEFAULT NULL,
  `State_idState` int(11) DEFAULT NULL,
  `Country_idCountry` int(11) DEFAULT NULL,
  `AddressType_idAddressType` int(11) NOT NULL,
  `Person_idPerson` int(11) DEFAULT NULL,
  PRIMARY KEY (`idAddress`),
  UNIQUE KEY `cn_Address_Unique` (`Address1`,`Address2`,`Address3`,`Zip`,`AddressType_idAddressType`,`Person_idPerson`),
  KEY `fk_Address_Person` (`Person_idPerson`),
  KEY `fk_Address_AddressType` (`AddressType_idAddressType`),
  KEY `fk_Address_State` (`State_idState`),
  KEY `fk_Address_Country` (`Country_idCountry`),
  CONSTRAINT `fk_Address_AddressType` FOREIGN KEY (`AddressType_idAddressType`) REFERENCES `AddressType` (`idAddressType`),
  CONSTRAINT `fk_Address_Country` FOREIGN KEY (`Country_idCountry`) REFERENCES `Country` (`idCountry`),
  CONSTRAINT `fk_Address_Person` FOREIGN KEY (`Person_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_Address_State` FOREIGN KEY (`State_idState`) REFERENCES `State` (`idState`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AddressType`
--

DROP TABLE IF EXISTS `AddressType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AddressType` (
  `idAddressType` int(11) NOT NULL AUTO_INCREMENT,
  `AddressType` varchar(45) NOT NULL,
  PRIMARY KEY (`idAddressType`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AddressType`
--

LOCK TABLES `AddressType` WRITE;
/*!40000 ALTER TABLE `AddressType` DISABLE KEYS */;
INSERT INTO `AddressType` VALUES (1,'Shipping'),(2,'Billing'),(3,'Mailing'),(4,'Property');
/*!40000 ALTER TABLE `AddressType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Attachment`
--

DROP TABLE IF EXISTS `Attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Attachment` (
  `idAttachment` int(11) NOT NULL AUTO_INCREMENT,
  `Attachment` longblob NOT NULL,
  `AttachmentName` varchar(100) NOT NULL,
  `AttachmentType` varchar(100) NOT NULL,
  PRIMARY KEY (`idAttachment`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Company`
--

DROP TABLE IF EXISTS `Company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Company` (
  `idCompany` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  `Organization_idOrganization` int(11) NOT NULL,
  `Person_idPerson` int(11) NOT NULL,
  PRIMARY KEY (`idCompany`),
  UNIQUE KEY `cn_Company_Unique` (`Name`,`Organization_idOrganization`,`Person_idPerson`),
  KEY `fk_Company_Organization` (`Organization_idOrganization`),
  KEY `fk_Company_Person` (`Person_idPerson`),
  CONSTRAINT `fk_Company_Organization` FOREIGN KEY (`Organization_idOrganization`) REFERENCES `Organization` (`idOrganization`),
  CONSTRAINT `fk_Company_Person` FOREIGN KEY (`Person_idPerson`) REFERENCES `Person` (`idPerson`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Country`
--

DROP TABLE IF EXISTS `Country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Country` (
  `idCountry` int(11) NOT NULL AUTO_INCREMENT,
  `CountryName` varchar(45) DEFAULT NULL,
  `CountryCode` varchar(2) NOT NULL,
  `ISO3` varchar(3) DEFAULT NULL,
  `NumCode` int(11) DEFAULT NULL,
  `PhoneCode` int(11) DEFAULT NULL,
  PRIMARY KEY (`idCountry`),
  UNIQUE KEY `CountryCode` (`CountryCode`)
) ENGINE=InnoDB AUTO_INCREMENT=240 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Country`
--

LOCK TABLES `Country` WRITE;
/*!40000 ALTER TABLE `Country` DISABLE KEYS */;
INSERT INTO `Country` VALUES (1,'United States','US','USA',840,1),(2,'Albania','AL','ALB',8,355),(3,'Algeria','DZ','DZA',12,213),(4,'American Samoa','AS','ASM',16,1684),(5,'Andorra','AD','AND',20,376),(6,'Angola','AO','AGO',24,244),(7,'Anguilla','AI','AIA',660,1264),(8,'Antarctica','AQ',NULL,NULL,0),(9,'Antigua and Barbuda','AG','ATG',28,1268),(10,'Argentina','AR','ARG',32,54),(11,'Armenia','AM','ARM',51,374),(12,'Aruba','AW','ABW',533,297),(13,'Australia','AU','AUS',36,61),(14,'Austria','AT','AUT',40,43),(15,'Azerbaijan','AZ','AZE',31,994),(16,'Bahamas','BS','BHS',44,1242),(17,'Bahrain','BH','BHR',48,973),(18,'Bangladesh','BD','BGD',50,880),(19,'Barbados','BB','BRB',52,1246),(20,'Belarus','BY','BLR',112,375),(21,'Belgium','BE','BEL',56,32),(22,'Belize','BZ','BLZ',84,501),(23,'Benin','BJ','BEN',204,229),(24,'Bermuda','BM','BMU',60,1441),(25,'Bhutan','BT','BTN',64,975),(26,'Bolivia','BO','BOL',68,591),(27,'Bosnia and Herzegovina','BA','BIH',70,387),(28,'Botswana','BW','BWA',72,267),(29,'Bouvet Island','BV',NULL,NULL,0),(30,'Brazil','BR','BRA',76,55),(31,'British Indian Ocean Territory','IO',NULL,NULL,246),(32,'Brunei Darussalam','BN','BRN',96,673),(33,'Bulgaria','BG','BGR',100,359),(34,'Burkina Faso','BF','BFA',854,226),(35,'Burundi','BI','BDI',108,257),(36,'Cambodia','KH','KHM',116,855),(37,'Cameroon','CM','CMR',120,237),(38,'Canada','CA','CAN',124,1),(39,'Cape Verde','CV','CPV',132,238),(40,'Cayman Islands','KY','CYM',136,1345),(41,'Central African Republic','CF','CAF',140,236),(42,'Chad','TD','TCD',148,235),(43,'Chile','CL','CHL',152,56),(44,'China','CN','CHN',156,86),(45,'Christmas Island','CX',NULL,NULL,61),(46,'Cocos (Keeling) Islands','CC',NULL,NULL,672),(47,'Colombia','CO','COL',170,57),(48,'Comoros','KM','COM',174,269),(49,'Congo','CG','COG',178,242),(50,'Congo, the Democratic Republic of the','CD','COD',180,242),(51,'Cook Islands','CK','COK',184,682),(52,'Costa Rica','CR','CRI',188,506),(53,'Cote DIvoire','CI','CIV',384,225),(54,'Croatia','HR','HRV',191,385),(55,'Cuba','CU','CUB',192,53),(56,'Cyprus','CY','CYP',196,357),(57,'Czech Republic','CZ','CZE',203,420),(58,'Denmark','DK','DNK',208,45),(59,'Djibouti','DJ','DJI',262,253),(60,'Dominica','DM','DMA',212,1767),(61,'Dominican Republic','DO','DOM',214,1809),(62,'Ecuador','EC','ECU',218,593),(63,'Egypt','EG','EGY',818,20),(64,'El Salvador','SV','SLV',222,503),(65,'Equatorial Guinea','GQ','GNQ',226,240),(66,'Eritrea','ER','ERI',232,291),(67,'Estonia','EE','EST',233,372),(68,'Ethiopia','ET','ETH',231,251),(69,'Falkland Islands (Malvinas)','FK','FLK',238,500),(70,'Faroe Islands','FO','FRO',234,298),(71,'Fiji','FJ','FJI',242,679),(72,'Finland','FI','FIN',246,358),(73,'France','FR','FRA',250,33),(74,'French Guiana','GF','GUF',254,594),(75,'French Polynesia','PF','PYF',258,689),(76,'French Southern Territories','TF',NULL,NULL,0),(77,'Gabon','GA','GAB',266,241),(78,'Gambia','GM','GMB',270,220),(79,'Georgia','GE','GEO',268,995),(80,'Germany','DE','DEU',276,49),(81,'Ghana','GH','GHA',288,233),(82,'Gibraltar','GI','GIB',292,350),(83,'Greece','GR','GRC',300,30),(84,'Greenland','GL','GRL',304,299),(85,'Grenada','GD','GRD',308,1473),(86,'Guadeloupe','GP','GLP',312,590),(87,'Guam','GU','GUM',316,1671),(88,'Guatemala','GT','GTM',320,502),(89,'Guinea','GN','GIN',324,224),(90,'Guinea-Bissau','GW','GNB',624,245),(91,'Guyana','GY','GUY',328,592),(92,'Haiti','HT','HTI',332,509),(93,'Heard Island and Mcdonald Islands','HM',NULL,NULL,0),(94,'Holy See (Vatican City State)','VA','VAT',336,39),(95,'Honduras','HN','HND',340,504),(96,'Hong Kong','HK','HKG',344,852),(97,'Hungary','HU','HUN',348,36),(98,'Iceland','IS','ISL',352,354),(99,'India','IN','IND',356,91),(100,'Indonesia','ID','IDN',360,62),(101,'Iran, Islamic Republic of','IR','IRN',364,98),(102,'Iraq','IQ','IRQ',368,964),(103,'Ireland','IE','IRL',372,353),(104,'Israel','IL','ISR',376,972),(105,'Italy','IT','ITA',380,39),(106,'Jamaica','JM','JAM',388,1876),(107,'Japan','JP','JPN',392,81),(108,'Jordan','JO','JOR',400,962),(109,'Kazakhstan','KZ','KAZ',398,7),(110,'Kenya','KE','KEN',404,254),(111,'Kiribati','KI','KIR',296,686),(112,'Korea, Democratic Peoples Republic of','KP','PRK',408,850),(113,'Korea, Republic of','KR','KOR',410,82),(114,'Kuwait','KW','KWT',414,965),(115,'Kyrgyzstan','KG','KGZ',417,996),(116,'Lao Peoples Democratic Republic','LA','LAO',418,856),(117,'Latvia','LV','LVA',428,371),(118,'Lebanon','LB','LBN',422,961),(119,'Lesotho','LS','LSO',426,266),(120,'Liberia','LR','LBR',430,231),(121,'Libyan Arab Jamahiriya','LY','LBY',434,218),(122,'Liechtenstein','LI','LIE',438,423),(123,'Lithuania','LT','LTU',440,370),(124,'Luxembourg','LU','LUX',442,352),(125,'Macao','MO','MAC',446,853),(126,'Macedonia, the Former Yugoslav Republic of','MK','MKD',807,389),(127,'Madagascar','MG','MDG',450,261),(128,'Malawi','MW','MWI',454,265),(129,'Malaysia','MY','MYS',458,60),(130,'Maldives','MV','MDV',462,960),(131,'Mali','ML','MLI',466,223),(132,'Malta','MT','MLT',470,356),(133,'Marshall Islands','MH','MHL',584,692),(134,'Martinique','MQ','MTQ',474,596),(135,'Mauritania','MR','MRT',478,222),(136,'Mauritius','MU','MUS',480,230),(137,'Mayotte','YT',NULL,NULL,269),(138,'Mexico','MX','MEX',484,52),(139,'Micronesia, Federated States of','FM','FSM',583,691),(140,'Moldova, Republic of','MD','MDA',498,373),(141,'Monaco','MC','MCO',492,377),(142,'Mongolia','MN','MNG',496,976),(143,'Montserrat','MS','MSR',500,1664),(144,'Morocco','MA','MAR',504,212),(145,'Mozambique','MZ','MOZ',508,258),(146,'Myanmar','MM','MMR',104,95),(147,'Namibia','NA','NAM',516,264),(148,'Nauru','NR','NRU',520,674),(149,'Nepal','NP','NPL',524,977),(150,'Netherlands','NL','NLD',528,31),(151,'Netherlands Antilles','AN','ANT',530,599),(152,'New Caledonia','NC','NCL',540,687),(153,'New Zealand','NZ','NZL',554,64),(154,'Nicaragua','NI','NIC',558,505),(155,'Niger','NE','NER',562,227),(156,'Nigeria','NG','NGA',566,234),(157,'Niue','NU','NIU',570,683),(158,'Norfolk Island','NF','NFK',574,672),(159,'Northern Mariana Islands','MP','MNP',580,1670),(160,'Norway','NO','NOR',578,47),(161,'Oman','OM','OMN',512,968),(162,'Pakistan','PK','PAK',586,92),(163,'Palau','PW','PLW',585,680),(164,'Palestinian Territory, Occupied','PS',NULL,NULL,970),(165,'Panama','PA','PAN',591,507),(166,'Papua New Guinea','PG','PNG',598,675),(167,'Paraguay','PY','PRY',600,595),(168,'Peru','PE','PER',604,51),(169,'Philippines','PH','PHL',608,63),(170,'Pitcairn','PN','PCN',612,0),(171,'Poland','PL','POL',616,48),(172,'Portugal','PT','PRT',620,351),(173,'Puerto Rico','PR','PRI',630,1787),(174,'Qatar','QA','QAT',634,974),(175,'Reunion','RE','REU',638,262),(176,'Romania','RO','ROM',642,40),(177,'Russian Federation','RU','RUS',643,70),(178,'Rwanda','RW','RWA',646,250),(179,'Saint Helena','SH','SHN',654,290),(180,'Saint Kitts and Nevis','KN','KNA',659,1869),(181,'Saint Lucia','LC','LCA',662,1758),(182,'Saint Pierre and Miquelon','PM','SPM',666,508),(183,'Saint Vincent and the Grenadines','VC','VCT',670,1784),(184,'Samoa','WS','WSM',882,684),(185,'San Marino','SM','SMR',674,378),(186,'Sao Tome and Principe','ST','STP',678,239),(187,'Saudi Arabia','SA','SAU',682,966),(188,'Senegal','SN','SEN',686,221),(189,'Serbia and Montenegro','CS',NULL,NULL,381),(190,'Seychelles','SC','SYC',690,248),(191,'Sierra Leone','SL','SLE',694,232),(192,'Singapore','SG','SGP',702,65),(193,'Slovakia','SK','SVK',703,421),(194,'Slovenia','SI','SVN',705,386),(195,'Solomon Islands','SB','SLB',90,677),(196,'Somalia','SO','SOM',706,252),(197,'South Africa','ZA','ZAF',710,27),(198,'South Georgia and the South Sandwich Islands','GS',NULL,NULL,0),(199,'Spain','ES','ESP',724,34),(200,'Sri Lanka','LK','LKA',144,94),(201,'Sudan','SD','SDN',736,249),(202,'Suriname','SR','SUR',740,597),(203,'Svalbard and Jan Mayen','SJ','SJM',744,47),(204,'Swaziland','SZ','SWZ',748,268),(205,'Sweden','SE','SWE',752,46),(206,'Switzerland','CH','CHE',756,41),(207,'Syrian Arab Republic','SY','SYR',760,963),(208,'Taiwan, Province of China','TW','TWN',158,886),(209,'Tajikistan','TJ','TJK',762,992),(210,'Tanzania, United Republic of','TZ','TZA',834,255),(211,'Thailand','TH','THA',764,66),(212,'Timor-Leste','TL',NULL,NULL,670),(213,'Togo','TG','TGO',768,228),(214,'Tokelau','TK','TKL',772,690),(215,'Tonga','TO','TON',776,676),(216,'Trinidad and Tobago','TT','TTO',780,1868),(217,'Tunisia','TN','TUN',788,216),(218,'Turkey','TR','TUR',792,90),(219,'Turkmenistan','TM','TKM',795,7370),(220,'Turks and Caicos Islands','TC','TCA',796,1649),(221,'Tuvalu','TV','TUV',798,688),(222,'Uganda','UG','UGA',800,256),(223,'Ukraine','UA','UKR',804,380),(224,'United Arab Emirates','AE','ARE',784,971),(225,'United Kingdom','GB','GBR',826,44),(226,'Afghanistan','AF','AFG',4,93),(227,'United States Minor Outlying Islands','UM',NULL,NULL,1),(228,'Uruguay','UY','URY',858,598),(229,'Uzbekistan','UZ','UZB',860,998),(230,'Vanuatu','VU','VUT',548,678),(231,'Venezuela','VE','VEN',862,58),(232,'Viet Nam','VN','VNM',704,84),(233,'Virgin Islands, British','VG','VGB',92,1284),(234,'Virgin Islands, U.s.','VI','VIR',850,1340),(235,'Wallis and Futuna','WF','WLF',876,681),(236,'Western Sahara','EH','ESH',732,212),(237,'Yemen','YE','YEM',887,967),(238,'Zambia','ZM','ZMB',894,260),(239,'Zimbabwe','ZW','ZWE',716,263);
/*!40000 ALTER TABLE `Country` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Email`
--

DROP TABLE IF EXISTS `Email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Email` (
  `idEmail` int(11) NOT NULL AUTO_INCREMENT,
  `Email` varchar(45) NOT NULL,
  `EmailType_idEmailType` int(11) NOT NULL,
  `Person_idPerson` int(11) NOT NULL,
  PRIMARY KEY (`idEmail`),
  UNIQUE KEY `cn_Email_Unique` (`Email`),
  KEY `fk_Email_EmailType` (`EmailType_idEmailType`),
  KEY `fk_Email_Person` (`Person_idPerson`),
  CONSTRAINT `fk_Email_EmailType` FOREIGN KEY (`EmailType_idEmailType`) REFERENCES `EmailType` (`idEmailType`),
  CONSTRAINT `fk_Email_Person` FOREIGN KEY (`Person_idPerson`) REFERENCES `Person` (`idPerson`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `EmailType`
--

DROP TABLE IF EXISTS `EmailType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `EmailType` (
  `idEmailType` int(11) NOT NULL AUTO_INCREMENT,
  `EmailType` varchar(45) NOT NULL,
  PRIMARY KEY (`idEmailType`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `EmailType`
--

LOCK TABLES `EmailType` WRITE;
/*!40000 ALTER TABLE `EmailType` DISABLE KEYS */;
INSERT INTO `EmailType` VALUES (1,'Login'),(2,'Alternate');
/*!40000 ALTER TABLE `EmailType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Gender`
--

DROP TABLE IF EXISTS `Gender`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Gender` (
  `idGender` int(11) NOT NULL AUTO_INCREMENT,
  `Gender` varchar(45) NOT NULL,
  PRIMARY KEY (`idGender`),
  UNIQUE KEY `Gender` (`Gender`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Gender`
--

LOCK TABLES `Gender` WRITE;
/*!40000 ALTER TABLE `Gender` DISABLE KEYS */;
INSERT INTO `Gender` VALUES (2,'Female'),(1,'Male'),(3,'Other');
/*!40000 ALTER TABLE `Gender` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Issue`
--

DROP TABLE IF EXISTS `Issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Issue` (
  `idIssue` int(11) NOT NULL AUTO_INCREMENT,
  `Issue` varchar(100) NOT NULL,
  `IssueDescription` varchar(255) DEFAULT NULL,
  `Attachment_idAttachment` int(11) DEFAULT NULL,
  `IssueCategory_idIssueCategory` int(11) NOT NULL,
  `CreatePerson_idPerson` int(11) NOT NULL,
  `PropertyInformation_idPropertyInformation` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idIssue`),
  KEY `IssueCategory_idIssueCategory` (`IssueCategory_idIssueCategory`),
  KEY `CreatePerson_idPerson` (`CreatePerson_idPerson`),
  KEY `PropertyInformation_idPropertyInformation` (`PropertyInformation_idPropertyInformation`),
  KEY `fk_Issue_Attachment` (`Attachment_idAttachment`),
  CONSTRAINT `fk_Issue_Attachment` FOREIGN KEY (`Attachment_idAttachment`) REFERENCES `Attachment` (`idAttachment`),
  CONSTRAINT `fk_Issue_CreatePerson` FOREIGN KEY (`CreatePerson_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_Issue_IssueCategory` FOREIGN KEY (`IssueCategory_idIssueCategory`) REFERENCES `IssueCategory` (`idIssueCategory`),
  CONSTRAINT `fk_Issue_PropertyInformation` FOREIGN KEY (`PropertyInformation_idPropertyInformation`) REFERENCES `PropertyInformation` (`idPropertyInformation`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IssueCategory`
--

DROP TABLE IF EXISTS `IssueCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IssueCategory` (
  `idIssueCategory` int(11) NOT NULL AUTO_INCREMENT,
  `IssueCategory` varchar(45) NOT NULL,
  PRIMARY KEY (`idIssueCategory`),
  UNIQUE KEY `IssueCategory` (`IssueCategory`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IssueCategory`
--

LOCK TABLES `IssueCategory` WRITE;
/*!40000 ALTER TABLE `IssueCategory` DISABLE KEYS */;
INSERT INTO `IssueCategory` VALUES (5,'Appliances'),(8,'Electrical'),(4,'Flooring'),(2,'HAVAC'),(9,'Heating'),(3,'Landscaping'),(10,'Other'),(6,'Paintwork'),(7,'Plumbing'),(1,'Roof');
/*!40000 ALTER TABLE `IssueCategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `IssueDetail`
--

DROP TABLE IF EXISTS `IssueDetail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IssueDetail` (
  `idIssueDetail` int(11) NOT NULL AUTO_INCREMENT,
  `Issue_idIssue` int(11) NOT NULL,
  `Status_idIssueStatus` int(11) NOT NULL,
  `StatusUpdate_idPerson` int(11) NOT NULL,
  `StatusUpdateDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`idIssueDetail`),
  KEY `fk_IssueDetail_Issue` (`Issue_idIssue`),
  KEY `fk_IssueDetail_IssueStatus` (`Status_idIssueStatus`),
  KEY `fk_IssueDetail_StatusUpdate_Person` (`StatusUpdate_idPerson`),
  CONSTRAINT `fk_IssueDetail_Issue` FOREIGN KEY (`Issue_idIssue`) REFERENCES `Issue` (`idIssue`),
  CONSTRAINT `fk_IssueDetail_IssueStatus` FOREIGN KEY (`Status_idIssueStatus`) REFERENCES `IssueStatus` (`idIssueStatus`),
  CONSTRAINT `fk_IssueDetail_StatusUpdate_Person` FOREIGN KEY (`StatusUpdate_idPerson`) REFERENCES `Person` (`idPerson`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IssueLog`
--

DROP TABLE IF EXISTS `IssueLog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IssueLog` (
  `idIssueLog` int(11) NOT NULL AUTO_INCREMENT,
  `Issue_idIssue` int(11) NOT NULL,
  `CreatePerson_idPerson` int(11) NOT NULL,
  `Log` longtext NOT NULL,
  `Attachment_idAttachment` int(11) DEFAULT NULL,
  `CreatedDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`idIssueLog`),
  KEY `fk_IssueLog_Person` (`CreatePerson_idPerson`),
  KEY `fk_IssueLog_Issue` (`Issue_idIssue`),
  KEY `fk_IssueLog_Attachment` (`Attachment_idAttachment`),
  CONSTRAINT `fk_IssueLog_Attachment` FOREIGN KEY (`Attachment_idAttachment`) REFERENCES `Attachment` (`idAttachment`),
  CONSTRAINT `fk_IssueLog_Issue` FOREIGN KEY (`Issue_idIssue`) REFERENCES `Issue` (`idIssue`),
  CONSTRAINT `fk_IssueLog_Person` FOREIGN KEY (`CreatePerson_idPerson`) REFERENCES `Person` (`idPerson`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `IssueStatus`
--

DROP TABLE IF EXISTS `IssueStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `IssueStatus` (
  `idIssueStatus` int(11) NOT NULL AUTO_INCREMENT,
  `IssueStatus` varchar(45) NOT NULL,
  PRIMARY KEY (`idIssueStatus`),
  UNIQUE KEY `IssueStatus` (`IssueStatus`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IssueStatus`
--

LOCK TABLES `IssueStatus` WRITE;
/*!40000 ALTER TABLE `IssueStatus` DISABLE KEYS */;
INSERT INTO `IssueStatus` VALUES (3,'Approved'),(2,'Completed'),(1,'Initiated');
/*!40000 ALTER TABLE `IssueStatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Organization`
--

DROP TABLE IF EXISTS `Organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Organization` (
  `idOrganization` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  PRIMARY KEY (`idOrganization`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Person`
--

DROP TABLE IF EXISTS `Person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Person` (
  `idPerson` int(11) NOT NULL AUTO_INCREMENT,
  `User_EmailAddress` varchar(45) NOT NULL,
  `PersonType_idPersonType` int(11) NOT NULL,
  `Role_idRole` int(11) DEFAULT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idPerson`),
  UNIQUE KEY `cn_Person_Unique` (`User_EmailAddress`),
  KEY `User_EmailAddress` (`User_EmailAddress`),
  KEY `PersonType_idPersonType` (`PersonType_idPersonType`),
  KEY `Role_idRole` (`Role_idRole`),
  CONSTRAINT `fk_Person_PersonType` FOREIGN KEY (`PersonType_idPersonType`) REFERENCES `PersonType` (`idPersonType`),
  CONSTRAINT `fk_Person_Role` FOREIGN KEY (`Role_idRole`) REFERENCES `Role` (`idRole`),
  CONSTRAINT `fk_Person_User` FOREIGN KEY (`User_EmailAddress`) REFERENCES `User` (`EmailAddress`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PersonType`
--

DROP TABLE IF EXISTS `PersonType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PersonType` (
  `idPersonType` int(11) NOT NULL AUTO_INCREMENT,
  `PersonType` varchar(45) NOT NULL,
  PRIMARY KEY (`idPersonType`),
  UNIQUE KEY `PersonType` (`PersonType`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PersonType`
--

LOCK TABLES `PersonType` WRITE;
/*!40000 ALTER TABLE `PersonType` DISABLE KEYS */;
INSERT INTO `PersonType` VALUES (7,'CompanyOwner'),(4,'Investor'),(1,'Owner'),(6,'Owner/PropertyManager'),(3,'PropertyManager'),(5,'ServiceProvider'),(2,'Tenant');
/*!40000 ALTER TABLE `PersonType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Phone`
--

DROP TABLE IF EXISTS `Phone`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Phone` (
  `idPhone` int(11) NOT NULL AUTO_INCREMENT,
  `PhoneNumber` varchar(15) NOT NULL,
  `PhoneType_idPhoneType` int(11) NOT NULL,
  `Person_idPerson` int(11) NOT NULL,
  PRIMARY KEY (`idPhone`),
  UNIQUE KEY `cn_Phone_Unique` (`PhoneNumber`,`PhoneType_idPhoneType`,`Person_idPerson`),
  KEY `fk_Phone_PhoneType` (`PhoneType_idPhoneType`),
  KEY `fk_Phone_Person` (`Person_idPerson`),
  CONSTRAINT `fk_Phone_Person` FOREIGN KEY (`Person_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_Phone_PhoneType` FOREIGN KEY (`PhoneType_idPhoneType`) REFERENCES `PhoneType` (`idPhoneType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PhoneType`
--

DROP TABLE IF EXISTS `PhoneType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PhoneType` (
  `idPhoneType` int(11) NOT NULL AUTO_INCREMENT,
  `PhoneType` varchar(45) NOT NULL,
  PRIMARY KEY (`idPhoneType`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PhoneType`
--

LOCK TABLES `PhoneType` WRITE;
/*!40000 ALTER TABLE `PhoneType` DISABLE KEYS */;
INSERT INTO `PhoneType` VALUES (1,'Office'),(2,'Home'),(3,'Mobile');
/*!40000 ALTER TABLE `PhoneType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PropertyInformation`
--

DROP TABLE IF EXISTS `PropertyInformation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PropertyInformation` (
  `idPropertyInformation` int(11) NOT NULL AUTO_INCREMENT,
  `SquareFeet` varchar(45) DEFAULT NULL,
  `Beds` varchar(45) DEFAULT NULL,
  `Baths` varchar(45) DEFAULT NULL,
  `Stories` varchar(45) DEFAULT NULL,
  `Lotsize` varchar(45) DEFAULT NULL,
  `YearBuilt` varchar(45) DEFAULT NULL,
  `YearRenovated` varchar(45) DEFAULT NULL,
  `APN` varchar(45) DEFAULT NULL,
  `Community` varchar(45) DEFAULT NULL,
  `HOADues` varchar(45) DEFAULT NULL,
  `PropertyType_idPropertyType` int(11) NOT NULL,
  `Address_idAddress` int(11) NOT NULL,
  `CreatePerson_idPerson` int(11) NOT NULL,
  `CreateDate` timestamp NULL DEFAULT NULL,
  `UpdatePerson_idPerson` int(11) DEFAULT NULL,
  `UpdateDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`idPropertyInformation`),
  KEY `PropertyType_idPropertyType` (`PropertyType_idPropertyType`),
  KEY `Address_idAddress` (`Address_idAddress`),
  KEY `CreatePerson_idPerson` (`CreatePerson_idPerson`),
  CONSTRAINT `fk_PropertyInformation_Address` FOREIGN KEY (`Address_idAddress`) REFERENCES `Address` (`idAddress`),
  CONSTRAINT `fk_PropertyInformation_Person` FOREIGN KEY (`CreatePerson_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_PropertyInformation_PropertyType` FOREIGN KEY (`PropertyType_idPropertyType`) REFERENCES `PropertyType` (`idPropertyType`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PropertyInformationDeleted`
--

DROP TABLE IF EXISTS `PropertyInformationDeleted`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PropertyInformationDeleted` (
  `idPropertyInformationDeleted` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyInformation_idPropertyInformation` int(11) NOT NULL,
  `DeletedReason` varchar(100) NOT NULL,
  `DeletedBy_idPerson` int(11) NOT NULL,
  `DeletedDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`idPropertyInformationDeleted`),
  KEY `fk_PropertyInformationDeleted_PropertyInformation` (`PropertyInformation_idPropertyInformation`),
  KEY `fk_PropertyInformationDeleted_Person` (`DeletedBy_idPerson`),
  CONSTRAINT `fk_PropertyInformationDeleted_Person` FOREIGN KEY (`DeletedBy_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_PropertyInformationDeleted_PropertyInformation` FOREIGN KEY (`PropertyInformation_idPropertyInformation`) REFERENCES `PropertyInformation` (`idPropertyInformation`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PropertyInformationPerson`
--

DROP TABLE IF EXISTS `PropertyInformationPerson`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PropertyInformationPerson` (
  `idPropertyInformationPerson` int(11) NOT NULL AUTO_INCREMENT,
  `Person_idPerson` int(11) NOT NULL,
  `PropertyInformation_idPropertyInformation` int(11) NOT NULL,
  `StartDate` timestamp NULL DEFAULT NULL,
  `EndDate` timestamp NULL DEFAULT NULL,
  `UpdateBy_idPerson` int(11) DEFAULT NULL,
  `UpdateDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`idPropertyInformationPerson`),
  UNIQUE KEY `cn_PropertyInformationPerson_Unique` (`Person_idPerson`,`PropertyInformation_idPropertyInformation`),
  KEY `fk_PIP_PropertyInformation` (`PropertyInformation_idPropertyInformation`),
  KEY `fk_PIP_UpdatePerson` (`UpdateBy_idPerson`),
  CONSTRAINT `fk_PIP_Person` FOREIGN KEY (`Person_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_PIP_PropertyInformation` FOREIGN KEY (`PropertyInformation_idPropertyInformation`) REFERENCES `PropertyInformation` (`idPropertyInformation`),
  CONSTRAINT `fk_PIP_UpdatePerson` FOREIGN KEY (`UpdateBy_idPerson`) REFERENCES `Person` (`idPerson`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PropertyType`
--

DROP TABLE IF EXISTS `PropertyType`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PropertyType` (
  `idPropertyType` int(11) NOT NULL AUTO_INCREMENT,
  `PropertyType` varchar(45) NOT NULL,
  PRIMARY KEY (`idPropertyType`),
  UNIQUE KEY `PropertyType` (`PropertyType`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PropertyType`
--

LOCK TABLES `PropertyType` WRITE;
/*!40000 ALTER TABLE `PropertyType` DISABLE KEYS */;
INSERT INTO `PropertyType` VALUES (4,'Condo/Appartments'),(1,'Land'),(3,'MultiFamily'),(2,'SingleFamily');
/*!40000 ALTER TABLE `PropertyType` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Role`
--

DROP TABLE IF EXISTS `Role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Role` (
  `idRole` int(11) NOT NULL AUTO_INCREMENT,
  `RoleName` varchar(45) NOT NULL,
  PRIMARY KEY (`idRole`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ServiceCategory`
--

DROP TABLE IF EXISTS `ServiceCategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ServiceCategory` (
  `idServiceCategory` int(11) NOT NULL AUTO_INCREMENT,
  `ServiceCategory` varchar(100) NOT NULL,
  PRIMARY KEY (`idServiceCategory`),
  UNIQUE KEY `ServiceCategory` (`ServiceCategory`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServiceCategory`
--

LOCK TABLES `ServiceCategory` WRITE;
/*!40000 ALTER TABLE `ServiceCategory` DISABLE KEYS */;
INSERT INTO `ServiceCategory` VALUES (3,'Air Conditioning'),(2,'Electrical'),(5,'Flooring'),(9,'HAVAC'),(4,'Heating'),(7,'Lansdcape'),(10,'Other'),(8,'Paintwork'),(1,'Plumbing'),(6,'Roof');
/*!40000 ALTER TABLE `ServiceCategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServiceProvider`
--

DROP TABLE IF EXISTS `ServiceProvider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ServiceProvider` (
  `idServiceProvider` int(11) NOT NULL AUTO_INCREMENT,
  `ServiceCategory_idServiceCategory` int(11) NOT NULL,
  `YearsOfExperience` varchar(4) DEFAULT NULL,
  `AreasOfExpertise` varchar(45) DEFAULT NULL,
  `AreaCoverage` varchar(45) DEFAULT NULL,
  `Person_idPerson` int(11) NOT NULL,
  PRIMARY KEY (`idServiceProvider`),
  UNIQUE KEY `Person_idPerson` (`Person_idPerson`),
  KEY `fk_ServiceProvider_ServiceCategory` (`ServiceCategory_idServiceCategory`),
  CONSTRAINT `fk_ServiceProvider_Person` FOREIGN KEY (`Person_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_ServiceProvider_ServiceCategory` FOREIGN KEY (`ServiceCategory_idServiceCategory`) REFERENCES `ServiceCategory` (`idServiceCategory`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ServiceProviderIssue`
--

DROP TABLE IF EXISTS `ServiceProviderIssue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ServiceProviderIssue` (
  `idServiceProviderIssue` int(11) NOT NULL AUTO_INCREMENT,
  `ServiceProvider_idServiceProvider` int(11) NOT NULL,
  `Issue_idIssue` int(11) NOT NULL,
  `AssignBy_idPerson` int(11) NOT NULL,
  `AssignDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`idServiceProviderIssue`),
  KEY `fk_ServiceProviderIssue_ServiceProvider` (`ServiceProvider_idServiceProvider`),
  KEY `fk_ServiceProviderIssue_Issue` (`Issue_idIssue`),
  KEY `fk_ServiceProviderIssue_AssignByPerson` (`AssignBy_idPerson`),
  CONSTRAINT `fk_ServiceProviderIssue_AssignByPerson` FOREIGN KEY (`AssignBy_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_ServiceProviderIssue_Issue` FOREIGN KEY (`Issue_idIssue`) REFERENCES `Issue` (`idIssue`),
  CONSTRAINT `fk_ServiceProviderIssue_ServiceProvider` FOREIGN KEY (`ServiceProvider_idServiceProvider`) REFERENCES `ServiceProvider` (`idServiceProvider`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `State`
--

DROP TABLE IF EXISTS `State`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `State` (
  `idState` int(11) NOT NULL AUTO_INCREMENT,
  `StateCode` varchar(2) NOT NULL,
  `StateName` varchar(45) DEFAULT NULL,
  `Country_idCountry` int(11) NOT NULL,
  PRIMARY KEY (`idState`),
  UNIQUE KEY `cn_State_Unique` (`StateCode`,`Country_idCountry`),
  KEY `fk_State_Country` (`Country_idCountry`),
  CONSTRAINT `fk_State_Country` FOREIGN KEY (`Country_idCountry`) REFERENCES `Country` (`idCountry`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `State`
--

LOCK TABLES `State` WRITE;
/*!40000 ALTER TABLE `State` DISABLE KEYS */;
INSERT INTO `State` VALUES (58,'AL','Alabama',1),(59,'AK','Alaska',1),(60,'AZ','Arizona',1),(61,'AR','Arkansas',1),(62,'CA','California',1),(63,'CO','Colorado',1),(64,'CT','Connecticut',1),(65,'DE','Delaware',1),(66,'DC','District of Columbia',1),(67,'FL','Florida',1),(68,'GA','Georgia',1),(69,'HI','Hawaii',1),(70,'ID','Idaho',1),(71,'IL','Illinois',1),(72,'IN','Indiana',1),(73,'IA','Iowa',1),(74,'KS','Kansas',1),(75,'KY','Kentucky',1),(76,'LA','Louisiana',1),(77,'ME','Maine',1),(78,'MD','Maryland',1),(79,'MA','Massachusetts',1),(80,'MI','Michigan',1),(81,'MN','Minnesota',1),(82,'MS','Mississippi',1),(83,'MO','Missouri',1),(84,'MT','Montana',1),(85,'NE','Nebraska',1),(86,'NV','Nevada',1),(87,'NH','New Hampshire',1),(88,'NJ','New Jersey',1),(89,'NM','New Mexico',1),(90,'NY','New York',1),(91,'NC','North Carolina',1),(92,'ND','North Dakota',1),(93,'OH','Ohio',1),(94,'OK','Oklahoma',1),(95,'OR','Oregon',1),(96,'PA','Pennsylvania',1),(97,'PR','Puerto Rico',1),(98,'RI','Rhode Island',1),(99,'SC','South Carolina',1),(100,'SD','South Dakota',1),(101,'TN','Tennessee',1),(102,'TX','Texas',1),(103,'UT','Utah',1),(104,'VT','Vermont',1),(105,'VA','Virginia',1),(106,'WA','Washington',1),(107,'WV','West Virginia',1),(108,'WI','Wisconsin',1),(109,'WY','Wyoming',1);
/*!40000 ALTER TABLE `State` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `EmailAddress` varchar(100) NOT NULL,
  `UserPassword` varchar(45) NOT NULL,
  `Enabled` tinyint(4) DEFAULT '0',
  `FirstName` varchar(45) DEFAULT NULL,
  `LastName` varchar(45) DEFAULT NULL,
  `MiddleName` varchar(45) DEFAULT NULL,
  `DOB` varchar(45) DEFAULT NULL,
  `Gender_idGender` int(11) DEFAULT NULL,
  `VerificationToken` varchar(45) NOT NULL,
  PRIMARY KEY (`EmailAddress`),
  KEY `fk_User_Gender` (`Gender_idGender`),
  CONSTRAINT `fk_User_Gender` FOREIGN KEY (`Gender_idGender`) REFERENCES `Gender` (`idGender`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-15 22:43:04
