angular.module('jitty.running', ['ngRoute', 'ngResource', 'ngMessages', 'ui.select', 'ngSanitize',
    'jitty.running.controllers', 'jitty.running.services', 'ui.bootstrap', 'ui.grid', 'jitty.util.services']).config(function ($routeProvider, $httpProvider, $provide) {

    $routeProvider.when('/tournamentdirector/overview', {
        title: 'Turnierleitung Übersicht',
        templateUrl: '/js/running/overview.html',
        controller: 'RunningController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


});