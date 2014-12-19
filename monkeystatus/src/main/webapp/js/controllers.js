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
	$scope.currentEvents = {};

	function updateServices() {
		$scope.services = Service.list(null, null, function(response) {
			console.log("got: " + response);
			for (var i = 0; i < response.length; i++) {
				var s = response[i];
				$scope.currentEvents[s.id] = Service.currentEvent({
					id : s.id
				});
			}
			$timeout(updateServices, 30000);
		}, function(response) {
			console.log("failed to get services");
		});
	};
	updateServices();

	$scope.statusIsGood = function(serviceId) {
		return $scope.currentEvents[serviceId].id ? false : true;
	};

	$scope.statusIsInfo = function(serviceId) {
		if (!$scope.currentEvents[serviceId])
			return false;
		var type = $scope.currentEvents[serviceId].type;
		return type == 'PLANNED_OUTAGE' || type == 'INFORMATIONAL';
	};

	$scope.statusIsWarning = function(serviceId) {
		if (!$scope.currentEvents[serviceId])
			return false;
		var type = $scope.currentEvents[serviceId].type;
		return type == 'UNPLANNED_PARTIAL_OUTAGE' || type == 'INTERMITTENT_OUTAGE';
	};

	$scope.statusIsBad = function(serviceId) {
		if (!$scope.currentEvents[serviceId])
			return false;
		var type = $scope.currentEvents[serviceId].type;
		return type == 'UNPLANNED_FULL_OUTAGE';
	};
}]);

msControllers.controller('serviceHistoryCtrl', ['$scope', '$http',
function($scope, $http) {
	//na
}]);

msControllers.controller('MonitorsCtrl', ['$scope', '$http',
function($scope, $http) {
	//na
}]);

msControllers.controller('MonitorHistoryCtrl', ['$scope', '$http',
function($scope, $http) {
	//na
}]);
