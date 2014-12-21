'use strict';

/* Controllers */
var msControllers = angular.module('msControllers', []);

msControllers.controller('MainCtrl', ['$scope', '$http',
function($scope, $http) {
	//na
}]);

msControllers.controller('HomeCtrl', ['$scope', '$http',
function($scope, $http) {
	//na
}]);

msControllers.controller('ServicesCtrl', ['$scope', '$http', '$timeout', 'Service',
function($scope, $http, $timeout, Service) {

	function updateServices() {
		$scope.services = Service.list(null, null, function(response) {
			console.log("got: " + response);
			$timeout(updateServices, 30000);
		}, function(response) {
			console.log("failed to get services");
		});
	};
	updateServices();

	$scope.statusClass = function(service) {
		if (!service.currentEvent) {
			return "success";
		}
		switch (service.currentEvent.type) {
		case 'PLANNED_OUTAGE':
		case 'INFORMATIONAL':
			return "info";
		case 'UNPLANNED_PARTIAL_OUTAGE':
		case 'INTERMITTENT_OUTAGE':
			return 'warning';
		case 'UNPLANNED_FULL_OUTAGE':
			return 'danger';
		default:
			return 'danger';
		}
	};
}]);

msControllers.controller('serviceHistoryCtrl', ['$scope', '$location', '$timeout', 'Service',
function($scope, $location, $timeout, Service) {
	$scope.serviceId = $location.search().serviceId;

	function updateHistory() {
		if ($scope.serviceId) {
			$scope.service = Service.get({
				id : $scope.serviceId
			});
			$scope.history = Service.history({
				id : $scope.serviceId
			}, null, function(response) {
				console.log("got: " + response);
				$timeout(updateHistory, 30000);
			}, function(response) {
				console.log("failed to get services");
			});
		}
	};
	updateHistory();

	$scope.dateformat = "yyyy-MM-dd HH:mm:ss";
	$scope.dateOptions = {
		'year-format' : "'yyyy'",
		'month-format' : "'MM'",
		'day-format:' : "'dd'",
		'starting-day' : 1
	};
	$scope.gridOptions = {
		data : 'history',
		selectedItems : $scope.selections,
		multiSelect : false,
		enableColumnResize : true,
		enableHighlighting : true,
		showFilter : true,
		showColumnMenu : true,
		columnDefs : [{
			field : 'type',
			displayName : 'Type',
			width : 300
		}, {
			field : 'description',
			displayName : 'Description',
			width : 350
		},{
			field : 'startDate',
			displayName : 'Start Time',
			cellFilter : 'date:\'yyyy-MM-dd HH:mm:ss\'',
			width : 160
		},{
			field : 'endDate',
			displayName : 'End Time',
			cellFilter : 'date:\'yyyy-MM-dd HH:mm:ss\'',
			width : 160
		}]
	};
}]);

msControllers.controller('MonitorsCtrl', ['$scope', '$http',
function($scope, $http) {
	//na
}]);

msControllers.controller('MonitorHistoryCtrl', ['$scope', '$http',
function($scope, $http) {
	//na
}]);
