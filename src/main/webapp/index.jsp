<html>
<head>
<title>Page Redirection</title>
</head>
<body>
<h1>Page Redirection</h1>
<%
   // New location to be redirected
   String site = "/speccdb/views/dashboard.jsp";
   response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
   response.setHeader("Location", site); 
%>
</body>
</html>