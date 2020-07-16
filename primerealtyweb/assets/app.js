'use strict';

// declare modules
angular.module('Env', []);
angular.module('Signup', []);
angular.module('Home', []);

angular.module('PrimeRealty', [
    'Env',
    'Signup',
    'Home',
    'ngRoute',
    'ngCookies'
])

.config(['$routeProvider', function ($routeProvider) {
    
    $routeProvider
	.when('/', {
            controller: 'IndexController',
            templateUrl: 'modules/signup/views/index.html'
        })	 
    .when('/signup', {
            controller: 'SignupController',
            templateUrl: 'modules/signup/views/signup.html'
        })    
	.when('/signin', {
            controller: 'SigninController',
            templateUrl: 'modules/signup/views/signin.html'
        })
	.when('/home', {
            controller: 'HomeController',
            templateUrl: 'modules/home/views/home.html'
        })    
        .otherwise({ redirectTo: '/' });
}]);
