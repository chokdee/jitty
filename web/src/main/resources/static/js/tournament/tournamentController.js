angular.module('jitty.tournament.controllers', []).controller('TournamentListController', function ($scope, $window, Tournament, $http, $location) {

    $scope.predicate = 'startDate';
    $scope.reverse = true;

    $scope.tournaments = Tournament.query();


    $scope.createNewTournament = function () {
        $window.location.href = '/#/tournament-add';
    };

    $scope.order = function (predicate) {
        $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
        $scope.predicate = predicate;
    };

    $scope.selectTournament = function (tid) {
        $http.get('http://localhost:8080/api/tournaments/actual/' + tid).
        success(function(data) {
            console.log('successful selected tournament ');
            $scope.tournamentname = data.name;
            $window.location.href = '/';
        });

    };
});