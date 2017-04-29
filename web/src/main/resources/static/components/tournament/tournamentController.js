/*
 * Copyright (c) 2017.
 * J. Melzer
 */

angular.module('jitty.tournament.controllers', []).controller('TournamentListController', function ($scope, $window, Flash, Tournament, $http, $location) {

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

    $scope.selectTournament = function (tid, name) {
        $http.get('/api/tournaments/actual/' + tid).success(function () {
            console.log('successful selected tournament ');
            Flash.create('success', 'Das Turnier ' + name + ' wurde als aktuelles Turnier festgelegt.', 4000, {container: 'flash-status'});
            $scope.tournamentname = name;
            $window.location.href = '/#/';
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

        $scope.deleteClass = function (cid) {
            $http({
                method: 'DELETE',
                url: '/api/tournament-classes/' + cid
            }).then(function successCallback(response) {
                //refresh data
                $scope.getTournament();

            }, function errorCallback(response) {
                if (response.status == 400) {
                    $scope.errorMessage = response.data.error;
                    console.log('got 400 response message = ' + response.data.error);
                }
            });

        };

        $scope.createNewTournamentClass = function () {
            $window.location.href = '/#/tournament/' + $scope.id + '/tournament-classes-add';
        };
    }
).controller('TournamentCreateController', function ($scope, $routeParams, Tournament, $location, $http, Flash) {
    var vm = this;
    $scope.types = [{
        id: 0,
        label: ''
    }, {
        id: 1,
        label: 'Allg. Turnier (z.B. Gruppe / KO Runde etc.'
    }, {
        id: 2,
        label: 'Cup Turnier (VR Cup, WTTV Cup etc.)'
    }, {
        id: 3,
        label: 'Ranglistenturnier (not supported yet)'
    }
    ];
    $scope.tournament = undefined;
    vm.tournamenttype = {};
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
        if (vm.tournamenttype == undefined || vm.tournamenttype.selected == undefined || vm.tournamenttype.selected.id < 1) {
            Flash.create('danger', 'Bitte Turniersystem auswählen:' + vm.tournamenttype, 4000, {container: 'flash-status'});
            return;
        }
        if ($scope.tournamentForm.$valid) {
            $scope.tournament.startDate = $scope.startDate;
            $scope.tournament.endDate = $scope.endDate;
            $scope.tournament.type = vm.tournamenttype.selected.id;
            $http({
                method: 'POST',
                url: '/api/tournaments/',
                data: $scope.tournament
            }).then(function successCallback(response) {
                Flash.create('success', 'Das Turnier wurde gespeichert, bitte noch die Klassen bearbeiten.', 4000, {container: 'flash-status'});
                $location.path('/tournaments/' + response.data);
            }, function errorCallback(response) {
            });
        }
    };


}).controller('TournamentClassCreateController', function ($scope, $routeParams, popupService, TournamentClass, Tournament, $location, $http) {
    $scope.tournamentClass = {startTTR: 0};

    $scope.saveTournamentClass = function () {
        if ($scope.tournamentClassForm.$valid) {

            $http({
                method: 'POST',
                url: '/api/tournament-classes/' + $routeParams.id,
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

        $scope.getTournamentSystemType = function () {
            $http.get('/api/tournaments/system-types', {}).then(function (response) {
                $scope.systems = response.data;
                $scope.getTournamentClass();
            });
        };
        $scope.getTournamentSystemType();

        $scope.canEdit = true;
        $scope.system = {};

        $scope.getTournamentClass = function () {
            $scope.tournamentClass = TournamentClass.get({id: $routeParams.id}, function () {
                console.log('Got TournamentClass successful');
                $scope.system.selected = $scope.selectSystem($scope.tournamentClass.systemType);
                console.log($scope.system.selected);

            });
            if ($scope.tournamentClass.running == true)
                $scope.canEdit = false;
        };


        $scope.selectSystem = function (val) {
            for (var i = 0; i < $scope.systems.length; i++) {
                var obj = $scope.systems[i];
                if (obj.value == val) {
                    return obj;
                }
            }
        }
        $scope.saveTournamentClass = function () {
            if ($scope.tournamentClassForm.$valid) {
                if ($scope.system.selected != null)
                    $scope.tournamentClass.systemType = $scope.system.selected.value;

                TournamentClass.save($scope.tournamentClass, function () {
                    console.log('TournamentClass saved successful');
                    //$location.path('/tournaments' + $scope.id);
                    $scope.back();
                });
            }
        };
    }
);

