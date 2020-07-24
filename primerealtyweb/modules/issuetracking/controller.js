'use strict';

angular.module('IssueTracking')

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

.controller('IssueTrackingController',
    ['$scope', '$rootScope', 
    function ($scope, $rootScope) {        
        $scope.authority = $rootScope.globals.currentUser.authority;
        if($scope.authority == 'ROLE_TENANT') {            
            $("#createissue").css("display","block");
            
        } else {
            $("#createissue").css("display","none");
        }
        
        if($scope.authority == 'ROLE_PROPERTYMANAGER') {
            $scope.pm = "Me";
            $scope.owner = "Joe";
            
        } else if($scope.authority == 'ROLE_OWNER') {
            $scope.pm = "Mark";
            $scope.owner = "Me";
            
        } else {
            $scope.pm = "Mark";
            $scope.owner = "Joe";
        }
        
    }]);
