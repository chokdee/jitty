/*
 * Copyright (c) 2017.
 * J. Melzer
 */

angular.module('jitty.settings', ['jitty.settings.controllers']).config(function ($routeProvider, $httpProvider, $provide) {

    $routeProvider.when('/settings/table', {
        title: 'Tische konfigurieren',
        templateUrl: '/components/settings/table.html',
        controller: 'TableSettingsController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


}).run(function ($rootScope, $location) {


});