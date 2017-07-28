/*
 * Copyright (c) 2017.
 * J. Melzer
 */

angular.module('jitty.draw.controllers', []).controller('DrawController', function ($scope, $http, $window, $routeParams) {

    $scope.translateStatus = function (s) {
        switch (s) {
            case "NOTSTARTED":
                return "nicht gestartet";
            case "PHASE1_STARTED_NOT_CALLED":
                return "Phase 1 läuft. Auslosung noch änderbar";
            case "PHASE1_FINISHED":
                return "Phase 1 beendet";
            case "PHASE2_STARTED_NOT_CALLED":
                return "Phase 2 läuft. Auslosung noch änderbar";
            case "PHASE1_AND_RESULTS":
                return "Phase 1 läuft";
            case "PHASE2_AND_RESULTS":
                return "Phase 2 läuft";
            case "SWISS_PHASE_RUNNING":
                return "begonnen";
            case "FINISHED":
                return "Beendet";
            default:
                return "unbekannt";
        }
    };


    $scope.getPossibleClasses = function () {
        $http.get('/api/tournament-classes/classes-with-status', {}).then(function (response) {
            $scope.possibleClasses = response.data;

            var pc = $scope.possibleClasses;
            for (i = 0; i < pc.length; i++) {

                switch (pc[i].status) {
                    case 'PHASE1_STARTED_NOT_CALLED':
                        pc[i].confirmMsg = 'Die Phase 1 hat bereits begonnen. Sicher?';
                        pc[i].confirm = true;
                        break;
                    case 'PHASE2_STARTED_NOT_CALLED':
                        pc[i].confirmMsg = 'Die Phase 2 hat bereits begonnen. Sicher?';
                        pc[i].confirm = true;
                        break;
                    case 'PHASE1_AND_RESULTS':
                    case 'PHASE2_AND_RESULTS':
                    case 'SWISS_PHASE_RUNNING':
                        pc[i].confirmMsg = 'Es gibt bereits Ergebnisse. Alle Ergebnisse werden gelöscht! Wirklich sicher?';
                        pc[i].confirm = true;
                        break;

                    default:
                        pc[i].confirm = false;
                }
            }
        });

    };

    $scope.selectClass = function (clz) {
        if (clz.confirm) {
            $window.location.href = '#/draw/' + clz.id;
            return;
        }
        $http({
            method: 'GET',
            url: '/api/draw/start?cid=' + clz.id

        }).then(function successCallback(response) {
            $window.location.href = '#/draw/' + clz.id;

        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
            $window.location.href = '#/draw/' + clz.id;
        });

    };

    $scope.$watch('modus', function () {
        $scope.selectPhaseCombination();
    });
    $scope.selectPhaseCombination = function () {
        if ($scope.modus != null && $routeParams.id != null)
            $http.get('/api/draw/select-phase-combination?cid=' + $routeParams.id + '&type=' + $scope.modus.id, {}).then(function () {
                console.log('Phase successfully selected')
                $scope.getTournamentClass();
            });

    };


    if ($routeParams.id == null)
        $scope.getPossibleClasses();


}).controller('DrawEditController', function ($scope, $http, $routeParams, $window, TournamentClass) {

    $scope.modi = [{
        id: undefined,
        label: ''
    }, {
        id: 1,
        label: 'VR Gruppe, ER KO-Runde'
    },
        // {
        // id: 2,
        // label: 'KO-Runde'}
    ];
    $scope.modus = {id: undefined};//$scope.modi[1];
    $scope.selectedPhase = -10;

    $scope.loadPart = function (index) {
        $scope.templateurl = '';
        if ($scope.modus != null && $scope.modus.id != undefined && $scope.modus.id === 1) {
            if (index === 0) {
                $scope.templateurl = 'components/draw/groups.html';
            }
            else if (index === 1) {
                $scope.templateurl = 'components/draw/bracket.html';
            }
        }
        console.log('modus = ' + $scope.modus);
        console.log('loadpart = ' + $scope.templateurl);
    };

    $scope.$watch('selectedPhase', function () {
        if ($scope.selectedPhase !== null)
            $scope.loadPart($scope.selectedPhase.counter);

    });

    $scope.getTournamentClass = function () {
        if ($scope.tournamentClass == null) {
            $scope.tournamentClass = TournamentClass.get({id: $routeParams.id}, function () {
                    console.log('Got TournamentClass successful');
                    $scope.modus = {id: $scope.tournamentClass.systemType};
                    $scope.loadPart();
                    $scope.getActualPhase();

                    if ($scope.tournamentClass.playerPerGroup === null)
                        $scope.tournamentClass.playerPerGroup = 4; //default


                    if ($scope.tournamentClass.running) {
                        $scope.templateurl = 'components/draw/bracket.html';
                    }
                    if ($scope.tournamentClass.system !== undefined && $scope.tournamentClass.system.phases !== null) {
                        for (i = 0; i < $scope.tournamentClass.system.phases.length; i++) {
                            if ($scope.tournamentClass.activePhaseNo === i) {
                                $scope.selectedPhase = $scope.tournamentClass.system.phases[i];
                            }
                            $scope.tournamentClass.system.phases[i].counter = i;
                        }
                    }


                }
            );
        }
    }
    ;


    $scope.getActualPhase = function () {
        $http.get('/api/draw/actual-phase?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.phase = response.data;
            $scope.groups = $scope.phase.groups;
        });
    };

    if ($routeParams.id != null) {
        $scope.getTournamentClass();

    }


    $scope.$watch('modus', function () {
        $scope.selectPhaseCombination();
    });

    $scope.selectPhaseCombination = function () {
        if ($scope.modus != undefined && $routeParams.id != null)
            $http.get('/api/draw/select-phase-combination?cid=' + $routeParams.id + '&type=' + $scope.modus.id, {}).then(function (response) {
                console.log('Phase successfully selected')
                $scope.getTournamentClass();
            });

    };


}).controller('SwissSystemController', function ($scope, $http, $routeParams, $window, uiGridConstants, TournamentClass, Flash) {
        var gridApi;
    var gridApi2;

        $scope.playerColumns = [
            {
                field: 'fullName',
                maxWidth: 150,
                displayName: 'Name',
            }, {
                field: 'qttr',
                maxWidth: 100,
                displayName: 'Q-TTR',
            }, {
                field: 'wonGames',
                maxWidth: 100,
                displayName: 'Gewonnen',
            }, {
                field: 'buchholzZahl',
                maxWidth: 100,
                displayName: 'Buchholz',
            }
        ];
    $scope.title = "I 'm a tooltip!";
    $scope.gridPlayerList = {
            enableSorting: false,
            rowHeight: 40,
            columnDefs: $scope.playerColumns,
            data: [],
        rowTemplate: '<div ng-class="{\'suspended-row\':row.entity.suspended===true}" title="{{row.entity.suspendedText}}" ng-click="grid.appScope.fnOne(row)" ' +
        'ng-repeat="col in colContainer.renderedColumns track by col.colDef.name" class="ui-grid-cell" ui-grid-cell>' +
        '</div>',

            onRegisterApi: function onRegisterApi(registeredApi) {
                gridApi = registeredApi;
            }
        };

        $scope.getPossibleSwissPlayer = function () {
            $http.get('/api/draw/possible-player-swiss-system?cid=' + $routeParams.id, {}).then(function (response) {
                // $scope.players = response.data;
                for (var i in response.data) {
                    $scope.gridPlayerList.data.push(response.data[i]);
                }
                // $scope.myData = response.data;
                gridApi.core.handleWindowResize();
            });
        };

        $scope.gameColumns = [
            {
                field: 'player1.fullName',
                maxWidth: 150,
                displayName: 'Spieler 1',
            }, {
                field: 'player1.qttr',
                maxWidth: 100,
                displayName: 'Q-TTR'
            }, {
                field: 'player1.wonGames',
                maxWidth: 50,
                displayName: 'Gew. '
            }, {
                field: 'player2.fullName',
                displayName: 'Spieler 2',
                maxWidth: 150
            }, {
                field: 'player2.qttr',
                maxWidth: 100,
                displayName: 'Q-TTR'
            }, {
                field: 'player2.wonGames',
                maxWidth: 50,
                displayName: 'Gew.'
            }
        ];
        $scope.gridGameList = {
            enableSorting: false,
            rowHeight: 40,
            columnDefs: $scope.gameColumns,
            onRegisterApi: function onRegisterApi(registeredApi) {
                gridApi2 = registeredApi;
            }
        };

        $scope.swissDraw = function () {
            $http({
                method: 'GET',
                url: '/api/draw/swiss-draw?cid=' + $routeParams.id,
            }).then(function successCallback(response) {
                $scope.gridGameList.data = response.data.games;
                $scope.freilos = response.data.freilos;
                gridApi2.core.handleWindowResize();

            }, function errorCallback(response) {
                $scope.errorMessage = response.data.error;
            });

        };

    $scope.deleteRound = function () {
        $http({
            method: 'GET',
            url: '/api/draw/delete-round?cid=' + $routeParams.id,
        }).then(function successCallback(response) {
        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });

    };
        $scope.startSwissRound = function () {
            $http({
                method: 'POST',
                url: '/api/draw/start-swiss-round?cid=' + $routeParams.id + '&round=' + $scope.roundNr,
                data: $scope.gridGameList.data

            }).then(function successCallback(response) {
                $window.location.href = '/#/tournamentdirector/overview';

            }, function errorCallback(response) {
                $scope.errorMessage = response.data.error;
            });

        };

        $scope.createRoundIfNexcessary = function () {
            $http({
                method: 'GET',
                url: '/api/draw/create-next-swiss-round-if-necessary?cid=' + $routeParams.id + '&round=' + $scope.roundNr,

            }).then(function successCallback(response) {
                // $scope.roundNr = response.data;

            }, function errorCallback(response) {
                $scope.errorMessage = response.data.error;
            });

        };

        $scope.getRoundNr = function () {
            $http({
                method: 'GET',
                url: '/api/draw/swiss-round?cid=' + $routeParams.id,

            }).then(function successCallback(response) {
                $scope.roundNr = response.data;
                $scope.createRoundIfNexcessary();

            }, function errorCallback(response) {
                $scope.errorMessage = response.data.error;
            });

        };

        $scope.getTournamentClassFroSwiss = function () {
            $scope.tournamentClass = TournamentClass.get({id: $routeParams.id}, function () {
                    console.log('Got TournamentClass successful');


                    $scope.getRoundNr();
                    $scope.getPossibleSwissPlayer();
                }
            )
        };

        if ($routeParams.id !== null) {
            $scope.getTournamentClassFroSwiss();

        }


    }
).controller('GroupController', function ($scope, $http, $routeParams, $window, TournamentClass, Flash) {
    $scope.resultSize = 0;

    $scope.$watch('phase.groupCount', function () {
        $scope.createGroups();
    });

    $scope.displaySaveMessage = function () {
        var message = 'Die Gruppenauslosung wurde erfolgreich gespeichert';
        var id = Flash.create('success', message, 4000, {container: 'flash-status'});
    };


    $scope.getPossiblePlayerForGroups = function () {
        $http.get('/api/draw/possible-player-for-groups?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.players = response.data;
        });
    };
    if ($routeParams.id != null) {
        if ($scope.tournamentClass == null)
            $scope.getTournamentClass();

        $scope.getPossiblePlayerForGroups();
    }

    $scope.createDummyPlayer = function () {
        $http.get('/api/draw/dummy-player?cid=' + $routeParams.id, {}).then(function (response) {
        });
    };

    $scope.fillAll = function () {
        $scope.automaticDraw();
        after(1000, $scope.saveDraw());
        after(1000, $scope.start());
    };
    function after(ms, fn) {
        setTimeout(fn, ms);
    }


    $scope.automaticDraw = function () {
        $http({
            method: 'POST',
            url: '/api/draw/automatic-draw?cid=' + $routeParams.id,
            data: $scope.phase
        }).then(function successCallback(response) {
            $scope.phase = response.data;

            $scope.createGroups();
            $scope.getPossiblePlayerForGroups();

        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });

    };
    $scope.saveDraw = function () {
        $scope.phase.groups = $scope.groups;
        $http({
            method: 'POST',
            url: '/api/draw/save',
            data: $scope.phase
        }).then(function successCallback(response) {
            $scope.displaySaveMessage();
        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });

    };
    $scope.start = function () {
        $http.get('/api/draw/start?cid=' + $routeParams.id, {}).then(function (response) {
            $window.location.href = '/#/tournamentdirector/overview';
        });

    };
    $scope.calcGroupSize = function () {
        $http({
            method: 'POST',
            url: '/api/draw/calc-optimal-group-size',
            data: $scope.phase
        }).then(function successCallback(response) {
            $scope.phase = response.data;
            $scope.resultSize = $scope.phase.playerPerGroup * $scope.phase.groupCount;
            console.log('resultsize = ' + $scope.resultSize);

            $scope.createGroups();

        }, function errorCallback(response) {
            $scope.errorMessage = response.data.error;
        });
    };

    $scope.createGroups = function () {
        if ($scope.phase == null) return;

        if ($scope.phase.groups == null || $scope.phase.groups.length == 0) {
            for (i = 0; i < $scope.phase.groupCount; i++) {
                $scope.groups[i] = {name: '' + (i + 1), players: {}};
            }
        } else {
            $scope.groups = $scope.phase.groups;
        }
    };
    $scope.refreshGroupSize = function () {
        if ($scope.phase != null && $scope.modus.id == 1 && $scope.phase.id != null)
            $scope.calcGroupSize();
    };

    $scope.$watch('phase.playerPerGroup', function () {
        $scope.refreshGroupSize();
    });

}).controller('KOController', function ($scope, $http, $routeParams, $window) {
    $scope.getGroupWinner = function () {
        $http.get('/api/draw/possible-player-for-kofield?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.players = response.data;
        });
    };
    $scope.calcKOSizeInInt = function () {
        $http.get('/api/draw/calc-ko-size?cid=' + $routeParams.id, {}).then(function (response) {
            $scope.calcKoSize = response.data;
        });
    };

    $scope.startKO = function () {
        $http.get('/api/draw/start-ko?cid=' + $routeParams.id, {}).then(function (response) {
            $window.location.href = '/#/tournamentdirector/overview';
        });
    };


    $scope.getKoField = function (assignPlayer) {
        $http.get('/api/draw/draw-ko?id=' + $routeParams.id + '&assignPlayer=' + assignPlayer, {}).then(function (response) {
            $scope.koField = response.data;
            $scope.dummArray = new Array($scope.koField.noOfRounds);
            $scope.rounds = new Array($scope.koField.noOfRounds);
            round = $scope.koField.round;
            $scope.rounds [0] = round;
            for (i = 0; i < $scope.rounds.length; i++) {
                $scope.rounds [i] = round;
                round = round.nextRound;
            }
            $scope.getGroupWinner();
        });

    };

    $scope.reset = function () {
        // $http.get('/api/draw/reset-ko?id=' + $routeParams.id, {}).then(function (response) {
        $scope.koField = null;
        $scope.rounds = null;
        $scope.getKoField(false);
        $scope.getGroupWinner();
        // });

    };
    $scope.getNumber2 = function (num) {
        return new Array(num);
    };

    $scope.getGroupWinner();
    $scope.calcKOSizeInInt();
    $scope.getKoField(false);
});


