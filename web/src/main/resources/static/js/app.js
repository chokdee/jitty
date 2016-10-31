angular.module('jitty', ['ngRoute', 'ngResource', 'ngMessages', 'ui.bootstrap', 'frapontillo.bootstrap-duallistbox', 'dndLists', ,
    'jitty.controllers', 'jitty.services', 'jitty.directives',
    'jitty.tournament', 'jitty.player', 'jitty.draw', 'jitty.running', 'jitty.liveview', 'jitty.util']).config(function ($routeProvider, $httpProvider) {

    $routeProvider.when('/', {
        title: 'Startseite',
        templateUrl: 'home.html',
        controller: 'home'
    }).when('/login', {
        title: 'Login',
        templateUrl: 'login.html',
        controller: 'navigation'
    }).when('/users', {
        title: 'Benutzerübersicht',
        templateUrl: 'users.html',
        controller: 'UserListController'
    }).when('/users/:id', {
        title: 'Benutzer bearbeiten',
        templateUrl: 'user-edit.html',
        controller: 'UserEditController'
    }).when('/users-add', {
        title: 'Benutzer hinzufügen',
        templateUrl: 'user-new.html',
        controller: 'UserCreateController'
    }).when('/user-pw-change/:id', {
        title: 'Passwort ändern',
        templateUrl: 'user-pw-change.html',
        controller: 'UserEditController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

}).controller(
    'navigation',

    function ($rootScope, $scope, $http, $location, $route) {

        $scope.tab = function (route) {
            return $route.current && route === $route.current.controller;
        };

        var authenticate = function (credentials, callback) {

            var headers = credentials ? {
                authorization: "Basic "
                + btoa(credentials.username + ":"
                    + credentials.password)
            } : {};

            $http.get('user', {
                headers: headers
            }).success(function (data) {
                if (data.name) {
                    $rootScope.authenticated = true;
                } else {
                    $rootScope.authenticated = false;
                }
                callback && callback($rootScope.authenticated);
            }).error(function () {
                $rootScope.authenticated = false;
                callback && callback(false);
            });

        };

        authenticate();

        $scope.credentials = {};
        $scope.login = function () {
            authenticate($scope.credentials, function (authenticated) {
                if (authenticated) {
                    console.log("Login succeeded")
                    $location.path("/");
                    $scope.error = false;
                    $rootScope.authenticated = true;
                } else {
                    console.log("Login failed")
                    $location.path("/login");
                    $scope.error = true;
                    $rootScope.authenticated = false;
                }
            })
        };

        $scope.logout = function () {
            $http.post('logout', {}).success(function () {
                $rootScope.authenticated = false;
                $location.path("/");
            }).error(function (data) {
                console.log("Logout failed")
                $rootScope.authenticated = false;
            });
        }

    }).controller('home', function ($scope, $http) {
    $http.get('/resource/').success(function (data) {
        $scope.greeting = data;
    })
}).run(function ($rootScope, $route, $location) {

    var history = [];

    $rootScope.$on('$routeChangeSuccess', function () {
        history.push($location.$$path);
        document.title = 'Jitty - ' + $route.current.title;
    });

    $rootScope.back = function () {
        var prevUrl = history.length > 1 ? history.splice(-2)[0] : "/";
        $location.path(prevUrl);
    };

    $rootScope.dateFormat = 'dd.MM.yyyy';
    $rootScope.altInputFormats = ['d!/M!/yyyy'];

    $rootScope.tournament = {};
});