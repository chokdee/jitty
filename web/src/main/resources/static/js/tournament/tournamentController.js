angular.module('jitty.tournament.controllers', []).controller('TournamentListController', function ($scope, $window, Tournament, $http, $location) {

    $scope.predicate = 'startDate';
    $scope.reverse = true;

    $scope.tournaments = Tournament.query();


    $scope.createNewTournament = function () {
        $window.location.href = '/#/tournament-add';
    };

    $scope.order = function (predicate) {
        $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
        $scope.predicate = predicate;
    };

    $scope.selectTournament = function (tid) {
        $http.get('http://localhost:8080/api/tournaments/actual/' + tid).
        success(function(data) {
            console.log('successful selected tournament ');
            $scope.tournamentname = data.name;
            $window.location.href = '/';
        });

    };
}).controller('TournamentEditController', function ($scope, $routeParams, Tournament, $location, $http) {

    //$scope.tournament = Tournament.get({id: $routeParams.id});

    $scope.formats = ['dd.MM.yyyy'];
    $scope.format = $scope.formats[0];
    $scope.altInputFormats = ['d!/M!/yyyy'];

        //$scope.tournament = new Object();
        //$scope.tournament.startDate = new Date();
        //$scope.tournament.name = 'test1';
        //$scope.tournament.id = 1;
    $scope.tournament = Tournament.get({id: $routeParams.id});

    $scope.dateOptions = {
        dateDisabled: false,
        formatYear: 'yy',
        maxDate: new Date(2020, 5, 22),
        minDate: new Date(),
        startingDay: 1
    };

    $scope.open1 = function() {
        $scope.popup1.opened = true;
    };
    $scope.popup1 = {
        opened: false
    };

    $scope.saveTournament = function () {
        if ($scope.tournamentForm.$valid) {
            Tournament.save($scope.tournament, function () {
                console.log('Tournament saved successful');
                $scope.tournaments = Tournament.query();
                $location.path('/tournaments');
            });
        }
    };


});