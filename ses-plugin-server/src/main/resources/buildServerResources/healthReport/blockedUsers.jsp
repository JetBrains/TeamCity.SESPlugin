

<%--@elvariable id="count" type="java.lang.Integer"--%>
<%@ page import="jetbrains.buildServer.web.openapi.healthStatus.HealthStatusItemDisplayMode" %>
<%@ include file="/include-internal.jsp"
%>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags"
%>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin"
%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags/tags"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="inplaceMode" value="<%=HealthStatusItemDisplayMode.IN_PLACE%>"/>
<div id="usersWithBlockedEmailsCount" class="huge_log">
    There are <c:out value="${count}"/> users with blocked emails
</div>