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
        <div class="successMessage hidden" id="successMessage"></div>

        <div id="enableDisable">
            <props:checkboxProperty className="hidden enableDisableSESIntegration" name="${constants.ENABLED}"/>
            <c:choose>
                <c:when test="${propertiesBean.properties[constants.ENABLED] != 'true'}">
                    <div class="headerNote">
                        <span class="icon icon16 build-status-icon build-status-icon_paused"></span>SES integration is
                        <strong>disabled</strong>. &nbsp;&nbsp;<a class="btn btn_mini" href="#"
                                                                  id="enable-btn">Enable</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="enableNote">
                        SES integration is <strong>enabled</strong>&nbsp;&nbsp;<a showdiscardchangesmessage="false"
                                                                                  class="btn btn_mini" href="#"
                                                                                  id="disable-btn"
                                                                                  onclick="return false;">Disable</a>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <table class="runnerFormTable" id="editSQSParamsTable">
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
    <forms:button showdiscardchangesmessage="false" className="hidden" id="delete">Delete integration...</forms:button>
    <forms:button showdiscardchangesmessage="false" className="btn_primary submitButton"
                  id="check">Test connection</forms:button>
    <c:if test="${debug}"><c:set value="hidden" var="hidden"/></c:if>
    <forms:button showdiscardchangesmessage="false" className="${hidden}"
                  id="receive">Receive messages now</forms:button>
    <span style="display: none" class="spinner"><i class="icon-refresh icon-spin"></i></span>

    <div class="statusBlock ${hidden}">
        <a showdiscardchangesmessage="false" onclick="return false;" href="#"
           id="statusLabel">${fn:length(disabledUsers)} <bs:plural txt="email" val="${fn:length(disabledUsers)}"/>
            <c:choose><c:when
                    test="${fn:length(disabledUsers) > 1}">are</c:when><c:otherwise>is</c:otherwise></c:choose> disabled
            due to
            bounces</a>
        <div id="status" class="hidden">
            <div>
                <label for="disabledEmailsCount">Users with disabled emails count: </label><span
                    id="disabledEmailsCount">${fn:length(disabledUsers)}</span>
                <table class="highlightable dark userList borderBottom">
                    <tr>
                        <th>User</th>
                        <th>Email address</th>
                    </tr>
                    <c:forEach items="${disabledUsers}" var="user">
                        <tr>
                            <td class="user"><bs:userLink user="${user}"><c:out
                                    value="${user.descriptiveName}"/></bs:userLink></td>
                            <td class="email"><c:out value="${user.email}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>

    <forms:modified/>
    <bs:dialog dialogId="testConnectionDialog" title="Test Connection" closeCommand="BS.TestConnectionDialog.close();"
               closeAttrs="showdiscardchangesmessage='false'">
        <div id="testConnectionStatus"></div>
        <div id="testConnectionDetails" class="mono"></div>
    </bs:dialog>

    <script>
        $j(BS.SESPlugin.EditSQSParams.init);
    </script>
</div>