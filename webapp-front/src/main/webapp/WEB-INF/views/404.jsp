<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
%><!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Computer Database</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!-- Bootstrap -->
    <link href="/speccdb/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="/speccdb/css/font-awesome.css" rel="stylesheet" media="screen">
    <link href="/speccdb/css/main.css" rel="stylesheet" media="screen">
</head>
<body id="error_404">
    <header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="/speccdb/dashboard.html"><spring:message code="lbl.title"/></a>

            <div class="navbar-right">
                <a href="?lang=fr"><img src="/speccdb/images/fr.jpg"></a>
                <a href="?lang=en"><img src="/speccdb/images/en.jpg"></a>
            </div>
        </div>
    </header>

    <section id="main">
        <div class="container">
            <div class="alert alert-danger"><spring:message text="Error 404: Page not found. Too bad bitch!" code="lbl.404"/>
                <br/>
            </div>
        </div>
    </section>

    <script src="/speccdb/js/jquery.min.js"></script>
    <script src="/speccdb/js/bootstrap.min.js"></script>
    <script src="/speccdb/js/dashboard.js"></script>

</body>
</html>