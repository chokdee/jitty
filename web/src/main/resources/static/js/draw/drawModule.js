angular.module('jitty.draw', ['ngRoute', 'ngResource', 'ngMessages', 'ui.select', 'ngSanitize',
    'jitty.draw.controllers', 'ui.bootstrap']).config(function ($routeProvider, $httpProvider, $provide) {

    $routeProvider.when('/draw-select-class', {
        templateUrl: '/js/draw/draw-select-class.html',
        controller: 'DrawController'
    }).when('/draw/:id', {
        templateUrl: '/js/draw/draw.html',
        controller: 'DrawController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


});