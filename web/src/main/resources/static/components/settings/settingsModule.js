/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

angular.module('jitty.settings', ['jitty.settings.controllers', angularDragula(angular)]).config(function ($routeProvider, $httpProvider, $provide) {

    $routeProvider.when('/settings/table', {
        title: 'Tische konfigurieren',
        templateUrl: '/components/settings/table.html',
        controller: 'TableSettingsController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


}).run(function ($rootScope, $location) {


});