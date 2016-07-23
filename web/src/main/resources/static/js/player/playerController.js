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

}).controller('PlayerEditController', function ($scope, $routeParams, Player, $location, $http, Club, Association, TournamentClass) {
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


        $scope.getPossibleClasses = function () {
            $http.get('/api/players/possible-tournaments-classes', {params: {id: $routeParams.id}}).then(function(response) {
                $scope.possibleClasses = response.data;
            });

        };

    $scope.getPossibleClasses();

    $scope.selectedA = [];
    $scope.selectedB = [];

    $scope.selectA = function(i) {
        $scope.selectedA.push(i);
    };

    $scope.selectB = function(i) {
        $scope.selectedB.push(i);
    };

        $scope.settings = {
            bootstrap2: false,
            filterClear: 'Show all!',
            filterPlaceHolder: 'Filter!',
            moveSelectedLabel: 'Move selected only',
            moveAllLabel: 'Move all!',
            removeSelectedLabel: 'Remove selected only',
            removeAllLabel: 'Remove all!',
            moveOnSelect: true,
            preserveSelection: 'moved',
            selectedListLabel: 'Angemeldet',
            nonSelectedListLabel: 'Mögliche Klassen',
            postfix: '_helperz',
            selectMinHeight: 130,
            filter: false,
            filterNonSelected: '1',
            filterSelected: '4',
            infoAll: ' ',
            infoFiltered: '<span class="label label-warning">Filtered</span> {0} from {1}!',
            infoEmpty: ' ',
            filterValues: false
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

