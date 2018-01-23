BS.SESPlugin = BS.SESPlugin || {};

BS.SESPlugin.EditSQSParams = BS.SESPlugin.EditSQSParams || {
    disableAllInputs: function () {
        $j('#editSQSParamsTable').find('input:not(.enableDisableSESIntegration)').attr('disabled', 'disabled');
        $j('#editSQSParams').find('#check').attr('disabled', 'disabled');
        $j('#editSQSParams').find('#receive').attr('disabled', 'disabled');
    },

    enableAllInputs: function () {
        if (BS.SESPlugin.EditSQSParams.isEnabled()) {
            $j('#editSQSParamsTable').find('input:not(.enableDisableSESIntegration)').removeAttr('disabled');
            $j('#editSQSParams').find('#check').removeAttr('disabled');
            $j('#editSQSParams').find('#receive').removeAttr('disabled');
        }
    },

    isEnabled: function () {
        return $j('#editSQSParams input[id=\'aws.sesIntegration.enabled\']')[0].checked;
    },

    isAnyFieldChanged: function () {
        var a = false;
        $j('#editSQSParams input').each(function (idx, elt) {
            var $elt = $j(elt);
            if ($elt.data('initValue') !== $elt.val()) {
                a = true;
                return false;
            }
        });
        return a;
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

        function sumbit() {
            sendRequest('submit')
                .done(function (data) {
                    if (data.successful) {
                        $j('#modifiedMessage').hide();
                        resetFieldsValues();
                    }
                    $j('#successMessage')[0].innerHTML = data.description;
                    $j('#successMessage').show();
                    BS.SESPlugin.EditSQSParams.FormCrutch.setModified(false);
                })
                .fail(function (data) {
                });
        }

        function resetFieldsValues() {
            $j('#editSQSParams input').each(function (idx, elt) {
                var $elt = $j(elt);
                $elt.data('initValue', $elt.val());
            });
        }

        $j('#modifiedMessage .submitButton').on('click', function () {
            sumbit();
        });

        resetFieldsValues();

        $j('#editSQSParams').on('focusin', 'input', function (e) {
            $j(document.getElementById('error_' + $j(e.target).attr('id'))).empty();
        }).on('change', 'input', function (e) {
            $j('#successMessage').hide();

            var $elt = $j(e.target);

            var anyChanged = $elt.data('initValue') !== $elt.val() || BS.SESPlugin.EditSQSParams.isAnyFieldChanged();

            BS.SESPlugin.EditSQSParams.FormCrutch.setModified(anyChanged);
        }).on('click', '#submit', function (e) {
            sumbit();
        }).on('click', '#check', function (e) {
            if (BS.SESPlugin.EditSQSParams.isEnabled()) {
                sendRequest('check')
                    .done(function (data) {
                        if (data.errorFields && data.errorFields.length > 0) {
                            BS.TestConnectionDialog.show(false, data.description);
                            for (var i = 0; i < data.errorFields.length; ++i) {
                                BS.SESPlugin.EditSQSParams.FormCrutch.showError(data.errorFields[i], "Should not be empty");
                            }
                        } else {
                            BS.TestConnectionDialog.show(data.successful, data.description);
                        }
                    })
                    .fail(function (data) {
                    });
            }
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
                    $j('#editSQSParams #editSQSParamsTable').find('input:not(.enableDisableSESIntegration)').val('');
                    if (data.successful) {
                    }
                })
                .fail(function (data) {
                });
        }).on('click', '#receive', function () {
            if (BS.SESPlugin.EditSQSParams.isEnabled()) {
                sendRequest('receive').done(function (data) {
                    if (data.errorFields && data.errorFields.length > 0) {
                        for (var i = 0; i < data.errorFields.length; ++i) {
                            BS.SESPlugin.EditSQSParams.FormCrutch.showError(data.errorFields[i], "Should not be empty");
                        }
                    } else {
                        alert(data.description)
                        BS.reload();
                    }
                });
            }
        }).on('click', '#statusLabel', function () {
            $j('#editSQSParams #status').show();
            $j('#editSQSParams #statusLabel').hide();
        }).on('click', '#enable-btn', function () {
            $j('input[id=\'aws.sesIntegration.enabled\']').val('true');
            sendRequest('enable')
                .done(function () {
                    BS.reload();
                });
        }).on('click', '#disable-btn', function () {
            var message;

            if (BS.SESPlugin.EditSQSParams.FormCrutch.modified) {
                message = "All unsaved changes will be discarded and SES integration will be disabled. Proceed?";
            } else {
                message = "SES integration will be disabled. Proceed?";
            }
            if (!confirm(message)) {
                return;
            }

            $j('input[id=\'aws.sesIntegration.enabled\']').val('false');
            sendRequest('enable')
                .done(function () {
                    BS.reload();
                });
        });

        awsCommonParamsUpdateVisibility();

        if (!BS.SESPlugin.EditSQSParams.isEnabled()) {
            BS.SESPlugin.EditSQSParams.disableAllInputs();
        }

        BS.SESPlugin.EditSQSParams.FormCrutch.setUpdateStateHandlers({
            updateState: function () {
            },
            saveState: function () {
            }
        })
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