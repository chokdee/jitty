angular.module('jitty.controllers', []).controller('UserListController', function ($scope, popupService, $window, User) {

    //first one is working without User parameter
    //$scope.users = [
    //    {loginname:'1111'},
    //    {loginname:'222'}
    //];
    $scope.users = User.query();

    $scope.deleteUser = function (userId) {
        if (popupService.showPopup('Möchten Sie den Benutzer wirklich löschen?')) {
            User.delete({id: userId}, function () {
                console.log('successful deleted user with id ' + userId);
                $scope.users = User.query();
                console.log('queried all user');
            });
        }
    };

    $scope.createNewUser = function () {
        $window.location.href = '/#/users-add';
    }

}).controller('UserEditController', function ($scope, $routeParams, User, $location) {

    $scope.user = User.get({id: $routeParams.id});

    $scope.saveUser = function () {
        if ($scope.userForm.$valid) {
            User.save($scope.user, function () {
                console.log('user saved successful');
                $scope.users = User.query();
                $location.path('/users');
            });
        }
    };

}).controller('UserCreateController', function ($scope, User, $location) {

    $scope.saveNewUser = function () {
        if ($scope.userForm.$valid) {
            User.save($scope.user, function () {
                console.log('user created successful');
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
