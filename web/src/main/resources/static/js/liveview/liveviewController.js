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

}).controller('LiveviewKOController', function ($scope, $http, TournamentClass, $routeParams) {


    $scope.tournamentClass = TournamentClass.get({id: $routeParams.id}, function () {
        console.log('TournamentClass got successful');
    });

    $scope.getKoField = function () {
        $http.get('/api/draw/get-ko?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.koField = response.data;
            $scope.dummArray = new Array($scope.koField.noOfRounds - 1);
            $scope.rounds = new Array($scope.koField.noOfRounds - 1);
            round = $scope.koField.round;
            $scope.rounds [0] = round;
            for (i = 0; i < $scope.rounds.length; i++) {
                round.setsArray = [];
            }
            for (i = 0; i < $scope.rounds.length; i++) {
                $scope.rounds [i] = round;
                if (round.nextRound == null) {
                    if (round.games[0].winner == 1) {
                        $scope.winner = round.games[0].player1;
                    } else if (round.games[0].winner == 2) {
                        $scope.winner = round.games[0].player2;
                    }
                    $scope.winnerSets = round.games[0].sets;

                }
                var a = [];
                for (j = 0; j < round.games.length; j++) {
                    a.push(round.games[j].sets)
                }
                if (round.nextRound != null)
                    round.nextRound.setsArray = a;
                round = round.nextRound;
            }
        });

    };
    $scope.getNumber2 = function (num) {
        return new Array(num);
    };
    $scope.getKoField();

    $scope.setsAsString = function (sets) {
        if (sets == undefined)
            return '';
        var result = null;
        for (i = 0; i < sets.length; i++) {
            if (sets[i].points2 > sets[i].points2) {
                result += '-';
                result += sets[i].points2 - sets[i].points1;
            } else {
                result += sets[i].points1 - sets[i].points2;
            }
            if (sets.length - 1 > i) {
                result += ',';
            }
        }
        if (result != null) {
            return '(' + result + ')';
        } else {
            return '';
        }
    }

});

