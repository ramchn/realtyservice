'use strict';

angular.module('Signup')

.controller('SignupController',
    ['$scope', '$location', 'RestService', 'AuthService', 'ENV',  
    function ($scope, $location, RestService, AuthService, ENV) {         
            
        RestService.getPersonTypes(function (personTypes) {
            // trim tenant from person type list
            for(var i=0;i<personTypes.length;i++){                
               if(personTypes[i].PersonType == "Tenant") {
                   personTypes.splice(i, 1);
               }
            }
            $scope.personTypes = personTypes;            
        });
        
        RestService.getGenders(function (genders) {
            $scope.genders = genders;
        }); 
    
        $scope.submitForm = function() {
            
            $('#signupsubmit').attr('disabled', true);
            $("#spinner").show();
            
            var formData = {              
              PersonTypeId:$scope.selectedPersonType.idPersonType, EmailAddress:$scope.EmailAddress, UserPassword:AuthService.encodeBase64($scope.UserPassword), 
              FirstName:$scope.FirstName, MiddleName:$scope.MiddleName,       LastName:$scope.LastName, 
              GenderId:angular.isDefined($scope.Gender) ? $scope.Gender.idGender : null,
              EmailSubject:"Email Verification", 
              EmailBody:"Email Verification Link: <a href='" + ENV.PRIMEREALTY_WEB_URL + "/primerealtyweb/index.html#/userverification?token=<VerificationToken>'>Click</a> here to verify your email address."    
            }

            RestService.signUp(formData, function (response) {      
                $scope.result = response;
            
                $('#signupsubmit').attr('disabled', false);
                $("#spinner").hide();
            });
        }         
    }])

.controller('UserVerificationController',
    ['$scope', '$location', 'RestService', 
    function ($scope, $location, RestService) {
        
        var qs = $location.search();
           
        var formData = {              
          token: qs.token             
        }

        RestService.userVerification(formData, function (response) {
             $scope.result = response;
        }); 
        
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
    ['$scope', 'RestService',
    function ($scope, RestService) {  
                
        $scope.getpassword = function () {     
            $('#fpwdsubmit').attr('disabled', true);
            $("#spinner").show();
            
            var formData = {  
                EmailAddress:$scope.username,
                EmailSubject:"Password for login",
                EmailBody: "Password is: <UserPassword>"
            }
            
            RestService.retrievePassword(formData, function (response) {                
                $scope.result = response; 
                
                $('#fpwdsubmit').attr('disabled', false);
                $("#spinner").hide();                     
            });                                
        }
    }]);
