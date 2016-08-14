angular.module('jitty.liveview.controllers', []).controller('LiveviewController', function ($scope, $http, TournamentClass, $routeParams) {

    $scope.getPossibleClasses = function () {
        $http.get('/api/lifeview/started-classes', {}).then(function (response) {
            $scope.possibleClasses = response.data;
        });

    };
    if ($routeParams.id == null)
        $scope.getPossibleClasses();

    $scope.getTournamentClass = function () {
        $scope.tournamentClass = TournamentClass.get({id: $routeParams.id}, function () {
            console.log('TournamentClass got successful');
        })
    };

    $scope.getGroups = function () {
        $http.get('/api/lifeview/groups?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.groups = response.data;
        });

    };

    if ($routeParams.id != null) {
        $scope.getTournamentClass();
        $scope.getGroups();
    }

});

