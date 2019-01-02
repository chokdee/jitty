/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */
function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    console.log(ev.target.innerHTML);
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop22(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    console.log(data);
    if (ev.target.innerHTML != null && !ev.target.innerHTML.startsWith("Tisch"))
        ev.target.innerHTML = null;
    ev.target.appendChild(document.getElementById(data));
}

angular.module('jitty.settings.controllers', []).controller('TableSettingsController', function ($scope, Flash, $http) {

    $scope.targetTable = [];
    $scope.tableCols = 4;
    $scope.number = 5;
    $scope.tableNr = 20;
    $scope.tableRows = 5;
    $scope.tableGrid = [];
    $scope.sourceTable = [];


    $scope.getNumber = function (num) {
        return new Array(num);
    };

    $scope.calcSourceTable = function () {
        $scope.sourceTable = [];
        for (i = 0; i < $scope.tableNr; i++) {
            $scope.sourceTable.push({nr: i})
        }
    };
    $scope.calcSourceTable();

    $scope.calc = function () {
        sum = 0;
        $scope.tableGrid = [];
        for (i = 0; i < $scope.tableRows; i++) {
            $scope.tableGrid[i] = [];
            for (j = 0; j < $scope.tableCols; j++) {
                sum++;
                $scope.tableGrid[i].push({name: ('Drop #' + sum), column: j, row: i})
            }
        }
    };

    $scope.loadTables = function () {
        $http.get('/api/settings/table-settings', {}).then(function (response) {
            $scope.tableGrid = response.data.tablePositions;
            $scope.tableNr = response.data.tableCount;
        });

    };

    // $scope.calc();
    $scope.loadTables();

    $scope.$watch('tableCols', function (model) {
        $scope.calc();
    }, true);
    $scope.$watch('tableRows', function (model) {
        $scope.calc();
    }, true);

    $scope.$watch('tableNr', function (model) {
        $scope.calcSourceTable();
    }, true);


});

