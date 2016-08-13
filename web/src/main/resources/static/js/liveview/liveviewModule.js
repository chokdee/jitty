angular.module('jitty.liveview', ['ngRoute', 'ngResource', 'ngMessages', 'ui.select', 'ngSanitize',
    'jitty.liveview.controllers', 'jitty.liveview.services', 'ui.bootstrap', 'ui.grid']).config(function ($routeProvider, $httpProvider, $provide) {

    $routeProvider.when('/liveview/overview', {
        templateUrl: '/js/liveview/overview.html',
        controller: 'LiveviewController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


});