
angular.module('jitty.controllers', []).controller('UserListController', function($scope, popupService, $window, User) {

    //first one is working without User parameter
    //$scope.users = [
    //    {loginname:'1111'},
    //    {loginname:'222'}
    //];
    $scope.users = User.query();

    $scope.deleteUser=function(user){
        console.log($user.loginName);
        if(popupService.showPopup('Really delete this?')){
            user.$delete(function(){
                $window.location.href='';
            });
        }
    }

}).controller('UserViewController',function($scope,$routeParams,User){

    $scope.user=User.get({id:$routeParams.id});

}).controller('UserCreateController',function($scope,$state,$stateParams,User){

    $scope.user=new User();

    $scope.addUser()=function(){
        $scope.user.$save(function(){
            $state.go('users');
        });
    }

});
