BS.SESPlugin = BS.SESPlugin || {};

BS.SESPlugin.EditSQSParams = BS.SESPlugin.EditSQSParams || {
    disableAllInputs: function () {
        $j('#editSQSParamsTable').find('input[class!=\'enableDisableSESIntegration\']').attr('disabled', 'disabled');
    },

    enableAllInputs: function () {
        $j('#editSQSParamsTable').find('input[class!=\'enableDisableSESIntegration\']').removeAttr('disabled');
    },

    init: function () {
        $j('#editSQSParams').on('click', '#submit', function (e) {
            var serialized = BS.SESPlugin.EditSQSParams.FormCrutch.serializeParameters().toQueryParams();
            serialized['type'] = 'submit';
            $j.ajax(window['base_uri'] + '/admin/editSQSParams.html',
                {
                    data: serialized,
                    dataType: 'json'
                })
                .done(function (data) {
                })
                .fail(function (data) {
                });
        }).on('click', '#check', function (e) {
            var serialized = BS.SESPlugin.EditSQSParams.FormCrutch.serializeParameters().toQueryParams();
            serialized['type'] = 'check';
            $j.ajax(window['base_uri'] + '/admin/editSQSParams.html',
                {
                    data: serialized,
                    dataType: 'json'
                })
                .done(function (data) {
                    if (data.successful) {
                        alert("Successfully connected to Amazon server")
                    } else {
                        alert(data.description)
                    }
                })
                .fail(function (data) {
                });
        }).on('click', '.enableDisableSESIntegration', function (e) {
            if (!e.target.checked) {
                BS.SESPlugin.EditSQSParams.disableAllInputs();
            } else {
                BS.SESPlugin.EditSQSParams.enableAllInputs();
            }
        });

        awsCommonParamsUpdateVisibility();
        if (!$j('.enableDisableSESIntegration').checked()) {
            BS.SESPlugin.EditSQSParams.disableAllInputs();
        }
    }
};


// noinspection JSUnusedGlobalSymbols
/**
 * A crutch to reuse PluginPropertiesForm.serializeParameters()
 */
BS.SESPlugin.EditSQSParams.FormCrutch = BS.SESPlugin.EditSQSParams.FormCrutch || OO.extend(BS.PluginPropertiesForm, {
    formElement: function () {
        return $j('#editSQSParamsForm')[0];
    }
});