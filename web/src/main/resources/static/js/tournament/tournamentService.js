angular.module('jitty.tournament.services', []).factory('Tournament', function ($resource) {
    return $resource('http://localhost:8080/api/tournaments/:id', {id: '@_id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('TournamentClass', function ($resource) {
    return $resource('http://localhost:8080/api/tournament-classes/:id', {id: '@_id'}, {
        update: {
            method: 'PUT'
        }
    });
});