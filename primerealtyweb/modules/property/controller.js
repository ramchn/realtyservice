'use strict';

angular.module('Property')

.run(['$rootScope', '$location', '$cookies', '$http',
    function ($rootScope, $location, $cookies, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookies.getObject('globals') || {};
        
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }

        $rootScope.$on('$locationChangeStart', function (event, next, current) {
            // redirect to login page if not logged in
            if ($location.path() == '/home' && !$rootScope.globals.currentUser) {
                $location.path('/signin');
            }
        });
    }])

.controller('PropertyController',
    ['$scope', '$rootScope', 'AuthService',
    function ($scope, $rootScope, AuthService) {        
         
        $scope.authority = $rootScope.globals.currentUser.authority;
        
        $scope.signupbypropertyinfolink = "http://localhost/~ramamoorthy/primerealtyweb/index.html#/signupbypropertyinfo?" + AuthService.encodeBase64("pi=1&pt=2");
        
        if($scope.authority == 'ROLE_OWNER' || $scope.authority == 'ROLE_COMPANYOWNER') {            
            $("#ownertable").css("display","none");
            $("#pmtable").css("display","inline");
            $("#tenanttable").css("display","inline");
            
        } else if($scope.authority == 'ROLE_PROPERTYMANAGER') {
            $("#pmtable").css("display","none");
            $("#ownertable").css("display","inline");            
            $("#tenanttable").css("display","inline");
            
        } else if($scope.authority == 'ROLE_OWNER/PROPERTYMANAGER') {
            $("#pmtable").css("display","none");
            $("#ownertable").css("display","none");            
            $("#tenanttable").css("display","inline");
            
        } 
        
        $scope.idPropertyInformation = 1; // should be dynamically fetched from backend
        $scope.PersonTypeId = 2; // should be dynamically fetched from backend

        $scope.submitForm = function() {
            
            $('#signupsubmit').attr('disabled', true);
            $("#spinner").show();
            
            var formData = {              
              PersonTypeId:$scope.PersonTypeId, EmailAddress:$scope.EmailAddress, UserPassword:AuthService.encodeBase64("password"),
              idPropertyInformation: $scope.idPropertyInformation,
              EmailSubject:"Default Password and Email Verification", 
              EmailBody:"Default password: password<br><br>Email Verification Link: <a href='" + ENV.PRIMEREALTY_WEB_URL + "/primerealtyweb/index.html#/userverification?token=<VerificationToken>'>Click</a> here to verify your email address."    
            }

            RestService.signupbyPropertyInfo(formData, function (response) { 
                $scope.result = response;

                $('#signupsubmit').attr('disabled', false);
                $("#spinner").hide(); 
                
            });              
        } 
    }]);
