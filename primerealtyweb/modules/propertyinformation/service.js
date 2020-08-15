'use strict';

angular.module('PropertyInformation')

.factory('PropertyInformationService',  
    ['$http', 'ENV', 'RestService',  
    function ($http, ENV, RestService) {
        var service = {};
        
        service.getPropertyTypes = function (callback) {
            RestService.httpGet(ENV.PROPERTYINFORMATION_API_URL + '/propertytypes', callback);      
        }
        
        service.getCountries = function (callback) {
            RestService.httpGet(ENV.PROPERTYINFORMATION_API_URL + '/countries', callback);      
        }
                
        service.getStateCodesByCountry = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/statecodesbycountry', formData, callback);              
        }
        
        service.createPropertyInformation = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/createpropertyinformation', formData, callback);              
        }
        
        service.editPropertyInformation = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/editpropertyinformation', formData, callback);              
        }
        
        service.isPropInfoPerson = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/propinfoperson', formData, callback);              
        }
        
        service.deletePropertyInformation = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/deletepropertyinformation', formData, callback);              
        }
        
        service.getPropertyInformation = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/propertyinformation', formData, callback);              
        }
        
        service.viewPropertyInformationsByPerson = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/propertyinformationsbyperson', formData, callback);              
        }
        
        service.signupbyPropertyInfo = function (formData, callback) {
            RestService.httpJsonPost(ENV.SIGNUP_API_URL + '/signupbypropertyinfo', formData, callback);       
        }
        
        service.getLiveTenants = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/livetenants', formData, callback);              
        }
        
        service.getAssignedTenants = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/assignedtenants', formData, callback);              
        }
        
        service.updateTenantForProperty = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/updatetenantforproperty', formData, callback);              
        }
        
        service.getLivePropMngrs = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/livepropmngrs', formData, callback);              
        }
        
        service.getAssignedPropMngr = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/assignedpropmngr', formData, callback);              
        }
        
        service.updatePropMngrForProperty = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/updatepropmngrforproperty', formData, callback);              
        }
        
        service.getLiveOwners = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/liveowners', formData, callback);              
        }
        
        service.getAssignedOwner = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/assignedowner', formData, callback);              
        }
                
        service.updateOwnerForProperty = function (formData, callback) {            
            RestService.httpJsonPost(ENV.PROPERTYINFORMATION_API_URL + '/updateownerforproperty', formData, callback);              
        }
                
        return service;
}]);
