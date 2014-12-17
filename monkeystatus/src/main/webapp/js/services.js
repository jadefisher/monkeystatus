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