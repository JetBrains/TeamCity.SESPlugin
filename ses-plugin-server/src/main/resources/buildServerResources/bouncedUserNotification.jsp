<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%--@elvariable id="contactInfo" type="java.lang.String"--%>
<script>
    $j(function () {
        $j('#globalHealthItems').append('<div class=\'icon_before icon16 attentionComment clearfix\'><div class=\'global-health-item__content\'>Your email was disabled. <c:out value="${contactInfo}"/>. </div></div>');
    });
</script>