angular.module('jitty', ['ngRoute', 'ngResource', 'ngMessages', 'jitty.auth', 'ui.bootstrap', 'frapontillo.bootstrap-duallistbox', 'dndLists', 'ngFlash',
    'jitty.controllers', 'jitty.services', 'jitty.directives', 'jitty.login',
    'jitty.tournament', 'jitty.player', 'jitty.draw', 'jitty.running', 'jitty.liveview', 'jitty.util']).config(function ($routeProvider, $httpProvider, $locationProvider) {

    // $locationProvider.html5Mode({
    //     enabled: false,
    // });

    $routeProvider.when('/', {
        title: 'Startseite',
        templateUrl: '/components/home/home.html',
        controller: 'home'
    }).when('/login', {
        title: 'Login',
        templateUrl: '/components/login/login.html',
        controller: 'LoginController',
        controllerAs : 'controller'
    }).when('/users', {
        title: 'Benutzerübersicht',
        templateUrl: '/components/user/users.html',
        controller: 'UserListController'
    }).when('/users/:id', {
        title: 'Benutzer bearbeiten',
        templateUrl: '/components/user/user-edit.html',
        controller: 'UserEditController'
    }).when('/users-add', {
        title: 'Benutzer hinzufügen',
        templateUrl: '/components/user/user-new.html',
        controller: 'UserCreateController'
    }).when('/user-pw-change/:id', {
        title: 'Passwort ändern',
        templateUrl: '/components/user/user-pw-change.html',
        controller: 'UserEditController'
    }).otherwise('/');

    $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

    $httpProvider.interceptors.push('authInterceptor');

}).controller('home', function ($scope, $http, auth) {

    $http.get('/resource/').success(function (data) {
        $scope.greeting = data;
    });

    $scope.authenticated = function() {
        return auth.authenticated;
    };


}).run(function ($rootScope, $route, $location, auth) {

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

    auth.init('/', '/login', '/logout');
});