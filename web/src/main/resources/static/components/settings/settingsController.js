/*
 * Copyright (c) 2018.
 * J. Melzer
 */

angular.module('jitty.settings.controllers', []).controller('TableSettingsController', function ($scope, $window, Player,
                                                                                                 Flash, FileUploader, $http) {
    $scope.tables = [];
    $scope.tables.push([]);
    $scope.tables.push([]);
    for (i = 1; i < 11; i++) {
        $scope.tables[0].push({name: ('Tisch #' + i), column: i})
    }
    for (j = 0; j < 9; j++) {
        $scope.tables[1].push({name: ('Tisch #' + i++), column: j})
    }
    $scope.models = {
        selected: null,
        lists: {"A": [], "B": []}
    };
    $scope.dropCallback = function (index, item, external, type) {
        $scope.logListEvent('dropped at', index, external, type);
        // Return false here to cancel drop. Return true if you insert the item yourself.
        if ($scope.models.lists.B.length > 0)
            return false;

        return item;
    };

    $scope.logListEvent = function (action, index, external, type) {
        var message = external ? 'External ' : '';
        message += ' element was ' + action + ' position ' + index;
        console.log(message);
        console.log($scope.models.lists.B.length);
    };
    // Generate initial model
    for (var i = 1; i <= 10; ++i) {
        $scope.models.lists.A.push({label: "Tisch #" + i});
        // $scope.models.lists.B.push({label: "Item B" + i});
    }

    // Model to JSON for demo purpose
    $scope.$watch('tables', function (model) {
        $scope.modelAsJson = angular.toJson(model, true);
    }, true);
});

