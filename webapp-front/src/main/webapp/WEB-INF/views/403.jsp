<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
	<title>Computer Database</title>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Bootstrap -->
	<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
	<link href="css/font-awesome.css" rel="stylesheet" media="screen">
	<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body id="error_403">
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
            <a class="navbar-brand" href="dashboard.html"><spring:message code="lbl.title"/></a>

            <div class="navbar-right">
                <a href="?lang=fr"><img src="images/fr.jpg"></a>
                <a href="?lang=en"><img src="images/en.jpg"></a>
            </div>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<div class="alert alert-danger">
                <spring:message text="Error 403: Access denied!" code="lbl.403"/>
				<br/>
				<!-- stacktrace -->
			</div>
		</div>
	</section>

	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>

</body>
</html>