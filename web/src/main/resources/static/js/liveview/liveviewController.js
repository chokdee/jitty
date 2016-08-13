angular.module('jitty.liveview.controllers', []).controller('LiveviewController', function ($scope, $http, $uibModal, $log) {

    $scope.getPossibleClasses = function () {
        $http.get('/api/tournament-classes/running', {}).then(function (response) {
            $scope.possibleClasses = response.data;
        });

    };
});

