/**
 * Created by Sandeep on 01/06/14.
 */

angular.module('jitty.services',[]).factory('User',function($resource){
    return $resource('http://localhost:8080/api/users/:id',{id:'@_id'},{
        update: {
            method: 'PUT'
        }
    });
});