'use strict';

angular.module('IssueRequest')

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

.directive('fileModel', ['$parse', function ($parse) {
    return {
       restrict: 'A',
       link: function(scope, element, attrs) {
          var model = $parse(attrs.fileModel);
          var modelSetter = model.assign;

          element.bind('change', function() {
             scope.$apply(function() {
                modelSetter(scope, element[0].files[0]);
             });
          });
       }
    };
 }])

.controller('IssueRequestController',
    ['$scope', '$rootScope', 
    function ($scope, $rootScope) {        
              
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
        
    }])

.controller('ViewIssueRequestLogsController',
    ['$scope', '$rootScope', '$location', 'IssueRequestService', 'ENV', '$window', 
    function ($scope, $rootScope, $location, IssueRequestService, ENV, $window) {        
        $scope.personid = $rootScope.globals.currentUser.personid;
        
        var qp = ''+0;
        var url = $location.absUrl();    
        var qsexist = url.lastIndexOf('?');
        if(qsexist != -1) {
            qp = url.substr(qsexist+1);              
        }  
        $scope.issueid = qp;
                
        var formData = { 'issueId': $scope.issueid }
        IssueRequestService.getIssueLogs(formData, function (issuelogs) {            
            for(var i = 0; i < issuelogs.length; i++) {
                if(angular.isDefined(issuelogs[i].AttachmentType)) { 
                    issuelogs[i].Url = ENV.ISSUEREQUEST_API_URL + '/viewattachmentbyissuelog?issuelogId='+issuelogs[i].idIssueLog;
                }
            }
            $scope.issuelogs = issuelogs;
        });
        
        $scope.showdetail = function(id) {
            var tdetail = document.getElementById("detail"+id);        
            if(tdetail.style.display == "none") {
                tdetail.style.display="block";
            } else {
                tdetail.style.display="none";
            }
        }
        
        // attachment type is image - open it in a new window
        $scope.openAttachment = function(Url) {
            $window.open("#!/viewattachment?url="+Url, "attachment", "width=700,height=500,left=150,top=150");
        }
        
        // attachment type is pdf - let it download
        $scope.downloadAttachment = function(Url) {
            $window.location.href = Url;
        }
                                          
        var uploadField = document.getElementById("attachment");
        // 1MB in Bytes is 1,048,576 
        uploadField.onchange = function() {
            var filetype = this.files[0].type;
            // allow only image and pdf types
            if(filetype.indexOf("image") == -1 && filetype.indexOf("pdf") == -1) {
               alert("Attachment should be either image or pdf!");
                this.value = "";
            } else if(this.files[0].size > 5242880){ // size should be less than 5MB              
                alert("Attachment should be less than 5MB!");
                this.value = "";
            }
        }   
        
        $scope.submitForm = function() {          
            
            $('#issuerequestlogsubmit').attr('disabled', true);
            $("#spinner").show();
            // only tenant cant submit the issue request
            // so assumption is tenant is always assocaiate with one property information
            if(angular.isDefined($scope.Attachment)) {
                
                var formData = new FormData();
                formData.append('log', $scope.Log);            
                formData.append('attachment', $scope.Attachment);            
                formData.append('issueId', $scope.issueid);
                formData.append('personId', $scope.personid);
                
                IssueRequestService.createIssueRequestLogMP(formData, function (response) {      
                    $scope.result = response;
                    
                    var formData = { 'issueId': $scope.issueid }
                    IssueRequestService.getIssueLogs(formData, function (issuelogs) {
                        for(var i = 0; i < issuelogs.length; i++) {
                            if(angular.isDefined(issuelogs[i].AttachmentType)) { 
                                issuelogs[i].Url = ENV.ISSUEREQUEST_API_URL + '/viewattachmentbyissuelog?issuelogId='+issuelogs[i].idIssueLog;
                            }
                        }
                        $scope.issuelogs = issuelogs;
                    });

                    $('#issuerequestlogsubmit').attr('disabled', false);
                    $("#spinner").hide();
                });
                
            } else {
                
                var formData = {
                    'log':$scope.Log,
                    'issueId':$scope.issueid,
                    'personId':$scope.personid                    
                }
                
                IssueRequestService.createIssueRequestLog(formData, function (response) {      
                    $scope.result = response;
                    
                    var formData = { 'issueId': $scope.issueid }
                    IssueRequestService.getIssueLogs(formData, function (issuelogs) {
                        for(var i = 0; i < issuelogs.length; i++) {
                            if(angular.isDefined(issuelogs[i].AttachmentType)) { 
                                issuelogs[i].Url = ENV.ISSUEREQUEST_API_URL + '/viewattachmentbyissuelog?issuelogId='+issuelogs[i].idIssueLog;
                            }
                        }
                        $scope.issuelogs = issuelogs;
                        $scope.Log = '';
                    });

                    $('#issuerequestsubmit').attr('disabled', false);
                    $("#spinner").hide();
                });
            }                                 
        }
        
    }])

