angular.module('jitty.draw.controllers', []).controller('DrawController', function ($scope, $http, $routeParams) {

    $scope.getPossibleClasses = function () {
        $http.get('/api/tournament-classes/not-running', {}).then(function (response) {
            $scope.possibleClasses = response.data;
        });

    };

    $scope.getPossibleClasses();

    $scope.getPlayerForClass = function () {
        $http.get('/api/draw/player-for-class?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.players = response.data;
        });

    };
    if ($routeParams.id != null)
        $scope.getPlayerForClass();

    $scope.createDummyPlayer = function () {
        $http.get('/api/draw/dummy-player?cid=' + $routeParams.id, {}).then(function (response) {
        });

    };
});

