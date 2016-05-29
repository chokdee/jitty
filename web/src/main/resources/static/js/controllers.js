angular.module('jitty.controllers', []).controller('UserListController', function ($scope, popupService, $window, User) {

    //first one is working without User parameter
    //$scope.users = [
    //    {loginname:'1111'},
    //    {loginname:'222'}
    //];
    $scope.users = User.query();

    $scope.deleteUser = function (user) {
        console.log($user.loginName);
        if (popupService.showPopup('Really delete this?')) {
            user.$delete(function () {
                $window.location.href = '';
            });
        }
    };

    $scope.createNewUser = function () {
        $window.location.href = '/#/users-add';
    }

}).controller('UserViewController', function ($scope, $routeParams, User) {

    $scope.user = User.get({id: $routeParams.id});

}).controller('UserCreateController', function ($scope, User, $location) {

// callback for ng-click 'saveNewUser ':
    $scope.debugMe = function () {
        console.log('hallo')
    };
    $scope.saveNewUser = function () {
        User.save($scope.user);
        $location.path('/user-list');
    };
    $scope.submitted = false;
    $scope.submit = function() {
        $scope.submitted = true;
    };
    $scope.interacted = function(field) {
        return $scope.submitted || field.$dirty;
    };

});
