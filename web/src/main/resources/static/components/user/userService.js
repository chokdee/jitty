angular.module('jitty.user.services', []).factory('User', function ($resource) {
    return $resource('/api/users/:id', {id: '@_id'}, {
        update: {
            method: 'PUT'
        }
    });
});