angular.module('jitty.tournament.services', []).factory('Tournament', function ($resource) {
    return $resource('/api/tournaments/:id', {id: '@_id'}, {
        update: {
            method: 'PUT'
        }
    });
}).factory('TournamentClass', function ($resource) {
    return $resource('/api/tournament-classes/:id', {id: '@_id'}, {
        update: {
            method: 'PUT'
        }
    });
});