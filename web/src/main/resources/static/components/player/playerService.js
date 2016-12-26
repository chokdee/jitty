angular.module('jitty.player.services', []).factory('Player', function ($resource) {
    return $resource('/api/players/:id', {id: '@_id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('Club', function ($resource) {
    return $resource('/api/clubs/:id', {id: '@_id'}, {

    });
}).factory('Association', function ($resource) {
    return $resource('/api/associations/:id', {id: '@_id'}, {

    });
}).factory('TournamentClass', function ($resource) {
    return $resource('/api/tournament-classes/:id', {id: '@_id'}, {

    });
});