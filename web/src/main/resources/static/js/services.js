angular.module('jitty.services', []).factory('User',
    function ($resource) {
        return $resource('/api/users/:id', {id: '@_id'}, {
            update: {
                method: 'PUT'
            }
        });
    }).service('popupService', function ($window) {
    this.showPopup = function (message) {
        return $window.confirm(message);
    }
});