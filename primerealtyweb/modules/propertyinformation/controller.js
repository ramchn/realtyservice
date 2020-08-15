'use strict';

angular.module('PropertyInformation')

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

.controller('CreatePropertyInformationController',
    ['$scope', '$rootScope', 'PropertyInformationService', 'ENV', 
    function ($scope, $rootScope, PropertyInformationService, ENV) {       
        
        $scope.PersonId = $rootScope.globals.currentUser.personid;
                 
        PropertyInformationService.getPropertyTypes(function (propertyTypes) {
            $scope.propertyTypes = propertyTypes;            
        });
                
        PropertyInformationService.getCountries(function (countries) {
            for(var i=0; i < countries.length; i++) {
                var countryname = countries[i].CountryName;
                if(countryname.length > 20) {
                    countries[i].CountryName = countryname.substr(0,20) + "...";
                } 
            }
            $scope.countries = countries;            
        });
        
        var formData1 = {              
          'countryId': ENV.DEFAULT_COUNTRY_ID    
        }
        PropertyInformationService.getStateCodesByCountry(formData1, function (stateCodes) {
            $scope.stateCodes = stateCodes;            
        });
        
        $scope.savePropInfo = function() {
            
            $('#propinfosubmit').attr('disabled', true);
            
            var formData = {            'address1':$scope.Address1,'address2':$scope.Address2,'address3':$scope.Address3,'zip':$scope.Zip,'cityName':$scope.CityName,'stateId':$scope.selectedState.idState,'countryId':$scope.selectedCountry.idCountry,'squareFeet':$scope.SquareFeet,'beds':$scope.Beds,'baths':$scope.Baths,'stories':$scope.Stories,'lotSize':$scope.Lotsize,'yearBuilt':$scope.YearBuilt,'yearRenovated':$scope.YearRenovated,'apn':$scope.APN,'community':$scope.Community,'hoaDues':$scope.HOADues,'propertyTypeId':$scope.selectedPropertyType.idPropertyType,'personId':$scope.PersonId
            }    

            PropertyInformationService.createPropertyInformation(formData, function (response) { 
                $scope.result = response;

                $('#propinfosubmit').attr('disabled', false);
                
            });              
        } 
    }])

