/*
 * Copyright (c) 2016-2018
 * J. Melzer
 */

angular.module('jitty.settings.controllers', []).controller('TableSettingsController', function ($scope, dragulaService, Player,
                                                                                                 Flash, FileUploader, $http) {
    $scope.many2 = ['Tische 1', 'them'];
    $scope.many = ['1', '2', '3', '4'];
    $scope.$on('table-bag.drag', function (e, el) {
        console.log(e);
        // console.log(e1);
        el.removeClass('ex-moved');
    });
    $scope.$on('table-bag.drop', function (e, el) {
        console.log(e);
        // console.log(e1);
        el.addClass('ex-moved');
    });
    $scope.$on('table-bag.drop-model', function (el, target, source) {
        console.log('drop-model:');
        console.log(target);
        // console.log(e1);
    });
    // $scope.targetTable = new Array(9);
    $scope.tableCols = 4;
    $scope.number = 5;
    $scope.tableNr = 20;
    $scope.tableRows = 6;
    // $scope.tableRowsA = new Array(9);
    $scope.tables = [];
    // $scope.tables.push([]);
    // $scope.tables.push([]);
    //
    $scope.getNumber = function (num) {
        return new Array(num);
    };
    //
    $scope.calc = function () {
        sum = 0;
        $scope.tables = [];
        for (i = 0; i < $scope.tableCols; i++) {
            $scope.tables[i] = [];
            for (j = 0; j < $scope.tableRows; j++) {
                sum++;
                $scope.tables[i].push({name: ('Tisch #' + sum), column: i, row: j})
            }
        }
    };
    $scope.calc();
    //
    // $scope.models = {
    //     selected: null,
    //     lists: {"A": [], "B": []}
    // };
    // $scope.dropCallback = function (index, item, external, type) {
    //     $scope.logListEvent('dropped at', index, item, external, type);
    //     // Return false here to cancel drop. Return true if you insert the item yourself.
    //     if ($scope.models.lists.B.length > 0)
    //         return false;
    //
    //     return item;
    // };
    //
    // $scope.logListEvent = function (action, index, item, external, type) {
    //     var message = external ? 'External ' : '';
    //     message += ' element was ' + action + ' position ' + index;
    //     // $scope.models.lists.B.push()
    //     console.log(message);
    //     console.log(item);
    //     console.log($scope.models.lists.B.length);
    // };
    // // Generate initial model
    // for (var i = 1; i <= 10; ++i) {
    //     $scope.models.lists.A.push({label: "Tisch #" + i});
    //     // $scope.models.lists.B.push({label: "Item B" + i});
    // }
    //
    // // Model to JSON for demo purpose
    // $scope.$watch('tables', function (model) {
    //     $scope.modelAsJson = angular.toJson(model, true);
    // }, true);
    //
    // $scope.$watch('tableCols', function (model) {
    //     $scope.calc();
    // }, true);
    // $scope.$watch('tableRows', function (model) {
    //     $scope.calc();
    // }, true);


});

