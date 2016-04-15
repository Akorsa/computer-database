<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="/WEB-INF/tagslib.tld" prefix="my" %>
<%@taglib tagdir="/WEB-INF/tags" prefix="my2" %>
<%--<jsp:useBean id="dashboardManager" scope="page"--%>
             <%--class="com.excilys.mviegas.speccdb.managers.DashboardManagerBean">--%>
    <%--<jsp:setProperty name="dashboardManager" property="*"/>--%>
<%--</jsp:useBean>--%>
<%--<c:choose>--%>
    <%--<c:when test="${pageContext.request.method=='POST' && not empty param.selection}">--%>
        <%--&lt;%&ndash;@elvariable id="dashboardManager" type="com.excilys.mviegas.speccdb.managers.DashboardManagerBean"&ndash;%&gt;--%>
        <%--<c:if test="${dashboardManager.delete(param.selection)}" var="deleteSuccessful">--%>
            <%--<c:if test="${param.selection}"/>--%>
        <%--</c:if>--%>
<%--&lt;%&ndash;&ndash;%&gt;--%>
    <%--</c:when>--%>
    <%--<c:when test="${param.add!=null}">--%>
    <%--</c:when>--%>
<%--</c:choose>--%>
<%--<c:if test="${dashboardManager.update()}"/>--%>
<!DOCTYPE html>
<html>
<head>
    <title>Computer Database</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="utf-8">
    <!-- Bootstrap -->
    <link href="/speccdb/css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link href="/speccdb/css/font-awesome.css" rel="stylesheet" media="screen">
    <link href="/speccdb/css/main.css" rel="stylesheet" media="screen">
</head>
<body>
<header class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
        <a class="navbar-brand" href="dashboard.html"> Application -
            Computer Database </a>
    </div>
