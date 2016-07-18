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
        $http.get('http://localhost:8080/api/tournaments/actual/' + tid).success(function (data) {
            console.log('successful selected tournament ');
            $scope.tournamentname = data.name;
            $window.location.href = '/';
        });

    };

}).controller('TournamentEditController', function ($scope, $routeParams, Tournament, $location, $http, popupService, $window) {
        $scope.id = $routeParams.id;

        $scope.formats = ['dd.MM.yyyy'];
        $scope.format = $scope.formats[0];
        $scope.altInputFormats = ['d!/M!/yyyy'];

        $scope.tournament = {};


        $scope.getTournament = function () {
            $scope.tournament = Tournament.get({id: $routeParams.id}, function () {
                console.log('Tournament got successful');
                $scope.startDate = new Date($scope.tournament.startDate);
                $scope.endDate = new Date($scope.tournament.endDate);
            })
        };
        $scope.getTournament();

        $scope.dateOptions = {
            dateDisabled: false,
            formatYear: 'yy',
            maxDate: new Date(2020, 5, 22),
            minDate: new Date(2014, 1, 1),
            startingDay: 1
        };

        $scope.open1 = function () {
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

        $scope.starDate = 'bla';
        $scope.deleteClass = function (cid) {
            if (popupService.showPopup('Möchten Sie die Turnierklasse wirklich löschen?')) {
                $http({
                    method: 'DELETE',
                    url: 'http://localhost:8080/api/tournament-classes/' + cid
                }).then(function successCallback(response) {
                    //refresh data
                    $scope.getTournament();

                }, function errorCallback(response) {
                    if (response.status == 400) {
                        $scope.errorMessage = response.data.error;
                        console.log('got 400 response message = ' + response.data.error);
                    }
                });

            }
        };

        $scope.createNewTournamentClass = function () {
            $window.location.href = '/#/tournament/' + $scope.id + '/tournament-classes-add';
        };
    }
).controller('TournamentCreateController', function ($scope, $routeParams, Tournament, $location, $http) {


    $scope.formats = ['dd.MM.yyyy'];
    $scope.format = $scope.formats[0];
    $scope.altInputFormats = ['d!/M!/yyyy'];

    $scope.startDate = new Date();
    var tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    $scope.endDate = tomorrow;

    $scope.dateOptions = {
        dateDisabled: false,
        formatYear: 'yy',
        maxDate: new Date(2020, 5, 22),
        minDate: new Date(2014, 5, 22),
        startingDay: 1
    };

    $scope.open1 = function () {
        $scope.popup1.opened = true;
    };
    $scope.popup1 = {
        opened: false
    };
    $scope.open2 = function () {
        $scope.popup2.opened = true;
    };
    $scope.popup2 = {
        opened: false
    };
    $scope.saveTournament = function () {
        if ($scope.tournamentForm.$valid) {
            $scope.tournament.startDate = $scope.startDate;
            $scope.tournament.endDate = $scope.endDate;
            Tournament.save($scope.tournament, function () {
                console.log('Tournament saved successful');
                $scope.tournaments = Tournament.query();
                $location.path('/tournaments');
            });
        }
    };


}).controller('TournamentClassCreateController', function ($scope, $routeParams, popupService, TournamentClass, Tournament, $location, $http) {
    $scope.tournamentClass = {startTTR: 0};

    $scope.saveTournamentClass = function () {
        if ($scope.tournamentClassForm.$valid) {

            $http({
                method: 'POST',
                url: 'http://localhost:8080/api/tournament-classes/' + $routeParams.id,
                data: $scope.tournamentClass
            }).then(function successCallback(response) {
                //refresh data
                //$scope.getTournament();
                $location.path('/tournaments/' + $routeParams.id);

            }, function errorCallback(response) {
                $scope.errorMessage = response.data.error;
            });


        }
    };
}).controller('TournamentClassEditController', function ($scope, $routeParams, TournamentClass, $location, $http, popupService, $window) {
        $scope.tournamentClass = {};


        $scope.getTournamentClass = function () {
            $scope.tournamentClass = TournamentClass.get({id: $routeParams.id}, function () {
                console.log('Got TournamentClass successful');
            })
        };
        $scope.getTournamentClass();

        $scope.saveTournamentClass = function () {
            if ($scope.tournamentClassForm.$valid) {
                TournamentClass.save($scope.tournamentClass, function () {
                    console.log('TournamentClass saved successful');
                    //$location.path('/tournaments' + $scope.id);
                    $scope.back();
                });
            }
        };
    }
);

