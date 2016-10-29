angular.module('jitty.draw.controllers', []).controller('DrawController', function ($scope, $http, $routeParams, TournamentClass) {

    $scope.getTournamentClass = function () {
        $scope.tournamentClass = TournamentClass.get({id: $routeParams.id}, function () {
            console.log('Got TournamentClass successful');
            if ($scope.tournamentClass.playerPerGroup == null)
                $scope.tournamentClass.playerPerGroup = 4; //default

        });
        $scope.groups = $scope.tournamentClass.groups;
    };

    if ($routeParams.id != null) {
        $scope.getTournamentClass();
    }

}).controller('GroupController', function ($scope, $http, $routeParams, TournamentClass) {
    $scope.resultSize = 0;
    $scope.modus = null;
    $scope.modi = [{
        id: 1,
        label: 'Gruppe'
    }, {
        id: 2,
        label: 'KO'
    }];

    $scope.getPossibleClasses = function () {
        $http.get('/api/tournament-classes/not-running', {}).then(function (response) {
            $scope.possibleClasses = response.data;
        });

    };
    if ($routeParams.id == null)
        $scope.getPossibleClasses();


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
    function after(ms, fn) {
        setTimeout(fn, ms);
    }


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

}).controller('KOController', function ($scope, $http, $routeParams, TournamentClass) {
    $scope.getGroupWinner = function () {
        $http.get('/api/draw/group-winner-for-class?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.players = response.data;
        });
    };
    $scope.calcKOSizeInInt = function () {
        $http.get('/api/draw/calc-ko-size?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.calcKoSize = response.data;
        });
    };

    $scope.getKoField = function () {
        $http.get('/api/tournamentdirector/start-ko?id=' + $routeParams.id + '&assignPlayer=false', {}).then(function (response) {
            $scope.koField = response.data;
            $scope.dummArray = new Array($scope.koField.noOfRounds - 1);
            $scope.rounds = new Array($scope.koField.noOfRounds - 1);
            lastRound = $scope.koField.round;
            $scope.rounds [0] = lastRound;
            for (i = 0; i < $scope.rounds.length; i++) {
                $scope.rounds [i] = lastRound;
                lastRound = lastRound.nextRound;
            }
        });
    };

    $scope.getNumber = function (num) {
        if (num === undefined)
            return new Array(0);

        var a = undefined;
        if (num == 0) {
            a = new Array($scope.koField.round.size);
        }
        else if (num == 1) {
            a = new Array($scope.koField.round.nextRound.size);
        }
        else if (num == 2) {
            a = new Array($scope.koField.round.nextRound.nextRound.size);
        }
        else if (num == 3) {
            a = new Array($scope.koField.round.nextRound.nextRound.nextRound.size);
        }
        else
            a = new Array(1);

        console.log(num + '=' + a.length);
        return a;
    };

    $scope.getGroupWinner();
    $scope.calcKOSizeInInt();
    $scope.getKoField();
});

