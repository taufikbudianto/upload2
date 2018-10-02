if(window['PrimeFaces'] && window['PrimeFaces'].widget.Dialog) {
    PrimeFaces.widget.Dialog = PrimeFaces.widget.Dialog.extend({

        enableModality: function() {
            this._super();
            $(document.body).children(this.jqId + '_modal').addClass('ui-dialog-mask');
            $('html, body').css('overflow', 'hidden');
        },
        disableModality: function() {
            this._super();
            $('html, body').css('overflow', 'auto');
        },

        syncWindowResize: function() {}
    });
}