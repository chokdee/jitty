angular.module('jitty.player', ['ngRoute', 'ngResource', 'ngMessages', 'ui.select', 'ngSanitize',
    'jitty.player.controllers', 'jitty.player.services', 'ui.bootstrap']).config(function ($routeProvider, $httpProvider, $provide) {

    $routeProvider.when('/players', {
        title: 'Spieler anzeigen',
        templateUrl: '/js/player/players.html',
        controller: 'PlayerListController'
    }).when('/players/:id', {
        title: 'Spieler bearbeiten',
        templateUrl: '/js/player/player-edit.html',
        controller: 'PlayerEditController'
    }).when('/player-add', {
        title: 'Spieler anlegen',
        templateUrl: '/js/player/player-edit.html',
        controller: 'PlayerEditController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


}).run(function ($rootScope, $location) {

    var history = [];

    $rootScope.$on('$routeChangeSuccess', function () {
        history.push($location.$$path);
    });

    $rootScope.back = function () {
        var prevUrl = history.length > 1 ? history.splice(-2)[0] : "/";
        $location.path(prevUrl);
    };

});