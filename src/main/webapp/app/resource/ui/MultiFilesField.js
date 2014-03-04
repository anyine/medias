Ext.ns('Ext.form');

Ext.form.MultiFilesField = Ext.extend(Ext.BoxComponent, {
	initComponent : function() {
		Ext.form.MultiFilesField.superclass.initComponent.call(this);
		this.addEvents('change');
	},
	onRender : function(ct, position) {
		Ext.form.MultiFilesField.superclass.onRender.call(this, ct, position);
		if (!this.el) {
			var cfg = this.getAutoCreate();
			if (!cfg.name) {
				cfg.name = this.name || this.id;
			}
			this.el = ct.createChild(cfg, position);
			this.createFileInput();
		}
	},
	createFileInput : function() {
		var id = Ext.id();
		var el = this.el.createChild({
			tag : 'div',
			children : [ {
				tag : 'label',
				'for' : id,
				html : this.fieldLabel + ":"
			}, {
				tag : 'input',
				id : id,
				name : this.name,
				multiple : true,
				type : 'file'
			}, {
				tag : 'img',
				src : 'app/resource/image/icons/delete.png',
				style : "cursor:hand",
				title : '删除文件组'
			} ]
		});
		el.child('input').on("change", this.onChange, {
			p : this.el,
			c : el,
			thiz : this
		});
		el.child('img').on("click", this.onDelete, {
			p : this.el,
			c : el,
			thiz : this
		});
	},
	onDelete : function() {
		this.c.remove();
		var cc = this.thiz.el.dom.children;
		if (cc.length == 0 || cc[cc.length - 1].children[1].files.length > 0) {
			this.thiz.createFileInput();
		}
		var filelist = [];
		for ( var i = 0; i < cc.length - 1; i++) {
			filelist.push(cc[i].children[1].files);
		}
		this.thiz.fireEvent("change", filelist);
	},
	onChange : function() {
		var ch = this.c.dom.children;
		if (ch[1].files.length == 0) {
			this.c.remove();
		}
		var cc = this.thiz.el.dom.children;
		if (cc.length == 0 || cc[cc.length - 1].children[1].files.length > 0) {
			this.thiz.createFileInput();
		}
		var filelist = [];
		for ( var i = 0; i < cc.length - 1; i++) {
			filelist.push(cc[i].children[1].files);
		}
		this.thiz.fireEvent("change", filelist);
	}
});

Ext.reg('multifilesfield', Ext.form.MultiFilesField);
