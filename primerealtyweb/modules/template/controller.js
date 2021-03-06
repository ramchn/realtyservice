'use strict';

angular.module('Template')

.run(['$rootScope', '$location', '$cookies', '$http',
    function ($rootScope, $location, $cookies, $http) {
        // keep user logged in after page refresh
        $rootScope.globals = $cookies.getObject('globals') || {};
        
        if ($rootScope.globals.currentUser) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
        }
    }])

.controller('MenuController',
    ['$scope', '$rootScope', '$location', 
    function ($scope, $rootScope, $location) {        
        $scope.authority = $rootScope.globals.currentUser.authority;
        
        if($scope.authority == 'ROLE_SERVICEPROVIDER' ||
            $scope.authority == 'ROLE_TENANT') {
            $("#mpm").css("display","none");
        }
        
        if($location.url() == '/home') {
            $("#mhome").addClass("active");
        } else if($location.url() == '/viewpi' ||
                 $location.url() == '/createpi' ||
                 $location.url() == '/editpi' ||
                 $location.url() == '/addtenant' ||
                 $location.url() == '/edittenant') {
            $("#mpm").addClass("active");          
        } else if($location.url() == '/createit' ||
                 $location.url() == '/viewit') {
            $("#mit").addClass("active");          
        } else if($location.url() == '/myinfo' ||
                 $location.url() == '/editname' ||
                 $location.url() == '/editphone' ||
                 $location.url() == '/editemail' ||
                 $location.url() == '/editaddress') {
            $("#mmyinfo").addClass("active");          
        }
        
    }]);