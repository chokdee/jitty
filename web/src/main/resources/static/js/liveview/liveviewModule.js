angular.module('jitty.liveview', ['ngRoute', 'ngResource', 'ngMessages', 'ui.select', 'ngSanitize',
    'jitty.liveview.controllers', 'jitty.liveview.services', 'ui.bootstrap', 'ui.grid']).config(function ($routeProvider, $httpProvider) {

    $routeProvider.when('/liveview/overview', {
        title: 'Klasse auswählen',
        templateUrl: '/js/liveview/overview.html',
        controller: 'LiveviewController'
    }).when('/liveview/groups/:id', {
        title: 'Liveview - Gruppen',
        templateUrl: '/js/liveview/groups.html',
        controller: 'LiveviewController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';



});