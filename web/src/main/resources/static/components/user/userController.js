angular.module('jitty.user.controllers', []).controller('UserListController', function ($scope, popupService, $window, User, Flash) {

    $scope.predicate = 'name';
    $scope.reverse = true;

    $scope.users = User.query();

    $scope.deleteUser = function (userId) {
        if (popupService.showPopup('Möchten Sie den Benutzer wirklich löschen?')) {
            User.delete({id: userId}, function () {
                console.log('successful deleted user with id ' + userId);
                Flash.create('success', 'Der Benutzer wurde erfolgreich gelöscht', 4000, {container: 'flash-status'});
                $scope.users = User.query();
                console.log('queried all user');
            });
        }
    };

    $scope.createNewUser = function () {
        $window.location.href = '/#/users-add';
    };

    $scope.order = function (predicate) {
        $scope.reverse = ($scope.predicate === predicate) ? !$scope.reverse : false;
        $scope.predicate = predicate;
    };

}).controller('UserEditController', function ($scope, $routeParams, User, $location, $http, Flash) {

    $scope.user = User.get({id: $routeParams.id});

    $scope.saveUser = function () {
        if ($scope.userForm.$valid) {
            User.save($scope.user, function () {
                console.log('user saved successful');
                var id = Flash.create('success', 'Der Benutzer wurde erfolgreich gespeichert', 4000, {container: 'flash-status'});
                $scope.users = User.query();
                $location.path('/users');
            });
        }
    };

    $scope.savePassword = function () {
        if ($scope.userForm.$valid) {
            $http({
                method: 'POST',
                url: '/api/users/change-password',
                data: {
                    id: $scope.user.id,
                    password: $scope.user.password
                }

            }).then(function successCallback(response) {
                console.log('password saved successful');
                Flash.create('success', 'Das Passwort wurde erfolgreich gespeichert', 4000, {container: 'flash-status'});
                $location.path('/users');

            }, function errorCallback(response) {
                console.log('error save password');
            });
        }
    };

}).controller('UserCreateController', function ($scope, User, $location, Flash) {

    $scope.saveNewUser = function () {
        if ($scope.userForm.$valid) {
            User.save($scope.user, function () {
                console.log('user created successful');
                Flash.create('success', 'Der Benutzer wurde erfolgreich gespeichert', 4000, {container: 'flash-status'});
                $scope.users = User.query();
                $location.path('/users');
            });
        }
    };
    $scope.submitted = false;
    $scope.submit = function () {
        $scope.submitted = true;
    };
    $scope.interacted = function (field) {
        return $scope.submitted || field.$dirty;
    };

});
