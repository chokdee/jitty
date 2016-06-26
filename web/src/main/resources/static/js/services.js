angular.module('jitty.services', []).factory('User',
    function ($resource) {
        return $resource('http://localhost:8080/api/users/:id', {id: '@_id'}, {
            update: {
                method: 'PUT'
            }
        });
    }).service('popupService', function ($window) {
    this.showPopup = function (message) {
        return $window.confirm(message);
    }
});