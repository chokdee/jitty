angular.module('jitty.player.services', []).factory('Player', function ($resource) {
    return $resource('http://localhost:8080/api/players/:id', {id: '@_id'}, {
        update: {
            method: 'PUT'
        }
    });
});