BS.SESPlugin = BS.SESPlugin || {};

BS.SESPlugin.EditSQSParams = BS.SESPlugin.EditSQSParams || {
    init: function() {
        $j('#editSQSParams input[id=\'aws.access.keys\']').trigger('click');

        $j('#editSQSParams').on('click', '#submit', function(e) {
            var serialized = BS.SESPlugin.EditSQSParams.FormCrutch.serializeParameters().toQueryParams();
            serialized['type'] = 'submit';
            $j.ajax(window['base_uri'] + '/IdontKnowYet.html', serialized);
        });

        $j('#editSQSParams').on('click', '#check', function(e) {
            var serialized = BS.SESPlugin.EditSQSParams.FormCrutch.serializeParameters().toQueryParams();
            serialized['type'] = 'check';
            $j.ajax(window['base_uri'] + '/IdontKnowYet.html', serialized);
        });
    }
};

BS.SESPlugin.EditSQSParams.FormCrutch = BS.SESPlugin.EditSQSParams.FormCrutch || OO.extend(BS.PluginPropertiesForm, {
    formElement: function() {
        return $j('#editSQSParamsForm')[0];
    }
});