.controller('EditPropertyInformationController',
    ['$scope', '$rootScope', '$location', 'PropertyInformationService', 'ENV', 
    function ($scope, $rootScope, $location, PropertyInformationService, ENV) {        
         
        $scope.PersonId = $rootScope.globals.currentUser.personid;
        
        $scope.DeleteDisabled = false;
        
        var qp = ''+0;
        var url = $location.absUrl();    
        var qsexist = url.lastIndexOf('?');
        if(qsexist != -1) {
            qp = url.substr(qsexist+1);            
        }
        
        $scope.PropertyInformationId = qp;
        
        var formData = {      
          'personId': $scope.PersonId    
        }
        PropertyInformationService.isPropInfoPerson(formData, function (response) {
            $scope.DeleteDisabled = response.IsPropInfoPerson;
        });
               
        PropertyInformationService.getPropertyTypes(function (propertyTypes) {
            $scope.propertyTypes = propertyTypes;            
        });
        
        PropertyInformationService.getCountries(function (countries) {
            for(var i=0; i < countries.length; i++) {
                var countryname = countries[i].CountryName;
                if(countryname.length > 20) {
                    countries[i].CountryName = countryname.substr(0,20) + "...";
                } 
            }
            $scope.countries = countries;            
        });
        
        var formData1 = {              
          'countryId': ENV.DEFAULT_COUNTRY_ID    
        }
        PropertyInformationService.getStateCodesByCountry(formData1, function (stateCodes) {
            $scope.stateCodes = stateCodes;            
        });
        
        var formData2 = {              
          'propertyInformationId': $scope.PropertyInformationId    
        }
        PropertyInformationService.getPropertyInformation(formData2, function (propertyInformation) {
            
            propertyInformation.propertytypedata = {
                        availableOptions: $scope.propertyTypes,
                        selectedOption: {idPropertyType: propertyInformation.idPropertyType, PropertyType: propertyInformation.PropertyType}
            }
            propertyInformation.countriesdata = {
                        availableOptions: $scope.countries,
                        selectedOption: {idCountry: propertyInformation.idCountry, CountryName: propertyInformation.CountryName}
            }
            propertyInformation.statecodesdata = {
                        availableOptions: $scope.stateCodes,
                        selectedOption: {idState: propertyInformation.idState, StateCode: propertyInformation.StateCode}
            }
            $scope.propertyInformation = propertyInformation;
            $scope.AddressId = propertyInformation.idAddress;
        });
                       
        $scope.updatePropInfo = function(propertyInformation) {
            $('#editpropinfosubmit').attr('disabled', true);
            
            var formData = {            'address1':propertyInformation.Address1,'address2':propertyInformation.Address2,'address3':propertyInformation.Address3,'zip':propertyInformation.Zip,'cityName':propertyInformation.CityName,'stateId':propertyInformation.statecodesdata.selectedOption.idState,'countryId':propertyInformation.countriesdata.selectedOption.idCountry,'addressId':propertyInformation.idAddress,'squareFeet':propertyInformation.SquareFeet,'beds':propertyInformation.Beds,'baths':propertyInformation.Baths,'stories':propertyInformation.Stories,'lotSize':propertyInformation.Lotsize,'yearBuilt':propertyInformation.YearBuilt,'yearRenovated':propertyInformation.YearRenovated,'apn':propertyInformation.APN,'community':propertyInformation.Community,'hoaDues':propertyInformation.HOADues,'propertyTypeId':propertyInformation.propertytypedata.selectedOption.idPropertyType,'personId':$scope.PersonId,'propertyInformationId':$scope.PropertyInformationId
            }            
            PropertyInformationService.editPropertyInformation(formData, function (response) { 
                $scope.result = response;

                $('#editpropinfosubmit').attr('disabled', false);            
            });
        }
        
        $scope.detailPropInfo = function() {
            $location.path('/detailpropinfo');
        }        
        
        $scope.deletePropInfo = function() {            
            document.getElementById('id01').style.display='block';
        }
        
        $scope.deletePropInfoConfirm = function() {
            document.getElementById('id01').style.display='none';
            var formData = {           'deletedReason':$scope.DeletedReason,'personId':$scope.PersonId,'propertyInformationId':$scope.PropertyInformationId
            }              
            PropertyInformationService.deletePropertyInformation(formData, function (response) { 
                $scope.result = response; 
                 $location.path('/viewpropinfo');
            });
        }        
    }])

.controller('DetailPropertyInformationController',
    ['$scope', '$rootScope', '$location', 'PropertyInformationService',
    function ($scope, $rootScope, $location, PropertyInformationService) {        
         
        $scope.PersonId = $rootScope.globals.currentUser.personid;
        
        var qp = ''+0;
        var url = $location.absUrl();    
        var qsexist = url.lastIndexOf('?');
        if(qsexist != -1) {
            qp = url.substr(qsexist+1);            
        }
        
        $scope.PropertyInformationId = qp;
        var formData = {              
          'propertyInformationId': $scope.PropertyInformationId    
        }
        PropertyInformationService.getPropertyInformation(formData, function (propertyInformation) {
            $scope.propertyInformation = propertyInformation;            
        });
        
        $scope.editPropInfo = function() {
            $location.path('/editpropinfo');
        }
         
    }])

