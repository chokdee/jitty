
angular.module('jitty.controllers', []).controller('UserListController', function($scope, User) {

    //first one is working without User parameter
    //$scope.users = [
    //    {loginname:'1111'},
    //    {loginname:'222'}
    //];
    $scope.users = User.query();

}).controller('UserViewController',function($scope,$stateParams,User){

    $scope.movie=User.get({id:$stateParams.id});

});
