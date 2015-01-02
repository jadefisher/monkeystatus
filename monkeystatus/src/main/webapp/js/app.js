'use strict';

/* App Module */

var msApp = angular.module('monkeyStatus', ['ngRoute', 'ngSanitize', 'ui.bootstrap', 'ui.grid', 'ui.grid.edit', 'xeditable', 'checklist-model', 'msControllers', 'msFilters', 'msServices', 'msDirectives']);

msApp.config(['$routeProvider',
function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl : 'partials/home.html?nocache=' + new Date().getTime(),
		controller : 'HomeCtrl',
		login : true
	}).when('/services', {
		templateUrl : 'partials/services.html?nocache=' + new Date().getTime(),
		controller : 'ServicesCtrl'
	}).when('/services/:serviceKey', {
		templateUrl : 'partials/service.html?nocache=' + new Date().getTime(),
		controller : 'ServiceCtrl'
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

msApp.run(['editableOptions',
function(editableOptions) {
	editableOptions.theme = 'bs3';
	// bootstrap3 theme. Can be also 'bs2', 'default'
}]);
