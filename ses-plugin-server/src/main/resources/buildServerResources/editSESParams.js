BS.SESPlugin = BS.SESPlugin || {};

BS.SESPlugin.EditSQSParams = BS.SESPlugin.EditSQSParams || {
    init: function() {
        $j('#editSQSParams input[id=\'aws.access.keys\']').trigger('click');

        $j('#editSQSParams').on('click', "#submit", function(e) {

        });

        $j('#editSQSParams').on('click', "#check", function(e) {

        });
    }
};