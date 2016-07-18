angular.module('jitty.player.controllers', []).controller('PlayerListController', function ($scope, $window, Player, $http, $location) {

    $scope.reverse = true;

    $scope.players = Player.query();


    $scope.createNewPlayer = function () {
        $window.location.href = '/#/player-add';
    };

    $scope.order = function (predicate) {
        $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
        $scope.predicate = predicate;
    };

}).controller('PlayerEditController', function ($scope, $routeParams, Player, $location, $http, popupService, $window) {
        $scope.id = $routeParams.id;

        $scope.player = {};


        $scope.getPlayer = function () {
            $scope.player = Player.get({id: $routeParams.id}, function () {
                console.log('Player got successful');
            })
        };
        $scope.getPlayer();

        $scope.savePlayer = function () {
            if ($scope.playerForm.$valid) {
                Player.save($scope.player, function () {
                    console.log('Player saved successful');
                    $scope.players = Player.query();
                    $location.path('/players');
                });
            }
        };

    }
).controller('PlayerCreateController', function ($scope, $routeParams, Player, $location, $http) {


    $scope.savePlayer = function () {
        if ($scope.playerForm.$valid) {
            Player.save($scope.player, function () {
                console.log('Player saved successful');
                $scope.players = Player.query();
                $location.path('/players');
            });
        }
    };


});

