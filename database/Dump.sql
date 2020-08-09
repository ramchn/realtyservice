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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;
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
  `OwnerPerson_idPerson` int(11) NOT NULL,
  `CreatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`idPropertyInformation`),
  KEY `PropertyType_idPropertyType` (`PropertyType_idPropertyType`),
  KEY `Address_idAddress` (`Address_idAddress`),
  KEY `CreatePerson_idPerson` (`OwnerPerson_idPerson`),
  CONSTRAINT `fk_PropertyInformation_Address` FOREIGN KEY (`Address_idAddress`) REFERENCES `Address` (`idAddress`),
  CONSTRAINT `fk_PropertyInformation_Person` FOREIGN KEY (`OwnerPerson_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_PropertyInformation_PropertyType` FOREIGN KEY (`PropertyType_idPropertyType`) REFERENCES `PropertyType` (`idPropertyType`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
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
  PRIMARY KEY (`idPropertyInformationPerson`),
  UNIQUE KEY `cn_PropertyInformationPerson_Unique` (`Person_idPerson`,`PropertyInformation_idPropertyInformation`),
  KEY `fk_PIP_PropertyInformation` (`PropertyInformation_idPropertyInformation`),
  CONSTRAINT `fk_PIP_Person` FOREIGN KEY (`Person_idPerson`) REFERENCES `Person` (`idPerson`),
  CONSTRAINT `fk_PIP_PropertyInformation` FOREIGN KEY (`PropertyInformation_idPropertyInformation`) REFERENCES `PropertyInformation` (`idPropertyInformation`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

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

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-09 16:47:02
