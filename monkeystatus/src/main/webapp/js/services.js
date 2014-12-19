'use strict';

/** 
 * Services 
 *
 * Syntax for using ngResource services is:
 * 
 * HTTP GET "class" actions: Resource.action([parameters], [success], [error])
 * non-GET "class" actions: Resource.action([parameters], postData, [success], [error])
 * non-GET instance actions: instance.$action([parameters], [success], [error])
 **/

var msServices = angular.module('msServices', ['ngResource']);


msServices.factory('Service', ['$resource',
function($resource) {
    return $resource('api/services/:id', {}, {
        query : {
            method : 'GET',
            params : {
                id : ''
            },
            isArray : true
        },
        list : {
            method : 'GET',
            isArray : true
        },
        update : {
            method : 'PUT',
            isArray : false
        },
        currentEvent: {
        	method: 'GET',
        	url: 'api/services/:id/currentEvent'
        },
        events: {
        	method : 'GET',
        	url: 'api/services/:id/events',
            isArray : true
        }
    });
}]);