.controller('AssignIssueRequestController',
    ['$scope', '$rootScope', '$location', 'IssueRequestService', '$window',  
    function ($scope, $rootScope, $location, IssueRequestService, $window) {        
        $scope.personid = $rootScope.globals.currentUser.personid;
        
        var qp = ''+0;
        var url = $location.absUrl();    
        var qsexist = url.lastIndexOf('?');
        if(qsexist != -1) {
            qp = url.substr(qsexist+1);             
        }       
        $scope.issueid = qp;
        
        IssueRequestService.getServiceProviders(function (serviceproviders) {
            $scope.serviceproviders = serviceproviders;
        });
        
        var formData = { 'issueId':$scope.issueid }
        IssueRequestService.assignedPersonForIssue(formData, function (assignedperson) {
            $scope.assignedperson = assignedperson;              
        });
                        
        $scope.assignPerson = function(selectedserviceproviderid) {
            $("#spinner").show();
            var formData = { 'personId':$scope.personid, 'issueId':$scope.issueid, 'serviceProviderId':selectedserviceproviderid }
            IssueRequestService.assignPersonForIssue(formData, function (response) {
                $scope.result = response;
                var formData = { 'issueId':$scope.issueid }
                IssueRequestService.assignedPersonForIssue(formData, function (assignedperson) {
                    $scope.assignedperson = assignedperson;              
                });
                $window.opener.location.reload();
                $("#spinner").hide();
            });             
        }        
    }])

.controller('ViewIssueRequestController',
    ['$scope', '$rootScope', 'IssueRequestService', '$window', 'ENV', 
    function ($scope, $rootScope, IssueRequestService, $window, ENV) {    
        
        $scope.authority = $rootScope.globals.currentUser.authority;
        $scope.personid = $rootScope.globals.currentUser.personid;
                
        IssueRequestService.getIssueStatuses(function (issueStatuses) {
            $scope.issueStatuses = issueStatuses;            
        });
           
        if($scope.authority == 'ROLE_TENANT') {            
            $("#createissue").css("display","block");
            
        } else {
            $("#createissue").css("display","none");
        }
        
        var formData = { 'personId':$scope.personid }
        IssueRequestService.getIssues(formData, function (propertiesissues) {
            
            for(var j=0; j < propertiesissues.length; j++) {   
                              
                for(var i=0; i < propertiesissues[j].Issues.length; i++) {   
                    if(angular.isDefined(propertiesissues[j].Issues[i].AttachmentType)) { 
                        propertiesissues[j].Issues[i].Url = ENV.ISSUEREQUEST_API_URL + '/viewattachmentbyissue?issueId='+propertiesissues[j].Issues[i].idIssue;
                    }
                    propertiesissues[j].Issues[i].issuestatusdata = {
                        availableOptions: $scope.issueStatuses,
                        selectedOption: {idIssueStatus: propertiesissues[j].Issues[i].idIssueStatus, IssueStatus: propertiesissues[j].Issues[i].IssueStatus} 
                    }
                    if(angular.isDefined(propertiesissues[j].Issues[i].AssignedPerson)) {
                        propertiesissues[j].Issues[i].assignperson = "Assigned";
                    } else {
                        propertiesissues[j].Issues[i].assignperson = "Unassigned";
                    }
                }
            }
            $scope.propertiesissues = propertiesissues;
        });
        
        // attachment type is image - open it in a new window
        $scope.openAttachment = function(Url) {
            $window.open("#!/viewattachment?url="+Url, "attachment", "width=700,height=500,left=150,top=150");
        }
        
        // attachment type is pdf - let it download
        $scope.downloadAttachment = function(Url) {
            $window.location.href = Url;
        }
        
        // status change
        $scope.statusChange = function(issueid, issuestatusid) {
            var formData = { 'issueId':issueid, 'issueStatusId':issuestatusid, 'personId':$scope.personid }
            IssueRequestService.updateIssueStatus(formData, function (response) {
                $scope.result = response;
            });
        }
        
        // view and assign a service provider - open it in a new window
        $scope.openServiceProvider = function(issueid) {
            $window.open("#!/assignissuereq?"+issueid, "serviceprovider", "width=600,height=400,left=150,top=150");     
            return false;
        }        
    }])

