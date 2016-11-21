angular.module('jitty.draw.controllers', []).controller('DrawController', function ($scope, $http, $routeParams, TournamentClass) {

    $scope.getTournamentClass = function () {
        $scope.tournamentClass = TournamentClass.get({id: $routeParams.id}, function () {
            console.log('Got TournamentClass successful');
            if ($scope.tournamentClass.playerPerGroup == null)
                $scope.tournamentClass.playerPerGroup = 4; //default


        });
        $scope.groups = $scope.tournamentClass.groups;
        if (!$scope.tournamentClass.running)
            $scope.templateurl = 'js/draw/groups.html';
        else
            $scope.templateurl = 'js/draw/bracket.html';
    };

    if ($routeParams.id != null) {
        $scope.getTournamentClass();
    }

    $scope.getPossibleClasses = function () {
        $http.get('/api/tournament-classes/not-running', {}).then(function (response) {
            $scope.possibleClasses = response.data;
        });

    };
    if ($routeParams.id == null)
        $scope.getPossibleClasses();


}).controller('GroupController', function ($scope, $http, $routeParams,  $window, TournamentClass, Flash) {
    $scope.resultSize = 0;
    $scope.modus = null;
    $scope.modi = [{
        id: 1,
        label: 'Gruppe'
    }, {
        id: 2,
        label: 'KO'
    }];


    $scope.displaySaveMessage = function () {
        var message = 'Die Gruppenauslosung wurde erfolgreich gespeichert';
        var id = Flash.create('success', message, 4000, {container: 'flash-status'});
        // First argument (string) is the type of the flash alert.
        // Second argument (string) is the message displays in the flash alert (HTML is ok).
        // Third argument (number, optional) is the duration of showing the flash. 0 to not automatically hide flash (user needs to click the cross on top-right corner).
        // Fourth argument (object, optional) is the custom class and id to be added for the flash message created.
        // Fifth argument (boolean, optional) is the visibility of close button for this flash.
        // Returns the unique id of flash message that can be used to call Flash.dismiss(id); to dismiss the flash message.
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
            $scope.displaySaveMessage();
        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });

    };
    $scope.start = function () {
        $http.get('/api/draw/start?cid=' + $routeParams.id, {}).then(function (response) {
            $window.location.href = '/#/tournamentdirector/overview';
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
        if ($scope.tournamentClass != null && $scope.modus.id == 1)
            if ($scope.tournamentClass != null && $scope.tournamentClass.id != null)
                $scope.calcGroupSize();
    };
    $scope.$watch('modus', function () {
        $scope.refreshGroupSize();
    });
    $scope.$watch('tournamentClass.playerPerGroup', function () {
        $scope.refreshGroupSize();
    });

}).controller('KOController', function ($scope, $http, $routeParams, TournamentClass) {
    $scope.getGroupWinner = function () {
        $http.get('/api/draw/possible-player-for-kofield?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.players = response.data;
        });
    };
    $scope.calcKOSizeInInt = function () {
        $http.get('/api/draw/calc-ko-size?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.calcKoSize = response.data;
        });
    };

    $scope.startKO = function () {
        $http.get('/api/draw/start-ko?cid=' + $routeParams.id, {}).then(function (response) {
        });
    };


    $scope.getKoField = function (assignPlayer) {
        $http.get('/api/draw/draw-ko?id=' + $routeParams.id + '&assignPlayer=' + assignPlayer, {}).then(function (response) {
            $scope.koField = response.data;
            $scope.dummArray = new Array($scope.koField.noOfRounds - 1);
            $scope.rounds = new Array($scope.koField.noOfRounds - 1);
            round = $scope.koField.round;
            $scope.rounds [0] = round;
            for (i = 0; i < $scope.rounds.length; i++) {
                $scope.rounds [i] = round;
                round = round.nextRound;
            }
            $scope.getGroupWinner();
        });

    };

    $scope.reset = function () {
        $http.get('/api/draw/reset-ko?id=' + $routeParams.id, {}).then(function (response) {
            $scope.koField = null;
            $scope.rounds = null;
            $scope.getKoField(false);
            $scope.getGroupWinner();
        });

    };
    $scope.getNumber2 = function (num) {
        return new Array(num);
    };

    $scope.getGroupWinner();
    $scope.calcKOSizeInInt();
    $scope.getKoField(false);
});

