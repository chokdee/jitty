angular.module('jitty.running.controllers', []).controller('RunningController', function ($scope, $http, $uibModal, $log, printer, uiGridConstants) {
    $scope.getPossibleGames = function () {
        $http.get('/api/tournamentdirector/possible-games', {}).then(function (response) {
            $scope.possibleGames = response.data;
            $scope.gridOptions.data = response.data;
        });

    };

    $scope.getTables = function () {
        $http.get('/api/tournamentdirector/tables', {}).then(function (response) {
            $scope.tables = response.data;
        });

    };

    $scope.callAll = function () {
        $scope.getPossibleGames();
        $scope.getRunningGames();
        $scope.getFinishedGames();
        $scope.anyPhaseFinished();
        $scope.getTables();
    };

    $scope.startGame = function (id) {
        $http({
            method: 'GET',
            url: '/api/tournamentdirector/start-game?id=' + id
        }).then(function successCallback(response) {
            //refresh data
            $scope.callAll();

        }, function errorCallback(response) {
            if (response.status == 400) {
                $scope.errorMessage = response.data.error;
                console.log('got 400 response message = ' + response.data.error);
            }
        });
        // $http.get('/api/tournamentdirector/start-game?id=' + id, {}).then(function (response) {
        //     $scope.callAll();
        // });

    };
    $scope.backToPossibleGames = function (id) {
        $http.get('/api/tournamentdirector/move-game-back-to-possiblegames?id=' + id, {}).then(function (response) {
            $scope.callAll();
        });

    };
    $scope.printSR = function (id) {
        $http.get('/api/tournamentdirector/get-game-for-printing?id=' + id, {}).then(function (response) {
            printer.print('/js/running/sr.html', {game: response.data});
        });
    };


    $scope.getRunningGames = function () {
        $http.get('/api/tournamentdirector/running-games', {}).then(function (response) {
            $scope.runningGames = response.data;
            $scope.gridOptionsRunning.data = response.data;
        });

    };

    $scope.anyPhaseFinished = function () {
        $http.get('/api/tournamentdirector/any-phase-finished', {}).then(function (response) {
            if (response.data.length > 0) {
                $scope.finishedTC = response.data[0];
            } else {
                $scope.finishedTC = null;
            }
        });

    };

    $scope.getFinishedGames = function () {
        $http.get('/api/tournamentdirector/finished-games', {}).then(function (response) {
            $scope.gridOptionsFinished.data = response.data;
        });

    };
    // ganes to play table
    $scope.columns = [{
        field: 'group.tournamentClass.name',
        maxWidth: 80,
        displayName: 'Klasse'
    },
        {
            field: 'roundOrGroupName',
            maxWidth: 85,
            displayName: 'Gruppe/Runde'
        },
        {
            field: 'player1.fullName',
            maxWidth: 100,
            displayName: 'Spieler 1'
        },
        {
            field: 'player1.periodSinceLastGame',
            displayName: 'Letztes Spiel',
            maxWidth: 60,
            cellTooltip: function (row, col) {
                return row.entity.player1.lastGameAt
            }
        },
        {
            field: 'player2.fullName',
            displayName: 'Spieler 2',
            maxWidth: 100
        },
        {field: 'player2.periodSinceLastGame', maxWidth: 70, displayName: 'Wartezeit'},
        {
            name: 'Aktion',
            cellTemplate: '<div class="ui-grid-cell-contents"><a class="btn btn-primary btn-sm" ng-click="grid.appScope.startGame(row.entity.id)" >Starten</a>' +
            '<a class="btn btn-primary btn-sm" ng-click="grid.appScope.printSR(row.entity.id)" >Drucken</a></div>',
            enableCellEdit: false,
            enableColumnMenu: false,
            enableSorting: false

        }
    ];

    $scope.gridOptions = {
        enableSorting: true,
        enableFiltering: true,
        rowHeight: 40,
        columnDefs: $scope.columns
    };
    $scope.enterGameResult = function (entity) {
        $scope.game = entity;
        $scope.gameResult = '';

        var modalInstance = $uibModal.open({
            scope: $scope, //passed current scope to the modal
            animation: false,
            controller: 'ModalInstanceCtrl',
            templateUrl: 'myModalContent.html',
            size: 'lg',
            resolve: {
                gameResult: function () {
                    return $scope.gameResult;
                }
            }
        });

        modalInstance.result.then(function (gameset) {
            $scope.game.sets = gameset;
            $scope.saveGame();
            //alert(gameset[0].points1);
            //todo safe the result in the db ab remove the game from the list
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };


    $scope.columnsRunning = [{field: 'group.tournamentClass.name', displayName: 'Klasse'},
        {
            field: 'roundOrGroupName',
            displayName: 'Gruppe/Runde',
            maxWidth: 85
        },
        {
            field: 'player1.fullName',
            maxWidth: 100,
            displayName: 'Spieler 1'
        },
        {
            field: 'player2.fullName',
            maxWidth: 100,
            displayName: 'Spieler 2'
        },
        {
            name: 'Aktion',
            cellTemplate: '<div class="ui-grid-cell-contents">' +
            '<a class="btn btn-primary btn-sm" ng-click="grid.appScope.enterGameResult(row.entity)" >Ergebnis...</a>' +
            '<a class="btn btn-primary btn-sm" ng-click="grid.appScope.backToPossibleGames(row.entity.id)" >Zur&uuml;ck</a>' +
            '</div>',
            enableCellEdit: false,
            enableColumnMenu: false,
            enableSorting: false
        }
    ];

    $scope.gridOptionsRunning = {
        enableSorting: true,
        rowHeight: 40,
        columnDefs: $scope.columnsRunning
    };

    $scope.columnsFinished = [{field: 'group.tournamentClass.name', displayName: 'Klasse'},
        {
            field: 'roundOrGroupName',
            displayName: 'Gruppe/Runde'
        },
        {field: 'player1.fullName', displayName: 'Spieler 1'},
        {field: 'player2.fullName', displayName: 'Spieler 2'},
        {field: 'winnerName', displayName: 'Gewinner'}
    ];

    $scope.gridOptionsFinished = {
        enableSorting: true,
        rowHeight: 40,
        columnDefs: $scope.columnsFinished
    };

    $scope.saveGame = function () {
        $http({
            method: 'POST',
            url: '/api/tournamentdirector/save-result',
            data: $scope.game
        }).then(function successCallback(response) {
            $scope.callAll();
        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });

    };
    $scope.startPossibleGames = function () {
        $http({
            method: 'GET',
            url: '/api/tournamentdirector/start-possible-games'
        }).then(function successCallback(response) {
            $scope.callAll();
        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });
    };

    $scope.saveTableCount = function () {
        $http({
            method: 'POST',
            url: '/api/tournamentdirector/save-table-count',
            data: $scope.tablecount
        }).then(function successCallback(response) {
            $scope.callAll();
        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });

    };

    $scope.callAll();

    $scope.range = function (min, max, step) {
        step = step || 1;
        var input = [];
        for (var i = min; i <= max; i += step) {
            input.push(i);
        }
        return input;
    };

}).controller('ModalInstanceCtrl', function ($scope, $uibModalInstance) {


    function calcResult(shortResult) {
        gameset = {};
        var sr = parseInt(shortResult);
        if (sr > 0) {
            if (sr < 10) {
                gameset.points1 = 11;
            }
            else {
                gameset.points1 = sr + 2;
            }
            gameset.points2 = sr;
        }
        if (sr < 0) {
            gameset.points1 = -1 * sr;
            if (sr > -9) {
                gameset.points2 = 11;
            } else {
                gameset.points2 = gameset.points1 + 2;

            }
        }
        return gameset;
    }

    $scope.$watch('gameResult', function () {
            var res = $scope.gameResult.split(" ");
            var arrayLength = res.length;
            var sets = [];
            if (arrayLength < 1 || res[0] == '')
                return;

            for (var i = 0; i < arrayLength; i++) {
                if (res[i].charAt(0) == '-') {
                    sets.push(calcResult(res[i]))

                } else {
                    sets.push(calcResult(res[i]))
                }
            }
            $scope.sets = sets;
        }
    );

    $scope.ok = function () {
        $uibModalInstance.close($scope.sets);
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

