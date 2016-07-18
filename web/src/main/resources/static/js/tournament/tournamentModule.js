angular.module('jitty.tournament', ['ngRoute', 'ngResource', 'ngMessages', 'jitty.tournament.controllers', 'jitty.tournament.services', 'ui.bootstrap']).
config(function ($routeProvider, $httpProvider) {

    $routeProvider.when('/tournaments', {
        templateUrl: '/js/tournament/tournaments.html',
        controller: 'TournamentListController'
    }).when('/tournaments/:id', {
        templateUrl: '/js/tournament/tournament-edit.html',
        controller: 'TournamentEditController'
    }).when('/tournament-add', {
        templateUrl: '/js/tournament/tournament-new.html',
        controller: 'TournamentCreateController'
    }).when('/tournament/:id/tournament-classes-add', {
        templateUrl: '/js/tournament/tournament-class-new.html',
        controller: 'TournamentClassCreateController'
    }).when('/tournament/tournament-classes/:id', {
        templateUrl: '/js/tournament/tournament-class-edit.html',
        controller: 'TournamentClassEditController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';


}).run(function ($rootScope, $location) {

    var history = [];

    $rootScope.$on('$routeChangeSuccess', function() {
        history.push($location.$$path);
    });

    $rootScope.back = function () {
        var prevUrl = history.length > 1 ? history.splice(-2)[0] : "/";
        $location.path(prevUrl);
    };

});