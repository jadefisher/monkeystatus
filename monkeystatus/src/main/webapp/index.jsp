<!DOCTYPE html>

<%@page contentType="text/html"%>

<%
	String buildVersion = String.valueOf(System.currentTimeMillis());
%>

<html lang="en" id="ng-app" data-ng-app="monkeyStatus">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0">
<title>Monkey Status</title>

<!-- Bootstrap Styles -->
<link rel="stylesheet"
	href="lib/bootstrap-3.3.1/css/bootstrap.min.css?buildVersion=<%=buildVersion%>">

<!-- ng-grid Styles -->
<link rel="stylesheet"
	href="lib/ui-grid-3.0.0/ui-grid-stable.min.css?buildVersion=<%=buildVersion%>">

<!-- Monkey Status Styles -->
<link rel="stylesheet"
	href="css/monkeystatus.css?buildVersion=<%=buildVersion%>">

<!-- AngularJS core libraries -->
<script
	src="lib/angularjs-1.3.7/angular.js?buildVersion=<%=buildVersion%>"></script>
<script
	src="lib/angularjs-1.3.7/angular-resource.min.js?buildVersion=<%=buildVersion%>"></script>
<script
	src="lib/angularjs-1.3.7/angular-route.min.js?buildVersion=<%=buildVersion%>"></script>
<script
	src="lib/angularjs-1.3.7/angular-sanitize.min.js?buildVersion=<%=buildVersion%>"></script>

<!-- AngularJS UI-Bootstrap library used for model windows and heaps of other widgets -->
<script
	src="lib/ui-bootstrap/ui-bootstrap-tpls-0.12.0.min.js?buildVersion=<%=buildVersion%>"></script>

<!-- AngularJS ng-grid library used for tables -->
<script
	src="lib/ui-grid-3.0.0/ui-grid-stable.min.js?buildVersion=<%=buildVersion%>"></script>

<!-- MonkeyStatus scripts -->
<script src="js/app.js?buildVersion=<%=buildVersion%>"></script>
<script src="js/controllers.js?buildVersion=<%=buildVersion%>"></script>
<script src="js/filters.js?buildVersion=<%=buildVersion%>"></script>
<script src="js/services.js?buildVersion=<%=buildVersion%>"></script>
<script src="js/directives.js?buildVersion=<%=buildVersion%>"></script>

<!--[if lte IE 8]>
    <script src="../lib/json2/json2.js"></script>
    <style>
	  input {
	    font-family: Arial;
	  }
	</style>
  <![endif]-->

<!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->

</head>
<body class="tp-body" data-ng-controller="MainCtrl">
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#/home">MonkeyStatus</a>
			</div>
			<div>
				<ul class="nav navbar-nav">
					<li><a href="#/services">Services</a></li>
					<li><a href="#/monitors">Monitors</a></li>
				</ul>
			</div>
		</div>
	</nav>
	<div class="container">
		<div class="view-container">
			<div data-ng-view class="view-frame"></div>
		</div>
	</div>
</body>
</html>
