/*
 * Copyright (c) 2017.
 * J. Melzer
 */

angular.module('jitty.liveview', ['ngRoute', 'ngResource', 'ngMessages', 'ui.select', 'ngSanitize',
    'jitty.liveview.controllers', 'jitty.liveview.services', 'ui.bootstrap', 'ui.grid']).config(function ($routeProvider, $httpProvider) {

    $routeProvider.when('/liveview/overview', {
        title: 'Klasse auswählen',
        templateUrl: '/components/liveview/overview.html',
        controller: 'LiveviewController'
    }).when('/liveview/groups/:id', {
        title: 'Liveview - Gruppen',
        templateUrl: '/components/liveview/groups.html',
        controller: 'LiveviewController'
    }).when('/liveview/kofield/:id', {
        title: 'Liveview - KO Feld',
        templateUrl: '/components/liveview/kofield.html',
        controller: 'LiveviewKOController'
    }).when('/liveview/swiss/:id', {
        title: 'Schweizersystem - Ergebnisse',
        templateUrl: '/components/liveview/swiss.html',
        controller: 'LiveviewSwissController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';



});