'use strict';

angular.module('IssueRequest')

.factory('IssueRequestService',  
    ['$http', 'ENV', 'RestService', 
    function ($http, ENV, RestService) {
        var service = {};
        
        service.getIssueLogs = function (formData, callback) {
            RestService.httpJsonPost(ENV.ISSUEREQUEST_API_URL + '/issuelogs', formData, callback); 
        }
        
        service.createIssueRequestLogMP = function (formData, callback) {            
           
            $http({
                method: 'POST',
                url: ENV.ISSUEREQUEST_API_URL + '/createissuerequestlogmp',
                data: formData,
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
            .then(function (response) {
                callback(response.data);
            }), (function (response) {
                callback(response);
            });            
        }   
        
        service.createIssueRequestLog = function (formData, callback) {            
            RestService.httpJsonPost(ENV.ISSUEREQUEST_API_URL + '/createissuerequestlog', formData, callback);            
        } 
        
        service.assignedPersonForIssue = function (formData, callback) {            
            RestService.httpJsonPost(ENV.ISSUEREQUEST_API_URL + '/assignedpersonforissue', formData, callback);            
        } 
        
        service.getServiceProviders = function (formData, callback) {            
            RestService.httpGet(ENV.ISSUEREQUEST_API_URL + '/getserviceproviders', formData, callback);            
        } 
        
        service.assignPersonForIssue = function (formData, callback) {            
            RestService.httpJsonPost(ENV.ISSUEREQUEST_API_URL + '/assignpersonforissue', formData, callback);            
        } 
        
        service.getPropertyInformations = function (formData, callback) {
            RestService.httpJsonPost(ENV.ISSUEREQUEST_API_URL + '/propertyinfosbyids', formData, callback); 
        }
        
        service.getIssueStatuses = function (callback) {
            RestService.httpGet(ENV.ISSUEREQUEST_API_URL + '/issuestatuses', callback);               
        }
                
        service.updateIssueStatus = function (formData, callback) {            
            RestService.httpJsonPost(ENV.ISSUEREQUEST_API_URL + '/updateissuestatus', formData, callback);            
        } 
        service.getIssues = function (formData, callback) {
            RestService.httpJsonPost(ENV.ISSUEREQUEST_API_URL + '/issuesbyperson', formData, callback); 
        }
        
        service.getIssueCategories = function (callback) {
            RestService.httpGet(ENV.ISSUEREQUEST_API_URL + '/issuecategories', callback);               
        }
                 
        service.createIssueRequestMP = function (formData, callback) {            
           
            $http({
                method: 'POST',
                url: ENV.ISSUEREQUEST_API_URL + '/createissuerequestmp',
                data: formData,
                transformRequest: angular.identity,
                headers: {'Content-Type': undefined}
            })
            .then(function (response) {
                callback(response.data);
            }), (function (response) {
                callback(response);
            });            
        }   
        
        service.createIssueRequest = function (formData, callback) {            
            RestService.httpJsonPost(ENV.ISSUEREQUEST_API_URL + '/createissuerequest', formData, callback);            
        } 
                        
        return service;
        
    }]);
