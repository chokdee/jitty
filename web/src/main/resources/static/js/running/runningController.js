angular.module('jitty.running.controllers', []).controller('RunningController', function ($scope, $http, $routeParams) {
    $scope.getPossibleGames = function () {
        $http.get('/api/draw/possible-games', {}).then(function (response) {
            $scope.possibleGames = response.data;
            $scope.gridOptions.data = response.data;
        });

    };
    $scope.getPossibleGames();

    $scope.startGame = function (id) {
        $http.get('/api/draw/start-game?id='+id, {}).then(function (response) {
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

    $scope.showMe = function(v){
        alert(v);
    };

    $scope.columns = [{ field: 'group.tournamentClass.name' ,  displayName: 'Klasse'},
        {field: 'group.name', displayName: 'Gruppe'},
        {field: 'player1.fullName', displayName: 'Spieler 1'},
        {field: 'player2.fullName', displayName: 'Spieler 2'},
        {
            name:'Aktion',
            cellTemplate: '<div class="ui-grid-cell-contents"><a class="btn btn-primary" ng-click="grid.appScope.startGame(row.entity.id)" >Spiel starten</a></div>',
            enableCellEdit : false
        }
    ];

    $scope.gridOptions = {
        enableSorting: true,
        rowHeight:40,
        columnDefs: $scope.columns
    };

    $scope.columnsRunning = [{ field: 'group.tournamentClass.name' ,  displayName: 'Klasse'},
        {field: 'group.name', displayName: 'Gruppe'},
        {field: 'player1.fullName', displayName: 'Spieler 1'},
        {field: 'player2.fullName', displayName: 'Spieler 2'},
        {
            name:'Aktion',
            cellTemplate: '<div class="ui-grid-cell-contents"><a class="btn btn-primary" ng-click="grid.appScope.result(row.entity.id)" >Ergebnis...</a></div>',
            enableCellEdit : false
        }
    ];

    $scope.gridOptionsRunning = {
        enableSorting: true,
        rowHeight:40,
        columnDefs: $scope.columnsRunning
    };
});

