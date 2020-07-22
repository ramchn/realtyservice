'use strict';

// declare modules
angular.module('Env', []);
angular.module('Signup', []);
angular.module('Template', []);
angular.module('Home', []);
angular.module('Property', []);
angular.module('IssueTracking', []);
angular.module('Myinfo', []);
angular.module('number', []);

angular.module('PrimeRealty', [
    'Env',
    'Signup',
    'Template',
    'Home',    
    'Property',
    'IssueTracking',
    'Myinfo',
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
    .state('signupbypropertyinfo', {
            url: '/signupbypropertyinfo',             
            views: {
                '': {
                    controller: 'SignupbyPropertyInfoController',
                    templateUrl: 'modules/signup/views/signupbypropertyinfo.html'      
                },
                'header@signupbypropertyinfo': {
                    templateUrl: 'modules/template/views/header.html'        
                },                
                'footer@signupbypropertyinfo': {
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
    .state('createpi', {
            url: '/createpi',             
            views: {
                '': {
                    controller: 'PropertyController',
                    templateUrl: 'modules/property/views/createpi.html'        
                },
                'header@createpi': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@createpi': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@createpi': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('viewpi', {
            url: '/viewpi',             
            views: {
                '': {
                    controller: 'PropertyController',
                    templateUrl: 'modules/property/views/viewpi.html'      
                },
                'header@viewpi': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@viewpi': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@viewpi': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('editpi', {
            url: '/editpi',             
            views: {
                '': {
                    controller: 'PropertyController',
                    templateUrl: 'modules/property/views/editpi.html'     
                },
                'header@editpi': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@editpi': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editpi': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('addtenant', {
            url: '/addtenant',             
            views: {
                '': {
                    controller: 'PropertyController',
                    templateUrl: 'modules/property/views/addtenant.html'     
                },
                'header@addtenant': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@addtenant': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@addtenant': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('edittenant', {
            url: '/edittenant',             
            views: {
                '': {
                    controller: 'PropertyController',
                    templateUrl: 'modules/property/views/edittenant.html'     
                },
                'header@edittenant': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@edittenant': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@edittenant': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('addpm', {
            url: '/addpm',             
            views: {
                '': {
                    controller: 'PropertyController',
                    templateUrl: 'modules/property/views/addpm.html'     
                },
                'header@addpm': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@addpm': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@addpm': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('editpm', {
            url: '/editpm',             
            views: {
                '': {
                    controller: 'PropertyController',
                    templateUrl: 'modules/property/views/editpm.html'     
                },
                'header@editpm': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@edittenant': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editpm': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('addowner', {
            url: '/addowner',             
            views: {
                '': {
                    controller: 'PropertyController',
                    templateUrl: 'modules/property/views/addowner.html'     
                },
                'header@addowner': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@addowner': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@addowner': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('editowner', {
            url: '/editowner',             
            views: {
                '': {
                    controller: 'PropertyController',
                    templateUrl: 'modules/property/views/editowner.html'     
                },
                'header@editowner': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@editowner': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@editowner': {
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
    .state('createit', {
            url: '/createit',             
            views: {
                '': {
                    controller: 'IssueTrackingController',
                    templateUrl: 'modules/issuetracking/views/createit.html'    
                },
                'header@createit': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@createit': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@createit': {
                    templateUrl: 'modules/template/views/footer.html'        
                }
            }  
        }) 
    .state('viewit', {
            url: '/viewit',             
            views: {
                '': {
                    controller: 'IssueTrackingController',
                    templateUrl: 'modules/issuetracking/views/viewit.html'    
                },
                'header@viewit': {
                    templateUrl: 'modules/template/views/header.html'        
                },
                'menu@viewit': {
                    controller: 'MenuController',
                    templateUrl: 'modules/template/views/menu.html'        
                },
                'footer@viewit': {
                    templateUrl: 'modules/template/views/footer.html'        
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
