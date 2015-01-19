'use strict';

/* Controllers */
var msControllers = angular.module('msControllers', []);

msControllers.controller('MainCtrl', ['$scope', '$http',
function($scope, $http) {
	$scope.dateformat = "yyyy-MM-dd HH:mm:ss";
	
	$scope.statusClass = function(service) {
		if (!service.currentEvent) {
			return "success";
		}
		switch (service.currentEvent.type) {
		case 'PLANNED_FULL_OUTAGE':
		case 'PLANNED_PARTIAL_OUTAGE':
		case 'PLANNED_INTERMITTENT_OUTAGE':
		case 'PLANNED_INFORMATIONAL':
		case 'UNPLANNED_INFORMATIONAL':
			return "info";
		case 'UNPLANNED_PARTIAL_OUTAGE':
		case 'UNPLANNED_INTERMITTENT_OUTAGE':
			return 'warning';
		case 'UNPLANNED_FULL_OUTAGE':
			return 'danger';
		default:
			return 'danger';
		}
	};
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
}]);

msControllers.controller('ServiceCtrl', ['$scope', '$routeParams', '$timeout', 'Service',
function($scope, $routeParams, $timeout, Service) {
	$scope.serviceKey = $routeParams.serviceKey;
	$scope.allDaysOfWeek = ["MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"];

	function updateService() {
		$scope.service = Service.get({
			key : $scope.serviceKey
		}, null, function(response) {
			console.log("got: " + response);
			$timeout(updateService, 30000);
		}, function(response) {
			console.log("failed to get service");
		});
	};
	updateService();
}]);

msControllers.controller('serviceHistoryCtrl', ['$scope', '$location', '$timeout', '$http', 'Service',
function($scope, $location, $timeout, $http, Service) {
	$scope.serviceKey = $location.search().serviceKey;
	$scope.pageData = null;
	$scope.page = 0;
	$scope.pageSize = 100;
	$scope.nextPageExists = false;
	$scope.prevPageExists = false;

	$scope.updateHistory = function() {
		if ($scope.serviceKey) {
			$scope.service = Service.get({
				key : $scope.serviceKey
			});
			var config = {};
			config.params = {
				page : $scope.page,
				pageSize : $scope.pageSize
			};

			$scope.pageData = $http.get('api/services/' + $scope.serviceKey + "/history", config).success(function(data, status, headers, config) {
				$scope.history = data.content;
				$scope.nextPageExists = data.last == false;
				$scope.prevPageExists = data.first == false;

				$timeout($scope.updateHistory, 30000);
			}).error(function(data, status, headers, config) {
				console.log("failed to get history");

				$timeout($scope.updateHistory, 30000);
			});
			/*
			 $scope.history = Service.history({
			 key : $scope.serviceKey
			 }, null, function(response) {
			 console.log("got: " + response);
			 //$scope.history = response.content;
			 $timeout(updateHistory, 30000);
			 }, function(response) {
			 console.log("failed to get services");
			 }); */
		}
	};
	$scope.updateHistory();
	
	$scope.$watch('pageSize', function(newValue, oldValue) {
		$scope.page = 0;
	});
	
	$scope.$watch('page', function(newValue, oldValue) {
		if (newValue !== oldValue)
			$scope.updateHistory();
	});

	$scope.prevPage = function() {
		$scope.page = $scope.page - 1;
	};

	$scope.nextPage = function() {
		$scope.page = $scope.page + 1;
	};

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
			params = {
				serviceKey : $scope.serviceKey
			};
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

msControllers.controller('MonitorHistoryCtrl', ['$scope', '$location', '$timeout', '$http', 'Monitor',
function($scope, $location, $timeout, $http, Monitor) {
	$scope.monitorKey = $location.search().monitorKey;
	$scope.serviceKey = $location.search().serviceKey;
	$scope.pageData = null;
	$scope.page = 0;
	$scope.pageSize = 100;
	$scope.nextPageExists = false;
	$scope.prevPageExists = false;

	$scope.updateHistory = function() {
		if ($scope.monitorKey) {
			$scope.monitor = Monitor.get({
				key : $scope.monitorKey
			});
			var config = {};
			config.params = {
				page : $scope.page,
				pageSize : $scope.pageSize
			};

			$scope.pageData = $http.get('api/monitors/' + $scope.monitorKey + "/history", config).success(function(data, status, headers, config) {
				$scope.history = data.content;
				$scope.nextPageExists = data.last == false;
				$scope.prevPageExists = data.first == false;

				$timeout($scope.updateHistory, 30000);
			}).error(function(data, status, headers, config) {
				console.log("failed to get history");

				$timeout($scope.updateHistory, 30000);
			});
			/*
			 $scope.history = Service.history({
			 key : $scope.serviceKey
			 }, null, function(response) {
			 console.log("got: " + response);
			 //$scope.history = response.content;
			 $timeout(updateHistory, 30000);
			 }, function(response) {
			 console.log("failed to get services");
			 }); */
		}
	};
	$scope.updateHistory();
	
	$scope.$watch('pageSize', function(newValue, oldValue) {
		$scope.page = 0;
	});
	
	$scope.$watch('page', function(newValue, oldValue) {
		if (newValue !== oldValue)
			$scope.updateHistory();
	});

	$scope.prevPage = function() {
		$scope.page = $scope.page - 1;
	};

	$scope.nextPage = function() {
		$scope.page = $scope.page + 1;
	};

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
