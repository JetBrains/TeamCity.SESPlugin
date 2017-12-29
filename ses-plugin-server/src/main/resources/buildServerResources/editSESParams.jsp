<%--@elvariable id="disabledUsers" type="java.util.List<jetbrains.buildServer.users.SUser>"--%>
<%@ include file="/include.jsp" %>

<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="constants" class="jetbrains.buildServer.sesPlugin.teamcity.util.Constants"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<%--@elvariable id="publicKey" type="java.lang.String"--%>
<%--@elvariable id="debug" type="java.lang.Boolean"--%>

<div id="editSQSParams">
    <form id="editSQSParamsForm">
        <table class="runnerFormTable" id="editSQSParamsTable">
            <tr>
                <th><label for="${constants.ENABLED}">Enable SES Integration: </label></th>
                <td><props:checkboxProperty className="enableDisableSESIntegration" name="${constants.ENABLED}"/></td>
            </tr>

            <jsp:include page="editAWSCommonParams.jsp"/>

            <l:settingsGroup title="SQS Queue Parameters">
                <tr>
                    <th><label for="${constants.ACCOUNT_ID_PARAM}">${constants.ACCOUNT_ID_LABEL}: <l:star/></label></th>
                    <td>
                        <props:textProperty name="${constants.ACCOUNT_ID_PARAM}"
                                            value="${propertiesBean.properties[constants.ACCOUNT_ID_PARAM]}"
                                            className="longField" maxlength="256"/>
                        <span class="smallNote">Owner ID</span>
                        <span class="error" id="error_${constants.ACCOUNT_ID_PARAM}"></span>
                    </td>
                </tr>
                <tr>
                    <th><label for="${constants.QUEUE_NAME_PARAM}">${constants.QUEUE_NAME_LABEL}: <l:star/></label></th>
                    <td>
                        <props:textProperty name="${constants.QUEUE_NAME_PARAM}"
                                            value="${propertiesBean.properties[constants.QUEUE_NAME_PARAM]}"
                                            className="longField" maxlength="256"/>
                        <span class="smallNote">Queue Name to pull SES events from</span>
                        <span class="error" id="error_${constants.QUEUE_NAME_PARAM}"></span>
                    </td>
                </tr>
            </l:settingsGroup>
        </table>

        <input type="hidden" id="prop:projectExtId" name="prop:projectExtId" value="_Root"/>
        <input type="hidden" id="publicKey" name="publicKey" value="${publicKey}"/>
    </form>

    <forms:submit label="Save" id="submit"/>
    <forms:button className="hidden" id="delete">Delete integration...</forms:button>
    <forms:button id="check">Test connection</forms:button>
    <c:if test="${debug}"><c:set value="hidden" var="hidden"/></c:if>
    <forms:button className="${hidden}" id="receive">Receive messages now</forms:button>
    <span style="display: none" class="spinner"><i class="icon-refresh icon-spin"></i></span>

    <div class="statusBlock ${hidden}">
        <a onclick="return false;" href="#" id="statusLabel">${fn:length(disabledUsers)} emails are disabled due to
            bounces</a>
        <div id="status" class="hidden">
            <div>
                <label for="disabledEmailsCount">Users with disabled emails count: </label><span
                    id="disabledEmailsCount">${fn:length(disabledUsers)}</span>
                <c:forEach items="${disabledUsers}" var="user">
                    <div class="disabledUser"><span class="userName">User '<c:out value="${user.extendedName}"/>'</span>
                        got
                        bounce on address '<span class="email"><c:out value="${user.email}"/></span>'
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>

    <forms:modified/>

    <script>
        $j(BS.SESPlugin.EditSQSParams.init);
    </script>
</div>