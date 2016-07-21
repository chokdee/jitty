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

}).controller('PlayerEditController', function ($scope, $routeParams, Player, $location, $http, Club, Association) {
        $scope.id = $routeParams.id;

        $scope.player = {};
        $scope.association = {};

        $scope.birthdate = {};
        $scope.clubs = Club.query();
        $scope.associations = Association.query();

        $scope.getPlayer = function () {
            $scope.player = Player.get({id: $routeParams.id}, function () {
                $scope.birthdate = new Date($scope.player.birthday);
                console.log('got Player ' + $routeParams.id + ' successfully');
            })
        };
        $scope.getPlayer();

        $scope.savePlayer = function () {
            if ($scope.playerForm.$valid) {
                $scope.player.birthday = $scope.birthdate;
                Player.save($scope.player, function () {
                    console.log('Player saved successful');
                    //$scope.players = Player.query();
                    $location.path('/players');
                    //$scope.back();
                });
            }
        };
        $scope.formats = ['dd.MM.yyyy'];
        $scope.format = $scope.formats[0];
        $scope.dateOptions = {
            dateDisabled: false,
            formatYear: 'yy',
            maxDate: new Date(),
            minDate: new Date(1900, 1, 1),
            startingDay: 1
        };

        $scope.open1 = function () {
            $scope.popup1.opened = true;
        };
        $scope.popup1 = {
            opened: false
        };


    }
).controller('PlayerCreateController', function ($scope, $routeParams, Player, $location, $http) {

    $scope.dateOptions = {
        dateDisabled: false,
        formatYear: 'yy',
        maxDate: new Date(),
        minDate: new Date(2000, 1, 1),
        startingDay: 1
    };

    $scope.open1 = function () {
        $scope.popup1.opened = true;
    };
    $scope.popup1 = {
        opened: false
    };


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

