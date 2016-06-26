angular.module('jitty.tournament.controllers', []).controller('TournamentListController', function ($scope, $window, Tournament) {

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

})