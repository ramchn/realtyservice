'use strict';

angular.module('Signup')

.controller('SignupController',
    ['$scope', '$location', 'SignupService', 'AuthService', 'ENV',  
    function ($scope, $location, SignupService, AuthService, ENV) {         
         
        SignupService.getPersonTypes(function (personTypes) {
            // trim tenant from person type list
            for(var i=0;i<personTypes.length;i++){                
               if(personTypes[i].PersonType == "Tenant") {
                   personTypes.splice(i, 1);
               }
            }
            $scope.personTypes = personTypes;            
        });
        
        SignupService.getGenders(function (genders) {
            $scope.genders = genders;
        }); 
        
        $scope.submitForm = function() {

            $('#signupsubmit').attr('disabled', true);
            $("#spinner").show();
                        
            var formData = {              
              emailAddress:$scope.EmailAddress, userPassword:$scope.UserPassword, 
              firstName:$scope.FirstName, middleName:$scope.MiddleName,       lastName:$scope.LastName, 
              genderId:angular.isDefined($scope.Gender) ? $scope.Gender.idGender : null, 
              personTypeId:$scope.selectedPersonType.idPersonType
            }

            SignupService.signUp(formData, function (response) {      
                $scope.result = response;
            
                $('#signupsubmit').attr('disabled', false);
                $("#spinner").hide();
            });

        }         
    }])

.controller('UserVerificationController',
    ['$scope', '$location', 'SignupService', 
    function ($scope, $location, SignupService) {
        
        var qs = $location.search();
           
        var formData = {              
          token: qs.token             
        }

        SignupService.userVerification(formData, function (response) {
             $scope.result = response;
        }); 
        
    }])

.controller('SigninController',
    ['$scope', '$location', 'SignupService', 'AuthService',
    function ($scope, $location, SignupService, AuthService) {  
        
        // reset login status
        AuthService.ClearCredentials();
                
        $scope.login = function () {      
            
            $scope.dataLoading = true;
            $("#spinner").show();
            
            var formData = {  
                emailAddress:$scope.username, userPassword:$scope.password
            }
                        
            SignupService.signIn(formData, function (auth) {
                $("#spinner").hide();
                if (auth.Authenticated) {                    

                    AuthService.SetCredentials($scope.username, $scope.password, auth.idPerson, auth.Authority, auth.FirstName, auth.LastName);
                    $location.path('/home');
                } else {
                    $scope.error = "either email not verified or invalid credentials";
                    $scope.dataLoading = false;
                }    
            });                                
        }
    }])

.controller('ForgetPasswordController',
    ['$scope', 'SignupService',
    function ($scope, SignupService) {  
                
        $scope.getpassword = function () {     
            $('#fpwdsubmit').attr('disabled', true);
            $("#spinner").show();
            
            var formData = {  
                emailAddress:$scope.username
            }
            
            SignupService.retrievePassword(formData, function (response) {                
                $scope.result = response; 
                
                $('#fpwdsubmit').attr('disabled', false);
                $("#spinner").hide();                     
            });                                
        }
    }]);
