'use strict';

angular.module('Signup')

.controller('IndexController',
    ['$scope',
    function ($scope) {
        
    }])

.controller('SignupController',
    ['$scope', '$location', 'RestService', 'AuthService',  
    function ($scope, $location, RestService, AuthService) {         
    
        RestService.getPersonTypes(function (personTypes) {
            $scope.personTypes = personTypes;            
        });
        
        RestService.getGenders(function (genders) {
            $scope.genders = genders;
        });
                
        $scope.submitForm = function() {
            $("#spinner").show();
            var formData = {              
              PersonTypeId:$scope.selectedPersonType.idPersonType, EmailAddress:$scope.EmailAddress, UserPassword:AuthService.encodeBase64($scope.UserPassword), 
              FirstName:$scope.FirstName, MiddleName:$scope.MiddleName,
              LastName:$scope.LastName, DOB:$scope.DOB, 
              GenderId:angular.isDefined($scope.Gender) ? $scope.Gender.idGender : null             
            }

            RestService.signUp(formData, function (response) {
                $("#spinner").hide();
                $scope.result = response;
            });              
        }            
    }])

.controller('SigninController',
    ['$scope', '$location', 'RestService', 'AuthService',
    function ($scope, $location, RestService, AuthService) {  
        
        // reset login status
        AuthService.ClearCredentials();
        
        $scope.login = function () {            
            $scope.dataLoading = true;
            $("#spinner").show();
            var formData = {  
                EmailAddress:$scope.username, UserPassword:AuthService.encodeBase64($scope.password)
            }
            
            RestService.signIn(formData, function (auth) {
                $("#spinner").hide();
                if (auth.Authenticated) {
                    AuthService.SetCredentials($scope.username, $scope.password);
                    $location.path('/home');
                } else {
                    $scope.error = "either email not verified or invalid credentials";
                    $scope.dataLoading = false;
                }    
            });                                
        }
    }]);