.controller('ViewPropertyInformationController',
    ['$scope', '$rootScope', 'PropertyInformationService',  
    function ($scope, $rootScope, PropertyInformationService) {    
        
        $scope.PersonId = $rootScope.globals.currentUser.personid;
        $scope.Authority = $rootScope.globals.currentUser.authority;
        
        $scope.OwnerOrCompanyOwner = false;
        $scope.PropertyManager = false;
        
        $scope.CreateDisabled = false;
        $scope.TenantReadOnly = false;
        
        var formData = {      
          'personId': $scope.PersonId    
        }
        PropertyInformationService.isPropInfoPerson(formData, function (response) {
            $scope.CreateDisabled = response.IsPropInfoPerson;
            
            //why would owner manage tenant, let property manager does
            if(response.IsPropInfoPerson && ($scope.Authority == 'ROLE_OWNER' || $scope.Authority == 'ROLE_COMPANYOWNER')) {
               $scope.TenantReadOnly = true;
            } else if(!response.IsPropInfoPerson && ($scope.Authority == 'ROLE_OWNER' || $scope.Authority == 'ROLE_COMPANYOWNER')) {
                $scope.TenantReadOnly = true;
                $scope.OwnerOrCompanyOwner = true;
            } else if(!response.IsPropInfoPerson && $scope.Authority == 'ROLE_PROPERTYMANAGER') {
                $scope.PropertyManager = true;
            }
               
        });
                
        var formData = {              
          'personId': $scope.PersonId
        }        
        PropertyInformationService.viewPropertyInformationsByPerson(formData, function (propertyInformations) {       
            for(var i=0; i < propertyInformations.length; i++) {                    if(angular.isDefined(propertyInformations[i].PropertyManager)) {
                    propertyInformations[i].PropertyManagerExist = true;
                }
                if(angular.isDefined(propertyInformations[i].Owner)) {
                    propertyInformations[i].OwnerExist = true;
                }
            }
            $scope.propertyInformations = propertyInformations;   
        });        
        
    }])

.controller('AssignTenantsController',
    ['$scope', '$rootScope', '$location', 'PropertyInformationService',  
    function ($scope, $rootScope, $location, PropertyInformationService) {        
         
        $scope.PersonId = $rootScope.globals.currentUser.personid;
        
        var qp = ''+0;
        var url = $location.absUrl();    
        var qsexist = url.lastIndexOf('?');
        if(qsexist != -1) {
            qp = url.substr(qsexist+1);  
        }
        
        $scope.PropertyInformationId = qp;
        
        var formData = {              
          'propertyInformationId': $scope.PropertyInformationId
        }

        PropertyInformationService.getPropertyInformation(formData, function (propertyInformation) {
            $scope.propertyInformation = propertyInformation;            
        });
        
        PropertyInformationService.getLiveTenants(formData, function (liveTenants) {
            
            PropertyInformationService.getAssignedTenants(formData, function (assignedTenants) {
                
                for(var i = 0; i < liveTenants.length; i++) {
                    if(liveTenants[i].StartDate != null) {
                        var startDate = liveTenants[i].StartDate;
                        liveTenants[i].StartDate = new Date(startDate);
                    }
                    if(liveTenants[i].EndDate != null) {
                        var endDate = liveTenants[i].EndDate;
                        liveTenants[i].EndDate = new Date(endDate);
                    }
   
                    liveTenants[i].AssignStatus = false; 
                    for(var j=0; j < assignedTenants.length; j++) {
                        if(assignedTenants[j].idPerson == liveTenants[i].idPerson) {
                            liveTenants[i].AssignStatus = true; 
                            break;
                        } 
                    }
                }
                $scope.tenants = liveTenants;
            });       
            
        });
        
        $scope.addTenant = function() {
            
            $('#addtenantsubmit').attr('disabled', true);
            $("#spinner").show();
            
            var formData = {              
              'emailAddress':$scope.EmailAddress, 'personTypeId':2, 'propertyInformationId': $scope.PropertyInformationId, 'updateByPersonId':$scope.PersonId
            }

            PropertyInformationService.signupbyPropertyInfo(formData, function (response) { 
                $scope.result = response;
                
                $scope.refreshTenant();

                $('#addtenantsubmit').attr('disabled', false);
                $("#spinner").hide(); 
                
            });              
        } 
        
        $scope.refreshTenant = function() {
                        
            var formData = {              
              'propertyInformationId': $scope.PropertyInformationId
            }
            
            PropertyInformationService.getLiveTenants(formData, function (liveTenants) {
                    
                PropertyInformationService.getAssignedTenants(formData, function (assignedTenants) {
                
                    for(var i = 0; i < liveTenants.length; i++) {
                        if(liveTenants[i].StartDate != null) {
                            var startDate = liveTenants[i].StartDate;
                            liveTenants[i].StartDate = new Date(startDate);
                        }
                        if(liveTenants[i].EndDate != null) {
                            var endDate = liveTenants[i].EndDate;
                            liveTenants[i].EndDate = new Date(endDate);
                        }

                        liveTenants[i].AssignStatus = false; 
                        for(var j=0; j < assignedTenants.length; j++) {
                            if(assignedTenants[j].idPerson == liveTenants[i].idPerson) {
                                liveTenants[i].AssignStatus = true; 
                                break;
                            } 
                        }
                    }
                    $scope.tenants = liveTenants;
            
                });
                
            });            
        }
     
        $scope.updateTenant = function() {
            
            var valid = true;            
            var atleastoneselected = false;
            for(var i = 0; i < $scope.tenants.length; i++) {
                var chk = document.getElementById('assignCheck'+$scope.tenants[i].idPerson);         if(chk.checked) {
                    atleastoneselected = true;
                }          
            }            
            if(!atleastoneselected) {
               alert('at least 1 person selected');
               valid = false;    
            } else {            
                for(var i = 0; i < $scope.tenants.length; i++) {
                    var chk = document.getElementById('assignCheck'+$scope.tenants[i].idPerson);
                    if(chk.checked && $scope.tenants[i].StartDate == null) {
                        alert('start date must be selected');
                        valid = false;
                    } 
                }
            }
            if(valid) {                   
                for(var i = 0; i < $scope.tenants.length; i++) {
                    var chk = document.getElementById('assignCheck'+$scope.tenants[i].idPerson);
                    if(chk.checked) {                          
                        var formData = {              
                          'propertyInformationId':$scope.PropertyInformationId, 'personId':$scope.tenants[i].idPerson, 
                          'updateByPersonId':$scope.PersonId, 'startDate':$scope.tenants[i].StartDate, 'endDate':$scope.tenants[i].EndDate
                        }
                        PropertyInformationService.updateTenantForProperty(formData, function (response) { 
                            $scope.result = response;
                                
                            $scope.refreshTenant();
                            
                        });                        
                    } 
                }                
            }  
            
        }                  
    }])

