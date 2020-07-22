'use strict';

angular.module('Property')

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

.controller('PropertyController',
    ['$scope', '$rootScope', 
    function ($scope, $rootScope) {        
         
        $scope.authority = $rootScope.globals.currentUser.authority;
        
        $scope.signupbypropertyinfolink = "http://localhost/~ramamoorthy/primerealtyweb/index.html#/signupbypropertyinfo?pi=1&pt=2";
        
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
    }]);
