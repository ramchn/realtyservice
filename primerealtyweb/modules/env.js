'use strict';

angular.module('Env')

.constant('ENV', {
    // API url
    SIGNUP_API_URL: 'http://localhost:8071',
    ISSUEREQUEST_API_URL: 'http://localhost:8075',
    PROPERTYINFORMATION_API_URL: 'http://localhost:8074',
    DEFAULT_COUNTRY_ID:1
});
