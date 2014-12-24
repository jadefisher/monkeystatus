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
    return $resource('api/services/:key', {}, {
        list : {
            method : 'GET',
            isArray : true
        },
        update : {
            method : 'PUT',
            isArray : false
        },
        history: {
        	method : 'GET',
        	url: 'api/services/:key/history',
            isArray : true
        }
    });
}]);

msServices.factory('Monitor', ['$resource',
function($resource) {
    return $resource('api/monitors/:key', {}, {
        list : {
            method : 'GET',
            isArray : true
        },
        update : {
            method : 'PUT',
            isArray : false
        },
        mostRecent: {
        	method : 'GET',
        	url: 'api/monitors/:key/mostRecent'
        },
        history: {
        	method : 'GET',
        	url: 'api/monitors/:key/history',
            isArray : true
        }
    });
}]);