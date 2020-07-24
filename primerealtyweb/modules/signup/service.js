'use strict';

angular.module('Signup')

.factory('RestService',  
    ['$http', 'ENV', 
    function ($http, ENV) {
        var service = {};
        
        service.getPersonTypes = function (callback) {
            service.httpGet(ENV.SIGNUP_API_URL + '/persontypes', callback);               
        }
        
        service.getGenders = function (callback) {
            service.httpGet(ENV.SIGNUP_API_URL + '/genders', callback);                   
        }
        
        service.signUp = function (formData, callback) {
            service.httpPost(ENV.SIGNUP_API_URL + '/signup', formData, callback);
        }
        
        service.signupbyPropertyInfo = function (formData, callback) {
            service.httpPost(ENV.SIGNUP_API_URL + '/signupbypropertyinfo', formData, callback);       
        }
        
        service.userVerification = function (formData, callback) {
            service.httpPost(ENV.SIGNUP_API_URL + '/signup/userverification', formData, callback);   
        }
        
        service.signIn = function (formData, callback) {            
            service.httpPost(ENV.SIGNUP_API_URL + '/signin', formData, callback);              
        }   
        
        service.retrievePassword = function (formData, callback) {            
            service.httpPost(ENV.SIGNUP_API_URL + '/signin/retrievepassword', formData, callback);          
        }         
        
        service.httpGet = function(url, callback) {
            $http.get(url)
            .then(function (response) {
                callback(response.data);
            }, function (response) {
                callback(response);
            });   
        }
        
        service.httpPost = function(url, formData, callback) {
            $http({
                method: 'POST',
                url: url,
                params: formData,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}                
            })
            .then(function (response) {
                callback(response.data);
            }, function (response) {
                callback(response);
            });  
        }
        
        return service;
    }])

.factory('AuthService',
    ['Base64', '$http', '$cookies', '$rootScope', 
    function (Base64, $http, $cookies, $rootScope) {
        var service = {};

        service.SetCredentials = function (username, password, personid, authority, firstname, lastname) {
            var authdata = Base64.encode(username + ':' + password);

            $rootScope.globals = {
                currentUser: {
                    username: username,
                    authdata: authdata,
                    personid: personid,
                    authority: authority,
                    firstname: firstname,
                    lastname: lastname
                }
            }

            $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata; // jshint ignore:line
            $cookies.putObject('globals', $rootScope.globals);
        }

        service.ClearCredentials = function () {
            $rootScope.globals = {};
            $cookies.remove('globals');
            $http.defaults.headers.common.Authorization = 'Basic ';
        }
        
        service.encodeBase64 = function (input) {
            return Base64.encode(input);
        }
        
        service.decodeBase64 = function (input) {
            return Base64.decode(input);
        }

        return service;
    }])

.factory('Base64', function () {
    /* jshint ignore:start */

    var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';

    return {
        encode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            do {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);

                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;

                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }

                output = output +
                    keyStr.charAt(enc1) +
                    keyStr.charAt(enc2) +
                    keyStr.charAt(enc3) +
                    keyStr.charAt(enc4);
                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";
            } while (i < input.length);

            return output;
        },

        decode: function (input) {
            var output = "";
            var chr1, chr2, chr3 = "";
            var enc1, enc2, enc3, enc4 = "";
            var i = 0;

            // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
            var base64test = /[^A-Za-z0-9\+\/\=]/g;
            if (base64test.exec(input)) {
                window.alert("There were invalid base64 characters in the input text.\n" +
                    "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                    "Expect errors in decoding.");
            }
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

            do {
                enc1 = keyStr.indexOf(input.charAt(i++));
                enc2 = keyStr.indexOf(input.charAt(i++));
                enc3 = keyStr.indexOf(input.charAt(i++));
                enc4 = keyStr.indexOf(input.charAt(i++));

                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;

                output = output + String.fromCharCode(chr1);

                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }

                chr1 = chr2 = chr3 = "";
                enc1 = enc2 = enc3 = enc4 = "";

            } while (i < input.length);

            return output;
        }
    };

    /* jshint ignore:end */
});