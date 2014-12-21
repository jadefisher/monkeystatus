'use strict';

/* App Module */

var msApp = angular.module('monkeyStatus', ['ngRoute', 'ngSanitize', 'ui.bootstrap', 'ui.grid', 'ui.grid.edit', 'msControllers', 'msFilters', 'msServices', 'msDirectives']);

msApp.config(['$routeProvider',
function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl : 'partials/home.html?nocache=' + new Date().getTime(),
		controller : 'HomeCtrl',
		login : true
	}).when('/services', {
		templateUrl : 'partials/services.html?nocache=' + new Date().getTime(),
		controller : 'ServicesCtrl'
	}).when('/serviceHistory', {
		templateUrl : 'partials/serviceHistory.html?nocache=' + new Date().getTime(),
		controller : 'serviceHistoryCtrl'
	}).when('/monitors', {
		templateUrl : 'partials/monitors.html?nocache=' + new Date().getTime(),
		controller : 'MonitorsCtrl'
	}).when('/monitorHistory', {
		templateUrl : 'partials/monitorHistory.html?nocache=' + new Date().getTime(),
		controller : 'MonitorHistoryCtrl'
	}).otherwise({
		redirectTo : '/home'
	});
}]);
