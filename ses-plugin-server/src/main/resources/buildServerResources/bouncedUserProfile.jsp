<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  ~ Copyright 2000-2021 JetBrains s.r.o.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

<%--@elvariable id="disableDescription" type="java.lang.String"--%>
<%--@elvariable id="canEdit" type="java.lang.Boolean"--%>
<%--@elvariable id="isDisabled" type="java.lang.Boolean"--%>
<%--@elvariable id="userId" type="java.lang.Long"--%>
<c:if test="${isDisabled}"><span class="hidden" id="profileDisabledEmailNote"><i
        class="icon icon16 tc-icon_attention_yellow"></i><c:choose><c:when test="${not empty disableDescription}"><c:out
        value="${disableDescription}"/></c:when><c:otherwise>Email is disabled</c:otherwise></c:choose></span></c:if>
<c:if test="${canEdit}"><span class="hidden" style="padding-left: 1em" id="enableDisabledEmailBlock"><a href="#"
                                                                                                        onclick="return false;"
                                                                                                        id="enableDisabledEmailLink"
                                                                                                        data-user-id="${userId}">enable</a></span></c:if>
<script>
    $j(function () {
        $j('#profileDisabledEmailNote').insertAfter('#profilePage .input-wrapper_email').show();
        $j('#enableDisabledEmailBlock').insertAfter('#profileDisabledEmailNote').show();
        $j('#enableDisabledEmailBlock a').on('click', function () {
            var userId = $j('#enableDisabledEmailBlock a').data('user-id');
            if (userId) {
                return $j.ajax(window['base_uri'] + '/admin/enableBouncedEmail.html', {
                    data: {
                        userId: userId
                    },
                    dataType: 'json'
                }).done(function (data) {
                    if (data.status) {
                        BS.reload();
                    } else if (data.error) {
                        alert(data.error);
                    }
                });
            }

        });
    });
</script>
