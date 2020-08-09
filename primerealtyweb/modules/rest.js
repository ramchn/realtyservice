'use strict';

angular.module('Rest')

.factory('RestService',  
    ['$http', 'ENV', 
    function ($http, ENV) {
        var service = {};
        
        service.httpGet = function(url, callback) {
            $http.get(url)
            .then(function (response) {
                callback(response.data);
            }, function (response) {
                callback(response);
            });   
        }
        
        service.httpJsonPost = function(url, formData, callback) {
            $http.post(url, formData)
            .then(function (response) {
                callback(response.data);
            }, function (response) {
                callback(response);
            });  
        }
                
        return service;
        
    }]);
