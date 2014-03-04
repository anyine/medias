Ext.ns('Ext.form');

Ext.form.FilesField = Ext.extend(Ext.form.Field, {
	multiple : false,
	inputType : 'file',
	onRender : function(ct, position) {
		Ext.form.FilesField.superclass.onRender.call(this, ct, position);
		this.el.dom.multiple = this.multiple;
	},
	initEvents : function(){
		Ext.form.FilesField.superclass.initEvents.call(this);
		this.el.on("change", this.onChange,  this);
	},
	onChange : function() {
		this.fireEvent("change", this);
	}
});

Ext.reg('filesfield', Ext.form.FilesField);