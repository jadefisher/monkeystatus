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

msControllers.controller('ServiceCtrl', ['$scope', '$routeParams', '$timeout', 'Service',
function($scope, $routeParams, $timeout, Service) {
	$scope.serviceKey = $routeParams.serviceKey;
	$scope.allDaysOfWeek = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"];
	
	function updateService() {
		$scope.service = Service.get({key: $scope.serviceKey}, null, function(response) {
			console.log("got: " + response);
			$timeout(updateService, 30000);
		}, function(response) {
			console.log("failed to get service");
		});
	};
	updateService();

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
	$scope.serviceKey = $location.search().serviceKey;

	function updateHistory() {
		if ($scope.serviceKey) {
			$scope.service = Service.get({
				key : $scope.serviceKey
			});
			$scope.history = Service.history({
				key : $scope.serviceKey
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
			field : 'serviceKey',
			displayName : 'Service',
			width : 200
		}, {
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
		$scope.serviceKey = $location.search().serviceKey;
		var params = null;
		if ($scope.serviceKey) {
			console.log("filtering monitors by serviceKey");
			$scope.service = Service.get({
				key : $scope.serviceKey
			});
			params = {serviceKey : $scope.serviceKey};
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

				$scope.mostRecentStates[monitor.key] = Monitor.mostRecent({
					key : monitor.key
				}, null, function(response) {
					console.log("got: " + response.logType);
					$scope.mostRecentStates[monitor.key] = response;
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
	$scope.monitorKey = $location.search().monitorKey;

	function updateHistory() {
		if ($scope.monitorKey) {
			$scope.monitor = Monitor.get({
				key : $scope.monitorKey
			});
			$scope.history = Monitor.history({
				key : $scope.monitorKey
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
			field : 'serviceKey',
			displayName : 'Service Key',
			width : 150
		}, {
			field : 'logType',
			displayName : 'Log Type',
			width : 100
		}, {
			field : 'timestamp',
			displayName : 'Timestamp',
			cellFilter : 'date:\'yyyy-MM-dd HH:mm:ss\'',
			width : 160
		}, {
			field : 'message',
			displayName : 'Details',
			width : 1000
		}]
	};
}]);
