CREATE DATABASE  IF NOT EXISTS `RealtyDB` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `RealtyDB`;
-- MySQL dump 10.13  Distrib 5.7.30, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: RealtyDB
-- ------------------------------------------------------
-- Server version	5.7.30-0ubuntu0.18.04.1

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Access`
--

LOCK TABLES `Access` WRITE;
/*!40000 ALTER TABLE `Access` DISABLE KEYS */;
/*!40000 ALTER TABLE `Access` ENABLE KEYS */;
UNLOCK TABLES;

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
  `Zip` int(5) DEFAULT NULL,
  `CityName` varchar(100) DEFAULT NULL,
  `StateCode` varchar(2) DEFAULT NULL,
  `CountryCode` varchar(2) DEFAULT NULL,
  `AddressType_idAddressType` int(11) NOT NULL,
  `Person_idPerson` int(11) DEFAULT NULL,
  PRIMARY KEY (`idAddress`),
  UNIQUE KEY `cn_Address_Unique` (`Address1`,`Address2`,`Address3`,`Zip`,`AddressType_idAddressType`,`Person_idPerson`),
  KEY `fk_Address_Person` (`Person_idPerson`),
  KEY `fk_Address_AddressType` (`AddressType_idAddressType`),
  CONSTRAINT `fk_Address_AddressType` FOREIGN KEY (`AddressType_idAddressType`) REFERENCES `AddressType` (`idAddressType`),
  CONSTRAINT `fk_Address_Person` FOREIGN KEY (`Person_idPerson`) REFERENCES `Person` (`idPerson`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Address`
--

LOCK TABLES `Address` WRITE;
/*!40000 ALTER TABLE `Address` DISABLE KEYS */;
/*!40000 ALTER TABLE `Address` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AddressType`
--

LOCK TABLES `AddressType` WRITE;
/*!40000 ALTER TABLE `AddressType` DISABLE KEYS */;
INSERT INTO `AddressType` VALUES (1,'Shipping'),(2,'Billing'),(3,'Mailing');
/*!40000 ALTER TABLE `AddressType` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `Company`
--

LOCK TABLES `Company` WRITE;
/*!40000 ALTER TABLE `Company` DISABLE KEYS */;
/*!40000 ALTER TABLE `Company` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Email`
--

LOCK TABLES `Email` WRITE;
/*!40000 ALTER TABLE `Email` DISABLE KEYS */;
/*!40000 ALTER TABLE `Email` ENABLE KEYS */;
UNLOCK TABLES;

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
INSERT INTO `Gender` VALUES (2,'Female'),(1,'Male'),(3,'TransGender');
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
  `Attachment` blob,
  `IssueStatus_idIssueStatus` int(11) NOT NULL,
  `IssueCategory_idIssueCategory` int(11) NOT NULL,
  `CreatePerson_idPerson` int(11) NOT NULL,
  `PropertyInformation_idPropertyInformation` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idIssue`),
  KEY `IssueStatus_idIssueStatus` (`IssueStatus_idIssueStatus`),
  KEY `IssueCategory_idIssueCategory` (`IssueCategory_idIssueCategory`),
  KEY `CreatePerson_idPerson` (`CreatePerson_idPerson`),
  KEY `PropertyInformation_idPropertyInformation` (`PropertyInformation_idPropertyInformation`),
  CONSTRAINT `fk_Issue_CreatePerson` FOREIGN KEY (`CreatePerson_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_Issue_IssueCategory` FOREIGN KEY (`IssueCategory_idIssueCategory`) REFERENCES `IssueCategory` (`idIssueCategory`),
  CONSTRAINT `fk_Issue_IssueStatus` FOREIGN KEY (`IssueStatus_idIssueStatus`) REFERENCES `IssueStatus` (`idIssueStatus`),
  CONSTRAINT `fk_Issue_PropertyInformation` FOREIGN KEY (`PropertyInformation_idPropertyInformation`) REFERENCES `PropertyInformation` (`idPropertyInformation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Issue`
--

LOCK TABLES `Issue` WRITE;
/*!40000 ALTER TABLE `Issue` DISABLE KEYS */;
/*!40000 ALTER TABLE `Issue` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IssueCategory`
--

LOCK TABLES `IssueCategory` WRITE;
/*!40000 ALTER TABLE `IssueCategory` DISABLE KEYS */;
INSERT INTO `IssueCategory` VALUES (5,'Appliances'),(4,'Flooring'),(2,'HAVAC'),(7,'Kitchen'),(3,'Landscaping'),(6,'Paintwork'),(1,'Roof');
/*!40000 ALTER TABLE `IssueCategory` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `IssueStatus`
--

LOCK TABLES `IssueStatus` WRITE;
/*!40000 ALTER TABLE `IssueStatus` DISABLE KEYS */;
INSERT INTO `IssueStatus` VALUES (2,'Completed'),(1,'Initiated');
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
-- Dumping data for table `Organization`
--

