BS.SESPlugin = BS.SESPlugin || {};

BS.SESPlugin.EditSQSParams = BS.SESPlugin.EditSQSParams || {
    disableAllInputs: function () {
        $j('#editSQSParamsTable').find('input:not(.enableDisableSESIntegration)').attr('disabled', 'disabled');
    },

    enableAllInputs: function () {
        $j('#editSQSParamsTable').find('input:not(.enableDisableSESIntegration)').removeAttr('disabled');
    },

    init: function () {
        function sendRequest(type) {
            var serialized = BS.SESPlugin.EditSQSParams.FormCrutch.serializeParameters().toQueryParams();
            serialized['type'] = type;

            BS.SESPlugin.EditSQSParams.disableAllInputs();
            $j('#editSQSParams .spinner').show();

            return $j.ajax(window['base_uri'] + '/admin/editSQSParams.html', {
                data: serialized,
                dataType: 'json'
            }).always(function () {
                BS.SESPlugin.EditSQSParams.enableAllInputs();
                $j('#editSQSParams .spinner').hide();
            });
        }

        $j('#editSQSParams').on('click', '#submit', function (e) {
            sendRequest('submit')
                .done(function (data) {
                })
                .fail(function (data) {
                });
        }).on('click', '#check', function (e) {
            sendRequest('check')
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
        }).on('click', '#delete', function () {
            sendRequest('delete')
                .done(function (data) {
                    if (data.successful) {
                    }
                })
                .fail(function (data) {
                });
        });

        awsCommonParamsUpdateVisibility();
        if (!$j('.enableDisableSESIntegration').attr('checked')) {
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