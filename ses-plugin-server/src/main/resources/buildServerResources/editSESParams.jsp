<%@ include file="/include.jsp" %>

<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<table class="runnerFormTable">
    <jsp:include page="editAWSCommonParams.jsp"/>


    <tr>
        <th><label for=""></label></th>
        <td></td>
    </tr>
</table>

<script>
    $j(function () {
//        $j('aws.access.keys').click();
    });
</script>