LOCK TABLES `Organization` WRITE;
/*!40000 ALTER TABLE `Organization` DISABLE KEYS */;
/*!40000 ALTER TABLE `Organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OwnerPropertyManager`
--

DROP TABLE IF EXISTS `OwnerPropertyManager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OwnerPropertyManager` (
  `idOwnerPropertyManager` int(11) NOT NULL AUTO_INCREMENT,
  `Owner_idPerson` int(11) NOT NULL,
  `PropertyManager_idPerson` int(11) NOT NULL,
  `PropertyInformation_idPropertyInformation` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idOwnerPropertyManager`),
  UNIQUE KEY `cn_OwnerPropertyManager_Unique` (`Owner_idPerson`,`PropertyManager_idPerson`,`PropertyInformation_idPropertyInformation`),
  KEY `Owner_idPerson` (`Owner_idPerson`),
  KEY `PropertyManager_idPerson` (`PropertyManager_idPerson`),
  KEY `fk_OwnerPropertyManager_PropertyInformation` (`PropertyInformation_idPropertyInformation`),
  CONSTRAINT `fk_OwnerPropertyManager_OwnerPerson` FOREIGN KEY (`Owner_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_OwnerPropertyManager_PropertyInformation` FOREIGN KEY (`PropertyInformation_idPropertyInformation`) REFERENCES `PropertyInformation` (`idPropertyInformation`),
  CONSTRAINT `fk_OwnerPropertyManager_PropertyManagerPerson` FOREIGN KEY (`PropertyManager_idPerson`) REFERENCES `Person` (`idPerson`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OwnerPropertyManager`
--

LOCK TABLES `OwnerPropertyManager` WRITE;
/*!40000 ALTER TABLE `OwnerPropertyManager` DISABLE KEYS */;
/*!40000 ALTER TABLE `OwnerPropertyManager` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Person`
--

LOCK TABLES `Person` WRITE;
/*!40000 ALTER TABLE `Person` DISABLE KEYS */;
/*!40000 ALTER TABLE `Person` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `Phone`
--

LOCK TABLES `Phone` WRITE;
/*!40000 ALTER TABLE `Phone` DISABLE KEYS */;
/*!40000 ALTER TABLE `Phone` ENABLE KEYS */;
UNLOCK TABLES;

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
  `OwnerPerson_idPerson` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idPropertyInformation`),
  KEY `PropertyType_idPropertyType` (`PropertyType_idPropertyType`),
  KEY `Address_idAddress` (`Address_idAddress`),
  KEY `CreatePerson_idPerson` (`OwnerPerson_idPerson`),
  CONSTRAINT `fk_PropertyInformation_Address` FOREIGN KEY (`Address_idAddress`) REFERENCES `Address` (`idAddress`),
  CONSTRAINT `fk_PropertyInformation_Person` FOREIGN KEY (`OwnerPerson_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_PropertyInformation_PropertyType` FOREIGN KEY (`PropertyType_idPropertyType`) REFERENCES `PropertyType` (`idPropertyType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PropertyInformation`
--

LOCK TABLES `PropertyInformation` WRITE;
/*!40000 ALTER TABLE `PropertyInformation` DISABLE KEYS */;
/*!40000 ALTER TABLE `PropertyInformation` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `Role`
--

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;
/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ServiceProvider`
--

DROP TABLE IF EXISTS `ServiceProvider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ServiceProvider` (
  `idServiceProvider` int(11) NOT NULL AUTO_INCREMENT,
  `ServiceCategory` varchar(45) NOT NULL,
  `YearsOfExperience` varchar(4) DEFAULT NULL,
  `AreasOfExpertise` varchar(45) DEFAULT NULL,
  `AreaCoverage` varchar(45) DEFAULT NULL,
  `Person_idPerson` int(11) NOT NULL,
  PRIMARY KEY (`idServiceProvider`),
  UNIQUE KEY `Person_idPerson` (`Person_idPerson`),
  CONSTRAINT `fk_ServiceProvider_Person` FOREIGN KEY (`Person_idPerson`) REFERENCES `Person` (`idPerson`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServiceProvider`
--

LOCK TABLES `ServiceProvider` WRITE;
/*!40000 ALTER TABLE `ServiceProvider` DISABLE KEYS */;
/*!40000 ALTER TABLE `ServiceProvider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tenant`
--

DROP TABLE IF EXISTS `Tenant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tenant` (
  `idTenant` int(11) NOT NULL AUTO_INCREMENT,
  `Person_idPerson` int(11) NOT NULL,
  `PropertyInformation_idPropertyInformation` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idTenant`),
  UNIQUE KEY `cn_Tenant_Unique` (`Person_idPerson`,`PropertyInformation_idPropertyInformation`),
  KEY `PropertyInformation_idPropertyInformation` (`PropertyInformation_idPropertyInformation`),
  CONSTRAINT `fk_Tenant_Person` FOREIGN KEY (`Person_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_Tenant_PropertyInformation` FOREIGN KEY (`PropertyInformation_idPropertyInformation`) REFERENCES `PropertyInformation` (`idPropertyInformation`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tenant`
--

LOCK TABLES `Tenant` WRITE;
/*!40000 ALTER TABLE `Tenant` DISABLE KEYS */;
/*!40000 ALTER TABLE `Tenant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `EmailAddress` varchar(100) NOT NULL,
  `UserPassword` varchar(100) NOT NULL,
  `Enabled` tinyint(4) DEFAULT '0',
  `FirstName` varchar(45) NOT NULL,
  `LastName` varchar(45) DEFAULT NULL,
  `MiddleName` varchar(45) DEFAULT NULL,
  `DOB` varchar(45) DEFAULT NULL,
  `Gender_idGender` int(11) DEFAULT NULL,
  `VerificationToken` varchar(100) NOT NULL,
  PRIMARY KEY (`EmailAddress`),
  KEY `fk_User_Gender` (`Gender_idGender`),
  CONSTRAINT `fk_User_Gender` FOREIGN KEY (`Gender_idGender`) REFERENCES `Gender` (`idGender`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-16 19:48:27
