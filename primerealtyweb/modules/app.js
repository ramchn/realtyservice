'use strict';

// declare modules
angular.module('Env', []);
angular.module('Signup', []);
angular.module('Template', []);
angular.module('Home', []);
angular.module('PropertyInformation', []);
angular.module('IssueRequest', []);
angular.module('Myinfo', []);
angular.module('Rest', []);
angular.module('number', []);

angular.module('PrimeRealty', [
    'Env',
    'Signup',
    'Template',
    'Home',    
    'PropertyInformation',
    'IssueRequest',
    'Myinfo',
    'Rest',
    'ui.router',
    'ngCookies',
    'number'
])

.config(['$stateProvider', '$urlRouterProvider', 
         function ($stateProvider, $urlRouterProvider) {
    
    $urlRouterProvider.otherwise('/');
         
    $stateProvider
	.state('index', {
            url: '/',            
            views: {
                '': {
                    templateUrl: 'modules/signup/views/index.html'      
                },
                'header@index': {
                    templateUrl: 'modules/template/views/header.html'        
                },                
                'footer@index': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            } 
        })	 
    .state('signup', {
            url: '/signup',             
            views: {
                '': {
                    controller: 'SignupController',
                    templateUrl: 'modules/signup/views/signup.html'      
                },
                'header@signup': {
                    templateUrl: 'modules/template/views/header.html'        
                },                
                'footer@signup': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            } 
        })    
    .state('userverification', {
            url: '/userverification',             
            views: {
                '': {
                    controller: 'UserVerificationController',
                    templateUrl: 'modules/signup/views/userverification.html'      
                },
                'header@userverification': {
                    templateUrl: 'modules/template/views/header.html'        
                },                
                'footer@userverification': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            } 
        })      
	.state('signin', {
            url: '/signin',             
            views: {
                '': {
                    controller: 'SigninController',
                    templateUrl: 'modules/signup/views/signin.html'       
                },
                'header@signin': {
                    templateUrl: 'modules/template/views/header.html'        
                },                
                'footer@signin': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }     
        })
    .state('forgetpassword', {
            url: '/forgetpassword',             
            views: {
                '': {
                    controller: 'ForgetPasswordController',
                    templateUrl: 'modules/signup/views/forgetpassword.html'       
                },
                'header@forgetpassword': {
                    templateUrl: 'modules/template/views/header.html'        
                },                
                'footer@forgetpassword': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }     
        })
	.state('home', {
            url: '/home', 
            views: {
                '': {
                    controller: 'HomeController',
                    templateUrl: 'modules/home/views/home.html'        
                },
                'header@home': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@home': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@home': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }            
        })     
    .state('editsp', {
            url: '/editsp',             
            views: {
                '': {
                    templateUrl: 'modules/home/views/editsp.html'    
                },
                'header@editsp': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@editsp': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editsp': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('viewpropinfo', {
            url: '/viewpropinfo',             
            views: {
                '': {
                    controller: 'ViewPropertyInformationController',
                    templateUrl: 'modules/propertyinformation/views/viewpropinfo.html'      
                },
                'header@viewpropinfo': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@viewpropinfo': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@viewpropinfo': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('createpropinfo', {
            url: '/createpropinfo',             
            views: {
                '': {
                    controller: 'CreatePropertyInformationController',
                    templateUrl: 'modules/propertyinformation/views/createpropinfo.html'        
                },
                'header@createpropinfo': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@createpropinfo': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@createpropinfo': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        })  
    .state('detailpropinfo', {
            url: '/detailpropinfo',             
            views: {
                '': {
                    controller: 'DetailPropertyInformationController',
                    templateUrl: 'modules/propertyinformation/views/detailpropinfo.html'     
                },
                'header@detailpropinfo': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@detailpropinfo': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@detailpropinfo': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('editpropinfo', {
            url: '/editpropinfo',             
            views: {
                '': {
                    controller: 'EditPropertyInformationController',
                    templateUrl: 'modules/propertyinformation/views/editpropinfo.html'     
                },
                'header@editpropinfo': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@editpropinfo': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editpropinfo': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        })     
    .state('assigntenant', {
            url: '/assigntenant',             
            views: {
                '': {
                    controller: 'AssignTenantsController',
                    templateUrl: 'modules/propertyinformation/views/assigntenant.html'     
                },
                'header@assigntenant': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@assigntenant': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@assigntenant': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('assignpropmngr', {
            url: '/assignpropmngr',             
            views: {
                '': {
                    controller: 'AssignPropertyMngrController',
                    templateUrl: 'modules/propertyinformation/views/assignpropmngr.html'     
                },
                'header@assignpropmngr': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@assignpropmngr': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@assignpropmngr': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('assignowner', {
            url: '/assignowner',             
            views: {
                '': {
                    controller: 'AssignOwnerController',
                    templateUrl: 'modules/propertyinformation/views/assignowner.html'     
                },
                'header@assignowner': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@assignowner': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@assignowner': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('createissuereq', {
            url: '/createissuereq',             
            views: {
                '': {
                    controller: 'CreateIssueRequestController',
                    templateUrl: 'modules/issuerequest/views/createissuereq.html'    
                },
                'header@createissuereq': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@createissuereq': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@createissuereq': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('viewissuereq', {
            url: '/viewissuereq',             
            views: {
                '': {
                    controller: 'ViewIssueRequestController',
                    templateUrl: 'modules/issuerequest/views/viewissuereq.html'    
                },
                'header@viewissuereq': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@viewissuereq': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@viewissuereq': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('viewattachment', {
            url: '/viewattachment',             
            views: {
                '': {
                    controller: 'ViewAttachmentController',
                    templateUrl: 'modules/issuerequest/views/viewattachment.html'    
                }
            }  
        }) 
    .state('viewissuereqlogs', {
            url: '/viewissuereqlogs',             
            views: {
                '': {
                    controller: 'ViewIssueRequestLogsController',
                    templateUrl: 'modules/issuerequest/views/viewissuereqlogs.html'    
                },
                'header@viewissuereqlogs': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@viewissuereqlogs': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@viewissuereqlogs': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        })              
    .state('assignissuereq', {
            url: '/assignissuereq',             
            views: {
                '': {
                    controller: 'AssignIssueRequestController',
                    templateUrl: 'modules/issuerequest/views/assignissuereq.html'    
                }
            }  
        }) 
    .state('myinfo', {
            url: '/myinfo',             
            views: {
                '': {
                    controller: 'MyinfoController',
                    templateUrl: 'modules/myinfo/views/myinfo.html'    
                },
                'header@myinfo': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@myinfo': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@myinfo': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('editname', {
            url: '/editname',             
            views: {
                '': {
                    controller: 'MyinfoController',
                    templateUrl: 'modules/myinfo/views/editname.html'     
                },
                'header@editname': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@editname': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editname': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('editcompany', {
            url: '/editcompany',             
            views: {
                '': {
                    controller: 'MyinfoController',
                    templateUrl: 'modules/myinfo/views/editcompany.html'     
                },
                'header@editcompany': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@editcompany': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editcompany': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('editphone', {
            url: '/editphone',             
            views: {
                '': {
                    controller: 'MyinfoController',
                    templateUrl: 'modules/myinfo/views/editphone.html'     
                },
                'header@editphone': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@editphone': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editphone': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('editemail', {
            url: '/editemail',             
            views: {
                '': {
                    controller: 'MyinfoController',
                    templateUrl: 'modules/myinfo/views/editemail.html'    
                },
                'header@editemail': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@editemail': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editemail': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('editpassword', {
            url: '/editpassword',             
            views: {
                '': {
                    controller: 'MyinfoController',
                    templateUrl: 'modules/myinfo/views/editpassword.html'    
                },
                'header@editpassword': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@editpassword': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editpassword': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('editaddress', {
            url: '/editaddress',             
            views: {
                '': {
                    controller: 'MyinfoController',
                    templateUrl: 'modules/myinfo/views/editaddress.html'    
                },
                'header@editaddress': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@editaddress': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editaddress': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        });
}]);
