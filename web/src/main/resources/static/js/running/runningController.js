angular.module('jitty.running.controllers', []).controller('RunningController', function ($scope, $http, $routeParams) {
    $scope.getPossibleGames = function () {
        $http.get('/api/draw/possible-games', {}).then(function (response) {
            $scope.possibleGames = response.data;
        });

    };
    $scope.getPossibleGames();


});

