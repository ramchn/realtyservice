'use strict';

angular.module('IssueTracking')

.run(['$rootScope', '$location', '$cookieStore', '$http',
    function ($rootScope, $location, $cookieStore, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookieStore.get('globals') || {};
        
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
    }]);
