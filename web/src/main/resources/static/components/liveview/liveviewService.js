angular.module('jitty.liveview.services', []).factory('TournamentClass', function ($resource) {
    return $resource('/api/tournament-classes/:id', {id: '@_id'}, {

    });
});