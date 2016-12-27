angular.module('jitty.draw.services', []).factory('TournamentClass', function ($resource) {
    return $resource('/api/tournament-classes/:id', {id: '@_id'}, {

    });


});