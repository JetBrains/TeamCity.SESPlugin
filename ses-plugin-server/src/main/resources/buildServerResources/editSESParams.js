BS.SESPlugin = BS.SESPlugin || {};

BS.SESPlugin.EditSQSParams = BS.SESPlugin.EditSQSParams || {
    init: function () {
        $j('#editSQSParams input[id=\'aws.access.keys\']').trigger('click');

        $j('#editSQSParams').on('click', '#submit', function (e) {
            var serialized = BS.SESPlugin.EditSQSParams.FormCrutch.serializeParameters().toQueryParams();
            serialized['type'] = 'submit';
            $j.ajax(window['base_uri'] + '/admin/editSQSParams.html',
                {
                    data: {
                        json: serialized
                    },
                    dataType: 'json'
                })
                .done(function (data) {
                    alert(data);
                })
                .fail(function (data) {
                    alert(data);
                });
        });

        $j('#editSQSParams').on('click', '#check', function (e) {
            var serialized = BS.SESPlugin.EditSQSParams.FormCrutch.serializeParameters().toQueryParams();
            serialized['type'] = 'check';
            $j.ajax(window['base_uri'] + '/admin/editSQSParams.html',
                {
                    data: {
                        json: serialized
                    },
                    dataType: 'json'
                })
                .done(function (data) {
                    alert(data);
                })
                .fail(function (data) {
                    alert(data);
                });
        });
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