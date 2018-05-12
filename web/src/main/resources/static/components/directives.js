/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

'use strict';

/* Directives */


angular.module('jitty.directives', [])
    .directive('matchValidator', function () {
        return {
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {
                ngModel.$parsers.push(function (value) {
                    ngModel.$setValidity('match', value == scope.$eval(attrs.matchValidator));
                    return value;
                });
            }
        }
    })
    .directive('passwordCharactersValidator', function () {
        var PASSWORD_FORMATS = [
            /[^\w\s]+/, //special characters
            /[A-Z]+/, //uppercase letters
            /\w+/, //other letters
            /\d+/ //numbers
        ];

        return {
            require: 'ngModel',
            link: function (scope, element, attrs, ngModel) {
                ngModel.$parsers.push(function (value) {
                    var status = true;
                    //todo uncomment if you want to enable format
                    //angular.forEach(PASSWORD_FORMATS, function(regex) {
                    //  status = status && regex.test(value);
                    //});
                    ngModel.$setValidity('password-characters', status);
                    return value;
                });
            }
        }
    })
    .directive('jitDragMe', function () {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                element.prop('draggable', true);
                element.on('dragstart', function (event) {
                    event.dataTransfer = event.originalEvent.dataTransfer;
                    console.log('dragstart:' + event.target.innerHTML);
                    console.log('event:' + event);
                    event.dataTransfer.setData('text', event.target.id)
                });
            }
        };
    })
    .directive('jitDropOnMe', function () {
        var DDO = {
            restrict: 'A',
            link: function (scope, element, attrs) {
                element.on('dragover', function (event) {
                    event.preventDefault();
                });
                element.on('drop', function (event) {
                    event.preventDefault();
                    event.dataTransfer = event.originalEvent.dataTransfer;
                    var data = event.dataTransfer.getData("text");

                    if (event.target.innerHTML != null && !event.target.innerHTML.startsWith("Tisch"))
                        event.target.innerHTML = null;

                    event.target.appendChild(document.getElementById(data));
                });
            }
        };
        return DDO;
    })
;
