'use strict';

angular.module('Myinfo')

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

.controller('MyinfoController',
    ['$scope', '$rootScope', 
    function ($scope, $rootScope) {        
        $scope.authority = $rootScope.globals.currentUser.authority;
        if($scope.authority == 'ROLE_COMPANYOWNER') {
            $("#companyowner").css("display","block");
        } else {
            $("#companyowner").css("display","none");
        }
    }]);
