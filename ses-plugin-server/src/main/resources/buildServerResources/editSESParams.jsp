<%@ include file="/include.jsp" %>

<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="util" uri="/WEB-INF/functions/util" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>

<jsp:useBean id="constants" class="jetbrains.buildServer.sesPlugin.teamcity.util.Constants"/>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<%--@elvariable id="publicKey" type="java.lang.String"--%>

<div id="editSQSParams">
    <div>
        Some sort of header
    </div>

    <form id="editSQSParamsForm">
        <table class="runnerFormTable">
            <jsp:include page="editAWSCommonParams.jsp"/>

            <l:settingsGroup title="SQS Queue Parameters">
                <tr>
                    <th><label for="${constants.ACCOUNT_ID_PARAM}">Owner Account ID: <l:star/></label></th>
                    <td>
                        <props:textProperty name="${constants.ACCOUNT_ID_PARAM}"
                                            value="${propertiesBean.properties[constants.ACCOUNT_ID_PARAM]}"
                                            className="longField" maxlength="256"/>
                        <span class="smallNote">Owner ID/span>
                    </td>
                </tr>
                <tr>
                    <th><label for="${constants.QUEUE_NAME_PARAM}">SQS Queue Name: <l:star/></label></th>
                    <td>
                        <props:textProperty name="${constants.QUEUE_NAME_PARAM}"
                                            value="${propertiesBean.properties[constants.QUEUE_NAME_PARAM]}"
                                            className="longField" maxlength="256"/>
                        <span class="smallNote">Queue Name to pull SES events from</span>
                    </td>
                </tr>
            </l:settingsGroup>
        </table>

        <input type="hidden" id="prop:projectExtId" name="prop:projectExtId" value="_Root"/>
        <input type="hidden" id="publicKey" name="publicKey" value="${publicKey}"/>
    </form>

    <forms:button id="submit">Submit</forms:button>
    <forms:button id="check">Check connection</forms:button>
</div>

<script>
    $j(BS.SESPlugin.EditSQSParams.init);
</script>