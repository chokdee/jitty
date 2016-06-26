angular.module('jitty.tournament', ['ngRoute', 'ngResource', 'ngMessages', 'jitty.tournament.controllers', 'jitty.tournament.services']).config(function ($routeProvider, $httpProvider) {

    $routeProvider.when('/tournaments', {
        templateUrl: '/js/tournament/tournaments.html',
        controller: 'TournamentListController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

});