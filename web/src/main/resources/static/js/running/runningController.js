angular.module('jitty.running.controllers', []).controller('RunningController', function ($scope, $http, $uibModal,$log) {
    $scope.getPossibleGames = function () {
        $http.get('/api/draw/possible-games', {}).then(function (response) {
            $scope.possibleGames = response.data;
            $scope.gridOptions.data = response.data;
        });

    };
    $scope.getPossibleGames();

    $scope.startGame = function (id) {
        $http.get('/api/draw/start-game?id=' + id, {}).then(function (response) {
            $scope.getPossibleGames();
            $scope.getRunningGames();
        });

    };

    $scope.getRunningGames = function () {
        $http.get('/api/draw/running-games', {}).then(function (response) {
            $scope.runningGames = response.data;
            $scope.gridOptionsRunning.data = response.data;
        });

    };
    $scope.getRunningGames();

    $scope.columns = [{field: 'group.tournamentClass.name', displayName: 'Klasse'},
        {field: 'group.name', displayName: 'Gruppe'},
        {field: 'player1.fullName', displayName: 'Spieler 1'},
        {field: 'player2.fullName', displayName: 'Spieler 2'},
        {
            name: 'Aktion',
            cellTemplate: '<div class="ui-grid-cell-contents"><a class="btn btn-primary" ng-click="grid.appScope.startGame(row.entity.id)" >Spiel starten</a></div>',
            enableCellEdit: false
        }
    ];

    $scope.gridOptions = {
        enableSorting: true,
        rowHeight: 40,
        columnDefs: $scope.columns
    };
    $scope.open = function (entity) {
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
           alert(gameset[0].points1);
            //todo safe the result in the db ab remove the game from the list
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };


    $scope.columnsRunning = [{field: 'group.tournamentClass.name', displayName: 'Klasse'},
        {field: 'group.name', displayName: 'Gruppe'},
        {field: 'player1.fullName', displayName: 'Spieler 1'},
        {field: 'player2.fullName', displayName: 'Spieler 2'},
        {
            name: 'Aktion',
            cellTemplate: '<div class="ui-grid-cell-contents"><a class="btn btn-primary" ng-click="grid.appScope.open(row.entity)" >Ergebnis...</a></div>',
            enableCellEdit: false
        }
    ];

    $scope.gridOptionsRunning = {
        enableSorting: true,
        rowHeight: 40,
        columnDefs: $scope.columnsRunning
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

