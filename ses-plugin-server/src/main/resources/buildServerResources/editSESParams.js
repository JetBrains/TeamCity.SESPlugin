BS.SESPlugin = BS.SESPlugin || {};

BS.SESPlugin.EditSQSParams = BS.SESPlugin.EditSQSParams || {
    disableAllInputs: function () {
        $j('#editSQSParamsTable').find('input:not(.enableDisableSESIntegration)').attr('disabled', 'disabled');
        $j('#editSQSParams').find('#check').attr('disabled', 'disabled');
        $j('#editSQSParams').find('#receive').attr('disabled', 'disabled');
    },

    enableAllInputs: function () {
        if ($j('#editSQSParamsTable .enableDisableSESIntegration')[0].checked) {
            $j('#editSQSParamsTable').find('input:not(.enableDisableSESIntegration)').removeAttr('disabled');
            $j('#editSQSParams').find('#check').removeAttr('disabled');
            $j('#editSQSParams').find('#receive').removeAttr('disabled');
        }
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
                    alert(data.description)
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
            if (!confirm("Delete all setting?")) return;

            sendRequest('delete')
                .done(function (data) {
                    $j('#editSQSParamsTable').find('input:not(.enableDisableSESIntegration)').val('');
                    if (data.successful) {
                    }
                })
                .fail(function (data) {
                });
        }).on('click', '#receive', function () {
            sendRequest('receive').done(function (data) {
                alert(data.description)
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