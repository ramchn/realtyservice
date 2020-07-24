'use strict';

angular.module('Home')

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

.controller('HomeController',
    ['$scope', '$rootScope', 
    function ($scope, $rootScope) {        
        $scope.authority = $rootScope.globals.currentUser.authority;
        $scope.firstname = $rootScope.globals.currentUser.firstname;
        $scope.lastname = $rootScope.globals.currentUser.lastname;
        
        if($scope.authority == 'ROLE_SERVICEPROVIDER') {
            $("#spdiv").css("display","block");
            $("#opmdiv").css("display","none");
            $("#tndiv").css("display","none");            
            $("#cihref").css("display","none");
            
        } else if($scope.authority == 'ROLE_TENANT') {
            $("#tndiv").css("display","block");
            $("#opmdiv").css("display","none");            
            $("#spdiv").css("display","none");
            $("#cihref").css("display","block");
            
        } else {
            $("#opmdiv").css("display","block");            
            $("#spdiv").css("display","none");
            $("#tndiv").css("display","none");
            $("#cihref").css("display","none");
        }
    }]);