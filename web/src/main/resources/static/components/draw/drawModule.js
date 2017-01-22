angular.module('jitty.draw', ['ngRoute', 'ngResource', 'ngMessages', 'ui.select', 'ngSanitize',
    'jitty.draw.controllers', 'jitty.draw.services', 'ui.bootstrap']).config(function ($routeProvider, $httpProvider, $provide) {

    $routeProvider.when('/draw-select-class', {
        title: 'Auslosung: Klasse auswählen',
        templateUrl: '/components/draw/draw-select-class.html',
        controller: 'DrawController'
    }).when('/draw/:id', {
        title: 'Auslosung',
        templateUrl: '/components/draw/draw.html',
        controller: 'DrawEditController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


});