angular.module('number', [])
    .directive('number', function () {
        return {
            require: 'ngModel',
            restrict: 'A',
            link: function (scope, element, attrs, ctrl) {
                ctrl.$parsers.push(function (input) {
                    if (input == undefined) return ''
                    var inputNumber = input.toString().replace(/[^0-9]/g, '');
                    if (inputNumber != input) {                        
                        ctrl.$setViewValue(inputNumber);
                        ctrl.$render();
                    }     
                    // restrict length to max 5
                    if(inputNumber.length >5) {
                        ctrl.$setViewValue(inputNumber.substr(0,4));
                        ctrl.$render();
                    }
                    return inputNumber;
                });
            }
        };
    });