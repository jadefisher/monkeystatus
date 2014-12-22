'use strict';

/* Controllers */
var msControllers = angular.module('msControllers', []);

msControllers.controller('MainCtrl', ['$scope', '$http',
function($scope, $http) {
	$scope.dateformat = "yyyy-MM-dd HH:mm:ss";
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
		}, {
			field : 'startDate',
			displayName : 'Start Time',
			cellFilter : 'date:\'yyyy-MM-dd HH:mm:ss\'',
			width : 160
		}, {
			field : 'endDate',
			displayName : 'End Time',
			cellFilter : 'date:\'yyyy-MM-dd HH:mm:ss\'',
			width : 160
		}]
	};
}]);

msControllers.controller('MonitorsCtrl', ['$scope', '$location', '$timeout', 'Monitor', 'Service',
function($scope, $location, $timeout, Monitor, Service) {
	

	$scope.endPointMonitors = [];
	$scope.logFileMonitors = [];
	$scope.pingMonitors = [];
	$scope.telnetMonitors = [];
	$scope.mostRecentStates = {};

	function updateMonitors() {
		$scope.serviceId = $location.search().serviceId;
		var params = null;
		if ($scope.serviceId) {
			console.log("filtering monitors by serviceId");
			$scope.service = Service.get({
				id : $scope.serviceId
			});
			params = {serviceId : $scope.serviceId};
		}
		
		$scope.monitors = Monitor.list(params, null, function(response) {
			console.log("got: " + response);

			$scope.endPointMonitors = [];
			$scope.logFileMonitors = [];
			$scope.pingMonitors = [];
			$scope.telnetMonitors = [];

			for (var i = 0; i < response.length; i++) {
				var monitor = response[i];

				switch (monitor.type) {
				case "net.jadefisher.monkeystatus.model.monitor.EndPointMonitor":
					$scope.endPointMonitors.push(monitor);
					break;
				case "net.jadefisher.monkeystatus.model.monitor.LogFileMonitor":
					$scope.logFileMonitors.push(monitor);
					break;
				case "net.jadefisher.monkeystatus.model.monitor.PingMonitor":
					$scope.pingMonitors.push(monitor);
					break;
				case "net.jadefisher.monkeystatus.model.monitor.TelnetMonitor":
					$scope.telnetMonitors.push(monitor);
					break;
				}

				$scope.mostRecentStates[monitor.id] = Monitor.mostRecent({
					id : monitor.id
				}, null, function(response) {
					console.log("got: " + response.logType);
					$scope.mostRecentStates[monitor.id] = response;
				}, function(response) {
					console.log("failed to get most recent monitor log");
				});
			}

			$timeout(updateMonitors, 30000);
		}, function(response) {
			console.log("failed to get monitors");
		});
	};
	updateMonitors();
}]);

msControllers.controller('MonitorHistoryCtrl', ['$scope', '$location', '$timeout', 'Monitor',
function($scope, $location, $timeout, Monitor) {
	$scope.monitorId = $location.search().monitorId;

	function updateHistory() {
		if ($scope.monitorId) {
			$scope.monitor = Monitor.get({
				id : $scope.monitorId
			});
			$scope.history = Monitor.history({
				id : $scope.monitorId
			}, null, function(response) {
				console.log("got: " + response);
				$timeout(updateHistory, 30000);
			}, function(response) {
				console.log("failed to get services");
			});
		}
	};
	updateHistory();

	$scope.dateOptions = {
		'year-format' : "'yyyy'",
		'month-format' : "'MM'",
		'day-format:' : "'dd'",
		'starting-day' : 1
	};

	//var tagsProp = '<div><div class="ngCellText">{{getRoleNameFromId(row.getProperty(col.field))}}</div></div>';
	var tagsProp = '<span data-ng-repeat="tag in grid.getCellValue(row, col)"> <span class="label label-default"> {{tag}} </span> &nbsp; </span>';

	$scope.gridOptions = {
		data : 'history',
		selectedItems : $scope.selections,
		multiSelect : false,
		enableColumnResize : true,
		enableHighlighting : true,
		showFilter : true,
		showColumnMenu : true,
		columnDefs : [{
			field : 'serviceId',
			displayName : 'Service Id',
			width : 150
		}, {
			field : 'logType',
			displayName : 'Log Type',
			width : 100
		}, {
			field : 'createdDate',
			displayName : 'Date',
			cellFilter : 'date:\'yyyy-MM-dd HH:mm:ss\'',
			width : 160
		}, {
			field : 'message',
			displayName : 'Details',
			width : 1000
		}]
	};
}]);
