angular.module('jitty.tournament', ['ngRoute', 'ngResource', 'ngMessages', 'jitty.tournament.controllers', 'jitty.tournament.services', 'ui.bootstrap']).config(function ($routeProvider, $httpProvider) {

    $routeProvider.when('/tournaments', {
        templateUrl: '/js/tournament/tournaments.html',
        controller: 'TournamentListController'
    }).when('/tournaments/:id', {
        templateUrl: '/js/tournament/tournament-edit.html',
        controller: 'TournamentEditController'
    }).when('/tournament-add', {
        templateUrl: '/js/tournament/tournament-new.html',
        controller: 'TournamentCreateController'
    }).when('/tournament-classes/:id', {
        templateUrl: '/js/tournament/tournament-classes-edit.html',
        controller: 'TournamentClassEditController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


});