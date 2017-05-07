/*
 * Copyright (c) 2017.
 * J. Melzer
 */

angular.module('jitty.running.controllers', []).controller('RunningController', function ($scope, $http, $uibModal, $timeout, $log, printer, uiGridConstants) {
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
            printer.print('/components/running/sr.html', {game: response.data});
        });
    };


    $scope.getRunningGames = function () {
        $http.get('/api/tournamentdirector/running-games', {}).then(function (response) {
            $scope.runningGames = response.data;
            $scope.gridOptionsRunning.data = response.data;
            $timeout(function () {
                $scope.addEL();
            });
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
    $scope.columns = [
        {
            field: 'id',
            maxWidth: 0,
            displayName: 'ID',
            cellTemplate: '<div class="ui-grid-cell-contents" id="{{ COL_FIELD }}">{{ COL_FIELD }}</div>'
        }, {
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


    function handleDrop(e) {
        // this / e.target is current target element.
        if (e.stopPropagation) {
            e.stopPropagation(); // stops the browser from redirecting.
        }
        console.log(e.dataTransfer.getData('Text'));
        // See the section on the DataTransfer object.
        return false;
    }

    function handleDragEnd(e) {
        // this/e.target is the source node.
        // [].forEach.call(cols, function (col) {
        //     col.classList.remove('over');
        // });
        console.log(e.dataTransfer.getData('Text'));
    }

    function handleDragStart(e) {
        this.style.opacity = '0.4';  // this / e.target is the source node.

        e.dataTransfer.effectAllowed = 'move';
        e.dataTransfer.setData('Text', e.currentTarget.firstChild.id);
    }

    $scope.addEL = function () {
        var cols = document.querySelectorAll('div.ui-grid-cell');
        [].forEach.call(cols, function (col) {
            col.addEventListener('dragstart', handleDragStart, false);
            col.addEventListener('drop', handleDrop, false);
            col.addEventListener('dragend', handleDragEnd, false);

        });
    };


    $scope.gridOptions = {
        enableSorting: true,
        enableFiltering: true,
        rowHeight: 40,
        columnDefs: $scope.columns,
        rowTemplate: "<div draggable=\"true\"   ng-repeat=\"(colRenderIndex, col) in colContainer.renderedColumns \" " +
        "style=\"cursor: move;\" class=\"ui-grid-cell\" ng-class=\"{ 'ui-grid-row-header-cell': col.isRowHeader }\" ui-grid-cell></div>"
        // rowTemplate: '<div ng-style="{\'cursor\': row.cursor, \'z-index\': col.zIndex() }" ng-repeat="col in renderedColumns" ng-class="col.colIndex()" class="ngCell {{col.cellClass}}" ng-cell></div>'
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

        modalInstance.result.then(function (game) {
            $scope.game.sets = game.sets;
            $scope.game.winByDefault = game.winByDefault;
            if (game.winReason != undefined)
                $scope.game.winReason = game.winReason.id;
            $scope.saveGame();
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
            url: '/api/tournamentdirector/save-result/',
            data: $scope.game
        }).then(function successCallback(response) {
            console.log('save success:' + response.data);
            $scope.callAll();
        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
            console.log('errorMessage = ' + response.data.error);
        });

    };
    $scope.openGedueck = function (id) {
        $window.location.href = '/#/draw/' + id;
    }
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
    $scope.sets = '';
    $scope.gameResult = {sets: [], winByDefault: false, winReason: 0};
    $scope.winReasons = [{
        id: 1,
        label: 'Spieler 1 hat aufgegeben',
    }, {
        id: 2,
        label: 'Spieler 2 hat aufgegeben',
    }];

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

    $scope.$watch('gameResult.sets', function () {
        var res = $scope.sets.split(" ");
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
        $scope.gameResult.sets = sets;
        }
    );

    $scope.ok = function () {
        $uibModalInstance.close($scope.gameResult);
    };

    $scope.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };
});

