/*
 * Copyright (c) 2017.
 * J. Melzer
 */

angular.module('jitty.player.controllers', []).controller('PlayerListController', function ($scope, $window, Player,
                                                                                            Flash, FileUploader, $http) {

    $scope.reverse = true;

    $scope.players = Player.query();

    $scope.hasOnlyOneClass = false;
    $scope.assignWhileImport = true;

    $scope.getKoField = function (assignPlayer) {
        $http.get('/api/tournaments/has-only-one-class', {}).then(function (response) {
            $scope.hasOnlyOneClass = response.data;
        });

    };
    $scope.getKoField();

    $scope.createNewPlayer = function () {
        $window.location.href = '/#/player-add';
    };

    $scope.order = function (predicate) {
        $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
        $scope.predicate = predicate;
    };

    var uploader = $scope.uploader = new FileUploader( {
        url: '/api/players/import-from-click-tt?assignWhileImport=' + $scope.assignWhileImport,
        autoUpload:true,
        removeAfterUpload:true,
        queueLimit:1
    });
    uploader.onCompleteAll = function() {
        console.info('onCompleteAll');
        $scope.players = Player.query();
    };
    uploader.onCompleteItem = function(fileItem, response, status, headers) {
        Flash.create('success', response, 4000, {container: 'flash-status'});
    };

    $scope.deletePlayer = function (id) {
        $http({
            method: 'DELETE',
            url: '/api/players/' + id
        }).then(function successCallback(response) {
            //refresh data
            $scope.players = Player.query();
            Flash.create('success', 'Der Spieler mit der Id ' + id + ' wurde gel\u00F6scht.', 4000, {container: 'flash-status'});

        }, function errorCallback(response) {
            if (response.status === 400) {
                Flash.create('danger', response.data.error, 4000, {container: 'flash-status'});
                console.log('got 400 response message = ' + response.data.error);
            }
        });

    };

}).controller('PlayerEditController', function ($scope, $routeParams, Player, $location, $http, Club, Association, Flash) {
        $scope.id = $routeParams.id;

        $scope.player = {};
        $scope.association = {};

        $scope.birthdate = null;
        $scope.clubs = Club.query();
        $scope.associations = Association.query();

        $scope.getPlayer = function () {
            $scope.player = Player.get({id: $routeParams.id}, function () {
                $scope.birthdate = new Date($scope.player.birthday);
                console.log('got Player ' + $routeParams.id + ' successfully');
            })
        };
        if ($routeParams.id != null)
            $scope.getPlayer();

        $scope.savePlayer = function () {
            if ($scope.playerForm.$valid) {
                $scope.player.birthday = $scope.birthdate;
                Player.save($scope.player, function () {
                    console.log('Player saved successful');
                    Flash.create('success', 'Der Spieler wurde erfolgreich gespeichert.', 4000, {container: 'flash-status'});
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
            $http({
                method: 'POST',
                url: '/api/players/possible-tournaments-classes',
                data: $scope.player
            }).then(function successCallback(response) {
                $scope.possibleClasses = response.data;
            }, function errorCallback(response) {
                $scope.errorMessage = response.data.error;
            });

        };

        $scope.$watch('player.qttr', function () {
                $scope.getPossibleClasses();
            }
        );

        $scope.getPossibleClasses();

        $scope.selectedA = [];
        $scope.selectedB = [];

        $scope.selectA = function (i) {
            $scope.selectedA.push(i);
        };

        $scope.selectB = function (i) {
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
                Flash.create('success', 'Der Spieler wurde erfolgreich gespeichert.', 4000, {container: 'flash-status'});
                $scope.players = Player.query();
                $location.path('/players');
            });
        }
    };


});

