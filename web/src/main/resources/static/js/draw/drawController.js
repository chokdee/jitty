angular.module('jitty.draw.controllers', []).controller('DrawController', function ($scope, $http, $routeParams, TournamentClass) {

    $scope.resultSize = 0;

    $scope.getPossibleClasses = function () {
        $http.get('/api/tournament-classes/not-running', {}).then(function (response) {
            $scope.possibleClasses = response.data;
        });

    };
    if ($routeParams.id == null)
        $scope.getPossibleClasses();

    $scope.getTournamentClass = function () {
        $scope.tournamentClass = TournamentClass.get({id: $routeParams.id}, function () {
            console.log('Got TournamentClass successful');
            if ($scope.tournamentClass.playerPerGroup == null)
                $scope.tournamentClass.playerPerGroup = 4; //default

        });
        $scope.groups = $scope.tournamentClass.groups;
    };


    $scope.getPlayerForClass = function () {
        $http.get('/api/draw/player-for-class?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.players = response.data;

        });

    };
    if ($routeParams.id != null) {
        $scope.getTournamentClass();
        $scope.getPlayerForClass();
    }

    $scope.createDummyPlayer = function () {
        $http.get('/api/draw/dummy-player?cid=' + $routeParams.id, {}).then(function (response) {
        });
    };

    $scope.fillAll = function () {
        $scope.automaticDraw();
        after(1000, $scope.saveDraw());
        after(1000, $scope.start());
    };
    function after(ms, fn){ setTimeout(fn, ms); }

    $scope.automaticDraw = function () {
        $http({
            method: 'POST',
            url: '/api/draw/automatic-draw',
            data: $scope.tournamentClass
        }).then(function successCallback(response) {
            $scope.tournamentClass = response.data;

            $scope.createGroups();

        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });

    };
    $scope.saveDraw = function () {
        $scope.tournamentClass.groups = $scope.groups;
        $http({
            method: 'POST',
            url: '/api/draw/save',
            data: $scope.tournamentClass
        }).then(function successCallback(response) {

        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });

    };
    $scope.start = function () {
        $http.get('/api/draw/start?cid=' + $routeParams.id, {}).then(function (response) {
        });

    };
    $scope.calcGroupSize = function () {
        $http({
            method: 'POST',
            url: '/api/draw/calc-optimal-group-size',
            data: $scope.tournamentClass
        }).then(function successCallback(response) {
            $scope.tournamentClass = response.data;
            $scope.resultSize = $scope.tournamentClass.playerPerGroup * $scope.tournamentClass.groupCount;
            console.log('resultsize = ' + $scope.resultSize);

            $scope.createGroups();

        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });
    };

    $scope.groups = {};

    $scope.createGroups = function () {
        if ($scope.tournamentClass.groups == null) {
            for (i = 0; i < $scope.tournamentClass.groupCount; i++) {
                $scope.groups[i] = {name: 'Gruppe ' + (i + 1), players: {}};
                //
                //for (j = 0; j < $scope.tournamentClass.playerPerGroup; j++) {
                //    $scope.groups[i].players[j] = {firstName: '---Frei--'};
                //}
            }
        } else {
            $scope.groups = $scope.tournamentClass.groups;
        }
    };
    $scope.refreshGroupSize = function () {
        if ($scope.tournamentClass != null && $scope.tournamentClass.gameModePhase1 == 'g')
            if ($scope.tournamentClass != null && $scope.tournamentClass.id != null)
                $scope.calcGroupSize();
    };
    $scope.$watch('tournamentClass.gameModePhase1', function () {
        $scope.refreshGroupSize();
    });
    $scope.$watch('tournamentClass.playerPerGroup', function () {
        $scope.refreshGroupSize();
    });
});

