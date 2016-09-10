angular.module('jitty.running.services', []).factory('TournamentClass', function ($resource) {
    return $resource('/api/tournament-classes/:id', {id: '@_id'}, {
    });
});