.controller('ViewAttachmentController',
    ['$scope', '$rootScope', '$location',  
    function ($scope, $rootScope, $location) {        
        $scope.personid = $rootScope.globals.currentUser.personid;
        var qs = $location.search();        
        $scope.Url = qs.url;
        
    }])

.controller('CreateIssueRequestController',
    ['$scope', '$rootScope', 'IssueRequestService',  
    function ($scope, $rootScope, IssueRequestService) {    
        $scope.personid = $rootScope.globals.currentUser.personid;    
        
        var formData = {
            'personId':$scope.personid
        }
        IssueRequestService.getPropInfoForTenant(formData, function (response) {
            $scope.PropertyInformationId = response.PropertyInformationId;
        });
                
        IssueRequestService.getIssueCategories(function (issueCategories) {
            $scope.issueCategories = issueCategories;
        });
        
        var uploadField = document.getElementById("attachment");
        // 1MB in Bytes is 1,048,576 
        uploadField.onchange = function() {
            var filetype = this.files[0].type;
            // allow only image and pdf types
            if(filetype.indexOf("image") == -1 && filetype.indexOf("pdf") == -1) {
               alert("Attachment should be either image or pdf!");
                this.value = "";
            } else if(this.files[0].size > 5242880){ // size should be less than 5MB              
                alert("Attachment should be less than 5MB!");
                this.value = "";
            }
        }     
                
        $scope.submitForm = function() {          
            
            $('#issuerequestsubmit').attr('disabled', true);
            $("#spinner").show();
            // only tenant cant submit the issue request
            // so assumption is tenant is always assocaiate with one property information
            if(angular.isDefined($scope.Attachment)) {
                
                var formData = new FormData();
                formData.append('issue', $scope.Issue);            
                formData.append('issueDescription', $scope.IssueDescription);             
                formData.append('attachment', $scope.Attachment);            
                formData.append('issueCategoryId', $scope.selectedIssueCategory.idIssueCategory);
                formData.append('personId', $scope.personid);
                formData.append('propertyInformationId', $scope.PropertyInformationId);
                
                IssueRequestService.createIssueRequestMP(formData, function (response) {      
                    $scope.result = response;

                    $('#issuerequestsubmit').attr('disabled', false);
                    $("#spinner").hide();
                });
                
            } else {
                
                var formData = {
                    'issue':$scope.Issue,
                    'issueDescription':$scope.IssueDescription,
                    'issueCategoryId':$scope.selectedIssueCategory.idIssueCategory,
                    'personId':$scope.personid,
                    'propertyInformationId':$scope.PropertyInformationId
                }
                
                IssueRequestService.createIssueRequest(formData, function (response) {      
                    $scope.result = response;

                    $('#issuerequestsubmit').attr('disabled', false);
                    $("#spinner").hide();
                });
            }   
            
        }
        
    }]);