</header>
<section id="main">
    
    <%-- Message de confirmations --%>
    <c:if test="${computerAdded || param.computerAdded}">
        <c:remove var="computerAdded" scope="session"/>
        <div class="alert alert-success alert-dismissible fade in" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                    aria-hidden="true">×</span>
            </button>
            <h4>Computer successfully added into Database</h4>
        </div>
    </c:if>
    
    <c:if test="${computerEdited}">
        <c:remove var="computerEdited" scope="session"/>
        <div class="alert alert-success alert-dismissible fade in" role="alert">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                    aria-hidden="true">×</span>
            </button>
            <h4>Modifications are successfully saved into Database</h4>
        </div>
    </c:if>
    
    <c:if test="${not empty deleteSuccessful}">
        <c:choose>

            <c:when test="${deleteSuccessful}">
                <div class="alert alert-success alert-dismissible fade in" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                            aria-hidden="true">×</span>
                    </button>
                    <h4>Computer successfully deleted from the Database</h4>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger alert-dismissible fade in" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span
                            aria-hidden="true">×</span>
                    </button>
                    <h4>Error on deletion of the selected computers.</h4>
                </div>
            </c:otherwise>
        </c:choose>

        <c:remove var="deleteSuccessful"/>
    </c:if>

	<%-- Début contenu de la page --%>
    <div class="container">
        <h1 id="homeTitle">
            ${dashboardManager.paginator.elementsCount} Computers found
        </h1>
        <div id="actions" class="form-horizontal">
            <div class="pull-left">
                <form id="searchForm" action="${my:link('dashboard')}" method="GET" class="form-inline">

                    <c:if test="${not empty param.size}">
                        <input type="hidden" name="size" value="${param.size}">
                    </c:if>
                    <input type="search" id="searchbox" name="search"
                           class="form-control" placeholder="Search name" value="${param.search}"/> <input
                        type="submit" id="searchsubmit" value="Filter by name"
                        class="btn btn-primary"/>
                </form>
            </div>
            <div class="pull-right">
                <a class="btn btn-success" id="addComputer" href="addComputer.html">Add
                    Computer</a> <a class="btn btn-default" id="editComputer" href="#"
                                    onclick="$.fn.toggleEditMode();">Edit</a>
            </div>
        </div>
    </div>

    <form id="deleteForm" method="POST">
        <input type="hidden" name="selection" value="">
    </form>

    <div class="container" style="margin-top: 10px;">
        <table class="table table-striped table-bordered">
            <thead>
            <tr>
                <!-- Variable declarations for passing labels as parameters -->
                <!-- Table header for Computer Name -->

                <th class="editMode" style="width: 60px; height: 22px;"><input
                        type="checkbox" id="selectall"/> <span
                        style="vertical-align: top;"> - <a href="#"
                                                           id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
                        class="fa fa-trash-o fa-lg"></i>
                </a>
						</span></th>
                <th><a href="${my:linkSort('dashboard', param, 'name', (dashboardManager.order == 'name' && dashboardManager.typeOrder != 'DESC' ? 'DESC' : null))}"><c:if test="${dashboardManager.order == 'name'}"><span class="glyphicon glyphicon-chevron-${dashboardManager.typeOrder == 'DESC' ? 'down': 'up'}" aria-hidden="true"></span></c:if> Computer name <span class="glyphicon glyphicon-sort-by-attributes${dashboardManager.order == 'name' && dashboardManager.typeOrder != 'DESC' ? '-alt': ''}" aria-hidden="true"></span></a></th>
                <th><a href="${my:linkSort('dashboard', param, 'introduced', (dashboardManager.order == 'introduced' && dashboardManager.typeOrder != 'DESC' ? 'DESC' : null))}"><c:if test="${dashboardManager.order == 'introduced'}"><span class="glyphicon glyphicon-chevron-${dashboardManager.typeOrder == 'DESC' ? 'down': 'up'}" aria-hidden="true"></span></c:if> Introduced date <span class="glyphicon glyphicon-sort-by-attributes${dashboardManager.order == 'introduced' && dashboardManager.typeOrder != 'DESC' ? '-alt': ''}" aria-hidden="true"></span></a></th>
                <th><a href="${my:linkSort('dashboard', param, 'discontinued', (dashboardManager.order == 'discontinued' && dashboardManager.typeOrder != 'DESC' ? 'DESC' : null))}"><c:if test="${dashboardManager.order == 'discontinued'}"><span class="glyphicon glyphicon-chevron-${dashboardManager.typeOrder == 'DESC' ? 'down': 'up'}" aria-hidden="true"></span></c:if> Discontinued date <span class="glyphicon glyphicon-sort-by-attributes${dashboardManager.order == 'discontinued' && dashboardManager.typeOrder != 'DESC' ? '-alt': ''}" aria-hidden="true"></span></a></th>
                <th><a href="${my:linkSort('dashboard', param, 'company_name', (dashboardManager.order == 'company_name' && dashboardManager.typeOrder != 'DESC' ? 'DESC' : null))}"><c:if test="${dashboardManager.order == 'company_name'}"><span class="glyphicon glyphicon-chevron-${dashboardManager.typeOrder == 'DESC' ? 'down': 'up'}" aria-hidden="true"></span></c:if> Company <span class="glyphicon glyphicon-sort-by-attributes${dashboardManager.order == 'company_name' && dashboardManager.typeOrder != 'DESC' ? '-alt': ''}" aria-hidden="true"></span></a></th>
            </tr>
            </thead>
            <!-- Browse attribute computers -->
            <tbody id="results">

            <c:forEach items="${dashboardManager.paginator.values}" var="computer">
                <tr>
                    <td class="editMode"><input type="checkbox" name="cb" class="cb" value="${computer.id}"></td>
                    <td>
                        <jsp:element name="a">
								<jsp:attribute name="href">
									editComputer.html?id=${computer.id}
								</jsp:attribute>
                            <jsp:body>
                                <c:out value="${computer.name}"/>
                            </jsp:body>
                        </jsp:element>
                    </td>
                    <td><c:out value="${computer.introducedDate}"/></td>
                    <td><c:out value="${computer.discontinuedDate}"/></td>
                    <td><c:out value="${computer.manufacturer.name}"/></td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>
</section>

<footer class="navbar-fixed-bottom">
    <div class="container text-center">
        <my:pagination paginator="${dashboardManager.paginator}"
                       parameters="${param}"/>

        <div class="btn-group btn-group-sm pull-right" role="group">
            <a type="button" class="btn btn-default ${param.size == 10 || empty param.size ? 'active' : ''}"
               href="${my:linkQE('dashboard', param, 'size', null)}">10</a>
            <a type="button" class="btn btn-default ${param.size == 50 ? 'active' : ''}" href="${my:linkQE('dashboard', param, 'size', 50)}">50</a>
            <a type="button" class="btn btn-default ${param.size == 100 ? 'active' : ''}" href="${my:linkQE('dashboard', param, 'size', 100)}">100</a>
        </div>
    </div>
</footer>
<script src="/speccdb/js/jquery.min.js"></script>
<script src="/speccdb/js/bootstrap.min.js"></script>
<script src="/speccdb/js/dashboard.js"></script>

</body>
</html>