.controller('AssignPropertyMngrController',
    ['$scope', '$rootScope', '$location', 'PropertyInformationService',
    function ($scope, $rootScope, $location, PropertyInformationService) {        
         
        $scope.PersonId = $rootScope.globals.currentUser.personid;
        
        /*
        var arr = new Array();
        arr[0] = {'id':32, 'name':'abc'};
        arr[1] = {'id':43, 'name':'xyz'};
        $scope.PIds = arr;
        $scope.myRad = 43;
        */
        
        var qp = ''+0;
        var url = $location.absUrl();    
        var qsexist = url.lastIndexOf('?');
        if(qsexist != -1) {
            qp = url.substr(qsexist+1);              
        }
        $scope.PropertyInformationId = qp;
        
        var formData1 = {              
          'propertyInformationId': $scope.PropertyInformationId
        }        
        PropertyInformationService.getPropertyInformation(formData1, function (propertyInformation) {
            $scope.propertyInformation = propertyInformation;            
        });
        
        var formData = {              
          'propertyInformationId': $scope.PropertyInformationId,
          'personId': $scope.PersonId    
        }        
        PropertyInformationService.getLivePropMngrs(formData, function (livePropMngrs) {
            var formData1 = {              
              'propertyInformationId': $scope.PropertyInformationId
            }            
            PropertyInformationService.getAssignedPropMngr(formData1, function (assignedPropMngr) {
                                
                for(var i = 0; i < livePropMngrs.length; i++) {
                    if(livePropMngrs[i].StartDate != null) {
                        var startDate = livePropMngrs[i].StartDate;
                        livePropMngrs[i].StartDate = new Date(startDate);
                    }
                    if(livePropMngrs[i].EndDate != null) {
                        var endDate = livePropMngrs[i].EndDate;
                        livePropMngrs[i].EndDate = new Date(endDate);
                    }
                    if(assignedPropMngr.idPerson == livePropMngrs[i].idPerson) { 
                        $scope.assignRad = assignedPropMngr.idPerson;
                    }                   
                }
                $scope.propMngrs = livePropMngrs;
            });
            
        });
        
        $scope.addPropMngr = function() {
            
            $('#addpropmngrsubmit').attr('disabled', true);
            $("#spinner").show();
            
            var formData = {              
              'emailAddress':$scope.EmailAddress, 'personTypeId':3, 'propertyInformationId': $scope.PropertyInformationId, 'updateByPersonId':$scope.PersonId
            }

            PropertyInformationService.signupbyPropertyInfo(formData, function (response) { 
                $scope.result = response;
                
                $scope.refreshPropMngr();

                $('#addpropmngrsubmit').attr('disabled', false);
                $("#spinner").hide(); 
                
            });              
        } 
        
        $scope.refreshPropMngr = function() {
            var formData = {              
              'propertyInformationId': $scope.PropertyInformationId,
              'personId': $scope.PersonId      
            }
            PropertyInformationService.getLivePropMngrs(formData, function (livePropMngrs) {
                var formData1 = {              
                  'propertyInformationId': $scope.PropertyInformationId
                }    
                PropertyInformationService.getAssignedPropMngr(formData1, function (assignedPropMngr) {

                    for(var i = 0; i < livePropMngrs.length; i++) {
                        if(livePropMngrs[i].StartDate != null) {
                            var startDate = livePropMngrs[i].StartDate;
                            livePropMngrs[i].StartDate = new Date(startDate);
                        }
                        if(livePropMngrs[i].EndDate != null) {
                            var endDate = livePropMngrs[i].EndDate;
                            livePropMngrs[i].EndDate = new Date(endDate);
                        }

                        if(assignedPropMngr.idPerson == livePropMngrs[i].idPerson) {              
                            $scope.assignRad = assignedPropMngr.idPerson;
                        } 
                    }
                    $scope.propMngrs = livePropMngrs;

                });

            });
        }
        
        $scope.updatePropMngr = function() {
            
            var valid = true;            
            if(angular.isUndefined($scope.assignRad)) {
                   alert('at least 1 person selected');
                
            } else {
                for(var i = 0; i < $scope.propMngrs.length; i++) {
                    if(($scope.propMngrs[i].idPerson == $scope.assignRad) && $scope.propMngrs[i].StartDate == null) {
                        alert('start date must be selected');
                        valid = false;
                    } 
                }
            }
            
            if(valid) {                   
                for(var i = 0; i < $scope.propMngrs.length; i++) {
                    if($scope.propMngrs[i].idPerson == $scope.assignRad) {      
                        
                        var formData = {              
                          'propertyInformationId':$scope.PropertyInformationId, 'personId':$scope.propMngrs[i].idPerson, 
                          'updateByPersonId':$scope.PersonId, 'startDate':$scope.propMngrs[i].StartDate, 'endDate':$scope.propMngrs[i].EndDate
                        }
    
                        PropertyInformationService.updatePropMngrForProperty(formData, function (response) { 
                            $scope.result = response;
                                
                            $scope.refreshPropMngr();
                            
                        });                        
                    } 
                }                 
            }             
        }                 
    }])

