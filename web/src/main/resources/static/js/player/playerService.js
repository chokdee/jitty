angular.module('jitty.player.services', []).factory('Player', function ($resource) {
    return $resource('http://localhost:8080/api/players/:id', {id: '@_id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Club', function ($resource) {
    return $resource('http://localhost:8080/api/clubs/:id', {id: '@_id'}, {

    });
}).factory('Association', function ($resource) {
    return $resource('http://localhost:8080/api/associations/:id', {id: '@_id'}, {

    });
}).factory('TournamentClass', function ($resource) {
    return $resource('http://localhost:8080/api/tournament-classes/:id', {id: '@_id'}, {

    });
});