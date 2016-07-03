angular.module('jitty.tournament', ['ngRoute', 'ngResource', 'ngMessages', 'jitty.tournament.controllers', 'jitty.tournament.services', 'ui.bootstrap']).config(function ($routeProvider, $httpProvider) {

    $routeProvider.when('/tournaments', {
        templateUrl: '/js/tournament/tournaments.html',
        controller: 'TournamentListController'
    }).when('/tournaments/:id', {
        templateUrl: '/js/tournament/tournament-edit.html',
        controller: 'TournamentEditController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


});