.controller('AssignOwnerController',
    ['$scope', '$rootScope', '$location', 'PropertyInformationService', 
    function ($scope, $rootScope, $location, PropertyInformationService) {        
         
        $scope.PersonId = $rootScope.globals.currentUser.personid;
        
        
        var arr = new Array();
        arr[0] = {'id':32, 'name':'abc'};
        //arr[1] = {'id':43, 'name':'xyz'};
        $scope.PIds = arr;
        //$scope.myRad = 43;
        
        $scope.testRad = function() {
            console.log($scope.myRad);
        }
        
        var qp = ''+0;
        var url = $location.absUrl();    
        var qsexist = url.lastIndexOf('?');
        if(qsexist != -1) {
            qp = url.substr(qsexist+1);              
        }
        $scope.PropertyInformationId = qp;
        
        var formData1 = {              
          'propertyInformationId': $scope.PropertyInformationId
        }        
        PropertyInformationService.getPropertyInformation(formData1, function (propertyInformation) {
            $scope.propertyInformation = propertyInformation;            
        });
        
        var formData = {              
          'propertyInformationId': $scope.PropertyInformationId,
          'personId': $scope.PersonId    
        }        
        PropertyInformationService.getLiveOwners(formData, function (liveOwners) {
            var formData1 = {              
              'propertyInformationId': $scope.PropertyInformationId
            }            
            PropertyInformationService.getAssignedOwner(formData1, function (assignedOwner) {
                                
                for(var i = 0; i < liveOwners.length; i++) {
                    if(liveOwners[i].StartDate != null) {
                        var startDate = liveOwners[i].StartDate;
                        liveOwners[i].StartDate = new Date(startDate);
                    }
                    if(liveOwners[i].EndDate != null) {
                        var endDate = liveOwners[i].EndDate;
                        liveOwners[i].EndDate = new Date(endDate);
                    }
                    if(assignedOwner.idPerson == liveOwners[i].idPerson) { 
                        $scope.assignOwnerRad = assignedOwner.idPerson;
                    }                   
                }
                $scope.owners = liveOwners;
            });
            
        });
        
        $scope.addOwner = function() {
            
            $('#addownersubmit').attr('disabled', true);
            $("#spinner").show();
            
            var formData = {              
              'emailAddress':$scope.EmailAddress, 'personTypeId':1, 'propertyInformationId': $scope.PropertyInformationId, 'updateByPersonId':$scope.PersonId
            }

            PropertyInformationService.signupbyPropertyInfo(formData, function (response) { 
                $scope.result = response;
                
                $scope.refreshOwner();

                $('#addownersubmit').attr('disabled', false);
                $("#spinner").hide(); 
                
            });              
        } 
        
        $scope.refreshOwner = function() {
            var formData = {              
              'propertyInformationId': $scope.PropertyInformationId,
              'personId': $scope.PersonId      
            }
            PropertyInformationService.getLiveOwners(formData, function (liveOwners) {
                var formData1 = {              
                  'propertyInformationId': $scope.PropertyInformationId
                }    
                PropertyInformationService.getAssignedOwner(formData1, function (assignedOwner) {

                    for(var i = 0; i < liveOwners.length; i++) {
                        if(liveOwners[i].StartDate != null) {
                            var startDate = liveOwners[i].StartDate;
                            liveOwners[i].StartDate = new Date(startDate);
                        }
                        if(liveOwners[i].EndDate != null) {
                            var endDate = liveOwners[i].EndDate;
                            liveOwners[i].EndDate = new Date(endDate);
                        }

                        if(assignedOwner.idPerson == liveOwners[i].idPerson) {              
                            $scope.assignOwnerRad = assignedOwner.idPerson;
                        } 
                    }
                    $scope.owners = liveOwners;

                });

            });
        }
        
        $scope.updateOwner = function() {
            
            var valid = true;            
            if(angular.isUndefined($scope.assignOwnerRad)) {
                   alert('at least 1 person selected');
                
            } else {
                for(var i = 0; i < $scope.owners.length; i++) {
                    if(($scope.owners[i].idPerson == $scope.assignOwnerRad) && $scope.owners[i].StartDate == null) {
                        alert('start date must be selected');
                        valid = false;
                    } 
                }
            }
            
            if(valid) {                   
                for(var i = 0; i < $scope.owners.length; i++) {
                    if($scope.owners[i].idPerson == $scope.assignOwnerRad) {      
                        
                        var formData = {              
                          'propertyInformationId':$scope.PropertyInformationId, 'personId':$scope.owners[i].idPerson, 
                          'updateByPersonId':$scope.PersonId, 'startDate':$scope.owners[i].StartDate, 'endDate':$scope.owners[i].EndDate
                        }
    
                        PropertyInformationService.updateOwnerForProperty(formData, function (response) { 
                            $scope.result = response;
                                
                            $scope.refreshOwner();
                            
                        });                        
                    } 
                }                 
            }             
        } 
                 
    }])

.controller('PropertyController',
    ['$scope', '$rootScope', 
    function ($scope, $rootScope) {        
         
        $scope.authority = $rootScope.globals.currentUser.authority;
        $scope.username = $rootScope.globals.currentUser.username;
        
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
        
        $scope.PropertyInformationId = 1; // should be dynamically fetched from backend
        $scope.PersonTypeId = 2; // should be dynamically fetched from backend

        
    }]);
