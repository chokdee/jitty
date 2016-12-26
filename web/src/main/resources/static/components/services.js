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
}).service('authInterceptor', function($q) {
    var service = this;

    service.responseError = function(response) {
        if (response.status == 401){
            window.location = "/#/login";
        }
        return $q.reject(response);
    };
});