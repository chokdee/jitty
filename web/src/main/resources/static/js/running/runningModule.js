angular.module('jitty.running', ['ngRoute', 'ngResource', 'ngMessages', 'ui.select', 'ngSanitize',
    'jitty.running.controllers', 'jitty.running.services', 'ui.bootstrap']).config(function ($routeProvider, $httpProvider, $provide) {

    $routeProvider.when('/overview', {
        templateUrl: '/js/running/overview.html',
        controller: 'RunningController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


});